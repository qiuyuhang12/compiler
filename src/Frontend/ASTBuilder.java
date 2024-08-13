package Frontend;

import AST.*;
import AST.def.*;
import AST.expr.*;
import AST.expr.atom.*;
import AST.stmt.*;
import Parser.MxBaseVisitor;
import Parser.MxParser;
import Util.Type;
//import Util.globalScope;
import Util.position;
import org.antlr.v4.runtime.ParserRuleContext;
import Util.error.*;
import org.antlr.v4.runtime.tree.TerminalNode;

public class ASTBuilder extends MxBaseVisitor<ASTNode> {

//    private globalScope gScope;
//    public ASTBuilder(globalScope gScope) {
//        this.gScope = gScope;
//    }

    @Override
    public ASTNode visitProgram(MxParser.ProgramContext ctx) {
        ProgramNode prog = new ProgramNode(new position(ctx));
        ctx.children.forEach(cd -> {
            if (!(cd instanceof TerminalNode)) prog.Defs.add(visit(cd));
        });
        return prog;
    }

    @Override
    public ASTNode visitType(MxParser.TypeContext ctx) {
        typeNode type = new typeNode(new position(ctx));
        if (ctx.basicType() != null) {
            if (ctx.basicType().Int() != null) type.type.atomType = Type.TypeEnum.INT;
            else if (ctx.basicType().Bool() != null) type.type.atomType = Type.TypeEnum.BOOL;
            else if (ctx.basicType().String() != null) type.type.atomType = Type.TypeEnum.STRING;
            else assert false;
        } else {
            type.type.atomType = Type.TypeEnum.CLASS;
            type.type.name = ctx.Identifier().toString();
        }
        if (ctx.dim() != null && !ctx.dim().isEmpty()) {
            type.type.isArray = true;
            type.type.dim = ctx.dim().size();
        }
        return type;
    }

    @Override
    public ASTNode visitTypeAtom(MxParser.TypeAtomContext ctx) {
        typeNode type = new typeNode(new position(ctx));
        if (ctx.basicType() != null) {
            if (ctx.basicType().Int() != null) type.type.atomType = Type.TypeEnum.INT;
            else if (ctx.basicType().Bool() != null) type.type.atomType = Type.TypeEnum.BOOL;
            else if (ctx.basicType().String() != null) type.type.atomType = Type.TypeEnum.STRING;
            else assert false;
        } else {
            type.type.atomType = Type.TypeEnum.CLASS;
            type.type.name = ctx.Identifier().toString();
        }
        return type;
    }


    //def
    @Override
    public ASTNode visitClassBuild(MxParser.ClassBuildContext ctx) {
        classBuildNode classBuild = new classBuildNode(new position(ctx));
        classBuild.name = ctx.Identifier().toString();
//        if (ctx.statement() != null){
//            throw new semanticError("classBuild doesn't accept parameters", new position(ctx));
//        }
        ctx.statement().forEach(stmt -> classBuild.stmts.add((StmtNode) visit(stmt)));
        return classBuild;
    }

    @Override
    public ASTNode visitClassDef(MxParser.ClassDefContext ctx) {
        classDefNode classDef = new classDefNode(new position(ctx));
        classDef.name = ctx.Identifier().toString();
        ctx.varDef().forEach(vd -> classDef.varDefs.add((varDefNode) visit(vd)));
        ctx.funDef().forEach(fd -> classDef.funDefs.add((funDefNode) visit(fd)));
        if (ctx.classBuild().size() > 1)
            throw new semanticError("classBuild size > 1", new position(ctx), semanticError.errorType.Multiple_Definitions);
        ctx.classBuild().forEach(cb -> classDef.build.add((classBuildNode) visit(cb)));
        return classDef;
    }

    @Override
    public ASTNode visitFunDef(MxParser.FunDefContext ctx) {
        funDefNode fun = new funDefNode(new position(ctx));
        fun.name = ctx.Identifier().toString();
        if (ctx.funParaList() != null) fun.paraList = (funParaList) visit(ctx.funParaList());

//        gScope.addType("int", intType, fun.pos);
//        gScope.addType("bool", boolType, fun.pos);

        if (ctx.type() != null) fun.typeNd = (typeNode) visit(ctx.type());
        else {
            fun.typeNd = new typeNode(new position(ctx));
            fun.typeNd.type.atomType = Type.TypeEnum.VOID;
        }
        if (ctx.statement() != null) {
            for (ParserRuleContext stmt : ctx.statement())
                fun.stmts.add((StmtNode) visit(stmt));
        }
        return fun;
    }

