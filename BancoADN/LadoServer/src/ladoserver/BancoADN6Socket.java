package ladoserver;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class BancoADN6Socket {

    private static final int PORT = 4990;
    private static final ExecutorService executor = Executors.newFixedThreadPool(50);

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Escuchando puerto " + PORT);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Cliente conectado" + clientSocket.getInetAddress());

                executor.submit(new BancoADN6RequestHandler(clientSocket));
            }

        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}
