package Optm.Mem2Reg;

import Frontend.IR.entity.IRVar;
import Frontend.IR.node.def.IRFunDef;
import Frontend.IR.node.inst.*;
import Frontend.IR.node.stmt.IRBlockNode;
import Frontend.IR.type.IRType;
import Frontend.IR.util.Renamer;
import org.antlr.v4.runtime.misc.Pair;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class M2r_Fun {
    public IRFunDef fun;
    public Renamer renamer;
    public HashMap<String, IRBlockNode> bl = new HashMap<>();//label to block
    public HashMap<String, ArrayList<String>> in = new HashMap<>();//label to in block
    public HashMap<String, ArrayList<String>> out = new HashMap<>();//label to out block
    public HashMap<String, ArrayList<String>> def = new HashMap<>();//var to def block
    public HashMap<String, ArrayList<String>> useBl = new HashMap<>();//var to use block
    public HashMap<String, ArrayList<String>> useVar = new HashMap<>();//block to used var
    public HashMap<String, HashSet<String>> dom = new HashMap<>();//label to dom block
    public HashMap<String, String> idom = new HashMap<>();//label to idom block label
    public HashMap<String, HashSet<String>> domFr = new HashMap<>();//label to dom frontier block
    public HashMap<String, String> var2type = new HashMap<>();//var to type
    public HashMap<String, ArrayList<String>> phiStack = new HashMap<>();//var to phi stack
    public HashSet<String> usefulPtr = new HashSet<>();//label to ele ptr
    
    public M2r_Fun(IRFunDef fun, Renamer renamer) {
        this.fun = fun;
        this.renamer = renamer;
    }
    
    
    public void run() {
        cfg();
        clear();
        cfg();
        handleDefUse();
        dom();
        idom();
        domFr();
        setphi();
        for (Map.Entry<String, ArrayList<String>> entry : in.entrySet()) {
            if (entry.getValue().isEmpty()) {
                rename(entry.getKey());
            }
        }
        Analysis analysis = new Analysis(fun, bl, in);
        analysis.run();
        Spill spill = new Spill(20, fun, bl);
        spill.run();
        try (FileWriter writer = new FileWriter("rubish/hh")) {
            writer.write(spill.spill.size());
        } catch (IOException e) {
            assert false;
        }
        Color color = new Color(20, fun, bl, idom);
        color.run();
        critical_edge();
    }
    
    HashSet<String> visited = new HashSet<>();
    HashSet<String> can_arrive = new HashSet<>();
    
    void cfg() {
        in.clear();
        out.clear();
        dom.clear();
        bl.clear();
        for (IRBlockNode block : fun.blocks) {
            bl.put(block.label, block);
            in.put(block.label, new ArrayList<>());
            dom.put(block.label, new HashSet<>());
        }
        for (IRBlockNode block : fun.blocks) {
            ArrayList<String> next = block.getNext();
            out.put(block.label, next);
            if (next == null) {
                continue;
            }
            for (String s : next) {
                in.get(s).add(block.label);
            }
        }
    }
    
    void clear() {
        bfs_arrive(fun.blocks.getFirst().label);
        for (int i = fun.blocks.size() - 1; i >= 0; i--) {
            if (!can_arrive.contains(fun.blocks.get(i).label)) {
                fun.blocks.remove(i);
            }
        }
    }
    
    void bfs_arrive(String label) {
        if (visited.contains(label)) {
            return;
        }
        visited.add(label);
        can_arrive.add(label);
        if (out.get(label) != null)
            for (String s : out.get(label)) {
                bfs_arrive(s);
            }
    }
    
    void handleDefUse() {
        for (IRBlockNode block : fun.blocks) {//统计getelement的指针
            for (instNode inst : block.insts) {
                if (inst instanceof getElementPtrInstNode gep) {
                    usefulPtr.add(gep.ptr);
                } else if (inst instanceof callInstNode call) {
                    if (call.funName.equals("string.copy")) {
                        usefulPtr.add(call.args.getFirst().toString());
                    }
                }
            }
        }
        for (IRBlockNode block : fun.blocks) {//统计局部变量
            int i = -1;
            ArrayList<Integer> index = new ArrayList<>();
            for (instNode inst : block.insts) {
                i++;
                if (inst instanceof allocaInstNode alloc) {
//                    if (alloc.type.type == IRType.IRTypeEnum.ptr || alloc.type.type == IRType.IRTypeEnum.struct) {
//                        continue;
//                    }
                    if (usefulPtr.contains(alloc.dest.toString())) {
                        continue;
                    }
                    String var = alloc.dest.toString();
                    def.put(var, new ArrayList<>());
                    useBl.put(var, new ArrayList<>());
                    var2type.put(var, alloc.type.toString());
                    phiStack.put(var, new ArrayList<>());
                    index.add(i);
                }
            }
            for (int j = index.size() - 1; j >= 0; j--) {
                block.insts.remove((int) index.get(j));
            }
        }
        for (IRBlockNode block : fun.blocks) {//def use
            for (instNode inst : block.insts) {
                if (inst instanceof loadInstNode ld) {
                    String var = ld.ptr;
                    if (useBl.containsKey(var)) {//排除全局
                        useBl.get(var).add(block.label);
                        if (!useVar.containsKey(block.label)) {
                            useVar.put(block.label, new ArrayList<>());
                        }
                        useVar.get(block.label).add(var);
                    }
                } else if (inst instanceof storeInstNode st) {
                    String var = st.ptr.toString();
                    if (def.containsKey(var)) {
                        def.get(var).add(block.label);
                    }
                }
            }
        }
    }
    
    void dom() {
        boolean changed = true;
        while (changed) {
            changed = false;
            for (String label : bl.keySet()) {
                changed = bfs(label) || changed;
            }
        }
    }
    
    boolean bfs(String label) {
        boolean changed;
        HashSet<String> dom_n = dom.get(label);
//        for (String s : out.get(label)) {
//            tmp.retainAll(dom.get(s));
//        }
        changed = dom_n.add(label);
//        HashSet
        HashSet<String> tmp = null;
        for (String s : in.get(label)) {
            if (tmp == null) {
                tmp = new HashSet<>(dom.get(s));
            } else {
                tmp.retainAll(dom.get(s));
            }
//            changed = changed || dom_n.addAll(dom.get(s));
        }
        if (tmp != null) {
            changed = dom_n.addAll(tmp) || changed;
        }
//        for (String s : out.get(label)) {
//            if (bfs(s)) {
//                changed = true;
//            }
//        }
        return changed;
    }
    
    void idom() {
        for (Map.Entry<String, HashSet<String>> entry : dom.entrySet()) {
            String label = entry.getKey();
            HashSet<String> set = entry.getValue();
            String[] arr = new String[set.size() + 1];
            for (String s : set) {
                arr[dom.get(s).size()] = s;
            }
            arr[0] = null;
            for (int i = 0; i < arr.length; i++) {
                if (i != arr.length - 1) {
                    idom.put(arr[i + 1], arr[i]);
                }
//                else {
//                    idom.put(label, arr[i]);
//                }
            }
        }
    }
    
    void domFr() {
        for (String var : bl.keySet()) {
            domFr.put(var, new HashSet<>());
        }
        for (String n : bl.keySet()) {
            HashSet<String> tmp = new HashSet<>();
            for (String m : in.get(n)) {
                HashSet<String> dom_m = new HashSet<>(dom.get(m));
                HashSet<String> dom_n = new HashSet<>(dom.get(n));
                dom_m.removeAll(dom_n);
                tmp.addAll(dom_m);
            }
            for (String label : tmp) {
                domFr.get(label).add(n);
            }
        }
    }
    
    void setphi() {
        for (var entry : def.entrySet()) {
            String var = entry.getKey();
            if (useBl.containsKey(var))
                for (String label : entry.getValue()) {
                    _setphi(label, var);
//                for (String fro : domFr.get(label)) {
//                    IRBlockNode block = bl.get(fro);
//                    block.setPhi(var, var2type.get(var));
//                }
                }
        }
    }
    
    void _setphi(String label, String var) {
        IRBlockNode blo = bl.get(label);
//        if (!blo.setPhi(var, var2type.get(var))) {
//            return;
//        }
        for (String fro : domFr.get(label)) {
            IRBlockNode block = bl.get(fro);
            if (block.setPhi(var, var2type.get(var))) _setphi(fro, var);
        }
    }
    
    HashMap<String, String> renameMap = new HashMap<>();//load 带来的 rename
    
    void rename(String label) {
        IRBlockNode block = bl.get(label);
        if (block.renamed) {
            return;
        }
        block.renamed = true;
        for (ArrayList<String> stack : phiStack.values()) {//入栈一次
            if (!stack.isEmpty()) stack.add(stack.getLast());
            else stack.add("这不合理");
        }
        for (phiInstNode phi : block.phis) {//rename phi
//            String var = phi.oriVar + "." + label;
            String var = renamer.getAnonymousName_m2r();
            phiStack.get(phi.oriVar).set(phiStack.get(phi.oriVar).size() - 1, var);
            phi.rename(var);
        }
//        HashMap<String, String> renameMap = new HashMap<>();//load 带来的 rename
        int i = -1;
        ArrayList<Integer> index = new ArrayList<>();
        for (instNode inst : block.insts) {//rename inst
            i++;
            if (inst instanceof loadInstNode ld) {
                if (phiStack.containsKey(ld.ptr)) {
                    renameMap.put(ld.dest, phiStack.get(ld.ptr).getLast());
                    index.add(i);
                }
            } else if (inst instanceof storeInstNode st) {
                st.rename(renameMap);
                if (!phiStack.containsKey(st.ptr.toString())) {
                    continue;
                }
                if (st.value instanceof IRVar var && renameMap.containsKey(st.value.toString())) {
                    var.name = renameMap.get(st.value.toString());
                }
                phiStack.get(st.ptr.toString()).set(phiStack.get(st.ptr.toString()).size() - 1, st.value.toString());
                index.add(i);
            } else {
                inst.rename(renameMap);
            }
        }
        for (int j = index.size() - 1; j >= 0; j--) {//remove load store
            block.insts.remove((int) index.get(j));
        }
        if (out.get(label) != null) for (String s : out.get(label)) {//fill next phi
            IRBlockNode next = bl.get(s);
            for (phiInstNode nextPhi : next.phis) {
                var stack = phiStack.get(nextPhi.oriVar);
                if (!stack.isEmpty() && !stack.getLast().equals("这不合理")) {
                    boolean tmp = nextPhi.add_source_m2r(stack.getLast(), label);
                    assert tmp;
                } else {
                    if (nextPhi.type.type == IRType.IRTypeEnum.i1 || nextPhi.type.type == IRType.IRTypeEnum.i32) {
                        nextPhi.add_source_m2r("0", label);
                    } else if (nextPhi.type.type == IRType.IRTypeEnum.ptr) {
                        nextPhi.add_source_m2r("null", label);
                    } else {
                        assert false;
                    }
                }
            }
            rename(s);
        }
        for (var stack : phiStack.values()) {
            stack.removeLast();
        }
    }
    
    HashMap<String, String> renameBl = new HashMap<>();//critical edge 带来的 rename
    ArrayList<Pair<Integer, IRBlockNode>> toInsert = new ArrayList<>();
    
    void critical_edge() {
        for (var entry : bl.entrySet()) {
            String label = entry.getKey();
            IRBlockNode block = entry.getValue();
            if (block.phis.isEmpty()) continue;
            int i = 0;
            for (var in_bl : in.get(label)) {
                if (out.get(in_bl).size() > 1) {
                    insert_block(in_bl, label, i);
                }
                i++;
            }
            for (var phi : block.phis) {
                phi.rename_phi_bl(renameBl);
            }
        }
        for (var pair : toInsert) {
            fun.blocks.add(pair.a + 1, pair.b);
        }
    }
    
    void insert_block(String parent, String son, int i) {
        IRBlockNode p = bl.get(parent);
        IRBlockNode s = bl.get(son);
        IRBlockNode new_block = new IRBlockNode(renamer.getAnonymousBlName_m2r());
        new_block.push(new brInstNode(null, new_block, s.label));
        brInstNode br = (brInstNode) p.insts.getLast();
        if (br.isCondBr) {
            if (br.ifTrue.equals(son)) {
                br.ifTrue = new_block.label;
            } else {
                br.ifFalse = new_block.label;
            }
        } else {
            br.dest = new_block.label;
        }
//        out.get(parent).remove(son);
//        out.get(parent).add(new_block.label);
//        in.get(son).remove(parent);
//        in.get(son).add(new_block.label);
//        out.put(new_block.label, new ArrayList<>(Collections.singletonList(son)));
//        in.put(new_block.label, new ArrayList<>(Collections.singletonList(parent)));
//        bl.put(new_block.label, new_block);

//        fun.blocks.add(new_block);//todo:warning:会不会太远？
        toInsert.add(new Pair<>(i, new_block));
        renameBl.put(parent, new_block.label);
//        for (phiInstNode phi : s.phis) {
//            phi.add_source_m2r("这不合理", new_block.label);
//        }
    }
    
    
}
