package Frontend;

import AST.ASTNode;
import AST.ASTVisitor;
import AST.ProgramNode;
import AST.def.*;
import AST.expr.*;
import AST.expr.atom.*;
import AST.stmt.*;
import AST.typeNode;
import Util.Class_;
import Util.Function;
import Util.Scope;
import Util.error.semanticError;


//只调用了add,只能查函数重名，类成员重名，（不能查函数参数重名
public class SymbolCollector implements ASTVisitor {
    Scope gScope;
    Function currentFunction;
    Class_ currentClass;

    public SymbolCollector(Scope gScope) {
        this.gScope = gScope;
    }

    @Override
    public void visit(ProgramNode it) {
        for (ASTNode node : it.Defs) {
            assert node instanceof classDefNode || node instanceof varDefNode || node instanceof funDefNode;
            if (node instanceof classDefNode cdn) {
                currentClass = new Class_();
                visit(cdn);
                gScope.addClass(cdn.name, currentClass, cdn.pos);
            }
        }
        for (ASTNode node : it.Defs) {
            assert node instanceof classDefNode || node instanceof varDefNode || node instanceof funDefNode;
            if (node instanceof funDefNode fdn) {
                currentFunction = new Function();
                visit(fdn);
                gScope.addFun(fdn.name, currentFunction, fdn.pos);
            }
        }
    }

    @Override
    public void visit(funDefNode it) {
        currentFunction.name = it.name;
        currentFunction.returnType = it.typeNd.type;
        if (it.paraList != null)
            for (varDefNode nd : it.paraList.paraList) {
                currentFunction.paramTypes.add(nd.typeNd.type);
                assert nd.units.size() == 1;
            }
    }

    @Override
    public void visit(classDefNode it) {
        currentClass.name = it.name;
        for (varDefNode nd : it.varDefs) {
//            assert nd.units.size() == 1;
//            assert (nd.units.getFirst().init == null);
            for (varDefUnitNode unit : nd.units) {
                if (unit.init != null){
                    throw new semanticError("Class var init has init value", nd.pos);
                }
//                currentClass.vars.put(unit.name, nd.typeNd.type);
                currentClass.addVar(unit.name, nd.typeNd.type, nd.pos);
            }
        }
        for (funDefNode nd : it.funDefs) {
            currentFunction = new Function();
            visit(nd);
//            currentClass.functions.put(nd.name, currentFunction);
            currentClass.addFun(nd.name, currentFunction, nd.pos);
        }
    }

    @Override
    public void visit(classBuildNode it) {
    }

    @Override
    public void visit(varDefNode it) {
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

    @Override
    public void visit(atomExprNode it) {
    }

    @Override
    public void visit(boolNode it) {
    }

    @Override
    public void visit(identifierNode it) {
    }

    @Override
    public void visit(intNode it) {
    }

    @Override
    public void visit(nullNode it) {
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
    }

    @Override
    public void visit(binaryExprNode it) {
    }

    @Override
    public void visit(conditionalExprNode it) {
    }

    @Override
    public void visit(formatStringExprNode it) {
    }

    @Override
    public void visit(funExprNode it) {
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
    }

    @Override
    public void visit(unaryExprNode it) {
    }

    @Override
    public void visit(blockStmtNode it) {
    }

    @Override
    public void visit(breakStmtNode it) {
    }

    @Override
    public void visit(continueStmtNode it) {
    }

    @Override
    public void visit(exprStmtNode it) {
    }

    @Override
    public void visit(forStmtNode it) {
    }

    @Override
    public void visit(ifStmtNode it) {
    }

    @Override
    public void visit(returnStmtNode it) {
    }

    @Override
    public void visit(whileStmtNode it) {
    }

    @Override
    public void visit(typeNode it) {
    }

    @Override
    public void visit(emptyStmtNode it) {
    }
}
