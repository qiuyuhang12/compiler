package Frontend.IR.node.inst;

import AST.ASTNode;
import Frontend.IR.node.IRNode;
import Frontend.IR.node.stmt.IRBlockNode;

import java.util.HashMap;
import java.util.HashSet;

public abstract class instNode extends IRNode {
    public IRBlockNode parent;
    public ASTNode src;//for debug
    public instNode(ASTNode _src, IRBlockNode _parent) {
        parent = _parent;
        src = _src;
    }
    public instNode(instNode other){
        this.parent = other.parent;
        this.src = other.src;
        this.live_out = new HashSet<>(other.live_out);
        this.lo_after_sp = new HashSet<>(other.lo_after_sp);
    }
    public abstract instNode copy(instNode other);
    @Override
    public abstract String toString();
    public String getVal(){
        return null;
    }
    public abstract void rename(HashMap<String, String> renameMap);
//    public HashSet<String> live_in = new HashSet<>();
    public HashSet<String> live_out = new HashSet<>();
    public HashSet<String> lo_after_sp = new HashSet<>();
    public abstract String getDef();
    public abstract HashSet<String> getUses();
    public boolean useless=true;
}
