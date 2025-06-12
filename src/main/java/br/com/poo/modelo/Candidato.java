package br.com.poo.modelo;

public class Candidato {
    public String nome;
    public String numero;
    public double votos;
    public Partido partido;
    public Cargo cargo;

    public Candidato(String[] candidatoInfo, Partido partido, Cargo cargo) {
        this.nome = candidatoInfo[0];
        this.numero = candidatoInfo[1];
        this.votos = Double.parseDouble(candidatoInfo[2]);
        this.partido = partido;
        this.cargo = cargo;
    }
}
