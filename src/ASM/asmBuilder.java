package ASM;

import ASM.inst.arith;
import ASM.inst.inst;
import ASM.section.data;
import ASM.section.rodata;
import ASM.section.text;
import Frontend.IR.IRProgramNode;
import Frontend.IR.entity.IRBoolLiteral;
import Frontend.IR.entity.IRIntLiteral;
import Frontend.IR.entity.IRLiteral;
import Frontend.IR.entity.IRNullPtrLiteral;
import Frontend.IR.node.IRNode;
import Frontend.IR.node.def.IRFunDef;
import Frontend.IR.node.def.IRGlobalVarDef;
import Frontend.IR.node.def.IRStringDef;
import Frontend.IR.node.inst.binaryInstNode;
import Frontend.IR.node.inst.instNode;
import Frontend.IR.node.stmt.IRBlockNode;

import java.util.HashMap;
import java.util.Vector;

public class asmBuilder {
    public IRProgramNode ir;
    public prog asm;
    public HashMap<String, Integer> varOffset = new HashMap<>();//变量->栈sp偏移
    
    public asmBuilder(IRProgramNode ir) {
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
    
    private void buildGlobalVar(IRGlobalVarDef it) {
        data d = new data(it.name, literalToInt(it.value));
        asm.pushData(d);
    }
    
    private void buildString(IRStringDef it) {
        rodata r = new rodata(it.name, it.value);
        asm.pushRodata(r);
    }
    
    private inst buildInst(instNode it) {
        inst res = null;
//        return res;
        return null;
    }
    private void paraHandle(IRFunDef it,text t){
    
    }
    private void collectVar(IRFunDef it){
        varOffset.clear();
        //todo:%,all
    }
    
    private void buildFun(IRFunDef it) {
        String funName = it.name;
        //todo:存t0-t2，ra，sp，a0;
        //todo:栈申请，分配与释放
        //todo:参数传递
        //todo:返回值传递
        //todo:全局变量
        for (IRBlockNode block : it.blocks) {
            if (block == null) {
                continue;
            }
            text t;
            if (funName.equals("main") && block.label.equals("entry")) {
                t = new text("main");
            } else {
                t = new text(funName + block.label);
            }
            for (instNode stmt : block.insts) {
                inst inst = buildInst(stmt);
                t.push(inst);
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
