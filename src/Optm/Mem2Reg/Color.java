package Optm.Mem2Reg;

import Frontend.IR.node.def.IRFunDef;
import Frontend.IR.node.stmt.IRBlockNode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class Color {
    public IRFunDef fun;
    public HashMap<String, IRBlockNode> bl = new HashMap<>();//label to block
    public HashMap<String, String> idom = new HashMap<>();//label to idom block label
    public HashMap<String, HashSet<String>> dt = new HashMap<>();//label to son
    public static int K;
    public HashMap<String, Integer> tempMap = new HashMap<>();
    public HashSet<Integer> inUse = new HashSet<>();
    public ArrayList<Integer> stack = new ArrayList<>();
    
    public Color(final int K, IRFunDef fun, HashMap<String, IRBlockNode> bl, HashMap<String, String> idom) {
        Color.K = K;
        this.fun = fun;
        this.bl = bl;
        this.idom = idom;
    }
    
    public void run() {
        tree_build();
        ssa_color();
    }
    
    public void tree_build() {
        for (var entry : idom.entrySet()) {
            var label = entry.getKey();
            var idom = entry.getValue();
            if (!dt.containsKey(idom)) {
                dt.put(idom, new HashSet<>());
            }
            dt.get(idom).add(label);
        }
    }
    
    void ssa_color() {
        for (int i = K - 1; i >= 0; i--) {
            stack.add(i);
        }
        pre_order(fun.blocks.getFirst().label);
    }
    
    void pre_order(String b) {
        var B = bl.get(b);
        if (!B.live_in.isEmpty())
            for (var var : B.live_in) {
                inUse.add(tempMap.get(var));
            }
        for (var s : B.insts) {
            if (s.getUses() != null)
                for (var x : s.getUses()) {
                    if (!s.lo_after_sp.contains(x)) {
                        inUse.remove(tempMap.get(x));
                        stack.add(tempMap.get(x));
                    }
                }
            var y = s.getDef();
            if (y == null || !s.lo_after_sp.contains(y)) continue;
            assert !stack.isEmpty();
            tempMap.put(y, stack.getLast());
            inUse.add(stack.getLast());
            stack.removeLast();
        }
        if (dt.containsKey(b))
            for (var c : dt.get(b)) {
                pre_order(c);
            }
    }
}


//63,64,67,69,70,18,19,24,3,31,41,...