package ASM.inst;

public class Li extends Inst {
    public String rd;
    public int imm;
    public Li(String rd, int imm){
        this.rd = rd;
        this.imm = imm;
    }
    public String toString(){
        return "li " + rd + ", " + imm;
    }
}
