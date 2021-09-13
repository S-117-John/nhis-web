package com.zebone.nhis.pro.zsba.compay.ins.qgyb.utils;

import net.sf.json.JSONObject;

import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.zebone.nhis.common.support.DateUtils;
import com.zebone.platform.common.support.UserContext;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
* 描述: 医保接口调用示例
*
* @author wangjl
*/
public class YbHttpClass {

	//测试环境
/*	 private static final String url = "http://192.168.0.20:80/ebus/gdyb_inf/poc/hsa/hgs/";
	 private static final String xRioPaasid = "test_hosp";    //应用账户编码，在医保注册提供
	 private static final String secretKey = "RhaDw4H0RUbWYyTxmRKM1eSeN0qyGLds"; //调用者的token码 在医保注册提供
	*/
	 //正式环境
	 private static final String url = "http://192.168.0.20/ebus/gdyb_api/prd/hsa/hgs/";
	 private static final String xRioPaasid = "zs_sc_rmyy";    //应用账户编码，在医保注册提供
	 private static final String secretKey = "Z5osNCopSWdqErKicwNIVd8lAzZ8SY9W"; //调用者的token码 在医保注册提供

	 /**
	 * 调用普通交易及文件下载交易
	 */
	 public static String HttpRequestYbQg(String funNo, String param) {
		 Map<String, Object> returnMap = new HashMap<String, Object>();
		 //System.out.println("医保功能号："+funNo+"入参："+param);
		 FileUtils.writerAndCreateFile("\n"+DateUtils.getDateTime()+" 入参：医保功能号："+funNo + "\n"+param, "yb-"+UserContext.getUser().getCodeEmp()+".txt");
		 //FileUtils.writerAndCreateFile("\n"+DateUtils.getDateTime()+" 入参：\n医保功能号："+funNo + "--"+param, "yb-"+"测试"+".txt");
		 String returnMsg = null;
		
		String xRioNonce = getRandomString(32);           //随机数
        String xRioTimestamp = String.valueOf(getCurrentUnixSeconds());    //时间戳
        String signature = getSHA256Str(xRioTimestamp + secretKey + xRioNonce + xRioTimestamp);   //签名
		 
		 CloseableHttpClient httpclient = HttpClients.createDefault();
		 HttpPost httppost = new HttpPost(url+funNo);
		 //setConnectTimeout：设置连接超时时间，单位毫秒。
		 //setConnectionRequestTimeout：设置从connect Manager获取Connection 超时时间，单位毫秒。这个属性是新加的属性，因为目前版本是可以共享连接池的。
		 //setSocketTimeout：请求获取数据的超时时间，单位毫秒。 如果访问一个接口，多少时间内无法返回数据，就直接放弃此次调用
		 RequestConfig requestConfig = 
		 RequestConfig.custom().setConnectTimeout(180000).setSocketTimeout(180000).build();
		 httppost.setConfig(requestConfig);
		 //请求头
		 httppost.addHeader("x-tif-paasid", xRioPaasid);
		 httppost.addHeader("x-tif-signature", signature);
		 httppost.setHeader("x-tif-timestamp", xRioTimestamp);
		 httppost.addHeader("x-tif-nonce", xRioNonce);
		 ByteArrayEntity entity = new 
		 ByteArrayEntity(param.getBytes(StandardCharsets.UTF_8));
		 entity.setContentType("text/plain");
		 httppost.setEntity(entity);
		 CloseableHttpResponse response = null;
		 try {
			 response = httpclient.execute(httppost);
			 int statusCode = response.getStatusLine().getStatusCode();
			 if (statusCode != HttpStatus.SC_OK) {
				 httppost.abort();
				 returnMap.put("infcode", "-1");
				 returnMap.put("err_msg", "HttpClient,error status code :" + 
						 statusCode);
				 JSONObject jsonObj=JSONObject.fromObject(returnMap);
				 returnMsg = jsonObj.toString();
				 //throw new RuntimeException("HttpClient,error status code :" + 
				 //statusCode);
			}else{
				HttpEntity responseEntity = response.getEntity();
				 String result;
				 if (responseEntity != null) {
					 if (responseEntity.getContentType().getValue().contains("application/octet-stream")) {
						 InputStream content = responseEntity.getContent();

						 //返回文件流
						 File file = new File("");
				         String filePath = file.getCanonicalPath();
				         int idx = filePath.lastIndexOf("\\");
				         filePath = filePath.substring(0, idx)+"\\logs\\ybxzdzlog";
				         String fileName = DateUtils.formatDate(new Date(),"yyyyMMddHHmmss");
				         String filePathParent = filePath+"/"+com.zebone.nhis.pro.zsba.compay.ins.qgyb.utils.DateUtils.getYear()+"/"+com.zebone.nhis.pro.zsba.compay.ins.qgyb.utils.DateUtils.getMonth();
				         filePath = filePathParent +"/"+fileName+".zip";
						 //File file = new File("testDownload.txt");
				         file = new File(filePath);
				         if(!file.getParentFile().exists()){
				        	 FileUtils.createDirectory(filePathParent);
						 }
						 FileOutputStream fileOutputStream = new FileOutputStream(file);
						 int temp;
						 while ((temp = content.read()) != -1) {
							 fileOutputStream.write(temp);
						 }
						 fileOutputStream.close();
						 returnMap.put("infcode", "0");
						 returnMap.put("err_msg", "下载成功，文件地址："+filePath);
						 returnMap.put("fileAddr", filePath);
						 JSONObject jsonObj=JSONObject.fromObject(returnMap);
						 returnMsg = jsonObj.toString();
					 } else {
						 //返回字符串
						 result = EntityUtils.toString(responseEntity, "UTF-8");
						 //System.out.println(result);
						 returnMsg = result;
					 }
				 }
				 EntityUtils.consume(entity);
			}
	 } catch (ClientProtocolException e) {
		 returnMap.put("infcode", "-1");
		 returnMap.put("err_msg", "提交给服务器的请求，不符合 HTTP 协议"+e.getMessage());
		 JSONObject jsonObj=JSONObject.fromObject(returnMap);
		 returnMsg = jsonObj.toString();
		 //throw new RuntimeException("提交给服务器的请求，不符合 HTTP 协议", e);
	 } catch (IOException e) {
		 returnMap.put("infcode", "-1");
		 returnMap.put("err_msg", "向服务器承保接口发起 http 请求,执行 post 请求异常"+e.getMessage());
		 JSONObject jsonObj=JSONObject.fromObject(returnMap);
		 returnMsg = jsonObj.toString();
		 //throw new RuntimeException("向服务器承保接口发起 http 请求,执行 post 请求异常", e);
	 } finally {
		 if (response != null) {
			 try {
				 response.close();
			 } catch (IOException e) {
				 e.printStackTrace();
			 }
		 }
		 if (httpclient != null) {
			 try {
				 httpclient.close();
			 } catch (IOException e) {
				 e.printStackTrace();
			 }
		 }
	 	}
		 FileUtils.writerAndCreateFile("\n"+DateUtils.getDateTime()+" 回参：医保功能号："+funNo + "\n"+returnMsg, "yb-"+UserContext.getUser().getCodeEmp()+".txt");
		 //FileUtils.writerAndCreateFile("\n"+DateUtils.getDateTime()+" 回参：\n医保功能号："+funNo + "--"+returnMsg, "yb-"+"测试"+".txt");
		 
		 return returnMsg;
	 }

