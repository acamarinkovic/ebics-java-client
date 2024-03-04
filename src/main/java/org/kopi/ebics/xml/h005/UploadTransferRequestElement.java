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

import org.kopi.ebics.enumeration.h005.EbicsAdminOrderType;
import org.kopi.ebics.session.EbicsSession;
import org.kopi.ebics.exception.EbicsException;
import org.kopi.ebics.interfaces.ContentFactory;
import org.kopi.ebics.schema.h005.DataTransferRequestType;
import org.kopi.ebics.schema.h005.EbicsRequestDocument;
import org.kopi.ebics.schema.h005.MutableHeaderType;
import org.kopi.ebics.schema.h005.StaticHeaderType;

/**
 * The <code>UTransferRequestElement</code> is the root element
 * for all ebics upload transfers.
 *
 * @author Hachani
 */
public class UploadTransferRequestElement extends TransferRequestElement {

    /**
     * Constructs a new <code>UTransferRequestElement</code> for ebics upload transfer.
     *
     * @param session       the current ebics session
     * @param orderType     the upload order type
     * @param segmentNumber the segment number
     * @param lastSegment   is it the last segment?
     * @param transactionId the transaction ID
     * @param content       the content factory
     */
    public UploadTransferRequestElement(EbicsSession session,
                                        EbicsAdminOrderType orderType,
                                        int segmentNumber,
                                        boolean lastSegment,
                                        byte[] transactionId,
                                        ContentFactory content) {
        super(session, generateName(orderType), orderType, segmentNumber, lastSegment, transactionId);
        this.content = content;
    }

    @Override
    public void buildTransfer() throws EbicsException {
        EbicsRequestDocument.EbicsRequest request;
        EbicsRequestDocument.EbicsRequest.Header header;
        EbicsRequestDocument.EbicsRequest.Body body;
        MutableHeaderType mutable;
        MutableHeaderType.SegmentNumber segmentNumber;
        StaticHeaderType xstatic;
        DataTransferRequestType.OrderData orderData;
        DataTransferRequestType dataTransfer;

        segmentNumber = EbicsXmlFactory.createSegmentNumber(this.segmentNumber, lastSegment);
        mutable = EbicsXmlFactory.createMutableHeaderType("Transfer", segmentNumber);
        xstatic = EbicsXmlFactory.createStaticHeaderType(session.getBankID(), transactionId);
        header = EbicsXmlFactory.createEbicsRequestHeader(true, mutable, xstatic);
        orderData = EbicsXmlFactory.createEbicsRequestOrderData(content.getFactoryContent());
        dataTransfer = EbicsXmlFactory.createDataTransferRequestType(orderData);
        body = EbicsXmlFactory.createEbicsRequestBody(dataTransfer);
        request = EbicsXmlFactory.createEbicsRequest(header, body);
        document = EbicsXmlFactory.createEbicsRequestDocument(request);
    }

    // --------------------------------------------------------------------
    // DATA MEMBERS
    // --------------------------------------------------------------------

    private ContentFactory content;
    private static final long serialVersionUID = 8465397978597444978L;
}
