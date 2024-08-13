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
                currentScope.funcReturnType = fdn.typeNd.type.clone();
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
                case blockStmtNode bsn -> {
                    currentScope = new Scope(currentScope, Scope.scopeType.BLOCK, "block");
                    visit(bsn);
                    currentScope = currentScope.parent();
                }
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
        if (currentScope.parent().getType() != Scope.scopeType.GLOBAL) {
            throw new semanticError("classDefNode in wrong scope", it.pos);
        }
        //不能重名
        if (gScope.containsFun(it.name)) {
            throw new semanticError("classDefNode name conflict", it.pos);
        }
//        if (currentScope.containsVar(it.name, false)) {
//            throw new semanticError("classDefNode name conflict", it.pos);
//        }
//        currentScope.addClass(it.name, new Class_(it.name));
        for (var node : it.varDefs) {
            visit(node);
        }
        for (var node : it.funDefs) {
            currentScope = new Scope(currentScope, Scope.scopeType.FUNCTION, node.name);
            currentScope.funcReturnType = node.typeNd.type.clone();
            visit(node);
            currentScope = currentScope.parent();
        }
        if (!it.build.isEmpty()) visit(it.build.getFirst());
    }

    boolean inFunScope() {
        Scope ret = currentScope;
        while (currentScope.getType() != Scope.scopeType.FUNCTION) {
            if (currentScope.getType() == Scope.scopeType.GLOBAL || currentScope.getType() == Scope.scopeType.CLASS || currentScope.getType() == null) {
                currentScope = ret;
                return false;
            }
            currentScope = currentScope.parent();
        }
        currentScope = ret;
        return true;
    }

    @Override
    public void visit(funDefNode it) {
        assert currentScope.funcReturnType != null;
        //不能重名
        if (currentScope==gScope&&gScope.containsClass(it.name)) {
            throw new semanticError("funDefNode name conflict0", it.pos);
        }
        //不能重名
        if (currentScope==gScope&&gScope.containsVar(it.name, false)) {
            throw new semanticError("funDefNode name conflict1", it.pos);
        }
//        assert false;
        //TODO:funDefNode 的 typeinfo
        it.typeNd.type.fun.funName = it.name;
        it.typeNd.type.fun.className = currentScope.name;
        if (currentScope == gScope) {
            it.typeNd.type.fun.inGlobal = true;
        }
        assert currentScope.getType() == Scope.scopeType.FUNCTION;
        assert it.name.equals(currentScope.name);
        if (it.paraList != null) visit(it.paraList);
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
                case blockStmtNode bsn -> {
                    currentScope = new Scope(currentScope, Scope.scopeType.BLOCK, "block");
                    visit(bsn);
                    currentScope = currentScope.parent();
                }
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
                case returnStmtNode rsn -> {
                    visit(rsn);
//                    if (!(it.typeNd.type.atomType == Type.TypeEnum.VOID && currentScope.funcReturnType == null) &&
//                            !it.typeNd.type.equals(currentScope.funcReturnType)) {
//                        throw new semanticError("funDefNode return type not match", it.pos);
//                    }
                }
                case whileStmtNode wsn -> {
                    currentScope = new Scope(currentScope, Scope.scopeType.WHILE, "while");
                    visit(wsn);
                    currentScope = currentScope.parent();
                }
                default -> throw new semanticError("classBuildNode contains illegal stmt", it.pos);
            }
        }
        if (!currentScope.existReturnForFun) {
            if (!(it.typeNd.type.atomType == Type.TypeEnum.VOID)) {
                if (currentScope.parent().getType() == Scope.scopeType.GLOBAL && it.name.equals("main")) {
                    return;
                }
                throw new semanticError("none void funDefNode without return", it.pos);
            }
        }
