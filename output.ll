%class.point = type { i32, i32, i32 }

@.str = private unnamed_addr constant [2 x i8] c"(\00"

@.str.1 = private unnamed_addr constant [3 x i8] c", \00"

@.str.2 = private unnamed_addr constant [3 x i8] c", \00"

@.str.3 = private unnamed_addr constant [2 x i8] c")\00"

define void @_.init() {
ret void
}
define void @point.set(ptr %this, i32 %a_x.val, i32 %a_y.val, i32 %a_z.val) {
entry:
%point.this = alloca ptr
store ptr %this, ptr %point.this
%point.x = getelementptr %class.point, ptr %this, i32 0, i32 0
%point.y = getelementptr %class.point, ptr %this, i32 0, i32 1
%point.z = getelementptr %class.point, ptr %this, i32 0, i32 2
%a_x = alloca i32
store i32 %a_x.val, ptr %a_x
%a_y = alloca i32
store i32 %a_y.val, ptr %a_y
%a_z = alloca i32
store i32 %a_z.val, ptr %a_z
%0 = load i32, ptr %point.x
%1 = load i32, ptr %a_x
store i32 %1, ptr %point.x
%2 = load i32, ptr %point.y
%3 = load i32, ptr %a_y
store i32 %3, ptr %point.y
%4 = load i32, ptr %point.z
%5 = load i32, ptr %a_z
store i32 %5, ptr %point.z
br label %return


return:
ret void


}

define i32 @point.sqrLen(ptr %this) {
entry:
%point.this = alloca ptr
store ptr %this, ptr %point.this
%point.x = getelementptr %class.point, ptr %this, i32 0, i32 0
%point.y = getelementptr %class.point, ptr %this, i32 0, i32 1
%point.z = getelementptr %class.point, ptr %this, i32 0, i32 2
%ret.val = alloca i32
%6 = load i32, ptr %point.x
%7 = load i32, ptr %point.x
%mul = mul i32 %6, %7
%8 = load i32, ptr %point.y
%9 = load i32, ptr %point.y
%mul.1 = mul i32 %8, %9
%add = add i32 %mul, %mul.1
%10 = load i32, ptr %point.z
%11 = load i32, ptr %point.z
%mul.2 = mul i32 %10, %11
%add.1 = add i32 %add, %mul.2
store i32 %add.1, ptr %ret.val
br label %return


return:
%r.e.t = load i32, ptr %ret.val
ret i32 %r.e.t


}

define i32 @point.sqrDis(ptr %this, ptr %other.val) {
entry:
%point.this = alloca ptr
store ptr %this, ptr %point.this
%point.x = getelementptr %class.point, ptr %this, i32 0, i32 0
%point.y = getelementptr %class.point, ptr %this, i32 0, i32 1
%point.z = getelementptr %class.point, ptr %this, i32 0, i32 2
%other = alloca ptr
store ptr %other.val, ptr %other
%ret.val.1 = alloca i32
%12 = load i32, ptr %point.x
%13 = load ptr, ptr %other
%14 = getelementptr %class.point, ptr %13, i32 0, i32 0
%15 = load i32, ptr %14
%sub = sub i32 %12, %15
%16 = load i32, ptr %point.x
%17 = load ptr, ptr %other
%18 = getelementptr %class.point, ptr %17, i32 0, i32 0
%19 = load i32, ptr %18
%sub.1 = sub i32 %16, %19
%mul.3 = mul i32 %sub, %sub.1
%20 = load i32, ptr %point.y
%21 = load ptr, ptr %other
%22 = getelementptr %class.point, ptr %21, i32 0, i32 1
%23 = load i32, ptr %22
%sub.2 = sub i32 %20, %23
%24 = load i32, ptr %point.y
%25 = load ptr, ptr %other
%26 = getelementptr %class.point, ptr %25, i32 0, i32 1
%27 = load i32, ptr %26
%sub.3 = sub i32 %24, %27
%mul.4 = mul i32 %sub.2, %sub.3
%add.2 = add i32 %mul.3, %mul.4
%28 = load i32, ptr %point.z
%29 = load ptr, ptr %other
%30 = getelementptr %class.point, ptr %29, i32 0, i32 2
%31 = load i32, ptr %30
%sub.4 = sub i32 %28, %31
%32 = load i32, ptr %point.z
%33 = load ptr, ptr %other
%34 = getelementptr %class.point, ptr %33, i32 0, i32 2
%35 = load i32, ptr %34
%sub.5 = sub i32 %32, %35
%mul.5 = mul i32 %sub.4, %sub.5
%add.3 = add i32 %add.2, %mul.5
store i32 %add.3, ptr %ret.val.1
br label %return


return:
%r.e.t.1 = load i32, ptr %ret.val.1
ret i32 %r.e.t.1


}

