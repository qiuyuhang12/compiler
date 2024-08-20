package Frontend.IR;

import AST.ASTNode;
import AST.ASTVisitor;
import AST.ProgramNode;
import AST.def.*;
import AST.expr.*;
import AST.expr.atom.*;
import AST.stmt.*;
import AST.typeNode;
import Frontend.IR.entity.IRBoolLiteral;
import Frontend.IR.entity.IREntity;
import Frontend.IR.entity.IRIntLiteral;
import Frontend.IR.entity.IRVar;
import Frontend.IR.node.def.IRFunDef;
import Frontend.IR.node.def.IRGlobalVarDef;
import Frontend.IR.node.inst.*;
import Frontend.IR.node.stmt.IRBlockNode;
import Frontend.IR.type.IRType;
import Frontend.IR.util.Renamer;
import Util.Type;

import java.util.ArrayList;

public class IRBuilder implements ASTVisitor {
    public Renamer renamer = new Renamer();
    public IRProgramNode irProgramNode = new IRProgramNode();
    public IRBlockNode currentBlock;
    public String currentTmpValName;//可能是字面量
    public String currentLeftVarAddr;

    public IRBuilder(ProgramNode programNode) {
        programNode.accept(this);
    }

    public void print() {
        System.out.println(irProgramNode.toString());
    }

    public IRFunDef currentFunDef;//谁使用谁复原
    public IRVar currentNoneStmtVarForFun;

    public enum status {
        global,
        inClass,
        inFun
    }

    public status currentStatus = status.global;

    @Override
    public void visit(ProgramNode it) {
        for (ASTNode def : it.Defs) {
            def.accept(this);
        }
    }

    @Override
    public void visit(classBuildNode it) {

    }

    @Override
    public void visit(classDefNode it) {

    }

    private IRType getIRtype(Type tp) {
        switch (tp.atomType) {
            case INT:
                return new IRType(IRType.IRTypeEnum.i32);
            case BOOL:
                return new IRType(IRType.IRTypeEnum.i1);
            case STRING:
            case CLASS:
            default:
                assert false;
                return null;
        }
    }

    @Override
    public void visit(funDefNode it) {
        assert currentStatus == status.global;
        assert currentFunDef == null;
        currentStatus = status.inFun;
        renamer.in();


        currentFunDef = new IRFunDef(getIRtype(it.typeNd.type), "@" + renamer.rename(it.name));
        irProgramNode.pushFunDef(currentFunDef);
        assert currentBlock == null;
        currentBlock = new IRBlockNode(null, currentFunDef, "entry");
        //处理参数
        for (varDefNode varDef : it.paraList.paraList) {
            assert currentNoneStmtVarForFun == null;
            varDef.accept(this);
            currentFunDef.pushPara(currentNoneStmtVarForFun);
            currentNoneStmtVarForFun = null;
        }
        currentFunDef.push(currentBlock);
        //留返回值
        if (it.typeNd.type.atomType != Type.TypeEnum.VOID) {
            varDefNode retVarDef = new varDefNode(it.pos);
            retVarDef.typeNd = it.typeNd;
            retVarDef.units.add(new varDefUnitNode(it.pos));
            retVarDef.units.getFirst().name = "retval";
            retVarDef.accept(this);
        }
        //处理函数体
        for (StmtNode stmt : it.stmts) {
            stmt.accept(this);
        }
        //处理返回值
        if (it.typeNd.type.atomType != Type.TypeEnum.VOID) {
            currentBlock = new IRBlockNode(currentBlock, currentFunDef, "return");
            loadInstNode lin = new loadInstNode(null, currentBlock, "%" + renamer.rename("r.e.t"), "%retval", getIRtype(it.typeNd.type));
            retInstNode rin = new retInstNode(null, currentBlock, new IRVar(getIRtype(it.typeNd.type).toString(), "%" + renamer.getRenamed("r.e.t"), false));
            currentBlock.push(lin);
            currentBlock.push(rin);
        }
        currentBlock = null;


        currentStatus = status.global;
        currentFunDef = null;
        renamer.out();
    }

