package AST.expr;

import AST.*;
import Util.position;
import AST.expr.atom.identifierNode;

public class memberExprNode extends ExprNode {
    public ExprNode object;
    public identifierNode member;
    public memberExprNode(position pos) {
        super(pos);
    }
    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
