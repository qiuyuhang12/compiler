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
    public HashMap<String, HashSet<String>> dt = new HashMap<>();//label to dom son
    public static int K;
    public HashSet<Integer> inUse = new HashSet<>();
    public ArrayList<Integer> stack = new ArrayList<>();
    public HashMap<String, Integer> tempMap = new HashMap<>();
    public HashSet<String> spill = new HashSet<>();
    
    
    public Color(final int K, IRFunDef fun, HashMap<String, IRBlockNode> bl, HashMap<String, String> idom, HashSet<String> spill) {
        Color.K = K;
        this.fun = fun;
        this.bl = bl;
        this.idom = idom;
        this.spill = spill;
    }
    
    public void run() {
        tree_build();
        ssa_color();
    }
    
    public void tree_build() {
        for (var entry : idom.entrySet()) {
            var label = entry.getKey();
            var idom = entry.getValue();
            if (idom == null) continue;
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
        for (var tmp : fun.blocks.getFirst().plo_after_sp) {
            assert tmp.charAt(0) == '%';
//            if (!spill.contains(tmp)) {
            int c = stack.getLast();
            tempMap.put(tmp, c);
            inUse.add(c);
            stack.remove((Integer) c);
//            }
//            if (i<K&&i<8) {
//                tempMap.put(tmp.name, i);
//                inUse.add(i);
//                stack.remove((Integer) i);
//            } else {
//                spill.add(tmp.name);
//            }
        }
        pre_order(fun.blocks.getFirst().label);
    }
    
    void pre_order(String b) {
        inUse.clear();
        var B = bl.get(b);
        B.plo_after_sp.removeAll(spill);
        HashSet<String> b_livein_after_spill;
//        if (B.phis.isEmpty()) {
        if (B.plo_after_sp.isEmpty()) {
            var tmp = B.insts.getFirst().lo_after_sp;
            tmp.removeAll(spill);
            b_livein_after_spill = new HashSet<>(B.insts.getFirst().lo_after_sp);
            b_livein_after_spill.remove(B.insts.getFirst().getDef());
            var tmp1 = B.insts.getFirst().getUses();
            if (tmp1 != null)
                b_livein_after_spill.addAll(tmp1);
        } else {
            b_livein_after_spill = new HashSet<>(B.plo_after_sp);
            b_livein_after_spill.removeAll(B.get_phi_def());
        }
        if (!b_livein_after_spill.isEmpty())
            for (var var : b_livein_after_spill) {
                assert tempMap.get(var) != null;
                Integer tmp = tempMap.get(var);
                stack.remove(tmp);
//                assert !stack.contains(tempMap.get(var));
                inUse.add(tempMap.get(var));
            }
        for (int i = 0; i < K; i++) {
            if (!inUse.contains(i) && !stack.contains(i)) {
                stack.add(i);
            }
        }
//        for (var var:b_livein_after_spill){
//            assert !stack.contains(tempMap.get(var));
//        }
        //TODO:PHI语句漏了！
//        var uses_ = B.get_phi_use();
//        if (uses_ != null)
//            for (var x : uses_.keySet()) {
//                if (!B.plo_after_sp.contains(x) && !spill.contains(x)) {
//                    var tmp=tempMap.get(x);
//                    if (tmp==null) continue;
////                    assert tmp!=null;
//                    inUse.remove(tmp);
//                    stack.add(tmp);
//                }
//            }
        var y_ = B.get_phi_def();
        if (y_ != null)
            for (var y : y_) {
                if (!B.plo_after_sp.contains(y)) continue;
                assert !stack.isEmpty();
                int c = stack.getLast();
                assert c >= 0;
                tempMap.put(y, c);
                inUse.add(c);
                stack.removeLast();
            }
        
        
        for (var s : B.insts) {
            s.lo_after_sp.removeAll(spill);
            var uses = s.getUses();
            if (uses != null)
                for (var x : uses) {
                    if (!s.lo_after_sp.contains(x) && !spill.contains(x)) {
                        var tmp = tempMap.get(x);
                        assert tmp != null;
                        inUse.remove(tmp);
                        stack.add(tmp);
                    }
                }
            var y = s.getDef();
            if (y == null || !s.lo_after_sp.contains(y)) continue;
            assert !stack.isEmpty();
            int c = stack.getLast();
            tempMap.put(y, c);
            inUse.add(c);
            stack.removeLast();
        }
        if (dt.containsKey(b))
            for (var c : dt.get(b)) {
                pre_order(c);
            }
    }
}


//63,64,67,69,70,18,19,24,3,31,41,...