package Frontend.IR.node.inst;

import AST.expr.ExprNode;
import Frontend.IR.entity.IREntity;
import Frontend.IR.entity.IRVar;

public class retInstNode extends instNode{
    public IREntity value;
    public retInstNode(ExprNode expr){
        super(expr);
        value = new IRVar();
    }
    public retInstNode(ExprNode expr,IREntity value){
        super(expr);
        this.value = value;
    }
    @Override
    public String toString() {
        return "ret "+value.typeInfo.toString()+value.toString();
    }
}
