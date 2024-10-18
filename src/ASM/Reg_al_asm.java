package ASM;

import ASM.inst.*;
import ASM.section.data;
import ASM.section.rodata;
//import ASM.section.text;
import ASM.section.text_new;
import Frontend.IR.IRProgramNode;
import Frontend.IR.entity.*;
import Frontend.IR.node.IRNode;
import Frontend.IR.node.def.IRFunDef;
import Frontend.IR.node.def.IRGlobalVarDef;
import Frontend.IR.node.def.IRStringDef;
import Frontend.IR.node.inst.*;
import Frontend.IR.node.stmt.IRBlockNode;
import Frontend.IR.type.IRType;
import org.antlr.v4.runtime.misc.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

//约定：被调用者保存寄存器t0-t2
//约定：调用者保存寄存器ra
public class Reg_al_asm {
    public IRProgramNode ir;
    public prog_new asm;
    HashMap<String, Pair<type, Integer>> var2regOrMem = new HashMap<>();
    HashMap<String, Integer> regOffset = new HashMap<>();//reg存储->栈sp偏移
    int stackSize = 0;
    String nowFun = null;
    static int K;
    HashSet<Integer> used_reg = new HashSet<>();
    String spare_reg = "x31";
    String phi_reg = "x27";
    String mem_reg1 = "x28";
    String mem_reg2 = "x29";
    String tmp_reg = "x30";
    HashMap<String, text_new> bl2text = new HashMap<>();
    
    class MvEntity {
        public type a;
        public Integer b;
        public String c;
        
        public MvEntity(Pair<type, Integer> a) {
            this.a = a.a;
            this.b = a.b;
        }
        
        public MvEntity(type a, String c) {
            this.a = a;
            this.c = c;
        }
        
        public MvEntity(type a, int b) {
            this.a = a;
            this.b = b;
        }
        
        @Override
        public boolean equals(Object o_) {
            if (this == o_) return true;
            if (o_ == null || getClass() != o_.getClass()) return false;
            MvEntity o = (MvEntity) o_;
            boolean flag;
            if (c == null) {
                flag = (o.c == null);
            } else if (o.c == null) {
                flag = false;
            } else {
                flag = c.equals(o.c);
            }
            return a == o.a && b == o.b && flag;
//            if (a == type.reg && o.a == type.reg) {
//                return b == o.b;
//            } else if (a == type.mem && o.a == type.mem) {
//                return b == o.b;
//            } else if (a == type.reg && o.a == type.mem) {
//                return c.equals(o.c);
//            } else if (a == type.mem && o.a == type.reg) {
//                return c.equals(o.c);
//            } else {
//                return false;
//            }
        }
        
        @Override
        public int hashCode() {
            int result = a.hashCode();
            result = 31 * result + (b != null ? b.hashCode() : 0);
            result = 31 * result + (c != null ? c.hashCode() : 0);
            return result;
        }
    }
    
    int get_reg_int(String reg) {
        return Integer.parseInt(reg.substring(1));
    }
    
    enum type {
        reg, mem, num, glob
    }
    
    
    public Reg_al_asm(IRProgramNode ir, int k) {
        this.ir = ir;
        asm = new prog_new();
        K = k;
    }
    
    public void print() {
        System.out.println(asm.toString());
    }
    
    private int literalToInt(IRLiteral it) {
        if (it instanceof IRIntLiteral) {
            return ((IRIntLiteral) it).value;
        } else if (it instanceof IRBoolLiteral) {
            return ((IRBoolLiteral) it).value ? 1 : 0;
        } else if (it instanceof IRNullPtrLiteral) {
            return 0;
        } else {
            assert false;
            return 0;
        }
    }
    
    
    private void buildGlobalVar(IRGlobalVarDef it) {//不去掉@，加入.data
        data d = new data(it.name, literalToInt(it.value));
        asm.pushData(d);
    }
    
    private void buildString(IRStringDef it) {
        rodata r = new rodata(it.name, it.value);
        asm.pushRodata(r);
    }
    
    private void inas(text_new t, HashSet<String> lo_after_sp) {
//        for (Integer i : used_reg) {
//            t.push(new Sw("x" + i, "sp", regOffset.get("x" + i)));
//        }
        for (String s : lo_after_sp) {
            var pos = var2regOrMem.get(s);
            assert pos != null;
            if (pos.a == type.reg) {
                t.push(new Sw("x" + pos.b, "sp", regOffset.get("x" + pos.b)));
            }
        }
    }

//    private void inas_builtin(text_new t) {
//        t.push(new Sw("x" + 10, "sp", regOffset.get("x" + 10)));
//        t.push(new Sw("x" + 11, "sp", regOffset.get("x" + 11)));
//    }
    
    private void outas(text_new t, int dest_reg, HashSet<String> lo_after_sp) {
//        for (Integer i : used_reg) {
//            if (i != dest_reg) t.push(new Lw("x" + i, "sp", regOffset.get("x" + i)));
//        }
        for (String s : lo_after_sp) {
            var pos = var2regOrMem.get(s);
            assert pos != null;
            if (pos.a == type.reg) {
                if (pos.b != dest_reg) t.push(new Lw("x" + pos.b, "sp", regOffset.get("x" + pos.b)));
            }
        }
    }
    
    enum var_type {
        num, reg
    }
    
