/*
 * 
 * LegendShop 多用户商城系统
 * 
 *  版权所有,并保留所有权利。
 * 
 */
package com.mifengs.order.component.entity;


import com.mifengs.order.component.util.AppUtils;
import com.mifengs.order.component.util.Arith;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * 支付参数Form
 *
 * @author tony
 */
@Getter
@Setter
@ToString
public class SysPaymentForm {
    
    private Double totalAmount; // 支付总金额
    private Double accountAmount; // 帐号金额
    private Long payWayId; // 支付方式
    private String userId;
    private String opendId;
    private String tradingNumbers; // 合并的订单号
    private String subSettlementSn; // 结算交易号
    private Integer interfaceType; // 支付服务接口
    private String payTypeId;
    private String payTypeName;
    private String subject; // 商品名称
    private String ip;
    private String bankCode;
    private String weixinMethod;
    private Double orderAmount; // 订单总金额
    
    private Date orderDatetime;
    
    private Integer appPay;
    
    
    /**
     * 网站商品的展示地址，不允许加?id=123这类自定义参数
     */
    private String showUrl;
    
    /**
     * The notify_url 交易过程中服务器通知的页面 要用 http://格式的完整路径，不允许加?id=123这类自定义参数.
     */
    private String notifyUrl;
    
    /**
     * The return_url. 付完款后跳转的页面 要用 http://格式的完整路径，不允许加?id=123这类自定义参数
     */
    private String returnUrl;
    private HttpServletRequest request;
    private HttpServletResponse response;
    
    public Double getTotalAmount() {
        Double n = Arith.sub(totalAmount, accountAmount);
        return n;
    }
    
    public SysPaymentForm() {
    }
    
    public SysPaymentForm(Double totalAmount, Double accountAmount,
                          Long payWayId, String userId, String tradingNumbers,
                          Integer interfaceType, String payTypeId, String payTypeName,
                          String subject, String ip) {
        super();
        this.totalAmount = totalAmount;
        this.accountAmount = accountAmount;
        this.payWayId = payWayId;
        this.userId = userId;
        this.tradingNumbers = tradingNumbers;
        this.interfaceType = interfaceType;
        this.payTypeId = payTypeId;
        this.payTypeName = payTypeName;
        this.subject = subject;
        this.ip = ip;
    }
    
    public SysPaymentForm(Double totalAmount, Double accountAmount,
                          Long payWayId, String userId, String tradingNumbers,
                          Integer interfaceType, String payTypeId, String payTypeName,
                          String subject, String ip, HttpServletRequest request,
                          HttpServletResponse response) {
        super();
        this.totalAmount = totalAmount;
        this.accountAmount = accountAmount;
        this.payWayId = payWayId;
        this.userId = userId;
        this.tradingNumbers = tradingNumbers;
        this.interfaceType = interfaceType;
        this.payTypeId = payTypeId;
        this.payTypeName = payTypeName;
        this.subject = subject;
        this.ip = ip;
        this.request = request;
        this.response = response;
        this.orderAmount = totalAmount;
    }
    
    public String getSubject() {
        if (AppUtils.isNotBlank(subject) && subject.length() > 32) {
            subject = subject.substring(0, 32);
        }
        return subject;
    }
    
}
