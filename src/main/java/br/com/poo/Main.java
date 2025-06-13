package br.com.poo;

import br.com.poo.controller.ControllerModel;

public class Main {
    public static void main(String[] args) {
        ControllerModel controller = new ControllerModel();
        controller.getColecao("partidos");
    }
}