    @Override
    public void visit(varDefNode it) {
        if (it.isStmt) {
            assert currentBlock != null;
            if (it.typeNd.type.isArray) {
                //TODO:array变量定义
                assert false;
            }
            IRType irType = getIRtype(it.typeNd.type);
            for (varDefUnitNode unit : it.units) {
                currentBlock.push(new allocaInstNode(it, currentBlock, new IRVar(irType.toString(), "%" + renamer.rename(unit.name), false), irType));
                if (unit.init != null) {
                    unit.init.accept(this);
                    currentBlock.push(new loadInstNode(it, currentBlock, "%" + renamer.getRenamed(unit.name), currentTmpValName, irType));
                }
            }
        } else {
            if (currentStatus == status.global) {
                if (it.typeNd.type.isArray) {
                    //TODO:array变量定义
                    assert false;
                } else {
                    switch (it.typeNd.type.atomType) {
                        case INT:
                            for (varDefUnitNode unit : it.units) {
                                if (unit.init == null) {//int a
                                    irProgramNode.pushVarDef(new IRGlobalVarDef("@" + renamer.rename(unit.name), new IRIntLiteral(0)));
                                } else if (unit.init instanceof intNode) {//int a=9;
                                    irProgramNode.pushVarDef(new IRGlobalVarDef("@" + renamer.rename(unit.name), new IRIntLiteral(((intNode) unit.init).value)));
                                } else {//int a=f(0);
                                    irProgramNode.pushVarDef(new IRGlobalVarDef("@" + renamer.rename(unit.name), new IRIntLiteral(0)));
                                    //TODO:init function
                                    assert false;
                                }
                            }
                            break;
                        case BOOL:
                            for (varDefUnitNode unit : it.units) {
                                if (unit.init == null) {//int a
                                    irProgramNode.pushVarDef(new IRGlobalVarDef("@" + renamer.rename(unit.name), new IRBoolLiteral(false)));
                                } else if (unit.init instanceof intNode) {//int a=9;
                                    irProgramNode.pushVarDef(new IRGlobalVarDef("@" + renamer.rename(unit.name), new IRBoolLiteral(((boolNode) unit.init).value)));
                                } else {//int a=f(0);
                                    irProgramNode.pushVarDef(new IRGlobalVarDef("@" + renamer.rename(unit.name), new IRBoolLiteral(false)));
                                    //TODO:init function
                                    assert false;
                                }
                            }
                            break;
                        case STRING:
                            //TODO:string
                            assert false;
                            break;
                        case CLASS:
                            //TODO:class
                            assert false;
                        default:
                            assert false;
                    }
                }
            } else if (currentStatus == status.inClass) {
                //TODO:class
                assert false;
            } else if (currentStatus == status.inFun) {//函数声明或定义的para
                if (it.typeNd.type.isArray) {
                    //TODO:array变量定义
                    assert false;
                } else {
                    assert it.units.size() == 1;
                    varDefUnitNode unit = it.units.getFirst();
                    assert unit.init == null;
                    switch (it.typeNd.type.atomType) {
                        case INT:
                            currentNoneStmtVarForFun = new IRVar("i32", "%" + renamer.rename(unit.name), false);
                            break;
                        case BOOL:
                            currentNoneStmtVarForFun = new IRVar("i1", "%" + renamer.rename(unit.name), false);
                            break;
                        case STRING:
                        case CLASS:
                        default:
                            assert false;
                    }
                }
            }
        }
    }

    @Override
    public void visit(funParaList it) {

    }

    @Override
    public void visit(varDefUnitNode it) {

    }

    @Override
    public void visit(arrayNode it) {

    }

//    @Override
//    public void visit(atomExprNode it) {
//
//    }

    @Override
    public void visit(boolNode it) {
        currentTmpValName = it.value ? "1" : "0";
    }

    @Override
    public void visit(identifierNode it) {
        currentTmpValName = renamer.getAnonymousName();
        currentLeftVarAddr = currentTmpValName;
        currentBlock.push(new loadInstNode(it, currentBlock, currentTmpValName, renamer.getRenamed(it.name), getIRtype(it.typeNd.type)));
    }

    @Override
    public void visit(intNode it) {
        currentTmpValName = String.valueOf(it.value);
    }

    @Override
    public void visit(nullNode it) {
        currentTmpValName = "null";
        currentLeftVarAddr = null;
    }

    @Override
    public void visit(stringNode it) {

    }

    @Override
    public void visit(thisNode it) {

    }

    @Override
    public void visit(arrayExprNode it) {

    }

