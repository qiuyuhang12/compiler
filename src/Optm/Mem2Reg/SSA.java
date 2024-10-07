package Optm.Mem2Reg;

import Frontend.IR.node.def.IRFunDef;
import Frontend.IR.node.stmt.IRBlockNode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Objects;

import Frontend.IR.*;
import Frontend.IR.node.inst.*;
import org.antlr.v4.runtime.misc.Pair;

public class SSA {
    public IRFunDef fun;
    public HashMap<String, IRBlockNode> bl = new HashMap<>();//label to block
    public HashMap<String, ArrayList<Pair<String, Integer>>> use = new HashMap<>();//var to inst
    public HashMap<String, ArrayList<String>> use_in_phi = new HashMap<>();//var to label
//    public HashMap<String, HashSet<String>> rely = new HashMap<>();//var to var
//    public HashMap<String, ArrayList<instNode>> def = new HashMap<>();//var to inst
    
    public SSA(IRFunDef fun, HashMap<String, IRBlockNode> bl) {
        this.fun = fun;
        this.bl = bl;
    }
    
    public void run() {
        topo();
        _clean();
//        clean();
        collect_use();
    }
    
    //    HashMap<Integer, ArrayList<String>> num2inst = new HashMap<>();
    HashSet<String> del = new HashSet<>();
//    HashSet<String> no_del = new HashSet<>();
    
    public void topo() {
        HashMap<String, HashSet<String>> rely = new HashMap<>();//var to var
        HashMap<String, HashSet<String>> be_relied = new HashMap<>();//var to var
        
        for (var bl : fun.blocks) {
            for (var inst : bl.insts) {
                var def = inst.getDef();
                if (def != null) {
                    if (!rely.containsKey(def)) {
                        rely.put(def, new HashSet<>());
                    }
                }
                var uses = inst.getUses();
                if (uses == null || uses.isEmpty()) continue;
                for (var var : uses) {
                    if (!rely.containsKey(var)) {
                        rely.put(var, new HashSet<>());
                    }
                    if (inst instanceof callInstNode) {
                        rely.get(var).add("不能删");
                    } else {
                        rely.get(var).add(inst.getDef());
                    }
                }
            }
            for (var phi : bl.phis) {
                var def = phi.getDef();
                if (def != null) {
                    if (!rely.containsKey(def)) {
                        rely.put(def, new HashSet<>());
                    }
                }
                var uses = phi.getUses();
                if (uses == null || uses.isEmpty()) continue;
                for (var var : uses) {
                    if (!rely.containsKey(var)) {
                        rely.put(var, new HashSet<>());
                    }
                    rely.get(var).add(phi.getDef());
                }
            }
        }
        for (var var : rely.entrySet()) {
            var key = var.getKey();
            var value = var.getValue();
            for (var var1 : value) {
                if (Objects.equals(var1, "不能删")) continue;
                if (!be_relied.containsKey(var1)) {
                    be_relied.put(var1, new HashSet<>());
                }
                be_relied.get(var1).add(key);
            }
        }
        while (true) {
            boolean flag = false;
            HashSet<String> tmp = new HashSet<>();
            for (var var : rely.entrySet()) {
                var key = var.getKey();
                var value = var.getValue();
                if (value.isEmpty()) {
                    flag = true;
                    del.add(key);
                    var be_relied_set = be_relied.get(key);
                    if (be_relied_set != null && !be_relied_set.isEmpty())
                        for (var var1 : be_relied_set) {
                            rely.get(var1).remove(key);
                        }
                    tmp.add(key);
                }
            }
            for (var var : tmp) {
                rely.remove(var);
                be_relied.remove(var);
            }
            if (!flag) break;
        }
    }
    
    public void _clean() {
        for (var bl : fun.blocks) {
            for (int i = bl.insts.size() - 1; i >= 0; i--) {
                var inst = bl.insts.get(i);
                boolean cannot_del = (inst instanceof brInstNode || inst instanceof callInstNode || inst instanceof retInstNode || inst instanceof storeInstNode);
                if (inst.getDef() != null && del.contains(inst.getDef())) {
                    if (inst instanceof callInstNode call) {
                        if (call.dest != null) {
                            call.dest = null;
                        }
                    } else {
                        bl.insts.remove(i);
                        assert !cannot_del;
                    }
                }
            }
            for (int i = bl.phis.size() - 1; i >= 0; i--) {
                var phi = bl.phis.get(i);
                if (del.contains(phi.getDef())) {
                    bl.phis.remove(i);
                }
            }
        }
    }
    
    public void clean() {
        boolean flag = true;
        while (flag) {
            HashSet<String> used = new HashSet<>();
            for (var bl : fun.blocks) {
                for (var inst : bl.insts) {
                    var uses = inst.getUses();
                    if (uses == null || uses.isEmpty()) continue;
                    used.addAll(uses);
                }
                for (var inst : bl.phis) {
                    var uses = inst.getUses();
                    if (uses == null || uses.isEmpty()) continue;
                    used.addAll(uses);
                }
            }
            flag = false;
            for (var bl : fun.blocks) {
                for (int i = bl.insts.size() - 1; i >= 0; i--) {
                    var inst = bl.insts.get(i);
                    boolean cannot_del = (inst instanceof brInstNode || inst instanceof callInstNode || inst instanceof retInstNode || inst instanceof storeInstNode);
                    if (inst.getDef() != null && !used.contains(inst.getDef())) {
                        flag = true;
                        bl.insts.remove(i);
                        assert !cannot_del;
                    }
                }
                for (int i = bl.phis.size() - 1; i >= 0; i--) {
                    var phi = bl.phis.get(i);
                    if (!used.contains(phi.getDef())) {
                        flag = true;
                        bl.phis.remove(i);
                    }
                }
            }
        }
    }
    
    public void collect_use() {
        for (var bl : fun.blocks) {
            int i = 0;
            for (var inst : bl.insts) {
                var uses = inst.getUses();
                if (uses == null || uses.isEmpty()) continue;
                for (var var : uses) {
                    if (!use.containsKey(var)) {
                        use.put(var, new ArrayList<>());
                        use_in_phi.put(var, new ArrayList<>());
                    }
                    use.get(var).add(new Pair<>(bl.label, i));
                }
                i++;
            }
            for (var phi : bl.phis) {
                var uses = phi.getUses();
                if (uses == null || uses.isEmpty()) continue;
                for (var var : uses) {
                    if (!use.containsKey(var)) {
                        use.put(var, new ArrayList<>());
                        use_in_phi.put(var, new ArrayList<>());
                    }
                    use_in_phi.get(var).add(bl.label);
                }
            }
        }
    }
}

//todo:attention:
//  全局变量load store保留了
//  全局变量不参与染色
//  有关getele的load store保留了， alloca应该没有，都是调用allco_help
//  这里的load,store参与染色


//todo:
//  无use变量删除
//  phi指令弄成一个!
//  新建contain接口
//  统计use
//  live_in live_out,存入指令
//  活跃变量分析

//todo:loop分析

//            var uses=bl.get_phi_use;
//            if (uses==null||uses.isEmpty()) continue;
//            for (var var : uses) {
//                if (!use.containsKey(var)) {
//                    use.put(var, new ArrayList<>());
//                    use_in_phi.put(var, new ArrayList<>());
//                }
//                use_in_phi.get(var).add(bl.label);
//            }