define i32 @point.dot(ptr %this, ptr %other.val) {
entry:
%point.this = alloca ptr
store ptr %this, ptr %point.this
%point.x = getelementptr %class.point, ptr %this, i32 0, i32 0
%point.y = getelementptr %class.point, ptr %this, i32 0, i32 1
%point.z = getelementptr %class.point, ptr %this, i32 0, i32 2
%other.1 = alloca ptr
store ptr %other.val, ptr %other.1
%ret.val.2 = alloca i32
%36 = load i32, ptr %point.x
%37 = load ptr, ptr %other.1
%38 = getelementptr %class.point, ptr %37, i32 0, i32 0
%39 = load i32, ptr %38
%mul.6 = mul i32 %36, %39
%40 = load i32, ptr %point.y
%41 = load ptr, ptr %other.1
%42 = getelementptr %class.point, ptr %41, i32 0, i32 1
%43 = load i32, ptr %42
%mul.7 = mul i32 %40, %43
%add.4 = add i32 %mul.6, %mul.7
%44 = load i32, ptr %point.z
%45 = load ptr, ptr %other.1
%46 = getelementptr %class.point, ptr %45, i32 0, i32 2
%47 = load i32, ptr %46
%mul.8 = mul i32 %44, %47
%add.5 = add i32 %add.4, %mul.8
store i32 %add.5, ptr %ret.val.2
br label %return


return:
%r.e.t.2 = load i32, ptr %ret.val.2
ret i32 %r.e.t.2


}

define ptr @point.cross(ptr %this, ptr %other.val) {
entry:
%point.this = alloca ptr
store ptr %this, ptr %point.this
%point.x = getelementptr %class.point, ptr %this, i32 0, i32 0
%point.y = getelementptr %class.point, ptr %this, i32 0, i32 1
%point.z = getelementptr %class.point, ptr %this, i32 0, i32 2
%other.2 = alloca ptr
store ptr %other.val, ptr %other.2
%ret.val.3 = alloca ptr
%retval = alloca ptr
%48 = call ptr @malloc(i32 12)
store ptr %48, ptr %retval
%49 = load ptr, ptr %retval
%50 = load i32, ptr %point.y
%51 = load ptr, ptr %other.2
%52 = getelementptr %class.point, ptr %51, i32 0, i32 2
%53 = load i32, ptr %52
%mul.9 = mul i32 %50, %53
%54 = load i32, ptr %point.z
%55 = load ptr, ptr %other.2
%56 = getelementptr %class.point, ptr %55, i32 0, i32 1
%57 = load i32, ptr %56
%mul.10 = mul i32 %54, %57
%sub.6 = sub i32 %mul.9, %mul.10
%58 = load i32, ptr %point.z
%59 = load ptr, ptr %other.2
%60 = getelementptr %class.point, ptr %59, i32 0, i32 0
%61 = load i32, ptr %60
%mul.11 = mul i32 %58, %61
%62 = load i32, ptr %point.x
%63 = load ptr, ptr %other.2
%64 = getelementptr %class.point, ptr %63, i32 0, i32 2
%65 = load i32, ptr %64
%mul.12 = mul i32 %62, %65
%sub.7 = sub i32 %mul.11, %mul.12
%66 = load i32, ptr %point.x
%67 = load ptr, ptr %other.2
%68 = getelementptr %class.point, ptr %67, i32 0, i32 1
%69 = load i32, ptr %68
%mul.13 = mul i32 %66, %69
%70 = load i32, ptr %point.y
%71 = load ptr, ptr %other.2
%72 = getelementptr %class.point, ptr %71, i32 0, i32 0
%73 = load i32, ptr %72
%mul.14 = mul i32 %70, %73
%sub.8 = sub i32 %mul.13, %mul.14
call void @point.set(ptr %49, i32 %sub.6, i32 %sub.7, i32 %sub.8)
%74 = load ptr, ptr %retval
store ptr %74, ptr %ret.val.3
br label %return


return:
%r.e.t.3 = load ptr, ptr %ret.val.3
ret ptr %r.e.t.3


}

