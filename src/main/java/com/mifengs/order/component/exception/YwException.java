package com.mifengs.order.component.exception;

import lombok.Getter;
import lombok.Setter;

/**
 * @author TangCai
 * @ClassName: YwException
 * @Description: 业务异常(这里用一句话描述这个类的作用)
 * @date 2017年5月3日 上午11:59:57
 */
@Setter
@Getter
public abstract class YwException extends RuntimeException {
    private static final long serialVersionUID = -3664225596755566456L;
    private String code;
    private String info;
    
    public YwException(String exceptionCode) {
        super(exceptionCode);
        setCode(exceptionCode);
    }
    
    public YwException(String exceptionCode, String exceptionInfo) {
        super(exceptionCode + "-->" + exceptionInfo);
        setCode(exceptionCode);
        setInfo(exceptionInfo);
    }
    
    public YwException(String exceptionCode, Throwable throwable) {
        super(exceptionCode, throwable);
        setCode(exceptionCode);
    }
    
    public YwException(String exceptionCode, String exceptionInfo,
                       Throwable throwable) {
        super(exceptionCode + "-->" + exceptionInfo, throwable);
        setCode(exceptionCode);
        setInfo(exceptionInfo);
    }
    
}
