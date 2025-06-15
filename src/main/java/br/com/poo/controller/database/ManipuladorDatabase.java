package br.com.poo.controller.database;

import org.bson.Document;
import org.bson.types.ObjectId;

import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoCollection;

import static com.mongodb.client.model.Filters.eq;
public class ManipuladorDatabase {
    private final String URI_DATABASE = "mongodb+srv://user1234:senha1234@urna-eletronica.qiefb7e.mongodb.net/?retryWrites=true&w=majority&appName=urna-eletronica";
    private MongoClient mongoClient;
    private MongoDatabase database;

    private MongoCollection<Document> partidos;
    private MongoCollection<Document> candidatos;

    public void iniciarCliente() {
        try {
            mongoClient = MongoClients.create(URI_DATABASE);
            database = mongoClient.getDatabase("urna_db");
            partidos = database.getCollection("partidos");
            candidatos = database.getCollection("candidatos");
            System.out.println("Conectado ao banco: " + database.getName());
        } catch(Exception e) {
            e.printStackTrace();
            System.out.println("Erro na conexão com MongoDB");
        }
    }

    public Document[] getCandidato(String voto) {
        for (Document candidato : candidatos.find()) {
            if (candidato.getString("numero").equals(voto)) {
                ObjectId idPartido = candidato.getObjectId("partido_id");
                Document partido = partidos.find(eq("_id", idPartido)).first();
                return new Document[] { candidato, partido };
            }
        }
        System.out.println("Database: Item não encontrado.");
        return null;
    }

    public void fecharConexao() {
        if (mongoClient != null) {
            mongoClient.close();
            System.out.println("Conexão com MongoDB encerrada.");
        }
    }
}
