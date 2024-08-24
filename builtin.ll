; ModuleID = 'builtin.c'
source_filename = "builtin.c"
target datalayout = "e-m:e-p:32:32-i64:64-n32-S128"
target triple = "riscv32-unknown-unknown-elf"

@.str = private unnamed_addr constant [3 x i8] c"%s\00", align 1
@.str.1 = private unnamed_addr constant [4 x i8] c"%s\0A\00", align 1
@.str.2 = private unnamed_addr constant [3 x i8] c"%d\00", align 1
@.str.3 = private unnamed_addr constant [4 x i8] c"%d\0A\00", align 1
@.str.4 = private unnamed_addr constant [5 x i8] c"true\00", align 1
@.str.5 = private unnamed_addr constant [6 x i8] c"false\00", align 1

; Function Attrs: nounwind
define dso_local void @print(ptr noundef %0) local_unnamed_addr #0 {
  %2 = tail call i32 (ptr, ...) @printf(ptr noundef nonnull @.str, ptr noundef %0) #12
  ret void
}

declare dso_local i32 @printf(ptr noundef, ...) local_unnamed_addr #1

; Function Attrs: nounwind
define dso_local void @println(ptr noundef %0) local_unnamed_addr #0 {
  %2 = tail call i32 (ptr, ...) @printf(ptr noundef nonnull @.str.1, ptr noundef %0) #12
  ret void
}

; Function Attrs: nounwind
define dso_local void @printInt(i32 noundef %0) local_unnamed_addr #0 {
  %2 = tail call i32 (ptr, ...) @printf(ptr noundef nonnull @.str.2, i32 noundef %0) #12
  ret void
}

; Function Attrs: nounwind
define dso_local void @printlnInt(i32 noundef %0) local_unnamed_addr #0 {
  %2 = tail call i32 (ptr, ...) @printf(ptr noundef nonnull @.str.3, i32 noundef %0) #12
  ret void
}

; Function Attrs: nofree nounwind
define dso_local noundef ptr @getString() local_unnamed_addr #2 {
  %1 = tail call dereferenceable_or_null(1024) ptr @malloc(i32 noundef 1024) #13
  %2 = tail call i32 (ptr, ...) @scanf(ptr noundef nonnull @.str, ptr noundef %1) #14
  ret ptr %1
}

; Function Attrs: mustprogress nocallback nofree nosync nounwind willreturn memory(argmem: readwrite)
declare void @llvm.lifetime.start.p0(i64 immarg, ptr nocapture) #3

; Function Attrs: mustprogress nofree nounwind willreturn allockind("alloc,uninitialized") allocsize(0) memory(inaccessiblemem: readwrite)
declare dso_local noalias noundef ptr @malloc(i32 noundef) local_unnamed_addr #4

; Function Attrs: nofree nounwind
declare dso_local noundef i32 @scanf(ptr nocapture noundef readonly, ...) local_unnamed_addr #2

; Function Attrs: mustprogress nocallback nofree nosync nounwind willreturn memory(argmem: readwrite)
declare void @llvm.lifetime.end.p0(i64 immarg, ptr nocapture) #3

; Function Attrs: nofree nounwind
define dso_local i32 @getInt() local_unnamed_addr #2 {
  %1 = alloca i32, align 4
  call void @llvm.lifetime.start.p0(i64 4, ptr nonnull %1) #15
  %2 = call i32 (ptr, ...) @scanf(ptr noundef nonnull @.str.2, ptr noundef nonnull %1) #14
  %3 = load i32, ptr %1, align 4, !tbaa !6
  call void @llvm.lifetime.end.p0(i64 4, ptr nonnull %1) #15
  ret i32 %3
}

; Function Attrs: nofree nounwind
define dso_local noalias noundef ptr @toString(i32 noundef %0) local_unnamed_addr #2 {
  %2 = tail call dereferenceable_or_null(12) ptr @malloc(i32 noundef 12) #13
  %3 = tail call i32 (ptr, ptr, ...) @sprintf(ptr noundef nonnull dereferenceable(1) %2, ptr noundef nonnull dereferenceable(1) @.str.2, i32 noundef %0) #14
  ret ptr %2
}

; Function Attrs: nofree nounwind
declare dso_local noundef i32 @sprintf(ptr noalias nocapture noundef writeonly, ptr nocapture noundef readonly, ...) local_unnamed_addr #2

; Function Attrs: mustprogress nofree norecurse nosync nounwind willreturn memory(none)
define dso_local noundef nonnull ptr @bool_toString(i1 noundef zeroext %0) local_unnamed_addr #5 {
  %2 = select i1 %0, ptr @.str.4, ptr @.str.5
  ret ptr %2
}

; Function Attrs: mustprogress nofree nounwind willreturn memory(write, argmem: none, inaccessiblemem: readwrite)
define dso_local noalias nonnull ptr @alloca_helper(i32 noundef %0, i32 noundef %1) local_unnamed_addr #6 {
  %3 = mul nsw i32 %1, %0
  %4 = add nsw i32 %3, 4
  %5 = tail call ptr @malloc(i32 noundef %4) #13
  store i32 %1, ptr %5, align 4, !tbaa !6
  %6 = getelementptr inbounds i32, ptr %5, i32 1
  ret ptr %6
}

; Function Attrs: mustprogress nofree norecurse nosync nounwind willreturn memory(argmem: read)
define dso_local i32 @array.size(ptr nocapture noundef readonly %0) local_unnamed_addr #7 {
  %2 = getelementptr inbounds i32, ptr %0, i32 -1
  %3 = load i32, ptr %2, align 4, !tbaa !6
  ret i32 %3
}

; Function Attrs: nofree norecurse nosync nounwind memory(argmem: read)
define dso_local i32 @string.length(ptr nocapture noundef readonly %0) local_unnamed_addr #8 {
  br label %2

2:                                                ; preds = %2, %1
  %3 = phi i32 [ 0, %1 ], [ %7, %2 ]
  %4 = getelementptr inbounds i8, ptr %0, i32 %3
  %5 = load i8, ptr %4, align 1, !tbaa !10
  %6 = icmp eq i8 %5, 0
  %7 = add nuw nsw i32 %3, 1
  br i1 %6, label %8, label %2, !llvm.loop !11

