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
import Optm.Mem2Reg.Mem2Reg;
import Parser.MxLexer;
import Parser.MxParser;
//import Util.MxErrorListener;
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
        if (false) {
//        if (true) {
            // 创建一个 PrintStream 对象，将输出重定向到文件
            PrintStream fileOut = new PrintStream(new FileOutputStream("output.ll"));
//            PrintStream fileOut = new PrintStream(new FileOutputStream("test.s"));
//            PrintStream output =new PrintStream(new FileOutputStream("src/output.ll"));
            
            // 将标准输出流重定向到文件
            System.setOut(fileOut);
        }
        PrintStream fileErr = new PrintStream(new FileOutputStream("errput.txt"));
        System.setErr(fileErr);

//        String name = "test.mx";
//        InputStream input = new FileInputStream(name);
        InputStream input = System.in;
        
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
////            String s = ib.irProgramNode.toString();
//            Mem2Reg mem2Reg = new Mem2Reg(ib.irProgramNode, ib.renamer);
//            mem2Reg.run();
//            String s1 = ib.irProgramNode.toString();
////            System.out.println(s);
//            System.out.println(s1);


            asmBuilder ab = new asmBuilder(ib.irProgramNode);
            ab.build();
            ab.print();
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