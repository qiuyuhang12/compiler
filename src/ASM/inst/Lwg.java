package ASM.inst;

public class Lwg extends Inst {
    public String rd;
    public String symbol;
    
    public Lwg(String rd, String symbol) {
        this.rd = rd;
        this.symbol = symbol;
    }
    
    public String toString() {
        return "lw " + rd + ", " + symbol;
    }
}
