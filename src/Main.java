import ASM.Reg_al_asm;
import ASM.asmBuilder;
import AST.ProgramNode;
//import Assembly.AsmFn;
//import Backend.*;
//import Frontend.SemanticChecker;
//import Frontend.SymbolCollector;
//import MIR.block;
//import MIR.mainFn;
import Frontend.ASTBuilder;
import Frontend.IR.IRBuilder;
import Frontend.SemanticChecker;
import Frontend.SymbolCollector;
import Optm.Mem2Reg.Color;
import Optm.Mem2Reg.Mem2Reg;
import Parser.MxLexer;
import Parser.MxParser;
//import Util.MxErrorListener;
import Util.Consts;
import Util.MxErrorListener;
import Util.Scope;
import Util.error.error;
import Util.error.semanticError;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

import java.io.*;
import java.util.ArrayList;


public class Main {
//todo::内建函数
    
    public static void main(String[] args) throws Exception {
        int K = 24;
        boolean asm = false, new_asm = false, redirect_input = false, redirect_output = false, redirect_err = false, show_color = false, oj = false;
        asm = true;
        new_asm = true;
//        redirect_input = true;
//        redirect_output = true;
//        redirect_err = true;
//        show_color = true;
        Consts.colour = true;
        Consts.colourCheck = true;
        Consts.sccp = true;
        Consts.dce = true;
//        Consts.showsccp = true;
        Consts.m2r = true;
        oj = true;
        if (redirect_output) {
//        if (true) {
            PrintStream fileOut;
            // 创建一个 PrintStream 对象，将输出重定向到文件
            if (!asm) fileOut = new PrintStream(new FileOutputStream("output.ll"));
            else fileOut = new PrintStream(new FileOutputStream("test.s"));
//            PrintStream output =new PrintStream(new FileOutputStream("src/output.ll"));
            // 将标准输出流重定向到文件
            System.setOut(fileOut);
        }
        if (redirect_err) {
            PrintStream fileErr = new PrintStream(new FileOutputStream("errput.txt"));
            System.setErr(fileErr);
        }
        InputStream input;
        if (redirect_input) {
            String name = "test.mx";
            input = new FileInputStream(name);
        } else input = System.in;
        
        try (FileWriter writer = new FileWriter("/run/media/qiuyuhang/data/ppca/compile/compiler/rubish/hh.txt")) {
            writer.write(" ");
        } catch (IOException e) {
            assert false;
        }
//
        try {
            ProgramNode programNode;
            Scope gScope = new Scope(null);
            gScope.addBuiltInFunction();
            MxLexer lexer = new MxLexer(CharStreams.fromStream(input));
            lexer.removeErrorListeners();
            lexer.addErrorListener(new MxErrorListener());
            MxParser parser = new MxParser(new CommonTokenStream(lexer));
            parser.removeErrorListeners();
            parser.addErrorListener(new MxErrorListener());
            ParseTree parseTreeRoot = parser.program();
//            ASTBuilder astBuilder = new ASTBuilder(gScope);
            ASTBuilder astBuilder = new ASTBuilder();
            programNode = (ProgramNode) astBuilder.visit(parseTreeRoot);
            new SymbolCollector(gScope).visit(programNode);
            //没有main函数
            if (gScope.getFunFromName("main", programNode.pos).returnType.atomType != Util.Type.TypeEnum.INT) {
                throw new semanticError("main not int", programNode.pos);
            }
            new SemanticChecker(gScope).visit(programNode);
            IRBuilder ib = new IRBuilder(programNode, gScope);
            ib.irProgramNode.initCall();
            ib.irProgramNode.clear();
            Mem2Reg mem2Reg;
            if (!asm || new_asm) {
//            String s = ib.irProgramNode.toString();
                mem2Reg = new Mem2Reg(ib.irProgramNode, ib.renamer, K);
                mem2Reg.run();
                String s1 = ib.irProgramNode.toString();
//            System.out.println(s);
                if (!new_asm) System.out.println(s1);
                if (show_color) {
                    System.out.println();
                    System.out.println();
                    System.out.println("K:" + K);
                    for (var fun : ib.irProgramNode.initFuns) {
                        System.out.println("fun:\n" + fun.name);
                        System.out.println("colour:\n" + fun.tempMap);
                        System.out.println("spill:\n" + fun.spill);
                    }
                    for (var fun : ib.irProgramNode.funDefs) {
                        System.out.println("fun:\n" + fun.name);
                        System.out.println("colour:\n" + fun.tempMap);
                        System.out.println("spill:\n" + fun.spill);
                    }
                }
            }
            if (asm) {
                if (!new_asm) {
                    asmBuilder ab = new asmBuilder(ib.irProgramNode);
                    ab.build();
                    ab.print();
                } else {
                    Reg_al_asm ab_new = new Reg_al_asm(ib.irProgramNode, K);
                    ab_new.build();
                    ab_new.print();
                }
                if (oj) {
                    //输出builtin.s的文件内容
                    try (FileReader reader = new FileReader("builtin.s")) {
                        System.out.print("\n\n\n\n\n\n\n");
                        char[] cbuf = new char[1024];
                        int hasRead = 0;
                        while ((hasRead = reader.read(cbuf)) > 0) {
                            System.out.print(new String(cbuf, 0, hasRead));
                        }
                    } catch (IOException e) {
                        assert false;
                    }
                }
            }
//            try (FileReader reader = new FileReader("builtin.s")) {
//                System.out.print("\n\n\n\n\n\n\n");
//                char[] cbuf = new char[1024];
//                int hasRead = 0;
//                while ((hasRead = reader.read(cbuf)) > 0) {
//                    System.out.print(new String(cbuf, 0, hasRead));
//                }
//            } catch (IOException e) {
//                assert false;
//            }
//
//            ib.print();

//            mainFn f = new mainFn();
//            new IRBuilder(f, gScope).visit(ASTRoot);
//             new IRPrinter(System.out).visitFn(f);
//
//            AsmFn asmF = new AsmFn();
//            new InstSelector(asmF).visitFn(f);
//            new RegAlloc(asmF).work();
//            new AsmPrinter(asmF, System.out).print();
        } catch (error er) {
            if (er instanceof semanticError) {
                if (((semanticError) er).type != null) {
                    ((semanticError) er).printType();
                }
            } else if (er instanceof Util.error.syntaxError) {
                System.out.println("Invalid Identifier");
            }
            System.err.println(er.toString());
            System.exit(1);
//            throw new RuntimeException();
        }
    }
}

//  WARNING