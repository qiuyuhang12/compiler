package AST.expr;
import AST.*;
import AST.def.*;
import AST.stmt.*;
import Util.Type;
import Util.position;
public class arrayExprNode extends ExprNode {
    public ExprNode array;
    public ExprNode index;
    public arrayExprNode(position pos) {
        super(pos);
    }
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
