package org.kopi.ebics.interfaces.h005;


import org.kopi.ebics.exception.h005.EbicsException;

import java.io.PrintStream;

public interface EbicsElement {
    /**
     * Builds the <code>EbicsElement</code> XML fragment
     * @throws EbicsException
     */
    void build() throws EbicsException;

    /**
     * Prints the <code>EbicsElement</code> into
     * the given stream.
     * @param stream the print stream
     */
    void print(PrintStream stream);
}
