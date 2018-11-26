package com.mifengs.order.component.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class OrderRespDto implements Serializable {
    
    private static final long serialVersionUID = 8349677412803259313L;
    
    private String orderNum; //商户订单号
    
    private String amount; //交易金额，单位：分
    
    private String orderStatus; //订单请求状态
    
    private String nonce_str; //随即字符串
    
    private String qyCode;//二维码
    
    private String productName;//商品名
    
    private String sign; //MD5签名结果
    
    
}
