package br.com.poo.modelo;

public class Partido {
    public String partido;
    public String siglaPartido;
    public String numeroPartido;
    public double votosPartido;

    public Partido(String[] partidoInfo) {
        this.partido = partidoInfo[0];
        this.siglaPartido = partidoInfo[1];
        this.numeroPartido = partidoInfo[2];
    }
}
