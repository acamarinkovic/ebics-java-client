package org.kopi.ebics.letter.h005;

import org.apache.commons.codec.binary.Base64;
import org.kopi.ebics.certificate.h005.KeyUtil;
import org.kopi.ebics.certificate.h005.UserCertificateManager;
import org.kopi.ebics.client.h005.user.EbicsUser;
import org.kopi.ebics.exception.h005.EbicsException;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Locale;

public class A005Letter extends AbstractInitLetter {

    private final Locale locale;
    private final EbicsUser user;
    private final UserCertificateManager userCert;
    private final boolean useCert;

    public A005Letter(Locale locale, EbicsUser user, UserCertificateManager userCert, boolean useCert) throws EbicsException, GeneralSecurityException, IOException {
        super(locale,
                user.getPartner().getBank().getHostId(),
                user.getPartner().getBank().getName(),
                user.getUserId(),
                user.getName(),
                user.getPartner().getPartnerId(),
                getString("INILetter.version", BUNDLE_NAME, locale),
                getString("INILetter.certificate", BUNDLE_NAME, locale),
                useCert ? Base64.encodeBase64(userCert.getA005CertificateBytes(), true) : null,
                getString("INILetter.digest", BUNDLE_NAME, locale),
                useCert ? KeyUtil.getCertificateHash(userCert.getA005CertificateBytes()) : KeyUtil.getKeyHash(userCert.getA005PublicKey())
        );

        this.locale = locale;
        this.user = user;
        this.userCert = userCert;
        this.useCert = useCert;
    }

    @Override
    public String getTitle() {
        return getString("INILetter.title", BUNDLE_NAME, locale);
    }

    @Override
    public String getName() {
        return getString("INILetter.name", BUNDLE_NAME, locale) + ".txt";
    }

    public Locale getLocale() {
        return locale;
    }

    public EbicsUser getUser() {
        return user;
    }

    public UserCertificateManager getUserCert() {
        return userCert;
    }

    public boolean isUseCert() {
        return useCert;
    }
}
