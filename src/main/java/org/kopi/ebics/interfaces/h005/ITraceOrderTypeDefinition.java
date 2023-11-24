package org.kopi.ebics.interfaces.h005;

import org.kopi.ebics.enumeration.h005.EbicsAdminOrderType;
import org.kopi.ebics.order.h005.IEbicsService;

public interface ITraceOrderTypeDefinition {

    EbicsAdminOrderType getAdminOrderType();
    IEbicsService getEbicsServiceType();
    String getBusinessOrderType();
}
