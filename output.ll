define void @_.init() {
ret void
}
define i32 @main() {
entry:
%ret.val = alloca i32
call void @_.init()
%a = alloca ptr
%0 = alloca ptr
%call = call ptr @array.alloca(i32 4, i32 1, i32 1, i32 4)
store ptr %call, ptr %0
%array-init = getelementptr ptr, ptr %0, i32 0
store i32 2, ptr %array-init
%array-init.1 = getelementptr ptr, ptr %0, i32 1
store i32 0, ptr %array-init.1
%array-init.2 = getelementptr ptr, ptr %0, i32 2
store i32 2, ptr %array-init.2
%array-init.3 = getelementptr ptr, ptr %0, i32 3
store i32 4, ptr %array-init.3
store ptr %0, ptr %a
%n = alloca i32
%1 = load ptr, ptr %a
%array.size = call i32 @array.size(ptr %1)
store i32 %array.size, ptr %n
%2 = load i32, ptr %n
call void @printlnInt(i32 %2)
%i = alloca i32
store i32 0, ptr %i
br label %cond..


cond..:
%3 = load i32, ptr %i
%4 = load i32, ptr %n
%less = icmp slt i32 %3, %4
br i1 %less, label %body.., label %end..


body..:
%5 = load ptr, ptr %a
%6 = load i32, ptr %i
%array.idx = getelementptr ptr, ptr %5, i32 %6
%array.idx.val = load i32, ptr %array.idx
call void @printlnInt(i32 %array.idx.val)
br label %step..


step..:
%7 = load i32, ptr %i
%pre.Inc = add i32 %7, 1
store i32 %pre.Inc, ptr %i
br label %cond..


end..:
store i32 0, ptr %ret.val
br label %return


return:
%r.e.t = load i32, ptr %ret.val
ret i32 %r.e.t


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

