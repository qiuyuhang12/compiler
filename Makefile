.PHONY: Compiler
Compiler:
	find src -name ".antlr" -exec rm -r {} \; && find src -name '*.java' | xargs javac -d bin -cp /usr/share/java/antlr-4.13.2-complete.jar

.PHONY: Sema
Sema: Compiler
	./testcases/sema/scripts/test.bash 'java -cp /usr/share/java/antlr-4.13.2-complete.jar:bin Main' $(file)

.PHONY: Semall
Semall: Compiler
	./testcases/sema/scripts/test_all.bash 'java -cp /usr/share/java/antlr-4.13.2-complete.jar:bin Main' testcases/sema/