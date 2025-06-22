package br.com.poo.view.legenda;

import javax.swing.*;
import javax.swing.border.*;
import org.bson.Document;
import br.com.poo.controller.ControllerUrna;
import java.awt.*;
import java.awt.event.*;

public class Legenda extends JPanel {

    private ControllerUrna controller;
    private final JPanel painelGrade = new JPanel(new GridLayout(2, 3, 10, 10));
    private final Color azul = new Color(30, 144, 255);
    private final Color verde = new Color(60, 179, 113);

    private JPanel painelPrincipal; // painel que contém tudo
    private JPanel painelCabecalho;

    public Legenda() {
        inicializarComponentes();
    }

    private void inicializarComponentes() {
        configurarJanela();
        criarPainelPrincipal();
    }
    
    private void configurarJanela() {
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(600, 180));
        setBackground(new Color(230, 230, 230));
        setBorder(BorderFactory.createLineBorder(Color.BLACK, 10, true));
    }

    private void criarPainelPrincipal() {
        painelPrincipal = painelComLayout(new BorderLayout(0, 10), azul, new LineBorder(Color.BLUE.darker(), 1, true));
        painelCabecalho = criarCabecalho();
        painelPrincipal.add(painelCabecalho, BorderLayout.NORTH);
        painelGrade.setOpaque(false);
        painelPrincipal.add(painelGrade, BorderLayout.CENTER);
        add(painelPrincipal, BorderLayout.CENTER);
    }

    private JPanel criarCabecalho() {
        JPanel painel = painelComLayout(new FlowLayout(FlowLayout.LEFT, 5, 0), azul, null);

        painel.add(criarLabel("Para visualização dos candidatos, ", Font.PLAIN, 14, false));
        painel.add(criarLabel("selecione um partido:", Font.BOLD, 14, false));

        return painel;
    }

    public void carregarPartidos() {
        painelGrade.removeAll();

        for (Document partido : controller.buscarDadosColecao("partidos")) {
            painelGrade.add(criarPainelPartido(partido));
        }

        painelGrade.add(new JLabel()); // espaço extra

        // Remove todo conteúdo do painelPrincipal e adiciona o cabeçalho original + painelGrade
        painelPrincipal.removeAll();
        painelPrincipal.add(painelCabecalho, BorderLayout.NORTH);
        painelPrincipal.add(painelGrade, BorderLayout.CENTER);

        painelPrincipal.revalidate();
        painelPrincipal.repaint();
    }

    private void exibirCandidatos(Document partido) {
        painelGrade.removeAll();

        for (Document candidato : controller.buscarCandidatosPartido(partido)) {
            painelGrade.add(criarPainelCandidato(candidato));
        }

        painelGrade.add(new JLabel()); // espaço extra

        // Cria painel topo com botão voltar
        JButton botaoVoltar = new JButton("← Voltar");
        botaoVoltar.setFocusable(false);
        botaoVoltar.addActionListener(e -> carregarPartidos());
        botaoVoltar.setBackground(Color.WHITE);
        botaoVoltar.setForeground(azul);
        botaoVoltar.setFont(new Font("Arial", Font.BOLD, 14));

        JPanel painelTopo = painelComLayout(new FlowLayout(FlowLayout.LEFT), azul, null);
        painelTopo.add(botaoVoltar);

        // Remove painelGrade do painelPrincipal e substitui pelo painelTopo + painelGrade
        painelPrincipal.removeAll();
        painelPrincipal.add(painelTopo, BorderLayout.NORTH);
        painelPrincipal.add(painelGrade, BorderLayout.CENTER);

        painelPrincipal.revalidate();
        painelPrincipal.repaint();
    }

    // === PAINÉIS ===

    private JPanel criarPainelPartido(Document partido) {
        JPanel painel = painelVertical(azul);
        painel.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                exibirCandidatos(partido);
            }
        });

        painel.add(Box.createVerticalStrut(5));
        painel.add(criarTopoPartido(partido));
        painel.add(Box.createVerticalStrut(4));
        painel.add(criarLabelCentralizado(partido.getString("nome"), Font.PLAIN, 11));
        painel.add(Box.createVerticalGlue());

        return painel;
    }

    private JPanel criarPainelCandidato(Document candidato) {
        JPanel painel = painelVertical(verde);

        painel.add(Box.createVerticalStrut(8));
        painel.add(criarLabel("Nº " + candidato.getString("numero"), Font.BOLD, 16, false));
        painel.add(Box.createVerticalStrut(5));
        painel.add(criarLabelCentralizado(candidato.getString("nome"), Font.PLAIN, 13));
        painel.add(Box.createVerticalGlue());

        return painel;
    }

    private JPanel criarTopoPartido(Document partido) {
        JPanel topo = painelComLayout(new FlowLayout(FlowLayout.CENTER, 5, 0), null, null);
        topo.setOpaque(false);
        topo.add(criarLabel(partido.getString("sigla"), Font.BOLD, 16, false));
        topo.add(criarLabel("Nº " + partido.getString("numero"), Font.PLAIN, 13, false));
        return topo;
    }

    // === UTILITÁRIOS ===

    private JPanel painelVertical(Color corFundo) {
        JPanel painel = new JPanel();
        painel.setLayout(new BoxLayout(painel, BoxLayout.Y_AXIS));
        painel.setBackground(corFundo);
        painel.setOpaque(true);
        painel.setBorder(new LineBorder(Color.WHITE, 1, true));
        painel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return painel;
    }

    private JPanel painelComLayout(LayoutManager layout, Color corFundo, Border borda) {
        JPanel painel = new JPanel(layout);
        if (corFundo != null) {
            painel.setBackground(corFundo);
            painel.setOpaque(true);
        }
        if (borda != null) painel.setBorder(borda);
        return painel;
    }

    private JLabel criarLabel(String texto, int estilo, int tamanho, boolean centralizar) {
        JLabel label = new JLabel(texto);
        label.setFont(new Font("Arial", estilo, tamanho));
        label.setForeground(Color.WHITE);
        if (centralizar) label.setAlignmentX(Component.CENTER_ALIGNMENT);
        return label;
    }

    private JLabel criarLabelCentralizado(String texto, int estilo, int tamanho) {
        return criarLabel("<html><center>" + texto + "</center></html>", estilo, tamanho, true);
    }

    public void setController(ControllerUrna controller) {
        this.controller = controller;
        carregarPartidos();
    }
}