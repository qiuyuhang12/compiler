package AST.expr;

import AST.*;
import Util.Type;
import Util.position;
import java.util.ArrayList;
public class formatStringExprNode extends ExprNode {
    //str0 expr0 str1 expr1 str2 expr2 ...strn
//    public String str;
    public ArrayList<String> strings = new ArrayList<>();
    public ArrayList<ExprNode> exprs = new ArrayList<>();
    public formatStringExprNode(position pos) {
        super(pos);
//        atTypeNd = atomExprNode.atomType.CONST;
        this.typeNd = new typeNode(pos);
        this.typeNd.type.atomType = Type.TypeEnum.STRING;
    }
    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
