package com.zebone.nhis.common.support;

import com.zebone.platform.modules.exception.BusException;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * HttpCLint工具类，提供了post，get请求，有些可以用，注释为灵璧的没有通用性
 *
 * @Auther: wuqiang
 * @Date: 2018/12/7 10:23
 * @Description:
 */
public class HttpClientUtil {

    private final static Logger logger = LoggerFactory.getLogger(HttpClientUtil.class);

    // utf-8字符编码
    public static final String CHARSET_UTF_8 = "utf-8";

    // HTTP内容类型。
    public static final String CONTENT_TYPE_TEXT_HTML = "text/xml";

    // HTTP内容类型。相当于form表单的形式，提交数据
    public static final String CONTENT_TYPE_FORM_URL = "application/x-www-form-urlencoded";

    // HTTP内容类型。相当于form表单的形式，提交数据
    public static final String CONTENT_TYPE_JSON_URL = "application/json;charset=utf-8";


    // 连接管理器
    private static PoolingHttpClientConnectionManager pool;

    // 请求配置
    private static RequestConfig requestConfig;

    static {

        try {
            //System.out.println("初始化HttpClientTest~~~开始");
            SSLContextBuilder builder = new SSLContextBuilder();
            builder.loadTrustMaterial(null, new TrustSelfSignedStrategy());
            SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
                    builder.build());
            // 配置同时支持 HTTP 和 HTPPS
            Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create().register(
                    "http", PlainConnectionSocketFactory.getSocketFactory()).register(
                    "https", sslsf).build();
            // 初始化连接管理器
            pool = new PoolingHttpClientConnectionManager(
                    socketFactoryRegistry);
            // 将最大连接数增加到2000，
            pool.setMaxTotal(2000);
            // 设置最大路由
            pool.setDefaultMaxPerRoute(2);
            // 根据默认超时限制初始化requestConfig
            int socketTimeout = 50000;
            int connectTimeout = 50000;
            int connectionRequestTimeout = 50000;
            // 设置请求超时时间
            requestConfig = RequestConfig.custom()
            		.setSocketTimeout(socketTimeout)
            		.setConnectTimeout(connectTimeout)
                    .setConnectionRequestTimeout(connectionRequestTimeout).build();
            
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }


        
    }

    public static CloseableHttpClient getHttpClient() {

        CloseableHttpClient httpClient = HttpClients.custom()
                // 设置连接池管理
                .setConnectionManager(pool)
                // 设置请求配置
                .setDefaultRequestConfig(requestConfig)
                // 设置重试次数
                .setRetryHandler(new DefaultHttpRequestRetryHandler(0, false))
                .build();

        return httpClient;
    }

    /**
     * 发送Post请求
     *
     * @param httpPost
     * @return
     */
    private static String sendHttpPost(HttpPost httpPost) {

        CloseableHttpClient httpClient = null;
        CloseableHttpResponse response = null;
        // 响应内容
        String responseContent = null;
        try {
            // 创建默认的httpClient实例.
            httpClient = getHttpClient();
            // 配置请求信息
            httpPost.setConfig(requestConfig);
            // 执行请求
            response = httpClient.execute(httpPost);
            // 得到响应实例
            HttpEntity entity = response.getEntity();

            // 判断响应状态
            if (response.getStatusLine().getStatusCode() >= 300) {
                throw new Exception(
                        "HTTP Request is not success, Response code is " + response.getStatusLine().getStatusCode());
            }

            if (HttpStatus.SC_OK == response.getStatusLine().getStatusCode()) {
                responseContent = EntityUtils.toString(entity, CHARSET_UTF_8);
                EntityUtils.consume(entity);
            }

        } catch (Exception e) {
            logger.error("sendHttpPostError:",e);
            throw new BusException("请求异常："+ (StringUtils.isNotBlank(e.getMessage())?e.getMessage():(e.getCause()!=null?e.getCause().getMessage():"")));
        } finally {
            try {
                // 释放资源
                if (response != null) {
                    response.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return responseContent;
    }

    /**
     * 发送Get请求
     *
     * @param httpGet
     * @return
     */
    public static String sendHttpGet(HttpGet httpGet) {

        CloseableHttpClient httpClient = null;
        CloseableHttpResponse response = null;
        // 响应内容
        String responseContent = null;
        try {
            // 创建默认的httpClient实例.
            httpClient = getHttpClient();
            // 配置请求信息
            httpGet.setConfig(requestConfig);
            // 执行请求
            response = httpClient.execute(httpGet);
            // 得到响应实例
            HttpEntity entity = response.getEntity();

            // 判断响应状态
            if (response.getStatusLine().getStatusCode() >= 300) {
                throw new Exception(
                        "HTTP Request is not success, Response code is " + response.getStatusLine().getStatusCode());
            }

            if (HttpStatus.SC_OK == response.getStatusLine().getStatusCode()) {
                responseContent = EntityUtils.toString(entity, CHARSET_UTF_8);
                EntityUtils.consume(entity);
            }

        } catch (Exception e) {
            logger.error("sendHttpGet异常：",e);
            throw new BusException("sendHttpGet异常"+e.getMessage());
        } finally {
            try {
                // 释放资源
                if (response != null) {
                    response.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return responseContent;
    }


    /**
     * 发送 post请求
     *
     * @param httpUrl 地址
     * @param params  参数(格式:key1=value1&key2=value2)
     */
    public static String sendHttpPost(String httpUrl, List<NameValuePair> params) {
        HttpPost httpPost = new HttpPost(httpUrl);// 创建httpPost
        try {
            // 设置参数
            if (params != null && params.size()> 0) {
            	httpPost.setEntity(new UrlEncodedFormEntity(params, StandardCharsets.UTF_8));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sendHttpPost(httpPost);
    }

    /**
     * 发送 post请求
     *
     * @param maps 参数
     */
    public static String sendHttpPost(String httpUrl, Map<String, String> maps) {
    	List<NameValuePair> parem= convertStringParamter(maps);
        return sendHttpPost(httpUrl, parem);
    }


    /**
     * 发送 post请求 发送json数据
     *
     * @param httpUrl    地址
     * @param paramsJson 参数(格式 json)
     */
    public static String sendHttpPostJson(String httpUrl, String paramsJson) {
        HttpPost httpPost = new HttpPost(httpUrl);// 创建httpPost
        try {
            // 设置参数
            if (paramsJson != null && paramsJson.trim().length() > 0) {
                StringEntity stringEntity = new StringEntity(paramsJson, "UTF-8");
                stringEntity.setContentType(CONTENT_TYPE_JSON_URL);
                httpPost.setEntity(stringEntity);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusException("请求前设置参数异常"+e.getMessage());
        }
        return sendHttpPost(httpPost);
    }

    /**
     * 发送 post请求 发送json数据
     * @param httpUrl
     * @param paramsJson
     * @param heads 头部参数
     * @return
     */
    public static String sendHttpPostJson(String httpUrl, String paramsJson,Map<String, String> heads) {
        HttpPost httpPost = new HttpPost(httpUrl);// 创建httpPost
        try {
            // 设置参数
            if (paramsJson != null && paramsJson.trim().length() > 0) {
                StringEntity stringEntity = new StringEntity(paramsJson, "UTF-8");
                stringEntity.setContentType(CONTENT_TYPE_JSON_URL);
                httpPost.setEntity(stringEntity);
                for (Map.Entry<String, String> entry : heads.entrySet()) {
                    httpPost.addHeader(entry.getKey(), entry.getValue());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusException("请求前设置参数异常"+e.getMessage());
        }
        return sendHttpPost(httpPost);
    }

    /**
     * 发送 post请求 发送json数据
     *
     * @param httpUrl    地址
     * @param paramsJson 参数(格式 json)
     */
    public static String sendHttpPost(String httpUrl, String paramsJson, Map<String, String> heads) {
        HttpPost httpPost = new HttpPost(httpUrl);// 创建httpPost
        try {
            // 设置参数
            if (paramsJson != null && paramsJson.trim().length() > 0) {
                StringEntity stringEntity = new StringEntity(paramsJson, "UTF-8");
                stringEntity.setContentType(CONTENT_TYPE_JSON_URL);
                httpPost.setEntity(stringEntity);
            }

            for (Map.Entry<String, String> entry : heads.entrySet()) {
                httpPost.addHeader(entry.getKey(), entry.getValue());
            }
            httpPost.setProtocolVersion(HttpVersion.HTTP_1_0);
            httpPost.addHeader(HTTP.CONN_DIRECTIVE,HTTP.CONN_CLOSE);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sendHttpPost(httpPost);
    }

    /**
     * 发送 post请求 发送xml数据
     *
     * @param httpUrl   地址
     * @param paramsXml 参数(格式 Xml)
     */
    public static String sendHttpPostXml(String httpUrl, String paramsXml) {
        HttpPost httpPost = new HttpPost(httpUrl);// 创建httpPost
        try {
            // 设置参数
            if (paramsXml != null && paramsXml.trim().length() > 0) {
                StringEntity stringEntity = new StringEntity(paramsXml, "UTF-8");
                stringEntity.setContentType(CONTENT_TYPE_TEXT_HTML);
                httpPost.setEntity(stringEntity);
            }
        } catch (Exception e) {
            throw new BusException(e);
        }
        return sendHttpPost(httpPost);
    }


    /**
     * 将map集合的键值对转化成：key1=value1&key2=value2 的形式
     *
     * @return 字符串
     */
    public static List<NameValuePair> convertStringParamter( Map<String,String>  params) {
    	List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		for (Iterator<String> iter = params.keySet().iterator(); iter.hasNext();) {
			String key =iter.next();
			String value =params.get(key);
			nvps.add(new BasicNameValuePair(key, value));
		}
        return nvps;
    }

    /**
     * 发送 post请求
     *
     * @param maps 参数
     */
    public static String sendHttpPostDrg(String httpUrl, Map<String, String> maps) {
    	List<NameValuePair> parem= convertStringParamter(maps);
    	HttpPost httpPost = new HttpPost(httpUrl);// 创建httpPost
        try {
            // 设置参数
            if (parem != null && parem.size()> 0) {
            	httpPost.setEntity(new UrlEncodedFormEntity(parem, StandardCharsets.UTF_8));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sendHttpPostDrg(httpPost);
    }
    /**
     * 发送Post请求
     *
     * @param httpPost
     * @return
     */
    private static String sendHttpPostDrg(HttpPost httpPost) {

        CloseableHttpClient httpClientDrg = null;
        CloseableHttpResponse responseDrg = null;
        // 响应内容
        String responseContent = null;
        try {
        	//// 设置请求超时时间
        	RequestConfig requestConfigDrg=RequestConfig.custom().setSocketTimeout(300000).setConnectTimeout(300000).setConnectionRequestTimeout(300000).build();;
            // 创建默认的httpClient实例.
        	httpClientDrg =HttpClients.custom().setConnectionManager(pool).setDefaultRequestConfig(requestConfigDrg)
                    .setRetryHandler(new DefaultHttpRequestRetryHandler(0, false)).build();;//// 设置连接池管理// 设置请求配置// 设置重试次数
            // 配置请求信息
            httpPost.setConfig(requestConfigDrg);
            // 执行请求
            responseDrg = httpClientDrg.execute(httpPost);
            // 得到响应实例
            HttpEntity entity = responseDrg.getEntity();

            // 判断响应状态
            if (responseDrg.getStatusLine().getStatusCode() >= 300) {
                throw new Exception(
                        "HTTP Request is not success, Response code is " + responseDrg.getStatusLine().getStatusCode());
            }

            if (HttpStatus.SC_OK == responseDrg.getStatusLine().getStatusCode()) {
                responseContent = EntityUtils.toString(entity, CHARSET_UTF_8);
                EntityUtils.consume(entity);
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new BusException("请求异常："+ (StringUtils.isNotBlank(e.getMessage())?e.getMessage():(e.getCause()!=null?e.getCause().getMessage():"")));
        } finally {
            try {
                // 释放资源
                if (responseDrg != null) {
                	responseDrg.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return responseContent;
    }

    /**
     * 发送 post请求 上传文件
     *
     * @param httpUrl    地址
     * @throws IOException
     */
    public static String sendHttpPost(String httpUrl, MultipartFile file, String code, Map<String, String> heads) throws IOException {
        HttpPost httpPost = new HttpPost(httpUrl);// 创建httpPost

        for (Map.Entry<String, String> entry : heads.entrySet()) {
            httpPost.addHeader(entry.getKey(), entry.getValue());
        }


        InputStream fis = file.getInputStream();//new FileInputStream(file);
        ByteArrayOutputStream bos = new ByteArrayOutputStream(1024);
        byte[] b = new byte[1024];
        int n;
        while ((n = fis.read(b)) != -1) {
            bos.write(b, 0, n);
        }
        fis.close();

        return sendHttpPost(httpPost);
    }

    /**
     * 发送 post请求 下载文件
     *
     * @param httpUrl    地址
     * @param paramsJson 参数(格式 json)
     * @param heads 头信息
     * @param saveFile
     * @throws IOException
     */
    public static void sendHttpDownload(String httpUrl, String paramsJson, Map<String, String> heads,File saveFile) {
        CloseableHttpClient httpClient = null;
        CloseableHttpResponse response = null;
        try {
            HttpPost httpPost = new HttpPost(httpUrl);
            httpClient = getHttpClient();
            httpPost.setConfig(requestConfig);
            if (StringUtils.isNotBlank(paramsJson)) {
                StringEntity stringEntity = new StringEntity(paramsJson, "UTF-8");
                stringEntity.setContentType(CONTENT_TYPE_JSON_URL);
                httpPost.setEntity(stringEntity);
            }
            for (Map.Entry<String, String> entry : heads.entrySet()) {
                httpPost.addHeader(entry.getKey(), entry.getValue());
            }
            response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            InputStream is = entity.getContent();
            if (HttpStatus.SC_OK == response.getStatusLine().getStatusCode()) {
                FileOutputStream fileout = new FileOutputStream(saveFile);
                byte[] buffer=new byte[1024];
                int ch = 0;
                while ((ch = is.read(buffer)) != -1) {
                    fileout.write(buffer,0,ch);
                }
                fileout.flush();
                fileout.close();
                is.close();
            }

        } catch (Exception e) {
            logger.error("sendHttpPostError:",e);
            throw new BusException("请求异常："+ (StringUtils.isNotBlank(e.getMessage())?e.getMessage():(e.getCause()!=null?e.getCause().getMessage():"")));
        } finally {
            try {
                // 释放资源
                if (response != null) {
                    response.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
