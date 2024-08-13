package Util;

import java.util.ArrayList;
import java.util.HashMap;

public class Function {
    public String name;
    public Type returnType;
    public ArrayList<Type> paramTypes = new ArrayList<>();

    public Function() {
        this.name = null;
        this.returnType = null;
    }

    public Function(String name, Type returnType) {
        this.name = name;
        this.returnType = returnType;
    }

    //only one parameter!
    public Function(String name, Type returnType, Type tp) {
        this.name = name;
        this.returnType = returnType;
        this.paramTypes.add(tp);
    }

    public Function(String name, Type returnType, Type tp1, Type tp2) {
        this.name = name;
        this.returnType = returnType;
        this.paramTypes.add(tp1);
        this.paramTypes.add(tp2);
    }

    static public Function lengthInBuild = new Function("length", new Type(Type.TypeEnum.INT));
    static public Function substringInBuild = new Function("substring", new Type(Type.TypeEnum.STRING), new Type(Type.TypeEnum.INT), new Type(Type.TypeEnum.INT));
    static public Function parseIntInBuild = new Function("parseInt", new Type(Type.TypeEnum.INT));
    static public Function ordInBuild = new Function("ord", new Type(Type.TypeEnum.INT), new Type(Type.TypeEnum.INT));
    static public Function sizeInBuild = new Function("size", new Type(Type.TypeEnum.INT));
}
