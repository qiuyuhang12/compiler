package ASM.section;

import ASM.inst.Inst;

import java.util.ArrayList;

public class text extends section {
    public ArrayList<Inst> insts = new ArrayList<>();
    
    public text(String label) {
        super(label);
    }
    
    public void push(Inst inst) {
        insts.add(inst);
    }
    
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("\t.section .text\n");
        sb.append("\t.globl ").append(label).append("\n");
        sb.append(label).append(":\n");
        for (Inst inst : insts) {
            sb.append("\t").append(inst.toString()).append("\n");
        }
        return sb.toString();
    }
}
