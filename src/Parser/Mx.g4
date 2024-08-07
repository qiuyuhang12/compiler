grammar Mx;

@header {package Parser;}
program: (funDef|classDef|varDef)*EOF;
classDef:Class Identifier '{'  (funDef|classBuild|varDef)* '}' Semi;
classBuild: Identifier '(' ')' '{' statement '}';
//classVisit: Identifier Member Identifier;


funDef: (type|Void) Identifier '(' (funParaList)? ')' '{' (statement)* '}';
funParaList: type Identifier (Comma type Identifier)*;

expression
    : New type ('[' expression? ']')+                                           #newArrayExpr
    | New type ('(' ')')?                                                       #newVarExpr
    | expression '(' (expression (Comma expression)*)? ')'                      #funExpr
    | expression '[' expression ']'                                             #arrayExpr
    | expression op=Dot Identifier                                              #memberExpr
    | expression op=(Increment | Decrement)                                     #unaryExpr
    | <assoc=right> op=(Not | LogicNot | Minus) expression                      #unaryExpr//去掉了+
    | <assoc=right> op=(Increment | Decrement) expression                       #preSelfExpr
    | expression op=(Mul | Div | Mod) expression                                #binaryExpr
    | expression op=(Plus | Minus) expression                                   #binaryExpr
    | expression op=(LeftShift | RightShift) expression                         #binaryExpr
    | expression op= (Greater | GreaterEqual | Less | LessEqual) expression     #binaryExpr
    | expression op=(Equal | UnEqual) expression                                #binaryExpr
    | expression op=And expression                                              #binaryExpr
    | expression op=Xor expression                                              #binaryExpr
    | expression op=Or expression                                               #binaryExpr
    | expression op=LogicAnd expression                                         #binaryExpr
    | expression op=LogicOr expression                                          #binaryExpr
    | <assoc=right> expression '?' expression ':' expression                    #conditionalExpr
    | <assoc=right> expression op=Assign expression                             #assignExpr
    | '(' expression ')'                                                        #parenExpr
    | (const|Identifier|This)                                                   #atomExpr
    | formatString                                                              #formatStringExpr
    ;

statement
    : block                                             #blockStmt
    | varDef ';'                                        #vardefStmt
    | if                                                #ifStmt
    | while                                             #whileStmt
    | for                                               #forStmt
    | return                                            #returnStmt
    | break                                             #breakStmt
    | continue                                          #continueStmt
    | expression ';'                                    #exprStmt
    | ';'                                               #emptyStmt
    ;
block: '{' statement* '}';
if: If '(' expression ')' thenStmt=statement (Else elseStmt=statement)?;
while: While '(' expression ')' statement;
for: For '(' initStmt=statement?  condExpr=expression? ';' stepExpr=expression? ')' statement;
return: Return expression? ';';
break: Break ';';
continue: Continue ';';

basicType: (Int|Bool|String);
type: (basicType|Identifier) ('[' ']')*;
varDef : type varDefUnit (Comma varDefUnit)* Semi;
varDefUnit : Identifier (Assign expression)?;

fragment FormatStringChar: '$$' |Escape | ~["$];
FmtStringS: 'f"' (FormatStringChar)* '"';
FmtStringL: 'f"' (FormatStringChar)* '$';
FmtStringM: '$' (FormatStringChar)* '$';
FmtStringR: '$' (FormatStringChar)* '"';
formatString
    : FmtStringS
    | FmtStringL (expression)? (FmtStringM (expression)?)*  FmtStringR;


Void : 'void';
Bool : 'bool';
Int : 'int';
String : 'string';
New : 'new';
Class : 'class';
Null : 'null';
True : 'true';
False : 'false';
This : 'this';
If : 'if';
Else : 'else';
For : 'for';
While : 'while';
Break : 'break';
Continue : 'continue';
Return : 'return';

//standard: Plus|Minus|Mul|Div|Mod;
Plus: '+';
Minus: '-';
Mul: '*';
Div: '/';
Mod: '%';

//relative: Greater|Less|GreaterEqual|LessEqual|UnEqual|Equal;
Greater: '>';
Less: '<';
GreaterEqual: '>=';
LessEqual: '<=';
UnEqual: '!=';
Equal: '==';

//logic: LogicAnd|LogicOr|LogicNot;
LogicAnd: '&&';
LogicOr: '||';
LogicNot: '!';

//bit: RightShift|LeftShift|And|Or|Xor|Not;
RightShift: '>>';
LeftShift: '<<';
And: '&';
Or: '|';
Xor: '^';
Not: '~';

Assign: '=';

Increment: '++';
Decrement: '--';

Dot: '.';


Identifier: [a-zA-Z][a-zA-Z0-9_]*;


Question: '?';
Colon: ':';
Semi : ';';
Comma : ',';



fragment Printable: [\u0020-\u007E];
const: True|False|IntegerConst|StringConst|Null|arrayConst;
IntegerConst: '0' | [1-9][0-9]*;
Escape: '\\\\' | '\\n' | '\\"';
StringConst: '"' (Escape | Printable| ' ')*? '"';
arrayConst: '{' (const (Comma const)*)? '}';


WhiteSpace: [\t\r\n ]+ -> skip;

LineComment: '//' ~[\r\n]* -> skip;
ParaComment: '/*' .*? '*/' -> skip;