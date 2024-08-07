package AST.expr.atom;

import AST.*;
import Util.*;

public class identifierNode extends atomExprNode {
    public String name;
    public identifierNode(position pos) {
        super(pos);
    }
    public identifierNode(position pos, String _name) {
        super(pos);
        name = _name;
    }
    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
