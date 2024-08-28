	.section .rodata
	.globl @.str
@.str:
	.asciz  "-1"
	.section .rodata
	.globl @.str.1
@.str.1:
	.asciz  " "
	.section .rodata
	.globl @.str.2
@.str.2:
	.asciz  ""
	.section .data
	.globl @n.1
@n.1:
	.word  0
	.section .data
	.globl @m.1
@m.1:
	.word  0
	.section .data
	.globl @g
@g:
	.word  0
	.section .data
	.globl @INF
@INF:
	.word  10000000
	.section .text
	.globl EdgeList.init
EdgeList.init:
	addi sp, sp, -228
	sw t0, 8(sp)
	sw t1, 12(sp)
	sw t2, 16(sp)
	# %EdgeList.this = alloca ptr
	addi t0, sp, 32
	sw t0, 28(sp)
	# %n = alloca i32
	addi t0, sp, 56
	sw t0, 52(sp)
	# %m = alloca i32
	addi t0, sp, 64
	sw t0, 60(sp)
	# %1 = alloca ptr
	addi t0, sp, 76
	sw t0, 72(sp)
	# %4 = alloca ptr
	addi t0, sp, 96
	sw t0, 92(sp)
	# %7 = alloca ptr
	addi t0, sp, 116
	sw t0, 112(sp)
	# %i = alloca i32
	addi t0, sp, 132
	sw t0, 128(sp)
	# store ptr %this, ptr %EdgeList.this
	mv t0, a0
	lw t1, 28(sp)
	sw t0, 0(t1)
	# %EdgeList.edges = getelementptr %class.EdgeList, ptr %this, i32 0, i32 0
	mv t0, a0
	li t1, 0
	li t2, 4
	mul t1, t1, t2
	add t0, t0, t1
	li t1, 0
	li t2, 4
	mul t1, t1, t2
	add t0, t0, t1
	sw t0, 36(sp)
	# %EdgeList.next = getelementptr %class.EdgeList, ptr %this, i32 0, i32 1
	mv t0, a0
	li t1, 0
	li t2, 4
	mul t1, t1, t2
	add t0, t0, t1
	li t1, 1
	li t2, 4
	mul t1, t1, t2
	add t0, t0, t1
	sw t0, 40(sp)
	# %EdgeList.first = getelementptr %class.EdgeList, ptr %this, i32 0, i32 2
	mv t0, a0
	li t1, 0
	li t2, 4
	mul t1, t1, t2
	add t0, t0, t1
	li t1, 2
	li t2, 4
	mul t1, t1, t2
	add t0, t0, t1
	sw t0, 44(sp)
	# %EdgeList.size = getelementptr %class.EdgeList, ptr %this, i32 0, i32 3
	mv t0, a0
	li t1, 0
	li t2, 4
	mul t1, t1, t2
	add t0, t0, t1
	li t1, 3
	li t2, 4
	mul t1, t1, t2
	add t0, t0, t1
	sw t0, 48(sp)
	# store i32 %n.val, ptr %n
	mv t0, a1
	lw t1, 52(sp)
	sw t0, 0(t1)
	# store i32 %m.val, ptr %m
	mv t0, a2
	lw t1, 60(sp)
	sw t0, 0(t1)
	# %0 = load ptr, ptr %EdgeList.edges
	lw t0, 36(sp)
	lw t0, 0(t0)
	sw t0, 68(sp)
	# %2 = load i32, ptr %m
	lw t0, 60(sp)
	lw t0, 0(t0)
	sw t0, 80(sp)
	# %call = call ptr @array.alloca(i32 4, i32 1, i32 1, i32 %2)
	sw ra, 20(sp)
	li a0, 4
	li a1, 1
	li a2, 1
	lw a3, 80(sp)
	call array.alloca
	lw ra, 20(sp)
	sw a0, 84(sp)
	# store ptr %call, ptr %1
	lw t0, 84(sp)
	lw t1, 72(sp)
	sw t0, 0(t1)
	# store ptr %call, ptr %EdgeList.edges
	lw t0, 84(sp)
	lw t1, 36(sp)
	sw t0, 0(t1)
	# %3 = load ptr, ptr %EdgeList.next
	lw t0, 40(sp)
	lw t0, 0(t0)
	sw t0, 88(sp)
	# %5 = load i32, ptr %m
	lw t0, 60(sp)
	lw t0, 0(t0)
	sw t0, 100(sp)
	# %call.1 = call ptr @array.alloca(i32 4, i32 1, i32 1, i32 %5)
	sw ra, 20(sp)
	li a0, 4
	li a1, 1
	li a2, 1
	lw a3, 100(sp)
	call array.alloca
	lw ra, 20(sp)
	sw a0, 104(sp)
	# store ptr %call.1, ptr %4
	lw t0, 104(sp)
	lw t1, 92(sp)
	sw t0, 0(t1)
	# store ptr %call.1, ptr %EdgeList.next
	lw t0, 104(sp)
	lw t1, 40(sp)
	sw t0, 0(t1)
	# %6 = load ptr, ptr %EdgeList.first
	lw t0, 44(sp)
	lw t0, 0(t0)
	sw t0, 108(sp)
	# %8 = load i32, ptr %n
	lw t0, 52(sp)
	lw t0, 0(t0)
	sw t0, 120(sp)
	# %call.2 = call ptr @array.alloca(i32 4, i32 1, i32 1, i32 %8)
	sw ra, 20(sp)
	li a0, 4
	li a1, 1
	li a2, 1
	lw a3, 120(sp)
	call array.alloca
	lw ra, 20(sp)
	sw a0, 124(sp)
	# store ptr %call.2, ptr %7
	lw t0, 124(sp)
	lw t1, 112(sp)
	sw t0, 0(t1)
	# store ptr %call.2, ptr %EdgeList.first
	lw t0, 124(sp)
	lw t1, 44(sp)
	sw t0, 0(t1)
	# %9 = load i32, ptr %i
	lw t0, 128(sp)
	lw t0, 0(t0)
	sw t0, 136(sp)
	# store i32 0, ptr %i
	li t0, 0
	lw t1, 128(sp)
	sw t0, 0(t1)
	# br label %cond..
	j EdgeList.init_cond..
	.section .text
	.globl EdgeList.init_cond..
EdgeList.init_cond..:
	# %10 = load i32, ptr %i
	lw t0, 128(sp)
	lw t0, 0(t0)
	sw t0, 140(sp)
	# %11 = load i32, ptr %m
	lw t0, 60(sp)
	lw t0, 0(t0)
	sw t0, 144(sp)
	# %less = icmp slt i32 %10, %11
	lw t0, 140(sp)
	lw t1, 144(sp)
	sub t2, t0, t1
	sltz t2, t2
	sw t2, 148(sp)
	# br i1 %less, label %body.., label %end..
	lw t0, 148(sp)
	beq t0, x0, EdgeList.init_end..
	j EdgeList.init_body..
	.section .text
	.globl EdgeList.init_body..
EdgeList.init_body..:
	# %12 = load ptr, ptr %EdgeList.next
	lw t0, 40(sp)
	lw t0, 0(t0)
	sw t0, 152(sp)
	# %13 = load i32, ptr %i
	lw t0, 128(sp)
	lw t0, 0(t0)
	sw t0, 156(sp)
	# %array.idx = getelementptr ptr, ptr %12, i32 %13
	lw t0, 152(sp)
	lw t1, 156(sp)
	li t2, 4
	mul t1, t1, t2
	add t0, t0, t1
	sw t0, 160(sp)
	# %array.idx.val = load i32, ptr %array.idx
	lw t0, 160(sp)
	lw t0, 0(t0)
	sw t0, 164(sp)
	# %minus = sub i32 0, 1
	li t0, 0
	li t1, 1
	sub t2, t0, t1
	sw t2, 168(sp)
	# store i32 %minus, ptr %array.idx
	lw t0, 168(sp)
	lw t1, 160(sp)
	sw t0, 0(t1)
	# br label %step..
	j EdgeList.init_step..
	.section .text
	.globl EdgeList.init_step..
EdgeList.init_step..:
	# %14 = load i32, ptr %i
	lw t0, 128(sp)
	lw t0, 0(t0)
	sw t0, 172(sp)
	# %pre.Inc = add i32 %14, 1
	lw t0, 172(sp)
	li t1, 1
	add t2, t0, t1
	sw t2, 176(sp)
	# store i32 %pre.Inc, ptr %i
	lw t0, 176(sp)
	lw t1, 128(sp)
	sw t0, 0(t1)
	# br label %cond..
	j EdgeList.init_cond..
	.section .text
	.globl EdgeList.init_end..
EdgeList.init_end..:
	# %15 = load i32, ptr %i
	lw t0, 128(sp)
	lw t0, 0(t0)
	sw t0, 180(sp)
	# store i32 0, ptr %i
	li t0, 0
	lw t1, 128(sp)
	sw t0, 0(t1)
	# br label %cond...1
	j EdgeList.init_cond...1
	.section .text
	.globl EdgeList.init_cond...1
EdgeList.init_cond...1:
	# %16 = load i32, ptr %i
	lw t0, 128(sp)
	lw t0, 0(t0)
	sw t0, 184(sp)
	# %17 = load i32, ptr %n
	lw t0, 52(sp)
	lw t0, 0(t0)
	sw t0, 188(sp)
	# %less.1 = icmp slt i32 %16, %17
	lw t0, 184(sp)
	lw t1, 188(sp)
	sub t2, t0, t1
	sltz t2, t2
	sw t2, 192(sp)
	# br i1 %less.1, label %body...1, label %end...1
	lw t0, 192(sp)
	beq t0, x0, EdgeList.init_end...1
	j EdgeList.init_body...1
	.section .text
	.globl EdgeList.init_body...1
EdgeList.init_body...1:
	# %18 = load ptr, ptr %EdgeList.first
	lw t0, 44(sp)
	lw t0, 0(t0)
	sw t0, 196(sp)
	# %19 = load i32, ptr %i
	lw t0, 128(sp)
	lw t0, 0(t0)
	sw t0, 200(sp)
	# %array.idx.1 = getelementptr ptr, ptr %18, i32 %19
	lw t0, 196(sp)
	lw t1, 200(sp)
	li t2, 4
	mul t1, t1, t2
	add t0, t0, t1
	sw t0, 204(sp)
	# %array.idx.val.1 = load i32, ptr %array.idx.1
	lw t0, 204(sp)
	lw t0, 0(t0)
	sw t0, 208(sp)
	# %minus.1 = sub i32 0, 1
	li t0, 0
	li t1, 1
	sub t2, t0, t1
	sw t2, 212(sp)
	# store i32 %minus.1, ptr %array.idx.1
	lw t0, 212(sp)
	lw t1, 204(sp)
	sw t0, 0(t1)
	# br label %step...1
	j EdgeList.init_step...1
	.section .text
	.globl EdgeList.init_step...1
EdgeList.init_step...1:
	# %20 = load i32, ptr %i
	lw t0, 128(sp)
	lw t0, 0(t0)
	sw t0, 216(sp)
	# %pre.Inc.1 = add i32 %20, 1
	lw t0, 216(sp)
	li t1, 1
	add t2, t0, t1
	sw t2, 220(sp)
	# store i32 %pre.Inc.1, ptr %i
	lw t0, 220(sp)
	lw t1, 128(sp)
	sw t0, 0(t1)
	# br label %cond...1
	j EdgeList.init_cond...1
	.section .text
	.globl EdgeList.init_end...1
EdgeList.init_end...1:
	# %21 = load i32, ptr %EdgeList.size
	lw t0, 48(sp)
	lw t0, 0(t0)
	sw t0, 224(sp)
	# store i32 0, ptr %EdgeList.size
	li t0, 0
	lw t1, 48(sp)
	sw t0, 0(t1)
	# br label %return
	j EdgeList.init_return
	.section .text
	.globl EdgeList.init_return
EdgeList.init_return:
	# ret void
	lw t0, 8(sp)
	lw t1, 12(sp)
	lw t2, 16(sp)
	addi sp, sp, 228
	ret
	.section .text
	.globl EdgeList.addEdge
EdgeList.addEdge:
	addi sp, sp, -216
	sw t0, 8(sp)
	sw t1, 12(sp)
	sw t2, 16(sp)
	# %EdgeList.this = alloca ptr
	addi t0, sp, 32
	sw t0, 28(sp)
	# %u = alloca i32
	addi t0, sp, 56
	sw t0, 52(sp)
	# %v = alloca i32
	addi t0, sp, 64
	sw t0, 60(sp)
	# %w = alloca i32
	addi t0, sp, 72
	sw t0, 68(sp)
	# %e = alloca ptr
	addi t0, sp, 80
	sw t0, 76(sp)
	# store ptr %this, ptr %EdgeList.this
	mv t0, a0
	lw t1, 28(sp)
	sw t0, 0(t1)
	# %EdgeList.edges = getelementptr %class.EdgeList, ptr %this, i32 0, i32 0
	mv t0, a0
	li t1, 0
	li t2, 4
	mul t1, t1, t2
	add t0, t0, t1
	li t1, 0
	li t2, 4
	mul t1, t1, t2
	add t0, t0, t1
	sw t0, 36(sp)
	# %EdgeList.next = getelementptr %class.EdgeList, ptr %this, i32 0, i32 1
	mv t0, a0
	li t1, 0
	li t2, 4
	mul t1, t1, t2
	add t0, t0, t1
	li t1, 1
	li t2, 4
	mul t1, t1, t2
	add t0, t0, t1
	sw t0, 40(sp)
	# %EdgeList.first = getelementptr %class.EdgeList, ptr %this, i32 0, i32 2
	mv t0, a0
	li t1, 0
	li t2, 4
	mul t1, t1, t2
	add t0, t0, t1
	li t1, 2
	li t2, 4
	mul t1, t1, t2
	add t0, t0, t1
	sw t0, 44(sp)
	# %EdgeList.size = getelementptr %class.EdgeList, ptr %this, i32 0, i32 3
	mv t0, a0
	li t1, 0
	li t2, 4
	mul t1, t1, t2
	add t0, t0, t1
	li t1, 3
	li t2, 4
	mul t1, t1, t2
	add t0, t0, t1
	sw t0, 48(sp)
	# store i32 %u.val, ptr %u
	mv t0, a1
	lw t1, 52(sp)
	sw t0, 0(t1)
	# store i32 %v.val, ptr %v
	mv t0, a2
	lw t1, 60(sp)
	sw t0, 0(t1)
	# store i32 %w.val, ptr %w
	mv t0, a3
	lw t1, 68(sp)
	sw t0, 0(t1)
	# %22 = call ptr @malloc(i32 12)
	sw ra, 20(sp)
	li a0, 12
	call malloc
	lw ra, 20(sp)
	sw a0, 84(sp)
	# store ptr %22, ptr %e
	lw t0, 84(sp)
	lw t1, 76(sp)
	sw t0, 0(t1)
	# %23 = load ptr, ptr %e
	lw t0, 76(sp)
	lw t0, 0(t0)
	sw t0, 88(sp)
	# %24 = getelementptr %class.Edge, ptr %23, i32 0, i32 0
	lw t0, 88(sp)
	li t1, 0
	li t2, 4
	mul t1, t1, t2
	add t0, t0, t1
	li t1, 0
	li t2, 4
	mul t1, t1, t2
	add t0, t0, t1
	sw t0, 92(sp)
	# %25 = load i32, ptr %24
	lw t0, 92(sp)
	lw t0, 0(t0)
	sw t0, 96(sp)
	# %26 = load i32, ptr %u
	lw t0, 52(sp)
	lw t0, 0(t0)
	sw t0, 100(sp)
	# store i32 %26, ptr %24
	lw t0, 100(sp)
	lw t1, 92(sp)
	sw t0, 0(t1)
	# %27 = load ptr, ptr %e
	lw t0, 76(sp)
	lw t0, 0(t0)
	sw t0, 104(sp)
	# %28 = getelementptr %class.Edge, ptr %27, i32 0, i32 1
	lw t0, 104(sp)
	li t1, 0
	li t2, 4
	mul t1, t1, t2
	add t0, t0, t1
	li t1, 1
	li t2, 4
	mul t1, t1, t2
	add t0, t0, t1
	sw t0, 108(sp)
	# %29 = load i32, ptr %28
	lw t0, 108(sp)
	lw t0, 0(t0)
	sw t0, 112(sp)
	# %30 = load i32, ptr %v
	lw t0, 60(sp)
	lw t0, 0(t0)
	sw t0, 116(sp)
	# store i32 %30, ptr %28
	lw t0, 116(sp)
	lw t1, 108(sp)
	sw t0, 0(t1)
	# %31 = load ptr, ptr %e
	lw t0, 76(sp)
	lw t0, 0(t0)
	sw t0, 120(sp)
	# %32 = getelementptr %class.Edge, ptr %31, i32 0, i32 2
	lw t0, 120(sp)
	li t1, 0
	li t2, 4
	mul t1, t1, t2
	add t0, t0, t1
	li t1, 2
	li t2, 4
	mul t1, t1, t2
	add t0, t0, t1
	sw t0, 124(sp)
	# %33 = load i32, ptr %32
	lw t0, 124(sp)
	lw t0, 0(t0)
	sw t0, 128(sp)
	# %34 = load i32, ptr %w
	lw t0, 68(sp)
	lw t0, 0(t0)
	sw t0, 132(sp)
	# store i32 %34, ptr %32
	lw t0, 132(sp)
	lw t1, 124(sp)
	sw t0, 0(t1)
	# %35 = load ptr, ptr %EdgeList.edges
	lw t0, 36(sp)
	lw t0, 0(t0)
	sw t0, 136(sp)
	# %36 = load i32, ptr %EdgeList.size
	lw t0, 48(sp)
	lw t0, 0(t0)
	sw t0, 140(sp)
	# %array.idx.2 = getelementptr ptr, ptr %35, i32 %36
	lw t0, 136(sp)
	lw t1, 140(sp)
	li t2, 4
	mul t1, t1, t2
	add t0, t0, t1
	sw t0, 144(sp)
	# %array.idx.val.2 = load ptr, ptr %array.idx.2
	lw t0, 144(sp)
	lw t0, 0(t0)
	sw t0, 148(sp)
	# %37 = load ptr, ptr %e
	lw t0, 76(sp)
	lw t0, 0(t0)
	sw t0, 152(sp)
	# store ptr %37, ptr %array.idx.2
	lw t0, 152(sp)
	lw t1, 144(sp)
	sw t0, 0(t1)
	# %38 = load ptr, ptr %EdgeList.next
	lw t0, 40(sp)
	lw t0, 0(t0)
	sw t0, 156(sp)
	# %39 = load i32, ptr %EdgeList.size
	lw t0, 48(sp)
	lw t0, 0(t0)
	sw t0, 160(sp)
	# %array.idx.3 = getelementptr ptr, ptr %38, i32 %39
	lw t0, 156(sp)
	lw t1, 160(sp)
	li t2, 4
	mul t1, t1, t2
	add t0, t0, t1
	sw t0, 164(sp)
	# %array.idx.val.3 = load i32, ptr %array.idx.3
	lw t0, 164(sp)
	lw t0, 0(t0)
	sw t0, 168(sp)
	# %40 = load ptr, ptr %EdgeList.first
	lw t0, 44(sp)
	lw t0, 0(t0)
	sw t0, 172(sp)
	# %41 = load i32, ptr %u
	lw t0, 52(sp)
	lw t0, 0(t0)
	sw t0, 176(sp)
	# %array.idx.4 = getelementptr ptr, ptr %40, i32 %41
	lw t0, 172(sp)
	lw t1, 176(sp)
	li t2, 4
	mul t1, t1, t2
	add t0, t0, t1
	sw t0, 180(sp)
	# %array.idx.val.4 = load i32, ptr %array.idx.4
	lw t0, 180(sp)
	lw t0, 0(t0)
	sw t0, 184(sp)
	# store i32 %array.idx.val.4, ptr %array.idx.3
	lw t0, 184(sp)
	lw t1, 164(sp)
	sw t0, 0(t1)
	# %42 = load ptr, ptr %EdgeList.first
	lw t0, 44(sp)
	lw t0, 0(t0)
	sw t0, 188(sp)
	# %43 = load i32, ptr %u
	lw t0, 52(sp)
	lw t0, 0(t0)
	sw t0, 192(sp)
	# %array.idx.5 = getelementptr ptr, ptr %42, i32 %43
	lw t0, 188(sp)
	lw t1, 192(sp)
	li t2, 4
	mul t1, t1, t2
	add t0, t0, t1
	sw t0, 196(sp)
	# %array.idx.val.5 = load i32, ptr %array.idx.5
	lw t0, 196(sp)
	lw t0, 0(t0)
	sw t0, 200(sp)
	# %44 = load i32, ptr %EdgeList.size
	lw t0, 48(sp)
	lw t0, 0(t0)
	sw t0, 204(sp)
	# store i32 %44, ptr %array.idx.5
	lw t0, 204(sp)
	lw t1, 196(sp)
	sw t0, 0(t1)
	# %45 = load i32, ptr %EdgeList.size
	lw t0, 48(sp)
	lw t0, 0(t0)
	sw t0, 208(sp)
	# %pre.Inc.2 = add i32 %45, 1
	lw t0, 208(sp)
	li t1, 1
	add t2, t0, t1
	sw t2, 212(sp)
	# store i32 %pre.Inc.2, ptr %EdgeList.size
	lw t0, 212(sp)
	lw t1, 48(sp)
	sw t0, 0(t1)
	# br label %return
	j EdgeList.addEdge_return
	.section .text
	.globl EdgeList.addEdge_return
