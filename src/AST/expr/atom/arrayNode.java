package AST.expr.atom;

import AST.*;
import Util.*;

import java.util.ArrayList;

public class arrayNode extends atomExprNode {

    public int dim = 0;
    public ArrayList<atomExprNode> value;

    arrayNode(position pos) {
        super(pos);
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
