package Frontend.IR.node.inst;

import AST.expr.ExprNode;
import Frontend.IR.entity.IREntity;
import Frontend.IR.entity.IRVar;

public class storeInstNode extends instNode{
    public IREntity value;
    public IRVar ptr;
    public storeInstNode(ExprNode expr,IREntity value,IRVar ptr){
        super(expr);
        this.value = value;
        this.ptr = ptr;
    }
    @Override
    public String toString() {
        return "store "+value.typeInfo.toString()+value.toString()+", "+ptr.typeInfo.toString()+ptr.toString();
    }
}
