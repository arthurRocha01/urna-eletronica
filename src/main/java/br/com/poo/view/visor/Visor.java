package br.com.poo.view.visor;

import javax.swing.*;
import javax.swing.border.LineBorder;
import org.bson.Document;
import br.com.poo.controller.ControllerModel;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;
import java.util.List;

public class Visor extends JPanel {
    private static final int BASE_WIDTH = 700;
    private static final int BASE_HEIGHT = 400;

    private ControllerModel controller;
    private final List<JComponent> componentesFixos = new ArrayList<>();
    private final List<JComponent> componentesInfo = new ArrayList<>();
    private JTextField[] camposDigito = new JTextField[5];

    public Visor() {
        setLayout(null);
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createLineBorder(Color.BLACK, 10, true));
        setPreferredSize(new Dimension(BASE_WIDTH, BASE_HEIGHT));
        iniciarTela();
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                redimensionarComponentes();
            }
        });
    }

    private void iniciarTela() {
        adicionarLabel("SEU VOTO PARA:", 17, new Rectangle(20, 10, 300, 20), false);
        adicionarLabel("Governador", 22, new Rectangle(300, 45, 300, 30), false);
        adicionarCamposNumericos(5);
    }

    private void adicionarCamposNumericos(int quantidade) {
        int larguraCampo = 40, alturaCampo = 55, espacamento = 5;
        int larguraTotal = quantidade * larguraCampo + (quantidade - 1) * espacamento;
        int xInicial = (BASE_WIDTH - larguraTotal) / 5;

        JPanel painel = new JPanel(null);
        painel.setBackground(Color.WHITE);
        painel.setBounds(xInicial, 85, larguraTotal, alturaCampo);

        for (int i = 0; i < quantidade; i++) {
            camposDigito[i] = criarCampoNumerico(larguraCampo, alturaCampo, i * (larguraCampo + espacamento), 0);
            painel.add(camposDigito[i]);
        }

        adicionarComponente(painel, false);
    }

    private JTextField criarCampoNumerico(int largura, int altura, int x, int y) {
        JTextField campo = new JTextField();
        campo.setFont(new Font("Arial", Font.BOLD, 28));
        campo.setHorizontalAlignment(JTextField.CENTER);
        campo.setBackground(Color.WHITE);
        campo.setBorder(new LineBorder(Color.BLACK, 2));
        campo.setFocusable(false);
        campo.setBounds(x, y, largura, altura);
        bloquearNaoNumeros(campo);
        return campo;
    }

    private void bloquearNaoNumeros(JTextField campo) {
        campo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent e) {
                if (!Character.isDigit(e.getKeyChar())) e.consume();
            }
        });
    }

    public void inserirDigito(String acao) {
        for (JTextField campo : camposDigito) {
            if (campo.getText().isEmpty()) {
                campo.setText(acao);
                break;
            }
        }
        if (votoCompleto()) acionarVoto();
    }

    private boolean votoCompleto() {
        for (JTextField campo : camposDigito) {
            if (campo.getText().isEmpty()) return false;
        }
        return true;
    }

    private void acionarVoto() {
        String voto = getVoto();
        Document[] info = controller.buscarInformacoesVoto(voto);
        if (info != null) adicionarInfosEntidade(info);
    }

    private String getVoto() {
        StringBuilder numero = new StringBuilder();
        for (JTextField campo : camposDigito) numero.append(campo.getText());
        return numero.toString();
    }

    public void acionarBotao(String acao) {
        switch (acao) {
            case "CORRIGE" -> apagarTextoCampos();
            case "CONFIRMA" -> System.out.println("Voto confirmado");
            case "BRANCO" -> System.out.println("Voto em branco");
        }
    }

    private void apagarTextoCampos() {
        for (JTextField campo : camposDigito) campo.setText("");
        removerInfosEntidade();
    }

    private void removerInfosEntidade() {
        for (JComponent comp : componentesInfo) remove(comp);
        componentesInfo.clear();
        revalidate();
        repaint();
    }

    private void adicionarInfosEntidade(Document[] info) {
        removerInfosEntidade();
        adicionarCamposInfo(info);
        adicionarCamposFoto();
        adicionarCampoAjuda();
        revalidate();
        repaint();
    }

    private void adicionarCamposInfo(Document[] info) {
        Document candidato = info[0];
        Document partido = info[1];

        adicionarLabelInfo("Número:", candidato.getString("numero"), new Rectangle(20, 160, 300, 20));
        adicionarLabelInfo("Nome:", candidato.getString("nome"), new Rectangle(20, 190, 300, 20));
        adicionarLabelInfo("Partido:", partido.getString("sigla"), new Rectangle(20, 220, 300, 20));

        adicionarLabelInfo("Vice-Governador:", "NOME DO VICE", new Rectangle(20, 250, 300, 20));

        adicionarLabel("Governador", 12, new Rectangle(585, 190, 100, 20), true);
        adicionarLabel("Vice-Governador", 12, new Rectangle(568, 360, 120, 20), true);
    }

    private void adicionarLabelInfo(String titulo, String valor, Rectangle bounds) {
        adicionarLabel(titulo, 16, bounds, true);
        Rectangle valorBounds = new Rectangle(bounds.x + 100, bounds.y, 200, bounds.height);
        adicionarLabel(valor, 16, valorBounds, true);
    }

    private void adicionarCamposFoto() {
        adicionarFoto(new Rectangle(570, 85, 100, 100));
        adicionarFoto(new Rectangle(570, 260, 100, 100));
    }

    private void adicionarFoto(Rectangle bounds) {
        JPanel foto = new JPanel();
        foto.setBackground(new Color(230, 230, 230));
        foto.setBorder(new LineBorder(Color.BLACK, 1));
        foto.setBounds(bounds);
        adicionarComponente(foto, true);
    }

    private void adicionarCampoAjuda() {
        adicionarLabel(
            "<html><div style='letter-spacing: 1.5px;'><hr style='border: 1px solid black;'><br><br>"
                    + "<div>Aperte a tecla:</div>"
                    + "<div><b>CONFIRMA</b> para CONFIRMAR este voto</div>"
                    + "<div><b>CORRIGE</b> para REINICIAR este voto</div></div></html>",
            15, new Rectangle(20, 285, 500, 100), true
        );
    }

    private void adicionarLabel(String texto, int tamanhoFonte, Rectangle bounds, boolean ehInfo) {
        JLabel label = new JLabel(texto);
        label.setFont(new Font("Arial", Font.PLAIN, tamanhoFonte));
        label.setBounds(bounds);
        adicionarComponente(label, ehInfo);
    }

    private void adicionarComponente(JComponent comp, boolean ehInfo) {
        if (ehInfo) componentesInfo.add(comp);
        else componentesFixos.add(comp);
        add(comp);
    }

    private void redimensionarComponentes() {
        double escalaX = getWidth() / (double) BASE_WIDTH;
        double escalaY = getHeight() / (double) BASE_HEIGHT;

        for (JComponent comp : componentesFixos) redimensionar(comp, escalaX, escalaY);
        for (JComponent comp : componentesInfo) redimensionar(comp, escalaX, escalaY);

        repaint();
    }

    private void redimensionar(JComponent comp, double escalaX, double escalaY) {
        Rectangle original = comp.getBounds();
        int x = (int) (original.x * escalaX);
        int y = (int) (original.y * escalaY);
        int largura = (int) (original.width * escalaX);
        int altura = (int) (original.height * escalaY);
        comp.setBounds(x, y, largura, altura);

        Font fonte = comp.getFont();
        if (fonte != null) {
            float novoTamanho = (float) (fonte.getSize2D() * Math.min(escalaX, escalaY));
            comp.setFont(fonte.deriveFont(novoTamanho));
        }
    }

    public void setController(ControllerModel controller) {
        this.controller = controller;
    }
}