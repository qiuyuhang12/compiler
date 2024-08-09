import AST.ProgramNode;
//import Assembly.AsmFn;
//import Backend.*;
//import Frontend.SemanticChecker;
//import Frontend.SymbolCollector;
//import MIR.block;
//import MIR.mainFn;
import Frontend.ASTBuilder;
import Parser.MxLexer;
import Parser.MxParser;
//import Util.MxErrorListener;
import Util.error.error;
//import Util.globalScope;
import Util.error.error;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

import java.io.FileInputStream;
import java.io.InputStream;


public class Main {
//todo::内建函数

    public static void main(String[] args) throws Exception {

        String name = "test.mx";
        InputStream input = new FileInputStream(name);

        try {
            ProgramNode ASTRoot;
//            globalScope gScope = new globalScope(null);

            MxLexer lexer = new MxLexer(CharStreams.fromStream(input));
//            lexer.removeErrorListeners();
//            lexer.addErrorListener(new MxErrorListener());
            MxParser parser = new MxParser(new CommonTokenStream(lexer));
//            parser.removeErrorListeners();
//            parser.addErrorListener(new MxErrorListener());
            ParseTree parseTreeRoot = parser.program();
//            ASTBuilder astBuilder = new ASTBuilder(gScope);
            ASTBuilder astBuilder = new ASTBuilder();
            ASTRoot = (ProgramNode) astBuilder.visit(parseTreeRoot);
//            new SymbolCollector(gScope).visit(ASTRoot);
//            new SemanticChecker(gScope).visit(ASTRoot);

//            mainFn f = new mainFn();
//            new IRBuilder(f, gScope).visit(ASTRoot);
//             new IRPrinter(System.out).visitFn(f);
//
//            AsmFn asmF = new AsmFn();
//            new InstSelector(asmF).visitFn(f);
//            new RegAlloc(asmF).work();
//            new AsmPrinter(asmF, System.out).print();
        } catch (error er) {
            System.err.println(er.toString());
            throw new RuntimeException();
        }
    }
}