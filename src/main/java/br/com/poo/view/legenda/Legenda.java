package br.com.poo.view.legenda;

import javax.swing.*;
import javax.swing.border.*;

import org.bson.Document;
import br.com.poo.controller.ControllerModel;

import java.awt.*;

public class Legenda extends JPanel {

    private ControllerModel controller;
    private JPanel painelGrade;

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

        painelGrade = criarPainel(new GridLayout(2, 3, 10, 10), null);
        painelGrade.setOpaque(false);
        painel.add(painelGrade, BorderLayout.CENTER);

        return painel;
    }

    private JPanel criarCabecalhoTexto() {
        JPanel painel = criarPainel(new FlowLayout(FlowLayout.LEFT, 5, 0), null);
        painel.setOpaque(false);

        painel.add(criarLabel("Para visualização dos candidatos, ", Font.PLAIN, 14, false));
        painel.add(criarLabel("selecione um partido:", Font.BOLD, 14, false));

        return painel;
    }

    public void carregarPartidos() {
        painelGrade.removeAll();

        Document[] partidos = controller.buscarDadosColecao("partidos");

        for (Document partido : partidos) {
            painelGrade.add(criarPainelPartido(partido.getString("sigla"), partido.getString("nome"), partido.getString("numero")));
        }

        painelGrade.add(new JLabel());
        revalidate();
        repaint();
    }

    private JPanel criarPainelPartido(String sigla, String nome, String numero) {
    JPanel painel = criarPainel();
    painel.setLayout(new BoxLayout(painel, BoxLayout.Y_AXIS));
    painel.setBorder(new LineBorder(Color.WHITE, 1, true));

    // Painel horizontal: sigla + número
    JPanel painelTopo = new JPanel();
    painelTopo.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 0));
    painelTopo.setOpaque(false);

    JLabel labelSigla = criarLabel(sigla, Font.BOLD, 16, false);
    JLabel labelNumero = criarLabel("Nº " + numero, Font.PLAIN, 13, false);
    
    painelTopo.add(labelSigla);
    painelTopo.add(labelNumero);

    JLabel labelNome = criarLabel("<html><center>" + nome + "</center></html>", Font.PLAIN, 11, true);

    painel.add(Box.createVerticalStrut(5));
    painel.add(painelTopo);
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

    public void setController(ControllerModel controller) {
        this.controller = controller;
        carregarPartidos();
    }
}