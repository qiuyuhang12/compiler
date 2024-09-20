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

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.PrintStream;


public class Main {
//todo::内建函数
    
    public static void main(String[] args) throws Exception {
//        if (false) {
        if (true) {
            // 创建一个 PrintStream 对象，将输出重定向到文件
            PrintStream fileOut = new PrintStream(new FileOutputStream("output.ll"));
//            PrintStream fileOut = new PrintStream(new FileOutputStream("test.s"));
//            PrintStream output =new PrintStream(new FileOutputStream("src/output.ll"));
            
            // 将标准输出流重定向到文件
            System.setOut(fileOut);
        }
        PrintStream fileErr = new PrintStream(new FileOutputStream("errput.txt"));
        System.setErr(fileErr);

        String name = "test.mx";
        InputStream input = new FileInputStream(name);
//        InputStream input = System.in;
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
            String s = ib.irProgramNode.toString();
            Mem2Reg mem2Reg = new Mem2Reg(ib.irProgramNode);
            System.out.println(s);
//            ib.irProgramNode.initCall();
//            asmBuilder ab = new asmBuilder(ib.irProgramNode);
//            ab.build();
//            ab.print();
//            System.out.println("\t.text\n" +
//                    "\t.attribute\t4, 16\n" +
//                    "\t.attribute\t5, \"rv32i2p1_m2p0_a2p1_c2p0\"\n" +
//                    "\t.file\t\"builtin.c\"\n" +
//                    "\t.option\tpush\n" +
//                    "\t.option\tarch, +a, +c, +m\n" +
//                    "\t.globl\tprint                           # -- Begin function print\n" +
//                    "\t.p2align\t1\n" +
//                    "\t.type\tprint,@function\n" +
//                    "print:                                  # @print\n" +
//                    "# %bb.0:\n" +
//                    "\tlui\ta1, %hi(.L.str)\n" +
//                    "\taddi\ta1, a1, %lo(.L.str)\n" +
//                    "\tmv\ta2, a0\n" +
//                    "\tmv\ta0, a1\n" +
//                    "\tmv\ta1, a2\n" +
//                    "\ttail\tprintf\n" +
//                    ".Lfunc_end0:\n" +
//                    "\t.size\tprint, .Lfunc_end0-print\n" +
//                    "                                        # -- End function\n" +
//                    "\t.option\tpop\n" +
//                    "\t.option\tpush\n" +
//                    "\t.option\tarch, +a, +c, +m\n" +
//                    "\t.globl\tprintln                         # -- Begin function println\n" +
//                    "\t.p2align\t1\n" +
//                    "\t.type\tprintln,@function\n" +
//                    "println:                                # @println\n" +
//                    "# %bb.0:\n" +
//                    "\tlui\ta1, %hi(.L.str.1)\n" +
//                    "\taddi\ta1, a1, %lo(.L.str.1)\n" +
//                    "\tmv\ta2, a0\n" +
//                    "\tmv\ta0, a1\n" +
//                    "\tmv\ta1, a2\n" +
//                    "\ttail\tprintf\n" +
//                    ".Lfunc_end1:\n" +
//                    "\t.size\tprintln, .Lfunc_end1-println\n" +
//                    "                                        # -- End function\n" +
//                    "\t.option\tpop\n" +
//                    "\t.option\tpush\n" +
//                    "\t.option\tarch, +a, +c, +m\n" +
//                    "\t.globl\tprintInt                        # -- Begin function printInt\n" +
//                    "\t.p2align\t1\n" +
//                    "\t.type\tprintInt,@function\n" +
//                    "printInt:                               # @printInt\n" +
//                    "# %bb.0:\n" +
//                    "\tlui\ta1, %hi(.L.str.2)\n" +
//                    "\taddi\ta1, a1, %lo(.L.str.2)\n" +
//                    "\tmv\ta2, a0\n" +
//                    "\tmv\ta0, a1\n" +
//                    "\tmv\ta1, a2\n" +
//                    "\ttail\tprintf\n" +
//                    ".Lfunc_end2:\n" +
//                    "\t.size\tprintInt, .Lfunc_end2-printInt\n" +
//                    "                                        # -- End function\n" +
//                    "\t.option\tpop\n" +
//                    "\t.option\tpush\n" +
//                    "\t.option\tarch, +a, +c, +m\n" +
//                    "\t.globl\tprintlnInt                      # -- Begin function printlnInt\n" +
//                    "\t.p2align\t1\n" +
//                    "\t.type\tprintlnInt,@function\n" +
//                    "printlnInt:                             # @printlnInt\n" +
//                    "# %bb.0:\n" +
//                    "\tlui\ta1, %hi(.L.str.3)\n" +
//                    "\taddi\ta1, a1, %lo(.L.str.3)\n" +
//                    "\tmv\ta2, a0\n" +
//                    "\tmv\ta0, a1\n" +
//                    "\tmv\ta1, a2\n" +
//                    "\ttail\tprintf\n" +
//                    ".Lfunc_end3:\n" +
//                    "\t.size\tprintlnInt, .Lfunc_end3-printlnInt\n" +
//                    "                                        # -- End function\n" +
//                    "\t.option\tpop\n" +
//                    "\t.option\tpush\n" +
//                    "\t.option\tarch, +a, +c, +m\n" +
//                    "\t.globl\tgetString                       # -- Begin function getString\n" +
//                    "\t.p2align\t1\n" +
//                    "\t.type\tgetString,@function\n" +
//                    "getString:                              # @getString\n" +
//                    "# %bb.0:\n" +
//                    "\taddi\tsp, sp, -16\n" +
//                    "\tsw\tra, 12(sp)                      # 4-byte Folded Spill\n" +
//                    "\tsw\ts0, 8(sp)                       # 4-byte Folded Spill\n" +
//                    "\tli\ta0, 1024\n" +
//                    "\tcall\tmalloc\n" +
//                    "\tmv\ts0, a0\n" +
//                    "\tlui\ta0, %hi(.L.str)\n" +
//                    "\taddi\ta0, a0, %lo(.L.str)\n" +
//                    "\tmv\ta1, s0\n" +
//                    "\tcall\tscanf\n" +
//                    "\tmv\ta0, s0\n" +
//                    "\tlw\tra, 12(sp)                      # 4-byte Folded Reload\n" +
//                    "\tlw\ts0, 8(sp)                       # 4-byte Folded Reload\n" +
//                    "\taddi\tsp, sp, 16\n" +
//                    "\tret\n" +
//                    ".Lfunc_end4:\n" +
//                    "\t.size\tgetString, .Lfunc_end4-getString\n" +
//                    "                                        # -- End function\n" +
//                    "\t.option\tpop\n" +
//                    "\t.option\tpush\n" +
//                    "\t.option\tarch, +a, +c, +m\n" +
//                    "\t.globl\tgetInt                          # -- Begin function getInt\n" +
//                    "\t.p2align\t1\n" +
//                    "\t.type\tgetInt,@function\n" +
//                    "getInt:                                 # @getInt\n" +
//                    "# %bb.0:\n" +
//                    "\taddi\tsp, sp, -16\n" +
//                    "\tsw\tra, 12(sp)                      # 4-byte Folded Spill\n" +
//                    "\tlui\ta0, %hi(.L.str.2)\n" +
//                    "\taddi\ta0, a0, %lo(.L.str.2)\n" +
//                    "\taddi\ta1, sp, 8\n" +
//                    "\tcall\tscanf\n" +
//                    "\tlw\ta0, 8(sp)\n" +
//                    "\tlw\tra, 12(sp)                      # 4-byte Folded Reload\n" +
//                    "\taddi\tsp, sp, 16\n" +
//                    "\tret\n" +
//                    ".Lfunc_end5:\n" +
//                    "\t.size\tgetInt, .Lfunc_end5-getInt\n" +
//                    "                                        # -- End function\n" +
//                    "\t.option\tpop\n" +
//                    "\t.option\tpush\n" +
//                    "\t.option\tarch, +a, +c, +m\n" +
//                    "\t.globl\ttoString                        # -- Begin function toString\n" +
//                    "\t.p2align\t1\n" +
//                    "\t.type\ttoString,@function\n" +
//                    "toString:                               # @toString\n" +
//                    "# %bb.0:\n" +
//                    "\taddi\tsp, sp, -16\n" +
//                    "\tsw\tra, 12(sp)                      # 4-byte Folded Spill\n" +
//                    "\tsw\ts0, 8(sp)                       # 4-byte Folded Spill\n" +
//                    "\tsw\ts1, 4(sp)                       # 4-byte Folded Spill\n" +
//                    "\tmv\ts0, a0\n" +
//                    "\tli\ta0, 12\n" +
//                    "\tcall\tmalloc\n" +
//                    "\tmv\ts1, a0\n" +
//                    "\tlui\ta0, %hi(.L.str.2)\n" +
//                    "\taddi\ta1, a0, %lo(.L.str.2)\n" +
//                    "\tmv\ta0, s1\n" +
//                    "\tmv\ta2, s0\n" +
//                    "\tcall\tsprintf\n" +
//                    "\tmv\ta0, s1\n" +
//                    "\tlw\tra, 12(sp)                      # 4-byte Folded Reload\n" +
//                    "\tlw\ts0, 8(sp)                       # 4-byte Folded Reload\n" +
//                    "\tlw\ts1, 4(sp)                       # 4-byte Folded Reload\n" +
//                    "\taddi\tsp, sp, 16\n" +
//                    "\tret\n" +
//                    ".Lfunc_end6:\n" +
//                    "\t.size\ttoString, .Lfunc_end6-toString\n" +
//                    "                                        # -- End function\n" +
//                    "\t.option\tpop\n" +
//                    "\t.option\tpush\n" +
//                    "\t.option\tarch, +a, +c, +m\n" +
//                    "\t.globl\tbool_toString                   # -- Begin function bool_toString\n" +
//                    "\t.p2align\t1\n" +
//                    "\t.type\tbool_toString,@function\n" +
//                    "bool_toString:                          # @bool_toString\n" +
//                    "# %bb.0:\n" +
//                    "\tbnez\ta0, .LBB7_2\n" +
//                    "# %bb.1:\n" +
//                    "\tlui\ta0, %hi(.L.str.5)\n" +
//                    "\taddi\ta0, a0, %lo(.L.str.5)\n" +
//                    "\tret\n" +
//                    ".LBB7_2:\n" +
//                    "\tlui\ta0, %hi(.L.str.4)\n" +
//                    "\taddi\ta0, a0, %lo(.L.str.4)\n" +
//                    "\tret\n" +
//                    ".Lfunc_end7:\n" +
//                    "\t.size\tbool_toString, .Lfunc_end7-bool_toString\n" +
//                    "                                        # -- End function\n" +
//                    "\t.option\tpop\n" +
//                    "\t.option\tpush\n" +
//                    "\t.option\tarch, +a, +c, +m\n" +
//                    "\t.globl\talloca_helper                   # -- Begin function alloca_helper\n" +
//                    "\t.p2align\t1\n" +
//                    "\t.type\talloca_helper,@function\n" +
//                    "alloca_helper:                          # @alloca_helper\n" +
//                    "# %bb.0:\n" +
//                    "\taddi\tsp, sp, -16\n" +
//                    "\tsw\tra, 12(sp)                      # 4-byte Folded Spill\n" +
//                    "\tsw\ts0, 8(sp)                       # 4-byte Folded Spill\n" +
//                    "\tmv\ts0, a1\n" +
//                    "\tmul\ta0, a1, a0\n" +
//                    "\taddi\ta0, a0, 4\n" +
//                    "\tcall\tmalloc\n" +
//                    "\taddi\ta1, a0, 4\n" +
//                    "\tsw\ts0, 0(a0)\n" +
//                    "\tmv\ta0, a1\n" +
//                    "\tlw\tra, 12(sp)                      # 4-byte Folded Reload\n" +
//                    "\tlw\ts0, 8(sp)                       # 4-byte Folded Reload\n" +
//                    "\taddi\tsp, sp, 16\n" +
//                    "\tret\n" +
//                    ".Lfunc_end8:\n" +
//                    "\t.size\talloca_helper, .Lfunc_end8-alloca_helper\n" +
//                    "                                        # -- End function\n" +
//                    "\t.option\tpop\n" +
//                    "\t.option\tpush\n" +
//                    "\t.option\tarch, +a, +c, +m\n" +
//                    "\t.globl\tarray.size                      # -- Begin function array.size\n" +
//                    "\t.p2align\t1\n" +
//                    "\t.type\tarray.size,@function\n" +
//                    "array.size:                             # @array.size\n" +
//                    "# %bb.0:\n" +
//                    "\tlw\ta0, -4(a0)\n" +
//                    "\tret\n" +
//                    ".Lfunc_end9:\n" +
//                    "\t.size\tarray.size, .Lfunc_end9-array.size\n" +
//                    "                                        # -- End function\n" +
//                    "\t.option\tpop\n" +
//                    "\t.option\tpush\n" +
//                    "\t.option\tarch, +a, +c, +m\n" +
//                    "\t.globl\tstring.length                   # -- Begin function string.length\n" +
//                    "\t.p2align\t1\n" +
//                    "\t.type\tstring.length,@function\n" +
//                    "string.length:                          # @string.length\n" +
//                    "# %bb.0:\n" +
//                    "\tli\ta1, 0\n" +
//                    ".LBB10_1:                               # =>This Inner Loop Header: Depth=1\n" +
//                    "\tadd\ta2, a0, a1\n" +
//                    "\tlbu\ta2, 0(a2)\n" +
//                    "\taddi\ta1, a1, 1\n" +
//                    "\tbnez\ta2, .LBB10_1\n" +
//                    "# %bb.2:\n" +
//                    "\taddi\ta0, a1, -1\n" +
//                    "\tret\n" +
//                    ".Lfunc_end10:\n" +
//                    "\t.size\tstring.length, .Lfunc_end10-string.length\n" +
//                    "                                        # -- End function\n" +
//                    "\t.option\tpop\n" +
//                    "\t.option\tpush\n" +
//                    "\t.option\tarch, +a, +c, +m\n" +
//                    "\t.globl\tstring.substring                # -- Begin function string.substring\n" +
//                    "\t.p2align\t1\n" +
//                    "\t.type\tstring.substring,@function\n" +
//                    "string.substring:                       # @string.substring\n" +
//                    "# %bb.0:\n" +
//                    "\taddi\tsp, sp, -16\n" +
//                    "\tsw\tra, 12(sp)                      # 4-byte Folded Spill\n" +
//                    "\tsw\ts0, 8(sp)                       # 4-byte Folded Spill\n" +
//                    "\tsw\ts1, 4(sp)                       # 4-byte Folded Spill\n" +
//                    "\tsw\ts2, 0(sp)                       # 4-byte Folded Spill\n" +
//                    "\tmv\ts0, a1\n" +
//                    "\tmv\ts2, a0\n" +
//                    "\tsub\ts1, a2, a1\n" +
//                    "\taddi\ta0, s1, 1\n" +
//                    "\tcall\tmalloc\n" +
//                    "\tadd\ta1, a0, s1\n" +
//                    "\tblez\ts1, .LBB11_3\n" +
//                    "# %bb.1:                                # %.preheader\n" +
//                    "\tadd\ts0, s0, s2\n" +
//                    "\tmv\ta2, a0\n" +
//                    ".LBB11_2:                               # =>This Inner Loop Header: Depth=1\n" +
//                    "\tlbu\ta3, 0(s0)\n" +
//                    "\tsb\ta3, 0(a2)\n" +
//                    "\taddi\ta2, a2, 1\n" +
//                    "\taddi\ts0, s0, 1\n" +
//                    "\tbne\ta2, a1, .LBB11_2\n" +
//                    ".LBB11_3:\n" +
//                    "\tsb\tzero, 0(a1)\n" +
//                    "\tlw\tra, 12(sp)                      # 4-byte Folded Reload\n" +
//                    "\tlw\ts0, 8(sp)                       # 4-byte Folded Reload\n" +
//                    "\tlw\ts1, 4(sp)                       # 4-byte Folded Reload\n" +
//                    "\tlw\ts2, 0(sp)                       # 4-byte Folded Reload\n" +
//                    "\taddi\tsp, sp, 16\n" +
//                    "\tret\n" +
//                    ".Lfunc_end11:\n" +
//                    "\t.size\tstring.substring, .Lfunc_end11-string.substring\n" +
//                    "                                        # -- End function\n" +
//                    "\t.option\tpop\n" +
//                    "\t.option\tpush\n" +
//                    "\t.option\tarch, +a, +c, +m\n" +
//                    "\t.globl\tstring.parseInt                 # -- Begin function string.parseInt\n" +
//                    "\t.p2align\t1\n" +
//                    "\t.type\tstring.parseInt,@function\n" +
//                    "string.parseInt:                        # @string.parseInt\n" +
//                    "# %bb.0:\n" +
//                    "\tlbu\ta1, 0(a0)\n" +
//                    "\tli\ta2, 45\n" +
//                    "\tbne\ta1, a2, .LBB12_5\n" +
//                    "# %bb.1:\n" +
//                    "\tlbu\ta1, 1(a0)\n" +
//                    "\taddi\ta2, a1, -48\n" +
//                    "\tandi\ta2, a2, 255\n" +
//                    "\tli\ta3, 9\n" +
//                    "\tbltu\ta3, a2, .LBB12_9\n" +
//                    "# %bb.2:                                # %.preheader\n" +
//                    "\tli\ta2, 0\n" +
//                    "\taddi\ta0, a0, 2\n" +
//                    "\tli\ta3, 10\n" +
//                    ".LBB12_3:                               # =>This Inner Loop Header: Depth=1\n" +
//                    "\tandi\ta4, a1, 255\n" +
//                    "\tlbu\ta1, 0(a0)\n" +
//                    "\tmul\ta2, a2, a3\n" +
//                    "\tadd\ta2, a2, a4\n" +
//                    "\taddi\ta2, a2, -48\n" +
//                    "\taddi\ta4, a1, -48\n" +
//                    "\tandi\ta4, a4, 255\n" +
//                    "\taddi\ta0, a0, 1\n" +
//                    "\tbltu\ta4, a3, .LBB12_3\n" +
//                    "# %bb.4:\n" +
//                    "\tneg\ta0, a2\n" +
//                    "\tret\n" +
//                    ".LBB12_5:\n" +
//                    "\taddi\ta2, a1, -48\n" +
//                    "\tandi\ta2, a2, 255\n" +
//                    "\tli\ta3, 9\n" +
//                    "\tbltu\ta3, a2, .LBB12_10\n" +
//                    "# %bb.6:                                # %.preheader1\n" +
//                    "\tli\ta3, 0\n" +
//                    "\taddi\ta2, a0, 1\n" +
//                    "\tli\ta0, 0\n" +
//                    "\tli\ta3, 10\n" +
//                    ".LBB12_7:                               # =>This Inner Loop Header: Depth=1\n" +
//                    "\tandi\ta4, a1, 255\n" +
//                    "\tlbu\ta1, 0(a2)\n" +
//                    "\tmul\ta0, a0, a3\n" +
//                    "\tadd\ta0, a0, a4\n" +
//                    "\taddi\ta0, a0, -48\n" +
//                    "\taddi\ta4, a1, -48\n" +
//                    "\tandi\ta4, a4, 255\n" +
//                    "\taddi\ta2, a2, 1\n" +
//                    "\tbltu\ta4, a3, .LBB12_7\n" +
//                    "# %bb.8:\n" +
//                    "\tret\n" +
//                    ".LBB12_9:\n" +
//                    "\tneg\ta0, zero\n" +
//                    "\tret\n" +
//                    ".LBB12_10:\n" +
//                    "\tli\ta0, 0\n" +
//                    "\tret\n" +
//                    ".Lfunc_end12:\n" +
//                    "\t.size\tstring.parseInt, .Lfunc_end12-string.parseInt\n" +
//                    "                                        # -- End function\n" +
//                    "\t.option\tpop\n" +
//                    "\t.option\tpush\n" +
//                    "\t.option\tarch, +a, +c, +m\n" +
//                    "\t.globl\tstring.compare                  # -- Begin function string.compare\n" +
//                    "\t.p2align\t1\n" +
//                    "\t.type\tstring.compare,@function\n" +
//                    "string.compare:                         # @string.compare\n" +
//                    "# %bb.0:\n" +
//                    "\tlbu\ta2, 0(a0)\n" +
//                    "\tbeqz\ta2, .LBB13_5\n" +
//                    "# %bb.1:                                # %.preheader\n" +
//                    "\tli\ta3, 0\n" +
//                    "\taddi\ta0, a0, 1\n" +
//                    ".LBB13_2:                               # =>This Inner Loop Header: Depth=1\n" +
//                    "\tadd\ta4, a1, a3\n" +
//                    "\tlbu\ta4, 0(a4)\n" +
//                    "\tbeqz\ta4, .LBB13_6\n" +
//                    "# %bb.3:                                #   in Loop: Header=BB13_2 Depth=1\n" +
//                    "\tandi\ta5, a2, 255\n" +
//                    "\tbne\ta5, a4, .LBB13_8\n" +
//                    "# %bb.4:                                #   in Loop: Header=BB13_2 Depth=1\n" +
//                    "\tadd\ta2, a0, a3\n" +
//                    "\tlbu\ta2, 0(a2)\n" +
//                    "\taddi\ta4, a3, 1\n" +
//                    "\tmv\ta3, a4\n" +
//                    "\tbnez\ta2, .LBB13_2\n" +
//                    "\tj\t.LBB13_7\n" +
//                    ".LBB13_5:\n" +
//                    "\tli\ta4, 0\n" +
//                    "\tj\t.LBB13_7\n" +
//                    ".LBB13_6:\n" +
//                    "\tmv\ta4, a3\n" +
//                    ".LBB13_7:\n" +
//                    "\tadd\ta1, a1, a4\n" +
//                    "\tlbu\ta4, 0(a1)\n" +
//                    ".LBB13_8:\n" +
//                    "\tandi\ta0, a2, 255\n" +
//                    "\tsub\ta0, a0, a4\n" +
//                    "\tret\n" +
//                    ".Lfunc_end13:\n" +
//                    "\t.size\tstring.compare, .Lfunc_end13-string.compare\n" +
//                    "                                        # -- End function\n" +
//                    "\t.option\tpop\n" +
//                    "\t.option\tpush\n" +
//                    "\t.option\tarch, +a, +c, +m\n" +
//                    "\t.globl\tstring.concat                   # -- Begin function string.concat\n" +
//                    "\t.p2align\t1\n" +
//                    "\t.type\tstring.concat,@function\n" +
//                    "string.concat:                          # @string.concat\n" +
//                    "# %bb.0:\n" +
//                    "\taddi\tsp, sp, -32\n" +
//                    "\tsw\tra, 28(sp)                      # 4-byte Folded Spill\n" +
//                    "\tsw\ts0, 24(sp)                      # 4-byte Folded Spill\n" +
//                    "\tsw\ts1, 20(sp)                      # 4-byte Folded Spill\n" +
//                    "\tsw\ts2, 16(sp)                      # 4-byte Folded Spill\n" +
//                    "\tsw\ts3, 12(sp)                      # 4-byte Folded Spill\n" +
//                    "\tsw\ts4, 8(sp)                       # 4-byte Folded Spill\n" +
//                    "\tmv\ts3, a1\n" +
//                    "\tmv\ts4, a0\n" +
//                    "\tli\ta0, 0\n" +
//                    ".LBB14_1:                               # =>This Inner Loop Header: Depth=1\n" +
//                    "\tmv\ts1, a0\n" +
//                    "\tadd\ta0, a0, s4\n" +
//                    "\tlbu\ta1, 0(a0)\n" +
//                    "\taddi\ta0, s1, 1\n" +
//                    "\tbnez\ta1, .LBB14_1\n" +
//                    "# %bb.2:                                # %.preheader2\n" +
//                    "\tli\ta0, 0\n" +
//                    ".LBB14_3:                               # =>This Inner Loop Header: Depth=1\n" +
//                    "\tmv\ts0, a0\n" +
//                    "\tadd\ta0, a0, s3\n" +
//                    "\tlbu\ta1, 0(a0)\n" +
//                    "\taddi\ta0, s0, 1\n" +
//                    "\tbnez\ta1, .LBB14_3\n" +
//                    "# %bb.4:\n" +
//                    "\tadd\ts2, s0, s1\n" +
//                    "\taddi\ta0, s2, 1\n" +
//                    "\tcall\tmalloc\n" +
//                    "\tadd\ta1, a0, s1\n" +
//                    "\tbeqz\ts1, .LBB14_7\n" +
//                    "# %bb.5:                                # %.preheader\n" +
//                    "\tmv\ta2, a0\n" +
//                    ".LBB14_6:                               # =>This Inner Loop Header: Depth=1\n" +
//                    "\tlbu\ta3, 0(s4)\n" +
//                    "\tsb\ta3, 0(a2)\n" +
//                    "\taddi\ta2, a2, 1\n" +
//                    "\taddi\ts4, s4, 1\n" +
//                    "\tbne\ta2, a1, .LBB14_6\n" +
//                    ".LBB14_7:\n" +
//                    "\tbeqz\ts0, .LBB14_10\n" +
//                    "# %bb.8:\n" +
//                    "\tadd\ta2, a0, s2\n" +
//                    ".LBB14_9:                               # =>This Inner Loop Header: Depth=1\n" +
//                    "\tlbu\ta3, 0(s3)\n" +
//                    "\tsb\ta3, 0(a1)\n" +
//                    "\taddi\ta1, a1, 1\n" +
//                    "\taddi\ts3, s3, 1\n" +
//                    "\tbne\ta1, a2, .LBB14_9\n" +
//                    ".LBB14_10:\n" +
//                    "\tadd\ts2, s2, a0\n" +
//                    "\tsb\tzero, 0(s2)\n" +
//                    "\tlw\tra, 28(sp)                      # 4-byte Folded Reload\n" +
//                    "\tlw\ts0, 24(sp)                      # 4-byte Folded Reload\n" +
//                    "\tlw\ts1, 20(sp)                      # 4-byte Folded Reload\n" +
//                    "\tlw\ts2, 16(sp)                      # 4-byte Folded Reload\n" +
//                    "\tlw\ts3, 12(sp)                      # 4-byte Folded Reload\n" +
//                    "\tlw\ts4, 8(sp)                       # 4-byte Folded Reload\n" +
//                    "\taddi\tsp, sp, 32\n" +
//                    "\tret\n" +
//                    ".Lfunc_end14:\n" +
//                    "\t.size\tstring.concat, .Lfunc_end14-string.concat\n" +
//                    "                                        # -- End function\n" +
//                    "\t.option\tpop\n" +
//                    "\t.option\tpush\n" +
//                    "\t.option\tarch, +a, +c, +m\n" +
//                    "\t.globl\tstring.copy                     # -- Begin function string.copy\n" +
//                    "\t.p2align\t1\n" +
//                    "\t.type\tstring.copy,@function\n" +
//                    "string.copy:                            # @string.copy\n" +
//                    "# %bb.0:\n" +
//                    "\taddi\tsp, sp, -16\n" +
//                    "\tsw\tra, 12(sp)                      # 4-byte Folded Spill\n" +
//                    "\tsw\ts0, 8(sp)                       # 4-byte Folded Spill\n" +
//                    "\tsw\ts1, 4(sp)                       # 4-byte Folded Spill\n" +
//                    "\tsw\ts2, 0(sp)                       # 4-byte Folded Spill\n" +
//                    "\tmv\ts1, a1\n" +
//                    "\tmv\ts2, a0\n" +
//                    "\tli\ts0, -1\n" +
//                    ".LBB15_1:                               # =>This Inner Loop Header: Depth=1\n" +
//                    "\tadd\ta0, s1, s0\n" +
//                    "\tlbu\ta0, 1(a0)\n" +
//                    "\taddi\ts0, s0, 1\n" +
//                    "\tbnez\ta0, .LBB15_1\n" +
//                    "# %bb.2:\n" +
//                    "\taddi\ta0, s0, 1\n" +
//                    "\tcall\tmalloc\n" +
//                    "\tsw\ta0, 0(s2)\n" +
//                    "\tbeqz\ts0, .LBB15_6\n" +
//                    "# %bb.3:                                # %.preheader\n" +
//                    "\tli\ta0, 0\n" +
//                    ".LBB15_4:                               # =>This Inner Loop Header: Depth=1\n" +
//                    "\tlw\ta1, 0(s2)\n" +
//                    "\tadd\ta2, s1, a0\n" +
//                    "\tlbu\ta2, 0(a2)\n" +
//                    "\tadd\ta1, a1, a0\n" +
//                    "\taddi\ta0, a0, 1\n" +
//                    "\tsb\ta2, 0(a1)\n" +
//                    "\tbne\ts0, a0, .LBB15_4\n" +
//                    "# %bb.5:\n" +
//                    "\tlw\ta0, 0(s2)\n" +
//                    ".LBB15_6:\n" +
//                    "\tadd\ta0, a0, s0\n" +
//                    "\tsb\tzero, 0(a0)\n" +
//                    "\tlw\tra, 12(sp)                      # 4-byte Folded Reload\n" +
//                    "\tlw\ts0, 8(sp)                       # 4-byte Folded Reload\n" +
//                    "\tlw\ts1, 4(sp)                       # 4-byte Folded Reload\n" +
//                    "\tlw\ts2, 0(sp)                       # 4-byte Folded Reload\n" +
//                    "\taddi\tsp, sp, 16\n" +
//                    "\tret\n" +
//                    ".Lfunc_end15:\n" +
//                    "\t.size\tstring.copy, .Lfunc_end15-string.copy\n" +
//                    "                                        # -- End function\n" +
//                    "\t.option\tpop\n" +
//                    "\t.option\tpush\n" +
//                    "\t.option\tarch, +a, +c, +m\n" +
//                    "\t.globl\tstring.ord                      # -- Begin function string.ord\n" +
//                    "\t.p2align\t1\n" +
//                    "\t.type\tstring.ord,@function\n" +
//                    "string.ord:                             # @string.ord\n" +
//                    "# %bb.0:\n" +
//                    "\tadd\ta0, a0, a1\n" +
//                    "\tlbu\ta0, 0(a0)\n" +
//                    "\tret\n" +
//                    ".Lfunc_end16:\n" +
//                    "\t.size\tstring.ord, .Lfunc_end16-string.ord\n" +
//                    "                                        # -- End function\n" +
//                    "\t.option\tpop\n" +
//                    "\t.option\tpush\n" +
//                    "\t.option\tarch, +a, +c, +m\n" +
//                    "\t.globl\tarray.alloca_inside             # -- Begin function array.alloca_inside\n" +
//                    "\t.p2align\t1\n" +
//                    "\t.type\tarray.alloca_inside,@function\n" +
//                    "array.alloca_inside:                    # @array.alloca_inside\n" +
//                    "# %bb.0:\n" +
//                    "\taddi\tsp, sp, -48\n" +
//                    "\tsw\tra, 44(sp)                      # 4-byte Folded Spill\n" +
//                    "\tsw\ts0, 40(sp)                      # 4-byte Folded Spill\n" +
//                    "\tsw\ts1, 36(sp)                      # 4-byte Folded Spill\n" +
//                    "\tsw\ts2, 32(sp)                      # 4-byte Folded Spill\n" +
//                    "\tsw\ts3, 28(sp)                      # 4-byte Folded Spill\n" +
//                    "\tsw\ts4, 24(sp)                      # 4-byte Folded Spill\n" +
//                    "\tsw\ts5, 20(sp)                      # 4-byte Folded Spill\n" +
//                    "\tsw\ts6, 16(sp)                      # 4-byte Folded Spill\n" +
//                    "\tsw\ts7, 12(sp)                      # 4-byte Folded Spill\n" +
//                    "\tli\ts0, 1\n" +
//                    "\tmv\ts4, a2\n" +
//                    "\tmv\ts3, a0\n" +
//                    "\tbne\ta1, s0, .LBB17_2\n" +
//                    "# %bb.1:\n" +
//                    "\tlw\ts0, 0(s4)\n" +
//                    "\tmul\ta0, s0, s3\n" +
//                    "\taddi\ta0, a0, 4\n" +
//                    "\tcall\tmalloc\n" +
//                    "\tsw\ts0, 0(a0)\n" +
//                    "\taddi\ta0, a0, 4\n" +
//                    "\tj\t.LBB17_7\n" +
//                    ".LBB17_2:\n" +
//                    "\tmv\ts2, a3\n" +
//                    "\tmv\ts5, a1\n" +
//                    "\tlw\ts1, 0(s4)\n" +
//                    "\tslli\ta0, s1, 2\n" +
//                    "\taddi\ta0, a0, 4\n" +
//                    "\tcall\tmalloc\n" +
//                    "\tsw\ts1, 0(a0)\n" +
//                    "\taddi\ta0, a0, 4\n" +
//                    "\tbeq\ts2, s0, .LBB17_7\n" +
//                    "# %bb.3:\n" +
//                    "\tblez\ts1, .LBB17_7\n" +
//                    "# %bb.4:\n" +
//                    "\tli\ts1, 0\n" +
//                    "\taddi\ts5, s5, -1\n" +
//                    "\taddi\ts7, s4, 4\n" +
//                    "\taddi\ts2, s2, -1\n" +
//                    "\tmv\ts6, a0\n" +
//                    "\tmv\ts0, a0\n" +
//                    ".LBB17_5:                               # =>This Inner Loop Header: Depth=1\n" +
//                    "\tmv\ta0, s3\n" +
//                    "\tmv\ta1, s5\n" +
//                    "\tmv\ta2, s7\n" +
//                    "\tmv\ta3, s2\n" +
//                    "\tcall\tarray.alloca_inside\n" +
//                    "\tlw\ta1, 0(s4)\n" +
//                    "\tsw\ta0, 0(s0)\n" +
//                    "\taddi\ts1, s1, 1\n" +
//                    "\taddi\ts0, s0, 4\n" +
//                    "\tblt\ts1, a1, .LBB17_5\n" +
//                    "# %bb.6:\n" +
//                    "\tmv\ta0, s6\n" +
//                    ".LBB17_7:\n" +
//                    "\tlw\tra, 44(sp)                      # 4-byte Folded Reload\n" +
//                    "\tlw\ts0, 40(sp)                      # 4-byte Folded Reload\n" +
//                    "\tlw\ts1, 36(sp)                      # 4-byte Folded Reload\n" +
//                    "\tlw\ts2, 32(sp)                      # 4-byte Folded Reload\n" +
//                    "\tlw\ts3, 28(sp)                      # 4-byte Folded Reload\n" +
//                    "\tlw\ts4, 24(sp)                      # 4-byte Folded Reload\n" +
//                    "\tlw\ts5, 20(sp)                      # 4-byte Folded Reload\n" +
//                    "\tlw\ts6, 16(sp)                      # 4-byte Folded Reload\n" +
//                    "\tlw\ts7, 12(sp)                      # 4-byte Folded Reload\n" +
//                    "\taddi\tsp, sp, 48\n" +
//                    "\tret\n" +
//                    ".Lfunc_end17:\n" +
//                    "\t.size\tarray.alloca_inside, .Lfunc_end17-array.alloca_inside\n" +
//                    "                                        # -- End function\n" +
//                    "\t.option\tpop\n" +
//                    "\t.option\tpush\n" +
//                    "\t.option\tarch, +a, +c, +m\n" +
//                    "\t.globl\tarray.alloca                    # -- Begin function array.alloca\n" +
//                    "\t.p2align\t1\n" +
//                    "\t.type\tarray.alloca,@function\n" +
//                    "array.alloca:                           # @array.alloca\n" +
//                    "# %bb.0:\n" +
//                    "\taddi\tsp, sp, -48\n" +
//                    "\tsw\tra, 20(sp)                      # 4-byte Folded Spill\n" +
//                    "\tsw\ts0, 16(sp)                      # 4-byte Folded Spill\n" +
//                    "\tsw\ts1, 12(sp)                      # 4-byte Folded Spill\n" +
//                    "\tsw\ts2, 8(sp)                       # 4-byte Folded Spill\n" +
//                    "\tsw\ts3, 4(sp)                       # 4-byte Folded Spill\n" +
//                    "\tmv\ts1, a2\n" +
//                    "\tmv\ts2, a1\n" +
//                    "\tmv\ts3, a0\n" +
//                    "\tsw\ta7, 44(sp)\n" +
//                    "\tsw\ta6, 40(sp)\n" +
//                    "\tsw\ta5, 36(sp)\n" +
//                    "\tsw\ta4, 32(sp)\n" +
//                    "\tsw\ta3, 28(sp)\n" +
//                    "\tslli\ts0, a2, 2\n" +
//                    "\tmv\ta0, s0\n" +
//                    "\tcall\tmalloc\n" +
//                    "\tmv\ta2, a0\n" +
//                    "\taddi\ta0, sp, 28\n" +
//                    "\tsw\ta0, 0(sp)\n" +
//                    "\tblez\ts1, .LBB18_3\n" +
//                    "# %bb.1:\n" +
//                    "\tlw\ta0, 0(sp)\n" +
//                    "\taddi\ta0, a0, 4\n" +
//                    "\tadd\ts0, s0, a2\n" +
//                    "\tmv\ta1, a2\n" +
//                    ".LBB18_2:                               # =>This Inner Loop Header: Depth=1\n" +
//                    "\tsw\ta0, 0(sp)\n" +
//                    "\tlw\ta3, -4(a0)\n" +
//                    "\tsw\ta3, 0(a1)\n" +
//                    "\taddi\ta1, a1, 4\n" +
//                    "\taddi\ta0, a0, 4\n" +
//                    "\tbne\ta1, s0, .LBB18_2\n" +
//                    ".LBB18_3:\n" +
//                    "\tmv\ta0, s3\n" +
//                    "\tmv\ta1, s2\n" +
//                    "\tmv\ta3, s1\n" +
//                    "\tcall\tarray.alloca_inside\n" +
//                    "\tlw\tra, 20(sp)                      # 4-byte Folded Reload\n" +
//                    "\tlw\ts0, 16(sp)                      # 4-byte Folded Reload\n" +
//                    "\tlw\ts1, 12(sp)                      # 4-byte Folded Reload\n" +
//                    "\tlw\ts2, 8(sp)                       # 4-byte Folded Reload\n" +
//                    "\tlw\ts3, 4(sp)                       # 4-byte Folded Reload\n" +
//                    "\taddi\tsp, sp, 48\n" +
//                    "\tret\n" +
//                    ".Lfunc_end18:\n" +
//                    "\t.size\tarray.alloca, .Lfunc_end18-array.alloca\n" +
//                    "                                        # -- End function\n" +
//                    "\t.option\tpop\n" +
//                    "\t.type\t.L.str,@object                  # @.str\n" +
//                    "\t.section\t.rodata.str1.1,\"aMS\",@progbits,1\n" +
//                    ".L.str:\n" +
//                    "\t.asciz\t\"%s\"\n" +
//                    "\t.size\t.L.str, 3\n" +
//                    "\n" +
//                    "\t.type\t.L.str.1,@object                # @.str.1\n" +
//                    ".L.str.1:\n" +
//                    "\t.asciz\t\"%s\\n\"\n" +
//                    "\t.size\t.L.str.1, 4\n" +
//                    "\n" +
//                    "\t.type\t.L.str.2,@object                # @.str.2\n" +
//                    ".L.str.2:\n" +
//                    "\t.asciz\t\"%d\"\n" +
//                    "\t.size\t.L.str.2, 3\n" +
//                    "\n" +
//                    "\t.type\t.L.str.3,@object                # @.str.3\n" +
//                    ".L.str.3:\n" +
//                    "\t.asciz\t\"%d\\n\"\n" +
//                    "\t.size\t.L.str.3, 4\n" +
//                    "\n" +
//                    "\t.type\t.L.str.4,@object                # @.str.4\n" +
//                    ".L.str.4:\n" +
//                    "\t.asciz\t\"true\"\n" +
//                    "\t.size\t.L.str.4, 5\n" +
//                    "\n" +
//                    "\t.type\t.L.str.5,@object                # @.str.5\n" +
//                    ".L.str.5:\n" +
//                    "\t.asciz\t\"false\"\n" +
//                    "\t.size\t.L.str.5, 6\n" +
//                    "\n" +
//                    "\t.ident\t\"clang version 18.1.8\"\n" +
//                    "\t.section\t\".note.GNU-stack\",\"\",@progbits\n");
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