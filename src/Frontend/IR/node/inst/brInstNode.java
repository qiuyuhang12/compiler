package Frontend.IR.node.inst;

import AST.ASTNode;
import Frontend.IR.entity.IREntity;
import Frontend.IR.node.stmt.IRBlockNode;
import Frontend.IR.type.IRType;

public class brInstNode extends instNode {
    public IREntity cond;
    public String ifTrue, ifFalse, dest;
    public boolean isCondBr;

    public brInstNode(ASTNode expr, IRBlockNode _parent, IREntity cond, String ifTrue, String ifFalse) {
        super(expr,_parent);
        assert cond.typeInfo.type == IRType.IRTypeEnum.i1;
        this.cond = cond;
        this.ifTrue = ifTrue;
        this.ifFalse = ifFalse;
        isCondBr = true;
    }

    public brInstNode(ASTNode expr, IRBlockNode _parent, String dest) {
        super(expr, _parent);
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
