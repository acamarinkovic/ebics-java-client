package org.kopi.ebics.certificate.h005;

import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.oiw.OIWObjectIdentifiers;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x509.*;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.X509ExtensionUtils;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.DigestCalculator;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.bc.BcDigestCalculatorProvider;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.kopi.ebics.enumeration.h005.EbicsKeyType;

import java.io.IOException;
import java.math.BigInteger;
import java.security.GeneralSecurityException;
import java.security.KeyPair;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class X509Generator {
    /**
     * Generates the signature certificate for the EBICS protocol
     *
     * @param keypair   the key pair
     * @param issuer    the certificate issuer
     * @param notBefore the begin validity date
     * @param notAfter  the end validity date
     * @return the signature certificate
     * @throws GeneralSecurityException
     * @throws IOException
     */
    public static X509Certificate generateA005Certificate(KeyPair keypair, String issuer, Date notBefore, Date notAfter)
            throws GeneralSecurityException, IOException, OperatorCreationException {
        return generateCertificate(keypair, issuer, notBefore, notAfter, EbicsKeyType.A005);
    }

    /**
     * Generates the authentication certificate for the EBICS protocol
     *
     * @param keypair   the key pair
     * @param issuer    the certificate issuer
     * @param notBefore the begin validity date
     * @param notAfter  the end validity date
     * @return the authentication certificate
     * @throws GeneralSecurityException
     * @throws IOException
     */
    public static X509Certificate generateX002Certificate(KeyPair keypair, String issuer, Date notBefore, Date notAfter)
            throws GeneralSecurityException, IOException, OperatorCreationException {
        return generateCertificate(keypair, issuer, notBefore, notAfter, EbicsKeyType.X002);
    }

    /**
     * Generates the encryption certificate for the EBICS protocol
     *
     * @param keypair   the key pair
     * @param issuer    the certificate issuer
     * @param notBefore the begin validity date
     * @param notAfter  the end validity date
     * @return the encryption certificate
     * @throws GeneralSecurityException
     * @throws IOException
     */
    public static X509Certificate generateE002Certificate(KeyPair keypair, String issuer, Date notBefore, Date notAfter)
            throws GeneralSecurityException, IOException, OperatorCreationException {
        return generateCertificate(keypair, issuer, notBefore, notAfter, EbicsKeyType.E002);
    }

    /**
     * Returns an `X509Certificate` from a given
     * `KeyPair` and limit dates validations
     *
     * @param keypair the given key pair
     * @param issuer  the certificate issuer
     * @param notBefore the begin validity date
     * @param notAfter  the end validity date
     * @param keyType  the EBICS certificate type usage
     * @return the X509 certificate
     * @throws GeneralSecurityException
     * @throws IOException
     */
    public static X509Certificate generateCertificate(KeyPair keypair, String issuer, Date notBefore, Date notAfter, EbicsKeyType keyType)
            throws GeneralSecurityException, IOException, OperatorCreationException
    {
        BigInteger serial = BigInteger.valueOf(generateSerial());
        SubjectPublicKeyInfo subPubKeyInfo = SubjectPublicKeyInfo.getInstance(keypair.getPublic().getEncoded());
        X509v3CertificateBuilder builder = new X509v3CertificateBuilder(new X500Name(issuer), serial, notBefore, notAfter, new X500Name(issuer), subPubKeyInfo);

        builder.addExtension(
                Extension.basicConstraints,
                false,
                new BasicConstraints(true));
        builder.addExtension(
                Extension.subjectKeyIdentifier,
                false,
                getSubjectKeyIdentifier(subPubKeyInfo));
        builder.addExtension(
                Extension.authorityKeyIdentifier,
                false,
                getAuthorityKeyIdentifier(subPubKeyInfo, issuer, serial));

        ASN1EncodableVector vector = new ASN1EncodableVector();
        vector.add(KeyPurposeId.id_kp_emailProtection);
        builder.addExtension(
                Extension.extendedKeyUsage,
                false,
                ExtendedKeyUsage.getInstance(new DERSequence(vector)));

        switch (keyType) {
            case A005:
                builder.addExtension(Extension.keyUsage, false, new KeyUsage(KeyUsage.nonRepudiation));
                break;
            case X002:
                builder.addExtension(Extension.keyUsage, false, new KeyUsage(KeyUsage.digitalSignature));
                break;
            case E002:
                builder.addExtension(Extension.keyUsage, false, new KeyUsage(KeyUsage.keyAgreement));
                break;
        }

        ContentSigner signer = new JcaContentSignerBuilder(X509Constants.SIGNATURE_ALGORITHM)
                .setProvider(new BouncyCastleProvider()).build(keypair.getPrivate());
        X509CertificateHolder holder = builder.build(signer);
        X509Certificate certificate = new JcaX509CertificateConverter().setProvider(new BouncyCastleProvider()).getCertificate(holder);
        certificate.checkValidity(new Date());
        certificate.verify(keypair.getPublic());
        return certificate;
    }

    /**
     * Returns the `AuthorityKeyIdentifier` corresponding to a given `PublicKey`
     *
     * @param pubKeyInfo the given public key info
     * @param issuer     the certificate issuer
     * @param serial     the certificate serial number
     * @return the authority key identifier of the public key
     */
    private static AuthorityKeyIdentifier getAuthorityKeyIdentifier(SubjectPublicKeyInfo pubKeyInfo, String issuer, BigInteger serial)
            throws OperatorCreationException {
        BcDigestCalculatorProvider digCalcProvider = new BcDigestCalculatorProvider();
        DigestCalculator digCalc = digCalcProvider.get(new AlgorithmIdentifier(OIWObjectIdentifiers.idSHA1));

        X509ExtensionUtils utils = new X509ExtensionUtils(digCalc);

        return utils.createAuthorityKeyIdentifier(pubKeyInfo);
    }

    /**
     * Returns the `SubjectKeyIdentifier` corresponding to a given `PublicKey`
     *
     * @param publicKeyInfo the given public key info
     * @return the subject key identifier
     * @throws GeneralSecurityException
     */
    private static SubjectKeyIdentifier getSubjectKeyIdentifier(SubjectPublicKeyInfo publicKeyInfo) throws GeneralSecurityException {
        try {
            X509ExtensionUtils extUtils = new X509ExtensionUtils(
                    new BcDigestCalculatorProvider().get(AlgorithmIdentifier.getInstance(OIWObjectIdentifiers.idSHA1)));

            return extUtils.createSubjectKeyIdentifier(publicKeyInfo);
        } catch (OperatorCreationException e) {
            throw new GeneralSecurityException(e);
        }
    }

    /**
     * Generates a random serial number
     *
     * @return the serial number
     */
    private static long generateSerial() {
        Date now = new Date();
        SimpleDateFormat sdfSerial = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        sdfSerial.setTimeZone(TimeZone.getTimeZone("UTC"));
        String sNow = sdfSerial.format(now);
        return Long.parseLong(sNow);
    }

}
