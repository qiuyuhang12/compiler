package Optm.Mem2Reg;

import Frontend.IR.node.def.IRFunDef;
import Frontend.IR.node.stmt.IRBlockNode;

import java.lang.constant.Constable;
import java.util.HashMap;
import java.util.HashSet;

public class Spill {
    public IRFunDef fun;
    public HashMap<String, IRBlockNode> bl = new HashMap<>();//label to block
    public static int K;
    //    public HashMap<String, Integer> degree = new HashMap<>();
    public HashSet<String> spill = new HashSet<>();
    
    public Spill(final int K, IRFunDef fun, HashMap<String, IRBlockNode> bl) {
        Spill.K = K;
        this.fun = fun;
        this.bl = bl;
    }
    
    public void run() {
        for (var block : fun.blocks) {
            do_spill(block.phi_live_out, block.plo_after_sp);
            for (var inst : block.insts) {
                do_spill(inst.live_out, inst.lo_after_sp);
            }
        }
    }
    
    void do_spill(HashSet<String> lo, HashSet<String> lo_af) {
        lo_af.addAll(lo);
        lo_af.removeAll(spill);
        if (lo_af.size() > K) {
            int cnt = lo_af.size() - K;
            HashSet<String> tmp = new HashSet<>();
            for (var var : lo_af) {
                if (cnt == 0) break;
                tmp.add(var);
                cnt--;
            }
            lo_af.removeAll(tmp);
        }
    }
}
