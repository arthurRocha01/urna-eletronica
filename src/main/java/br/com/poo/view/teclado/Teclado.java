package br.com.poo.view.teclado;

import java.util.List;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import br.com.poo.controller.ControllerUrna;

public class Teclado extends JPanel {

    private ControllerUrna controller;

    public Teclado() {
        configurarLayout();
        construirComponentes();
    }

    private void configurarLayout() {
        setLayout(null);
        setBackground(new Color(30, 30, 30));
        setPreferredSize(new Dimension(400, 600));
        setBorder(BorderFactory.createLineBorder(Color.BLACK, 10, true));
    }

    private void construirComponentes() {
        adicionarCabecalho();
        adicionarBotoesNumericos();
        adicionarBotoesFuncionais();
    }

    private void adicionarCabecalho() {
        int largura = getPreferredSize().width;
        JPanel cabecalho = criarPainel(this, Color.LIGHT_GRAY, 0, 0, largura, 80);

        int brasaoTam = 50;
        int xBrasao = (largura - brasaoTam) / 5;
        int yBrasao = 15;

        criarPainel(cabecalho, Color.DARK_GRAY, xBrasao, yBrasao, brasaoTam, brasaoTam);
        cabecalho.add(criarLabel("JUSTIÇA ELEITORAL", 16, Color.BLACK, 15, 25, largura, 30, SwingConstants.CENTER));
    }

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

    private void criarBotaoNumerico(String texto, int x, int y, int largura, int altura) {
        JButton botao = criarBotao(texto, 20, Color.BLACK, Color.WHITE, x, y, largura, altura);
        botao.addActionListener(e -> controller.executarAcao(texto));
        add(botao);
    }

    private void criarBotaoFuncional(String texto, Color fundo, Color corTexto, int x, int y, int largura, int altura) {
        JButton botao = criarBotao(texto, 14, fundo, corTexto, x, y, largura, altura);
        botao.addActionListener(e -> controller.executarAcao(texto));
        add(botao);
    }

    private JButton criarBotao(String texto, int tamanhoFonte, Color fundo, Color corTexto, int x, int y, int largura, int altura) {
        JButton botao = new JButton(texto);
        botao.setFont(new Font("Arial", Font.BOLD, tamanhoFonte));
        botao.setBackground(fundo);
        botao.setForeground(corTexto);
        botao.setBounds(x, y, largura, altura);
        botao.setFocusable(false);
        return botao;
    }

    private JLabel criarLabel(String texto, int tamanhoFonte, Color corTexto, int x, int y, int largura, int altura, int alinhamento) {
        JLabel label = new JLabel(texto, alinhamento);
        label.setFont(new Font("Arial", Font.BOLD, tamanhoFonte));
        label.setForeground(corTexto);
        label.setBounds(x, y, largura, altura);
        return label;
    }

    private JPanel criarPainel(Container pai, Color corFundo, int x, int y, int largura, int altura) {
        JPanel painel = new JPanel(null);
        painel.setBackground(corFundo);
        painel.setBounds(x, y, largura, altura);
        pai.add(painel);
        return painel;
    }

    public void setController(ControllerUrna controller) {
        this.controller = controller;
    }
}