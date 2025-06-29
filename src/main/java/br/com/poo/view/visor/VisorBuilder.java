package br.com.poo.view.visor;

import java.util.List;

import javax.swing.*;
import javax.swing.border.LineBorder;
import org.bson.Document;
import java.awt.*;
import java.awt.event.*;

public class VisorBuilder {

    private static final String CAMINHO_IMAGENS = "src/main/resources/images/";

    private final Visor visor;
    public final TelaConfirmaCandidato telaConfirmaCandidato;
    public final TelaVotoBranco telaConfirmaBranco;
    public final TelaContabilidade telaContabilidade;

    private JLabel lblNumero;
    private JLabel lblNomeTitulo;
    private JLabel lblNomeValor;
    private JLabel lblPartidoTitulo;
    private JLabel lblPartidoValor;
    private JLabel lblInstrucao;
    private JLabel lblFotoTitulo;
    private JLabel lblFoto;
    private JPanel painelFoto;

    public VisorBuilder(Visor visor) {
        this.visor = visor;
        this.telaConfirmaCandidato = new TelaConfirmaCandidato(visor, this);
        this.telaConfirmaBranco = new TelaVotoBranco(visor, this);
        telaContabilidade = new TelaContabilidade(visor);
    }

    public void iniciarTela() {
        configurarVisor();
        montarInterface();
    }

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

    private void montarInterface() {
        adicionarLabel("SEU VOTO PARA:", 17, new Rectangle(20, 10, 300, 20));
        adicionarLabel("Governador", 22, new Rectangle(300, 45, 300, 30));
        adicionarCamposNumericos(5);

        lblNumero = criarLabel("", 18, new Rectangle(25, 95, 300, 20));
        lblNomeTitulo = criarLabel("", 16, new Rectangle(20, 190, 100, 20));
        lblNomeValor = criarLabel("", 16, new Rectangle(120, 190, 200, 20));
        lblPartidoTitulo = criarLabel("", 16, new Rectangle(20, 220, 100, 20));
        lblPartidoValor = criarLabel("", 16, new Rectangle(120, 220, 200, 20));
        lblInstrucao = criarLabel("", 15, new Rectangle(20, 285, 500, 100));
        lblInstrucao.setVerticalAlignment(SwingConstants.TOP);
        lblFotoTitulo = criarLabel("", 12, new Rectangle(585, 190, 100, 20));

        criarPainelFoto();
    }

    private void criarPainelFoto() {
        lblFoto = new JLabel();
        painelFoto = new JPanel(new BorderLayout());
        painelFoto.setBackground(new Color(230, 230, 230));
        painelFoto.setBorder(new LineBorder(Color.BLACK, 1));
        painelFoto.setBounds(570, 85, 100, 100);
        painelFoto.add(lblFoto, BorderLayout.CENTER);

        visor.add(painelFoto);
        painelFoto.setVisible(false);
    }

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

    private JLabel criarLabel(String texto, int tamanhoFonte, Rectangle bounds) {
        JLabel label = new JLabel(texto);
        label.setFont(new Font("Arial", Font.PLAIN, tamanhoFonte));
        label.setBounds(bounds);
        visor.add(label);
        return label;
    }

    private void adicionarLabel(String texto, int tamanhoFonte, Rectangle bounds) {
        JLabel label = new JLabel(texto);
        label.setFont(new Font("Arial", Font.PLAIN, tamanhoFonte));
        label.setBounds(bounds);
        visor.add(label);
    }

    public void mostrarInformacoesPartido(Document partido) {
        String sigla = partido.getString("sigla");

        lblNumero.setText("Número:");
        lblPartidoTitulo.setText("Partido");
        lblPartidoValor.setText(sigla);

        lblInstrucao.setText("<html><div style='letter-spacing:1.5px;'>"
            + "<hr style='border:1px solid black;'><br><br>"
            + "<div>Aperte a tecla:</div><div><b>CONFIRMA</b> para CONFIRMAR este voto</div>"
            + "<div><b>CORRIGE</b> para REINICIAR este voto</div></div></html>");
    }

    public void mostrarInformacoesCandidato(Document candidato) {
        String nome = candidato.getString("nome");

        lblNomeTitulo.setText("Nome:");
        lblNomeValor.setText(nome);

        atualizarFoto(lblPartidoValor.getText(), nome);
        lblFotoTitulo.setText("Governante");
    }

    public void limparInformacoesCandidato() {
        lblNomeTitulo.setText("");
        lblNomeValor.setText("");

        lblPartidoTitulo.setText("");
        lblPartidoValor.setText("");

        lblInstrucao.setText("");
        lblFoto.setVisible(false);
    }

    private void atualizarFoto(String sigla, String nome) {
        String caminho = CAMINHO_IMAGENS + sigla + "/" + nome.toLowerCase() + ".png";
        Image imagem = new ImageIcon(caminho).getImage().getScaledInstance(
            painelFoto.getWidth(), painelFoto.getHeight(), Image.SCALE_SMOOTH);
        lblFoto.setIcon(new ImageIcon(imagem));
        painelFoto.setVisible(true);
    }

    public void exibirTelaVoto() {
        telaContabilidade.fechar();
    }

    public void mostrarContabilidade(List<Document> votosContabilizados) {
        telaContabilidade.exibir(votosContabilizados);
    }

    public void exibirConfirmaVoto() {
        telaConfirmaBranco.fechar();
        telaConfirmaCandidato.exibirTela();
        desbloquearVisorComDelay();
    }

    public void manipuladorTelaVotoBranco(String acao) {
        if (acao.equals("mostrar")) telaConfirmaBranco.exibir();
        if (acao.equals("fechar")) telaConfirmaBranco.fechar();
    }

    private void desbloquearVisorComDelay() {
        Timer timer = new Timer(3500, e -> {
            visor.tecladoBloqueado = false;
            visor.botoesBloqueados = false;
            visor.desbloquearBotoes();
        });
        timer.setRepeats(false);
        timer.start();
    }

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
}