package AST.expr.atom;

import AST.*;
import Util.*;
import java.util.List;
public class arrayNode extends atomExprNode {
    public int dim = 0;
    public List<arrayNode> value;
    arrayNode(position pos) {
        super(pos);
    }
    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
