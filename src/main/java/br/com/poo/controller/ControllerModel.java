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
        manipuladorDatabase.iniciarCliente();
    }

    public void onAcao(String acao) {
        if (acao.matches("\\d+")) {
            display.visor.inserirDigito(acao);
        } else {
            display.visor.acionarBotao(acao);
        }
    }

    public Document[] buscarInformacoesVoto(String voto) {
        return manipuladorDatabase.getCandidato(voto);
    }
}