    @Override
    public ASTNode visitVarDef(MxParser.VarDefContext ctx) {
        varDefNode varDef = new varDefNode(new position(ctx));
        varDef.typeNd = (typeNode) visit(ctx.type());
        ctx.varDefUnit().forEach(vdu -> {
            varDefUnitNode unit = new varDefUnitNode(new position(vdu));
            unit.name = vdu.Identifier().toString();
            if (vdu.expression() != null) unit.init = (ExprNode) visit(vdu.expression());
            varDef.units.add(unit);
        });
        return varDef;
    }

    @Override
    public ASTNode visitFunParaList(MxParser.FunParaListContext ctx) {
        funParaList paraList = new funParaList(new position(ctx));
        for (int i = 0; i < ctx.Identifier().size(); i++) {
            varDefNode vd = new varDefNode(new position(ctx.type(i)));
            vd.typeNd = (typeNode) visit(ctx.type(i));
            varDefUnitNode unit = new varDefUnitNode(new position(ctx));
            unit.name = ctx.Identifier(i).toString();
            vd.units.add(unit);
            paraList.paraList.add(vd);
        }
        return paraList;
    }


    //expr.atom
    @Override
    public ASTNode visitAtomExpr(MxParser.AtomExprContext ctx) {
        if (ctx.literal() != null) {
            if (ctx.literal().True() != null)
                return new boolNode(new position(ctx.literal().True()), true);
            else if (ctx.literal().False() != null)
                return new boolNode(new position(ctx.literal().False()), false);
            else if (ctx.literal().Null() != null)
                return new nullNode(new position(ctx.literal().Null()));
            else if (ctx.literal().StringConst() != null)
                return new stringNode(new position(ctx.literal().StringConst()), ctx.literal().StringConst().toString());
            else if (ctx.literal().IntegerConst() != null)
                return new intNode(new position(ctx.literal().IntegerConst()), Integer.parseInt(ctx.literal().IntegerConst().toString()));
            else if (ctx.literal().arrayConst() != null)
                return visit(ctx.literal().arrayConst());
        } else if (ctx.Identifier() != null) {
            return new identifierNode(new position(ctx.Identifier()), ctx.Identifier().toString());
        } else if (ctx.This() != null)
            return new thisNode(new position(ctx.This()));
        throw new semanticError("atomExpr error", new position(ctx), semanticError.errorType.Invalid_Identifier);
    }

    @Override
    public ASTNode visitArrayConst(MxParser.ArrayConstContext ctx) {
        arrayNode array = new arrayNode(new position(ctx));
        ctx.literal().forEach(lit -> array.value.add((atomExprNode) visit(lit)));
        if (array.value.isEmpty()) {
            assert ctx.children.size() == 2 &&
                    ctx.children.get(0).getText().equals("{") &&
                    ctx.children.get(1).getText().equals("}");
            array.typeNd = new typeNode(new position(ctx));
            array.typeNd.type.dim = 1;
            array.typeNd.type.atomType = Type.TypeEnum.NULL;
            array.typeNd.type.isArray = true;
            return array;
        }
        ExprNode first = array.value.getFirst();
//        ExprNode first;
        for (atomExprNode a : array.value) {
            if (!a.typeNd.type.atomType.equals(Type.TypeEnum.NULL)) {
                first = a;
                break;
            }
        }
        if (first instanceof arrayNode) {
            array.typeNd = new typeNode(new position(ctx));
            array.typeNd.type = first.typeNd.type.clone();
            array.typeNd.type.dim = ((arrayNode) first).typeNd.type.dim + 1;
            for (atomExprNode a : array.value) {
                if (!(a instanceof arrayNode))
                    throw new semanticError("arrayConst error:Different type 1", new position(ctx), semanticError.errorType.Type_Mismatch);
                if (((arrayNode) a).typeNd.type.dim != array.typeNd.type.dim - 1)
                    throw new semanticError("arrayConst error:Dim error 1", new position(ctx), semanticError.errorType.Type_Mismatch);
                if (!((arrayNode) a).typeNd.type.equals(first.typeNd.type) && !((arrayNode) a).typeNd.type.atomType.equals(Type.TypeEnum.NULL))
                    throw new semanticError("arrayConst error:Different type 1", new position(ctx), semanticError.errorType.Type_Mismatch);
            }
        } else {
//            array.dim = 1;
            array.typeNd = new typeNode(new position(ctx));
//            try {
            array.typeNd.type = first.typeNd.type.clone();
//            } catch (CloneNotSupportedException e) {
//                System.err.println("不应该发生，因为我们实现了 Cloneable 3");
//            }
            array.typeNd.type.dim = 1;
            array.typeNd.type.isArray = true;
            for (atomExprNode a : array.value) {
                if (a instanceof arrayNode)
                    throw new semanticError("arrayConst error:Dim error 0", new position(ctx), semanticError.errorType.Type_Mismatch);
                //比较类型
                if (!(a.getClass().equals(first.getClass())))
                    throw new semanticError("arrayConst error:Different type 0", new position(ctx), semanticError.errorType.Type_Mismatch);
                if (a instanceof identifierNode || a instanceof thisNode)
                    throw new semanticError("arrayConst error:using Identifier or this as init", new position(ctx), semanticError.errorType.Invalid_Identifier);
            }
        }
        return array;
    }


