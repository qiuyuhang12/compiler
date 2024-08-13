package Util.error;
import Util.position;

public class semanticError extends error {
    public enum errorType{
        Invalid_Identifier,
        Multiple_Definition,
        Undefined_Identifier,
        Type_Mismatch,
        Invalid_Control_Flow,
        Invalid_Type,
        Missing_Return_Statement,
        Dimension_Out_Of_Bound,
    }
    public errorType type;
    public void printType(){
        switch(type){
            case Invalid_Identifier:
                System.out.println("Invalid Identifier");
                break;
            case Multiple_Definition:
                System.out.println("Multiple Definition");
                break;
            case Undefined_Identifier:
                System.out.println("Undefined Identifier");
                break;
            case Type_Mismatch:
                System.out.println("Type Mismatch");
                break;
            case Invalid_Control_Flow:
                System.out.println("Invalid Control Flow");
                break;
            case Invalid_Type:
                System.out.println("Invalid Type");
                break;
            case Missing_Return_Statement:
                System.out.println("Missing Return Statement");
                break;
            case Dimension_Out_Of_Bound:
                System.out.println("Dimension Out Of Bound");
                break;
        }
    }
    public semanticError(String msg, position pos) {
        super("Semantic Error: " + msg, pos);
    }

    public semanticError(String msg, position pos, errorType type) {
        super("Semantic Error: " + msg, pos);
        this.type = type;
    }

}
