package com.mifengs.order.component.action;

import com.mifengs.order.component.base.BaseAction;
import com.mifengs.order.component.base.MyConstants;
import com.mifengs.order.component.base.Result;
import com.mifengs.order.component.entity.SysPaymentForm;
import com.mifengs.order.component.enums.FourthPayTypeEnum;
import com.mifengs.order.component.service.FourthPayService;
import com.mifengs.order.component.util.AmountUtils;
import com.mifengs.order.component.util.AssertUtil;
import com.mifengs.order.component.util.RegexUtils;
import com.mifengs.order.component.util.WeiXinUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.xmlpull.v1.XmlPullParserException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Map;

/**
 * @author Zhaojiaheng
 * @ClassName: PaymentController
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @date 2018年10月22日 下午 16:59:50
 * 注意：本内容仅限于蜜蜂商城内部传阅，禁止外泄以及用于其他的商业目
 */
@Slf4j
@RestController
public class PaymentAction extends BaseAction {
    
    @Autowired
    private FourthPayService fourthPayService;
    
    /**
     * 统一下单
     * @param request
     * @param req - totalAmount，payTypeId，subject，ip，subSettlementSn
     * @return
     */
    @RequestMapping(value = "/pay/unifiedorder")
    public Result unifiedorder(HttpServletRequest request, SysPaymentForm req) {
        log.info("pay.unifiedorder req:" + req.toString());
        Result result = new Result();
        checkSysPaymentForm(req);
        String rsp = fourthPayService.payto(req);
        log.info("pay.unifiedorder rsp:" + rsp);
        result.getParamer().put("qrCode", rsp);
        return result;
    }
    
    private String getIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip != null && ip.length() != 0 && !"unknown".equalsIgnoreCase(ip)) {
            // 多次反向代理后会有多个ip值，第一个ip才是真实ip
            if (ip.indexOf(",") != -1) {
                ip = ip.split(",")[0];
            }
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
    
    
    public void checkSysPaymentForm(SysPaymentForm paymentForm) {
        AssertUtil.assertNotBlank(paymentForm.getSubSettlementSn(), MyConstants.RESULT.FI1006, "订单号为空");
        String amount = AmountUtils.changeY2F(paymentForm.getTotalAmount()+"");
        AssertUtil.assertNotBlank(amount, MyConstants.RESULT.FI1006, "订单金额为空");
        AssertUtil.assertNotFalse(RegexUtils.checkPositiveDigit(amount), MyConstants.RESULT.FI1014, "订单金额非法");
        AssertUtil.assertNotBlank(paymentForm.getSubject(), MyConstants.RESULT.FI1006, "商品名为空");
        String payType = paymentForm.getPayTypeId();
        FourthPayTypeEnum payTypeEnum = Arrays.stream(FourthPayTypeEnum.values()).filter(fourthPayTypeEnum -> fourthPayTypeEnum.getValue().equals(payType)).findAny().orElse(null);
        AssertUtil.assertNotNull(payTypeEnum, MyConstants.RESULT.FI1014, "支付类型无效");
        String ip = paymentForm.getIp();
        AssertUtil.assertNotBlank(paymentForm.getIp(), MyConstants.RESULT.FI1006, "客户端IP为空");
        AssertUtil.assertNotFalse(RegexUtils.checkIpAddress(ip), MyConstants.RESULT.FI1014, "客户端IP非法");
    }
    
    @RequestMapping(value = "/pay/wechat/scancodepay/notify")
    public void wxcodenotify(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String resXml = "";
        /** 支付成功后，微信回调返回的信息 */
        Map<String, String> map = null;
        try {
            map = WeiXinUtil.parseXml(request);
            resXml = fourthPayService.wxcodeNotify(map,FourthPayTypeEnum.WECHAT_CODE);
        } catch (XmlPullParserException e1) {
            e1.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        BufferedOutputStream out = new BufferedOutputStream(
                response.getOutputStream());
        out.write(resXml.getBytes());
        out.flush();
        out.close();
    }
    
    @RequestMapping(value = "/pay/wechat/h5pay/notify")
    public void wxh5notify(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String resXml = "";
        /** 支付成功后，微信回调返回的信息 */
        Map<String, String> map = null;
        try {
            map = WeiXinUtil.parseXml(request);
            resXml = fourthPayService.wxcodeNotify(map,FourthPayTypeEnum.WECHAT_H5);
        } catch (XmlPullParserException e1) {
            e1.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        BufferedOutputStream out = new BufferedOutputStream(
                response.getOutputStream());
        out.write(resXml.getBytes());
        out.flush();
        out.close();
    }
}
