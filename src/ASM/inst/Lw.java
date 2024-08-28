package ASM.inst;

public class Lw extends Inst{
    public String rd;
    public String rs;
    public int imm;
    public Lw(String rd, String rs, int imm){
        this.rd = rd;
        this.rs = rs;
        this.imm = imm;
    }
    public String toString(){
        return "lw " + rd + ", " + imm + "(" + rs + ")";
    }
}
