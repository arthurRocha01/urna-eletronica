package br.com.poo.view.visor;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.border.LineBorder;

import org.bson.Document;

/**
 * Classe responsável por construir e gerenciar os componentes visuais do visor da urna eletrônica,
 * incluindo as telas de confirmação, voto em branco, e contabilidade dos votos.
 * 
 * Permite a montagem da interface, atualização dos dados exibidos, 
 * e controle da aparência responsiva.
 * 
 * @author Arthur Rocha
 * @version 1.0
 * @since 1.0
 */
public class VisorBuilder {

    private static final String CAMINHO_IMAGENS = "src/main/resources/images/";

    private Visor visor;
    /** Tela de confirmação do voto em candidato */
    public TelaConfirmaCandidato telaConfirmaCandidato;
    /** Tela de confirmação do voto em branco */
    public TelaVotoBranco telaConfirmaBranco;
    /** Tela de contabilidade dos votos */
    public TelaContabilidade telaContabilidade;

    private JLabel labelNumero;
    private JLabel labelNomeTitulo;
    private JLabel labelNomeValor;
    private JLabel labelPartidoTitulo;
    private JLabel labelPartidoValor;
    private JLabel labelInstrucao;
    private JLabel labelFotoTitulo;
    private JLabel labelFoto;
    private JPanel painelFoto;

    /**
     * Construtor que recebe o visor principal e inicializa as telas auxiliares.
     * 
     * @param visor Instância do painel Visor onde os componentes serão exibidos
     */
    public VisorBuilder(Visor visor) {
        this.visor = visor;
        this.telaConfirmaCandidato = new TelaConfirmaCandidato(visor, this);
        this.telaConfirmaBranco = new TelaVotoBranco(visor, this);
        telaContabilidade = new TelaContabilidade(visor);
    }

    /**
     * Inicia a configuração da interface do visor, definindo layout,
     * adicionando bordas e construindo os componentes iniciais.
     */
    public void iniciarTela() {
        configurarVisor();
        montarInterface();
    }

