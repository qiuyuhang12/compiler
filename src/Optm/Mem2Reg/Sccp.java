package Optm.Mem2Reg;

import Frontend.IR.entity.IREntity;
import Frontend.IR.node.def.IRFunDef;
import Frontend.IR.node.stmt.IRBlockNode;
import Frontend.IR.node.inst.*;
import org.antlr.v4.runtime.misc.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class Sccp {
    IRFunDef fun;
    HashMap<String, IRBlockNode> bl = new HashMap<>();//label to block
    HashMap<String, ArrayList<String>> in = new HashMap<>();//label to in block
    HashMap<String, ArrayList<String>> out = new HashMap<>();//label to out block
    ArrayList<Pair<String, String>> edges = new ArrayList<>();
    ArrayList<instNode> worklist = new ArrayList<>();
    HashSet<Pair<String, String>> visitedEdge = new HashSet<>();
    HashMap<String, Pair<Type, String>> latticeCells = new HashMap<>();
    HashMap<String, ArrayList<instNode>> uses = new HashMap<>();
    HashMap<String, ArrayList<String>> brBlock = new HashMap<>();
    HashSet<String> visitedBlock = new HashSet<>();
    public HashMap<String,String> constMap=new HashMap<>();
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
        buildDefUse();
        bfs(fun.blocks.getFirst().label);
        for (var var:latticeCells.entrySet()){
            if (var.getValue().a==Type.const_){
                constMap.put(var.getKey(),var.getValue().b);
            }
        }
    }
    
    void buildDefUse() {
        for (var block : fun.blocks) {
            for (var inst : block.phis) {
                _buildDefUse(inst, block.label);
            }
        }
        for (var block : fun.blocks) {
            for (var inst : block.insts) {
                _buildDefUse(inst, block.label);
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
            assert _val.equals(Integer.toString(Integer.parseInt(_val))) || _val.equals("null");
            return new Pair<>(t, val);
        }
    }
    
    Pair<Type, String> add(Pair<Type, String> a, Pair<Type, String> b, String op) {
        if (a.a == Type.top) return new Pair<>(b.a, b.b);
        if (b.a == Type.top) return new Pair<>(a.a, a.b);
        if (a.a == Type.bottom || b.a == Type.bottom) return new Pair<>(Type.bottom, null);
        if (a.a == Type.const_ && b.a == Type.const_) {
            if (op.equals("union")) if (a.b.equals(b.b)) return new Pair<>(a.a, a.b);
            else return new Pair<>(Type.bottom, null);
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
                case "div" -> {
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
                var cond = varInfo(is.cond.toString());
                if (cond.a != Type.bottom) {
                    if (!brBlock.containsKey(is.cond.toString())) brBlock.put(is.cond.toString(), new ArrayList<>());
                    brBlock.get(is.cond.toString()).add(_bl);
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
                latticeCells.put(def, new Pair<>(Type.top, null));
                for (int i = 0; i < is.labels.size(); i++) {
                    var val = is.values.get(i).toString();
                    if (val.charAt(0) == '%') {
                        if (uses.containsKey(val)) uses.get(val).add(it);
                    }
//                    handlePhi(is, _bl);
                }
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
    
    private void _bin(String def, IREntity lhs2, IREntity rhs2, String op, instNode it) {
        String lhs = lhs2.toString();
        String rhs = rhs2.toString();
        var lhs_ = varInfo(lhs);
        var rhs_ = varInfo(rhs);
        var res = add(lhs_, rhs_, op);
        latticeCells.put(def, res);
        if (res.a != Type.bottom) {
            uses.put(def, new ArrayList<>());
            if (lhs.charAt(0) == '%') {
                uses.get(lhs).add(it);
            }
            if (rhs.charAt(0) == '%') {
                uses.get(rhs).add(it);
            }
        }
    }
    
    private Pair<Type, String> __bin(String def, IREntity lhs2, IREntity rhs2, String op, instNode it) {
        String lhs = lhs2.toString();
        String rhs = rhs2.toString();
        var lhs_ = varInfo(lhs);
        var rhs_ = varInfo(rhs);
        return add(lhs_, rhs_, op);
    }
    
    void bfs(String entry) {
        edges.add(new Pair<>(null, entry));
        while (!edges.isEmpty()) {
            var edge = edges.getFirst();
            edges.removeFirst();
            if (visitedEdge.contains(edge)) continue;
            visitedEdge.add(edge);
            var block = bl.get(edge.b);
            for (var inst : block.phis) {
                int i = inst.labels.indexOf(edge.a);
                String var = inst.values.get(i).toString();
                var tp = varInfo(var);
                var old = latticeCells.get(inst.getDef());
                var rsl = add(old, tp, "union");
                if (!old.equals(rsl)) {
                    assert old.a != Type.bottom;
                    worklist.addAll(uses.get(inst.getDef()));
                    latticeCells.put(inst.getDef(), rsl);
//                    for (var ub : useByPhi.get(inst.getDef())) {
//                        var old_ = latticeCells.get(ub.phi_var);
//                        var rsl_ = add(old_, tp, "union");
//                        if (!old_.equals(rsl_)) {
//                            assert old_.a != Type.bottom;
//                            worklist.addAll(uses.get(ub.phi_var));
//                            latticeCells.put(ub.phi_var, rsl_);
//                        }
//                    }
                }
            }
            if (visitedBlock.contains(edge.b)) continue;
            visitedBlock.add(edge.b);
            for (var inst : block.insts) {
                switch (inst) {
                    case binaryInstNode is -> {
                        var rsl = __bin(is.getDef(), is.lhs, is.rhs, is.op.toString(), is);
                        var old = latticeCells.get(is.getDef());
                        var rsl_ = add(old, rsl, "union");
                        if (!old.equals(rsl_)) {
                            assert old.a != Type.bottom;
                            worklist.addAll(uses.get(is.getDef()));
                            latticeCells.put(is.getDef(), rsl_);
                        }
                    }
                    case icmpInstNode is -> {
                        var rsl = __bin(is.getDef(), is.lhs, is.rhs, is.op.toString(), is);
                        var old = latticeCells.get(is.getDef());
                        var rsl_ = add(old, rsl, "union");
                        if (!old.equals(rsl_)) {
                            assert old.a != Type.bottom;
                            worklist.addAll(uses.get(is.getDef()));
                            latticeCells.put(is.getDef(), rsl_);
                        }
                    }
                    default -> {
                        assert false;
                    }
                }
            }
        }
        while (!worklist.isEmpty()) {
            var inst = worklist.getFirst();
            worklist.removeFirst();
            switch (inst) {
                case binaryInstNode is -> {
                    var rsl = __bin(is.getDef(), is.lhs, is.rhs, is.op.toString(), is);
                    var old = latticeCells.get(is.getDef());
                    var rsl_ = add(old, rsl, "union");
                    if (!old.equals(rsl_)) {
                        assert old.a != Type.bottom;
                        worklist.addAll(uses.get(is.getDef()));
                        latticeCells.put(is.getDef(), rsl_);
                    }
                }
                case icmpInstNode is -> {
                    var rsl = __bin(is.getDef(), is.lhs, is.rhs, is.op.toString(), is);
                    var old = latticeCells.get(is.getDef());
                    var rsl_ = add(old, rsl, "union");
                    if (!old.equals(rsl_)) {
                        assert old.a != Type.bottom;
                        worklist.addAll(uses.get(is.getDef()));
                        latticeCells.put(is.getDef(), rsl_);
                    }
                }
                case phiInstNode is -> {
                    Pair<Type, String> rsl = new Pair<>(Type.top, null);
                    for (int i = 0; i < is.labels.size(); i++) {
                        var val = is.values.get(i).toString();
                        var tmp=varInfo(val);
                        rsl = add(rsl, tmp, "union");
                    }
                    var old = latticeCells.get(is.getDef());
                    var rsl_ = add(old, rsl, "union");
                    if (!old.equals(rsl_)) {
                        assert old.a != Type.bottom;
                        worklist.addAll(uses.get(is.getDef()));
                        latticeCells.put(is.getDef(), rsl_);
                    }
                }
                default -> {
                    assert false;
                }
            }
        }
    }
}
