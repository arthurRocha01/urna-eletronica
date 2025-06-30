package br.com.poo.modelo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bson.Document;

import br.com.poo.controller.ControllerUrna;

/**
 * Classe responsável por armazenar e gerenciar os votos registrados na urna.
 *
 * <p>Realiza o controle interno da contagem de votos para candidatos e votos em branco,
 * além de calcular os candidatos mais votados e suas respectivas porcentagens.
 *
 * @author Arthur Rocha
 * @version 1.0
 * @since 1.0
 */
public class ModeloUrna {

    /**
     * Lista de documentos representando os votos dos candidatos e do voto branco.
     */
    public List<Document> contabilidadeVotos = new ArrayList<>();

    /**
     * Referência ao controlador principal da aplicação.
     */
    ControllerUrna controller;

    /**
     * Construtor que inicializa o modelo com base nos candidatos disponíveis no banco.
     *
     * @param controller instância do controlador da urna
     */
    public ModeloUrna(ControllerUrna controller) {
        this.controller = controller;
        inicializarVotos(controller.buscarTodosCandidatos());
    }

    /**
     * Inicializa a contabilidade de votos com os candidatos e seus respectivos partidos.
     *
     * @param candidatos array de documentos representando os candidatos
     */
    private void inicializarVotos(Document[] candidatos) {
        Arrays.stream(candidatos)
            .forEach(candidato -> {
                String nome = candidato.getString("nome");
                String partido = controller
                    .buscarPartido(candidato.getString("numero").substring(0, 2))
                    .getString("sigla");
                contabilidadeVotos.add(new Document("nome", nome)
                        .append("sigla", partido)
                        .append("votos", 0));
            });

        contabilidadeVotos.add(new Document("nome", "branco").append("votos", 0));
    }

    /**
     * Registra um voto incrementando a contagem do candidato ou voto branco.
     *
     * @param nome nome do candidato ou "branco"
     */
    public void registrarVoto(String nome) {
        for (Document candidato : contabilidadeVotos) {
            if (candidato.getString("nome").equals(nome)) {
                int votos = candidato.getInteger("votos");
                candidato.put("votos", votos + 1);
                break;
            }
        }
    }

    /**
     * Retorna a lista dos votos contabilizados com porcentagens e imprime os resultados no console.
     *
     * @return lista de documentos com os votos dos três candidatos mais votados e os votos brancos
     */
    public List<Document> contabilizarVotos() {
        int totalVotos = pegarTotalVotos();
        int totalVotosBrancos = pegarTotalVotosBrancos();
        List<Document> votosContabilizados = pegarMaisVotados();

        votosContabilizados.add(new Document("nome", "branco").append("votos", totalVotosBrancos));
        calcularPorcentagemVotos(votosContabilizados, totalVotos);

        for (Document votoCandidato : votosContabilizados) {
            String nome = votoCandidato.getString("nome");
            String partido = votoCandidato.getString("sigla");
            int votos = votoCandidato.getInteger("votos");
            double porcentagem = votoCandidato.getDouble("porcentagem");
            System.out.printf("Candidato: %s - Partido: %s - Votos: %d - Porcentagem: %.2f%%\n", nome, partido, votos, porcentagem);
        }

        return votosContabilizados;
    }

    /**
     * Calcula o total de votos (incluindo brancos).
     *
     * @return número total de votos
     */
    private int pegarTotalVotos() {
        int count = 0;
        for (Document votoCandidato : contabilidadeVotos) {
            count += votoCandidato.getInteger("votos");
        }
        return count;
    }

    /**
     * Calcula o total de votos em branco.
     *
     * @return número de votos em branco
     */
    private int pegarTotalVotosBrancos() {
        int count = 0;
        for (Document votoCandidato : contabilidadeVotos) {
            if (votoCandidato.getString("nome").equals("branco")) {
                count += votoCandidato.getInteger("votos");
            }
        }
        return count;
    }

    /**
     * Retorna uma lista com os três candidatos mais votados (excluindo brancos).
     *
     * @return lista de documentos dos candidatos mais votados
     */
    private List<Document> pegarMaisVotados() {
        List<Document> candidatosValidos = new ArrayList<>();
        for (Document votoCandidato : contabilidadeVotos) {
            if (!"branco".equals(votoCandidato.getString("nome"))) {
                candidatosValidos.add(votoCandidato);
            }
        }

        candidatosValidos.sort((candidato1, candidato2) ->
            Integer.compare(candidato2.getInteger("votos"), candidato1.getInteger("votos")));

        return new ArrayList<>(candidatosValidos.subList(0, Math.min(3, candidatosValidos.size())));
    }

    /**
     * Calcula a porcentagem de votos para cada candidato da lista com base no total de votos.
     *
     * @param votosContabilizados lista de votos contabilizados
     * @param totalVotos total de votos
     */
    private void calcularPorcentagemVotos(List<Document> votosContabilizados, int totalVotos) {
        for (Document doc : votosContabilizados) {
            int numeroVotos = doc.getInteger("votos");
            doc.append("porcentagem", calcularPorcentagem(numeroVotos, totalVotos));
        }
    }

    /**
     * Calcula a porcentagem de votos com base no total.
     *
     * @param numeroVotos número de votos do candidato
     * @param totalVotos total de votos
     * @return valor da porcentagem com duas casas decimais
     */
    private double calcularPorcentagem(int numeroVotos, int totalVotos) {
        return numeroVotos == 0 ? 0.0 : (numeroVotos * 100.0) / totalVotos;
    }
}