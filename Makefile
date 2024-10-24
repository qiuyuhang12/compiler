
.PHONY: build
build:
	find src -name ".antlr" -exec rm -r {} \; && find src -name '*.java' | xargs javac -d bin -cp /ulib/antlr-4.13.2-complete.jar

# 	find src -name ".antlr" -exec rm -r {} \; && find src -name '*.java' | xargs javac -d bin -cp /ulib/antlr-4.13.2-complete.jar

.PHONY: run
run:
	java -Xss40M -cp /ulib/antlr-4.13.2-complete.jar:bin Main



# .PHONY: build
# build:
# 	find src -name ".antlr" -exec rm -r {} \; && find src -name '*.java' | xargs javac -d bin -cp ./src/Parser/lib/antlr-4.13.2-complete.jar
#
#
# .PHONY: run
# run:
# # build
# 	java -ea -Xss40M -cp ./src/Parser/lib/antlr-4.13.2-complete.jar:bin Main
# # 	java -server -cp ./src/Parser/lib/antlr-4.13.2-complete.jar:bin Main
#
# #
# #
# # # .PHONY: Sema
# # # Sema: build
# # # 	./testcases/sema/scripts/test.bash 'java -cp /usr/share/java/antlr-4.13.2-complete.jar:bin Main' $(file)
#
# .PHONY: Codegen
# Codegen: build
# 	./testcases/codegen/scripts/test_asm.bash 'java -Xss40M -cp /usr/share/java/antlr-4.13.2-complete.jar:bin Main' $(file) builtin.s
# # 	./testcases/codegen/scripts/test_asm.bash 'java -cp /usr/share/java/antlr-4.13.2-complete.jar:bin Main' testcases/codegen/e1.mx builtin.s
# #
# #
# #
# .PHONY: Codegenall
# Codegenall:build
# 	./testcases/codegen/scripts/test_asm_all.bash 'java -ea -Xss40M -cp /usr/share/java/antlr-4.13.2-complete.jar:bin Main -S' testcases/codegen/ builtin.s
# #
#
# # libfmt.so.10
# .PHONY: Semall
# Semall: build
# 	./testcases/sema/scripts/test_all.bash 'java -cp /usr/share/java/antlr-4.13.2-complete.jar:bin Main' testcases/sema/
# #
# #
# # # .PHONY: test
# # # test:
# # #     java -Xss8m -cp ulib/antlr-4.13.2-complete.jar:bin Main
# #
# # #testcases/codegen/scripts/test_asm_all.bash 'java -cp /usr/share/java/antlr-4.13.2-complete.jar:bin Main -S' testcases/codegen/ builtin.s
# #
# # # testcases/codegen/scripts/test_asm.bash 'java -cp /usr/share/java/antlr-runtime-4.13.2.jar:bin Main -S' testcases/codegen/e1.mx bin/builtin.s
# #
