package Frontend.IR.node.inst;

import AST.ASTNode;
import Frontend.IR.entity.IREntity;
import Frontend.IR.entity.IRVar;
import Frontend.IR.node.stmt.IRBlockNode;
import Frontend.IR.type.IRType;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class brInstNode extends instNode {
    public IREntity cond = null;
    public String ifTrue, ifFalse, dest;
    //br i1 %cond, label %ifTrue, label %ifFalse ; Conditional branch
    //br label %dest ; Unconditional branch
    public boolean isCondBr;
    
    public brInstNode(ASTNode expr, IRBlockNode _parent, IREntity cond, String ifTrue, String ifFalse) {
        super(expr, _parent);
        assert cond.typeInfo.type == IRType.IRTypeEnum.i1;
        assert ifTrue.charAt(0) != '%' && ifFalse.charAt(0) != '%';
        this.cond = cond;
        this.ifTrue = ifTrue;
        this.ifFalse = ifFalse;
        isCondBr = true;
    }
    
    public brInstNode(ASTNode expr, IRBlockNode _parent, String dest) {
        super(expr, _parent);
        assert dest.charAt(0) != '%';
        this.dest = dest;
        isCondBr = false;
    }
    
    @Override
    public String toString() {
        if (isCondBr) {
            return "br i1 " + cond.toString() + ", label %" + ifTrue + ", label %" + ifFalse + "\n";
        } else {
            return "br label %" + dest + "\n";
        }
    }
    
    @Override
    public void rename(HashMap<String, String> renameMap) {
        if (isCondBr) {
            if (cond instanceof IRVar var) {
                var.name = renameMap.getOrDefault(var.name, var.name);
            }
        }
    }
    
    @Override
    public String getDef() {
        return null;
    }
    
    @Override
    public HashSet<String> getUses() {
        if (isCondBr && cond instanceof IRVar var && var.name.charAt(0) == '%') {
            return new HashSet<>(Set.of(var.name));
        } else {
            return null;
        }
    }
}
