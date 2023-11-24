package org.kopi.ebics.order.h005;

import org.kopi.ebics.enumeration.h005.EbicsAdminOrderType;

import java.util.Map;

public abstract class AbstractEbicsUploadOrder extends EbicsOrder {

    public AbstractEbicsUploadOrder(EbicsAdminOrderType adminOrderType, Map<String, String> params) {
        super(adminOrderType, params);
    }
}
