package Frontend;

import AST.ASTVisitor;
import AST.ProgramNode;
import AST.def.*;
import AST.expr.*;
import AST.expr.atom.*;
import AST.stmt.*;
import AST.typeNode;
import Util.*;
import Util.error.semanticError;

public class SemanticChecker implements ASTVisitor {
    Scope gScope;
    Scope currentScope;
    Type currentType;//存放刚visit的表达式的type

    public SemanticChecker(Scope gScope) {
        this.gScope = gScope;
    }

    @Override
    public void visit(ProgramNode it) {
        currentScope = gScope;
        for (var node : it.Defs) {
            assert node instanceof classDefNode || node instanceof varDefNode || node instanceof funDefNode;
            if (node instanceof classDefNode cdn) {
                currentScope = new Scope(currentScope, Scope.scopeType.CLASS, cdn.name);
                visit(cdn);
                currentScope = currentScope.parent();
            }
            if (node instanceof funDefNode fdn) {
                currentScope = new Scope(currentScope, Scope.scopeType.FUNCTION, fdn.name);
                visit(fdn);
                currentScope = currentScope.parent();
            }
            if (node instanceof varDefNode vdn) {
                visit(vdn);
            }
        }
    }

    @Override
    public void visit(classBuildNode it) {
        if (currentScope.getType() != Scope.scopeType.CLASS) {
            throw new semanticError("classBuildNode in wrong scope", it.pos);
        }
        if (!it.name.equals(currentScope.name)) {
            throw new semanticError("classBuildNode name not match", it.pos);
        }
        currentScope = new Scope(currentScope, Scope.scopeType.FUNCTION, it.name);
        for (var node : it.stmts) {
            boolean isBlock = false, isNotBlock = false;
            if (node instanceof blockStmtNode) {
                if (isBlock || isNotBlock) {
                    throw new semanticError("classBuildNode contains illegal stmt 0", it.pos);
                }
                isBlock = true;
                isNotBlock = false;
            } else {
                if (isBlock) {
                    throw new semanticError("classBuildNode contains illegal stmt 1", it.pos);
                }
                isBlock = false;
                isNotBlock = true;
            }
            switch (node) {
                case blockStmtNode bsn -> visit(bsn);
                case varDefNode vdn -> visit(vdn);
                case breakStmtNode bsn -> visit(bsn);
                case continueStmtNode csn -> visit(csn);
                case emptyStmtNode esn -> visit(esn);
                case exprStmtNode esn -> visit(esn);
                case forStmtNode fsn -> {
                    currentScope = new Scope(currentScope, Scope.scopeType.FOR, "for");
                    visit(fsn);
                    currentScope = currentScope.parent();
                }
                case ifStmtNode isn -> {
                    currentScope = new Scope(currentScope, Scope.scopeType.IF, "if");
                    visit(isn);
                    currentScope = currentScope.parent();
                }
                case returnStmtNode rsn -> visit(rsn);
                case whileStmtNode wsn -> {
                    currentScope = new Scope(currentScope, Scope.scopeType.WHILE, "while");
                    visit(wsn);
                    currentScope = currentScope.parent();
                }
                default -> throw new semanticError("classBuildNode contains illegal stmt", it.pos);
            }
        }
        currentScope = currentScope.parent();
    }

    @Override
    public void visit(classDefNode it) {
        if (currentScope.getType() != Scope.scopeType.GLOBAL) {
            throw new semanticError("classDefNode in wrong scope", it.pos);
        }
//        if (currentScope.containsVar(it.name, false)) {
//            throw new semanticError("classDefNode name conflict", it.pos);
//        }
//        currentScope.addClass(it.name, new Class_(it.name));
        visit(it.build.getFirst());
        for (var node : it.funDefs) {
            currentScope = new Scope(currentScope, Scope.scopeType.FUNCTION, node.name);
            visit(node);
            currentScope = currentScope.parent();
        }
        for (var node : it.varDefs) {
            visit(node);
        }
    }

