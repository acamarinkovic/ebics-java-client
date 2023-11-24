package org.kopi.ebics.http.h005.factory;

import org.kopi.ebics.interfaces.h005.ContentFactory;

import java.net.URL;

public interface IHttpClientRequest {

    URL getRequestURL();

    ContentFactory getContent();
}
