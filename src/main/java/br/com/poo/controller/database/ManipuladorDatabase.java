package br.com.poo.controller.database;

import com.mongodb.client.MongoClients;
import com.mongodb.MongoNamespace;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;

public class ManipuladorDatabase {
    private String URI_DATABASE = "mongodb+srv://user1234:senha1234@urna-eletronica.qiefb7e.mongodb.net/?retryWrites=true&w=majority&appName=urna-eletronica";
    private MongoDatabase database;
    public void iniciarCliente() {
        try (MongoClient mongoClient = MongoClients.create(URI_DATABASE)) {
            database = mongoClient.getDatabase("urna-eletronica");
            System.out.println("Conectado ao banco: " + database.getName());
        } catch(Exception e) {
            e.printStackTrace();
            System.out.println("Erro na conexão com MongoDB");
        }
    }
}
