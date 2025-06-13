package br.com.poo.controller.database;

import org.bson.Document;

import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;

public class ManipuladorDatabase {
    private String URI_DATABASE = "mongodb+srv://user1234:senha1234@urna-eletronica.qiefb7e.mongodb.net/?retryWrites=true&w=majority&appName=urna-eletronica";
    MongoClient mongoClient;
    private MongoDatabase database;

    private MongoCollection<Document> partidos;
    private MongoCollection<Document> cargos;
    private MongoCollection<Document> candidatos;

    private void inicarColecoes() {
        partidos = database.getCollection("partidos");
        cargos = database.getCollection("cargos");
        candidatos = database.getCollection("candidatos");
    }

    public void iniciarCliente() {
        try {
            mongoClient = MongoClients.create(URI_DATABASE);
            database = mongoClient.getDatabase("urna_db");
            inicarColecoes();
            System.out.println("Conectado ao banco: " + database.getName());
        } catch(Exception e) {
            e.printStackTrace();
            System.out.println("Erro na conexão com MongoDB");
        }
    }

    public FindIterable<Document> getItens(String colecao) {
        if(colecao.equals("cargos")) return cargos.find();
        if (colecao.equals("partidos")) return partidos.find();
        if (colecao.equals("candidatos")) return candidatos.find();
        return null;
    }

    public void fecharConexao() {
    if (mongoClient != null) {
        mongoClient.close();
        System.out.println("Conexão com MongoDB encerrada.");
    }
}
}
