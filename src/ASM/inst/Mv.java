package ASM.inst;

public class Mv extends Inst {//mv x,y   is   y->x
    public String rd;
    public String rs;
    public Mv(String rd, String rs){//rs->rd
        assert !rd.equals(rs);
        this.rd = rd;
        this.rs = rs;
    }
    public String toString(){
        if (rd.equals(rs)) return "";
        return "mv " + rd + ", " + rs;
    }
}
