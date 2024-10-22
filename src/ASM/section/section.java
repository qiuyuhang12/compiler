package ASM.section;

public abstract class section {
    public String label;
    public section(String label){
        this.label = label;
    }
    abstract public String toString();
}
