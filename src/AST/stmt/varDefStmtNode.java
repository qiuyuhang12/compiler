package AST.stmt;

import AST.*;
import AST.def.*;
import AST.expr.*;

import Util.Type;
import Util.position;

import java.util.ArrayList;

public class varDefStmtNode extends StmtNode {
    public Type type;
    public ArrayList<String> name;

    public ArrayList<ExprNode> init;

    public varDefStmtNode(position pos) {
        super(pos);
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
