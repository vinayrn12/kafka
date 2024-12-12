package network;

import java.io.IOException;

public class ConnectionManager {
    public void startServer(int port) {
        MultiClientConnectionManager connectionManager;
        try {
            connectionManager = new MultiClientConnectionManager(port, 100);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        connectionManager.start();
    }
}
