package AST.stmt;
import AST.*;
import AST.expr.ExprNode;
import Util.position;

public class exprStmtNode extends StmtNode{
    public ExprNode expr;

    public exprStmtNode(ExprNode expr, position pos) {
        super(pos);
        this.expr = expr;
    }
    public exprStmtNode(position pos) {
        super(pos);
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
