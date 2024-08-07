package AST.expr;
import AST.*;
import AST.def.*;
import AST.stmt.*;
import Util.Type;
import Util.position;

public class binaryExprNode extends ExprNode {
    public ExprNode lhs, rhs;
    public enum binaryOpType {
        add, sub, mul, div, mod,
        shl, shr, and, or, xor, lt,
        gt, le, ge, eq, ne, andl, orl
    }
    public binaryOpType opCode;

    public binaryExprNode(ExprNode lhs, ExprNode rhs, binaryOpType opCode, Type intType, position pos) {
        super(pos);
        this.lhs = lhs;
        this.rhs = rhs;
        this.opCode = opCode;
        type = intType;
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
