package AST.expr;

import AST.*;
import Util.position;

public class arrayExprNode extends ExprNode {
    public ExprNode array;
    public ExprNode index;

    public arrayExprNode(position pos) {
        super(pos);
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
