package br.com.poo.view.visor;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;

import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import org.bson.Document;

public class TelaContabilidade extends JPanel {

    private Visor visor;
    private static final String CAMINHO_IMAGENS = "src/main/resources/images/";

    public TelaContabilidade(Visor visor) {
        this.visor = visor;
    }

    public void exibir(List<Document> votos) {
        prepararVisor();
        configurarPainel();
        addTitulo();

        int y = 60;
        int posicao = 1;

        for (Document votoCandidato : votos) {
            String nome = votoCandidato.getString("nome");

            if ("branco".equalsIgnoreCase(nome)) {
                y = addVotoBranco(votoCandidato, y);
            } else {
                y = addCandidato(votoCandidato, posicao++, y);
            }
        }
    }

    private void prepararVisor() {
        visor.removeAll();
        visor.setLayout(new BorderLayout());
        visor.add(this, BorderLayout.CENTER);
        visor.revalidate();
        visor.repaint();
    }

    private void configurarPainel() {
        setLayout(null);
        setBackground(Color.WHITE);
        setPreferredSize(new Dimension(600, 800));
    }

    private void addTitulo() {
        addLabel("CONTABILIDADE DOS VOTOS", 22, 180, 10, 400, 30);
    }

    private int addCandidato(Document doc, int posicao, int y) {
        String nome = doc.getString("nome");
        String partido = doc.getString("sigla");
        int votos = doc.getInteger("votos", 0);
        double porcentagem = doc.getDouble("porcentagem") != null ? doc.getDouble("porcentagem") : 0.0;

        addImagem(partido, nome, 30, y);
        addLabel(posicao + "º Mais Votado:", 16, 110, y, 400, 25);
        addLabel("Nome - " + nome, 16, 110, y + 25, 400, 20);
        addLabel("Partido - " + partido, 16, 110, y + 45, 400, 20);
        addLabel(String.format("Votos - %d (%.2f%%)", votos, porcentagem), 14, 110, y + 65, 400, 20);

        return y + 110;
    }

    private int addVotoBranco(Document doc, int y) {
        int votos = doc.getInteger("votos", 0);
        double porcentagem = doc.getDouble("porcentagem") != null ? doc.getDouble("porcentagem") : 0.0;

        addLabel("Votos em Branco:", 18, 320, 130, 250, 25);
        addLabel(String.format("Total: %d - %.2f%%", votos, porcentagem), 16, 340, 150, 300, 25);

        return y + 60;
    }

    private void addImagem(String sigla, String nome, int x, int y) {
        String caminhoImagem = CAMINHO_IMAGENS + sigla + "/" + nome.toLowerCase() + ".png";
        JLabel imagem = new JLabel();
        imagem.setBounds(x, y, 60, 60);
        imagem.setBorder(new LineBorder(Color.BLACK, 1));

        try {
            ImageIcon icon = new ImageIcon(caminhoImagem);
            Image imgRedimensionada = icon.getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH);
            imagem.setIcon(new ImageIcon(imgRedimensionada));
        } catch (Exception e) {
            System.err.println("Erro ao carregar imagem: " + caminhoImagem);
        }

        add(imagem);
    }

    private void addLabel(String texto, int tamanho, int x, int y, int largura, int altura) {
        JLabel label = new JLabel(texto);
        label.setFont(new Font("Arial", Font.PLAIN, tamanho));
        label.setBounds(x, y, largura, altura);
        add(label);
    }

    public void fechar() {
        visor.remove(this);
        visor.builder.iniciarTela();
        visor.revalidate();
        visor.repaint();
    }
}