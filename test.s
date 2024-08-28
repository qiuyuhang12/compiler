	.section .text
	.globl main
main:
	addi sp, sp, -284
	sw t0, 8(sp)
	sw t1, 12(sp)
	sw t2, 16(sp)
	# %ret.val = alloca i32
	addi t0, sp, 60
	sw t0, 56(sp)
	# %a = alloca i32
	addi t0, sp, 68
	sw t0, 64(sp)
	# %b = alloca i32
	addi t0, sp, 80
	sw t0, 76(sp)
	# %c = alloca i32
	addi t0, sp, 92
	sw t0, 88(sp)
	# %d = alloca i32
	addi t0, sp, 104
	sw t0, 100(sp)
	# %2 = alloca i32
	addi t0, sp, 128
	sw t0, 124(sp)
	# %5 = alloca i32
	addi t0, sp, 152
	sw t0, 148(sp)
	# %10 = alloca i32
	addi t0, sp, 196
	sw t0, 192(sp)
	# %13 = alloca i32
	addi t0, sp, 224
	sw t0, 220(sp)
	# call void @_.init()
	sw ra, 20(sp)
	sw a0, 24(sp)
	sw a1, 28(sp)
	sw a2, 32(sp)
	sw a3, 36(sp)
	sw a4, 40(sp)
	sw a5, 44(sp)
	sw a6, 48(sp)
	sw a7, 52(sp)
	call _.init
	lw ra, 20(sp)
	lw a0, 24(sp)
	lw a1, 28(sp)
	lw a2, 32(sp)
	lw a3, 36(sp)
	lw a4, 40(sp)
	lw a5, 44(sp)
	lw a6, 48(sp)
	lw a7, 52(sp)
	# %getInt.ret = call i32 @getInt()
	sw ra, 20(sp)
	sw a0, 24(sp)
	sw a1, 28(sp)
	sw a2, 32(sp)
	sw a3, 36(sp)
	sw a4, 40(sp)
	sw a5, 44(sp)
	sw a6, 48(sp)
	sw a7, 52(sp)
	call getInt
	lw ra, 20(sp)
	sw a0, 72(sp)
	lw a0, 24(sp)
	lw a1, 28(sp)
	lw a2, 32(sp)
	lw a3, 36(sp)
	lw a4, 40(sp)
	lw a5, 44(sp)
	lw a6, 48(sp)
	lw a7, 52(sp)
	# store i32 %getInt.ret, ptr %a
	lw t0, 72(sp)
	lw t1, 64(sp)
	sw t0, 0(t1)
	# %getInt.ret.1 = call i32 @getInt()
	sw ra, 20(sp)
	sw a0, 24(sp)
	sw a1, 28(sp)
	sw a2, 32(sp)
	sw a3, 36(sp)
	sw a4, 40(sp)
	sw a5, 44(sp)
	sw a6, 48(sp)
	sw a7, 52(sp)
	call getInt
	lw ra, 20(sp)
	sw a0, 84(sp)
	lw a0, 24(sp)
	lw a1, 28(sp)
	lw a2, 32(sp)
	lw a3, 36(sp)
	lw a4, 40(sp)
	lw a5, 44(sp)
	lw a6, 48(sp)
	lw a7, 52(sp)
	# store i32 %getInt.ret.1, ptr %b
	lw t0, 84(sp)
	lw t1, 76(sp)
	sw t0, 0(t1)
	# %getInt.ret.2 = call i32 @getInt()
	sw ra, 20(sp)
	sw a0, 24(sp)
	sw a1, 28(sp)
	sw a2, 32(sp)
	sw a3, 36(sp)
	sw a4, 40(sp)
	sw a5, 44(sp)
	sw a6, 48(sp)
	sw a7, 52(sp)
	call getInt
	lw ra, 20(sp)
	sw a0, 96(sp)
	lw a0, 24(sp)
	lw a1, 28(sp)
	lw a2, 32(sp)
	lw a3, 36(sp)
	lw a4, 40(sp)
	lw a5, 44(sp)
	lw a6, 48(sp)
	lw a7, 52(sp)
	# store i32 %getInt.ret.2, ptr %c
	lw t0, 96(sp)
	lw t1, 88(sp)
	sw t0, 0(t1)
	# %0 = load i32, ptr %a
	lw t0, 64(sp)
	lw t0, 0(t0)
	sw t0, 108(sp)
	# %Inc = add i32 %0, 1
	lw t0, 108(sp)
	li t1, 1
	add t2, t0, t1
	sw t2, 112(sp)
	# store i32 %Inc, ptr %a
	lw t0, 112(sp)
	lw t1, 64(sp)
	sw t0, 0(t1)
	# %1 = load i32, ptr %b
	lw t0, 76(sp)
	lw t0, 0(t0)
	sw t0, 116(sp)
	# %eq = icmp eq i32 %0, %1
	lw t0, 108(sp)
	lw t1, 116(sp)
	sub t2, t0, t1
	seqz t2, t2
	sw t2, 120(sp)
	# br i1 %eq, label %cond.true, label %cond.false
	lw t0, 120(sp)
	bne t0, x0, .+8
	j main_cond.false
	j main_cond.true
	.section .text
	.globl main_cond.true
