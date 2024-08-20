package AST;
import AST.expr.atom.*;
import AST.stmt.*;
import AST.expr.*;
import AST.def.*;
public interface ASTVisitor {
    void visit(ProgramNode it);

    //def
    void visit(classBuildNode it);
    void visit(classDefNode it);
    void visit(funDefNode it);
    void visit(varDefNode it);
    void visit(funParaList it);
    void visit(varDefUnitNode it);

    //expr.atom
    void visit(arrayNode it);
//    void visit(atomExprNode it);
    void visit(boolNode it);
    void visit(identifierNode it);
    void visit(intNode it);
    void visit(nullNode it);
    void visit(stringNode it);
    void visit(thisNode it);

    //expr
    void visit(arrayExprNode it);
    void visit(assignExprNode it);
    void visit(binaryExprNode it);
    void visit(conditionalExprNode it);
    void visit(formatStringExprNode it);
    void visit(funExprNode it);
    void visit(memberExprNode it);
    void visit(newArrayExprNode it);
    void visit(newVarExprNode it);
    void visit(preSelfExprNode it);
    void visit(unaryExprNode it);

    //stmt
    void visit(blockStmtNode it);
    void visit(breakStmtNode it);
    void visit(continueStmtNode it);
    void visit(exprStmtNode it);
    void visit(forStmtNode it);
    void visit(ifStmtNode it);
    void visit(returnStmtNode it);
    void visit(whileStmtNode it);

    void visit(typeNode it);

    void visit(emptyStmtNode it);
}
