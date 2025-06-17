package br.com.poo.view.visor;

import javax.swing.*;
import javax.swing.border.LineBorder;
import org.bson.Document;
import java.awt.*;
import java.awt.event.*;

public class VisorBuilder {
    private String caminhoImagens = "src/main/resources/images/";
    private Visor visor;
    TelaAnimadaVoto telaAnimadaVoto;

    public VisorBuilder(Visor visor) {
        this.visor = visor;
        telaAnimadaVoto = new TelaAnimadaVoto(visor, this);
    }
    
    public void iniciarTela() {
        configurarVisor();
        construirTela();
    }

    private void configurarVisor() {
        visor.setLayout(null);
        visor.setBackground(Color.WHITE);
        visor.setBorder(BorderFactory.createLineBorder(Color.BLACK, 10, true));
        visor.setPreferredSize(new Dimension(visor.BASE_WIDTH, visor.BASE_HEIGHT));
        visor.addComponentListener(new ComponentAdapter() {
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

    private void adicionarCamposNumericos(int qtd) {
        int largura = 40, altura = 55, espacamento = 5;
        int total = qtd * (largura + espacamento) - espacamento;
        int x = (visor.BASE_WIDTH - total) / 4;

        JPanel painel = new JPanel(null);
        painel.setBackground(Color.WHITE);
        painel.setBounds(x, 85, total, altura);

        for (int i = 0; i < qtd; i++) {
            JTextField campo = new JTextField();
            campo.setFont(new Font("Arial", Font.BOLD, 28));
            campo.setHorizontalAlignment(JTextField.CENTER);
            campo.setBorder(new LineBorder(Color.BLACK, 2));
            campo.setFocusable(false);
            campo.setBounds(i * (largura + espacamento), 0, largura, altura);
            visor.camposDigito[i] = campo;
            painel.add(campo);
        }
        adicionarComponente(painel, false);
    }

    public void adicionarInfosEntidade(Document[] info) {
        removerInfosEntidade();
        Document candidato = info[0], partido = info[1];
        String nomeCandidato = candidato.getString("nome"), nomePartido = partido.getString("nome"),
        siglaPartido = partido.getString("sigla");

        adicionarLabel("Número:", 18, new Rectangle(25, 95, 300, 20), false);
        adicionarLabelInfo("Nome:", nomeCandidato, new Rectangle(20, 190, 300, 20));
        adicionarLabelInfo("Partido:", siglaPartido, new Rectangle(20, 220, 300, 20));
        adicionarFoto(siglaPartido, nomeCandidato, new Rectangle(570, 85, 100, 100));
        adicionarLabel("<html><div style='letter-spacing:1.5px;'><hr style='border:1px solid black;'><br><br>"
                + "<div>Aperte a tecla:</div><div><b>CONFIRMA</b> para CONFIRMAR este voto</div>"
                + "<div><b>CORRIGE</b> para REINICIAR este voto</div></div></html>", 15,
                new Rectangle(20, 285, 500, 100), true);
        visor.revalidate();
        visor.repaint();
    }

    private void removerInfosEntidade() {
        visor.componentesInfo.forEach(visor::remove);
        visor.componentesInfo.clear();
    }

    private void adicionarLabelInfo(String titulo, String valor, Rectangle bounds) {
        adicionarLabel(titulo, 16, bounds, true);
        Rectangle valBounds = new Rectangle(bounds.x + 100, bounds.y, 200, bounds.height);
        adicionarLabel(valor, 16, valBounds, true);
    }

    private void adicionarFoto(String sigla, String nome, Rectangle bounds) {
        String caminho = caminhoImagens + sigla + "/" + nome.toLowerCase() + ".png";
        ImageIcon img = new ImageIcon(new ImageIcon(caminho).getImage()
                .getScaledInstance(bounds.width, bounds.height, Image.SCALE_SMOOTH));
        adicionarLabel("Governador", 12, new Rectangle(585, 190, 100, 20), true);

        JLabel imgLabel = new JLabel(img, JLabel.CENTER);
        JPanel foto = new JPanel(new BorderLayout());
        foto.setBackground(new Color(230, 230, 230));
        foto.setBorder(new LineBorder(Color.BLACK, 1));
        foto.setBounds(bounds);
        foto.add(imgLabel, BorderLayout.CENTER);
        adicionarComponente(foto, true);
    }

    private void adicionarLabel(String texto, int tamanho, Rectangle bounds, boolean ehInfo) {
        JLabel label = new JLabel(texto);
        label.setFont(new Font("Arial", Font.PLAIN, tamanho));
        label.setBounds(bounds);
        adicionarComponente(label, ehInfo);
    }

    public void apagarTextoCampos() {
        for (JTextField campo : visor.camposDigito) campo.setText("");
        removerInfosEntidade();
    }

    public void exibirConfirmaVoto() {
    // Remove todos os componentes do visor
    visor.removeAll();
    visor.componentesFixos.clear();
    visor.componentesInfo.clear();

    // Muda o layout para centralizar a animação
    visor.setLayout(new BorderLayout());
    visor.add(telaAnimadaVoto, BorderLayout.CENTER);

    telaAnimadaVoto.iniciarAnimacao();

    visor.revalidate();
    visor.repaint();
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

    private void redimensionar(JComponent comp, double sx, double sy) {
        Rectangle b = comp.getBounds();
        comp.setBounds((int)(b.x * sx), (int)(b.y * sy), (int)(b.width * sx), (int)(b.height * sy));
        Font f = comp.getFont();
        if (f != null) comp.setFont(f.deriveFont((float)(f.getSize2D() * Math.min(sx, sy))));
    }
}