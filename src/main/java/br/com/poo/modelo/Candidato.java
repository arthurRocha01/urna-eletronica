package br.com.poo.modelo;

public class Candidato {
    public String candidato;
    public String numeroCandidato;

    public Candidato(String[] candidatoInfo) {
        this.candidato = candidatoInfo[0];
        this.numeroCandidato = candidatoInfo[1];
    }

    public String getCandidato() {
        return candidato;
    }

    public void setCandidato(String candidato) {
        this.candidato = candidato;
    }

    public String getNumeroCandidato() {
        return numeroCandidato;
    }

    public void setNumeroCandidato(String numeroCandidato) {
        this.numeroCandidato = numeroCandidato;
    }
}