    Pair<var_type, String> src(IREntity entity, text_new t, String reg, boolean hard) {
        if (entity instanceof IRLiteral) {
            int val = entity.getVal();
            if (val == 0 && !hard) {
                return new Pair<>(var_type.reg, "x0");
            }
            t.push(new Li(reg, val));
            return new Pair<>(var_type.reg, reg);
//            if (val <= 2047 && val >= -2048) {
//                return new Pair<>(var_type.num, String.valueOf(val));
//            } else {
//                t.push(new Li(reg, val));
//                return new Pair<>(var_type.reg, reg);
//            }
        } else if (entity instanceof IRVar var) {
            if (var.name.charAt(0) == '@') {
                t.push(new La(reg, var.name));
                return new Pair<>(var_type.reg, reg);
//                assert false;
//                return null;
//                t.push(new La(reg, var.name));
            } else {
                if (var.name.equals("null")) {
                    if (!hard) {
                        return new Pair<>(var_type.reg, "x0");
                    }
                    t.push(new Li(reg, 0));
                    return new Pair<>(var_type.reg, reg);
//                    return new Pair<>(var_type.num, "0");
                }
                if (!(var.name.charAt(0) == '%')) {
                    if (var.name.equals("这不合理")) {
                        System.exit(0);
                    }
                    int val = Integer.parseInt(var.name);
                    t.push(new Li(reg, val));
                    return new Pair<>(var_type.reg, reg);
//                    if (val <= 2047 && val >= -2048) {
//                        return new Pair<>(var_type.num, String.valueOf(val));
//                    } else {
//                        t.push(new Li(reg, val));
//                        return new Pair<>(var_type.reg, reg);
//                    }
                }
                var pos = var2regOrMem.get(var.name);
                assert pos != null;
                if (pos.a == type.reg) {
                    if (!hard) return new Pair<>(var_type.reg, "x" + pos.b);
                    else {
                        if (!reg.equals("x" + pos.b)) t.push(new Mv(reg, "x" + pos.b));
                        return new Pair<>(var_type.reg, reg);
                    }
                } else {
                    int offset = pos.b;
                    t.push(new Lw(reg, "sp", offset));
                    return new Pair<>(var_type.reg, reg);
                }
            }
        } else {
            assert false;
            return null;
        }
    }
    
    Pair<var_type, String> src(String entity, text_new t, String reg, boolean hard) {
        if (entity.charAt(0) != '@' && entity.charAt(0) != '%') {
            int val = Integer.parseInt(entity);
            if (val == 0 && !hard) {
                return new Pair<>(var_type.reg, "x0");
            }
            t.push(new Li(reg, val));
            return new Pair<>(var_type.reg, reg);
        } else {
            if (entity.charAt(0) == '@') {
                t.push(new La(reg, entity));
                return new Pair<>(var_type.reg, reg);
            } else {
                assert entity.charAt(0) == '%';
                var pos = var2regOrMem.get(entity);
                assert pos != null;
                if (pos.a == type.reg) {
                    if (!hard) return new Pair<>(var_type.reg, "x" + pos.b);
                    else {
                        if (!reg.equals("x" + pos.b)) t.push(new Mv(reg, "x" + pos.b));
                        return new Pair<>(var_type.reg, reg);
                    }
                } else {
                    int offset = pos.b;
                    t.push(new Lw(reg, "sp", offset));
                    return new Pair<>(var_type.reg, reg);
                }
            }
        }
    }
    
    MvEntity src(IREntity entity) {
        if (entity instanceof IRLiteral) {
            return new MvEntity(type.num, entity.getVal());
        } else if (entity instanceof IRVar var) {
            if (var.name.charAt(0) == '@') {
                return new MvEntity(type.glob, var.name);
            } else {
                if (var.name.equals("null")) {
                    return new MvEntity(type.num, 0);
                }
                if (!(var.name.charAt(0) == '%')) {
                    return new MvEntity(type.num, Integer.parseInt(var.name));
                }
                var pos = var2regOrMem.get(var.name);
                assert pos != null;
                return new MvEntity(pos);
            }
        } else {
            assert false;
            return null;
        }
    }
    
    private void buildPhi(ArrayList<phiInstNode> it, text_new t) {
        HashMap<String, HashMap<MvEntity, MvEntity>> bl2permute = new HashMap<>();
        for (phiInstNode stmt : it) {
            var new_ = new MvEntity(var2regOrMem.get(stmt.dest.name));
            for (int i = 0; i < stmt.values.size(); i++) {
                String label = stmt.labels.get(i);
                IREntity old = stmt.values.get(i);
                MvEntity old_;
                if (old instanceof IRVar var) {
                    if (var.name.charAt(0) == '@') {
                        old_ = new MvEntity(type.glob, var.name);
                    } else if (var.name.equals("null")) {
                        old_ = new MvEntity(type.num, 0);
                    } else if (var.toString().charAt(0) != '%') {
                        int c = Integer.parseInt(var.toString());
                        old_ = new MvEntity(type.num, c);
                    } else {
                        var pos = var2regOrMem.get(var.name);
                        assert pos != null;
                        old_ = new MvEntity(pos);
                    }
                } else {
                    old_ = new MvEntity(type.num, old.getVal());
                }
                bl2permute.computeIfAbsent(label, aa -> new HashMap<>()).put(new_, old_);
            }
        }
        for (var entry : bl2permute.entrySet()) {
            text_new t_ = bl2text.get(entry.getKey());
            HashMap<MvEntity, MvEntity> new2old = entry.getValue();
            permute(new2old, t_, text_new.permute_type.phi);
        }
    }
    
    void mv(String entity, String reg, text_new t) {//reg->entity
        assert entity.charAt(0) == '%';
        var pos = var2regOrMem.get(entity);
        assert pos != null;
        if (pos.a == type.reg) {
            t.push(new Mv("x" + pos.b, reg));
        } else {
            int offset = pos.b;
            t.push(new Sw(reg, "sp", offset));
        }
    }
    
    boolean is_builtin_fun(String funName) {
        if (funName.equals("print") || funName.equals("println") || funName.equals("getString") || funName.equals("getInt") || funName.equals("toString")) {
            return true;
        }
        if (funName.equals("malloc") || funName.equals("printInt") || funName.equals("printlnInt") || funName.equals("bool_toString") || funName.equals("alloca_helper") || funName.equals("array.size") || funName.equals("string.length") || funName.equals("string.parseInt") || funName.equals("string.compare") || funName.equals("string.concat") || funName.equals("string.copy") || funName.equals("string.ord")) {
            return true;
        }
//        if (funName.equals("string.substring")|| funName.equals("array.alloca_inside") || funName.equals("array.alloca")){
//            return false;
//        }
        return false;
    }
    
    int literalToInt(String it) {
        assert it.charAt(0) != '@';
        assert it.charAt(0) != '%';
        if (it.equals("null")) {
            return 0;
        }
        if (it.equals("true")) {
            return 1;
        }
        if (it.equals("false")) {
            return 0;
        }
        int val = Integer.parseInt(it);
        assert it.equals(val + "");
        return val;
    }
    
    static <T> void swap(T a, T b) {
        T c = a;
        a = b;
        b = c;
    }
    
    int cnt(int x) {
        int cnt = 0;
        while ((x & 1) == 0) {
            x >>= 1;
            cnt++;
        }
        return cnt;
    }
    
