import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Cliente {
  private static final String HOST = "localhost";
  private static final int PORTA = 5000;

  public static void main(String[] args) {
    System.out.println("=== CLIENTE TCP ===");
    System.out.println("Conectando ao servidor " + HOST + ":" + PORTA + "...\n");

    try (Socket socket = new Socket(HOST, PORTA);
        BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter output = new PrintWriter(socket.getOutputStream(), true);
        Scanner scanner = new Scanner(System.in)) {

      System.out.println("✅ Conectado ao servidor!");
      System.out.println("Digite suas mensagens (digite 'sair' para encerrar):\n");

      String mensagem;
      while (true) {
        System.out.print("👤 Você: ");
        mensagem = scanner.nextLine();

        output.println(mensagem);

        if ("sair".equalsIgnoreCase(mensagem.trim())) {
          System.out.println("👋 Encerrando conexão...");
          break;
        }

        String resposta = input.readLine();
        if (resposta != null) {
          System.out.println("🤖 Servidor: " + resposta);
          System.out.println();
        } else {
          System.out.println("❌ Conexão perdida com o servidor.");
          break;
        }
      }

    } catch (UnknownHostException e) {
      System.err.println("❌ Host não encontrado: " + e.getMessage());
    } catch (IOException e) {
      System.err.println("❌ Erro de conexão: " + e.getMessage());
    }

    System.out.println("🔒 Cliente encerrado.");
  }
}