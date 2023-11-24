package org.kopi.ebics.xml.h005;

import org.kopi.ebics.enumeration.h005.EbicsAdminOrderType;
import org.kopi.ebics.exception.h005.EbicsException;
import org.kopi.ebics.interfaces.h005.ContentFactory;
import org.kopi.ebics.io.h005.ByteArrayContentFactory;
import org.kopi.ebics.order.h005.EbicsMessage;
import org.kopi.ebics.order.h005.EbicsService;
import org.kopi.ebics.order.h005.OrderType;
import org.kopi.ebics.schema.h005.AuthOrderInfoType;
import org.kopi.ebics.schema.h005.HTDReponseOrderDataType;
import org.kopi.ebics.schema.h005.HTDResponseOrderDataDocument;
import org.kopi.ebics.schema.h005.UserPermissionType;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class HTDResponseOrderDataElement extends DefaultResponseElement{

    private HTDReponseOrderDataType response;

    public HTDResponseOrderDataElement(ContentFactory factory) {
        super(factory);
    }

    @Override
    public void build() throws EbicsException {
        parse(factory);
        response = ((HTDResponseOrderDataDocument) document).getHTDResponseOrderData();
    }

    /**
     * Return list of order-types available for user
     * with all details (description, number of signatures, rights,..)
     */
    public List<OrderType> getOrderTypes() {
        return Arrays.stream(response.getUserInfo().getPermissionArray())
                .filter(permission -> !(permission.getService() == null && ("BTU".equals(permission.getAdminOrderType()) || "BTF".equals(permission.getAdminOrderType()))))
                .map(permission -> {
                    AuthOrderInfoType authOrderInfoType = findAuthOrderInfoType(permission);
                    return merge2OrderType(permission, authOrderInfoType);
                })
                .collect(Collectors.toList());
    }

    private AuthOrderInfoType findAuthOrderInfoType(UserPermissionType permission) {
        return Arrays.stream(response.getPartnerInfo().getOrderInfoArray())
                .filter(orderInfo -> orderInfo.getAdminOrderType().equals(permission.getAdminOrderType()) &&
                        ((orderInfo.getService() == null && permission.getService() == null) ||
                                (orderInfo.getService() != null && permission.getService() != null && orderInfo.getService().xmlText().equals(permission.getService().xmlText()))))
                .findFirst()
                .orElse(null);
    }

    private OrderType merge2OrderType(UserPermissionType userPermissionType, AuthOrderInfoType authOrderInfoType) {
        return new OrderType(
                EbicsAdminOrderType.valueOf(userPermissionType.getAdminOrderType()),
                Optional.ofNullable(userPermissionType.getService())
                        .map(st -> new EbicsService(
                                st.getServiceName(),
                                st.getServiceOption(),
                                st.getScope(),
                                EnumUtil.toContainerType(st.getContainer()),
                                new EbicsMessage(
                                        st.getMsgName().getStringValue(),
                                        st.getMsgName().getVariant(),
                                        st.getMsgName().getVersion(),
                                        st.getMsgName().getFormat()
                                )
                        ))
                        .orElse(null),
                Optional.ofNullable(authOrderInfoType).map(AuthOrderInfoType::getDescription).orElse(null),
                EnumUtil.toAuthLevel(userPermissionType),
                Optional.ofNullable(authOrderInfoType).map(AuthOrderInfoType::getNumSigRequired).orElse(null)
        );
    }

    // --------------------------------------------------------------------
    // DATA MEMBERS
    // --------------------------------------------------------------------
    private static final long serialVersionUID = -1305363936881364049L;

    private static List<OrderType> parseHtdAndGetOrderTypes(byte[] htdContent) throws EbicsException {
        HTDResponseOrderDataElement element = new HTDResponseOrderDataElement(new ByteArrayContentFactory(htdContent));
        element.build();
        element.validate();
        return element.getOrderTypes();
    }

    /**
     * Shortcut to ordertypes from HTD xml, for further processing
     */
    public static List<OrderType> getOrderTypes(byte[] htdContent) throws EbicsException {
        return parseHtdAndGetOrderTypes(htdContent);
    }
}
