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

    /**
     * Inicia o banco de dados carrega os dados e fecha a conexão.
     */
    private void iniciarDatabase() {
        banco.conectar();
        banco.carregarDados();
        banco.fecharConexao();
    }

    /**
     * Executa uma ação com base no comando recebido da interface.
     *
     * @param comando texto do botão pressionado
     */
    public void executarAcao(String comando) {
        if (isInserindoVoto(comando)) {
            tela.visor.inserirDigito(comando);
        } else if (!tela.visor.botoesBloqueados || isVotandoBranco() || isReiniciar()) {
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

    /**
     * Processa os comandos "CORRIGE", "CONFIRMA" ou "BRANCO".
     *
     * @param comando comando a ser processado
     */
    private void processarComando(String comando) {
        switch (comando) {
            case "CORRIGE" -> corrigir();
            case "CONFIRMA" -> confirmarVoto();
            case "BRANCO" -> mostrarConfirmacaoVotoBranco();
            default -> {}
        }
    }

    /**
     * Corrige o voto branco se estiver votando branco e limpa o campo de dígito.
     */
    private void corrigir() {
        if (isVotandoBranco()) tela.visor.builder.exibirTelaVoto();
        tela.visor.limparCamposVoto();
    }

    /**
     * Confirma o voto digitado e executa a ação apropriada (registrar, branco, finalizar ou reinicirar).
     */
    private void confirmarVoto() {
        String voto = tela.visor.getVotoInserido();
        Document candidato = buscarCandidato(voto);
        
        if (isReiniciar()) tela.visor.builder.exibirTelaVoto();
        else if (isVotando(voto)) registrarVoto(voto, candidato);
        else if (isVotandoBranco()) registrarVotoBranco();
        else if (isFinalizando(voto)) finalizarVotacao();
    }

    /**
     * Verifica se o caso atual é de reiniciar.
     */
    private boolean isReiniciar() {
        return tela.visor.isTelaRelatorio();
    }
    
    /**
     * Verifica se o caso atual é de votação.
     */
    private boolean isVotando(String voto) {
        return tela.visor.isVotoCompleto() && tela.visor.isTelaVoto() && !voto.equals("99999");
    }

    /**
     * Realiza o registro do voto para um candidato ou voto branco, dependendo da validade.
     *
     * @param numero número do candidato
     * @param candidato documento do candidato (ou null)
     */
    private void registrarVoto(String numero, Document candidato) {
        if (votoValido(numero, candidato)) registrarVotoCandidato(candidato);
        else mostrarConfirmacaoVotoBranco();
        tela.visor.limparCamposVoto();
    }

    /**
     * Verifica se o caso atual é de confirmar o voto branco.
     */
    private boolean isConfimandoVotoBranco() {
        return !isVotandoBranco() && !tela.visor.isTelaGravando();
    }

    /**
     * Exibe a confirmação de voto branco se for o caso adequado.
     */
    private void mostrarConfirmacaoVotoBranco() {
        if (isConfimandoVotoBranco()) tela.visor.builder.manipuladorTelaVotoBranco("mostrar");
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
    }

    /**
     * Verifica se o caso atual é de voto branco.
     */
    private boolean isVotandoBranco() {
        return tela.visor.isTelaBranco();
    }

    /**
     * Registra um voto branco no sistema.
     */
    private void registrarVotoBranco() {
        urna.registrarVoto("branco");
        tela.visor.builder.manipuladorTelaVotoBranco("fechar");
        tela.visor.builder.exibirConfirmaVoto();
    }

    private boolean isFinalizando(String voto) {
        return tela.visor.isTelaVoto() && tela.visor.isVotoCompleto() && voto.equals("99999");
    }
    
    /**
     * Finaliza o processo de votação e exibe os resultados na tela.
     */
    private void finalizarVotacao() {
        List<Document> votosContabilizados = urna.contabilizarVotos();
        tela.visor.builder.exibirRelatorio(votosContabilizados);
    }

    /**
     * Busca um candidato no banco de dados pelo número.
     *
     * @param numero número do candidato
     * @return documento do candidato ou null
     */
    public Document buscarCandidato(String numero) {
        return banco.getCandidato(numero);
    }

    /**
     * Busca um partido no banco de dados pelo número.
     *
     * @param numero número do partido
     * @return documento do partido ou null
     */
    public Document buscarPartido(String numero) {
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