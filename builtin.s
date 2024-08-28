	.text
	.attribute	4, 16
	.attribute	5, "rv32i2p1_m2p0_a2p1_c2p0"
	.file	"builtin.c"
	.option	push
	.option	arch, +a, +c, +m
	.globl	print                           # -- Begin function print
	.p2align	1
	.type	print,@function
print:                                  # @print
# %bb.0:
	lui	a1, %hi(.L.str)
	addi	a1, a1, %lo(.L.str)
	mv	a2, a0
	mv	a0, a1
	mv	a1, a2
	tail	printf
.Lfunc_end0:
	.size	print, .Lfunc_end0-print
                                        # -- End function
	.option	pop
	.option	push
	.option	arch, +a, +c, +m
	.globl	println                         # -- Begin function println
	.p2align	1
	.type	println,@function
println:                                # @println
# %bb.0:
	lui	a1, %hi(.L.str.1)
	addi	a1, a1, %lo(.L.str.1)
	mv	a2, a0
	mv	a0, a1
	mv	a1, a2
	tail	printf
.Lfunc_end1:
	.size	println, .Lfunc_end1-println
                                        # -- End function
	.option	pop
	.option	push
	.option	arch, +a, +c, +m
	.globl	printInt                        # -- Begin function printInt
	.p2align	1
	.type	printInt,@function
printInt:                               # @printInt
# %bb.0:
	lui	a1, %hi(.L.str.2)
	addi	a1, a1, %lo(.L.str.2)
	mv	a2, a0
	mv	a0, a1
	mv	a1, a2
	tail	printf
.Lfunc_end2:
	.size	printInt, .Lfunc_end2-printInt
                                        # -- End function
	.option	pop
	.option	push
	.option	arch, +a, +c, +m
	.globl	printlnInt                      # -- Begin function printlnInt
	.p2align	1
	.type	printlnInt,@function
printlnInt:                             # @printlnInt
# %bb.0:
	lui	a1, %hi(.L.str.3)
	addi	a1, a1, %lo(.L.str.3)
	mv	a2, a0
	mv	a0, a1
	mv	a1, a2
	tail	printf
.Lfunc_end3:
	.size	printlnInt, .Lfunc_end3-printlnInt
                                        # -- End function
	.option	pop
	.option	push
	.option	arch, +a, +c, +m
	.globl	getString                       # -- Begin function getString
	.p2align	1
	.type	getString,@function
getString:                              # @getString
# %bb.0:
	addi	sp, sp, -16
	sw	ra, 12(sp)                      # 4-byte Folded Spill
	sw	s0, 8(sp)                       # 4-byte Folded Spill
	li	a0, 1024
	call	malloc
	mv	s0, a0
	lui	a0, %hi(.L.str)
	addi	a0, a0, %lo(.L.str)
	mv	a1, s0
	call	scanf
	mv	a0, s0
	lw	ra, 12(sp)                      # 4-byte Folded Reload
	lw	s0, 8(sp)                       # 4-byte Folded Reload
	addi	sp, sp, 16
	ret
.Lfunc_end4:
	.size	getString, .Lfunc_end4-getString
                                        # -- End function
	.option	pop
	.option	push
	.option	arch, +a, +c, +m
	.globl	getInt                          # -- Begin function getInt
	.p2align	1
	.type	getInt,@function
getInt:                                 # @getInt
# %bb.0:
	addi	sp, sp, -16
	sw	ra, 12(sp)                      # 4-byte Folded Spill
	lui	a0, %hi(.L.str.2)
	addi	a0, a0, %lo(.L.str.2)
	addi	a1, sp, 8
	call	scanf
	lw	a0, 8(sp)
	lw	ra, 12(sp)                      # 4-byte Folded Reload
	addi	sp, sp, 16
	ret
.Lfunc_end5:
	.size	getInt, .Lfunc_end5-getInt
                                        # -- End function
	.option	pop
	.option	push
	.option	arch, +a, +c, +m
	.globl	toString                        # -- Begin function toString
	.p2align	1
	.type	toString,@function
