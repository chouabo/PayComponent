package com.mifengs.order.component.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 微信工具类
 * 
 * @author tony
 * 
 */
public class WeiXinUtil {
	

	/** The log. */
	private final static Logger logger = LoggerFactory.getLogger(WeiXinUtil.class);

	/**
	 * 判断是否来自微信, 5.0 之后的支持微信支付
	 * 
	 * @param request
	 * @return
	 */
	public static boolean isWeiXin(HttpServletRequest request) {
		String userAgent = request.getHeader("User-Agent");
		if (StringUtils.isNotBlank(userAgent)) {
			Pattern p = Pattern.compile("MicroMessenger/(\\d+).+");
			Matcher m = p.matcher(userAgent);
			String version = null;
			if (m.find()) {
				version = m.group(1);
			}
			return (null != version && NumberUtils.toInt(version) >= 5);
		}
		return false;
	}
	
	public static boolean isMoblie(HttpServletRequest request) {
		boolean isMoblie = false;
		String[] mobileAgents = { "iphone", "android", "phone", "mobile", "wap", "netfront", "java", "opera mobi",
				"opera mini", "ucweb", "windows ce", "symbian", "series", "webos", "sony", "blackberry", "dopod",
				"nokia", "samsung", "palmsource", "xda", "pieplus", "meizu", "midp", "cldc", "motorola", "foma",
				"docomo", "up.browser", "up.link", "blazer", "helio", "hosin", "huawei", "novarra", "coolpad", "webos",
				"techfaith", "palmsource", "alcatel", "amoi", "ktouch", "nexian", "ericsson", "philips", "sagem",
				"wellcom", "bunjalloo", "maui", "smartphone", "iemobile", "spice", "bird", "zte-", "longcos",
				"pantech", "gionee", "portalmmm", "jig browser", "hiptop", "benq", "haier", "^lct", "320x320",
				"240x320", "176x220", "w3c ", "acs-", "alav", "alca", "amoi", "audi", "avan", "benq", "bird", "blac",
				"blaz", "brew", "cell", "cldc", "cmd-", "dang", "doco", "eric", "hipt", "inno", "ipaq", "java", "jigs",
				"kddi", "keji", "leno", "lg-c", "lg-d", "lg-g", "lge-", "maui", "maxo", "midp", "mits", "mmef", "mobi",
				"mot-", "moto", "mwbp", "nec-", "newt", "noki", "oper", "palm", "pana", "pant", "phil", "play", "port",
				"prox", "qwap", "sage", "sams", "sany", "sch-", "sec-", "send", "seri", "sgh-", "shar", "sie-", "siem",
				"smal", "smar", "sony", "sph-", "symb", "t-mo", "teli", "tim-", "tosh", "tsm-", "upg1", "upsi", "vk-v",
				"voda", "wap-", "wapa", "wapi", "wapp", "wapr", "webc", "winw", "winw", "xda", "xda-",
				"Googlebot-Mobile" };
		if (request.getHeader("User-Agent") != null) {
			for (String mobileAgent : mobileAgents) {
				if (request.getHeader("User-Agent").toLowerCase().indexOf(mobileAgent) >= 0) {
					isMoblie = true;
					break;
				}
			}
		}
		return isMoblie;
	}
	
	/**
	 * 判断是否请求成功
	 * @param resultStr
	 * @throws
	 */
	public static void isSuccess(String resultStr) {		
		JSONObject jsonObject = JSONObject.parseObject(resultStr);
		Integer errCode =jsonObject.getIntValue("errcode");
		if (errCode!=null && errCode!=0) {
			String errMsg = WeiXinErrcodeUtil.getErrorMsg(errCode);
			if (errMsg.equals("")) {
				errMsg = jsonObject.getString("errmsg");
			}
			throw new RuntimeException("异常码:"+errCode+";异常说明:"+errMsg);
		}
	}
	

