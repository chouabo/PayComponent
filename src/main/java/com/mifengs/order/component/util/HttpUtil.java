package com.mifengs.order.component.util;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLEncoder;
import java.security.SecureRandom;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Zhaojiaheng
 * @ClassName: HttpUtil
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @date 2018年11月05日 下午 17:59:16
 * 注意：本内容仅限于蜜蜂商城内部传阅，禁止外泄以及用于其他的商业目
 */
public class HttpUtil {
    private static final Logger logger = LoggerFactory.getLogger(HttpUtil.class);
    public static final String POST = "POST";
    public static final String GET = "GET";
    public static final String PUT = "PUT";
    public static final String DELETE = "DELETE";
    public static final String CHARSET = "UTF-8";
    
    public static String httpsRequest(String requestUrl, String requestMethod) {
        return httpsRequest(requestUrl, requestMethod, null);
    }
    
    public static String httpsRequest(String requestUrl, String requestMethod, String outputStr) {
        logger.debug("*********requestUrl is {}  AND requestMethod IS {}  AND  outputStr {} END ********", new Object[]{requestUrl, requestMethod, outputStr});
        StringBuffer buffer = new StringBuffer();
        try {
            TrustManager[] tm = {new MyX509TrustManager()};
            SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
            sslContext.init(null, tm, new SecureRandom());
            
            SSLSocketFactory ssf = sslContext.getSocketFactory();
            
            URL url = new URL(requestUrl);
            HttpsURLConnection httpUrlConn = (HttpsURLConnection) url.openConnection();
            httpUrlConn.setSSLSocketFactory(ssf);
            
            httpUrlConn.setDoOutput(true);
            httpUrlConn.setDoInput(true);
            httpUrlConn.setUseCaches(false);
            httpUrlConn.setRequestMethod(requestMethod);
            httpUrlConn.setRequestProperty("User-Agent", "Mozilla/5.0 (Linux; Android 5.0; SM-N9100 Build/LRX21V) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/37.0.0.0 Mobile Safari/537.36 MicroMessenger/6.0.2.56_r958800.520 NetType/WIFI");
            if ("GET".equalsIgnoreCase(requestMethod)) {
                httpUrlConn.connect();
            }
            if (null != outputStr) {
                OutputStream outputStream = httpUrlConn.getOutputStream();
                outputStream.write(outputStr.getBytes("UTF-8"));
                outputStream.close();
            }
            InputStream inputStream = httpUrlConn.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            
            String str = null;
            while ((str = bufferedReader.readLine()) != null) {
                buffer.append(str);
            }
            bufferedReader.close();
            inputStreamReader.close();
            
            inputStream.close();
            inputStream = null;
            httpUrlConn.disconnect();
            return buffer.toString();
        } catch (ConnectException ce) {
            logger.error("ConnectException happened ", ce);
        } catch (Exception e) {
            logger.error("Exception happened ", e.getMessage());
        }
        logger.error("Can not get content from {} ", requestUrl);
        return null;
    }
    
    public static String httpRequest(String urlAddress, String requestMethod) {
        return httpRequest(urlAddress, requestMethod, null, null, "application/x-www-form-urlencoded;charset=UTF-8");
    }
    
    public static String httpRequest(String urlAddress, String requestMethod, Map<String, String> headers, Map<String, String> params) {
        return httpRequest(urlAddress, requestMethod, headers, params, "application/x-www-form-urlencoded;charset=UTF-8");
    }
    
