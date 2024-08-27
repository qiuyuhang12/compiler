package ASM.inst;

public class lw extends inst{
    public String rd;
    public String rs;
    public String imm;
    public lw(String rd, String rs, String imm){
        this.rd = rd;
        this.rs = rs;
        this.imm = imm;
    }
    public String toString(){
        return "lw " + rd + ", " + imm + "(" + rs + ")";
    }
}
