package br.com.poo.controller.database;

import java.util.ArrayList;
import java.util.List;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoClient;
import org.bson.Document;
import org.bson.types.ObjectId;

import br.com.poo.controller.ControllerUrna;

/**
 * Classe responsável por manipular a conexão e operações com o banco de dados MongoDB.
 *
 * <p>Fornece métodos para recuperar documentos da base de dados de candidatos e partidos,
 * bem como estabelecer e encerrar conexões com o banco. Também serve como ponte entre a
 * aplicação e o banco de dados, utilizando a camada de controle {@code ControllerUrna}.
 *
 * @author Arthur Rocha
 * @version 1.0
 * @since 1.0
 */
public class ManipuladorDatabase {

    private static final String URI_DATABASE = "mongodb+srv://user1234:senha1234@urna-eletronica.qiefb7e.mongodb.net/?retryWrites=true&w=majority&appName=urna-eletronica";
    private static final String DATABASE_NAME = "urna_db";

    private MongoClient mongoClient;
    private MongoDatabase database;
    private ControllerUrna controller;

    private MongoCollection<Document> colecaoPartido;
    private MongoCollection<Document> colecaoCandidato;
    
    private List<Document> partidos;
    private List<Document> candidatos;

    /**
     * Construtor da classe. Estabelece conexão com o banco de dados e inicializa as coleções.
     *
     * @param controller instância do controlador da urna eletrônica
     */
    public ManipuladorDatabase(ControllerUrna controller) {
        this.controller = controller;
    }

    /**
     * Realiza a conexão com o banco de dados MongoDB e inicializa as coleções de partidos e candidatos.
     */
    public void conectar() {
        try {
            mongoClient = MongoClients.create(URI_DATABASE);
            database = mongoClient.getDatabase(DATABASE_NAME);
            colecaoPartido = database.getCollection("partidos");
            colecaoCandidato = database.getCollection("candidatos");
            controller.avisarSistema("ManipuladorDatabase", "conexão feita com sucesso - " + DATABASE_NAME);
        } catch (Exception e) {
            controller.avisarSistema("ManipuladorDatabase", "Erro na conexão com MongoDB:");
            e.printStackTrace();
        }
    }
    
    public void carregarDados() {
        partidos = colecaoPartido.find().into(new ArrayList<>());
        candidatos = colecaoCandidato.find().into(new ArrayList<>());
    }

    /**
     * Recupera um partido a partir do número fornecido.
     *
     * @param numero número do partido
     * @return documento do partido encontrado, ou {@code null} se não existir
     */
    public Document getPartido(String numero) {
        for (Document partido : partidos) {
            if (numero.equals(partido.getString("numero"))) return partido;
        }
        return null;
    }

    /**
     * Recupera um candidato a partir do número fornecido.
     *
     * @param numero número do candidato
     * @return documento do candidato encontrado, ou {@code null} se não existir
     */
    public Document getCandidato(String numero) {
        for (Document candidato : candidatos) {
            if (numero.equals(candidato.getString("numero"))) return candidato;
        }
        return null;
    }

    /**
     * Recupera todos os documentos da coleção de candidatos.
     *
     * @return array de documentos representando todos os candidatos
     */
    public Document[] getTodosCandidatos() {
        return candidatos.toArray(new Document[0]);
    }

    /**
     * Recupera todos os documentos de uma coleção específica (partidos ou candidatos).
     *
     * @param nome nome da coleção desejada
     * @return array de documentos da coleção, ou array vazio se o nome for inválido
     */
    public Document[] getColecao(String nome) {
        List<Document> colecao = switch (nome) {
            case "partidos" -> partidos;
            case "candidatos" -> candidatos;
            default -> null;
        };
                
        return colecao != null ? colecao.toArray(new Document[0]) : new Document[0];
    }

    /**
     * Recupera todos os candidatos vinculados a um determinado partido.
     *
     * @param partido documento do partido
     * @return array de documentos dos candidatos pertencentes ao partido
     */
    public Document[] getCandidatosPartido(Document partido) {
        List<Document> candidatosFiltrados = new ArrayList<>();
        ObjectId idPartido = partido.getObjectId("_id");
        for (Document candidato : candidatos) {
            if (idPartido.equals(candidato.getObjectId("partido_id"))) {
                candidatosFiltrados.add(candidato);
            };
        }
        
        return candidatosFiltrados.toArray(new Document[0]);
    }

    /**
     * Encerra a conexão com o MongoDB, se estiver ativa.
     */
    public void fecharConexao() {
        if (mongoClient != null) {
            mongoClient.close();
            controller.avisarSistema("ManipuladorDatabase", "Conexão com MongoDB encerrada");
        }
    }
}