EdgeList.addEdge_return:
	# ret void
	lw t0, 8(sp)
	lw t1, 12(sp)
	lw t2, 16(sp)
	addi sp, sp, 216
	ret
	.section .text
	.globl EdgeList.nVertices
EdgeList.nVertices:
	addi sp, sp, -72
	sw t0, 8(sp)
	sw t1, 12(sp)
	sw t2, 16(sp)
	# %EdgeList.this = alloca ptr
	addi t0, sp, 32
	sw t0, 28(sp)
	# %ret.val = alloca i32
	addi t0, sp, 56
	sw t0, 52(sp)
	# store ptr %this, ptr %EdgeList.this
	mv t0, a0
	lw t1, 28(sp)
	sw t0, 0(t1)
	# %EdgeList.edges = getelementptr %class.EdgeList, ptr %this, i32 0, i32 0
	mv t0, a0
	li t1, 0
	li t2, 4
	mul t1, t1, t2
	add t0, t0, t1
	li t1, 0
	li t2, 4
	mul t1, t1, t2
	add t0, t0, t1
	sw t0, 36(sp)
	# %EdgeList.next = getelementptr %class.EdgeList, ptr %this, i32 0, i32 1
	mv t0, a0
	li t1, 0
	li t2, 4
	mul t1, t1, t2
	add t0, t0, t1
	li t1, 1
	li t2, 4
	mul t1, t1, t2
	add t0, t0, t1
	sw t0, 40(sp)
	# %EdgeList.first = getelementptr %class.EdgeList, ptr %this, i32 0, i32 2
	mv t0, a0
	li t1, 0
	li t2, 4
	mul t1, t1, t2
	add t0, t0, t1
	li t1, 2
	li t2, 4
	mul t1, t1, t2
	add t0, t0, t1
	sw t0, 44(sp)
	# %EdgeList.size = getelementptr %class.EdgeList, ptr %this, i32 0, i32 3
	mv t0, a0
	li t1, 0
	li t2, 4
	mul t1, t1, t2
	add t0, t0, t1
	li t1, 3
	li t2, 4
	mul t1, t1, t2
	add t0, t0, t1
	sw t0, 48(sp)
	# %46 = load ptr, ptr %EdgeList.first
	lw t0, 44(sp)
	lw t0, 0(t0)
	sw t0, 60(sp)
	# %array.size = call i32 @array.size(ptr %46)
	sw ra, 20(sp)
	lw a0, 60(sp)
	call array.size
	lw ra, 20(sp)
	sw a0, 64(sp)
	# store i32 %array.size, ptr %ret.val
	lw t0, 64(sp)
	lw t1, 52(sp)
	sw t0, 0(t1)
	# br label %return
	j EdgeList.nVertices_return
	.section .text
	.globl EdgeList.nVertices_return
EdgeList.nVertices_return:
	# %r.e.t = load i32, ptr %ret.val
	lw t0, 52(sp)
	lw t0, 0(t0)
	sw t0, 68(sp)
	# ret i32 %r.e.t
	lw a0, 68(sp)
	lw t0, 8(sp)
	lw t1, 12(sp)
	lw t2, 16(sp)
	addi sp, sp, 72
	ret
	.section .text
	.globl EdgeList.nEdges
EdgeList.nEdges:
	addi sp, sp, -72
	sw t0, 8(sp)
	sw t1, 12(sp)
	sw t2, 16(sp)
	# %EdgeList.this = alloca ptr
	addi t0, sp, 32
	sw t0, 28(sp)
	# %ret.val.1 = alloca i32
	addi t0, sp, 56
	sw t0, 52(sp)
	# store ptr %this, ptr %EdgeList.this
	mv t0, a0
	lw t1, 28(sp)
	sw t0, 0(t1)
	# %EdgeList.edges = getelementptr %class.EdgeList, ptr %this, i32 0, i32 0
	mv t0, a0
	li t1, 0
	li t2, 4
	mul t1, t1, t2
	add t0, t0, t1
	li t1, 0
	li t2, 4
	mul t1, t1, t2
	add t0, t0, t1
	sw t0, 36(sp)
	# %EdgeList.next = getelementptr %class.EdgeList, ptr %this, i32 0, i32 1
	mv t0, a0
	li t1, 0
	li t2, 4
	mul t1, t1, t2
	add t0, t0, t1
	li t1, 1
	li t2, 4
	mul t1, t1, t2
	add t0, t0, t1
	sw t0, 40(sp)
	# %EdgeList.first = getelementptr %class.EdgeList, ptr %this, i32 0, i32 2
	mv t0, a0
	li t1, 0
	li t2, 4
	mul t1, t1, t2
	add t0, t0, t1
	li t1, 2
	li t2, 4
	mul t1, t1, t2
	add t0, t0, t1
	sw t0, 44(sp)
	# %EdgeList.size = getelementptr %class.EdgeList, ptr %this, i32 0, i32 3
	mv t0, a0
	li t1, 0
	li t2, 4
	mul t1, t1, t2
	add t0, t0, t1
	li t1, 3
	li t2, 4
	mul t1, t1, t2
	add t0, t0, t1
	sw t0, 48(sp)
	# %47 = load ptr, ptr %EdgeList.edges
	lw t0, 36(sp)
	lw t0, 0(t0)
	sw t0, 60(sp)
	# %array.size.1 = call i32 @array.size(ptr %47)
	sw ra, 20(sp)
	lw a0, 60(sp)
	call array.size
	lw ra, 20(sp)
	sw a0, 64(sp)
	# store i32 %array.size.1, ptr %ret.val.1
	lw t0, 64(sp)
	lw t1, 52(sp)
	sw t0, 0(t1)
	# br label %return
	j EdgeList.nEdges_return
	.section .text
	.globl EdgeList.nEdges_return
EdgeList.nEdges_return:
	# %r.e.t.1 = load i32, ptr %ret.val.1
	lw t0, 52(sp)
	lw t0, 0(t0)
	sw t0, 68(sp)
	# ret i32 %r.e.t.1
	lw a0, 68(sp)
	lw t0, 8(sp)
	lw t1, 12(sp)
	lw t2, 16(sp)
	addi sp, sp, 72
	ret
	.section .text
	.globl Array_Node.push_back
Array_Node.push_back:
	addi sp, sp, -96
	sw t0, 8(sp)
	sw t1, 12(sp)
	sw t2, 16(sp)
	# %Array_Node.this = alloca ptr
	addi t0, sp, 32
	sw t0, 28(sp)
	# %v.1 = alloca ptr
	addi t0, sp, 48
	sw t0, 44(sp)
	# store ptr %this, ptr %Array_Node.this
	mv t0, a0
	lw t1, 28(sp)
	sw t0, 0(t1)
	# %Array_Node.storage = getelementptr %class.Array_Node, ptr %this, i32 0, i32 0
	mv t0, a0
	li t1, 0
	li t2, 4
	mul t1, t1, t2
	add t0, t0, t1
	li t1, 0
	li t2, 4
	mul t1, t1, t2
	add t0, t0, t1
	sw t0, 36(sp)
	# %Array_Node.sz = getelementptr %class.Array_Node, ptr %this, i32 0, i32 1
	mv t0, a0
	li t1, 0
	li t2, 4
	mul t1, t1, t2
	add t0, t0, t1
	li t1, 1
	li t2, 4
	mul t1, t1, t2
	add t0, t0, t1
	sw t0, 40(sp)
	# store ptr %v.val, ptr %v.1
	mv t0, a1
	lw t1, 44(sp)
	sw t0, 0(t1)
	# %call.null.ret = call i32 @Array_Node.size(ptr %this)
	sw ra, 20(sp)
	mv a0, a0
	call Array_Node.size
	lw ra, 20(sp)
	sw a0, 52(sp)
	# %48 = load ptr, ptr %Array_Node.storage
	lw t0, 36(sp)
	lw t0, 0(t0)
	sw t0, 56(sp)
	# %array.size.2 = call i32 @array.size(ptr %48)
	sw ra, 20(sp)
	lw a0, 56(sp)
	call array.size
	lw ra, 20(sp)
	sw a0, 60(sp)
	# %eq = icmp eq i32 %call.null.ret, %array.size.2
	lw t0, 52(sp)
	lw t1, 60(sp)
	sub t2, t0, t1
	seqz t2, t2
	sw t2, 64(sp)
	# br i1 %eq, label %if.true, label %if.false
	lw t0, 64(sp)
	beq t0, x0, Array_Node.push_back_if.false
	j Array_Node.push_back_if.true
	.section .text
	.globl Array_Node.push_back_if.true
Array_Node.push_back_if.true:
	# call void @Array_Node.doubleStorage(ptr %this)
	sw ra, 20(sp)
	mv a0, a0
	call Array_Node.doubleStorage
	lw ra, 20(sp)
	# br label %if.end
	j Array_Node.push_back_if.end
	.section .text
	.globl Array_Node.push_back_if.false
Array_Node.push_back_if.false:
	# br label %if.end
	j Array_Node.push_back_if.end
	.section .text
	.globl Array_Node.push_back_if.end
Array_Node.push_back_if.end:
	# %49 = load ptr, ptr %Array_Node.storage
	lw t0, 36(sp)
	lw t0, 0(t0)
	sw t0, 68(sp)
	# %50 = load i32, ptr %Array_Node.sz
	lw t0, 40(sp)
	lw t0, 0(t0)
	sw t0, 72(sp)
	# %array.idx.6 = getelementptr ptr, ptr %49, i32 %50
	lw t0, 68(sp)
	lw t1, 72(sp)
	li t2, 4
	mul t1, t1, t2
	add t0, t0, t1
	sw t0, 76(sp)
	# %array.idx.val.6 = load ptr, ptr %array.idx.6
	lw t0, 76(sp)
	lw t0, 0(t0)
	sw t0, 80(sp)
	# %51 = load ptr, ptr %v.1
	lw t0, 44(sp)
	lw t0, 0(t0)
	sw t0, 84(sp)
	# store ptr %51, ptr %array.idx.6
	lw t0, 84(sp)
	lw t1, 76(sp)
	sw t0, 0(t1)
	# %52 = load i32, ptr %Array_Node.sz
	lw t0, 40(sp)
	lw t0, 0(t0)
	sw t0, 88(sp)
	# %pre.Inc.3 = add i32 %52, 1
	lw t0, 88(sp)
	li t1, 1
	add t2, t0, t1
	sw t2, 92(sp)
	# store i32 %pre.Inc.3, ptr %Array_Node.sz
	lw t0, 92(sp)
	lw t1, 40(sp)
	sw t0, 0(t1)
	# br label %return
	j Array_Node.push_back_return
	.section .text
	.globl Array_Node.push_back_return
Array_Node.push_back_return:
	# ret void
	lw t0, 8(sp)
	lw t1, 12(sp)
	lw t2, 16(sp)
	addi sp, sp, 96
	ret
	.section .text
	.globl Array_Node.pop_back
Array_Node.pop_back:
	addi sp, sp, -80
	sw t0, 8(sp)
	sw t1, 12(sp)
	sw t2, 16(sp)
	# %Array_Node.this = alloca ptr
	addi t0, sp, 32
	sw t0, 28(sp)
	# %ret.val.2 = alloca ptr
	addi t0, sp, 48
	sw t0, 44(sp)
	# store ptr %this, ptr %Array_Node.this
	mv t0, a0
	lw t1, 28(sp)
	sw t0, 0(t1)
	# %Array_Node.storage = getelementptr %class.Array_Node, ptr %this, i32 0, i32 0
	mv t0, a0
	li t1, 0
	li t2, 4
	mul t1, t1, t2
	add t0, t0, t1
	li t1, 0
	li t2, 4
	mul t1, t1, t2
	add t0, t0, t1
	sw t0, 36(sp)
	# %Array_Node.sz = getelementptr %class.Array_Node, ptr %this, i32 0, i32 1
	mv t0, a0
	li t1, 0
	li t2, 4
	mul t1, t1, t2
	add t0, t0, t1
	li t1, 1
	li t2, 4
	mul t1, t1, t2
	add t0, t0, t1
	sw t0, 40(sp)
	# %53 = load i32, ptr %Array_Node.sz
	lw t0, 40(sp)
	lw t0, 0(t0)
	sw t0, 52(sp)
	# %pre.Dec = sub i32 %53, 1
	lw t0, 52(sp)
	li t1, 1
	sub t2, t0, t1
	sw t2, 56(sp)
	# store i32 %pre.Dec, ptr %Array_Node.sz
	lw t0, 56(sp)
	lw t1, 40(sp)
	sw t0, 0(t1)
	# %54 = load ptr, ptr %Array_Node.storage
	lw t0, 36(sp)
	lw t0, 0(t0)
	sw t0, 60(sp)
	# %55 = load i32, ptr %Array_Node.sz
	lw t0, 40(sp)
	lw t0, 0(t0)
	sw t0, 64(sp)
	# %array.idx.7 = getelementptr ptr, ptr %54, i32 %55
	lw t0, 60(sp)
	lw t1, 64(sp)
	li t2, 4
	mul t1, t1, t2
	add t0, t0, t1
	sw t0, 68(sp)
	# %array.idx.val.7 = load ptr, ptr %array.idx.7
	lw t0, 68(sp)
	lw t0, 0(t0)
	sw t0, 72(sp)
	# store ptr %array.idx.val.7, ptr %ret.val.2
	lw t0, 72(sp)
	lw t1, 44(sp)
	sw t0, 0(t1)
	# br label %return
	j Array_Node.pop_back_return
	.section .text
	.globl Array_Node.pop_back_return
Array_Node.pop_back_return:
	# %r.e.t.2 = load ptr, ptr %ret.val.2
	lw t0, 44(sp)
	lw t0, 0(t0)
	sw t0, 76(sp)
	# ret ptr %r.e.t.2
	lw a0, 76(sp)
	lw t0, 8(sp)
	lw t1, 12(sp)
	lw t2, 16(sp)
	addi sp, sp, 80
	ret
	.section .text
	.globl Array_Node.back
Array_Node.back:
	addi sp, sp, -76
	sw t0, 8(sp)
	sw t1, 12(sp)
	sw t2, 16(sp)
	# %Array_Node.this = alloca ptr
	addi t0, sp, 32
	sw t0, 28(sp)
	# %ret.val.3 = alloca ptr
	addi t0, sp, 48
	sw t0, 44(sp)
	# store ptr %this, ptr %Array_Node.this
	mv t0, a0
	lw t1, 28(sp)
	sw t0, 0(t1)
	# %Array_Node.storage = getelementptr %class.Array_Node, ptr %this, i32 0, i32 0
	mv t0, a0
	li t1, 0
	li t2, 4
	mul t1, t1, t2
	add t0, t0, t1
	li t1, 0
	li t2, 4
	mul t1, t1, t2
	add t0, t0, t1
	sw t0, 36(sp)
	# %Array_Node.sz = getelementptr %class.Array_Node, ptr %this, i32 0, i32 1
	mv t0, a0
	li t1, 0
	li t2, 4
	mul t1, t1, t2
	add t0, t0, t1
	li t1, 1
	li t2, 4
	mul t1, t1, t2
	add t0, t0, t1
	sw t0, 40(sp)
	# %56 = load ptr, ptr %Array_Node.storage
	lw t0, 36(sp)
	lw t0, 0(t0)
	sw t0, 52(sp)
	# %57 = load i32, ptr %Array_Node.sz
	lw t0, 40(sp)
	lw t0, 0(t0)
	sw t0, 56(sp)
	# %sub = sub i32 %57, 1
	lw t0, 56(sp)
	li t1, 1
	sub t2, t0, t1
	sw t2, 60(sp)
	# %array.idx.8 = getelementptr ptr, ptr %56, i32 %sub
	lw t0, 52(sp)
	lw t1, 60(sp)
	li t2, 4
	mul t1, t1, t2
	add t0, t0, t1
	sw t0, 64(sp)
	# %array.idx.val.8 = load ptr, ptr %array.idx.8
	lw t0, 64(sp)
	lw t0, 0(t0)
	sw t0, 68(sp)
	# store ptr %array.idx.val.8, ptr %ret.val.3
	lw t0, 68(sp)
	lw t1, 44(sp)
	sw t0, 0(t1)
	# br label %return
	j Array_Node.back_return
	.section .text
	.globl Array_Node.back_return
Array_Node.back_return:
	# %r.e.t.3 = load ptr, ptr %ret.val.3
	lw t0, 44(sp)
	lw t0, 0(t0)
	sw t0, 72(sp)
	# ret ptr %r.e.t.3
	lw a0, 72(sp)
	lw t0, 8(sp)
	lw t1, 12(sp)
	lw t2, 16(sp)
	addi sp, sp, 76
	ret
	.section .text
	.globl Array_Node.front
