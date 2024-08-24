package Frontend.IR.node.def;

import Frontend.IR.node.IRNode;

public class IRStringDef extends IRNode {
    public String name;
    public String value;

    public IRStringDef(String name, String value) {
        assert name.charAt(0) == '@';
        this.name = name;
        this.value = value;
    }

    @Override
    public String toString() {
        assert false;
//        十进制
        value.replace("\"", "\22");
        value.replace("\n", "\12");
//        value.replace("\\", "\");
        return name + " = private unnamed_addr constant [" + (value.length()+1) + " x i8] c\"" + value + "\\00\"\n";
    }
}
