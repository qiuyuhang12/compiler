package Frontend.IR.node.inst;

import AST.ASTNode;
import Frontend.IR.entity.IREntity;
import Frontend.IR.entity.IRVar;
import Frontend.IR.node.stmt.IRBlockNode;
import Frontend.IR.type.IRType;

import java.util.HashMap;
import java.util.HashSet;

public class selectInstNode extends instNode {
    public IRVar dest;
    public IREntity cond, trueVal, falseVal;
    public selectInstNode(selectInstNode other) {
        super(other);
        this.dest = other.dest;
        this.cond = other.cond;
        this.trueVal = other.trueVal;
        this.falseVal = other.falseVal;
    }
    public selectInstNode(ASTNode expr, IRBlockNode _parent, IRVar dest, IREntity cond, IREntity trueVal, IREntity falseVal) {
        super(expr, _parent);
        assert false;
        this.dest = dest;
        assert cond.typeInfo.type == IRType.IRTypeEnum.i1;
        this.cond = cond;
        this.trueVal = trueVal;
        this.falseVal = falseVal;
    }
    
    @Override
    public instNode copy(instNode other) {
        return new selectInstNode(this);
    }
    
    @Override
    public String toString() {
        assert false;
        return dest.toString() + " = select i1 " + cond.toString() + ", " + trueVal.typeInfo.toString() + trueVal.toString() + ", " + falseVal.typeInfo.toString() + falseVal.toString() + "\n";
    }
    
    @Override
    public void rename(HashMap<String, String> renameMap) {
        assert false;
    }
    
    @Override
    public String getDef() {
        assert false;
        return null;
    }
    
    @Override
    public HashSet<String> getUses() {
        assert false;
        return null;
    }
    
    
}
