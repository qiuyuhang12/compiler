package AST.expr;

import AST.*;
import AST.def.*;
import AST.stmt.*;
import Util.Type;
import Util.position;
import java.util.ArrayList;
public class formatStringExprNode extends ExprNode {
    //str0 expr0 str1 expr1 str2 expr2 ...
    public ArrayList<String> strings = new ArrayList<>();
    public ArrayList<ExprNode> exprs = new ArrayList<>();
    formatStringExprNode(position pos) {
        super(pos);
    }
    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
