package br.com.poo.view.visor;

import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.*;

/**
 * Tela que exibe a confirmação visual do voto em branco durante a simulação da urna.
 * Apresenta uma mensagem piscante e a data/hora atual.
 * 
 * @author Arthur Rocha
 * @version 1.0
 * @since 1.0
 */
public class TelaVotoBranco extends JPanel {

    private Visor visor;
    private VisorBuilder builder;

    private JLabel labelTitulo;
    private JLabel labelMensagem;
    private JLabel labelRodape;
    private JLabel labelRelogio;

    private Timer timerPiscar;
    private Timer timerRelogio;

    private boolean visivel = true;

    /**
     * Construtor que recebe o painel do visor e o builder para troca de telas.
     *
     * @param visor painel principal da urna
     * @param builder responsável por construir e navegar entre telas
     */
    public TelaVotoBranco(Visor visor, VisorBuilder builder) {
        this.visor = visor;
        this.builder = builder;

        configurarTela();
        construirComponentes();
        iniciarTimers();
    }

    /**
     * Configura o layout e aparência da tela de voto em branco.
     */
    private void configurarTela() {
        setLayout(null);
        setBackground(Color.WHITE);
        setPreferredSize(new Dimension(600, 400));
        setOpaque(true);
    }

    /**
     * Constrói e posiciona os componentes visuais na tela.
     */
    private void construirComponentes() {
        labelTitulo = criarLabel("TREINAMENTO", 16, Color.GRAY, 0, 10, 600, 30, SwingConstants.CENTER);
        labelMensagem = criarLabel("VOTO EM BRANCO", 32, Color.BLACK, 0, 130, 600, 60, SwingConstants.CENTER);
        labelRodape = criarLabel("Município: 99999 - Minha Cidade   Zona: 9999   Seção: 9999",
                12, Color.DARK_GRAY, 0, 370, 600, 30, SwingConstants.CENTER);
        labelRodape.setOpaque(true);
        labelRodape.setBackground(new Color(230, 235, 245));

        labelRelogio = criarLabel("", 12, Color.DARK_GRAY, 10, 10, 200, 20, SwingConstants.LEFT);

        add(labelTitulo);
        add(labelMensagem);
        add(labelRodape);
        add(labelRelogio);
    }

    /**
     * Exibe esta tela no visor da urna.
     */
    public void mostrar() {
        visor.removeAll();
        visor.setLayout(new BorderLayout());
        visor.add(this, BorderLayout.CENTER);
        visor.revalidate();
        visor.repaint();
    }

    /**
     * Fecha esta tela e retorna para a tela inicial via builder.
     */
    public void fechar() {
        visor.remove(this);
        builder.iniciarTela();
        visor.revalidate();
        visor.repaint();
    }

    /**
     * Inicia os temporizadores para piscar a mensagem e atualizar o relógio.
     */
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

    /**
     * Cria e configura um JLabel para ser usado na tela.
     *
     * @param texto texto exibido
     * @param tamanhoFonte tamanho da fonte
     * @param cor cor do texto
     * @param x posição horizontal
     * @param y posição vertical
     * @param largura largura do componente
     * @param altura altura do componente
     * @param alinhamento alinhamento horizontal do texto
     * @return JLabel configurado
     */
    private JLabel criarLabel(String texto, int tamanhoFonte, Color cor,
        int x, int y, int largura, int altura, int alinhamento) {
        JLabel label = new JLabel(texto, alinhamento);
        label.setFont(new Font("SansSerif", Font.BOLD, tamanhoFonte));
        label.setForeground(cor);
        label.setBounds(x, y, largura, altura);
        return label;
    }
}