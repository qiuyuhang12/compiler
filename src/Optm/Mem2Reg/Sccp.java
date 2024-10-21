package Optm.Mem2Reg;

import Frontend.IR.entity.IRBoolLiteral;
import Frontend.IR.entity.IREntity;
import Frontend.IR.entity.IRVar;
import Frontend.IR.node.def.IRFunDef;
import Frontend.IR.node.stmt.IRBlockNode;
import Frontend.IR.node.inst.*;
import Util.Consts;
import org.antlr.v4.runtime.misc.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Objects;

public class Sccp {
    IRFunDef fun;
    HashMap<String, IRBlockNode> bl = new HashMap<>();//label to block
    HashMap<String, ArrayList<String>> in = new HashMap<>();//label to in block
    HashMap<String, ArrayList<String>> out = new HashMap<>();//label to out block
    //    HashSet<Pair<String, String>> visitedEdge = new HashSet<>();
    HashMap<String, Pair<Type, String>> latticeCells = new HashMap<>();
    HashMap<String, ArrayList<Pair<instNode, Pair<String, String>>>> uses = new HashMap<>();//var -> use{inst,edge}
    HashMap<String, ArrayList<Pair<instNode, Pair<String, String>>>> brBlock = new HashMap<>();
    public HashMap<String, String> constMap = new HashMap<>();
//    HashMap<String, ArrayList<ubp>> useByPhi = new HashMap<>();

//    class ubp {
//        public phiInstNode phi;
//        public Pair<String, String> edge;
//        public String phi_var;
//
//        public ubp(phiInstNode _phi, Pair<String, String> _edge, String _phi_var) {
//            phi = _phi;
//            edge = _edge;
//            phi_var = _phi_var;
//        }
//    }
    
    enum Type {top, const_, bottom}
    
    public Sccp(IRFunDef _fun, HashMap<String, IRBlockNode> _bl, HashMap<String, ArrayList<String>> _in, HashMap<String, ArrayList<String>> _out) {
        fun = _fun;
        bl = _bl;
        in = _in;
        out = _out;
    }
    
    public void run() {
        if (!Consts.sccp) return;
        buildDefUse();
        bfs(fun.blocks.getFirst().label);
        for (var var : latticeCells.entrySet()) {
            if (var.getValue().a == Type.const_) {
                constMap.put(var.getKey(), var.getValue().b);
            }
        }
        if (Consts.showsccp) {
            System.out.println(constMap);
            System.out.println(latticeCells);
        }
        del_block();
        replace();
    }
    
    void buildDefUse() {
        for (var para : fun.parameters) {
            latticeCells.put(para.name, new Pair<>(Type.bottom, null));
        }
        for (var block : fun.blocks) {
            for (var it : block.phis) {
                String def = it.getDef();
                assert def != null;
                latticeCells.put(def, new Pair<>(Type.top, null));
                uses.put(def, new ArrayList<>());
//                worklist.add(new Pair<>(it, new Pair<>(block.label, null)));
//                _buildDefUse(inst, block.label);
            }
        }
        for (var block : fun.blocks) {
            for (var inst : block.insts) {
                _buildDefUse(inst, block.label);
            }
        }
        for (var block : fun.blocks) {
            for (var it : block.phis) {
                String def = it.getDef();
                assert def != null;
                for (int i = 0; i < it.labels.size(); i++) {
                    var val = it.values.get(i).toString();
                    if (val.charAt(0) == '%') {
                        if (uses.containsKey(val))
                            uses.get(val).add(new Pair<>(it, new Pair<>(it.labels.get(i), block.label)));
                    }
                }
            }
        }
    }

//    enum varType {
//        glob, num, loc
//    }

//    class varInfo {
//        public varType t;
//        public String val;
//
//        public varInfo(String _val) {
//            if (_val.charAt(0) == '@') t = varType.glob;
//            else if (_val.charAt(0) == '%') t = varType.loc;
//            else {
//                t = varType.num;
//                val = _val;
//                assert _val.equals(Integer.toString(Integer.parseInt(_val)))||_val.equals("null");
//            }
//        }
//    }
    
