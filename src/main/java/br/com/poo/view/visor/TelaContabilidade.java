package br.com.poo.view.visor;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;

public class TelaContabilidade extends JPanel {

    private JLabel lblTitulo;
    private JLabel lblPrimeiroTitulo;
    private JLabel lblPrimeiroValor;

    private JLabel lblSegundoTitulo;
    private JLabel lblSegundoValor;

    private JLabel lblTerceiroTitulo;
    private JLabel lblTerceiroValor;

    private JLabel lblNulosTitulo;
    private JLabel lblNulosValor;

    private JPanel painelEstatisticas;

    public Visor visor;
    public VisorBuilder builder;

    public TelaContabilidade(Visor visor, VisorBuilder builder) {
        this.visor = visor;
        this.builder = builder;
    }
    
    public void exibir() {
        visor.removeAll();
        visor.setLayout(new BorderLayout());
        visor.add(this, BorderLayout.CENTER);
        visor.revalidate();
        visor.repaint();

        configurarTela();
        montarInterface();
    }

    private void configurarTela() {
        setLayout(null);
        setBackground(Color.WHITE);
        setOpaque(true);
        setPreferredSize(new Dimension(600, 400));
    }

    private void montarInterface() {
        lblTitulo = criarLabel("CONTABILIDADE DOS VOTOS", 22, new Rectangle(200, 10, 400, 30));

        lblPrimeiroTitulo = criarLabel("1º Mais Votado:", 18, new Rectangle(50, 70, 200, 25));
        lblPrimeiroValor = criarLabel("Nome - Partido - Votos", 16, new Rectangle(250, 70, 400, 25));

        lblSegundoTitulo = criarLabel("2º Mais Votado:", 18, new Rectangle(50, 110, 200, 25));
        lblSegundoValor = criarLabel("Nome - Partido - Votos", 16, new Rectangle(250, 110, 400, 25));

        lblTerceiroTitulo = criarLabel("3º Mais Votado:", 18, new Rectangle(50, 150, 200, 25));
        lblTerceiroValor = criarLabel("Nome - Partido - Votos", 16, new Rectangle(250, 150, 400, 25));

        lblNulosTitulo = criarLabel("Votos Nulos:", 18, new Rectangle(50, 200, 200, 25));
        lblNulosValor = criarLabel("Total: X votos", 16, new Rectangle(250, 200, 400, 25));

        // criarPainelEstatisticas();
    }

    private JLabel criarLabel(String texto, int tamanhoFonte, Rectangle bounds) {
        JLabel label = new JLabel(texto);
        label.setFont(new Font("Arial", Font.PLAIN, tamanhoFonte));
        label.setBounds(bounds);
        add(label);
        return label;
    }

    private void criarPainelEstatisticas() {
        painelEstatisticas = new JPanel();
        painelEstatisticas.setLayout(new BorderLayout());
        painelEstatisticas.setBackground(new Color(245, 245, 245));
        painelEstatisticas.setBorder(new LineBorder(Color.GRAY, 1));
        painelEstatisticas.setBounds(50, 250, 600, 120);

        JLabel placeholder = new JLabel("Gráfico ou tabela pode ser exibido aqui");
        placeholder.setHorizontalAlignment(SwingConstants.CENTER);
        placeholder.setFont(new Font("Arial", Font.ITALIC, 14));
        painelEstatisticas.add(placeholder, BorderLayout.CENTER);

        add(painelEstatisticas);
    }
}
