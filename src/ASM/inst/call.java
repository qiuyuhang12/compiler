package ASM.inst;

public class call extends inst{
    public String name;
    public call(String name){
        this.name = name;
    }
    public String toString(){
        return "call " + name;
    }
}
