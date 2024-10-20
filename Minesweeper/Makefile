SUBMIT := $(wildcard src/main/java/**/*.java src/test/java/**/*.java src/main/java/Game.java files/** README.txt)

ts := $(shell /bin/date "+%Y-%m-%d-%H:%M:%S")

ZIPNAME := $(HWNAME)-submit($(ts)).zip

.PHONY: all compile run zip test test-stack-trace checkstyle format clean

all:	

compile:
	mvn compile

run: compile
	mvn exec:java

test: 
	mvn test

test-stack-trace:
	mvn -DtrimStackTrace=false test

checkstyle: 
	mvn checkstyle:checkstyle

format:
	mvn formatter:format

clean:
	rm -f src/*.class bin/* test/*.class
	rm -rf *.zip
	mvn clean
