.PHONY: all build clean run test package

POM=pom.xml
MAIN_CLASS=br.com.poo.Main

all: build

build:
	mvn compile

package:
	mvn package

clean:
	mvn clean

test:
	mvn test

run:
	mvn exec:java -Dexec.mainClass=$(MAIN_CLASS)