8:                                                ; preds = %2
  ret i32 %3
}

; Function Attrs: nofree nounwind memory(write, argmem: read, inaccessiblemem: readwrite)
define dso_local noalias noundef ptr @string.substring(ptr nocapture noundef readonly %0, i32 noundef %1, i32 noundef %2) local_unnamed_addr #9 {
  %4 = sub nsw i32 %2, %1
  %5 = add nsw i32 %4, 1
  %6 = tail call ptr @malloc(i32 noundef %5) #13
  %7 = getelementptr i8, ptr %0, i32 %1
  %8 = icmp sgt i32 %4, 0
  br i1 %8, label %11, label %9

9:                                                ; preds = %11, %3
  %10 = getelementptr inbounds i8, ptr %6, i32 %4
  store i8 0, ptr %10, align 1, !tbaa !10
  ret ptr %6

11:                                               ; preds = %3, %11
  %12 = phi i32 [ %16, %11 ], [ 0, %3 ]
  %13 = getelementptr i8, ptr %7, i32 %12
  %14 = load i8, ptr %13, align 1, !tbaa !10
  %15 = getelementptr inbounds i8, ptr %6, i32 %12
  store i8 %14, ptr %15, align 1, !tbaa !10
  %16 = add nuw nsw i32 %12, 1
  %17 = icmp eq i32 %16, %4
  br i1 %17, label %9, label %11, !llvm.loop !13
}

; Function Attrs: nofree norecurse nosync nounwind memory(argmem: read)
define dso_local i32 @string.parseInt(ptr nocapture noundef readonly %0) local_unnamed_addr #8 {
  %2 = load i8, ptr %0, align 1, !tbaa !10
  switch i8 %2, label %22 [
    i8 45, label %3
    i8 0, label %34
  ]

3:                                                ; preds = %1
  %4 = getelementptr inbounds i8, ptr %0, i32 1
  %5 = load i8, ptr %4, align 1, !tbaa !10
  %6 = icmp eq i8 %5, 0
  br i1 %6, label %7, label %10

7:                                                ; preds = %10, %3
  %8 = phi i32 [ 0, %3 ], [ %17, %10 ]
  %9 = sub nsw i32 0, %8
  br label %34

10:                                               ; preds = %3, %10
  %11 = phi i8 [ %20, %10 ], [ %5, %3 ]
  %12 = phi i32 [ %18, %10 ], [ 1, %3 ]
  %13 = phi i32 [ %17, %10 ], [ 0, %3 ]
  %14 = zext i8 %11 to i32
  %15 = mul nsw i32 %13, 10
  %16 = add i32 %15, -48
  %17 = add i32 %16, %14
  %18 = add nuw nsw i32 %12, 1
  %19 = getelementptr inbounds i8, ptr %0, i32 %18
  %20 = load i8, ptr %19, align 1, !tbaa !10
  %21 = icmp eq i8 %20, 0
  br i1 %21, label %7, label %10, !llvm.loop !14

22:                                               ; preds = %1, %22
  %23 = phi i8 [ %32, %22 ], [ %2, %1 ]
  %24 = phi i32 [ %30, %22 ], [ 0, %1 ]
  %25 = phi i32 [ %29, %22 ], [ 0, %1 ]
  %26 = zext i8 %23 to i32
  %27 = mul nsw i32 %25, 10
  %28 = add i32 %27, -48
  %29 = add i32 %28, %26
  %30 = add nuw nsw i32 %24, 1
  %31 = getelementptr inbounds i8, ptr %0, i32 %30
  %32 = load i8, ptr %31, align 1, !tbaa !10
  %33 = icmp eq i8 %32, 0
  br i1 %33, label %34, label %22, !llvm.loop !15

34:                                               ; preds = %22, %1, %7
  %35 = phi i32 [ %9, %7 ], [ 0, %1 ], [ %29, %22 ]
  ret i32 %35
}

; Function Attrs: nofree norecurse nosync nounwind memory(argmem: read)
define dso_local i32 @string.compare(ptr nocapture noundef readonly %0, ptr nocapture noundef readonly %1) local_unnamed_addr #8 {
  %3 = load i8, ptr %0, align 1, !tbaa !10
  %4 = icmp eq i8 %3, 0
  br i1 %4, label %18, label %5

5:                                                ; preds = %2, %13
  %6 = phi i8 [ %16, %13 ], [ %3, %2 ]
  %7 = phi i32 [ %14, %13 ], [ 0, %2 ]
  %8 = getelementptr inbounds i8, ptr %1, i32 %7
  %9 = load i8, ptr %8, align 1, !tbaa !10
  %10 = icmp eq i8 %9, 0
  br i1 %10, label %18, label %11

11:                                               ; preds = %5
  %12 = icmp eq i8 %6, %9
  br i1 %12, label %13, label %23

13:                                               ; preds = %11
  %14 = add nuw nsw i32 %7, 1
  %15 = getelementptr inbounds i8, ptr %0, i32 %14
  %16 = load i8, ptr %15, align 1, !tbaa !10
  %17 = icmp eq i8 %16, 0
  br i1 %17, label %18, label %5, !llvm.loop !16

18:                                               ; preds = %5, %13, %2
  %19 = phi i32 [ 0, %2 ], [ %14, %13 ], [ %7, %5 ]
  %20 = phi i8 [ 0, %2 ], [ 0, %13 ], [ %6, %5 ]
  %21 = getelementptr inbounds i8, ptr %1, i32 %19
  %22 = load i8, ptr %21, align 1, !tbaa !10
  br label %23

23:                                               ; preds = %11, %18
  %24 = phi i8 [ %20, %18 ], [ %6, %11 ]
  %25 = phi i8 [ %22, %18 ], [ %9, %11 ]
  %26 = zext i8 %24 to i32
  %27 = zext i8 %25 to i32
  %28 = sub nsw i32 %26, %27
  ret i32 %28
}

