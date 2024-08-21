package Frontend.IR.node.inst;

import AST.ASTNode;
import AST.expr.ExprNode;
import AST.expr.binaryExprNode;
import Frontend.IR.entity.IREntity;
import Frontend.IR.entity.IRVar;
import Frontend.IR.node.stmt.IRBlockNode;

public class binaryInstNode extends instNode {
    public enum opEnum {
        add,
        sub,
        mul,
        sdiv,
        srem,
        shl,
        ashr,
        and,
        or,
        xor,
    }

    public String opToString() {
        return switch (op) {
            case add -> "add";
            case sub -> "sub";
            case mul -> "mul";
            case sdiv -> "sdiv";
            case srem -> "srem";
            case shl -> "shl";
            case ashr -> "ashr";
            case and -> "and";
            case or -> "or";
            case xor -> "xor";
        };
    }

    public binaryInstNode(ASTNode expr, IRBlockNode _parent, IRVar dest, binaryExprNode.binaryOpType op, IREntity lhs, IREntity rhs) {
        super(expr, _parent);
        this.dest = dest;
        this.op = switch (op) {
            case add -> opEnum.add;
            case sub -> opEnum.sub;
            case mul -> opEnum.mul;
            case div -> opEnum.sdiv;
            case mod -> opEnum.srem;
            case leftSh -> opEnum.shl;
            case rightSh -> opEnum.ashr;
            case and -> opEnum.and;
            case or -> opEnum.or;
            case xor -> opEnum.xor;
            default -> {
                System.err.println("Invalid binary operation");
                System.exit(1);
                yield null;
            }
        };
        this.lhs = lhs;
        this.rhs = rhs;
    }

    public opEnum op;
    public IREntity lhs, rhs;
    public IRVar dest;

    @Override
    public String toString() {
        return dest.toString() + " = i32 " + opToString() + " " + lhs.toString() + ", " + rhs.toString() + "\n";
    }
}