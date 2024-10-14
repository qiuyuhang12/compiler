package Optm.Mem2Reg;

import Frontend.IR.node.def.IRFunDef;
import Frontend.IR.node.stmt.IRBlockNode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class ColorChecker {
    public IRFunDef fun;
    //    public HashMap<String, IRBlockNode> bl = new HashMap<>();//label to block
    public static int K;
    public HashMap<String, Integer> tempMap = new HashMap<>();
    public HashSet<String> spill = new HashSet<>();
    
    public ColorChecker(final int K, IRFunDef fun, HashMap<String, IRBlockNode> bl, HashSet<String> spill) {
        ColorChecker.K = K;
        this.fun = fun;
//        this.bl = bl;
        this.spill = spill;
    }
    
    HashSet<String> vars = new HashSet<>();
    ArrayList<String> stack = new ArrayList<>();
    
    public void run() {
        var tmp = new HashSet<>(fun.spill);
        tmp.addAll(fun.tempMap.keySet());
        var tmp2_ = new HashSet<>(fun.spill);
        if (fun.spill.size() + fun.tempMap.size() > tmp.size()) {
            tmp2_.retainAll(fun.tempMap.keySet());
            System.err.println(tmp2_);
        }
        assert tmp.size() == fun.spill.size() + fun.tempMap.size();
        collectVars();
        if (vars.size() != fun.spill.size() + fun.tempMap.size()) {
            HashSet<String> tmp2;
            if (vars.size() > fun.spill.size() + fun.tempMap.size()) {
                tmp2 = new HashSet<>(vars);
                tmp2.removeAll(tmp);
            } else {
                tmp2 = new HashSet<>(tmp);
                tmp2.removeAll(vars);
            }
            System.err.println(tmp2);
            assert false;
        }
        for (var var : vars) {
            boolean isSpill = spill.contains(var);
            boolean isTemp = fun.tempMap.containsKey(var);
            assert isSpill ^ isTemp;
        }
    }
    
    void collectVars() {
        for (var block : fun.blocks) {
            for (var inst : block.insts) {
                var use = inst.getUses();
                var def = inst.getDef();
                if (use != null) vars.addAll(use);
                if (def != null) vars.add(def);
            }
            var def = block.get_phi_def();
            var use = block.get_phi_use();
            if (def != null)
                vars.addAll(def);
            if (use != null)
                vars.addAll(use.keySet());
        }
    }
}
