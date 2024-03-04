package org.kopi.ebics.order.h005;

import org.kopi.ebics.enumeration.h005.EbicsAdminOrderType;

import java.util.Date;
import java.util.Map;

public class EbicsDownloadOrder extends AbstractEbicsDownloadOrder {

    private IEbicsService orderService;

    public EbicsDownloadOrder(EbicsAdminOrderType adminOrderType,
                              IEbicsService orderService,
                              Date startDate,
                              Date endDate,
                              Map<String, String> params
                              ){
        super(adminOrderType, params, startDate, endDate);
        this.orderService = orderService;
        this.adminOrderType = EbicsAdminOrderType.BTD;
    }

    @Override
    public String toString() {
        if(orderService == null){
            return "AdminOrderType=" + adminOrderType;
        }
        else{
            if (startDate == null && endDate == null) {
                return "BTD=[" + orderService + "]";
            }
            else{
                return "BTD=[" + orderService + "] start=" + startDate + " end=" + endDate;
            }
        }
    }

    public IEbicsService getOrderService() {
        return orderService;
    }

    public void setOrderService(IEbicsService orderService) {
        this.orderService = orderService;
    }
}
