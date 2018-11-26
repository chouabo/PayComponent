package com.mifengs.order.component.service.impl;

import com.mifengs.order.component.base.MyConstants;
import com.mifengs.order.component.config.FourthPayConfig;
import com.mifengs.order.component.entity.SysPaymentForm;
import com.mifengs.order.component.enums.PayTypeStatusEnum;
import com.mifengs.order.component.service.ThirdPayService;
import com.mifengs.order.component.util.AssertUtil;
import com.mifengs.order.component.util.FourthPayUtil;
import com.mifengs.order.component.util.WxPayCommonUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * @author Zhaojiaheng
 * @ClassName: WechatH5PayServiceImpl
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @date 2018年10月26日 上午 09:24:12
 * 注意：本内容仅限于蜜蜂商城内部传阅，禁止外泄以及用于其他的商业目
 */
@Slf4j
@Service(value = "wechatH5")
public class WechatH5PayServiceImpl implements ThirdPayService {
    
    @Autowired
    private FourthPayConfig fourthPayConfig;
    
    @Override
    public String payto(SysPaymentForm paymentForm) {
        String outTradeNo = paymentForm.getSubSettlementSn();
        
        AssertUtil.assertEquals(fourthPayConfig.getWechatH5().getStatus(), PayTypeStatusEnum.YES.getStatus(), MyConstants.RESULT.FI1006, "渠道未开启");
        
        BigDecimal b1 = new BigDecimal(String.valueOf(paymentForm.getTotalAmount()));
        BigDecimal b2 = new BigDecimal("100");
    
    
        SortedMap<String, String> signParams = new TreeMap<String, String>();
        signParams.put("appid", fourthPayConfig.getWechatH5().getAppId());
        signParams.put("mch_id", fourthPayConfig.getWechatH5().getMchId());
        signParams.put("nonce_str", FourthPayUtil.getRandomStringByLength(32));//32位不重复的编号
        signParams.put("body", paymentForm.getSubject());//商品参数信息
        signParams.put("out_trade_no", outTradeNo);//订单编号
        signParams.put("total_fee", String.valueOf(b1.multiply(b2).intValue()));//支付金额 单位为分
        signParams.put("spbill_create_ip", paymentForm.getIp());//终端IP
    
        signParams.put("notify_url", fourthPayConfig.getWechatH5().getNotifyUrl());//回调页面
        signParams.put("trade_type", "MWEB");//付款类型为MWEB
//        signParams.put("scene_info", "MWEB");//付款类型为场景信息
    
        String sign = WxPayCommonUtil.createSign("UTF-8", signParams, fourthPayConfig.getWechatH5().getPartnerKey());
        signParams.put("sign", sign);
    
        String xml = FourthPayUtil.callMapToXML(signParams);
        log.info("统一下单报文：" + xml);
        System.out.println("统一下单报文：" + xml);
    
        CloseableHttpClient httpclient = HttpClients.createDefault();
        try {
            HttpPost httpost = new HttpPost(fourthPayConfig.getWechatH5().getGateway());
            httpost.addHeader("Content-Type", "text/html; charset=UTF-8");
        
            StringEntity stringEntity = new StringEntity(xml, "UTF-8");
            httpost.setEntity(stringEntity);
        
            CloseableHttpResponse response = httpclient.execute(httpost);
            HttpEntity entity = response.getEntity();
            String jsonStr = EntityUtils.toString(entity, "UTF-8");
        
            log.info("微信返回=" + jsonStr);
            System.out.println("微信返回=" + jsonStr);
            Map<String, String> map = FourthPayUtil.parseXml(jsonStr);
        
            String return_code = map.get("return_code"); // 返回状态码
            if ("SUCCESS".equals(return_code)) {
                String result_code = map.get("result_code");
                if ("SUCCESS".equals(result_code)) {
                    String mweb_url = map.get("mweb_url");
                    return mweb_url;
                }
            }
        } catch (Exception e) {
            log.info(e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
    
    public static void main(String[] args) {
        WechatH5PayServiceImpl wechatH5PayService = new WechatH5PayServiceImpl();
        Double totalAmount = 100d, accountAmount = 0d;
        Long payWayId = 1l;
        Integer interfaceType = 1;
        String payTypeId = "", payTypeName = "", subject = "测试商品1", ip = "127.0.0.1", tradingNumbers = "201810221558", userId = "0014586f-6e2a-4aba-ba33-6e128552a7c2";
        SysPaymentForm paymentForm = new SysPaymentForm(totalAmount,accountAmount,payWayId,userId,tradingNumbers,interfaceType,payTypeId,payTypeName,subject,ip);
        paymentForm.setSubSettlementSn(tradingNumbers);
        System.out.println(wechatH5PayService.payto(paymentForm));
    }
}
