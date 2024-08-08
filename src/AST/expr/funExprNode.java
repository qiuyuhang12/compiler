package AST.expr;

import AST.*;
import Util.position;

import java.util.ArrayList;

public class funExprNode extends ExprNode {
    public ExprNode fun;
    public ArrayList<ExprNode> paras = new ArrayList<>();
    public funExprNode(position pos) {
        super(pos);
    }
    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
