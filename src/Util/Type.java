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

    public boolean isfString = false;
    public TypeEnum atomType = null;
    public boolean isArray = false;
    public boolean isFun = false;
    public int dim = 0;
    //    public boolean isClass = false;
    public String name = null;

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

    public Type clone() /*throws CloneNotSupportedException */{
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
//    public HashMap<String, Type> members = null;
}
