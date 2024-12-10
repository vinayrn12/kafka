package network;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ConnectionManager {
    public void startServer(int port) {
        ServerSocket serverSocket = null;
        Socket clientSocket = null;
        try {
            serverSocket = new ServerSocket(port);

            // Since the tester restarts your program quite often, setting SO_REUSEADDR
            // ensures that we don't run into 'Address already in use' errors
            serverSocket.setReuseAddress(true);

            // Wait for connection from client.
            clientSocket = serverSocket.accept();

            RequestProcessor requestProcessor = new RequestProcessor(clientSocket);
            int correlationId = requestProcessor.processRequest();

            ResponseProcessor responseProcessor = new ResponseProcessor(clientSocket);
            responseProcessor.sendResponse(correlationId);

        } catch (IOException e) {
            System.out.println("IOException: " + e.getMessage());
        } finally {
            closeClientSocket(clientSocket);
            closeServerSocket(serverSocket);
        }
    }

    private void closeClientSocket(Socket clientSocket) {
        try {
            if (clientSocket != null) {
                clientSocket.close();
            }
        } catch (IOException e) {
            System.out.println("IOException closing client socket: " + e.getMessage());
        }
    }

    private void closeServerSocket(ServerSocket serverSocket) {
        try {
            if (serverSocket != null) {
                serverSocket.close();
            }
        } catch (IOException e) {
            System.out.println("IOException closing server socket: " + e.getMessage());
        }
    }
}
