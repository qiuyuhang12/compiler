package Frontend.IR;

import Frontend.IR.node.IRNode;
import Frontend.IR.node.def.IRFunDeclare;
import Frontend.IR.node.def.IRFunDef;
import Frontend.IR.node.def.IRGlobalVarDef;
import Frontend.IR.node.def.IRStructDef;

import java.util.ArrayList;

public class IRProgramNode extends IRNode {
    public ArrayList<IRFunDeclare> funDecls = new ArrayList<>();
    public ArrayList<IRFunDef> funDefs = new ArrayList<>();
    public ArrayList<IRFunDef> initFuns = new ArrayList<>();
    public ArrayList<IRGlobalVarDef> globalVarDefs = new ArrayList<>();
    public ArrayList<IRStructDef> structDefs = new ArrayList<>();

    public void pushVarDef(IRGlobalVarDef varDef) {
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

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (IRStructDef structDef : structDefs) {
            sb.append(structDef.toString()).append("\n");
        }
        for (IRGlobalVarDef globalVarDef : globalVarDefs) {
            sb.append(globalVarDef.toString()).append("\n");
        }
        for (IRFunDeclare funDecl : funDecls) {
            sb.append(funDecl.toString()).append("\n");
        }
        for (IRFunDef funDef : funDefs) {
            sb.append(funDef.toString()).append("\n");
        }
        //TODO:Init Function加入main
        assert false;
        return sb.toString() + "\n";
    }
}
