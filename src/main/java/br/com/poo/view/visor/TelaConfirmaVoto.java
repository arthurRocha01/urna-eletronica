package br.com.poo.view.visor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TelaConfirmaVoto extends JPanel {
    private Visor visor;
    private VisorBuilder builder;

    private JProgressBar barraProgresso;
    private JLabel labelMensagem;
    private JLabel labelRodape;
    private JLabel labelTitulo;
    private JLabel labelFim;
    private JLabel labelVotou;
    private JLabel labelRelogio;

    private Timer timerGravacao;
    private Timer timerRelogio;

    public TelaConfirmaVoto(Visor visor, VisorBuilder builder) {
        this.visor = visor;
        this.builder = builder;

        setLayout(null);
        setBackground(Color.WHITE);
        setOpaque(true);
        setPreferredSize(new Dimension(600, 400));

        iniciarComponentes();
    }

    private void iniciarComponentes() {
        labelTitulo = new JLabel("TREINAMENTO", SwingConstants.CENTER);
        labelTitulo.setFont(new Font("SansSerif", Font.BOLD, 16));
        labelTitulo.setForeground(Color.GRAY);
        labelTitulo.setBounds(0, 10, 600, 30);
        add(labelTitulo);

        barraProgresso = new JProgressBar(0, 100);
        barraProgresso.setBounds(100, 140, 400, 25);
        barraProgresso.setForeground(Color.GREEN);
        add(barraProgresso);

        labelMensagem = new JLabel("Gravando", SwingConstants.CENTER);
        labelMensagem.setFont(new Font("SansSerif", Font.BOLD, 20));
        labelMensagem.setBounds(0, 180, 600, 30);
        add(labelMensagem);

        labelRodape = new JLabel("Município: 99999 - Minha Cidade   Zona: 9999   Seção: 9999", SwingConstants.CENTER);
        labelRodape.setOpaque(true);
        labelRodape.setBackground(new Color(230, 235, 245));
        labelRodape.setFont(new Font("SansSerif", Font.PLAIN, 12));
        labelRodape.setBounds(0, 370, 600, 30);
        add(labelRodape);

        labelFim = new JLabel("FIM", SwingConstants.CENTER);
        labelFim.setFont(new Font("SansSerif", Font.BOLD, 72));
        labelFim.setBounds(0, 100, 600, 80);
        labelFim.setVisible(false);
        add(labelFim);

        labelVotou = new JLabel("VOTOU", SwingConstants.RIGHT);
        labelVotou.setFont(new Font("SansSerif", Font.BOLD, 24));
        labelVotou.setForeground(Color.LIGHT_GRAY);
        labelVotou.setBounds(450, 200, 140, 30);
        labelVotou.setVisible(false);
        add(labelVotou);

        labelRelogio = new JLabel("", SwingConstants.LEFT);
        labelRelogio.setFont(new Font("SansSerif", Font.PLAIN, 12));
        labelRelogio.setForeground(Color.DARK_GRAY);
        labelRelogio.setBounds(10, 10, 200, 20);
        add(labelRelogio);

        timerRelogio = new Timer(1000, e -> {
            labelRelogio.setText(new SimpleDateFormat("EEE dd/MM/yyyy HH:mm:ss").format(new Date()).toUpperCase());
        });
        timerRelogio.start();
    }

    public void iniciarAnimacao() {
        visor.removeAll();
        visor.setLayout(new BorderLayout());
        visor.add(this, BorderLayout.CENTER);

        visor.revalidate();
        visor.repaint();

        barraProgresso.setValue(0);
        barraProgresso.setVisible(true);
        labelMensagem.setVisible(true);
        labelFim.setVisible(false);
        labelVotou.setVisible(false);

        Timer timer = new Timer(30, new ActionListener() {
            int progresso = 0;
            @Override
            public void actionPerformed(ActionEvent e) {
                progresso++;
                barraProgresso.setValue(progresso);
                if (progresso >= 100) {
                    ((Timer) e.getSource()).stop();
                    mostrarTelaFim();
                }
            }
        });
        timer.start();
    }

    private void mostrarTelaFim() {
        barraProgresso.setVisible(false);
        labelMensagem.setVisible(false);
        labelFim.setVisible(true);
        labelVotou.setVisible(true);

        // Remove a animação e volta para a tela inicial após 2 segundos
        Timer fim = new Timer(2000, e -> {
            visor.remove(this);
            builder.iniciarTela();
            visor.revalidate();
            visor.repaint();
        });
        fim.setRepeats(false);
        fim.start();
    }
}