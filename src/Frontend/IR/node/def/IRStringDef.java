package Frontend.IR.node.def;

import Frontend.IR.node.IRNode;

public class IRStringDef extends IRNode {
    public String name;
    public String value;

    public IRStringDef(String name, String value) {
        assert name.charAt(0) == '@';
        this.name = name;
        this.value = value.replace("$$","$");
    }

    @Override
    public String toString() {
        String ret=value;
        String tmp=value.replace("\\\"", "\042");
        tmp=tmp.replace("\\n", "\012");
        tmp=tmp.replace("\\\\", "\134");
        value=value.replace("\\\"", "\\22");
        value=value.replace("\\n", "\\0A");
        String rsl=name + " = private unnamed_addr constant [" + (tmp.length()+1) + " x i8] c\"" + value + "\\00\"\n";
        value=ret;
        return rsl;
//        return name + " = private unnamed_addr constant [" + (tmp.length()+1) + " x i8] c\"" + value + "\\00\"\n";
    }
}
