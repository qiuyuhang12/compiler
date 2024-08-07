package AST.def;

import AST.*;
import AST.expr.*;
import AST.stmt.*;
import AST.*;
import Util.position;

import java.util.ArrayList;

public class classDefNode extends ASTNode {
    public String name;
    public ArrayList<varDefNode> varDefs = new ArrayList<>();
    public ArrayList<funDefNode> funDefs = new ArrayList<>();

    public classDefNode(position pos) {
        super(pos);
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
