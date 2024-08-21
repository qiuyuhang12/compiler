package Frontend.IR.type;

import Frontend.IR.node.IRNode;

import java.util.ArrayList;

public class IRStruct extends IRNode {
//    String name;
    public ArrayList<IRType> struct;
    @Override
    public String toString(){
        StringBuilder ret = new StringBuilder();
//        StringBuilder ret = new StringBuilder("<");
        for (int i = 0; i < struct.size(); i++) {
            if (i > 0) ret.append(", ");
            ret.append(struct.get(i).toString());
        }
//        ret.append(">");
        return ret.toString();
//        return name;
    }
}
