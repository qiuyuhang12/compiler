package AST.stmt;
import AST.*;

import Util.position;

public abstract class StmtNode extends ASTNode {
    public StmtNode(position pos) {
        super(pos);
    }
}
