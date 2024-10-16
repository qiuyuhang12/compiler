package ASM.section;

import ASM.inst.*;

import java.util.ArrayList;

public class text_new extends section {
    public ArrayList<Inst> insts = new ArrayList<>();
    public ArrayList<Inst> insts_phi = new ArrayList<>();
    public ArrayList<Inst> para_permute = new ArrayList<>();
    public String spare_reg;
    
    public text_new(String label, String spare_reg) {
        super(label);
        this.spare_reg = spare_reg;
    }
    
    public enum permute_type {
        phi, para, call
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
                assert lw.rs.equals("x28") || lw.rs.equals("sp");
                String reg = lw.rd.equals("x29") ? "x30" : "x29";
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
    
    public void push_phi(Inst inst, permute_type type) {
        ArrayList<Inst> tmp;
        switch (type) {
            case phi -> tmp = insts_phi;
            case para -> tmp = para_permute;
            case call -> tmp = insts;
            default -> throw new IllegalStateException("Unexpected value: " + type);
        }
//        if (is_para) tmp = para_permute;
        if (inst instanceof Sw sw) {
            if (sw.imm <= 2047 && sw.imm >= -2048)
                tmp.add(inst);
            else {
                Li li = new Li(spare_reg, sw.imm);
//                assert !sw.val.equals(spare_reg) && !sw.addr.equals(spare_reg);
                Arith arith = new Arith("add", spare_reg, sw.addr, spare_reg);
                tmp.add(li);
                tmp.add(arith);
                tmp.add(new Sw(sw.val, spare_reg, 0));
            }
        } else if (inst instanceof Lw lw) {
            if (lw.imm <= 2047 && lw.imm >= -2048)
                tmp.add(inst);
            else {
                assert lw.rs.equals("x28") || lw.rs.equals("sp");
                String reg = lw.rd.equals("x29") ? "x30" : "x29";
                Li li = new Li(reg, lw.imm);
                Arith arith = new Arith("add", reg, lw.rs, reg);
//                assert !lw.rd.equals("x29") && !lw.rs.equals("x29");
                tmp.add(li);
                tmp.add(arith);
                tmp.add(new Lw(lw.rd, reg, 0));
            }
        } else
            tmp.add(inst);
    }
    
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("\t.section .text\n");
        sb.append("\t.globl ").append(label).append("\n");
        sb.append(label).append(":\n");
        boolean flag = false;
        if (!para_permute.isEmpty()) {
            flag = true;
            sb.append("\t").append(insts.getFirst().toString()).append("\n");
            for (Inst inst : para_permute) {
                sb.append("\t").append(inst.toString()).append("\n");
            }
        }
        if (insts_phi.isEmpty()) {
            int cnt = -1;
            for (Inst inst : insts) {
                cnt++;
                if (cnt == 0 && flag) continue;
                sb.append("\t").append(inst.toString()).append("\n");
            }
        } else {
            int j_count = 0;
            for (Inst inst : insts) {
                if (inst instanceof Jump) {
                    j_count++;
                }
            }
            assert j_count == 1;
            int ttt = insts.size() - 1;
            while (insts.get(ttt) instanceof Comment) ttt--;
            assert insts.get(ttt) instanceof Jump;
            for (int i = flag ? 1 : 0; i < ttt; i++) {
                sb.append("\t").append(insts.get(i).toString()).append("\n");
            }
            for (Inst inst : insts_phi) {
                sb.append("\t").append(inst.toString()).append("\n");
            }
            sb.append("\t").append(insts.get(ttt).toString()).append("\n");
        }
        return sb.toString();
    }
}
