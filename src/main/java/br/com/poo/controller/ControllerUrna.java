package br.com.poo.controller;

import org.bson.Document;
import br.com.poo.controller.database.ManipuladorDatabase;
import br.com.poo.modelo.ModeloUrna;
import br.com.poo.view.TelaPrincipal;

public class ControllerUrna {

    public TelaPrincipal tela;
    public ModeloUrna urna;
    private final ManipuladorDatabase banco;

    public ControllerUrna(TelaPrincipal tela) {
        this.tela = tela;
        this.banco = new ManipuladorDatabase();
        this.urna = new ModeloUrna(this);
    }

    public void executarAcao(String comando) {
        if (comando.matches("\\d+") && !tela.visor.tecladoBloqueado) {
            tela.visor.inserirDigito(comando);
        } else if (!tela.visor.botoesBloqueados) {
            if (comando.equals("CONFIRMA")) {
                tela.visor.botoesBloqueados = true;
            }
            processarComando(comando);
        }
    }

    private void processarComando(String comando) {
        switch (comando) {
            case "CORRIGE" -> tela.visor.builder.apagarTextoCampos();
            case "CONFIRMA" -> confirmarVoto();
            case "BRANCO" -> registrarVotoBranco();
        }
    }

    private void confirmarVoto() {
        String numero = tela.visor.getNumeroDigitado();
        Document candidato = buscarCandidato(numero);

        if (votoEhValido(numero, candidato)) {
            registrarVoto(candidato.getString("nome"));
            tela.visor.builder.exibirConfirmaVoto();
        } else {
            registrarVotoBranco();
            tela.visor.builder.apagarTextoCampos();
        }
    }

    private boolean votoEhValido(String numero, Document candidato) {
        return !numero.equals("99999") && candidato != null;
    }

    private void registrarVoto(String nome) {
        urna.votosPorCandidato.put(nome, urna.votosPorCandidato.getInteger(nome, 0) + 1);
    }

    private void registrarVotoBranco() {
        registrarVoto("branco");
        System.out.println("Voto branco ou inválido.");
    }

    public Document buscarCandidato(String numero) {
        return banco.getCandidato(numero);
    }

    public Document buscarPartido(String sigla) {
        return banco.getPartido(sigla);
    }

    public Document[] buscarInfoCandidato(String numero) {
        return banco.getInfoCandidato(numero);
    }

    public Document[] buscarColecao(String nomeColecao) {
        return banco.getColecao(nomeColecao);
    }

    public Document[] buscarCandidatosPorPartido(Document partido) {
        return banco.getCandidatosPartido(partido);
    }

    public Document[] buscarTodosCandidatos() {
        return banco.getTodosCandidatos();
    }
}