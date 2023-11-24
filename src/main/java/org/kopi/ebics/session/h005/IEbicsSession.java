package org.kopi.ebics.session.h005;

import org.kopi.ebics.certificate.h005.BankCertificateManager;
import org.kopi.ebics.certificate.h005.UserCertificateManager;
import org.kopi.ebics.client.h005.user.EbicsUser;
import org.kopi.ebics.exception.h005.EbicsException;

import java.util.Map;

public interface IEbicsSession {
    String getSessionId();

    EbicsUser getUser();

    EbicsConfiguration getConfiguration();

    EbicsProduct getProduct();

    UserCertificateManager getUserCert();

    BankCertificateManager getBankCert();

    void addSessionParam(String key, String value);

    String getSessionParam(String key);

    Map<String, String> getParameters();

    String getBankID() throws EbicsException;
}