    //expr
    @Override
    public ASTNode visitArrayExpr(MxParser.ArrayExprContext ctx) {
        arrayExprNode array = new arrayExprNode(new position(ctx));
        if (ctx.array instanceof MxParser.NewArrayExprContext) {
            throw new semanticError("ArrayExpr: NewArrayExpr", new position(ctx), semanticError.errorType.Invalid_Identifier);
        }
        array.array = (ExprNode) visit(ctx.array);
        array.index = (ExprNode) visit(ctx.index);
        return array;
    }

    @Override
    public ASTNode visitAssignExpr(MxParser.AssignExprContext ctx) {
        assignExprNode assign = new assignExprNode(new position(ctx));
        assign.lhs = (ExprNode) visit(ctx.lhs);
        assign.rhs = (ExprNode) visit(ctx.rhs);
        return assign;
    }

    @Override
    public ASTNode visitBinaryExpr(MxParser.BinaryExprContext ctx) {
        binaryExprNode binary = new binaryExprNode(new position(ctx));
        switch (ctx.op.getText()) {
            case "*":
                binary.opCode = binaryExprNode.binaryOpType.mul;
                break;
            case "/":
                binary.opCode = binaryExprNode.binaryOpType.div;
                break;
            case "%":
                binary.opCode = binaryExprNode.binaryOpType.mod;
                break;
            case "+":
                binary.opCode = binaryExprNode.binaryOpType.add;
                break;
            case "-":
                binary.opCode = binaryExprNode.binaryOpType.sub;
                break;
            case "<<":
                binary.opCode = binaryExprNode.binaryOpType.leftSh;
                break;
            case ">>":
                binary.opCode = binaryExprNode.binaryOpType.rightSh;
                break;
            case "<":
                binary.opCode = binaryExprNode.binaryOpType.less;
                break;
            case ">":
                binary.opCode = binaryExprNode.binaryOpType.greater;
                break;
            case "<=":
                binary.opCode = binaryExprNode.binaryOpType.lessEq;
                break;
            case ">=":
                binary.opCode = binaryExprNode.binaryOpType.grEq;
                break;
            case "==":
                binary.opCode = binaryExprNode.binaryOpType.eq;
                break;
            case "!=":
                binary.opCode = binaryExprNode.binaryOpType.unEq;
                break;
            case "&":
                binary.opCode = binaryExprNode.binaryOpType.and;
                break;
            case "^":
                binary.opCode = binaryExprNode.binaryOpType.xor;
                break;
            case "|":
                binary.opCode = binaryExprNode.binaryOpType.or;
                break;
            case "&&":
                binary.opCode = binaryExprNode.binaryOpType.andLg;
                break;
            case "||":
                binary.opCode = binaryExprNode.binaryOpType.orLg;
                break;
            default:
                throw new semanticError("binaryExpr error", new position(ctx), semanticError.errorType.Invalid_Identifier);
        }
        binary.lhs = (ExprNode) visit(ctx.lhs);
        binary.rhs = (ExprNode) visit(ctx.rhs);
        return binary;
    }

