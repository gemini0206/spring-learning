package com.example.spring.grpc.server;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Grpc server property handler
 *
 * @author essen
 */
@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "grpc")
public class GrpcServerProperties {

    /**
     * Security options for transport security. Defaults to disabled.
     */
    private final Security security = new Security();
    /**
     * Server port to listen on.
     */
    private int port;
    /**
     * Bind address for the server. Defaults to 0.0.0.0.
     */
    private String host = "0.0.0.0";

    /**
     * Set trace enable, Default is false
     */
    private boolean traceEnable = false;

    /**
     * Set stats enable, Default is false
     */
    private boolean statsEnable = false;

    private int serverWorkerCount = 10;

    /**
     * In process server name.
     * If  the value is not empty, the embedded in-process server will be created and started.
     */
    private String inProcessServerName;

    public int getPort() {
//        if (this.port == 0) {
//            this.port = SocketUtils.findAvailableTcpPort();
//        }
        return this.port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public Security getSecurity() {
        return security;
    }

    public boolean isTraceEnable() {
        return traceEnable;
    }

    public void setTraceEnable(boolean traceEnable) {
        this.traceEnable = traceEnable;
    }

    public boolean isStatsEnable() {
        return statsEnable;
    }

    public void setStatsEnable(boolean statsEnable) {
        this.statsEnable = statsEnable;
    }

    public String getHost() {
        return host;
    }

    public static class Security {

        /**
         * Flag that controls whether transport security is used
         */
        private Boolean enabled = false;

        /**
         * Path to SSL certificate chain
         */
        private String certificateChainPath = "";

        /**
         * Path to SSL certificate
         */
        private String certificatePath = "";

        public Boolean getEnabled() {
            return enabled;
        }

        public void setEnabled(Boolean enabled) {
            this.enabled = enabled;
        }

        public String getCertificateChainPath() {
            return certificateChainPath;
        }

        public void setCertificateChainPath(String certificateChainPath) {
            this.certificateChainPath = certificateChainPath;
        }

        public String getCertificatePath() {
            return certificatePath;
        }

        public void setCertificatePath(String certificatePath) {
            this.certificatePath = certificatePath;
        }
    }
}