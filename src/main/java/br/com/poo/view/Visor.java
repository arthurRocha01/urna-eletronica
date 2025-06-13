package br.com.poo.view;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;

public class Visor extends JPanel {

    public Visor() {
        setLayout(null);
        setBackground(Color.WHITE);
        setBorder(new LineBorder(Color.BLACK, 3));
        setPreferredSize(new Dimension(550, 350));

        inicializarComponentes();
    }

    private void inicializarComponentes() {
        criarLabels();
        criarFotos();
        criarCamposNumero(2, new int[]{80, 65}, new Dimension(120, 60));
    }

    private void criarLabels() {
        Object[][] labels = {
            { "SEU VOTO PARA:", 14, new Rectangle(10, 5, 150, 20) },
            { "TREINAMENTO", 14, new Rectangle(200, 5, 150, 20) },
            { "Governador", 22, new Rectangle(10, 25, 200, 30) },
            { "Número:", 16, new Rectangle(10, 70, 100, 20) },
            { "Nome:", 16, new Rectangle(10, 135, 100, 20) },
            { "Vôlei", 16, new Rectangle(70, 135, 200, 20) },
            { "Partido:", 16, new Rectangle(10, 160, 100, 20) },
            { "PEsp", 16, new Rectangle(75, 160, 100, 20) },
            { "Vice-Governador:", 16, new Rectangle(10, 185, 160, 20) },
            { "Tênis", 16, new Rectangle(160, 185, 200, 20) },
            { "Governador", 12, new Rectangle(400, 120, 100, 20) },
            { "Vice-Governador", 12, new Rectangle(390, 250, 120, 20) },
            { "<html>Aperte a tecla:<br>CONFIRMA para CONFIRMAR este voto<br>CORRIGE para REINICIAR este voto</html>", 12, new Rectangle(10, 250, 350, 60) }
        };

        for (Object[] l : labels)
            add(criarLabel((String) l[0], (int) l[1], (Rectangle) l[2]));
    }

    private JLabel criarLabel(String texto, int tamanhoFonte, Rectangle bounds) {
        JLabel label = new JLabel(texto);
        label.setFont(new Font("Arial", Font.PLAIN, tamanhoFonte));
        label.setBounds(bounds);
        return label;
    }

    private void criarFotos() {
        Rectangle[] posicoes = {
            new Rectangle(400, 20, 100, 100),
            new Rectangle(400, 150, 100, 100)
        };

        for (Rectangle pos : posicoes)
            add(criarPainelFoto(pos));
    }

    private JPanel criarPainelFoto(Rectangle bounds) {
        JPanel painel = new JPanel();
        painel.setBackground(new Color(230, 230, 230));
        painel.setBorder(new LineBorder(Color.BLACK, 1));
        painel.setBounds(bounds);
        return painel;
    }

    private void criarCamposNumero(int quantidade, int[] posicaoPainel, Dimension tamanhoPainel) {
        JPanel numeroPanel = new JPanel(null);
        numeroPanel.setBackground(Color.WHITE);
        numeroPanel.setBounds(posicaoPainel[0], posicaoPainel[1], tamanhoPainel.width, tamanhoPainel.height);

        int fieldWidth = 50;
        int fieldHeight = 60;
        int spacing = 10;

        for (int i = 0; i < quantidade; i++) {
            JTextField campo = new JTextField();
            campo.setFont(new Font("Arial", Font.BOLD, 28));
            campo.setHorizontalAlignment(JTextField.CENTER);
            campo.setBackground(Color.WHITE);
            campo.setBorder(new LineBorder(Color.BLACK, 2));
            campo.setBounds(i * (fieldWidth + spacing), 0, fieldWidth, fieldHeight);
            numeroPanel.add(campo);
        }

        add(numeroPanel);
    }
}
