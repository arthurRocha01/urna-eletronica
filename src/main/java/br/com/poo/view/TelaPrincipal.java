package br.com.poo.view;

import javax.swing.*;
import java.awt.*;

import br.com.poo.controller.ControllerModel;
import br.com.poo.view.legenda.Legenda;
import br.com.poo.view.teclado.Teclado;
import br.com.poo.view.visor.Visor;

public class TelaPrincipal extends JFrame {

    private Legenda legenda;
    public Visor visor;
    public Teclado teclado;
    public ControllerModel controller;

    public TelaPrincipal() {
        inicializarComponentes();
        iniciarController();
    }

    private void inicializarComponentes() {
        configurarJanela();
        configurarPainelPrincipal();
        setVisible(true);
    }

    private void configurarJanela() {
        setTitle("Simulador de Voto TSE - Layout");
        setSize(1300, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
    }

    private void configurarPainelPrincipal() {
        JPanel painelPrincipal = criarPainelPrincipal();
        JPanel painelEsquerda = criarPainelEsquerda();

        painelPrincipal.add(painelEsquerda);

        teclado = new Teclado();
        painelPrincipal.add(teclado);

        setContentPane(painelPrincipal);
    }

    private JPanel criarPainelPrincipal() {
        JPanel painel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        painel.setBackground(new Color(208, 208, 208));
        return painel;
    }

    private JPanel criarPainelEsquerda() {
        JPanel painel = new JPanel();
        painel.setLayout(new BoxLayout(painel, BoxLayout.Y_AXIS));
        painel.setOpaque(false);

        legenda = new Legenda();
        visor = new Visor();

        painel.add(legenda);
        painel.add(Box.createRigidArea(new Dimension(0, 10)));
        painel.add(visor);

        return painel;
    }

    private void iniciarController() {
        controller = new ControllerModel(this);
        teclado.setController(controller);
        visor.setController(controller);
        legenda.setController(controller);
    }
}