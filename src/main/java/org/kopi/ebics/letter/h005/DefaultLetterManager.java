package org.kopi.ebics.letter.h005;

import org.kopi.ebics.certificate.h005.UserCertificateManager;
import org.kopi.ebics.client.h005.user.EbicsUser;
import org.kopi.ebics.exception.h005.EbicsException;
import org.kopi.ebics.interfaces.h005.InitLetter;
import org.kopi.ebics.interfaces.h005.LetterManager;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Locale;

public class DefaultLetterManager implements LetterManager {

    private Locale locale;

    @Override
    public InitLetter createA005Letter(EbicsUser user, UserCertificateManager userCert) throws GeneralSecurityException, IOException, EbicsException {
        return new A005Letter(locale, user, userCert, user.isUseCertificate());
    }

    @Override
    public InitLetter createE002Letter(EbicsUser user, UserCertificateManager userCert) throws GeneralSecurityException, IOException, EbicsException {
        return new E002Letter(locale, user, userCert, user.isUseCertificate());
    }

    @Override
    public InitLetter createX002Letter(EbicsUser user, UserCertificateManager userCert) throws GeneralSecurityException, IOException, EbicsException {
        return new X002Letter(locale, user, userCert, user.isUseCertificate());
    }

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }
}