    boolean buildBin(binaryInstNode is, text_new t) {
        assert is.lhs.toString().charAt(0) != '@';
        assert is.rhs.toString().charAt(0) != '@';
        String _lhs = is.lhs.toString();
        String _rhs = is.rhs.toString();
        boolean lIsNum = _lhs.charAt(0) != '%';
        boolean rIsNum = _rhs.charAt(0) != '%';
        int lNum = 0, rNum = 0;
        if (lIsNum) lNum = literalToInt(_lhs);
        if (rIsNum) rNum = literalToInt(_rhs);
        var pos = var2regOrMem.get(is.dest.name);
        assert pos != null;
        String target = (pos.a == type.reg) ? "x" + pos.b : mem_reg1;
        boolean flag = false;
        if (!(lIsNum && rIsNum)) {
            if (lIsNum) {
                if (lNum < -2048 || lNum > 2047) {
                    if (!is.op.equals(binaryInstNode.opEnum.shl) && !is.op.equals(binaryInstNode.opEnum.ashr)) {
                        return false;
                    }
                }
            }
            if (rIsNum) {
                if (rNum < -2048 || rNum > 2047) {
                    if (!is.op.equals(binaryInstNode.opEnum.shl) && !is.op.equals(binaryInstNode.opEnum.ashr)) {
                        return false;
                    }
                }
            }
        }
        switch (is.op) {
            case add -> {
                if (lIsNum && rIsNum) {
                    t.push(new Li(target, lNum + rNum));
                    flag = true;
                } else if (lIsNum) {
                    Pair<var_type, String> rhs = src(is.rhs, t, mem_reg2, false);
                    t.push(new Arithimm("addi", target, rhs.b, lNum));
                    flag = true;
                } else if (rIsNum) {
                    Pair<var_type, String> lhs = src(is.lhs, t, mem_reg1, false);
                    t.push(new Arithimm("addi", target, lhs.b, rNum));
                    flag = true;
                } else {
                    Pair<var_type, String> lhs = src(is.lhs, t, mem_reg1, false);
                    Pair<var_type, String> rhs = src(is.rhs, t, mem_reg2, false);
                    t.push(new Arith("add", target, lhs.b, rhs.b));
                    flag = true;
                }
            }
            case sub -> {
                if (lIsNum && rIsNum) {
                    t.push(new Li(target, lNum - rNum));
                    flag = true;
                } else if (lIsNum) {
                    if (lNum == 0) {
                        Pair<var_type, String> rhs = src(is.rhs, t, mem_reg2, false);
                        t.push(new Arith("neg", target, rhs.b));
                    } else {
                        t.push(new Li(mem_reg1, lNum));
                        Pair<var_type, String> rhs = src(is.rhs, t, mem_reg2, false);
                        t.push(new Arith("sub", target, mem_reg1, rhs.b));
                    }
                    flag = true;
                } else if (rIsNum) {
                    Pair<var_type, String> lhs = src(is.lhs, t, mem_reg1, false);
                    t.push(new Arithimm("addi", target, lhs.b, -rNum));
                    flag = true;
                } else {
                    Pair<var_type, String> lhs = src(is.lhs, t, mem_reg1, false);
                    Pair<var_type, String> rhs = src(is.rhs, t, mem_reg2, false);
                    t.push(new Arith("sub", target, lhs.b, rhs.b));
                    flag = true;
                }
            }
            case mul -> {
                if (lIsNum && rIsNum) {
                    t.push(new Li(target, lNum * rNum));
                    flag = true;
                } else {
                    boolean flag_ = false;
                    if (rIsNum) {
                        flag_ = true;
                        lIsNum = true;
                        rIsNum = false;
                        int tmp = lNum;
                        lNum = rNum;
                        rNum = tmp;
                        var tmp_ = is.lhs;
                        is.lhs = is.rhs;
                        is.rhs = tmp_;
                    }
                    if (lIsNum) {
                        if (lNum == 0) {
                            t.push(new Li(target, 0));
                            flag = true;
                        } else if (lNum == 1) {
                            Pair<var_type, String> rhs = src(is.rhs, t, mem_reg2, false);
                            t.push(new Mv(target, rhs.b));
                            flag = true;
                        } else if (lNum == -1) {
                            Pair<var_type, String> rhs = src(is.rhs, t, mem_reg2, false);
                            t.push(new Arith("neg", target, rhs.b));
                            flag = true;
                        } else if (lNum > 0 && (lNum & (lNum - 1)) == 0) {
                            int cnt = cnt(lNum);
                            Pair<var_type, String> rhs = src(is.rhs, t, mem_reg2, false);
                            t.push(new Arithimm("slli", target, rhs.b, cnt));
                            flag = true;
                        } else if (lNum < 0 && (-lNum & (-lNum - 1)) == 0) {
                            lNum = -lNum;
                            int cnt = cnt(lNum);
                            Pair<var_type, String> rhs = src(is.rhs, t, mem_reg2, false);
                            t.push(new Arithimm("slli", target, rhs.b, cnt));
                            t.push(new Arith("neg", target, target));
                            flag = true;
                            lNum = -lNum;
                        } else {
                            t.push(new Li(mem_reg1, lNum));
                            Pair<var_type, String> rhs = src(is.rhs, t, mem_reg2, false);
                            t.push(new Arith("mul", target, mem_reg1, rhs.b));
                            flag = true;
                        }
                    } else {
                        assert !rIsNum;
                        Pair<var_type, String> lhs = src(is.lhs, t, mem_reg1, false);
                        Pair<var_type, String> rhs = src(is.rhs, t, mem_reg2, false);
                        t.push(new Arith("mul", target, lhs.b, rhs.b));
                        flag = true;
                    }
                    if (flag_) {
                        lIsNum = false;
                        rIsNum = true;
                        int tmp = lNum;
                        lNum = rNum;
                        rNum = tmp;
                        var tmp_ = is.lhs;
                        is.lhs = is.rhs;
                        is.rhs = tmp_;
                    }
                }
            }
            case sdiv -> {
                if (lIsNum && rIsNum) {
                    if (rNum == 0) {
                        t.push(new Comment("除以0异常，114514！"));
                        return false;
                    }
                    t.push(new Li(target, lNum / rNum));
                    flag = true;
                } else {
                    if (rIsNum) {
                        Pair<var_type, String> lhs = src(is.lhs, t, mem_reg1, false);
                        if (rNum == 1) {
                            t.push(new Mv(target, lhs.b));
                            flag = true;
                        } else if (rNum == -1) {
                            t.push(new Arith("neg", target, lhs.b));
                            flag = true;
                        } else if (rNum > 0 && (rNum & (rNum - 1)) == 0) {
                            if (true) return false;
                            int cnt = cnt(rNum);
                            t.push(new Arithimm("srai", target, lhs.b, cnt));
                            flag = true;
                        } else if (rNum < 0 && (-rNum & (-rNum - 1)) == 0) {
                            if (true) return false;
                            rNum = -rNum;
                            int cnt = cnt(rNum);
                            t.push(new Arithimm("srai", target, lhs.b, cnt));
                            t.push(new Arith("neg", target, target));
                            flag = true;
                            rNum = -rNum;
                        } else {
                            t.push(new Li(mem_reg2, rNum));
                            t.push(new Arith("div", target, lhs.b, mem_reg2));
                            flag = true;
                        }
                    } else if (lIsNum && lNum == 0) {
                        t.push(new Li(target, 0));
                        flag = true;
                    } else {
                        Pair<var_type, String> lhs = src(is.lhs, t, mem_reg1, false);
                        Pair<var_type, String> rhs = src(is.rhs, t, mem_reg2, false);
                        t.push(new Arith("div", target, lhs.b, rhs.b));
                        flag = true;
                    }
                }
            }
            case srem -> {
                if (lIsNum && rIsNum) {
                    t.push(new Li(target, lNum % rNum));
                    flag = true;
                } else {
                    if (rIsNum) {
                        Pair<var_type, String> lhs = src(is.lhs, t, mem_reg1, false);
                        if (rNum == 1 || rNum == -1) {
                            t.push(new Li(target, 0));
                            flag = true;
                        } else if (rNum > 0 && (rNum & (rNum - 1)) == 0) {
                            if (true) return false;
                            int tmp = rNum - 1;
                            t.push(new Arithimm("andi", target, lhs.b, tmp));
                            flag = true;
                        } else if (rNum < 0 && (-rNum & (-rNum - 1)) == 0) {
                            if (true) return false;
                            rNum = -rNum;
                            int tmp = rNum - 1;
                            t.push(new Arithimm("andi", target, lhs.b, tmp));
                            t.push(new Arith("neg", target, target));
                            flag = true;
                            rNum = -rNum;
                        } else {
                            t.push(new Li(mem_reg2, rNum));
                            t.push(new Arith("rem", target, lhs.b, mem_reg2));
                            flag = true;
                        }
                    } else if (lIsNum && lNum == 0) {
                        t.push(new Li(target, 0));
                        flag = true;
                    } else {
                        Pair<var_type, String> lhs = src(is.lhs, t, mem_reg1, false);
                        Pair<var_type, String> rhs = src(is.rhs, t, mem_reg2, false);
                        t.push(new Arith("rem", target, lhs.b, rhs.b));
                        flag = true;
                    }
                }
            }
            case shl -> {
                if (lIsNum && rIsNum) {
                    t.push(new Li(target, lNum << rNum));
                    flag = true;
                } else {
                    if (rIsNum) {
                        Pair<var_type, String> lhs = src(is.lhs, t, mem_reg1, false);
                        if (rNum == 0) {
                            t.push(new Mv(target, lhs.b));
                            flag = true;
                        } else if (rNum > -32 && rNum < 32) {
                            t.push(new Arithimm("slli", target, lhs.b, rNum));
                            flag = true;
                        } else {
                            t.push(new Li(target, 0));
                            flag = true;
                        }
                    } else if (lIsNum) {
                        if (lNum == 0) {
                            t.push(new Li(target, 0));
                        } else {
                            t.push(new Li(mem_reg1, lNum));
                            Pair<var_type, String> rhs = src(is.rhs, t, mem_reg2, false);
                            t.push(new Arith("sll", target, mem_reg1, rhs.b));
                        }
                        flag = true;
                    } else {
                        Pair<var_type, String> lhs = src(is.lhs, t, mem_reg1, false);
                        Pair<var_type, String> rhs = src(is.rhs, t, mem_reg2, false);
                        t.push(new Arith("sll", target, lhs.b, rhs.b));
                        flag = true;
                    }
                }
            }
            case ashr -> {
                if (lIsNum && rIsNum) {
                    t.push(new Li(target, lNum >> rNum));
                    flag = true;
                } else {
                    if (rIsNum) {
                        Pair<var_type, String> lhs = src(is.lhs, t, mem_reg1, false);
                        if (rNum == 0) {
                            t.push(new Mv(target, lhs.b));
                            flag = true;
                        } else if (rNum > -32 && rNum < 32) {
                            t.push(new Arithimm("srai", target, lhs.b, rNum));
                            flag = true;
                        } else if (rNum <= -32) {
                            t.push(new Li(target, 0));
                            flag = true;
                        } else {
                            assert rNum >= 32;
                            t.push(new Arithimm("srai", target, lhs.b, 31));
                            flag = true;
                        }
                    } else if (lIsNum) {
                        if (lNum == 0) {
                            t.push(new Li(target, 0));
                        } else {
                            t.push(new Li(mem_reg1, lNum));
                            Pair<var_type, String> rhs = src(is.rhs, t, mem_reg2, false);
                            t.push(new Arith("sra", target, mem_reg1, rhs.b));
                        }
                        flag = true;
                    } else {
                        Pair<var_type, String> lhs = src(is.lhs, t, mem_reg1, false);
                        Pair<var_type, String> rhs = src(is.rhs, t, mem_reg2, false);
                        t.push(new Arith("sra", target, lhs.b, rhs.b));
                        flag = true;
                    }
                }
            }
            case and -> {
                if (lIsNum && rIsNum) {
                    t.push(new Li(target, lNum & rNum));
                    flag = true;
                } else {
                    if (lIsNum) {
                        if (lNum == 0) {
                            t.push(new Li(target, 0));
                            flag = true;
                        } else if (lNum == -1) {
                            Pair<var_type, String> rhs = src(is.rhs, t, target, true);
                            flag = true;
                        } else {
                            Pair<var_type, String> rhs = src(is.rhs, t, mem_reg2, false);
                            t.push(new Arithimm("andi", target, rhs.b, lNum));
                            flag = true;
                        }
                    } else if (rIsNum) {
                        if (rNum == 0) {
                            t.push(new Li(target, 0));
                            flag = true;
                        } else if (rNum == -1) {
                            Pair<var_type, String> lhs = src(is.lhs, t, target, true);
                            flag = true;
                        } else {
                            Pair<var_type, String> lhs = src(is.lhs, t, mem_reg1, false);
                            t.push(new Arithimm("andi", target, lhs.b, rNum));
                            flag = true;
                        }
                    } else {
                        Pair<var_type, String> lhs = src(is.lhs, t, mem_reg1, false);
                        Pair<var_type, String> rhs = src(is.rhs, t, mem_reg2, false);
                        t.push(new Arith("and", target, lhs.b, rhs.b));
                        flag = true;
                    }
                }
            }
            case or -> {
                if (lIsNum && rIsNum) {
                    t.push(new Li(target, lNum | rNum));
                    flag = true;
                } else {
                    if (lIsNum) {
                        if (lNum == 0) {
                            Pair<var_type, String> rhs = src(is.rhs, t, target, true);
                            flag = true;
                        } else if (lNum == -1) {
                            t.push(new Li(target, -1));
                            flag = true;
                        } else {
                            Pair<var_type, String> rhs = src(is.rhs, t, mem_reg2, false);
                            t.push(new Arithimm("ori", target, rhs.b, lNum));
                            flag = true;
                        }
                    } else if (rIsNum) {
                        if (rNum == 0) {
                            Pair<var_type, String> lhs = src(is.lhs, t, target, true);
                            flag = true;
                        } else if (rNum == -1) {
                            t.push(new Li(target, -1));
                            flag = true;
                        } else {
                            Pair<var_type, String> lhs = src(is.lhs, t, mem_reg1, false);
                            t.push(new Arithimm("ori", target, lhs.b, rNum));
                            flag = true;
                        }
                    } else {
                        Pair<var_type, String> lhs = src(is.lhs, t, mem_reg1, false);
                        Pair<var_type, String> rhs = src(is.rhs, t, mem_reg2, false);
                        t.push(new Arith("or", target, lhs.b, rhs.b));
                        flag = true;
                    }
                }
            }
            case xor -> {
                if (lIsNum && rIsNum) {
                    t.push(new Li(target, lNum ^ rNum));
                    flag = true;
                } else {
                    if (lIsNum) {
                        if (lNum == 0) {
                            Pair<var_type, String> rhs = src(is.rhs, t, target, true);
                            flag = true;
                        } else {
                            Pair<var_type, String> rhs = src(is.rhs, t, mem_reg2, false);
                            t.push(new Arithimm("xori", target, rhs.b, lNum));
                            flag = true;
                        }
                    } else if (rIsNum) {
                        if (rNum == 0) {
                            Pair<var_type, String> lhs = src(is.lhs, t, target, true);
                            flag = true;
                        } else {
                            Pair<var_type, String> lhs = src(is.lhs, t, mem_reg1, false);
                            t.push(new Arithimm("xori", target, lhs.b, rNum));
                            flag = true;
                        }
                    } else {
                        Pair<var_type, String> lhs = src(is.lhs, t, mem_reg1, false);
                        Pair<var_type, String> rhs = src(is.rhs, t, mem_reg2, false);
                        t.push(new Arith("xor", target, lhs.b, rhs.b));
                        flag = true;
                    }
                }
            }
        }
        if (pos.a == type.mem) {
            t.push(new Sw(target, "sp", pos.b));
        }
        assert flag;
        return flag;
    }
    
