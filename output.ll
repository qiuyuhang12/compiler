@.str = private unnamed_addr constant [2 x i8] c"s\00"
define i32 @main(){
entry:
  call void @print(ptr @.str)
  ret i32 0
}
declare void @print(ptr)