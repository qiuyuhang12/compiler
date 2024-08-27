package ASM.inst;

public class jump extends inst {
    public String label;

    public jump(String label) {
        this.label = label;
    }

    @Override
    public String toString() {
        return "j " + label;
    }
}
