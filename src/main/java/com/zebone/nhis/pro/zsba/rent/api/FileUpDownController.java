package com.zebone.nhis.pro.zsba.rent.api;

import java.io.File;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.zebone.nhis.pro.zsba.common.support.JsonResult;
import com.zebone.nhis.pro.zsba.common.support.StringTools;

/**
 * 文-上传、下载
 * @author lipz
 *
 */
@Controller
@RequestMapping("/static/file")
public class FileUpDownController {

	private static Logger logger = LoggerFactory.getLogger(FileUpDownController.class);
	
	private static String subDri = "src/main/static/rentApp";//归类到出租设备目录下
	
	/**
	 * 上传文件
	 * @param request
	 * @param response
	 */
	@RequestMapping("/up")
	public void upFile(HttpServletRequest request, HttpServletResponse response){
		String jsonData = "";
    	try {
    		StringBuffer filePath = new StringBuffer("");
    		
    		// 转换request，解析出request中的文件
    		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
    		// 获取文件map集合
    		Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
    		// 循环遍历，取出单个文件
    		for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {
    			// 获取单个文件保存到本地
    			MultipartFile file = entity.getValue();
    			String[] paths = FileDriUtils.save(file, subDri);
    			filePath.append(paths[0]).append(",");
    			
    			//再将本地文件上传到FTP
    			FtpUtils.connectServer();
    			FtpUtils.upload(paths[1], paths[0]);
    			
    		}
    		String paths = filePath.toString();
    		if(paths.length()>0){
    			jsonData = JsonResult.toJsonObject(new JsonResult(paths.substring(0, paths.length()-1)).success());
    		}else{
    			jsonData = JsonResult.toJsonObject(new JsonResult().fail(JsonResult.NOT_EXIST, "上传失败，文件为空"));
    		}
		} catch (Exception e) {
			logger.error("文件上传异常：{}", e.getLocalizedMessage());
			jsonData = JsonResult.toJsonObject(new JsonResult().fail(JsonResult.SYSTEM_ERROR, "[upFile]系统异常："+e.getMessage()));
		} finally{
			StringTools.writeResp(response, jsonData);
		}
	}
	
	/**
	 * 获取文件相对路径
	 * @param fileName
	 * @param request
	 * @param response
	 */
	@RequestMapping("/get")
	public void getFile(String fileName, HttpServletRequest request, HttpServletResponse response){
		String jsonData = "";
    	try {
    		if(StringUtils.isEmpty(fileName)){
    			jsonData = JsonResult.toJsonObject(new JsonResult().fail(JsonResult.NOT_ALLOWED, "文件名不能为空"));
    		}else{
    			StringBuffer filePath = new StringBuffer("");
    			String[] files = fileName.split(",");
    			for (String name : files) {
    				String localFullPath = FileDriUtils.getUploadPath(subDri) + "/" + name;
					File file = new File(localFullPath);
					if (!file.exists()) {
						FtpUtils.connectServer();
						FtpUtils.download(name, localFullPath);
					}
					filePath.append(subDri).append("/").append(name).append(",");
				}
    			String paths = filePath.toString();
    			if(paths.length()>0){
    				jsonData = JsonResult.toJsonObject(new JsonResult(paths.substring(0, paths.length()-1)).success());
    			}else{
    				jsonData = JsonResult.toJsonObject(new JsonResult().fail(JsonResult.NOT_EXIST, "获取文件为空"));
    			}
    		}
		} catch (Exception e) {
			logger.error("文件上传异常：{}", e.getLocalizedMessage());
			jsonData = JsonResult.toJsonObject(new JsonResult().fail(JsonResult.SYSTEM_ERROR, "[upFile]系统异常："+e.getMessage()));
		} finally{
			StringTools.writeResp(response, jsonData);
		}
	}

}
