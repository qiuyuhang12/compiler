package Frontend.IR.node.inst;

import AST.ASTNode;
import AST.expr.binaryExprNode;
import Frontend.IR.entity.IREntity;
import Frontend.IR.entity.IRVar;
import Frontend.IR.node.stmt.IRBlockNode;
import Frontend.IR.type.IRType;

public class icmpInstNode extends instNode {
    public IRVar dest;
    public IREntity lhs, rhs;

    public enum opEnum {
        eq,
        ne,
        sgt,
        sge,
        slt,
        sle
    }

    public opEnum op;

    public icmpInstNode(ASTNode expr, IRBlockNode _parent, IRVar dest, binaryExprNode.binaryOpType op, IREntity lhs, IREntity rhs) {
        super(expr, _parent);
        assert dest.typeInfo.type == IRType.IRTypeEnum.i1;
        this.dest = dest;
        this.op = switch (op) {
            case eq -> opEnum.eq;
            case unEq -> opEnum.ne;
            case greater -> opEnum.sgt;
            case grEq -> opEnum.sge;
            case less -> opEnum.slt;
            case lessEq -> opEnum.sle;
            default -> {
                System.err.println("Invalid icmp operation");
                System.exit(1);
                yield null;
            }
        };
        assert lhs.typeInfo.type.equals(rhs.typeInfo.type);
        this.lhs = lhs;
        this.rhs = rhs;
    }

    @Override
    public String toString() {
        return dest.toString() + " = icmp " + op.toString() + " " + lhs.typeInfo.toString() + " " + lhs.toString() + ", " + rhs.toString();
    }
}
