package ASM.inst;

public class S_z extends Inst {
    public String rs,rd,op;
    public S_z(String op,String rd,String rs){
        assert !rd.equals("x0");
        check(op);
        this.op = op;
        this.rd = rd;
        this.rs = rs;
    }
    void check(String op){
        assert op.equals("seqz") || op.equals("snez") || op.equals("sltz") || op.equals("sgtz");
    }
    public String toString(){
        return this.op + " " + this.rd + ", " + this.rs;
    }
}
