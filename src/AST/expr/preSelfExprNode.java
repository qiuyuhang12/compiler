package AST.expr;

import AST.*;
import Util.position;

public class preSelfExprNode extends ExprNode {//++a,自增，不添加新的变量
    public enum opType {
        Inc, Dec
    }
    public ExprNode body;
    public opType op;
    public preSelfExprNode(position pos) {
        super(pos);
    }
    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
