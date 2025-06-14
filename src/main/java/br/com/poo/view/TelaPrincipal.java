package br.com.poo.view;

import javax.swing.*;
import java.awt.*;

public class TelaPrincipal extends JFrame {

    public TelaPrincipal() {
        inicializarComponentes();
    }

    private void inicializarComponentes() {
        configurarJanela();
        adicionarPainelPrincipal();
    }

    private void configurarJanela() {
        setTitle("Simulador de Voto TSE - Layout");
        setSize(1300, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
    }

    private void adicionarPainelPrincipal() {
        JPanel painelPrincipal = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        painelPrincipal.setBackground(new Color(208, 208, 208));

        // Painel com legenda e visor empilhados verticalmente
        JPanel painelEsquerda = new JPanel();
        painelEsquerda.setLayout(new BoxLayout(painelEsquerda, BoxLayout.Y_AXIS));
        painelEsquerda.setOpaque(false);

        painelEsquerda.add(new Legenda());
        painelEsquerda.add(Box.createRigidArea(new Dimension(0, 10))); // Espaço entre Legenda e Visor
        painelEsquerda.add(new Visor());

        painelPrincipal.add(painelEsquerda);
        painelPrincipal.add(new Teclado());

        setContentPane(painelPrincipal);
        setVisible(true);
    }
}
