package AST.expr;

import AST.*;
import AST.def.*;
import AST.stmt.*;
import Util.Type;
import Util.position;

public class conditionalExprNode extends ExprNode {
    public ExprNode condition, trueExpr, falseExpr;

    public conditionalExprNode(position pos) {
        super(pos);
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
