package br.com.poo.view.visor;

import javax.swing.*;
import org.bson.Document;
import java.util.ArrayList;
import java.util.List;

import br.com.poo.controller.ControllerModel;

public class Visor extends JPanel {
    public int BASE_WIDTH = 700;
    public int BASE_HEIGHT = 400;
    
    public ControllerModel controller;
    public List<JComponent> componentesFixos = new ArrayList<>();
    public List<JComponent> componentesInfo = new ArrayList<>();
    public JTextField[] camposDigito = new JTextField[5];
    VisorBuilder visorFunctios;

    public Visor() {
        visorFunctios = new VisorBuilder(this);
    }

    public void inserirDigito(String acao) {
        for (JTextField campo : camposDigito) {
            if (campo.getText().isEmpty()) {
                campo.setText(acao);
                break;
            }
        }
        if (votoCompleto()) acionarVoto();
    }

    private boolean votoCompleto() {
        for (JTextField campo : camposDigito) {
            if (campo.getText().isEmpty()) return false;
        }
        return true;
    }

    private void acionarVoto() {
        String voto = getVoto();
        Document[] info = controller.buscarInformacoesVoto(voto);
        if (info != null) visorFunctios.adicionarInfosEntidade(info);
    }

    private String getVoto() {
        StringBuilder numero = new StringBuilder();
        for (JTextField campo : camposDigito) numero.append(campo.getText());
        return numero.toString();
    }

    public void acionarBotao(String acao) {
        switch (acao) {
            case "CORRIGE" -> apagarTextoCampos();
            case "CONFIRMA" -> System.out.println("Voto confirmado");
            case "BRANCO" -> System.out.println("Voto em branco");
        }
    }

    private void apagarTextoCampos() {
        for (JTextField campo : camposDigito) campo.setText("");
        removerInfosEntidade();
    }

    private void removerInfosEntidade() {
        for (JComponent comp : componentesInfo) remove(comp);
        componentesInfo.clear();
        revalidate();
        repaint();
    }

    public void setController(ControllerModel controller) {
        this.controller = controller;
    }
}