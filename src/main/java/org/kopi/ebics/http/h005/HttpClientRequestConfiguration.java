package org.kopi.ebics.http.h005;

public interface HttpClientRequestConfiguration {
    String getDisplayName();

    /**
     * Specific HTTP header (if null then no header will be provided)
     * Example: "text/xml; charset=ISO-8859-1"
     */
    String getHttpContentType();
}
