package Frontend.IR.type;

import Frontend.IR.node.IRNode;

import java.util.ArrayList;

public class IRStruct extends IRNode {
    String name;
    public ArrayList<IRType> struct;
    @Override
    public String toString(){
        return name;
    }
}
