package br.com.poo.view;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;

public class Visor extends JPanel {

    public Visor() {
        inicializarComponentes();
    }

    private void inicializarComponentes() {
        configurarPainelPrincipal();
        adicionarLabels();
        adicionarFotos();
        adicionarCamposNumero(2, new int[]{110, 85}, new Dimension(120, 60));
    }

    private void configurarPainelPrincipal() {
        setLayout(null);
        setBackground(Color.WHITE);
        setBorder(new LineBorder(Color.BLACK, 3));
        setPreferredSize(new Dimension(800, 450));
    }

    private void adicionarLabels() {
        Object[][] labels = {
            { "SEU VOTO PARA:", 17, new Rectangle(20, 10, 300, 20) },
            { "Governador", 22, new Rectangle(300, 45, 300, 30) },
            { "Número:", 16, new Rectangle(20, 100, 100, 20) },

            { "Nome:", 16, new Rectangle(20, 190, 100, 20) },
            { "Vôlei", 16, new Rectangle(100, 190, 200, 20) },
            { "Partido:", 16, new Rectangle(20, 220, 100, 20) },
            { "PEsp", 16, new Rectangle(100, 220, 100, 20) },
            { "Vice-Governador:", 16, new Rectangle(20, 250, 160, 20) },
            { "Tênis", 16, new Rectangle(170, 250, 200, 20) },

            { "Governador", 12, new Rectangle(670, 210, 100, 20) },
            { "Vice-Governador", 12, new Rectangle(660, 380, 120, 20) },
            
            {
                "<html>" +
                    "<div style='letter-spacing: 1.5px;'>" +
                        "<hr style='border: 1px solid black;'><br><br>" +
                        "<div>Aperte a tecla:</div>" +
                        "<div><b>CONFIRMA</b> para CONFIRMAR este voto</div>" +
                        "<div><b>CORRIGE</b> para REINICIAR este voto</div>" +
                    "</div>" +
                "</html>",
                15, new Rectangle(20, 330, 500, 100)
            }
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

    private void adicionarFotos() {
        Rectangle[] posicoes = {
            new Rectangle(670, 100, 100, 100), // Foto Governador
            new Rectangle(670, 270, 100, 100)  // Foto Vice
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

    private void adicionarCamposNumero(int quantidade, int[] posicaoPainel, Dimension tamanhoPainel) {
        JPanel numeroPanel = new JPanel(null);
        numeroPanel.setBackground(Color.WHITE);
        numeroPanel.setBounds(posicaoPainel[0], posicaoPainel[1], tamanhoPainel.width, tamanhoPainel.height);

        int fieldWidth = 40;
        int fieldHeight = 55;
        int spacing = 5;

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