package Frontend.IR.entity;

import Frontend.IR.node.IRNode;
import Frontend.IR.type.IRType;

public abstract class IREntity extends IRNode {
    public IRType typeInfo;
    public IREntity(){
        typeInfo = new IRType();
    }
    public abstract String toString();
    abstract public int getVal();
}
