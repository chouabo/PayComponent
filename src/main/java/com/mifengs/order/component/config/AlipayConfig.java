package com.mifengs.order.component.config;

import lombok.Data;

/**
 * @author Zhaojiaheng
 * @ClassName: AlipayConfig
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @date 2018年11月01日 上午 10:31:35
 * 注意：本内容仅限于蜜蜂商城内部传阅，禁止外泄以及用于其他的商业目
 */
@Data
public class AlipayConfig {
    
    private Integer status;//状态
    private String appId;//应用ID
    private String gateway;//网关
    private String appPrivateKey;//应用私钥
    private String alipayPublicKey;//支付宝公钥
    private String notifyUrl;//回调地址
    private String resultUrl;//通知地址
    
}
