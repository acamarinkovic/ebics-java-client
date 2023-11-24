package org.kopi.ebics.letter.h005;

import org.apache.commons.codec.binary.Base64;
import org.kopi.ebics.certificate.h005.KeyUtil;
import org.kopi.ebics.certificate.h005.UserCertificateManager;
import org.kopi.ebics.client.h005.user.EbicsUser;
import org.kopi.ebics.exception.h005.EbicsException;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Locale;

public class X002Letter extends AbstractInitLetter{

    public X002Letter(Locale locale, EbicsUser user, UserCertificateManager userCert, boolean useCert) throws EbicsException, GeneralSecurityException, IOException {
        super(locale,
                user.getPartner().getBank().getHostId(),
                user.getPartner().getBank().getName(),
                user.getUserId(),
                user.getName(),
                user.getPartner().getPartnerId(),
                getString("HIALetter.x002.version", BUNDLE_NAME, locale),
                getString("HIALetter.x002.certificate", BUNDLE_NAME, locale),
                useCert ? Base64.encodeBase64(userCert.getX002CertificateBytes(), true) : null,
                getString("HIALetter.x002.digest", BUNDLE_NAME, locale),
                useCert ? KeyUtil.getCertificateHash(userCert.getX002CertificateBytes()) : KeyUtil.getKeyHash(userCert.getX002PublicKey())
        );
    }

    @Override
    public String getTitle() {
        return getString("HIALetter.x002.title", BUNDLE_NAME, locale);
    }

    @Override
    public String getName() {
        return getString("HIALetter.x002.name", BUNDLE_NAME, locale) + ".txt";
    }
}
