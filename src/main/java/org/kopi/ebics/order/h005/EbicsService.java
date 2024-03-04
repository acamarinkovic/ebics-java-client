package org.kopi.ebics.order.h005;

import org.kopi.ebics.enumeration.h005.ContainerType;

public class EbicsService implements IEbicsService {
    private String serviceName;
    private String serviceOption;
    private String scope;
    private ContainerType containerType;
    private EbicsMessage message;

    public EbicsService(
            String serviceName,
            String serviceOption,
            String scope,
            ContainerType containerType,
            EbicsMessage message
    ) {
        this.serviceName = serviceName;
        this.serviceOption = serviceOption;
        this.scope = scope;
        this.containerType = containerType;
        this.message = message;
    }

    @Override
    public String getServiceName() {
        return serviceName;
    }

    @Override
    public String getServiceOption() {
        return serviceOption;
    }

    @Override
    public String getScope() {
        return scope;
    }

    @Override
    public ContainerType getContainerType() {
        return containerType;
    }

    @Override
    public IEbicsMessage getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return String.join("|",
                serviceName,
                serviceOption,
                scope,
                containerType != null ? containerType.toString() : "-",
                message.getMessageNameFormat(),
                message.toString()
        );
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public void setServiceOption(String serviceOption) {
        this.serviceOption = serviceOption;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public void setContainerType(ContainerType containerType) {
        this.containerType = containerType;
    }

    public void setMessage(EbicsMessage message) {
        this.message = message;
    }
}
