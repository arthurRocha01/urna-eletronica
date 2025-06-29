package br.com.poo.modelo;

import org.bson.Document;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import br.com.poo.controller.ControllerUrna;

public class ModeloUrna {
    private List<Document> contabilidadeVotos = new ArrayList<>();
    private ControllerUrna controller;

    public ModeloUrna(ControllerUrna controller) {
        this.controller = controller;
        inicializarVotos(controller.buscarTodosCandidatos());
    }

    private void inicializarVotos(Document[] candidatos) {
        Arrays.stream(candidatos)
            .map(c -> c.getString("nome"))
            .forEach(nome -> contabilidadeVotos.add(new Document("nome", nome).append("votos", 0)));

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

        for (Document doc : votosContabilizados) {
            String nome = doc.getString("nome");
            int votos = doc.getInteger("votos");
            double porcentagem = doc.getDouble("porcentagem");
            System.out.printf("Candidato: %s - Votos: %d - Porcentagem: %.2f%%\n", nome, votos, porcentagem);
        }

        return votosContabilizados;
    }

    private int pegarTotalVotos() {
        int count = 0;
        for (Document doc : contabilidadeVotos) {
            count += doc.getInteger("votos");
        }
        return count;
    }

    private int pegarTotalVotosBrancos() {
        int count = 0;
        for (Document doc : contabilidadeVotos) {
            if (doc.getString("nome").equals("branco")) count += doc.getInteger("votos");
        }
        return count;
    }

    private List<Document> pegarMaisVotados() {
        List<Document> candidatosValidos = new ArrayList<>();
        for (Document d : contabilidadeVotos) {
            if (!"branco".equals(d.getString("nome"))) {
                candidatosValidos.add(d);
            }
        }
        candidatosValidos.sort((d1, d2) -> Integer.compare(d2.getInteger("votos"), d1.getInteger("votos")));
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
