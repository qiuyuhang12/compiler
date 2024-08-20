package Frontend.IR.node.inst;

import AST.ASTNode;
import Frontend.IR.entity.IREntity;
import Frontend.IR.entity.IRVar;
import Frontend.IR.node.stmt.IRBlockNode;

public class retInstNode extends instNode {
    public IREntity value;

    public retInstNode(ASTNode expr, IRBlockNode _parent) {
        super(expr, _parent);
        value = new IRVar();
    }

    public retInstNode(ASTNode expr, IRBlockNode _parent, IREntity value) {
        super(expr, _parent);
        this.value = value;
    }

    @Override
    public String toString() {
        return "ret " + value.typeInfo.toString() + value.toString() + "\n";
    }
}
