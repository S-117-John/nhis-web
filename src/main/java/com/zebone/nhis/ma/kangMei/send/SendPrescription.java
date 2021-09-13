package com.zebone.nhis.ma.kangMei.send;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.zebone.nhis.common.support.ApplicationUtils;

@Service
public class SendPrescription {
	
	private Logger logger = LoggerFactory.getLogger("ma.syxInterface");
	
	/**
	 * httpURLConnection方式调用  webService
	 * @param strSendDate  加密后的xml
	 * @return
	 * @throws IOException
	 */
	public String sendHospDisp(String strSendDate) throws IOException{
		
		
		//第一步：创建服务地址  
		String getUrl=ApplicationUtils.getPropertyValue("KangMei.url", "0");
        URL url = new URL(getUrl);  
        
        //第二步：打开一个通向服务地址的连接  
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();  
        
        //第三步：设置参数  
        //3.1发送方式设置：POST必须大写  
        connection.setRequestMethod("POST");  
        
        //3.2设置数据格式：content-type  
        connection.setRequestProperty("content-type", "text/xml;charset=utf-8");  
        
        //3.3设置输入输出，因为默认新创建的connection没有读写权限，  
        connection.setDoInput(true);  
        connection.setDoOutput(true);  
  
        //第四步：组织SOAP数据，发送请求  
        String soapXML = strSendDate;  
        
        //将信息以流的方式发送出去
        OutputStream os = connection.getOutputStream();  
        os.write(soapXML.getBytes());  
        
        //第五步：接收服务端响应，打印  
        int responseCode = connection.getResponseCode();  
        logger.info("【康美发药接口】服务端响应，HTTP状态码："+responseCode);
        StringBuilder sb = null;
        if(200 == responseCode){//表示服务端响应成功  
            //获取当前连接请求返回的数据流
            InputStream is = connection.getInputStream();  
            InputStreamReader isr = new InputStreamReader(is);  
            BufferedReader br = new BufferedReader(isr);  
              
            sb = new StringBuilder();  
            String temp = null;  
            while(null != (temp = br.readLine())){  
                sb.append(temp);  
            }  
            
            /**
             * 打印结果
             */
//            System.out.println("康美返回的"+sb.toString());  
            
            is.close();  
            isr.close();  
            br.close();  
        }
        os.close(); 
		return sb.toString();
	}

}
