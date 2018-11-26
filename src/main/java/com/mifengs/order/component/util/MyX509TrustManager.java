package com.mifengs.order.component.util;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import javax.net.ssl.X509TrustManager;
/**
 * @author Zhaojiaheng
 * @ClassName: MyX509TrustManager
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @date 2018年11月05日 下午 18:06:42
 * 注意：本内容仅限于蜜蜂商城内部传阅，禁止外泄以及用于其他的商业目
 */
public class MyX509TrustManager implements X509TrustManager {
    public void checkClientTrusted(X509Certificate[] chain, String authType)
            throws CertificateException
    {}
    
    public void checkServerTrusted(X509Certificate[] chain, String authType)
            throws CertificateException
    {}
    
    public X509Certificate[] getAcceptedIssuers()
    {
        return null;
    }
}
