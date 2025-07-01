package br.com.poo.view.legenda;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;

import org.bson.Document;

import br.com.poo.controller.ControllerUrna;

/**
 * Componente gráfico responsável por exibir a legenda interativa com partidos e candidatos.
 *
 * <p>Permite que o usuário visualize os partidos cadastrados e, ao clicar em um deles,
 * visualize os candidatos associados. É possível voltar para a visualização de partidos.
 *
 * <p>Este painel é dinâmico e se adapta de acordo com a interação do usuário,
 * exibindo informações com base nos dados fornecidos pelo {@link ControllerUrna}.
 *
 * @author Arthur Rocha
 * @version 1.0
 * @since 1.0
 */
public class Legenda extends JPanel {

    private ControllerUrna controller;
    private JPanel painelGrade = new JPanel(new GridLayout(2, 3, 10, 10));
    private Color corPartido = new Color(30, 144, 255);
    private Color corCandidato = new Color(60, 179, 113);

    private JPanel painelPrincipal;
    private JPanel painelCabecalho;

    /**
     * Construtor da classe Legenda. Configura o layout inicial e os componentes visuais.
     */
    public Legenda() {
        configurarLayout();
        construirComponentes();
    }

    /**
     * Define o layout e aparência geral do componente.
     */
    private void configurarLayout() {
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(600, 180));
        setBackground(new Color(230, 230, 230));
        setBorder(BorderFactory.createLineBorder(Color.BLACK, 10, true));
    }

    /**
     * Constrói os componentes internos do painel, como cabeçalho e grade.
     */
    private void construirComponentes() {
        painelPrincipal = criarPainel(new BorderLayout(0, 10), corPartido, new LineBorder(Color.BLUE.darker(), 1, true));
        painelCabecalho = criarCabecalho();
        painelGrade.setOpaque(false);

        painelPrincipal.add(painelCabecalho, BorderLayout.NORTH);
        painelPrincipal.add(painelGrade, BorderLayout.CENTER);
        add(painelPrincipal, BorderLayout.CENTER);
    }

    /**
     * Cria o painel de cabeçalho com instruções para o usuário.
     *
     * @return painel do cabeçalho
     */
    private JPanel criarCabecalho() {
        JPanel cabecalho = criarPainel(new FlowLayout(FlowLayout.LEFT, 5, 0), corPartido, null);
        cabecalho.add(criarLabel("Para visualização dos candidatos, ", Font.PLAIN, 14, false));
        cabecalho.add(criarLabel("selecione um partido:", Font.BOLD, 14, false));
        return cabecalho;
    }

    /**
     * Carrega os partidos disponíveis a partir do controller e os exibe em grade.
     */
    private void carregarPartidos() {
        painelGrade.removeAll();

        for (Document partido : controller.buscarColecao("partidos")) {
            painelGrade.add(criarPainelPartido(partido));
        }

        painelGrade.add(new JLabel()); // espaço extra
        atualizarPainel(painelCabecalho, painelGrade);
    }

        /**
     * Cria o painel visual de um partido, incluindo evento de clique.
     *
     * @param partido documento do partido
     * @return painel do partido
     */
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

    /**
     * Exibe os candidatos associados a um partido selecionado.
     *
     * @param partido documento do partido selecionado
     */
    private void exibirCandidatos(Document partido) {
        painelGrade.removeAll();

        for (Document candidato : controller.buscarCandidatosPorPartido(partido)) {
            painelGrade.add(criarPainelCandidato(candidato));
        }

        painelGrade.add(new JLabel()); // espaço extra
        JPanel topo = criarPainelTopoComVoltar();
        atualizarPainel(topo, painelGrade);
    }

    /**
     * Cria o painel visual de um candidato.
     *
     * @param candidato documento do candidato
     * @return painel do candidato
     */
    private JPanel criarPainelCandidato(Document candidato) {
        JPanel painel = criarPainelVertical(corCandidato);

        painel.add(Box.createVerticalStrut(8));
        painel.add(criarLabel("Nº " + candidato.getString("numero"), Font.BOLD, 16, false));
        painel.add(Box.createVerticalStrut(5));
        painel.add(criarLabelCentralizado(candidato.getString("nome"), Font.PLAIN, 13));
        painel.add(Box.createVerticalGlue());

        return painel;
    }

    /**
     * Cria um painel vertical com estilo definido para candidatos ou partidos.
     *
     * @param corFundo cor de fundo do painel
     * @return painel vertical
     */
    private JPanel criarPainelVertical(Color corFundo) {
        JPanel painel = new JPanel();
        painel.setLayout(new BoxLayout(painel, BoxLayout.Y_AXIS));
        painel.setBackground(corFundo);
        painel.setOpaque(true);
        painel.setBorder(new LineBorder(Color.WHITE, 1, true));
        painel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return painel;
    }

    /**
     * Atualiza o painel principal com um novo cabeçalho e novo conteúdo.
     *
     * @param topo componente do topo (ex: cabeçalho ou botão voltar)
     * @param conteudo painel de conteúdo a ser exibido
     */
    private void atualizarPainel(JComponent topo, JComponent conteudo) {
        painelPrincipal.removeAll();
        painelPrincipal.add(topo, BorderLayout.NORTH);
        painelPrincipal.add(conteudo, BorderLayout.CENTER);
        painelPrincipal.revalidate();
        painelPrincipal.repaint();
    }


    /**
     * Cria o painel superior com botão de voltar para visualização de partidos.
     *
     * @return painel superior
     */
    private JPanel criarPainelTopoComVoltar() {
        JButton botaoVoltar = new JButton("← Voltar");
        botaoVoltar.setFocusable(false);
        botaoVoltar.setBackground(Color.WHITE);
        botaoVoltar.setForeground(corPartido);
        botaoVoltar.setFont(new Font("Arial", Font.BOLD, 14));
        botaoVoltar.addActionListener(e -> carregarPartidos());

        JPanel topo = criarPainel(new FlowLayout(FlowLayout.LEFT), corPartido, null);
        topo.add(botaoVoltar);
        return topo;
    }

    /**
     * Cria o topo do painel de partido, com sigla e número.
     *
     * @param partido documento do partido
     * @return painel do topo com sigla e número
     */
    private JPanel criarTopoPartido(Document partido) {
        JPanel topo = criarPainel(new FlowLayout(FlowLayout.CENTER, 5, 0), null, null);
        topo.setOpaque(false);
        topo.add(criarLabel(partido.getString("sigla"), Font.BOLD, 16, false));
        topo.add(criarLabel("Nº " + partido.getString("numero"), Font.PLAIN, 13, false));
        return topo;
    }

    /**
     * Cria um painel genérico com layout, cor de fundo e borda definidos.
     *
     * @param layout layout manager a ser utilizado
     * @param corFundo cor de fundo (opcional)
     * @param borda borda (opcional)
     * @return painel configurado
     */
    private JPanel criarPainel(LayoutManager layout, Color corFundo, Border borda) {
        JPanel painel = new JPanel(layout);
        if (corFundo != null) {
            painel.setBackground(corFundo);
            painel.setOpaque(true);
        }
        if (borda != null) painel.setBorder(borda);
        return painel;
    }

    /**
     * Cria um {@code JLabel} com texto, fonte e alinhamento definidos.
     *
     * @param texto conteúdo do rótulo
     * @param estilo estilo da fonte (ex: Font.BOLD)
     * @param tamanho tamanho da fonte
     * @param centralizado se deve ser centralizado no painel
     * @return rótulo configurado
     */
    private JLabel criarLabel(String texto, int estilo, int tamanho, boolean centralizado) {
        JLabel label = new JLabel(texto);
        label.setFont(new Font("Arial", estilo, tamanho));
        label.setForeground(Color.WHITE);
        if (centralizado) label.setAlignmentX(Component.CENTER_ALIGNMENT);
        return label;
    }

    /**
     * Cria um {@code JLabel} com texto centralizado em HTML.
     *
     * @param texto conteúdo do rótulo
     * @param estilo estilo da fonte
     * @param tamanho tamanho da fonte
     * @return rótulo com texto centralizado
     */
    private JLabel criarLabelCentralizado(String texto, int estilo, int tamanho) {
        return criarLabel("<html><center>" + texto + "</center></html>", estilo, tamanho, true);
    }

    /**
     * Define o controller da aplicação e carrega os partidos na interface.
     *
     * @param controller instância do {@link ControllerUrna}
     */
    public void setController(ControllerUrna controller) {
        this.controller = controller;
        carregarPartidos();
    }
}