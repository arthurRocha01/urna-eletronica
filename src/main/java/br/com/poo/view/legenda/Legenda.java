package br.com.poo.view.legenda;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;

import org.bson.Document;

import br.com.poo.controller.ControllerUrna;

public class Legenda extends JPanel {

    private ControllerUrna controller;
    private final JPanel painelGrade = new JPanel(new GridLayout(2, 3, 10, 10));
    private final Color corPartido = new Color(30, 144, 255);
    private final Color corCandidato = new Color(60, 179, 113);

    private JPanel painelPrincipal;
    private JPanel painelCabecalho;

    public Legenda() {
        configurarLayout();
        construirComponentes();
    }

    private void configurarLayout() {
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(600, 180));
        setBackground(new Color(230, 230, 230));
        setBorder(BorderFactory.createLineBorder(Color.BLACK, 10, true));
    }

    private void construirComponentes() {
        painelPrincipal = criarPainel(new BorderLayout(0, 10), corPartido, new LineBorder(Color.BLUE.darker(), 1, true));
        painelCabecalho = criarCabecalho();
        painelGrade.setOpaque(false);

        painelPrincipal.add(painelCabecalho, BorderLayout.NORTH);
        painelPrincipal.add(painelGrade, BorderLayout.CENTER);
        add(painelPrincipal, BorderLayout.CENTER);
    }

    private JPanel criarCabecalho() {
        JPanel cabecalho = criarPainel(new FlowLayout(FlowLayout.LEFT, 5, 0), corPartido, null);
        cabecalho.add(criarLabel("Para visualização dos candidatos, ", Font.PLAIN, 14, false));
        cabecalho.add(criarLabel("selecione um partido:", Font.BOLD, 14, false));
        return cabecalho;
    }

    public void carregarPartidos() {
        painelGrade.removeAll();

        for (Document partido : controller.buscarColecao("partidos")) {
            painelGrade.add(criarPainelPartido(partido));
        }

        painelGrade.add(new JLabel()); // espaço extra
        atualizarPainel(painelCabecalho, painelGrade);
    }

    private void exibirCandidatos(Document partido) {
        painelGrade.removeAll();

        for (Document candidato : controller.buscarCandidatosPorPartido(partido)) {
            painelGrade.add(criarPainelCandidato(candidato));
        }

        painelGrade.add(new JLabel()); // espaço extra
        JPanel topo = criarPainelTopoComVoltar();
        atualizarPainel(topo, painelGrade);
    }

    private JPanel criarPainelTopoComVoltar() {
        JButton btnVoltar = new JButton("← Voltar");
        btnVoltar.setFocusable(false);
        btnVoltar.setBackground(Color.WHITE);
        btnVoltar.setForeground(corPartido);
        btnVoltar.setFont(new Font("Arial", Font.BOLD, 14));
        btnVoltar.addActionListener(e -> carregarPartidos());

        JPanel topo = criarPainel(new FlowLayout(FlowLayout.LEFT), corPartido, null);
        topo.add(btnVoltar);
        return topo;
    }

    private void atualizarPainel(JComponent topo, JComponent conteudo) {
        painelPrincipal.removeAll();
        painelPrincipal.add(topo, BorderLayout.NORTH);
        painelPrincipal.add(conteudo, BorderLayout.CENTER);
        painelPrincipal.revalidate();
        painelPrincipal.repaint();
    }

    private JPanel criarPainelPartido(Document partido) {
        JPanel painel = criarPainelVertical(corPartido);
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
        JPanel painel = criarPainelVertical(corCandidato);

        painel.add(Box.createVerticalStrut(8));
        painel.add(criarLabel("Nº " + candidato.getString("numero"), Font.BOLD, 16, false));
        painel.add(Box.createVerticalStrut(5));
        painel.add(criarLabelCentralizado(candidato.getString("nome"), Font.PLAIN, 13));
        painel.add(Box.createVerticalGlue());

        return painel;
    }

    private JPanel criarTopoPartido(Document partido) {
        JPanel topo = criarPainel(new FlowLayout(FlowLayout.CENTER, 5, 0), null, null);
        topo.setOpaque(false);
        topo.add(criarLabel(partido.getString("sigla"), Font.BOLD, 16, false));
        topo.add(criarLabel("Nº " + partido.getString("numero"), Font.PLAIN, 13, false));
        return topo;
    }

    private JPanel criarPainelVertical(Color corFundo) {
        JPanel painel = new JPanel();
        painel.setLayout(new BoxLayout(painel, BoxLayout.Y_AXIS));
        painel.setBackground(corFundo);
        painel.setOpaque(true);
        painel.setBorder(new LineBorder(Color.WHITE, 1, true));
        painel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return painel;
    }

    private JPanel criarPainel(LayoutManager layout, Color corFundo, Border borda) {
        JPanel painel = new JPanel(layout);
        if (corFundo != null) {
            painel.setBackground(corFundo);
            painel.setOpaque(true);
        }
        if (borda != null) painel.setBorder(borda);
        return painel;
    }

    private JLabel criarLabel(String texto, int estilo, int tamanho, boolean centralizado) {
        JLabel label = new JLabel(texto);
        label.setFont(new Font("Arial", estilo, tamanho));
        label.setForeground(Color.WHITE);
        if (centralizado) label.setAlignmentX(Component.CENTER_ALIGNMENT);
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