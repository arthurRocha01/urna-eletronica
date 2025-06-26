MAIN_CLASS=br.com.poo.Main

.PHONY: all build run clean test

all: build

build:
	@echo "🔧 Compilando com Maven..."
	@mvn -q compile

run:
	@echo "🔧 Compilando com Maven..."
	@mvn -q compile
	@echo
	@echo "🚀 Executando com Maven..."
	@mvn -q exec:java -Dexec.mainClass=$(MAIN_CLASS)

test:
	@echo "🧪 Executando testes..."
	@mvn -q test

clean:
	@echo "🧹 Limpando projeto..."
	@mvn -q clean

