package Frontend.IR.entity;

import Frontend.IR.type.IRType;

public class IRIntLiteral extends IRLiteral {
    public int value;
    public IRIntLiteral(int value) {
        this.value = value;
        typeInfo.type = IRType.IRTypeEnum.i32;
    }
    @Override
    public String toString() {
        return Integer.toString(value);
    }
    @Override
    public int getVal() {
        return value;
    }
}
