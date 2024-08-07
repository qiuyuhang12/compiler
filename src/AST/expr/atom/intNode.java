package AST.expr.atom;

import AST.*;
import Util.*;

public class intNode extends atomExprNode{
    public int value;
    public intNode(position pos) {
        super(pos);
    }
    public intNode(position pos, int value) {
        super(pos);
        this.value = value;
    }
    @Override
public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
