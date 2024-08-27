package ASM.inst;

public class lui extends inst{
    public String rd;
    public String imm;
    public lui(String rd, String imm){
        this.rd = rd;
        this.imm = imm;
    }
    public String toString(){
        return "lui " + rd + ", " + imm;
    }
}
