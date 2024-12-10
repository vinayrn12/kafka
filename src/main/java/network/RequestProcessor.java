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
        Request request = new Request.Builder()
                .setSize(this.inputStream.readInt())
                .setApiKey(this.inputStream.readShort())
                .setApiVersion(this.inputStream.readShort())
                .setCorrelationId(this.inputStream.readInt())
                .build();

        return request;
    }
}
