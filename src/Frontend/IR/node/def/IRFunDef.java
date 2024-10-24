package Frontend.IR.node.def;

import Frontend.IR.entity.IRVar;
import Frontend.IR.node.IRNode;
import Frontend.IR.node.stmt.IRBlockNode;
import Frontend.IR.type.IRType;
import Frontend.IR.node.inst.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class IRFunDef extends IRFunDeclare {
    public ArrayList<IRBlockNode> blocks = new ArrayList<>();
    public HashMap<String, Integer> tempMap = new HashMap<>();
    public HashSet<String> spill = new HashSet<>();
    
    
    public IRFunDef(IRType returnType, String name, IRVar... parameters) {
        super(returnType, name, parameters);
    }
    
    public HashSet<String> get_para_def() {
        HashSet<String> res = new HashSet<>();
        for (IRVar var : parameters) {
            String tmp = var.toString();
            if (tmp.charAt(0) == '%') res.add(tmp);
        }
        return res;
    }
    
    public boolean call_plenty(){
        int calls=0;
        for (IRBlockNode block : blocks) {
            for (IRNode node : block.insts) {
                if (node instanceof callInstNode) {
                    calls++;
                }
            }
        }
        return calls > 4;
    }
    
    public void push(IRBlockNode block) {
        blocks.add(block);
    }
    
    public String getEntry() {
        return blocks.getFirst().label;
    }
    public void clear(){
        for (IRBlockNode bl : blocks) {
            bl.clear();
        }
    }
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("define ").append(returnType.toString()).append(" ").append(name).append("(");
        for (int i = 0; i < parameters.size(); i++) {
            if (i != 0) sb.append(", ");
            sb.append(parameters.get(i).typeInfo.toString()).append(" ").append(parameters.get(i).toString());
        }
        sb.append(") {");
        for (IRBlockNode bl : blocks) {
            sb.append("\n").append(bl.toString());
        }
        sb.append("\n}");
        return sb.toString() + "\n";
    }
}