    @Override
    public void visit(assignExprNode it) {
        it.lhs.accept(this);
        String lhsName = currentTmpValName;
        it.rhs.accept(this);
        String rhsName = currentTmpValName;
        currentBlock.push(new loadInstNode(it, currentBlock, lhsName, rhsName, getIRtype(it.lhs.typeNd.type)));
        currentTmpValName = lhsName;
    }

    @Override
    public void visit(binaryExprNode it) {
        if (it.opCode == binaryExprNode.binaryOpType.andLg || it.opCode == binaryExprNode.binaryOpType.orLg) {
            it.lhs.accept(this);
            String lhsName = currentTmpValName;
            String tmplabel = currentBlock.label;
            String nextlabel = renamer.rename("shortcircuit.next");
            String endlabel = renamer.rename("shortcircuit.end");
            //lhs:
            IRVar lhs = new IRVar("i1", lhsName, false);
            if (it.opCode == binaryExprNode.binaryOpType.andLg) {
                currentBlock.push(new brInstNode(it, currentBlock, lhs, nextlabel, endlabel));
            } else {
                currentBlock.push(new brInstNode(it, currentBlock, lhs, endlabel, nextlabel));
            }
            //next(rhs):
            currentBlock = new IRBlockNode(currentBlock, currentFunDef, nextlabel);
            it.rhs.accept(this);
            String rhsName = currentTmpValName;
            IRVar rhs = new IRVar("i1", rhsName, false);
            currentBlock.push(new brInstNode(it, currentBlock, endlabel));
            //end:
            currentBlock = new IRBlockNode(currentBlock, currentFunDef, endlabel);
            IRVar dest = new IRVar("i1", "%" + renamer.rename("shortcircuit"), false);
            phiInstNode phi = new phiInstNode(it, currentBlock, dest);
            phi.push(lhs, tmplabel);
            phi.push(rhs, nextlabel);
            currentBlock.push(phi);
            currentTmpValName = "%" + dest.name;
            return;
        }
        it.lhs.accept(this);
        String lhsName = currentTmpValName;
        it.rhs.accept(this);
        String rhsName = currentTmpValName;
        IRType irType = getIRtype(it.lhs.typeNd.type);
        IRVar lhs = new IRVar(irType.toString(), lhsName, false);
        IRVar rhs = new IRVar(irType.toString(), rhsName, false);
        IRVar dest = new IRVar(irType.toString(), "%" + renamer.rename(it.opCode.toString()), false);
        switch (it.opCode) {
            case add:
            case sub:
            case mul:
            case div:
            case mod:
            case leftSh:
            case rightSh:
            case and:
            case or:
            case xor:
                currentBlock.push(new binaryInstNode(it, currentBlock, dest, it.opCode, lhs, rhs));
            case greater:
            case less:
            case grEq:
            case lessEq:
            case unEq:
            case eq:
                currentBlock.push(new icmpInstNode(it, currentBlock, dest, it.opCode, lhs, rhs));
            case andLg:
            case orLg:
            case not:
            default:
                assert false;
        }
        currentTmpValName = "%" + dest.name;
    }

    @Override
    public void visit(conditionalExprNode it) {
        it.condition.accept(this);
        String condName = currentTmpValName;
        IRVar cond = new IRVar("i1", condName, false);
        String trueLabel = renamer.rename("cond.true");
        String falseLabel = renamer.rename("cond.false");
        String endLabel = renamer.rename("cond.end");
        currentBlock.push(new brInstNode(it, currentBlock, cond, trueLabel, falseLabel));
        currentBlock = new IRBlockNode(currentBlock, currentFunDef, trueLabel);
        String trueAddr = currentTmpValName;
        currentBlock.push(new brInstNode(it, currentBlock, endLabel));
        currentBlock = currentBlock.jumpSrc;
        currentBlock = new IRBlockNode(currentBlock, currentFunDef, falseLabel);
        it.falseExpr.accept(this);
        String falseAddr = currentTmpValName;
        currentBlock.push(new brInstNode(it, currentBlock, endLabel));
        currentBlock = currentBlock.jumpSrc;
        currentBlock = new IRBlockNode(currentBlock, currentFunDef, endLabel);
        currentTmpValName = "%" + renamer.rename("cond");
        IRVar dest = new IRVar(getIRtype(it.typeNd.type).toString(), currentTmpValName, false);
        phiInstNode phi = new phiInstNode(it, currentBlock, dest);
        phi.push(new IRVar(getIRtype(it.trueExpr.typeNd.type).toString(), trueAddr, false), trueLabel);
        phi.push(new IRVar(getIRtype(it.falseExpr.typeNd.type).toString(), falseAddr, false), falseLabel);
        currentBlock.push(phi);
    }

