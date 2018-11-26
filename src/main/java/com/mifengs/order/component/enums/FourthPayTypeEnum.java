package com.mifengs.order.component.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 支付类型变量
 */
@Getter
@AllArgsConstructor
public enum FourthPayTypeEnum {
    
    /**
     * 微信二维码
     */
    WECHAT_CODE("wechatCode", "微信扫码"),
    
    /**
     * 微信H5
     */
    WECHAT_H5("wechatH5", "微信H5"),
    
    /**
     * 支付宝二维码
     */
    ALI_CODE("aliCode", "支付宝扫码"),;
    /**
     * The value.
     */
    private String value;
    
    private String desc;
    
}
