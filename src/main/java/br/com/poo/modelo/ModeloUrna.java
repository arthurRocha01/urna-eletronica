package br.com.poo.modelo;

import org.bson.Document;

import br.com.poo.controller.ControllerUrna;

public class ModeloUrna {
    private ControllerUrna controller;
    public Document contabilidadeVotos;

    public ModeloUrna(ControllerUrna controller) {
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

    public void addVoto(String chave) {
        int votosAtuais = contabilidadeVotos.getInteger(chave);
        contabilidadeVotos.put(chave, votosAtuais + 1);
        contabilidadeVotos.forEach((key, value) -> System.err.printf("%s:%s\n", key, value));
    }

    public void setController(ControllerUrna controller) {
        this.controller = controller;
    }
}