Array_Node.front:
	addi sp, sp, -68
	sw t0, 8(sp)
	sw t1, 12(sp)
	sw t2, 16(sp)
	# %Array_Node.this = alloca ptr
	addi t0, sp, 32
	sw t0, 28(sp)
	# %ret.val.4 = alloca ptr
	addi t0, sp, 48
	sw t0, 44(sp)
	# store ptr %this, ptr %Array_Node.this
	mv t0, a0
	lw t1, 28(sp)
	sw t0, 0(t1)
	# %Array_Node.storage = getelementptr %class.Array_Node, ptr %this, i32 0, i32 0
	mv t0, a0
	li t1, 0
	li t2, 4
	mul t1, t1, t2
	add t0, t0, t1
	li t1, 0
	li t2, 4
	mul t1, t1, t2
	add t0, t0, t1
	sw t0, 36(sp)
	# %Array_Node.sz = getelementptr %class.Array_Node, ptr %this, i32 0, i32 1
	mv t0, a0
	li t1, 0
	li t2, 4
	mul t1, t1, t2
	add t0, t0, t1
	li t1, 1
	li t2, 4
	mul t1, t1, t2
	add t0, t0, t1
	sw t0, 40(sp)
	# %58 = load ptr, ptr %Array_Node.storage
	lw t0, 36(sp)
	lw t0, 0(t0)
	sw t0, 52(sp)
	# %array.idx.9 = getelementptr ptr, ptr %58, i32 0
	lw t0, 52(sp)
	li t1, 0
	li t2, 4
	mul t1, t1, t2
	add t0, t0, t1
	sw t0, 56(sp)
	# %array.idx.val.9 = load ptr, ptr %array.idx.9
	lw t0, 56(sp)
	lw t0, 0(t0)
	sw t0, 60(sp)
	# store ptr %array.idx.val.9, ptr %ret.val.4
	lw t0, 60(sp)
	lw t1, 44(sp)
	sw t0, 0(t1)
	# br label %return
	j Array_Node.front_return
	.section .text
	.globl Array_Node.front_return
Array_Node.front_return:
	# %r.e.t.4 = load ptr, ptr %ret.val.4
	lw t0, 44(sp)
	lw t0, 0(t0)
	sw t0, 64(sp)
	# ret ptr %r.e.t.4
	lw a0, 64(sp)
	lw t0, 8(sp)
	lw t1, 12(sp)
	lw t2, 16(sp)
	addi sp, sp, 68
	ret
	.section .text
	.globl Array_Node.size
Array_Node.size:
	addi sp, sp, -60
	sw t0, 8(sp)
	sw t1, 12(sp)
	sw t2, 16(sp)
	# %Array_Node.this = alloca ptr
	addi t0, sp, 32
	sw t0, 28(sp)
	# %ret.val.5 = alloca i32
	addi t0, sp, 48
	sw t0, 44(sp)
	# store ptr %this, ptr %Array_Node.this
	mv t0, a0
	lw t1, 28(sp)
	sw t0, 0(t1)
	# %Array_Node.storage = getelementptr %class.Array_Node, ptr %this, i32 0, i32 0
	mv t0, a0
	li t1, 0
	li t2, 4
	mul t1, t1, t2
	add t0, t0, t1
	li t1, 0
	li t2, 4
	mul t1, t1, t2
	add t0, t0, t1
	sw t0, 36(sp)
	# %Array_Node.sz = getelementptr %class.Array_Node, ptr %this, i32 0, i32 1
	mv t0, a0
	li t1, 0
	li t2, 4
	mul t1, t1, t2
	add t0, t0, t1
	li t1, 1
	li t2, 4
	mul t1, t1, t2
	add t0, t0, t1
	sw t0, 40(sp)
	# %59 = load i32, ptr %Array_Node.sz
	lw t0, 40(sp)
	lw t0, 0(t0)
	sw t0, 52(sp)
	# store i32 %59, ptr %ret.val.5
	lw t0, 52(sp)
	lw t1, 44(sp)
	sw t0, 0(t1)
	# br label %return
	j Array_Node.size_return
	.section .text
	.globl Array_Node.size_return
Array_Node.size_return:
	# %r.e.t.5 = load i32, ptr %ret.val.5
	lw t0, 44(sp)
	lw t0, 0(t0)
	sw t0, 56(sp)
	# ret i32 %r.e.t.5
	lw a0, 56(sp)
	lw t0, 8(sp)
	lw t1, 12(sp)
	lw t2, 16(sp)
	addi sp, sp, 60
	ret
	.section .text
	.globl Array_Node.resize
Array_Node.resize:
	addi sp, sp, -76
	sw t0, 8(sp)
	sw t1, 12(sp)
	sw t2, 16(sp)
	# %Array_Node.this = alloca ptr
	addi t0, sp, 32
	sw t0, 28(sp)
	# %newSize = alloca i32
	addi t0, sp, 48
	sw t0, 44(sp)
	# store ptr %this, ptr %Array_Node.this
	mv t0, a0
	lw t1, 28(sp)
	sw t0, 0(t1)
	# %Array_Node.storage = getelementptr %class.Array_Node, ptr %this, i32 0, i32 0
	mv t0, a0
	li t1, 0
	li t2, 4
	mul t1, t1, t2
	add t0, t0, t1
	li t1, 0
	li t2, 4
	mul t1, t1, t2
	add t0, t0, t1
	sw t0, 36(sp)
	# %Array_Node.sz = getelementptr %class.Array_Node, ptr %this, i32 0, i32 1
	mv t0, a0
	li t1, 0
	li t2, 4
	mul t1, t1, t2
	add t0, t0, t1
	li t1, 1
	li t2, 4
	mul t1, t1, t2
	add t0, t0, t1
	sw t0, 40(sp)
	# store i32 %newSize.val, ptr %newSize
	mv t0, a1
	lw t1, 44(sp)
	sw t0, 0(t1)
	# br label %step...2
	j Array_Node.resize_step...2
	.section .text
	.globl Array_Node.resize_step...2
Array_Node.resize_step...2:
	# %60 = load ptr, ptr %Array_Node.storage
	lw t0, 36(sp)
	lw t0, 0(t0)
	sw t0, 52(sp)
	# %array.size.3 = call i32 @array.size(ptr %60)
	sw ra, 20(sp)
	lw a0, 52(sp)
	call array.size
	lw ra, 20(sp)
	sw a0, 56(sp)
	# %61 = load i32, ptr %newSize
	lw t0, 44(sp)
	lw t0, 0(t0)
	sw t0, 60(sp)
	# %less.2 = icmp slt i32 %array.size.3, %61
	lw t0, 56(sp)
	lw t1, 60(sp)
	sub t2, t0, t1
	sltz t2, t2
	sw t2, 64(sp)
	# br i1 %less.2, label %body...2, label %end...2
	lw t0, 64(sp)
	beq t0, x0, Array_Node.resize_end...2
	j Array_Node.resize_body...2
	.section .text
	.globl Array_Node.resize_body...2
Array_Node.resize_body...2:
	# call void @Array_Node.doubleStorage(ptr %this)
	sw ra, 20(sp)
	mv a0, a0
	call Array_Node.doubleStorage
	lw ra, 20(sp)
	# br label %step...2
	j Array_Node.resize_step...2
	.section .text
	.globl Array_Node.resize_end...2
Array_Node.resize_end...2:
	# %62 = load i32, ptr %Array_Node.sz
	lw t0, 40(sp)
	lw t0, 0(t0)
	sw t0, 68(sp)
	# %63 = load i32, ptr %newSize
	lw t0, 44(sp)
	lw t0, 0(t0)
	sw t0, 72(sp)
	# store i32 %63, ptr %Array_Node.sz
	lw t0, 72(sp)
	lw t1, 40(sp)
	sw t0, 0(t1)
	# br label %return
	j Array_Node.resize_return
	.section .text
	.globl Array_Node.resize_return
Array_Node.resize_return:
	# ret void
	lw t0, 8(sp)
	lw t1, 12(sp)
	lw t2, 16(sp)
	addi sp, sp, 76
	ret
	.section .text
	.globl Array_Node.get
Array_Node.get:
	addi sp, sp, -80
	sw t0, 8(sp)
	sw t1, 12(sp)
	sw t2, 16(sp)
	# %Array_Node.this = alloca ptr
	addi t0, sp, 32
	sw t0, 28(sp)
	# %i.1 = alloca i32
	addi t0, sp, 48
	sw t0, 44(sp)
	# %ret.val.6 = alloca ptr
	addi t0, sp, 56
	sw t0, 52(sp)
	# store ptr %this, ptr %Array_Node.this
	mv t0, a0
	lw t1, 28(sp)
	sw t0, 0(t1)
	# %Array_Node.storage = getelementptr %class.Array_Node, ptr %this, i32 0, i32 0
	mv t0, a0
	li t1, 0
	li t2, 4
	mul t1, t1, t2
	add t0, t0, t1
	li t1, 0
	li t2, 4
	mul t1, t1, t2
	add t0, t0, t1
	sw t0, 36(sp)
	# %Array_Node.sz = getelementptr %class.Array_Node, ptr %this, i32 0, i32 1
	mv t0, a0
	li t1, 0
	li t2, 4
	mul t1, t1, t2
	add t0, t0, t1
	li t1, 1
	li t2, 4
	mul t1, t1, t2
	add t0, t0, t1
	sw t0, 40(sp)
	# store i32 %i.val, ptr %i.1
	mv t0, a1
	lw t1, 44(sp)
	sw t0, 0(t1)
	# %64 = load ptr, ptr %Array_Node.storage
	lw t0, 36(sp)
	lw t0, 0(t0)
	sw t0, 60(sp)
	# %65 = load i32, ptr %i.1
	lw t0, 44(sp)
	lw t0, 0(t0)
	sw t0, 64(sp)
	# %array.idx.10 = getelementptr ptr, ptr %64, i32 %65
	lw t0, 60(sp)
	lw t1, 64(sp)
	li t2, 4
	mul t1, t1, t2
	add t0, t0, t1
	sw t0, 68(sp)
	# %array.idx.val.10 = load ptr, ptr %array.idx.10
	lw t0, 68(sp)
	lw t0, 0(t0)
	sw t0, 72(sp)
	# store ptr %array.idx.val.10, ptr %ret.val.6
	lw t0, 72(sp)
	lw t1, 52(sp)
	sw t0, 0(t1)
	# br label %return
	j Array_Node.get_return
	.section .text
	.globl Array_Node.get_return
Array_Node.get_return:
	# %r.e.t.6 = load ptr, ptr %ret.val.6
	lw t0, 52(sp)
	lw t0, 0(t0)
	sw t0, 76(sp)
	# ret ptr %r.e.t.6
	lw a0, 76(sp)
	lw t0, 8(sp)
	lw t1, 12(sp)
	lw t2, 16(sp)
	addi sp, sp, 80
	ret
	.section .text
	.globl Array_Node.set
Array_Node.set:
	addi sp, sp, -80
	sw t0, 8(sp)
	sw t1, 12(sp)
	sw t2, 16(sp)
	# %Array_Node.this = alloca ptr
	addi t0, sp, 32
	sw t0, 28(sp)
	# %i.2 = alloca i32
	addi t0, sp, 48
	sw t0, 44(sp)
	# %v.2 = alloca ptr
	addi t0, sp, 56
	sw t0, 52(sp)
	# store ptr %this, ptr %Array_Node.this
	mv t0, a0
	lw t1, 28(sp)
	sw t0, 0(t1)
	# %Array_Node.storage = getelementptr %class.Array_Node, ptr %this, i32 0, i32 0
	mv t0, a0
	li t1, 0
	li t2, 4
	mul t1, t1, t2
	add t0, t0, t1
	li t1, 0
	li t2, 4
	mul t1, t1, t2
	add t0, t0, t1
	sw t0, 36(sp)
	# %Array_Node.sz = getelementptr %class.Array_Node, ptr %this, i32 0, i32 1
	mv t0, a0
	li t1, 0
	li t2, 4
	mul t1, t1, t2
	add t0, t0, t1
	li t1, 1
	li t2, 4
	mul t1, t1, t2
	add t0, t0, t1
	sw t0, 40(sp)
	# store i32 %i.val, ptr %i.2
	mv t0, a1
	lw t1, 44(sp)
	sw t0, 0(t1)
	# store ptr %v.val, ptr %v.2
	mv t0, a2
	lw t1, 52(sp)
	sw t0, 0(t1)
	# %66 = load ptr, ptr %Array_Node.storage
	lw t0, 36(sp)
	lw t0, 0(t0)
	sw t0, 60(sp)
	# %67 = load i32, ptr %i.2
	lw t0, 44(sp)
	lw t0, 0(t0)
	sw t0, 64(sp)
	# %array.idx.11 = getelementptr ptr, ptr %66, i32 %67
	lw t0, 60(sp)
	lw t1, 64(sp)
	li t2, 4
	mul t1, t1, t2
	add t0, t0, t1
	sw t0, 68(sp)
	# %array.idx.val.11 = load ptr, ptr %array.idx.11
	lw t0, 68(sp)
	lw t0, 0(t0)
	sw t0, 72(sp)
	# %68 = load ptr, ptr %v.2
	lw t0, 52(sp)
	lw t0, 0(t0)
	sw t0, 76(sp)
	# store ptr %68, ptr %array.idx.11
	lw t0, 76(sp)
	lw t1, 68(sp)
	sw t0, 0(t1)
	# br label %return
	j Array_Node.set_return
	.section .text
	.globl Array_Node.set_return
Array_Node.set_return:
	# ret void
	lw t0, 8(sp)
	lw t1, 12(sp)
	lw t2, 16(sp)
	addi sp, sp, 80
	ret
	.section .text
	.globl Array_Node.swap
Array_Node.swap:
	addi sp, sp, -136
	sw t0, 8(sp)
	sw t1, 12(sp)
	sw t2, 16(sp)
	# %Array_Node.this = alloca ptr
	addi t0, sp, 32
	sw t0, 28(sp)
	# %i.3 = alloca i32
	addi t0, sp, 48
	sw t0, 44(sp)
	# %j = alloca i32
	addi t0, sp, 56
	sw t0, 52(sp)
	# %t = alloca ptr
	addi t0, sp, 64
	sw t0, 60(sp)
	# store ptr %this, ptr %Array_Node.this
	mv t0, a0
	lw t1, 28(sp)
	sw t0, 0(t1)
	# %Array_Node.storage = getelementptr %class.Array_Node, ptr %this, i32 0, i32 0
	mv t0, a0
	li t1, 0
	li t2, 4
	mul t1, t1, t2
	add t0, t0, t1
	li t1, 0
	li t2, 4
	mul t1, t1, t2
	add t0, t0, t1
	sw t0, 36(sp)
	# %Array_Node.sz = getelementptr %class.Array_Node, ptr %this, i32 0, i32 1
	mv t0, a0
	li t1, 0
	li t2, 4
	mul t1, t1, t2
	add t0, t0, t1
	li t1, 1
	li t2, 4
	mul t1, t1, t2
	add t0, t0, t1
	sw t0, 40(sp)
	# store i32 %i.val, ptr %i.3
	mv t0, a1
	lw t1, 44(sp)
	sw t0, 0(t1)
	# store i32 %j.val, ptr %j
	mv t0, a2
	lw t1, 52(sp)
	sw t0, 0(t1)
	# %69 = load ptr, ptr %Array_Node.storage
	lw t0, 36(sp)
	lw t0, 0(t0)
	sw t0, 68(sp)
	# %70 = load i32, ptr %i.3
	lw t0, 44(sp)
	lw t0, 0(t0)
	sw t0, 72(sp)
	# %array.idx.12 = getelementptr ptr, ptr %69, i32 %70
	lw t0, 68(sp)
	lw t1, 72(sp)
	li t2, 4
	mul t1, t1, t2
	add t0, t0, t1
	sw t0, 76(sp)
	# %array.idx.val.12 = load ptr, ptr %array.idx.12
	lw t0, 76(sp)
	lw t0, 0(t0)
	sw t0, 80(sp)
	# store ptr %array.idx.val.12, ptr %t
	lw t0, 80(sp)
	lw t1, 60(sp)
	sw t0, 0(t1)
	# %71 = load ptr, ptr %Array_Node.storage
	lw t0, 36(sp)
	lw t0, 0(t0)
	sw t0, 84(sp)
	# %72 = load i32, ptr %i.3
	lw t0, 44(sp)
	lw t0, 0(t0)
	sw t0, 88(sp)
	# %array.idx.13 = getelementptr ptr, ptr %71, i32 %72
	lw t0, 84(sp)
	lw t1, 88(sp)
	li t2, 4
	mul t1, t1, t2
	add t0, t0, t1
	sw t0, 92(sp)
	# %array.idx.val.13 = load ptr, ptr %array.idx.13
	lw t0, 92(sp)
	lw t0, 0(t0)
	sw t0, 96(sp)
	# %73 = load ptr, ptr %Array_Node.storage
	lw t0, 36(sp)
	lw t0, 0(t0)
	sw t0, 100(sp)
	# %74 = load i32, ptr %j
	lw t0, 52(sp)
	lw t0, 0(t0)
	sw t0, 104(sp)
	# %array.idx.14 = getelementptr ptr, ptr %73, i32 %74
	lw t0, 100(sp)
	lw t1, 104(sp)
	li t2, 4
	mul t1, t1, t2
	add t0, t0, t1
	sw t0, 108(sp)
	# %array.idx.val.14 = load ptr, ptr %array.idx.14
	lw t0, 108(sp)
	lw t0, 0(t0)
	sw t0, 112(sp)
	# store ptr %array.idx.val.14, ptr %array.idx.13
	lw t0, 112(sp)
	lw t1, 92(sp)
	sw t0, 0(t1)
	# %75 = load ptr, ptr %Array_Node.storage
	lw t0, 36(sp)
	lw t0, 0(t0)
	sw t0, 116(sp)
	# %76 = load i32, ptr %j
	lw t0, 52(sp)
	lw t0, 0(t0)
	sw t0, 120(sp)
	# %array.idx.15 = getelementptr ptr, ptr %75, i32 %76
	lw t0, 116(sp)
	lw t1, 120(sp)
	li t2, 4
	mul t1, t1, t2
	add t0, t0, t1
	sw t0, 124(sp)
	# %array.idx.val.15 = load ptr, ptr %array.idx.15
	lw t0, 124(sp)
	lw t0, 0(t0)
	sw t0, 128(sp)
	# %77 = load ptr, ptr %t
	lw t0, 60(sp)
	lw t0, 0(t0)
	sw t0, 132(sp)
	# store ptr %77, ptr %array.idx.15
	lw t0, 132(sp)
	lw t1, 124(sp)
	sw t0, 0(t1)
	# br label %return
	j Array_Node.swap_return
	.section .text
	.globl Array_Node.swap_return
Array_Node.swap_return:
	# ret void
	lw t0, 8(sp)
	lw t1, 12(sp)
	lw t2, 16(sp)
	addi sp, sp, 136
	ret
	.section .text
	.globl Array_Node.doubleStorage
