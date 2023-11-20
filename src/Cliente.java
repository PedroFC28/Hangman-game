import javax.swing.*;
import java.awt.Window;
import java.awt.event.WindowEvent;
import java.awt.EventQueue;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Cliente extends JFrame {
	public Cliente() {
	}

    private Socket clienteSocket;
    private ObjectOutputStream saida;
    private ObjectInputStream entrada;
    private JLabel lblPalavra;
    private JLabel lblTentativas;
    private String palavraAtual  ;
    private int tentativasRestantes;
    
    

    public static void main(String[] args) {
        

	
    	
    	SwingUtilities.invokeLater(() -> {
            Cliente cliente = new Cliente();
            cliente.iniciar();
        });
    }

    private void iniciar() {
        exibirInterfaceGrafica();
        conectarAoServidor();
    }


    private void exibirInterfaceGrafica() {
        JFrame frame = new JFrame("Jogo da Forca - Cliente");
        frame.setSize(400, 200);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.getContentPane().setLayout(new java.awt.GridLayout(3, 1));

        lblPalavra = new JLabel();
        lblTentativas = new JLabel();
        JTextField txtLetra = new JTextField();
        JButton btnAdivinhar = new JButton("Adivinhar");
        JButton btnNovoJogo = new JButton("Novo Jogo");
        

        frame.getContentPane().add(lblPalavra);
        frame.getContentPane().add(lblTentativas);
        frame.getContentPane().add(txtLetra);
        frame.getContentPane().add(btnAdivinhar);
        frame.getContentPane().add(btnNovoJogo);

        btnAdivinhar.addActionListener(e -> enviarLetra(txtLetra.getText()));

        btnNovoJogo.addActionListener(e -> novoJogo());

        frame.setVisible(true);
        
        
    }

    private void conectarAoServidor() {
        try {
            clienteSocket = new Socket("127.0.0.1", 12345);
            saida = new ObjectOutputStream(clienteSocket.getOutputStream());
            entrada = new ObjectInputStream(clienteSocket.getInputStream());

            Thread thread = new Thread(() -> {
                try {
                    while (true) {
                        String mensagem = (String) entrada.readObject();
                        if (mensagem != null) {
                            atualizarGUI(mensagem);
                        }
                    }
                } catch (IOException | ClassNotFoundException ex) {
                    System.out.println("Erro ao receber mensagem do servidor.");
                }
            });
            thread.start();

            // Solicita ao servidor para iniciar um novo jogo
            enviarMensagem("novo_jogo");
            enviarMensagem("underscore");

        } catch (IOException ex) {
            System.out.println("Erro ao conectar ao servidor.");
        }
    }
    
    

    private void enviarLetra(String letra) {
        try {
            char caractere = letra.toLowerCase().charAt(0);
            if (caractere >= 'a' && caractere <= 'z') {
                enviarMensagem(String.valueOf(caractere));
            } else {
                JOptionPane.showMessageDialog(null, "Digite uma letra válida.", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    private void novoJogo() {
        enviarMensagem("novo_jogo"); // Envia a mensagem para o servidor iniciar um novo jogo.
        palavraAtual = ""; // Reinicia a palavra atual para uma string vazia.
        tentativasRestantes = 6; // Reinicia o número de tentativas restantes.
    }


    private void enviarMensagem(String mensagem) {
        try {
            saida.writeObject(mensagem);
            saida.flush();
        } catch (IOException ex) {
            System.out.println("Erro ao enviar mensagem ao servidor.");
        }
    }

    private void atualizarGUI(String mensagem) {
        SwingUtilities.invokeLater(() -> {
            if (mensagem.startsWith("Palavra:")) {
                lblPalavra.setText(mensagem.substring(8)); // Remove "Palavra: " da mensagem e atualiza a interface.
            } else if (mensagem.startsWith("Tentativas restantes:")) {
                lblTentativas.setText(mensagem); // Atualiza a interface com o número de tentativas restantes.
            } else if (mensagem.startsWith("Você perdeu!") || mensagem.startsWith("Parabéns!")) {
                int opcao = JOptionPane.showConfirmDialog(null, "Deseja jogar novamente?", "Fim do Jogo", JOptionPane.YES_NO_OPTION);
                if (opcao == JOptionPane.YES_OPTION) {
                    novoJogo(); // Se o jogador escolher "Sim", inicia um novo jogo.
                    lblPalavra.setText(palavraAtual); // Atualiza a interface com a palavra atual com underscores.
                    lblTentativas.setText("Tentativas restantes: " + tentativasRestantes); // Atualiza a interface com o número de tentativas restantes.
                    
                } else {
                    // Se o jogador escolher "Não" ou fechar a caixa de diálogo, fecha o cliente.
                    System.exit(0);
                }
            }
        });
    }


}


