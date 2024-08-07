package AST.expr;
import AST.*;
import Util.Type;
import Util.position;

public class zzzconstExprNode extends ExprNode {
    public int value;

    public zzzconstExprNode(int value, Type intType, position pos) {
        super(pos);
        this.value = value;
        type = intType;
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
