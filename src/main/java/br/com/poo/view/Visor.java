package br.com.poo.view;

import javax.swing.*;
import javax.swing.border.LineBorder;

import br.com.poo.controller.ControllerModel;

import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;
import java.util.List;

public class Visor extends JPanel {
    private static final int BASE_WIDTH = 700;
    private static final int BASE_HEIGHT = 400;

    ControllerModel controller;
    private final List<JComponent> componentesFixos = new ArrayList<>();
    private final List<JComponent> componentesInfo = new ArrayList<>();
    private JTextField[] camposDigito = new JTextField[2];

    public Visor() {
        setLayout(null);
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createLineBorder(Color.BLACK, 10, true));
        setPreferredSize(new Dimension(BASE_WIDTH, BASE_HEIGHT));

        iniciarTelaSimples();

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                redimensionarComponentes();
            }
        });
    }

    private void iniciarTelaSimples() {
        iniciarHeader();
        adicionarCamposNumericos(2, new Rectangle(110, 85, 120, 60));
    }

    private void iniciarHeader() {
        adicionarLabel("SEU VOTO PARA:", 17, new Rectangle(20, 10, 300, 20), false);
        adicionarLabel("Governador", 22, new Rectangle(300, 45, 300, 30), false);
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

    private void adicionarCamposNumericos(int quantidade, Rectangle bounds) {
        JPanel painelNumeros = new JPanel(null);
        painelNumeros.setBackground(Color.WHITE);
        painelNumeros.setBounds(bounds);

        int larguraCampo = 40;
        int alturaCampo = 55;
        int espacamento = 5;

        for (int i = 0; i < quantidade; i++) {
            camposDigito[i] = criarCampoNumerico(larguraCampo, alturaCampo, i * (larguraCampo + espacamento), 0);
            painelNumeros.add(camposDigito[i]);
        }

        adicionarComponente(painelNumeros, false);
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
                if (!Character.isDigit(e.getKeyChar())) {
                    e.consume();
                }
            }
        });
    }

    public void inserirDigito(String acao) {
        if (camposDigito[0].getText().isEmpty()) camposDigito[0].setText(acao);
        else if (camposDigito[1].getText().isEmpty()) {
            camposDigito[1].setText(acao);
            adicionarInfosEntidade();
        }
    }

    public void acionarBotao(String acao) {
        if (acao.equals("CORRIGE")) apagarTextoCampos();
        if (acao.equals("CONFIRMA")) System.out.println("voto confirmado");
        if (acao.equals("BRANCO")) System.out.println("voto em branco");
    }

    private void apagarTextoCampos() {
        for (JTextField campo : camposDigito) campo.setText("");
        removerInfosEntidade();
    }

    private void removerInfosEntidade() {
        for (JComponent comp : componentesInfo) {
            remove(comp);
        }
        componentesInfo.clear();

        revalidate();
        repaint();
    }

    private void adicionarInfosEntidade() {
        adicionarCamposInfo();
        adicionarCamposFoto();
        adicionarCampoAjuda();
        revalidate();
        repaint();
    }

    private void adicionarCamposInfo() {
        adicionarLabel("Número:", 16, new Rectangle(20, 100, 100, 20), true);
        adicionarLabel("Nome:", 16, new Rectangle(20, 190, 100, 20), true);
        adicionarLabel("Vôlei", 16, new Rectangle(100, 190, 200, 20), true);
        adicionarLabel("Partido:", 16, new Rectangle(20, 220, 100, 20), true);
        adicionarLabel("PEsp", 16, new Rectangle(100, 220, 100, 20), true);
        adicionarLabel("Vice-Governador:", 16, new Rectangle(20, 250, 160, 20), true);
        adicionarLabel("Tênis", 16, new Rectangle(170, 250, 200, 20), true);
        adicionarLabel("Governador", 12, new Rectangle(585, 190, 100, 20), true);
        adicionarLabel("Vice-Governador", 12, new Rectangle(568, 360, 120, 20), true);
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