Array_Node.doubleStorage:
	addi sp, sp, -152
	sw t0, 8(sp)
	sw t1, 12(sp)
	sw t2, 16(sp)
	# %Array_Node.this = alloca ptr
	addi t0, sp, 32
	sw t0, 28(sp)
	# %copy = alloca ptr
	addi t0, sp, 48
	sw t0, 44(sp)
	# %szCopy = alloca i32
	addi t0, sp, 60
	sw t0, 56(sp)
	# %81 = alloca ptr
	addi t0, sp, 76
	sw t0, 72(sp)
	# store ptr %this, ptr %Array_Node.this
	mv t0, a0
	lw t1, 28(sp)
	sw t0, 0(t1)
	# %Array_Node.storage = getelementptr %class.Array_Node, ptr %this, i32 0, i32 0
	mv t0, a0
	li t1, 0
	li t2, 4
	mul t1, t1, t2
	add t0, t0, t1
	li t1, 0
	li t2, 4
	mul t1, t1, t2
	add t0, t0, t1
	sw t0, 36(sp)
	# %Array_Node.sz = getelementptr %class.Array_Node, ptr %this, i32 0, i32 1
	mv t0, a0
	li t1, 0
	li t2, 4
	mul t1, t1, t2
	add t0, t0, t1
	li t1, 1
	li t2, 4
	mul t1, t1, t2
	add t0, t0, t1
	sw t0, 40(sp)
	# %78 = load ptr, ptr %Array_Node.storage
	lw t0, 36(sp)
	lw t0, 0(t0)
	sw t0, 52(sp)
	# store ptr %78, ptr %copy
	lw t0, 52(sp)
	lw t1, 44(sp)
	sw t0, 0(t1)
	# %79 = load i32, ptr %Array_Node.sz
	lw t0, 40(sp)
	lw t0, 0(t0)
	sw t0, 64(sp)
	# store i32 %79, ptr %szCopy
	lw t0, 64(sp)
	lw t1, 56(sp)
	sw t0, 0(t1)
	# %80 = load ptr, ptr %Array_Node.storage
	lw t0, 36(sp)
	lw t0, 0(t0)
	sw t0, 68(sp)
	# %82 = load ptr, ptr %copy
	lw t0, 44(sp)
	lw t0, 0(t0)
	sw t0, 80(sp)
	# %array.size.4 = call i32 @array.size(ptr %82)
	sw ra, 20(sp)
	lw a0, 80(sp)
	call array.size
	lw ra, 20(sp)
	sw a0, 84(sp)
	# %mul = mul i32 %array.size.4, 2
	lw t0, 84(sp)
	li t1, 2
	mul t2, t0, t1
	sw t2, 88(sp)
	# %call.3 = call ptr @array.alloca(i32 4, i32 1, i32 1, i32 %mul)
	sw ra, 20(sp)
	li a0, 4
	li a1, 1
	li a2, 1
	lw a3, 88(sp)
	call array.alloca
	lw ra, 20(sp)
	sw a0, 92(sp)
	# store ptr %call.3, ptr %81
	lw t0, 92(sp)
	lw t1, 72(sp)
	sw t0, 0(t1)
	# store ptr %call.3, ptr %Array_Node.storage
	lw t0, 92(sp)
	lw t1, 36(sp)
	sw t0, 0(t1)
	# %83 = load i32, ptr %Array_Node.sz
	lw t0, 40(sp)
	lw t0, 0(t0)
	sw t0, 96(sp)
	# store i32 0, ptr %Array_Node.sz
	li t0, 0
	lw t1, 40(sp)
	sw t0, 0(t1)
	# br label %cond...2
	j Array_Node.doubleStorage_cond...2
	.section .text
	.globl Array_Node.doubleStorage_cond...2
Array_Node.doubleStorage_cond...2:
	# %84 = load i32, ptr %Array_Node.sz
	lw t0, 40(sp)
	lw t0, 0(t0)
	sw t0, 100(sp)
	# %85 = load i32, ptr %szCopy
	lw t0, 56(sp)
	lw t0, 0(t0)
	sw t0, 104(sp)
	# %unEq = icmp ne i32 %84, %85
	lw t0, 100(sp)
	lw t1, 104(sp)
	sub t2, t0, t1
	snez t2, t2
	sw t2, 108(sp)
	# br i1 %unEq, label %body...3, label %end...3
	lw t0, 108(sp)
	beq t0, x0, Array_Node.doubleStorage_end...3
	j Array_Node.doubleStorage_body...3
	.section .text
	.globl Array_Node.doubleStorage_body...3
Array_Node.doubleStorage_body...3:
	# %86 = load ptr, ptr %Array_Node.storage
	lw t0, 36(sp)
	lw t0, 0(t0)
	sw t0, 112(sp)
	# %87 = load i32, ptr %Array_Node.sz
	lw t0, 40(sp)
	lw t0, 0(t0)
	sw t0, 116(sp)
	# %array.idx.16 = getelementptr ptr, ptr %86, i32 %87
	lw t0, 112(sp)
	lw t1, 116(sp)
	li t2, 4
	mul t1, t1, t2
	add t0, t0, t1
	sw t0, 120(sp)
	# %array.idx.val.16 = load ptr, ptr %array.idx.16
	lw t0, 120(sp)
	lw t0, 0(t0)
	sw t0, 124(sp)
	# %88 = load ptr, ptr %copy
	lw t0, 44(sp)
	lw t0, 0(t0)
	sw t0, 128(sp)
	# %89 = load i32, ptr %Array_Node.sz
	lw t0, 40(sp)
	lw t0, 0(t0)
	sw t0, 132(sp)
	# %array.idx.17 = getelementptr ptr, ptr %88, i32 %89
	lw t0, 128(sp)
	lw t1, 132(sp)
	li t2, 4
	mul t1, t1, t2
	add t0, t0, t1
	sw t0, 136(sp)
	# %array.idx.val.17 = load ptr, ptr %array.idx.17
	lw t0, 136(sp)
	lw t0, 0(t0)
	sw t0, 140(sp)
	# store ptr %array.idx.val.17, ptr %array.idx.16
	lw t0, 140(sp)
	lw t1, 120(sp)
	sw t0, 0(t1)
	# br label %step...3
	j Array_Node.doubleStorage_step...3
	.section .text
	.globl Array_Node.doubleStorage_step...3
Array_Node.doubleStorage_step...3:
	# %90 = load i32, ptr %Array_Node.sz
	lw t0, 40(sp)
	lw t0, 0(t0)
	sw t0, 144(sp)
	# %pre.Inc.4 = add i32 %90, 1
	lw t0, 144(sp)
	li t1, 1
	add t2, t0, t1
	sw t2, 148(sp)
	# store i32 %pre.Inc.4, ptr %Array_Node.sz
	lw t0, 148(sp)
	lw t1, 40(sp)
	sw t0, 0(t1)
	# br label %cond...2
	j Array_Node.doubleStorage_cond...2
	.section .text
	.globl Array_Node.doubleStorage_end...3
Array_Node.doubleStorage_end...3:
	# br label %return
	j Array_Node.doubleStorage_return
	.section .text
	.globl Array_Node.doubleStorage_return
Array_Node.doubleStorage_return:
	# ret void
	lw t0, 8(sp)
	lw t1, 12(sp)
	lw t2, 16(sp)
	addi sp, sp, 152
	ret
	.section .text
	.globl Array_Node.build
Array_Node.build:
	addi sp, sp, -64
	sw t0, 8(sp)
	sw t1, 12(sp)
	sw t2, 16(sp)
	# %Array_Node.this = alloca ptr
	addi t0, sp, 32
	sw t0, 28(sp)
	# %93 = alloca ptr
	addi t0, sp, 56
	sw t0, 52(sp)
	# store ptr %this, ptr %Array_Node.this
	mv t0, a0
	lw t1, 28(sp)
	sw t0, 0(t1)
	# %Array_Node.storage = getelementptr %class.Array_Node, ptr %this, i32 0, i32 0
	mv t0, a0
	li t1, 0
	li t2, 4
	mul t1, t1, t2
	add t0, t0, t1
	li t1, 0
	li t2, 4
	mul t1, t1, t2
	add t0, t0, t1
	sw t0, 36(sp)
	# %Array_Node.sz = getelementptr %class.Array_Node, ptr %this, i32 0, i32 1
	mv t0, a0
	li t1, 0
	li t2, 4
	mul t1, t1, t2
	add t0, t0, t1
	li t1, 1
	li t2, 4
	mul t1, t1, t2
	add t0, t0, t1
	sw t0, 40(sp)
	# %91 = load i32, ptr %Array_Node.sz
	lw t0, 40(sp)
	lw t0, 0(t0)
	sw t0, 44(sp)
	# store i32 0, ptr %Array_Node.sz
	li t0, 0
	lw t1, 40(sp)
	sw t0, 0(t1)
	# %92 = load ptr, ptr %Array_Node.storage
	lw t0, 36(sp)
	lw t0, 0(t0)
	sw t0, 48(sp)
	# %call.4 = call ptr @array.alloca(i32 4, i32 1, i32 1, i32 16)
	sw ra, 20(sp)
	li a0, 4
	li a1, 1
	li a2, 1
	li a3, 16
	call array.alloca
	lw ra, 20(sp)
	sw a0, 60(sp)
	# store ptr %call.4, ptr %93
	lw t0, 60(sp)
	lw t1, 52(sp)
	sw t0, 0(t1)
	# store ptr %call.4, ptr %Array_Node.storage
	lw t0, 60(sp)
	lw t1, 36(sp)
	sw t0, 0(t1)
	# br label %return
	j Array_Node.build_return
	.section .text
	.globl Array_Node.build_return
Array_Node.build_return:
	# ret void
	lw t0, 8(sp)
	lw t1, 12(sp)
	lw t2, 16(sp)
	addi sp, sp, 64
	ret
	.section .text
	.globl Heap_Node.push
Heap_Node.push:
	addi sp, sp, -152
	sw t0, 8(sp)
	sw t1, 12(sp)
	sw t2, 16(sp)
	# %Heap_Node.this = alloca ptr
	addi t0, sp, 32
	sw t0, 28(sp)
	# %v.3 = alloca ptr
	addi t0, sp, 44
	sw t0, 40(sp)
	# %x = alloca i32
	addi t0, sp, 60
	sw t0, 56(sp)
	# %p = alloca i32
	addi t0, sp, 84
	sw t0, 80(sp)
	# store ptr %this, ptr %Heap_Node.this
	mv t0, a0
	lw t1, 28(sp)
	sw t0, 0(t1)
	# %Heap_Node.storage = getelementptr %class.Heap_Node, ptr %this, i32 0, i32 0
	mv t0, a0
	li t1, 0
	li t2, 4
	mul t1, t1, t2
	add t0, t0, t1
	li t1, 0
	li t2, 4
	mul t1, t1, t2
	add t0, t0, t1
	sw t0, 36(sp)
	# store ptr %v.val, ptr %v.3
	mv t0, a1
	lw t1, 40(sp)
	sw t0, 0(t1)
	# %94 = load ptr, ptr %Heap_Node.storage
	lw t0, 36(sp)
	lw t0, 0(t0)
	sw t0, 48(sp)
	# %95 = load ptr, ptr %v.3
	lw t0, 40(sp)
	lw t0, 0(t0)
	sw t0, 52(sp)
	# call void @Array_Node.push_back(ptr %94, ptr %95)
	sw ra, 20(sp)
	lw a0, 48(sp)
	lw a1, 52(sp)
	call Array_Node.push_back
	lw ra, 20(sp)
	# %call.null.ret.1 = call i32 @Heap_Node.size(ptr %this)
	sw ra, 20(sp)
	mv a0, a0
	call Heap_Node.size
	lw ra, 20(sp)
	sw a0, 64(sp)
	# %sub.1 = sub i32 %call.null.ret.1, 1
	lw t0, 64(sp)
	li t1, 1
	sub t2, t0, t1
	sw t2, 68(sp)
	# store i32 %sub.1, ptr %x
	lw t0, 68(sp)
	lw t1, 56(sp)
	sw t0, 0(t1)
	# br label %step...4
	j Heap_Node.push_step...4
	.section .text
	.globl Heap_Node.push_step...4
Heap_Node.push_step...4:
	# %96 = load i32, ptr %x
	lw t0, 56(sp)
	lw t0, 0(t0)
	sw t0, 72(sp)
	# %greater = icmp sgt i32 %96, 0
	lw t0, 72(sp)
	li t1, 0
	sub t2, t0, t1
	sgtz t2, t2
	sw t2, 76(sp)
	# br i1 %greater, label %body...4, label %end...4
	lw t0, 76(sp)
	beq t0, x0, Heap_Node.push_end...4
	j Heap_Node.push_body...4
	.section .text
	.globl Heap_Node.push_body...4
Heap_Node.push_body...4:
	# %97 = load i32, ptr %x
	lw t0, 56(sp)
	lw t0, 0(t0)
	sw t0, 88(sp)
	# %call.null.ret.2 = call i32 @Heap_Node.pnt(ptr %this, i32 %97)
	sw ra, 20(sp)
	mv a0, a0
	lw a1, 88(sp)
	call Heap_Node.pnt
	lw ra, 20(sp)
	sw a0, 92(sp)
	# store i32 %call.null.ret.2, ptr %p
	lw t0, 92(sp)
	lw t1, 80(sp)
	sw t0, 0(t1)
	# %98 = load ptr, ptr %Heap_Node.storage
	lw t0, 36(sp)
	lw t0, 0(t0)
	sw t0, 96(sp)
	# %99 = load i32, ptr %p
	lw t0, 80(sp)
	lw t0, 0(t0)
	sw t0, 100(sp)
	# %call.null.ret.3 = call ptr @Array_Node.get(ptr %98, i32 %99)
	sw ra, 20(sp)
	lw a0, 96(sp)
	lw a1, 100(sp)
	call Array_Node.get
	lw ra, 20(sp)
	sw a0, 104(sp)
	# %call.null.ret.4 = call i32 @Node.key_(ptr %call.null.ret.3)
	sw ra, 20(sp)
	lw a0, 104(sp)
	call Node.key_
	lw ra, 20(sp)
	sw a0, 108(sp)
	# %100 = load ptr, ptr %Heap_Node.storage
	lw t0, 36(sp)
	lw t0, 0(t0)
	sw t0, 112(sp)
	# %101 = load i32, ptr %x
	lw t0, 56(sp)
	lw t0, 0(t0)
	sw t0, 116(sp)
	# %call.null.ret.5 = call ptr @Array_Node.get(ptr %100, i32 %101)
	sw ra, 20(sp)
	lw a0, 112(sp)
	lw a1, 116(sp)
	call Array_Node.get
	lw ra, 20(sp)
	sw a0, 120(sp)
	# %call.null.ret.6 = call i32 @Node.key_(ptr %call.null.ret.5)
	sw ra, 20(sp)
	lw a0, 120(sp)
	call Node.key_
	lw ra, 20(sp)
	sw a0, 124(sp)
	# %grEq = icmp sge i32 %call.null.ret.4, %call.null.ret.6
	lw t0, 108(sp)
	lw t1, 124(sp)
	sub t2, t0, t1
	sgtz t0, t2
	seqz t1, t2
	or t2, t0, t1
	sw t2, 128(sp)
	# br i1 %grEq, label %if.true.1, label %if.false.1
	lw t0, 128(sp)
	beq t0, x0, Heap_Node.push_if.false.1
	j Heap_Node.push_if.true.1
	.section .text
	.globl Heap_Node.push_if.true.1
Heap_Node.push_if.true.1:
	# br label %end...4
	j Heap_Node.push_end...4
	.section .text
	.globl Heap_Node.push_if.false.1
Heap_Node.push_if.false.1:
	# br label %if.end.1
	j Heap_Node.push_if.end.1
	.section .text
	.globl Heap_Node.push_if.end.1
Heap_Node.push_if.end.1:
	# %102 = load ptr, ptr %Heap_Node.storage
	lw t0, 36(sp)
	lw t0, 0(t0)
	sw t0, 132(sp)
	# %103 = load i32, ptr %p
	lw t0, 80(sp)
	lw t0, 0(t0)
	sw t0, 136(sp)
	# %104 = load i32, ptr %x
	lw t0, 56(sp)
	lw t0, 0(t0)
	sw t0, 140(sp)
	# call void @Array_Node.swap(ptr %102, i32 %103, i32 %104)
	sw ra, 20(sp)
	lw a0, 132(sp)
	lw a1, 136(sp)
	lw a2, 140(sp)
	call Array_Node.swap
	lw ra, 20(sp)
	# %105 = load i32, ptr %x
	lw t0, 56(sp)
	lw t0, 0(t0)
	sw t0, 144(sp)
	# %106 = load i32, ptr %p
	lw t0, 80(sp)
	lw t0, 0(t0)
	sw t0, 148(sp)
	# store i32 %106, ptr %x
	lw t0, 148(sp)
	lw t1, 56(sp)
	sw t0, 0(t1)
	# br label %step...4
	j Heap_Node.push_step...4
	.section .text
	.globl Heap_Node.push_end...4
Heap_Node.push_end...4:
	# br label %return
	j Heap_Node.push_return
	.section .text
	.globl Heap_Node.push_return
Heap_Node.push_return:
	# ret void
	lw t0, 8(sp)
	lw t1, 12(sp)
	lw t2, 16(sp)
	addi sp, sp, 152
	ret
	.section .text
	.globl Heap_Node.pop
Heap_Node.pop:
	addi sp, sp, -92
	sw t0, 8(sp)
	sw t1, 12(sp)
	sw t2, 16(sp)
	# %Heap_Node.this = alloca ptr
	addi t0, sp, 32
	sw t0, 28(sp)
	# %ret.val.7 = alloca ptr
	addi t0, sp, 44
	sw t0, 40(sp)
	# %res = alloca ptr
	addi t0, sp, 52
	sw t0, 48(sp)
	# store ptr %this, ptr %Heap_Node.this
	mv t0, a0
	lw t1, 28(sp)
	sw t0, 0(t1)
	# %Heap_Node.storage = getelementptr %class.Heap_Node, ptr %this, i32 0, i32 0
	mv t0, a0
	li t1, 0
	li t2, 4
	mul t1, t1, t2
	add t0, t0, t1
	li t1, 0
	li t2, 4
	mul t1, t1, t2
	add t0, t0, t1
	sw t0, 36(sp)
	# %107 = load ptr, ptr %Heap_Node.storage
	lw t0, 36(sp)
	lw t0, 0(t0)
	sw t0, 56(sp)
	# %call.null.ret.7 = call ptr @Array_Node.front(ptr %107)
	sw ra, 20(sp)
	lw a0, 56(sp)
	call Array_Node.front
	lw ra, 20(sp)
	sw a0, 60(sp)
	# store ptr %call.null.ret.7, ptr %res
	lw t0, 60(sp)
	lw t1, 48(sp)
	sw t0, 0(t1)
	# %108 = load ptr, ptr %Heap_Node.storage
	lw t0, 36(sp)
	lw t0, 0(t0)
	sw t0, 64(sp)
	# %call.null.ret.8 = call i32 @Heap_Node.size(ptr %this)
	sw ra, 20(sp)
	mv a0, a0
	call Heap_Node.size
	lw ra, 20(sp)
	sw a0, 68(sp)
	# %sub.2 = sub i32 %call.null.ret.8, 1
	lw t0, 68(sp)
	li t1, 1
	sub t2, t0, t1
	sw t2, 72(sp)
	# call void @Array_Node.swap(ptr %108, i32 0, i32 %sub.2)
	sw ra, 20(sp)
	lw a0, 64(sp)
	li a1, 0
	lw a2, 72(sp)
	call Array_Node.swap
	lw ra, 20(sp)
	# %109 = load ptr, ptr %Heap_Node.storage
	lw t0, 36(sp)
	lw t0, 0(t0)
	sw t0, 76(sp)
	# %call.null.ret.9 = call ptr @Array_Node.pop_back(ptr %109)
	sw ra, 20(sp)
	lw a0, 76(sp)
	call Array_Node.pop_back
	lw ra, 20(sp)
	sw a0, 80(sp)
	# call void @Heap_Node.maxHeapify(ptr %this, i32 0)
	sw ra, 20(sp)
	mv a0, a0
	li a1, 0
	call Heap_Node.maxHeapify
	lw ra, 20(sp)
	# %110 = load ptr, ptr %res
	lw t0, 48(sp)
	lw t0, 0(t0)
	sw t0, 84(sp)
	# store ptr %110, ptr %ret.val.7
	lw t0, 84(sp)
	lw t1, 40(sp)
	sw t0, 0(t1)
	# br label %return
	j Heap_Node.pop_return
	.section .text
	.globl Heap_Node.pop_return
