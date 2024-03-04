package org.kopi.ebics.http.h005.factory;

import org.kopi.ebics.interfaces.EbicsBank;
import org.kopi.ebics.session.EbicsSession;

import java.net.URL;

public class HttpTransferSession implements IHttpTransferSession{

    private final URL ebicsUrl;
    private final String httpConfigurationName;

    public HttpTransferSession(URL ebicsUrl, String httpConfigurationName) {
        this.ebicsUrl = ebicsUrl;
        this.httpConfigurationName = httpConfigurationName;
    }

    public HttpTransferSession(EbicsBank ebicsBank) {
        this(ebicsBank.getURL(), ebicsBank.getName());
    }

    public HttpTransferSession(EbicsSession ebicsSession) {
        this(ebicsSession.getUser().getPartner().getBank());
    }

    @Override
    public URL getEbicsUrl() {
        return ebicsUrl;
    }

    @Override
    public String getHttpConfigurationName() {
        return httpConfigurationName;
    }
}
