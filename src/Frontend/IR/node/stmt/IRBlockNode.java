package Frontend.IR.node.stmt;

import Frontend.IR.node.IRNode;
import Frontend.IR.node.def.IRFunDeclare;
import Frontend.IR.node.inst.*;

import java.util.ArrayList;

public class IRBlockNode extends IRNode {
    public boolean renamed=false;
    public IRBlockNode jumpSrc;//来源
    public IRFunDeclare func;
    public String label;
    public ArrayList<instNode> insts = new ArrayList<>();
    public ArrayList<phiInstNode> phis=new ArrayList<>();
    public boolean setPhi(String oriVar,String type){
        for (phiInstNode phi:phis){
            if (phi.oriVar.equals(oriVar)){
                return false;
            }
        }
        phiInstNode phi = new phiInstNode(oriVar,type);
        phis.add(phi);
        return true;
    }
    public IRBlockNode(IRBlockNode jumpSrc, IRFunDeclare func, String label) {
        this.jumpSrc = jumpSrc;
        this.func = func;
        this.label = label;
    }
    
    public void push(instNode inst) {
        insts.add(inst);
    }
    
    public ArrayList<String> getNext(){
        if (insts.getLast() instanceof brInstNode br) {
            ArrayList<String> res = new ArrayList<>();
            if (br.isCondBr) {
                res.add(br.ifTrue);
                res.add(br.ifFalse);
            } else {
                res.add(br.dest);
            }
            return res;
        } else {
            return null;
        }
    }
    @Override
    public String toString() {
        assert insts != null && !insts.isEmpty();
        assert insts.getLast() instanceof brInstNode || insts.getLast() instanceof retInstNode;
        StringBuilder sb = new StringBuilder();
        sb.append(label).append(":\n");
        for (phiInstNode phi:phis){
            sb.append(phi.toString());
        }
        int i = 0;
        for (instNode inst : insts) {
            i++;
            sb.append(inst.toString());
            if (inst instanceof brInstNode || inst instanceof retInstNode) {
                if (i < insts.size()) {
                    insts.subList(i, insts.size()).clear();
                }
                break;
            }
        }
        return sb.toString() + "\n";
    }
}