Heap_Node.pop_return:
	# %r.e.t.7 = load ptr, ptr %ret.val.7
	lw t0, 40(sp)
	lw t0, 0(t0)
	sw t0, 88(sp)
	# ret ptr %r.e.t.7
	lw a0, 88(sp)
	lw t0, 8(sp)
	lw t1, 12(sp)
	lw t2, 16(sp)
	addi sp, sp, 92
	ret
	.section .text
	.globl Heap_Node.top
Heap_Node.top:
	addi sp, sp, -60
	sw t0, 8(sp)
	sw t1, 12(sp)
	sw t2, 16(sp)
	# %Heap_Node.this = alloca ptr
	addi t0, sp, 32
	sw t0, 28(sp)
	# %ret.val.8 = alloca ptr
	addi t0, sp, 44
	sw t0, 40(sp)
	# store ptr %this, ptr %Heap_Node.this
	mv t0, a0
	lw t1, 28(sp)
	sw t0, 0(t1)
	# %Heap_Node.storage = getelementptr %class.Heap_Node, ptr %this, i32 0, i32 0
	mv t0, a0
	li t1, 0
	li t2, 4
	mul t1, t1, t2
	add t0, t0, t1
	li t1, 0
	li t2, 4
	mul t1, t1, t2
	add t0, t0, t1
	sw t0, 36(sp)
	# %111 = load ptr, ptr %Heap_Node.storage
	lw t0, 36(sp)
	lw t0, 0(t0)
	sw t0, 48(sp)
	# %call.null.ret.10 = call ptr @Array_Node.front(ptr %111)
	sw ra, 20(sp)
	lw a0, 48(sp)
	call Array_Node.front
	lw ra, 20(sp)
	sw a0, 52(sp)
	# store ptr %call.null.ret.10, ptr %ret.val.8
	lw t0, 52(sp)
	lw t1, 40(sp)
	sw t0, 0(t1)
	# br label %return
	j Heap_Node.top_return
	.section .text
	.globl Heap_Node.top_return
Heap_Node.top_return:
	# %r.e.t.8 = load ptr, ptr %ret.val.8
	lw t0, 40(sp)
	lw t0, 0(t0)
	sw t0, 56(sp)
	# ret ptr %r.e.t.8
	lw a0, 56(sp)
	lw t0, 8(sp)
	lw t1, 12(sp)
	lw t2, 16(sp)
	addi sp, sp, 60
	ret
	.section .text
	.globl Heap_Node.size
Heap_Node.size:
	addi sp, sp, -60
	sw t0, 8(sp)
	sw t1, 12(sp)
	sw t2, 16(sp)
	# %Heap_Node.this = alloca ptr
	addi t0, sp, 32
	sw t0, 28(sp)
	# %ret.val.9 = alloca i32
	addi t0, sp, 44
	sw t0, 40(sp)
	# store ptr %this, ptr %Heap_Node.this
	mv t0, a0
	lw t1, 28(sp)
	sw t0, 0(t1)
	# %Heap_Node.storage = getelementptr %class.Heap_Node, ptr %this, i32 0, i32 0
	mv t0, a0
	li t1, 0
	li t2, 4
	mul t1, t1, t2
	add t0, t0, t1
	li t1, 0
	li t2, 4
	mul t1, t1, t2
	add t0, t0, t1
	sw t0, 36(sp)
	# %112 = load ptr, ptr %Heap_Node.storage
	lw t0, 36(sp)
	lw t0, 0(t0)
	sw t0, 48(sp)
	# %call.null.ret.11 = call i32 @Array_Node.size(ptr %112)
	sw ra, 20(sp)
	lw a0, 48(sp)
	call Array_Node.size
	lw ra, 20(sp)
	sw a0, 52(sp)
	# store i32 %call.null.ret.11, ptr %ret.val.9
	lw t0, 52(sp)
	lw t1, 40(sp)
	sw t0, 0(t1)
	# br label %return
	j Heap_Node.size_return
	.section .text
	.globl Heap_Node.size_return
Heap_Node.size_return:
	# %r.e.t.9 = load i32, ptr %ret.val.9
	lw t0, 40(sp)
	lw t0, 0(t0)
	sw t0, 56(sp)
	# ret i32 %r.e.t.9
	lw a0, 56(sp)
	lw t0, 8(sp)
	lw t1, 12(sp)
	lw t2, 16(sp)
	addi sp, sp, 60
	ret
	.section .text
	.globl Heap_Node.lchild
Heap_Node.lchild:
	addi sp, sp, -72
	sw t0, 8(sp)
	sw t1, 12(sp)
	sw t2, 16(sp)
	# %Heap_Node.this = alloca ptr
	addi t0, sp, 32
	sw t0, 28(sp)
	# %x.1 = alloca i32
	addi t0, sp, 44
	sw t0, 40(sp)
	# %ret.val.10 = alloca i32
	addi t0, sp, 52
	sw t0, 48(sp)
	# store ptr %this, ptr %Heap_Node.this
	mv t0, a0
	lw t1, 28(sp)
	sw t0, 0(t1)
	# %Heap_Node.storage = getelementptr %class.Heap_Node, ptr %this, i32 0, i32 0
	mv t0, a0
	li t1, 0
	li t2, 4
	mul t1, t1, t2
	add t0, t0, t1
	li t1, 0
	li t2, 4
	mul t1, t1, t2
	add t0, t0, t1
	sw t0, 36(sp)
	# store i32 %x.val, ptr %x.1
	mv t0, a1
	lw t1, 40(sp)
	sw t0, 0(t1)
	# %113 = load i32, ptr %x.1
	lw t0, 40(sp)
	lw t0, 0(t0)
	sw t0, 56(sp)
	# %mul.1 = mul i32 %113, 2
	lw t0, 56(sp)
	li t1, 2
	mul t2, t0, t1
	sw t2, 60(sp)
	# %add = add i32 %mul.1, 1
	lw t0, 60(sp)
	li t1, 1
	add t2, t0, t1
	sw t2, 64(sp)
	# store i32 %add, ptr %ret.val.10
	lw t0, 64(sp)
	lw t1, 48(sp)
	sw t0, 0(t1)
	# br label %return
	j Heap_Node.lchild_return
	.section .text
	.globl Heap_Node.lchild_return
Heap_Node.lchild_return:
	# %r.e.t.10 = load i32, ptr %ret.val.10
	lw t0, 48(sp)
	lw t0, 0(t0)
	sw t0, 68(sp)
	# ret i32 %r.e.t.10
	lw a0, 68(sp)
	lw t0, 8(sp)
	lw t1, 12(sp)
	lw t2, 16(sp)
	addi sp, sp, 72
	ret
	.section .text
	.globl Heap_Node.rchild
Heap_Node.rchild:
	addi sp, sp, -72
	sw t0, 8(sp)
	sw t1, 12(sp)
	sw t2, 16(sp)
	# %Heap_Node.this = alloca ptr
	addi t0, sp, 32
	sw t0, 28(sp)
	# %x.2 = alloca i32
	addi t0, sp, 44
	sw t0, 40(sp)
	# %ret.val.11 = alloca i32
	addi t0, sp, 52
	sw t0, 48(sp)
	# store ptr %this, ptr %Heap_Node.this
	mv t0, a0
	lw t1, 28(sp)
	sw t0, 0(t1)
	# %Heap_Node.storage = getelementptr %class.Heap_Node, ptr %this, i32 0, i32 0
	mv t0, a0
	li t1, 0
	li t2, 4
	mul t1, t1, t2
	add t0, t0, t1
	li t1, 0
	li t2, 4
	mul t1, t1, t2
	add t0, t0, t1
	sw t0, 36(sp)
	# store i32 %x.val, ptr %x.2
	mv t0, a1
	lw t1, 40(sp)
	sw t0, 0(t1)
	# %114 = load i32, ptr %x.2
	lw t0, 40(sp)
	lw t0, 0(t0)
	sw t0, 56(sp)
	# %mul.2 = mul i32 %114, 2
	lw t0, 56(sp)
	li t1, 2
	mul t2, t0, t1
	sw t2, 60(sp)
	# %add.1 = add i32 %mul.2, 2
	lw t0, 60(sp)
	li t1, 2
	add t2, t0, t1
	sw t2, 64(sp)
	# store i32 %add.1, ptr %ret.val.11
	lw t0, 64(sp)
	lw t1, 48(sp)
	sw t0, 0(t1)
	# br label %return
	j Heap_Node.rchild_return
	.section .text
	.globl Heap_Node.rchild_return
Heap_Node.rchild_return:
	# %r.e.t.11 = load i32, ptr %ret.val.11
	lw t0, 48(sp)
	lw t0, 0(t0)
	sw t0, 68(sp)
	# ret i32 %r.e.t.11
	lw a0, 68(sp)
	lw t0, 8(sp)
	lw t1, 12(sp)
	lw t2, 16(sp)
	addi sp, sp, 72
	ret
	.section .text
	.globl Heap_Node.pnt
Heap_Node.pnt:
	addi sp, sp, -72
	sw t0, 8(sp)
	sw t1, 12(sp)
	sw t2, 16(sp)
	# %Heap_Node.this = alloca ptr
	addi t0, sp, 32
	sw t0, 28(sp)
	# %x.3 = alloca i32
	addi t0, sp, 44
	sw t0, 40(sp)
	# %ret.val.12 = alloca i32
	addi t0, sp, 52
	sw t0, 48(sp)
	# store ptr %this, ptr %Heap_Node.this
	mv t0, a0
	lw t1, 28(sp)
	sw t0, 0(t1)
	# %Heap_Node.storage = getelementptr %class.Heap_Node, ptr %this, i32 0, i32 0
	mv t0, a0
	li t1, 0
	li t2, 4
	mul t1, t1, t2
	add t0, t0, t1
	li t1, 0
	li t2, 4
	mul t1, t1, t2
	add t0, t0, t1
	sw t0, 36(sp)
	# store i32 %x.val, ptr %x.3
	mv t0, a1
	lw t1, 40(sp)
	sw t0, 0(t1)
	# %115 = load i32, ptr %x.3
	lw t0, 40(sp)
	lw t0, 0(t0)
	sw t0, 56(sp)
	# %sub.3 = sub i32 %115, 1
	lw t0, 56(sp)
	li t1, 1
	sub t2, t0, t1
	sw t2, 60(sp)
	# %div = sdiv i32 %sub.3, 2
	lw t0, 60(sp)
	li t1, 2
	div t2, t0, t1
	sw t2, 64(sp)
	# store i32 %div, ptr %ret.val.12
	lw t0, 64(sp)
	lw t1, 48(sp)
	sw t0, 0(t1)
	# br label %return
	j Heap_Node.pnt_return
	.section .text
	.globl Heap_Node.pnt_return
Heap_Node.pnt_return:
	# %r.e.t.12 = load i32, ptr %ret.val.12
	lw t0, 48(sp)
	lw t0, 0(t0)
	sw t0, 68(sp)
	# ret i32 %r.e.t.12
	lw a0, 68(sp)
	lw t0, 8(sp)
	lw t1, 12(sp)
	lw t2, 16(sp)
	addi sp, sp, 72
	ret
	.section .text
	.globl Heap_Node.maxHeapify
Heap_Node.maxHeapify:
	addi sp, sp, -256
	sw t0, 8(sp)
	sw t1, 12(sp)
	sw t2, 16(sp)
	# %Heap_Node.this = alloca ptr
	addi t0, sp, 32
	sw t0, 28(sp)
	# %x.4 = alloca i32
	addi t0, sp, 44
	sw t0, 40(sp)
	# %l = alloca i32
	addi t0, sp, 52
	sw t0, 48(sp)
	# %r = alloca i32
	addi t0, sp, 68
	sw t0, 64(sp)
	# %largest = alloca i32
	addi t0, sp, 84
	sw t0, 80(sp)
	# %shortcircuit.ptr = alloca i1
	addi t0, sp, 108
	sw t0, 104(sp)
	# %shortcircuit.ptr.1 = alloca i1
	addi t0, sp, 176
	sw t0, 172(sp)
	# store ptr %this, ptr %Heap_Node.this
	mv t0, a0
	lw t1, 28(sp)
	sw t0, 0(t1)
	# %Heap_Node.storage = getelementptr %class.Heap_Node, ptr %this, i32 0, i32 0
	mv t0, a0
	li t1, 0
	li t2, 4
	mul t1, t1, t2
	add t0, t0, t1
	li t1, 0
	li t2, 4
	mul t1, t1, t2
	add t0, t0, t1
	sw t0, 36(sp)
	# store i32 %x.val, ptr %x.4
	mv t0, a1
	lw t1, 40(sp)
	sw t0, 0(t1)
	# %116 = load i32, ptr %x.4
	lw t0, 40(sp)
	lw t0, 0(t0)
	sw t0, 56(sp)
	# %call.null.ret.12 = call i32 @Heap_Node.lchild(ptr %this, i32 %116)
	sw ra, 20(sp)
	mv a0, a0
	lw a1, 56(sp)
	call Heap_Node.lchild
	lw ra, 20(sp)
	sw a0, 60(sp)
	# store i32 %call.null.ret.12, ptr %l
	lw t0, 60(sp)
	lw t1, 48(sp)
	sw t0, 0(t1)
	# %117 = load i32, ptr %x.4
	lw t0, 40(sp)
	lw t0, 0(t0)
	sw t0, 72(sp)
	# %call.null.ret.13 = call i32 @Heap_Node.rchild(ptr %this, i32 %117)
	sw ra, 20(sp)
	mv a0, a0
	lw a1, 72(sp)
	call Heap_Node.rchild
	lw ra, 20(sp)
	sw a0, 76(sp)
	# store i32 %call.null.ret.13, ptr %r
	lw t0, 76(sp)
	lw t1, 64(sp)
	sw t0, 0(t1)
	# %118 = load i32, ptr %x.4
	lw t0, 40(sp)
	lw t0, 0(t0)
	sw t0, 88(sp)
	# store i32 %118, ptr %largest
	lw t0, 88(sp)
	lw t1, 80(sp)
	sw t0, 0(t1)
	# %119 = load i32, ptr %l
	lw t0, 48(sp)
	lw t0, 0(t0)
	sw t0, 92(sp)
	# %call.null.ret.14 = call i32 @Heap_Node.size(ptr %this)
	sw ra, 20(sp)
	mv a0, a0
	call Heap_Node.size
	lw ra, 20(sp)
	sw a0, 96(sp)
	# %less.3 = icmp slt i32 %119, %call.null.ret.14
	lw t0, 92(sp)
	lw t1, 96(sp)
	sub t2, t0, t1
	sltz t2, t2
	sw t2, 100(sp)
	# store i1 %less.3, ptr %shortcircuit.ptr
	lw t0, 100(sp)
	lw t1, 104(sp)
	sw t0, 0(t1)
	# br i1 %less.3, label %shortcircuit.next, label %shortcircuit.end
	lw t0, 100(sp)
	beq t0, x0, Heap_Node.maxHeapify_shortcircuit.end
	j Heap_Node.maxHeapify_shortcircuit.next
	.section .text
	.globl Heap_Node.maxHeapify_shortcircuit.next
Heap_Node.maxHeapify_shortcircuit.next:
	# %120 = load ptr, ptr %Heap_Node.storage
	lw t0, 36(sp)
	lw t0, 0(t0)
	sw t0, 112(sp)
	# %121 = load i32, ptr %l
	lw t0, 48(sp)
	lw t0, 0(t0)
	sw t0, 116(sp)
	# %call.null.ret.15 = call ptr @Array_Node.get(ptr %120, i32 %121)
	sw ra, 20(sp)
	lw a0, 112(sp)
	lw a1, 116(sp)
	call Array_Node.get
	lw ra, 20(sp)
	sw a0, 120(sp)
	# %call.null.ret.16 = call i32 @Node.key_(ptr %call.null.ret.15)
	sw ra, 20(sp)
	lw a0, 120(sp)
	call Node.key_
	lw ra, 20(sp)
	sw a0, 124(sp)
	# %122 = load ptr, ptr %Heap_Node.storage
	lw t0, 36(sp)
	lw t0, 0(t0)
	sw t0, 128(sp)
	# %123 = load i32, ptr %largest
	lw t0, 80(sp)
	lw t0, 0(t0)
	sw t0, 132(sp)
	# %call.null.ret.17 = call ptr @Array_Node.get(ptr %122, i32 %123)
	sw ra, 20(sp)
	lw a0, 128(sp)
	lw a1, 132(sp)
	call Array_Node.get
	lw ra, 20(sp)
	sw a0, 136(sp)
	# %call.null.ret.18 = call i32 @Node.key_(ptr %call.null.ret.17)
	sw ra, 20(sp)
	lw a0, 136(sp)
	call Node.key_
	lw ra, 20(sp)
	sw a0, 140(sp)
	# %greater.1 = icmp sgt i32 %call.null.ret.16, %call.null.ret.18
	lw t0, 124(sp)
	lw t1, 140(sp)
	sub t2, t0, t1
	sgtz t2, t2
	sw t2, 144(sp)
	# store i1 %greater.1, ptr %shortcircuit.ptr
	lw t0, 144(sp)
	lw t1, 104(sp)
	sw t0, 0(t1)
	# br label %shortcircuit.end
	j Heap_Node.maxHeapify_shortcircuit.end
	.section .text
	.globl Heap_Node.maxHeapify_shortcircuit.end
Heap_Node.maxHeapify_shortcircuit.end:
	# %124 = load i1, ptr %shortcircuit.ptr
	lw t0, 104(sp)
	lw t0, 0(t0)
	sw t0, 148(sp)
	# br i1 %124, label %if.true.2, label %if.false.2
	lw t0, 148(sp)
	beq t0, x0, Heap_Node.maxHeapify_if.false.2
	j Heap_Node.maxHeapify_if.true.2
	.section .text
	.globl Heap_Node.maxHeapify_if.true.2
