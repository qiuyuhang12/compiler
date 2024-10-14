package Frontend.IR;

import AST.ASTNode;
import AST.ASTVisitor;
import AST.ProgramNode;
import AST.def.*;
import AST.expr.*;
import AST.expr.atom.*;
import AST.stmt.*;
import AST.typeNode;
import Frontend.IR.entity.*;
import Frontend.IR.node.def.IRFunDef;
import Frontend.IR.node.def.IRGlobalVarDef;
import Frontend.IR.node.def.IRStringDef;
import Frontend.IR.node.def.IRStructDef;
import Frontend.IR.node.inst.*;
import Frontend.IR.node.stmt.IRBlockNode;
import Frontend.IR.type.IRStruct;
import Frontend.IR.type.IRType;
import Frontend.IR.util.Renamer;
import Util.Scope;
import Util.Type;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class IRBuilder implements ASTVisitor {
    //classname->(varname->int)
    public HashMap<String, HashMap<String, Integer>> classVarIndex = new HashMap<>();
    public Renamer renamer = new Renamer();
    public IRProgramNode irProgramNode = new IRProgramNode();
    public IRBlockNode currentBlock;
    public String currentTmpValName;//可能是字面量
    public String currentLeftVarAddr;
    public ArrayList<instNode> MemberGets;
    public IRFunDef currentFunDef;//谁使用谁复原
    public IRVar currentNoneStmtVarForFun;
    public status currentStatus = status.global;
    public String currentArray;//处理数组常量
    public HashSet<String> hasBuildClass = new HashSet<>();
    private Scope gScope;
    
    public IRBuilder(ProgramNode programNode, Scope gScope) {
        programNode.accept(this);
        this.gScope = gScope;
    }
    
    public void print() {
        System.out.println(irProgramNode.toString());
    }
    
    public enum status {
        global, inClass, inFun
    }
    
    
    @Override
    public void visit(ProgramNode it) {
//        renamer.in();
        for (ASTNode def : it.Defs) {
            def.accept(this);
        }
//        renamer.out();
    }
    
    @Override
    public void visit(classBuildNode it) {
        assert currentFunDef == null;
        hasBuildClass.add(it.name.substring(0, it.name.length() - 6));
        status retStatus = currentStatus;
        currentStatus = status.inClass;
        renamer.in();
        
        currentFunDef = new IRFunDef(new IRType(), "@" + renamer.rename(it.name));
        irProgramNode.pushFunDef(currentFunDef);
        assert currentBlock == null;
        currentBlock = new IRBlockNode(null, currentFunDef, "entry");
        assert MemberGets != null;
        for (instNode inst : MemberGets) {
            currentBlock.push(inst);
        }
        currentFunDef.pushPara(new IRVar("ptr", "%this", false));
        currentFunDef.push(currentBlock);
        //处理函数体
        for (StmtNode stmt : it.stmts) {
            stmt.accept(this);
        }
        //处理返回
        currentBlock.push(new brInstNode(it, currentBlock, "return"));
        currentBlock = new IRBlockNode(currentBlock, currentFunDef, "return");
        retInstNode rin = new retInstNode(null, currentBlock);
        currentBlock.push(rin);
        currentFunDef.push(currentBlock);
        currentBlock = null;
        
        currentStatus = retStatus;
        currentFunDef = null;
        renamer.out();
    }
    
    @Override
    public void visit(classDefNode it) {
        assert currentStatus == status.global;
        assert currentFunDef == null;
        classVarIndex.put(it.name, new HashMap<>());
        currentStatus = status.inClass;
        renamer.in(it.name);
        IRStruct irStruct = new IRStruct();
        IRStructDef irStructDef = new IRStructDef("%class." + it.name, irStruct);
        irProgramNode.pushStructDef(irStructDef);
        //初始this
        renamer.rename("this");
        allocaInstNode alloc = new allocaInstNode(it, null, new IRVar("ptr", "%" + renamer.rename("this"), false), new IRType(IRType.IRTypeEnum.ptr));
        storeInstNode store = new storeInstNode(it, null, new IRVar("ptr", "%this", false), new IRVar("ptr", renamer.getRenamed("this"), false));
        MemberGets = new ArrayList<>();
        MemberGets.add(alloc);
        MemberGets.add(store);
        for (int i = 0; i < it.varDefs.size(); ++i) {
            varDefNode varDef = it.varDefs.get(i);
            for (int j = 0; j < varDef.units.size(); ++j) {
                varDefUnitNode unit = varDef.units.get(j);
                classVarIndex.get(it.name).put(unit.name, i + j);
//                unit.irIndex = i + j;
                irStruct.struct.add(getIRtype(varDef.typeNd.type));
                IRType irType = new IRType(IRType.IRTypeEnum.struct);
                irType.structInfo = irStructDef.name;
//                getElementPtrInstNode get = new getElementPtrInstNode(varDef, currentBlock, "%" + it.name + "." + unit.name, renamer.getRenamed("this"), irType);
                getElementPtrInstNode get = new getElementPtrInstNode(varDef, currentBlock, "%" + it.name + "." + unit.name, "%this", irType);
                get.push(new IRType(IRType.IRTypeEnum.i32), 0);
                get.push(new IRType(IRType.IRTypeEnum.i32), i + j);
                MemberGets.add(get);
                renamer.rename(unit.name);
            }
        }
        for (funDefNode funDef : it.funDefs) {
//            renamer.rename(funDef.name);
            funDef.name = it.name + "." + funDef.name;
            funDef.accept(this);
        }
        if (!it.build.isEmpty()) {
            classBuildNode build = it.build.getFirst();
            build.name = it.name + ".build";
            build.accept(this);
        }
        MemberGets = null;
        currentStatus = status.global;
        renamer.out(it.name);
    }
    
    private IRType getIRtype(Type tp) {
        if (tp.isArray) {
            return new IRType(IRType.IRTypeEnum.ptr);
        }
        switch (tp.atomType) {
            case INT:
                return new IRType(IRType.IRTypeEnum.i32);
            case BOOL:
                return new IRType(IRType.IRTypeEnum.i1);
            case CLASS:
            case STRING:
                return new IRType(IRType.IRTypeEnum.ptr);
            case NULL:
                return new IRType(IRType.IRTypeEnum.ptr);
            case VOID:
                return new IRType(IRType.IRTypeEnum.void_);
            default:
                assert false;
                return null;
        }
    }
    
    private IRType getIRTypeForGetElementPtr(Type tp) {
        if (tp.isArray) {
            return new IRType(IRType.IRTypeEnum.ptr);
        }
        switch (tp.atomType) {
            case INT:
            case BOOL:
            case STRING:
            case VOID:
//                return getIRtype(tp);
            case CLASS:
                IRType it = new IRType(IRType.IRTypeEnum.struct);
                it.structInfo = "%class." + tp.name;
                return it;
            default:
                assert false;
                return null;
        }
    }
    
    private IRLiteral init(String s) {
        if (s.equals("i1")) {
            return new IRBoolLiteral(false);
        } else if (s.equals("i32")) {
            return new IRIntLiteral(0);
        } else if (s.equals("ptr")) {
            return new IRNullPtrLiteral();
        }
        assert false;
        return null;
    }
    
    @Override
    public void visit(funDefNode it) {
        assert currentFunDef == null;
        status retStatus = currentStatus;
        currentStatus = status.inFun;
        renamer.in();
        
        currentFunDef = new IRFunDef(getIRtype(it.typeNd.type), "@" + renamer.rename(it.name));
        irProgramNode.pushFunDef(currentFunDef);
        assert currentBlock == null;
        currentBlock = new IRBlockNode(null, currentFunDef, "entry");
        if (MemberGets != null) for (instNode inst : MemberGets) {
            var tmp = inst.copy(inst);
            currentBlock.push(tmp);
        }
        //处理参数
        if (retStatus.equals(status.inClass)) {
            currentFunDef.pushPara(new IRVar("ptr", "%this", false));
//            currentNoneStmtVarForFun = new IRVar("ptr", "%this", false);
//            currentBlock.push(new allocaInstNode(it, currentBlock, currentNoneStmtVarForFun, new IRType(IRType.IRTypeEnum.ptr)));
//            currentBlock.push(new storeInstNode(it, currentBlock, new IRVar("ptr", "%this.val", false), new IRVar("ptr", renamer.getRenamed("this"), false)));
        }
        if (it.paraList != null) for (varDefNode varDef : it.paraList.paraList) {
            assert currentNoneStmtVarForFun == null;
            varDef.isStmt = false;
            varDef.accept(this);
            currentFunDef.pushPara(currentNoneStmtVarForFun);
            currentNoneStmtVarForFun = null;
            currentBlock.push(new allocaInstNode(varDef, currentBlock, new IRVar(getIRtype(varDef.typeNd.type).toString(), "%" + renamer.rename(varDef.units.getFirst().name), false), getIRtype(varDef.typeNd.type)));
            currentBlock.push(new storeInstNode(varDef, currentBlock, new IRVar(getIRtype(varDef.typeNd.type).toString(), "%" + varDef.units.getFirst().name + ".val", false), new IRVar(getIRtype(varDef.typeNd.type).toString(), renamer.getRenamed(varDef.units.getFirst().name), false)));
        }
        currentFunDef.push(currentBlock);
        //留返回值
        if (it.typeNd.type.atomType != Type.TypeEnum.VOID) {
            varDefNode retVarDef = new varDefNode(it.pos);
            retVarDef.typeNd = it.typeNd;
            retVarDef.units.add(new varDefUnitNode(it.pos));
            retVarDef.units.getFirst().name = "ret.val";
            retVarDef.accept(this);
            IREntity irEntity = init(getIRtype(it.typeNd.type).toString());
            currentBlock.push(new storeInstNode(null, currentBlock, irEntity, new IRVar(getIRtype(it.typeNd.type).toString(), renamer.getRenamed("ret.val"), false)));
        }
        //处理函数体
        if (it.name.equals("main")) {
            callInstNode call = new callInstNode(it, currentBlock, "_.init");
            currentBlock.push(call);
        }
        for (StmtNode stmt : it.stmts) {
            stmt.accept(this);
        }
        //处理返回值
        if (it.typeNd.type.atomType != Type.TypeEnum.VOID) {
            currentBlock.push(new brInstNode(it, currentBlock, "return"));
            currentBlock = new IRBlockNode(currentBlock, currentFunDef, "return");
            loadInstNode lin = new loadInstNode(null, currentBlock, "%" + renamer.rename("r.e.t"), renamer.getRenamed("ret.val"), getIRtype(it.typeNd.type));
            retInstNode rin = new retInstNode(null, currentBlock, new IRVar(getIRtype(it.typeNd.type).toString(), renamer.getRenamed("r.e.t"), false));
            currentBlock.push(lin);
            currentBlock.push(rin);
        } else {
            currentBlock.push(new brInstNode(it, currentBlock, "return"));
            currentBlock = new IRBlockNode(currentBlock, currentFunDef, "return");
            retInstNode rin = new retInstNode(null, currentBlock);
            currentBlock.push(rin);
        }
        currentFunDef.push(currentBlock);
        currentBlock = null;
        
        
        currentStatus = retStatus;
        currentFunDef = null;
        renamer.out();
    }
    
    
    private void addInitFun(varDefNode it, varDefUnitNode unit, String type) {
        IRFunDef funDef = new IRFunDef(new IRType(), "@" + renamer.rename(unit.name + ".init"));
        irProgramNode.pushInitFun(funDef);
        
        
        currentStatus = status.inFun;
        renamer.in();
        
        currentFunDef = funDef;
        assert currentBlock == null;
        currentBlock = new IRBlockNode(null, currentFunDef, "entry");
        currentFunDef.push(currentBlock);
        //处理函数体
        currentLeftVarAddr = null;
        currentTmpValName = null;
        if (unit.init instanceof arrayNode) {//int []a={9};
            currentLeftVarAddr = renamer.getAnonymousName();
            currentArray = currentLeftVarAddr;
            allocaInstNode alloc2 = new allocaInstNode(it, currentBlock, new IRVar("ptr", currentLeftVarAddr, false), new IRType(IRType.IRTypeEnum.ptr));
            currentBlock.push(alloc2);
            unit.init.accept(this);
        } else {//int []a=f(0);//int []a=new int [4] ;or int []a=new int []{1,2,3,4};//int a=b+c;
            unit.init.accept(this);
            storeInstNode store = new storeInstNode(it, currentBlock, new IRVar(getIRtype(it.typeNd.type).toString(), currentTmpValName, false), new IRVar("ptr", renamer.getRenamed(unit.name), true));
            currentBlock.push(store);
        }
        //处理返回值
        currentBlock.push(new brInstNode(it, currentBlock, "return"));
        currentBlock = new IRBlockNode(currentBlock, currentFunDef, "return");
        retInstNode rin = new retInstNode(null, currentBlock);
        currentBlock.push(rin);
        currentFunDef.push(currentBlock);
        currentBlock = null;
        currentStatus = status.global;
        currentFunDef = null;
        renamer.out();
    }
    
    @Override
    public void visit(varDefNode it) {
        if (currentBlock == null) {
            assert currentStatus == status.global;
            it.isStmt = false;
        }
        if (it.isStmt) {
//            assert currentBlock != null;
            if (it.typeNd.type.isArray) {
                for (varDefUnitNode unit : it.units) {
                    currentLeftVarAddr = null;
                    currentArray = null;
                    allocaInstNode alloc = new allocaInstNode(it, currentBlock, new IRVar("ptr", "%" + renamer.rename(unit.name), false), new IRType(IRType.IRTypeEnum.ptr));
                    currentBlock.push(alloc);
                    if (unit.init == null) {//int []a;
                        continue;
                    } else {
                        storeInstNode store;
                        if (unit.init instanceof arrayNode) {//int []a={9};
                            currentArray = renamer.getAnonymousName();
                            String tmp = currentArray;
                            allocaInstNode alloc2 = new allocaInstNode(it, currentBlock, new IRVar("ptr", currentArray, false), new IRType(IRType.IRTypeEnum.ptr));
                            currentBlock.push(alloc2);
                            unit.init.accept(this);
                            store = new storeInstNode(it, currentBlock, new IRVar("ptr", tmp, false), new IRVar("ptr", renamer.getRenamed(unit.name), false));
                        } else {//int []a=f(0);//int []a=new int [4] ;or int []a=new int []{1,2,3,4};
                            unit.init.accept(this);
                            store = new storeInstNode(it, currentBlock, new IRVar("ptr", currentTmpValName, false), new IRVar("ptr", renamer.getRenamed(unit.name), false));
                        }
                        currentBlock.push(store);
                    }
//                    currentLeftVarAddr = store.ptr.name;
//                    currentTmpValName = store.ptr.name;
                }
            } else if (it.typeNd.type.atomType.equals(Type.TypeEnum.STRING)) {
                for (varDefUnitNode unit : it.units) {
                    allocaInstNode alloc = new allocaInstNode(it, currentBlock, new IRVar("ptr", "%" + renamer.rename(unit.name), false), new IRType(IRType.IRTypeEnum.ptr));
                    currentBlock.push(alloc);
                    if (unit.init == null) {//string a;
                        continue;
                    } else {
                        unit.init.accept(this);
                        callInstNode call = new callInstNode(it, currentBlock, "string.copy", alloc.dest, new IRVar("ptr", currentTmpValName, false));
                        currentBlock.push(call);
                    }
                }
            } else {
                IRType irType = getIRtype(it.typeNd.type);
                for (varDefUnitNode unit : it.units) {
                    currentBlock.push(new allocaInstNode(it, currentBlock, new IRVar(irType.toString(), "%" + renamer.rename(unit.name), false), irType));
                    if (unit.init != null) {
                        unit.init.accept(this);
                        storeInstNode store = new storeInstNode(it, currentBlock, new IRVar(getIRtype(it.typeNd.type).toString(), currentTmpValName, false), new IRVar(getIRtype(it.typeNd.type).toString(), renamer.getRenamed(unit.name), false));
                        currentBlock.push(store);
                    }
                }
            }
        } else {
            if (currentStatus == status.global) {
                if (it.typeNd.type.isArray) {
                    for (varDefUnitNode unit : it.units) {
                        IRGlobalVarDef irGlobalVarDef = new IRGlobalVarDef("@" + renamer.rename(unit.name), new IRNullPtrLiteral());
                        irProgramNode.pushVarDef(irGlobalVarDef);
//                        allocaInstNode alloc = new allocaInstNode(it, null, new IRVar("ptr", "@" + renamer.rename(unit.name), true), new IRType(IRType.IRTypeEnum.ptr));
//                        irProgramNode.pushVarDef(alloc);
                        if (unit.init == null) {//int []a;
                            continue;
                        } else {
                            addInitFun(it, unit, "array");
                        }
                    }
                } else {
                    switch (it.typeNd.type.atomType) {
                        case INT:
                            for (varDefUnitNode unit : it.units) {
                                if (unit.init == null) {//int a
                                    irProgramNode.pushVarDef(new IRGlobalVarDef("@" + renamer.rename(unit.name), new IRIntLiteral(0)));
                                } else if (unit.init instanceof intNode) {//int a=9;
                                    irProgramNode.pushVarDef(new IRGlobalVarDef("@" + renamer.rename(unit.name), new IRIntLiteral(((intNode) unit.init).value)));
                                } else {//int a=f(0);
                                    irProgramNode.pushVarDef(new IRGlobalVarDef("@" + renamer.rename(unit.name), new IRIntLiteral(0)));
                                    addInitFun(it, unit, "int");
                                }
                            }
                            break;
                        case BOOL:
                            for (varDefUnitNode unit : it.units) {
                                if (unit.init == null) {//int a
                                    irProgramNode.pushVarDef(new IRGlobalVarDef("@" + renamer.rename(unit.name), new IRBoolLiteral(false)));
                                } else if (unit.init instanceof intNode) {//int a=9;
                                    irProgramNode.pushVarDef(new IRGlobalVarDef("@" + renamer.rename(unit.name), new IRBoolLiteral(((boolNode) unit.init).value)));
                                } else {//int a=f(0);
                                    irProgramNode.pushVarDef(new IRGlobalVarDef("@" + renamer.rename(unit.name), new IRBoolLiteral(false)));
                                    addInitFun(it, unit, "bool");
                                }
                            }
                            break;
                        case STRING:
                            for (varDefUnitNode unit : it.units) {
                                IRGlobalVarDef irGlobalVarDef = new IRGlobalVarDef("@" + renamer.rename(unit.name), new IRNullPtrLiteral());
                                irProgramNode.pushVarDef(irGlobalVarDef);
//                                allocaInstNode alloc = new allocaInstNode(it, null, new IRVar("ptr", "@" + renamer.rename(unit.name), true), new IRType(IRType.IRTypeEnum.ptr));
//                                irProgramNode.pushVarDef(alloc);
                                if (unit.init == null) {//string a;
                                    continue;
                                } else {
                                    addInitFun(it, unit, "string");
                                }
                            }
                            break;
                        case CLASS:
                            for (varDefUnitNode unit : it.units) {
                                IRGlobalVarDef irGlobalVarDef = new IRGlobalVarDef("@" + renamer.rename(unit.name), new IRNullPtrLiteral());
                                irProgramNode.pushVarDef(irGlobalVarDef);
//                                allocaInstNode alloc = new allocaInstNode(it, null, new IRVar("ptr", "@" + renamer.rename(unit.name), true), new IRType(IRType.IRTypeEnum.ptr));
//                                irProgramNode.pushVarDef(alloc);
                                if (unit.init == null) {//string a;
                                    continue;
                                } else {
                                    addInitFun(it, unit, "class");
                                }
                            }
                            break;
                        default:
                            assert false;
                    }
                }
            } else if (currentStatus == status.inClass) {
                assert false;
            } else if (currentStatus == status.inFun) {//(函数声明或定义)的para
                currentNoneStmtVarForFun = new IRVar(getIRtype(it.typeNd.type).toString(), "%" + it.units.getFirst().name + ".val", false);
//                if (it.typeNd.type.isArray) {
//                    assert false;
//                } else {
//                    assert it.units.size() == 1;
//                    varDefUnitNode unit = it.units.getFirst();
//                    assert unit.init == null;
//                    switch (it.typeNd.type.atomType) {
//                        case INT:
//                            currentNoneStmtVarForFun = new IRVar("i32", "%" + unit.name + ".val", false);
//                            break;
//                        case BOOL:
//                            currentNoneStmtVarForFun = new IRVar("i1", "%" + unit.name + ".val", false);
//                            break;
//                        case STRING:
//                        case CLASS:
//                        default:
//                            assert false;
//                    }
//                }
            }
        }
    }
    
    @Override
    public void visit(funParaList it) {
        assert false;
    }
    
    @Override
    public void visit(varDefUnitNode it) {
        assert false;
    }
    
    
    private String getArrElementType(Type type) {
        if (type.dim == 1) {
            Type tmp = type.clone();
            tmp.isArray = false;
            return getIRtype(tmp).toString();
        } else {
            return "ptr";
        }
    }
    
    @Override
    public void visit(arrayNode it) {
        String arrayAddr = currentArray;
        Type arrType = it.typeNd.type;
//        callInstNode call = new callInstNode(it, currentBlock, new IRVar("ptr", "%" + renamer.rename("call"), false), new IRType(IRType.IRTypeEnum.ptr), "array.alloca", new IRIntLiteral(getSize(arrType)), new IRIntLiteral(1), new IRIntLiteral(1), new IRIntLiteral(it.value.size()));
//        storeInstNode store = new storeInstNode(it, currentBlock, call.dest, new IRVar("ptr", arrayAddr, false));
//        currentBlock.push(call);
//        currentBlock.push(store);
        assert arrayAddr != null;
        if (arrType.dim == 1) {
            for (int i = 0; i < it.value.size(); ++i) {
                it.value.get(i).accept(this);
                String dest = "%" + renamer.rename("array-init");
                getElementPtrInstNode get = new getElementPtrInstNode(it, currentBlock, dest, arrayAddr, getIRtype(arrType));
//                get.push(new IRType("i32"), 0);
                get.push(getIRtype(it.value.get(i).typeNd.type), i);
                currentBlock.push(get);
                currentBlock.push(new storeInstNode(it, currentBlock, new IRVar(getArrElementType(arrType).toString(), currentTmpValName, false), new IRVar(getIRtype(arrType).toString(), dest, false)));
            }
        } else {
            for (int i = 0; i < it.value.size(); ++i) {
                getElementPtrInstNode get = new getElementPtrInstNode(it, currentBlock, "%" + renamer.rename("array-init"), arrayAddr, getIRtype(arrType));
//                get.push(new IRType("i32"), 0);
                get.push(getIRtype(it.value.get(i).typeNd.type), i);
                currentBlock.push(get);
                currentArray = get.dest;
                it.value.get(i).accept(this);
            }
        }
        currentTmpValName = null;
        currentLeftVarAddr = null;
    }
    
    @Override
    public void visit(boolNode it) {
        currentTmpValName = it.value ? "1" : "0";
    }
    
    @Override
    public void visit(identifierNode it) {
        currentTmpValName = renamer.getAnonymousName();
        currentLeftVarAddr = renamer.getRenamed(it.name);
        currentBlock.push(new loadInstNode(it, currentBlock, currentTmpValName, renamer.getRenamed(it.name), getIRtype(it.typeNd.type)));
    }
    
    @Override
    public void visit(intNode it) {
        currentTmpValName = String.valueOf(it.value);
    }
    
    @Override
    public void visit(nullNode it) {
        currentTmpValName = "null";
        currentLeftVarAddr = null;
    }
    
    @Override
    public void visit(stringNode it) {
        IRStringDef irStringDef = new IRStringDef("@" + renamer.rename(".str"), it.value);
        irProgramNode.pushStringDef(irStringDef);
        currentTmpValName = irStringDef.name;
        currentLeftVarAddr = irStringDef.name;
    }
    
    @Override
    public void visit(thisNode it) {
        currentLeftVarAddr = renamer.getRenamed("this");
        currentTmpValName = "%this";
    }
    
    @Override
    public void visit(arrayExprNode it) {
        it.array.accept(this);
        String arrayName = currentTmpValName;
        it.index.accept(this);
        String indexName = currentTmpValName;
        IRVar array = new IRVar("ptr", arrayName, false);
        IRVar index = new IRVar("i32", indexName, false);
        IRVar dest = new IRVar("ptr", "%" + renamer.rename("array.idx"), false);
        getElementPtrInstNode get = new getElementPtrInstNode(it, currentBlock, dest, array, getIRtype(it.array.typeNd.type));
        get.push(new IRType(IRType.IRTypeEnum.i32), indexName);
        currentBlock.push(get);
        currentLeftVarAddr = dest.name;
        IRVar dest2 = new IRVar(getArrElementType(it.array.typeNd.type).toString(), "%" + renamer.rename("array.idx.val"), false);
        currentBlock.push(new loadInstNode(it, currentBlock, dest2.name, dest.name, new IRType(getArrElementType(it.array.typeNd.type).toString())));
        currentTmpValName = dest2.name;
    }
    
    @Override
    public void visit(assignExprNode it) {
        it.lhs.accept(this);
        String lhsName = currentLeftVarAddr;
        it.rhs.accept(this);
        String rhsName = currentTmpValName;
        if (it.lhs.typeNd.type.atomType.equals(Type.TypeEnum.STRING)) {
            currentBlock.push(new callInstNode(it, currentBlock, "string.copy", new IRVar("ptr", lhsName, false), new IRVar("ptr", rhsName, false)));
            currentTmpValName = null;
            currentLeftVarAddr = null;
        } else {
            currentBlock.push(new storeInstNode(it, currentBlock, new IRVar(getIRtype(it.rhs.typeNd.type).toString(), rhsName, false), new IRVar(getIRtype(it.lhs.typeNd.type).toString(), lhsName, false)));
//        currentBlock.push(new loadInstNode(it, currentBlock, lhsName, rhsName, getIRtype(it.lhs.typeNd.type)));
            currentTmpValName = lhsName;
        }
    }
    
    @Override
    public void visit(binaryExprNode it) {
        if (it.opCode == binaryExprNode.binaryOpType.andLg || it.opCode == binaryExprNode.binaryOpType.orLg) {
            it.lhs.accept(this);
            String lhsName = currentTmpValName;
//            String tmplabel = currentBlock.label;
            String nextlabel = renamer.rename("shortcircuit.next");
            String endlabel = renamer.rename("shortcircuit.end");
            IRVar dest = new IRVar("ptr", "%" + renamer.rename("shortcircuit.ptr"), false);
            currentBlock.push(new allocaInstNode(it, currentBlock, dest, new IRType(IRType.IRTypeEnum.i1)));
            //lhs:
            IRVar lhs = new IRVar("i1", lhsName, false);
            storeInstNode store = new storeInstNode(it, currentBlock, lhs, dest);
            currentBlock.push(store);
//            allocaInstNode rsl = new allocaInstNode(it, currentBlock, new IRVar("i1", renamer.getAnonymousName(), false), new IRType(IRType.IRTypeEnum.i1));
//            currentBlock.push(rsl);
//            currentBlock.push(new storeInstNode(it, currentBlock, new IRVar("i1", lhsName, false), new IRVar("i1", rsl.dest.name, false)));
            if (it.opCode == binaryExprNode.binaryOpType.andLg) {
                currentBlock.push(new brInstNode(it, currentBlock, lhs, nextlabel, endlabel));
            } else {
                currentBlock.push(new brInstNode(it, currentBlock, lhs, endlabel, nextlabel));
            }
            //next(rhs):
            currentBlock = new IRBlockNode(currentBlock, currentFunDef, nextlabel);
            currentFunDef.push(currentBlock);
            it.rhs.accept(this);
            String rhsName = currentTmpValName;
            IRVar rhs = new IRVar("i1", rhsName, false);
            storeInstNode store2 = new storeInstNode(it, currentBlock, rhs, dest);
            currentBlock.push(store2);
//            currentBlock.push(new storeInstNode(it, currentBlock, new IRVar("i1", rhsName, false), new IRVar("i1", rsl.dest.name, false)));
            currentBlock.push(new brInstNode(it, currentBlock, endlabel));
            //end:
            currentBlock = new IRBlockNode(currentBlock, currentFunDef, endlabel);
            currentFunDef.push(currentBlock);
            loadInstNode load = new loadInstNode(it, currentBlock, renamer.getAnonymousName(), dest.name, new IRType(IRType.IRTypeEnum.i1));
            currentBlock.push(load);
//            IRVar dest = new IRVar("i1", "%" + renamer.rename("shortcircuit"), false);
//            currentBlock.push(new loadInstNode(it, currentBlock, dest.name, rsl.dest.name, new IRType(IRType.IRTypeEnum.i1)));
//            phiInstNode phi = new phiInstNode(it, currentBlock, dest);
//            phi.push(lhs, tmplabel);
//            phi.push(rhs, nextlabel);
//            currentBlock.push(phi);
            currentTmpValName = load.dest;
//            currentLeftVarAddr = dest.name;
            currentLeftVarAddr = null;
            return;
        }
        it.lhs.accept(this);
        String lhsName = currentTmpValName;
        it.rhs.accept(this);
        String rhsName = currentTmpValName;
        IRType irType = getIRtype(it.lhs.typeNd.type);
        IRVar lhs = new IRVar(irType.toString(), lhsName, false);
        IRVar rhs = new IRVar(irType.toString(), rhsName, false);
        IRType irType2 = getIRtype(it.typeNd.type);
        IRVar dest = new IRVar(irType2.toString(), "%" + renamer.rename(it.opCode.toString()), false);
        if (!it.lhs.typeNd.type.atomType.equals(Type.TypeEnum.STRING))
            switch (it.opCode) {
                case add:
                case sub:
                case mul:
                case div:
                case mod:
                case leftSh:
                case rightSh:
                case and:
                case or:
                case xor:
                    currentBlock.push(new binaryInstNode(it, currentBlock, dest, it.opCode, lhs, rhs));
                    break;
                case greater:
                case less:
                case grEq:
                case lessEq:
                case unEq:
                case eq:
                    currentBlock.push(new icmpInstNode(it, currentBlock, dest, it.opCode, lhs, rhs));
                    break;
                case andLg:
                case orLg:
                case not:
                default:
                    assert false;
            }
        else {
            if (it.opCode == binaryExprNode.binaryOpType.add) {
                //hhhh
//                System.err.println("string add:lhs,rhs可能有空指针，val or left");
                currentBlock.push(new callInstNode(it, currentBlock, dest, new IRType(IRType.IRTypeEnum.ptr), "string.concat", lhs, rhs));
            } else {
                String name = renamer.getAnonymousName();
                IRVar tmp = new IRVar("i32", name, false);
                currentBlock.push(new callInstNode(it, currentBlock, tmp, new IRType(IRType.IRTypeEnum.i32), "string.compare", lhs, rhs));
                switch (it.opCode) {
                    case greater:
                    case less:
                    case grEq:
                    case lessEq:
                    case unEq:
                    case eq:
                        currentBlock.push(new icmpInstNode(it, currentBlock, dest, it.opCode, tmp, new IRIntLiteral(0)));
                        break;
                    default:
                        assert false;
                }
            }
        }
        currentTmpValName = dest.name;
    }
    
    @Override
    public void visit(conditionalExprNode it) {
        boolean isVoid = it.typeNd.type.atomType.equals(Type.TypeEnum.VOID);
        it.condition.accept(this);
        String condName = currentTmpValName;
        IRVar cond = new IRVar("i1", condName, false);
        String trueLabel = renamer.rename("cond.true");
        String falseLabel = renamer.rename("cond.false");
        String endLabel = renamer.rename("cond.end");
        IRVar dest = new IRVar(getIRtype(it.typeNd.type).toString(), renamer.getAnonymousName(), false);
        if (!isVoid) {
            allocaInstNode alloc = new allocaInstNode(it, currentBlock, dest, getIRtype(it.typeNd.type));
            currentBlock.push(alloc);
        }
        currentBlock.push(new brInstNode(it, currentBlock, cond, trueLabel, falseLabel));
        currentBlock = new IRBlockNode(currentBlock, currentFunDef, trueLabel);
        currentFunDef.push(currentBlock);
        it.trueExpr.accept(this);
        if (!isVoid) {
            storeInstNode store = new storeInstNode(it, currentBlock, new IRVar(getIRtype(it.trueExpr.typeNd.type).toString(), currentTmpValName, false), dest);
            currentBlock.push(store);
        }
//        trueLabel = currentBlock.label;
//        String trueAddr = currentTmpValName;
        currentBlock.push(new brInstNode(it, currentBlock, endLabel));
        currentBlock = currentBlock.jumpSrc;
        currentBlock = new IRBlockNode(currentBlock, currentFunDef, falseLabel);
        currentFunDef.push(currentBlock);
        it.falseExpr.accept(this);
        if (!isVoid) {
            storeInstNode store = new storeInstNode(it, currentBlock, new IRVar(getIRtype(it.falseExpr.typeNd.type).toString(), currentTmpValName, false), dest);
            currentBlock.push(store);
        }
//        falseLabel = currentBlock.label;
//        String falseAddr = currentTmpValName;
        currentBlock.push(new brInstNode(it, currentBlock, endLabel));
        currentBlock = currentBlock.jumpSrc;
        currentBlock = new IRBlockNode(currentBlock, currentFunDef, endLabel);
        currentFunDef.push(currentBlock);
        currentTmpValName = "%" + renamer.rename("cond");
        if (!isVoid) {
            loadInstNode load = new loadInstNode(it, currentBlock, currentTmpValName, dest.name, getIRtype(it.typeNd.type));
            currentBlock.push(load);
        }
//            IRVar dest = new IRVar(getIRtype(it.typeNd.type).toString(), currentTmpValName, false);
//            phiInstNode phi = new phiInstNode(it, currentBlock, dest);
//            phi.push(new IRVar(getIRtype(it.trueExpr.typeNd.type).toString(), trueAddr, false), trueLabel);
//            phi.push(new IRVar(getIRtype(it.falseExpr.typeNd.type).toString(), falseAddr, false), falseLabel);
//            currentBlock.push(phi);
//        }
    }

//        boolean isVoid = it.typeNd.type.atomType.equals(Type.TypeEnum.VOID);
//        it.condition.accept(this);
//        String condName = currentTmpValName;
//        IRVar cond = new IRVar("i1", condName, false);
//        it.trueExpr.accept(this);
//        String trueAddr = currentTmpValName;
//        it.falseExpr.accept(this);
//        String falseAddr = currentTmpValName;
//        if (!isVoid) {
//            currentTmpValName = "%" + renamer.rename("cond");
//            currentLeftVarAddr = null;
//            IRVar dest = new IRVar(getIRtype(it.typeNd.type).toString(), currentTmpValName, false);
//            selectInstNode select = new selectInstNode(it, currentBlock, dest, cond, new IRVar(getIRtype(it.trueExpr.typeNd.type).toString(), trueAddr, false), new IRVar(getIRtype(it.falseExpr.typeNd.type).toString(), falseAddr, false));
//            currentBlock.push(select);
//        }
    
    @Override
    public void visit(formatStringExprNode it) {
        //hh
//        System.err.println("formatStringExprNode:$x+y$$x+y$中间的string是null吗？");
        String rslAddr = null;
        for (int i = 0; i < it.strings.size(); ++i) {
            if (it.strings.get(i) != null && !it.strings.get(i).isEmpty()) {
                IRStringDef irStringDef = new IRStringDef("@" + renamer.rename(".fstr"), it.strings.get(i));
                irProgramNode.pushStringDef(irStringDef);
                currentTmpValName = null;
                currentLeftVarAddr = irStringDef.name;
                if (rslAddr == null) {
                    rslAddr = currentLeftVarAddr;
                } else {
                    callInstNode call = new callInstNode(it, currentBlock, new IRVar("ptr", "%" + renamer.rename("call"), false), new IRType(IRType.IRTypeEnum.ptr), "string.concat", new IRVar("ptr", rslAddr, false), new IRVar("ptr", currentLeftVarAddr, false));
                    rslAddr = call.dest.name;
                    currentBlock.push(call);
                }
            }
            if (i < it.exprs.size()) {
                if (it.exprs.get(i) == null) {
                    continue;
                }
                it.exprs.get(i).accept(this);
                String tmp = null;
                callInstNode call;
                switch (it.exprs.get(i).typeNd.type.atomType) {
                    case INT:
                        tmp = "%" + renamer.rename("int");
                        call = new callInstNode(it, currentBlock, new IRVar("ptr", tmp, false), new IRType(IRType.IRTypeEnum.ptr), "toString", new IRVar("i32", currentTmpValName, false));
                        currentBlock.push(call);
                        break;
                    case BOOL:
                        tmp = "%" + renamer.rename("bool");
                        call = new callInstNode(it, currentBlock, new IRVar("ptr", tmp, false), new IRType(IRType.IRTypeEnum.ptr), "bool_toString", new IRVar("i1", currentTmpValName, false));
                        currentBlock.push(call);
                        break;
                    case STRING:
                        tmp = currentTmpValName;
                        break;
                    default:
                        assert false;
                }
                if (rslAddr == null) {
                    rslAddr = tmp;
                } else {
                    callInstNode call2 = new callInstNode(it, currentBlock, new IRVar("ptr", "%" + renamer.rename("call"), false), new IRType(IRType.IRTypeEnum.ptr), "string.concat", new IRVar("ptr", rslAddr, false), new IRVar("ptr", tmp, false));
                    rslAddr = call2.dest.name;
                    currentBlock.push(call2);
                }
            }
        }
        currentLeftVarAddr = rslAddr;
        currentTmpValName = rslAddr;
    }
    
    
    private String getInBuildFunName(funExprNode it) {
        memberExprNode memberExpr = (memberExprNode) it.fun;
        if (memberExpr.object.typeNd.type.isArray) return "array." + memberExpr.member.name;
        else if (memberExpr.object.typeNd.type.atomType.equals(Type.TypeEnum.STRING))
            return "string." + memberExpr.member.name;
        else {
            assert false;
            return null;
        }
    }
    
    private IRType getInBuildFunType(funExprNode it) {
        assert it.fun instanceof memberExprNode;
        assert it.fun.typeNd.type.fun.isInBuildMethod;
        memberExprNode memberExpr = (memberExprNode) it.fun;
        if (it.typeNd.type.atomType.equals(Type.TypeEnum.STRING))
            return new IRType(IRType.IRTypeEnum.ptr);
        else if (it.typeNd.type.atomType.equals(Type.TypeEnum.INT)) {
            return getIRtype(it.typeNd.type);//防array
        } else {
            assert false;
            return null;
        }
    }
    
    //a.f().h();
    @Override
    public void visit(funExprNode it) {
        if (it.fun.typeNd.type.fun.inGlobal) {
            ArrayList<IREntity> paras = new ArrayList<>();
            for (ExprNode para : it.paras) {
                para.accept(this);
                paras.add(new IRVar(getIRtype(para.typeNd.type).toString(), currentTmpValName, false));
            }
            if (it.typeNd.type.atomType == Type.TypeEnum.VOID) {
                currentLeftVarAddr = null;
                currentTmpValName = null;
                currentBlock.push(new callInstNode(it, currentBlock, it.fun.typeNd.type.fun.funName, paras));
            } else {
                currentTmpValName = "%" + renamer.rename(it.fun.typeNd.type.fun.funName + ".ret");
                currentLeftVarAddr = currentTmpValName;
                IRVar dest = new IRVar(getIRtype(it.typeNd.type).toString(), currentTmpValName, false);
                currentBlock.push(new callInstNode(it, currentBlock, dest, getIRtype(it.typeNd.type), it.fun.typeNd.type.fun.funName, paras));
            }
        } else if (it.fun.typeNd.type.fun.isInBuildMethod) {
            it.fun.accept(this);
            String funName = getInBuildFunName(it);
            IRType funType = getInBuildFunType(it);
            IRVar dest = new IRVar(funType.toString(), "%" + renamer.rename(funName), false);
            callInstNode call = new callInstNode(it, currentBlock, dest, funType, funName, new IRVar("ptr", currentTmpValName, false));
            for (ExprNode para : it.paras) {
                para.accept(this);
                call.args.add(new IRVar(getIRtype(para.typeNd.type).toString(), currentTmpValName, false));
            }
            currentBlock.push(call);
            currentTmpValName = call.dest.name;
            currentLeftVarAddr = call.dest.name;
        } else {
            Type funTypeInfo = it.fun.typeNd.type;
            String thisPtr;
            if (it.fun instanceof identifierNode) {
                thisPtr = "%this";
            } else {
                assert it.fun instanceof memberExprNode;
                it.fun.accept(this);
                thisPtr = currentTmpValName;
            }
            callInstNode call;
            String currentTmpValNameRET;
            String currentLeftVarAddrRET;
            if (funTypeInfo.atomType == Type.TypeEnum.VOID) {
                call = new callInstNode(it, currentBlock, funTypeInfo.fun.className + "." + funTypeInfo.fun.funName, new IRVar("ptr", thisPtr, false));
                currentLeftVarAddr = null;
                currentTmpValName = null;
                currentLeftVarAddrRET = null;
                currentTmpValNameRET = null;
            } else {
                currentTmpValName = "%call." + renamer.rename(it.typeNd.type.fun.funName + ".ret");
                currentLeftVarAddr = currentTmpValName;
                currentLeftVarAddrRET = currentTmpValName;
                currentTmpValNameRET = currentTmpValName;
                IRVar dest = new IRVar(getIRtype(it.typeNd.type).toString(), currentTmpValName, false);
                call = new callInstNode(it, currentBlock, dest, getIRtype(it.typeNd.type), funTypeInfo.fun.className + "." + funTypeInfo.fun.funName, new IRVar("ptr", thisPtr, false));
            }
            for (ExprNode para : it.paras) {
                para.accept(this);
                call.args.add(new IRVar(getIRtype(para.typeNd.type).toString(), currentTmpValName, false));
            }
            currentBlock.push(call);
            currentTmpValName = currentTmpValNameRET;
            currentLeftVarAddr = currentLeftVarAddrRET;
        }
    }
    
    @Override
    public void visit(memberExprNode it) {
        it.object.accept(this);
//        String ob = currentLeftVarAddr;
        String ob = currentTmpValName;
        if (!it.typeNd.type.isFun) {
            getElementPtrInstNode get = new getElementPtrInstNode(it, currentBlock, renamer.getAnonymousName(), ob, getIRTypeForGetElementPtr(it.object.typeNd.type));
            get.push(new IRType(IRType.IRTypeEnum.i32), 0);
            if (!classVarIndex.containsKey(it.object.typeNd.type.name)) {
                System.exit(0);
            }
            get.push(new IRType(IRType.IRTypeEnum.i32), classVarIndex.get(it.object.typeNd.type.name).get(it.member.name));
            currentBlock.push(get);
            currentLeftVarAddr = get.dest;
            loadInstNode lin = new loadInstNode(it, currentBlock, renamer.getAnonymousName(), currentLeftVarAddr, getIRtype(it.typeNd.type));
            currentBlock.push(lin);
            currentTmpValName = lin.dest;
        } else {
            //交给funcExpr
        }
    }
    
    private Integer getSize(Type type) {
        if (type.isArray) {
            return 4;
        }
        return switch (type.atomType) {
            case INT, STRING, CLASS, BOOL -> 4;
            default -> {
                assert false;
                yield 0;
            }
        };
    }
    
    @Override
    public void visit(newArrayExprNode it) {
        allocaInstNode alloc = new allocaInstNode(it, currentBlock, new IRVar("ptr", renamer.getAnonymousName(), false), new IRType(IRType.IRTypeEnum.ptr));
        if (it.init == null) {
            currentBlock.push(alloc);
            callInstNode call;
            call = new callInstNode(it, currentBlock, new IRVar("ptr", "%" + renamer.rename("call"), false), new IRType(IRType.IRTypeEnum.ptr), "array.alloca", new IRIntLiteral(getSize(it.typeNd.type)), new IRIntLiteral(it.dim), new IRIntLiteral(it.sizeInit.size()));
            for (ExprNode expr : it.sizeInit) {
                expr.accept(this);
                call.args.add(new IRVar("i32", currentTmpValName, false));
            }
            currentBlock.push(call);
            currentBlock.push(new storeInstNode(it, currentBlock, new IRVar("ptr", call.dest.name, false), new IRVar("ptr", alloc.dest.name, false)));
            currentTmpValName = call.dest.name;
            currentLeftVarAddr = alloc.dest.name;
        }
        if (it.init != null) {
            currentBlock.push(alloc);
            Type arrType = it.init.typeNd.type;
            callInstNode call = new callInstNode(it, currentBlock, new IRVar("ptr", "%" + renamer.rename("call"), false), new IRType(IRType.IRTypeEnum.ptr), "array.alloca", new IRIntLiteral(getSize(arrType)), new IRIntLiteral(1), new IRIntLiteral(1), new IRIntLiteral(it.init.value.size()));
            currentArray = call.dest.name;
            currentBlock.push(call);
            currentBlock.push(new storeInstNode(it, currentBlock, new IRVar("ptr", call.dest.name, false), new IRVar("ptr", alloc.dest.name, false)));
            it.init.accept(this);
            currentTmpValName = call.dest.name;
            currentLeftVarAddr = alloc.dest.name;
        }
    }
    
    @Override
    public void visit(newVarExprNode it) {
        assert !it.typeNd.type.isArray;
        assert it.typeNd.type.atomType.equals(Type.TypeEnum.CLASS);
        allocaInstNode alloca = new allocaInstNode(it, currentBlock, new IRVar("ptr", "%" + renamer.rename("_." + it.typeNd.type.name), false), new IRType(IRType.IRTypeEnum.ptr));
//        currentBlock.push(alloca);
        if (!classVarIndex.containsKey(it.typeNd.type.name)) {
            System.exit(0);
        }
        callInstNode call0 = new callInstNode(it, currentBlock, new IRVar("ptr", renamer.getAnonymousName(), false), new IRType(IRType.IRTypeEnum.ptr), "malloc", new IRIntLiteral(classVarIndex.get(it.typeNd.type.name).size() * 4));
        currentBlock.push(call0);
        if (hasBuildClass.contains(it.typeNd.type.name)) {
            callInstNode call = new callInstNode(it, currentBlock, it.typeNd.type.name + ".build", call0.dest);
            currentBlock.push(call);
        }
        currentLeftVarAddr = alloca.dest.name;
        currentTmpValName = call0.dest.name;
    }
    
    @Override
    public void visit(preSelfExprNode it) {
        it.body.accept(this);
        IRVar body = new IRVar(getIRtype(it.body.typeNd.type).toString(), currentTmpValName, false);
        IRVar dest = new IRVar(getIRtype(it.typeNd.type).toString(), "%" + renamer.rename("pre." + it.op.toString()), false);
        currentBlock.push(new binaryInstNode(it, currentBlock, dest, it.op == preSelfExprNode.opType.Dec ? binaryExprNode.binaryOpType.sub : binaryExprNode.binaryOpType.add, body, new IRIntLiteral(1)));
        IRVar addr = new IRVar("ptr", currentLeftVarAddr, false);
        addr.typeInfo = getIRtype(it.typeNd.type);
        currentBlock.push(new storeInstNode(it, currentBlock, dest, addr));
//        currentLeftVarAddr = currentLeftVarAddr;
        currentTmpValName = dest.name;
    }
    
    @Override
    public void visit(unaryExprNode it) {
        it.body.accept(this);
        IRVar body = new IRVar(getIRtype(it.body.typeNd.type).toString(), currentTmpValName, false);
        IRVar dest = new IRVar(getIRtype(it.typeNd.type).toString(), "%" + renamer.rename(it.opCode.toString()), false);
        switch (it.opCode) {
            case Inc:
            case Dec:
                currentBlock.push(new binaryInstNode(it, currentBlock, dest, it.opCode == unaryExprNode.unaryOpType.Dec ? binaryExprNode.binaryOpType.sub : binaryExprNode.binaryOpType.add, body, new IRIntLiteral(1)));
                IRVar addr = new IRVar("ptr", currentLeftVarAddr, false);
                addr.typeInfo = getIRtype(it.typeNd.type);
                currentBlock.push(new storeInstNode(it, currentBlock, dest, addr));
//                currentTmpValName = currentTmpValName;
                currentLeftVarAddr = null;
                break;
            case logNot:
                currentBlock.push(new binaryInstNode(it, currentBlock, dest, binaryExprNode.binaryOpType.xor, body, new IRBoolLiteral(true)));
                currentTmpValName = dest.name;
                break;
            case bitNot:
                currentBlock.push(new binaryInstNode(it, currentBlock, dest, binaryExprNode.binaryOpType.xor, body, new IRIntLiteral(-1)));
                currentTmpValName = dest.name;
                break;
            case minus:
                currentBlock.push(new binaryInstNode(it, currentBlock, dest, binaryExprNode.binaryOpType.sub, new IRIntLiteral(0), body));
                currentTmpValName = dest.name;
                break;
            case plus:
                break;
        }
    }
    
    @Override
    public void visit(blockStmtNode it) {
        renamer.in();
        for (StmtNode stmt : it.stmts) {
            stmt.accept(this);
        }
        renamer.out();
    }
    
    @Override
    public void visit(breakStmtNode it) {
        currentBlock.push(new brInstNode(it, currentBlock, renamer.getRenamed("end..").substring(1)));
    }
    
    @Override
    public void visit(continueStmtNode it) {
        currentBlock.push(new brInstNode(it, currentBlock, renamer.getRenamed("step..").substring(1)));
    }
    
    @Override
    public void visit(exprStmtNode it) {
        it.expr.accept(this);
    }
    
    @Override
    public void visit(forStmtNode it) {
        renamer.in();
        if (it.init != null) {
            it.init.accept(this);
        }
        String condLabel = renamer.rename("cond..");
        String bodyLabel = renamer.rename("body..");
        String stepLabel = renamer.rename("step..");
        String endLabel = renamer.rename("end..");
        currentBlock.push(new brInstNode(it, currentBlock, condLabel));
        currentBlock = new IRBlockNode(currentBlock, currentFunDef, condLabel);
        currentFunDef.push(currentBlock);
        if (it.cond != null) {
            it.cond.accept(this);
            IRVar cond = new IRVar("i1", currentTmpValName, false);
            currentBlock.push(new brInstNode(it, currentBlock, cond, bodyLabel, endLabel));
        } else {
            currentBlock.push(new brInstNode(it, currentBlock, bodyLabel));
        }
        currentBlock = new IRBlockNode(currentBlock, currentFunDef, bodyLabel);
        currentFunDef.push(currentBlock);
        if (it.body != null) {
            it.body.accept(this);
        }
        currentBlock.push(new brInstNode(it, currentBlock, stepLabel));
        currentBlock = new IRBlockNode(currentBlock, currentFunDef, stepLabel);
        currentFunDef.push(currentBlock);
        if (it.step != null) {
            it.step.accept(this);
        }
        currentBlock.push(new brInstNode(it, currentBlock, condLabel));
        currentBlock = new IRBlockNode(currentBlock, currentFunDef, endLabel);
        currentFunDef.push(currentBlock);
        renamer.out();
    }
    
    @Override
    public void visit(ifStmtNode it) {
        it.condition.accept(this);
        IRVar cond = new IRVar("i1", currentTmpValName, false);
        String trueLabel = renamer.rename("if.true");
        String falseLabel = renamer.rename("if.false");
        String endLabel = renamer.rename("if.end");
        renamer.in();
        currentBlock.push(new brInstNode(it, currentBlock, cond, trueLabel, falseLabel));
        currentBlock = new IRBlockNode(currentBlock, currentFunDef, trueLabel);
        currentFunDef.push(currentBlock);
        if (it.thenStmt != null) {
            it.thenStmt.accept(this);
        }
        currentBlock.push(new brInstNode(it, currentBlock, endLabel));
        currentBlock = new IRBlockNode(currentBlock, currentFunDef, falseLabel);
        currentFunDef.push(currentBlock);
        if (it.elseStmt != null) {
            it.elseStmt.accept(this);
        }
        currentBlock.push(new brInstNode(it, currentBlock, endLabel));
        currentBlock = new IRBlockNode(currentBlock, currentFunDef, endLabel);
        currentFunDef.push(currentBlock);
        renamer.out();
    }
    
    @Override
    public void visit(returnStmtNode it) {
//        TODO:return;是null还是void？
//        assert false;
        if (it.value != null) if (it.value.typeNd.type.atomType != Type.TypeEnum.VOID) {
            it.value.accept(this);
            IRVar ret = new IRVar(getIRtype(it.value.typeNd.type).toString(), currentTmpValName, false);
            currentBlock.push(new storeInstNode(it, currentBlock, ret, new IRVar(getIRtype(it.value.typeNd.type).toString(), renamer.getRenamed("ret.val"), false)));
        }
        currentBlock.push(new brInstNode(it, currentBlock, "return"));
    }
    
    @Override
    public void visit(whileStmtNode it) {
        renamer.in();
        String condLabel = renamer.rename("step..");
        String bodyLabel = renamer.rename("body..");
        String endLabel = renamer.rename("end..");
        currentBlock.push(new brInstNode(it, currentBlock, condLabel));
        currentBlock = new IRBlockNode(currentBlock, currentFunDef, condLabel);
        currentFunDef.push(currentBlock);
        it.condition.accept(this);
        IRVar cond = new IRVar("i1", currentTmpValName, false);
        currentBlock.push(new brInstNode(it, currentBlock, cond, bodyLabel, endLabel));
        currentBlock = new IRBlockNode(currentBlock, currentFunDef, bodyLabel);
        currentFunDef.push(currentBlock);
        it.body.accept(this);
        currentBlock.push(new brInstNode(it, currentBlock, condLabel));
        currentBlock = new IRBlockNode(currentBlock, currentFunDef, endLabel);
        currentFunDef.push(currentBlock);
        renamer.out();
    }
    
    @Override
    public void visit(typeNode it) {
        assert false;
    }
    
    @Override
    public void visit(emptyStmtNode it) {
        return;
//        assert false;
    }
}