toString:                               # @toString
# %bb.0:
	addi	sp, sp, -16
	sw	ra, 12(sp)                      # 4-byte Folded Spill
	sw	s0, 8(sp)                       # 4-byte Folded Spill
	sw	s1, 4(sp)                       # 4-byte Folded Spill
	mv	s0, a0
	li	a0, 12
	call	malloc
	mv	s1, a0
	lui	a0, %hi(.L.str.2)
	addi	a1, a0, %lo(.L.str.2)
	mv	a0, s1
	mv	a2, s0
	call	sprintf
	mv	a0, s1
	lw	ra, 12(sp)                      # 4-byte Folded Reload
	lw	s0, 8(sp)                       # 4-byte Folded Reload
	lw	s1, 4(sp)                       # 4-byte Folded Reload
	addi	sp, sp, 16
	ret
.Lfunc_end6:
	.size	toString, .Lfunc_end6-toString
                                        # -- End function
	.option	pop
	.option	push
	.option	arch, +a, +c, +m
	.globl	bool_toString                   # -- Begin function bool_toString
	.p2align	1
	.type	bool_toString,@function
bool_toString:                          # @bool_toString
# %bb.0:
	bnez	a0, .LBB7_2
# %bb.1:
	lui	a0, %hi(.L.str.5)
	addi	a0, a0, %lo(.L.str.5)
	ret
.LBB7_2:
	lui	a0, %hi(.L.str.4)
	addi	a0, a0, %lo(.L.str.4)
	ret
.Lfunc_end7:
	.size	bool_toString, .Lfunc_end7-bool_toString
                                        # -- End function
	.option	pop
	.option	push
	.option	arch, +a, +c, +m
	.globl	alloca_helper                   # -- Begin function alloca_helper
	.p2align	1
	.type	alloca_helper,@function
alloca_helper:                          # @alloca_helper
# %bb.0:
	addi	sp, sp, -16
	sw	ra, 12(sp)                      # 4-byte Folded Spill
	sw	s0, 8(sp)                       # 4-byte Folded Spill
	mv	s0, a1
	mul	a0, a1, a0
	addi	a0, a0, 4
	call	malloc
	addi	a1, a0, 4
	sw	s0, 0(a0)
	mv	a0, a1
	lw	ra, 12(sp)                      # 4-byte Folded Reload
	lw	s0, 8(sp)                       # 4-byte Folded Reload
	addi	sp, sp, 16
	ret
.Lfunc_end8:
	.size	alloca_helper, .Lfunc_end8-alloca_helper
                                        # -- End function
	.option	pop
	.option	push
	.option	arch, +a, +c, +m
	.globl	array.size                      # -- Begin function array.size
	.p2align	1
	.type	array.size,@function
array.size:                             # @array.size
# %bb.0:
	lw	a0, -4(a0)
	ret
.Lfunc_end9:
	.size	array.size, .Lfunc_end9-array.size
                                        # -- End function
	.option	pop
	.option	push
	.option	arch, +a, +c, +m
	.globl	string.length                   # -- Begin function string.length
	.p2align	1
	.type	string.length,@function
string.length:                          # @string.length
# %bb.0:
	li	a1, 0
.LBB10_1:                               # =>This Inner Loop Header: Depth=1
	add	a2, a0, a1
	lbu	a2, 0(a2)
	addi	a1, a1, 1
	bnez	a2, .LBB10_1
# %bb.2:
	addi	a0, a1, -1
	ret
.Lfunc_end10:
	.size	string.length, .Lfunc_end10-string.length
                                        # -- End function
	.option	pop
	.option	push
	.option	arch, +a, +c, +m
	.globl	string.substring                # -- Begin function string.substring
	.p2align	1
	.type	string.substring,@function
