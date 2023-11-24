package org.kopi.ebics.letter.h005;

import org.kopi.ebics.messages.h005.Messages;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.ResourceBundle;

public class Letter {

    private final String title;
    private final String hostId;
    private final String bankName;
    private final String userId;
    private final String username;
    private final String partnerId;
    private final String version;
    private final byte[] certificate;
    private final byte[] hash;
    private final Locale locale;

    private final ByteArrayOutputStream out = new ByteArrayOutputStream();
    private final Writer writer = new PrintWriter(out, true);

    private static final String BUNDLE_NAME = "org.kopi.ebics.letter.messages";
    private static final String LINE_SEPARATOR = System.getProperty("line.separator");

    public Letter(String title, String hostId, String bankName, String userId, String username, String partnerId,
                  String version, String certTitle, byte[] certificate, String hashTitle, byte[] hash, Locale locale) throws IOException {
        this.title = title;
        this.hostId = hostId;
        this.bankName = bankName;
        this.userId = userId;
        this.username = username;
        this.partnerId = partnerId;
        this.version = version;
        this.certificate = certificate;
        this.hash = hash;
        this.locale = locale;

        buildTitle();
        buildHeader();
        buildCertificate(certTitle, certificate);
        buildHash(hashTitle, hash);
        buildFooter();
        closeStreams();
    }

    private void buildTitle() throws IOException {
        emit(title);
        emit(LINE_SEPARATOR);
        emit(LINE_SEPARATOR);
        emit(LINE_SEPARATOR);
    }

    private void buildHeader() throws IOException {
        emit(Messages.getString("Letter.date", BUNDLE_NAME, locale));
        appendSpacer();
        emit(LINE_SEPARATOR);
        emit(Messages.getString("Letter.time", BUNDLE_NAME, locale));
        appendSpacer();
        emit(formatTime(new Date()));
        emit(LINE_SEPARATOR);
        emit(Messages.getString("Letter.hostId", BUNDLE_NAME, locale));
        appendSpacer();
        emit(hostId);
        emit(LINE_SEPARATOR);
        emit(Messages.getString("Letter.bank", BUNDLE_NAME, locale));
        appendSpacer();
        emit(bankName);
        emit(LINE_SEPARATOR);
        emit(Messages.getString("Letter.userId", BUNDLE_NAME, locale));
        appendSpacer();
        emit(userId);
        emit(LINE_SEPARATOR);
        emit(Messages.getString("Letter.username", BUNDLE_NAME, locale));
        appendSpacer();
        emit(username);
        emit(LINE_SEPARATOR);
        emit(Messages.getString("Letter.partnerId", BUNDLE_NAME, locale));
        appendSpacer();
        emit(partnerId);
        emit(LINE_SEPARATOR);
        emit(Messages.getString("Letter.version", BUNDLE_NAME, locale));
        appendSpacer();
        emit(version);
        emit(LINE_SEPARATOR);
        emit(LINE_SEPARATOR);
        emit(LINE_SEPARATOR);
    }

    private void buildCertificate(String title, byte[] cert) throws IOException {
        emit(title);
        emit(LINE_SEPARATOR);
        emit(LINE_SEPARATOR);
        emit("-----BEGIN CERTIFICATE-----${LINE_SEPARATOR}");
        if (cert != null)
            emit(new String(cert));
        emit("-----END CERTIFICATE-----${LINE_SEPARATOR}");
        emit(LINE_SEPARATOR);
        emit(LINE_SEPARATOR);
    }

    private void buildHash(String title, byte[] hash) throws IOException {
        emit(title);
        emit(LINE_SEPARATOR);
        emit(LINE_SEPARATOR);
        emit(new String(hash));
        emit(LINE_SEPARATOR);
        emit(LINE_SEPARATOR);
        emit(LINE_SEPARATOR);
        emit(LINE_SEPARATOR);
        emit(LINE_SEPARATOR);
        emit(LINE_SEPARATOR);
        emit(LINE_SEPARATOR);
    }

    private void buildFooter() throws IOException {
        emit(Messages.getString("Letter.date", BUNDLE_NAME, locale));
        emit("                                  ");
        emit(Messages.getString("Letter.signature", BUNDLE_NAME, locale));
    }

    private void appendSpacer() throws IOException {
        emit("        ");
    }

    private void emit(String text) throws IOException {
        writer.write(text);
    }

    private void closeStreams() throws IOException {
        writer.close();
        out.flush();
        out.close();
    }

    private String formatDate(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat(
                ResourceBundle.getBundle(BUNDLE_NAME, locale).getString("Letter.dateFormat"),
                locale
        );
        return formatter.format(date);
    }

    private String formatTime(Date time) {
        SimpleDateFormat formatter = new SimpleDateFormat(
                ResourceBundle.getBundle(BUNDLE_NAME, locale).getString("Letter.timeFormat"),
                locale
        );
        return formatter.format(time);
    }

    public byte[] getLetterBytes() {
        return out.toByteArray();
    }

    public String getTitle() {
        return title;
    }

    public String getHostId() {
        return hostId;
    }

    public String getBankName() {
        return bankName;
    }

    public String getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public String getPartnerId() {
        return partnerId;
    }

    public String getVersion() {
        return version;
    }

    public byte[] getCertificate() {
        return certificate;
    }

    public byte[] getHash() {
        return hash;
    }

    public Locale getLocale() {
        return locale;
    }

    public ByteArrayOutputStream getOut() {
        return out;
    }

    public Writer getWriter() {
        return writer;
    }
}
