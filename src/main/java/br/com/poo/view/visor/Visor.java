package br.com.poo.view.visor;

import javax.swing.*;
import org.bson.Document;
import java.util.ArrayList;
import java.util.List;
import br.com.poo.controller.ControllerUrna;

public class Visor extends JPanel {

    public final int LARGURA = 700;
    public final int ALTURA = 400;

    public VisorBuilder builder;
    public ControllerUrna controlador;

    public StringBuilder numeroDigitado;
    public Document[] dadosCandidato;
    public JTextField[] camposNumero = new JTextField[5];

    public List<JComponent> componentesFixos = new ArrayList<>();
    public List<JComponent> componentesDinamicos = new ArrayList<>();

    public boolean tecladoBloqueado = false;
    public boolean botoesBloqueados = false;

    public Visor() {
        this.builder = new VisorBuilder(this);
        builder.iniciarTela();
    }

    public void inserirDigito(String digito) {
        if (tecladoBloqueado) return;

        for (JTextField campo : camposNumero) {
            if (campo.getText().isEmpty()) {
                campo.setText(digito);
                break;
            }
        }

        if (votoEstaCompleto()) {
            processarVoto();
        }
    }

    public boolean votoEstaCompleto() {
        for (JTextField campo : camposNumero) {
            if (campo.getText().isEmpty()) return false;
        }
        tecladoBloqueado = true;
        return true;
    }

    private void processarVoto() {
        String voto = getNumeroDigitado();
        dadosCandidato = controlador.buscarInfoCandidato(voto);
        if (dadosCandidato != null) {
            builder.adicionarInfosEntidade(dadosCandidato);
        }
    }

    public String getNumeroDigitado() {
        numeroDigitado = new StringBuilder();
        for (JTextField campo : camposNumero) {
            numeroDigitado.append(campo.getText());
        }
        return numeroDigitado.toString();
    }

        public void apagarTextoCampos() {
        tecladoBloqueado = false;
        botoesBloqueados = false;
        System.out.println("botoes desbloqueados");
        for (JTextField campo : camposNumero) campo.setText("");
        builder.removerInfosEntidade();
    }

    public void setController(ControllerUrna controlador) {
        this.controlador = controlador;
    }
}