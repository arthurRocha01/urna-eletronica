package br.com.poo.view;

import javax.swing.*;
import java.awt.*;

public class TelaPrincipal extends JFrame {

    public TelaPrincipal() {
        inicializarComponentes();
    }

    private void inicializarComponentes() {
        configurarPainelPrincipal();
        adcionarPainel();
    }

    private void configurarPainelPrincipal() {
        setTitle("Simulador de Voto TSE - Layout");
        setSize(1300, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        
    }

    private void adcionarPainel() {
        JPanel urnaPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 40));
        urnaPanel.setBackground(new Color(208, 208, 208));
        adcionarPaineis(urnaPanel);
        add(urnaPanel);
        setVisible(true);
    }

    private void adcionarPaineis(Container layout) {
        layout.add(new Visor());
        layout.add(new Teclado());
    }
}
