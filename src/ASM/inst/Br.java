package ASM.inst;

public class Br extends Inst {
    public String op;
    public String label;
    public String rs1;
    public String rs2;
    public Br(String op, String rs1, String rs2, String label) {
        check(op);
        this.op = op;
        this.label = label;
        this.rs1 = rs1;
        this.rs2 = rs2;
    }
    public String toString() {
        return this.op + " " + this.rs1 + ", " + this.rs2 + ", " + this.label;
    }
    void check(String op) {
        assert op.equals("beq") || op.equals("bne") || op.equals("blt") || op.equals("bge") || op.equals("bltu") || op.equals("bgeu")|| op.equals("bgt")|| op.equals("ble");
    }
    
}
