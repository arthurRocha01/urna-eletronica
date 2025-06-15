package br.com.poo.view.visor;

import javax.swing.*;
import javax.swing.border.LineBorder;
import org.bson.Document;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class VisorBuilder {
    private final Visor visor;

    public VisorBuilder(Visor visor) {
        this.visor = visor;
        configurarVisor();
        construirTela();
    }

    private void configurarVisor() {
        visor.setLayout(null);
        visor.setBackground(Color.WHITE);
        visor.setBorder(BorderFactory.createLineBorder(Color.BLACK, 10, true));
        visor.setPreferredSize(new Dimension(visor.BASE_WIDTH, visor.BASE_HEIGHT));

        visor.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                redimensionarComponentes();
            }
        });
    }

    private void construirTela() {
        adicionarLabel("SEU VOTO PARA:", 17, new Rectangle(20, 10, 300, 20), false);
        adicionarLabel("Governador", 22, new Rectangle(300, 45, 300, 30), false);
        adicionarCamposNumericos(5);
    }

    private void adicionarCamposNumericos(int quantidade) {
        int largura = 40, altura = 55, espacamento = 5;
        int total = quantidade * largura + (quantidade - 1) * espacamento;
        int x = (visor.BASE_WIDTH - total) / 4;

        JPanel painel = new JPanel(null);
        painel.setBackground(Color.WHITE);
        painel.setBounds(x, 85, total, altura);

        for (int i = 0; i < quantidade; i++) {
            JTextField campo = criarCampoNumerico(i * (largura + espacamento), 0, largura, altura);
            visor.camposDigito[i] = campo;
            painel.add(campo);
        }

        adicionarComponente(painel, false);
    }

    private JTextField criarCampoNumerico(int x, int y, int largura, int altura) {
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

    public void adicionarInfosEntidade(Document[] info) {
        removerInfosEntidade();

        Document candidato = info[0];
        Document partido = info[1];

        adicionarLabel("Número:", 18, new Rectangle(25, 95, 300, 20), false);
        adicionarLabelInfo("Nome:", candidato.getString("nome"), new Rectangle(20, 190, 300, 20));
        adicionarLabelInfo("Partido:", partido.getString("sigla"), new Rectangle(20, 220, 300, 20));
        adicionarLabelInfo("Vice-Governador:", "-", new Rectangle(20, 250, 300, 20));

        adicionarLabel("Governador", 12, new Rectangle(585, 190, 100, 20), true);
        adicionarLabel("Vice-Governador", 12, new Rectangle(568, 360, 120, 20), true);

        adicionarFoto(new Rectangle(570, 85, 100, 100));
        adicionarFoto(new Rectangle(570, 260, 100, 100));

        adicionarLabel(
            "<html><div style='letter-spacing: 1.5px;'><hr style='border: 1px solid black;'><br><br>"
                    + "<div>Aperte a tecla:</div>"
                    + "<div><b>CONFIRMA</b> para CONFIRMAR este voto</div>"
                    + "<div><b>CORRIGE</b> para REINICIAR este voto</div></div></html>",
            15, new Rectangle(20, 285, 500, 100), true
        );

        visor.revalidate();
        visor.repaint();
    }

    public void removerInfosEntidade() {
        visor.componentesInfo.forEach(visor::remove);
        visor.componentesInfo.clear();
    }

    private void adicionarLabelInfo(String titulo, String valor, Rectangle bounds) {
        adicionarLabel(titulo, 16, bounds, true);
        Rectangle valorBounds = new Rectangle(bounds.x + (titulo.equals("Vice-Governador") ? 200 : 100), bounds.y, 200, bounds.height);
        adicionarLabel(valor, 16, valorBounds, true);
    }

    private void adicionarFoto(Rectangle bounds) {
        JPanel foto = new JPanel();
        foto.setBackground(new Color(230, 230, 230));
        foto.setBorder(new LineBorder(Color.BLACK, 1));
        foto.setBounds(bounds);
        adicionarComponente(foto, true);
    }

    private void adicionarLabel(String texto, int tamanho, Rectangle bounds, boolean ehInfo) {
        JLabel label = new JLabel(texto);
        label.setFont(new Font("Arial", Font.PLAIN, tamanho));
        label.setBounds(bounds);
        adicionarComponente(label, ehInfo);
    }

    private void adicionarComponente(JComponent comp, boolean ehInfo) {
        (ehInfo ? visor.componentesInfo : visor.componentesFixos).add(comp);
        visor.add(comp);
    }

    private void redimensionarComponentes() {
        double escalaX = visor.getWidth() / (double) visor.BASE_WIDTH;
        double escalaY = visor.getHeight() / (double) visor.BASE_HEIGHT;

        visor.componentesFixos.forEach(c -> redimensionar(c, escalaX, escalaY));
        visor.componentesInfo.forEach(c -> redimensionar(c, escalaX, escalaY));

        visor.repaint();
    }

    private void redimensionar(JComponent comp, double escalaX, double escalaY) {
        Rectangle bounds = comp.getBounds();
        comp.setBounds(
                (int) (bounds.x * escalaX),
                (int) (bounds.y * escalaY),
                (int) (bounds.width * escalaX),
                (int) (bounds.height * escalaY)
        );
        Font font = comp.getFont();
        if (font != null)
            comp.setFont(font.deriveFont((float) (font.getSize2D() * Math.min(escalaX, escalaY))));
    }
}