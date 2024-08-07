package AST.expr;

import AST.*;
import Util.position;
import AST.expr.atom.arrayNode;
import java.util.ArrayList;

public class newArrayExprNode extends ExprNode {
    public String name;
    public int dim=0;
    public ArrayList<ExprNode> sizeInit = new ArrayList<>();
    public arrayNode init;
    public newArrayExprNode(position pos) {
        super(pos);
    }
    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
