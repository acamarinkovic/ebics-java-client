package org.kopi.ebics.http.h005.factory;

import org.kopi.ebics.http.h005.HttpClientRequestConfiguration;

public interface IHttpClientConfiguration extends HttpClientRequestConfiguration {

    /**
     * The SSL trusted store, used for establishing connections if needed (usually for no public EBICS servers only).
     */
    String getSslTrustedStoreFile();
    String getSslTrustedStoreFilePassword();

    /**
     * HTTP Proxy, with optional technical user/password
     */
    String getHttpProxyHost();
    Integer getHttpProxyPort();
    String getHttpProxyUser();
    String getHttpProxyPassword();

    /**
     * Timeouts in milliseconds (or -1)
     * Default suggested value 300,000 = 300s
     */
    int getSocketTimeoutMilliseconds();
    int getConnectionTimeoutMilliseconds();
}