string.substring:                       # @string.substring
# %bb.0:
	addi	sp, sp, -16
	sw	ra, 12(sp)                      # 4-byte Folded Spill
	sw	s0, 8(sp)                       # 4-byte Folded Spill
	sw	s1, 4(sp)                       # 4-byte Folded Spill
	sw	s2, 0(sp)                       # 4-byte Folded Spill
	mv	s0, a1
	mv	s2, a0
	sub	s1, a2, a1
	addi	a0, s1, 1
	call	malloc
	add	a1, a0, s1
	blez	s1, .LBB11_3
# %bb.1:                                # %.preheader
	add	s0, s0, s2
	mv	a2, a0
.LBB11_2:                               # =>This Inner Loop Header: Depth=1
	lbu	a3, 0(s0)
	sb	a3, 0(a2)
	addi	a2, a2, 1
	addi	s0, s0, 1
	bne	a2, a1, .LBB11_2
.LBB11_3:
	sb	zero, 0(a1)
	lw	ra, 12(sp)                      # 4-byte Folded Reload
	lw	s0, 8(sp)                       # 4-byte Folded Reload
	lw	s1, 4(sp)                       # 4-byte Folded Reload
	lw	s2, 0(sp)                       # 4-byte Folded Reload
	addi	sp, sp, 16
	ret
.Lfunc_end11:
	.size	string.substring, .Lfunc_end11-string.substring
                                        # -- End function
	.option	pop
	.option	push
	.option	arch, +a, +c, +m
	.globl	string.parseInt                 # -- Begin function string.parseInt
	.p2align	1
	.type	string.parseInt,@function
string.parseInt:                        # @string.parseInt
# %bb.0:
	lbu	a1, 0(a0)
	li	a2, 45
	bne	a1, a2, .LBB12_5
# %bb.1:
	lbu	a1, 1(a0)
	addi	a2, a1, -48
	andi	a2, a2, 255
	li	a3, 9
	bltu	a3, a2, .LBB12_9
# %bb.2:                                # %.preheader
	li	a2, 0
	addi	a0, a0, 2
	li	a3, 10
.LBB12_3:                               # =>This Inner Loop Header: Depth=1
	andi	a4, a1, 255
	lbu	a1, 0(a0)
	mul	a2, a2, a3
	add	a2, a2, a4
	addi	a2, a2, -48
	addi	a4, a1, -48
	andi	a4, a4, 255
	addi	a0, a0, 1
	bltu	a4, a3, .LBB12_3
# %bb.4:
	neg	a0, a2
	ret
.LBB12_5:
	addi	a2, a1, -48
	andi	a2, a2, 255
	li	a3, 9
	bltu	a3, a2, .LBB12_10
# %bb.6:                                # %.preheader1
	li	a3, 0
	addi	a2, a0, 1
	li	a0, 0
	li	a3, 10
.LBB12_7:                               # =>This Inner Loop Header: Depth=1
	andi	a4, a1, 255
	lbu	a1, 0(a2)
	mul	a0, a0, a3
	add	a0, a0, a4
	addi	a0, a0, -48
	addi	a4, a1, -48
	andi	a4, a4, 255
	addi	a2, a2, 1
	bltu	a4, a3, .LBB12_7
# %bb.8:
	ret
.LBB12_9:
	neg	a0, zero
	ret
.LBB12_10:
	li	a0, 0
	ret
.Lfunc_end12:
	.size	string.parseInt, .Lfunc_end12-string.parseInt
                                        # -- End function
	.option	pop
	.option	push
	.option	arch, +a, +c, +m
	.globl	string.compare                  # -- Begin function string.compare
	.p2align	1
	.type	string.compare,@function
string.compare:                         # @string.compare
# %bb.0:
	lbu	a2, 0(a0)
	beqz	a2, .LBB13_5
# %bb.1:                                # %.preheader
	li	a3, 0
	addi	a0, a0, 1
.LBB13_2:                               # =>This Inner Loop Header: Depth=1
	add	a4, a1, a3
	lbu	a4, 0(a4)
	beqz	a4, .LBB13_6
# %bb.3:                                #   in Loop: Header=BB13_2 Depth=1
	andi	a5, a2, 255
	bne	a5, a4, .LBB13_8