define ptr @point.add(ptr %this, ptr %other.val) {
entry:
%point.this = alloca ptr
store ptr %this, ptr %point.this
%point.x = getelementptr %class.point, ptr %this, i32 0, i32 0
%point.y = getelementptr %class.point, ptr %this, i32 0, i32 1
%point.z = getelementptr %class.point, ptr %this, i32 0, i32 2
%other.3 = alloca ptr
store ptr %other.val, ptr %other.3
%ret.val.4 = alloca ptr
%75 = load i32, ptr %point.x
%76 = load i32, ptr %point.x
%77 = load ptr, ptr %other.3
%78 = getelementptr %class.point, ptr %77, i32 0, i32 0
%79 = load i32, ptr %78
%add.6 = add i32 %76, %79
store i32 %add.6, ptr %point.x
%80 = load i32, ptr %point.y
%81 = load i32, ptr %point.y
%82 = load ptr, ptr %other.3
%83 = getelementptr %class.point, ptr %82, i32 0, i32 1
%84 = load i32, ptr %83
%add.7 = add i32 %81, %84
store i32 %add.7, ptr %point.y
%85 = load i32, ptr %point.z
%86 = load i32, ptr %point.z
%87 = load ptr, ptr %other.3
%88 = getelementptr %class.point, ptr %87, i32 0, i32 2
%89 = load i32, ptr %88
%add.8 = add i32 %86, %89
store i32 %add.8, ptr %point.z
store ptr %this, ptr %ret.val.4
br label %return


return:
%r.e.t.4 = load ptr, ptr %ret.val.4
ret ptr %r.e.t.4


}

define ptr @point.sub(ptr %this, ptr %other.val) {
entry:
%point.this = alloca ptr
store ptr %this, ptr %point.this
%point.x = getelementptr %class.point, ptr %this, i32 0, i32 0
%point.y = getelementptr %class.point, ptr %this, i32 0, i32 1
%point.z = getelementptr %class.point, ptr %this, i32 0, i32 2
%other.4 = alloca ptr
store ptr %other.val, ptr %other.4
%ret.val.5 = alloca ptr
%90 = load i32, ptr %point.x
%91 = load i32, ptr %point.x
%92 = load ptr, ptr %other.4
%93 = getelementptr %class.point, ptr %92, i32 0, i32 0
%94 = load i32, ptr %93
%sub.9 = sub i32 %91, %94
store i32 %sub.9, ptr %point.x
%95 = load i32, ptr %point.y
%96 = load i32, ptr %point.y
%97 = load ptr, ptr %other.4
%98 = getelementptr %class.point, ptr %97, i32 0, i32 1
%99 = load i32, ptr %98
%sub.10 = sub i32 %96, %99
store i32 %sub.10, ptr %point.y
%100 = load i32, ptr %point.z
%101 = load i32, ptr %point.z
%102 = load ptr, ptr %other.4
%103 = getelementptr %class.point, ptr %102, i32 0, i32 2
%104 = load i32, ptr %103
%sub.11 = sub i32 %101, %104
store i32 %sub.11, ptr %point.z
store ptr %this, ptr %ret.val.5
br label %return


return:
%r.e.t.5 = load ptr, ptr %ret.val.5
ret ptr %r.e.t.5


}

