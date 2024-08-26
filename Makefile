# .PHONY: build
# build:
# 	find src -name ".antlr" -exec rm -r {} \; && find src -name '*.java' | xargs javac -d bin -cp /ulib/antlr-4.13.2-complete.jar
.PHONY: build
build:
	find src -name ".antlr" -exec rm -r {} \; && find src -name '*.java' | xargs javac -d bin -cp ./src/Parser/lib/antlr-4.13.2-complete.jar


# .PHONY: run
# run:
# 	java -cp /ulib/antlr-4.13.2-complete.jar:bin Main
.PHONY: run
run:
	java -cp ./src/Parser/lib/antlr-4.13.2-complete.jar:bin Main


.PHONY: Sema
Sema: build
	./testcases/sema/scripts/test.bash 'java -cp /usr/share/java/antlr-4.13.2-complete.jar:bin Main' $(file)



.PHONY: Semall
Semall: build
	./testcases/sema/scripts/test_all.bash 'java -cp /usr/share/java/antlr-4.13.2-complete.jar:bin Main' testcases/sema/


# .PHONY: test
# test:
#     java -Xss8m -cp ulib/antlr-4.13.2-complete.jar:bin Main