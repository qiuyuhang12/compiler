package AST.def;

import AST.*;
import AST.stmt.*;
import Util.position;

import java.util.ArrayList;

public class classBuildNode extends ASTNode {
    public String name = null;
    public ArrayList<StmtNode> stmts=new ArrayList<>();

    public classBuildNode(position pos) {
        super(pos);
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
