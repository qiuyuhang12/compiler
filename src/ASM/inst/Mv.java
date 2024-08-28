package ASM.inst;

public class Mv extends Inst {//mv x,y   is   y->x
    public String rd;
    public String rs;
    public Mv(String rd, String rs){//rs->rd
        this.rd = rd;
        this.rs = rs;
    }
    public String toString(){
        return "mv " + rd + ", " + rs;
    }
}