//        if (!(it.typeNd.type.atomType == Type.TypeEnum.VOID && currentScope.funcReturnType == null) &&
//                !it.typeNd.type.equals(currentScope.funcReturnType)) {
//            throw new semanticError("funDefNode return type not match", it.pos);
//        }
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
        if (it.typeNd.type.atomType == Type.TypeEnum.VOID || it.typeNd.type.atomType == Type.TypeEnum.NULL) {
            throw new semanticError("varDefNode type is void", it.pos);
        }
        if (it.typeNd.type.atomType == Type.TypeEnum.CLASS) {
            gScope.getClassFromName(it.typeNd.type.name, it.pos);
        }

        for (var unit : it.units) {
            if (unit.init != null) {
                //不能重名
                if (gScope.containsFun(unit.name)) {
                    throw new semanticError("varDefNode name conflict", it.pos);
                }
                //在visit更新currentType
                visit(unit.init);
                if (!currentType.equals(it.typeNd.type) && !currentType.equals(new Type(Type.TypeEnum.NULL))) {
                    throw new semanticError("varDefNode init type not match", it.pos);
                }
            }
            currentScope.addVar(unit.name, it.typeNd.type, unit.pos);
        }
    }

    @Override
    public void visit(funParaList it) {
        if (it.paraList == null) return;
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
        Type tmp = it.typeNd.type.clone();
        tmp.dim--;
        if (tmp.dim == 0) {
            tmp.isArray = false;
        }
        for (var node : it.value) {
            visit(node);
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
            case arrayNode an -> visit(an);
            default -> throw new semanticError("visit atomExprNode", it.pos);
        }
    }

    @Override
    public void visit(boolNode it) {
        it.isLeftValue = false;
        currentType = it.typeNd.type;
    }

    private Class_ nowClass() {
        Scope ret = currentScope;
        while (currentScope.getType() != Scope.scopeType.CLASS) {
            if (currentScope.getType() == Scope.scopeType.GLOBAL || currentScope.getType() == null) {
                currentScope = ret;
                return null;
            }
            currentScope = currentScope.parent();
        }
        Class_ cla = gScope.getClassFromName(currentScope.name, new position(-1, -1));
        currentScope = ret;
        return cla;
    }

    @Override
    public void visit(identifierNode it) {
        if (currentScope.containsVar(it.name, true)) {
            it.isLeftValue = true;
            it.typeNd.type = currentScope.getVarType(it.name, true).clone();
        } else {
//            assert it.typeNd.type == null;
            it.typeNd = new typeNode(it.pos);
            it.typeNd.type = new Type();
            it.typeNd.type.isFun = true;
//            System.err.println("WARNING:假设 identifier 是 global function");
            Class_ cla = nowClass();
            Function fun;
            if (cla != null && cla.containsFun(it.name)) {
                fun = cla.getFunFromName(it.name, it.pos);
                it.typeNd.type = fun.returnType.clone();
                it.typeNd.type.fun = new Type.funInfo(fun.name, cla.name);
            } else {
                fun = gScope.getFunFromName(it.name, it.pos);
                it.typeNd.type = fun.returnType.clone();
                it.typeNd.type.fun = new Type.funInfo(fun.name);
            }
            it.isLeftValue = false;
            it.typeNd.type.isFun = true;
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
        Class_ cla = nowClass();
        if (cla != null) {
            it.typeNd.type.name = cla.name;
        } else {
            throw new semanticError("thisNode not in class", it.pos);
        }
        currentType = it.typeNd.type;
    }

    @Override
    public void visit(arrayExprNode it) {
        visit(it.array);
        if (!currentType.isArray) {
            throw new semanticError("arrayExprNode array type not match 01", it.pos);
        }
//        if (it.array instanceof newArrayExprNode) {
//            throw new semanticError("array can't be new", it.pos);
//        }
        visit(it.index);
        if (!currentType.equals(new Type(Type.TypeEnum.INT))) {
            throw new semanticError("arrayExprNode index type not match 02", it.pos);
        }
        it.isLeftValue = true;
        it.typeNd = new typeNode(it.pos);
        it.typeNd.type = it.array.typeNd.type.clone();
        it.typeNd.type.dim--;
        if (it.typeNd.type.dim == 0) {
            it.typeNd.type.isArray = false;
        }
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
        if (!(it.rhs instanceof nullNode) && !it.rhs.typeNd.type.equals(it.lhs.typeNd.type)) {
            throw new semanticError("assignExprNode type not match", it.pos);
        }
        if (it.rhs instanceof nullNode) {
            if (it.lhs.typeNd.type.atomType == Type.TypeEnum.INT ||
                    it.lhs.typeNd.type.atomType == Type.TypeEnum.BOOL) {
                if (!it.lhs.typeNd.type.isArray)
                    throw new semanticError("null cannot be assigned to primitive type variable", it.pos);
            }
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
        it.typeNd = new typeNode(it.pos);
        it.isLeftValue = false;
        if (it.opCode.equals(binaryExprNode.binaryOpType.eq) || it.opCode.equals(binaryExprNode.binaryOpType.unEq)) {
            it.typeNd.type = new Type(Type.TypeEnum.BOOL);
            boolean lnull = it.lhs instanceof nullNode,
                    rnull = it.rhs instanceof nullNode;
            if (lnull) {
                if (!rnull && !it.rhs.typeNd.type.equals(new Type(Type.TypeEnum.CLASS)) && !it.rhs.typeNd.type.equals(new Type(Type.TypeEnum.STRING))) {
                    throw new semanticError("binaryExprNode type not match", it.pos);
                }
            } else if (rnull) {
                if (it.lhs.typeNd.type.atomType != Type.TypeEnum.CLASS && !it.lhs.typeNd.type.equals(new Type(Type.TypeEnum.STRING)) && !it.lhs.typeNd.type.isArray) {
                    throw new semanticError("binaryExprNode type not match", it.pos);
                }
            } else if (!it.lhs.typeNd.type.equals(it.rhs.typeNd.type)) {
                throw new semanticError("binaryExprNode type not match", it.pos);
            }
            currentType = it.typeNd.type;
            return;
        }
        if (!it.rhs.typeNd.type.equals(it.lhs.typeNd.type)) {
            throw new semanticError("binaryExprNode type not match: different type", it.pos);
        }
        if (it.lhs.typeNd.type.equals(new Type(Type.TypeEnum.INT))) {
            if (it.opCode.equals(binaryExprNode.binaryOpType.andLg) ||
                    it.opCode.equals(binaryExprNode.binaryOpType.orLg)) {
                throw new semanticError("binaryExprNode type not match: int invalid", it.pos);
            }
            if (it.opCode.equals(binaryExprNode.binaryOpType.greater) || it.opCode.equals(binaryExprNode.binaryOpType.less) ||
                    it.opCode.equals(binaryExprNode.binaryOpType.grEq) || it.opCode.equals(binaryExprNode.binaryOpType.lessEq)
//                    ||it.opCode.equals(binaryExprNode.binaryOpType.unEq) || it.opCode.equals(binaryExprNode.binaryOpType.eq)
            ) {
                it.typeNd.type = new Type(Type.TypeEnum.BOOL);
            } else {
                it.typeNd.type = new Type(Type.TypeEnum.INT);
            }
        } else if (it.lhs.typeNd.type.equals(new Type(Type.TypeEnum.STRING))) {
            if (it.opCode.equals(binaryExprNode.binaryOpType.add)) {
                it.typeNd.type = new Type(Type.TypeEnum.STRING);
            } else if (
//                    it.opCode.equals(binaryExprNode.binaryOpType.eq) ||
//                    it.opCode.equals(binaryExprNode.binaryOpType.unEq) ||
                    it.opCode.equals(binaryExprNode.binaryOpType.less) ||
                            it.opCode.equals(binaryExprNode.binaryOpType.lessEq) ||
                            it.opCode.equals(binaryExprNode.binaryOpType.greater) ||
                            it.opCode.equals(binaryExprNode.binaryOpType.grEq)) {
                it.typeNd.type = new Type(Type.TypeEnum.BOOL);
            } else {
                throw new semanticError("binaryExprNode type not match: string invalid", it.pos);
            }
        } else if (it.lhs.typeNd.type.equals(new Type(Type.TypeEnum.BOOL))) {
            if (
//                    it.opCode.equals(binaryExprNode.binaryOpType.eq) ||
//                    it.opCode.equals(binaryExprNode.binaryOpType.unEq) ||
                    it.opCode.equals(binaryExprNode.binaryOpType.andLg) ||
                            it.opCode.equals(binaryExprNode.binaryOpType.orLg)) {
                it.typeNd.type = new Type(Type.TypeEnum.BOOL);
            } else {
                throw new semanticError("binaryExprNode type not match: bool invalid", it.pos);
            }
        } else {
            throw new semanticError("binaryExprNode type not match: type invalid", it.pos);
        }
        currentType = it.typeNd.type;
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

    private boolean canBeNull(Type t) {
        return t.atomType == Type.TypeEnum.CLASS || t.atomType == Type.TypeEnum.STRING || t.isArray || t.isfString;
    }

    @Override
    public void visit(funExprNode it) {
        visit(it.fun);
        if (!currentType.isFun) {
            throw new semanticError("funExprNode fun type not match", it.pos);
        }
        Function fun = null;
        if (it.fun.typeNd.type.fun.isInBuildMethod) {
            switch (it.fun.typeNd.type.fun.funName) {
                case "length" -> fun = Function.lengthInBuild;
                case "substring" -> fun = Function.substringInBuild;
                case "parseInt" -> fun = Function.parseIntInBuild;
                case "ord" -> fun = Function.ordInBuild;
                case "size" -> fun = Function.sizeInBuild;
                default -> {
                    assert false;
                }
            }
        } else {
            if (it.fun.typeNd.type.fun.inGlobal) {
                fun = gScope.getFunFromName(it.fun.typeNd.type.fun.funName, it.pos);
            } else {
                Class_ class_ = gScope.getClassFromName(it.fun.typeNd.type.fun.className, it.pos);
                fun = class_.getFunFromName(it.fun.typeNd.type.fun.funName, it.pos);
            }
        }
        if (!it.paras.isEmpty())
            for (var node : it.paras) {
                visit(node);
                assert currentType.equals(node.typeNd.type);
            }
        if (fun.paramTypes.size() != it.paras.size()) {
            throw new semanticError("funExprNode para size not match", it.pos);
        }
        for (int i = 0; i < fun.paramTypes.size(); i++) {
            if (!fun.paramTypes.get(i).equals(it.paras.get(i).typeNd.type)) {
                if (canBeNull(fun.paramTypes.get(i)) && it.paras.get(i).typeNd.type.equals(new Type(Type.TypeEnum.NULL))) {
                    continue;
                }
                throw new semanticError("funExprNode para type not match", it.pos);
            }
        }
        it.typeNd = new typeNode(it.pos);
        it.typeNd.type = fun.returnType.clone();
        it.isLeftValue = false;
        currentType = fun.returnType;
    }

    private Boolean inBuildMethod(memberExprNode it) {
        //this
//        assert false;
        it.typeNd = new typeNode(it.pos);
        if (it.object instanceof stringNode sn || it.object.typeNd.type.atomType == Type.TypeEnum.STRING) {
            if (it.member.name.equals("length")) {
                it.typeNd.type = new Type(Type.TypeEnum.INT, "length");
                currentType = it.typeNd.type;
                return true;
            } else if (it.member.name.equals("substring")) {
                it.typeNd.type = new Type(Type.TypeEnum.STRING, "substring");
                currentType = it.typeNd.type;
                return true;
            } else if (it.member.name.equals("parseInt")) {
                it.typeNd.type = new Type(Type.TypeEnum.INT, "parseInt");
                currentType = it.typeNd.type;
                return true;
            } else if (it.member.name.equals("ord")) {
                it.typeNd.type = new Type(Type.TypeEnum.INT, "ord");
                currentType = it.typeNd.type;
                return true;
            }
        } else if (it.object.typeNd.type.isArray && !(it.object instanceof arrayNode)) {
            if (it.member.name.equals("size")) {
                it.typeNd.type = new Type(Type.TypeEnum.INT, "size");
                currentType = it.typeNd.type;
                return true;
            }
        }
        return false;
    }

    private String getThis() {
        Scope ret = currentScope;
        String name;
        while (currentScope.getType() != Scope.scopeType.CLASS) {
            if (currentScope.getType() == Scope.scopeType.GLOBAL || currentScope.getType() == null) {
                throw new semanticError("this not in class", new position(0, 0));
            }
            currentScope = currentScope.parent();
        }
        name = currentScope.name;
        currentScope = ret;
        return name;
    }

    @Override
    public void visit(memberExprNode it) {
        visit(it.object);
        assert currentType.equals(it.object.typeNd.type);
        if (inBuildMethod(it)) {
            it.isLeftValue = false;
            return;
        }
        if (!it.object.typeNd.type.atomType.equals(Type.TypeEnum.CLASS)) {
            throw new semanticError("memberExprNode object is not a class", it.pos);
        }
        if (it.object instanceof thisNode) {
            it.object.typeNd.type.name = getThis();
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
            it.typeNd.type.isFun = true;
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
        if (it.init != null) {
            visit(it.init);
            assert currentType.equals(it.init.typeNd.type);
            if (!it.init.typeNd.type.equals(it.typeNd.type)) {
                throw new semanticError("newVarExprNode type not match", it.pos);
            }
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
        it.isLeftValue = true;
        it.typeNd = new typeNode(it.pos);
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
            it.typeNd = new typeNode(it.pos);
            it.typeNd.type = new Type(Type.TypeEnum.BOOL);
        } else {
            if (it.opCode.equals(unaryExprNode.unaryOpType.Inc) || it.opCode.equals(unaryExprNode.unaryOpType.Dec)) {
                if (!it.body.isLeftValue) {
                    throw new semanticError("unaryExprNode body not left value", it.pos);
                }
            }
            if (!it.body.typeNd.type.equals(new Type(Type.TypeEnum.INT))) {
                throw new semanticError("unaryExprNode not type not int", it.pos);
            }
            it.isLeftValue = false;
            it.typeNd = new typeNode(it.pos);
            it.typeNd.type = new Type(Type.TypeEnum.INT);
        }
        currentType = it.typeNd.type;
    }

    @Override
    public void visit(blockStmtNode it) {
        for (var node : it.stmts) {
            switch (node) {
                case blockStmtNode bsn -> {
                    currentScope = new Scope(currentScope, Scope.scopeType.BLOCK, "block");
                    visit(bsn);
                    currentScope = currentScope.parent();
                }
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
            case varDefNode vdn -> visit(vdn);
            default -> throw new semanticError("visit StmtNode", it.pos);
        }
    }

    @Override
    public void visit(forStmtNode it) {
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
        currentScope = new Scope(currentScope, Scope.scopeType.IF, "if");
        visit(it.thenStmt);
        currentScope = currentScope.parent();
        if (it.elseStmt != null) {
            currentScope = new Scope(currentScope, Scope.scopeType.IF, "else");
            visit(it.elseStmt);
            currentScope = currentScope.parent();
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
        if (currentScope.funcReturnType == null) {
            if (it.value != null) {
                throw new semanticError("returnStmtNode type not match", it.pos);
            }
            currentScope = ret;
            return;
        }

        returnType = currentScope.funcReturnType.clone();
        currentScope.existReturnForFun = true;

        currentScope = ret;
        if (it.value != null) {
            visit(it.value);
//            currentScope.funcReturnType=it.value.typeNd.type.clone();
            if (!currentType.equals(returnType)) {
                if (currentType.equals(new Type(Type.TypeEnum.NULL))) {
                    if (returnType.isArray || returnType.isfString || returnType.atomType == Type.TypeEnum.CLASS || returnType.atomType == Type.TypeEnum.STRING) {
                        return;
                    }
                }
                throw new semanticError("returnStmtNode type not match", it.pos);
            }
        } else {
//            currentScope.funcReturnType=null;
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
        currentScope = currentScope.parent();
    }

    @Override
    public void visit(typeNode it) {
        assert false;
    }

    @Override
    public void visit(emptyStmtNode it) {
    }
}
