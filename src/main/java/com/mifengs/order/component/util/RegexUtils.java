package com.mifengs.order.component.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
* @ClassName: RegexUtils
* @Description: 正则表达式验证(这里用一句话描述这个类的作用)
* @author 
* @date 2017年4月28日 下午4:21:56
* 
*/
public class RegexUtils {
	/** 
     * 验证Email 
     * @param email email地址，格式：zhangsan@zuidaima.com，zhangsan@xxx.com.cn，xxx代表邮件服务商 
     * @return 验证成功返回true，验证失败返回false 
     */   
    public static boolean checkEmail(String email) {   
        String regex = "\\w+@\\w+\\.[a-z]+(\\.[a-z]+)?";   
        return matcher(regex, email);   
    }   
       
    /** 
     * 验证身份证号码 
     * @param idCard 居民身份证号码15位或18位，最后一位可能是数字或字母 
     * @return 验证成功返回true，验证失败返回false 
     */   
    public static boolean checkIdCard(String idCard) {   
        String regex = "[1-9]\\d{13,16}[a-zA-Z0-9]{1}";   
        return matcher(regex,idCard);   
    }   
       
    /** 
     * 验证手机号码（支持国际格式，+86135xxxx...（中国内地），+00852137xxxx...（中国香港）） 
     * @param mobile 移动、联通、电信运营商的号码段 
     *<p>移动的号段：134(0-8)、135、136、137、138、139、147（预计用于TD上网卡） 
     *、150、151、152、157（TD专用）、158、159、187（未启用）、188（TD专用）</p> 
     *<p>联通的号段：130、131、132、155、156（世界风专用）、185（未启用）、186（3g）</p> 
     *<p>电信的号段：133、153、180（未启用）、189</p> 
     * @return 验证成功返回true，验证失败返回false 
     */   
    public static boolean checkMobile(String mobile) {   
        String regex = "(\\+\\d+)?1[345678]\\d{9}$";   
        return matcher(regex,mobile);   
    }   
       
    /** 
     * 验证固定电话号码 
     * @param phone 电话号码，格式：国家（地区）电话代码 + 区号（城市代码） + 电话号码，如：+8602085588447 
     * <p><b>国家（地区） 代码 ：</b>标识电话号码的国家（地区）的标准国家（地区）代码。它包含从 0 到 9 的一位或多位数字， 
     *  数字之后是空格分隔的国家（地区）代码。</p> 
     * <p><b>区号（城市代码）：</b>这可能包含一个或多个从 0 到 9 的数字，地区或城市代码放在圆括号—— 
     * 对不使用地区或城市代码的国家（地区），则省略该组件。</p> 
     * <p><b>电话号码：</b>这包含从 0 到 9 的一个或多个数字 </p> 
     * @return 验证成功返回true，验证失败返回false 
     */   
    public static boolean checkPhone(String phone) {   
        String regex = "(\\+\\d+)?(\\d{3,4}\\-?)?\\d{7,8}$";   
        return matcher(regex, phone);   
    }   
       
    /** 
     * 验证整数（正整数和负整数） 
     * @param digit 一位或多位0-9之间的整数 
     * @return 验证成功返回true，验证失败返回false 
     */   
    public static boolean checkDigit(String digit) {   
        String regex = "\\-?[1-9]\\d+";   
        return matcher(regex,digit);   
    }
    
    /**
     * 验证正整数
     * @param digit 一位或多位0-9之间的整数
     * @return 验证成功返回true，验证失败返回false
     */
    public static boolean checkPositiveDigit(String digit) {
        String regex = "^[1-9]\\d*$";
        return matcher(regex,digit);
    }
       
    /** 
     * 验证整数和浮点数（正负整数和正负浮点数） 
     * @param decimals 一位或多位0-9之间的浮点数，如：1.23，233.30 
     * @return 验证成功返回true，验证失败返回false 
     */   
    public static boolean checkDecimals(String decimals) {   
        String regex = "\\-?[1-9]\\d+(\\.\\d+)?";   
        return matcher(regex,decimals);   
    }    
       
    /** 
     * 验证空白字符 
     * @param blankSpace 空白字符，包括：空格、\t、\n、\r、\f、\x0B 
     * @return 验证成功返回true，验证失败返回false 
     */   
    public static boolean checkBlankSpace(String blankSpace) {   
        String regex = "\\s+";   
        return matcher(regex,blankSpace);   
    }   
       
    /** 
     * 验证中文 
     * @param chinese 中文字符 
     * @return 验证成功返回true，验证失败返回false 
     */   
    public static boolean checkChinese(String chinese) {   
        String regex = "^[\u4E00-\u9FA5]+$";   
        return matcher(regex,chinese);   
    }   
       
    /** 
     * 验证日期（年月日） 
     * @param birthday 日期，格式：1992-09-03，或1992.09.03 
     * @return 验证成功返回true，验证失败返回false 
     */   
    public static boolean checkBirthday(String birthday) {   
        String regex = "[0-9]{4}([-./])\\d{1,2}\\1\\d{1,2}";   
        return matcher(regex,birthday);   
    }   
       
    /** 
     * 验证URL地址 
     * @param url 格式：http://blog.csdn.net:80/xyang81/article/details/7705960? 或 http://www.csdn.net:80 
     * @return 验证成功返回true，验证失败返回false 
     */   
    public static boolean checkURL(String url) {   
        String regex = "(https?://(w{3}\\.)?)?\\w+\\.\\w+(\\.[a-zA-Z]+)*(:\\d{1,5})?(/\\w*)*(\\??(.+=.*)?(&.+=.*)?)?";   
        return matcher(regex, url);   
    }   
      
