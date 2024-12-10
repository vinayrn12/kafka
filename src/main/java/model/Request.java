package model;

public class Request {
    private final int size;
    private final short apiKey;
    private final short apiVersion;
    private final int correlationId;

    private Request(short apiKey, short apiVersion, int correlationId, int size) {
        this.apiKey = apiKey;
        this.apiVersion = apiVersion;
        this.correlationId = correlationId;
        this.size = size;
    }

    public short getApiKey() {
        return apiKey;
    }

    public short getApiVersion() {
        return apiVersion;
    }

    public int getCorrelationId() {
        return correlationId;
    }

    public int getSize() {
        return size;
    }

    public static class Builder {
        private int size;
        private short apiKey;
        private short apiVersion;
        private int correlationId;

        public Builder setSize(int size) {
            this.size = size;
            return this;
        }

        public Builder setApiKey(short apiKey) {
            this.apiKey = apiKey;
            return this;
        }

        public Builder setApiVersion(short apiVersion) {
            this.apiVersion = apiVersion;
            return this;
        }

        public Builder setCorrelationId(int correlationId) {
            this.correlationId = correlationId;
            return this;
        }

        public Request build() {
            return new Request(this.apiKey, this.apiVersion, this.correlationId, this.size);
        }
    }
}