# %bb.4:                                #   in Loop: Header=BB13_2 Depth=1
	add	a2, a0, a3
	lbu	a2, 0(a2)
	addi	a4, a3, 1
	mv	a3, a4
	bnez	a2, .LBB13_2
	j	.LBB13_7
.LBB13_5:
	li	a4, 0
	j	.LBB13_7
.LBB13_6:
	mv	a4, a3
.LBB13_7:
	add	a1, a1, a4
	lbu	a4, 0(a1)
.LBB13_8:
	andi	a0, a2, 255
	sub	a0, a0, a4
	ret
.Lfunc_end13:
	.size	string.compare, .Lfunc_end13-string.compare
                                        # -- End function
	.option	pop
	.option	push
	.option	arch, +a, +c, +m
	.globl	string.concat                   # -- Begin function string.concat
	.p2align	1
	.type	string.concat,@function
string.concat:                          # @string.concat
# %bb.0:
	addi	sp, sp, -32
	sw	ra, 28(sp)                      # 4-byte Folded Spill
	sw	s0, 24(sp)                      # 4-byte Folded Spill
	sw	s1, 20(sp)                      # 4-byte Folded Spill
	sw	s2, 16(sp)                      # 4-byte Folded Spill
	sw	s3, 12(sp)                      # 4-byte Folded Spill
	sw	s4, 8(sp)                       # 4-byte Folded Spill
	mv	s3, a1
	mv	s4, a0
	li	a0, 0
.LBB14_1:                               # =>This Inner Loop Header: Depth=1
	mv	s1, a0
	add	a0, a0, s4
	lbu	a1, 0(a0)
	addi	a0, s1, 1
	bnez	a1, .LBB14_1
# %bb.2:                                # %.preheader2
	li	a0, 0
.LBB14_3:                               # =>This Inner Loop Header: Depth=1
	mv	s0, a0
	add	a0, a0, s3
	lbu	a1, 0(a0)
	addi	a0, s0, 1
	bnez	a1, .LBB14_3
# %bb.4:
	add	s2, s0, s1
	addi	a0, s2, 1
	call	malloc
	add	a1, a0, s1
	beqz	s1, .LBB14_7
# %bb.5:                                # %.preheader
	mv	a2, a0
.LBB14_6:                               # =>This Inner Loop Header: Depth=1
	lbu	a3, 0(s4)
	sb	a3, 0(a2)
	addi	a2, a2, 1
	addi	s4, s4, 1
	bne	a2, a1, .LBB14_6
.LBB14_7:
	beqz	s0, .LBB14_10
# %bb.8:
	add	a2, a0, s2
.LBB14_9:                               # =>This Inner Loop Header: Depth=1
	lbu	a3, 0(s3)
	sb	a3, 0(a1)
	addi	a1, a1, 1
	addi	s3, s3, 1
	bne	a1, a2, .LBB14_9
.LBB14_10:
	add	s2, s2, a0
	sb	zero, 0(s2)
	lw	ra, 28(sp)                      # 4-byte Folded Reload
	lw	s0, 24(sp)                      # 4-byte Folded Reload
	lw	s1, 20(sp)                      # 4-byte Folded Reload
	lw	s2, 16(sp)                      # 4-byte Folded Reload
	lw	s3, 12(sp)                      # 4-byte Folded Reload
	lw	s4, 8(sp)                       # 4-byte Folded Reload
	addi	sp, sp, 32
	ret
.Lfunc_end14:
	.size	string.concat, .Lfunc_end14-string.concat
                                        # -- End function
	.option	pop
	.option	push
	.option	arch, +a, +c, +m
	.globl	string.copy                     # -- Begin function string.copy
	.p2align	1
	.type	string.copy,@function
string.copy:                            # @string.copy
# %bb.0:
	addi	sp, sp, -16
	sw	ra, 12(sp)                      # 4-byte Folded Spill
	sw	s0, 8(sp)                       # 4-byte Folded Spill
	sw	s1, 4(sp)                       # 4-byte Folded Spill
	sw	s2, 0(sp)                       # 4-byte Folded Spill
	mv	s1, a1
	mv	s2, a0
	li	s0, -1
