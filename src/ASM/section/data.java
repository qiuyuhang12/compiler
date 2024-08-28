package ASM.section;

public class data extends section {
    public int value = 0;
    
    public data(String label, int value) {
        super(label);
        this.value = value;
    }
    
    public String toString() {
        return "\t.section .data\n" +
                "\t.globl " + label + "\n" +
                label + ":\n" +
                "\t.word  " + value + "\n";
    }
}
