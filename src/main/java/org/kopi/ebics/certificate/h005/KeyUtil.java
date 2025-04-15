package org.kopi.ebics.certificate.h005;


import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.kopi.ebics.exception.EbicsException;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;

public class KeyUtil {
    /**
     * Generates a `KeyPair` in RSA format.
     *
     * @param keyLen - key size
     * @return KeyPair the key pair
     * @throws NoSuchAlgorithmException
     */
    public static KeyPair makeKeyPair(int keyLen) throws NoSuchAlgorithmException {
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
        keyGen.initialize(keyLen, new SecureRandom());
        return keyGen.generateKeyPair();
    }

    /**
     * Generates a random password
     *
     * @return the password
     */
    public static String generatePassword() {
        try {
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
            String pwd = Base64.encodeBase64String(random.generateSeed(5));
            return pwd.substring(0, pwd.length() - 2);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Returns the digest value of a given public key.
     * <p>
     * In Version “H004” of the EBICS protocol the ES of the financial:
     * <p>
     * The SHA-256 hash values of the financial institution's public keys for X002 and E002 are
     * composed by concatenating the exponent with a blank character and the modulus in hexadecimal
     * representation (using lower case letters) without leading zero (as to the hexadecimal
     * representation). The resulting string has to be converted into a byte array based on US ASCII
     * code.
     *
     * @param publicKey the public key
     * @return the digest value
     * @throws EbicsException
     */
    public static byte[] getKeyHash(RSAPublicKey publicKey) throws EbicsException {
        String exponent = Hex.encodeHexString(publicKey.getPublicExponent().toByteArray());
        String modulus = Hex.encodeHexString(removeFirstByte(publicKey.getModulus().toByteArray()));
        String hash = (exponent + " " + modulus).replaceFirst("0", "");
        byte[] digest;
        try {
            digest = MessageDigest.getInstance("SHA-256", BouncyCastleProvider.PROVIDER_NAME).digest(hash.getBytes(StandardCharsets.US_ASCII));
        } catch (GeneralSecurityException e) {
            throw new EbicsException(e.getMessage());
        }

        return Arrays.toString(Hex.encodeHex(digest, false)).getBytes();
    }

    /**
     * Remove the first byte of a byte array
     *
     * @return the array
     */
    private static byte[] removeFirstByte(byte[] byteArray) {
        return Arrays.copyOfRange(byteArray, 1, byteArray.length);
    }

    /**
     * Returns the certificate hash
     *
     * @param certificate the certificate
     * @return the certificate hash
     * @throws GeneralSecurityException
     */
    public static byte[] getCertificateHash(byte[] certificate) throws GeneralSecurityException {
        String hash256 = Hex.encodeHexString(MessageDigest.getInstance("SHA-256").digest(certificate), false);
        return format(hash256).getBytes();
    }

    /**
     * Formats a hash 256 input.
     *
     * @param hash256 the hash input
     * @return the formatted hash
     */
    private static String format(String hash256) {
        StringBuilder buffer = new StringBuilder();
        for (int i = 0; i < hash256.length(); i += 2) {
            buffer.append(hash256.charAt(i));
            buffer.append(hash256.charAt(i + 1));
            buffer.append(' ');
        }
        return buffer.substring(0, 48) + LINE_SEPARATOR + buffer.substring(48) + LINE_SEPARATOR;
    }

    public static void saveKey(OutputStream fos, Key key) throws IOException {
        X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(key.getEncoded());
        fos.write(x509EncodedKeySpec.getEncoded());
        fos.close();
    }

    public static Key loadKey(InputStream fis) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        byte[] encodedPublicKey = new byte[10000];
        fis.read(encodedPublicKey);
        fis.close();
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(encodedPublicKey);
        return keyFactory.generatePublic(publicKeySpec);
    }

    private static final String LINE_SEPARATOR = System.getProperty("line.separator");
}