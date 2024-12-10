package network;

import model.Request;
import model.Response;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class ResponseProcessor {
    private final DataOutputStream outputStream;

    public ResponseProcessor(Socket clientSocket) throws IOException {
        this.outputStream = new DataOutputStream(clientSocket.getOutputStream());
    }

    public void sendResponse(Request request) throws IOException {
        if (request.getApiVersion() == 4) {
            List<Response.ApiMetadata> apiMetadataList = new ArrayList<>();
            apiMetadataList.add(new Response.ApiMetadata(
                    request.getApiKey(),  // api_key
                    (short)3,   // min_version
                    (short)4,   // max_version
                    (byte)0     // tagged_fields
            ));

            // Build the response
            Response response = new Response.Builder()
                    .correlationId(request.getCorrelationId())
                    .errorCode((short)0)
                    .apiMetadata(apiMetadataList)
                    .throttleTime(0)
                    .taggedFields((byte)0)
                    .build();

            // Use ByteArrayOutputStream to build the binary response
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            response.writeTo(bos);

            // Get the byte representation of the response size
            byte[] sizeBytes = ByteBuffer.allocate(4).putInt(bos.size()).array();

            // Get the byte representation of the response data
            byte[] responseBytes = bos.toByteArray();

            // Write size and response to the output stream
            outputStream.write(sizeBytes);
            outputStream.write(responseBytes);
        } else {
            outputStream.writeInt(6);
            outputStream.writeInt(request.getCorrelationId());
            outputStream.writeShort(35);
        }
    }
}
