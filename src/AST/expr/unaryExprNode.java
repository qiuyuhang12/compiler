package AST.expr;

import AST.*;
import Util.position;

public class unaryExprNode extends ExprNode {
    public enum unaryOpType {
        Inc, Dec, logNot, bitNot, minus, plus
    }
    public unaryOpType opCode;
    public ExprNode body;
    public unaryExprNode(position pos) {
        super(pos);
    }
    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