; Function Attrs: nofree nounwind memory(write, argmem: read, inaccessiblemem: readwrite)
define dso_local noalias noundef ptr @string.concat(ptr nocapture noundef readonly %0, ptr nocapture noundef readonly %1) local_unnamed_addr #9 {
  br label %3

3:                                                ; preds = %3, %2
  %4 = phi i32 [ 0, %2 ], [ %8, %3 ]
  %5 = getelementptr inbounds i8, ptr %0, i32 %4
  %6 = load i8, ptr %5, align 1, !tbaa !10
  %7 = icmp eq i8 %6, 0
  %8 = add nuw nsw i32 %4, 1
  br i1 %7, label %9, label %3, !llvm.loop !11

9:                                                ; preds = %3, %9
  %10 = phi i32 [ %14, %9 ], [ 0, %3 ]
  %11 = getelementptr inbounds i8, ptr %1, i32 %10
  %12 = load i8, ptr %11, align 1, !tbaa !10
  %13 = icmp eq i8 %12, 0
  %14 = add nuw nsw i32 %10, 1
  br i1 %13, label %15, label %9, !llvm.loop !11

15:                                               ; preds = %9
  %16 = add nuw nsw i32 %10, %4
  %17 = add nuw nsw i32 %16, 1
  %18 = tail call ptr @malloc(i32 noundef %17) #13
  %19 = icmp eq i32 %4, 0
  br i1 %19, label %20, label %24

20:                                               ; preds = %24, %15
  %21 = icmp eq i32 %10, 0
  br i1 %21, label %31, label %22

22:                                               ; preds = %20
  %23 = getelementptr i8, ptr %18, i32 %4
  br label %33

24:                                               ; preds = %15, %24
  %25 = phi i32 [ %29, %24 ], [ 0, %15 ]
  %26 = getelementptr inbounds i8, ptr %0, i32 %25
  %27 = load i8, ptr %26, align 1, !tbaa !10
  %28 = getelementptr inbounds i8, ptr %18, i32 %25
  store i8 %27, ptr %28, align 1, !tbaa !10
  %29 = add nuw nsw i32 %25, 1
  %30 = icmp eq i32 %29, %4
  br i1 %30, label %20, label %24, !llvm.loop !17

31:                                               ; preds = %33, %20
  %32 = getelementptr inbounds i8, ptr %18, i32 %16
  store i8 0, ptr %32, align 1, !tbaa !10
  ret ptr %18

33:                                               ; preds = %22, %33
  %34 = phi i32 [ 0, %22 ], [ %38, %33 ]
  %35 = getelementptr inbounds i8, ptr %1, i32 %34
  %36 = load i8, ptr %35, align 1, !tbaa !10
  %37 = getelementptr i8, ptr %23, i32 %34
  store i8 %36, ptr %37, align 1, !tbaa !10
  %38 = add nuw nsw i32 %34, 1
  %39 = icmp eq i32 %38, %10
  br i1 %39, label %31, label %33, !llvm.loop !18
}

; Function Attrs: nofree nounwind memory(write, argmem: readwrite, inaccessiblemem: readwrite)
define dso_local void @string.copy(ptr nocapture noundef %0, ptr nocapture noundef readonly %1) local_unnamed_addr #10 {
  br label %3

3:                                                ; preds = %3, %2
  %4 = phi i32 [ 0, %2 ], [ %8, %3 ]
  %5 = getelementptr inbounds i8, ptr %1, i32 %4
  %6 = load i8, ptr %5, align 1, !tbaa !10
  %7 = icmp eq i8 %6, 0
  %8 = add nuw nsw i32 %4, 1
  br i1 %7, label %9, label %3, !llvm.loop !11

9:                                                ; preds = %3
  %10 = tail call ptr @malloc(i32 noundef %8) #13
  store ptr %10, ptr %0, align 4, !tbaa !19
  %11 = icmp eq i32 %4, 0
  br i1 %11, label %14, label %17

12:                                               ; preds = %17
  %13 = load ptr, ptr %0, align 4, !tbaa !19
  br label %14

14:                                               ; preds = %12, %9
  %15 = phi ptr [ %13, %12 ], [ %10, %9 ]
  %16 = getelementptr inbounds i8, ptr %15, i32 %4
  store i8 0, ptr %16, align 1, !tbaa !10
  ret void

17:                                               ; preds = %9, %17
  %18 = phi i32 [ %23, %17 ], [ 0, %9 ]
  %19 = getelementptr inbounds i8, ptr %1, i32 %18
  %20 = load i8, ptr %19, align 1, !tbaa !10
  %21 = load ptr, ptr %0, align 4, !tbaa !19
  %22 = getelementptr inbounds i8, ptr %21, i32 %18
  store i8 %20, ptr %22, align 1, !tbaa !10
  %23 = add nuw nsw i32 %18, 1
  %24 = icmp eq i32 %23, %4
  br i1 %24, label %12, label %17, !llvm.loop !21
}

; Function Attrs: mustprogress nofree norecurse nosync nounwind willreturn memory(argmem: read)
define dso_local i32 @__string.ord(ptr nocapture noundef readonly %0, i32 noundef %1) local_unnamed_addr #7 {
  %3 = getelementptr inbounds i8, ptr %0, i32 %1
  %4 = load i8, ptr %3, align 1, !tbaa !10
  %5 = zext i8 %4 to i32
  ret i32 %5
}

; Function Attrs: nofree nounwind memory(write, argmem: read, inaccessiblemem: readwrite)
define dso_local noalias nonnull ptr @array.alloca_inside(i32 noundef %0, i32 noundef %1, ptr nocapture noundef readonly %2, i32 noundef %3) local_unnamed_addr #9 {
  %5 = icmp eq i32 %1, 1
  br i1 %5, label %6, label %12

6:                                                ; preds = %4
  %7 = load i32, ptr %2, align 4, !tbaa !6
  %8 = mul nsw i32 %7, %0
  %9 = add nsw i32 %8, 4
  %10 = tail call ptr @malloc(i32 noundef %9) #13
  store i32 %7, ptr %10, align 4, !tbaa !6
  %11 = getelementptr inbounds i32, ptr %10, i32 1
  br label %32

