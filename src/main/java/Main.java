import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {

    private static Integer CO_RELATION_ID = 7;
    private static Integer MESSAGE_SIZE = 1;

    public static void main(String[] args){
        ServerSocket serverSocket;
        Socket clientSocket = null;
        PrintWriter writer = null;
        int port = 9092;
        try {
            serverSocket = new ServerSocket(port);

            // Since the tester restarts your program quite often, setting SO_REUSEADDR
            // ensures that we don't run into 'Address already in use' errors
            serverSocket.setReuseAddress(true);

            // Wait for connection from client.
            clientSocket = serverSocket.accept();
            writer = new PrintWriter(clientSocket.getOutputStream(), true);
            writer.println(MESSAGE_SIZE);
            writer.println(CO_RELATION_ID);
            writer.close();

        } catch (IOException e) {
            System.out.println("IOException: " + e.getMessage());
        } finally {
            try {
                if (clientSocket != null) {
                    clientSocket.close();
                }
            } catch (IOException e) {
                System.out.println("IOException: " + e.getMessage());
            }
        }
    }
}
