package Frontend.IR.type;

public class IRType {
    public irTypeEnum type;
    public irTypeEnum ptrType;
    public IRStruct structInfo;
    public enum irTypeEnum{
        i1,
        i32,
        ptr,
        void_,
        struct
    }
    public IRType(irTypeEnum type){
        this.type = type;
    }
    public IRType(){}
    public String toString(){
        return switch (type) {
            case i1 -> "i1";
            case i32 -> "i32";
            case ptr -> "ptr";
            case void_ -> "void";
            case struct -> {
                //TODO:Class\Struct Info
                assert false;
                yield "struct";
            }
        };
    }
}
