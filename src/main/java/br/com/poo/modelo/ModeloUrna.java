package br.com.poo.modelo;

import org.bson.Document;
import java.util.Arrays;

import br.com.poo.controller.ControllerUrna;

public class ModeloUrna {
    public Document votosPorCandidato = new Document();
    ControllerUrna controller;

    public ModeloUrna(ControllerUrna controller) {
        this.controller = controller;
        inicializarVotos(controller.buscarTodosCandidatos());
    }

    private void inicializarVotos(Document[] candidatos) {
        Arrays.stream(candidatos)
            .map(c -> c.getString("nome"))
            .forEach(nome -> votosPorCandidato.append(nome, 0));
        votosPorCandidato.append("branco", 0);
    }

    public void registrarVoto(String nome) {
        int votos = votosPorCandidato.getInteger(nome, 0);
        votosPorCandidato.put(nome, votos + 1);
        votosPorCandidato.forEach(
            (k, v) -> System.out.printf("%s: %s\n", k, v)
        );
    }

    public Document getMapaVotos() {
        return votosPorCandidato;
    }
}