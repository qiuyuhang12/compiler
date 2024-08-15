package Frontend.IR.node.inst;

import AST.expr.ExprNode;
import Frontend.IR.entity.IRVar;
import Frontend.IR.type.IRType;

public class loadInstNode extends instNode{
    public IRVar dest,ptr;
    public IRType type;
    public loadInstNode(ExprNode expr,IRVar dest, IRVar ptr, IRType type){
        super(expr);
        assert ptr.typeInfo.type==IRType.irTypeEnum.ptr;
        this.dest = dest;
        this.ptr = ptr;
        this.type = type;
    }

    @Override
    public String toString() {
        return dest.toString()+" = load "+type.toString()+", ptr "+ptr.toString();
    }
}
