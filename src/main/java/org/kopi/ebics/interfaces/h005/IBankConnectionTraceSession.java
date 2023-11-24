package org.kopi.ebics.interfaces.h005;

import org.kopi.ebics.client.h005.user.EbicsUser;

public interface IBankConnectionTraceSession extends IBaseTraceSession{
    EbicsUser getBankConnection();
}