	/**
	 * 解析微信发来的请求（XML）
	 * 
	 * @param request
	 * @return Map<String, String>
	 * @throws Exception
	 */
	public static Map<String, String> parseXml(HttpServletRequest request )throws XmlPullParserException, IOException {
		InputStream inputStream = request.getInputStream();
		Map<String, String> map = null;
		XmlPullParser pullParser = XmlPullParserFactory.newInstance().newPullParser();
		pullParser.setInput(inputStream, "UTF-8"); // 为xml设置要解析的xml数据
		int eventType = pullParser.getEventType();
		while (eventType != XmlPullParser.END_DOCUMENT) {
			switch (eventType) {
			case XmlPullParser.START_DOCUMENT:
				map = new HashMap<String, String>();
				break;
			case XmlPullParser.START_TAG:
				String key = pullParser.getName();
				if (key.equals("xml"))
					break;
				String value = pullParser.nextText();
				map.put(key, value);
				break;
			case XmlPullParser.END_TAG:
				break;
			}
			eventType = pullParser.next();
		}

		return map;
	}
	
	/**
	 * 通过网络文件上传多媒体文件到微信公众平台
	 * 
	 * @param uploadMediaUrl
	 *            http://file.api.weixin.qq.com/cgi-bin/media/upload?
	 *            access_token=ACCESS_TOKEN&type=TYPE
	 * @param mediaFileUrl
	 *            媒体文件url(如：http://c.hiphotos.baidu.com/zhidao/pic/item/14
	 *            ce36d3d539b600286c7dd4eb50352ac65cb736.jpg)
	 * @return
	 */
	public static String uploadMediaByMediaFileUrl(String uploadMediaUrl, String mediaFileUrl) {
		String result = null;
		BufferedReader bufferedReader = null;
		InputStream inputStream = null;
		InputStreamReader inputStreamReader = null;
		HttpURLConnection uploadConn = null;
		// 定义数据分隔符
		String boundary = "---------------------------" + System.currentTimeMillis();
		try {
			URL uploadUrl = new URL(uploadMediaUrl);
			uploadConn = (HttpURLConnection) uploadUrl.openConnection();
			uploadConn.setDoOutput(true);
			uploadConn.setDoInput(true);
			uploadConn.setUseCaches(false);
			uploadConn.setRequestMethod("POST");

			// 设置请求头信息
			uploadConn.setRequestProperty("Connection", "Keep-Alive");
			// 设置请求头Content-Type
			uploadConn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);

			// 获取媒体文件上传的输出流（往微信服务器写数据）
			OutputStream outputStream = uploadConn.getOutputStream();
			URL mediaUrl = new URL(mediaFileUrl);
			HttpURLConnection mediaConn = (HttpURLConnection) mediaUrl.openConnection();
			mediaConn.setDoOutput(true);
			mediaConn.setRequestMethod("GET");

			// 从请求头获取内容类型
			String contentType = mediaConn.getHeaderField("Content-Type");
			// 根据内容类型判断文件扩展名
			String fileExt = getUploadFileExt(contentType);
			// 请求体开始
			outputStream.write(("--" + boundary + "\r\n").getBytes());
			outputStream.write(String.format("Content-Disposition: form-data; name=\"media\";filename=\"file1%s\"\r\n", fileExt).getBytes());
			outputStream.write(String.format("Content-Type: %s\r\n\r\n", contentType).getBytes());

			// 获取媒体文件的输入流(读取文件)
			BufferedInputStream bis = new BufferedInputStream(mediaConn.getInputStream());
			byte[] buf = new byte[8096];
			int size = 0;
			while ((size = bis.read(buf)) != -1) {
				// 将媒体文件写到输出流（往微信服务器写数据）
				outputStream.write(buf, 0, size);
			}

			// 请求体结束
			outputStream.write(("\r\n--" + boundary + "--\r\n").getBytes());
			outputStream.close();
			bis.close();
			mediaConn.disconnect();

			// 获取媒体文件上传的输入流（从微信服务器读数据）
			inputStream = uploadConn.getInputStream();
			inputStreamReader = new InputStreamReader(inputStream, "utf-8");
			bufferedReader = new BufferedReader(inputStreamReader);
			StringBuffer buffer = new StringBuffer();
			String str = null;
			while ((str = bufferedReader.readLine()) != null) {
				buffer.append(str);
			}
			result = buffer.toString();
		} catch (ConnectException ce) {
			System.out.println("Weixin server connection timed out.");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("上传媒体文件失败:{}", e);
		} finally {
			try {
				if (bufferedReader != null) {
					bufferedReader.close();
				}
				if (inputStreamReader != null) {
					inputStreamReader.close();
				}
				if (inputStream != null) {
					inputStream.close();
				}
				bufferedReader = null;
				inputStreamReader = null;
				inputStream = null;
				uploadConn.disconnect();
			} catch (IOException e) {
				e.printStackTrace();
				logger.error("上传媒体文件失败:{}", e);
			}
		}
		return result;
	}

	/**
	 * 通过本地文件上传多媒体文件到微信公众平台
	 * 
	 * @param requestUrl
	 * 
	 * @param file
	 *            文件
	 * @param content_type
	 *            文件类型
	 * @return 返回的字符串
	 * @throws Exception
	 */
	public static String uploadMediaByMediaFile(String requestUrl, File file, String content_type) {

		String result = null;
		StringBuffer bufferStr = new StringBuffer();
		String end = "\r\n";
		String twoHyphens = "--"; // 用于拼接
		String boundary = "*****"; // 用于拼接 可自定义
		URL submit = null;
		DataOutputStream dos = null;
		BufferedInputStream bufin = null;
		BufferedReader bufferedReader = null;
		try {
			submit = new URL(requestUrl);
			HttpURLConnection conn = (HttpURLConnection) submit.openConnection();
			conn.setDoInput(true);
			conn.setDoOutput(true);
			conn.setUseCaches(false);

			conn.setRequestMethod("POST");
			conn.setRequestProperty("Connection", "Keep-Alive");
			conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);

			// 获取输出流对象，准备上传文件
			dos = new DataOutputStream(conn.getOutputStream());
			dos.writeBytes(twoHyphens + boundary + end);
			dos.writeBytes("Content-Disposition: form-data; name=\"" + file + "\";filename=\"" + file.getName() + ";Content-Type=\"" + content_type + end);
			dos.writeBytes(end);
			// 对文件进行传输
			bufin = new BufferedInputStream(new FileInputStream(file));
			byte[] buffer = new byte[8192]; // 8k
			int count = 0;
			while ((count = bufin.read(buffer)) != -1) {
				dos.write(buffer, 0, count);
			}

			bufin.close(); // 关闭文件流

			dos.writeBytes(end);
			dos.writeBytes(twoHyphens + boundary + twoHyphens + end);
			dos.flush();

			// 读取URL链接返回字符串
			InputStream inputStream = conn.getInputStream();
			InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
			bufferedReader = new BufferedReader(inputStreamReader);

			String str = null;
			while ((str = bufferedReader.readLine()) != null) {
				bufferStr.append(str);
			}
			result = bufferStr.toString();
			System.out.println("-------------读取URL链接返回字符串--------------" + result);
		} catch (ConnectException ce) {
			System.out.println("Weixin server connection timed out.");
		} catch (Exception e) {
			logger.error("上传媒体文件失败:{}", e);
			e.printStackTrace();
			// throw new Exception("微信服务器连接错误！" + e.toString());
		} finally {
			try {
				if (dos != null) {
					dos.close();
				}
				if (bufferedReader != null) {
					bufferedReader.close();
				}

			} catch (Exception e2) {
				logger.error("上传媒体文件失败:{}", e2);
			}
		}
		// 获取到返回Json请自行根据返回码获取相应的结果
		return result;
	}

	/**
	 * 解析微信发来的请求（XML）
	 * 
	 * @param
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
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
	 * 根据类型判断文件扩展名
	 * 
	 * @param contentType
	 *            内容类型
	 * @return String
	 */
	private static String getUploadFileExt(String contentType) {
		String fileExt = "";
		if (contentType == null || contentType == "") {
			return fileExt;
		}
		String[] content = contentType.split(";");
		if (content[0] != null) {
			if ("image/jpeg".equals(content[0])) {
				fileExt = ".jpg";
			} else if ("audio/mpeg".equals(content[0])) {
				fileExt = ".mp3";
			} else if ("audio/amr".equals(content[0])) {
				fileExt = ".amr";
			} else if ("video/mp4".equals(content[0])) {
				fileExt = ".mp4";
			} else if ("video/mpeg4".equals(content[0])) {
				fileExt = ".mp4";
			}
		}
		return fileExt;
	}
	
	/**
	 * 根据类型判断文件扩展名
	 * 
	 * @param contentType
	 *            内容类型
	 * @return String
	 */
	public static String getDownFileExt(String contentType) {
		String fileExt = "";
		if ("image/jpeg".equals(contentType)) {
			fileExt = ".jpg";
		} else if ("audio/mpeg".equals(contentType)) {
			fileExt = ".mp3";
		} else if ("audio/amr".equals(contentType)) {
			fileExt = ".amr";
		} else if ("video/mp4".equals(contentType)) {
			fileExt = ".mp4";
		} else if ("video/mpeg4".equals(contentType)) {
			fileExt = ".mp4";
		}
		return fileExt;
	}
	
	public static Map<String, String> httpPostXML(String urlAddress, String xml) {
		URL url = null;
		HttpURLConnection con = null;
		BufferedReader in = null;
		try {
			url = new URL(urlAddress);
			con = (HttpURLConnection) url.openConnection();
			con.setUseCaches(false);
			con.setDoInput(true);
			con.setDoOutput(true);// 如果通过post提交数据，必须设置允许对外输出数据

			con.setRequestProperty("Connection", "keep-alive");
			con.setRequestProperty("Accept", "*/*");
			con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
			con.setRequestProperty("Host", "api.mch.weixin.qq.com");
			con.setRequestProperty("X-Requested-With", "XMLHttpRequest");
			con.setRequestProperty("Cache-Control", "max-age=0");
			con.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.0) ");
			con.connect();
			DataOutputStream out = new DataOutputStream(con.getOutputStream());
			out.write(xml.getBytes("UTF-8"));
//			out.writeBytes(new String(xml.getBytes(),"UTF-8")); // 写入请求的字符串
			out.flush();
			out.close();

			InputStream inputStream = con.getInputStream();
			Map<String, String> map = null;
			XmlPullParser pullParser = XmlPullParserFactory.newInstance().newPullParser();
			pullParser.setInput(inputStream, "UTF-8"); // 为xml设置要解析的xml数据
			int eventType = pullParser.getEventType();
			while (eventType != XmlPullParser.END_DOCUMENT) {
				switch (eventType) {
				case XmlPullParser.START_DOCUMENT:
					map = new HashMap<String, String>();
					break;
				case XmlPullParser.START_TAG:
					String key = pullParser.getName();
					if (key.equals("xml"))
						break;
					String value = pullParser.nextText();
					map.put(key, value);
					break;
				case XmlPullParser.END_TAG:
					break;
				}
				eventType = pullParser.next();
			}
			return map;
		} catch (ConnectException ce) {
			logger.warn(urlAddress + " connection timed out.");
		} catch (IOException e) {
			e.printStackTrace();
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		} finally {
			try {
				if (in != null) {
					in.close();
				}
				if (con != null) {
					con.disconnect();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	public static void main(String[] args) throws Exception {
		String xml = "<xml><return_code><![CDATA[SUCCESS]]></return_code><return_msg><![CDATA[OK]]></return_msg><appid><![CDATA[wx36c81449649b8e2e]]></appid><mch_id><![CDATA[1269202901]]></mch_id><nonce_str><![CDATA[OTWZ1wmVZJsfoqYL]]></nonce_str><sign><![CDATA[39BAB793F388730E5CE1C350DDC21F1C]]></sign><result_code><![CDATA[FAIL]]></result_code><err_code><![CDATA[REFUND_FEE_INVALID]]></err_code><err_code_des><![CDATA[累计退款金额大于支付金额]]></err_code_des></xml> ";
		Map<String, String> map = parseXml(xml);
		System.out.println(JSON.toJSONString(map));
	}

}