.LBB15_1:                               # =>This Inner Loop Header: Depth=1
	add	a0, s1, s0
	lbu	a0, 1(a0)
	addi	s0, s0, 1
	bnez	a0, .LBB15_1
# %bb.2:
	addi	a0, s0, 1
	call	malloc
	sw	a0, 0(s2)
	beqz	s0, .LBB15_6
# %bb.3:                                # %.preheader
	li	a0, 0
.LBB15_4:                               # =>This Inner Loop Header: Depth=1
	lw	a1, 0(s2)
	add	a2, s1, a0
	lbu	a2, 0(a2)
	add	a1, a1, a0
	addi	a0, a0, 1
	sb	a2, 0(a1)
	bne	s0, a0, .LBB15_4
# %bb.5:
	lw	a0, 0(s2)
.LBB15_6:
	add	a0, a0, s0
	sb	zero, 0(a0)
	lw	ra, 12(sp)                      # 4-byte Folded Reload
	lw	s0, 8(sp)                       # 4-byte Folded Reload
	lw	s1, 4(sp)                       # 4-byte Folded Reload
	lw	s2, 0(sp)                       # 4-byte Folded Reload
	addi	sp, sp, 16
	ret
.Lfunc_end15:
	.size	string.copy, .Lfunc_end15-string.copy
                                        # -- End function
	.option	pop
	.option	push
	.option	arch, +a, +c, +m
	.globl	string.ord                      # -- Begin function string.ord
	.p2align	1
	.type	string.ord,@function
string.ord:                             # @string.ord
# %bb.0:
	add	a0, a0, a1
	lbu	a0, 0(a0)
	ret
.Lfunc_end16:
	.size	string.ord, .Lfunc_end16-string.ord
                                        # -- End function
	.option	pop
	.option	push
	.option	arch, +a, +c, +m
	.globl	array.alloca_inside             # -- Begin function array.alloca_inside
	.p2align	1
	.type	array.alloca_inside,@function
array.alloca_inside:                    # @array.alloca_inside
# %bb.0:
	addi	sp, sp, -48
	sw	ra, 44(sp)                      # 4-byte Folded Spill
	sw	s0, 40(sp)                      # 4-byte Folded Spill
	sw	s1, 36(sp)                      # 4-byte Folded Spill
	sw	s2, 32(sp)                      # 4-byte Folded Spill
	sw	s3, 28(sp)                      # 4-byte Folded Spill
	sw	s4, 24(sp)                      # 4-byte Folded Spill
	sw	s5, 20(sp)                      # 4-byte Folded Spill
	sw	s6, 16(sp)                      # 4-byte Folded Spill
	sw	s7, 12(sp)                      # 4-byte Folded Spill
	li	s0, 1
	mv	s4, a2
	mv	s3, a0
	bne	a1, s0, .LBB17_2
# %bb.1:
	lw	s0, 0(s4)
	mul	a0, s0, s3
	addi	a0, a0, 4
	call	malloc
	sw	s0, 0(a0)
	addi	a0, a0, 4
	j	.LBB17_7
.LBB17_2:
	mv	s2, a3
	mv	s5, a1
	lw	s1, 0(s4)
	slli	a0, s1, 2
	addi	a0, a0, 4
	call	malloc
	sw	s1, 0(a0)
	addi	a0, a0, 4
	beq	s2, s0, .LBB17_7
# %bb.3:
	blez	s1, .LBB17_7
# %bb.4:
	li	s1, 0
	addi	s5, s5, -1
	addi	s7, s4, 4
	addi	s2, s2, -1
	mv	s6, a0
	mv	s0, a0
.LBB17_5:                               # =>This Inner Loop Header: Depth=1
	mv	a0, s3
	mv	a1, s5
	mv	a2, s7
	mv	a3, s2
	call	array.alloca_inside
	lw	a1, 0(s4)
	sw	a0, 0(s0)
	addi	s1, s1, 1
	addi	s0, s0, 4
	blt	s1, a1, .LBB17_5