    /**
     * Configura o layout, cor de fundo, borda e o listener para redimensionamento
     * do visor principal.
     */
    private void configurarVisor() {
        visor.setLayout(null);
        visor.setBackground(Color.WHITE);
        visor.setBorder(BorderFactory.createLineBorder(Color.BLACK, 10, true));
        visor.setPreferredSize(new Dimension(visor.LARGURA, visor.ALTURA));

        visor.addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
                redimensionarComponentes();
            }
        });
    }

    /**
     * Redimensiona todos os componentes dentro do visor para manter
     * uma aparência responsiva ao redimensionar a janela.
     */
    private void redimensionarComponentes() {
        double escalaX = visor.getWidth() / (double) visor.LARGURA;
        double escalaY = visor.getHeight() / (double) visor.ALTURA;

        for (Component comp : visor.getComponents()) {
            if (comp instanceof JComponent) {
                redimensionarComponente((JComponent) comp, escalaX, escalaY);
            }
        }

        visor.repaint();
    }

    /**
     * Redimensiona um componente individualmente ajustando sua posição,
     * tamanho e tamanho da fonte conforme os fatores de escala.
     * 
     * @param componente Componente a ser redimensionado
     * @param escalaX Fator de escala horizontal
     * @param escalaY Fator de escala vertical
     */
    private void redimensionarComponente(JComponent componente, double escalaX, double escalaY) {
        Rectangle b = componente.getBounds();
        componente.setBounds(
            (int)(b.x * escalaX),
            (int)(b.y * escalaY),
            (int)(b.width * escalaX),
            (int)(b.height * escalaY)
        );
        Font fonte = componente.getFont();
        if (fonte != null) {
            componente.setFont(fonte.deriveFont((float)(fonte.getSize2D() * Math.min(escalaX, escalaY))));
        }
    }

    /**
     * Monta os componentes básicos da interface, incluindo títulos, campos numéricos
     * e áreas para exibição de informações.
     */
    private void montarInterface() {
        adicionarLabel("SEU VOTO PARA:", 17, new Rectangle(20, 10, 300, 20));
        adicionarLabel("Governador", 22, new Rectangle(300, 45, 300, 30));
        adicionarCamposNumericos(5);

        labelNumero = criarLabel("", 18, new Rectangle(25, 95, 300, 20));
        labelNomeTitulo = criarLabel("", 16, new Rectangle(20, 190, 100, 20));
        labelNomeValor = criarLabel("", 16, new Rectangle(120, 190, 200, 20));
        labelPartidoTitulo = criarLabel("", 16, new Rectangle(20, 220, 100, 20));
        labelPartidoValor = criarLabel("", 16, new Rectangle(120, 220, 200, 20));
        labelInstrucao = criarLabel("", 15, new Rectangle(20, 285, 500, 100));
        labelInstrucao.setVerticalAlignment(SwingConstants.TOP);
        labelFotoTitulo = criarLabel("", 12, new Rectangle(585, 190, 100, 20));

        criarPainelFoto();
    }

        /**
     * Adiciona um JLabel ao visor sem retornar.
     * 
     * @param texto Texto do label
     * @param tamanhoFonte Tamanho da fonte
     * @param bounds Posição e tamanho do componente
     */
    private void adicionarLabel(String texto, int tamanhoFonte, Rectangle bounds) {
        JLabel label = new JLabel(texto);
        label.setFont(new Font("Arial", Font.PLAIN, tamanhoFonte));
        label.setBounds(bounds);
        visor.add(label);
    }

        /**
     * Adiciona os campos numéricos para entrada do voto.
     * 
     * @param quantidade Número de campos numéricos a serem criados
     */
    private void adicionarCamposNumericos(int quantidade) {
        int largura = 40, altura = 55, espacamento = 5;
        int total = quantidade * (largura + espacamento) - espacamento;
        int x = (visor.LARGURA - total) / 4;

        JPanel painel = new JPanel(null);
        painel.setBackground(Color.WHITE);
        painel.setBounds(x, 85, total, altura);

        for (int i = 0; i < quantidade; i++) {
            JTextField campo = new JTextField();
            campo.setFont(new Font("Arial", Font.BOLD, 28));
            campo.setHorizontalAlignment(JTextField.CENTER);
            campo.setBorder(new LineBorder(Color.BLACK, 2));
            campo.setFocusable(false);
            campo.setBounds(i * (largura + espacamento), 0, largura, altura);
            visor.camposNumero[i] = campo;
            painel.add(campo);
        }

        visor.add(painel);
    }

            /**
     * Cria um JLabel e adiciona ao visor.
     * 
     * @param texto Texto do label
     * @param tamanhoFonte Tamanho da fonte
     * @param bounds Posição e tamanho do componente
     * @return JLabel criado
     */
    private JLabel criarLabel(String texto, int tamanhoFonte, Rectangle bounds) {
        JLabel label = new JLabel(texto);
        label.setFont(new Font("Arial", Font.PLAIN, tamanhoFonte));
        label.setBounds(bounds);
        visor.add(label);
        return label;
    }

    /**
     * Cria o painel onde será exibida a foto do candidato.
     */
    private void criarPainelFoto() {
        labelFoto = new JLabel();
        painelFoto = new JPanel(new BorderLayout());
        painelFoto.setBackground(new Color(230, 230, 230));
        painelFoto.setBorder(new LineBorder(Color.BLACK, 1));
        painelFoto.setBounds(570, 85, 100, 100);
        painelFoto.add(labelFoto, BorderLayout.CENTER);

        visor.add(painelFoto);
        painelFoto.setVisible(false);
    }

    /**
     * Atualiza os campos de informações para exibir dados do partido.
     * 
     * @param partido Documento contendo informações do partido
     */
    public void mostrarInformacoesPartido(Document partido) {
        String sigla = partido.getString("sigla");

        labelNumero.setText("Número:");
        labelPartidoTitulo.setText("Partido");
        labelPartidoValor.setText(sigla);

        labelInstrucao.setText("<html><div style='letter-spacing:1.5px;'>"
            + "<hr style='border:1px solid black;'><br><br>"
            + "<div>Aperte a tecla:</div><div><b>CONFIRMA</b> para CONFIRMAR este voto</div>"
            + "<div><b>CORRIGE</b> para REINICIAR este voto</div></div></html>");
    }

    /**
     * Atualiza os campos de informações para exibir dados do candidato,
     * incluindo a foto, caso disponível.
     * 
     * @param candidato Documento contendo informações do candidato
     */
    public void mostrarInformacoesCandidato(Document candidato) {
        String nome = candidato.getString("nome");

        labelNomeTitulo.setText("Nome:");
        labelNomeValor.setText(nome);

        atualizarFoto(labelPartidoValor.getText(), nome);
        labelFotoTitulo.setText("Governante");
    }

        /**
     * Atualiza a foto do candidato exibida no painel de foto.
     * 
     * @param sigla Sigla do partido
     * @param nome Nome do candidato
     */
    private void atualizarFoto(String sigla, String nome) {
        String caminho = CAMINHO_IMAGENS + sigla + "/" + nome.toLowerCase() + ".png";
        Image imagem = new ImageIcon(caminho).getImage().getScaledInstance(
            painelFoto.getWidth(), painelFoto.getHeight(), Image.SCALE_SMOOTH);
        labelFoto.setIcon(new ImageIcon(imagem));
        painelFoto.setVisible(true);
    }

    /**
     * Limpa as informações exibidas sobre candidato e partido, removendo foto e textos.
     */
    public void limparInformacoesCandidato() {
        labelNumero.setText("");

        labelNomeTitulo.setText("");
        labelNomeValor.setText("");

        labelPartidoTitulo.setText("");
        labelPartidoValor.setText("");

        painelFoto.setVisible(false);
        labelFotoTitulo.setText("");
        
        labelInstrucao.setText("");
    }

    /**
     * Fecha a tela de contabilidade e fecha tela de confirmação do voto branco 
     * caso estiver sendo exiida, voltando para tela anterior.
     */
    public void exibirTelaVoto() {
        if (telaConfirmaBranco.isShowing()) manipuladorTelaVotoBranco("fechar");
        telaContabilidade.fechar();
    }

    /**
     * Exibe a tela de confirmação do voto e libera o visor após um atraso.
     */
    public void exibirConfirmaVoto() {
        telaConfirmaBranco.fechar();
        telaConfirmaCandidato.mostrar();
        desbloquearVisor();
    }

        /**
     * Manipula a exibição da tela de voto branco conforme a ação recebida.
     * 
     * @param acao "mostrar" para exibir, "fechar" para fechar a tela
     */
    public void manipuladorTelaVotoBranco(String acao) {
        if (acao.equals("mostrar")) telaConfirmaBranco.mostrar();
        if (acao.equals("fechar")) telaConfirmaBranco.fechar();
    }

    /**
     * Exibe a tela de contabilidade dos votos com os dados fornecidos.
     * 
     * @param votosContabilizados Lista de documentos contendo votos contabilizados
     */
    public void exibirRelatorio(List<Document> votosContabilizados) {
        telaContabilidade.exibir(votosContabilizados);
    }

    /**
     * Desbloqueia o teclado e botões do visor após um delay configurado.
     */
    private void desbloquearVisor() {
        Timer timer = new Timer(2500, e -> {
            visor.desbloquearTeclado();
            visor.desbloquearBotoes();
        });
        timer.setRepeats(false);
        timer.start();
    }
}