    @Override
    public void visit(funDefNode it) {

        assert false;
        //TODO:funDefNode 的 typeinfo

        assert currentScope.getType() == Scope.scopeType.FUNCTION;
        assert it.name.equals(currentScope.name);
        visit(it.paraList);
        for (var node : it.stmts) {
            boolean isBlock = false, isNotBlock = false;
            if (node instanceof blockStmtNode) {
                if (isBlock || isNotBlock) {
                    throw new semanticError("classBuildNode contains illegal stmt 0", it.pos);
                }
                isBlock = true;
                isNotBlock = false;
            } else {
                if (isBlock) {
                    throw new semanticError("classBuildNode contains illegal stmt 1", it.pos);
                }
                isBlock = false;
                isNotBlock = true;
            }
            switch (node) {
                case blockStmtNode bsn -> visit(bsn);
                case varDefNode vdn -> visit(vdn);
                case breakStmtNode bsn -> visit(bsn);
                case continueStmtNode csn -> visit(csn);
                case emptyStmtNode esn -> visit(esn);
                case exprStmtNode esn -> visit(esn);
                case forStmtNode fsn -> {
                    currentScope = new Scope(currentScope, Scope.scopeType.FOR, "for");
                    visit(fsn);
                    currentScope = currentScope.parent();
                }
                case ifStmtNode isn -> {
                    currentScope = new Scope(currentScope, Scope.scopeType.IF, "if");
                    visit(isn);
                    currentScope = currentScope.parent();
                }
                case returnStmtNode rsn -> visit(rsn);
                case whileStmtNode wsn -> {
                    currentScope = new Scope(currentScope, Scope.scopeType.WHILE, "while");
                    visit(wsn);
                    currentScope = currentScope.parent();
                }
                default -> throw new semanticError("classBuildNode contains illegal stmt", it.pos);
            }
        }
        if (!(it.typeNd.type.atomType == Type.TypeEnum.VOID && currentScope.funcReturnType == null) &&
                !it.typeNd.type.equals(currentScope.funcReturnType)) {
            throw new semanticError("funDefNode return type not match", it.pos);
        }
    }

    private void visit(ExprNode it) {
        switch (it) {
            case arrayExprNode aen -> visit(aen);
            case assignExprNode aen -> visit(aen);
            case binaryExprNode ben -> visit(ben);
            case conditionalExprNode cen -> visit(cen);
            case formatStringExprNode fsen -> visit(fsen);
            case funExprNode fen -> visit(fen);
            case memberExprNode node -> visit(node);
            case newArrayExprNode naen -> visit(naen);
            case newVarExprNode nven -> visit(nven);
            case preSelfExprNode psen -> visit(psen);
            case unaryExprNode uen -> visit(uen);
            case atomExprNode aen -> visit(aen);
            default -> throw new semanticError("visit ExprNode", it.pos);
        }
    }

    @Override
    public void visit(varDefNode it) {
        for (var unit : it.units) {
            currentScope.addVar(unit.name, it.typeNd.type, unit.pos);
            if (unit.init != null) {
                //在visit更新currentType
                visit(unit.init);
                if (!currentType.equals(currentScope.getVarType(unit.name, true))) {
                    throw new semanticError("varDefNode init type not match", it.pos);
                }
            }
        }
    }

    @Override
    public void visit(funParaList it) {
        for (var node : it.paraList) {
            currentScope.addVar(node.units.getFirst().name, node.typeNd.type, node.pos);
        }
    }

    @Override
    public void visit(varDefUnitNode it) {
        assert false;
    }

    @Override
    public void visit(arrayNode it) {
        for (var node : it.value) {
            visit(node);
            Type tmp = it.typeNd.type.clone();
            tmp.dim--;
            if (!currentType.equals(tmp)) {
                throw new semanticError("arrayNode type not match", it.pos);
            }
        }
        it.isLeftValue = false;
        currentType = it.typeNd.type;
    }

    @Override
    public void visit(atomExprNode it) {
        switch (it) {
            case boolNode bn -> visit(bn);
            case identifierNode in -> visit(in);
            case intNode in -> visit(in);
            case nullNode nn -> visit(nn);
            case stringNode sn -> visit(sn);
            case thisNode tn -> visit(tn);
            default -> throw new semanticError("visit atomExprNode", it.pos);
        }
    }

    @Override
    public void visit(boolNode it) {
        it.isLeftValue = false;
        currentType = it.typeNd.type;
    }

    @Override
    public void visit(identifierNode it) {
        it.isLeftValue = true;
        if (currentScope.containsVar(it.name, true)) {
            it.typeNd.type = currentScope.getVarType(it.name, true).clone();
        } else {
            assert it.typeNd.type == null;
            it.typeNd.type = new Type();
            it.typeNd.type.isFun = true;
            System.err.println("WARNING:Assume identifierNode is function");
        }
        currentType = it.typeNd.type;
    }

    @Override
    public void visit(intNode it) {
        it.isLeftValue = false;
        currentType = it.typeNd.type;
    }

    @Override
    public void visit(nullNode it) {
        it.isLeftValue = false;
        currentType = it.typeNd.type;
    }

    @Override
    public void visit(stringNode it) {
        it.isLeftValue = false;
        currentType = it.typeNd.type;
    }