	 /**
	 * 调用普通交易及文件下载交易
	 */
	 public static String HttpRequestYbQgJson(String funNo, String param) {
		 Map<String, Object> returnMap = new HashMap<String, Object>();
		 //System.out.println("医保功能号："+funNo+"入参："+param);
		 FileUtils.writerAndCreateFile("\n"+DateUtils.getDateTime()+" 入参：医保功能号："+funNo + "\n"+param, "yb-"+UserContext.getUser().getCodeEmp()+".txt");
		 //FileUtils.writerAndCreateFile("\n"+DateUtils.getDateTime()+" 入参：\n医保功能号："+funNo + "--"+param, "yb-"+"测试"+".txt");
		 String returnMsg = null;
		
		String xRioNonce = getRandomString(32);           //随机数
        String xRioTimestamp = String.valueOf(getCurrentUnixSeconds());    //时间戳
        String signature = getSHA256Str(xRioTimestamp + secretKey + xRioNonce + xRioTimestamp);   //签名
		 
		 CloseableHttpClient httpclient = HttpClients.createDefault();
		 HttpPost httppost = new HttpPost(url+funNo);
		 //setConnectTimeout：设置连接超时时间，单位毫秒。
		 //setConnectionRequestTimeout：设置从connect Manager获取Connection 超时时间，单位毫秒。这个属性是新加的属性，因为目前版本是可以共享连接池的。
		 //setSocketTimeout：请求获取数据的超时时间，单位毫秒。 如果访问一个接口，多少时间内无法返回数据，就直接放弃此次调用
		 RequestConfig requestConfig = 
		 RequestConfig.custom().setConnectTimeout(180000).setSocketTimeout(180000).build();
		 httppost.setConfig(requestConfig);
		 //请求头
		 httppost.addHeader("x-tif-paasid", xRioPaasid);
		 httppost.addHeader("x-tif-signature", signature);
		 httppost.setHeader("x-tif-timestamp", xRioTimestamp);
		 httppost.addHeader("x-tif-nonce", xRioNonce);
		 ByteArrayEntity entity = new 
		 ByteArrayEntity(param.getBytes(StandardCharsets.UTF_8));
		 entity.setContentType("application/json");
		 httppost.setEntity(entity);
		 CloseableHttpResponse response = null;
		 try {
			 response = httpclient.execute(httppost);
			 int statusCode = response.getStatusLine().getStatusCode();
			 if (statusCode != HttpStatus.SC_OK) {
				 httppost.abort();
				 returnMap.put("infcode", "-1");
				 returnMap.put("err_msg", "HttpClient,error status code :" + 
						 statusCode);
				 JSONObject jsonObj=JSONObject.fromObject(returnMap);
				 returnMsg = jsonObj.toString();
				 //throw new RuntimeException("HttpClient,error status code :" + 
				 //statusCode);
			}else{
				HttpEntity responseEntity = response.getEntity();
				 String result;
				 if (responseEntity != null) {
					 if (responseEntity.getContentType().getValue().contains("application/octet-stream")) {
						 InputStream content = responseEntity.getContent();
						 //返回文件流
						 File file = new File("");
				         String filePath = file.getCanonicalPath();
				         String fileName = DateUtils.formatDate(new Date(),"yyyyMMddHHmmss");
				         filePath = filePath+"/dz"+"/"+com.zebone.nhis.pro.zsba.compay.ins.qgyb.utils.DateUtils.getYear()+"/"+com.zebone.nhis.pro.zsba.compay.ins.qgyb.utils.DateUtils.getMonth()+"/"+fileName+".zip";
						 //File file = new File("testDownload.txt");
				         file = new File(filePath);
				         if(!file.getParentFile().exists()){
								file.getParentFile().mkdir();
							}
						 FileOutputStream fileOutputStream = new FileOutputStream(file);
						 int temp;
						 while ((temp = content.read()) != -1) {
							 fileOutputStream.write(temp);
						 }
						 fileOutputStream.close();
						 returnMap.put("infcode", "0");
						 returnMap.put("err_msg", "下载成功，文件地址："+filePath);
						 JSONObject jsonObj=JSONObject.fromObject(returnMap);
						 returnMsg = jsonObj.toString();
					 } else {
						 //返回字符串
						 result = EntityUtils.toString(responseEntity, "UTF-8");
						 //System.out.println(result);
						 returnMsg = result;
					 }
				 }
				 EntityUtils.consume(entity);
			}
	 } catch (ClientProtocolException e) {
		 returnMap.put("infcode", "-1");
		 returnMap.put("err_msg", "提交给服务器的请求，不符合 HTTP 协议"+e.getMessage());
		 JSONObject jsonObj=JSONObject.fromObject(returnMap);
		 returnMsg = jsonObj.toString();
		 //throw new RuntimeException("提交给服务器的请求，不符合 HTTP 协议", e);
	 } catch (IOException e) {
		 returnMap.put("infcode", "-1");
		 returnMap.put("err_msg", "向服务器承保接口发起 http 请求,执行 post 请求异常"+e.getMessage());
		 JSONObject jsonObj=JSONObject.fromObject(returnMap);
		 returnMsg = jsonObj.toString();
		 //throw new RuntimeException("向服务器承保接口发起 http 请求,执行 post 请求异常", e);
	 } finally {
		 if (response != null) {
			 try {
				 response.close();
			 } catch (IOException e) {
				 e.printStackTrace();
			 }
		 }
		 if (httpclient != null) {
			 try {
				 httpclient.close();
			 } catch (IOException e) {
				 e.printStackTrace();
			 }
		 }
	 	}
		 FileUtils.writerAndCreateFile("\n"+DateUtils.getDateTime()+" 回参：医保功能号："+funNo + "\n"+returnMsg, "yb-"+UserContext.getUser().getCodeEmp()+".txt");
		 //FileUtils.writerAndCreateFile("\n"+DateUtils.getDateTime()+" 回参：\n医保功能号："+funNo + "--"+returnMsg, "yb-"+"测试"+".txt");
		 
		 return returnMsg;
	 }

	 
	 /**
	  * 测试   调用普通交易及文件下载交易
	  * @param funNo
	  * @param param
	  * @return
	  */
	 public static String HttpRequestYbQgCs(String funNo, String param) {
		 Map<String, Object> returnMap = new HashMap<String, Object>();
		 System.out.println("医保功能号："+funNo+"入参："+param);
		 FileUtils.writerAndCreateFile("\n"+DateUtils.getDateTime()+" 入参：\n医保功能号："+funNo + "--"+param, "yb-"+"测试"+".txt");
		 String returnMsg = null;
		 
		 String xRioPaasid = "test_hosp";    //应用账户编码，在医保注册提供
        String secretKey = "RhaDw4H0RUbWYyTxmRKM1eSeN0qyGLds"; //调用者的token码 在医保注册提供
        String xRioNonce = getRandomString(32);           //随机数
        String xRioTimestamp = String.valueOf(getCurrentUnixSeconds());    //时间戳
        String signature = getSHA256Str(xRioTimestamp + secretKey + xRioNonce + xRioTimestamp);   //签名
		 
		 CloseableHttpClient httpclient = HttpClients.createDefault();
		 HttpPost httppost = new HttpPost(url+funNo);
		 //setConnectTimeout：设置连接超时时间，单位毫秒。
		 //setConnectionRequestTimeout：设置从connect Manager获取Connection 超时时间，单位毫秒。这个属性是新加的属性，因为目前版本是可以共享连接池的。
		 //setSocketTimeout：请求获取数据的超时时间，单位毫秒。 如果访问一个接口，多少时间内无法返回数据，就直接放弃此次调用
		 RequestConfig requestConfig = 
		 RequestConfig.custom().setConnectTimeout(30000).setSocketTimeout(30000).build();
		 httppost.setConfig(requestConfig);
		 //请求头
		 httppost.addHeader("x-tif-paasid", xRioPaasid);
		 httppost.addHeader("x-tif-signature", signature);
		 httppost.setHeader("x-tif-timestamp", xRioTimestamp);
		 httppost.addHeader("x-tif-nonce", xRioNonce);
		 ByteArrayEntity entity = new 
		 ByteArrayEntity(param.getBytes(StandardCharsets.UTF_8));
		 entity.setContentType("text/plain");
		 httppost.setEntity(entity);
		 CloseableHttpResponse response = null;
		 try {
			 response = httpclient.execute(httppost);
			 int statusCode = response.getStatusLine().getStatusCode();
			 if (statusCode != HttpStatus.SC_OK) {
				 httppost.abort();
				 returnMap.put("infcode", "-1");
				 returnMap.put("err_msg", "HttpClient,error status code :" + 
						 statusCode);
				 JSONObject jsonObj=JSONObject.fromObject(returnMap);
				 returnMsg = jsonObj.toString();
				 //throw new RuntimeException("HttpClient,error status code :" + 
				 //statusCode);
			}else{
				HttpEntity responseEntity = response.getEntity();
				 String result;
				 if (responseEntity != null) {
					 if (responseEntity.getContentType().getValue().contains("application/octet-stream")) {
						 InputStream content = responseEntity.getContent();
						 //返回文件流
						 File file = new File("testDownload.txt");
						 FileOutputStream fileOutputStream = new FileOutputStream(file);
						 int temp;
						 while ((temp = content.read()) != -1) {
							 fileOutputStream.write(temp);
						 }
						 fileOutputStream.close();
					 } else {
						 //返回字符串
						 result = EntityUtils.toString(responseEntity, "UTF-8");
						 System.out.println(result);
						 returnMsg = result;
					 }
				 }
				 EntityUtils.consume(entity);
			}
	 } catch (ClientProtocolException e) {
		 returnMap.put("infcode", "-1");
		 returnMap.put("err_msg", "提交给服务器的请求，不符合 HTTP 协议"+e.getMessage());
		 JSONObject jsonObj=JSONObject.fromObject(returnMap);
		 returnMsg = jsonObj.toString();
		 //throw new RuntimeException("提交给服务器的请求，不符合 HTTP 协议", e);
	 } catch (IOException e) {
		 returnMap.put("infcode", "-1");
		 returnMap.put("err_msg", "向服务器承保接口发起 http 请求,执行 post 请求异常"+e.getMessage());
		 JSONObject jsonObj=JSONObject.fromObject(returnMap);
		 returnMsg = jsonObj.toString();
		 //throw new RuntimeException("向服务器承保接口发起 http 请求,执行 post 请求异常", e);
	 } finally {
		 if (response != null) {
			 try {
				 response.close();
			 } catch (IOException e) {
				 e.printStackTrace();
			 }
		 }
		 if (httpclient != null) {
			 try {
				 httpclient.close();
			 } catch (IOException e) {
				 e.printStackTrace();
			 }
		 }
	 	}
		 FileUtils.writerAndCreateFile("\n"+DateUtils.getDateTime()+" 回参：\n医保功能号："+funNo + "--"+returnMsg, "yb-"+"测试"+".txt");
		 
		 return returnMsg;
	 }

	 
	 public static void main(String[] args) {
		 
		 Map<String, Object> map = new HashMap<String, Object>();
		 
		 map.put("infno","1101");//
		 map.put("msgid","H44020300006202010211931220001");//
		 map.put("insuplc_admdvs","");//参保地医保区划
		 map.put("mdtrtarea_admvs","");//就医地医保区划
		 map.put("recer_admdvs","01");//接收方医保区划代码
		 map.put("dev_no","");//设备编号
		 map.put("dev_safe_info","");//设备安全信息
		 map.put("signtype","SM3");//签名类型
		 map.put("cainfo","");//数字签名信息
		 map.put("infver","V1.0");//接口版本号
		 map.put("opter_type","1");//经办人类别 1-经办人；2-自助终端；3-移动终端
		 map.put("opter","1");//经办人 传入经办人编号
		 map.put("opter_name","测试");//经办人姓名
		 SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
		 String date = df.format(System.currentTimeMillis());
		 System.out.println(date);
		 map.put("inf_time", date);//交易时间
		 map.put("fixmedins_code","H44200100026");//定点医疗机构编号
		 map.put("sign_no","");//签到流水号
		 map.put("recer_sys_code","01");//
		 Map<String, Object> inputMap = new HashMap<String, Object>();
		 Map<String, Object> dataMap = new HashMap<String, Object>();
		 dataMap.put("mdtrt_cert_type", "02");//
		 dataMap.put("mdtrt_cert_no", "442000198006052785");//
		 dataMap.put("card_sn", "");//
		 dataMap.put("begntime", "");//
		 dataMap.put("psn_cert_type", "1");//
		 dataMap.put("certno", "");//
		 dataMap.put("psn_name", "");//
		 inputMap.put("data", dataMap);
		 map.put("input",inputMap);//交易输入
		 JSONObject jsonObject = JSONObject.fromObject(map);
		 System.out.println(jsonObject.toString());
		 HttpRequestYbQg("1101",jsonObject.toString());

	}
	 