12:                                               ; preds = %4
  %13 = icmp ne i32 %3, 1
  %14 = load i32, ptr %2, align 4, !tbaa !6
  %15 = shl nsw i32 %14, 2
  %16 = add nsw i32 %15, 4
  %17 = tail call ptr @malloc(i32 noundef %16) #13
  store i32 %14, ptr %17, align 4, !tbaa !6
  %18 = getelementptr inbounds i32, ptr %17, i32 1
  %19 = icmp sgt i32 %14, 0
  %20 = and i1 %13, %19
  br i1 %20, label %21, label %32

21:                                               ; preds = %12
  %22 = add nsw i32 %1, -1
  %23 = getelementptr inbounds i32, ptr %2, i32 1
  %24 = add nsw i32 %3, -1
  br label %25

25:                                               ; preds = %21, %25
  %26 = phi i32 [ 0, %21 ], [ %29, %25 ]
  %27 = tail call ptr @array.alloca_inside(i32 noundef %0, i32 noundef %22, ptr noundef nonnull %23, i32 noundef %24) #14
  %28 = getelementptr inbounds ptr, ptr %18, i32 %26
  store ptr %27, ptr %28, align 4, !tbaa !19
  %29 = add nuw nsw i32 %26, 1
  %30 = load i32, ptr %2, align 4, !tbaa !6
  %31 = icmp slt i32 %29, %30
  br i1 %31, label %25, label %32, !llvm.loop !22

32:                                               ; preds = %25, %12, %6
  %33 = phi ptr [ %11, %6 ], [ %18, %12 ], [ %18, %25 ]
  ret ptr %33
}

; Function Attrs: nofree nounwind
define dso_local noalias nonnull ptr @array.alloca(i32 noundef %0, i32 noundef %1, i32 noundef %2, ...) local_unnamed_addr #2 {
  %4 = alloca ptr, align 4
  call void @llvm.lifetime.start.p0(i64 4, ptr nonnull %4) #15
  %5 = shl i32 %2, 2
  %6 = tail call ptr @malloc(i32 noundef %5) #13
  call void @llvm.va_start(ptr nonnull %4)
  %7 = icmp sgt i32 %2, 0
  br i1 %7, label %8, label %10

8:                                                ; preds = %3
  %9 = load ptr, ptr %4, align 4
  br label %12

10:                                               ; preds = %12, %3
  call void @llvm.va_end(ptr nonnull %4)
  %11 = call ptr @array.alloca_inside(i32 noundef %0, i32 noundef %1, ptr noundef %6, i32 noundef %2) #14
  call void @llvm.lifetime.end.p0(i64 4, ptr nonnull %4) #15
  ret ptr %11

12:                                               ; preds = %8, %12
  %13 = phi i32 [ %18, %12 ], [ 0, %8 ]
  %14 = phi ptr [ %15, %12 ], [ %9, %8 ]
  %15 = getelementptr inbounds i8, ptr %14, i32 4
  store ptr %15, ptr %4, align 4
  %16 = load i32, ptr %14, align 4
  %17 = getelementptr inbounds i32, ptr %6, i32 %13
  store i32 %16, ptr %17, align 4, !tbaa !6
  %18 = add nuw nsw i32 %13, 1
  %19 = icmp eq i32 %18, %2
  br i1 %19, label %10, label %12, !llvm.loop !23
}

; Function Attrs: mustprogress nocallback nofree nosync nounwind willreturn
declare void @llvm.va_start(ptr) #11

; Function Attrs: mustprogress nocallback nofree nosync nounwind willreturn
declare void @llvm.va_end(ptr) #11

