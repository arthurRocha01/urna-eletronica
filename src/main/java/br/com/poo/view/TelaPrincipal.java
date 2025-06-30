package br.com.poo.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;

import br.com.poo.controller.ControllerUrna;
import br.com.poo.view.legenda.Legenda;
import br.com.poo.view.teclado.Teclado;
import br.com.poo.view.visor.Visor;

/**
 * Classe principal da interface gráfica que representa a janela principal do simulador de voto.
 * 
 * Configura e organiza os componentes visuais como o visor, teclado e legenda,
 * além de gerenciar a ligação com o controlador da aplicação.
 * 
 * @author Arthur Rocha
 * @version 1.0
 * @since 1.0
 */
public class TelaPrincipal extends JFrame {

    /** Controlador da aplicação */
    public ControllerUrna controlador;
    /** Componente visual do visor de votos */
    public Visor visor;
    /** Componente visual do teclado */
    public Teclado teclado;
    /** Componente visual da legenda */
    private Legenda legenda;

    /**
     * Construtor que inicializa o controlador da aplicação.
     */
    public TelaPrincipal() {
        this.controlador = new ControllerUrna(this);
    }
    
    /**
     * Inicializa a interface gráfica, configurando e exibindo a janela principal.
     */
    public void iniciar() {        
        inicializarInterface();
        vincularControladores();
    }

    /**
     * Inicializa os componentes visuais e configura o layout da janela.
     */
    private void inicializarInterface() {
        configurarJanela();
        configurarLayoutPrincipal();
        setVisible(true);
    }

    /**
     * Configura as propriedades básicas da janela principal.
     */
    private void configurarJanela() {
        setTitle("Simulador de Voto TSE - Layout");
        setSize(1300, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
    }

    /**
     * Configura o layout principal, adicionando o painel esquerdo e o teclado.
     */
    private void configurarLayoutPrincipal() {
        JPanel painelPrincipal = criarPainelPrincipal();
        painelPrincipal.add(criarPainelEsquerdo());

        this.teclado = new Teclado();
        painelPrincipal.add(teclado);

        setContentPane(painelPrincipal);
    }

    /**
     * Cria o painel principal com layout FlowLayout e cor de fundo definida.
     * 
     * @return JPanel painel principal configurado
     */
    private JPanel criarPainelPrincipal() {
        JPanel painel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        painel.setBackground(new Color(208, 208, 208));
        return painel;
    }

    /**
     * Cria o painel da esquerda contendo a legenda e o visor, organizados em BoxLayout vertical.
     * 
     * @return JPanel painel esquerdo configurado
     */
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

    /**
     * Vincula os controladores (controller) aos componentes visuais para permitir comunicação.
     */
    private void vincularControladores() {
        teclado.setController(controlador);
        visor.setController(controlador);
        legenda.setController(controlador);
    }
}