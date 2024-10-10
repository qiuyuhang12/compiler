package ASM.section;

import ASM.inst.*;

import java.util.ArrayList;

public class text_new extends section {
    public ArrayList<Inst> insts = new ArrayList<>();
    public String phi_and_tmp_int;
    public text_new(String label, String phi_and_tmp_int) {
        super(label);
        this.phi_and_tmp_int = phi_and_tmp_int;
    }
    
    public void push(Inst inst) {
        if (inst instanceof Sw sw) {
            if (sw.imm <= 2047 && sw.imm >= -2048)
                insts.add(inst);
            else {
                Li li = new Li("t6", sw.imm);
//                assert !sw.val.equals("t6") && !sw.addr.equals("t6");
                Arith arith = new Arith("add", "t6", sw.addr, "t6");
                insts.add(li);
                insts.add(arith);
                insts.add(new Sw(sw.val, "t6", 0));
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
