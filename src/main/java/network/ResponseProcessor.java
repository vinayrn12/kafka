package network;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ResponseProcessor {
    private final DataOutputStream outputStream;

    public ResponseProcessor(Socket clientSocket) throws IOException {
        this.outputStream = new DataOutputStream(clientSocket.getOutputStream());
    }

    public void sendResponse(int correlationId) throws IOException {
        outputStream.writeInt(6);
        // Header
        outputStream.writeInt(correlationId);
        // Body
        outputStream.writeShort(35); // Error code 35 for now

        outputStream.flush();
    }
}
