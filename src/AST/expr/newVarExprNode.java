package AST.expr;

import AST.ASTVisitor;
import Util.position;

public class newVarExprNode extends ExprNode {
//    public String name;
    public ExprNode init;
    public newVarExprNode(position pos) {
        super(pos);
    }
    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
