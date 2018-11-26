package com.mifengs.order.component.exception;

public class YwServiceException extends YwException {
    private static final long serialVersionUID = 7498517390326327837L;
    
    public YwServiceException(String exceptionCode, String exceptionInfo) {
        super(exceptionCode, exceptionInfo);
    }
    
    public YwServiceException(String exceptionCode, String exceptionInfo,
                              Throwable throwable) {
        super(exceptionCode, exceptionInfo, throwable);
    }
    
    public YwServiceException(ErrorHolder e) {
        super(e.getCode(), e.getStatusText());
    }
}