main_cond.true:
	# %3 = load i32, ptr %a
	lw t0, 64(sp)
	lw t0, 0(t0)
	sw t0, 132(sp)
	# %Inc.1 = add i32 %3, 1
	lw t0, 132(sp)
	li t1, 1
	add t2, t0, t1
	sw t2, 136(sp)
	# store i32 %Inc.1, ptr %a
	lw t0, 136(sp)
	lw t1, 64(sp)
	sw t0, 0(t1)
	# %4 = load i32, ptr %b
	lw t0, 76(sp)
	lw t0, 0(t0)
	sw t0, 140(sp)
	# %eq.1 = icmp eq i32 %3, %4
	lw t0, 132(sp)
	lw t1, 140(sp)
	sub t2, t0, t1
	seqz t2, t2
	sw t2, 144(sp)
	# br i1 %eq.1, label %cond.true.1, label %cond.false.1
	lw t0, 144(sp)
	bne t0, x0, .+8
	j main_cond.false.1
	j main_cond.true.1
	.section .text
	.globl main_cond.true.1
main_cond.true.1:
	# %6 = load i32, ptr %c
	lw t0, 88(sp)
	lw t0, 0(t0)
	sw t0, 156(sp)
	# %pre.Inc = add i32 %6, 1
	lw t0, 156(sp)
	li t1, 1
	add t2, t0, t1
	sw t2, 160(sp)
	# store i32 %pre.Inc, ptr %c
	lw t0, 160(sp)
	lw t1, 88(sp)
	sw t0, 0(t1)
	# store i32 %pre.Inc, ptr %5
	lw t0, 160(sp)
	lw t1, 148(sp)
	sw t0, 0(t1)
	# br label %cond.end.1
	j main_cond.end.1
	.section .text
	.globl main_cond.false.1
main_cond.false.1:
	# %7 = load i32, ptr %a
	lw t0, 64(sp)
	lw t0, 0(t0)
	sw t0, 164(sp)
	# %pre.Inc.1 = add i32 %7, 1
	lw t0, 164(sp)
	li t1, 1
	add t2, t0, t1
	sw t2, 168(sp)
	# store i32 %pre.Inc.1, ptr %a
	lw t0, 168(sp)
	lw t1, 64(sp)
	sw t0, 0(t1)
	# store i32 %pre.Inc.1, ptr %5
	lw t0, 168(sp)
	lw t1, 148(sp)
	sw t0, 0(t1)
	# br label %cond.end.1
	j main_cond.end.1
	.section .text
	.globl main_cond.end.1
main_cond.end.1:
	# %cond = load i32, ptr %5
	lw t0, 148(sp)
	lw t0, 0(t0)
	sw t0, 172(sp)
	# store i32 %cond, ptr %2
	lw t0, 172(sp)
	lw t1, 124(sp)
	sw t0, 0(t1)
	# br label %cond.end
	j main_cond.end
	.section .text
	.globl main_cond.false
main_cond.false:
	# %8 = load i32, ptr %a
	lw t0, 64(sp)
	lw t0, 0(t0)
	sw t0, 176(sp)
	# %Inc.2 = add i32 %8, 1
	lw t0, 176(sp)
	li t1, 1
	add t2, t0, t1
	sw t2, 180(sp)
	# store i32 %Inc.2, ptr %a
	lw t0, 180(sp)
	lw t1, 64(sp)
	sw t0, 0(t1)
	# %9 = load i32, ptr %b
	lw t0, 76(sp)
	lw t0, 0(t0)
	sw t0, 184(sp)
	# %eq.2 = icmp eq i32 %8, %9
	lw t0, 176(sp)
	lw t1, 184(sp)
	sub t2, t0, t1
	seqz t2, t2
	sw t2, 188(sp)
	# br i1 %eq.2, label %cond.true.2, label %cond.false.2
	lw t0, 188(sp)
	bne t0, x0, .+8
	j main_cond.false.2
	j main_cond.true.2
	.section .text
	.globl main_cond.true.2
