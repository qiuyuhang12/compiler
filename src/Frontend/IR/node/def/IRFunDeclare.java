package Frontend.IR.node.def;

import Frontend.IR.entity.IRVar;
import Frontend.IR.node.IRNode;
import Frontend.IR.type.IRType;

import java.util.ArrayList;
import java.util.Arrays;

public class IRFunDeclare extends IRNode {
    public IRType returnType;
    public String name;
    public ArrayList<IRVar> parameters = new ArrayList<>();

    public IRFunDeclare(IRType returnType, String name, IRVar... parameters) {
        assert name.charAt(0) == '@';
        this.returnType = returnType;
        this.name = name;
        this.parameters.addAll(Arrays.asList(parameters));
    }

    public void pushPara(IRVar parameter) {
        parameters.add(parameter);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("declare ").append(returnType.toString()).append(" ").append(name).append("(");
        for (int i = 0; i < parameters.size(); i++) {
            if (i != 0) sb.append(", ");
            sb.append(parameters.get(i).typeInfo.toString()).append(" ").append(parameters.get(i).toString());
        }
        sb.append(")");
        return sb.toString() + "\n";
    }
}
