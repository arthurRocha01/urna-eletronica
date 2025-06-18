package br.com.poo.controller;

import org.bson.Document;
import com.mongodb.client.FindIterable;

import br.com.poo.controller.database.ManipuladorDatabase;
import br.com.poo.view.TelaPrincipal;

public class ControllerUrna {

    private ManipuladorDatabase manipuladorDatabase = new ManipuladorDatabase();

    private TelaPrincipal display;

    public ControllerUrna(TelaPrincipal display) {
        this.display = display;
        manipuladorDatabase.iniciarCliente();
    }

    public void onAcao(String acao) {
        if (acao.matches("\\d+") && !display.visor.visorBloqueado) {
            if (display.visor.visorBloqueado) return;
            display.visor.inserirDigito(acao);
        } else {
            acionarBotao(acao);
        }
    }

    private void acionarBotao(String acao) {
        switch (acao) {
            case "CORRIGE" -> display.visor.visorFunctios.apagarTextoCampos();
            case "CONFIRMA" -> display.visor.confirmaVoto();
            case "BRANCO" -> System.out.println("Voto em branco");
        }
    }

    public Document[] buscarInformacoesVoto(String voto) {
        return manipuladorDatabase.getInfoCandidato(voto);
    }

    public Document buscarInformacoesPartido(String sigla) {
        return manipuladorDatabase.getPartido(sigla);
    }

    public Document[] buscarDadosColecao(String colecao) {
        return manipuladorDatabase.getColecao(colecao);
    }

    public Document[] buscarCandidatosPartido(Document partido) {
        return manipuladorDatabase.getCandidatosPartido(partido);
    }

    public Document[] buscarTodosCandidatos() {
        return manipuladorDatabase.getTodosCandidatos();
    }

    public Document buscarCandidato(String numero) {
        return manipuladorDatabase.getCandidato(numero);
    }
}