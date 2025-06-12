package br.com.poo;

import br.com.poo.controller.ControllerModel;

public class Main {
    public static void main(String[] args) {
        Main main = new Main();
        main.teste();
    }

    public void teste() {
        String[] partidoInfo = {"Nome Partido", "Sigla Partido", "Número partido"};
        String[] cargoInfo = {"Nome cargo"};
        String[] candidatoInfo = {"Nome candidato", "Número Candidato"};
        ControllerModel controller = new ControllerModel();
        controller.criarCandidato(partidoInfo, cargoInfo, candidatoInfo);
    }
}