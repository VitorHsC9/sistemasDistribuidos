import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class Servidor {
  private static final int PORTA = 5000;
  private static final String[] RESPOSTAS = {
      "Olá!",
      "Bom dia.",
      "Como foi o seu dia?",
      "Conte-me mais sobre isso.",
      "Hmm, entendo seu ponto de vista.",
      "Isso é realmente fascinante.",
      "O que mais você gostaria de saber?",
      "Continue, estou ouvindo.",
  };

  public static void main(String[] args) {
    System.out.println("=== SERVIDOR TCP INICIADO ===");
    System.out.println("Aguardando conexões na porta " + PORTA + "...\n");

    try (ServerSocket serverSocket = new ServerSocket(PORTA)) {
      while (true) {
        Socket clientSocket = serverSocket.accept();

        String clientIP = clientSocket.getInetAddress().getHostAddress();
        System.out.println("🔗 Novo cliente conectado: " + clientIP);

        Thread clientThread = new Thread(new ClientHandler(clientSocket, clientIP));
        clientThread.start();
      }
    } catch (IOException e) {
      System.err.println("Erro no servidor: " + e.getMessage());
    }
  }

  static class ClientHandler implements Runnable {
    private Socket clientSocket;
    private String clientIP;
    private BufferedReader input;
    private PrintWriter output;

    public ClientHandler(Socket socket, String ip) {
      this.clientSocket = socket;
      this.clientIP = ip;
    }

    @Override
    public void run() {
      try {
        input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        output = new PrintWriter(clientSocket.getOutputStream(), true);

        System.out.println("✅ Thread criada para cliente: " + clientIP);

        String mensagem;
        while ((mensagem = input.readLine()) != null) {
          System.out.println("📨 [" + clientIP + "]: " + mensagem);

          if ("sair".equalsIgnoreCase(mensagem.trim())) {
            System.out.println("👋 Cliente " + clientIP + " desconectou");
            break;
          }

          String resposta = gerarRespostaAleatoria();
          output.println(resposta);
          System.out.println("📤 [Servidor -> " + clientIP + "]: " + resposta);
        }

      } catch (IOException e) {
        System.err.println("❌ Erro na comunicação com cliente " + clientIP + ": " + e.getMessage());
      } finally {
        try {
          if (input != null) input.close();
          if (output != null) output.close();
          if (clientSocket != null) clientSocket.close();
          System.out.println("🔒 Conexão com " + clientIP + " encerrada");
        } catch (IOException e) {
          System.err.println("Erro ao fechar conexão: " + e.getMessage());
        }
      }
    }

    private String gerarRespostaAleatoria() {
      int indice = ThreadLocalRandom.current().nextInt(RESPOSTAS.length);
      return RESPOSTAS[indice];
    }
  }
}