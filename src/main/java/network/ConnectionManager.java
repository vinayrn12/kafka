package network;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ConnectionManager {
    public void startServer(int port) {
        ServerSocket serverSocket;
        Socket clientSocket = null;
        try {
            serverSocket = new ServerSocket(port);

            // Since the tester restarts your program quite often, setting SO_REUSEADDR
            // ensures that we don't run into 'Address already in use' errors
            serverSocket.setReuseAddress(true);

            // Wait for connection from client.
            clientSocket = serverSocket.accept();

            RequestProcessor requestProcessor = new RequestProcessor();
            int correlationId = requestProcessor.processRequest(clientSocket);

            ResponseProcessor responseProcessor = new ResponseProcessor();
            responseProcessor.sendResponse(clientSocket, correlationId);

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