    private void buildInst(instNode it, text_new t) {
        if (it instanceof allocaInstNode is) {
            assert false;
        } else if (it instanceof binaryInstNode is) {
            if (buildBin(is, t)) return;
            assert is.lhs.toString().charAt(0) != '@';
            assert is.rhs.toString().charAt(0) != '@';
//            ldVar(is.lhs, "x28", t);
//            ldVar(is.rhs, "x29", t);
            Pair<var_type, String> lhs = src(is.lhs, t, mem_reg1, false);
            Pair<var_type, String> rhs = src(is.rhs, t, mem_reg2, false);
            var pos = var2regOrMem.get(is.dest.name);
            assert pos != null;
            String target = (pos.a == type.reg) ? "x" + pos.b : mem_reg1;
            switch (is.op) {
                case add -> t.push(new Arith("add", target, lhs.b, rhs.b));
                case sub -> t.push(new Arith("sub", target, lhs.b, rhs.b));//
                case mul -> t.push(new Arith("mul", target, lhs.b, rhs.b));//
                case sdiv -> t.push(new Arith("div", target, lhs.b, rhs.b));//
                case srem -> t.push(new Arith("rem", target, lhs.b, rhs.b));//
                case shl -> t.push(new Arith("sll", target, lhs.b, rhs.b));
                case ashr -> t.push(new Arith("sra", target, lhs.b, rhs.b));
                case and -> t.push(new Arith("and", target, lhs.b, rhs.b));
                case or -> t.push(new Arith("or", target, lhs.b, rhs.b));
                case xor -> t.push(new Arith("xor", target, lhs.b, rhs.b));
            }
            if (pos.a == type.mem) {
                t.push(new Sw(target, "sp", pos.b));
            }
        } else if (it instanceof brInstNode is) {
            if (is.cond != null) {
                var cd = src(is.cond, t, mem_reg1, false);
                t.push(new Br("bne", cd.b, "x0", ".+8"));
                t.push(new Jump(nowFun + "_" + is.ifFalse));
                t.push(new Jump(nowFun + "_" + is.ifTrue));
//                t.push(new Br("beq", "x28", "x0", nowFun + "_" + is.ifFalse));
//                t.push(new Jump(nowFun + "_" + is.ifTrue));//可省略？？
            } else {
                t.push(new Jump(nowFun + "_" + is.dest));
            }
        } else if (it instanceof callInstNode is) {
            //done:传参：a0-a7，ret：返回值：a0,恢复ra
            Sw sw = new Sw("ra", "sp", regOffset.get("ra"));
            t.push(sw);
            inas(t, is.lo_after_sp);
            HashMap<MvEntity, MvEntity> new2old = new HashMap<>();
            for (int i = 0; i < is.args.size(); i++) {
                var src = src(is.args.get(i));
                if (i < 8) {
                    new2old.put(new MvEntity(type.reg, i + 10), src);
//                    src(is.args.get(i), t, "a" + i, true);
                } else {
                    int d = -(is.args.size() - i) * 4;
                    new2old.put(new MvEntity(type.mem, d), src);
//                    var src = src(is.args.get(i), t, mem_reg1, false);
//                    int d = -(is.args.size() - i) * 4;
//                    sw = new Sw(src.b, "sp", d);
//                    t.push(sw);
                }
            }
            permute(new2old, t, text_new.permute_type.call);
            t.push(new Call(is.funName));
            int dest_reg = -1;
            if (is.dest != null) {
                var dest = var2regOrMem.get(is.dest.name);
                assert dest != null;
                if (dest.a == type.reg) {
                    t.push(new Mv("x" + dest.b, "a0"));
                    dest_reg = dest.b;
                } else {
                    t.push(new Sw("a0", "sp", dest.b));
                }
            }
            Lw lw = new Lw("ra", "sp", regOffset.get("ra"));
            t.push(lw);
            outas(t, dest_reg, is.lo_after_sp);
        } else if (it instanceof getElementPtrInstNode is) {//一般而言，第一个变量是堆地址
            System.err.println("我默认长度全是4,即都是i32");
            String entity = is.dest;
            assert entity.charAt(0) == '%';
            var pos = var2regOrMem.get(entity);
            assert pos != null;
            String reg_ = (pos.a == type.reg) ? "x" + pos.b : mem_reg1;
            String reg = mem_reg1;
            src(is.ptr, t, reg, true);
            for (int i = 0; i < is.tys.size(); i++) {
                var idx = src(is.idxs.get(i), t, mem_reg2, false);
                t.push(new Arithimm("slli", tmp_reg, idx.b, 2));
                if (i < is.tys.size() - 1)
                    t.push(new Arith("add", reg, reg, tmp_reg));
                else
                    t.push(new Arith("add", reg_, reg, tmp_reg));
            }
            if (pos.a == type.mem) {
                t.push(new Sw(reg, "sp", pos.b));
            }
//            mv(is.dest, mem_reg1, t);
        } else if (it instanceof icmpInstNode is) {
//            ldVar(is.lhs, "x28", t);
//            ldVar(is.rhs, "x29", t);
//            var lhs = src(is.lhs, t, mem_reg1, false);
//            var rhs = src(is.rhs, t, mem_reg2, false);
            var entity = is.dest.name;
            assert entity.charAt(0) == '%';
            var pos = var2regOrMem.get(entity);
            assert pos != null;
            String reg = (pos.a == type.reg) ? "x" + pos.b : mem_reg1;
            
            Pair<var_type, String> lhs, rhs;
            lhs = src(is.lhs, t, mem_reg1, false);
            rhs = src(is.rhs, t, mem_reg2, false);
            assert lhs != null && rhs != null;
            if (lhs.b.equals("x0") && rhs.b.equals("x0")) {
                switch (is.op) {
                    case eq, sle, sge -> t.push(new Li(reg, 1));
                    case ne, slt, sgt -> t.push(new Li(reg, 0));
                }
                return;
            } else if (lhs.b.equals("x0")) {
                Pair<var_type, String> tmp = lhs;
                lhs = rhs;
                rhs = tmp;
                switch (is.op) {
                    case eq -> is.op = icmpInstNode.opEnum.eq;
                    case ne -> is.op = icmpInstNode.opEnum.ne;
                    case sgt -> is.op = icmpInstNode.opEnum.slt;
                    case sge -> is.op = icmpInstNode.opEnum.sle;
                    case slt -> is.op = icmpInstNode.opEnum.sgt;
                    case sle -> is.op = icmpInstNode.opEnum.sge;
                }
            }
            if (rhs.b.equals("x0")) {
                switch (is.op) {
                    case eq -> {
                        t.push(new S_z("seqz", reg, lhs.b));
                    }
                    case ne -> {
                        t.push(new S_z("snez", reg, lhs.b));
                    }
                    case sgt -> {
                        t.push(new S_z("sgtz", reg, lhs.b));
                    }
                    case sge -> {
                        t.push(new S_z("sltz", reg, lhs.b));
                        t.push(new Arith("neg", reg, reg));
                    }
                    case slt -> {
                        t.push(new S_z("sltz", reg, lhs.b));
                    }
                    case sle -> {
                        t.push(new S_z("sgtz", reg, lhs.b));
                        t.push(new Arith("neg", reg, reg));
                    }
                }
            } else {
                Arith arith = new Arith("sub", reg, lhs.b, rhs.b);
                t.push(arith);
                switch (is.op) {
                    case eq -> t.push(new S_z("seqz", reg, reg));
                    case ne -> t.push(new S_z("snez", reg, reg));
                    case sgt -> t.push(new S_z("sgtz", reg, reg));
                    case sge -> {
                        t.push(new S_z("sgtz", mem_reg1, reg));
                        t.push(new S_z("seqz", mem_reg2, reg));
                        t.push(new Arith("or", reg, mem_reg1, mem_reg2));
                    }
                    case slt -> t.push(new S_z("sltz", reg, reg));
                    case sle -> {
                        t.push(new S_z("sltz", mem_reg1, reg));
                        t.push(new S_z("seqz", mem_reg2, reg));
                        t.push(new Arith("or", reg, mem_reg1, mem_reg2));
                    }
                }
            }
            if (pos.a == type.mem) {
                t.push(new Sw(reg, "sp", pos.b));
            }
//            int offset = varOffset.get(is.dest.name);
//            t.push(new Sw("x30", "sp", offset));
        } else if (it instanceof loadInstNode is) {
            var ptr = src(is.ptr, t, mem_reg1, false);
            var pos = var2regOrMem.get(is.dest);
            assert pos != null;
            if (pos.a == type.reg) {
                t.push(new Lw("x" + pos.b, ptr.b, 0));
            } else {
                t.push(new Lw(mem_reg1, ptr.b, 0));
                t.push(new Sw(mem_reg1, "sp", pos.b));
            }
        } else if (it instanceof retInstNode is) {
            if (is.value.typeInfo.type != IRType.IRTypeEnum.void_) {
//                ldVar(is.value, "a0", t);
                src(is.value, t, "x10", true);
            }
//            for (int i = 27; i <= 31; i++) {
//                t.push(new Lw("x" + i, "sp", regOffset.get("x" + i)));
//            }
//            Lw lw0 = new Lw("x28", "sp", regOffset.get("x28"));
//            Lw lw1 = new Lw("x29", "sp", regOffset.get("x29"));
//            Lw lw2 = new Lw("x30", "sp", regOffset.get("x30"));
//            t.push(lw0);
//            t.push(lw1);
//            t.push(lw2);
            if (stackSize <= 2047 && stackSize >= -2048) {
                Arithimm arithimm = new Arithimm("addi", "sp", "sp", stackSize);
                t.push(arithimm);
            } else {
                Li li = new Li("x28", stackSize);
                t.push(li);
                Arith arith = new Arith("add", "sp", "x28", "sp");
                t.push(arith);
            }
            t.push(new Ret());
        } else if (it instanceof storeInstNode is) {
//            ldVar(is.value, "x28", t);
//            ldVar(is.ptr, "x29", t);
            var value = src(is.value, t, mem_reg1, false);
            var ptr = src(is.ptr, t, mem_reg2, false);
            t.push(new Sw(value.b, ptr.b, 0));
//            t.push(new Sw("x28", "x29", 0));
        } else {
            assert false;
        }
    }
    
