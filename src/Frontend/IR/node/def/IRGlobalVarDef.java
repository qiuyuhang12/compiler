package Frontend.IR.node.def;

import Frontend.IR.entity.IRLiteral;
import Frontend.IR.node.IRNode;

public class IRGlobalVarDef extends IRNode {
    public String name;
    public IRLiteral value;

    public IRGlobalVarDef(String name, IRLiteral value) {
        assert name.charAt(0) == '@';
        this.name = name;
        this.value = value;
    }

    @Override
    public String toString() {
        return name + " = global " + value.typeInfo.toString() + " " + value.toString() + "\n";
    }
}
