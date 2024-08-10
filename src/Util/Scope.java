package Util;

//import MIR.register;

import Util.error.semanticError;

import java.util.HashMap;

public class Scope {
    private Scope parentScope;
    private HashMap<String, Class_> Classes = new HashMap<>();//only in global scope
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

    public boolean containsVar(String name, boolean lookUpon) {
        if (Vars.containsKey(name)) return true;
        else if (parentScope != null && lookUpon) return parentScope.containsVar(name, true);
        else return false;
    }

    public Type getVarType(String name, boolean lookUpon) {
        System.err.println("\nWARNING: \tgetVarType 可能返回NULL并且不THROW.\n\t\t\t程序所在路径: src/Util/Scope.java");
        if (Vars.containsKey(name)) return Vars.get(name);
        else if (parentScope != null && lookUpon) return parentScope.getVarType(name, true);
        return null;
    }



    public void addClass(String name, Class_ c, position pos) {
        if (Classes.containsKey(name)) throw new semanticError("Class multiple definition of " + name, pos);
        Classes.put(name, c);
    }

    public Class_ getClassFromName(String name, position pos) {
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




}
