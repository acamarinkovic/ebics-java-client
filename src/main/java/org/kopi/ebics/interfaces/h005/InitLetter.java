package org.kopi.ebics.interfaces.h005;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public interface InitLetter {

    /**
     * Saves the `InitLetter` to the given output stream.
     *
     * @param output the output stream.
     * @throws IOException Save error.
     */
    void writeTo(OutputStream output) throws IOException;

    default String toStr() {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            writeTo(out);
            return out.toString(StandardCharsets.UTF_8.name());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Returns the initialization letter title.
     * @return the letter title.
     */
    String getTitle();

    /**
     * Returns the letter name.
     * @return the letter name.
     */
    String getName();
}
