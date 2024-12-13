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

            // APIVersions API
            apiMetadataList.add(new Response.ApiMetadata(
                    request.getApiKey(),  // api_key
                    (short)0,   // min_version
                    (short)4,   // max_version
                    (byte)0     // tagged_fields
            ));

            // DescribeTopicPartitions API
            apiMetadataList.add(new Response.ApiMetadata(
                    (short) 75,
                    (short) 0,
                    (short) 4,
                    (byte) 0
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
            this.outputStream.write(sizeBytes);
            this.outputStream.write(responseBytes);
        } else {
            this.outputStream.writeInt(6);
            this.outputStream.writeInt(request.getCorrelationId());
            this.outputStream.writeShort(35);
        }
    }
}
