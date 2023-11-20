import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Servidor {

    private String[] palavras = {"java", "programacao", "computador", "desenvolvimento", "linguagem"};
    private String palavraSecreta;
    public static char[] palavraAdivinhada;
    public static int tentativas = 6;
    public String palavraAtual;
    public int tentativasRestantes;

    public static void main(String[] args) {
    	
    	
    	
        Servidor servidor = new Servidor();
        servidor.iniciar();
    }

    private void iniciar() {
        try {
            System.out.println("Servidor iniciado na porta 12345");
            ServerSocket servidor = new ServerSocket(12345);

            while (true) {
                Socket cliente = servidor.accept();
                System.out.println("Cliente conectado: " + cliente.getInetAddress().getHostAddress());

                ObjectOutputStream saida = new ObjectOutputStream(cliente.getOutputStream());
                ObjectInputStream entrada = new ObjectInputStream(cliente.getInputStream());

                novoJogo();
                enviarMensagem("Novo jogo iniciado! Adivinhe a palavra: " + String.valueOf(palavraAdivinhada), saida);

                while (true) {
                    String mensagem = (String) entrada.readObject();

                    if (mensagem.equalsIgnoreCase("novo_jogo")) {
                        novoJogo();
                        enviarMensagem("Novo jogo iniciado! Adivinhe a palavra: " + String.valueOf(palavraAdivinhada), saida);
                    } else if (mensagem.length() == 1) {
                        char letra = mensagem.toLowerCase().charAt(0);
                        verificarLetra(letra);
                        enviarMensagem("Palavra: " + String.valueOf(palavraAdivinhada), saida);
                        enviarMensagem("Tentativas restantes: " + tentativas, saida);

                        if (tentativas == 0 || String.valueOf(palavraAdivinhada).equals(palavraSecreta)) {
                            if (tentativas == 0) {
                                enviarMensagem("Você perdeu! A palavra secreta era: " + palavraSecreta, saida);
                            } else {
                                enviarMensagem("Parabéns! Você adivinhou a palavra correta: " + palavraSecreta, saida);
                            }
                            reiniciarJogo();
                        }
                    }else if (mensagem.equalsIgnoreCase("underscore")) {
                    	novoJogo();
                    	 enviarMensagem("Palavra: " + String.valueOf(palavraAdivinhada), saida);
                    	 enviarMensagem("Tentativas restantes: " + tentativas, saida);
                    }
                    
                }

            }

        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void novoJogo() {
        palavraSecreta = escolherPalavra(palavras);
        palavraAdivinhada = new char[palavraSecreta.length()];
        underScores();
        tentativas = 6;
    }
    
    
    private void underScores() {
        for (int i = 0; i < palavraAdivinhada.length; i++) {
            palavraAdivinhada[i] = '_';
        }
    };

    private void verificarLetra(char letra) {
        boolean letraAdivinhada = false;

        for (int i = 0; i < palavraSecreta.length(); i++) {
            if (palavraSecreta.charAt(i) == letra) {
                palavraAdivinhada[i] = letra;
                letraAdivinhada = true;
            }
        }

        if (!letraAdivinhada) {
            tentativas--;
        }
    }

    private String escolherPalavra(String[] palavras) {
        int indiceAleatorio = (int) (Math.random() * palavras.length);
        return palavras[indiceAleatorio];
    }

    private void enviarMensagem(String mensagem, ObjectOutputStream saida) {
        try {
            saida.writeObject(mensagem);
            saida.flush();
        } catch (IOException ex) {
            System.out.println("Erro ao enviar mensagem ao cliente.");
        }
    }
    private void reiniciarJogo() {
        novoJogo();
        palavraAtual = String.valueOf(palavraAdivinhada);
        tentativasRestantes = tentativas;
    }



    
}