define void @point.printPoint(ptr %this) {
entry:
%point.this = alloca ptr
store ptr %this, ptr %point.this
%point.x = getelementptr %class.point, ptr %this, i32 0, i32 0
%point.y = getelementptr %class.point, ptr %this, i32 0, i32 1
%point.z = getelementptr %class.point, ptr %this, i32 0, i32 2
%105 = load i32, ptr %point.x
%toString.ret = call ptr @toString(i32 %105)
%add.9 = call ptr @string.concat(ptr @.str, ptr %toString.ret)
%add.10 = call ptr @string.concat(ptr %add.9, ptr @.str.1)
%106 = load i32, ptr %point.y
%toString.ret.1 = call ptr @toString(i32 %106)
%add.11 = call ptr @string.concat(ptr %add.10, ptr %toString.ret.1)
%add.12 = call ptr @string.concat(ptr %add.11, ptr @.str.2)
%107 = load i32, ptr %point.z
%toString.ret.2 = call ptr @toString(i32 %107)
%add.13 = call ptr @string.concat(ptr %add.12, ptr %toString.ret.2)
%add.14 = call ptr @string.concat(ptr %add.13, ptr @.str.3)
call void @println(ptr %add.14)
br label %return


return:
ret void


}

define void @point.build() {
entry:
%point.this = alloca ptr
store ptr %this, ptr %point.this
%point.x = getelementptr %class.point, ptr %this, i32 0, i32 0
%point.y = getelementptr %class.point, ptr %this, i32 0, i32 1
%point.z = getelementptr %class.point, ptr %this, i32 0, i32 2
%108 = load i32, ptr %point.x
store i32 0, ptr %point.x
%109 = load i32, ptr %point.y
store i32 0, ptr %point.y
%110 = load i32, ptr %point.z
store i32 0, ptr %point.z
br label %return


return:
ret void


}

