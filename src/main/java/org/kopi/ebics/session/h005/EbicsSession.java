//package org.kopi.ebics.session.h005;
//
//import org.kopi.ebics.certificate.h005.BankCertificateManager;
//import org.kopi.ebics.certificate.h005.UserCertificateManager;
//import org.kopi.ebics.exception.EbicsException;
//import org.kopi.ebics.interfaces.EbicsUser;
//
//
//import java.util.HashMap;
//import java.util.Map;
//import java.util.UUID;
//
//public class EbicsSession implements IEbicsSession{
//
//    private final EbicsUser user;
//    private final EbicsConfiguration configuration;
//    private final EbicsProduct product;
//    private final UserCertificateManager userCert;
//    private final BankCertificateManager bankCert;
//    private final String sessionId;
//    private final Map<String, String> parameters = new HashMap<>();
//
//    public EbicsSession(EbicsUser user, EbicsConfiguration configuration, EbicsProduct product,
//                        UserCertificateManager userCert, BankCertificateManager bankCert) {
//        this.user = user;
//        this.configuration = configuration;
//        this.product = product;
//        this.userCert = userCert;
//        this.bankCert = bankCert;
//        this.sessionId = UUID.randomUUID().toString();
//    }
//
//    @Override
//    public String getSessionId() {
//        return sessionId;
//    }
//
//    @Override
//    public EbicsUser getUser() {
//        return user;
//    }
//
//    @Override
//    public EbicsConfiguration getConfiguration() {
//        return configuration;
//    }
//
//    @Override
//    public EbicsProduct getProduct() {
//        return product;
//    }
//
//    @Override
//    public UserCertificateManager getUserCert() {
//        return userCert;
//    }
//
//    @Override
//    public BankCertificateManager getBankCert() {
//        return bankCert;
//    }
//
//    @Override
//    public void addSessionParam(String key, String value) {
//        parameters.put(key, value);
//    }
//
//    @Override
//    public String getSessionParam(String key) {
//        return parameters.get(key);
//    }
//
//    @Override
//    public Map<String, String> getParameters() {
//        return parameters;
//    }
//
//    @Override
//    public String getBankID() throws EbicsException {
//        return user.getPartner().getBank().getHostId();
//    }
//}
