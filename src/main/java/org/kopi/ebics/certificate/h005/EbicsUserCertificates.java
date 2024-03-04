package org.kopi.ebics.certificate.h005;

import org.bouncycastle.operator.OperatorCreationException;
import org.kopi.ebics.enumeration.h005.EbicsKeyType;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.KeyPair;
import java.security.KeyStoreException;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.util.Calendar;
import java.util.Date;

public class EbicsUserCertificates extends UserCertificateManager{

    private X509Certificate a005Certificate;
    private X509Certificate x002Certificate;
    private X509Certificate e002Certificate;
    private PrivateKey a005PrivateKey;
    private PrivateKey x002PrivateKey;
    private PrivateKey e002PrivateKey;

    private EbicsUserCertificates(
            X509Certificate a005Certificate,
            X509Certificate x002Certificate,
            X509Certificate e002Certificate,
            PrivateKey a005PrivateKey,
            PrivateKey x002PrivateKey,
            PrivateKey e002PrivateKey
    ) {
        super(a005Certificate, x002Certificate, e002Certificate, a005PrivateKey, x002PrivateKey, e002PrivateKey);
    }

    private static class CertKeyPair {
        private final X509Certificate certificate;
        private final PrivateKey privateKey;

        public CertKeyPair(X509Certificate certificate, PrivateKey privateKey) {
            this.certificate = certificate;
            this.privateKey = privateKey;
        }
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (other == null || getClass() != other.getClass()) return false;

        EbicsUserCertificates that = (EbicsUserCertificates) other;

        return a005Certificate.equals(that.a005Certificate) &&
                x002Certificate.equals(that.x002Certificate) &&
                e002Certificate.equals(that.e002Certificate) &&
                a005PrivateKey.equals(that.a005PrivateKey) &&
                x002PrivateKey.equals(that.x002PrivateKey) &&
                e002PrivateKey.equals(that.e002PrivateKey);
    }

    public void save(KeyStoreManager manager, String aliasPrefix) throws KeyStoreException {
        manager.setKeyEntry(aliasPrefix + "-A005", a005PrivateKey, a005Certificate);
        manager.setKeyEntry(aliasPrefix + "-X002", x002PrivateKey, x002Certificate);
        manager.setKeyEntry(aliasPrefix + "-E002", e002PrivateKey, e002Certificate);
    }

    public static EbicsUserCertificates load(KeyStoreManager manager, String aliasPrefix) throws GeneralSecurityException {
        return new EbicsUserCertificates(
                (X509Certificate) manager.getCertificate(aliasPrefix + "-A005"),
                (X509Certificate) manager.getCertificate(aliasPrefix + "-X002"),
                (X509Certificate) manager.getCertificate(aliasPrefix + "-E002"),
                (PrivateKey) manager.getPrivateKey(aliasPrefix + "-A005"),
                (PrivateKey) manager.getPrivateKey(aliasPrefix + "-X002"),
                (PrivateKey) manager.getPrivateKey(aliasPrefix + "-E002")
        );
    }

    public static EbicsUserCertificates create(String userDn) throws GeneralSecurityException, IOException {
        try {
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DAY_OF_YEAR, X509Constants.DEFAULT_DURATION);
            Date endDate = new Date(calendar.getTimeInMillis());
            CertKeyPair a005Pair = createCertificateKeyPair(EbicsKeyType.A005, userDn, endDate);
            CertKeyPair x002Pair = createCertificateKeyPair(EbicsKeyType.X002, userDn, endDate);
            CertKeyPair e002Pair = createCertificateKeyPair(EbicsKeyType.E002, userDn, endDate);
            return new EbicsUserCertificates(
                    a005Pair.certificate,
                    x002Pair.certificate,
                    e002Pair.certificate,
                    a005Pair.privateKey,
                    x002Pair.privateKey,
                    e002Pair.privateKey
            );
        } catch (Exception ex) {
            throw new IllegalArgumentException("Can't create certificate for dn='" + userDn + "' error: " + ex.getMessage(), ex);
        }
    }

    private static CertKeyPair createCertificateKeyPair(EbicsKeyType keyType, String userDn, Date end) throws GeneralSecurityException, IOException, OperatorCreationException {
        KeyPair keyPair = KeyUtil.makeKeyPair(X509Constants.EBICS_KEY_SIZE);
        X509Certificate cert = X509Generator.generateCertificate(keyPair, userDn, new Date(), end, keyType);
        return new CertKeyPair(cert, keyPair.getPrivate());
    }
}