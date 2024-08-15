package Frontend.IR.node.inst;

import AST.expr.ExprNode;
import Frontend.IR.entity.IRVar;
import Frontend.IR.type.IRType;

public class allocaInstNode extends instNode{
    public IRVar dest;
    public IRType type;
    public allocaInstNode(ExprNode expr,IRVar dest,IRType type){
        super(expr);
        this.dest = dest;
        this.type = type;
    }
    @Override
    public String toString() {
        return dest.toString()+" = alloca "+type.toString();
    }
}
