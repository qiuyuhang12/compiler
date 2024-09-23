package Frontend.IR.node.inst;

import AST.ASTNode;
import Frontend.IR.entity.IREntity;
import Frontend.IR.entity.IRVar;
import Frontend.IR.node.stmt.IRBlockNode;
import Frontend.IR.type.IRType;

import java.util.ArrayList;
import java.util.HashMap;

public class phiInstNode extends instNode {
    public IRVar dest;
    public IRType type;
    public String oriVar;
    public ArrayList<IREntity> values = new ArrayList<>();
    public ArrayList<String> labels = new ArrayList<>();
    
    public phiInstNode(ASTNode expr, IRBlockNode _parent, IRVar dest, IRType type) {
        super(expr, _parent);
        this.dest = dest;
        this.type = type;
    }
    
    public phiInstNode(ASTNode expr, IRBlockNode _parent, IRVar dest) {
        super(expr, _parent);
        this.dest = dest;
        this.type = dest.typeInfo;
    }
    
    public void push(IREntity value, String label) {
        values.add(value);
        labels.add(label);
    }
    
    public phiInstNode(String oriVar, String type) {
        super(null, null);
        this.oriVar = oriVar;
        this.type = new IRType(type);
    }
    
    public boolean add_source_m2r(String value, String label) {
        if (labels.contains(label)) return false;
        values.add(new IRVar(type.toString(), value, false));
        labels.add(label);
        return true;
    }
    
    public boolean rename(String dest) {
        if (this.dest != null) return false;
        this.dest = new IRVar(type.toString(), dest, false);
        return true;
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(dest.toString()).append(" = phi ").append(type.toString()).append(" ");
        for (int i = 0; i < values.size(); i++) {
            if (i != 0) sb.append(", ");
            sb.append("[ ").append(values.get(i).toString()).append(", %").append(labels.get(i)).append(" ]");
        }
        return sb.toString() + "\n";
    }
    
    @Override
    public void rename(HashMap<String, String> renameMap) {}
}
