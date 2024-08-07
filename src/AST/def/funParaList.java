package AST.def;

import AST.*;
import AST.stmt.*;
import Util.Type;
import Util.position;

import java.util.ArrayList;

public class funParaList extends ASTNode {
    public ArrayList<varDefNode> paraList = new ArrayList<>();

    public funParaList(position pos) {
        super(pos);
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
