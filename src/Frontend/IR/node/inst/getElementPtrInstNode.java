package Frontend.IR.node.inst;

import AST.ASTNode;
import Frontend.IR.entity.IRVar;
import Frontend.IR.node.stmt.IRBlockNode;
import Frontend.IR.type.IRType;

import java.util.ArrayList;

public class getElementPtrInstNode extends instNode {
    public String dest, ptr;
    public IRType type;
    public ArrayList<IRType> tys = new ArrayList<>();
    public ArrayList<String> idxs = new ArrayList<>();

    public getElementPtrInstNode(ASTNode expr, IRBlockNode _parent, IRVar dest, IRVar ptr, IRType type) {
        super(expr, _parent);
        assert !ptr.toString().equals("null");
        this.dest = dest.name;
        assert ptr.typeInfo.type == IRType.IRTypeEnum.ptr;
        this.ptr = ptr.name;
        this.type = type;
    }

    public getElementPtrInstNode(ASTNode expr, IRBlockNode _parent, String dest, String ptr, IRType type) {
        super(expr, _parent);
        if (ptr==null) {
            System.err.println("ptr is null");
        }
        assert !ptr.equals("null");
        this.dest = dest;
        this.ptr = ptr;
        this.type = type;
    }

    public void push(IRType ty, int idx) {
        tys.add(ty);
        idxs.add(idx + "");
    }

    public void push(IRType ty, String idx) {
        tys.add(ty);
        idxs.add(idx);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(dest).append(" = getelementptr ").append(type.toString()).append(", ptr ").append(ptr);
        for (int i = 0; i < tys.size(); i++) {
            sb.append(", ").append(tys.get(i).toString()).append(" ").append(idxs.get(i));
        }
        return sb.toString() + "\n";
    }
}
