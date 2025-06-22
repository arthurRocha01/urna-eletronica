package br.com.poo.controller;

import org.bson.Document;
import br.com.poo.controller.database.ManipuladorDatabase;
import br.com.poo.modelo.ModeloUrna;
import br.com.poo.view.TelaPrincipal;

public class ControllerUrna {

    public TelaPrincipal display;
    public ModeloUrna model;
    private ManipuladorDatabase db;

    public ControllerUrna(TelaPrincipal display) {
        this.display = display;
        this.db = new ManipuladorDatabase();
        this.model = new ModeloUrna(this);
    }

    public void onAcao(String acao) {
        if (acao.matches("\\d+") && !display.visor.tecladoBloqueado) {
            display.visor.inserirDigito(acao);
        } else if (!display.visor.botoesBloqeuado) {
            if (acao.equals("CONFIRMA")) display.visor.botoesBloqeuado = true;
            tratarBotao(acao);
        }
    }

    private void tratarBotao(String acao) {
        switch (acao) {
            case "CORRIGE" -> display.visor.visorFunctios.apagarTextoCampos();
            case "CONFIRMA" -> registrarVoto();
            case "BRANCO" -> registrarBranco();
        }
    }

    private void registrarVoto() {
        String voto = display.visor.getVoto();
        Document candidato = buscarCandidato(voto);

        if (votoValido(voto, candidato)) {
            registrarVotoCandidato(candidato);
        } else {
            registrarBranco();
            display.visor.visorFunctios.apagarTextoCampos();
        }
    }

    private boolean votoValido(String voto, Document candidato) {
        return !voto.equals("99999") && candidato != null;
    }

    private void registrarVotoCandidato(Document candidato) {
        display.visor.visorFunctios.exibirConfirmaVoto();
        incrementarVoto(candidato.getString("nome"));
    }

    private void registrarBranco() {
        incrementarVoto("branco");
        System.out.println("Voto branco ou inválido.");
    }

    private void incrementarVoto(String nome) {
        int votos = model.contabilidadeVotos.getInteger(nome, 0);
        model.contabilidadeVotos.put(nome, votos + 1);
    }

    public Document buscarCandidato(String numero)                { return db.getCandidato(numero); }
    public Document buscarInformacoesPartido(String sigla)        { return db.getPartido(sigla); }
    public Document[] buscarInformacoesVoto(String voto)          { return db.getInfoCandidato(voto); }
    public Document[] buscarDadosColecao(String colecao)          { return db.getColecao(colecao); }
    public Document[] buscarCandidatosPartido(Document partido)   { return db.getCandidatosPartido(partido); }
    public Document[] buscarTodosCandidatos()                     { return db.getTodosCandidatos(); }
}
