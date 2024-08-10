package AST.expr.atom;

import AST.*;
import Util.*;

import java.net.URL;

public class identifierNode extends atomExprNode {
    public String name;

    public identifierNode(position pos) {
        super(pos);
        atTypeNd = atomType.IDENTIFIER;
        this.typeNd = new typeNode(pos);
//        this.typeNd.type.type = Type.TypeEnum.CLASS;
        //你有没有确定class的type类型（不能就是个class！）
        assert false;
    }

    public identifierNode(position pos, String _name) {
        super(pos);
        name = _name;
        atTypeNd = atomType.IDENTIFIER;
        this.typeNd = new typeNode(pos);
//        URL url = identifierNode.class.getProtectionDomain().getCodeSource().getLocation();
//        String path = url.getPath();
        String path = "src/AST/expr/atom/identifierNode.java";

        System.err.println("\nWARNING：\t你有没有确定identifier是class?basicType?fun?类型（不能就是个class！）。" +
                "\n\t\t\t程序所在路径: " + path);
//        this.typeNd.type.type = Type.TypeEnum.CLASS;
        //你有没有确定identifier是class?basicType?fun?类型（不能就是个class！）
//        assert false;
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
