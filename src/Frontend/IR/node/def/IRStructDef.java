package Frontend.IR.node.def;

import Frontend.IR.node.IRNode;
import Frontend.IR.type.IRStruct;

public class IRStructDef extends IRNode {
    public String name;
    public IRStruct struct;

    public IRStructDef(String name, IRStruct struct) {
        assert name.charAt(0) == '%';
        this.name = name;
        this.struct = struct;
    }

    @Override
    public String toString() {
        return name + " = type { " + struct.toString() + " }\n";
    }
}