	    /*** 获取随机数 */
	    public static String getRandomString(int length) {
	        String str = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
	        Random random = new Random();
	        StringBuffer sb = new StringBuffer();
	        for (int i = 0; i < length; ++i) {
	            int number = random.nextInt(62);
	            sb.append(str.charAt(number));
	        }
	        return sb.toString();
	    }

	    /*** 获取当前时间戳 */
	    private static long getCurrentUnixSeconds() {
	        ZoneOffset zoneOffset = ZoneOffset.ofHours(8);
	        LocalDateTime localDateTime = LocalDateTime.now();
	        return localDateTime.toEpochSecond(zoneOffset);
	    }

	    /*** 使用SHA-256算法进行加密 */
	    private static String getSHA256Str(String str) {
	        String encodeStr = "";
	        try {
	            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
	            messageDigest.update(str.getBytes("UTF-8"));
	            encodeStr = byte2Hex(messageDigest.digest());
	        } catch (NoSuchAlgorithmException var4) {
	            var4.printStackTrace();
	        } catch (UnsupportedEncodingException var5) {
	            var5.printStackTrace();
	        }
	        return encodeStr;
	    }

	    private static String byte2Hex(byte[] bytes) {
	        StringBuffer stringBuffer = new StringBuffer();
	        String temp = null;
	        for (int i = 0; i < bytes.length; ++i) {
	            temp = Integer.toHexString(bytes[i] & 255);
	            if (temp.length() == 1) {
	                stringBuffer.append("0");
	            }
	            stringBuffer.append(temp);
	        }
	        return stringBuffer.toString();
	    }
 
