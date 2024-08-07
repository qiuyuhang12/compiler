package AST.expr.atom;

import AST.*;
import Util.*;

public class boolNode extends atomExprNode {
    public boolean value;
    public boolNode(position pos) {
        super(pos);
    }
    public boolNode(position pos, boolean value) {
        super(pos);
        this.value = value;
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
