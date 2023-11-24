package org.kopi.ebics.order.h005;

public interface IEbicsMessage {
    String getMessageName();
    String getMessageNameVariant();
    String getMessageNameVersion();
    String getMessageNameFormat();
}
