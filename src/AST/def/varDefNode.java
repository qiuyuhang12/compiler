package AST.def;

import AST.*;
import AST.expr.*;
import Util.position;

import java.util.ArrayList;

public class varDefNode extends ASTNode {
    public ArrayList<varDefUnitNode> units = null;
    public typeNode typeNd = null;
    public ExprNode init = null;

    public varDefNode(position pos) {
        super(pos);
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
