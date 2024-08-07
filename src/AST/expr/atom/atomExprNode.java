package AST.expr.atom;

import AST.*;
import AST.expr.*;
import Util.position;

public abstract class atomExprNode extends ExprNode{
    enum atomType {
        CONST, THIS, IDENTIFIER
    }
    public atomExprNode(position pos) {
        super(pos);
    }
}