Heap_Node.maxHeapify_if.true.2:
	# %125 = load i32, ptr %largest
	lw t0, 80(sp)
	lw t0, 0(t0)
	sw t0, 152(sp)
	# %126 = load i32, ptr %l
	lw t0, 48(sp)
	lw t0, 0(t0)
	sw t0, 156(sp)
	# store i32 %126, ptr %largest
	lw t0, 156(sp)
	lw t1, 80(sp)
	sw t0, 0(t1)
	# br label %if.end.2
	j Heap_Node.maxHeapify_if.end.2
	.section .text
	.globl Heap_Node.maxHeapify_if.false.2
Heap_Node.maxHeapify_if.false.2:
	# br label %if.end.2
	j Heap_Node.maxHeapify_if.end.2
	.section .text
	.globl Heap_Node.maxHeapify_if.end.2
Heap_Node.maxHeapify_if.end.2:
	# %127 = load i32, ptr %r
	lw t0, 64(sp)
	lw t0, 0(t0)
	sw t0, 160(sp)
	# %call.null.ret.19 = call i32 @Heap_Node.size(ptr %this)
	sw ra, 20(sp)
	mv a0, a0
	call Heap_Node.size
	lw ra, 20(sp)
	sw a0, 164(sp)
	# %less.4 = icmp slt i32 %127, %call.null.ret.19
	lw t0, 160(sp)
	lw t1, 164(sp)
	sub t2, t0, t1
	sltz t2, t2
	sw t2, 168(sp)
	# store i1 %less.4, ptr %shortcircuit.ptr.1
	lw t0, 168(sp)
	lw t1, 172(sp)
	sw t0, 0(t1)
	# br i1 %less.4, label %shortcircuit.next.1, label %shortcircuit.end.1
	lw t0, 168(sp)
	beq t0, x0, Heap_Node.maxHeapify_shortcircuit.end.1
	j Heap_Node.maxHeapify_shortcircuit.next.1
	.section .text
	.globl Heap_Node.maxHeapify_shortcircuit.next.1
Heap_Node.maxHeapify_shortcircuit.next.1:
	# %128 = load ptr, ptr %Heap_Node.storage
	lw t0, 36(sp)
	lw t0, 0(t0)
	sw t0, 180(sp)
	# %129 = load i32, ptr %r
	lw t0, 64(sp)
	lw t0, 0(t0)
	sw t0, 184(sp)
	# %call.null.ret.20 = call ptr @Array_Node.get(ptr %128, i32 %129)
	sw ra, 20(sp)
	lw a0, 180(sp)
	lw a1, 184(sp)
	call Array_Node.get
	lw ra, 20(sp)
	sw a0, 188(sp)
	# %call.null.ret.21 = call i32 @Node.key_(ptr %call.null.ret.20)
	sw ra, 20(sp)
	lw a0, 188(sp)
	call Node.key_
	lw ra, 20(sp)
	sw a0, 192(sp)
	# %130 = load ptr, ptr %Heap_Node.storage
	lw t0, 36(sp)
	lw t0, 0(t0)
	sw t0, 196(sp)
	# %131 = load i32, ptr %largest
	lw t0, 80(sp)
	lw t0, 0(t0)
	sw t0, 200(sp)
	# %call.null.ret.22 = call ptr @Array_Node.get(ptr %130, i32 %131)
	sw ra, 20(sp)
	lw a0, 196(sp)
	lw a1, 200(sp)
	call Array_Node.get
	lw ra, 20(sp)
	sw a0, 204(sp)
	# %call.null.ret.23 = call i32 @Node.key_(ptr %call.null.ret.22)
	sw ra, 20(sp)
	lw a0, 204(sp)
	call Node.key_
	lw ra, 20(sp)
	sw a0, 208(sp)
	# %greater.2 = icmp sgt i32 %call.null.ret.21, %call.null.ret.23
	lw t0, 192(sp)
	lw t1, 208(sp)
	sub t2, t0, t1
	sgtz t2, t2
	sw t2, 212(sp)
	# store i1 %greater.2, ptr %shortcircuit.ptr.1
	lw t0, 212(sp)
	lw t1, 172(sp)
	sw t0, 0(t1)
	# br label %shortcircuit.end.1
	j Heap_Node.maxHeapify_shortcircuit.end.1
	.section .text
	.globl Heap_Node.maxHeapify_shortcircuit.end.1
Heap_Node.maxHeapify_shortcircuit.end.1:
	# %132 = load i1, ptr %shortcircuit.ptr.1
	lw t0, 172(sp)
	lw t0, 0(t0)
	sw t0, 216(sp)
	# br i1 %132, label %if.true.3, label %if.false.3
	lw t0, 216(sp)
	beq t0, x0, Heap_Node.maxHeapify_if.false.3
	j Heap_Node.maxHeapify_if.true.3
	.section .text
	.globl Heap_Node.maxHeapify_if.true.3
Heap_Node.maxHeapify_if.true.3:
	# %133 = load i32, ptr %largest
	lw t0, 80(sp)
	lw t0, 0(t0)
	sw t0, 220(sp)
	# %134 = load i32, ptr %r
	lw t0, 64(sp)
	lw t0, 0(t0)
	sw t0, 224(sp)
	# store i32 %134, ptr %largest
	lw t0, 224(sp)
	lw t1, 80(sp)
	sw t0, 0(t1)
	# br label %if.end.3
	j Heap_Node.maxHeapify_if.end.3
	.section .text
	.globl Heap_Node.maxHeapify_if.false.3
Heap_Node.maxHeapify_if.false.3:
	# br label %if.end.3
	j Heap_Node.maxHeapify_if.end.3
	.section .text
	.globl Heap_Node.maxHeapify_if.end.3
Heap_Node.maxHeapify_if.end.3:
	# %135 = load i32, ptr %largest
	lw t0, 80(sp)
	lw t0, 0(t0)
	sw t0, 228(sp)
	# %136 = load i32, ptr %x.4
	lw t0, 40(sp)
	lw t0, 0(t0)
	sw t0, 232(sp)
	# %eq.1 = icmp eq i32 %135, %136
	lw t0, 228(sp)
	lw t1, 232(sp)
	sub t2, t0, t1
	seqz t2, t2
	sw t2, 236(sp)
	# br i1 %eq.1, label %if.true.4, label %if.false.4
	lw t0, 236(sp)
	beq t0, x0, Heap_Node.maxHeapify_if.false.4
	j Heap_Node.maxHeapify_if.true.4
	.section .text
	.globl Heap_Node.maxHeapify_if.true.4
Heap_Node.maxHeapify_if.true.4:
	# br label %return
	j Heap_Node.maxHeapify_return
	.section .text
	.globl Heap_Node.maxHeapify_if.false.4
Heap_Node.maxHeapify_if.false.4:
	# br label %if.end.4
	j Heap_Node.maxHeapify_if.end.4
	.section .text
	.globl Heap_Node.maxHeapify_if.end.4
Heap_Node.maxHeapify_if.end.4:
	# %137 = load ptr, ptr %Heap_Node.storage
	lw t0, 36(sp)
	lw t0, 0(t0)
	sw t0, 240(sp)
	# %138 = load i32, ptr %x.4
	lw t0, 40(sp)
	lw t0, 0(t0)
	sw t0, 244(sp)
	# %139 = load i32, ptr %largest
	lw t0, 80(sp)
	lw t0, 0(t0)
	sw t0, 248(sp)
	# call void @Array_Node.swap(ptr %137, i32 %138, i32 %139)
	sw ra, 20(sp)
	lw a0, 240(sp)
	lw a1, 244(sp)
	lw a2, 248(sp)
	call Array_Node.swap
	lw ra, 20(sp)
	# %140 = load i32, ptr %largest
	lw t0, 80(sp)
	lw t0, 0(t0)
	sw t0, 252(sp)
	# call void @Heap_Node.maxHeapify(ptr %this, i32 %140)
	sw ra, 20(sp)
	mv a0, a0
	lw a1, 252(sp)
	call Heap_Node.maxHeapify
	lw ra, 20(sp)
	# br label %return
	j Heap_Node.maxHeapify_return
	.section .text
	.globl Heap_Node.maxHeapify_return
Heap_Node.maxHeapify_return:
	# ret void
	lw t0, 8(sp)
	lw t1, 12(sp)
	lw t2, 16(sp)
	addi sp, sp, 256
	ret
	.section .text
	.globl Heap_Node.build
Heap_Node.build:
	addi sp, sp, -48
	sw t0, 8(sp)
	sw t1, 12(sp)
	sw t2, 16(sp)
	# %Heap_Node.this = alloca ptr
	addi t0, sp, 32
	sw t0, 28(sp)
	# store ptr %this, ptr %Heap_Node.this
	mv t0, a0
	lw t1, 28(sp)
	sw t0, 0(t1)
	# %Heap_Node.storage = getelementptr %class.Heap_Node, ptr %this, i32 0, i32 0
	mv t0, a0
	li t1, 0
	li t2, 4
	mul t1, t1, t2
	add t0, t0, t1
	li t1, 0
	li t2, 4
	mul t1, t1, t2
	add t0, t0, t1
	sw t0, 36(sp)
	# %141 = load ptr, ptr %Heap_Node.storage
	lw t0, 36(sp)
	lw t0, 0(t0)
	sw t0, 40(sp)
	# %142 = call ptr @malloc(i32 8)
	sw ra, 20(sp)
	li a0, 8
	call malloc
	lw ra, 20(sp)
	sw a0, 44(sp)
	# call void @Array_Node.build(ptr %142)
	sw ra, 20(sp)
	lw a0, 44(sp)
	call Array_Node.build
	lw ra, 20(sp)
	# store ptr %142, ptr %Heap_Node.storage
	lw t0, 44(sp)
	lw t1, 36(sp)
	sw t0, 0(t1)
	# br label %return
	j Heap_Node.build_return
	.section .text
	.globl Heap_Node.build_return
Heap_Node.build_return:
	# ret void
	lw t0, 8(sp)
	lw t1, 12(sp)
	lw t2, 16(sp)
	addi sp, sp, 48
	ret
	.section .text
	.globl init
init:
	addi sp, sp, -148
	sw t0, 8(sp)
	sw t1, 12(sp)
	sw t2, 16(sp)
	# %i.4 = alloca i32
	addi t0, sp, 68
	sw t0, 64(sp)
	# %u.1 = alloca i32
	addi t0, sp, 92
	sw t0, 88(sp)
	# %v.4 = alloca i32
	addi t0, sp, 104
	sw t0, 100(sp)
	# %w.1 = alloca i32
	addi t0, sp, 116
	sw t0, 112(sp)
	# %143 = load i32, ptr @n.1
	la t0, @n.1
	lw t0, 0(t0)
	sw t0, 28(sp)
	# %getInt.ret = call i32 @getInt()
	sw ra, 20(sp)
	call getInt
	lw ra, 20(sp)
	sw a0, 32(sp)
	# store i32 %getInt.ret, ptr @n.1
	lw t0, 32(sp)
	la t1, @n.1
	sw t0, 0(t1)
	# %144 = load i32, ptr @m.1
	la t0, @m.1
	lw t0, 0(t0)
	sw t0, 36(sp)
	# %getInt.ret.1 = call i32 @getInt()
	sw ra, 20(sp)
	call getInt
	lw ra, 20(sp)
	sw a0, 40(sp)
	# store i32 %getInt.ret.1, ptr @m.1
	lw t0, 40(sp)
	la t1, @m.1
	sw t0, 0(t1)
	# %145 = load ptr, ptr @g
	la t0, @g
	lw t0, 0(t0)
	sw t0, 44(sp)
	# %146 = call ptr @malloc(i32 16)
	sw ra, 20(sp)
	li a0, 16
	call malloc
	lw ra, 20(sp)
	sw a0, 48(sp)
	# store ptr %146, ptr @g
	lw t0, 48(sp)
	la t1, @g
	sw t0, 0(t1)
	# %147 = load ptr, ptr @g
	la t0, @g
	lw t0, 0(t0)
	sw t0, 52(sp)
	# %148 = load i32, ptr @n.1
	la t0, @n.1
	lw t0, 0(t0)
	sw t0, 56(sp)
	# %149 = load i32, ptr @m.1
	la t0, @m.1
	lw t0, 0(t0)
	sw t0, 60(sp)
	# call void @EdgeList.init(ptr %147, i32 %148, i32 %149)
	sw ra, 20(sp)
	lw a0, 52(sp)
	lw a1, 56(sp)
	lw a2, 60(sp)
	call EdgeList.init
	lw ra, 20(sp)
	# %150 = load i32, ptr %i.4
	lw t0, 64(sp)
	lw t0, 0(t0)
	sw t0, 72(sp)
	# store i32 0, ptr %i.4
	li t0, 0
	lw t1, 64(sp)
	sw t0, 0(t1)
	# br label %cond...3
	j init_cond...3
	.section .text
	.globl init_cond...3
init_cond...3:
	# %151 = load i32, ptr %i.4
	lw t0, 64(sp)
	lw t0, 0(t0)
	sw t0, 76(sp)
	# %152 = load i32, ptr @m.1
	la t0, @m.1
	lw t0, 0(t0)
	sw t0, 80(sp)
	# %less.5 = icmp slt i32 %151, %152
	lw t0, 76(sp)
	lw t1, 80(sp)
	sub t2, t0, t1
	sltz t2, t2
	sw t2, 84(sp)
	# br i1 %less.5, label %body...5, label %end...5
	lw t0, 84(sp)
	beq t0, x0, init_end...5
	j init_body...5
	.section .text
	.globl init_body...5
init_body...5:
	# %getInt.ret.2 = call i32 @getInt()
	sw ra, 20(sp)
	call getInt
	lw ra, 20(sp)
	sw a0, 96(sp)
	# store i32 %getInt.ret.2, ptr %u.1
	lw t0, 96(sp)
	lw t1, 88(sp)
	sw t0, 0(t1)
	# %getInt.ret.3 = call i32 @getInt()
	sw ra, 20(sp)
	call getInt
	lw ra, 20(sp)
	sw a0, 108(sp)
	# store i32 %getInt.ret.3, ptr %v.4
	lw t0, 108(sp)
	lw t1, 100(sp)
	sw t0, 0(t1)
	# %getInt.ret.4 = call i32 @getInt()
	sw ra, 20(sp)
	call getInt
	lw ra, 20(sp)
	sw a0, 120(sp)
	# store i32 %getInt.ret.4, ptr %w.1
	lw t0, 120(sp)
	lw t1, 112(sp)
	sw t0, 0(t1)
	# %153 = load ptr, ptr @g
	la t0, @g
	lw t0, 0(t0)
	sw t0, 124(sp)
	# %154 = load i32, ptr %u.1
	lw t0, 88(sp)
	lw t0, 0(t0)
	sw t0, 128(sp)
	# %155 = load i32, ptr %v.4
	lw t0, 100(sp)
	lw t0, 0(t0)
	sw t0, 132(sp)
	# %156 = load i32, ptr %w.1
	lw t0, 112(sp)
	lw t0, 0(t0)
	sw t0, 136(sp)
	# call void @EdgeList.addEdge(ptr %153, i32 %154, i32 %155, i32 %156)
	sw ra, 20(sp)
	lw a0, 124(sp)
	lw a1, 128(sp)
	lw a2, 132(sp)
	lw a3, 136(sp)
	call EdgeList.addEdge
	lw ra, 20(sp)
	# br label %step...5
	j init_step...5
	.section .text
	.globl init_step...5
init_step...5:
	# %157 = load i32, ptr %i.4
	lw t0, 64(sp)
	lw t0, 0(t0)
	sw t0, 140(sp)
	# %pre.Inc.5 = add i32 %157, 1
	lw t0, 140(sp)
	li t1, 1
	add t2, t0, t1
	sw t2, 144(sp)
	# store i32 %pre.Inc.5, ptr %i.4
	lw t0, 144(sp)
	lw t1, 64(sp)
	sw t0, 0(t1)
	# br label %cond...3
	j init_cond...3
	.section .text
	.globl init_end...5
init_end...5:
	# br label %return
	j init_return
	.section .text
	.globl init_return
init_return:
	# ret void
	lw t0, 8(sp)
	lw t1, 12(sp)
	lw t2, 16(sp)
	addi sp, sp, 148
	ret
	.section .text
	.globl Node.key_
Node.key_:
	addi sp, sp, -64
	sw t0, 8(sp)
	sw t1, 12(sp)
	sw t2, 16(sp)
	# %Node.this = alloca ptr
	addi t0, sp, 32
	sw t0, 28(sp)
	# %ret.val.13 = alloca i32
	addi t0, sp, 48
	sw t0, 44(sp)
	# store ptr %this, ptr %Node.this
	mv t0, a0
	lw t1, 28(sp)
	sw t0, 0(t1)
	# %Node.node = getelementptr %class.Node, ptr %this, i32 0, i32 0
	mv t0, a0
	li t1, 0
	li t2, 4
	mul t1, t1, t2
	add t0, t0, t1
	li t1, 0
	li t2, 4
	mul t1, t1, t2
	add t0, t0, t1
	sw t0, 36(sp)
	# %Node.dist = getelementptr %class.Node, ptr %this, i32 0, i32 1
	mv t0, a0
	li t1, 0
	li t2, 4
	mul t1, t1, t2
	add t0, t0, t1
	li t1, 1
	li t2, 4
	mul t1, t1, t2
	add t0, t0, t1
	sw t0, 40(sp)
	# %158 = load i32, ptr %Node.dist
	lw t0, 40(sp)
	lw t0, 0(t0)
	sw t0, 52(sp)
	# %minus.2 = sub i32 0, %158
	li t0, 0
	lw t1, 52(sp)
	sub t2, t0, t1
	sw t2, 56(sp)
	# store i32 %minus.2, ptr %ret.val.13
	lw t0, 56(sp)
	lw t1, 44(sp)
	sw t0, 0(t1)
	# br label %return
	j Node.key__return
	.section .text
	.globl Node.key__return
Node.key__return:
	# %r.e.t.13 = load i32, ptr %ret.val.13
	lw t0, 44(sp)
	lw t0, 0(t0)
	sw t0, 60(sp)
	# ret i32 %r.e.t.13
	lw a0, 60(sp)
	lw t0, 8(sp)
	lw t1, 12(sp)
	lw t2, 16(sp)
	addi sp, sp, 64
	ret
	.section .text
	.globl dijkstra
