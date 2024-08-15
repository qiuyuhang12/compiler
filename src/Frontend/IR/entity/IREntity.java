package Frontend.IR.entity;

import Frontend.IR.type.IRType;

public abstract class IREntity {
    public IRType typeInfo;
    public IREntity(){
        typeInfo = new IRType();
    }
    public abstract String toString();
}
