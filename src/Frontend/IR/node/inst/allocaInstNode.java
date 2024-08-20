package Frontend.IR.node.inst;

import AST.ASTNode;
import AST.expr.ExprNode;
import Frontend.IR.entity.IRVar;
import Frontend.IR.node.stmt.IRBlockNode;
import Frontend.IR.type.IRType;

public class allocaInstNode extends instNode{
    public IRVar dest;
    public IRType type;
    public allocaInstNode(ASTNode expr, IRBlockNode _parent, IRVar dest, IRType type){
        super(expr, _parent);
        this.dest = dest;
        this.type = type;
    }
    @Override
    public String toString() {
        return dest.toString()+" = alloca "+type.toString() + "\n";
    }
}