dijkstra:
	addi sp, sp, -620
	sw t0, 8(sp)
	sw t1, 12(sp)
	sw t2, 16(sp)
	# %s = alloca i32
	addi t0, sp, 32
	sw t0, 28(sp)
	# %ret.val.14 = alloca ptr
	addi t0, sp, 40
	sw t0, 36(sp)
	# %visited = alloca ptr
	addi t0, sp, 48
	sw t0, 44(sp)
	# %159 = alloca ptr
	addi t0, sp, 56
	sw t0, 52(sp)
	# %d = alloca ptr
	addi t0, sp, 72
	sw t0, 68(sp)
	# %161 = alloca ptr
	addi t0, sp, 80
	sw t0, 76(sp)
	# %i.5 = alloca i32
	addi t0, sp, 96
	sw t0, 92(sp)
	# %q = alloca ptr
	addi t0, sp, 180
	sw t0, 176(sp)
	# %src = alloca ptr
	addi t0, sp, 192
	sw t0, 188(sp)
	# %node = alloca ptr
	addi t0, sp, 252
	sw t0, 248(sp)
	# %u.2 = alloca i32
	addi t0, sp, 268
	sw t0, 264(sp)
	# %k = alloca i32
	addi t0, sp, 324
	sw t0, 320(sp)
	# %v.5 = alloca i32
	addi t0, sp, 372
	sw t0, 368(sp)
	# %w.2 = alloca i32
	addi t0, sp, 412
	sw t0, 408(sp)
	# %alt = alloca i32
	addi t0, sp, 452
	sw t0, 448(sp)
	# store i32 %s.val, ptr %s
	mv t0, a0
	lw t1, 28(sp)
	sw t0, 0(t1)
	# %160 = load i32, ptr @n.1
	la t0, @n.1
	lw t0, 0(t0)
	sw t0, 60(sp)
	# %call.5 = call ptr @array.alloca(i32 4, i32 1, i32 1, i32 %160)
	sw ra, 20(sp)
	li a0, 4
	li a1, 1
	li a2, 1
	lw a3, 60(sp)
	call array.alloca
	lw ra, 20(sp)
	sw a0, 64(sp)
	# store ptr %call.5, ptr %159
	lw t0, 64(sp)
	lw t1, 52(sp)
	sw t0, 0(t1)
	# store ptr %call.5, ptr %visited
	lw t0, 64(sp)
	lw t1, 44(sp)
	sw t0, 0(t1)
	# %162 = load i32, ptr @n.1
	la t0, @n.1
	lw t0, 0(t0)
	sw t0, 84(sp)
	# %call.6 = call ptr @array.alloca(i32 4, i32 1, i32 1, i32 %162)
	sw ra, 20(sp)
	li a0, 4
	li a1, 1
	li a2, 1
	lw a3, 84(sp)
	call array.alloca
	lw ra, 20(sp)
	sw a0, 88(sp)
	# store ptr %call.6, ptr %161
	lw t0, 88(sp)
	lw t1, 76(sp)
	sw t0, 0(t1)
	# store ptr %call.6, ptr %d
	lw t0, 88(sp)
	lw t1, 68(sp)
	sw t0, 0(t1)
	# %163 = load i32, ptr %i.5
	lw t0, 92(sp)
	lw t0, 0(t0)
	sw t0, 100(sp)
	# store i32 0, ptr %i.5
	li t0, 0
	lw t1, 92(sp)
	sw t0, 0(t1)
	# br label %cond...4
	j dijkstra_cond...4
	.section .text
	.globl dijkstra_cond...4
dijkstra_cond...4:
	# %164 = load i32, ptr %i.5
	lw t0, 92(sp)
	lw t0, 0(t0)
	sw t0, 104(sp)
	# %165 = load i32, ptr @n.1
	la t0, @n.1
	lw t0, 0(t0)
	sw t0, 108(sp)
	# %less.6 = icmp slt i32 %164, %165
	lw t0, 104(sp)
	lw t1, 108(sp)
	sub t2, t0, t1
	sltz t2, t2
	sw t2, 112(sp)
	# br i1 %less.6, label %body...6, label %end...6
	lw t0, 112(sp)
	beq t0, x0, dijkstra_end...6
	j dijkstra_body...6
	.section .text
	.globl dijkstra_body...6
dijkstra_body...6:
	# %166 = load ptr, ptr %d
	lw t0, 68(sp)
	lw t0, 0(t0)
	sw t0, 116(sp)
	# %167 = load i32, ptr %i.5
	lw t0, 92(sp)
	lw t0, 0(t0)
	sw t0, 120(sp)
	# %array.idx.18 = getelementptr ptr, ptr %166, i32 %167
	lw t0, 116(sp)
	lw t1, 120(sp)
	li t2, 4
	mul t1, t1, t2
	add t0, t0, t1
	sw t0, 124(sp)
	# %array.idx.val.18 = load i32, ptr %array.idx.18
	lw t0, 124(sp)
	lw t0, 0(t0)
	sw t0, 128(sp)
	# %168 = load i32, ptr @INF
	la t0, @INF
	lw t0, 0(t0)
	sw t0, 132(sp)
	# store i32 %168, ptr %array.idx.18
	lw t0, 132(sp)
	lw t1, 124(sp)
	sw t0, 0(t1)
	# %169 = load ptr, ptr %visited
	lw t0, 44(sp)
	lw t0, 0(t0)
	sw t0, 136(sp)
	# %170 = load i32, ptr %i.5
	lw t0, 92(sp)
	lw t0, 0(t0)
	sw t0, 140(sp)
	# %array.idx.19 = getelementptr ptr, ptr %169, i32 %170
	lw t0, 136(sp)
	lw t1, 140(sp)
	li t2, 4
	mul t1, t1, t2
	add t0, t0, t1
	sw t0, 144(sp)
	# %array.idx.val.19 = load i32, ptr %array.idx.19
	lw t0, 144(sp)
	lw t0, 0(t0)
	sw t0, 148(sp)
	# store i32 0, ptr %array.idx.19
	li t0, 0
	lw t1, 144(sp)
	sw t0, 0(t1)
	# br label %step...6
	j dijkstra_step...6
	.section .text
	.globl dijkstra_step...6
dijkstra_step...6:
	# %171 = load i32, ptr %i.5
	lw t0, 92(sp)
	lw t0, 0(t0)
	sw t0, 152(sp)
	# %pre.Inc.6 = add i32 %171, 1
	lw t0, 152(sp)
	li t1, 1
	add t2, t0, t1
	sw t2, 156(sp)
	# store i32 %pre.Inc.6, ptr %i.5
	lw t0, 156(sp)
	lw t1, 92(sp)
	sw t0, 0(t1)
	# br label %cond...4
	j dijkstra_cond...4
	.section .text
	.globl dijkstra_end...6
dijkstra_end...6:
	# %172 = load ptr, ptr %d
	lw t0, 68(sp)
	lw t0, 0(t0)
	sw t0, 160(sp)
	# %173 = load i32, ptr %s
	lw t0, 28(sp)
	lw t0, 0(t0)
	sw t0, 164(sp)
	# %array.idx.20 = getelementptr ptr, ptr %172, i32 %173
	lw t0, 160(sp)
	lw t1, 164(sp)
	li t2, 4
	mul t1, t1, t2
	add t0, t0, t1
	sw t0, 168(sp)
	# %array.idx.val.20 = load i32, ptr %array.idx.20
	lw t0, 168(sp)
	lw t0, 0(t0)
	sw t0, 172(sp)
	# store i32 0, ptr %array.idx.20
	li t0, 0
	lw t1, 168(sp)
	sw t0, 0(t1)
	# %174 = call ptr @malloc(i32 4)
	sw ra, 20(sp)
	li a0, 4
	call malloc
	lw ra, 20(sp)
	sw a0, 184(sp)
	# call void @Heap_Node.build(ptr %174)
	sw ra, 20(sp)
	lw a0, 184(sp)
	call Heap_Node.build
	lw ra, 20(sp)
	# store ptr %174, ptr %q
	lw t0, 184(sp)
	lw t1, 176(sp)
	sw t0, 0(t1)
	# %175 = call ptr @malloc(i32 8)
	sw ra, 20(sp)
	li a0, 8
	call malloc
	lw ra, 20(sp)
	sw a0, 196(sp)
	# store ptr %175, ptr %src
	lw t0, 196(sp)
	lw t1, 188(sp)
	sw t0, 0(t1)
	# %176 = load ptr, ptr %src
	lw t0, 188(sp)
	lw t0, 0(t0)
	sw t0, 200(sp)
	# %177 = getelementptr %class.Node, ptr %176, i32 0, i32 1
	lw t0, 200(sp)
	li t1, 0
	li t2, 4
	mul t1, t1, t2
	add t0, t0, t1
	li t1, 1
	li t2, 4
	mul t1, t1, t2
	add t0, t0, t1
	sw t0, 204(sp)
	# %178 = load i32, ptr %177
	lw t0, 204(sp)
	lw t0, 0(t0)
	sw t0, 208(sp)
	# store i32 0, ptr %177
	li t0, 0
	lw t1, 204(sp)
	sw t0, 0(t1)
	# %179 = load ptr, ptr %src
	lw t0, 188(sp)
	lw t0, 0(t0)
	sw t0, 212(sp)
	# %180 = getelementptr %class.Node, ptr %179, i32 0, i32 0
	lw t0, 212(sp)
	li t1, 0
	li t2, 4
	mul t1, t1, t2
	add t0, t0, t1
	li t1, 0
	li t2, 4
	mul t1, t1, t2
	add t0, t0, t1
	sw t0, 216(sp)
	# %181 = load i32, ptr %180
	lw t0, 216(sp)
	lw t0, 0(t0)
	sw t0, 220(sp)
	# %182 = load i32, ptr %s
	lw t0, 28(sp)
	lw t0, 0(t0)
	sw t0, 224(sp)
	# store i32 %182, ptr %180
	lw t0, 224(sp)
	lw t1, 216(sp)
	sw t0, 0(t1)
	# %183 = load ptr, ptr %q
	lw t0, 176(sp)
	lw t0, 0(t0)
	sw t0, 228(sp)
	# %184 = load ptr, ptr %src
	lw t0, 188(sp)
	lw t0, 0(t0)
	sw t0, 232(sp)
	# call void @Heap_Node.push(ptr %183, ptr %184)
	sw ra, 20(sp)
	lw a0, 228(sp)
	lw a1, 232(sp)
	call Heap_Node.push
	lw ra, 20(sp)
	# br label %step...7
	j dijkstra_step...7
	.section .text
	.globl dijkstra_step...7
dijkstra_step...7:
	# %185 = load ptr, ptr %q
	lw t0, 176(sp)
	lw t0, 0(t0)
	sw t0, 236(sp)
	# %call.null.ret.24 = call i32 @Heap_Node.size(ptr %185)
	sw ra, 20(sp)
	lw a0, 236(sp)
	call Heap_Node.size
	lw ra, 20(sp)
	sw a0, 240(sp)
	# %unEq.1 = icmp ne i32 %call.null.ret.24, 0
	lw t0, 240(sp)
	li t1, 0
	sub t2, t0, t1
	snez t2, t2
	sw t2, 244(sp)
	# br i1 %unEq.1, label %body...7, label %end...7
	lw t0, 244(sp)
	beq t0, x0, dijkstra_end...7
	j dijkstra_body...7
	.section .text
	.globl dijkstra_body...7
dijkstra_body...7:
	# %186 = load ptr, ptr %q
	lw t0, 176(sp)
	lw t0, 0(t0)
	sw t0, 256(sp)
	# %call.null.ret.25 = call ptr @Heap_Node.pop(ptr %186)
	sw ra, 20(sp)
	lw a0, 256(sp)
	call Heap_Node.pop
	lw ra, 20(sp)
	sw a0, 260(sp)
	# store ptr %call.null.ret.25, ptr %node
	lw t0, 260(sp)
	lw t1, 248(sp)
	sw t0, 0(t1)
	# %187 = load ptr, ptr %node
	lw t0, 248(sp)
	lw t0, 0(t0)
	sw t0, 272(sp)
	# %188 = getelementptr %class.Node, ptr %187, i32 0, i32 0
	lw t0, 272(sp)
	li t1, 0
	li t2, 4
	mul t1, t1, t2
	add t0, t0, t1
	li t1, 0
	li t2, 4
	mul t1, t1, t2
	add t0, t0, t1
	sw t0, 276(sp)
	# %189 = load i32, ptr %188
	lw t0, 276(sp)
	lw t0, 0(t0)
	sw t0, 280(sp)
	# store i32 %189, ptr %u.2
	lw t0, 280(sp)
	lw t1, 264(sp)
	sw t0, 0(t1)
	# %190 = load ptr, ptr %visited
	lw t0, 44(sp)
	lw t0, 0(t0)
	sw t0, 284(sp)
	# %191 = load i32, ptr %u.2
	lw t0, 264(sp)
	lw t0, 0(t0)
	sw t0, 288(sp)
	# %array.idx.21 = getelementptr ptr, ptr %190, i32 %191
	lw t0, 284(sp)
	lw t1, 288(sp)
	li t2, 4
	mul t1, t1, t2
	add t0, t0, t1
	sw t0, 292(sp)
	# %array.idx.val.21 = load i32, ptr %array.idx.21
	lw t0, 292(sp)
	lw t0, 0(t0)
	sw t0, 296(sp)
	# %eq.2 = icmp eq i32 %array.idx.val.21, 1
	lw t0, 296(sp)
	li t1, 1
	sub t2, t0, t1
	seqz t2, t2
	sw t2, 300(sp)
	# br i1 %eq.2, label %if.true.5, label %if.false.5
	lw t0, 300(sp)
	beq t0, x0, dijkstra_if.false.5
	j dijkstra_if.true.5
	.section .text
	.globl dijkstra_if.true.5
dijkstra_if.true.5:
	# br label %step...7
	j dijkstra_step...7
	.section .text
	.globl dijkstra_if.false.5
dijkstra_if.false.5:
	# br label %if.end.5
	j dijkstra_if.end.5
	.section .text
	.globl dijkstra_if.end.5
dijkstra_if.end.5:
	# %192 = load ptr, ptr %visited
	lw t0, 44(sp)
	lw t0, 0(t0)
	sw t0, 304(sp)
	# %193 = load i32, ptr %u.2
	lw t0, 264(sp)
	lw t0, 0(t0)
	sw t0, 308(sp)
	# %array.idx.22 = getelementptr ptr, ptr %192, i32 %193
	lw t0, 304(sp)
	lw t1, 308(sp)
	li t2, 4
	mul t1, t1, t2
	add t0, t0, t1
	sw t0, 312(sp)
	# %array.idx.val.22 = load i32, ptr %array.idx.22
	lw t0, 312(sp)
	lw t0, 0(t0)
	sw t0, 316(sp)
	# store i32 1, ptr %array.idx.22
	li t0, 1
	lw t1, 312(sp)
	sw t0, 0(t1)
	# %194 = load i32, ptr %k
	lw t0, 320(sp)
	lw t0, 0(t0)
	sw t0, 328(sp)
	# %195 = load ptr, ptr @g
	la t0, @g
	lw t0, 0(t0)
	sw t0, 332(sp)
	# %196 = getelementptr %class.EdgeList, ptr %195, i32 0, i32 2
	lw t0, 332(sp)
	li t1, 0
	li t2, 4
	mul t1, t1, t2
	add t0, t0, t1
	li t1, 2
	li t2, 4
	mul t1, t1, t2
	add t0, t0, t1
	sw t0, 336(sp)
	# %197 = load ptr, ptr %196
	lw t0, 336(sp)
	lw t0, 0(t0)
	sw t0, 340(sp)
	# %198 = load i32, ptr %u.2
	lw t0, 264(sp)
	lw t0, 0(t0)
	sw t0, 344(sp)
	# %array.idx.23 = getelementptr ptr, ptr %197, i32 %198
	lw t0, 340(sp)
	lw t1, 344(sp)
	li t2, 4
	mul t1, t1, t2
	add t0, t0, t1
	sw t0, 348(sp)
	# %array.idx.val.23 = load i32, ptr %array.idx.23
	lw t0, 348(sp)
	lw t0, 0(t0)
	sw t0, 352(sp)
	# store i32 %array.idx.val.23, ptr %k
	lw t0, 352(sp)
	lw t1, 320(sp)
	sw t0, 0(t1)
	# br label %cond...5
	j dijkstra_cond...5
	.section .text
	.globl dijkstra_cond...5
dijkstra_cond...5:
	# %199 = load i32, ptr %k
	lw t0, 320(sp)
	lw t0, 0(t0)
	sw t0, 356(sp)
	# %minus.3 = sub i32 0, 1
	li t0, 0
	li t1, 1
	sub t2, t0, t1
	sw t2, 360(sp)
	# %unEq.2 = icmp ne i32 %199, %minus.3
	lw t0, 356(sp)
	lw t1, 360(sp)
	sub t2, t0, t1
	snez t2, t2
	sw t2, 364(sp)
	# br i1 %unEq.2, label %body...8, label %end...8
	lw t0, 364(sp)
	beq t0, x0, dijkstra_end...8
	j dijkstra_body...8
	.section .text
	.globl dijkstra_body...8
