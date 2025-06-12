package br.com.poo.modelo;

public class EntidadeEleitoral {
    public Partido partido;
    public Cargo cargo;
    public Candidato candidato;

    public EntidadeEleitoral(Partido partido, Cargo cargo, Candidato candidato) {
        this.partido = partido;
        this.cargo = cargo;
        this.candidato = candidato;
    }
}
