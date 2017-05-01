package top.cuihp.serverlibrary.server;

/**
 * Created by cuihp on 2017/4/15.
 */

public class ServerConfig {
    private int port;
    private int readBufferSize;
    private long connectionTimeout;
    private int maxReadBufferSize;
    private int minReadBufferSize;
    private int wiiteTimeout;


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
        private int port = 8888;
        private int readBufferSize = 1024 * 10;
        private long connectionTimeout = 1000 * 10;

        public Builder() {
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

        private void applyConfig(ServerConfig config) {
            config.port = this.port;
            config.connectionTimeout = this.connectionTimeout;
            config.readBufferSize = this.readBufferSize;
        }

        public ServerConfig build() {
            ServerConfig config = new ServerConfig();
            applyConfig(config);
            return config;
        }
    }
}
