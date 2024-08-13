package AST.expr.atom;

import AST.*;
import Util.*;

public class thisNode extends atomExprNode{
    public thisNode(position pos) {
        super(pos);
        atTypeNd = atomType.THIS;
        this.typeNd = new typeNode(pos);
        this.typeNd.type.atomType = Type.TypeEnum.CLASS;
        //你有没有确定class的type类型（不能就是个class！）
//        assert false;
    }
    @Override
public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
