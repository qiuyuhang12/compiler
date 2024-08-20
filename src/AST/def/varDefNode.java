package AST.def;

import AST.*;
import AST.expr.*;
import AST.stmt.StmtNode;
import Util.position;

import java.util.ArrayList;

public class varDefNode extends StmtNode {
    public ArrayList<varDefUnitNode> units = new ArrayList<>();
    public typeNode typeNd = null;
    public boolean isStmt=true;//true: varDefStmt, false: varDefPara,globalVarDef, classVarDef
    public varDefNode(position pos) {
        super(pos);
    }

    @Override
    public void accept(ASTVisitor visitor) {
        //TODO:修改stmt
        assert false;
        if (isStmt) {
            System.err.println("WARNING:你有没有考虑classVarDef");
        }
        visitor.visit(this);
    }
}
