package Optm.Mem2Reg;

import Frontend.IR.IRProgramNode;
import Frontend.IR.util.Renamer;

public class Mem2Reg {
    public IRProgramNode ir;
    public Renamer renamer;
    public Mem2Reg(IRProgramNode ir, Renamer renamer) {
        this.ir = ir;
        this.renamer = renamer;
    }
    public void run() {
        for (int i = 0; i < ir.funDefs.size(); i++) {
            M2r_Fun m2r_fun = new M2r_Fun(ir.funDefs.get(i), renamer);
            m2r_fun.run();
        }
        for (int i =0; i<ir.initFuns.size();i++){
            M2r_Fun m2r_fun = new M2r_Fun(ir.initFuns.get(i), renamer);
            m2r_fun.run();
        }
    }
}
