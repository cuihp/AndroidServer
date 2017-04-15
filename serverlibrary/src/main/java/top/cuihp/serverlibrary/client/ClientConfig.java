package top.cuihp.serverlibrary.client;

/**
 * Created by cuihp on 2017/4/15.
 */

public class ClientConfig {
    private String ip;
    private int port;
    private int readBufferSize;
    private long connectionTimeout;


    public String getIp() {
        return ip;
    }

    public int getPort() {
        return port;
    }

    public int getReadBufferSize() {
        return readBufferSize;
    }

    public long getConnectionTimeout() {
        return connectionTimeout;
    }

    public static class Builder {
        private String ip = "127.0.0.1";
        private int port = 8888;
        private int readBufferSize = 1024 * 10;
        private long connectionTimeout = 1000 * 10;

        public Builder() {
        }

        public Builder setIp(String ip) {
            this.ip = ip;
            return this;
        }

        public Builder setPort(int port) {
            this.port = port;
            return this;
        }

        public Builder setReadBufferSize(int readBufferSize) {
            this.readBufferSize = readBufferSize;
            return this;
        }

        public Builder setConnectionTimeout(long connectionTimeout) {
            this.connectionTimeout = connectionTimeout;
            return this;
        }

        private void applyConfig(ClientConfig config) {
            config.ip = this.ip;
            config.port = this.port;
            config.connectionTimeout = this.connectionTimeout;
            config.readBufferSize = this.readBufferSize;
        }

        public ClientConfig build() {
            ClientConfig config = new ClientConfig();
            applyConfig(config);
            return config;
        }
    }
}
