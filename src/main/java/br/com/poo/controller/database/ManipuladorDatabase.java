package br.com.poo.controller.database;

import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoClients;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import org.bson.types.ObjectId;
import org.bson.Document;

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

    private MongoCollection<Document> partidos;
    private MongoCollection<Document> candidatos;

    /**
     * Construtor da classe. Estabelece conexão com o banco de dados e inicializa as coleções.
     *
     * @param controller instância do controlador da urna eletrônica
     */
    public ManipuladorDatabase(ControllerUrna controller) {
        this.controller = controller;
        conectar();
    }

    /**
     * Realiza a conexão com o banco de dados MongoDB e inicializa as coleções de partidos e candidatos.
     */
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

    /**
     * Recupera um partido a partir do número fornecido.
     *
     * @param numero número do partido
     * @return documento do partido encontrado, ou {@code null} se não existir
     */
    public Document getPartido(String numero) {
        return partidos.find(eq("numero", numero)).first();
    }

    /**
     * Recupera um candidato a partir do número fornecido.
     *
     * @param numero número do candidato
     * @return documento do candidato encontrado, ou {@code null} se não existir
     */
    public Document getCandidato(String numero) {
        return candidatos.find(eq("numero", numero)).first();
    }

    /**
     * Recupera todos os documentos da coleção de candidatos.
     *
     * @return array de documentos representando todos os candidatos
     */
    public Document[] getTodosCandidatos() {
        return toArray(candidatos.find());
    }

    /**
     * Recupera todos os documentos de uma coleção específica (partidos ou candidatos).
     *
     * @param nome nome da coleção desejada
     * @return array de documentos da coleção, ou array vazio se o nome for inválido
     */
    public Document[] getColecao(String nome) {
        MongoCollection<Document> colecao = switch (nome) {
            case "partidos" -> partidos;
            case "candidatos" -> candidatos;
            default -> null;
        };

        return colecao != null ? toArray(colecao.find()) : new Document[0];
    }

    /**
     * Recupera todos os candidatos vinculados a um determinado partido.
     *
     * @param partido documento do partido
     * @return array de documentos dos candidatos pertencentes ao partido
     */
    public Document[] getCandidatosPartido(Document partido) {
        ObjectId id = partido.getObjectId("_id");
        return toArray(candidatos.find(eq("partido_id", id)));
    }

    /**
     * Converte um iterable de documentos MongoDB em um array.
     *
     * @param iterable documentos retornados por uma consulta
     * @return array de documentos
     */
    private Document[] toArray(FindIterable<Document> iterable) {
        List<Document> lista = iterable.into(new ArrayList<>());
        return lista.toArray(new Document[0]);
    }

    /**
     * Encerra a conexão com o MongoDB, se estiver ativa.
     */
    public void fecharConexao() {
        if (mongoClient != null) {
            mongoClient.close();
            System.out.println("Conexão com MongoDB encerrada.");
        }
    }
}