    Pair<Type, String> varInfo(String _val) {
        Type t;
        String val;
        if (_val.charAt(0) == '@') {
            t = Type.bottom;
            val = null;
            return new Pair<>(t, val);
        } else if (_val.charAt(0) == '%') {
            var tmp = latticeCells.get(_val);
            assert tmp != null;
            return new Pair<>(tmp.a, tmp.b);
//            if (latticeCells.containsKey(_val)) {
//                return latticeCells.get(_val);
//            }
//            assert false;
        } else {
            t = Type.const_;
            val = _val;
            if (_val.equals("true")) val = "1";
            if (_val.equals("false")) val = "0";
            assert _val.equals("null") || _val.equals("true") || _val.equals("false") || _val.equals(Integer.toString(Integer.parseInt(_val)));
            return new Pair<>(t, val);
        }
    }
    
    Pair<Type, String> add(Pair<Type, String> a, Pair<Type, String> b, String op) {
        if (op.equals("union")) {
            if (a.a == Type.top) return new Pair<>(b.a, b.b);
            if (b.a == Type.top) return new Pair<>(a.a, a.b);
        } else {
            if (a.a == Type.top || b.a == Type.top) return new Pair<>(Type.top, null);
        }
        if (a.a == Type.bottom || b.a == Type.bottom) return new Pair<>(Type.bottom, null);
        if (a.a == Type.const_ && b.a == Type.const_) {
            if (op.equals("union")) if (a.b.equals(b.b)) return new Pair<>(a.a, a.b);
            else return new Pair<>(Type.bottom, null);
            if (a.b.equals("null") || b.b.equals("null")) {
//                assert a.b.equals("null") && b.b.equals("null");
                if (a.b.equals("null") && b.b.equals("null")) switch (op) {
                    case "eq" -> {
                        return new Pair<>(Type.const_, "1");
                    }
                    case "ne" -> {
                        return new Pair<>(Type.const_, "0");
                    }
                    default -> {
                        System.err.println("Sccp:null err0!");
                        return new Pair<>(Type.const_, "0");
                    }
                }
                else {
                    System.err.println("Sccp:null err1!");
                    return new Pair<>(Type.const_, "0");
                }
            }
            int x = Integer.parseInt(a.b);
            int y = Integer.parseInt(b.b);
            switch (op) {
                case "add" -> {
                    return new Pair<>(Type.const_, Integer.toString(x + y));
                }
                case "sub" -> {
                    return new Pair<>(Type.const_, Integer.toString(x - y));
                }
                case "mul" -> {
                    return new Pair<>(Type.const_, Integer.toString(x * y));
                }
                case "sdiv" -> {
                    if (y == 0) {
                        System.err.println("Sccp:div 0 err!");
                        return new Pair<>(Type.const_, "0");
                    }
                    return new Pair<>(Type.const_, Integer.toString(x / y));
                }
                case "srem" -> {
                    return new Pair<>(Type.const_, Integer.toString(x % y));
                }
                case "shl" -> {
                    return new Pair<>(Type.const_, Integer.toString(x << y));
                }
                case "ashr" -> {
                    return new Pair<>(Type.const_, Integer.toString(x >> y));
                }
                case "and" -> {
                    return new Pair<>(Type.const_, Integer.toString(x & y));
                }
                case "or" -> {
                    return new Pair<>(Type.const_, Integer.toString(x | y));
                }
                case "xor" -> {
                    return new Pair<>(Type.const_, Integer.toString(x ^ y));
                }
                case "eq" -> {
                    return new Pair<>(Type.const_, x == y ? "1" : "0");
                }
                case "ne" -> {
                    return new Pair<>(Type.const_, x != y ? "1" : "0");
                }
                case "sgt" -> {
                    return new Pair<>(Type.const_, x > y ? "1" : "0");
                }
                case "sge" -> {
                    return new Pair<>(Type.const_, x >= y ? "1" : "0");
                }
                case "slt" -> {
                    return new Pair<>(Type.const_, x < y ? "1" : "0");
                }
                case "sle" -> {
                    return new Pair<>(Type.const_, x <= y ? "1" : "0");
                }
                default -> {
                    assert false;
                    return null;
                }
            }
        }
        assert false;
        return null;
    }
    
    
    void _buildDefUse(instNode it, String _bl) {
        String def = it.getDef();
        if (def == null && !(it instanceof brInstNode)) return;
        assert def == null || !def.isEmpty();
        switch (it) {
            case binaryInstNode is -> {
                //-self,mul 0,shl...没考虑
                _bin(def, is.lhs, is.rhs, is.op.toString(), it);
            }
            case brInstNode is -> {
                if (is.cond == null) return;
                var cond = varInfo(is.cond.toString());
                if (cond.a != Type.bottom) {
                    if (!brBlock.containsKey(is.cond.toString())) brBlock.put(is.cond.toString(), new ArrayList<>());
                    brBlock.get(is.cond.toString()).add(new Pair<>(is, new Pair<>(null, _bl)));
                }
            }
            case callInstNode is -> {
                //string_compare不考虑
                latticeCells.put(def, new Pair<>(Type.bottom, null));
            }
            case getElementPtrInstNode is -> {
                latticeCells.put(def, new Pair<>(Type.bottom, null));
            }
            case icmpInstNode is -> {
                _bin(def, is.lhs, is.rhs, is.op.toString(), it);
            }
            case loadInstNode is -> {
                latticeCells.put(def, new Pair<>(Type.bottom, null));
            }
            case phiInstNode is -> {
                assert false;
//                latticeCells.put(def, new Pair<>(Type.top, null));
//                uses.put(def, new ArrayList<>());
//                worklist.add(is);
//                for (int i = 0; i < is.labels.size(); i++) {
//                    var val = is.values.get(i).toString();
//                    if (val.charAt(0) == '%') {
//                        //todo:如果phi的入还没有扫到，无法真正加入use链，解决：分开吧，一个建链，一个入殓
//                        if (uses.containsKey(val)) uses.get(val).add(it);
//                    }
////                    handlePhi(is, _bl);
//                }
            }
            default -> {
                assert false;
            }
        }
    }

//    void handlePhi(phiInstNode it, String _bl) {
//        String def = it.getDef();
//        for (int i = 0; i < it.labels.size(); i++) {
//            var val = it.values.get(i).toString();
//            var label = it.labels.get(i);
//            if (val.charAt(0) == '%') {
//                if (!useByPhi.containsKey(val)) useByPhi.put(val, new ArrayList<>());
//                useByPhi.get(val).add(new ubp(it, new Pair<>(_bl, label), def));
//            }
//        }
//    }
    
