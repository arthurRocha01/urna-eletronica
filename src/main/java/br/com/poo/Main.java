package br.com.poo;

import br.com.poo.view.TelaPrincipal;

/**
 * Classe principal de execução da aplicação.
 * Inicializa e inicia a interface gráfica do simulador de voto.
 * 
 * @author Arthur Rocha
 * @version 1.0
 * @since 1.0
 */
public class Main {
    
    /**
     * Método principal que inicia a aplicação.
     * 
     * @param args argumentos de linha de comando (não utilizados)
     */
    public static void main(String[] args) {
        TelaPrincipal app = new TelaPrincipal();
        app.iniciar();
    }
}