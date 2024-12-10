import network.ConnectionManager;

public class Main {
    public static void main(String[] args){
        ConnectionManager connectionManager = new ConnectionManager();
        connectionManager.startServer(9092);
    }
}
