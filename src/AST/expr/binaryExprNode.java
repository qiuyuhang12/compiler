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
        leftSh, rightSh, and, or, xor, not,
        greater, less, grEq, lessEq, unEq, eq,
        andLg, orLg, notLg,
    }

    public binaryOpType opCode;

    public binaryExprNode(ExprNode lhs, ExprNode rhs, binaryOpType opCode, position pos) {
        super(pos);
        this.lhs = lhs;
        this.rhs = rhs;
        this.opCode = opCode;
    }

    public binaryExprNode(position pos) {
        super(pos);
    }

    @Override
public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
