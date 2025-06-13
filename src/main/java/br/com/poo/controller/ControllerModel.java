package br.com.poo.controller;

import org.bson.Document;

import com.mongodb.client.FindIterable;

import br.com.poo.controller.database.ManipuladorDatabase;

public class ControllerModel {
    private ManipuladorDatabase manipuladorDatabase = new ManipuladorDatabase();

    public ControllerModel() {
        manipuladorDatabase.iniciarCliente();
        System.out.println(getColecao("partidos").first());
    }

    public FindIterable<Document> getColecao(String colecao) {
        return manipuladorDatabase.getItens(colecao);
    }
}
