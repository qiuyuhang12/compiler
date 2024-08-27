package ASM.inst;

public class mv extends inst {//mv x,y   is   y->x
    public String rd;
    public String rs;
    public mv(String rd, String rs){
        this.rd = rd;
        this.rs = rs;
    }
    public String toString(){
        return "mv " + rd + ", " + rs;
    }
}
