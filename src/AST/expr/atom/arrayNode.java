package AST.expr.atom;

import AST.*;
import Util.*;

import java.util.ArrayList;

public class arrayNode extends atomExprNode {

//    public int dim = 0;
    public ArrayList<atomExprNode> value=new ArrayList<>();

    public arrayNode(position pos) {
        super(pos);
        atTypeNd = atomType.CONST;
        this.typeNd=new typeNode(pos);
//        this.typeNd.type.type= Type.TypeEnum.ARRAY;
        this.typeNd.type.isArray= true;
        //你有没有确定array的type类型（不能就是个Array！），还有dim
//        assert false;
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
