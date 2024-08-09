package AST.expr;

import AST.*;
import AST.expr.atom.atomExprNode;
import Util.Type;
import Util.position;
import java.util.ArrayList;
public class formatStringExprNode extends ExprNode {
    //str0 expr0 str1 expr1 str2 expr2 ...
//    public String str;
    public ArrayList<String> strings = new ArrayList<>();
    public ArrayList<ExprNode> exprs = new ArrayList<>();
    public formatStringExprNode(position pos) {
        super(pos);
//        atTypeNd = atomExprNode.atomType.CONST;
        this.typeNd = new typeNode(pos);
        this.typeNd.type.type = Type.TypeEnum.STRING;
    }
    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
