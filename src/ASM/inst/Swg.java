package ASM.inst;

public class Swg extends Inst {
    public String val;
    public String symbol;
    public String rt;
    
    public Swg(String val, String symbol, String rt) {
        this.val = val;
        this.symbol = symbol;
        this.rt = rt;
    }
    
    public String toString() {
        return "sw " + val + ", " + symbol + ", " + rt;
    }
}
