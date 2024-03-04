package org.kopi.ebics.interfaces.h005;

import org.kopi.ebics.enumeration.h005.TraceCategory;
import org.kopi.ebics.exception.EbicsException;
import org.kopi.ebics.exception.h005.EbicsServerException;


public interface TraceManager {

    interface ThrowingFunction<T> {
        T apply() throws EbicsServerException;
    }

    @FunctionalInterface
    interface CustomFunction<T> {
        T call(Object... params) throws Exception;
    }

    void trace(ContentFactory contentFactory, IBaseTraceSession traceSession, boolean request);

    void trace(ContentFactory contentFactory, IBaseTraceSession traceSession);

    void traceException(Exception exception, IBaseTraceSession traceSession);

    default  <T> void callAndUpdateLastTrace(IBaseTraceSession traceSession, ThrowingFunction<T> functionThrowingEbicsServerException) throws EbicsServerException {
        try {
            T result = functionThrowingEbicsServerException.apply();
            updateLastTrace(traceSession, TraceCategory.EbicsResponseOk);
        } catch (EbicsServerException ebicsServerException) {
            updateLastTrace(traceSession, TraceCategory.EbicsResponseError, ebicsServerException);
            throw ebicsServerException;
        } catch (Exception exception) {
            traceException(exception, traceSession);
            throw exception;
        }
    }

    default  <T> T callAndUpdateLastTrace(IBaseTraceSession traceSession, CustomFunction<T> function, Object... params) {
        try {
            return function.call(params);
        } catch (Exception exception) {
            traceException(exception, traceSession);
            throw new RuntimeException(exception);
        }
    }

    void updateLastTrace(IBaseTraceSession traceSession, TraceCategory traceCategory, EbicsException exception);

    void updateLastTrace(IBaseTraceSession traceSession, TraceCategory traceCategory);

    void setTraceEnabled(boolean enabled);

    void updateSessionOrderNumber(IBankConnectionTraceSession traceSession, String orderNumber);

}
