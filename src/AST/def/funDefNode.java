package AST;
import Util.Type;
import Util.position;

import java.util.ArrayList;

public class funDefNode extends ASTNode {
    public ArrayList<StmtNode> stmts;
    public Type intType, boolType;

    public funDefNode(position pos) {
        super(pos);
        stmts = new ArrayList<>();
        intType = new Type();
        boolType = new Type();
        intType.isInt = true;
        boolType.isBool = true;
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