    private void collectVar(IRFunDef it) {//spill
        HashMap<String, Integer> varOffset = new HashMap<>();//变量->栈sp偏移,包含大于8个参数的参数
        regOffset.clear();
        stackSize = 0;
        int offset = 1;
        regOffset.put("ra", (++offset) * 4);
        for (var reg : used_reg) {
            regOffset.put("x" + reg, (++offset) * 4);
        }
//        for (int i = 27; i <= 31; i++) {
//            regOffset.put("x" + i, (++offset) * 4);
//        }
        stackSize = (offset + it.spill.size()) * 4 + 4;
//int d = stackSize - (it.parameters.size() - i) * 4;
//varOffset.put(para, d);
        for (int i = 0; i < it.parameters.size(); i++) {
            if (i >= 8) {
                String para = it.parameters.get(i).name;
                if (para.charAt(0) == '%') {
                    if (it.spill.contains(para)) {
                        varOffset.put(para, stackSize - (it.parameters.size() - i) * 4);
                    }
                }
            }
        }
        for (String var : it.spill) {
            if (!varOffset.containsKey(var)) {
                varOffset.put(var, (++offset) * 4);
            }
        }
        for (var entry : varOffset.entrySet()) {
            var2regOrMem.put(entry.getKey(), new Pair<>(type.mem, entry.getValue()));
        }
    }
    
