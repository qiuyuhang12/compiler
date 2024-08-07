package AST;

import Util.position;
import AST.stmt.*;
import AST.expr.*;
import AST.def.*;

import java.util.ArrayList;

public class ProgramNode extends ASTNode {
    public funDefNode fn;
    public ArrayList<classDefNode> strDefs = new ArrayList<>();

    public ProgramNode(position pos, funDefNode fn) {
        super(pos);
        this.fn = fn;
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
