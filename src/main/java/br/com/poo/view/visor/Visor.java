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

        if (isVotoCompleto()) {
            processarVoto();
        }
    }

    public boolean isVotoCompleto() {
        for (JTextField campo : camposNumero) {
            if (campo.getText().isEmpty()) return false;
        }
        bloquarTeclado();
        return true;
    }

    public boolean isVotandoBranco() {
        return builder.telaConfirmaBranco.isShowing();
    }
    
    public void bloquarTeclado() {
        tecladoBloqueado = true;
        controlador.avisarSistema("Visor", "teclado bloquado");
    }

    public void desbloquearTeclado() {
        tecladoBloqueado = false;
        controlador.avisarSistema("Visor", "teclado desbloqueado");
    }

    public void bloquearBotoes() {
        botoesBloqueados = true;
        controlador.avisarSistema("Visor", "botoes bloquados");
    }

    public void desbloquearBotoes() {
        botoesBloqueados = false;
        controlador.avisarSistema("Visor", "botoes desbloqueados");
    }

    private void processarVoto() {
        String voto = getVotoInserido();
        dadosCandidato = controlador.buscarInfoCandidato(voto);
        if (dadosCandidato != null) {
            builder.adicionarInfosEntidade(dadosCandidato);
            controlador.avisarSistema("Visor", "informações do candidato adcionadas");
        }
    }

    public String getVotoInserido() {
        numeroDigitado = new StringBuilder();
        for (JTextField campo : camposNumero) {
            numeroDigitado.append(campo.getText());
        }
        return numeroDigitado.toString();
    }

    public void limparCamposVoto() {
        tecladoBloqueado = false;
        botoesBloqueados = false;
        desbloquearTeclado();
        desbloquearBotoes();
        for (JTextField campo : camposNumero) campo.setText("");
        builder.removerInfosEntidade();
    }

    public boolean votosEstaVazio() {
        for (JTextField campo : camposNumero) {
            if (!campo.getText().isEmpty()) return false;
        }
        return true;
    }

    public void setController(ControllerUrna controlador) {
        this.controlador = controlador;
    }
}