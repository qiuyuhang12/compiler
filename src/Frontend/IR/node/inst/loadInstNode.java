package Frontend.IR.node.inst;

import AST.ASTNode;
import Frontend.IR.entity.IRVar;
import Frontend.IR.node.stmt.IRBlockNode;
import Frontend.IR.type.IRType;

public class loadInstNode extends instNode{
    public String dest;
    public String ptr;
    public IRType type;
    public loadInstNode(ASTNode expr, IRBlockNode _parent, String dest, String ptr, IRType type){
        super(expr, _parent);
        this.dest = dest;
        this.ptr = ptr;
        this.type = type;
    }

    @Override
    public String toString() {
        return dest+" = load "+type.toString()+", ptr "+ptr + "\n";
    }
}
