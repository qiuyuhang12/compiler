package AST;

import Util.position;

import java.util.ArrayList;

public class ProgramNode extends ASTNode {
//    public ArrayList<classDefNode> claDefs = new ArrayList<>();
//    public ArrayList<varDefNode> varDefs = new ArrayList<>();
//    public ArrayList<funDefNode> funDefs = new ArrayList<>();
    public ArrayList<ASTNode> Defs = new ArrayList<>();

    public ProgramNode(position pos) {
        super(pos);
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
