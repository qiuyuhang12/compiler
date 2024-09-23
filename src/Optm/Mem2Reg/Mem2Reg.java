package Optm.Mem2Reg;

import Frontend.IR.IRProgramNode;

public class Mem2Reg {
    public IRProgramNode ir;
    public Mem2Reg(IRProgramNode ir) {
        this.ir = ir;
    }
    public void run() {
        for (int i = 0; i < ir.funDefs.size(); i++) {
            M2r_Fun m2r_fun = new M2r_Fun(ir.funDefs.get(i));
            m2r_fun.run();
        }
        for (int i =0; i<ir.initFuns.size();i++){
            M2r_Fun m2r_fun = new M2r_Fun(ir.initFuns.get(i));
            m2r_fun.run();
        }
    }
}
