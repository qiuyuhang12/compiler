package AST.stmt;

import AST.ASTVisitor;
import AST.expr.ExprNode;
import Util.position;

public class forStmtNode extends StmtNode {
    public StmtNode init, body;
    public ExprNode cond, step;

    public forStmtNode(position pos) {
        super(pos);
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
