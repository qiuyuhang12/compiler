@N = global i32 0

@h = global i32 99

@i = global i32 100

@j = global i32 101

@k = global i32 102

@total = global i32 0

@.fstr = private unnamed_addr constant [3 x i8] c"a=\00"

@.fstr.1 = private unnamed_addr constant [5 x i8] c", b=\00"

@.fstr.2 = private unnamed_addr constant [5 x i8] c", c=\00"

@.fstr.3 = private unnamed_addr constant [5 x i8] c", d=\00"

@.fstr.4 = private unnamed_addr constant [5 x i8] c", e=\00"

@.fstr.5 = private unnamed_addr constant [5 x i8] c", f=\00"

@.fstr.6 = private unnamed_addr constant [5 x i8] c",oo=\00"

define i32 @main() {
entry:
%ret.val = alloca i32
call void @_.init()
%a = alloca i32
%b = alloca i32
%c = alloca i32
%d = alloca i32
%e = alloca i32
%f = alloca i32
%0 = load i32, ptr @N
store i32 6, ptr @N
%oo = alloca i32
store i32 0, ptr %oo
%1 = load i32, ptr %a
store i32 1, ptr %a
br label %cond..


cond..:
%2 = load i32, ptr %a
%3 = load i32, ptr @N
%lessEq = icmp sle i32 %2, %3
br i1 %lessEq, label %body.., label %end..


body..:
%4 = load i32, ptr %b
store i32 1, ptr %b
br label %cond...1


cond...1:
%5 = load i32, ptr %b
%6 = load i32, ptr @N
%lessEq.1 = icmp sle i32 %5, %6
br i1 %lessEq.1, label %body...1, label %end...1


body...1:
%7 = load i32, ptr %c
store i32 1, ptr %c
br label %cond...2


cond...2:
%8 = load i32, ptr %c
%9 = load i32, ptr @N
%lessEq.2 = icmp sle i32 %8, %9
br i1 %lessEq.2, label %body...2, label %end...2


body...2:
%10 = load i32, ptr %d
store i32 1, ptr %d
br label %cond...3


cond...3:
%11 = load i32, ptr %d
%12 = load i32, ptr @N
%lessEq.3 = icmp sle i32 %11, %12
br i1 %lessEq.3, label %body...3, label %end...3


body...3:
%13 = load i32, ptr %e
store i32 1, ptr %e
br label %cond...4


cond...4:
%14 = load i32, ptr %e
%15 = load i32, ptr @N
%lessEq.4 = icmp sle i32 %14, %15
br i1 %lessEq.4, label %body...4, label %end...4


body...4:
%16 = load i32, ptr %f
store i32 1, ptr %f
br label %cond...5


cond...5:
%17 = load i32, ptr %f
%18 = load i32, ptr @N
%lessEq.5 = icmp sle i32 %17, %18
br i1 %lessEq.5, label %body...5, label %end...5


body...5:
%19 = load i32, ptr %a
%int = call ptr @toString(i32 %19)
%call = call ptr @string.concat(ptr @.fstr, ptr %int)
%call.1 = call ptr @string.concat(ptr %call, ptr @.fstr.1)
%20 = load i32, ptr %b
%int.1 = call ptr @toString(i32 %20)
%call.2 = call ptr @string.concat(ptr %call.1, ptr %int.1)
%call.3 = call ptr @string.concat(ptr %call.2, ptr @.fstr.2)
%21 = load i32, ptr %c
%int.2 = call ptr @toString(i32 %21)
%call.4 = call ptr @string.concat(ptr %call.3, ptr %int.2)
%call.5 = call ptr @string.concat(ptr %call.4, ptr @.fstr.3)
%22 = load i32, ptr %d
%int.3 = call ptr @toString(i32 %22)
%call.6 = call ptr @string.concat(ptr %call.5, ptr %int.3)
%call.7 = call ptr @string.concat(ptr %call.6, ptr @.fstr.4)
%23 = load i32, ptr %e
%int.4 = call ptr @toString(i32 %23)
%call.8 = call ptr @string.concat(ptr %call.7, ptr %int.4)
%call.9 = call ptr @string.concat(ptr %call.8, ptr @.fstr.5)
%24 = load i32, ptr %f
%int.5 = call ptr @toString(i32 %24)
%call.10 = call ptr @string.concat(ptr %call.9, ptr %int.5)
%call.11 = call ptr @string.concat(ptr %call.10, ptr @.fstr.6)
call void @print(ptr %call.11)
%25 = load i32, ptr %oo
%Inc = add i32 %25, 1
store i32 %Inc, ptr %oo
%toString.ret = call ptr @toString(i32 %25)
call void @println(ptr %toString.ret)
%26 = load i32, ptr %a
%27 = load i32, ptr %b
%unEq = icmp ne i32 %26, %27
%bool = call ptr @bool_toString(i1 %unEq)
%28 = load i32, ptr %a
%29 = load i32, ptr %c
%unEq.1 = icmp ne i32 %28, %29
%bool.1 = call ptr @bool_toString(i1 %unEq.1)
%call.12 = call ptr @string.concat(ptr %bool, ptr %bool.1)
%30 = load i32, ptr %a
%31 = load i32, ptr %d
%unEq.2 = icmp ne i32 %30, %31
%bool.2 = call ptr @bool_toString(i1 %unEq.2)
%call.13 = call ptr @string.concat(ptr %call.12, ptr %bool.2)
%32 = load i32, ptr %a
%33 = load i32, ptr %e
%unEq.3 = icmp ne i32 %32, %33
%bool.3 = call ptr @bool_toString(i1 %unEq.3)
%call.14 = call ptr @string.concat(ptr %call.13, ptr %bool.3)
%34 = load i32, ptr %a
%35 = load i32, ptr %f
%unEq.4 = icmp ne i32 %34, %35
%bool.4 = call ptr @bool_toString(i1 %unEq.4)
%call.15 = call ptr @string.concat(ptr %call.14, ptr %bool.4)
%36 = load i32, ptr %a
%37 = load i32, ptr @h
%unEq.5 = icmp ne i32 %36, %37
%bool.5 = call ptr @bool_toString(i1 %unEq.5)
%call.16 = call ptr @string.concat(ptr %call.15, ptr %bool.5)
%38 = load i32, ptr %a
%39 = load i32, ptr @i
%unEq.6 = icmp ne i32 %38, %39
%bool.6 = call ptr @bool_toString(i1 %unEq.6)
%call.17 = call ptr @string.concat(ptr %call.16, ptr %bool.6)
%40 = load i32, ptr %f
%41 = load i32, ptr @h
%unEq.7 = icmp ne i32 %40, %41
%bool.7 = call ptr @bool_toString(i1 %unEq.7)
%call.18 = call ptr @string.concat(ptr %call.17, ptr %bool.7)
%42 = load i32, ptr %f
%43 = load i32, ptr @i
%unEq.8 = icmp ne i32 %42, %43
%bool.8 = call ptr @bool_toString(i1 %unEq.8)
%call.19 = call ptr @string.concat(ptr %call.18, ptr %bool.8)
%44 = load i32, ptr %f
%45 = load i32, ptr @j
%unEq.9 = icmp ne i32 %44, %45
%bool.9 = call ptr @bool_toString(i1 %unEq.9)
%call.20 = call ptr @string.concat(ptr %call.19, ptr %bool.9)
%46 = load i32, ptr %f
%47 = load i32, ptr @k
%unEq.10 = icmp ne i32 %46, %47
%bool.10 = call ptr @bool_toString(i1 %unEq.10)
%call.21 = call ptr @string.concat(ptr %call.20, ptr %bool.10)
%48 = load i32, ptr @i
%49 = load i32, ptr @j
%unEq.11 = icmp ne i32 %48, %49
%bool.11 = call ptr @bool_toString(i1 %unEq.11)
%call.22 = call ptr @string.concat(ptr %call.21, ptr %bool.11)
%50 = load i32, ptr @h
%51 = load i32, ptr @k
%unEq.12 = icmp ne i32 %50, %51
%bool.12 = call ptr @bool_toString(i1 %unEq.12)
%call.23 = call ptr @string.concat(ptr %call.22, ptr %bool.12)
call void @println(ptr %call.23)
%52 = load i32, ptr %a
%53 = load i32, ptr %b
%unEq.13 = icmp ne i32 %52, %53
%shortcircuit.ptr = alloca i1
store i1 %unEq.13, ptr %shortcircuit.ptr
br i1 %unEq.13, label %shortcircuit.next, label %shortcircuit.end


shortcircuit.next:
%54 = load i32, ptr %a
%55 = load i32, ptr %c
%unEq.14 = icmp ne i32 %54, %55
store i1 %unEq.14, ptr %shortcircuit.ptr
br label %shortcircuit.end


shortcircuit.end:
%56 = load i1, ptr %shortcircuit.ptr
%shortcircuit.ptr.1 = alloca i1
store i1 %56, ptr %shortcircuit.ptr.1
br i1 %56, label %shortcircuit.next.1, label %shortcircuit.end.1


shortcircuit.next.1:
%57 = load i32, ptr %a
%58 = load i32, ptr %d
%unEq.15 = icmp ne i32 %57, %58
store i1 %unEq.15, ptr %shortcircuit.ptr.1
br label %shortcircuit.end.1


shortcircuit.end.1:
%59 = load i1, ptr %shortcircuit.ptr.1
%shortcircuit.ptr.2 = alloca i1
store i1 %59, ptr %shortcircuit.ptr.2
br i1 %59, label %shortcircuit.next.2, label %shortcircuit.end.2


shortcircuit.next.2:
%60 = load i32, ptr %a
%61 = load i32, ptr %e
%unEq.16 = icmp ne i32 %60, %61
store i1 %unEq.16, ptr %shortcircuit.ptr.2
br label %shortcircuit.end.2


shortcircuit.end.2:
%62 = load i1, ptr %shortcircuit.ptr.2
%shortcircuit.ptr.3 = alloca i1
store i1 %62, ptr %shortcircuit.ptr.3
br i1 %62, label %shortcircuit.next.3, label %shortcircuit.end.3


shortcircuit.next.3:
%63 = load i32, ptr %a
%64 = load i32, ptr %f
%unEq.17 = icmp ne i32 %63, %64
store i1 %unEq.17, ptr %shortcircuit.ptr.3
br label %shortcircuit.end.3


shortcircuit.end.3:
%65 = load i1, ptr %shortcircuit.ptr.3
%shortcircuit.ptr.4 = alloca i1
store i1 %65, ptr %shortcircuit.ptr.4
br i1 %65, label %shortcircuit.next.4, label %shortcircuit.end.4


shortcircuit.next.4:
%66 = load i32, ptr %a
%67 = load i32, ptr @h
%unEq.18 = icmp ne i32 %66, %67
store i1 %unEq.18, ptr %shortcircuit.ptr.4
br label %shortcircuit.end.4


shortcircuit.end.4:
%68 = load i1, ptr %shortcircuit.ptr.4
%shortcircuit.ptr.5 = alloca i1
store i1 %68, ptr %shortcircuit.ptr.5
br i1 %68, label %shortcircuit.next.5, label %shortcircuit.end.5


shortcircuit.next.5:
%69 = load i32, ptr %a
%70 = load i32, ptr @i
%unEq.19 = icmp ne i32 %69, %70
store i1 %unEq.19, ptr %shortcircuit.ptr.5
br label %shortcircuit.end.5


shortcircuit.end.5:
%71 = load i1, ptr %shortcircuit.ptr.5
%shortcircuit.ptr.6 = alloca i1
store i1 %71, ptr %shortcircuit.ptr.6
br i1 %71, label %shortcircuit.next.6, label %shortcircuit.end.6


shortcircuit.next.6:
%72 = load i32, ptr %f
%73 = load i32, ptr @h
%unEq.20 = icmp ne i32 %72, %73
store i1 %unEq.20, ptr %shortcircuit.ptr.6
br label %shortcircuit.end.6


shortcircuit.end.6:
%74 = load i1, ptr %shortcircuit.ptr.6
%shortcircuit.ptr.7 = alloca i1
store i1 %74, ptr %shortcircuit.ptr.7
br i1 %74, label %shortcircuit.next.7, label %shortcircuit.end.7


shortcircuit.next.7:
%75 = load i32, ptr %f
%76 = load i32, ptr @i
%unEq.21 = icmp ne i32 %75, %76
store i1 %unEq.21, ptr %shortcircuit.ptr.7
br label %shortcircuit.end.7


shortcircuit.end.7:
%77 = load i1, ptr %shortcircuit.ptr.7
%shortcircuit.ptr.8 = alloca i1
store i1 %77, ptr %shortcircuit.ptr.8
br i1 %77, label %shortcircuit.next.8, label %shortcircuit.end.8


shortcircuit.next.8:
%78 = load i32, ptr %f
%79 = load i32, ptr @j
%unEq.22 = icmp ne i32 %78, %79
store i1 %unEq.22, ptr %shortcircuit.ptr.8
br label %shortcircuit.end.8


shortcircuit.end.8:
%80 = load i1, ptr %shortcircuit.ptr.8
%shortcircuit.ptr.9 = alloca i1
store i1 %80, ptr %shortcircuit.ptr.9
br i1 %80, label %shortcircuit.next.9, label %shortcircuit.end.9


shortcircuit.next.9:
%81 = load i32, ptr %f
%82 = load i32, ptr @k
%unEq.23 = icmp ne i32 %81, %82
store i1 %unEq.23, ptr %shortcircuit.ptr.9
br label %shortcircuit.end.9


shortcircuit.end.9:
%83 = load i1, ptr %shortcircuit.ptr.9
%shortcircuit.ptr.10 = alloca i1
store i1 %83, ptr %shortcircuit.ptr.10
br i1 %83, label %shortcircuit.next.10, label %shortcircuit.end.10


shortcircuit.next.10:
%84 = load i32, ptr @i
%85 = load i32, ptr @j
%unEq.24 = icmp ne i32 %84, %85
store i1 %unEq.24, ptr %shortcircuit.ptr.10
br label %shortcircuit.end.10


shortcircuit.end.10:
%86 = load i1, ptr %shortcircuit.ptr.10
%shortcircuit.ptr.11 = alloca i1
store i1 %86, ptr %shortcircuit.ptr.11
br i1 %86, label %shortcircuit.next.11, label %shortcircuit.end.11


shortcircuit.next.11:
%87 = load i32, ptr @h
%88 = load i32, ptr @k
%unEq.25 = icmp ne i32 %87, %88
store i1 %unEq.25, ptr %shortcircuit.ptr.11
br label %shortcircuit.end.11


shortcircuit.end.11:
%89 = load i1, ptr %shortcircuit.ptr.11
br i1 %89, label %if.true, label %if.false


if.true:
%90 = load i32, ptr @total
%Inc.1 = add i32 %90, 1
store i32 %Inc.1, ptr @total
br label %if.end


if.false:
br label %if.end


if.end:
br label %step...5


step...5:
%91 = load i32, ptr %f
%Inc.2 = add i32 %91, 1
store i32 %Inc.2, ptr %f
br label %cond...5


end...5:
br label %step...4


step...4:
%92 = load i32, ptr %e
%Inc.3 = add i32 %92, 1
store i32 %Inc.3, ptr %e
br label %cond...4


end...4:
br label %step...3


step...3:
%93 = load i32, ptr %d
%Inc.4 = add i32 %93, 1
store i32 %Inc.4, ptr %d
br label %cond...3


end...3:
br label %step...2


step...2:
%94 = load i32, ptr %c
%Inc.5 = add i32 %94, 1
store i32 %Inc.5, ptr %c
br label %cond...2


end...2:
br label %step...1


step...1:
%95 = load i32, ptr %b
%Inc.6 = add i32 %95, 1
store i32 %Inc.6, ptr %b
br label %cond...1


end...1:
br label %step..


step..:
%96 = load i32, ptr %a
%Inc.7 = add i32 %96, 1
store i32 %Inc.7, ptr %a
br label %cond..


end..:
%97 = load i32, ptr @total
%toString.ret.1 = call ptr @toString(i32 %97)
call void @println(ptr %toString.ret.1)
store i32 0, ptr %ret.val
br label %return


return:
%r.e.t = load i32, ptr %ret.val
ret i32 %r.e.t


}

define void @_.init() {
entry:
ret void


}

declare ptr @malloc(i32)
declare void @print(ptr)
declare void @println(ptr %0)
declare void @printInt(i32 %0)
declare void @printlnInt(i32 %0)
declare ptr @getString()
declare i32 @getInt()
declare ptr @toString(i32 %0)
declare ptr @bool_toString(i1 %0)
declare ptr @alloca_helper(i32 %0, i32 %1)
declare i32 @array.size(ptr %0)
declare i32 @string.length(ptr %0)
declare ptr @string.substring(ptr %0, i32 %1, i32 %2)
declare i32 @string.parseInt(ptr %0)
declare i32 @string.compare(ptr %0, ptr %1)
declare ptr @string.concat(ptr %0, ptr %1)
declare void @string.copy(ptr %0, ptr %1)
declare i32 @string.ord(ptr %0, i32 %1)
declare ptr @array.alloca_inside(i32 %0, i32 %1, ptr %2, i32 %3)
declare ptr @array.alloca(i32 %0, i32 %1, i32 %2, ...)

