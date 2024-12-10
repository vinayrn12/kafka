package network;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

public class RequestProcessor {
    public int processRequest(Socket clientSocket) throws IOException {
        DataInputStream inputStream = new DataInputStream(clientSocket.getInputStream());
        inputStream.skipBytes(8); // Skip the first 8 bytes (message_size + api_key + api_version)

        byte[] correlation_byteArray = new byte[4];
        inputStream.read(correlation_byteArray, 0, 4);

        int correlationId = 0;
        for (byte b : correlation_byteArray) {
            correlationId = (correlationId << 8) + (b & 0xFF);
        }

        inputStream.close();
        return correlationId;
    }
}