    @Override
    public void visit(thisNode it) {
        it.isLeftValue = false;
        currentType = it.typeNd.type;
    }

    @Override
    public void visit(arrayExprNode it) {
        visit(it.array);
        if (!currentType.isArray) {
            throw new semanticError("arrayExprNode array type not match 01", it.pos);
        }
        if (it.array instanceof newArrayExprNode) {
            throw new semanticError("array can't be new", it.pos);
        }
        visit(it.index);
        if (!currentType.equals(new Type(Type.TypeEnum.INT))) {
            throw new semanticError("arrayExprNode index type not match 02", it.pos);
        }
        it.isLeftValue = true;
        currentType = it.array.typeNd.type.clone();
        currentType.dim--;
        if (currentType.dim == 0) {
            currentType.isArray = false;
        }
    }

    @Override
    public void visit(assignExprNode it) {
        visit(it.lhs);
        if (!it.lhs.isLeftValue) {
            throw new semanticError("assignExprNode lhs not left value", it.pos);
        }
        visit(it.rhs);
        if (!currentType.equals(it.lhs.typeNd.type)) {
            throw new semanticError("assignExprNode type not match", it.pos);
        }
        it.isLeftValue = false;
        currentType = it.lhs.typeNd.type;
    }

    @Override
    public void visit(binaryExprNode it) {
        visit(it.lhs);
        assert currentType.equals(it.lhs.typeNd.type);
        visit(it.rhs);
        assert currentType.equals(it.rhs.typeNd.type);
        if (!it.rhs.typeNd.type.equals(it.lhs.typeNd.type)) {
            throw new semanticError("binaryExprNode type not match: different type", it.pos);
        }
        if (it.lhs.typeNd.type.equals(new Type(Type.TypeEnum.INT))) {
        } else if (it.lhs.typeNd.type.equals(new Type(Type.TypeEnum.STRING))) {
            if (it.opCode.equals(binaryExprNode.binaryOpType.add) ||
                    it.opCode.equals(binaryExprNode.binaryOpType.eq) ||
                    it.opCode.equals(binaryExprNode.binaryOpType.unEq) ||
                    it.opCode.equals(binaryExprNode.binaryOpType.less) ||
                    it.opCode.equals(binaryExprNode.binaryOpType.lessEq) ||
                    it.opCode.equals(binaryExprNode.binaryOpType.greater) ||
                    it.opCode.equals(binaryExprNode.binaryOpType.grEq)) {
            } else {
                throw new semanticError("binaryExprNode type not match: string invalid", it.pos);
            }
        } else if (it.lhs.typeNd.type.equals(new Type(Type.TypeEnum.BOOL))) {
            if (it.opCode.equals(binaryExprNode.binaryOpType.eq) ||
                    it.opCode.equals(binaryExprNode.binaryOpType.unEq) ||
                    it.opCode.equals(binaryExprNode.binaryOpType.andLg) ||
                    it.opCode.equals(binaryExprNode.binaryOpType.orLg)) {
            } else {
                throw new semanticError("binaryExprNode type not match: bool invalid", it.pos);
            }
        } else {
            throw new semanticError("binaryExprNode type not match: type invalid", it.pos);
        }
        it.isLeftValue = false;
        currentType = it.lhs.typeNd.type;
    }

    @Override
    public void visit(conditionalExprNode it) {
        visit(it.condition);
        if (!currentType.equals(new Type(Type.TypeEnum.BOOL))) {
            throw new semanticError("conditionalExprNode cond type not match", it.pos);
        }
        visit(it.trueExpr);
        assert currentType.equals(it.trueExpr.typeNd.type);
        visit(it.falseExpr);
        assert currentType.equals(it.falseExpr.typeNd.type);
        if (!it.falseExpr.typeNd.type.equals(it.trueExpr.typeNd.type)) {
            throw new semanticError("conditionalExprNode TF type not match", it.pos);
        }
        it.isLeftValue = false;
        currentType = it.trueExpr.typeNd.type;
    }

    @Override
    public void visit(formatStringExprNode it) {
        for (var node : it.exprs) {
            visit(node);
            if (!currentType.equals(new Type(Type.TypeEnum.INT)) &&
                    !currentType.equals(new Type(Type.TypeEnum.STRING)) &&
                    !currentType.equals(new Type(Type.TypeEnum.BOOL))) {
                throw new semanticError("formatStringExprNode type not match", it.pos);
            }
        }
        it.isLeftValue = false;
        currentType = it.typeNd.type;
    }

