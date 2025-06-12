package br.com.poo.modelo;

public class Partido {
    public String nome;
    public String sigla;
    public String numero;
    public double votosPartido;

    public Partido(String[] partidoInfo) {
        this.nome = partidoInfo[0];
        this.sigla = partidoInfo[1];
        this.numero = partidoInfo[2];
    }
}
