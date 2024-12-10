package model;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class Response {
    private final int correlationId;
    private final short errorCode;
    private final List<ApiMetadata> apiMetadata;
    private final int throttleTime;
    private final byte taggedFields;

    public static class ApiMetadata {
        private final short apiKey;
        private final short minVersion;
        private final short maxVersion;
        private final byte taggedFields;

        public ApiMetadata(short apiKey, short minVersion, short maxVersion, byte taggedFields) {
            this.apiKey = apiKey;
            this.minVersion = minVersion;
            this.maxVersion = maxVersion;
            this.taggedFields = taggedFields;
        }

        public void writeTo(ByteArrayOutputStream bos) throws IOException {
            bos.write(new byte[] {(byte)(apiKey >> 8), (byte)apiKey});
            bos.write(new byte[] {(byte)(minVersion >> 8), (byte)minVersion});
            bos.write(new byte[] {(byte)(maxVersion >> 8), (byte)maxVersion});
            bos.write(taggedFields);
        }
    }

    public static class Builder {
        private int correlationId;
        private short errorCode = 0;
        private List<ApiMetadata> apiMetadata = new ArrayList<>();
        private int throttleTime = 0;
        private byte taggedFields = 0;

        public Builder correlationId(int correlationId) {
            this.correlationId = correlationId;
            return this;
        }

        public Builder errorCode(short errorCode) {
            this.errorCode = errorCode;
            return this;
        }

        public Builder apiMetadata(List<ApiMetadata> apiMetadata) {
            this.apiMetadata = new ArrayList<>(apiMetadata);
            return this;
        }

        public Builder throttleTime(int throttleTime) {
            this.throttleTime = throttleTime;
            return this;
        }

        public Builder taggedFields(byte taggedFields) {
            this.taggedFields = taggedFields;
            return this;
        }

        public Response build() {
            return new Response(this);
        }
    }

    private Response(Builder builder) {
        this.correlationId = builder.correlationId;
        this.errorCode = builder.errorCode;
        this.apiMetadata = new ArrayList<>(builder.apiMetadata);
        this.throttleTime = builder.throttleTime;
        this.taggedFields = builder.taggedFields;
    }

    public void writeTo(ByteArrayOutputStream bos) throws IOException {
        // Write correlationId
        bos.write(ByteBuffer.allocate(4).putInt(correlationId).array());

        // Write error code
        bos.write(new byte[] {(byte)(errorCode >> 8), (byte)errorCode});

        // Write array size + 1 (include 1 for the tagged field byte)
        bos.write(apiMetadata.size() + 1);

        // Write each API metadata entry
        for (ApiMetadata metadata : apiMetadata) {
            metadata.writeTo(bos);
        }

        // Write throttle time
        bos.write(ByteBuffer.allocate(4).putInt(throttleTime).array());

        // Write final tagged fields
        bos.write(taggedFields);
    }
}