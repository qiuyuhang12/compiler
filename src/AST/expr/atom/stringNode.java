package AST.expr.atom;

import AST.*;
import Util.*;

public class stringNode extends atomExprNode{
    public stringNode(position pos, String _value) {
        super(pos);
        atTypeNd = atomType.CONST;
        this.typeNd = new typeNode(pos);
        this.typeNd.type.type = Type.TypeEnum.STRING;
        value = _value;
    }
    public stringNode(position pos) {
        super(pos);
        atTypeNd = atomType.CONST;
        this.typeNd = new typeNode(pos);
        this.typeNd.type.type = Type.TypeEnum.STRING;
    }
    public String value;

    @Override
public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
