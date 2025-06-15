package br.com.poo.controller.database;

import org.bson.Document;
import org.bson.types.ObjectId;

import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoCollection;

import static com.mongodb.client.model.Filters.*;

import java.util.ArrayList;
import java.util.List;

public class ManipuladorDatabase {
    private static final String URI_DATABASE = "mongodb+srv://user1234:senha1234@urna-eletronica.qiefb7e.mongodb.net/?retryWrites=true&w=majority&appName=urna-eletronica";

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
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Erro na conexão com MongoDB");
        }
    }

    public Document getPartido(String sigla) {
        Document partido = partidos.find(eq("sigla", sigla)).first();
        if (partido == null) {
            System.out.println("Database: Partido não encontrado.");
        }
        return partido;
    }

    public Document[] getCandidato(String numero) {
        Document candidato = candidatos.find(eq("numero", numero)).first();
        if (candidato == null) {
            System.out.println("Database: Candidato não encontrado.");
            return new Document[0];
        }

        ObjectId idPartido = candidato.getObjectId("partido_id");
        Document partido = partidos.find(eq("_id", idPartido)).first();

        return new Document[] { candidato, partido };
    }

    public Document[] getColecao(String nomeColecao) {
        MongoCollection<Document> colecao = switch (nomeColecao) {
            case "partidos" -> partidos;
            case "candidatos" -> candidatos;
            default -> null;
        };

        List<Document> documentos = colecao.find().into(new ArrayList<>());
        return documentos.toArray(new Document[0]);
    }

    public void fecharConexao() {
        if (mongoClient != null) {
            mongoClient.close();
            System.out.println("Conexão com MongoDB encerrada.");
        }
    }
}