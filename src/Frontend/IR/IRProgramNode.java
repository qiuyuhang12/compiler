package Frontend.IR;

import Frontend.IR.node.IRNode;
import Frontend.IR.node.def.*;

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
    
    public String initCall(){
        StringBuilder sb = new StringBuilder();
        sb.append("define void @_.init() {\n");
        for (IRFunDef initFun : initFuns) {
            sb.append("call void ").append(initFun.name).append("()").append("\n");
        }
        sb.append("ret void\n");
        sb.append("}\n");
        return sb.toString();
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
        for (IRFunDef initFun : initFuns) {
            sb.append(initFun.toString()).append("\n");
        }
        sb.append(initCall());
        for (IRFunDeclare funDecl : funDecls) {
            sb.append(funDecl.toString()).append("\n");
        }
        for (IRFunDef funDef : funDefs) {
            sb.append(funDef.toString()).append("\n");
        }
        return sb.toString() + "\n";
    }
}
