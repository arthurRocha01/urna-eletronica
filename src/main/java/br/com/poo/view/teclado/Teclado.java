package br.com.poo.view.teclado;

import java.util.List;
import java.awt.*;
import javax.swing.*;

import br.com.poo.controller.ControllerUrna;

/**
 * Classe responsável por construir e exibir o teclado da urna eletrônica.
 * 
 * <p>Contém botões numéricos (0 a 9) e botões funcionais (BRANCO, CORRIGE, CONFIRMA),
 * além de um cabeçalho com a identificação da Justiça Eleitoral.
 *
 * <p>Os botões executam ações diretamente no {@link ControllerUrna}, conforme interação do usuário.
 * 
 * @author Arthur Rocha
 * @version 1.0
 * @since 1.0
 */
public class Teclado extends JPanel {

    private ControllerUrna controller;

    /**
     * Construtor da classe Teclado. Inicializa o layout e os componentes visuais.
     */
    public Teclado() {
        configurarLayout();
        construirComponentes();
    }

    /**
     * Define as configurações de layout do teclado, como fundo, borda e tamanho.
     */
    private void configurarLayout() {
        setLayout(null);
        setBackground(new Color(30, 30, 30));
        setPreferredSize(new Dimension(400, 600));
        setBorder(BorderFactory.createLineBorder(Color.BLACK, 10, true));
    }

    /**
     * Constrói os componentes principais do teclado: cabeçalho, botões numéricos e funcionais.
     */
    private void construirComponentes() {
        adicionarCabecalho();
        adicionarBotoesNumericos();
        adicionarBotoesFuncionais();
    }

    /**
     * Adiciona o cabeçalho visual com brasão e texto da Justiça Eleitoral.
     */
    private void adicionarCabecalho() {
        int largura = getPreferredSize().width;
        JPanel cabecalho = criarPainel(this, Color.LIGHT_GRAY, 0, 0, largura, 80);

        int brasaoTam = 50;
        int xBrasao = (largura - brasaoTam) / 5;
        int yBrasao = 15;

        criarPainel(cabecalho, Color.DARK_GRAY, xBrasao, yBrasao, brasaoTam, brasaoTam);
        cabecalho.add(criarLabel("JUSTIÇA ELEITORAL", 16, Color.BLACK, 15, 25, largura, 30, SwingConstants.CENTER));
    }

    /**
     * Adiciona os botões numéricos de 1 a 9, organizados em grade.
     * O botão 0 é adicionado na linha inferior, centralizado.
     */
    private void adicionarBotoesNumericos() {
        List<String> numeros = List.of("1", "2", "3", "4", "5", "6", "7", "8", "9");
        int largura = 80, altura = 60, padding = 15;
        int colunas = 3;
        int totalLargura = colunas * largura + (colunas - 1) * padding;
        int xInicial = (getPreferredSize().width - totalLargura) / 2;
        int yInicial = 100;

        int x = xInicial, y = yInicial;

        for (int i = 0; i < numeros.size(); i++) {
            criarBotaoNumerico(numeros.get(i), x, y, largura, altura);
            x += largura + padding;
            if ((i + 1) % colunas == 0) {
                x = xInicial;
                y += altura + padding;
            }
        }

        criarBotaoNumerico("0", xInicial + largura + padding, y, largura, altura);
    }

    /**
     * Adiciona os botões BRANCO, CORRIGE e CONFIRMA, com cores e larguras distintas.
     */
    private void adicionarBotoesFuncionais() {
        int altura = 65, padding = 15;
        int larguraBranco = 100;
        int larguraCorrige = 110;
        int larguraConfirma = 230;
        int y = 100 + 3 * (60 + padding) + padding + 60;

        int xBase = (getPreferredSize().width - (larguraBranco + larguraCorrige + padding)) / 2;

        criarBotaoFuncional("BRANCO", Color.WHITE, Color.BLACK, xBase, y, larguraBranco, altura);
        criarBotaoFuncional("CORRIGE", new Color(255, 140, 0), Color.WHITE, xBase + larguraBranco + padding, y, larguraCorrige, altura);

        y += altura + padding;
        int xConfirma = (getPreferredSize().width - larguraConfirma) / 2;
        criarBotaoFuncional("CONFIRMA", new Color(0, 153, 0), Color.WHITE, xConfirma, y, larguraConfirma, altura);
    }

