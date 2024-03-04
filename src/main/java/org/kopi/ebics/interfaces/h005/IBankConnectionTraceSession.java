package org.kopi.ebics.interfaces.h005;


import org.kopi.ebics.interfaces.EbicsUser;

public interface IBankConnectionTraceSession extends IBaseTraceSession{
    EbicsUser getBankConnection();
}
