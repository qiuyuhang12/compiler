package Frontend.IR.entity;
import Frontend.IR.type.IRType;
public class IRBoolLiteral extends IRLiteral {
    public boolean value;
    public IRBoolLiteral(boolean value) {
        this.value = value;
        typeInfo.type = IRType.irTypeEnum.i1;
    }

    @Override
    public String toString() {
        return value ? "true" : "false";
    }
}
