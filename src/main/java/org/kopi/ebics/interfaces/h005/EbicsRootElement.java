package org.kopi.ebics.interfaces.h005;


import org.kopi.ebics.exception.h005.EbicsException;

import java.io.OutputStream;

public interface EbicsRootElement extends EbicsElement{
    /**
     * Converts the <code>EbicsElement</code> to a byte array
     * @return the equivalent byte array of this <code>EbicsElement</code>
     */
    byte[] toByteArray();

    /**
     * Validates the request element according to the
     * EBICS XML schema specification
     * @throws EbicsException throws an EbicsException when validation fails
     */
    void validate() throws EbicsException;

    /**
     * Adds a namespace declaration for the <code>EbicsRootElement</code>
     * @param prefix namespace prefix
     * @param uri namespace uri
     */
    void addNamespaceDecl(String prefix, String uri);

    /**
     * Saves the <code>EbicsElement</code> into a given output stream.
     * @param out the output stream
     * @throws EbicsException the save operation fails
     */
    void save(OutputStream out) throws EbicsException;

}
