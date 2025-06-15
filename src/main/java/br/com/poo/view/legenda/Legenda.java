package br.com.poo.view.legenda;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.util.List;
import java.util.Arrays;

public class Legenda extends JPanel {

    public Legenda() {
        iniciarPainelPrincipal();
    }

    private void iniciarPainelPrincipal() {
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(600, 180));
        setBackground(new Color(230, 230, 230));
        setBorder(BorderFactory.createLineBorder(Color.BLACK, 10, true));
        add(criarPainelFundo(), BorderLayout.CENTER);
    }

    private JPanel criarPainelFundo() {
        JPanel painel = criarPainel(new BorderLayout(0, 10), new Color(30, 144, 255));
        painel.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(Color.BLUE.darker(), 1, true),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        painel.add(criarCabecalhoTexto(), BorderLayout.NORTH);
        painel.add(criarGradePartidos(), BorderLayout.CENTER);

        return painel;
    }

    private JPanel criarCabecalhoTexto() {
        JPanel painel = criarPainel(new FlowLayout(FlowLayout.LEFT, 5, 0), null);
        painel.setOpaque(false);

        painel.add(criarLabel("Para visualização dos candidatos, ", Font.PLAIN, 14, false));
        painel.add(criarLabel("selecione um partido:", Font.BOLD, 14, false));

        return painel;
    }

    private JPanel criarGradePartidos() {
        JPanel painel = criarPainel(new GridLayout(2, 3, 10, 10), null);
        painel.setOpaque(false);

        List<String[]> partidos = Arrays.asList(
            new String[]{"91 PEsp", "PARTIDO DOS ESPORTES"},
            new String[]{"92 PPop", "PARTIDO POPULAR"},
            new String[]{"93 PVer", "PARTIDO VERDE"},
            new String[]{"94 PFest", "PARTIDO DOS RITMOS"},
            new String[]{"95 PLib", "PARTIDO DA LIBERDADE"}
        );

        partidos.forEach(p -> painel.add(criarPainelPartido(p[0], p[1])));
        painel.add(new JLabel()); // célula vazia para completar a grade

        return painel;
    }

    private JPanel criarPainelPartido(String sigla, String nome) {
        JPanel painel = criarPainel();
        painel.setLayout(new BoxLayout(painel, BoxLayout.Y_AXIS));
        painel.setBorder(new LineBorder(Color.WHITE, 1, true));

        JLabel labelSigla = criarLabel(sigla, Font.PLAIN, 16, true);
        JLabel labelNome = criarLabel("<html><center>" + nome + "</center></html>", Font.PLAIN, 11, true);

        painel.add(Box.createVerticalStrut(5));
        painel.add(labelSigla);
        painel.add(Box.createVerticalStrut(4));
        painel.add(labelNome);
        painel.add(Box.createVerticalGlue());

        return painel;
    }

    private JPanel criarPainel(LayoutManager layout, Color corFundo) {
        JPanel painel = new JPanel(layout);
        if (corFundo != null) painel.setBackground(corFundo);
        return painel;
    }

    private JPanel criarPainel() {
        JPanel painel = new JPanel();
        painel.setBackground(new Color(30, 144, 255));
        return painel;
    }

    private JLabel criarLabel(String texto, int estilo, int tamanho, boolean centralizar) {
        JLabel label = new JLabel(texto);
        label.setFont(new Font("Arial", estilo, tamanho));
        label.setForeground(Color.WHITE);
        if (centralizar) label.setAlignmentX(Component.CENTER_ALIGNMENT);
        return label;
    }
}