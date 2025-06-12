package br.com.poo.controller;

import br.com.poo.modelo.EntidadeEleitoral;
import br.com.poo.modelo.Partido;
import br.com.poo.modelo.Cargo;
import br.com.poo.modelo.Candidato;

public class ControllerModel {
    public void criarCandidato(String[] partidoInfo, String[] cargoInfo, String[] candidtoInfo) {
        Partido partidoCandidato = new Partido(partidoInfo);
        Cargo cargoCandidato = new Cargo(cargoInfo);
        Candidato candidato = new Candidato(candidtoInfo);

        EntidadeEleitoral entidadeEleitoral = new EntidadeEleitoral(partidoCandidato, cargoCandidato, candidato);
        System.out.println(entidadeEleitoral.candidato.candidato);
    }
}