define i32 @main() {
entry:
%ret.val.6 = alloca i32
call void @_.init()
%a = alloca ptr
%111 = call ptr @malloc(i32 12)
call void @point.build(ptr %point.1)
store ptr %111, ptr %a
%b = alloca ptr
%112 = call ptr @malloc(i32 12)
call void @point.build(ptr %point.2)
store ptr %112, ptr %b
%c = alloca ptr
%113 = call ptr @malloc(i32 12)
call void @point.build(ptr %point.3)
store ptr %113, ptr %c
%d = alloca ptr
%114 = call ptr @malloc(i32 12)
call void @point.build(ptr %point.4)
store ptr %114, ptr %d
%115 = load ptr, ptr %a
call void @point.printPoint(ptr %115)
%116 = load ptr, ptr %a
%minus = sub i32 0, 463
call void @point.set(ptr %116, i32 849, i32 %minus, i32 480)
%117 = load ptr, ptr %b
%minus.1 = sub i32 0, 208
%minus.2 = sub i32 0, 150
call void @point.set(ptr %117, i32 %minus.1, i32 585, i32 %minus.2)
%118 = load ptr, ptr %c
%minus.3 = sub i32 0, 670
%minus.4 = sub i32 0, 742
call void @point.set(ptr %118, i32 360, i32 %minus.3, i32 %minus.4)
%119 = load ptr, ptr %d
%minus.5 = sub i32 0, 29
%minus.6 = sub i32 0, 591
%minus.7 = sub i32 0, 960
call void @point.set(ptr %119, i32 %minus.5, i32 %minus.6, i32 %minus.7)
%120 = load ptr, ptr %a
%121 = load ptr, ptr %b
%call.null.ret = call ptr @point.add(ptr %120, ptr %121)
%122 = load ptr, ptr %b
%123 = load ptr, ptr %c
%call.null.ret.1 = call ptr @point.add(ptr %122, ptr %123)
%124 = load ptr, ptr %d
%125 = load ptr, ptr %c
%call.null.ret.2 = call ptr @point.add(ptr %124, ptr %125)
%126 = load ptr, ptr %c
%127 = load ptr, ptr %a
%call.null.ret.3 = call ptr @point.sub(ptr %126, ptr %127)
%128 = load ptr, ptr %b
%129 = load ptr, ptr %d
%call.null.ret.4 = call ptr @point.sub(ptr %128, ptr %129)
%130 = load ptr, ptr %d
%131 = load ptr, ptr %c
%call.null.ret.5 = call ptr @point.sub(ptr %130, ptr %131)
%132 = load ptr, ptr %c
%133 = load ptr, ptr %b
%call.null.ret.6 = call ptr @point.add(ptr %132, ptr %133)
%134 = load ptr, ptr %a
%135 = load ptr, ptr %b
%call.null.ret.7 = call ptr @point.add(ptr %134, ptr %135)
%136 = load ptr, ptr %b
%137 = load ptr, ptr %b
%call.null.ret.8 = call ptr @point.add(ptr %136, ptr %137)
%138 = load ptr, ptr %c
%139 = load ptr, ptr %c
%call.null.ret.9 = call ptr @point.add(ptr %138, ptr %139)
%140 = load ptr, ptr %a
%141 = load ptr, ptr %d
%call.null.ret.10 = call ptr @point.sub(ptr %140, ptr %141)
%142 = load ptr, ptr %a
%143 = load ptr, ptr %b
%call.null.ret.11 = call ptr @point.add(ptr %142, ptr %143)
%144 = load ptr, ptr %b
%145 = load ptr, ptr %c
%call.null.ret.12 = call ptr @point.sub(ptr %144, ptr %145)
%146 = load ptr, ptr %a
%call.null.ret.13 = call i32 @point.sqrLen(ptr %146)
%toString.ret.3 = call ptr @toString(i32 %call.null.ret.13)
call void @println(ptr %toString.ret.3)
%147 = load ptr, ptr %b
%call.null.ret.14 = call i32 @point.sqrLen(ptr %147)
%toString.ret.4 = call ptr @toString(i32 %call.null.ret.14)
call void @println(ptr %toString.ret.4)
%148 = load ptr, ptr %b
%149 = load ptr, ptr %c
%call.null.ret.15 = call i32 @point.sqrDis(ptr %148, ptr %149)
%toString.ret.5 = call ptr @toString(i32 %call.null.ret.15)
call void @println(ptr %toString.ret.5)
%150 = load ptr, ptr %d
%151 = load ptr, ptr %a
%call.null.ret.16 = call i32 @point.sqrDis(ptr %150, ptr %151)
%toString.ret.6 = call ptr @toString(i32 %call.null.ret.16)
call void @println(ptr %toString.ret.6)
%152 = load ptr, ptr %c
%153 = load ptr, ptr %a
%call.null.ret.17 = call i32 @point.dot(ptr %152, ptr %153)
%toString.ret.7 = call ptr @toString(i32 %call.null.ret.17)
call void @println(ptr %toString.ret.7)
%154 = load ptr, ptr %b
%155 = load ptr, ptr %d
%call.null.ret.18 = call ptr @point.cross(ptr %154, ptr %155)
call void @point.printPoint(ptr %call.null.ret.18)
%156 = load ptr, ptr %a
call void @point.printPoint(ptr %156)
%157 = load ptr, ptr %b
call void @point.printPoint(ptr %157)
%158 = load ptr, ptr %c
call void @point.printPoint(ptr %158)
%159 = load ptr, ptr %d
call void @point.printPoint(ptr %159)
store i32 0, ptr %ret.val.6
br label %return


return:
%r.e.t.6 = load i32, ptr %ret.val.6
ret i32 %r.e.t.6


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

