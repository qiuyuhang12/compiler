package AST.expr.atom;

import AST.*;
import Util.*;

public class nullNode extends atomExprNode{
    public nullNode(position pos) {
        super(pos);
        atTypeNd = atomType.CONST;
        this.typeNd = new typeNode(pos);
        this.typeNd.type.type = Type.TypeEnum.NULL;
        //为什么会出现？？？
        assert false;
    }
    @Override
public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
