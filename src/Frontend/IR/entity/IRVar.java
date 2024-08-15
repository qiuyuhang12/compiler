package Frontend.IR.entity;

import Frontend.IR.type.IRType;

public class IRVar extends IREntity{
    public String name;
    public boolean isGlobal;
    public IRVar(String type, String name, boolean isGlobal){
        switch (type){
            case "i1":
                typeInfo = new IRType(IRType.irTypeEnum.i1);
                break;
            case "i32":
                typeInfo = new IRType(IRType.irTypeEnum.i32);
                break;
            case "ptr":
                typeInfo = new IRType(IRType.irTypeEnum.ptr);
                break;
            case "void":
                assert false;
            default:
                typeInfo = new IRType(IRType.irTypeEnum.struct);
        }
        this.name = name;
        this.isGlobal = isGlobal;
    }
    public IRVar(){
        typeInfo = new IRType(IRType.irTypeEnum.void_);
    }
    @Override
    public String toString(){
        if (isGlobal){
//            return typeInfo.toString()+" @"+name;
            return "@"+name;
        }else {
//            return typeInfo.toString()+" %"+name;
            return "%"+name;
        }
    }
}
