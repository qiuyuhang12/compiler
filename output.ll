%class.Queue_int = type { ptr, i32, i32 }

@.str = private unnamed_addr constant [37 x i8] c"Warning: Queue_int::pop: empty queue\00"

@.str.1 = private unnamed_addr constant [27 x i8] c"q.size() != N after pushes\00"

@.str.2 = private unnamed_addr constant [10 x i8] c"Head != i\00"

@.str.3 = private unnamed_addr constant [21 x i8] c"Failed: q.pop() != i\00"

@.str.4 = private unnamed_addr constant [22 x i8] c"q.size() != N - i - 1\00"

@.str.5 = private unnamed_addr constant [14 x i8] c"Passed tests.\00"

define void @_.init() {
ret void
}
define void @Queue_int.push(ptr %this, i32 %v.val) {
entry:
%Queue_int.this = alloca ptr
store ptr %this, ptr %Queue_int.this
%Queue_int.storage = getelementptr %class.Queue_int, ptr %this, i32 0, i32 0
%Queue_int.beg = getelementptr %class.Queue_int, ptr %this, i32 0, i32 1
%Queue_int.end = getelementptr %class.Queue_int, ptr %this, i32 0, i32 2
%v = alloca i32
store i32 %v.val, ptr %v
%call.null.ret = call i32 @Queue_int.size(ptr %this)
%0 = load ptr, ptr %Queue_int.storage
%array.size = call i32 @array.size(ptr %0)
%sub = sub i32 %array.size, 1
%eq = icmp eq i32 %call.null.ret, %sub
br i1 %eq, label %if.true, label %if.false


if.true:
call void @Queue_int.doubleStorage(ptr %this)
br label %if.end


if.false:
br label %if.end


if.end:
%1 = load ptr, ptr %Queue_int.storage
%2 = load i32, ptr %Queue_int.end
%array.idx = getelementptr ptr, ptr %1, i32 %2
%array.idx.val = load i32, ptr %array.idx
%3 = load i32, ptr %v
store i32 %3, ptr %array.idx
%4 = load i32, ptr %Queue_int.end
%5 = load i32, ptr %Queue_int.end
%add = add i32 %5, 1
%6 = load ptr, ptr %Queue_int.storage
%array.size.1 = call i32 @array.size(ptr %6)
%mod = srem i32 %add, %array.size.1
store i32 %mod, ptr %Queue_int.end
br label %return


return:
ret void


}

define i32 @Queue_int.top(ptr %this) {
entry:
%Queue_int.this = alloca ptr
store ptr %this, ptr %Queue_int.this
%Queue_int.storage = getelementptr %class.Queue_int, ptr %this, i32 0, i32 0
%Queue_int.beg = getelementptr %class.Queue_int, ptr %this, i32 0, i32 1
%Queue_int.end = getelementptr %class.Queue_int, ptr %this, i32 0, i32 2
%ret.val = alloca i32
%7 = load ptr, ptr %Queue_int.storage
%8 = load i32, ptr %Queue_int.beg
%array.idx.1 = getelementptr ptr, ptr %7, i32 %8
%array.idx.val.1 = load i32, ptr %array.idx.1
store i32 %array.idx.val.1, ptr %ret.val
br label %return


return:
%r.e.t = load i32, ptr %ret.val
ret i32 %r.e.t


}

define i32 @Queue_int.pop(ptr %this) {
entry:
%Queue_int.this = alloca ptr
store ptr %this, ptr %Queue_int.this
%Queue_int.storage = getelementptr %class.Queue_int, ptr %this, i32 0, i32 0
%Queue_int.beg = getelementptr %class.Queue_int, ptr %this, i32 0, i32 1
%Queue_int.end = getelementptr %class.Queue_int, ptr %this, i32 0, i32 2
%ret.val.1 = alloca i32
%call.null.ret.1 = call i32 @Queue_int.size(ptr %this)
%eq.1 = icmp eq i32 %call.null.ret.1, 0
br i1 %eq.1, label %if.true.1, label %if.false.1


if.true.1:
call void @println(ptr @.str)
br label %if.end.1


if.false.1:
br label %if.end.1


if.end.1:
%res = alloca i32
%call.null.ret.2 = call i32 @Queue_int.top(ptr %this)
store i32 %call.null.ret.2, ptr %res
%9 = load i32, ptr %Queue_int.beg
%10 = load i32, ptr %Queue_int.beg
%add.1 = add i32 %10, 1
%11 = load ptr, ptr %Queue_int.storage
%array.size.2 = call i32 @array.size(ptr %11)
%mod.1 = srem i32 %add.1, %array.size.2
store i32 %mod.1, ptr %Queue_int.beg
%12 = load i32, ptr %res
store i32 %12, ptr %ret.val.1
br label %return


return:
%r.e.t.1 = load i32, ptr %ret.val.1
ret i32 %r.e.t.1


}