    /**
     * Cria e adiciona um botão numérico que envia o texto para o controller.
     *
     * @param texto texto do botão (número)
     * @param x posição horizontal
     * @param y posição vertical
     * @param largura largura do botão
     * @param altura altura do botão
     */
    private void criarBotaoNumerico(String texto, int x, int y, int largura, int altura) {
        JButton botao = criarBotao(texto, 20, Color.BLACK, Color.WHITE, x, y, largura, altura);
        botao.addActionListener(e -> controller.executarAcao(texto));
        add(botao);
    }

    /**
     * Cria e adiciona um botão funcional (BRANCO, CORRIGE, CONFIRMA).
     *
     * @param texto texto do botão
     * @param fundo cor de fundo
     * @param corTexto cor do texto
     * @param x posição horizontal
     * @param y posição vertical
     * @param largura largura do botão
     * @param altura altura do botão
     */
    private void criarBotaoFuncional(String texto, Color fundo, Color corTexto, int x, int y, int largura, int altura) {
        JButton botao = criarBotao(texto, 14, fundo, corTexto, x, y, largura, altura);
        botao.addActionListener(e -> controller.executarAcao(texto));
        add(botao);
    }

    /**
     * Cria um botão genérico com texto, cores, posição e tamanho definidos.
     *
     * @param texto texto do botão
     * @param tamanhoFonte tamanho da fonte
     * @param fundo cor de fundo
     * @param corTexto cor do texto
     * @param x posição horizontal
     * @param y posição vertical
     * @param largura largura do botão
     * @param altura altura do botão
     * @return botão configurado
     */
    private JButton criarBotao(String texto, int tamanhoFonte, Color fundo, Color corTexto, int x, int y, int largura, int altura) {
        JButton botao = new JButton(texto);
        botao.setFont(new Font("Arial", Font.BOLD, tamanhoFonte));
        botao.setBackground(fundo);
        botao.setForeground(corTexto);
        botao.setBounds(x, y, largura, altura);
        botao.setFocusable(false);
        return botao;
    }

    /**
     * Cria um rótulo (JLabel) com texto, fonte, cor e alinhamento especificados.
     *
     * @param texto texto do rótulo
     * @param tamanhoFonte tamanho da fonte
     * @param corTexto cor do texto
     * @param x posição horizontal
     * @param y posição vertical
     * @param largura largura do rótulo
     * @param altura altura do rótulo
     * @param alinhamento alinhamento do texto (ex: SwingConstants.CENTER)
     * @return rótulo configurado
     */
    private JLabel criarLabel(String texto, int tamanhoFonte, Color corTexto, int x, int y, int largura, int altura, int alinhamento) {
        JLabel label = new JLabel(texto, alinhamento);
        label.setFont(new Font("Arial", Font.BOLD, tamanhoFonte));
        label.setForeground(corTexto);
        label.setBounds(x, y, largura, altura);
        return label;
    }

    /**
     * Cria um painel com posição, cor e tamanho definidos, e o adiciona ao container pai.
     *
     * @param pai container ao qual o painel será adicionado
     * @param corFundo cor de fundo do painel
     * @param x posição horizontal
     * @param y posição vertical
     * @param largura largura do painel
     * @param altura altura do painel
     * @return painel criado
     */
    private JPanel criarPainel(Container pai, Color corFundo, int x, int y, int largura, int altura) {
        JPanel painel = new JPanel(null);
        painel.setBackground(corFundo);
        painel.setBounds(x, y, largura, altura);
        pai.add(painel);
        return painel;
    }

    /**
     * Define o controller associado ao teclado, responsável por processar ações dos botões.
     *
     * @param controller instância do {@link ControllerUrna}
     */
    public void setController(ControllerUrna controller) {
        this.controller = controller;
    }
}