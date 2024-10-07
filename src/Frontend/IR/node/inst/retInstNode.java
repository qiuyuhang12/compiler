package Frontend.IR.node.inst;

import AST.ASTNode;
import Frontend.IR.entity.IREntity;
import Frontend.IR.entity.IRVar;
import Frontend.IR.node.stmt.IRBlockNode;

import java.util.HashMap;
import java.util.HashSet;

public class retInstNode extends instNode {
    public IREntity value;
    
    public retInstNode(ASTNode expr, IRBlockNode _parent) {
        super(expr, _parent);
        value = new IRVar();
    }
    
    public retInstNode(ASTNode expr, IRBlockNode _parent, IREntity value) {
        super(expr, _parent);
        this.value = value;
    }
    
    @Override
    public String toString() {
        if (value.typeInfo.toString().equals("void"))
            return "ret void\n";
        else
            return "ret " + value.typeInfo.toString() + " " + value.toString() + "\n";
    }
    
    @Override
    public void rename(HashMap<String, String> renameMap) {
        if (value == null) return;
        if (value instanceof IRVar var) {
            var.name = renameMap.getOrDefault(var.name, var.name);
        }
    }
    
    @Override
    public String getDef() {
        return "";
    }
    
    @Override
    public HashSet<String> getUses() {
        HashSet<String> ret = new HashSet<>();
        if (value instanceof IRVar var) {
            ret.add(var.name);
        }
        return ret;
    }
}
