package Frontend.IR.node.inst;

import AST.expr.ExprNode;
import Frontend.IR.node.IRNode;

public abstract class instNode extends IRNode {
    public ExprNode src;//for debug
    public instNode(ExprNode _src){
        src = _src;
    }
}
