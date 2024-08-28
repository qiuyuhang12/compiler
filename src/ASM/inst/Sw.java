package ASM.inst;

public class Sw extends Inst{
    public String val;
    public String addr;
    public int imm;
    public Sw(String val, String addr, int imm){
        this.val = val;
        this.addr = addr;
        this.imm = imm;
    }
    public String toString(){
        return "sw " + val + ", " + imm + "(" + addr + ")";
    }
}