    @Override
    public void visit(funExprNode it) {
        visit(it.fun);
        if (!currentType.isFun) {
            throw new semanticError("funExprNode fun type not match", it.pos);
        }
        Function fun;
        if (it.fun.typeNd.type.fun.inGlobal) {
            fun = gScope.getFunFromName(it.fun.typeNd.type.fun.funName, it.pos);
        } else {
            Class_ class_ = gScope.getClassFromName(it.fun.typeNd.type.fun.className, it.pos);
            fun = class_.getFunFromName(it.fun.typeNd.type.fun.funName, it.pos);
        }
        for (var node : it.paras) {
            visit(node);
            assert currentType.equals(node.typeNd.type);
        }
        if (fun.paramTypes.size() != it.paras.size()) {
            throw new semanticError("funExprNode para size not match", it.pos);
        }
        for (int i = 0; i < fun.paramTypes.size(); i++) {
            if (!fun.paramTypes.get(i).equals(it.paras.get(i).typeNd.type)) {
                throw new semanticError("funExprNode para type not match", it.pos);
            }
        }
        it.isLeftValue = false;
        currentType = fun.returnType;
    }

    @Override
    public void visit(memberExprNode it) {
        visit(it.object);
        assert currentType.equals(it.object.typeNd.type);
        if (!it.object.typeNd.type.atomType.equals(Type.TypeEnum.CLASS)) {
            throw new semanticError("memberExprNode object is not a class", it.pos);
        }
        Class_ class_ = gScope.getClassFromName(it.object.typeNd.type.name, it.pos);
        if (class_.containsVar(it.member.name)) {
            it.typeNd.type = class_.getTypeFromVarName(it.member.name, it.pos).clone();
            it.isLeftValue = true;
            currentType = it.typeNd.type;
            return;
        } else if (class_.containsFun(it.member.name)) {
            Function f = class_.getFunFromName(it.member.name, it.pos);
            it.typeNd.type = f.returnType.clone();
            it.typeNd.type.fun = new Type.funInfo(f.name, class_.name);
            it.isLeftValue = false;
            currentType = it.typeNd.type;
            return;
        }
//        it.typeNd.type = class_.getVarType(it.member, true).clone();
//        it.isLeftValue = true;
//        currentType = it.typeNd.type;
        throw new semanticError("memberExprNode member not found", it.pos);
    }

    @Override
    public void visit(newArrayExprNode it) {
        for (var node : it.sizeInit) {
            visit(node);
            assert currentType.equals(node.typeNd.type);
            if (!currentType.equals(new Type(Type.TypeEnum.INT))) {
                throw new semanticError("newArrayExprNode size type should be int", it.pos);
            }
        }
        it.isLeftValue = false;
        currentType = it.typeNd.type;
    }

    @Override
    public void visit(newVarExprNode it) {
        visit(it.init);
        assert currentType.equals(it.init.typeNd.type);
        if (!it.init.typeNd.type.equals(it.typeNd.type)) {
            throw new semanticError("newVarExprNode type not match", it.pos);
        }
        it.isLeftValue = false;
        currentType = it.typeNd.type;
    }

    @Override
    public void visit(preSelfExprNode it) {
        visit(it.body);
        if (!it.body.isLeftValue) {
            throw new semanticError("preSelfExprNode body not left value", it.pos);
        }
        if (!it.body.typeNd.type.equals(new Type(Type.TypeEnum.INT))) {
            throw new semanticError("preSelfExprNode body type not int", it.pos);
        }
        it.isLeftValue = false;
        it.typeNd.type = new Type(Type.TypeEnum.INT);
        currentType = it.typeNd.type;
    }

    @Override
    public void visit(unaryExprNode it) {
        visit(it.body);
        if (it.opCode.equals(unaryExprNode.unaryOpType.logNot)) {
            if (!it.body.typeNd.type.equals(new Type(Type.TypeEnum.BOOL))) {
                throw new semanticError("logNot unaryExprNode not type not bool", it.pos);
            }
            it.isLeftValue = false;
            it.typeNd.type = new Type(Type.TypeEnum.BOOL);
        } else {
            if (!it.body.typeNd.type.equals(new Type(Type.TypeEnum.INT))) {
                throw new semanticError("unaryExprNode not type not int", it.pos);
            }
            it.isLeftValue = false;
            it.typeNd.type = new Type(Type.TypeEnum.INT);
        }
        currentType = it.typeNd.type;
    }

