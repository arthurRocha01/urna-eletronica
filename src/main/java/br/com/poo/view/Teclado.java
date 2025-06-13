package br.com.poo.view;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;

public class Teclado extends JPanel {

    public Teclado() {
        setLayout(null);
        setBackground(new Color(169, 169, 169));
        setPreferredSize(new Dimension(220, 320));
        setBorder(new LineBorder(Color.BLACK, 3));

        int btnWidth = 50;
        int btnHeight = 45;
        int padding = 5;
        String[] numeros = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "0"};

        int x = 0, y = 0;
        for (int i = 0; i < numeros.length; i++) {
            JButton btn = new JButton(numeros[i]);
            btn.setFont(new Font("Arial", Font.BOLD, 16));
            btn.setBounds(x, y, btnWidth, btnHeight);
            add(btn);

            x += btnWidth + padding;
            if ((i + 1) % 3 == 0) {
                x = 0;
                y += btnHeight + padding;
            }
        }

        JButton btnBranco = new JButton("BRANCO");
        btnBranco.setFont(new Font("Arial", Font.BOLD, 14));
        btnBranco.setBackground(Color.WHITE);
        btnBranco.setBounds(0, y, btnWidth * 2 + padding, btnHeight);
        add(btnBranco);

        JButton btnCorrige = new JButton("CORRIGE");
        btnCorrige.setFont(new Font("Arial", Font.BOLD, 14));
        btnCorrige.setBackground(new Color(255, 69, 0));
        btnCorrige.setForeground(Color.WHITE);
        btnCorrige.setBounds(btnBranco.getWidth() + padding, y, btnWidth, btnHeight);
        add(btnCorrige);

        JButton btnConfirma = new JButton("CONFIRMA");
        btnConfirma.setFont(new Font("Arial", Font.BOLD, 14));
        btnConfirma.setBackground(new Color(34, 139, 34));
        btnConfirma.setForeground(Color.WHITE);
        btnConfirma.setBounds(0, y + btnHeight + padding, btnWidth * 3 + 2 * padding, btnHeight + 5);
        add(btnConfirma);
    }
}
