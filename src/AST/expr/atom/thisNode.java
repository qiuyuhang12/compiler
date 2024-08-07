package AST.expr.atom;

import AST.*;
import Util.*;

public class thisNode extends atomExprNode{
    public thisNode(position pos) {
        super(pos);
    }
    @Override
public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