    @Override
    public void visit(formatStringExprNode it) {

    }

    //a.f().h();
    @Override
    public void visit(funExprNode it) {
        if (it.typeNd.type.fun.inGlobal) {
            ArrayList<IREntity> paras = new ArrayList<>();
            for (ExprNode para : it.paras) {
                para.accept(this);
                paras.add(new IRVar(getIRtype(para.typeNd.type).toString(), currentTmpValName, false));
            }
            if (it.typeNd.type.atomType == Type.TypeEnum.VOID) {
                currentBlock.push(new callInstNode(it, currentBlock, "@" + it.typeNd.type.fun.funName, paras));
            } else {
                currentTmpValName = "%" + renamer.rename(it.typeNd.type.fun.funName + ".ret");
                IRVar dest = new IRVar(getIRtype(it.typeNd.type).toString(), currentTmpValName, false);
                currentBlock.push(new callInstNode(it, currentBlock, dest, getIRtype(it.typeNd.type), "@" + it.typeNd.type.fun.funName, paras));
            }
        } else {
            //TODO: member-function call
            assert false;
        }
    }

    @Override
    public void visit(memberExprNode it) {

    }

    @Override
    public void visit(newArrayExprNode it) {

    }

    @Override
    public void visit(newVarExprNode it) {

    }

    @Override
    public void visit(preSelfExprNode it) {
        it.body.accept(this);
        IRVar body = new IRVar(getIRtype(it.body.typeNd.type).toString(), currentTmpValName, false);
        IRVar dest = new IRVar(getIRtype(it.typeNd.type).toString(), "%" + renamer.rename("pre." + it.op.toString()), false);
        currentBlock.push(new binaryInstNode(it, currentBlock, dest, it.op == preSelfExprNode.opType.Dec ? binaryExprNode.binaryOpType.sub : binaryExprNode.binaryOpType.add, body, new IRIntLiteral(1)));
        IRVar addr = new IRVar("ptr", currentLeftVarAddr, false);
        addr.typeInfo = getIRtype(it.typeNd.type);
        currentBlock.push(new storeInstNode(it, currentBlock, dest, addr));
//        currentLeftVarAddr = currentLeftVarAddr;
        currentTmpValName = "%" + dest.name;
    }

    @Override
    public void visit(unaryExprNode it) {
        it.body.accept(this);
        IRVar body = new IRVar(getIRtype(it.body.typeNd.type).toString(), currentTmpValName, false);
        IRVar dest = new IRVar(getIRtype(it.typeNd.type).toString(), "%" + renamer.rename(it.opCode.toString()), false);
        switch (it.opCode) {
            case Inc:
            case Dec:
                currentBlock.push(new binaryInstNode(it, currentBlock, dest, it.opCode == unaryExprNode.unaryOpType.Dec ? binaryExprNode.binaryOpType.sub : binaryExprNode.binaryOpType.add, body, new IRIntLiteral(1)));
                IRVar addr = new IRVar("ptr", currentLeftVarAddr, false);
                addr.typeInfo = getIRtype(it.typeNd.type);
                currentBlock.push(new storeInstNode(it, currentBlock, dest, addr));
//                currentTmpValName = currentTmpValName;
                currentLeftVarAddr = null;
                break;
            case logNot:
                currentBlock.push(new binaryInstNode(it, currentBlock, dest, binaryExprNode.binaryOpType.xor, body, new IRBoolLiteral(true)));
                currentTmpValName = "%" + dest.name;
                break;
            case bitNot:
                currentBlock.push(new binaryInstNode(it, currentBlock, dest, binaryExprNode.binaryOpType.xor, body, new IRIntLiteral(-1)));
                currentTmpValName = "%" + dest.name;
                break;
            case minus:
                currentBlock.push(new binaryInstNode(it, currentBlock, dest, binaryExprNode.binaryOpType.sub, new IRIntLiteral(0), body));
                currentTmpValName = "%" + dest.name;
                break;
            case plus:
                break;
        }
    }

    @Override
    public void visit(blockStmtNode it) {
        renamer.in();
        for (StmtNode stmt : it.stmts) {
            stmt.accept(this);
        }
        renamer.out();
    }

