package ASM.inst;

public class sw extends inst{
    public String rs1;
    public String rs2;
    public String imm;
    public sw(String rs1, String rs2, String imm){
        this.rs1 = rs1;
        this.rs2 = rs2;
        this.imm = imm;
    }
    public String toString(){
        return "sw " + rs1 + ", " + imm + "(" + rs2 + ")";
    }
}
