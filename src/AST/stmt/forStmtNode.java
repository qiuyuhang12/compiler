package AST.stmt;

import AST.ASTVisitor;
import Util.position;

public class forStmtNode extends StmtNode {
    public StmtNode init, cond, step, body;

    public forStmtNode(position pos) {
        super(pos);
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
