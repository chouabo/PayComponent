package com.mifengs.order.component.config;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author Zhaojiaheng
 * @ClassName: FourthPayConfig
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @date 2018年10月23日 上午 11:51:49
 * 注意：本内容仅限于蜜蜂商城内部传阅，禁止外泄以及用于其他的商业目
 */
@Data
@Component
@ConfigurationProperties(prefix = "com.mifengs.fourthpay")
public class FourthPayConfig {
    
    private AlipayConfig aliCode;
    
    private WechatConfig wechatCode;
    
    private WechatConfig wechatH5;
    
    private String componentKey;
    
    
}