    //in buildDefUse
    private void _bin(String def, IREntity lhs2, IREntity rhs2, String op, instNode it) {
        String lhs = lhs2.toString();
        String rhs = rhs2.toString();
        var lhs_ = varInfo(lhs);
        var rhs_ = varInfo(rhs);
        var res = add(lhs_, rhs_, op);
        latticeCells.put(def, res);
        if (res.a != Type.bottom) {
            uses.put(def, new ArrayList<>());
            if (lhs.charAt(0) == '%' && uses.containsKey(lhs)) {
                uses.get(lhs).add(new Pair<>(it, null));
            }
            if (rhs.charAt(0) == '%' && uses.containsKey(rhs)) {
                uses.get(rhs).add(new Pair<>(it, null));
            }
        }
    }
    
    //in bfs
    private Pair<Type, String> __bin(String def, IREntity lhs2, IREntity rhs2, String op, instNode it) {
        String lhs = lhs2.toString();
        String rhs = rhs2.toString();
        if (lhs.equals(rhs)) {
            if (op.equals("eq") || op.equals("sge") || op.equals("sle"))
                return new Pair<>(Type.const_, "1");
            if (op.equals("ne") || op.equals("sgt") || op.equals("slt"))
                return new Pair<>(Type.const_, "0");
        }
        var lhs_ = varInfo(lhs);
        var rhs_ = varInfo(rhs);
        return add(lhs_, rhs_, op);
    }
    
