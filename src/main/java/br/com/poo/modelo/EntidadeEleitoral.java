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

    public Partido getPartido() {
        return partido;
    }

    public void setPartido(Partido partido) {
        this.partido = partido;
    }

    public Cargo getCargo() {
        return cargo;
    }

    public void setCargo(Cargo cargo) {
        this.cargo = cargo;
    }

    public Candidato getCandidato() {
        return candidato;
    }

    public void setCandidato(Candidato candidato) {
        this.candidato = candidato;
    }
}
