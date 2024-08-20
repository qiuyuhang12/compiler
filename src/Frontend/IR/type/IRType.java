package Frontend.IR.type;

import Frontend.IR.node.IRNode;

public class IRType extends IRNode {
    public IRTypeEnum type;
    public IRTypeEnum ptrType;
    public IRStruct structInfo;
    public enum IRTypeEnum {
        i1,
        i32,
        ptr,
        void_,
        struct
    }
    public IRType(IRTypeEnum type){
        this.type = type;
    }
    public IRType(){
        this.type = IRTypeEnum.void_;
    }
    @Override
    public String toString(){
        return switch (type) {
            case i1 -> "i1";
            case i32 -> "i32";
            case ptr -> "ptr";
            case void_ -> "void";
            case struct -> {
                //TODO:Class\Struct Info
//                assert false;
//                yield "struct";
                yield structInfo.toString();
            }
            default -> {
                assert false;
                yield null;
            }
        };
    }
}
