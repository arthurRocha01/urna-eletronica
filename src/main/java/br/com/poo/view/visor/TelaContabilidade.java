package br.com.poo.view.visor;

import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.border.LineBorder;

import org.bson.Document;

/**
 * Exibe a tela de contabilidade de votos da urna.
 * 
 * <p>Apresenta os três candidatos mais votados e os votos em branco com nome, partido,
 * quantidade de votos e porcentagem.</p>
 * 
 * <p>Também exibe as imagens dos candidatos conforme sua sigla e nome.</p>
 * 
 * @author Arthur Rocha
 * @version 1.0
 * @since 1.0
 */
public class TelaContabilidade extends JPanel {

    private Visor visor;
    private static final String CAMINHO_IMAGENS = "src/main/resources/images/";

    /**
     * Construtor que recebe o painel do visor principal.
     *
     * @param visor painel principal que contém esta tela
     */
    public TelaContabilidade(Visor visor) {
        this.visor = visor;
    }

    /**
     * Exibe todos os candidatos e votos brancos com seus respectivos dados.
     *
     * @param votos lista de documentos contendo dados de contabilidade de votos
     */
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

    /**
     * Limpa e prepara o visor principal para exibir esta tela.
     */
    private void prepararVisor() {
        visor.removeAll();
        visor.setLayout(new BorderLayout());
        visor.add(this, BorderLayout.CENTER);
        visor.revalidate();
        visor.repaint();
    }

    /**
     * Configura o layout e aparência do painel da tela.
     */
    private void configurarPainel() {
        setLayout(null);
        setBackground(Color.WHITE);
        setPreferredSize(new Dimension(600, 800));
    }

    /**
     * Adiciona o título da tela.
     */
    private void addTitulo() {
        addLabel("CONTABILIDADE DOS VOTOS", 22, 180, 10, 400, 30);
    }

    /**
     * Adiciona os dados de um candidato ao painel.
     *
     * @param doc documento contendo informações do candidato
     * @param posicao posição do candidato no ranking
     * @param y posição vertical para inserção
     * @return nova posição vertical após a adição
     */
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

    /**
     * Adiciona os dados dos votos brancos ao painel.
     *
     * @param doc documento contendo informações dos votos brancos
     * @param y posição vertical para inserção
     * @return nova posição vertical após a adição
     */
    private int addVotoBranco(Document doc, int y) {
        int votos = doc.getInteger("votos", 0);
        double porcentagem = doc.getDouble("porcentagem") != null ? doc.getDouble("porcentagem") : 0.0;

        addLabel("Votos em Branco:", 18, 320, 130, 250, 25);
        addLabel(String.format("Total: %d - %.2f%%", votos, porcentagem), 16, 340, 150, 300, 25);

        return y + 60;
    }

    /**
     * Adiciona a imagem de um candidato ao painel.
     *
     * @param sigla sigla do partido (nome da pasta)
     * @param nome nome do candidato (nome da imagem em minúsculas)
     * @param x coordenada x
     * @param y coordenada y
     */
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
            visor.controlador.avisarSistema("TelaContabilidade", "Erro ao carregar imagem: " + caminhoImagem);
        }

        add(imagem);
    }

    /**
     * Adiciona um JLabel com as configurações definidas.
     *
     * @param texto texto do label
     * @param tamanho tamanho da fonte
     * @param x coordenada x
     * @param y coordenada y
     * @param largura largura do label
     * @param altura altura do label
     */
    private void addLabel(String texto, int tamanho, int x, int y, int largura, int altura) {
        JLabel label = new JLabel(texto);
        label.setFont(new Font("Arial", Font.PLAIN, tamanho));
        label.setBounds(x, y, largura, altura);
        add(label);
    }

    /**
     * Fecha esta tela e retorna para a tela inicial.
     */
    public void fechar() {
        visor.remove(this);
        visor.builder.iniciarTela();
        visor.revalidate();
        visor.repaint();
    }
}