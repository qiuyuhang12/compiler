package AST.expr.atom;

import AST.*;
import Util.*;

public class nullNode extends atomExprNode{
    public nullNode(position pos) {
        super(pos);
    }
    @Override
public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
