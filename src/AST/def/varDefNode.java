package AST.def;

import AST.*;
import AST.expr.*;
import AST.stmt.*;
import Util.position;

public class varDefNode extends StmtNode {
    public String name = null, typeName = null;
    public ExprNode init = null;

    public varDefNode(position pos) {
        super(pos);
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