attributes #0 = { nounwind "no-builtin-memcpy" "no-builtin-printf" "no-trapping-math"="true" "stack-protector-buffer-size"="8" "target-cpu"="generic-rv32" "target-features"="+32bit,+a,+c,+m,+relax,-d,-e,-experimental-zacas,-experimental-zcmop,-experimental-zfbfmin,-experimental-zicfilp,-experimental-zicfiss,-experimental-zimop,-experimental-ztso,-experimental-zvfbfmin,-experimental-zvfbfwma,-f,-h,-smaia,-smepmp,-ssaia,-svinval,-svnapot,-svpbmt,-v,-xcvalu,-xcvbi,-xcvbitmanip,-xcvelw,-xcvmac,-xcvmem,-xcvsimd,-xsfvcp,-xsfvfnrclipxfqf,-xsfvfwmaccqqq,-xsfvqmaccdod,-xsfvqmaccqoq,-xtheadba,-xtheadbb,-xtheadbs,-xtheadcmo,-xtheadcondmov,-xtheadfmemidx,-xtheadmac,-xtheadmemidx,-xtheadmempair,-xtheadsync,-xtheadvdot,-xventanacondops,-za128rs,-za64rs,-zawrs,-zba,-zbb,-zbc,-zbkb,-zbkc,-zbkx,-zbs,-zca,-zcb,-zcd,-zce,-zcf,-zcmp,-zcmt,-zdinx,-zfa,-zfh,-zfhmin,-zfinx,-zhinx,-zhinxmin,-zic64b,-zicbom,-zicbop,-zicboz,-ziccamoa,-ziccif,-zicclsm,-ziccrse,-zicntr,-zicond,-zicsr,-zifencei,-zihintntl,-zihintpause,-zihpm,-zk,-zkn,-zknd,-zkne,-zknh,-zkr,-zks,-zksed,-zksh,-zkt,-zmmul,-zvbb,-zvbc,-zve32f,-zve32x,-zve64d,-zve64f,-zve64x,-zvfh,-zvfhmin,-zvkb,-zvkg,-zvkn,-zvknc,-zvkned,-zvkng,-zvknha,-zvknhb,-zvks,-zvksc,-zvksed,-zvksg,-zvksh,-zvkt,-zvl1024b,-zvl128b,-zvl16384b,-zvl2048b,-zvl256b,-zvl32768b,-zvl32b,-zvl4096b,-zvl512b,-zvl64b,-zvl65536b,-zvl8192b" }
attributes #1 = { "no-builtin-memcpy" "no-builtin-printf" "no-trapping-math"="true" "stack-protector-buffer-size"="8" "target-cpu"="generic-rv32" "target-features"="+32bit,+a,+c,+m,+relax,-d,-e,-experimental-zacas,-experimental-zcmop,-experimental-zfbfmin,-experimental-zicfilp,-experimental-zicfiss,-experimental-zimop,-experimental-ztso,-experimental-zvfbfmin,-experimental-zvfbfwma,-f,-h,-smaia,-smepmp,-ssaia,-svinval,-svnapot,-svpbmt,-v,-xcvalu,-xcvbi,-xcvbitmanip,-xcvelw,-xcvmac,-xcvmem,-xcvsimd,-xsfvcp,-xsfvfnrclipxfqf,-xsfvfwmaccqqq,-xsfvqmaccdod,-xsfvqmaccqoq,-xtheadba,-xtheadbb,-xtheadbs,-xtheadcmo,-xtheadcondmov,-xtheadfmemidx,-xtheadmac,-xtheadmemidx,-xtheadmempair,-xtheadsync,-xtheadvdot,-xventanacondops,-za128rs,-za64rs,-zawrs,-zba,-zbb,-zbc,-zbkb,-zbkc,-zbkx,-zbs,-zca,-zcb,-zcd,-zce,-zcf,-zcmp,-zcmt,-zdinx,-zfa,-zfh,-zfhmin,-zfinx,-zhinx,-zhinxmin,-zic64b,-zicbom,-zicbop,-zicboz,-ziccamoa,-ziccif,-zicclsm,-ziccrse,-zicntr,-zicond,-zicsr,-zifencei,-zihintntl,-zihintpause,-zihpm,-zk,-zkn,-zknd,-zkne,-zknh,-zkr,-zks,-zksed,-zksh,-zkt,-zmmul,-zvbb,-zvbc,-zve32f,-zve32x,-zve64d,-zve64f,-zve64x,-zvfh,-zvfhmin,-zvkb,-zvkg,-zvkn,-zvknc,-zvkned,-zvkng,-zvknha,-zvknhb,-zvks,-zvksc,-zvksed,-zvksg,-zvksh,-zvkt,-zvl1024b,-zvl128b,-zvl16384b,-zvl2048b,-zvl256b,-zvl32768b,-zvl32b,-zvl4096b,-zvl512b,-zvl64b,-zvl65536b,-zvl8192b" }
attributes #2 = { nofree nounwind "no-builtin-memcpy" "no-builtin-printf" "no-trapping-math"="true" "stack-protector-buffer-size"="8" "target-cpu"="generic-rv32" "target-features"="+32bit,+a,+c,+m,+relax,-d,-e,-experimental-zacas,-experimental-zcmop,-experimental-zfbfmin,-experimental-zicfilp,-experimental-zicfiss,-experimental-zimop,-experimental-ztso,-experimental-zvfbfmin,-experimental-zvfbfwma,-f,-h,-smaia,-smepmp,-ssaia,-svinval,-svnapot,-svpbmt,-v,-xcvalu,-xcvbi,-xcvbitmanip,-xcvelw,-xcvmac,-xcvmem,-xcvsimd,-xsfvcp,-xsfvfnrclipxfqf,-xsfvfwmaccqqq,-xsfvqmaccdod,-xsfvqmaccqoq,-xtheadba,-xtheadbb,-xtheadbs,-xtheadcmo,-xtheadcondmov,-xtheadfmemidx,-xtheadmac,-xtheadmemidx,-xtheadmempair,-xtheadsync,-xtheadvdot,-xventanacondops,-za128rs,-za64rs,-zawrs,-zba,-zbb,-zbc,-zbkb,-zbkc,-zbkx,-zbs,-zca,-zcb,-zcd,-zce,-zcf,-zcmp,-zcmt,-zdinx,-zfa,-zfh,-zfhmin,-zfinx,-zhinx,-zhinxmin,-zic64b,-zicbom,-zicbop,-zicboz,-ziccamoa,-ziccif,-zicclsm,-ziccrse,-zicntr,-zicond,-zicsr,-zifencei,-zihintntl,-zihintpause,-zihpm,-zk,-zkn,-zknd,-zkne,-zknh,-zkr,-zks,-zksed,-zksh,-zkt,-zmmul,-zvbb,-zvbc,-zve32f,-zve32x,-zve64d,-zve64f,-zve64x,-zvfh,-zvfhmin,-zvkb,-zvkg,-zvkn,-zvknc,-zvkned,-zvkng,-zvknha,-zvknhb,-zvks,-zvksc,-zvksed,-zvksg,-zvksh,-zvkt,-zvl1024b,-zvl128b,-zvl16384b,-zvl2048b,-zvl256b,-zvl32768b,-zvl32b,-zvl4096b,-zvl512b,-zvl64b,-zvl65536b,-zvl8192b" }
attributes #3 = { mustprogress nocallback nofree nosync nounwind willreturn memory(argmem: readwrite) }
attributes #4 = { mustprogress nofree nounwind willreturn allockind("alloc,uninitialized") allocsize(0) memory(inaccessiblemem: readwrite) "alloc-family"="malloc" "no-builtin-memcpy" "no-builtin-printf" "no-trapping-math"="true" "stack-protector-buffer-size"="8" "target-cpu"="generic-rv32" "target-features"="+32bit,+a,+c,+m,+relax,-d,-e,-experimental-zacas,-experimental-zcmop,-experimental-zfbfmin,-experimental-zicfilp,-experimental-zicfiss,-experimental-zimop,-experimental-ztso,-experimental-zvfbfmin,-experimental-zvfbfwma,-f,-h,-smaia,-smepmp,-ssaia,-svinval,-svnapot,-svpbmt,-v,-xcvalu,-xcvbi,-xcvbitmanip,-xcvelw,-xcvmac,-xcvmem,-xcvsimd,-xsfvcp,-xsfvfnrclipxfqf,-xsfvfwmaccqqq,-xsfvqmaccdod,-xsfvqmaccqoq,-xtheadba,-xtheadbb,-xtheadbs,-xtheadcmo,-xtheadcondmov,-xtheadfmemidx,-xtheadmac,-xtheadmemidx,-xtheadmempair,-xtheadsync,-xtheadvdot,-xventanacondops,-za128rs,-za64rs,-zawrs,-zba,-zbb,-zbc,-zbkb,-zbkc,-zbkx,-zbs,-zca,-zcb,-zcd,-zce,-zcf,-zcmp,-zcmt,-zdinx,-zfa,-zfh,-zfhmin,-zfinx,-zhinx,-zhinxmin,-zic64b,-zicbom,-zicbop,-zicboz,-ziccamoa,-ziccif,-zicclsm,-ziccrse,-zicntr,-zicond,-zicsr,-zifencei,-zihintntl,-zihintpause,-zihpm,-zk,-zkn,-zknd,-zkne,-zknh,-zkr,-zks,-zksed,-zksh,-zkt,-zmmul,-zvbb,-zvbc,-zve32f,-zve32x,-zve64d,-zve64f,-zve64x,-zvfh,-zvfhmin,-zvkb,-zvkg,-zvkn,-zvknc,-zvkned,-zvkng,-zvknha,-zvknhb,-zvks,-zvksc,-zvksed,-zvksg,-zvksh,-zvkt,-zvl1024b,-zvl128b,-zvl16384b,-zvl2048b,-zvl256b,-zvl32768b,-zvl32b,-zvl4096b,-zvl512b,-zvl64b,-zvl65536b,-zvl8192b" }
attributes #5 = { mustprogress nofree norecurse nosync nounwind willreturn memory(none) "no-builtin-memcpy" "no-builtin-printf" "no-trapping-math"="true" "stack-protector-buffer-size"="8" "target-cpu"="generic-rv32" "target-features"="+32bit,+a,+c,+m,+relax,-d,-e,-experimental-zacas,-experimental-zcmop,-experimental-zfbfmin,-experimental-zicfilp,-experimental-zicfiss,-experimental-zimop,-experimental-ztso,-experimental-zvfbfmin,-experimental-zvfbfwma,-f,-h,-smaia,-smepmp,-ssaia,-svinval,-svnapot,-svpbmt,-v,-xcvalu,-xcvbi,-xcvbitmanip,-xcvelw,-xcvmac,-xcvmem,-xcvsimd,-xsfvcp,-xsfvfnrclipxfqf,-xsfvfwmaccqqq,-xsfvqmaccdod,-xsfvqmaccqoq,-xtheadba,-xtheadbb,-xtheadbs,-xtheadcmo,-xtheadcondmov,-xtheadfmemidx,-xtheadmac,-xtheadmemidx,-xtheadmempair,-xtheadsync,-xtheadvdot,-xventanacondops,-za128rs,-za64rs,-zawrs,-zba,-zbb,-zbc,-zbkb,-zbkc,-zbkx,-zbs,-zca,-zcb,-zcd,-zce,-zcf,-zcmp,-zcmt,-zdinx,-zfa,-zfh,-zfhmin,-zfinx,-zhinx,-zhinxmin,-zic64b,-zicbom,-zicbop,-zicboz,-ziccamoa,-ziccif,-zicclsm,-ziccrse,-zicntr,-zicond,-zicsr,-zifencei,-zihintntl,-zihintpause,-zihpm,-zk,-zkn,-zknd,-zkne,-zknh,-zkr,-zks,-zksed,-zksh,-zkt,-zmmul,-zvbb,-zvbc,-zve32f,-zve32x,-zve64d,-zve64f,-zve64x,-zvfh,-zvfhmin,-zvkb,-zvkg,-zvkn,-zvknc,-zvkned,-zvkng,-zvknha,-zvknhb,-zvks,-zvksc,-zvksed,-zvksg,-zvksh,-zvkt,-zvl1024b,-zvl128b,-zvl16384b,-zvl2048b,-zvl256b,-zvl32768b,-zvl32b,-zvl4096b,-zvl512b,-zvl64b,-zvl65536b,-zvl8192b" }
attributes #6 = { mustprogress nofree nounwind willreturn memory(write, argmem: none, inaccessiblemem: readwrite) "no-builtin-memcpy" "no-builtin-printf" "no-trapping-math"="true" "stack-protector-buffer-size"="8" "target-cpu"="generic-rv32" "target-features"="+32bit,+a,+c,+m,+relax,-d,-e,-experimental-zacas,-experimental-zcmop,-experimental-zfbfmin,-experimental-zicfilp,-experimental-zicfiss,-experimental-zimop,-experimental-ztso,-experimental-zvfbfmin,-experimental-zvfbfwma,-f,-h,-smaia,-smepmp,-ssaia,-svinval,-svnapot,-svpbmt,-v,-xcvalu,-xcvbi,-xcvbitmanip,-xcvelw,-xcvmac,-xcvmem,-xcvsimd,-xsfvcp,-xsfvfnrclipxfqf,-xsfvfwmaccqqq,-xsfvqmaccdod,-xsfvqmaccqoq,-xtheadba,-xtheadbb,-xtheadbs,-xtheadcmo,-xtheadcondmov,-xtheadfmemidx,-xtheadmac,-xtheadmemidx,-xtheadmempair,-xtheadsync,-xtheadvdot,-xventanacondops,-za128rs,-za64rs,-zawrs,-zba,-zbb,-zbc,-zbkb,-zbkc,-zbkx,-zbs,-zca,-zcb,-zcd,-zce,-zcf,-zcmp,-zcmt,-zdinx,-zfa,-zfh,-zfhmin,-zfinx,-zhinx,-zhinxmin,-zic64b,-zicbom,-zicbop,-zicboz,-ziccamoa,-ziccif,-zicclsm,-ziccrse,-zicntr,-zicond,-zicsr,-zifencei,-zihintntl,-zihintpause,-zihpm,-zk,-zkn,-zknd,-zkne,-zknh,-zkr,-zks,-zksed,-zksh,-zkt,-zmmul,-zvbb,-zvbc,-zve32f,-zve32x,-zve64d,-zve64f,-zve64x,-zvfh,-zvfhmin,-zvkb,-zvkg,-zvkn,-zvknc,-zvkned,-zvkng,-zvknha,-zvknhb,-zvks,-zvksc,-zvksed,-zvksg,-zvksh,-zvkt,-zvl1024b,-zvl128b,-zvl16384b,-zvl2048b,-zvl256b,-zvl32768b,-zvl32b,-zvl4096b,-zvl512b,-zvl64b,-zvl65536b,-zvl8192b" }
attributes #7 = { mustprogress nofree norecurse nosync nounwind willreturn memory(argmem: read) "no-builtin-memcpy" "no-builtin-printf" "no-trapping-math"="true" "stack-protector-buffer-size"="8" "target-cpu"="generic-rv32" "target-features"="+32bit,+a,+c,+m,+relax,-d,-e,-experimental-zacas,-experimental-zcmop,-experimental-zfbfmin,-experimental-zicfilp,-experimental-zicfiss,-experimental-zimop,-experimental-ztso,-experimental-zvfbfmin,-experimental-zvfbfwma,-f,-h,-smaia,-smepmp,-ssaia,-svinval,-svnapot,-svpbmt,-v,-xcvalu,-xcvbi,-xcvbitmanip,-xcvelw,-xcvmac,-xcvmem,-xcvsimd,-xsfvcp,-xsfvfnrclipxfqf,-xsfvfwmaccqqq,-xsfvqmaccdod,-xsfvqmaccqoq,-xtheadba,-xtheadbb,-xtheadbs,-xtheadcmo,-xtheadcondmov,-xtheadfmemidx,-xtheadmac,-xtheadmemidx,-xtheadmempair,-xtheadsync,-xtheadvdot,-xventanacondops,-za128rs,-za64rs,-zawrs,-zba,-zbb,-zbc,-zbkb,-zbkc,-zbkx,-zbs,-zca,-zcb,-zcd,-zce,-zcf,-zcmp,-zcmt,-zdinx,-zfa,-zfh,-zfhmin,-zfinx,-zhinx,-zhinxmin,-zic64b,-zicbom,-zicbop,-zicboz,-ziccamoa,-ziccif,-zicclsm,-ziccrse,-zicntr,-zicond,-zicsr,-zifencei,-zihintntl,-zihintpause,-zihpm,-zk,-zkn,-zknd,-zkne,-zknh,-zkr,-zks,-zksed,-zksh,-zkt,-zmmul,-zvbb,-zvbc,-zve32f,-zve32x,-zve64d,-zve64f,-zve64x,-zvfh,-zvfhmin,-zvkb,-zvkg,-zvkn,-zvknc,-zvkned,-zvkng,-zvknha,-zvknhb,-zvks,-zvksc,-zvksed,-zvksg,-zvksh,-zvkt,-zvl1024b,-zvl128b,-zvl16384b,-zvl2048b,-zvl256b,-zvl32768b,-zvl32b,-zvl4096b,-zvl512b,-zvl64b,-zvl65536b,-zvl8192b" }
attributes #8 = { nofree norecurse nosync nounwind memory(argmem: read) "no-builtin-memcpy" "no-builtin-printf" "no-trapping-math"="true" "stack-protector-buffer-size"="8" "target-cpu"="generic-rv32" "target-features"="+32bit,+a,+c,+m,+relax,-d,-e,-experimental-zacas,-experimental-zcmop,-experimental-zfbfmin,-experimental-zicfilp,-experimental-zicfiss,-experimental-zimop,-experimental-ztso,-experimental-zvfbfmin,-experimental-zvfbfwma,-f,-h,-smaia,-smepmp,-ssaia,-svinval,-svnapot,-svpbmt,-v,-xcvalu,-xcvbi,-xcvbitmanip,-xcvelw,-xcvmac,-xcvmem,-xcvsimd,-xsfvcp,-xsfvfnrclipxfqf,-xsfvfwmaccqqq,-xsfvqmaccdod,-xsfvqmaccqoq,-xtheadba,-xtheadbb,-xtheadbs,-xtheadcmo,-xtheadcondmov,-xtheadfmemidx,-xtheadmac,-xtheadmemidx,-xtheadmempair,-xtheadsync,-xtheadvdot,-xventanacondops,-za128rs,-za64rs,-zawrs,-zba,-zbb,-zbc,-zbkb,-zbkc,-zbkx,-zbs,-zca,-zcb,-zcd,-zce,-zcf,-zcmp,-zcmt,-zdinx,-zfa,-zfh,-zfhmin,-zfinx,-zhinx,-zhinxmin,-zic64b,-zicbom,-zicbop,-zicboz,-ziccamoa,-ziccif,-zicclsm,-ziccrse,-zicntr,-zicond,-zicsr,-zifencei,-zihintntl,-zihintpause,-zihpm,-zk,-zkn,-zknd,-zkne,-zknh,-zkr,-zks,-zksed,-zksh,-zkt,-zmmul,-zvbb,-zvbc,-zve32f,-zve32x,-zve64d,-zve64f,-zve64x,-zvfh,-zvfhmin,-zvkb,-zvkg,-zvkn,-zvknc,-zvkned,-zvkng,-zvknha,-zvknhb,-zvks,-zvksc,-zvksed,-zvksg,-zvksh,-zvkt,-zvl1024b,-zvl128b,-zvl16384b,-zvl2048b,-zvl256b,-zvl32768b,-zvl32b,-zvl4096b,-zvl512b,-zvl64b,-zvl65536b,-zvl8192b" }
attributes #9 = { nofree nounwind memory(write, argmem: read, inaccessiblemem: readwrite) "no-builtin-memcpy" "no-builtin-printf" "no-trapping-math"="true" "stack-protector-buffer-size"="8" "target-cpu"="generic-rv32" "target-features"="+32bit,+a,+c,+m,+relax,-d,-e,-experimental-zacas,-experimental-zcmop,-experimental-zfbfmin,-experimental-zicfilp,-experimental-zicfiss,-experimental-zimop,-experimental-ztso,-experimental-zvfbfmin,-experimental-zvfbfwma,-f,-h,-smaia,-smepmp,-ssaia,-svinval,-svnapot,-svpbmt,-v,-xcvalu,-xcvbi,-xcvbitmanip,-xcvelw,-xcvmac,-xcvmem,-xcvsimd,-xsfvcp,-xsfvfnrclipxfqf,-xsfvfwmaccqqq,-xsfvqmaccdod,-xsfvqmaccqoq,-xtheadba,-xtheadbb,-xtheadbs,-xtheadcmo,-xtheadcondmov,-xtheadfmemidx,-xtheadmac,-xtheadmemidx,-xtheadmempair,-xtheadsync,-xtheadvdot,-xventanacondops,-za128rs,-za64rs,-zawrs,-zba,-zbb,-zbc,-zbkb,-zbkc,-zbkx,-zbs,-zca,-zcb,-zcd,-zce,-zcf,-zcmp,-zcmt,-zdinx,-zfa,-zfh,-zfhmin,-zfinx,-zhinx,-zhinxmin,-zic64b,-zicbom,-zicbop,-zicboz,-ziccamoa,-ziccif,-zicclsm,-ziccrse,-zicntr,-zicond,-zicsr,-zifencei,-zihintntl,-zihintpause,-zihpm,-zk,-zkn,-zknd,-zkne,-zknh,-zkr,-zks,-zksed,-zksh,-zkt,-zmmul,-zvbb,-zvbc,-zve32f,-zve32x,-zve64d,-zve64f,-zve64x,-zvfh,-zvfhmin,-zvkb,-zvkg,-zvkn,-zvknc,-zvkned,-zvkng,-zvknha,-zvknhb,-zvks,-zvksc,-zvksed,-zvksg,-zvksh,-zvkt,-zvl1024b,-zvl128b,-zvl16384b,-zvl2048b,-zvl256b,-zvl32768b,-zvl32b,-zvl4096b,-zvl512b,-zvl64b,-zvl65536b,-zvl8192b" }
attributes #10 = { nofree nounwind memory(write, argmem: readwrite, inaccessiblemem: readwrite) "no-builtin-memcpy" "no-builtin-printf" "no-trapping-math"="true" "stack-protector-buffer-size"="8" "target-cpu"="generic-rv32" "target-features"="+32bit,+a,+c,+m,+relax,-d,-e,-experimental-zacas,-experimental-zcmop,-experimental-zfbfmin,-experimental-zicfilp,-experimental-zicfiss,-experimental-zimop,-experimental-ztso,-experimental-zvfbfmin,-experimental-zvfbfwma,-f,-h,-smaia,-smepmp,-ssaia,-svinval,-svnapot,-svpbmt,-v,-xcvalu,-xcvbi,-xcvbitmanip,-xcvelw,-xcvmac,-xcvmem,-xcvsimd,-xsfvcp,-xsfvfnrclipxfqf,-xsfvfwmaccqqq,-xsfvqmaccdod,-xsfvqmaccqoq,-xtheadba,-xtheadbb,-xtheadbs,-xtheadcmo,-xtheadcondmov,-xtheadfmemidx,-xtheadmac,-xtheadmemidx,-xtheadmempair,-xtheadsync,-xtheadvdot,-xventanacondops,-za128rs,-za64rs,-zawrs,-zba,-zbb,-zbc,-zbkb,-zbkc,-zbkx,-zbs,-zca,-zcb,-zcd,-zce,-zcf,-zcmp,-zcmt,-zdinx,-zfa,-zfh,-zfhmin,-zfinx,-zhinx,-zhinxmin,-zic64b,-zicbom,-zicbop,-zicboz,-ziccamoa,-ziccif,-zicclsm,-ziccrse,-zicntr,-zicond,-zicsr,-zifencei,-zihintntl,-zihintpause,-zihpm,-zk,-zkn,-zknd,-zkne,-zknh,-zkr,-zks,-zksed,-zksh,-zkt,-zmmul,-zvbb,-zvbc,-zve32f,-zve32x,-zve64d,-zve64f,-zve64x,-zvfh,-zvfhmin,-zvkb,-zvkg,-zvkn,-zvknc,-zvkned,-zvkng,-zvknha,-zvknhb,-zvks,-zvksc,-zvksed,-zvksg,-zvksh,-zvkt,-zvl1024b,-zvl128b,-zvl16384b,-zvl2048b,-zvl256b,-zvl32768b,-zvl32b,-zvl4096b,-zvl512b,-zvl64b,-zvl65536b,-zvl8192b" }
attributes #11 = { mustprogress nocallback nofree nosync nounwind willreturn }
attributes #12 = { nobuiltin nounwind "no-builtin-memcpy" "no-builtin-printf" }
attributes #13 = { allocsize(0) "no-builtin-memcpy" "no-builtin-printf" }
attributes #14 = { "no-builtin-memcpy" "no-builtin-printf" }
attributes #15 = { nounwind }

