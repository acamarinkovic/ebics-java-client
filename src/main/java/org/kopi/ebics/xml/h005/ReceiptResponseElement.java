/*
 * Copyright (c) 1990-2012 kopiLeft Development SARL, Bizerte, Tunisia
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License version 2.1 as published by the Free Software Foundation.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 *
 * $Id$
 */

package org.kopi.ebics.xml.h005;

import org.kopi.ebics.exception.h005.EbicsException;
import org.kopi.ebics.exception.h005.EbicsReturnCode;
import org.kopi.ebics.interfaces.h005.ContentFactory;
import org.kopi.ebics.schema.h005.EbicsResponseDocument;

/**
 * The <code>ReceiptResponseElement</code> is the response element
 * for ebics receipt request.
 *
 * @author Hachani
 */
public class ReceiptResponseElement extends DefaultResponseElement {

    /**
     * Constructs a new <code>ReceiptResponseElement</code> object
     *
     * @param factory the content factory
     */
    public ReceiptResponseElement(ContentFactory factory) {
        super(factory);
    }

    @Override
    public void build() throws EbicsException {
        String code;
        String text;
        EbicsResponseDocument.EbicsResponse response;

        parse(factory);
        response = ((EbicsResponseDocument) document).getEbicsResponse();
        code = response.getHeader().getMutable().getReturnCode();
        text = response.getHeader().getMutable().getReportText();
        returnCode = EbicsReturnCode.toReturnCode(code, text);
        report();
    }

    @Override
    public void report() throws EbicsException {
        if (!returnCode.equals(EbicsReturnCode.EBICS_DOWNLOAD_POSTPROCESS_DONE)) {
            returnCode.throwException();
        }
    }

    // --------------------------------------------------------------------
    // DATA MEMBERS
    // --------------------------------------------------------------------

    private static final long serialVersionUID = 2994403708414164919L;
}
