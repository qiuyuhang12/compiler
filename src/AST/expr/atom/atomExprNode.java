package AST.expr.atom;

import AST.expr.*;
import Util.position;

public abstract class atomExprNode extends ExprNode {
    public enum atomType {
        CONST, THIS, IDENTIFIER
    }

    public atomType atTypeNd;

    public atomExprNode(position pos) {
        super(pos);
    }
}
