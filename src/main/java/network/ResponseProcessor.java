package network;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ResponseProcessor {
    public void sendResponse(Socket clientSocket, int correlationId) throws IOException {
        DataOutputStream outputStream = new DataOutputStream(clientSocket.getOutputStream());

        outputStream.writeInt(4);
        outputStream.writeInt(correlationId);

        outputStream.flush();
        outputStream.close();
    }
}
