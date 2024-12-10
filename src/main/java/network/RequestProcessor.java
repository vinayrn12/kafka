package network;

import model.Request;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

public class RequestProcessor {
    private final DataInputStream inputStream;

    public RequestProcessor(Socket clientSocket) throws IOException {
        this.inputStream = new DataInputStream(clientSocket.getInputStream());
    }

    public Request processRequest() throws IOException {
        int size = this.inputStream.readInt();

        short apiKey = this.inputStream.readShort();

        short apiVersion = this.inputStream.readShort();

        int correlationId = this.inputStream.readInt();

        // Consume remaining bytes
        byte[] remainingBytes = new byte[size - 8];
        this.inputStream.readFully(remainingBytes);

        return new Request.Builder()
                .setSize(size)
                .setApiKey(apiKey)
                .setApiVersion(apiVersion)
                .setCorrelationId(correlationId)
                .build();
    }
}
