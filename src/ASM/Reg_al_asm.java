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
    
    int get_reg_int(String reg) {
        return Integer.parseInt(reg.substring(1));
    }
    
    enum type {
        reg, mem, num
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
    
    private void inas(text_new t) {
        for (Integer i : used_reg) {
            t.push(new Sw("x" + i, "sp", regOffset.get("x" + i)));
        }
    }
    
    private void outas(text_new t) {
        for (Integer i : used_reg) {
            t.push(new Lw("x" + i, "sp", regOffset.get("x" + i)));
        }
    }
    
    enum var_type {
        num, reg
    }
    
    Pair<var_type, String> src(IREntity entity, text_new t, String reg, boolean hard) {
        if (entity instanceof IRLiteral) {
            int val = entity.getVal();
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
                assert false;
                return null;
//                t.push(new La(reg, var.name));
            } else {
                if (var.name.equals("null")) {
                    t.push(new Li(reg, 0));
                    return new Pair<>(var_type.reg, reg);
//                    return new Pair<>(var_type.num, "0");
                }
                if (!(var.name.charAt(0) == '%')) {
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
            t.push(new Li(reg, Integer.parseInt(entity)));
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
    
    private void buildPhi(ArrayList<phiInstNode> it, text_new t) {
        HashMap<String, HashMap<Pair<type, Integer>, Pair<type, Integer>>> bl2permute = new HashMap<>();
        for (phiInstNode stmt : it) {
            var new_ = var2regOrMem.get(stmt.dest.name);
            for (int i = 0; i < stmt.values.size(); i++) {
                String label = stmt.labels.get(i);
                IREntity old = stmt.values.get(i);
                Pair<type, Integer> old_;
                if (old instanceof IRVar var) {
                    old_ = var2regOrMem.get(var.name);
                } else {
                    old_ = new Pair<>(type.num, old.getVal());
                }
                bl2permute.computeIfAbsent(label, _ -> new HashMap<>()).put(new_, old_);
            }
        }
        for (var entry : bl2permute.entrySet()) {
            text_new t_ = bl2text.get(entry.getKey());
            HashMap<Pair<type, Integer>, Pair<type, Integer>> new2old = entry.getValue();
            permute(new2old, t_);
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
    
    private void buildInst(instNode it, text_new t) {
        if (it instanceof allocaInstNode is) {
            assert false;
        } else if (it instanceof binaryInstNode is) {
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
            inas(t);
            for (int i = 0; i < is.args.size(); i++) {
                if (i < 8) {
                    var src = src(is.args.get(i), t, "a" + i, true);
                } else {
                    var src = src(is.args.get(i), t, mem_reg1, false);
                    int d = -(is.args.size() - i) * 4;
                    sw = new Sw(src.b, "sp", d);
                    t.push(sw);
                }
            }
            t.push(new Call(is.funName));
            Lw lw = new Lw("ra", "sp", regOffset.get("ra"));
            t.push(lw);
            if (is.dest != null) {
                var dest = var2regOrMem.get(is.dest.name);
                assert dest != null;
                if (dest.a == type.reg) {
                    t.push(new Mv("x" + dest.b, "a0"));
                } else {
                    t.push(new Sw("a0", "sp", dest.b));
                }
            }
            outas(t);
        } else if (it instanceof getElementPtrInstNode is) {//一般而言，第一个变量是堆地址
            System.err.println("我默认长度全是4,即都是i32");
            var ptr = src(is.ptr, t, mem_reg1, true);
//            ldVar(is.ptr, "x28", t);
            for (int i = 0; i < is.tys.size(); i++) {
                var idx = src(is.idxs.get(i), t, mem_reg2, false);
                t.push(new Arith("slli", tmp_reg, idx.b, "2"));
                t.push(new Arith("add", mem_reg1, mem_reg1, tmp_reg));
            }
            mv(is.dest, mem_reg1, t);
        } else if (it instanceof icmpInstNode is) {
//            ldVar(is.lhs, "x28", t);
//            ldVar(is.rhs, "x29", t);
            var lhs = src(is.lhs, t, mem_reg1, false);
            var rhs = src(is.rhs, t, mem_reg2, false);
            Arith arith = new Arith("sub", "x30", lhs.b, rhs.b);
            t.push(arith);
            switch (is.op) {
                case eq -> t.push(new S_z("seqz", "x30", "x30"));
                case ne -> t.push(new S_z("snez", "x30", "x30"));
                case sgt -> t.push(new S_z("sgtz", "x30", "x30"));
                case sge -> {
                    t.push(new S_z("sgtz", lhs.b, "x30"));
                    t.push(new S_z("seqz", rhs.b, "x30"));
                    t.push(new Arith("or", "x30", lhs.b, rhs.b));
                }
                case slt -> t.push(new S_z("sltz", "x30", "x30"));
                case sle -> {
                    t.push(new S_z("sltz", lhs.b, "x30"));
                    t.push(new S_z("seqz", rhs.b, "x30"));
                    t.push(new Arith("or", "x30", lhs.b, rhs.b));
                }
            }
            mv(is.dest.name, "x30", t);
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
                var src = src(is.value, t, "x10", true);
            }
            for (int i = 27; i <= 31; i++) {
                t.push(new Lw("x" + i, "sp", regOffset.get("x" + i)));
            }
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
        for (int i = 27; i <= 31; i++) {
            regOffset.put("x" + i, (++offset) * 4);
        }
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
        Sw sw = new Sw("x28", "sp", regOffset.get("x28"));
        t.push(sw);
        sw = new Sw("x29", "sp", regOffset.get("x29"));
        t.push(sw);
        sw = new Sw("x30", "sp", regOffset.get("x30"));
        t.push(sw);
//        sw = new Sw("ra", "sp", regOffset.get("ra"));
//        t.push(sw);
//        sw = new Sw("a0", "sp", regOffset.get("a0"));
//        t.push(sw);
    }
    
    private void buildFun(IRFunDef it) {
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
                if (-stackSize <= 2047 && -stackSize >= -2048) {
                    Arithimm arithimm = new Arithimm("addi", "sp", "sp", -stackSize);
                    t.push(arithimm);
                } else {
                    Li li = new Li(spare_reg, -stackSize);
                    t.push(li);
                    Arith arith = new Arith("add", "sp", "x28", "sp");
                    t.push(arith);
                }
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
                for (int j = 3; j < 3 + K; j++) {
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
    
    void permute(HashMap<Pair<type, Integer>, Pair<type, Integer>> new2old, text_new t) {
        HashMap<Pair<type, Integer>, Pair<type, Integer>> old2new = new HashMap<>();
        for (var entry : new2old.entrySet()) {
            old2new.put(entry.getValue(), entry.getKey());
        }
        HashSet<Pair<type, Integer>> visited = new HashSet<>();
        for (var entry : old2new.entrySet()) {
            var old = entry.getKey();
            if (visited.contains(old)) {
                continue;
            }
            visited.add(old);
            var new_ = entry.getValue();
            if (old.equals(new_)) {
                continue;
            }
            ArrayList<Pair<type, Integer>> queue = new ArrayList<>();
            queue.add(old);
            queue.add(new_);
            while (true) {
                var tmp = old2new.get(queue.getLast());
                if (tmp == null) {
                    mv(queue.getLast(), new Pair<>(type.reg, get_reg_int(mem_reg2)), t);
                    mvs(queue, t);
                    mv(new Pair<>(type.reg, get_reg_int(mem_reg2)), queue.getFirst(), t);
                    break;
                } else {
                    if (queue.getFirst().equals(tmp)) {
                        
                        mvs(queue, t);
                        break;
                    }
                    queue.add(tmp);
                    visited.add(tmp);
                }
            }
        }
    }
    
    void mvs(ArrayList<Pair<type, Integer>> queue, text_new t) {
        for (int i = queue.size() - 1; i > 0; i--) {
            var old = queue.get(i);
            var new_ = queue.get(i + 1);
            mv(old, new_, t);
        }
    }
    
    void mv(Pair<type, Integer> old, Pair<type, Integer> new_, text_new t) {
        if (old.a == type.reg && new_.a == type.reg) {
            t.push(new Mv("x" + new_.b, "x" + old.b));
        } else if (old.a == type.mem && new_.a == type.reg) {
            t.push(new Lw("x" + new_.b, "sp", old.b));
        } else if (old.a == type.reg && new_.a == type.mem) {
            t.push(new Sw("x" + old.b, "sp", new_.b));
        } else if (old.a == type.mem && new_.a == type.mem) {
            t.push(new Lw(mem_reg1, "sp", old.b));
            t.push(new Sw(mem_reg1, "sp", new_.b));
        } else if (old.a == type.num && new_.a == type.reg) {
            t.push(new Li("x" + new_.b, old.b));
        } else if (old.a == type.num && new_.a == type.mem) {
            t.push(new Li(mem_reg1, old.b));
            t.push(new Sw(mem_reg1, "sp", new_.b));
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
        HashMap<Pair<type, Integer>, Pair<type, Integer>> new2old = new HashMap<>();
        for (var entry : old_para_pos.entrySet()) {
            String para = entry.getKey();
            Pair<type, Integer> old = entry.getValue();
            Pair<type, Integer> new_ = var2regOrMem.get(para);
            assert new_ != null;
            new2old.put(new_, old);
        }
        permute(new2old, t);
    }
}