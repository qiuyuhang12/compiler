package AST.expr;

import AST.*;
import Util.position;

public class preSelfExprNode extends ExprNode {
    public enum opType {
        Inc, Dec
    }
    public ExprNode expr;
    public opType op;
    public preSelfExprNode(position pos) {
        super(pos);
    }
    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