define i32 @Queue_int.size(ptr %this) {
entry:
%Queue_int.this = alloca ptr
store ptr %this, ptr %Queue_int.this
%Queue_int.storage = getelementptr %class.Queue_int, ptr %this, i32 0, i32 0
%Queue_int.beg = getelementptr %class.Queue_int, ptr %this, i32 0, i32 1
%Queue_int.end = getelementptr %class.Queue_int, ptr %this, i32 0, i32 2
%ret.val.2 = alloca i32
%13 = load i32, ptr %Queue_int.end
%14 = load ptr, ptr %Queue_int.storage
%array.size.3 = call i32 @array.size(ptr %14)
%add.2 = add i32 %13, %array.size.3
%15 = load i32, ptr %Queue_int.beg
%sub.1 = sub i32 %add.2, %15
%16 = load ptr, ptr %Queue_int.storage
%array.size.4 = call i32 @array.size(ptr %16)
%mod.2 = srem i32 %sub.1, %array.size.4
store i32 %mod.2, ptr %ret.val.2
br label %return


return:
%r.e.t.2 = load i32, ptr %ret.val.2
ret i32 %r.e.t.2


}

define void @Queue_int.doubleStorage(ptr %this) {
entry:
%Queue_int.this = alloca ptr
store ptr %this, ptr %Queue_int.this
%Queue_int.storage = getelementptr %class.Queue_int, ptr %this, i32 0, i32 0
%Queue_int.beg = getelementptr %class.Queue_int, ptr %this, i32 0, i32 1
%Queue_int.end = getelementptr %class.Queue_int, ptr %this, i32 0, i32 2
%copy = alloca ptr
%17 = load ptr, ptr %Queue_int.storage
store ptr %Queue_int.storage, ptr %copy
%begCopy = alloca i32
%18 = load i32, ptr %Queue_int.beg
store i32 %18, ptr %begCopy
%endCopy = alloca i32
%19 = load i32, ptr %Queue_int.end
store i32 %19, ptr %endCopy
%20 = load ptr, ptr %Queue_int.storage
%21 = load ptr, ptr %copy
%array.size.5 = call i32 @array.size(ptr %21)
%mul = mul i32 %array.size.5, 2
%call = call ptr @array.alloca(i32 4, i32 1, i32 1, i32 %mul)
store ptr %call, ptr %Queue_int.storage
%22 = load i32, ptr %Queue_int.beg
store i32 0, ptr %Queue_int.beg
%23 = load i32, ptr %Queue_int.end
store i32 0, ptr %Queue_int.end
%i = alloca i32
%24 = load i32, ptr %begCopy
store i32 %24, ptr %i
br label %step..


step..:
%25 = load i32, ptr %i
%26 = load i32, ptr %endCopy
%unEq = icmp ne i32 %25, %26
br i1 %unEq, label %body.., label %end..


body..:
%27 = load ptr, ptr %Queue_int.storage
%28 = load i32, ptr %Queue_int.end
%array.idx.2 = getelementptr ptr, ptr %27, i32 %28
%array.idx.val.2 = load i32, ptr %array.idx.2
%29 = load ptr, ptr %copy
%30 = load i32, ptr %i
%array.idx.3 = getelementptr ptr, ptr %29, i32 %30
%array.idx.val.3 = load i32, ptr %array.idx.3
store i32 %array.idx.val.3, ptr %array.idx.2
%31 = load i32, ptr %Queue_int.end
%Inc = add i32 %31, 1
store i32 %Inc, ptr %Queue_int.end
%32 = load i32, ptr %i
%33 = load i32, ptr %i
%add.3 = add i32 %33, 1
%34 = load ptr, ptr %copy
%array.size.6 = call i32 @array.size(ptr %34)
%mod.3 = srem i32 %add.3, %array.size.6
store i32 %mod.3, ptr %i
br label %step..


end..:
br label %return


return:
ret void


}

