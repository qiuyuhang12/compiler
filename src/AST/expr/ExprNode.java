package AST.expr;
import AST.*;
import AST.def.*;
import AST.stmt.*;
//import MIR.entity;
import Util.Type;
import Util.position;

public abstract class ExprNode extends ASTNode{
    public Type type;
//    public entity val;

    public ExprNode(position pos) {
        super(pos);
    }

    public boolean isAssignable() {
        return false;
    }
}