main_cond.true.2:
	# %11 = load i32, ptr %b
	lw t0, 76(sp)
	lw t0, 0(t0)
	sw t0, 200(sp)
	# %pre.Inc.2 = add i32 %11, 1
	lw t0, 200(sp)
	li t1, 1
	add t2, t0, t1
	sw t2, 204(sp)
	# store i32 %pre.Inc.2, ptr %b
	lw t0, 204(sp)
	lw t1, 76(sp)
	sw t0, 0(t1)
	# %12 = load i32, ptr %c
	lw t0, 88(sp)
	lw t0, 0(t0)
	sw t0, 208(sp)
	# %Inc.3 = add i32 %12, 1
	lw t0, 208(sp)
	li t1, 1
	add t2, t0, t1
	sw t2, 212(sp)
	# store i32 %Inc.3, ptr %c
	lw t0, 212(sp)
	lw t1, 88(sp)
	sw t0, 0(t1)
	# %eq.3 = icmp eq i32 %pre.Inc.2, %12
	lw t0, 204(sp)
	lw t1, 208(sp)
	sub t2, t0, t1
	seqz t2, t2
	sw t2, 216(sp)
	# br i1 %eq.3, label %cond.true.3, label %cond.false.3
	lw t0, 216(sp)
	bne t0, x0, .+8
	j main_cond.false.3
	j main_cond.true.3
	.section .text
	.globl main_cond.true.3
main_cond.true.3:
	# %14 = load i32, ptr %b
	lw t0, 76(sp)
	lw t0, 0(t0)
	sw t0, 228(sp)
	# %pre.Inc.3 = add i32 %14, 1
	lw t0, 228(sp)
	li t1, 1
	add t2, t0, t1
	sw t2, 232(sp)
	# store i32 %pre.Inc.3, ptr %b
	lw t0, 232(sp)
	lw t1, 76(sp)
	sw t0, 0(t1)
	# store i32 %pre.Inc.3, ptr %13
	lw t0, 232(sp)
	lw t1, 220(sp)
	sw t0, 0(t1)
	# br label %cond.end.3
	j main_cond.end.3
	.section .text
	.globl main_cond.false.3
main_cond.false.3:
	# %15 = load i32, ptr %c
	lw t0, 88(sp)
	lw t0, 0(t0)
	sw t0, 236(sp)
	# %pre.Inc.4 = add i32 %15, 1
	lw t0, 236(sp)
	li t1, 1
	add t2, t0, t1
	sw t2, 240(sp)
	# store i32 %pre.Inc.4, ptr %c
	lw t0, 240(sp)
	lw t1, 88(sp)
	sw t0, 0(t1)
	# store i32 %pre.Inc.4, ptr %13
	lw t0, 240(sp)
	lw t1, 220(sp)
	sw t0, 0(t1)
	# br label %cond.end.3
	j main_cond.end.3
	.section .text
	.globl main_cond.end.3
main_cond.end.3:
	# %cond.1 = load i32, ptr %13
	lw t0, 220(sp)
	lw t0, 0(t0)
	sw t0, 244(sp)
	# store i32 %cond.1, ptr %10
	lw t0, 244(sp)
	lw t1, 192(sp)
	sw t0, 0(t1)
	# br label %cond.end.2
	j main_cond.end.2
	.section .text
	.globl main_cond.false.2
main_cond.false.2:
	# %16 = load i32, ptr %a
	lw t0, 64(sp)
	lw t0, 0(t0)
	sw t0, 248(sp)
	# %pre.Inc.5 = add i32 %16, 1
	lw t0, 248(sp)
	li t1, 1
	add t2, t0, t1
	sw t2, 252(sp)
	# store i32 %pre.Inc.5, ptr %a
	lw t0, 252(sp)
	lw t1, 64(sp)
	sw t0, 0(t1)
	# store i32 %pre.Inc.5, ptr %10
	lw t0, 252(sp)
	lw t1, 192(sp)
	sw t0, 0(t1)
	# br label %cond.end.2
	j main_cond.end.2
	.section .text
	.globl main_cond.end.2
main_cond.end.2:
	# %cond.2 = load i32, ptr %10
	lw t0, 192(sp)
	lw t0, 0(t0)
	sw t0, 256(sp)
	# store i32 %cond.2, ptr %2
	lw t0, 256(sp)
	lw t1, 124(sp)
	sw t0, 0(t1)
	# br label %cond.end
	j main_cond.end
	.section .text
	.globl main_cond.end
