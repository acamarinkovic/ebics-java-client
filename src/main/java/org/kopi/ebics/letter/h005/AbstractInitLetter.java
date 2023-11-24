package org.kopi.ebics.letter.h005;

import org.kopi.ebics.interfaces.h005.InitLetter;
import org.kopi.ebics.messages.h005.Messages;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Locale;

public abstract class AbstractInitLetter implements InitLetter {

    protected final Locale locale;
    private final Letter letter;

    public AbstractInitLetter(Locale locale, String hostId, String bankName, String userId, String username, String partnerId,
                              String version, String certTitle, byte[] certificate, String hashTitle, byte[] hash) throws IOException {
        this.locale = locale;
        this.letter = new Letter(getTitle(), hostId, bankName, userId, username, partnerId,
                version, certTitle, certificate, hashTitle, hash, locale);
    }

    @Override
    public void writeTo(OutputStream output) throws IOException {
        output.write(letter.getLetterBytes());
    }

    protected static String getString(String key, String bundleName, Locale locale) {
        return Messages.getString(key, bundleName, locale);
    }

    protected static final String BUNDLE_NAME = "org.kopi.ebics.letter.messages";
}
