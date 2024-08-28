package ASM.inst;

public class Jump extends Inst {
    public String label;

    public Jump(String label) {
        this.label = label;
    }

    @Override
    public String toString() {
        return "j " + label;
    }
}
