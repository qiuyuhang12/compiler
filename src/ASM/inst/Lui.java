package ASM.inst;

public class Lui extends Inst{
    public String rd;
    public int imm;
    public Lui(String rd, int imm){
        this.rd = rd;
        this.imm = imm;
    }
    public String toString(){
        return "lui " + rd + ", " + imm;
    }
}
