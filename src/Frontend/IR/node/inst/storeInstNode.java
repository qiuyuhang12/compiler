package Frontend.IR.node.inst;

import AST.ASTNode;
import Frontend.IR.entity.IREntity;
import Frontend.IR.entity.IRLiteral;
import Frontend.IR.entity.IRVar;
import Frontend.IR.node.stmt.IRBlockNode;

import java.util.HashMap;
import java.util.HashSet;

public class storeInstNode extends instNode {
    public IREntity value;
    public IRVar ptr;
    
    public storeInstNode(ASTNode expr, IRBlockNode _parent, IREntity value, IRVar ptr) {
        super(expr, _parent);
//        assert value instanceof IRLiteral||value.toString().charAt(0)== '%';
        this.value = value;
        this.ptr = ptr;
    }
//    public storeInstNode(ASTNode expr, IRBlockNode _parent, String value, IRVar ptr) {
//        super(expr, _parent);
//        this.value = new IRVar(ptr.typeInfo.toString(),value,ptr.isGlobal);
//        this.ptr = ptr;
//    }
    
    @Override
    public String toString() {
        return "store " + value.typeInfo.toString() + " " + value.toString() + ", ptr " + ptr.toString() + "\n";
    }
    
    @Override
    public void rename(HashMap<String, String> renameMap) {
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
        HashSet<String> res = new HashSet<>();
        if (value instanceof IRVar var) {
            res.add(var.name);
        }
        res.add(ptr.name);
        return res;
    }
}
