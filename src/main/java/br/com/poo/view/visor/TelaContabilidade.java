package br.com.poo.view.visor;

import javax.swing.*;
import org.bson.Document;
import java.awt.*;
import java.util.List;

public class TelaContabilidade extends JPanel {

    private final Visor visor;

    public TelaContabilidade(Visor visor) {
        this.visor = visor;
    }

    public void exibir(List<Document> votos) {
        prepararVisor();
        configurarPainel();
        exibirTitulo();
        exibirVotos(votos);
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
        setPreferredSize(new Dimension(600, 400));
    }

    private void exibirTitulo() {
        addLabel("CONTABILIDADE DOS VOTOS", 22, 200, 10, 400, 30);
    }

    private void exibirVotos(List<Document> votos) {
        int y = 70;
        for (int i = 0; i < votos.size(); i++) {
            Document doc = votos.get(i);
            String nome = doc.getString("nome");

            if ("branco".equals(nome)) {
                exibirVotoBranco(doc);
            } else {
                exibirCandidato(doc, i, y);
                y += 40;
            }
        }
    }

    private void exibirCandidato(Document doc, int index, int y) {
        String nome = doc.getString("nome");
        String partido = doc.getString("partido");
        int votos = doc.getInteger("votos");
        double porcentagem = doc.getDouble("porcentagem");

        addLabel(String.format("%dº Mais Votado:", index + 1), 18, 50, y, 200, 25);
        addLabel(String.format("%s - %s - %d votos - %.2f%%", nome, partido, votos, porcentagem), 16, 250, y, 400, 25);
    }

    private void exibirVotoBranco(Document doc) {
        int votos = doc.getInteger("votos");
        double porcentagem = doc.getDouble("porcentagem");

        addLabel("Votos em Branco:", 18, 50, 200, 200, 25);
        addLabel(String.format("Total: %d votos - %.2f%%", votos, porcentagem), 16, 250, 200, 400, 25);
    }

    private void addLabel(String texto, int tamanho, int x, int y, int largura, int altura) {
        JLabel label = new JLabel(texto);
        label.setFont(new Font("Arial", Font.PLAIN, tamanho));
        label.setBounds(x, y, largura, altura);
        add(label);
    }
}