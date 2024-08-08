package AST.def;

import AST.*;
import AST.stmt.*;
import Util.position;

import java.util.ArrayList;

public class funDefNode extends ASTNode {
    public String name = null;
    public funParaList paraList;
    public ArrayList<StmtNode> stmts = new ArrayList<>();
    public typeNode typeNd = null;

    public funDefNode(position pos) {
        super(pos);
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
