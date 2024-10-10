package ASM.section;

import ASM.inst.*;

import java.util.ArrayList;

public class text_new extends section {
    public ArrayList<Inst> insts = new ArrayList<>();
    public String spare_reg;
    public text_new(String label, String spare_reg) {
        super(label);
        this.spare_reg = spare_reg;
    }
    
    public void push(Inst inst) {
        if (inst instanceof Sw sw) {
            if (sw.imm <= 2047 && sw.imm >= -2048)
                insts.add(inst);
            else {
                Li li = new Li(spare_reg, sw.imm);
//                assert !sw.val.equals(spare_reg) && !sw.addr.equals(spare_reg);
                Arith arith = new Arith("add", spare_reg, sw.addr, spare_reg);
                insts.add(li);
                insts.add(arith);
                insts.add(new Sw(sw.val, spare_reg, 0));
            }
        } else if (inst instanceof Lw lw) {
            if (lw.imm <= 2047 && lw.imm >= -2048)
                insts.add(inst);
            else {
                assert lw.rs.equals("x28")||lw.rs.equals("sp");
                String reg=lw.rd.equals("x29")?"x30":"x29";
                Li li = new Li(reg, lw.imm);
                Arith arith = new Arith("add", reg, lw.rs, reg);
//                assert !lw.rd.equals("x29") && !lw.rs.equals("x29");
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
