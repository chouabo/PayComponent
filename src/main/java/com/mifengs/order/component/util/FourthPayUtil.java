package com.mifengs.order.component.util;

import com.mifengs.order.component.config.FourthPayConfig;
import com.mifengs.order.component.config.WechatConfig;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Zhaojiaheng
 * @ClassName: FourthPayUtil
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @date 2018年10月23日 上午 10:56:39
 * 注意：本内容仅限于蜜蜂商城内部传阅，禁止外泄以及用于其他的商业目
 */
public class FourthPayUtil {
    
    /**
     * 正则替换所有特殊字符
     *
     * @param orgStr
     * @return
     */
    public static String replaceSpecStr(String orgStr) {
        if (null != orgStr && !"".equals(orgStr.trim())) {
            String regEx = "[\\s~·`!！@#￥$%^……&*（()）\\-——\\-_=+【\\[\\]】｛{}｝\\|、\\\\；;：:‘'“”\"，,《<。.》>、/？?]";
            Pattern p = Pattern.compile(regEx);
            Matcher m = p.matcher(orgStr);
            return m.replaceAll("").trim();
        }
        return "商品";
    }
    
    /**
     * 生成随机数
     *
     * @param length
     * @return
     */
    public static String getRandomStringByLength(int length) {
        String base = "abcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }
    
    /**
     * map转xml形式报文
     *
     * @param map
     * @return
     */
    public static String callMapToXML(Map map) {
        StringBuffer sb = new StringBuffer();
        sb.append("<xml>");
        mapToXML(map, sb);
        sb.append("</xml>");
        try {
            return sb.toString();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
    
    /**
     * map转xml形式报文
     *
     * @param map
     * @param sb
     */
    public static void mapToXML(Map map, StringBuffer sb) {
        Set set = map.keySet();
        for (Iterator it = set.iterator(); it.hasNext(); ) {
            String key = (String) it.next();
            Object value = map.get(key);
            if (null == value)
                value = "";
            if (value.getClass().getName().equals("java.util.ArrayList")) {
                ArrayList list = (ArrayList) map.get(key);
                sb.append("<" + key + ">");
                for (int i = 0; i < list.size(); i++) {
                    HashMap hm = (HashMap) list.get(i);
                    mapToXML(hm, sb);
                }
                sb.append("</" + key + ">");
            } else {
                if (value instanceof HashMap) {
                    sb.append("<" + key + ">");
                    mapToXML((HashMap) value, sb);
                    sb.append("</" + key + ">");
                } else {
                    sb.append("<" + key + ">" + value + "</" + key + ">");
                }
            }
        }
    }
    
    /**
     * 解析xml報文
     *
     * @param xml
     * @return
     * @throws Exception
     */
    public static Map<String, String> parseXml(String xml) throws Exception {
        // 将解析结果存储在HashMap中
        Map<String, String> map = new HashMap<String, String>();
        Document document = DocumentHelper.parseText(xml);
        // 得到xml根元素
        Element root = document.getRootElement();
        // 得到根元素的所有子节点
        List<Element> elementList = root.elements();
        // 遍历所有子节点
        for (Element e : elementList)
            map.put(e.getName(), e.getText());
        return map;
    }
    
    /**
     * 获取签名
     *
     * @param data
     * @param key
     * @param characterEncoding
     * @return
     */
    public static String encodeData(SortedMap<String, String> data, String key, String characterEncoding) {
        StringBuffer sb = new StringBuffer();
        Set es = data.entrySet();
        Iterator it = es.iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            String k = (String) entry.getKey();
            Object v = entry.getValue();
            if (null != v && !"".equals(v) && !"sign".equals(k) && !"key".equals(k)) {
                sb.append(k + "=" + v + "&");
            }
        }
        sb.append("key=" + key);// 最后加密时添加商户密钥，由于key值放在最后，所以不用添加到SortMap里面去，单独处理，编码方式采用UTF-8
        System.out.println("字符串" + sb.toString());
        String sign = WxMD5Util.MD5Encode(sb.toString(), characterEncoding).toUpperCase();
        return sign;
    }
    
    /**
     * 获取签名
     *
     * @param data
     * @param key
     * @return
     */
    public static String encodeData(SortedMap<String, String> data, String key) {
        return encodeData(data, key, "UTF-8");
    }
    
    /**
     * 验签
     *
     * @param data
     * @param key
     * @return
     */
    public static boolean checkData(SortedMap<String, String> data, String key) {
        String sign = data.get("sign");
        if (AppUtils.isBlank(sign)) return false;
        return encodeData(data, key, "UTF-8").equals(sign);
    }
    
    public static SortedMap<String, String> ObjToSortedMap(Object obj) {
        SortedMap<String, String> data = new TreeMap<String, String>();
        if (obj == null) {
            return null;
        }
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            for (PropertyDescriptor property : propertyDescriptors) {
                String key = property.getName();
                if (key.compareToIgnoreCase("class") == 0) {
                    continue;
                }
                Method getter = property.getReadMethod();
                Object value = getter != null ? getter.invoke(obj) : null;
                if (AppUtils.isNotBlank(value)) data.put(key, value + "");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
        
    }
    
    public static Map<String, Object> ObjToHashMap(Object obj) {
        Map<String, Object> data = new HashMap<String, Object>();
        if (obj == null) {
            return null;
        }
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            for (PropertyDescriptor property : propertyDescriptors) {
                String key = property.getName();
                if (key.compareToIgnoreCase("class") == 0) {
                    continue;
                }
                Method getter = property.getReadMethod();
                Object value = getter != null ? getter.invoke(obj) : null;
                if (AppUtils.isNotBlank(value)) data.put(key, value + "");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
        
    }
    
    public static WechatConfig getConfig(String payType,FourthPayConfig fourthPayConfig){
        try {
            Field field = fourthPayConfig.getClass().getField(payType);
            return (WechatConfig) field.get(fourthPayConfig);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public static void main(String[] args) {
        SortedMap<String, String> data = new TreeMap<>();
        data.put("merchantNum", "88888");
        data.put("nonce_str", "124331242");
        data.put("merMark", "TEST");
        data.put("clientIp", "113.88.99.140");
        data.put("orderTime", "2018-10-29 10:00:00");
        data.put("payType", "wechatH5");
        data.put("orderNum", "TEST201810291627");
        data.put("amount", "1");
        data.put("body", "test1111");
        data.put("signType", "MD5");
        String ms = FourthPayUtil.encodeData(data, "abc12345");
        System.out.println(ms);
        System.out.println("18F8D00646AB01755611F32066AF9437".equals(ms));
    }
}
