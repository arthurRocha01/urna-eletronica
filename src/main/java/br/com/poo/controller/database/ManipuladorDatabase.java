package br.com.poo.controller.database;

import org.bson.Document;
import org.bson.types.ObjectId;

import com.mongodb.client.*;

import br.com.poo.controller.ControllerUrna;

import static com.mongodb.client.model.Filters.*;

import java.util.ArrayList;
import java.util.List;

public class ManipuladorDatabase {
    private static final String URI_DATABASE = "mongodb+srv://user1234:senha1234@urna-eletronica.qiefb7e.mongodb.net/?retryWrites=true&w=majority&appName=urna-eletronica";
    private static final String DATABASE_NAME = "urna_db";

    private MongoClient mongoClient;
    private MongoDatabase database;
    private ControllerUrna controller;

    private MongoCollection<Document> partidos;
    private MongoCollection<Document> candidatos;

    public ManipuladorDatabase(ControllerUrna controller) {
        this.controller = controller;
        conectar();
    }

    private void conectar() {
        try {
            mongoClient = MongoClients.create(URI_DATABASE);
            database = mongoClient.getDatabase(DATABASE_NAME);
            partidos = database.getCollection("partidos");
            candidatos = database.getCollection("candidatos");
            controller.avisarSistema("ManipuladorDatabase", "conexão feita com sucesso - " + DATABASE_NAME);
        } catch (Exception e) {
            controller.avisarSistema("ManipuladorDatabase", "Erro na conexão com MongoDB:");
            e.printStackTrace();
        }
    }

    public Document getPartido(String sigla) {
        return partidos.find(eq("sigla", sigla)).first();
    }

    public Document getCandidato(String numero) {
        return candidatos.find(eq("numero", numero)).first();
    }

    public Document[] getTodosCandidatos() {
        return toArray(candidatos.find());
    }

    public Document[] getColecao(String nome) {
        MongoCollection<Document> colecao = switch (nome) {
            case "partidos" -> partidos;
            case "candidatos" -> candidatos;
            default -> null;
        };

        return colecao != null ? toArray(colecao.find()) : new Document[0];
    }

    public Document[] getInfoCandidato(String numero) {
        Document candidato = getCandidato(numero);
        if (candidato == null) {
            controller.avisarSistema("ManipuladorDatabase", "Candidato não encontrado");
            return null;
        }

        ObjectId partidoId = candidato.getObjectId("partido_id");
        Document partido = partidos.find(eq("_id", partidoId)).first();

        return new Document[] { candidato, partido };
    }

    public Document[] getCandidatosPartido(Document partido) {
        ObjectId id = partido.getObjectId("_id");
        return toArray(candidatos.find(eq("partido_id", id)));
    }


    private Document[] toArray(FindIterable<Document> iterable) {
        List<Document> lista = iterable.into(new ArrayList<>());
        return lista.toArray(new Document[0]);
    }

    public void fecharConexao() {
        if (mongoClient != null) {
            mongoClient.close();
            System.out.println("Conexão com MongoDB encerrada.");
        }
    }
}