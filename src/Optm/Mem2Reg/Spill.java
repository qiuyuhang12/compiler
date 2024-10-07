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
    public Spill(final int K,IRFunDef fun, HashMap<String, IRBlockNode> bl) {
        Spill.K = K;
        this.fun = fun;
        this.bl = bl;
    }
    public void run() {
        for (var block : fun.blocks) {
            if (block.phi_live_out.size() > K) {
                var plo = block.phi_live_out;
                for (var var: block.phi_live_out) {
                    HashSet<String> remain = (new HashSet<>(plo));
                    remain.removeAll(spill);//todo：未考虑phi的连带溢出
                    if (remain.size()>K){
                        int cnt = remain.size()-K;
                        for (var var2:remain){
                            if (cnt==0) break;
                            cnt--;
                            spill.add(var2);//todo:未考虑phi的连带溢出
                        }
                    }
                }
            }
            for (var inst : block.insts) {
            
            }
        }
    }
    
}
