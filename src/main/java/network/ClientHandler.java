package network;

import model.Request;

import java.io.IOException;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClientHandler implements Runnable{
    private final Socket clientSocket;
    private static final int SOCKET_TIMEOUT = 30000;
    private static final Logger LOGGER = Logger.getLogger(ClientHandler.class.getName());

    public ClientHandler(Socket socket) throws IOException {
        this.clientSocket = socket;
        this.clientSocket.setSoTimeout(SOCKET_TIMEOUT);
        this.clientSocket.setTcpNoDelay(true);
    }

    @Override
    public void run() {
        try (clientSocket) {
            while (! Thread.currentThread().isInterrupted() && ! clientSocket.isClosed()) {
                try {
                    RequestProcessor requestProcessor = new RequestProcessor(clientSocket);
                    Request request = requestProcessor.processRequest();

                    if (request == null) {
                        break; // Client disconnected
                    }

                    ResponseProcessor responseProcessor = new ResponseProcessor(clientSocket);
                    responseProcessor.sendResponse(request);
                } catch (SocketTimeoutException e) {
                    LOGGER.warning("Client connection timed out");
                    break;
                }
            }
        } catch (IOException e) {
            LOGGER.log(Level.WARNING, "Error handling client connection: ", e);
        } finally {
            LOGGER.info("Client disconnected: " + clientSocket.getInetAddress());
        }
    }
}
