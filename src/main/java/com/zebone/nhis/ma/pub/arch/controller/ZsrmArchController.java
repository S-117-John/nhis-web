package com.zebone.nhis.ma.pub.arch.controller;


import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.ma.pub.arch.service.FileArchHandler;
import com.zebone.nhis.ma.pub.arch.service.FileArchService;
import com.zebone.nhis.ma.pub.arch.vo.ArchResponse;
import com.zebone.nhis.ma.pub.arch.vo.ArchResultVo;
import com.zebone.platform.modules.utils.JsonUtil;

/**
 * 人医病历归档服务入口http/json
 * @author chengjia
 *
 */
@Controller
@RequestMapping("/static/arch/")
public class ZsrmArchController {
    private static Logger logger = LoggerFactory.getLogger(FileArchService.class);


    @Resource
	private FileArchHandler archHandler;//归档服务
    
    @Resource
    private FileArchService archService;

    @RequestMapping(value = "/exec", method = RequestMethod.POST)
    @ResponseBody
    public Object exec(@RequestBody String param){
        String result = "";
        try {
            logger.info("归档入参内容:"+param);
//            Calendar oldDate=Calendar.getInstance();
//            oldDate.setTime(new Date());//设置为想要比较的日期
//            Long timeOld=oldDate.getTimeInMillis();
            
//            Calendar nowDate=Calendar.getInstance();
//            nowDate.setTime(new Date());//设置为当前系统时间
//            Long timeNow=nowDate.getTimeInMillis();
            
//            Long time = (timeNow-timeOld);//相差毫秒数
//            logger.info("time:"+time);
            result = archService.archive(param);
//            Calendar nowDate2=Calendar.getInstance();
//            nowDate2.setTime(new Date());//设置为当前系统时间
//            Long timeNow2=nowDate2.getTimeInMillis();
//            Long time2 = (timeNow2-timeNow);
//            logger.info("time2:"+time2);
            logger.info("result:"+result);
        } catch (Exception e){
           e.printStackTrace();
        } finally {
           
        }
        return result;
    }
    

    @RequestMapping(value = "/file", method = RequestMethod.POST)
    @ResponseBody
    public Object file(@RequestParam String funcId,@RequestParam String pkOrg,@RequestParam MultipartFile content){
        String result = "";
        ArchResultVo rtnError = null;
        try {
            logger.info("归档入参内容：funcId:"+funcId);
            logger.info("归档入参内容：pkOrg:"+pkOrg);
            if(CommonUtils.isEmptyString(funcId)){
            	rtnError = new ArchResultVo();
				rtnError.setResponse(ArchResponse.getErrorInstance("未获取到接口调用的功能号funcId!"));	
				return  JsonUtil.writeValueAsString(rtnError);	            	
    		}
            if(content==null) {
            	rtnError = new ArchResultVo();
				rtnError.setResponse(ArchResponse.getErrorInstance("未获取到接口调用的内容参数!"));	
				return  JsonUtil.writeValueAsString(rtnError);	
            }
            byte[] bytes = content.getBytes();
			
			String param = new String(bytes);
    			
            switch(funcId) {  
			case "ARCH_01"://业务系统文件上传接口
				result = archHandler.uploadFile(param,"2");
				break;
			} 
            
            logger.info("result:"+result);
        } catch (Exception e){
           e.printStackTrace();
        } finally {
           
        }
        return result;
    }
        
    /**
     * 文件上传
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    @ResponseBody
    public String upload(HttpServletRequest request) {

    	String result = "";
    	
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
       
        MultipartFile file = multipartRequest.getFile("file");
        byte[] bytes;
		try {
			bytes = file.getBytes();
			
			String param = new String(bytes);
			
			result = archService.archive(param);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
			return result;
		}
       
		return result;
        
    }
    
    
    @RequestMapping("/upload2") 
    @ResponseBody
    public String upload2(@RequestParam MultipartFile file,@RequestParam String messageContent ) { 
        //多个参数的话只要多个@RequestParam即可，注意参数名要和表单里面的属性名一致
     com.alibaba.fastjson.JSONObject json =new com.alibaba.fastjson.JSONObject();
     logger.info(messageContent);
      String orgiginalFileName = ""; 
      int m =new Random().nextInt(100)+10;
      logger.info("m="+m);
      String path="D:/"+m+"b.txt";
      try { 
        File newFile =new File(path);
        file.transferTo(newFile);
        String fileName = file.getName(); 
        InputStream inputStream = file.getInputStream(); 
        String content = file.getContentType(); 
        orgiginalFileName = file.getOriginalFilename(); 
        System.out.println("fileName: "+fileName+", inputStream: "+ inputStream 
              +"\r\n content: "+content+", orgiginalFileName: ="+ orgiginalFileName 
              +"\r\n projectName: ");   
      } catch (IOException e) { 
        e.printStackTrace(); 
      } 
      json.put("flag", true);
      json.put("message", "success");
      logger.info(json.toJSONString());
      return json.toJSONString(); 
    } 
    
    
    @RequestMapping(value = "upload3")
    @ResponseBody
    public void upload3(@RequestParam("file") MultipartFile file, @RequestParam("id") String id) throws Exception {
    //form表单提交的参数测试为String类型
     
        if (file == null) return ;
     
        File newFile =new File("d:\\File\\test");
        file.transferTo(newFile);
 
    }
  
   
}
