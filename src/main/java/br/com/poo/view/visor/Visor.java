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

        processarVoto();
    }

    private void processarVoto() {
        String voto = getVotoInserido();
        System.out.println(voto);
        
        if (voto.length() == 2) {
            Document partido = controlador.buscarPartido(voto);
            System.out.println(partido);
            if (partido != null) builder.mostrarInformacoesPartido(partido);
        }
        else if (voto.length() == 5) {
            Document candidato = controlador.buscarCandidato(voto);
            System.out.println(candidato);
            if (candidato != null) builder.mostrarInformacoesCandidato(candidato);
        }
    }

    public String getVotoInserido() {
        numeroDigitado = new StringBuilder();
        for (JTextField campo : camposNumero) {
            numeroDigitado.append(campo.getText());
        }
        return numeroDigitado.toString();
    }

    public boolean isVotoCompleto() {
        for (JTextField campo : camposNumero) {
            if (campo.getText().isEmpty()) return false;
        }
        bloquarTeclado();
        return true;
    }

    public void bloquarTeclado() {
        tecladoBloqueado = true;
        controlador.avisarSistema("Visor", "teclado bloquado");
    }

    public boolean isVotandoBranco() {
        return builder.telaConfirmaBranco.isShowing();
    }

    public void limparCamposVoto() {
        desbloquearTeclado();
        desbloquearBotoes();
        for (JTextField campo : camposNumero) campo.setText("");
        builder.limparInformacoesCandidato();
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