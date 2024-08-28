package Frontend.IR;

import Frontend.IR.node.IRNode;
import Frontend.IR.node.def.*;
import Frontend.IR.node.inst.callInstNode;
import Frontend.IR.node.inst.retInstNode;
import Frontend.IR.node.stmt.IRBlockNode;
import Frontend.IR.type.IRType;

import java.util.ArrayList;

public class IRProgramNode extends IRNode {
    public ArrayList<IRFunDeclare> funDecls = new ArrayList<>();
    public ArrayList<IRFunDef> funDefs = new ArrayList<>();
    public ArrayList<IRFunDef> initFuns = new ArrayList<>();
    public ArrayList<IRNode> globalVarDefs = new ArrayList<>();
    public ArrayList<IRStructDef> structDefs = new ArrayList<>();
    public ArrayList<IRStringDef> stringDefs = new ArrayList<>();

    public void pushVarDef(IRNode varDef) {
        globalVarDefs.add(varDef);
    }

    public void pushFunDef(IRFunDef funDef) {
        funDefs.add(funDef);
    }

    public void pushFunDecl(IRFunDeclare funDecl) {
        funDecls.add(funDecl);
    }

    public void pushStructDef(IRStructDef structDef) {
        structDefs.add(structDef);
    }

    public void pushInitFun(IRFunDef funDef) {
        initFuns.add(funDef);
    }

    public void pushStringDef(IRStringDef stringDef) {
        stringDefs.add(stringDef);
    }
    
    public void initCall(){
        IRFunDef initFun = new IRFunDef(new IRType(),"@_.init");
        initFun.blocks.add(new IRBlockNode(null,initFun,"entry"));
        for (IRFunDef funDef : initFuns) {
            callInstNode callInstNode = new callInstNode(null,initFun.blocks.getFirst(),funDef.name.substring(1));
            initFun.blocks.getFirst().push(callInstNode);
        }
        initFun.blocks.getFirst().push(new retInstNode(null,initFun.blocks.getFirst()));
        funDefs.add(initFun);
//        return initFun.toString();
//        StringBuilder sb = new StringBuilder();
//        sb.append("define void @_.init() {\n");
//        for (IRFunDef initFun : initFuns) {
//            sb.append("call void ").append(initFun.name).append("()").append("\n");
//        }
//        sb.append("ret void\n");
//        sb.append("}\n");
//        return sb.toString();
    }
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (IRStructDef structDef : structDefs) {
            sb.append(structDef.toString()).append("\n");
        }
        for (IRNode globalVarDef : globalVarDefs) {
            sb.append(globalVarDef.toString()).append("\n");
        }
        for (IRStringDef stringDef : stringDefs) {
            sb.append(stringDef.toString()).append("\n");
        }
        initCall();
        for (IRFunDef initFun : initFuns) {
            sb.append(initFun.toString()).append("\n");
        }
//        sb.append(initCall());
        for (IRFunDeclare funDecl : funDecls) {
            sb.append(funDecl.toString()).append("\n");
        }
        for (IRFunDef funDef : funDefs) {
            sb.append(funDef.toString()).append("\n");
        }
        sb.append("""
                declare ptr @malloc(i32)
                declare void @print(ptr)
                declare void @println(ptr %0)
                declare void @printInt(i32 %0)
                declare void @printlnInt(i32 %0)
                declare ptr @getString()
                declare i32 @getInt()
                declare ptr @toString(i32 %0)
                declare ptr @bool_toString(i1 %0)
                declare ptr @alloca_helper(i32 %0, i32 %1)
                declare i32 @array.size(ptr %0)
                declare i32 @string.length(ptr %0)
                declare ptr @string.substring(ptr %0, i32 %1, i32 %2)
                declare i32 @string.parseInt(ptr %0)
                declare i32 @string.compare(ptr %0, ptr %1)
                declare ptr @string.concat(ptr %0, ptr %1)
                declare void @string.copy(ptr %0, ptr %1)
                declare i32 @string.ord(ptr %0, i32 %1)
                declare ptr @array.alloca_inside(i32 %0, i32 %1, ptr %2, i32 %3)
                declare ptr @array.alloca(i32 %0, i32 %1, i32 %2, ...)""");
        return sb.toString() + "\n";
    }
}
