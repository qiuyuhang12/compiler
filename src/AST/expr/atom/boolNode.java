package AST.expr.atom;

import AST.*;
import Util.*;

public class boolNode extends atomExprNode {
    public boolean value;

    public boolNode(position pos) {
        super(pos);
        atTypeNd = atomType.CONST;
        this.typeNd = new typeNode(pos);
        this.typeNd.type.atomType = Type.TypeEnum.BOOL;
    }

    public boolNode(position pos, boolean value) {
        super(pos);
        this.typeNd = new typeNode(pos);
        atTypeNd = atomType.CONST;
        this.typeNd.type.atomType = Type.TypeEnum.BOOL;
        this.value = value;
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
