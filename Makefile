# Makefile para projeto Maven Java

.PHONY: all build clean run test package

# Caminho do arquivo pom.xml
POM=pom.xml

# Nome do main class (ajuste conforme necessário)
MAIN_CLASS=br.com.poo.Main

# Caminho do JAR gerado
TARGET_JAR=target/$(shell mvn help:evaluate -Dexpression=project.build.finalName -q -DforceStdout).jar

all: build

build:
	mvn compile

package:
	mvn package

clean:
	mvn clean

test:
	mvn test

run: package
	java -cp $(TARGET_JAR) $(MAIN_CLASS)

