package br.com.poo.modelo;

public class Candidato {
    public String candidato;
    public String numeroCandidato;
    public double votosCandidato;

    public Candidato(String[] candidatoInfo) {
        this.candidato = candidatoInfo[0];
        this.numeroCandidato = candidatoInfo[1];
        this.votosCandidato = Double.parseDouble(candidatoInfo[2]);
    }
}
