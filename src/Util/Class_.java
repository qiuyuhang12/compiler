package Util;

import Util.error.semanticError;

import java.util.HashMap;

public class Class_ {
    public String name;
    private HashMap<String, Type> vars = new HashMap<>();//int aaa;-> Type String;
    private HashMap<String, Function> funs = new HashMap<>();

    public void addVar(String name, Type t, position pos) {
        if (vars.containsKey(name)) throw new semanticError("Var multiple definition of " + name, pos, semanticError.errorType.Multiple_Definitions);
        vars.put(name, t);
    }

    public Type getTypeFromVarName(String name, position pos) {
        if (vars.containsKey(name)) return vars.get(name);
        throw new semanticError("no such type: " + name, pos);
    }

    public boolean containsVar(String name) {
        if (vars.containsKey(name)) return true;
        else return false;
    }

    public void addFun(String name, Function f, position pos) {
        if (funs.containsKey(name)) throw new semanticError("Function multiple definition of " + name, pos);
        funs.put(name, f);
    }

    public Function getFunFromName(String name, position pos) {
        if (funs.containsKey(name)) return funs.get(name);
        throw new semanticError("no such function: " + name, pos);
    }

    public boolean containsFun(String name) {
        if (funs.containsKey(name)) return true;
        else return false;
    }


}