define void @Queue_int.build() {
entry:
%Queue_int.this = alloca ptr
store ptr %this, ptr %Queue_int.this
%Queue_int.storage = getelementptr %class.Queue_int, ptr %this, i32 0, i32 0
%Queue_int.beg = getelementptr %class.Queue_int, ptr %this, i32 0, i32 1
%Queue_int.end = getelementptr %class.Queue_int, ptr %this, i32 0, i32 2
%35 = load i32, ptr %Queue_int.beg
store i32 0, ptr %Queue_int.beg
%36 = load i32, ptr %Queue_int.end
store i32 0, ptr %Queue_int.end
%37 = load ptr, ptr %Queue_int.storage
%call.1 = call ptr @array.alloca(i32 4, i32 1, i32 1, i32 16)
store ptr %call.1, ptr %Queue_int.storage
br label %return


return:
ret void


}

define i32 @main() {
entry:
%ret.val.3 = alloca i32
call void @_.init()
%q = alloca ptr
%38 = call ptr @malloc(i32 12)
call void @Queue_int.build(ptr %Queue_int)
store ptr %38, ptr %q
%i.1 = alloca i32
%N = alloca i32
store i32 100, ptr %N
%39 = load i32, ptr %i.1
store i32 0, ptr %i.1
br label %cond..


cond..:
%40 = load i32, ptr %i.1
%41 = load i32, ptr %N
%less = icmp slt i32 %40, %41
br i1 %less, label %body...1, label %end...1


body...1:
%42 = load ptr, ptr %q
%43 = load i32, ptr %i.1
call void @Queue_int.push(ptr %42, i32 %43)
br label %step...1


step...1:
%44 = load i32, ptr %i.1
%pre.Inc = add i32 %44, 1
store i32 %pre.Inc, ptr %i.1
br label %cond..


end...1:
%45 = load ptr, ptr %q
%call.null.ret.3 = call i32 @Queue_int.size(ptr %45)
%46 = load i32, ptr %N
%unEq.1 = icmp ne i32 %call.null.ret.3, %46
br i1 %unEq.1, label %if.true.2, label %if.false.2


if.true.2:
call void @println(ptr @.str.1)
store i32 1, ptr %ret.val.3
br label %return


if.false.2:
br label %if.end.2


if.end.2:
%47 = load i32, ptr %i.1
store i32 0, ptr %i.1
br label %cond...1


cond...1:
%48 = load i32, ptr %i.1
%49 = load i32, ptr %N
%less.1 = icmp slt i32 %48, %49
br i1 %less.1, label %body...2, label %end...2


body...2:
%head = alloca i32
%50 = load ptr, ptr %q
%call.null.ret.4 = call i32 @Queue_int.top(ptr %50)
store i32 %call.null.ret.4, ptr %head
%51 = load i32, ptr %head
%52 = load i32, ptr %i.1
%unEq.2 = icmp ne i32 %51, %52
br i1 %unEq.2, label %if.true.3, label %if.false.3


if.true.3:
call void @println(ptr @.str.2)
store i32 1, ptr %ret.val.3
br label %return


if.false.3:
br label %if.end.3


if.end.3:
%53 = load ptr, ptr %q
%call.null.ret.5 = call i32 @Queue_int.pop(ptr %53)
%54 = load i32, ptr %i.1
%unEq.3 = icmp ne i32 %call.null.ret.5, %54
br i1 %unEq.3, label %if.true.4, label %if.false.4


if.true.4:
call void @println(ptr @.str.3)
store i32 1, ptr %ret.val.3
br label %return


if.false.4:
br label %if.end.4


if.end.4:
%55 = load ptr, ptr %q
%call.null.ret.6 = call i32 @Queue_int.size(ptr %55)
%56 = load i32, ptr %N
%57 = load i32, ptr %i.1
%sub.2 = sub i32 %56, %57
%sub.3 = sub i32 %sub.2, 1
%unEq.4 = icmp ne i32 %call.null.ret.6, %sub.3
br i1 %unEq.4, label %if.true.5, label %if.false.5


if.true.5:
call void @println(ptr @.str.4)
store i32 1, ptr %ret.val.3
br label %return


if.false.5:
br label %if.end.5


if.end.5:
br label %step...2


step...2:
%58 = load i32, ptr %i.1
%pre.Inc.1 = add i32 %58, 1
store i32 %pre.Inc.1, ptr %i.1
br label %cond...1


end...2:
call void @println(ptr @.str.5)
store i32 0, ptr %ret.val.3
br label %return


return:
%r.e.t.3 = load i32, ptr %ret.val.3
ret i32 %r.e.t.3


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

