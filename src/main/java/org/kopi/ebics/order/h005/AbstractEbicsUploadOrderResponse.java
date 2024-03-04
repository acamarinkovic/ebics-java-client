package org.kopi.ebics.order.h005;

public abstract class AbstractEbicsUploadOrderResponse {

    private final String orderNumber;
    private final String transactionId;

    public AbstractEbicsUploadOrderResponse(String orderNumber, String transactionId) {
        this.orderNumber = orderNumber;
        this.transactionId = transactionId;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public String getTransactionId() {
        return transactionId;
    }
}
