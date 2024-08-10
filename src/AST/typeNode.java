package AST;
import Util.Type;
import Util.position;

public class typeNode extends ASTNode {
    public Type type=new Type();
    public typeNode(position pos) {
        super(pos);
    }
    @Override
    public boolean equals(Object node) {
        if (!(node instanceof typeNode)) return false;
        return type.equals(((typeNode) node).type);
    }
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
