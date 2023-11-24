package org.kopi.ebics.certificate.h005;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openssl.PEMParser;
import org.kopi.ebics.exception.h005.EbicsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.*;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.RSAPublicKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.logging.Level;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;


public class BankCertificateManager {

    private static final Logger logger = LoggerFactory.getLogger(BankCertificateManager.class);

    private byte[] e002Digest;
    private byte[] x002Digest;
    private RSAPublicKey e002Key;
    private RSAPublicKey x002Key;
    private X509Certificate e002Certificate;
    private X509Certificate x002Certificate;
    private byte[] e002CertificateDigest;
    private byte[] x002CertificateDigest;

    private boolean useCertificates;

    public BankCertificateManager(
            byte[] e002Digest,
            byte[] x002Digest,
            RSAPublicKey e002Key,
            RSAPublicKey x002Key,
            X509Certificate e002Certificate,
            X509Certificate x002Certificate,
            byte[] e002CertificateDigest,
            byte[] x002CertificateDigest
    ) {
        this.e002Digest = e002Digest;
        this.x002Digest = x002Digest;
        this.e002Key = e002Key;
        this.x002Key = x002Key;
        this.e002Certificate = e002Certificate;
        this.x002Certificate = x002Certificate;
        this.e002CertificateDigest = e002CertificateDigest;
        this.x002CertificateDigest = x002CertificateDigest;
        this.useCertificates = e002Certificate != null && x002Certificate != null;
    }

    public BankCertificateManager(
            byte[] e002Digest,
            byte[] x002Digest,
            RSAPublicKey e002Key,
            RSAPublicKey x002Key
    ) {
        this.e002Digest = e002Digest;
        this.x002Digest = x002Digest;
        this.e002Key = e002Key;
        this.x002Key = x002Key;
    }


    public byte[] getE002Digest() {
        return e002Digest;
    }

    public byte[] getX002Digest() {
        return x002Digest;
    }

    public RSAPublicKey getE002Key() {
        return e002Key;
    }

    public RSAPublicKey getX002Key() {
        return x002Key;
    }

    public X509Certificate getE002Certificate() {
        return e002Certificate;
    }

    public X509Certificate getX002Certificate() {
        return x002Certificate;
    }

    public byte[] getE002CertificateDigest() {
        return e002CertificateDigest;
    }

    public byte[] getX002CertificateDigest() {
        return x002CertificateDigest;
    }

    public boolean isUseCertificates() {
        return useCertificates;
    }

    public static BankCertificateManager createFromCertificates(byte[] bankE002Certificate, byte[] bankX002Certificate) {
        try {
            X509Certificate e002Certificate = readCertificate(new ByteArrayInputStream(bankE002Certificate));
            X509Certificate x002Certificate = readCertificate(new ByteArrayInputStream(bankX002Certificate));
            MessageDigest sha256digest = MessageDigest.getInstance("SHA-256", BouncyCastleProvider.PROVIDER_NAME);
            byte[] e002CertificateDigest = sha256digest.digest(bankE002Certificate);
            byte[] x002CertificateDigest = sha256digest.digest(bankX002Certificate);
            RSAPublicKey e002Key = (RSAPublicKey) e002Certificate.getPublicKey();
            RSAPublicKey x002Key = (RSAPublicKey) x002Certificate.getPublicKey();

            return new BankCertificateManager(
                    KeyUtil.getKeyHash(e002Key),
                    KeyUtil.getKeyHash(x002Key),
                    e002Key,
                    x002Key,
                    e002Certificate,
                    x002Certificate,
                    e002CertificateDigest,
                    x002CertificateDigest
            );
        } catch (CertificateException | IOException | NoSuchAlgorithmException | EbicsException |
                 NoSuchProviderException e) {
            throw new RuntimeException(e);
        }
    }

    public static BankCertificateManager createFromPubKeyExponentAndModulus(
            byte[] bankE002PublicKeyExponent,
            byte[] bankE002PublicKeyModulus,
            byte[] bankX002PublicKeyExponent,
            byte[] bankX002PublicKeyModulus
    ) {
        try {
            RSAPublicKey e002Key = getPublicKey(new BigInteger(bankE002PublicKeyExponent), new BigInteger(bankE002PublicKeyModulus));
            RSAPublicKey x002Key = getPublicKey(new BigInteger(bankX002PublicKeyExponent), new BigInteger(bankX002PublicKeyModulus));

            return new BankCertificateManager(
                    KeyUtil.getKeyHash(e002Key),
                    KeyUtil.getKeyHash(x002Key),
                    e002Key,
                    x002Key
            );
        } catch (EbicsException e) {
            logger.error("Exception creating bank certificate manager from public key exponent and modulus", e);
            throw new RuntimeException(e);
        }
    }

    private static X509Certificate readCertificate(InputStream input) throws CertificateException, IOException {
        return readCertificate(input, Security.getProvider(BouncyCastleProvider.PROVIDER_NAME));
    }

    private static X509Certificate readCertificate(InputStream input, Provider provider) throws CertificateException, IOException {
        CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509", provider);
        X509Certificate certificate = (X509Certificate) certificateFactory.generateCertificate(input);

        if (certificate == null) {
            return (X509Certificate) new PEMParser(new InputStreamReader(input)).readObject();
        } else {
            return certificate;
        }
    }

    private static RSAPublicKey getPublicKey(BigInteger publicExponent, BigInteger modulus) {
        try {
            return (RSAPublicKey) KeyFactory.getInstance("RSA").generatePublic(new RSAPublicKeySpec(modulus, publicExponent));
        } catch (Exception e) {
           logger.error("Exception creating bank public key from exponent and modulus", e);
           throw new RuntimeException(e);
        }
    }

    /**
     * Reconstruct the RSAPublicKey from its encoded version (RSAPublicKey.getEncoded())
     */
    private static RSAPublicKey getPublicKey(byte[] pubKeyEncoded) {
        try {
            return (RSAPublicKey) KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(pubKeyEncoded));
        } catch (Exception e) {
            logger.error("Exception creating bank public key from encoded version", e);
            throw new RuntimeException(e);
        }
    }

    public void setE002Digest(byte[] e002Digest) {
        this.e002Digest = e002Digest;
    }

    public void setX002Digest(byte[] x002Digest) {
        this.x002Digest = x002Digest;
    }

    public void setE002Key(RSAPublicKey e002Key) {
        this.e002Key = e002Key;
    }

    public void setX002Key(RSAPublicKey x002Key) {
        this.x002Key = x002Key;
    }

    public void setE002Certificate(X509Certificate e002Certificate) {
        this.e002Certificate = e002Certificate;
    }

    public void setX002Certificate(X509Certificate x002Certificate) {
        this.x002Certificate = x002Certificate;
    }

    public void setE002CertificateDigest(byte[] e002CertificateDigest) {
        this.e002CertificateDigest = e002CertificateDigest;
    }

    public void setX002CertificateDigest(byte[] x002CertificateDigest) {
        this.x002CertificateDigest = x002CertificateDigest;
    }

    public void setUseCertificates(boolean useCertificates) {
        this.useCertificates = useCertificates;
    }
}