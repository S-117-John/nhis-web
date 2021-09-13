package com.zebone.nhis.ma.pub.arch.controller;


import java.io.IOException;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.zebone.nhis.ma.pub.arch.service.FileArchHandler;
import com.zebone.nhis.ma.pub.arch.service.FileArchService;

/**
 * 深大病历归档服务入口http/json
 * @author chengjia
 *
 */
@Controller
@RequestMapping("/static/sdarch/")
public class SdArchController {
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

            result = archService.archive(param);

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
    
  
}
