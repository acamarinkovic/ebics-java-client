package org.kopi.ebics.http.h005.factory;

import java.net.URL;

public interface IHttpTransferSession {
    URL getEbicsUrl();
    String getHttpConfigurationName();
}
