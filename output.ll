define i32 @main() {
entry:
%ret.val = alloca i32
store i32 0, ptr %ret.val
call void @_.init()
br label %step..


step..:
br i1 1, label %body.., label %end..


body..:
%x = alloca i32
br label %step..


end..:
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

