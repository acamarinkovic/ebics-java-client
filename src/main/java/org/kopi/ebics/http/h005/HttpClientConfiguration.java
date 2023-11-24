package org.kopi.ebics.http.h005;

import org.kopi.ebics.http.h005.factory.IHttpClientConfiguration;

public class HttpClientConfiguration implements IHttpClientConfiguration {

    private String displayName = "default";
    private String sslTrustedStoreFile;
    private String sslTrustedStoreFilePassword;
    private String httpProxyHost;
    private Integer httpProxyPort;
    private String httpProxyUser;
    private String httpProxyPassword;
    private String httpContentType;
    private int socketTimeoutMilliseconds = 300 * 1000;
    private int connectionTimeoutMilliseconds = 300 * 1000;

    public HttpClientConfiguration() {
    }

    public HttpClientConfiguration(
            String displayName,
            String sslTrustedStoreFile,
            String sslTrustedStoreFilePassword,
            String httpProxyHost,
            Integer httpProxyPort,
            String httpProxyUser,
            String httpProxyPassword,
            String httpContentType,
            int socketTimeoutMilliseconds,
            int connectionTimeoutMilliseconds
    ) {
        this.displayName = displayName;
        this.sslTrustedStoreFile = sslTrustedStoreFile;
        this.sslTrustedStoreFilePassword = sslTrustedStoreFilePassword;
        this.httpProxyHost = httpProxyHost;
        this.httpProxyPort = httpProxyPort;
        this.httpProxyUser = httpProxyUser;
        this.httpProxyPassword = httpProxyPassword;
        this.httpContentType = httpContentType;
        this.socketTimeoutMilliseconds = socketTimeoutMilliseconds;
        this.connectionTimeoutMilliseconds = connectionTimeoutMilliseconds;
    }

    @Override
    public String getDisplayName() {
        return this.displayName;
    }

    @Override
    public String getHttpContentType() {
        return this.httpContentType;
    }

    @Override
    public String getSslTrustedStoreFile() {
        return this.sslTrustedStoreFile;
    }

    @Override
    public String getSslTrustedStoreFilePassword() {
        return this.sslTrustedStoreFilePassword;
    }

    @Override
    public String getHttpProxyHost() {
        return this.httpProxyHost;
    }

    @Override
    public Integer getHttpProxyPort() {
        return this.httpProxyPort;
    }

    @Override
    public String getHttpProxyUser() {
        return this.httpProxyUser;
    }

    @Override
    public String getHttpProxyPassword() {
        return this.httpProxyPassword;
    }

    @Override
    public int getSocketTimeoutMilliseconds() {
        return this.socketTimeoutMilliseconds;
    }

    @Override
    public int getConnectionTimeoutMilliseconds() {
        return this.connectionTimeoutMilliseconds;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public void setSslTrustedStoreFile(String sslTrustedStoreFile) {
        this.sslTrustedStoreFile = sslTrustedStoreFile;
    }

    public void setSslTrustedStoreFilePassword(String sslTrustedStoreFilePassword) {
        this.sslTrustedStoreFilePassword = sslTrustedStoreFilePassword;
    }

    public void setHttpProxyHost(String httpProxyHost) {
        this.httpProxyHost = httpProxyHost;
    }

    public void setHttpProxyPort(Integer httpProxyPort) {
        this.httpProxyPort = httpProxyPort;
    }

    public void setHttpProxyUser(String httpProxyUser) {
        this.httpProxyUser = httpProxyUser;
    }

    public void setHttpProxyPassword(String httpProxyPassword) {
        this.httpProxyPassword = httpProxyPassword;
    }

    public void setHttpContentType(String httpContentType) {
        this.httpContentType = httpContentType;
    }

    public void setSocketTimeoutMilliseconds(int socketTimeoutMilliseconds) {
        this.socketTimeoutMilliseconds = socketTimeoutMilliseconds;
    }

    public void setConnectionTimeoutMilliseconds(int connectionTimeoutMilliseconds) {
        this.connectionTimeoutMilliseconds = connectionTimeoutMilliseconds;
    }
}