    @Override
    public void visit(blockStmtNode it) {
        for (var node : it.stmts) {
            switch (node) {
                case blockStmtNode bsn -> throw new semanticError("blockStmtNode contains blockStmtNode", it.pos);
                case varDefNode vdn -> visit(vdn);
                case breakStmtNode bsn -> visit(bsn);
                case continueStmtNode csn -> visit(csn);
                case emptyStmtNode esn -> visit(esn);
                case exprStmtNode esn -> visit(esn);
                case forStmtNode fsn -> {
                    currentScope = new Scope(currentScope, Scope.scopeType.FOR, "for");
                    visit(fsn);
                    currentScope = currentScope.parent();
                }
                case ifStmtNode isn -> {
                    currentScope = new Scope(currentScope, Scope.scopeType.IF, "if");
                    visit(isn);
                    currentScope = currentScope.parent();
                }
                case returnStmtNode rsn -> visit(rsn);
                case whileStmtNode wsn -> {
                    currentScope = new Scope(currentScope, Scope.scopeType.WHILE, "while");
                    visit(wsn);
                    currentScope = currentScope.parent();
                }
                default -> throw new semanticError("blockStmtNode contains illegal stmt", it.pos);
            }
        }
    }


    private void brcocheck(position pos) {
        Scope ret = currentScope;
        Scope.scopeType tmp = currentScope.getType();
        while (tmp != Scope.scopeType.FOR && tmp != Scope.scopeType.WHILE) {
            if (tmp == Scope.scopeType.GLOBAL || tmp == Scope.scopeType.CLASS || tmp == Scope.scopeType.FUNCTION) {
                throw new semanticError("break/continue not in loop", pos);
            }
            currentScope = currentScope.parent();
            tmp = currentScope.getType();
        }
        currentScope = ret;
    }

    @Override
    public void visit(breakStmtNode it) {
        brcocheck(it.pos);
    }

    @Override
    public void visit(continueStmtNode it) {
        brcocheck(it.pos);
    }

    @Override
    public void visit(exprStmtNode it) {
        visit(it.expr);
    }

    private void visit(StmtNode it) {
        switch (it) {
            case blockStmtNode bsn -> visit(bsn);
            case breakStmtNode bsn -> visit(bsn);
            case continueStmtNode csn -> visit(csn);
            case emptyStmtNode esn -> visit(esn);
            case exprStmtNode esn -> visit(esn);
            case forStmtNode fsn -> visit(fsn);
            case ifStmtNode isn -> visit(isn);
            case returnStmtNode rsn -> visit(rsn);
            case whileStmtNode wsn -> visit(wsn);
            default -> throw new semanticError("visit StmtNode", it.pos);
        }
    }

    @Override
    public void visit(forStmtNode it) {
        currentScope = new Scope(currentScope, Scope.scopeType.FOR, "for");
        if (it.init != null) {
            visit(it.init);
        }
        if (it.cond != null) {
            visit(it.cond);
            if (!currentType.equals(new Type(Type.TypeEnum.BOOL))) {
                throw new semanticError("forStmtNode cond type not bool", it.pos);
            }
        }
        if (it.step != null) {
            visit(it.step);
        }
        visit(it.body);
    }

    @Override
    public void visit(ifStmtNode it) {
        visit(it.condition);
        if (!currentType.equals(new Type(Type.TypeEnum.BOOL))) {
            throw new semanticError("ifStmtNode cond type not bool", it.pos);
        }
        visit(it.thenStmt);
        if (it.elseStmt != null) {
            visit(it.elseStmt);
        }
    }

    @Override
    public void visit(returnStmtNode it) {
        Type returnType;

        Scope ret = currentScope;
        Scope.scopeType tmp = currentScope.getType();
        while (tmp != Scope.scopeType.FUNCTION) {
            if (tmp == Scope.scopeType.GLOBAL || tmp == Scope.scopeType.CLASS) {
                throw new semanticError("break/continue not in loop", it.pos);
            }
            currentScope = currentScope.parent();
            tmp = currentScope.getType();
        }
        returnType = currentScope.funcReturnType.clone();
        currentScope = ret;


        if (it.value != null) {
            visit(it.value);
            if (!currentType.equals(returnType)) {
                throw new semanticError("returnStmtNode type not match", it.pos);
            }
        } else {
            if (!returnType.equals(new Type(Type.TypeEnum.VOID))) {
                throw new semanticError("returnStmtNode type not match", it.pos);
            }
        }
    }

    @Override
    public void visit(whileStmtNode it) {
        visit(it.condition);
        if (!currentType.equals(new Type(Type.TypeEnum.BOOL))) {
            throw new semanticError("whileStmtNode cond type not bool", it.pos);
        }
        currentScope = new Scope(currentScope, Scope.scopeType.WHILE, "while");
        visit(it.body);
    }

    @Override
    public void visit(typeNode it) {
        assert false;
    }

    @Override
    public void visit(emptyStmtNode it) {
    }
}
