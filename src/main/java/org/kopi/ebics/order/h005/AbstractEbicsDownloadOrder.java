package org.kopi.ebics.order.h005;

import org.kopi.ebics.enumeration.h005.EbicsAdminOrderType;

import java.util.Date;
import java.util.Map;

abstract class AbstractEbicsDownloadOrder extends EbicsOrder {

    protected Date startDate;
    protected Date endDate;

    public AbstractEbicsDownloadOrder(EbicsAdminOrderType adminOrderType, Map<String, String> params, Date startDate, Date endDate) {
        super(adminOrderType, params);
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
}