    @Override
    public ASTNode visitConditionalExpr(MxParser.ConditionalExprContext ctx) {
        conditionalExprNode cond = new conditionalExprNode(new position(ctx));
        cond.condition = (ExprNode) visit(ctx.cond);
        cond.trueExpr = (ExprNode) visit(ctx.trueex);
        cond.falseExpr = (ExprNode) visit(ctx.falseex);
        return cond;
    }

    @Override
    public ASTNode visitFormatStringExpr(MxParser.FormatStringExprContext ctx) {
        formatStringExprNode format = new formatStringExprNode(new position(ctx));
        if (ctx.formatString().FmtStringS() != null) {
            assert ctx.formatString().FmtStringL() == null &&
                    ctx.formatString().FmtStringM() == null &&
                    ctx.formatString().FmtStringS() == null &&
                    ctx.formatString().expression().isEmpty();
            format.strings.add(ctx.formatString().FmtStringS().toString());
        } else {
            format.strings.add(ctx.formatString().FmtStringL().toString());
            ctx.formatString().FmtStringM().forEach(m -> format.strings.add(m.toString()));
            format.strings.add(ctx.formatString().FmtStringR().toString());
            ctx.formatString().expression().forEach(exp -> format.exprs.add((ExprNode) visit(exp)));
        }
//        format.str = ctx.formatString().toString();
//        System.out.println(ctx.formatString().FmtStringS().toString());
//        System.err.println("你检查toString的正确性了吗？str：" + format.str);
        return format;
    }

    @Override
    public ASTNode visitNewVarExpr(MxParser.NewVarExprContext ctx) {
        //todo SCOPE!

        newVarExprNode newVar = new newVarExprNode(new position(ctx));
        newVar.typeNd = (typeNode) visit(ctx.typeAtom());
        if (ctx.init != null) newVar.init = (ExprNode) visit(ctx.init);
        return newVar;
    }

    @Override
    public ASTNode visitFunExpr(MxParser.FunExprContext ctx) {
        //你有办法确定返回值吗？
//        assert false;
        funExprNode fun = new funExprNode(new position(ctx));
        fun.fun = (ExprNode) visit(ctx.fun);
        if (ctx.para != null) {
            ctx.para.forEach(p -> fun.paras.add((ExprNode) visit(p)));
        }
        return fun;
    }

    @Override
    public ASTNode visitPreSelfExpr(MxParser.PreSelfExprContext ctx) {
        preSelfExprNode preSelf = new preSelfExprNode(new position(ctx));
        if (ctx.op.getText().equals("++")) preSelf.op = preSelfExprNode.opType.Inc;
        else preSelf.op = preSelfExprNode.opType.Dec;
        preSelf.body = (ExprNode) visit(ctx.expression());
        return preSelf;
    }

    @Override
    public ASTNode visitMemberExpr(MxParser.MemberExprContext ctx) {
        memberExprNode member = new memberExprNode(new position(ctx));
        member.object = (ExprNode) visit(ctx.expression());
        member.member = new identifierNode(new position(ctx.Identifier()), ctx.Identifier().toString());
        return member;
    }

    @Override
    public ASTNode visitParenExpr(MxParser.ParenExprContext ctx) {
        return visit(ctx.expression());
    }

    @Override
    public ASTNode visitUnaryExpr(MxParser.UnaryExprContext ctx) {
        unaryExprNode unary = new unaryExprNode(new position(ctx));
        switch (ctx.op.getText()) {
            case "++":
                unary.opCode = unaryExprNode.unaryOpType.Inc;
                break;
            case "--":
                unary.opCode = unaryExprNode.unaryOpType.Dec;
                break;
            case "+":
                unary.opCode = unaryExprNode.unaryOpType.plus;
                break;
            case "-":
                unary.opCode = unaryExprNode.unaryOpType.minus;
                break;
            case "!":
                unary.opCode = unaryExprNode.unaryOpType.logNot;
                break;
            case "~":
                unary.opCode = unaryExprNode.unaryOpType.bitNot;
                break;
            default:
                throw new semanticError("unaryExpr error", new position(ctx), semanticError.errorType.Invalid_Identifier);
        }
        unary.body = (ExprNode) visit(ctx.expression());
        return unary;
    }

