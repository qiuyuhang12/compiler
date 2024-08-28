package ASM.inst;

public class Comment extends Inst{
    private String comment;

    public Comment(String comment) {
        this.comment = comment;
    }

    @Override
    public String toString() {
        return "# " + comment;
    }
}
