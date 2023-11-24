package org.kopi.ebics.interfaces.h005;

import org.kopi.ebics.enumeration.h005.EbicsVersion;

public interface IBaseTraceSession {
    EbicsBank getBank();
    ITraceOrderTypeDefinition getOrderType();
    boolean isUpload();
    boolean isRequest();
    EbicsVersion getEbicsVersion();
    String getSessionId();
    String getOrderNumber();
    void setOrderNumber(String orderNumber);
    Long getLastTraceId();
    void setLastTraceId(Long lastTraceId);
}
