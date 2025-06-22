package br.com.poo.view;

import javax.swing.*;
import java.awt.*;

import br.com.poo.controller.ControllerUrna;
import br.com.poo.view.legenda.Legenda;
import br.com.poo.view.teclado.Teclado;
import br.com.poo.view.visor.Visor;

public class TelaPrincipal extends JFrame {

    public ControllerUrna controlador;
    public Visor visor;
    public Teclado teclado;

    private Legenda legenda;

    public TelaPrincipal() {
        this.controlador = new ControllerUrna(this);
    }
    
    public void iniciar() {        
        inicializarInterface();
        vincularControladores();
    }

    private void inicializarInterface() {
        configurarJanela();
        configurarLayoutPrincipal();
        setVisible(true);
    }

    private void configurarJanela() {
        setTitle("Simulador de Voto TSE - Layout");
        setSize(1300, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
    }

    private void configurarLayoutPrincipal() {
        JPanel painelPrincipal = criarPainelPrincipal();
        painelPrincipal.add(criarPainelEsquerdo());

        this.teclado = new Teclado();
        painelPrincipal.add(teclado);

        setContentPane(painelPrincipal);
    }

    private JPanel criarPainelPrincipal() {
        JPanel painel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        painel.setBackground(new Color(208, 208, 208));
        return painel;
    }

    private JPanel criarPainelEsquerdo() {
        JPanel painel = new JPanel();
        painel.setLayout(new BoxLayout(painel, BoxLayout.Y_AXIS));
        painel.setOpaque(false);

        this.legenda = new Legenda();
        this.visor = new Visor();

        painel.add(legenda);
        painel.add(Box.createRigidArea(new Dimension(0, 10)));
        painel.add(visor);

        return painel;
    }

    private void vincularControladores() {
        teclado.setController(controlador);
        visor.setController(controlador);
        legenda.setController(controlador);
    }
}