    /** 
     * <pre> 
     * 获取网址 URL 的一级域名 
     * http://www.zuidaima.com/share/1550463379442688.htm ->> zuidaima.com 
     * </pre> 
     *  
     * @param url 
     * @return 
     */  
    public static String getDomain(String url) {  
        Pattern p = Pattern.compile("(?<=http://|\\.)[^.]*?\\.(com|cn|net|org|biz|info|cc|tv)", Pattern.CASE_INSENSITIVE);  
        // 获取完整的域名  
        // Pattern p=Pattern.compile("[^//]*?\\.(com|cn|net|org|biz|info|cc|tv)", Pattern.CASE_INSENSITIVE);  
        Matcher matcher = p.matcher(url);  
        matcher.find();  
        return matcher.group();  
    }  
    /** 
     * 匹配中国邮政编码 
     * @param postcode 邮政编码 
     * @return 验证成功返回true，验证失败返回false 
     */   
    public static boolean checkPostcode(String postcode) {   
        String regex = "^[0-9][0-9]{5}$";   
        return matcher(regex, postcode);   
    }   
       
    /** 
     * 匹配IP地址(简单匹配，格式，如：192.168.1.1，127.0.0.1，没有匹配IP段的大小) 
     * @param ipAddress IPv4标准地址 
     * @return 验证成功返回true，验证失败返回false 
     */   
    public static boolean checkIpAddress(String ipAddress) {   
        String regex = "[1-9](\\d{1,2})?\\.(0|([1-9](\\d{1,2})?))\\.(0|([1-9](\\d{1,2})?))\\.(0|([1-9](\\d{1,2})?))";   
        return matcher(regex, ipAddress);   
    }   
    
    
    /** 
    * @Title: checkUserName 
    * @Description: TODO(这里用一句话描述这个方法的作用) 
    * @param @param userName
    * @param @return    设定文件 
    * @return boolean    返回类型 
    * @throws 
    */ 	
    public static boolean checkUserName(String userName){
		// 用户名验证规则：用户名（字母开头 + 数字/字母/下划线）
		String regEx = "^[A-Za-z][A-Za-z0-9_-]+$";
		return matcher(regEx,userName);
	}
    
    
    /** 
    * @Title: checkLatiLongiTude 
    * @Description: 经纬度检查(这里用一句话描述这个方法的作用) 
    * @param @param location
    * @param @return    设定文件 
    * @return boolean    返回类型 
    * @throws 
    */ 	
    public static boolean checkLatiLongiTude(String location){
		boolean bool = false;
		String[] stres = location.split(",");
		if(stres.length == 2){
			String regEx = "^-?((0|1?[0-7]?[0-9]?)(([.][0-9]{1,4})?)|180(([.][0]{1,4})?))$";
			bool = matcher(regEx,stres[0]);
			if(bool){
				regEx = "^-?((0|[1-8]?[0-9]?)(([.][0-9]{1,4})?)|90(([.][0]{1,4})?))$ ";
				bool = matcher(regEx,stres[1]);
			}
		}
		return bool;
	}
    
    /**  
     * 校验银行卡卡号  
     */    
    public static boolean checkBankCard(String bankCard) {    
         if(bankCard.length() < 15 || bankCard.length() > 19) {  
             return false;  
         }  
         char bit = getBankCardCheckCode(bankCard.substring(0, bankCard.length() - 1));    
         if(bit == 'N'){    
             return false;    
         }    
         return bankCard.charAt(bankCard.length() - 1) == bit;    
    }    

    /**  
     * 从不含校验位的银行卡卡号采用 Luhm 校验算法获得校验位  
     * @param nonCheckCodeBankCard  
     * @return  
     */    
    private static char getBankCardCheckCode(String nonCheckCodeBankCard){    
        if(nonCheckCodeBankCard == null || nonCheckCodeBankCard.trim().length() == 0    
                || !nonCheckCodeBankCard.matches("\\d+")) {    
            //如果传的不是数据返回N    
            return 'N';    
        }    
        char[] chs = nonCheckCodeBankCard.trim().toCharArray();    
        int luhmSum = 0;    
        for(int i = chs.length - 1, j = 0; i >= 0; i--, j++) {    
            int k = chs[i] - '0';    
            if(j % 2 == 0) {    
                k *= 2;    
                k = k / 10 + k % 10;    
            }    
            luhmSum += k;               
        }    
        return (luhmSum % 10 == 0) ? '0' : (char)((10 - luhmSum % 10) + '0');    
    }
    
    public static boolean matcher(String regEx,String string){
		if(string == null) string = "";
		Pattern pattern = Pattern.compile(regEx);
	    // 忽略大小写的写法
	    // Pattern pat = Pattern.compile(regEx, Pattern.CASE_INSENSITIVE);
	    Matcher matcher = pattern.matcher(string);
	    // 查找字符串中是否有匹配正则表达式的字符/字符串
	    return matcher.find();
	}
    /**
     * 校验密码（6-16位 数字和字母组成,纯数字纯密码不行）
     * @date 2018-05-08
     * @author zhengjie
     */
    public static boolean checkPwd(String password){
        String regex = "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,16}$";
        return matcher(regex,password);
    }

    /**
     * 校验金钱是否匹配(小数点前8位，小数点后两位，金额不等于0)
     * @param decimals
     * @return
     */
    public static boolean checkMoneyAmount(String decimals){
        String regex = "(?!^0\\.0?0$|0$)^([1-9][0-9]{0,7}|0)?(\\.[0-9]{1,2})?$";
        return matcher(regex,decimals);
    }

}
