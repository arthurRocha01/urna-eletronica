package br.com.poo.view;

import javax.swing.*;
import java.awt.*;

public class TelaPrincipal extends JFrame {

    public TelaPrincipal() {
        setTitle("Simulador de Voto TSE - Layout");
        setSize(900, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);

        JPanel urnaPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 40));
        urnaPanel.setBackground(new Color(208, 208, 208));

        urnaPanel.add(new Visor());
        urnaPanel.add(new Teclado());

        add(urnaPanel);
        setVisible(true);
    }
}