    ArrayList<Pair<String, String>> edges = new ArrayList<>();
    HashSet<String> visitedBlock = new HashSet<>();
    ArrayList<Pair<instNode, Pair<String, String>>> worklist = new ArrayList<>(); //inst edge(上一个->此)
    
    void bfs_inst(binaryInstNode is, Pair<String, String> edge) {
        var rsl = __bin(is.getDef(), is.lhs, is.rhs, is.op.toString(), is);
        update(is, rsl, edge);
    }
    
    void bfs_inst(icmpInstNode is, Pair<String, String> edge) {
        var rsl = __bin(is.getDef(), is.lhs, is.rhs, is.op.toString(), is);
        update(is, rsl, edge);
    }
    
    void bfs_inst(brInstNode is, Pair<String, String> edge) {
        if (is.cond == null) {
            edges.add(new Pair<>(edge.b, is.dest));
            return;
        }
        var cond = is.cond.toString();
        var cond_ = varInfo(cond);
        switch (cond_.a) {
            case const_ -> {
                if (cond_.b.equals("1")) {
                    edges.add(new Pair<>(edge.b, is.ifTrue));
                } else {
                    assert cond_.b.equals("0");
                    edges.add(new Pair<>(edge.b, is.ifFalse));
                }
            }
            case bottom -> {
                edges.add(new Pair<>(edge.b, is.ifTrue));
                edges.add(new Pair<>(edge.b, is.ifFalse));
            }
            case top -> {
            }
        }
    }
    
    void bfs_inst(phiInstNode inst, Pair<String, String> edge) {
        int i = inst.labels.indexOf(edge.a);
        String var = inst.values.get(i).toString();
        var tp = varInfo(var);
        update(inst, tp, edge);
    }
    
    void bfs_phis(Pair<String, String> edge, IRBlockNode block) {
        for (var inst : block.phis) {
            bfs_inst(inst, edge);
        }
    }
    
    void bfs(String entry) {
        edges.add(new Pair<>(null, entry));
        while (!edges.isEmpty() || !worklist.isEmpty()) {
            if (!edges.isEmpty()) {
                var edge = edges.getFirst();
                edges.removeFirst();
                var block = bl.get(edge.b);
                boolean isVisited = visitedBlock.contains(edge.b);
                visitedBlock.add(edge.b);
                bfs_phis(edge, block);
                if (!isVisited) {
                    for (var inst : block.insts) {
                        switch (inst) {
                            case binaryInstNode is -> {
                                bfs_inst(is, edge);
                            }
                            case icmpInstNode is -> {
                                bfs_inst(is, edge);
                            }
                            case brInstNode is -> {
                                bfs_inst(is, edge);
                            }
                            default -> {
                            }
                        }
                    }
                }
            } else {
                var inst_label = worklist.getFirst();
                worklist.removeFirst();
                switch (inst_label.a) {
                    case binaryInstNode is -> {
                        bfs_inst(is, inst_label.b);
                    }
                    case icmpInstNode is -> {
                        bfs_inst(is, inst_label.b);
                    }
                    case phiInstNode is -> {
                        bfs_inst(is, inst_label.b);
                    }
                    case brInstNode is -> {
                        bfs_inst(is, inst_label.b);
                    }
                    default -> {
                        assert false;
                    }
                }
            }
        }
    }
    
    void update(instNode inst, Pair<Type, String> tp, Pair<String, String> edge) {
        var old = latticeCells.get(inst.getDef());
        var rsl = add(old, tp, "union");
        if (!old.equals(rsl)) {
            assert old.a != Type.bottom;
            worklist.addAll(uses.get(inst.getDef()));
            var brs = brBlock.get(inst.getDef());
            if (brs != null) {
                worklist.addAll(brs);
            }
            latticeCells.put(inst.getDef(), rsl);
        }
    }
    
