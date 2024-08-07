package AST.expr;
import AST.*;
import AST.def.*;
import AST.stmt.*;
import Util.Type;
import Util.position;
public class parenExprNode {
public void accept(ASTVisitor visitor) {
visitor.visit(this);
}
}
