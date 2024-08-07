package AST.expr.atom;

import AST.*;
import Util.*;

public class stringNode extends atomExprNode{
    public stringNode(position pos, String _value) {
        super(pos);
        value = _value;
    }
    public stringNode(position pos) {
        super(pos);
    }
    public String value;

    @Override
public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
