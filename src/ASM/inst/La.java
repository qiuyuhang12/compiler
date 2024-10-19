package ASM.inst;

public class La extends Inst{
    public String rd;
    public String symbol;
    public La(String rd, String symbol) {
        assert !rd.equals("x0");
        this.rd = rd;
        this.symbol = symbol;
    }
    @Override
    public String toString() {
        return "la " + rd + ", " + symbol;
    }
}
