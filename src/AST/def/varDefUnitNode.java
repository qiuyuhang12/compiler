package AST.def;

import AST.*;
import AST.expr.ExprNode;
import Util.position;

public class varDefUnitNode extends ASTNode {
    public String name = null;
    public ExprNode init = null;
    public varDefUnitNode(position pos) {
        super(pos);
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
