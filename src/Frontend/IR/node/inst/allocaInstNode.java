package Frontend.IR.node.inst;

import AST.ASTNode;
import AST.expr.ExprNode;
import Frontend.IR.entity.IRVar;
import Frontend.IR.node.stmt.IRBlockNode;
import Frontend.IR.type.IRType;

import java.util.HashMap;
import java.util.HashSet;

public class allocaInstNode extends instNode{
    public IRVar dest;
    public IRType type;
    public allocaInstNode(ASTNode expr, IRBlockNode _parent, IRVar dest, IRType type){
        super(expr, _parent);
        assert dest.name.charAt(0) == '%';
        this.dest = dest;
        this.type = type;
    }
    public allocaInstNode(allocaInstNode other){
        super(other);
        this.dest = other.dest;
        this.type = other.type;
    }
//    @Override
//    public instNode copy(instNode other) {
//        return new allocaInstNode(this);
//    }
    
    @Override
    public instNode copy(instNode other) {
        return new allocaInstNode(this);
    }
    
    @Override
    public String toString() {
//        assert false;
        return dest.toString()+" = alloca "+type.toString() + "\n";
    }
    
    @Override
    public void rename(HashMap<String, String> renameMap) {
        return;
    }
    
    @Override
    public String getDef() {
        return null;
    }
    
    @Override
    public HashSet<String> getUses() {
        return null;
    }
}
