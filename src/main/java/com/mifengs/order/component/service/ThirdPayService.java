package com.mifengs.order.component.service;


import com.mifengs.order.component.entity.SysPaymentForm;

/**
 * @author Zhaojiaheng
 * @ClassName: ThirdPayService
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @date 2018年10月23日 下午 20:45:39
 * 注意：本内容仅限于蜜蜂商城内部传阅，禁止外泄以及用于其他的商业目
 */
public interface ThirdPayService {
    
    String payto(SysPaymentForm paymentForm);
    
}
