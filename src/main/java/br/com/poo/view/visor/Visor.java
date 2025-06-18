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
    public Document contabilidadeVotos;

    public boolean visorBloqueado = false;

    public Visor() {
        visorFunctios = new VisorBuilder(this);
        visorFunctios.iniciarTela();
    }

    public void inserirDigito(String acao) {
        if (visorBloqueado) return;
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
        visorBloqueado = true;
        String voto = getVoto();
        info = controller.buscarInformacoesVoto(voto);
        if (info != null) visorFunctios.adicionarInfosEntidade(info);
    }

    private String getVoto() {
        numero = new StringBuilder();
        for (JTextField campo : camposDigito) numero.append(campo.getText());
        return numero.toString();
    }
    
    public void confirmaVoto() {
        if (!votoCompleto()) return;
        String voto = getVoto();
        visorFunctios.exibirConfirmaVoto();

        if (voto.equals("99999")) {
            int votosAtuais = contabilidadeVotos.getInteger("branco");
            contabilidadeVotos.put("branco", votosAtuais + 1);
            return;
        }
        Document candidatoVotado = controller.buscarCandidato(voto);
        String nome = candidatoVotado.getString("nome");
        int votosAtuais = contabilidadeVotos.getInteger(nome);
        contabilidadeVotos.put(nome, votosAtuais + 1);
        
    }

    public void setController(ControllerUrna controller) {
        this.controller = controller;
        iniciarContabilidade();
    }

    private void iniciarContabilidade() {
        contabilidadeVotos = new Document();
        Document[] listaCandidatos = controller.buscarTodosCandidatos();
        for (Document candidato : listaCandidatos) {
            contabilidadeVotos.append(candidato.getString("nome"), 0);
        }
        contabilidadeVotos.append("branco", 0);
    }
}