    @Override
    public void visit(breakStmtNode it) {
        currentBlock.push(new brInstNode(it, currentBlock, renamer.getRenamed("end..")));
    }

    @Override
    public void visit(continueStmtNode it) {
        currentBlock.push(new brInstNode(it, currentBlock, renamer.getRenamed("step..")));
    }

    @Override
    public void visit(exprStmtNode it) {
        it.expr.accept(this);
    }

    @Override
    public void visit(forStmtNode it) {
        renamer.in();
        if (it.init != null) {
            it.init.accept(this);
        }
        String condLabel = renamer.rename("cond..");
        String bodyLabel = renamer.rename("body..");
        String stepLabel = renamer.rename("step..");
        String endLabel = renamer.rename("end..");
        currentBlock.push(new brInstNode(it, currentBlock, condLabel));
        currentBlock = new IRBlockNode(currentBlock, currentFunDef, condLabel);
        if (it.cond != null) {
            it.cond.accept(this);
            IRVar cond = new IRVar("i1", currentTmpValName, false);
            currentBlock.push(new brInstNode(it, currentBlock, cond, bodyLabel, endLabel));
        } else {
            currentBlock.push(new brInstNode(it, currentBlock, bodyLabel));
        }
        currentBlock = new IRBlockNode(currentBlock, currentFunDef, bodyLabel);
        if (it.body != null) {
            it.body.accept(this);
        }
        currentBlock.push(new brInstNode(it, currentBlock, stepLabel));
        currentBlock = new IRBlockNode(currentBlock, currentFunDef, stepLabel);
        if (it.step != null) {
            it.step.accept(this);
        }
        currentBlock.push(new brInstNode(it, currentBlock, condLabel));
        currentBlock = new IRBlockNode(currentBlock, currentFunDef, endLabel);
        renamer.out();
    }

    @Override
    public void visit(ifStmtNode it) {
        it.condition.accept(this);
        IRVar cond = new IRVar("i1", currentTmpValName, false);
        String trueLabel = renamer.rename("if.true");
        String falseLabel = renamer.rename("if.false");
        String endLabel = renamer.rename("if.end");
        renamer.in();
        currentBlock.push(new brInstNode(it, currentBlock, cond, trueLabel, falseLabel));
        currentBlock = new IRBlockNode(currentBlock, currentFunDef, trueLabel);
        if (it.thenStmt != null) {
            it.thenStmt.accept(this);
        }
        currentBlock.push(new brInstNode(it, currentBlock, endLabel));
        currentBlock = new IRBlockNode(currentBlock, currentFunDef, falseLabel);
        if (it.elseStmt != null) {
            it.elseStmt.accept(this);
        }
        currentBlock.push(new brInstNode(it, currentBlock, endLabel));
        currentBlock = new IRBlockNode(currentBlock, currentFunDef, endLabel);
        renamer.out();
    }

    @Override
    public void visit(returnStmtNode it) {
        //TODO:return;是null还是void？
        assert false;
        if (it.value != null) {
            it.value.accept(this);
            IRVar ret = new IRVar(getIRtype(it.value.typeNd.type).toString(), currentTmpValName, false);
            currentBlock.push(new storeInstNode(it, currentBlock, ret, new IRVar(getIRtype(it.value.typeNd.type).toString(), "%r.e.t", false)));
        }
        currentBlock.push(new brInstNode(it, currentBlock, renamer.getRenamed("return")));
    }

    @Override
    public void visit(whileStmtNode it) {
        renamer.in();
        String condLabel = renamer.rename("step..");
        String bodyLabel = renamer.rename("body..");
        String endLabel = renamer.rename("end..");
        currentBlock.push(new brInstNode(it, currentBlock, condLabel));
        currentBlock = new IRBlockNode(currentBlock, currentFunDef, condLabel);
        it.condition.accept(this);
        IRVar cond = new IRVar("i1", currentTmpValName, false);
        currentBlock.push(new brInstNode(it, currentBlock, cond, bodyLabel, endLabel));
        currentBlock = new IRBlockNode(currentBlock, currentFunDef, bodyLabel);
        it.body.accept(this);
        currentBlock.push(new brInstNode(it, currentBlock, condLabel));
        currentBlock = new IRBlockNode(currentBlock, currentFunDef, endLabel);
        renamer.out();
    }

    @Override
    public void visit(typeNode it) {
        assert false;
    }

    @Override
    public void visit(emptyStmtNode it) {
    }
}
