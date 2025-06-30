package br.com.poo.modelo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bson.Document;

import br.com.poo.controller.ControllerUrna;

public class ModeloUrna {
    public List<Document> contabilidadeVotos = new ArrayList<>();
    ControllerUrna controller;

    public ModeloUrna(ControllerUrna controller) {
        this.controller = controller;
        inicializarVotos(controller.buscarTodosCandidatos());
    }

    private void inicializarVotos(Document[] candidatos) {
    Arrays.stream(candidatos)
        .forEach(candidato -> {
            String nome = candidato.getString("nome");
            String partido = controller.buscarPartido(candidato.getString("numero").substring(0, 2)).getString("sigla");
            contabilidadeVotos.add(new Document("nome", nome)
                                    .append("sigla", partido)
                                    .append("votos", 0));
        });
    contabilidadeVotos.add(new Document("nome", "branco").append("votos", 0));
}

    public void registrarVoto(String nome) {
        for (Document candidato : contabilidadeVotos) {
            if (candidato.getString("nome").equals(nome)) {
                int votos = candidato.getInteger("votos");
                candidato.put("votos", votos + 1);
                break;
            }
        }
    }

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

    private int pegarTotalVotos() {
        int count = 0;
        for (Document votoCandidato : contabilidadeVotos) {
            count += votoCandidato.getInteger("votos");
        }
        return count;
    }

    private int pegarTotalVotosBrancos() {
        int count = 0;
        for (Document votoCandidato : contabilidadeVotos) {
            if (votoCandidato.getString("nome").equals("branco")) count += votoCandidato.getInteger("votos");
        }
        return count;
    }

    private List<Document> pegarMaisVotados() {
        List<Document> candidatosValidos = new ArrayList<>();
        for (Document votoCandidato : contabilidadeVotos) {
            if (!"branco".equals(votoCandidato.getString("nome"))) {
                candidatosValidos.add(votoCandidato);
            }
        }
        candidatosValidos.sort((candidato1, candidato2) -> Integer.compare(candidato1.getInteger("votos"), candidato2.getInteger("votos")));
        return new ArrayList<>(candidatosValidos.subList(0, Math.min(3, contabilidadeVotos.size())));
    }
    
    private void calcularPorcentagemVotos(List<Document> votosContabilizados, int totalVotos) {
        for (Document doc : votosContabilizados) {
            int numeroVotos = doc.getInteger("votos");
            doc.append("porcentagem", calcularPorcentagem(numeroVotos, totalVotos));
        }
    }

    private double calcularPorcentagem(int numeroVotos, int totalVotos) {
        return numeroVotos == 0 ? 0.0 : (numeroVotos * 100.0) / totalVotos;
    }
}
