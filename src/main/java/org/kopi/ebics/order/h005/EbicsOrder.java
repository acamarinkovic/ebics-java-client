package org.kopi.ebics.order.h005;

import org.kopi.ebics.enumeration.h005.EbicsAdminOrderType;

import java.util.Map;


public class EbicsOrder {

    protected EbicsAdminOrderType adminOrderType;
    protected Map<String, String> params;

    public EbicsOrder(EbicsAdminOrderType adminOrderType, Map<String, String> params) {
        this.adminOrderType = adminOrderType;
        this.params = params;
    }

    public EbicsAdminOrderType getAdminOrderType() {
        return adminOrderType;
    }

    public void setAdminOrderType(EbicsAdminOrderType adminOrderType) {
        this.adminOrderType = adminOrderType;
    }

    public Map<String, String> getParams() {
        return params;
    }

    public void setParams(Map<String, String> params) {
        this.params = params;
    }
}
