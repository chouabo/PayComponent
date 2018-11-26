package com.mifengs.order.component.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class OrderReqDto implements Serializable {
    
    private static final long serialVersionUID = -4569953559247880988L;
    
    private String version; //当前接口版本 V1.0
    
    private Long merchantNum; //商户号
    
    private String nonce_str; //随即字符串
    
    private String merMark; //商户标识
    
    private String clientIp; //客户端ip
    
    private String orderTime; //订单时间
    
    private String payType; //支付类型
    
    private String orderNum; //订单号
    
    private String amount; //订单金额（分）
    
    private String body; //订单描述
    
    private String signType; //签名类型
    
    private String bankCode; //为B2C时必填
    
    private String notifyUrl; //通知地址
    
    private String sign; //MD5签名结果
    
    private String realIp;
    
    
}
