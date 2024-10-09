package ASM;

import ASM.inst.*;
import ASM.section.data;
import ASM.section.rodata;
import ASM.section.text;
import Frontend.IR.IRProgramNode;
import Frontend.IR.entity.*;
import Frontend.IR.node.IRNode;
import Frontend.IR.node.def.IRFunDef;
import Frontend.IR.node.def.IRGlobalVarDef;
import Frontend.IR.node.def.IRStringDef;
import Frontend.IR.node.inst.*;
import Frontend.IR.node.stmt.IRBlockNode;
import Frontend.IR.type.IRType;

import java.util.ArrayList;
import java.util.HashMap;

public class Reg_al_asm {
    public IRProgramNode ir;
    public prog asm;
    public HashMap<String, Integer> varOffset = new HashMap<>();//变量->栈sp偏移,包含大于8个参数的参数
    public HashMap<String, Integer> regOffset = new HashMap<>();//变量->栈sp偏移
    public ArrayList<Inst> fromAlloc = new ArrayList<>();//alloca指令
    public HashMap<String, String> paraReg = new HashMap<>();//参数->寄存器
    public int stackSize = 0;
    public String nowFun = null;
    
    public Reg_al_asm(IRProgramNode ir) {
        this.ir = ir;
        asm = new prog();
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
    
    public void ldVar(IREntity entity, String reg, text t) {
        if (entity instanceof IRLiteral) {
            t.push(new Li(reg, entity.getVal()));
        } else if (entity instanceof IRVar var) {
            if (var.name.charAt(0) == '@') {
                t.push(new La(reg, var.name));
            } else {
                if (var.name.equals("null")) {
                    t.push(new Li(reg, 0));
                    return;
                }
                if (!(var.name.charAt(0) == '%')) {
                    t.push(new Li(reg, Integer.parseInt(var.name)));
                    return;
                }
                if (paraReg.containsKey(var.name)) {
                    t.push(new Mv(reg, paraReg.get(var.name)));
                } else {
                    int offset = varOffset.get(var.name);
                    t.push(new Lw(reg, "sp", offset));
                }
            }
        } else {
            assert false;
        }
    }
    
    public void ldVar(String entity, String reg, text t) {
        if (entity.charAt(0) != '@' && entity.charAt(0) != '%') {
            t.push(new Li(reg, Integer.parseInt(entity)));
        } else {
            if (entity.charAt(0) == '@') {
                t.push(new La(reg, entity));
            } else {
                assert entity.charAt(0) == '%';
                if (paraReg.containsKey(entity)) {
                    t.push(new Mv(reg, paraReg.get(entity)));
                } else {
                    int offset = varOffset.get(entity);
                    t.push(new Lw(reg, "sp", offset));
                }
            }
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
    
    private void inas(text t) {
        t.push(new Sw("a0", "sp", regOffset.get("a0")));
        t.push(new Sw("a1", "sp", regOffset.get("a1")));
        t.push(new Sw("a2", "sp", regOffset.get("a2")));
        t.push(new Sw("a3", "sp", regOffset.get("a3")));
        t.push(new Sw("a4", "sp", regOffset.get("a4")));
        t.push(new Sw("a5", "sp", regOffset.get("a5")));
        t.push(new Sw("a6", "sp", regOffset.get("a6")));
        t.push(new Sw("a7", "sp", regOffset.get("a7")));
    }
    
    private void outas(text t) {
        t.push(new Lw("a0", "sp", regOffset.get("a0")));
        t.push(new Lw("a1", "sp", regOffset.get("a1")));
        t.push(new Lw("a2", "sp", regOffset.get("a2")));
        t.push(new Lw("a3", "sp", regOffset.get("a3")));
        t.push(new Lw("a4", "sp", regOffset.get("a4")));
        t.push(new Lw("a5", "sp", regOffset.get("a5")));
        t.push(new Lw("a6", "sp", regOffset.get("a6")));
        t.push(new Lw("a7", "sp", regOffset.get("a7")));
    }
    
    private void buildInst(instNode it, text t) {
        if (it instanceof allocaInstNode is) {
        } else if (it instanceof binaryInstNode is) {
            ldVar(is.lhs, "t0", t);
            ldVar(is.rhs, "t1", t);
            switch (is.op) {
                case add -> t.push(new Arith("add", "t2", "t0", "t1"));
                case sub -> t.push(new Arith("sub", "t2", "t0", "t1"));
                case mul -> t.push(new Arith("mul", "t2", "t0", "t1"));
                case sdiv -> t.push(new Arith("div", "t2", "t0", "t1"));
                case srem -> t.push(new Arith("rem", "t2", "t0", "t1"));
                case shl -> t.push(new Arith("sll", "t2", "t0", "t1"));
                case ashr -> t.push(new Arith("sra", "t2", "t0", "t1"));
                case and -> t.push(new Arith("and", "t2", "t0", "t1"));
                case or -> t.push(new Arith("or", "t2", "t0", "t1"));
                case xor -> t.push(new Arith("xor", "t2", "t0", "t1"));
            }
            int offset = varOffset.get(is.dest.name);
            t.push(new Sw("t2", "sp", offset));
        } else if (it instanceof brInstNode is) {
            if (is.cond != null) {
                ldVar(is.cond, "t0", t);
                t.push(new Br("bne", "t0", "x0", ".+8"));
                t.push(new Jump(nowFun + "_" + is.ifFalse));
                t.push(new Jump(nowFun + "_" + is.ifTrue));
//                t.push(new Br("beq", "t0", "x0", nowFun + "_" + is.ifFalse));
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
                    ldVar(is.args.get(i), "a" + i, t);
                } else {
                    ldVar(is.args.get(i), "t0", t);
                    int d = -(is.args.size() - i) * 4;
                    sw = new Sw("t0", "sp", d);
                    t.push(sw);
                }
            }
            t.push(new Call(is.funName));
            Lw lw = new Lw("ra", "sp", regOffset.get("ra"));
            t.push(lw);
            if (is.dest != null) {
                int offset = varOffset.get(is.dest.name);
                t.push(new Sw("a0", "sp", offset));
            }
            outas(t);
        } else if (it instanceof getElementPtrInstNode is) {//一般而言，第一个变量是堆地址
            System.err.println("我默认长度全是4,即都是i32");
            ldVar(is.ptr, "t0", t);
            for (int i = 0; i < is.tys.size(); i++) {
                ldVar(is.idxs.get(i), "t1", t);
                Li li = new Li("t2", 4);
                t.push(li);
                t.push(new Arith("mul", "t1", "t1", "t2"));
                t.push(new Arith("add", "t0", "t0", "t1"));
            }
            int offset = varOffset.get(is.dest);
            t.push(new Sw("t0", "sp", offset));
        } else if (it instanceof icmpInstNode is) {
            ldVar(is.lhs, "t0", t);
            ldVar(is.rhs, "t1", t);
            Arith arith = new Arith("sub", "t2", "t0", "t1");
            t.push(arith);
            switch (is.op) {
                case eq -> t.push(new S_z("seqz", "t2", "t2"));
                case ne -> t.push(new S_z("snez", "t2", "t2"));
                case sgt -> t.push(new S_z("sgtz", "t2", "t2"));
                case sge -> {
                    t.push(new S_z("sgtz", "t0", "t2"));
                    t.push(new S_z("seqz", "t1", "t2"));
                    t.push(new Arith("or", "t2", "t0", "t1"));
                }
                case slt -> t.push(new S_z("sltz", "t2", "t2"));
                case sle -> {
                    t.push(new S_z("sltz", "t0", "t2"));
                    t.push(new S_z("seqz", "t1", "t2"));
                    t.push(new Arith("or", "t2", "t0", "t1"));
                }
            }
            int offset = varOffset.get(is.dest.name);
            t.push(new Sw("t2", "sp", offset));
        } else if (it instanceof loadInstNode is) {
            ldVar(is.ptr, "t0", t);
//            Lw lw = new Lw("t0", "sp", varOffset.get(is.ptr));
            Lw lw1 = new Lw("t0", "t0", 0);
//            t.push(lw);
            t.push(lw1);
            int offset = varOffset.get(is.dest);
            t.push(new Sw("t0", "sp", offset));
        } else if (it instanceof retInstNode is) {
            if (is.value.typeInfo.type != IRType.IRTypeEnum.void_) {
                ldVar(is.value, "a0", t);
            }
            Lw lw0 = new Lw("t0", "sp", regOffset.get("t0"));
            Lw lw1 = new Lw("t1", "sp", regOffset.get("t1"));
            Lw lw2 = new Lw("t2", "sp", regOffset.get("t2"));
            t.push(lw0);
            t.push(lw1);
            t.push(lw2);
            if (stackSize <= 2047 && stackSize >= -2048) {
                Arithimm arithimm = new Arithimm("addi", "sp", "sp", stackSize);
                t.push(arithimm);
            } else {
                Li li = new Li("t0", stackSize);
                t.push(li);
                Arith arith = new Arith("add", "sp", "t0", "sp");
                t.push(arith);
            }
            t.push(new Ret());
        } else if (it instanceof storeInstNode is) {
            ldVar(is.value, "t0", t);
            ldVar(is.ptr, "t1", t);
//            int offset = varOffset.get(is.ptr.name);
//            Lw lw = new Lw("t1", "sp", offset);
//            t.push(lw);
            t.push(new Sw("t0", "t1", 0));
        } else {
            assert false;
        }
    }
    
    private void paraHandle(IRFunDef it, text t) {
        for (int i = 0; i < it.parameters.size(); i++) {
            String para = it.parameters.get(i).name;
            if (i < 8) {
                paraReg.put(para, "a" + i);
            } else {
                int d = stackSize - (it.parameters.size() - i) * 4;
                varOffset.put(para, d);
            }
        }
    }
    
    private void collectVar(IRFunDef it) {
        varOffset.clear();
        regOffset.clear();
        fromAlloc.clear();
        paraReg.clear();
        stackSize = 0;
        int offset = 1;
        regOffset.put("t0", (++offset) * 4);
        regOffset.put("t1", (++offset) * 4);
        regOffset.put("t2", (++offset) * 4);
        regOffset.put("ra", (++offset) * 4);
        regOffset.put("a0", (++offset) * 4);
        regOffset.put("a1", (++offset) * 4);
        regOffset.put("a2", (++offset) * 4);
        regOffset.put("a3", (++offset) * 4);
        regOffset.put("a4", (++offset) * 4);
        regOffset.put("a5", (++offset) * 4);
        regOffset.put("a6", (++offset) * 4);
        regOffset.put("a7", (++offset) * 4);
        for (IRBlockNode bl : it.blocks) {
            for (instNode inst : bl.insts) {
                if (inst instanceof allocaInstNode allo) {
                    int d = (++offset) * 4, d_val = (++offset) * 4;
                    Comment comment = new Comment(allo.toString().substring(0, allo.toString().length() - 1));
                    fromAlloc.add(comment);
                    if (d_val <= 2047 && d_val >= -2048) {
                        Arithimm arithimm = new Arithimm("addi", "t0", "sp", d_val);
                        fromAlloc.add(arithimm);
                    } else {
                        Li li = new Li("t0", d_val);
                        fromAlloc.add(li);
                        Arith arith = new Arith("add", "t0", "t0", "sp");
                        fromAlloc.add(arith);
                    }
                    Sw sw = new Sw("t0", "sp", d);
                    fromAlloc.add(sw);
                    varOffset.put(allo.dest.name, d);
                    varOffset.put(allo.dest.name + "_val", d_val);
                } else {
                    String dest = inst.getVal();
                    if (dest != null) {
                        varOffset.put(dest, (++offset) * 4);
                    }
                }
            }
        }
//        offset+=3*4;//t0-t2,ra,sp,a0
        int cnt = (it.parameters.size() - 8);
        cnt = Math.max(cnt, 0);
        offset += cnt;
//        assert offset % 4 == 0;
        stackSize = offset * 4 + 4;
    }
    
    //约定：被调用者保存寄存器t0-t2
    //约定：调用者保存寄存器ra
    
    private void storeReg(text t) {
        Sw sw = new Sw("t0", "sp", regOffset.get("t0"));
        t.push(sw);
        sw = new Sw("t1", "sp", regOffset.get("t1"));
        t.push(sw);
        sw = new Sw("t2", "sp", regOffset.get("t2"));
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
            text t;
//            if (funName.equals("main") && block.label.equals("entry")) {
//                t = new text("main");
            if (block.label.equals("entry")) {
                t = new text(funName);
            } else {
                t = new text(funName + "_" + block.label);
            }
            if (block.label.equals("entry")) {
                collectVar(it);
                if (-stackSize <= 2047 && -stackSize >= -2048) {
                    Arithimm arithimm = new Arithimm("addi", "sp", "sp", -stackSize);
                    t.push(arithimm);
                } else {
                    Li li = new Li("t6", -stackSize);
                    t.push(li);
                    Arith arith = new Arith("add", "sp", "t0", "sp");
                    t.push(arith);
                }
                storeReg(t);
                for (Inst inst : fromAlloc) {
                    t.push(inst);
                }
                paraHandle(it, t);
            }
            for (instNode stmt : block.insts) {
                //done:添加注释
                //done:处理判断是参数还是变量
                //done:判断变量是全局变量还是局部变量
                //done：la
                if (!(stmt instanceof allocaInstNode)) {
                    Comment comment = new Comment(stmt.toString().substring(0, stmt.toString().length() - 1));
                    t.push(comment);
                }
                buildInst(stmt, t);
            }
            asm.pushText(t);
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
}