    public static String httpRequest(String urlAddress, String requestMethod, Map<String, String> headers, Map<String, String> params, String contentType) {
        StringBuffer paramsTemp = new StringBuffer();
        if ((params != null) && (!params.isEmpty())) {
            for (String paramKey : params.keySet()) {
                String paramValue = (String) params.get(paramKey);
                if ((paramValue != null) && (paramValue.length() > 0)) {
                    try {
                        paramValue = URLEncoder.encode((String) params.get(paramKey), "UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        logger.warn(urlAddress, e);
                    }
                }
                paramsTemp.append("&").append(paramKey).append("=").append(paramValue);
            }
            return request(urlAddress, requestMethod, headers, paramsTemp.substring(1).toString(), contentType);
        }
        return request(urlAddress, requestMethod, headers, null, contentType);
    }
    
    public static String request(String urlAddress, String requestMethod, Map<String, String> headers, String params, String contentType) {
        logger.debug("*********requestUrl is {}  AND requestMethod IS {}  END ********", urlAddress, requestMethod);
        URL url = null;
        HttpURLConnection con = null;
        BufferedReader in = null;
        try {
            url = new URL(urlAddress);
            con = (HttpURLConnection) url.openConnection();
            con.setUseCaches(false);
            con.setDoOutput(true);
            con.setConnectTimeout(30000);
            con.setRequestMethod(requestMethod);
            
            
            con.setRequestProperty("Content-Type", contentType);
            con.setRequestProperty("Accept-Charset", "UTF-8");
            con.setRequestProperty("User-Agent", "Mozilla/5.0 (Linux; Android 5.0; SM-N9100 Build/LRX21V) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/37.0.0.0 Mobile Safari/537.36 MicroMessenger/6.0.2.56_r958800.520 NetType/WIFI");
            if ((headers != null) && (!headers.isEmpty())) {
                for (String headerKey : headers.keySet()) {
                    con.setRequestProperty(headerKey, (String) headers.get(headerKey));
                }
            }
            if ((params != null) && (params.length() > 0)) {
                DataOutputStream out = new DataOutputStream(con.getOutputStream());
                out.writeBytes(params);
                out.flush();
                out.close();
            }
            int returnCode = con.getResponseCode();
            StringBuffer result;
            if (returnCode == 200) {
                result = new StringBuffer();
                in = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
                String line;
                for (; ; ) {
                    line = in.readLine();
                    if (line == null) {
                        break;
                    }
                    result.append(line);
                }
                return result.toString();
            }
            return "returnCode=" + returnCode;
        } catch (ConnectException ce) {
            logger.warn(urlAddress + " connection timed out.");
        } catch (IOException e) {
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
        logger.error("Can not get content from {} ", urlAddress);
        return null;
    }
    
    public static String httpPost(String urlAddress, Map<String, String> params) {
        return httpRequest(urlAddress, "POST", null, params, "application/x-www-form-urlencoded;charset=UTF-8");
    }
    
    public static String httpPost(String urlAddress, Map<String, String> headers, Map<String, String> params) {
        return httpRequest(urlAddress, "POST", headers, params, "application/x-www-form-urlencoded;charset=UTF-8");
    }
    
    public static String httpGet(String urlAddress) {
        URL url = null;
        HttpURLConnection con = null;
        BufferedReader in = null;
        try {
            url = new URL(urlAddress);
            con = (HttpURLConnection) url.openConnection();
            con.setUseCaches(false);
            con.setDoOutput(true);
            con.setConnectTimeout(30000);
            con.setReadTimeout(30000);
            con.setRequestMethod("GET");
            con.setRequestProperty("User-Agent", "Mozilla/5.0 (Linux; Android 5.0; SM-N9100 Build/LRX21V) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/37.0.0.0 Mobile Safari/537.36 MicroMessenger/6.0.2.56_r958800.520 NetType/WIFI");
            
            
            con.setRequestProperty("Charset", "UTF-8");
            int returnCode = con.getResponseCode();
            StringBuffer result;
            if (returnCode == 200) {
                result = new StringBuffer();
                in = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
                String line;
                for (; ; ) {
                    line = in.readLine();
                    if (line == null) {
                        break;
                    }
                    result.append(line);
                }
                return result.toString();
            }
            return "returnCode=" + returnCode;
        } catch (SocketTimeoutException e) {
            logger.warn(urlAddress + " connection timed out.");
        } catch (IOException e) {
            logger.warn(urlAddress + " IOException.", e);
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
        logger.error("Can not get content from {} ", urlAddress);
        return null;
    }
    
    public static String httpPost(String actionUrl, Map<String, String> headers, Map<String, String> params, Map<String, File> files)
            throws IOException {
        String BOUNDARY = UUID.randomUUID().toString();
        String PREFIX = "--";
        String LINEND = "\r\n";
        String MULTIPART_FROM_DATA = "multipart/form-data";
        URL uri = new URL(actionUrl);
        
        HttpURLConnection conn = null;
        DataOutputStream outStream = null;
        BufferedReader bufferedReader = null;
        try {
            conn = (HttpURLConnection) uri.openConnection();
            conn.setConnectTimeout(30000);
            conn.setReadTimeout(30000);
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setUseCaches(false);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("connection", "keep-alive");
            conn.setRequestProperty("Charsert", "UTF-8");
            conn.setRequestProperty("Content-Type", MULTIPART_FROM_DATA + ";boundary=" + BOUNDARY);
            if (headers != null) {
                for (String key : headers.keySet()) {
                    conn.setRequestProperty(key, (String) headers.get(key));
                }
            }
            StringBuilder sb = new StringBuilder();
            if (params != null) {
                for (Map.Entry<String, String> entry : params.entrySet()) {
                    sb.append(PREFIX);
                    sb.append(BOUNDARY);
                    sb.append(LINEND);
                    sb.append("Content-Disposition: form-data; name=\"" + (String) entry.getKey() + "\"" + LINEND);
                    sb.append("Content-Type: text/plain; charset=UTF-8" + LINEND);
                    sb.append("Content-Transfer-Encoding: 8bit" + LINEND);
                    sb.append(LINEND);
                    sb.append((String) entry.getValue());
                    sb.append(LINEND);
                }
            }
            outStream = new DataOutputStream(conn.getOutputStream());
            if (sb.length() > 0) {
                outStream.write(sb.toString().getBytes());
            }
            if (files != null) {
                for (Map.Entry<String, File> file : files.entrySet()) {
                    StringBuilder sb1 = new StringBuilder();
                    sb1.append(PREFIX);
                    sb1.append(BOUNDARY);
                    sb1.append(LINEND);
                    sb1.append("Content-Disposition: form-data; name=\"file\"; filename=\"" + (String) file.getKey() + "\"" + LINEND);
                    sb1.append("Content-Type: application/octet-stream; charset=UTF-8" + LINEND);
                    sb1.append(LINEND);
                    outStream.write(sb1.toString().getBytes());
                    
                    InputStream is = new FileInputStream((File) file.getValue());
                    byte[] buffer = new byte[1024];
                    int len = 0;
                    while ((len = is.read(buffer)) != -1) {
                        outStream.write(buffer, 0, len);
                    }
                    is.close();
                    outStream.write(LINEND.getBytes());
                }
            }
            int len;
            byte[] end_data = (PREFIX + BOUNDARY + PREFIX + LINEND).getBytes();
            outStream.write(end_data);
            outStream.flush();
            logger.info("HttpUtil", "conn.getContentLength():" + conn.getContentLength());
            
            
            int res = conn.getResponseCode();
            InputStream in = conn.getInputStream();
            StringBuffer buffer;
            if (res == 200) {
                bufferedReader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
                buffer = new StringBuffer();
                String line = "";
                while ((line = bufferedReader.readLine()) != null) {
                    buffer.append(line);
                }
                return buffer.toString();
            }
            return "returnCode=" + res;
        } catch (Exception e) {
            logger.error(actionUrl + " Exception", e);
        } finally {
            if (bufferedReader != null) {
                bufferedReader.close();
            }
            if (outStream != null) {
                outStream.close();
            }
            if (conn != null) {
                conn.disconnect();
            }
        }
        logger.error("Can not get content from {} ", actionUrl);
        return null;
    }
}
