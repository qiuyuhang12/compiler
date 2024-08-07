package AST.expr;

import AST.*;
import AST.def.*;
import AST.stmt.*;
import Util.Type;
import Util.position;

import java.util.ArrayList;

public class funExprNode extends ExprNode {
    public String name;
    public ArrayList<ExprNode> parameters = new ArrayList<>();
    public funExprNode(position pos) {
        super(pos);
    }
    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
