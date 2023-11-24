package org.kopi.ebics.order.h005;

import org.kopi.ebics.enumeration.h005.ContainerType;

public interface IEbicsService {
    String getServiceName();
    String getServiceOption();
    String getScope();
    ContainerType getContainerType();
    IEbicsMessage getMessage();
}