!llvm.module.flags = !{!0, !1, !2, !4}
!llvm.ident = !{!5}

!0 = !{i32 1, !"wchar_size", i32 4}
!1 = !{i32 1, !"target-abi", !"ilp32"}
!2 = !{i32 6, !"riscv-isa", !3}
!3 = !{!"rv32i2p1_m2p0_a2p1_c2p0"}
!4 = !{i32 8, !"SmallDataLimit", i32 8}
!5 = !{!"clang version 18.1.8"}
!6 = !{!7, !7, i64 0}
!7 = !{!"int", !8, i64 0}
!8 = !{!"omnipotent char", !9, i64 0}
!9 = !{!"Simple C/C++ TBAA"}
!10 = !{!8, !8, i64 0}
!11 = distinct !{!11, !12}
!12 = !{!"llvm.loop.mustprogress"}
!13 = distinct !{!13, !12}
!14 = distinct !{!14, !12}
!15 = distinct !{!15, !12}
!16 = distinct !{!16, !12}
!17 = distinct !{!17, !12}
!18 = distinct !{!18, !12}
!19 = !{!20, !20, i64 0}
!20 = !{!"any pointer", !8, i64 0}
!21 = distinct !{!21, !12}
!22 = distinct !{!22, !12}
!23 = distinct !{!23, !12}
