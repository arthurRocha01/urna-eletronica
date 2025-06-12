package br.com.poo.modelo.auxiliares;

public class ManipuladorDados {
    private String parseString(Object dado) {
        return (String) dado;
    }

    private double parseDounle(Object dado) {
        return (Double) dado;
    }

    public void getDado(Object dado) {
        if (dado instanceof String) parseString(dado);
        if (dado instanceof Double) parseDounle(dado);
    }
}
