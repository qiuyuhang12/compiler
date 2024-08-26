package Frontend.IR.type;

import Frontend.IR.node.IRNode;

import java.util.ArrayList;

public class IRStruct extends IRNode {
//    String name;
    public ArrayList<IRType> struct=new ArrayList<>();
    @Override
    public String toString(){
        StringBuilder ret = new StringBuilder();
//        StringBuilder ret = new StringBuilder("<");
        for (int i = 0; i < struct.size(); i++) {
            if (i > 0) ret.append(", ");
            if (struct.get(i).type == IRType.IRTypeEnum.i1)
                ret.append("i32");
            else if (struct.get(i).type == IRType.IRTypeEnum.i32)
                ret.append("i32");
            else if (struct.get(i).type == IRType.IRTypeEnum.ptr)
                ret.append("ptr");
            else
                assert false;
//            ret.append(struct.get(i).toString());
        }
//        ret.append(">");
        return ret.toString();
//        return name;
    }
}