    private void storeReg(text_new t) {
//        Sw sw = new Sw("x28", "sp", regOffset.get("x28"));
//        t.push(sw);
//        sw = new Sw("x29", "sp", regOffset.get("x29"));
//        t.push(sw);
//        sw = new Sw("x30", "sp", regOffset.get("x30"));
//        t.push(sw);
//        sw = new Sw("ra", "sp", regOffset.get("ra"));
//        t.push(sw);
//        sw = new Sw("a0", "sp", regOffset.get("a0"));
//        t.push(sw);
    }
    
    private void buildFun(IRFunDef it) {
        used_reg.clear();
        var2regOrMem.clear();
        bl2text.clear();
        old_para_pos.clear();
        String funName = it.name.substring(1);
        nowFun = funName;
        //ret：
        //done:返回值传递
        //done:恢复t0-t2，ra，sp（栈释放），a0;
        
        for (IRBlockNode block : it.blocks) {
            if (block == null) {
                continue;
            }
            text_new t;
//            if (funName.equals("main") && block.label.equals("entry")) {
//                t = new text_new("main");
            if (block.label.equals("entry")) {
                t = new text_new(funName, spare_reg);
            } else {
                t = new text_new(funName + "_" + block.label, spare_reg);
            }
            bl2text.put(block.label, t);
            if (block.label.equals("entry")) {
                color2reg(it);//reg
                collectVar(it);//spill
                paraHandle(it);//old_para_pos
                para_permute(t);//permute para
                
                storeReg(t);
            }
            
            for (instNode stmt : block.insts) {
                //done:添加注释
                //done:处理判断是参数还是变量
                //done:判断变量是全局变量还是局部变量
                //done：la
                assert !(stmt instanceof allocaInstNode);
                Comment comment = new Comment(stmt.toString().substring(0, stmt.toString().length() - 1));
                t.push(comment);
                buildInst(stmt, t);
            }
            asm.pushText(t);
        }
        for (IRBlockNode block : it.blocks) {
            if (block == null) {
                continue;
            }
            text_new t = bl2text.get(block.label);
            for (phiInstNode stmt : block.phis) {
                Comment comment = new Comment(stmt.toString().substring(0, stmt.toString().length() - 1));
                t.push(comment);
            }
            buildPhi(block.phis, t);
        }
    }
    
