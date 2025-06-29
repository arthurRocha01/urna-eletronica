package br.com.poo.view.visor;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TelaVotoBranco extends JPanel {

    private final Visor visor;
    private final VisorBuilder builder;

    private JLabel labelTitulo;
    private JLabel labelMensagem;
    private JLabel labelRodape;
    private JLabel labelRelogio;

    private Timer timerPiscar;
    private Timer timerRelogio;
    private boolean visivel = true;

    public TelaVotoBranco(Visor visor, VisorBuilder builder) {
        this.visor = visor;
        this.builder = builder;

        configurarTela();
        inicializarComponentes();
        iniciarTimers();
    }

    private void configurarTela() {
        setLayout(null);
        setBackground(Color.WHITE);
        setPreferredSize(new Dimension(600, 400));
        setOpaque(true);
    }

    private void inicializarComponentes() {
        labelTitulo = criarLabelComEstilo("TREINAMENTO", 16, Color.GRAY, 0, 10, 600, 30, SwingConstants.CENTER);
        labelMensagem = criarLabelComEstilo("VOTO EM BRANCO", 32, Color.BLACK, 0, 130, 600, 60, SwingConstants.CENTER);
        labelRodape = criarLabelComEstilo("Município: 99999 - Minha Cidade   Zona: 9999   Seção: 9999",
                12, Color.DARK_GRAY, 0, 370, 600, 30, SwingConstants.CENTER);
        labelRodape.setOpaque(true);
        labelRodape.setBackground(new Color(230, 235, 245));

        labelRelogio = criarLabelComEstilo("", 12, Color.DARK_GRAY, 10, 10, 200, 20, SwingConstants.LEFT);

        add(labelTitulo);
        add(labelMensagem);
        add(labelRodape);
        add(labelRelogio);
    }

    public void exibir() {
        visor.removeAll();
        visor.setLayout(new BorderLayout());
        visor.add(this, BorderLayout.CENTER);
        visor.revalidate();
        visor.repaint();
    }

    public void fechar() {
        visor.remove(this);
        builder.iniciarTela();
        visor.revalidate();
        visor.repaint();
    }

    private void iniciarTimers() {
        timerPiscar = new Timer(500, e -> {
            visivel = !visivel;
            labelMensagem.setVisible(visivel);
        });
        timerPiscar.start();

        timerRelogio = new Timer(1000, e -> {
            String hora = new SimpleDateFormat("EEE dd/MM/yyyy HH:mm:ss")
                    .format(new Date()).toUpperCase();
            labelRelogio.setText(hora);
        });
        timerRelogio.start();
    }

    private JLabel criarLabelComEstilo(String texto, int tamanhoFonte, Color cor,
        int x, int y, int largura, int altura, int alinhamento) {
        JLabel label = new JLabel(texto, alinhamento);
        label.setFont(new Font("SansSerif", Font.BOLD, tamanhoFonte));
        label.setForeground(cor);
        label.setBounds(x, y, largura, altura);
        return label;
    }
}