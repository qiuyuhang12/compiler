//package ASM;
//
//import ASM.inst.*;
//import ASM.section.data;
//import ASM.section.rodata;
//import ASM.section.text;
//import Frontend.IR.IRProgramNode;
//import Frontend.IR.entity.*;
//import Frontend.IR.node.IRNode;
//import Frontend.IR.node.def.IRFunDef;
//import Frontend.IR.node.def.IRGlobalVarDef;
//import Frontend.IR.node.def.IRStringDef;
//import Frontend.IR.node.inst.*;
//import Frontend.IR.node.stmt.IRBlockNode;
//import Frontend.IR.type.IRType;
//import Optm.Mem2Reg.Spill;
//import org.antlr.v4.runtime.misc.Pair;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.HashSet;
//
//public class Reg_al_asm {
//    public IRProgramNode ir;
//    public prog asm;
//    HashMap<String, Pair<type, Integer>> var2regOrMem = new HashMap<>();
//    HashMap<String, Integer> regOffset = new HashMap<>();//reg存储->栈sp偏移
//    //    public ArrayList<Inst> fromAlloc = new ArrayList<>();//alloca指令
////    public HashMap<String, String> paraReg = new HashMap<>();//参数->寄存器
//    int stackSize = 0;
//    String nowFun = null;
//    static int K;
//    HashSet<Integer> used_reg = new HashSet<>();
//
//    enum type {
//        reg, mem
//    }
//
//
//    public Reg_al_asm(IRProgramNode ir, int k) {
//        this.ir = ir;
//        asm = new prog();
//        K = k;
//    }
//
//    public void print() {
//        System.out.println(asm.toString());
//    }
//
//    private int literalToInt(IRLiteral it) {
//        if (it instanceof IRIntLiteral) {
//            return ((IRIntLiteral) it).value;
//        } else if (it instanceof IRBoolLiteral) {
//            return ((IRBoolLiteral) it).value ? 1 : 0;
//        } else if (it instanceof IRNullPtrLiteral) {
//            return 0;
//        } else {
//            assert false;
//            return 0;
//        }
//    }
//
//    public void ldVar(IREntity entity, String reg, text t) {
//        if (entity instanceof IRLiteral) {
//            t.push(new Li(reg, entity.getVal()));
//        } else if (entity instanceof IRVar var) {
//            if (var.name.charAt(0) == '@') {
//                t.push(new La(reg, var.name));
//            } else {
//                if (var.name.equals("null")) {
//                    t.push(new Li(reg, 0));
//                    return;
//                }
//                if (!(var.name.charAt(0) == '%')) {
//                    t.push(new Li(reg, Integer.parseInt(var.name)));
//                    return;
//                }
//                var pos = var2regOrMem.get(var.name);
//                assert pos != null;
//                if (pos.a == type.reg) {
//                    t.push(new Mv(reg, "x" + pos.b));
//                } else {
//                    int offset = pos.b;
//                    t.push(new Lw(reg, "sp", offset));
//                }
//
//            }
//        } else {
//            assert false;
//        }
//    }
//
//    public void ldVar(String entity, String reg, text t) {
//        if (entity.charAt(0) != '@' && entity.charAt(0) != '%') {
//            t.push(new Li(reg, Integer.parseInt(entity)));
//        } else {
//            if (entity.charAt(0) == '@') {
//                t.push(new La(reg, entity));
//            } else {
//                assert entity.charAt(0) == '%';
//                var pos = var2regOrMem.get(entity);
//                assert pos != null;
//                if (pos.a == type.reg) {
//                    t.push(new Mv(reg, "x" + pos.b));
//                } else {
//                    int offset = pos.b;
//                    t.push(new Lw(reg, "sp", offset));
//                }
//            }
//        }
//    }
//
//    private void buildGlobalVar(IRGlobalVarDef it) {//不去掉@，加入.data
//        data d = new data(it.name, literalToInt(it.value));
//        asm.pushData(d);
//    }
//
//    private void buildString(IRStringDef it) {
//        rodata r = new rodata(it.name, it.value);
//        asm.pushRodata(r);
//    }
//
//    private void inas(text t) {
//        for (Integer i : used_reg) {
//            t.push(new Sw("x" + i, "sp", regOffset.get("x" + i)));
//        }
//    }
//
//    private void outas(text t) {
//        for (Integer i : used_reg) {
//            t.push(new Lw("x" + i, "sp", regOffset.get("x" + i)));
//        }
//    }
//
//    private void buildInst(instNode it, text t) {
//        if (it instanceof allocaInstNode is) {
//            assert false;
//        } else if (it instanceof binaryInstNode is) {
//            ldVar(is.lhs, "t0", t);
//            ldVar(is.rhs, "t1", t);
//            switch (is.op) {
//                case add -> t.push(new Arith("add", "t2", "t0", "t1"));
//                case sub -> t.push(new Arith("sub", "t2", "t0", "t1"));
//                case mul -> t.push(new Arith("mul", "t2", "t0", "t1"));
//                case sdiv -> t.push(new Arith("div", "t2", "t0", "t1"));
//                case srem -> t.push(new Arith("rem", "t2", "t0", "t1"));
//                case shl -> t.push(new Arith("sll", "t2", "t0", "t1"));
//                case ashr -> t.push(new Arith("sra", "t2", "t0", "t1"));
//                case and -> t.push(new Arith("and", "t2", "t0", "t1"));
//                case or -> t.push(new Arith("or", "t2", "t0", "t1"));
//                case xor -> t.push(new Arith("xor", "t2", "t0", "t1"));
//            }
//            int offset = varOffset.get(is.dest.name);
//            t.push(new Sw("t2", "sp", offset));
//        } else if (it instanceof brInstNode is) {
//            if (is.cond != null) {
//                ldVar(is.cond, "t0", t);
//                t.push(new Br("bne", "t0", "x0", ".+8"));
//                t.push(new Jump(nowFun + "_" + is.ifFalse));
//                t.push(new Jump(nowFun + "_" + is.ifTrue));
////                t.push(new Br("beq", "t0", "x0", nowFun + "_" + is.ifFalse));
////                t.push(new Jump(nowFun + "_" + is.ifTrue));//可省略？？
//            } else {
//                t.push(new Jump(nowFun + "_" + is.dest));
//            }
//        } else if (it instanceof callInstNode is) {
//            //done:传参：a0-a7，ret：返回值：a0,恢复ra
//            Sw sw = new Sw("ra", "sp", regOffset.get("ra"));
//            t.push(sw);
//            inas(t);
//            for (int i = 0; i < is.args.size(); i++) {
//                if (i < 8) {
//                    ldVar(is.args.get(i), "a" + i, t);
//                } else {
//                    ldVar(is.args.get(i), "t0", t);
//                    int d = -(is.args.size() - i) * 4;
//                    sw = new Sw("t0", "sp", d);
//                    t.push(sw);
//                }
//            }
//            t.push(new Call(is.funName));
//            Lw lw = new Lw("ra", "sp", regOffset.get("ra"));
//            t.push(lw);
//            if (is.dest != null) {
//                int offset = varOffset.get(is.dest.name);
//                t.push(new Sw("a0", "sp", offset));
//            }
//            outas(t);
//        } else if (it instanceof getElementPtrInstNode is) {//一般而言，第一个变量是堆地址
//            System.err.println("我默认长度全是4,即都是i32");
//            ldVar(is.ptr, "t0", t);
//            for (int i = 0; i < is.tys.size(); i++) {
//                ldVar(is.idxs.get(i), "t1", t);
//                Li li = new Li("t2", 4);
//                t.push(li);
//                t.push(new Arith("mul", "t1", "t1", "t2"));
//                t.push(new Arith("add", "t0", "t0", "t1"));
//            }
//            int offset = varOffset.get(is.dest);
//            t.push(new Sw("t0", "sp", offset));
//        } else if (it instanceof icmpInstNode is) {
//            ldVar(is.lhs, "t0", t);
//            ldVar(is.rhs, "t1", t);
//            Arith arith = new Arith("sub", "t2", "t0", "t1");
//            t.push(arith);
//            switch (is.op) {
//                case eq -> t.push(new S_z("seqz", "t2", "t2"));
//                case ne -> t.push(new S_z("snez", "t2", "t2"));
//                case sgt -> t.push(new S_z("sgtz", "t2", "t2"));
//                case sge -> {
//                    t.push(new S_z("sgtz", "t0", "t2"));
//                    t.push(new S_z("seqz", "t1", "t2"));
//                    t.push(new Arith("or", "t2", "t0", "t1"));
//                }
//                case slt -> t.push(new S_z("sltz", "t2", "t2"));
//                case sle -> {
//                    t.push(new S_z("sltz", "t0", "t2"));
//                    t.push(new S_z("seqz", "t1", "t2"));
//                    t.push(new Arith("or", "t2", "t0", "t1"));
//                }
//            }
//            int offset = varOffset.get(is.dest.name);
//            t.push(new Sw("t2", "sp", offset));
//        } else if (it instanceof loadInstNode is) {
//            ldVar(is.ptr, "t0", t);
////            Lw lw = new Lw("t0", "sp", varOffset.get(is.ptr));
//            Lw lw1 = new Lw("t0", "t0", 0);
////            t.push(lw);
//            t.push(lw1);
//            int offset = varOffset.get(is.dest);
//            t.push(new Sw("t0", "sp", offset));
//        } else if (it instanceof retInstNode is) {
//            if (is.value.typeInfo.type != IRType.IRTypeEnum.void_) {
//                ldVar(is.value, "a0", t);
//            }
//            Lw lw0 = new Lw("t0", "sp", regOffset.get("t0"));
//            Lw lw1 = new Lw("t1", "sp", regOffset.get("t1"));
//            Lw lw2 = new Lw("t2", "sp", regOffset.get("t2"));
//            t.push(lw0);
//            t.push(lw1);
//            t.push(lw2);
//            if (stackSize <= 2047 && stackSize >= -2048) {
//                Arithimm arithimm = new Arithimm("addi", "sp", "sp", stackSize);
//                t.push(arithimm);
//            } else {
//                Li li = new Li("t0", stackSize);
//                t.push(li);
//                Arith arith = new Arith("add", "sp", "t0", "sp");
//                t.push(arith);
//            }
//            t.push(new Ret());
//        } else if (it instanceof storeInstNode is) {
//            ldVar(is.value, "t0", t);
//            ldVar(is.ptr, "t1", t);
////            int offset = varOffset.get(is.ptr.name);
////            Lw lw = new Lw("t1", "sp", offset);
////            t.push(lw);
//            t.push(new Sw("t0", "t1", 0));
//        } else {
//            assert false;
//        }
//    }
//
//
//    private void collectVar(IRFunDef it) {
//        HashMap<String, Integer> varOffset = new HashMap<>();//变量->栈sp偏移,包含大于8个参数的参数
//        regOffset.clear();
//        stackSize = 0;
//        var2regOrMem.clear();
//        int offset = 1;
//        regOffset.put("ra", (++offset) * 4);
//        for (var reg : used_reg) {
//            regOffset.put("x" + reg, (++offset) * 4);
//        }
//        stackSize = (offset + it.spill.size()) * 4 + 4;
////int d = stackSize - (it.parameters.size() - i) * 4;
////varOffset.put(para, d);
//        for (int i = 0; i < it.parameters.size(); i++) {
//            if (i >= 8) {
//                String para = it.parameters.get(i).name;
//                if (para.charAt(0) == '%') {
//                    if (it.spill.contains(para)) {
//                        varOffset.put(para, stackSize - (it.parameters.size() - i) * 4);
//                    }
//                }
//            }
//        }
//        for (String var : it.spill) {
//            if (!varOffset.containsKey(var)) {
//                varOffset.put(var, (++offset) * 4);
//            }
//        }
//        for (var entry : varOffset.entrySet()) {
//            var2regOrMem.put(entry.getKey(), new Pair<>(type.mem, entry.getValue()));
//        }
//    }
//
//    //约定：被调用者保存寄存器t0-t2
//    //约定：调用者保存寄存器ra
//
//    private void storeReg(text t) {
//        Sw sw = new Sw("t0", "sp", regOffset.get("t0"));
//        t.push(sw);
//        sw = new Sw("t1", "sp", regOffset.get("t1"));
//        t.push(sw);
//        sw = new Sw("t2", "sp", regOffset.get("t2"));
//        t.push(sw);
////        sw = new Sw("ra", "sp", regOffset.get("ra"));
////        t.push(sw);
////        sw = new Sw("a0", "sp", regOffset.get("a0"));
////        t.push(sw);
//    }
//
//    private void buildFun(IRFunDef it) {
//        String funName = it.name.substring(1);
//        nowFun = funName;
//        //ret：
//        //done:返回值传递
//        //done:恢复t0-t2，ra，sp（栈释放），a0;
//
//        for (IRBlockNode block : it.blocks) {
//            if (block == null) {
//                continue;
//            }
//            text t;
////            if (funName.equals("main") && block.label.equals("entry")) {
////                t = new text("main");
//            if (block.label.equals("entry")) {
//                t = new text(funName);
//            } else {
//                t = new text(funName + "_" + block.label);
//            }
//            if (block.label.equals("entry")) {
//                collectVar(it);
//                if (-stackSize <= 2047 && -stackSize >= -2048) {
//                    Arithimm arithimm = new Arithimm("addi", "sp", "sp", -stackSize);
//                    t.push(arithimm);
//                } else {
//                    Li li = new Li("t6", -stackSize);
//                    t.push(li);
//                    Arith arith = new Arith("add", "sp", "t0", "sp");
//                    t.push(arith);
//                }
//                storeReg(t);
//            }
//            for (instNode stmt : block.insts) {
//                //done:添加注释
//                //done:处理判断是参数还是变量
//                //done:判断变量是全局变量还是局部变量
//                //done：la
//                assert !(stmt instanceof allocaInstNode);
//                Comment comment = new Comment(stmt.toString().substring(0, stmt.toString().length() - 1));
//                t.push(comment);
//                buildInst(stmt, t);
//            }
//            asm.pushText(t);
//        }
//    }
//
//    public void build() {
//        for (IRNode it : ir.globalVarDefs) {
//            buildGlobalVar((IRGlobalVarDef) it);
//        }
//        for (IRStringDef it : ir.stringDefs) {
//            buildString(it);
//        }
//        for (IRFunDef it : ir.initFuns) {
//            buildFun(it);
//        }
//        for (IRFunDef it : ir.funDefs) {
//            buildFun(it);
//        }
//    }
//
//    void color2reg(IRFunDef it) {
//        var spill = it.spill;
//        var color = it.tempMap;
//        HashMap<Integer, Integer> num2reg = new HashMap<>();
//        HashSet<Integer> alled_num = new HashSet<>();
//        for (int i = 0; i < it.parameters.size(); i++) {
//            if (i < 8) {
//                var para = it.parameters.get(i).toString();
//                if (para.charAt(0) == '%' && color.containsKey(para)) {
//                    num2reg.put(color.get(para), 10 + i);
//                    used_reg.add(10 + i);
//                    alled_num.add(color.get(para));
//                }
//            }
//        }
//        for (int i = 0; i < K; i++) {
//            if (color.containsValue(i) && !alled_num.contains(i)) {
//                for (int j = 3; j < 3 + K; j++) {
//                    if (!used_reg.contains(j)) {
//                        num2reg.put(i, j);
//                        used_reg.add(j);
//                        break;
//                    }
//                }
//            }
//        }
//        for (var entry : color.entrySet()) {
//            assert entry.getKey().charAt(0) == '%' && !spill.contains(entry.getKey());
//            int reg = num2reg.get(entry.getValue());
//            var2regOrMem.put(entry.getKey(), new Pair<>(type.reg, reg));
//        }
//        collectVar(it);
//    }
//
//    void permute(HashMap<Pair<type, Integer>, Pair<type, Integer>> new2old) {
//        HashMap<Pair<type, Integer>, Pair<type, Integer>> old2new = new HashMap<>();
//        for (var entry : new2old.entrySet()) {
//            old2new.put(entry.getValue(), entry.getKey());
//        }
//        HashSet<Pair<type, Integer>> visited = new HashSet<>();
//        for (var entry : old2new.entrySet()) {
//            var old = entry.getKey();
//            if (visited.contains(old)) {
//                continue;
//            }
//            visited.add(old);
//            var new_ = entry.getValue();
//            ArrayList<Pair<type, Integer>> queue = new ArrayList<>();
//            queue.add(old);
//            queue.add(new_);
//            while (true) {
//                var tmp = old2new.get(queue.getLast());
//                if (tmp == null) {
//                    break;
//                } else {
//                    if (queue.getFirst().equals(tmp)) {
//                        break;
//                    }
//                    queue.add(tmp);
//                    visited.add(tmp);
//                }
//            }
//        }
//    }
//
//    void mv(ArrayList<Pair<type, Integer>> queue, text t){
//        for (int i = 0; i < queue.size() - 1; i++) {
//            var old = queue.get(i);
//            var new_ = queue.get(i + 1);
//            if (old.a == type.reg && new_.a == type.reg) {
//                t.push(new Mv("x" + new_.b, "x" + old.b));
//            } else if (old.a == type.mem && new_.a == type.reg) {
//                t.push(new Lw("x" + new_.b, "sp", old.b));
//            } else if (old.a == type.reg && new_.a == type.mem) {
//                t.push(new Sw("x" + old.b, "sp", new_.b));
//            } else {
//                assert false;
//            }
//        }
//    }
//}
//
//
////"t6"