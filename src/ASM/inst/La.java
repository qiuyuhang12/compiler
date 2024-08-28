package ASM.inst;

public class La extends Inst{
    public String rd;
    public String symbol;
    public La(String rd, String symbol) {
        this.rd = rd;
        this.symbol = symbol;
    }
    @Override
    public String toString() {
        return "la " + rd + ", " + symbol;
    }
}
