package AST.stmt;

import AST.*;
import AST.expr.*;
import Util.position;

public class ifStmtNode extends StmtNode {
    public ExprNode condition;
    public StmtNode thenStmt, elseStmt;

    public ifStmtNode(ExprNode condition, StmtNode thenStmt, StmtNode elseStmt, position pos) {
        super(pos);
        this.condition = condition;
        this.thenStmt = thenStmt;
        this.elseStmt = elseStmt;
    }
    public ifStmtNode(position pos) {
        super(pos);
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
