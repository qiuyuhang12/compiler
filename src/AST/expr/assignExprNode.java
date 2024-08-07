package AST.expr;

import AST.*;
import AST.def.*;
import AST.stmt.*;
import Util.position;

public class assignExprNode extends ExprNode {
    public ExprNode lhs, rhs;

    public assignExprNode(position pos) {
        super(pos);
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
