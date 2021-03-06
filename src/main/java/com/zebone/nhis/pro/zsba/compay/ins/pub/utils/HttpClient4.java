package com.zebone.nhis.pro.zsba.compay.ins.pub.utils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;







import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import net.sf.json.JSONObject;

import com.zebone.nhis.pro.zsba.compay.ins.qgyb.utils.DateUtils;
import com.zebone.nhis.pro.zsba.compay.ins.qgyb.utils.FileUtils;
import com.zebone.nhis.pro.zsba.compay.pub.vo.SettleCallBack;
import com.zebone.platform.common.support.UserContext;

public class HttpClient4 {
	public static String doGet(String url) {
        CloseableHttpClient httpClient = null;
        CloseableHttpResponse response = null;
        String result = "";
        try {
            // 通过址默认配置创建一个httpClient实例
            httpClient = HttpClients.createDefault();
            // 创建httpGet远程连接实例
            HttpGet httpGet = new HttpGet(url);
            // 设置请求头信息，鉴权
            httpGet.setHeader("Authorization", "Bearer da3efcbf-0845-4fe3-8aba-ee040be542c0");
            // 设置配置请求参数
            RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(35000)// 连接主机服务超时时间
                    .setConnectionRequestTimeout(35000)// 请求超时时间
                    .setSocketTimeout(60000)// 数据读取超时时间
                    .build();
            // 为httpGet实例设置配置
            httpGet.setConfig(requestConfig);
            // 执行get请求得到返回对象
            response = httpClient.execute(httpGet);
            // 通过返回对象获取返回数据
            HttpEntity entity = response.getEntity();
            // 通过EntityUtils中的toString方法将结果转换为字符串
            result = EntityUtils.toString(entity);
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // 关闭资源
            if (null != response) {
                try {
                    response.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (null != httpClient) {
                try {
                    httpClient.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

    public static String doPost(String url, Map<String, Object> paramMap) {
        CloseableHttpClient httpClient = null;
        CloseableHttpResponse httpResponse = null;
        String result = "";
        // 创建httpClient实例
        httpClient = HttpClients.createDefault();
        // 创建httpPost远程连接实例
        HttpPost httpPost = new HttpPost(url);
        // 配置请求参数实例
        RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(35000)// 设置连接主机服务超时时间
                .setConnectionRequestTimeout(35000)// 设置连接请求超时时间
                .setSocketTimeout(60000)// 设置读取数据连接超时时间
                .build();
        // 为httpPost实例设置配置
        httpPost.setConfig(requestConfig);
        // 设置请求头
        //httpPost.addHeader("Content-Type", "application/x-www-form-urlencoded");
        //httpPost.setContentEncoding("UTF-8");
        //httpPost.addHeader("Content-Type", "application/json");
        JSONObject jsonObj=JSONObject.fromObject(paramMap);
        // 解决中文乱码问题
		StringEntity entityR = new StringEntity(jsonObj.toString(),
				"utf-8");
		entityR.setContentEncoding("UTF-8");
		entityR.setContentType("application/json");
		FileUtils.writerAndCreateFile("\n"+DateUtils.getDateTime()+" 入参：\n"+jsonObj.toString(), "dzpz-"+UserContext.getUser().getCodeEmp()+".txt");
        httpPost.setEntity(entityR);       
        try {
            // httpClient对象执行post请求,并返回响应参数对象
            httpResponse = httpClient.execute(httpPost);
            // 从响应对象中获取响应内容
            HttpEntity entity = httpResponse.getEntity();
            result = EntityUtils.toString(entity);
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // 关闭资源
            if (null != httpResponse) {
                try {
                    httpResponse.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (null != httpClient) {
                try {
                    httpClient.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        FileUtils.writerAndCreateFile("\n"+DateUtils.getDateTime()+" 回参：\n"+result, "dzpz-"+UserContext.getUser().getCodeEmp()+".txt");
        return result;
    }
    
    public static String doPost2(String url, JSONObject paramMap) {
        CloseableHttpClient httpClient = null;
        CloseableHttpResponse httpResponse = null;
        String result = "";
        // 创建httpClient实例
        httpClient = HttpClients.createDefault();
        // 创建httpPost远程连接实例
        HttpPost httpPost = new HttpPost(url);
        // 配置请求参数实例
        RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(35000)// 设置连接主机服务超时时间
                .setConnectionRequestTimeout(35000)// 设置连接请求超时时间
                .setSocketTimeout(60000)// 设置读取数据连接超时时间
                .build();
        // 为httpPost实例设置配置
        httpPost.setConfig(requestConfig);
        // 设置请求头
        //httpPost.addHeader("Content-Type", "application/x-www-form-urlencoded");
        //httpPost.setContentEncoding("UTF-8");
        //httpPost.addHeader("Content-Type", "application/json");
        //JSONObject jsonObj=new JSONObject(paramMap);
        // 解决中文乱码问题
		StringEntity entityR = new StringEntity(paramMap.toString(),
				"utf-8");
		entityR.setContentEncoding("UTF-8");
		entityR.setContentType("application/json");
        System.out.println("电子凭证入参"+paramMap.toString());
        httpPost.setEntity(entityR);     
        try {
            // httpClient对象执行post请求,并返回响应参数对象
            httpResponse = httpClient.execute(httpPost);
            // 从响应对象中获取响应内容
            HttpEntity entity = httpResponse.getEntity();
            result = EntityUtils.toString(entity);
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // 关闭资源
            if (null != httpResponse) {
                try {
                    httpResponse.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (null != httpClient) {
                try {
                    httpClient.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        System.out.println("电子凭证回参"+result);
        return result;
    }
    
	/**
	 * post请求
	 * 
	 * @param url
	 *            url地址
	 * @param jsonParam
	 *            参数
	 * @param noNeedResponse
	 *            不需要返回结果
	 * @return
	 */
	public static String httpPost(String url, JSONObject jsonParam,
			boolean noNeedResponse) {
		//logger.info("post请求提交URL:"+url+"?"+jsonParam.toString());
		// post请求返回结果
		DefaultHttpClient httpClient = new DefaultHttpClient();
		String jsonStr = null;
		HttpPost method = new HttpPost(url);
		try {
			System.out.println("电子凭证入参"+jsonParam.toString());
			if (null != jsonParam) {
				// 解决中文乱码问题
				StringEntity entity = new StringEntity(jsonParam.toString(),
						"utf-8");
				entity.setContentEncoding("UTF-8");
				entity.setContentType("application/json");
				method.setEntity(entity);
			}
			HttpResponse result = httpClient.execute(method);
			url = URLDecoder.decode(url, "UTF-8");
			/** 请求发送成功，并得到响应 **/
			if (result.getStatusLine().getStatusCode() == 200) {
				String str = "";
				try {
					/** 读取服务器返回过来的json字符串数据 **/
					str = EntityUtils.toString(result.getEntity());
					if (noNeedResponse) {
						return null;
					}
					/** 把json字符串转换成json对象 **/
					//jsonStr = StringUtils.strip(str,"[]");
					//logger.debug("接口返回数据："+jsonStr);
				} catch (Exception e) {
					//logger.error("post请求提交失败:"+e.getMessage());
				}
			}
		} catch (IOException e) {
			//logger.error("post请求提交失败:"+e.getMessage());
		}
		System.out.println("电子凭证回参"+jsonStr);
		return jsonStr;
	}
    
    public static final String CHARSET = "UTF-8";
    //post请求方法
    public static  String sendPost(String url, Map<String,Object> params) {
       String response = null;
       System.out.println(url);
       System.out.println(params);
       try {
           List<NameValuePair> pairs = null;
            if (params != null && !params.isEmpty()) {
                pairs = new ArrayList<NameValuePair>(params.size());
                for (String key : params.keySet()) {
                    pairs.add(new BasicNameValuePair(key, params.get(key).toString()));
            }
            }
           CloseableHttpClient httpclient = null;
           CloseableHttpResponse httpresponse = null;
           try {
               httpclient = HttpClients.createDefault();
               HttpPost httppost = new HttpPost(url);
              // StringEntity stringentity = new StringEntity(data);
               if (pairs != null && pairs.size() > 0) {
                   httppost.setEntity(new UrlEncodedFormEntity(pairs, CHARSET));
               }
               httpresponse = httpclient.execute(httppost);
               response = EntityUtils
                       .toString(httpresponse.getEntity());
               System.out.println(response);
           } finally {
               if (httpclient != null) {
                   httpclient.close();
               }
               if (httpresponse != null) {
                   httpresponse.close();
               }
           }
       } catch (Exception e) {
           e.printStackTrace();
       }
       return response;
    }
}
