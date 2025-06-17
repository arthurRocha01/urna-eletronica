package br.com.poo.view.visor;

import javax.swing.*;
import java.awt.*;
import javax.swing.Timer;

public class TelaAnimadaVoto extends JPanel {
    private Visor visor;
    private VisorBuilder builder;
    private JLabel mensagem;
    private float opacidade = 0f;

    private Timer animarEntrada;
    private Timer pausa;
    private Timer animarSaida;

    public TelaAnimadaVoto(Visor visor, VisorBuilder builder) {
        this.visor = visor;
        this.builder = builder;
        setOpaque(false);
        setLayout(new GridBagLayout());

        mensagem = new JLabel("VOTO CONFIRMADO");
        mensagem.setFont(new Font("Arial", Font.BOLD, 40));
        mensagem.setForeground(new Color(0, 255, 0, 0));
        add(mensagem);

        configurarAnimacao();
    }

    private void configurarAnimacao() {
        animarEntrada = new Timer(50, e -> atualizarOpacidade(0.05f, true));
        pausa = new Timer(1500, e -> animarSaida.start());
        pausa.setRepeats(false);
        animarSaida = new Timer(50, e -> atualizarOpacidade(-0.05f, false));
    }

    private void atualizarOpacidade(float delta, boolean entrada) {
        opacidade += delta;
        opacidade = Math.max(0f, Math.min(1f, opacidade));

        mensagem.setForeground(new Color(0, 255, 0, (int)(opacidade * 255)));
        mensagem.repaint();

        if (entrada && opacidade >= 1f) {
            animarEntrada.stop();
            pausa.start();
        } else if (!entrada && opacidade <= 0f) {
            animarSaida.stop();
            visor.remove(this);
            visor.revalidate();
            visor.repaint();

            // Reconstrói a tela inicial após a animação
            builder.iniciarTela();
        }
    }

    public void iniciarAnimacao() {
        opacidade = 0f;
        mensagem.setForeground(new Color(0, 255, 0, 0));
        animarEntrada.start();
    }
}