main_cond.end:
	# %cond.3 = load i32, ptr %2
	lw t0, 124(sp)
	lw t0, 0(t0)
	sw t0, 260(sp)
	# store i32 %cond.3, ptr %d
	lw t0, 260(sp)
	lw t1, 100(sp)
	sw t0, 0(t1)
	# %17 = load i32, ptr %a
	lw t0, 64(sp)
	lw t0, 0(t0)
	sw t0, 264(sp)
	# call void @printlnInt(i32 %17)
	sw ra, 20(sp)
	sw a0, 24(sp)
	sw a1, 28(sp)
	sw a2, 32(sp)
	sw a3, 36(sp)
	sw a4, 40(sp)
	sw a5, 44(sp)
	sw a6, 48(sp)
	sw a7, 52(sp)
	lw a0, 264(sp)
	call printlnInt
	lw ra, 20(sp)
	lw a0, 24(sp)
	lw a1, 28(sp)
	lw a2, 32(sp)
	lw a3, 36(sp)
	lw a4, 40(sp)
	lw a5, 44(sp)
	lw a6, 48(sp)
	lw a7, 52(sp)
	# %18 = load i32, ptr %b
	lw t0, 76(sp)
	lw t0, 0(t0)
	sw t0, 268(sp)
	# call void @printlnInt(i32 %18)
	sw ra, 20(sp)
	sw a0, 24(sp)
	sw a1, 28(sp)
	sw a2, 32(sp)
	sw a3, 36(sp)
	sw a4, 40(sp)
	sw a5, 44(sp)
	sw a6, 48(sp)
	sw a7, 52(sp)
	lw a0, 268(sp)
	call printlnInt
	lw ra, 20(sp)
	lw a0, 24(sp)
	lw a1, 28(sp)
	lw a2, 32(sp)
	lw a3, 36(sp)
	lw a4, 40(sp)
	lw a5, 44(sp)
	lw a6, 48(sp)
	lw a7, 52(sp)
	# %19 = load i32, ptr %c
	lw t0, 88(sp)
	lw t0, 0(t0)
	sw t0, 272(sp)
	# call void @printlnInt(i32 %19)
	sw ra, 20(sp)
	sw a0, 24(sp)
	sw a1, 28(sp)
	sw a2, 32(sp)
	sw a3, 36(sp)
	sw a4, 40(sp)
	sw a5, 44(sp)
	sw a6, 48(sp)
	sw a7, 52(sp)
	lw a0, 272(sp)
	call printlnInt
	lw ra, 20(sp)
	lw a0, 24(sp)
	lw a1, 28(sp)
	lw a2, 32(sp)
	lw a3, 36(sp)
	lw a4, 40(sp)
	lw a5, 44(sp)
	lw a6, 48(sp)
	lw a7, 52(sp)
	# %20 = load i32, ptr %d
	lw t0, 100(sp)
	lw t0, 0(t0)
	sw t0, 276(sp)
	# call void @printlnInt(i32 %20)
	sw ra, 20(sp)
	sw a0, 24(sp)
	sw a1, 28(sp)
	sw a2, 32(sp)
	sw a3, 36(sp)
	sw a4, 40(sp)
	sw a5, 44(sp)
	sw a6, 48(sp)
	sw a7, 52(sp)
	lw a0, 276(sp)
	call printlnInt
	lw ra, 20(sp)
	lw a0, 24(sp)
	lw a1, 28(sp)
	lw a2, 32(sp)
	lw a3, 36(sp)
	lw a4, 40(sp)
	lw a5, 44(sp)
	lw a6, 48(sp)
	lw a7, 52(sp)
	# br label %return
	j main_return
	.section .text
	.globl main_return
main_return:
	# %r.e.t = load i32, ptr %ret.val
	lw t0, 56(sp)
	lw t0, 0(t0)
	sw t0, 280(sp)
	# ret i32 %r.e.t
	lw a0, 280(sp)
	lw t0, 8(sp)
	lw t1, 12(sp)
	lw t2, 16(sp)
	addi sp, sp, 284
	ret
	.section .text
	.globl _.init
_.init:
	addi sp, sp, -56
	sw t0, 8(sp)
	sw t1, 12(sp)
	sw t2, 16(sp)
	# ret void
	lw t0, 8(sp)
	lw t1, 12(sp)
	lw t2, 16(sp)
	addi sp, sp, 56
	ret

