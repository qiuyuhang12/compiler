package ASM.inst;

public class arith extends inst {
    public String op;
    public String rd;
    public String rs1;
    public String rs2;
    
    public arith(String op, String rd, String rs1, String rs2) {
        check(op);
        this.op = op;
        this.rd = rd;
        this.rs1 = rs1;
        this.rs2 = rs2;
    }
    
    public String toString() {
        return this.op + " " + this.rd + ", " + this.rs1 + ", " + this.rs2;
    }
    
    void check(String op) {
        assert op.equals("add") || op.equals("sub") || op.equals("mul") || op.equals("and") || op.equals("or") || op.equals("xor") || op.equals("sll") || op.equals("sra") || op.equals("srl");
    }
}
