package network;

import model.Request;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ConnectionManager {
    public void startServer(int port) {
        ServerSocket serverSocket = null;
        Socket clientSocket = null;
        try {
            serverSocket = new ServerSocket(port);
            serverSocket.setReuseAddress(true); // Handle 'Address already in use' errors

            // Wait for a connection from a client
            clientSocket = serverSocket.accept();
            System.out.println("Client connected from " + clientSocket.getInetAddress());

            // Handling requests in a loop
            while (true) {
                RequestProcessor requestProcessor = new RequestProcessor(clientSocket);
                Request request = requestProcessor.processRequest();

                ResponseProcessor responseProcessor = new ResponseProcessor(clientSocket);
                responseProcessor.sendResponse(request);
            }
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
