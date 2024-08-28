package ASM.inst;

public class Arithimm extends Inst {
    public String op;
    public String rd;
    public String rs;
    public int imm;

    public Arithimm(String op, String rd, String rs, int imm){
        check(op);
        this.op = op;
        this.rd = rd;
        this.rs = rs;
        this.imm = imm;
    }

    public String toString(){
        return op + " " + rd + ", " + rs + ", " + imm;
    }
    
    void check(String op) {
        assert op.equals("addi") || op.equals("slli") || op.equals("slti") || op.equals("sltiu") || op.equals("xori") || op.equals("srli") || op.equals("srai") || op.equals("ori") || op.equals("andi");
    }
}
