package Frontend.IR.node.inst;

import AST.ASTNode;
import AST.expr.binaryExprNode;
import Frontend.IR.entity.IREntity;
import Frontend.IR.entity.IRVar;
import Frontend.IR.node.stmt.IRBlockNode;
import Frontend.IR.type.IRType;

import java.util.HashMap;
import java.util.HashSet;

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
                //hh
//                System.err.println("Invalid icmp operation");
                System.exit(1);
                yield null;
            }
        };
        if (!lhs.typeInfo.type.equals(rhs.typeInfo.type)) {
            System.err.println("Invalid icmp operation");
            System.exit(1);
        }
        assert lhs.typeInfo.type.equals(rhs.typeInfo.type);
        this.lhs = lhs;
        this.rhs = rhs;
    }
    
    @Override
    public instNode copy(instNode other) {
        return new icmpInstNode(this);
    }
    
    @Override
    public String toString() {
        return dest.toString() + " = icmp " + op.toString() + " " + lhs.typeInfo.toString() + " " + lhs.toString() + ", " + rhs.toString() + "\n";
    }
    
    @Override
    public String getVal() {
        return dest.toString();
    }
    
    @Override
    public void rename(HashMap<String, String> renameMap) {
        if (lhs instanceof IRVar var) {
            var.name = renameMap.getOrDefault(var.name, var.name);
        }
        if (rhs instanceof IRVar var) {
            var.name = renameMap.getOrDefault(var.name, var.name);
        }
    }
    
    
    @Override
    public String getDef() {
        return dest.name;
    }
    public icmpInstNode(icmpInstNode other) {
        super(other);
        this.dest = other.dest;
        this.lhs = other.lhs;
        this.rhs = other.rhs;
        this.op = other.op;
    }
    
    @Override
    public HashSet<String> getUses() {
        HashSet<String> ret = new HashSet<>();
        if (lhs instanceof IRVar var && var.name.charAt(0) == '%') {
            ret.add(var.name);
        }
        if (rhs instanceof IRVar var && var.name.charAt(0) == '%') {
            ret.add(var.name);
        }
        return ret;
    }
}
