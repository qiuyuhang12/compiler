package AST.def;

import AST.*;
import AST.expr.*;
import AST.stmt.StmtNode;
import Util.position;

import java.util.ArrayList;

public class varDefNode extends StmtNode {
    public ArrayList<varDefUnitNode> units = new ArrayList<>();
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
