package com.mifengs.order.component.service.impl;

import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradePrecreateModel;
import com.alipay.api.request.AlipayTradePrecreateRequest;
import com.alipay.api.response.AlipayTradePrecreateResponse;
import com.mifengs.order.component.base.MyConstants;
import com.mifengs.order.component.config.FourthPayConfig;
import com.mifengs.order.component.entity.SysPaymentForm;
import com.mifengs.order.component.enums.PayTypeStatusEnum;
import com.mifengs.order.component.service.ThirdPayService;
import com.mifengs.order.component.util.AssertUtil;
import com.mifengs.order.component.util.FourthPayUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * @author Zhaojiaheng
 * @ClassName: AlipayScanCodeServiceImpl
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @date 2018年10月22日 上午 10:17:15
 * 注意：本内容仅限于蜜蜂商城内部传阅，禁止外泄以及用于其他的商业目
 */
@Slf4j
@Service(value = "aliCode")
public class AlipayScanCodeServiceImpl implements ThirdPayService {
    
    @Autowired
    private FourthPayConfig fourthPayConfig;
    
    @Override
    public String payto(SysPaymentForm paymentForm) {
        String out_trade_no = paymentForm.getSubSettlementSn();//商户网站唯一订单号
        String total_fee = String.valueOf(paymentForm.getTotalAmount());//订单总金额
        BigDecimal bd = new BigDecimal(total_fee);
        bd = bd.setScale(2, RoundingMode.HALF_UP);
        total_fee = bd.toString();
        
        AssertUtil.assertEquals(fourthPayConfig.getAliCode().getStatus(), PayTypeStatusEnum.YES.getStatus(), MyConstants.RESULT.FI1006, "渠道未开启");
    
        try {
            AlipayClient ailpayClient = new DefaultAlipayClient(fourthPayConfig.getAliCode().getGateway(), fourthPayConfig.getAliCode().getAppId(), fourthPayConfig.getAliCode().getAppPrivateKey(),"json", "utf-8", fourthPayConfig.getAliCode().getAlipayPublicKey(), "RSA2");
            
            AlipayTradePrecreateRequest request = new AlipayTradePrecreateRequest();
            AlipayTradePrecreateModel model = new AlipayTradePrecreateModel();
            
            model.setOutTradeNo(out_trade_no);
//            model.setSellerId();//卖家支付宝用户ID
            model.setTotalAmount(total_fee);
            model.setSubject(FourthPayUtil.replaceSpecStr(paymentForm.getSubject().trim()));
//            model.setBody(replaceSpecStr(paymentForm.getSubject().trim()));

            request.setBizModel(model);
            request.setNotifyUrl(fourthPayConfig.getAliCode().getNotifyUrl());
            AlipayTradePrecreateResponse response = ailpayClient.execute(request);
            String body = response.getBody();
            log.info("支付宝返回="+body);
//            System.out.println("支付宝返回="+body);
            String qrcode = response.getQrCode();
            return qrcode;
        } catch (Exception e) {
            log.info(e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
    
    public static void main(String[] args) {
        AlipayScanCodeServiceImpl alipayScanCodeProcessor = new AlipayScanCodeServiceImpl();
        Double totalAmount = 100d, accountAmount = 0d;
        Long payWayId = 1l;
        Integer interfaceType = 1;
        String payTypeId = "", payTypeName = "", subject = "测试商品1", ip = "127.0.0.1", tradingNumbers = "201810231618", userId = "0014586f-6e2a-4aba-ba33-6e128552a7c2";
        SysPaymentForm paymentForm = new SysPaymentForm(totalAmount,accountAmount,payWayId,userId,tradingNumbers,interfaceType,payTypeId,payTypeName,subject,ip);
        paymentForm.setSubSettlementSn(tradingNumbers);
        System.out.println(alipayScanCodeProcessor.payto(paymentForm));
    }
}