dijkstra_body...8:
	# %200 = load ptr, ptr @g
	la t0, @g
	lw t0, 0(t0)
	sw t0, 376(sp)
	# %201 = getelementptr %class.EdgeList, ptr %200, i32 0, i32 0
	lw t0, 376(sp)
	li t1, 0
	li t2, 4
	mul t1, t1, t2
	add t0, t0, t1
	li t1, 0
	li t2, 4
	mul t1, t1, t2
	add t0, t0, t1
	sw t0, 380(sp)
	# %202 = load ptr, ptr %201
	lw t0, 380(sp)
	lw t0, 0(t0)
	sw t0, 384(sp)
	# %203 = load i32, ptr %k
	lw t0, 320(sp)
	lw t0, 0(t0)
	sw t0, 388(sp)
	# %array.idx.24 = getelementptr ptr, ptr %202, i32 %203
	lw t0, 384(sp)
	lw t1, 388(sp)
	li t2, 4
	mul t1, t1, t2
	add t0, t0, t1
	sw t0, 392(sp)
	# %array.idx.val.24 = load ptr, ptr %array.idx.24
	lw t0, 392(sp)
	lw t0, 0(t0)
	sw t0, 396(sp)
	# %204 = getelementptr %class.Edge, ptr %array.idx.val.24, i32 0, i32 1
	lw t0, 396(sp)
	li t1, 0
	li t2, 4
	mul t1, t1, t2
	add t0, t0, t1
	li t1, 1
	li t2, 4
	mul t1, t1, t2
	add t0, t0, t1
	sw t0, 400(sp)
	# %205 = load i32, ptr %204
	lw t0, 400(sp)
	lw t0, 0(t0)
	sw t0, 404(sp)
	# store i32 %205, ptr %v.5
	lw t0, 404(sp)
	lw t1, 368(sp)
	sw t0, 0(t1)
	# %206 = load ptr, ptr @g
	la t0, @g
	lw t0, 0(t0)
	sw t0, 416(sp)
	# %207 = getelementptr %class.EdgeList, ptr %206, i32 0, i32 0
	lw t0, 416(sp)
	li t1, 0
	li t2, 4
	mul t1, t1, t2
	add t0, t0, t1
	li t1, 0
	li t2, 4
	mul t1, t1, t2
	add t0, t0, t1
	sw t0, 420(sp)
	# %208 = load ptr, ptr %207
	lw t0, 420(sp)
	lw t0, 0(t0)
	sw t0, 424(sp)
	# %209 = load i32, ptr %k
	lw t0, 320(sp)
	lw t0, 0(t0)
	sw t0, 428(sp)
	# %array.idx.25 = getelementptr ptr, ptr %208, i32 %209
	lw t0, 424(sp)
	lw t1, 428(sp)
	li t2, 4
	mul t1, t1, t2
	add t0, t0, t1
	sw t0, 432(sp)
	# %array.idx.val.25 = load ptr, ptr %array.idx.25
	lw t0, 432(sp)
	lw t0, 0(t0)
	sw t0, 436(sp)
	# %210 = getelementptr %class.Edge, ptr %array.idx.val.25, i32 0, i32 2
	lw t0, 436(sp)
	li t1, 0
	li t2, 4
	mul t1, t1, t2
	add t0, t0, t1
	li t1, 2
	li t2, 4
	mul t1, t1, t2
	add t0, t0, t1
	sw t0, 440(sp)
	# %211 = load i32, ptr %210
	lw t0, 440(sp)
	lw t0, 0(t0)
	sw t0, 444(sp)
	# store i32 %211, ptr %w.2
	lw t0, 444(sp)
	lw t1, 408(sp)
	sw t0, 0(t1)
	# %212 = load ptr, ptr %d
	lw t0, 68(sp)
	lw t0, 0(t0)
	sw t0, 456(sp)
	# %213 = load i32, ptr %u.2
	lw t0, 264(sp)
	lw t0, 0(t0)
	sw t0, 460(sp)
	# %array.idx.26 = getelementptr ptr, ptr %212, i32 %213
	lw t0, 456(sp)
	lw t1, 460(sp)
	li t2, 4
	mul t1, t1, t2
	add t0, t0, t1
	sw t0, 464(sp)
	# %array.idx.val.26 = load i32, ptr %array.idx.26
	lw t0, 464(sp)
	lw t0, 0(t0)
	sw t0, 468(sp)
	# %214 = load i32, ptr %w.2
	lw t0, 408(sp)
	lw t0, 0(t0)
	sw t0, 472(sp)
	# %add.2 = add i32 %array.idx.val.26, %214
	lw t0, 468(sp)
	lw t1, 472(sp)
	add t2, t0, t1
	sw t2, 476(sp)
	# store i32 %add.2, ptr %alt
	lw t0, 476(sp)
	lw t1, 448(sp)
	sw t0, 0(t1)
	# %215 = load i32, ptr %alt
	lw t0, 448(sp)
	lw t0, 0(t0)
	sw t0, 480(sp)
	# %216 = load ptr, ptr %d
	lw t0, 68(sp)
	lw t0, 0(t0)
	sw t0, 484(sp)
	# %217 = load i32, ptr %v.5
	lw t0, 368(sp)
	lw t0, 0(t0)
	sw t0, 488(sp)
	# %array.idx.27 = getelementptr ptr, ptr %216, i32 %217
	lw t0, 484(sp)
	lw t1, 488(sp)
	li t2, 4
	mul t1, t1, t2
	add t0, t0, t1
	sw t0, 492(sp)
	# %array.idx.val.27 = load i32, ptr %array.idx.27
	lw t0, 492(sp)
	lw t0, 0(t0)
	sw t0, 496(sp)
	# %grEq.1 = icmp sge i32 %215, %array.idx.val.27
	lw t0, 480(sp)
	lw t1, 496(sp)
	sub t2, t0, t1
	sgtz t0, t2
	seqz t1, t2
	or t2, t0, t1
	sw t2, 500(sp)
	# br i1 %grEq.1, label %if.true.6, label %if.false.6
	lw t0, 500(sp)
	beq t0, x0, dijkstra_if.false.6
	j dijkstra_if.true.6
	.section .text
	.globl dijkstra_if.true.6
dijkstra_if.true.6:
	# br label %step...8
	j dijkstra_step...8
	.section .text
	.globl dijkstra_if.false.6
dijkstra_if.false.6:
	# br label %if.end.6
	j dijkstra_if.end.6
	.section .text
	.globl dijkstra_if.end.6
dijkstra_if.end.6:
	# %218 = load ptr, ptr %d
	lw t0, 68(sp)
	lw t0, 0(t0)
	sw t0, 504(sp)
	# %219 = load i32, ptr %v.5
	lw t0, 368(sp)
	lw t0, 0(t0)
	sw t0, 508(sp)
	# %array.idx.28 = getelementptr ptr, ptr %218, i32 %219
	lw t0, 504(sp)
	lw t1, 508(sp)
	li t2, 4
	mul t1, t1, t2
	add t0, t0, t1
	sw t0, 512(sp)
	# %array.idx.val.28 = load i32, ptr %array.idx.28
	lw t0, 512(sp)
	lw t0, 0(t0)
	sw t0, 516(sp)
	# %220 = load i32, ptr %alt
	lw t0, 448(sp)
	lw t0, 0(t0)
	sw t0, 520(sp)
	# store i32 %220, ptr %array.idx.28
	lw t0, 520(sp)
	lw t1, 512(sp)
	sw t0, 0(t1)
	# %221 = load ptr, ptr %node
	lw t0, 248(sp)
	lw t0, 0(t0)
	sw t0, 524(sp)
	# %222 = call ptr @malloc(i32 8)
	sw ra, 20(sp)
	li a0, 8
	call malloc
	lw ra, 20(sp)
	sw a0, 528(sp)
	# store ptr %222, ptr %node
	lw t0, 528(sp)
	lw t1, 248(sp)
	sw t0, 0(t1)
	# %223 = load ptr, ptr %node
	lw t0, 248(sp)
	lw t0, 0(t0)
	sw t0, 532(sp)
	# %224 = getelementptr %class.Node, ptr %223, i32 0, i32 0
	lw t0, 532(sp)
	li t1, 0
	li t2, 4
	mul t1, t1, t2
	add t0, t0, t1
	li t1, 0
	li t2, 4
	mul t1, t1, t2
	add t0, t0, t1
	sw t0, 536(sp)
	# %225 = load i32, ptr %224
	lw t0, 536(sp)
	lw t0, 0(t0)
	sw t0, 540(sp)
	# %226 = load i32, ptr %v.5
	lw t0, 368(sp)
	lw t0, 0(t0)
	sw t0, 544(sp)
	# store i32 %226, ptr %224
	lw t0, 544(sp)
	lw t1, 536(sp)
	sw t0, 0(t1)
	# %227 = load ptr, ptr %node
	lw t0, 248(sp)
	lw t0, 0(t0)
	sw t0, 548(sp)
	# %228 = getelementptr %class.Node, ptr %227, i32 0, i32 1
	lw t0, 548(sp)
	li t1, 0
	li t2, 4
	mul t1, t1, t2
	add t0, t0, t1
	li t1, 1
	li t2, 4
	mul t1, t1, t2
	add t0, t0, t1
	sw t0, 552(sp)
	# %229 = load i32, ptr %228
	lw t0, 552(sp)
	lw t0, 0(t0)
	sw t0, 556(sp)
	# %230 = load ptr, ptr %d
	lw t0, 68(sp)
	lw t0, 0(t0)
	sw t0, 560(sp)
	# %231 = load i32, ptr %v.5
	lw t0, 368(sp)
	lw t0, 0(t0)
	sw t0, 564(sp)
	# %array.idx.29 = getelementptr ptr, ptr %230, i32 %231
	lw t0, 560(sp)
	lw t1, 564(sp)
	li t2, 4
	mul t1, t1, t2
	add t0, t0, t1
	sw t0, 568(sp)
	# %array.idx.val.29 = load i32, ptr %array.idx.29
	lw t0, 568(sp)
	lw t0, 0(t0)
	sw t0, 572(sp)
	# store i32 %array.idx.val.29, ptr %228
	lw t0, 572(sp)
	lw t1, 552(sp)
	sw t0, 0(t1)
	# %232 = load ptr, ptr %q
	lw t0, 176(sp)
	lw t0, 0(t0)
	sw t0, 576(sp)
	# %233 = load ptr, ptr %node
	lw t0, 248(sp)
	lw t0, 0(t0)
	sw t0, 580(sp)
	# call void @Heap_Node.push(ptr %232, ptr %233)
	sw ra, 20(sp)
	lw a0, 576(sp)
	lw a1, 580(sp)
	call Heap_Node.push
	lw ra, 20(sp)
	# br label %step...8
	j dijkstra_step...8
	.section .text
	.globl dijkstra_step...8
dijkstra_step...8:
	# %234 = load i32, ptr %k
	lw t0, 320(sp)
	lw t0, 0(t0)
	sw t0, 584(sp)
	# %235 = load ptr, ptr @g
	la t0, @g
	lw t0, 0(t0)
	sw t0, 588(sp)
	# %236 = getelementptr %class.EdgeList, ptr %235, i32 0, i32 1
	lw t0, 588(sp)
	li t1, 0
	li t2, 4
	mul t1, t1, t2
	add t0, t0, t1
	li t1, 1
	li t2, 4
	mul t1, t1, t2
	add t0, t0, t1
	sw t0, 592(sp)
	# %237 = load ptr, ptr %236
	lw t0, 592(sp)
	lw t0, 0(t0)
	sw t0, 596(sp)
	# %238 = load i32, ptr %k
	lw t0, 320(sp)
	lw t0, 0(t0)
	sw t0, 600(sp)
	# %array.idx.30 = getelementptr ptr, ptr %237, i32 %238
	lw t0, 596(sp)
	lw t1, 600(sp)
	li t2, 4
	mul t1, t1, t2
	add t0, t0, t1
	sw t0, 604(sp)
	# %array.idx.val.30 = load i32, ptr %array.idx.30
	lw t0, 604(sp)
	lw t0, 0(t0)
	sw t0, 608(sp)
	# store i32 %array.idx.val.30, ptr %k
	lw t0, 608(sp)
	lw t1, 320(sp)
	sw t0, 0(t1)
	# br label %cond...5
	j dijkstra_cond...5
	.section .text
	.globl dijkstra_end...8
dijkstra_end...8:
	# br label %step...7
	j dijkstra_step...7
	.section .text
	.globl dijkstra_end...7
dijkstra_end...7:
	# %239 = load ptr, ptr %d
	lw t0, 68(sp)
	lw t0, 0(t0)
	sw t0, 612(sp)
	# store ptr %239, ptr %ret.val.14
	lw t0, 612(sp)
	lw t1, 36(sp)
	sw t0, 0(t1)
	# br label %return
	j dijkstra_return
	.section .text
	.globl dijkstra_return
dijkstra_return:
	# %r.e.t.14 = load ptr, ptr %ret.val.14
	lw t0, 36(sp)
	lw t0, 0(t0)
	sw t0, 616(sp)
	# ret ptr %r.e.t.14
	lw a0, 616(sp)
	lw t0, 8(sp)
	lw t1, 12(sp)
	lw t2, 16(sp)
	addi sp, sp, 620
	ret
	.section .text
	.globl main
main:
	addi sp, sp, -164
	sw t0, 8(sp)
	sw t1, 12(sp)
	sw t2, 16(sp)
	# %ret.val.15 = alloca i32
	addi t0, sp, 32
	sw t0, 28(sp)
	# %i.6 = alloca i32
	addi t0, sp, 40
	sw t0, 36(sp)
	# %j.1 = alloca i32
	addi t0, sp, 48
	sw t0, 44(sp)
	# %d.1 = alloca ptr
	addi t0, sp, 72
	sw t0, 68(sp)
	# call void @_.init()
	sw ra, 20(sp)
	call _.init
	lw ra, 20(sp)
	# call void @init()
	sw ra, 20(sp)
	call init
	lw ra, 20(sp)
	# %240 = load i32, ptr %i.6
	lw t0, 36(sp)
	lw t0, 0(t0)
	sw t0, 52(sp)
	# store i32 0, ptr %i.6
	li t0, 0
	lw t1, 36(sp)
	sw t0, 0(t1)
	# br label %cond...6
	j main_cond...6
	.section .text
	.globl main_cond...6
main_cond...6:
	# %241 = load i32, ptr %i.6
	lw t0, 36(sp)
	lw t0, 0(t0)
	sw t0, 56(sp)
	# %242 = load i32, ptr @n.1
	la t0, @n.1
	lw t0, 0(t0)
	sw t0, 60(sp)
	# %less.7 = icmp slt i32 %241, %242
	lw t0, 56(sp)
	lw t1, 60(sp)
	sub t2, t0, t1
	sltz t2, t2
	sw t2, 64(sp)
	# br i1 %less.7, label %body...9, label %end...9
	lw t0, 64(sp)
	beq t0, x0, main_end...9
	j main_body...9
	.section .text
	.globl main_body...9
main_body...9:
	# %243 = load i32, ptr %i.6
	lw t0, 36(sp)
	lw t0, 0(t0)
	sw t0, 76(sp)
	# %dijkstra.ret = call ptr @dijkstra(i32 %243)
	sw ra, 20(sp)
	lw a0, 76(sp)
	call dijkstra
	lw ra, 20(sp)
	sw a0, 80(sp)
	# store ptr %dijkstra.ret, ptr %d.1
	lw t0, 80(sp)
	lw t1, 68(sp)
	sw t0, 0(t1)
	# %244 = load i32, ptr %j.1
	lw t0, 44(sp)
	lw t0, 0(t0)
	sw t0, 84(sp)
	# store i32 0, ptr %j.1
	li t0, 0
	lw t1, 44(sp)
	sw t0, 0(t1)
	# br label %cond...7
	j main_cond...7
	.section .text
	.globl main_cond...7
main_cond...7:
	# %245 = load i32, ptr %j.1
	lw t0, 44(sp)
	lw t0, 0(t0)
	sw t0, 88(sp)
	# %246 = load i32, ptr @n.1
	la t0, @n.1
	lw t0, 0(t0)
	sw t0, 92(sp)
	# %less.8 = icmp slt i32 %245, %246
	lw t0, 88(sp)
	lw t1, 92(sp)
	sub t2, t0, t1
	sltz t2, t2
	sw t2, 96(sp)
	# br i1 %less.8, label %body...10, label %end...10
	lw t0, 96(sp)
	beq t0, x0, main_end...10
	j main_body...10
	.section .text
	.globl main_body...10
main_body...10:
	# %247 = load ptr, ptr %d.1
	lw t0, 68(sp)
	lw t0, 0(t0)
	sw t0, 100(sp)
	# %248 = load i32, ptr %j.1
	lw t0, 44(sp)
	lw t0, 0(t0)
	sw t0, 104(sp)
	# %array.idx.31 = getelementptr ptr, ptr %247, i32 %248
	lw t0, 100(sp)
	lw t1, 104(sp)
	li t2, 4
	mul t1, t1, t2
	add t0, t0, t1
	sw t0, 108(sp)
	# %array.idx.val.31 = load i32, ptr %array.idx.31
	lw t0, 108(sp)
	lw t0, 0(t0)
	sw t0, 112(sp)
	# %249 = load i32, ptr @INF
	la t0, @INF
	lw t0, 0(t0)
	sw t0, 116(sp)
	# %eq.3 = icmp eq i32 %array.idx.val.31, %249
	lw t0, 112(sp)
	lw t1, 116(sp)
	sub t2, t0, t1
	seqz t2, t2
	sw t2, 120(sp)
	# br i1 %eq.3, label %if.true.7, label %if.false.7
	lw t0, 120(sp)
	beq t0, x0, main_if.false.7
	j main_if.true.7
	.section .text
	.globl main_if.true.7
main_if.true.7:
	# call void @print(ptr @.str)
	sw ra, 20(sp)
	la a0, @.str
	call print
	lw ra, 20(sp)
	# br label %if.end.7
	j main_if.end.7
	.section .text
	.globl main_if.false.7
main_if.false.7:
	# %250 = load ptr, ptr %d.1
	lw t0, 68(sp)
	lw t0, 0(t0)
	sw t0, 124(sp)
	# %251 = load i32, ptr %j.1
	lw t0, 44(sp)
	lw t0, 0(t0)
	sw t0, 128(sp)
	# %array.idx.32 = getelementptr ptr, ptr %250, i32 %251
	lw t0, 124(sp)
	lw t1, 128(sp)
	li t2, 4
	mul t1, t1, t2
	add t0, t0, t1
	sw t0, 132(sp)
	# %array.idx.val.32 = load i32, ptr %array.idx.32
	lw t0, 132(sp)
	lw t0, 0(t0)
	sw t0, 136(sp)
	# %toString.ret = call ptr @toString(i32 %array.idx.val.32)
	sw ra, 20(sp)
	lw a0, 136(sp)
	call toString
	lw ra, 20(sp)
	sw a0, 140(sp)
	# call void @print(ptr %toString.ret)
	sw ra, 20(sp)
	lw a0, 140(sp)
	call print
	lw ra, 20(sp)
	# br label %if.end.7
	j main_if.end.7
	.section .text
	.globl main_if.end.7
main_if.end.7:
	# call void @print(ptr @.str.1)
	sw ra, 20(sp)
	la a0, @.str.1
	call print
	lw ra, 20(sp)
	# br label %step...10
	j main_step...10
	.section .text
	.globl main_step...10
main_step...10:
	# %252 = load i32, ptr %j.1
	lw t0, 44(sp)
	lw t0, 0(t0)
	sw t0, 144(sp)
	# %pre.Inc.7 = add i32 %252, 1
	lw t0, 144(sp)
	li t1, 1
	add t2, t0, t1
	sw t2, 148(sp)
	# store i32 %pre.Inc.7, ptr %j.1
	lw t0, 148(sp)
	lw t1, 44(sp)
	sw t0, 0(t1)
	# br label %cond...7
	j main_cond...7
	.section .text
	.globl main_end...10
main_end...10:
	# call void @println(ptr @.str.2)
	sw ra, 20(sp)
	la a0, @.str.2
	call println
	lw ra, 20(sp)
	# br label %step...9
	j main_step...9
	.section .text
	.globl main_step...9
main_step...9:
	# %253 = load i32, ptr %i.6
	lw t0, 36(sp)
	lw t0, 0(t0)
	sw t0, 152(sp)
	# %pre.Inc.8 = add i32 %253, 1
	lw t0, 152(sp)
	li t1, 1
	add t2, t0, t1
	sw t2, 156(sp)
	# store i32 %pre.Inc.8, ptr %i.6
	lw t0, 156(sp)
	lw t1, 36(sp)
	sw t0, 0(t1)
	# br label %cond...6
	j main_cond...6
	.section .text
	.globl main_end...9
main_end...9:
	# store i32 0, ptr %ret.val.15
	li t0, 0
	lw t1, 28(sp)
	sw t0, 0(t1)
	# br label %return
	j main_return
	.section .text
	.globl main_return
main_return:
	# %r.e.t.15 = load i32, ptr %ret.val.15
	lw t0, 28(sp)
	lw t0, 0(t0)
	sw t0, 160(sp)
	# ret i32 %r.e.t.15
	lw a0, 160(sp)
	lw t0, 8(sp)
	lw t1, 12(sp)
	lw t2, 16(sp)
	addi sp, sp, 164
	ret
	.section .text
	.globl _.init
_.init:
	addi sp, sp, -28
	sw t0, 8(sp)
	sw t1, 12(sp)
	sw t2, 16(sp)
	# ret void
	lw t0, 8(sp)
	lw t1, 12(sp)
	lw t2, 16(sp)
	addi sp, sp, 28
	ret