    public void build() {
        for (IRNode it : ir.globalVarDefs) {
            buildGlobalVar((IRGlobalVarDef) it);
        }
        for (IRStringDef it : ir.stringDefs) {
            buildString(it);
        }
        for (IRFunDef it : ir.initFuns) {
            buildFun(it);
        }
        for (IRFunDef it : ir.funDefs) {
            buildFun(it);
        }
    }
    
    int start = 3;
    
    void color2reg(IRFunDef it) {//reg
        var spill = it.spill;
        var color = it.tempMap;
        HashMap<Integer, Integer> num2reg = new HashMap<>();
        HashSet<Integer> alled_num = new HashSet<>();
        for (int i = 0; i < it.parameters.size(); i++) {
            if (i < 8) {
                var para = it.parameters.get(i).toString();
                if (para.charAt(0) == '%' && color.containsKey(para)) {
                    num2reg.put(color.get(para), 10 + i);
                    used_reg.add(10 + i);
                    alled_num.add(color.get(para));
                }
            }
        }
        for (int i = 0; i < K; i++) {
            if (color.containsValue(i) && !alled_num.contains(i)) {
                for (int j = start; j < start + K; j++) {
                    if (!used_reg.contains(j)) {
                        num2reg.put(i, j);
                        used_reg.add(j);
                        break;
                    }
                }
            }
        }
        for (var entry : color.entrySet()) {
            assert entry.getKey().charAt(0) == '%' && !spill.contains(entry.getKey());
            int reg = num2reg.get(entry.getValue());
            var2regOrMem.put(entry.getKey(), new Pair<>(type.reg, reg));
        }
    }
    
