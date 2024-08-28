package ASM.section;

import ASM.inst.*;

import java.util.ArrayList;

public class text extends section {
    public ArrayList<Inst> insts = new ArrayList<>();
    
    public text(String label) {
        super(label);
    }
    
    public void push(Inst inst) {
        if (inst instanceof Sw sw) {
            if (sw.imm <= 2047 && sw.imm >= -2048)
                insts.add(inst);
            else {
                Li li = new Li("t1", sw.imm);
                assert !sw.val.equals("t1") && !sw.addr.equals("t1");
                Arith arith = new Arith("add", "t1", sw.addr, "t1");
                insts.add(li);
                insts.add(arith);
                insts.add(new Sw(sw.val, "t1", 0));
            }
        } else if (inst instanceof Lw lw) {
            if (lw.imm <= 2047 && lw.imm >= -2048)
                insts.add(inst);
            else {
                assert lw.rs.equals("t0")||lw.rs.equals("sp");
                String reg=lw.rd.equals("t1")?"t2":"t1";
                Li li = new Li(reg, lw.imm);
                Arith arith = new Arith("add", reg, lw.rs, reg);
//                assert !lw.rd.equals("t1") && !lw.rs.equals("t1");
                insts.add(li);
                insts.add(arith);
                insts.add(new Lw(lw.rd, reg, 0));
            }
        } else
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
