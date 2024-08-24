package Frontend.IR.node.inst;

import AST.ASTNode;
import AST.expr.ExprNode;
import Frontend.IR.node.IRNode;
import Frontend.IR.node.stmt.IRBlockNode;

public abstract class instNode extends IRNode {
    public IRBlockNode parent;
    public ASTNode src;//for debug
    public instNode(ASTNode _src, IRBlockNode _parent) {
        parent = _parent;
        src = _src;
    }
    @Override
    public abstract String toString();
}
