package AST.expr.atom;

import AST.*;
import Util.*;

public class intNode extends atomExprNode{
    public int value;
    public intNode(position pos) {
        super(pos);
        atTypeNd = atomType.CONST;
        this.typeNd = new typeNode(pos);
        this.typeNd.type.type = Type.TypeEnum.INT;
    }
    public intNode(position pos, int value) {
        super(pos);
        atTypeNd = atomType.CONST;
        this.typeNd = new typeNode(pos);
        this.typeNd.type.type = Type.TypeEnum.INT;
        this.value = value;
    }
    @Override
public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
