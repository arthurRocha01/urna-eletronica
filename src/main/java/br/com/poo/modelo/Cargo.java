package br.com.poo.modelo;

public class Cargo {
    public String cargo;

    public Cargo(String[] cargoInfo) {
        this.cargo = cargoInfo[0];
    }

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }
}
