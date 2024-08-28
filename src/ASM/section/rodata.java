package ASM.section;

public class rodata extends section {
    public String value=null;
    
    public rodata(String label, String value) {
        super(label);
        System.err.println("IR的string与asm不同");
        this.value = value;
    }
    
    public String toString() {
        return "\t.section .rodata\n" +
                "\t.globl " + label + "\n" +
                label + ":\n" +
                "\t.asciz  " + value + "\n";
    }
}
