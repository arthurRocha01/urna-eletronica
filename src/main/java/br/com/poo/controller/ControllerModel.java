package br.com.poo.controller;

import org.bson.Document;

import com.mongodb.client.FindIterable;

import br.com.poo.controller.database.ManipuladorDatabase;
import br.com.poo.view.TelaPrincipal;

public class ControllerModel {
    private ManipuladorDatabase manipuladorDatabase = new ManipuladorDatabase();
    private TelaPrincipal display;

    public ControllerModel(TelaPrincipal display) {
        this.display = display;
        // manipuladorDatabase.iniciarCliente();
        // System.out.println(getColecao("partidos").first());
    }

    public FindIterable<Document> getColecao(String colecao) {
        return manipuladorDatabase.getItens(colecao);
    }

    public void onAcao(String acao) {
        if (acao.matches("\\d+")) display.visor.inserirDigito(acao);
        else display.visor.acionarBotao(acao);
    }
}
