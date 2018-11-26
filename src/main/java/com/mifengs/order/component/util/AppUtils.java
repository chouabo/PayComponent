package com.mifengs.order.component.util;

/**
 * @author Zhaojiaheng
 * @ClassName: AppUtils
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @date 2018年10月31日 下午 16:08:03
 * 注意：本内容仅限于蜜蜂商城内部传阅，禁止外泄以及用于其他的商业目
 */

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.security.MessageDigest;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.CRC32;

public class AppUtils {
    private static Logger logger = LoggerFactory.getLogger(AppUtils.class);
    
    public AppUtils() {
    }
    
    public static String getDisplayDate(Calendar pCalendar) {
        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
        return pCalendar != null ? format.format(pCalendar.getTime()) : "";
    }
    
    public static Calendar str2Calendar(String str) {
        Calendar cal = null;
        if (isNotBlank(str)) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
                Date d = sdf.parse(str);
                cal = Calendar.getInstance();
                cal.setTime(d);
            } catch (ParseException var4) {
                ;
            }
        }
        
        return cal;
    }
    
    public static String getCurrentDate() {
        return getDisplayDate(GregorianCalendar.getInstance());
    }
    
    public static boolean isBlank(String str) {
        return str == null || str.trim().length() <= 0;
    }
    
    public static boolean isNotBlank(String str) {
        return !isBlank(str);
    }
    
    public static boolean isBlank(Object[] objs) {
        return objs == null || objs.length <= 0;
    }
    
    public static boolean isNotBlank(Object[] objs) {
        return !isBlank(objs);
    }
    
    public static boolean isBlank(Object objs) {
        return objs == null || "".equals(objs);
    }
    
    public static boolean isNotBlank(Object objs) {
        return !isBlank(objs);
    }
    
    public static boolean isBlank(Collection obj) {
        return obj == null || obj.size() <= 0;
    }
    
    public static boolean isNotBlank(Collection obj) {
        return !isBlank(obj);
    }
    
    public static boolean isBlank(Set obj) {
        return obj == null || obj.size() <= 0;
    }
    
    public static boolean isNotBlank(Set obj) {
        return !isBlank(obj);
    }
    
    public static boolean isBlank(Serializable obj) {
        return obj == null;
    }
    
    public static boolean isNotBlank(Serializable obj) {
        return !isBlank(obj);
    }
    
    public static boolean isBlank(Map obj) {
        return obj == null || obj.size() <= 0;
    }
    
    public static boolean isNotBlank(Map obj) {
        return !isBlank(obj);
    }
    
    public static String[] list2Strings(List<String> list) {
        String[] value = null;
        
        try {
            if (list == null) {
                return null;
            }
            
            value = new String[list.size()];
            
            for (int i = 0; i < list.size(); ++i) {
                value[i] = (String) list.get(i);
            }
        } catch (Exception var3) {
            logger.error("list is null: " + var3);
        }
        
        return value;
    }
    
    public static String list2String(List<Object> list) {
        if (isBlank((Collection) list)) {
            return "";
        } else {
            StringBuffer sbuf = new StringBuffer();
            sbuf.append(list.get(0));
            
            for (int idx = 1; idx < list.size(); ++idx) {
                sbuf.append(",");
                sbuf.append(list.get(idx));
            }
            
            return sbuf.toString();
        }
    }
    
    public static List<String> Strings2List(String[] args) {
        ArrayList list = new ArrayList();
        
        try {
            if (args == null) {
                return null;
            }
            
            for (int i = 0; i < args.length; ++i) {
                list.add(args[i]);
            }
        } catch (Exception var3) {
            logger.error("list is null: " + var3);
        }
        
        return list;
    }
    
    public static String[] getStrings(String str) {
        List<String> values = getStringCollection(str);
        return values.size() == 0 ? null : (String[]) values.toArray(new String[values.size()]);
    }
    
    public static List<String> getStringCollection(String str) {
        List<String> values = new ArrayList();
        if (str == null) {
            return values;
        } else {
            StringTokenizer tokenizer = new StringTokenizer(str, ",");
            values = new ArrayList();
            
            while (tokenizer.hasMoreTokens()) {
                values.add(tokenizer.nextToken());
            }
            
            return values;
        }
    }
    
    public static String[] searchByKeyword(String keyword) {
        Pattern.compile("[' ']+");
        Pattern p = Pattern.compile("[.。！？#@#￥$%&*()（）=《》<>‘、’；：\"\\?!:']");
        Matcher m = p.matcher(keyword);
        String list1 = m.replaceAll(" ").replaceAll("，", ",");
        String list2 = StringUtils.replace(list1, " ", ",");
        String[] list = StringUtils.split(list2, ",");
        return list;
    }
    
    public static String formatNumber(Long number) {
        if (number == null) {
            return null;
        } else {
            NumberFormat format = NumberFormat.getIntegerInstance();
            format.setMinimumIntegerDigits(8);
            format.setGroupingUsed(false);
            return format.format(number);
        }
    }
    
    public static Long getCRC32(String value) {
        CRC32 crc32 = new CRC32();
        crc32.update(value.getBytes());
        return crc32.getValue();
    }
    
    public static String arrayToString(String[] strs) {
        if (strs.length == 0) {
            return "";
        } else {
            StringBuffer sbuf = new StringBuffer();
            sbuf.append(strs[0]);
            
            for (int idx = 1; idx < strs.length; ++idx) {
                sbuf.append(",");
                sbuf.append(strs[idx]);
            }
            
            return sbuf.toString();
        }
    }
    
    public static String getDefaultValue(String value, String defaultValue) {
        return isNotBlank(value) ? value : defaultValue;
    }
    
    public static String encodePassword(String password, String algorithm) {
        byte[] unencodedPassword = password.getBytes();
        MessageDigest md = null;
        
        try {
            md = MessageDigest.getInstance(algorithm);
        } catch (Exception var7) {
            var7.printStackTrace();
            return password;
        }
        
        md.reset();
        md.update(unencodedPassword);
        byte[] encodedPassword = md.digest();
        StringBuffer buf = new StringBuffer();
        
        for (int i = 0; i < encodedPassword.length; ++i) {
            if ((encodedPassword[i] & 255) < 16) {
                buf.append("0");
            }
            
            buf.append(Long.toString((long) (encodedPassword[i] & 255), 16));
        }
        
        return buf.toString();
    }
    
    public static String md5(String password) {
        return encodePassword(password, "md5");
    }
    
    public static String sha1(String password) {
        return encodePassword(password, "sha1");
    }
    
    public static boolean isTheSame(Object a, Object b) {
        if (a == null && b == null) {
            return true;
        } else {
            return a != null && a.equals(b);
        }
    }
    
    public static String removeYanText(String nickName) {
        if (isBlank(nickName)) {
            return "";
        } else {
            byte[] b_text = nickName.getBytes();
            
            for (int i = 0; i < b_text.length; ++i) {
                if ((b_text[i] & 248) == 240) {
                    for (int j = 0; j < 4; ++j) {
                        b_text[i + j] = 0;
                    }
                    
                    i += 3;
                }
            }
            
            return new String(b_text);
        }
    }
}

