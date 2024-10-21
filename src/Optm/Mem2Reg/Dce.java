package Optm.Mem2Reg;

import Frontend.IR.node.def.IRFunDef;
import Frontend.IR.node.stmt.IRBlockNode;
import Frontend.IR.node.inst.*;
import Frontend.IR.util.Renamer;
import Util.Consts;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class Dce {
    IRFunDef fun;
    HashMap<String, IRBlockNode> bl = new HashMap<>();//label to block
    //    HashMap<String, ArrayList<String>> in = new HashMap<>();//label to in block
//    HashMap<String, ArrayList<String>> out = new HashMap<>();//label to out block
    HashMap<String, instNode> defIst = new HashMap<>();//label to def inst
    HashSet<String> useful = new HashSet<>();//label to useful var
    ArrayList<instNode> workList = new ArrayList<>();//work list
    HashSet<String> params = new HashSet<>();//params
    
    public Dce(IRFunDef fun, HashMap<String, IRBlockNode> bl) {
        this.fun = fun;
        this.bl = bl;
//        this.in = in;
//        this.out = out;
    }
    
    void run() {
        if (!Consts.dce) return;
        makeUseless();
        build();
        analyze();
        del();
    }
    void makeUseless() {
        for (IRBlockNode block : fun.blocks) {
            for (instNode inst : block.insts) {
                inst.useless = true;
            }
        }
    }
    
    void build() {
        for (IRBlockNode block : fun.blocks) {
            for (var param : fun.parameters) {
                params.add(param.name);
            }
            for (phiInstNode phi : block.phis) {
                var def = phi.getDef();
                if (def != null) {
                    defIst.put(def, phi);
                }
            }
            for (instNode inst : block.insts) {
                var def = inst.getDef();
                if (def != null) {
                    defIst.put(def, inst);
                }
                if (inst instanceof retInstNode || inst instanceof storeInstNode || inst instanceof callInstNode || inst instanceof brInstNode) {
                    inst.useless = false;
                    workList.add(inst);
                }
            }
        }
    }
    
    void analyze() {
        while (!workList.isEmpty()) {
            var inst = workList.getFirst();
            workList.removeFirst();
            var uses = inst.getUses();
            if (uses == null || uses.isEmpty()) continue;
            for (var use : uses) {
                if (useful.contains(use)) continue;
                useful.add(use);
                var _inst = defIst.get(use);
                if (_inst != null) {
                    _inst.useless = false;
                    workList.add(_inst);
                } else assert params.contains(use);
            }
        }
    }
    
    void del() {
        for (IRBlockNode block : fun.blocks) {
            for (int i = 0; i < block.insts.size(); i++) {
                var inst = block.insts.get(i);
                if (inst.useless) {
                    block.insts.remove(i);
                    i--;
                }
            }
            for (int i = 0; i < block.phis.size(); i++) {
                var inst = block.phis.get(i);
                if (inst.useless) {
                    block.phis.remove(i);
                    i--;
                }
            }
        }
    }
}
