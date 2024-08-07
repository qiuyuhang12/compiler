package AST.stmt;

import AST.ASTVisitor;
import AST.expr.ExprNode;
import Util.position;

public class whileStmtNode extends StmtNode {
    public StmtNode body;
    public ExprNode cond;

    public whileStmtNode(position pos) {
        super(pos);
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