# %bb.6:
	mv	a0, s6
.LBB17_7:
	lw	ra, 44(sp)                      # 4-byte Folded Reload
	lw	s0, 40(sp)                      # 4-byte Folded Reload
	lw	s1, 36(sp)                      # 4-byte Folded Reload
	lw	s2, 32(sp)                      # 4-byte Folded Reload
	lw	s3, 28(sp)                      # 4-byte Folded Reload
	lw	s4, 24(sp)                      # 4-byte Folded Reload
	lw	s5, 20(sp)                      # 4-byte Folded Reload
	lw	s6, 16(sp)                      # 4-byte Folded Reload
	lw	s7, 12(sp)                      # 4-byte Folded Reload
	addi	sp, sp, 48
	ret
.Lfunc_end17:
	.size	array.alloca_inside, .Lfunc_end17-array.alloca_inside
                                        # -- End function
	.option	pop
	.option	push
	.option	arch, +a, +c, +m
	.globl	array.alloca                    # -- Begin function array.alloca
	.p2align	1
	.type	array.alloca,@function
array.alloca:                           # @array.alloca
# %bb.0:
	addi	sp, sp, -48
	sw	ra, 20(sp)                      # 4-byte Folded Spill
	sw	s0, 16(sp)                      # 4-byte Folded Spill
	sw	s1, 12(sp)                      # 4-byte Folded Spill
	sw	s2, 8(sp)                       # 4-byte Folded Spill
	sw	s3, 4(sp)                       # 4-byte Folded Spill
	mv	s1, a2
	mv	s2, a1
	mv	s3, a0
	sw	a7, 44(sp)
	sw	a6, 40(sp)
	sw	a5, 36(sp)
	sw	a4, 32(sp)
	sw	a3, 28(sp)
	slli	s0, a2, 2
	mv	a0, s0
	call	malloc
	mv	a2, a0
	addi	a0, sp, 28
	sw	a0, 0(sp)
	blez	s1, .LBB18_3
# %bb.1:
	lw	a0, 0(sp)
	addi	a0, a0, 4
	add	s0, s0, a2
	mv	a1, a2
.LBB18_2:                               # =>This Inner Loop Header: Depth=1
	sw	a0, 0(sp)
	lw	a3, -4(a0)
	sw	a3, 0(a1)
	addi	a1, a1, 4
	addi	a0, a0, 4
	bne	a1, s0, .LBB18_2
.LBB18_3:
	mv	a0, s3
	mv	a1, s2
	mv	a3, s1
	call	array.alloca_inside
	lw	ra, 20(sp)                      # 4-byte Folded Reload
	lw	s0, 16(sp)                      # 4-byte Folded Reload
	lw	s1, 12(sp)                      # 4-byte Folded Reload
	lw	s2, 8(sp)                       # 4-byte Folded Reload
	lw	s3, 4(sp)                       # 4-byte Folded Reload
	addi	sp, sp, 48
	ret
.Lfunc_end18:
	.size	array.alloca, .Lfunc_end18-array.alloca
                                        # -- End function
	.option	pop
	.type	.L.str,@object                  # @.str
	.section	.rodata.str1.1,"aMS",@progbits,1
.L.str:
	.asciz	"%s"
	.size	.L.str, 3

	.type	.L.str.1,@object                # @.str.1
.L.str.1:
	.asciz	"%s\n"
	.size	.L.str.1, 4

	.type	.L.str.2,@object                # @.str.2
.L.str.2:
	.asciz	"%d"
	.size	.L.str.2, 3

	.type	.L.str.3,@object                # @.str.3
.L.str.3:
	.asciz	"%d\n"
	.size	.L.str.3, 4

	.type	.L.str.4,@object                # @.str.4
.L.str.4:
	.asciz	"true"
	.size	.L.str.4, 5

	.type	.L.str.5,@object                # @.str.5
.L.str.5:
	.asciz	"false"
	.size	.L.str.5, 6

	.ident	"clang version 18.1.8"
	.section	".note.GNU-stack","",@progbits
