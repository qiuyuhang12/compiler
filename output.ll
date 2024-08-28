define void @_.init() {
ret void
}
define i32 @main() {
entry:
%ret.val = alloca i32
call void @_.init()
%bs = alloca ptr
%0 = alloca ptr
%call = call ptr @array.alloca(i32 4, i32 1, i32 1, i32 5)
store ptr %call, ptr %0
store ptr %call, ptr %bs
%1 = load ptr, ptr %bs
%array.idx = getelementptr ptr, ptr %1, i32 2
%array.idx.val = load i1, ptr %array.idx
store i1 1, ptr %array.idx
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

