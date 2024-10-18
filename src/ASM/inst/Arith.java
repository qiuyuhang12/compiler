package ASM.inst;

public class Arith extends Inst {
    public String op;
    public String rd;
    public String rs1;
    public String rs2;
    
    public Arith(String op, String rd, String rs1, String rs2) {
        check(op);
        this.op = op;
        this.rd = rd;
        this.rs1 = rs1;
        this.rs2 = rs2;
    }
    
    public Arith(String op, String rd, String rs1) {
        assert op.equals("neg");
        this.op = op;
        this.rd = rd;
        this.rs1 = rs1;
    }
    
    public String toString() {
        if (this.op.equals("neg")) {
            return this.op + " " + this.rd + ", " + this.rs1;
        }
        return this.op + " " + this.rd + ", " + this.rs1 + ", " + this.rs2;
    }
    
    void check(String op) {
        assert op.equals("add") || op.equals("sub") || op.equals("mul") || op.equals("and") || op.equals("or") || op.equals("xor") || op.equals("sll") || op.equals("sra") || op.equals("srl") || op.equals("div") || op.equals("rem");
    }
}