    HashMap<String, String> single_phi_renamer = new HashMap<>();
    
    void del_block() {
        for (int i = 0; i < fun.blocks.size(); i++) {
            var block = fun.blocks.get(i);
            if (!visitedBlock.contains(block.label)) {
                fun.blocks.remove(i);
                i--;
                continue;
            }
            for (int i1 = 0; i1 < block.phis.size(); i1++) {
                var inst = block.phis.get(i1);
                for (int j = 0; j < inst.values.size(); j++) {
                    String val = inst.values.get(j).toString();
                    String label = inst.labels.get(j);
                    if (!visitedBlock.contains(label)) {
                        inst.values.remove(j);
                        inst.labels.remove(j);
                        j--;
                    }
                }
                if (inst.values.size() == 1) {
                    String val = inst.values.getFirst().toString();
                    single_phi_renamer.put(inst.getDef(), constMap.getOrDefault(val, val));
                    block.phis.remove(i1);
                    i1--;
                }
                
            }
            int j = -1;
            for (var inst : block.insts) {
                j++;
                if (Objects.requireNonNull(inst) instanceof brInstNode is) {
                    assert j == block.insts.size() - 1;
                    if (is.cond == null) continue;
                    String iftrue = is.ifTrue;
                    String iffalse = is.ifFalse;
                    if (!visitedBlock.contains(iftrue) && !visitedBlock.contains(iffalse)) {
                        assert false;
                    } else if (!visitedBlock.contains(iftrue)) {
                        if (is.cond.toString().charAt(0) == '%') {
                            assert latticeCells.get(is.cond.toString()).a == Type.const_;
                            assert latticeCells.get(is.cond.toString()).b.equals("0");
                        }
                        block.insts.removeLast();
                        block.insts.add(new brInstNode(null, block, iffalse));
                    } else if (!visitedBlock.contains(iffalse)) {
                        if (is.cond.toString().charAt(0) == '%') {
                            assert latticeCells.get(is.cond.toString()).a == Type.const_;
                            assert latticeCells.get(is.cond.toString()).b.equals("1");
                        }
                        block.insts.removeLast();
                        block.insts.add(new brInstNode(null, block, iftrue));
                    }
                }
            }
        }
    }
    
    void handle_renamer() {
        HashMap<String, String> new_single_phi_renamer = new HashMap<>();
        for (var entry : single_phi_renamer.entrySet()) {
            String key = entry.getKey();
            String val = entry.getValue();
            while (single_phi_renamer.containsKey(val)) {
                val = single_phi_renamer.get(val);
            }
            new_single_phi_renamer.put(key, val);
        }
        single_phi_renamer = new_single_phi_renamer;
    }
    
