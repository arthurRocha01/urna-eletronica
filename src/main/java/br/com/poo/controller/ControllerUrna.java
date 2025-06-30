package br.com.poo.controller;

import java.util.List;

import org.bson.Document;

import br.com.poo.controller.database.ManipuladorDatabase;
import br.com.poo.view.TelaPrincipal;
import br.com.poo.modelo.ModeloUrna;

/**
 * Classe responsável por coordenar as ações entre a interface gráfica (visão),
 * o modelo de dados (ModeloUrna) e o banco de dados (ManipuladorDatabase).
 *
 * <p>Gerencia a lógica principal da urna eletrônica, incluindo inserção de votos,
 * confirmação, votos brancos, encerramento da votação e comunicação com o sistema.
 *
 * @author Arthur Rocha
 * @version 1.0
 * @since 1.0
 */
public class ControllerUrna {

    public TelaPrincipal tela;
    public ModeloUrna urna;
    private ManipuladorDatabase banco;

    /**
     * Construtor principal da classe. Inicializa a tela, o manipulador do banco e o modelo da urna.
     *
     * @param tela a tela principal da aplicação
     */
    public ControllerUrna(TelaPrincipal tela) {
        this.tela = tela;
        this.banco = new ManipuladorDatabase(this);
        iniciarDatabase();
        this.urna = new ModeloUrna(this);
    }
    
    private void iniciarDatabase() {
        banco.conectar();
        banco.carregarDados();
//        banco.fecharConexao();
    }

    /**
     * Executa uma ação com base no comando recebido da interface.
     *
     * @param comando texto do botão pressionado
     */
    public void executarAcao(String comando) {
        if (isInserindoVoto(comando)) {
            tela.visor.inserirDigito(comando);
        } else if (!tela.visor.botoesBloqueados || tela.visor.isVotandoBranco() || tela.visor.isFinalizado()) {
            if (isVotando(comando)) tela.visor.bloquearBotoes();
            processarComando(comando);
        }
    }

    /**
     * Verifica se o comando corresponde à inserção de dígitos de um voto.
     *
     * @param comando valor pressionado
     * @return verdadeiro se for um número e o teclado não estiver bloqueado
     */
    private boolean isInserindoVoto(String comando) {
        return comando.matches("\\d+") && !tela.visor.tecladoBloqueado;
    }

    /**
     * Verifica se o comando é um voto válido (confirmação).
     *
     * @param comando valor pressionado
     * @return verdadeiro se for "CONFIRMA" e o voto estiver completo
     */
    private boolean isVotando(String comando) {
        if (comando.equals("CONFIRMA") && tela.visor.isVotoCompleto()) {
            tela.visor.bloquearBotoes();
            return true;
        }
        return false;
    }

    /**
     * Processa os comandos "CORRIGE", "CONFIRMA" ou "BRANCO".
     *
     * @param comando comando a ser processado
     */
    private void processarComando(String comando) {
        switch (comando) {
            case "CORRIGE" -> tela.visor.limparCamposVoto();
            case "CONFIRMA" -> confirmarVoto();
            case "BRANCO" -> tela.visor.builder.manipuladorTelaVotoBranco("mostrar");
            default -> {}
        }
    }

    /**
     * Confirma o voto digitado e executa a ação apropriada (registrar, branco ou finalizar).
     */
    private void confirmarVoto() {
        String voto = tela.visor.getVotoInserido();
        Document candidato = buscarCandidato(voto);
        
        if (tela.visor.isFinalizado()) tela.visor.builder.exibirTelaVoto();
        else if (voto.equals("99999")) finalizarVotacao();
        else if (tela.visor.isVotoCompleto()) registrarVoto(voto, candidato);
        else if (tela.visor.isVotandoBranco()) registrarVotoBranco();
    }

    /**
     * Realiza o registro do voto para um candidato ou voto branco, dependendo da validade.
     *
     * @param numero número do candidato
     * @param candidato documento do candidato (ou null)
     */
    private void registrarVoto(String numero, Document candidato) {
        if (votoValido(numero, candidato)) registrarVotoCandidato(candidato);
        else tela.visor.builder.manipuladorTelaVotoBranco("mostrar");
        tela.visor.limparCamposVoto();
    }

    /**
     * Verifica se o voto é válido.
     *
     * @param numero número digitado
     * @param candidato documento do candidato
     * @return verdadeiro se não for número de finalização e o candidato existir
     */
    private boolean votoValido(String numero, Document candidato) {
        return !numero.equals("99999") && candidato != null;
    }

    /**
     * Registra o voto para o candidato.
     *
     * @param candidato documento do candidato
     */
    private void registrarVotoCandidato(Document candidato) {
        urna.registrarVoto(candidato.getString("nome"));
        tela.visor.builder.exibirConfirmaVoto();
        avisarSistema("Controller", "candidato votado");
    }

    /**
     * Registra um voto branco no sistema.
     */
    private void registrarVotoBranco() {
        urna.registrarVoto("branco");
        avisarSistema("Controller", "Voto branco ou inválido.");
        tela.visor.builder.manipuladorTelaVotoBranco("fechar");
        tela.visor.builder.exibirConfirmaVoto();
    }

    /**
     * Finaliza o processo de votação e exibe os resultados na tela.
     */
    private void finalizarVotacao() {
        avisarSistema("Controller(finalizar())", "exibir contabilidade");
        List<Document> votosContabilizados = urna.contabilizarVotos();
        tela.visor.builder.mostrarContabilidade(votosContabilizados);
    }

    /**
     * Busca um candidato no banco de dados pelo número.
     *
     * @param numero número do candidato
     * @return documento do candidato ou null
     */
    public Document buscarCandidato(String numero) {
        avisarSistema("Controller(buscarCandidato())", numero);
        return banco.getCandidato(numero);
    }

    /**
     * Busca um partido no banco de dados pelo número.
     *
     * @param numero número do partido
     * @return documento do partido ou null
     */
    public Document buscarPartido(String numero) {
        avisarSistema("Controller(buscarPartido())", numero);
        return banco.getPartido(numero);
    }

    /**
     * Busca uma coleção completa no banco (partidos ou candidatos).
     *
     * @param nomeColecao nome da coleção
     * @return array de documentos da coleção
     */
    public Document[] buscarColecao(String nomeColecao) {
        return banco.getColecao(nomeColecao);
    }

    /**
     * Retorna todos os candidatos pertencentes a um determinado partido.
     *
     * @param partido documento do partido
     * @return array de documentos dos candidatos
     */
    public Document[] buscarCandidatosPorPartido(Document partido) {
        return banco.getCandidatosPartido(partido);
    }

    /**
     * Retorna todos os candidatos cadastrados no banco.
     *
     * @return array de documentos dos candidatos
     */
    public Document[] buscarTodosCandidatos() {
        return banco.getTodosCandidatos();
    }

    /**
     * Imprime mensagens no sistema para rastreamento e depuração.
     *
     * @param tipo identificador da origem
     * @param mensagem conteúdo da mensagem
     */
    public void avisarSistema(Object tipo, String mensagem) {
        System.out.printf("--> %s: %s.\n", tipo, mensagem);
    }
}