package br.com.poo.view.visor;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.*;

/**
 * Tela de confirmação do voto que simula o processo de gravação e finalização.
 * 
 * <p>Apresenta uma barra de progresso e mensagens visuais para o usuário,
 * incluindo a palavra "FIM", confirmação de voto e hora atual.</p>
 * 
 * <p>Após a animação, a tela é resetada para o início.</p>
 * 
 * @author Arthur Rocha
 * @version 1.0
 * @since 1.0
 */
public class TelaConfirmaCandidato extends JPanel {

    private Visor visor;
    private VisorBuilder builder;

    private JProgressBar barraProgresso;
    private JLabel mensagem;
    private JLabel rodape;
    private JLabel titulo;
    private JLabel fim;
    private JLabel votou;
    private JLabel relogio;

    private Timer timerRelogio;

    /**
     * Construtor da tela de confirmação.
     * 
     * @param visor painel principal onde esta tela será exibida
     * @param builder objeto responsável por reconstruir as telas
     */
    public TelaConfirmaCandidato(Visor visor, VisorBuilder builder) {
        this.visor = visor;
        this.builder = builder;

        configurarTela();
        construirComponentes();
    }

    /**
     * Define configurações visuais iniciais da tela.
     */
    private void configurarTela() {
        setLayout(null);
        setBackground(Color.WHITE);
        setOpaque(true);
        setPreferredSize(new Dimension(600, 400));
    }

    /**
     * Constrói os componentes visuais como labels, barra de progresso e relógio.
     */
    private void construirComponentes() {
        titulo = criarLabel("TREINAMENTO", 16, Color.GRAY, 0, 10, 600, 30, SwingConstants.CENTER);
        add(titulo);

        barraProgresso = new JProgressBar(0, 100);
        barraProgresso.setBounds(100, 140, 400, 25);
        barraProgresso.setForeground(Color.GREEN);
        add(barraProgresso);

        mensagem = criarLabel("Gravando", 20, Color.BLACK, 0, 180, 600, 30, SwingConstants.CENTER);
        add(mensagem);

        rodape = criarLabel("Município: 99999 - Minha Cidade   Zona: 9999   Seção: 9999", 12, Color.DARK_GRAY, 0, 370, 600, 30, SwingConstants.CENTER);
        rodape.setBackground(new Color(230, 235, 245));
        rodape.setOpaque(true);
        add(rodape);

        fim = criarLabel("FIM", 72, Color.BLACK, 0, 100, 600, 80, SwingConstants.CENTER);
        fim.setVisible(false);
        add(fim);

        votou = criarLabel("VOTOU", 24, Color.LIGHT_GRAY, 450, 200, 140, 30, SwingConstants.RIGHT);
        votou.setVisible(false);
        add(votou);

        relogio = criarLabel("", 12, Color.DARK_GRAY, 10, 10, 200, 20, SwingConstants.LEFT);
        add(relogio);

        // Timer para atualizar o relógio em tempo real
        timerRelogio = new Timer(1000, e -> {
            String horaAtual = new SimpleDateFormat("EEE dd/MM/yyyy HH:mm:ss").format(new Date()).toUpperCase();
            relogio.setText(horaAtual);
        });
        timerRelogio.start();
    }

    /**
     * Mostra esta tela no visor e inicia a animação de confirmação do voto.
     */
    public void mostrar() {
        visor.removeAll();
        visor.setLayout(new BorderLayout());
        visor.add(this, BorderLayout.CENTER);
        visor.revalidate();
        visor.repaint();

        barraProgresso.setValue(0);
        barraProgresso.setVisible(true);
        mensagem.setVisible(true);
        fim.setVisible(false);
        votou.setVisible(false);

        Timer animador = new Timer(15, new AbstractAction() {
            int progresso = 0;
            @Override
            public void actionPerformed(ActionEvent e) {
                progresso++;
                barraProgresso.setValue(progresso);
                if (progresso >= 100) {
                    ((Timer) e.getSource()).stop();
                    exibirFim();
                }
            }
        });
        animador.start();
    }

    /**
     * Exibe a mensagem final "FIM" e reinicia a tela após breve pausa.
     */
    private void exibirFim() {
        barraProgresso.setVisible(false);
        mensagem.setVisible(false);
        fim.setVisible(true);
        votou.setVisible(true);

        Timer resetarTela = new Timer(1000, e -> {
            visor.remove(this);
            builder.iniciarTela();
            visor.revalidate();
            visor.repaint();
        });
        resetarTela.setRepeats(false);
        resetarTela.start();
    }

    /**
     * Cria e retorna um JLabel com as configurações fornecidas.
     *
     * @param texto conteúdo textual do label
     * @param tamanhoFonte tamanho da fonte
     * @param cor cor do texto
     * @param x posição horizontal
     * @param y posição vertical
     * @param largura largura do label
     * @param altura altura do label
     * @param alinhamento alinhamento horizontal do texto
     * @return JLabel criado com as configurações especificadas
     */
    private JLabel criarLabel(String texto, int tamanhoFonte, Color cor, int x, int y, int largura, int altura, int alinhamento) {
        JLabel label = new JLabel(texto, alinhamento);
        label.setFont(new Font("SansSerif", Font.BOLD, tamanhoFonte));
        label.setForeground(cor);
        label.setBounds(x, y, largura, altura);
        return label;
    }
}