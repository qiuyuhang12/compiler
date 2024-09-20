package Optm.Mem2Reg;

import Frontend.IR.node.def.IRFunDef;
import Frontend.IR.node.inst.instNode;
import Frontend.IR.node.inst.loadInstNode;
import Frontend.IR.node.inst.storeInstNode;
import Frontend.IR.node.stmt.IRBlockNode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class M2r_Fun {
    public IRFunDef fun;
    public HashMap<String, IRBlockNode> bl = new HashMap<>();//label to block
    public HashMap<String, ArrayList<String>> in = new HashMap<>();//label to in block
    public HashMap<String, ArrayList<String>> out = new HashMap<>();//label to out block
    public HashMap<String, ArrayList<String>> def = new HashMap<>();//var to def block
    public HashMap<String, ArrayList<String>> useBl = new HashMap<>();//var to use block
    public HashMap<String, ArrayList<String>> useVar = new HashMap<>();//block to used var
    public HashMap<String, HashSet<String>> dom = new HashMap<>();//label to dom block
    public HashMap<String, String> idom = new HashMap<>();//label to idom block label
    
    public M2r_Fun(IRFunDef fun) {
        this.fun = fun;
    }
    
    
    public void run() {
        cfg();
    }
    
    void cfg() {
        for (IRBlockNode block : fun.blocks) {
            bl.put(block.label, block);
            in.put(block.label, new ArrayList<>());
            dom.put(block.label, new HashSet<>());
        }
        for (IRBlockNode block : fun.blocks) {
            ArrayList<String> next = block.getNext();
            for (String s : next) {
                in.get(s).add(block.label);
            }
            out.put(block.label, next);
        }
    }
    
    void handleDefUse() {
        for (IRBlockNode block : fun.blocks) {
            for (instNode inst : block.insts) {
                if (inst instanceof loadInstNode ld) {
                    String var = ld.ptr;
                    if (!useBl.containsKey(var)) {
                        useBl.put(var, new ArrayList<>());
                    }
                    useBl.get(var).add(block.label);
                    if (!useVar.containsKey(block.label)) {
                        useVar.put(block.label, new ArrayList<>());
                    }
                    useVar.get(block.label).add(var);
                } else if (inst instanceof storeInstNode st) {
                    String var = st.ptr.toString();
                    if (!def.containsKey(var)) {
                        def.put(var, new ArrayList<>());
                    }
                    def.get(var).add(block.label);
                }
            }
        }
    }
    
    void dom() {
        boolean changed = true;
        while (changed) {
            changed = bfs(fun.getEntry());
        }
        idom();
    }
    
    boolean bfs(String label) {
        boolean changed;
        HashSet<String> tmp = dom.get(label);
        for (String s : out.get(label)) {
            tmp.retainAll(dom.get(s));
        }
        changed = tmp.add(label);
        for (String s : in.get(label)) {
            changed = changed || tmp.addAll(dom.get(s));
        }
        for (String s : out.get(label)) {
            if (bfs(s)) {
                changed = true;
            }
        }
        return changed;
    }
    
    void idom() {
        for (Map.Entry<String, HashSet<String>> entry : dom.entrySet()) {
            String label = entry.getKey();
            HashSet<String> set = entry.getValue();
            String[] arr = new String[set.size()];
            for (String s : set) {
                arr[dom.get(s).size()] = s;
            }
            for (int i = 0; i < arr.length; i++) {
                if (i == arr.length - 1) {
                    idom.put(arr[i + 1], arr[i]);
                } else {
                    idom.put(label, arr[i]);
                }
            }
        }
    }
}