    @Override
    public ASTNode visitNewArrayExpr(MxParser.NewArrayExprContext ctx) {
        newArrayExprNode newArray = new newArrayExprNode(new position(ctx));
        newArray.typeNd = (typeNode) visit(ctx.typeAtom());
        newArray.typeNd.type.isArray = true;
        ctx.expression().forEach(exp -> newArray.sizeInit.add((ExprNode) visit(exp)));
        if (ctx.arrayConst() != null) newArray.init = (arrayNode) visit(ctx.arrayConst());
        newArray.dim = ctx.count.size();
        newArray.typeNd.type.dim = ctx.count.size();
        if (ctx.arrayConst() != null && !newArray.typeNd.type.equals(newArray.init.typeNd.type))
            throw new semanticError("newArrayExpr error:type/dim unmatched", new position(ctx), semanticError.errorType.Type_Mismatch);
        return newArray;
    }


    //stmt
    @Override
    public ASTNode visitBlockStmt(MxParser.BlockStmtContext ctx) {
        blockStmtNode block = new blockStmtNode(new position(ctx));
        ctx.block().statement().forEach(stmt -> block.stmts.add((StmtNode) visit(stmt)));
        return block;
    }

    @Override
    public ASTNode visitVarDefStmt(MxParser.VarDefStmtContext ctx) {//特殊：返回一个varDefNode
        return visitVarDef(ctx.varDef());
    }

    @Override
    public ASTNode visitWhileStmt(MxParser.WhileStmtContext ctx) {
        whileStmtNode whileStmt = new whileStmtNode(new position(ctx));
        whileStmt.condition = (ExprNode) visit(ctx.while_().expression());
        whileStmt.body = (StmtNode) visit(ctx.while_().statement());
        return whileStmt;
    }

    @Override
    public ASTNode visitForStmt(MxParser.ForStmtContext ctx) {
        forStmtNode forStmt = new forStmtNode(new position(ctx));
        if (ctx.for_().init != null) forStmt.init = (StmtNode) visit(ctx.for_().init);
        if (ctx.for_().cond != null) forStmt.cond = (ExprNode) visit(ctx.for_().cond);
        if (ctx.for_().step != null) forStmt.step = (ExprNode) visit(ctx.for_().step);
        forStmt.body = (StmtNode) visit(ctx.for_().body);
        return forStmt;
    }

    @Override
    public ASTNode visitBreakStmt(MxParser.BreakStmtContext ctx) {
        return new breakStmtNode(new position(ctx));
    }

    @Override
    public ASTNode visitContinueStmt(MxParser.ContinueStmtContext ctx) {
        return new continueStmtNode(new position(ctx));
    }

    @Override
    public ASTNode visitExprStmt(MxParser.ExprStmtContext ctx) {
        return new exprStmtNode((ExprNode) visit(ctx.expression()), new position(ctx));
    }

    @Override
    public ASTNode visitEmptyStmt(MxParser.EmptyStmtContext ctx) {
        return new emptyStmtNode(new position(ctx));
    }

    @Override
    public ASTNode visitIfStmt(MxParser.IfStmtContext ctx) {
        ifStmtNode ifStmt = new ifStmtNode(new position(ctx));
        ifStmt.condition = (ExprNode) visit(ctx.if_().expression());
        ifStmt.thenStmt = (StmtNode) visit(ctx.if_().thenStmt);
        if (ctx.if_().elseStmt != null) ifStmt.elseStmt = (StmtNode) visit(ctx.if_().elseStmt);
        return ifStmt;
    }

    @Override
    public ASTNode visitReturnStmt(MxParser.ReturnStmtContext ctx) {
        if (ctx.return_().expression() != null) {
            return new returnStmtNode((ExprNode) visit(ctx.return_().expression()), new position(ctx));
        } else {
            return new returnStmtNode(new position(ctx));
        }
    }

    @Override
    public ASTNode visitLiteral(MxParser.LiteralContext ctx) {
        if (ctx.True() != null)
            return new boolNode(new position(ctx.True()), true);
        else if (ctx.False() != null)
            return new boolNode(new position(ctx.False()), false);
        else if (ctx.Null() != null)
            return new nullNode(new position(ctx.Null()));
        else if (ctx.StringConst() != null)
            return new stringNode(new position(ctx.StringConst()), ctx.StringConst().toString());
        else if (ctx.IntegerConst() != null)
            return new intNode(new position(ctx.IntegerConst()), Integer.parseInt(ctx.IntegerConst().toString()));
        else if (ctx.arrayConst() != null)
            return visit(ctx.arrayConst());
        throw new semanticError("literal error", new position(ctx));
    }
}
