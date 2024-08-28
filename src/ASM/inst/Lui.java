package ASM.inst;

public class Lui extends Inst{
    public String rd;
    public String imm;
    public Lui(String rd, String imm){
        this.rd = rd;
        this.imm = imm;
    }
    public String toString(){
        return "lui " + rd + ", " + imm;
    }
}