    void permute(HashMap<MvEntity, MvEntity> new2old, text_new t, text_new.permute_type type_) {
        HashMap<MvEntity, ArrayList<MvEntity>> old2new = new HashMap<>();
        for (var entry : new2old.entrySet()) {
            if (!entry.getKey().equals(entry.getValue()))
                old2new.computeIfAbsent(entry.getValue(), aa -> new ArrayList<>()).add(entry.getKey());
//                old2new.put(entry.getValue(), entry.getKey());
        }
        boolean flag = true;
        while (flag) {
            flag = false;
            HashSet<Pair<MvEntity, MvEntity>> to_remove = new HashSet<>();
            for (var entry : old2new.entrySet()) {
                var old = entry.getKey();
                var new___ = entry.getValue();
//                int i=-1;
                for (var new_ : new___) {
//                    i++;
                    var key_set = old2new.keySet();
                    boolean cont = false;
                    for (var key_ : key_set) {
                        if (key_.equals(new_)) {
                            cont = true;
                            break;
                        }
                    }
                    if (!cont) {
                        mv(old, new_, t, type_);
                        to_remove.add(new Pair<>(old, new_));
                        flag = true;
                    }
                }
            }
            for (var entry : to_remove) {
                var old = entry.a;
                var new_ = entry.b;
                var old_node = old2new.get(old);
                old_node.remove(new_);
//                old2new.get(entry.a).remove((int)entry.b);
                if (old2new.get(entry.a).isEmpty()) {
                    old2new.remove(entry.a);
                }
//                old2new.remove(entry);
            }
        }
        assert old2new.size() != 1;
        while (!old2new.isEmpty()) {
            var old = old2new.keySet().iterator().next();
            ArrayList<MvEntity> queue = new ArrayList<>();
            queue.add(old);
            while (true) {
                var last = queue.getLast();
                MvEntity tmp = null;
                for (var entry : old2new.entrySet()) {
                    if (entry.getKey().equals(last)) {
                        var tmp_ = entry.getValue();
                        assert tmp_.size() == 1;
                        tmp = tmp_.getFirst();
                    }
                }
//                var tmp = old2new.get(last);
                assert tmp != null;
                if (queue.contains(tmp)) {
                    assert queue.getFirst().equals(tmp);
                    break;
                }
                queue.add(tmp);
            }
            assert queue.size() >= 2;
            mv(queue.getLast(), new MvEntity(type.reg, get_reg_int(mem_reg2)), t, type_);
            mvs(queue, t, type_);
            mv(new MvEntity(type.reg, get_reg_int(mem_reg2)), queue.getFirst(), t, type_);
            for (var entry : queue) {
                old2new.remove(entry);
            }
        }
    }
    
    void mvs(ArrayList<MvEntity> queue, text_new t, text_new.permute_type type_) {
        for (int i = queue.size() - 1; i > 0; i--) {
            var old = queue.get(i - 1);
            var new_ = queue.get(i);
            mv(old, new_, t, type_);
        }
    }
    
    void mv(MvEntity old, MvEntity new_, text_new t, text_new.permute_type type_) {
        if (old.a == type.reg && new_.a == type.reg) {
            t.push_phi(new Mv("x" + new_.b, "x" + old.b), type_);
        } else if (old.a == type.mem && new_.a == type.reg) {
            t.push_phi(new Lw("x" + new_.b, "sp", old.b), type_);
        } else if (old.a == type.reg && new_.a == type.mem) {
            t.push_phi(new Sw("x" + old.b, "sp", new_.b), type_);
        } else if (old.a == type.mem && new_.a == type.mem) {
            t.push_phi(new Lw(mem_reg1, "sp", old.b), type_);
            t.push_phi(new Sw(mem_reg1, "sp", new_.b), type_);
        } else if (old.a == type.num && new_.a == type.reg) {
            t.push_phi(new Li("x" + new_.b, old.b), type_);
        } else if (old.a == type.num && new_.a == type.mem) {
            t.push_phi(new Li(mem_reg1, old.b), type_);
            t.push_phi(new Sw(mem_reg1, "sp", new_.b), type_);
        } else if (old.a == type.glob && new_.a == type.reg) {
            t.push_phi(new La("x" + new_.b, old.c), type_);
        } else if (old.a == type.glob && new_.a == type.mem) {
            t.push_phi(new La(mem_reg1, old.c), type_);
            t.push_phi(new Sw(mem_reg1, "sp", new_.b), type_);
        } else {
            assert false;
        }
    }
    
    HashMap<String, Pair<type, Integer>> old_para_pos = new HashMap<>();//参数->寄存器
    
    void paraHandle(IRFunDef it) {
        for (int i = 0; i < it.parameters.size(); i++) {
            String para = it.parameters.get(i).name;
            if (i < 8) {
                old_para_pos.put(para, new Pair<>(type.reg, 10 + i));
            } else {
                int d = stackSize - (it.parameters.size() - i) * 4;
                old_para_pos.put(para, new Pair<>(type.mem, d));
            }
        }
    }
    
    void para_permute(text_new t) {
        HashMap<MvEntity, MvEntity> new2old = new HashMap<>();
        for (var entry : old_para_pos.entrySet()) {
            String para = entry.getKey();
            Pair<type, Integer> old = entry.getValue();
            var new__ = var2regOrMem.get(para);
            if (new__ == null) {
                continue;
            }
//            assert new__ != null;
            MvEntity new_ = new MvEntity(new__);
            if (!new__.equals(old)) {
                new2old.put(new_, new MvEntity(old));
            }
        }
        if (-stackSize <= 2047 && -stackSize >= -2048) {
            Arithimm arithimm = new Arithimm("addi", "sp", "sp", -stackSize);
            t.push(arithimm);
        } else {
            Li li = new Li(spare_reg, -stackSize);
            t.push(li);
            Arith arith = new Arith("add", "sp", "x28", "sp");
            t.push(arith);
        }
        permute(new2old, t, text_new.permute_type.para);
    }
}