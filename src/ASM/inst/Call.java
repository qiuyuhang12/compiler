package ASM.inst;

public class Call extends Inst{
    public String name;
    public Call(String name){
        this.name = name;
    }
    public String toString(){
        return "call " + name;
    }
}
