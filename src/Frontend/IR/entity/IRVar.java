package Frontend.IR.entity;

import Frontend.IR.type.IRType;

public class IRVar extends IREntity{
    public String name;
    public boolean isGlobal;
    public IRVar(String type, String name, boolean isGlobal){
        switch (type){
            case "i1":
                typeInfo = new IRType(IRType.IRTypeEnum.i1);
                break;
            case "i32":
                typeInfo = new IRType(IRType.IRTypeEnum.i32);
                break;
            case "ptr":
                typeInfo = new IRType(IRType.IRTypeEnum.ptr);
                break;
            case "void":
                break;
//                assert false;
            default:
                typeInfo = new IRType(IRType.IRTypeEnum.struct);
        }
        this.name = name;
        this.isGlobal = isGlobal;
    }
    public IRVar(){
        typeInfo = new IRType(IRType.IRTypeEnum.void_);
    }
    @Override
    public String toString(){
        return name;
//        if (isGlobal){
////            return typeInfo.toString()+" @"+name;
//            return "@"+name;
//        }else {
////            return typeInfo.toString()+" %"+name;
//            return "%"+name;
//        }
    }
    @Override
    public int getVal(){
        assert false;
        return 0;
    }
}
