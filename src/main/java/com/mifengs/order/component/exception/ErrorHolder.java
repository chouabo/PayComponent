package com.mifengs.order.component.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;

/**
 * @author TangCai
 * @version v1.0
 * @Description:Http异常结果包
 * @date 2017年4月26日 上午9:34:38
 */
@Setter
@Getter
public class ErrorHolder {
    
    private HttpStatus statusCode;
    
    private String statusText;
    
    private String code;
    
    private String responseBody;
    
    private HttpHeaders responseHeaders;
    
    public ErrorHolder(HttpStatus statusCode, String statusText,
                       String responseBody) {
        this.statusCode = statusCode;
        this.statusText = statusText;
        this.responseBody = responseBody;
    }
    
    public ErrorHolder(HttpStatus statusCode, String statusText,
                       String responseBody, String code) {
        this.statusCode = statusCode;
        this.statusText = statusText;
        this.responseBody = responseBody;
        this.code = code;
    }
    
    public ErrorHolder(String statusText) {
        this.statusText = statusText;
        this.code = "500";
    }
    
    public static ErrorHolder build(Exception exception) {
        if (exception instanceof HttpClientErrorException) {
            HttpClientErrorException e = (HttpClientErrorException) exception;
            return new ErrorHolder(e.getStatusCode(), e.getStatusText(),
                    e.getResponseBodyAsString(), e.getStatusCode().toString());
        }
        return new ErrorHolder(exception.getMessage());
    }
    
}