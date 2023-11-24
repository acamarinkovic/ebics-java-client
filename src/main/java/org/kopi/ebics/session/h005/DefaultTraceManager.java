package org.kopi.ebics.session.h005;

import org.kopi.ebics.enumeration.h005.TraceCategory;
import org.kopi.ebics.exception.h005.EbicsException;
import org.kopi.ebics.interfaces.h005.ContentFactory;
import org.kopi.ebics.interfaces.h005.IBankConnectionTraceSession;
import org.kopi.ebics.interfaces.h005.IBaseTraceSession;
import org.kopi.ebics.interfaces.h005.TraceManager;

public class DefaultTraceManager implements TraceManager {

    @Override
    public void trace(ContentFactory contentFactory, IBaseTraceSession traceSession, boolean request) {

    }

    @Override
    public void trace(ContentFactory contentFactory, IBaseTraceSession traceSession) {
    }

    @Override
    public void traceException(Exception exception, IBaseTraceSession traceSession) {
        //TODO

    }

    @Override
    public void updateLastTrace(IBaseTraceSession traceSession, TraceCategory traceCategory, EbicsException exception) {

    }

    @Override
    public void updateLastTrace(IBaseTraceSession traceSession, TraceCategory traceCategory) {

    }

    @Override
    public void setTraceEnabled(boolean enabled) {

    }

    @Override
    public void updateSessionOrderNumber(IBankConnectionTraceSession traceSession, String orderNumber) {

    }
}
