package br.com.poo.controller;

import br.com.poo.modelo.Partido;
import br.com.poo.modelo.Cargo;
import br.com.poo.modelo.Candidato;

public class ControllerModel {
    private Partido[] partidos;

    public void criarPartidos() {
        partidos = null;
        // Instancia os partidos existentes do banco de dados.
    }

        private String[] partidoInfo = {"Nome Partido", "Sigla Partido", "Número partido"};
        private String[] cargoInfo = {"Nome cargo"};
        private String[] candidatoInfo = {"Nome candidato", "Número Candidato", "10"};
        public void teste() {
            criarCandidatos();
        }

    public void criarCandidatos() {
        Partido partido = new Partido(partidoInfo); // buscar o partido correspondente.
        Cargo cargo = new Cargo(cargoInfo); // buscar o cargo correspondente.
        // String[] candidatoInfo = null; // buscar as infos correspondentes.
        Candidato candidato = new Candidato(candidatoInfo, partido, cargo);
        System.out.println(candidato.partido.nome);
    }

    // public void criarCandidato(String[] partidoInfo, String[] cargoInfo, String[] candidtoInfo) {
    //     Partido partidoCandidato = new Partido(partidoInfo);
    //     Cargo cargoCandidato = new Cargo(cargoInfo);
    //     Candidato candidato = new Candidato(candidtoInfo);

    //     EntidadeEleitoral entidadeEleitoral = new EntidadeEleitoral(partidoCandidato, cargoCandidato, candidato);
    //     System.out.println(entidadeEleitoral.candidato.votosCandidato);
    // }
}
