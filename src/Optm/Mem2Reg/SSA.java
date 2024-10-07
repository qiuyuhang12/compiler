package Optm.Mem2Reg;

import Frontend.IR.node.def.IRFunDef;
import Frontend.IR.node.stmt.IRBlockNode;

import java.util.ArrayList;
import java.util.HashMap;

import Frontend.IR.*;
import Frontend.IR.node.inst.*;

public class SSA {
    public IRFunDef fun;
    public HashMap<String, IRBlockNode> bl = new HashMap<>();//label to block
    public HashMap<String, ArrayList<instNode>> use = new HashMap<>();//var to inst
    public HashMap<String, ArrayList<String>> use_in_phi = new HashMap<>();//var to label
//    public HashMap<String, ArrayList<instNode>> def = new HashMap<>();//var to inst
    
    public SSA(IRFunDef fun, HashMap<String, IRBlockNode> bl) {
        this.fun = fun;
        this.bl = bl;
    }
    
    public void collect_use() {
        for (var bl : fun.blocks) {
            for (var inst : bl.insts) {
                for (var var : inst.getUses()) {
                    if (!use.containsKey(var)) {
                        use.put(var, new ArrayList<>());
                        use_in_phi.put(var, new ArrayList<>());
                    }
                    use.get(var).add(inst);
                }
            }
            for (var var : bl.get_phi_use) {
                if (!use.containsKey(var)) {
                    use.put(var, new ArrayList<>());
                    use_in_phi.put(var, new ArrayList<>());
                }
                use_in_phi.get(var).add(bl.label);
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