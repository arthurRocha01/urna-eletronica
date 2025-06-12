# Makefile para projeto Maven Java

.PHONY: all build clean run test package

# Caminho do arquivo pom.xml
POM=pom.xml

# Nome da classe principal
MAIN_CLASS=br.com.poo.Main

# Caminho do JAR gerado
TARGET_JAR=target/$(shell mvn help:evaluate -Dexpression=project.build.finalName -q -DforceStdout)

all: build

build:
	@mvn compile > /dev/null

package:
	@mvn package > /dev/null

clean:
	@mvn clean > /dev/null

test:
	@mvn test

run: package
	@java -cp $(TARGET_JAR).jar $(MAIN_CLASS)