    void replace() {
        handle_renamer();
        for (var block : fun.blocks) {
            for (var inst : block.phis) {
                for (int i = 0; i < inst.values.size(); i++) {
                    String val = inst.values.get(i).toString();
                    if (val.charAt(0) == '%' && constMap.containsKey(val)) {
                        ((IRVar) inst.values.get(i)).name = constMap.get(val);
                    } else if (val.charAt(0) == '%' && single_phi_renamer.containsKey(val)) {
                        ((IRVar) inst.values.get(i)).name = single_phi_renamer.get(val);
                    }
                }
            }
            for (var it : block.insts) {
                switch (it) {
                    case binaryInstNode is -> {
                        //-self,mul 0,shl...没考虑
//                        _bin_replace(is.lhs, is.rhs);
                        entity_replace(is.lhs);
                        entity_replace(is.rhs);
                    }
                    case brInstNode is -> {
                        if (is.cond == null) continue;
                        entity_replace(is.cond);
//                        String cond = is.cond.toString();
//                        if (cond.charAt(0) == '%' && constMap.containsKey(cond)) {
//                            is.cond = new IRBoolLiteral(constMap.get(cond).equals("1"));
//                        }
                    }
                    case callInstNode is -> {
                        //string_compare不考虑
                        for (int i = 0; i < is.args.size(); i++) {
                            entity_replace(is.args.get(i));
//                            String arg = is.args.get(i).toString();
//                            if (arg.charAt(0) == '%' && constMap.containsKey(arg)) {
//                                ((IRVar) is.args.get(i)).name = constMap.get(arg);
//                            }
                        }
                    }
                    case getElementPtrInstNode is -> {
                        for (int i = 0; i < is.idxs.size(); i++) {
                            String _idx = is.idxs.get(i);
                            if (_idx.charAt(0) == '%' && constMap.containsKey(_idx)) {
                                is.idxs.set(i, constMap.get(_idx));
                            } else if (_idx.charAt(0) == '%' && single_phi_renamer.containsKey(_idx)) {
                                is.idxs.set(i, single_phi_renamer.get(_idx));
                            }
                        }
                    }
                    case icmpInstNode is -> {
                        entity_replace(is.lhs);
                        entity_replace(is.rhs);
//                        _bin_replace(is.lhs, is.rhs);
                    }
                    case loadInstNode is -> {
                        if (is.ptr.charAt(0) == '%' && constMap.containsKey(is.ptr)) {
                            is.ptr = constMap.get(is.ptr);
                        } else if (is.ptr.charAt(0) == '%' && single_phi_renamer.containsKey(is.ptr)) {
                            is.ptr = single_phi_renamer.get(is.ptr);
                        }
                    }
//                    case phiInstNode is -> {
//                        for (int i = 0; i < is.values.size(); i++) {
//                            String val = is.values.get(i).toString();
//                            if (val.charAt(0) == '%' && constMap.containsKey(val)) {
//                                ((IRVar) is.values.get(i)).name = constMap.get(val);
//                            }
//                        }
//                    }
                    case retInstNode is -> {
                        if (is.value.typeInfo.toString().equals("void")) continue;
                        entity_replace(is.value);
//                        String val = is.value.toString();
//                        if (val.charAt(0) == '%' && constMap.containsKey(val)) {
//                            ((IRVar) is.value).name = constMap.get(val);
//                        }
                    }
                    case storeInstNode is -> {
                        entity_replace(is.value);
                        entity_replace(is.ptr);
//                        String val = is.value.toString();
//                        if (val.charAt(0) == '%' && constMap.containsKey(val)) {
//                            ((IRVar) is.value).name = constMap.get(val);
//                        }
//                        String ptr = is.ptr.toString();
//                        if (ptr.charAt(0) == '%' && constMap.containsKey(ptr)) {
//                            ((IRVar) is.ptr).name = constMap.get(ptr);
//                        }
                    }
                    default -> {
                        assert false;
                    }
                }
            }
        }
    }
    
    void entity_replace(IREntity entity) {
        if (entity.toString().charAt(0) == '%' && constMap.containsKey(entity.toString())) {
            ((IRVar) entity).name = constMap.get(entity.toString());
        } else if (entity.toString().charAt(0) == '%' && single_phi_renamer.containsKey(entity.toString())) {
            ((IRVar) entity).name = single_phi_renamer.get(entity.toString());
        }
    }

//    private void _bin_replace(IREntity lhs2, IREntity rhs2) {
////        String lhs = lhs2.toString();
////        String rhs = rhs2.toString();
////        if (lhs.charAt(0) == '%' && constMap.containsKey(lhs)) {
////            ((IRVar) lhs2).name = constMap.get(lhs);
////        }
////        if (rhs.charAt(0) == '%' && constMap.containsKey(rhs)) {
////            ((IRVar) rhs2).name = constMap.get(rhs);
////        }
//        entity_replace(lhs2);
//        entity_replace(rhs2);
//    }
}
