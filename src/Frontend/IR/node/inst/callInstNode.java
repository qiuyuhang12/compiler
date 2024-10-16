package Frontend.IR.node.inst;

import AST.ASTNode;
import Frontend.IR.entity.IREntity;
import Frontend.IR.entity.IRVar;
import Frontend.IR.node.stmt.IRBlockNode;
import Frontend.IR.type.IRType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

public class callInstNode extends instNode {
    public IRVar dest;
    public IRType retType;
    public String funName;
    public ArrayList<IREntity> args = new ArrayList<>();
    public callInstNode(callInstNode other) {
        super(other);
        this.dest = other.dest;
        this.retType = other.retType;
        this.funName = other.funName;
        this.args = new ArrayList<>(other.args);
    }
    public callInstNode(ASTNode expr, IRBlockNode _parent, IRVar dest, IRType retType, String funName, IREntity... args) {
        super(expr, _parent);
        this.dest = dest;
        this.retType = retType;
        this.funName = funName;
        this.args.addAll(Arrays.asList(args));
    }
    
    //针对void返回值
    public callInstNode(ASTNode expr, IRBlockNode _parent, String funName, IREntity... args) {
        super(expr, _parent);
        this.dest = null;
        this.retType = new IRType(IRType.IRTypeEnum.void_);
        this.funName = funName;
        this.args.addAll(Arrays.asList(args));
    }
    
    public callInstNode(ASTNode expr, IRBlockNode _parent, IRVar dest, IRType retType, String funName, ArrayList<IREntity> args) {
        super(expr, _parent);
        this.dest = dest;
        this.retType = retType;
        this.funName = funName;
        this.args = args;
    }
    
    //针对void返回值
    public callInstNode(ASTNode expr, IRBlockNode _parent, String funName, ArrayList<IREntity> args) {
        super(expr, _parent);
        this.dest = null;
        this.retType = new IRType(IRType.IRTypeEnum.void_);
        this.funName = funName;
        this.args = args;
    }
    
    @Override
    public instNode copy(instNode other) {
        return new callInstNode(this);
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (dest != null) {
            sb.append(dest.toString()).append(" = ");
        }
        sb.append("call ").append(retType.toString()).append(" @").append(funName).append("(");
        for (int i = 0; i < args.size(); i++) {
            if (i != 0) {
                sb.append(", ");
            }
            sb.append(args.get(i).typeInfo.toString()).append(" ").append(args.get(i).toString());
        }
        sb.append(")");
        return sb.toString() + "\n";
    }
    
    @Override
    public String getVal() {
        if (dest == null) return null;
        return dest.toString();
    }
    
    @Override
    public void rename(HashMap<String, String> renameMap) {
        for (IREntity arg : args) {
            if (arg instanceof IRVar var) {
//                var.name = renameMap.getOrDefault(var.name, var.name);
                String tmp = renameMap.getOrDefault(var.name, var.name);
                if (!tmp.equals("这不合理")) {
                    var.name = tmp;
                }
            }
        }
    }
    
    @Override
    public String getDef() {
        if (!retType.type.equals(IRType.IRTypeEnum.void_)&&dest!=null) {
            return dest.name;
        } else return null;
    }
    
    @Override
    public HashSet<String> getUses() {
        HashSet<String> st = new HashSet<>();
        for (IREntity arg : args) {
            if (arg instanceof IRVar var && var.name.charAt(0) == '%') {
                st.add(var.name);
            }
        }
        return st;
    }
}
