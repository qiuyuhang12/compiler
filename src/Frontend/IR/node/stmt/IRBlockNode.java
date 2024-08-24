package Frontend.IR.node.stmt;

import Frontend.IR.node.IRNode;
import Frontend.IR.node.def.IRFunDeclare;
import Frontend.IR.node.inst.brInstNode;
import Frontend.IR.node.inst.instNode;
import Frontend.IR.node.inst.retInstNode;

import java.util.ArrayList;

public class IRBlockNode extends IRNode {
    public IRBlockNode jumpSrc;//来源
    public IRFunDeclare func;
    public String label;
    public ArrayList<instNode> insts = new ArrayList<>();
    
    public IRBlockNode(IRBlockNode jumpSrc, IRFunDeclare func, String label) {
        this.jumpSrc = jumpSrc;
        this.func = func;
        this.label = label;
    }
    
    public void push(instNode inst) {
        insts.add(inst);
    }
    
    @Override
    public String toString() {
        assert insts != null && !insts.isEmpty();
        assert insts.getLast() instanceof brInstNode || insts.getLast() instanceof retInstNode;
        StringBuilder sb = new StringBuilder();
        sb.append(label).append(":\n");
        int i = 0;
        for (instNode inst : insts) {
            i++;
            sb.append(inst.toString());
            if (inst instanceof brInstNode || inst instanceof retInstNode) {
                if (i + 1 < insts.size()) {
                    insts.subList(i, insts.size()).clear();
                }
                break;
            }
        }
        return sb.toString() + "\n";
    }
}
