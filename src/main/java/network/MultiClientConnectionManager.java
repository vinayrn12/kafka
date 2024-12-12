package network;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MultiClientConnectionManager implements AutoCloseable{
    private static final Logger LOGGER = Logger.getLogger(MultiClientConnectionManager.class.getName());
    private final ServerSocket serverSocket;
    private final ExecutorService executorService;
    private volatile boolean isRunning;

    public MultiClientConnectionManager(int port, int maxClients) throws IOException {
        this.serverSocket = new ServerSocket(port);
        this.serverSocket.setReuseAddress(true);
        this.executorService = new ThreadPoolExecutor(
                maxClients/2,
                maxClients,
                60L,
                TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(100),
                new ThreadPoolExecutor.CallerRunsPolicy()
        );
        this.isRunning = true;
    }

    public void start() {
        LOGGER.info("Server starting on port: " + serverSocket.getLocalPort());

        while (isRunning) {
            try {
                Socket clientSocket = serverSocket.accept();
                executorService.submit(new ClientHandler(clientSocket));
                LOGGER.info("New client connected from " + clientSocket.getInetAddress());
            } catch (IOException e) {
                if (isRunning) {
                    LOGGER.log(Level.SEVERE, "Error accepting client connection: ", e);
                }
            }
        }
    }

    @Override
    public void close() throws Exception {
        shutdown();
    }

    private void shutdown() {
        isRunning = false;
        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(60, TimeUnit.SECONDS)) {
                executorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            executorService.shutdownNow();
            Thread.currentThread().interrupt();
        }

        try {
            if (!serverSocket.isClosed()) {
                serverSocket.close();
            }
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error closing server socket", e);
        }
    }
}
