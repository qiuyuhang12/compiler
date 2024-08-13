package Util;

//import MIR.register;

import Util.error.semanticError;

import java.util.HashMap;

public class Scope {
    static public enum scopeType {
        GLOBAL,CLASS, FUNCTION, FOR,IF,WHILE,BLOCK
    }
    public String name;
    private Scope.scopeType type;
    private Scope parent;
    private HashMap<String, Class_> Classes = new HashMap<>();//only in global scope
    private HashMap<String, Function> Funs = new HashMap<>();//only in global scope
    private HashMap<String, Type> Vars = new HashMap<>();//int aaa;-> Type String;
    public Type funcReturnType;
    public Type correctFuncReturnType;//int f(); 那么correctFuncReturnType 是 int
    public boolean existReturnForFun = false;
    public Scope parent() {
        return parent;
    }

    public Scope.scopeType getType() {
        return type;
    }

    public Scope(Scope parent) {
        this.parent = parent;
        this.type = Scope.scopeType.GLOBAL;
    }

    public Scope(Scope parent, Scope.scopeType scopeType, String name) {
        this.parent = parent;
        this.type = scopeType;
        this.name = name;
    }
    public void addVar(String name, Type t, position pos) {
        if (Vars.containsKey(name)) throw new semanticError("Var multiple definition of " + name, pos);
        Vars.put(name, t);
    }

    public boolean containsVar(String name, boolean lookUpon) {
        if (Vars.containsKey(name)) return true;
        else if (parent != null && lookUpon) return parent.containsVar(name, true);
        else return false;
    }

    public Type getVarType(String name, boolean lookUpon) {
//        System.err.println("\nWARNING: \tgetVarType 可能返回NULL并且不THROW.\n\t\t\t程序所在路径: src/Util/Scope.java");
        if (Vars.containsKey(name)) return Vars.get(name);
        else if (parent != null && lookUpon) return parent.getVarType(name, true);
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

    public boolean containsClass(String name) {
        if (Classes.containsKey(name)) return true;
        else return false;
    }

    public Type getThisClass() {
        if (type == Scope.scopeType.CLASS) {
            Type tmp=new Type(Type.TypeEnum.CLASS);
            tmp.name = name;
            return tmp;
        }
        else if (parent != null) return parent.getThisClass();
        else return null;
    }


    public void addFun(String name, Function f, position pos) {
        if (Funs.containsKey(name)) throw new semanticError("Function multiple definition of " + name, pos);
        Funs.put(name, f);
    }

    public boolean containsFun(String name) {
        if (Funs.containsKey(name)) return true;
        else return false;
    }

    public Function getFunFromName(String name, position pos) {
        if (Funs.containsKey(name)) return Funs.get(name);
        throw new semanticError("no such function: " + name, pos);
    }

    public void addBuiltInFunction(String name, Function f) {
        Funs.put(name, f);
    }

    public void addBuiltInFunction() {
        Type int_ = new Type(Type.TypeEnum.INT);
        Type string_ = new Type(Type.TypeEnum.STRING);
        addBuiltInFunction("print", new Function("print", new Type(Type.TypeEnum.VOID), string_));
        addBuiltInFunction("println", new Function("println", new Type(Type.TypeEnum.VOID), string_));
        addBuiltInFunction("printInt", new Function("printInt", new Type(Type.TypeEnum.VOID), int_));
        addBuiltInFunction("printlnInt", new Function("printlnInt", new Type(Type.TypeEnum.VOID), int_));
        addBuiltInFunction("getString", new Function("getString", string_));
        addBuiltInFunction("getInt", new Function("getInt", int_));
        addBuiltInFunction("toString", new Function("toString", string_, int_));
    }

}