	    /**
		 * 格式化
		 * 
		 * @param jsonStr
		 * @return
		 * @author lizhgb
		 * @Date 2015-10-14 下午1:17:35
		 * @Modified 2017-04-28 下午8:55:35
		 */
		public static String formatJson(String jsonStr) {
			if (null == jsonStr || "".equals(jsonStr))
				return "";
			StringBuilder sb = new StringBuilder();
			char last = '\0';
			char current = '\0';
			int indent = 0;
			boolean isInQuotationMarks = false;
			for (int i = 0; i < jsonStr.length(); i++) {
				last = current;
				current = jsonStr.charAt(i);
				switch (current) {
				case '"':
	                                if (last != '\\'){
					    isInQuotationMarks = !isInQuotationMarks;
	                                }
					sb.append(current);
					break;
				case '{':
				case '[':
					sb.append(current);
					if (!isInQuotationMarks) {
						sb.append('\n');
						indent++;
						addIndentBlank(sb, indent);
					}
					break;
				case '}':
				case ']':
					if (!isInQuotationMarks) {
						sb.append('\n');
						indent--;
						addIndentBlank(sb, indent);
					}
					sb.append(current);
					break;
				case ',':
					sb.append(current);
					if (last != '\\' && !isInQuotationMarks) {
						sb.append('\n');
						addIndentBlank(sb, indent);
					}
					break;
				default:
					sb.append(current);
				}
			}
	 
			return sb.toString();
		}
	 
		/**
		 * 添加space
		 * 
		 * @param sb
		 * @param indent
		 * @author lizhgb
		 * @Date 2015-10-14 上午10:38:04
		 */
		private static void addIndentBlank(StringBuilder sb, int indent) {
			for (int i = 0; i < indent; i++) {
				sb.append('\t');
			}
		}

}
