package Optm.Mem2Reg;

import Frontend.IR.node.def.IRFunDef;
import Frontend.IR.node.stmt.IRBlockNode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Objects;

import Frontend.IR.node.inst.*;
import org.antlr.v4.runtime.misc.Pair;

public class Analysis {
    public IRFunDef fun;
    public HashMap<String, IRBlockNode> bl = new HashMap<>();//label to block
    public HashMap<String, ArrayList<Pair<String, Integer>>> use = new HashMap<>();//var to inst
    public HashMap<String, ArrayList<Pair<String, String>>> use_in_phi = new HashMap<>();//var to label,jumpSrc
    //    public HashMap<String, ArrayList<instNode>> def = new HashMap<>();//var to inst
    public HashMap<String, ArrayList<String>> in = new HashMap<>();//label to in block
//    public HashMap<String, String> idom = new HashMap<>();//label to idom block label
    
    public Analysis(IRFunDef fun, HashMap<String, IRBlockNode> bl, HashMap<String, ArrayList<String>> in) {
        this.fun = fun;
        this.bl = bl;
        this.in = in;
//        this.idom = idom;
    }
    
    public void run() {
        topo();
        clean();
        collect_use();
        ssa_liveness();
        for (var entry : in.entrySet()) {
            var label = entry.getKey();
            var block = this.bl.get(label);
            var ins = entry.getValue();
            if (ins == null || ins.isEmpty()) continue;
            for (var in : ins) {
                var in_block = this.bl.get(in);
                block.live_in.addAll(in_block.insts.getLast().live_out);
            }
        }
    }
    
    HashSet<String> del = new HashSet<>();
    
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
                    if (be_relied_set != null && !be_relied_set.isEmpty()) for (var var1 : be_relied_set) {
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
    
    public void clean() {
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
    
    public void collect_use() {
        for (var bl : fun.blocks) {
            int i = 0;
            for (var inst : bl.insts) {
                var uses = inst.getUses();
                if (uses != null && !uses.isEmpty())
                    for (var var : uses) {
                        if (!use.containsKey(var)) {
                            use.put(var, new ArrayList<>());
                            use_in_phi.put(var, new ArrayList<>());
                        }
                        use.get(var).add(new Pair<>(bl.label, i));
                    }
                i++;
            }
            var uses = bl.get_phi_use();
            if (uses == null || uses.isEmpty()) continue;
            for (var var : uses.entrySet()) {
                var key = var.getKey();
                var value = var.getValue();
                if (!use_in_phi.containsKey(key)) {
                    use.put(key, new ArrayList<>());
                    use_in_phi.put(key, new ArrayList<>());
                }
                use_in_phi.get(key).addAll(value);
            }
        }
    }
    
    HashSet<String> scanned_block = new HashSet<>();
    
    void ssa_liveness() {
        for (var entry : use.entrySet()) {
            scanned_block.clear();
            var x = entry.getKey();
            var uses = entry.getValue();
            for (var pair : uses) {
                var B = pair.a;
                var id = pair.b;
                scan_live_in(B, id, x);
            }
            var phi_use = use_in_phi.get(x);
            for (var pair : phi_use) {
//                var B = pair.a;
                var Pi = pair.b;
                scan_block(Pi, x);
            }
        }
    }
    
    void scan_block(String B, String x) {//label,var
        if (scanned_block.contains(B)) return;
        scanned_block.add(B);
        var insts = bl.get(B).insts;
        if (insts == null || insts.isEmpty()) {
            scan_live_out_phi(B, x);
        } else {
            scan_live_out(B, insts.size() - 1, x);
        }
    }
    
    void scan_live_in(String B, Integer id, String x) {//inst,var
        if (id == 0) {
            var bl = this.bl.get(B);
            if (bl.phis == null || bl.phis.isEmpty()) {
                for (var P : in.get(B)) {
                    scan_block(P, x);
                }
            } else {
                scan_live_out_phi(B, x);
            }
            
        } else {
            scan_live_out(B, id - 1, x);
        }
    }
    
    void scan_live_out(String B, Integer id, String x) {//inst,var
        var bl = this.bl.get(B);
        var inst = bl.insts.get(id);
        var D = inst.getDef();
        inst.live_out.add(x);
        if (D != null) {
            inst.live_out.add(D);
        }
        if (!x.equals(D)) {
            scan_live_in(B, id, x);
        }
    }
    
    void scan_live_in_phi(String B, String x) {//inst,var
        for (var P : in.get(B)) {
            scan_block(P, x);
        }
    }
    
    void scan_live_out_phi(String B, String x) {//inst,var
        var bl = this.bl.get(B);
        var D = bl.get_phi_def();
        if (B.equals(fun.blocks.getFirst().label)) {
            D.addAll(fun.get_para_def());
        }
        bl.phi_live_out.addAll(D);
        bl.phi_live_out.add(x);
        if (!D.contains(x)) {
            scan_live_in_phi(B, x);
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

//todo:idom是子到父

//todo:loop分析
