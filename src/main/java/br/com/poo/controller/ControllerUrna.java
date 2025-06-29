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
        this.banco = new ManipuladorDatabase(this);
        this.urna = new ModeloUrna(this);
    }

    public void executarAcao(String comando) {
        if (isInserindoVoto(comando)) {
            tela.visor.inserirDigito(comando);
        } else if (!tela.visor.botoesBloqueados || tela.visor.isVotandoBranco()) {
            if (isVotando(comando)) tela.visor.bloquearBotoes();
            processarComando(comando);
        }
    }

    private boolean isInserindoVoto(String comando) {
        return comando.matches("\\d+") && !tela.visor.tecladoBloqueado;
    }

    private boolean isVotando(String comando) {
        if (comando.equals("CONFIRMA") && tela.visor.isVotoCompleto()) {
            tela.visor.bloquearBotoes();
            return true;
        }
        return false;
    }

    private void processarComando(String comando) {
        switch (comando) {
            case "CORRIGE" -> tela.visor.limparCamposVoto();
            case "CONFIRMA" -> confirmarVoto();
            case "BRANCO" -> tela.visor.builder.manipuladorTelaVotoBranco("mostrar");
            default -> {}
        }
    }

    private void confirmarVoto() {
        String voto = tela.visor.getVotoInserido();
        Document candidato = buscarCandidato(voto);
        
        if (voto.equals("99999")) finalizarVotacao();
        else if (tela.visor.isVotoCompleto()) registrarVoto(voto, candidato);
        else if (tela.visor.isVotandoBranco()) registrarVotoBranco();
    }

    private void registrarVoto(String numero, Document candidato) {
        if (votoValido(numero, candidato)) registrarVotoCandidato(candidato);
        else tela.visor.builder.manipuladorTelaVotoBranco("mostrar");
        tela.visor.limparCamposVoto();
    }

    private boolean votoValido(String numero, Document candidato) {
        return !numero.equals("99999") && candidato != null;
    }

    private void registrarVotoCandidato(Document candidato) {
        urna.registrarVoto(candidato.getString("nome"));
        tela.visor.builder.exibirConfirmaVoto();
        avisarSistema("Controller", "candidato votado");
    }

    private void registrarVotoBranco() {
        urna.registrarVoto("branco");
        avisarSistema("Controller", "Voto branco ou inválido.");
        tela.visor.builder.manipuladorTelaVotoBranco("fechar");
        tela.visor.builder.exibirConfirmaVoto();
    }

    private void finalizarVotacao() {
        avisarSistema("Controller(finalizar())", "exibir contabilidade");
        tela.visor.builder.mostrarContabilidade();
        urna.contabilizarVotos();
    }

    public Document buscarCandidato(String numero) {
        avisarSistema("Controller(buscarCandidato())", numero);
        return banco.getCandidato(numero);
    }

    public Document buscarPartido(String numero) {
        avisarSistema("Controller(buscarPartido())", numero);
        return banco.getPartido(numero);
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

    public void avisarSistema(Object tipo, String mensagem) {
        System.out.printf("--> %s: %s.\n", tipo, mensagem);
    }
}