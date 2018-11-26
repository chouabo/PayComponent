package com.mifengs.order.component.service;


import com.mifengs.order.component.entity.SysPaymentForm;
import com.mifengs.order.component.enums.FourthPayTypeEnum;

import java.util.Map;

/**
 * @author Zhaojiaheng
 * @ClassName: FourthPayService
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @date 2018年10月23日 上午 10:46:35
 * 注意：本内容仅限于蜜蜂商城内部传阅，禁止外泄以及用于其他的商业目
 */
public interface FourthPayService {
    
    String payto(SysPaymentForm paymentForm);
    
    String wxcodeNotify(Map<String, String> map, FourthPayTypeEnum wechatH5);
}
