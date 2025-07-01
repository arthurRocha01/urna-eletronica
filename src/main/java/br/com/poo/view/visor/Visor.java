package br.com.poo.view.visor;

import javax.swing.JPanel;
import javax.swing.JTextField;

import org.bson.Document;

import br.com.poo.controller.ControllerUrna;

/**
 * Painel principal que representa o visor da urna eletrônica,
 * responsável por receber e exibir os dígitos do voto, controlar o fluxo
 * da votação e interagir com o controlador para buscar dados e atualizar a interface.
 * 
 * @author Arthur Rocha
 * @version 1.0
 * @since 1.0
 */
public class Visor extends JPanel {

    /** Largura fixa do visor */
    public final int LARGURA = 700;
    /** Altura fixa do visor */
    public final int ALTURA = 400;

    /** Builder responsável pela construção e navegação das telas internas */
    public VisorBuilder builder;
    /** Controlador para manipulação da lógica de votação */
    public ControllerUrna controlador;

    /** Número digitado pelo usuário, acumulado */
    public StringBuilder numeroDigitado;
    /** Campos de texto para cada dígito do voto (máximo 5) */
    public JTextField[] camposNumero = new JTextField[5];

    /** Indica se o teclado está bloqueado para entrada */
    public boolean tecladoBloqueado = false;
    /** Indica se os botões estão bloqueados */
    public boolean botoesBloqueados = false;

    /**
     * Construtor padrão que inicializa o builder e inicia a tela inicial.
     */
    public Visor() {
        this.builder = new VisorBuilder(this);
        builder.iniciarTela();
    }

    /**
     * Insere um dígito no primeiro campo vazio, se o teclado não estiver bloqueado,
     * e atualiza as informações da tela conforme o voto é digitado.
     * 
     * @param digito dígito a ser inserido (deve ser string numérica)
     */
    public void inserirDigito(String digito) {
        if (tecladoBloqueado) return;

        for (JTextField campo : camposNumero) {
            if (campo.getText().isEmpty()) {
                campo.setText(digito);
                break;
            }
        }

        processarVoto();
    }

    /**
     * Verifica o número digitado e busca informações de partido ou candidato
     * para exibir na interface, conforme o tamanho do voto digitado.
     */
    private void processarVoto() {
        String voto = getVotoInserido();
        
        if (voto.length() == 2) {
            Document partido = controlador.buscarPartido(voto);
            if (partido != null) builder.mostrarInformacoesPartido(partido);
        }
        else if (voto.length() == 5) {
            Document candidato = controlador.buscarCandidato(voto);
            if (candidato != null) builder.mostrarInformacoesCandidato(candidato);
        }
    }

    /**
     * Retorna o número completo digitado pelo usuário,
     * concatenando o texto dos campos de dígitos.
     * 
     * @return String com o voto inserido
     */
    public String getVotoInserido() {
        numeroDigitado = new StringBuilder();
        for (JTextField campo : camposNumero) {
            numeroDigitado.append(campo.getText());
        }
        return numeroDigitado.toString();
    }

    /**
     * Verifica se todos os campos de voto estão preenchidos,
     * e bloqueia o teclado para evitar mais entradas.
     * 
     * @return true se o voto estiver completo, false caso contrário
     */
    public boolean isVotoCompleto() {
        for (JTextField campo : camposNumero) {
            if (campo.getText().isEmpty()) return false;
        }
        // bloquearTeclado();
        return true;
    }

    /**
     * Bloqueia o teclado para impedir novas entradas.
     */
    public void bloquearTeclado() {
        tecladoBloqueado = true;
        controlador.avisarSistema("Visor", "teclado bloquado");
    }

    /**
     * Indica se a tela de confirmação de voto branco está sendo exibida.
     * 
     * @return true se estiver votando em branco, false caso contrário
     */
    public boolean isVotandoBranco() {
        return builder.telaConfirmaBranco.isShowing();
    }

    /**
     * Indica se a tela de gravação do voto está sendo exibida.
     * 
     * @return true se estiver gravando, false caso contrário
     */
    public boolean isGravandoVoto() {
        return builder.telaConfirmaCandidato.isShowing();
    }

    /**
     * Indica se a votação foi finalizada, ou seja, se a tela de contabilidade está visível.
     * 
     * @return true se a votação está finalizada, false caso contrário
     */
    public boolean isFinalizado() {
        return builder.telaContabilidade.isShowing();
    }

    /**
     * Limpa os campos de entrada do voto, desbloqueia teclado e botões,
     * e limpa informações exibidas do candidato.
     */
    public void limparCamposVoto() {
        desbloquearTeclado();
        desbloquearBotoes();
        for (JTextField campo : camposNumero) campo.setText("");
        builder.limparInformacoesCandidato();
    }

    /**
     * Desbloqueia o teclado para permitir entrada de dígitos.
     */
    public void desbloquearTeclado() {
        tecladoBloqueado = false;
        controlador.avisarSistema("Visor", "teclado desbloqueado");
    }

    /**
     * Bloqueia os botões para impedir ações do usuário.
     */
    public void bloquearBotoes() {
        botoesBloqueados = true;
        controlador.avisarSistema("Visor", "botoes bloquados");
    }

    /**
     * Desbloqueia os botões para permitir ações do usuário.
     */
    public void desbloquearBotoes() {
        botoesBloqueados = false;
        controlador.avisarSistema("Visor", "botoes desbloqueados");
    }

    /**
     * Verifica se todos os campos de voto estão vazios.
     * 
     * @return true se todos os campos estiverem vazios, false caso contrário
     */
    public boolean votosEstaVazio() {
        for (JTextField campo : camposNumero) {
            if (!campo.getText().isEmpty()) return false;
        }
        return true;
    }

    /**
     * Define o controlador responsável pela lógica da urna para esta instância.
     * 
     * @param controlador ControllerUrna a ser associado
     */
    public void setController(ControllerUrna controlador) {
        this.controlador = controlador;
    }
}