package br.com.poo.modelo;

public class Partido {
    public String nome;
    public String sigla;
    public String numero;
    public double votosPartido;

    public Partido(String nome, String sigla, String numero) {
        this.nome = nome;
        this.sigla = sigla;
        this.numero = numero;
    }
}
