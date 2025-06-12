package br.com.poo.modelo;

public class Partido {
    public String partido;
    public String siglaPartido;
    public String numeroPartido;

    public Partido(String[] partidoInfo) {
        this.partido = partidoInfo[0];
        this.siglaPartido = partidoInfo[1];
        this.numeroPartido = partidoInfo[2];
    }

    public String getSiglaPartido() {
        return siglaPartido;
    }

    public void setSiglaPartido(String siglaPartido) {
        this.siglaPartido = siglaPartido;
    }

    public String getPartido() {
        return partido;
    }

    public void setPartido(String partido) {
        this.partido = partido;
    }

    public String getNumeroPartido() {
        return numeroPartido;
    }

    public void setNumeroPartido(String numeroPartido) {
        this.numeroPartido = numeroPartido;
    }
}
