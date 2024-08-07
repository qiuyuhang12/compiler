package AST.expr;

import AST.*;
import AST.def.*;
import AST.stmt.*;
import Util.Type;
import Util.position;

public class memberExprNode extends ExprNode {
    public ExprNode object, member;
    public memberExprNode(position pos) {
        super(pos);
    }
    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
