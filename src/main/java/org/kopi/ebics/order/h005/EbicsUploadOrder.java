package org.kopi.ebics.order.h005;

import org.kopi.ebics.enumeration.h005.EbicsAdminOrderType;

import java.util.Map;

public class EbicsUploadOrder extends AbstractEbicsUploadOrder {

    private final EbicsService orderService;
    private final boolean signatureFlag;
    private final boolean edsFlag;
    private final String fileName;

    public EbicsUploadOrder(EbicsService orderService, boolean signatureFlag, boolean edsFlag, String fileName, Map<String, String> params) {
        super(EbicsAdminOrderType.BTU, params);
        this.orderService = orderService;
        this.signatureFlag = signatureFlag;
        this.edsFlag = edsFlag;
        this.fileName = fileName;
    }

    @Override
    public String toString() {
        return "BTU=[" + orderService + "] signatureFlag=" + signatureFlag + ", edsFlag=" + edsFlag + ", fileName=" + fileName;
    }

    public EbicsService getOrderService() {
        return orderService;
    }

    public boolean isSignatureFlag() {
        return signatureFlag;
    }

    public boolean isEdsFlag() {
        return edsFlag;
    }

    public String getFileName() {
        return fileName;
    }
}
