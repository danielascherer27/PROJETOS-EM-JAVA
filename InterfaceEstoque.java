import javax.swing.*;
import java.awt.*;
import java.io.File;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class InterfaceEstoque extends JFrame {
    private Estoque estoque;
    private JTextArea areaTexto;
    private JTextArea areaHistorico;

    public InterfaceEstoque(Estoque estoque) {
        this.estoque = estoque;
        setTitle("Gerenciamento de Estoque");
        setSize(800, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel panelBotoes = new JPanel();
        JButton btnAdicionar = new JButton("Adicionar Produto");
        JButton btnAtualizar = new JButton("Atualizar Produto");
        JButton btnRemover = new JButton("Remover Produto");
        JButton btnSair = new JButton("Sair");
        JButton btnVisualizar = new JButton("Visualizar Produtos");
        JButton btnHistorico = new JButton("Visualizar Histórico");

        areaTexto = new JTextArea(10, 30);
        areaTexto.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(areaTexto);

        areaHistorico = new JTextArea(10, 30);
        areaHistorico.setEditable(false);
        JScrollPane scrollPaneHistorico = new JScrollPane(areaHistorico);

        btnAdicionar.addActionListener(e -> adicionarProduto());
        btnAtualizar.addActionListener(e -> atualizarProduto());
        btnRemover.addActionListener(e -> removerProduto());
        btnSair.addActionListener(e -> System.exit(0));
        btnVisualizar.addActionListener(e -> visualizarProdutos());
        btnHistorico.addActionListener(e -> visualizarHistorico());

        panelBotoes.add(btnAdicionar);
        panelBotoes.add(btnAtualizar);
        panelBotoes.add(btnRemover);
        panelBotoes.add(btnSair);
        panelBotoes.add(btnVisualizar);
        panelBotoes.add(btnHistorico);

        add(panelBotoes, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        // Adicionando a area de historico
        JPanel panelHistorico = new JPanel();
        panelHistorico.setLayout(new BorderLayout());
        panelHistorico.add(scrollPaneHistorico, BorderLayout.CENTER);
        add(panelHistorico, BorderLayout.SOUTH);
        carregarLogo();
        definirMochila();
    }

    private void carregarLogo() {
        try {
            File file = new File("PROJETOS-EM-JAVA/logo.jpg");
            if (!file.exists()) {
                JOptionPane.showMessageDialog(this, "Arquivo logo.jpg não encontrado.", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }
            BufferedImage logoImage = ImageIO.read(file);
            int newWidth = Math.max(100, logoImage.getWidth() / 4);
            int newHeight = Math.max(100, logoImage.getHeight() / 4);
            Image scaledLogo = logoImage.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
            ImageIcon logoIcon = new ImageIcon(scaledLogo);

            JLabel logoLabel = new JLabel(logoIcon);
            JPanel panelLogo = new JPanel();
            panelLogo.setLayout(new FlowLayout(FlowLayout.RIGHT));
            panelLogo.add(logoLabel);
            add(panelLogo, BorderLayout.EAST);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar a imagem. Verifique o caminho e o formato do arquivo.", "Erro", JOptionPane.ERROR_MESSAGE);
            System.out.println("Erro ao carregar a imagem: " + e.getMessage());
        }
    }

    private void definirMochila() {
        try {
            int mochilaComprimento = Integer.parseInt(JOptionPane.showInputDialog(this, "Digite o Comprimento da Mochila:"));
            int mochilaAltura = Integer.parseInt(JOptionPane.showInputDialog(this, "Digite a Altura da Mochila:"));
            int mochilaLargura = Integer.parseInt(JOptionPane.showInputDialog(this, "Digite a Largura da Mochila:"));

            if (mochilaComprimento <= 0 || mochilaAltura <= 0 || mochilaLargura <= 0) {
                JOptionPane.showMessageDialog(this, "As dimensões da mochila devem ser positivas.", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }
            Mochila mochila = new Mochila(mochilaComprimento, mochilaAltura, mochilaLargura);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Valor inválido. Certifique-se de digitar números.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }


    private void adicionarProduto() {
        String nomeProduto = JOptionPane.showInputDialog(this, "Digite o nome do produto:");
        if (nomeProduto != null && !nomeProduto.isEmpty()) {
            try {
                double comprimento = Double.parseDouble(JOptionPane.showInputDialog(this, "Digite o comprimento do produto:"));
                double altura = Double.parseDouble(JOptionPane.showInputDialog(this, "Digite a altura do produto:"));
                double largura = Double.parseDouble(JOptionPane.showInputDialog(this, "Digite o largura do produto:"));
                int quantidade = Integer.parseInt(JOptionPane.showInputDialog(this, "Digite a quantidade do produto:"));
                double valor = Double.parseDouble(JOptionPane.showInputDialog(this, "Digite o valor do produto:"));

                Produto produto = new Produto(nomeProduto, comprimento, altura, largura, quantidade, valor);
                boolean sucesso = estoque.adicionarProduto(produto);

                if (sucesso) {
                    JOptionPane.showMessageDialog(this, "Produto adicionado com sucesso!");
                    visualizarProdutos();
                } else {
                    JOptionPane.showMessageDialog(this, "Estoque cheio. Não é possível adicionar mais produtos.");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Quantidade ou valor inválido.");
            }
        }
    }

    private void atualizarProduto() {
        String nomeProduto = JOptionPane.showInputDialog(this, "Digite o nome do produto a ser atualizado:");
        if (nomeProduto == null || nomeProduto.trim().isEmpty()) return;

        Produto produto = estoque.buscarProduto(nomeProduto.trim());
        if (produto == null) {
            JOptionPane.showMessageDialog(this, "Produto não encontrado.");
            return;
        }

        try {
            double comprimentoAtual = produto.getComprimento();
            double alturaAtual = produto.getAltura();
            double larguraAtual = produto.getLargura();
            int quantidadeAtual = produto.getQuantidade();
            double valorAtual = produto.getValor();
            double novoComprimento = Double.parseDouble(JOptionPane.showInputDialog(this, "Digite o novo Comprimento:", comprimentoAtual));
            double novaAltura = Double.parseDouble(JOptionPane.showInputDialog(this, "Digite o novo valor:", alturaAtual));
            double novaLargura = Double.parseDouble(JOptionPane.showInputDialog(this, "Digite o novo valor:", larguraAtual));
            int novaQuantidade = Integer.parseInt(JOptionPane.showInputDialog(this, "Digite a nova quantidade:", quantidadeAtual));
            double novoValor = Double.parseDouble(JOptionPane.showInputDialog(this, "Digite o novo valor:", valorAtual));

            StringBuilder alteracoes = new StringBuilder();
            if (comprimentoAtual != novoComprimento) {
                alteracoes.append("Comprimento: ").append(quantidadeAtual).append(" -> ").append(novaQuantidade).append(". ");
                produto.setComprimento(comprimentoAtual);
            }
            if (quantidadeAtual != novaQuantidade) {
                alteracoes.append("Quantidade alterada de ").append(quantidadeAtual).append(" para ").append(novaQuantidade).append(". ");
                produto.setQuantidade(novaQuantidade);
            }
            if (valorAtual != novoValor) {
                alteracoes.append("Valor alterado de ").append(valorAtual).append(" para ").append(novoValor).append(". ");
                produto.setValor(novoValor);
            }
            if (alteracoes.length() > 0) {
                estoque.adicionarHistorico("Atualização", produto.getNome(), alteracoes.toString());
                JOptionPane.showMessageDialog(this, "Produto atualizado com sucesso!");
            } else {
                JOptionPane.showMessageDialog(this, "Nenhuma alteração foi realizada.");
            }

            visualizarProdutos();

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Valor inválido.");
        }
    }


    private void removerProduto() {
        String nomeProduto = JOptionPane.showInputDialog(this, "Digite o nome do produto a ser removido:");
        if (nomeProduto != null && !nomeProduto.isEmpty()) {
            boolean sucesso = estoque.removerProduto(nomeProduto);
            if (sucesso) {
                JOptionPane.showMessageDialog(this, "Produto removido com sucesso!");
                visualizarProdutos();
            } else {
                JOptionPane.showMessageDialog(this, "Produto não encontrado.");
            }
        }
    }

    private void visualizarProdutos() {
        areaTexto.setText("");
        if (estoque.getProdutos().isEmpty()) {
            areaTexto.setText("Estoque vazio.");
        } else {
            estoque.getProdutos().forEach(produto -> areaTexto.append(produto + "\n"));
        }
    }

    private void visualizarHistorico() {
        areaHistorico.setText("");
        estoque.getHistorico().forEach(historico -> areaHistorico.append(historico + "\n"));
    }

    public static void main(String[] args) {
        Estoque estoque = new Estoque(100);
        InterfaceEstoque interfaceEstoque = new InterfaceEstoque(estoque);
        interfaceEstoque.setVisible(true);
    }
}
