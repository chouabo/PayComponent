package com.mifengs.order.component.service.impl;


import com.mifengs.order.component.base.MyConstants;
import com.mifengs.order.component.config.FourthPayConfig;
import com.mifengs.order.component.config.WechatConfig;
import com.mifengs.order.component.entity.SysPaymentForm;
import com.mifengs.order.component.enums.FourthPayTypeEnum;
import com.mifengs.order.component.service.FourthPayService;
import com.mifengs.order.component.service.ThirdPayService;
import com.mifengs.order.component.util.AppUtils;
import com.mifengs.order.component.util.AssertUtil;
import com.mifengs.order.component.util.FourthPayUtil;
import com.mifengs.order.component.util.HttpUtil;
import com.mifengs.order.component.util.WxPayCommonUtil;
import org.apache.http.client.methods.HttpPost;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * @author Zhaojiaheng
 * @ClassName: FourthPayServiceImpl
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @date 2018年10月23日 下午 20:34:42
 * 注意：本内容仅限于蜜蜂商城内部传阅，禁止外泄以及用于其他的商业目
 */
@Service
public class FourthPayServiceImpl implements FourthPayService {
    
    @Autowired
    private Map<String, ThirdPayService> paymentServiceList;
    
    @Autowired
    private FourthPayConfig fourthPayConfig;
    
    public void setPaymentServiceList(Map<String, ThirdPayService> paymentServiceList) {
        this.paymentServiceList = paymentServiceList;
    }
    
    @Override
    public String payto(SysPaymentForm paymentForm) {
        ThirdPayService thirdPayService = paymentServiceList.get(paymentForm.getPayTypeId());
        AssertUtil.assertNotNull(thirdPayService, MyConstants.RESULT.FI1006,"未找到对应支付方式");
        return thirdPayService.payto(paymentForm);
    }
    
    @Override
    public String wxcodeNotify(Map<String, String> map, FourthPayTypeEnum payType) {
        String resXml = "";
        //非空校验
        String out_trade_no = map.get("out_trade_no");
        String openid = map.get("openid");
        boolean config = true;
        if(AppUtils.isBlank(out_trade_no) || AppUtils.isBlank(openid)){
            resXml = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>"
                    + "<return_msg><![CDATA[报文为空]]></return_msg>" + "</xml> ";
            config=false;
        }
        if(map.get("result_code").toString().equalsIgnoreCase("FAIL")){
            resXml = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>"
                    + "<return_msg><![CDATA[报文为空]]></return_msg>" + "</xml> ";
            config=false;
        }
        
        //验签
        SortedMap<String, String> parameters = new TreeMap<>();
        for (String keyValue : map.keySet()) {
            if (!"sign".equals(keyValue)) {
                parameters.put(keyValue, map.get(keyValue));
            }
        }
        if(config){
            String checkSign = WxPayCommonUtil.createSign("UTF-8", parameters, FourthPayUtil.getConfig(payType.getValue(),fourthPayConfig).getPartnerKey());
            if (checkSign.equals(map.get("sign"))) {
                String total_fee=map.get("total_fee");
                String transaction_id=map.get("transaction_id");
                SortedMap<String, String> resultMap = new TreeMap<>();
                resultMap.put("total_fee", total_fee);
                resultMap.put("open_id", openid);
                resultMap.put("out_trade_no", out_trade_no);
                resultMap.put("pay_type", payType.getValue());
                resultMap.put("transaction_id", transaction_id);
                String resSign = WxPayCommonUtil.createSign("UTF-8", resultMap, fourthPayConfig.getComponentKey());
                resultMap.put("sign", resSign);
                String url = "";
                String result = HttpUtil.httpPost(url, resultMap);
                if(AppUtils.isNotBlank(result) && "success".equals(result)){
                    resXml = "<xml>"+ "<return_code><![CDATA[SUCCESS]]></return_code>"+ "<return_msg><![CDATA[OK]]></return_msg>" + "</xml> ";
                }else{
                    resXml = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>"
                            + "<return_msg><![CDATA[报文为空]]></return_msg>" + "</xml> ";
                }
            }else{
                resXml = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>"
                        + "<return_msg><![CDATA[签名错误]]></return_msg>" + "</xml> ";
            }
        }
        return resXml;
    }
    
}
