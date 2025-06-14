package br.com.poo.view;

import javax.swing.*;
import java.awt.*;

public class Teclado extends JPanel {

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
    }

    private void adicionarCabecalho() {
        int largura = getPreferredSize().width;
        JPanel cabecalho = criarPainel(this, Color.LIGHT_GRAY, 0, 0, largura, 80);

        int larguraBrasa = 50;
        int alturaBrasa = 50;
        int xBrasa = (largura - larguraBrasa) / 5;
        int yBrasa = 15;
        criarPainel(cabecalho, Color.DARK_GRAY, xBrasa, yBrasa, larguraBrasa, alturaBrasa);

        JLabel label = new JLabel("JUSTIÇA ELEITORAL", SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 16));
        label.setBounds(15, 25, largura, 30);
        cabecalho.add(label);
    }

    private JPanel criarPainel(Container pai, Color cor, int x, int y, int largura, int altura) {
        JPanel painel = new JPanel(null);
        painel.setBackground(cor);
        painel.setBounds(x, y, largura, altura);
        pai.add(painel);
        return painel;
    }

    private void adicionarBotoesNumericos() {
        String[] numeros = {"1", "2", "3", "4", "5", "6", "7", "8", "9"};
        int btnWidth = 80, btnHeight = 60, padding = 15;

        int totalBtnWidth = 3 * btnWidth + 2 * padding;
        int xStart = (getPreferredSize().width - totalBtnWidth) / 2;
        int yStart = 100;
        int x = xStart, y = yStart;

        for (int i = 0; i < numeros.length; i++) {
            criarBotaoNumerico(numeros[i], x, y, btnWidth, btnHeight);
            x += btnWidth + padding;
            if ((i + 1) % 3 == 0) {
                x = xStart;
                y += btnHeight + padding;
            }
        }

        // Botão 0 centralizado abaixo
        criarBotaoNumerico("0", xStart + btnWidth + padding, y, btnWidth, btnHeight);
    }

    private void adicionarBotoesEspeciais() {
        int btnWidth = 100, btnHeight = 65, padding = 15;
        int confirmaWidth = 230;
        int xStart = (getPreferredSize().width - (2 * btnWidth + padding)) / 2;
        int y = 100 + 3 * (60 + padding) + padding + 60;

        criarBotaoEspecial("BRANCO", 14, Color.WHITE, Color.BLACK, xStart, y, btnWidth, btnHeight);
        criarBotaoEspecial("CORRIGE", 14, new Color(255, 140, 0), Color.WHITE, xStart + btnWidth + padding, y, btnWidth +10, btnHeight);

        y += btnHeight + padding;

        int xConfirma = (getPreferredSize().width - confirmaWidth) / 2;
        criarBotaoEspecial("CONFIRMA", 14, new Color(0, 153, 0), Color.WHITE, xConfirma, y, confirmaWidth, btnHeight);
    }

    private void criarBotaoNumerico(String texto, int x, int y, int largura, int altura) {
        JButton btn = new JButton(texto);
        btn.setFont(new Font("Arial", Font.BOLD, 20));
        btn.setBackground(Color.BLACK);
        btn.setForeground(Color.WHITE);
        btn.setBounds(x, y, largura, altura);
        add(btn);
    }

    private void criarBotaoEspecial(String texto, int tamanhoFonte, Color corFundo, Color corTexto, int x, int y, int largura, int altura) {
        JButton botao = new JButton(texto);
        botao.setFont(new Font("Arial", Font.BOLD, tamanhoFonte));
        botao.setBackground(corFundo);
        botao.setForeground(corTexto);
        botao.setBounds(x, y, largura, altura);
        add(botao);
    }
}