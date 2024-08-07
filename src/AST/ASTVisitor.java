package AST;
import AST.stmt.*;
import AST.expr.*;
import AST.def.*;
public interface ASTVisitor {
    void visit(ProgramNode it);
    void visit(classDefNode it);
    void visit(funDefNode it);

    void visit(varDefStmtNode it);
    void visit(returnStmtNode it);
    void visit(blockStmtNode it);
    void visit(exprStmtNode it);
    void visit(ifStmtNode it);

    void visit(assignExprNode it);
    void visit(binaryExprNode it);
    void visit(zzzconstExprNode it);
    void visit(zzzvarExprNode it);
    void visit(varDefNode it);
}
