package Frontend.IR.node.inst;

import AST.expr.ExprNode;
import Frontend.IR.entity.IREntity;
import Frontend.IR.entity.IRVar;
import Frontend.IR.type.IRType;

public class brInstNode extends instNode {
    public IREntity cond;
    public String ifTrue, ifFalse, dest;
    public boolean isCondBr;

    public brInstNode(ExprNode expr, IREntity cond, String ifTrue, String ifFalse) {
        super(expr);
        assert cond.typeInfo.type == IRType.irTypeEnum.i1;
        this.cond = cond;
        this.ifTrue = ifTrue;
        this.ifFalse = ifFalse;
        isCondBr = true;
    }

    public brInstNode(ExprNode expr, String dest) {
        super(expr);
        this.dest = dest;
        isCondBr = false;
    }

    @Override
    public String toString() {
        if (isCondBr) {
            return "br i1 " + cond.toString() + ", label %" + ifTrue + ", label %" + ifFalse;
        } else {
            return "br label %" + dest;
        }
    }
}
