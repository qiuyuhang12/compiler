package Frontend.IR.entity;

import Frontend.IR.type.IRType;

public class IRNullPtrLiteral extends IRLiteral {
    public IRNullPtrLiteral() {
        typeInfo.type = IRType.IRTypeEnum.ptr;
    }

    @Override
    public String toString() {
        return "null";
    }
}
