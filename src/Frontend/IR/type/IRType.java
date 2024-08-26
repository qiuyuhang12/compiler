package Frontend.IR.type;

import Frontend.IR.node.IRNode;

public class IRType extends IRNode {
    public IRTypeEnum type;
    public IRTypeEnum ptrType;
    public String structInfo;
    public enum IRTypeEnum {
        i1,
        i32,
        ptr,
        void_,
        struct
    }
    public IRType(String s){
        switch (s){
            case "i1":
                this.type = IRTypeEnum.i1;
                break;
            case "i32":
                this.type = IRTypeEnum.i32;
                break;
            case "ptr":
                this.type = IRTypeEnum.ptr;
                break;
            case "void":
                this.type = IRTypeEnum.void_;
                break;
            default:
                assert false;
        }
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
            case struct -> //TODO:Class\Struct Info
                //                assert false;
                //                yield "struct";
                    structInfo;
            default -> {
                assert false;
                yield null;
            }
        };
    }
}
