package network;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

public class RequestProcessor {
    private final DataInputStream inputStream;

    public RequestProcessor(Socket clientSocket) throws IOException {
        this.inputStream = new DataInputStream(clientSocket.getInputStream());
    }

    public int processRequest() throws IOException {
        this.inputStream.skipBytes(8); // Skip the first 8 bytes (message_size + api_key + api_version)

        byte[] correlation_byteArray = new byte[4];
        this.inputStream.read(correlation_byteArray, 0, 4);

        int correlationId = 0;
        for (byte b : correlation_byteArray) {
            correlationId = (correlationId << 8) + (b & 0xFF);
        }

        return correlationId;
    }
}
