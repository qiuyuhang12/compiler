package Frontend.IR.node.inst;

import AST.ASTNode;
import Frontend.IR.node.IRNode;
import Frontend.IR.node.stmt.IRBlockNode;

import java.util.HashMap;

public abstract class instNode extends IRNode {
    public IRBlockNode parent;
    public ASTNode src;//for debug
    public instNode(ASTNode _src, IRBlockNode _parent) {
        parent = _parent;
        src = _src;
    }
    @Override
    public abstract String toString();
    public String getVal(){
        return null;
    }
    public abstract void rename(HashMap<String, String> renameMap);
}
