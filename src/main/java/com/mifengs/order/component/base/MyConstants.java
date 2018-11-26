package com.mifengs.order.component.base;

public class MyConstants {

	/**
	 * 请求返回码对象
	 */
	public static class RESULT{
		//成功
		public final static String SC0000="0000" ;//操作成功
		
		//失败码
		public final static String FI1000="1000" ;//用户不存在
		public final static String FI1001="1001" ;//密码错误
		public final static String FI1002="1002" ;//验证码无效
		public final static String FI1003="1003" ;//手机号存在
		public final static String FI1004="1004" ;//操作失败
		public final static String FI1005="1005" ;//邮箱存在
		public final static String FI1006="1006" ;//参数为空或非法
		public final static String FI1007="1007" ;//更新失败
		public final static String FI1008="1008" ;//用户未绑定微信
		public final static String FI1009="1009" ;//密码输入错误次数超过5次
		public final static String FI1010="1010" ;//用户未登陆
		public final static String FI1011="1011" ;//用户被锁定或者删除
		public final static String FI1012="1012" ;//删除失败
		public final static String FI1013="1013" ;//用户已存在
		public final static String FI1014="1014" ;//无效
		public final static String FI1015="1015" ;//用户已经注册（微信绑定）
		public final static String FI1016="1016" ;//用户已微信绑定
		public final static String FI1017="1017" ;//用户不属于门店用户
		public final static String FI1018="1018" ;//用户可用零钱不足
		public final static String FI1019="1019" ;//订单异常
		public final static String FI1020="1020" ;//提货码错误
		public final static String FI1021="1021" ;//返回结果为空
		public final static String FI1022="1022" ;//无登陆权限
		public final static String FI1023="1023" ;//客户号有误
		public final static String FI1024="1024" ;//客户号已绑定
		
		public final static String FI1025="1025" ;//客户号已绑定

		
		
		//异常码
		public final static String EX9110="9110" ;//非法操作
		public final static String EX9111="9111" ;//token签名与本地计算签名不匹配
		public final static String EX9112="9112" ;//token过期
		public final static String EX9990="9990" ;//系统异常
		public final static String EX9999="9999" ;//未知异常
		
		public final static Boolean TRUE = true; //存在
		public final static Boolean FALSE = false;//不存在

	}
	
	/**
	 * 系统配置
	 */
	public static class CONFIG {
		/*private static Map<String,String> data = new HashMap<String,String>();
		private static PropsUtil pu = new PropsUtil("/conf.properties");
		public static String GET(String name){
			String res = null;
			if(!data.containsKey(name))
				data.put(name, pu.readSingleProps(name));
			res = data.get(name);
			return res;
		}*/
		
		public final static String ENCODED_UTF8="UTF-8"; 
		
		public final static String AUTHOR_USER_REGEX = ".*/p/.*\\.do";//过滤登陆权限
		

		public final static String RUQUEST_MEMBER_NAME = "OnlineMember";//登陆会员获取的name值
		public final static String RUQUEST_INFO_NAME = "QequestInfoName";//请求用户信息加密

		
		public final static String USER_AGENT_HEAD_NAME = "user-agent";//存客户端的版本类型
		public final static String INTERFACE_VERSION_HEAD_NAME = "InterVersion";//调用接口版本
		public final static String TOKEN_HEAD_NAME = "app_token";//存token的head的name
		
		public final static int TOKEN_CACHE_TIME = 15*60;
		

		
	}
	public static void main(String[] str) throws Exception{
		
	}
}
