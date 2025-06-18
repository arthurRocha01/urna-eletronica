package br.com.poo.view.teclado;

import br.com.poo.controller.ControllerUrna;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.List;

public class Teclado extends JPanel {

    private ControllerUrna controller;

    public Teclado() {
        inicializarComponentes();
    }

    private void inicializarComponentes() {
        configurarPainelPrincipal();
        adicionarCabecalho();
        adicionarBotoesNumericos();
        adicionarBotoesEspeciais();
    }

    private void configurarPainelPrincipal() {
        setLayout(null);
        setBackground(new Color(30, 30, 30));
        setPreferredSize(new Dimension(400, 600));
        setBorder(BorderFactory.createLineBorder(Color.BLACK, 10, true));
    }

    private void adicionarCabecalho() {
        int largura = getPreferredSize().width;
        JPanel cabecalho = criarPainel(this, Color.LIGHT_GRAY, 0, 0, largura, 80);

        int larguraBrasa = 50;
        int alturaBrasa = 50;
        int xBrasa = (largura - larguraBrasa) / 5;
        int yBrasa = 15;

        criarPainel(cabecalho, Color.DARK_GRAY, xBrasa, yBrasa, larguraBrasa, alturaBrasa);
        cabecalho.add(criarLabel("JUSTIÇA ELEITORAL", 16, Color.BLACK, 15, 25, largura, 30, SwingConstants.CENTER));
    }

    private JLabel criarLabel(String texto, int tamanho, Color cor, int x, int y, int largura, int altura, int alinhamento) {
        JLabel label = new JLabel(texto, alinhamento);
        label.setFont(new Font("Arial", Font.BOLD, tamanho));
        label.setForeground(cor);
        label.setBounds(x, y, largura, altura);
        return label;
    }

    private JPanel criarPainel(Container pai, Color cor, int x, int y, int largura, int altura) {
        JPanel painel = new JPanel(null);
        painel.setBackground(cor);
        painel.setBounds(x, y, largura, altura);
        pai.add(painel);
        return painel;
    }

    private void adicionarBotoesNumericos() {
        List<String> numeros = Arrays.asList("1", "2", "3", "4", "5", "6", "7", "8", "9");
        int btnWidth = 80;
        int btnHeight = 60;
        int padding = 15;
        int totalBtnWidth = 3 * btnWidth + 2 * padding;
        int xStart = (getPreferredSize().width - totalBtnWidth) / 2;
        int yStart = 100;
        int x = xStart;
        int y = yStart;

        for (int i = 0; i < numeros.size(); i++) {
            criarBotaoNumerico(numeros.get(i), x, y, btnWidth, btnHeight);
            x += btnWidth + padding;
            if ((i + 1) % 3 == 0) {
                x = xStart;
                y += btnHeight + padding;
            }
        }

        criarBotaoNumerico("0", xStart + btnWidth + padding, y, btnWidth, btnHeight);
    }

    private void criarBotaoNumerico(String texto, int x, int y, int largura, int altura) {
        JButton btn = criarBotao(texto, 20, Color.BLACK, Color.WHITE, x, y, largura, altura);
        btn.setFocusable(false);
        btn.addActionListener(e -> controller.onAcao(texto));
        add(btn);
    }

    private void adicionarBotoesEspeciais() {
        int btnWidth = 100;
        int btnHeight = 65;
        int padding = 15;
        int confirmaWidth = 230;
        int xStart = (getPreferredSize().width - (2 * btnWidth + padding)) / 2;
        int y = 100 + 3 * (60 + padding) + padding + 60;

        criarBotaoEspecial("BRANCO", 14, Color.WHITE, Color.BLACK, xStart, y, btnWidth, btnHeight);
        criarBotaoEspecial("CORRIGE", 14, new Color(255, 140, 0), Color.WHITE, xStart + btnWidth + padding, y, btnWidth + 10, btnHeight);

        y += btnHeight + padding;
        int xConfirma = (getPreferredSize().width - confirmaWidth) / 2;
        criarBotaoEspecial("CONFIRMA", 14, new Color(0, 153, 0), Color.WHITE, xConfirma, y, confirmaWidth, btnHeight);
    }

    private void criarBotaoEspecial(String texto, int fonte, Color fundo, Color textoCor, int x, int y, int largura, int altura) {
        JButton btn = criarBotao(texto, fonte, fundo, textoCor, x, y, largura, altura);
        btn.addActionListener(e -> controller.onAcao(texto));
        add(btn);
    }

    private JButton criarBotao(String texto, int fonte, Color fundo, Color textoCor, int x, int y, int largura, int altura) {
        JButton botao = new JButton(texto);
        botao.setFont(new Font("Arial", Font.BOLD, fonte));
        botao.setBackground(fundo);
        botao.setForeground(textoCor);
        botao.setBounds(x, y, largura, altura);
        return botao;
    }

    public void setController(ControllerUrna controller) {
        this.controller = controller;
    }
}