package Util;

//import MIR.register;

import Util.error.semanticError;

import java.util.HashMap;

public class Scope {
    private Scope parentScope;
    private HashMap<String, Class> Classes = new HashMap<>();//only in global scope
    private HashMap<String, Function> Funs = new HashMap<>();//only in global scope
    private HashMap<String, Type> Vars = new HashMap<>();//int aaa;-> Type String;

    public Scope parentScope() {
        return parentScope;
    }

    public Scope(Scope parentScope) {
        this.parentScope = parentScope;
    }

    public void addVar(String name, Type t, position pos) {
        if (Vars.containsKey(name)) throw new semanticError("Var multiple definition of " + name, pos);
        Vars.put(name, t);
    }

    public Type getTypeFromName(String name, position pos) {
        if (Vars.containsKey(name)) return Vars.get(name);
        throw new semanticError("no such type: " + name, pos);
    }

    public void addClass(String name, Class c, position pos) {
        if (Classes.containsKey(name)) throw new semanticError("Class multiple definition of " + name, pos);
        Classes.put(name, c);
    }

    public Class getClassFromName(String name, position pos) {
        if (Classes.containsKey(name)) return Classes.get(name);
        throw new semanticError("no such class: " + name, pos);
    }

    public void addFun(String name, Function f, position pos) {
        if (Funs.containsKey(name)) throw new semanticError("Function multiple definition of " + name, pos);
        Funs.put(name, f);
    }

    public Function getFunFromName(String name, position pos) {
        if (Funs.containsKey(name)) return Funs.get(name);
        throw new semanticError("no such function: " + name, pos);
    }

    public boolean containsVariable(String name, boolean lookUpon) {
        if (Vars.containsKey(name)) return true;
        else if (parentScope != null && lookUpon) return parentScope.containsVariable(name, true);
        else return false;
    }

    public Type getType(String name, boolean lookUpon) {
        if (Vars.containsKey(name)) return Vars.get(name);
        else if (parentScope != null && lookUpon) return parentScope.getType(name, true);
        return null;
    }
}
