package Util;

public class Type {
    public enum TypeEnum {
        VOID,
        BOOL,
        INT,
        STRING,
        NULL,
        //        ARRAY,
        CLASS
    }
    
    static public class funInfo {
        public boolean inGlobal = false;
        public boolean isInBuildMethod = false;//全局内建函数不在此列,也就是只有形如array.size()的
        public String funName;
        public String className;
        
        public funInfo(String funName, String className) {
            this.funName = funName;
            this.className = className;
        }
        
        public funInfo() {
        }
        
        public funInfo(String funName) {
            this.funName = funName;
            this.inGlobal = true;
        }
    }
    
    public boolean isfString = false;
    public TypeEnum atomType = null;
    public boolean isArray = false;
    public boolean isFun = false;//函数表达式
    public funInfo fun = new funInfo(null, null);
    public int dim = 0;
    //    public boolean isClass = false;
    public String name = null;//for class
    
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Type t) {
            boolean isfStringEqual = this.isfString == t.isfString;
            boolean atomTypeEqual = this.atomType == t.atomType;
            boolean isArrayEqual = this.isArray == t.isArray;
            boolean isFunEqual = this.isFun == t.isFun;
            boolean dimEqual = this.dim == t.dim;
            boolean nameEqual = (this.name == null && t.name == null) ||
                    ((this.name != null && t.name != null) && this.name.equals(t.name));
            return isfStringEqual && atomTypeEqual && isArrayEqual && isFunEqual && dimEqual && nameEqual;
        }
        return false;
    }
    
    public Type(TypeEnum tn) {
        this.atomType = tn;
    }
    
    public Type() {
    }
    
    public Type clone() /*throws CloneNotSupportedException */ {
        if (this == null) return null;
//        try {
//            Type ret = (Type) super.clone();
        Type ret = new Type();
        ret.isfString = this.isfString;
        ret.atomType = this.atomType;
        ret.isArray = this.isArray;
        ret.isFun = this.isFun;
        ret.dim = this.dim;
        ret.name = this.name;
        return ret;
//        }catch (CloneNotSupportedException e) {
//            System.err.println("不应该发生，因为我们实现了 Cloneable");
//        }
//        assert false;
//        return null;
    }
    
    //a construction for inbuildmethod
    public Type(TypeEnum tn, String name) {
        this.atomType = tn;
        this.isFun = true;
        this.fun.funName = name;
        this.fun.isInBuildMethod = true;
    }
//    public HashMap<String, Type> members = null;
}