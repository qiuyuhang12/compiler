package ASM.inst;

public class li extends inst{
    public String rd;
    public int imm;
    public li(String rd, int imm){
        this.rd = rd;
        this.imm = imm;
    }
    public String toString(){
        return "li " + rd + ", " + imm;
    }
}
