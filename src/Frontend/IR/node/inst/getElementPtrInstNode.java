package Frontend.IR.node.inst;

import AST.ASTNode;
import Frontend.IR.entity.IRVar;
import Frontend.IR.node.stmt.IRBlockNode;
import Frontend.IR.type.IRType;

import java.util.ArrayList;

public class getElementPtrInstNode extends instNode {
    public IRVar dest, ptr;
    public IRType type;
    public ArrayList<IRType> tys = new ArrayList<>();
    public ArrayList<Integer> idxs = new ArrayList<>();

    public getElementPtrInstNode(ASTNode expr, IRBlockNode _parent, IRVar dest, IRVar ptr, IRType type) {
        super(expr, _parent);
        this.dest = dest;
        assert ptr.typeInfo.type == IRType.IRTypeEnum.ptr;
        this.ptr = ptr;
        this.type = type;
    }

    public void push(IRType ty, int idx) {
        tys.add(ty);
        idxs.add(idx);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(dest.toString()).append(" = getelementptr ").append(type.toString()).append(", ptr ").append(ptr.toString());
        for (int i = 0; i < tys.size(); i++) {
            sb.append(", ").append(tys.get(i).toString()).append(" ").append(idxs.get(i));
        }
        return sb.toString() + "\n";
    }
}
