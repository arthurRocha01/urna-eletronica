package br.com.poo.view.visor;

import javax.swing.*;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

import br.com.poo.controller.ControllerUrna;

public class Visor extends JPanel {
    public int BASE_WIDTH = 700;
    public int BASE_HEIGHT = 400;

    public VisorBuilder visorFunctios;
    public ControllerUrna controller;

    public StringBuilder numero;
    public Document[] info;
    public JTextField[] camposDigito = new JTextField[5];
    
    public List<JComponent> componentesFixos = new ArrayList<>();
    public List<JComponent> componentesInfo = new ArrayList<>();

    public boolean tecladoBloqueado = false;
    public boolean botoesBloqeuado = false;

    public Visor() {
        visorFunctios = new VisorBuilder(this);
        visorFunctios.iniciarTela();
    }

    public void inserirDigito(String acao) {
        if (tecladoBloqueado) return;
        for (JTextField campo : camposDigito) {
            if (campo.getText().isEmpty()) {
                campo.setText(acao);
                break;
            }
        }
        if (votoCompleto()) acionarVoto();
    }

    public boolean votoCompleto() {
        for (JTextField campo : camposDigito) {
            if (campo.getText().isEmpty()) return false;
        }
        tecladoBloqueado = true;
        return true;
    }
    
    private void acionarVoto() {
        String voto = getVoto();
        info = controller.buscarInformacoesVoto(voto);
        if (info != null) visorFunctios.adicionarInfosEntidade(info);
    }

    public String getVoto() {
        numero = new StringBuilder();
        for (JTextField campo : camposDigito) numero.append(campo.getText());
        return numero.toString();
    }

    public void setController(ControllerUrna controller) {
        this.controller = controller;
    }
}