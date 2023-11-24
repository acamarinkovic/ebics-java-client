package org.kopi.ebics.utils.h005;

import org.apache.xml.security.c14n.Canonicalizer;
import org.apache.xml.security.utils.IgnoreAllErrorHandler;
import org.apache.xmlbeans.XmlObject;
import org.apache.xpath.XPathAPI;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.kopi.ebics.exception.h005.EbicsException;
import org.kopi.ebics.messages.Messages;
import org.kopi.ebics.utils.Constants;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.traversal.NodeIterator;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Date;
import java.util.zip.*;

public class Utils {

    static {
        org.apache.xml.security.Init.init();
    }

    public static byte[] zip(byte[] unzippedInput) throws EbicsException {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream(unzippedInput.length)) {
            try (DeflaterOutputStream output = new DeflaterOutputStream(out)) {
                output.write(unzippedInput);
            }
            return out.toByteArray();
        } catch (IOException e) {
            throw new EbicsException(e.getMessage());
        }
    }

    public static byte[] generateNonce() throws EbicsException {
        try {
            SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
            return secureRandom.generateSeed(16);
        } catch (NoSuchAlgorithmException e) {
            throw new EbicsException(e.getMessage());
        }
    }

    public static byte[] unzip(byte[] zippedInput) throws EbicsException {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream(zippedInput.length * 2)) {
            try (InflaterOutputStream output = new InflaterOutputStream(out)) {
                output.write(zippedInput);
            }
            return out.toByteArray();
        } catch (IOException e) {
            throw new EbicsException(e.getMessage());
        }
    }

    public static byte[] canonize(byte[] input) throws EbicsException {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(true);
            factory.setValidating(true);
            DocumentBuilder builder = factory.newDocumentBuilder();
            builder.setErrorHandler(new IgnoreAllErrorHandler());
            Document document = builder.parse(new ByteArrayInputStream(input));
            NodeIterator iter = XPathAPI.selectNodeIterator(document, "//*[@authenticate='true']");
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            Node node;
            while ((node = iter.nextNode()) != null) {
                Canonicalizer canonicalizer = Canonicalizer.getInstance(Canonicalizer.ALGO_ID_C14N_OMIT_COMMENTS);
                output.write(canonicalizer.canonicalizeSubtree(node));
            }
            return output.toByteArray();
        } catch (Exception e) {
            throw new EbicsException(e.getMessage());
        }
    }

    public static String toHexString(byte[] byteArray) {
        StringBuilder stringBuilder = new StringBuilder();
        for (byte eachByte : byteArray) {
            stringBuilder.append(String.format("%02x", eachByte));
        }
        return stringBuilder.toString();
    }

}
