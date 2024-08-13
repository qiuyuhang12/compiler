package AST.expr;
import AST.*;
//import MIR.entity;
import Util.position;

public abstract class ExprNode extends ASTNode{
    public typeNode typeNd;
    public boolean isLeftValue = false;
//    public Type type;
//    public entity val;

    public ExprNode(position pos) {
        super(pos);
    }
}
