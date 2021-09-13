package com.zebone.nhis.task.arch.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.apache.commons.net.ftp.FTPFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.common.support.FTPUtil;
import com.zebone.nhis.webservice.zhongshan.service.ArchOuterHandler;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.quartz.modle.QrtzJobCfg;


@Service
public class ArchTaskService {
	
	@Autowired
	private ArchOuterHandler handler;
	
	@SuppressWarnings("rawtypes")
	public Map executeTask(QrtzJobCfg cfg){
		String pkOrg = cfg.getJgs();
		pkOrg="~";
		if(CommonUtils.isEmptyString(pkOrg))
			throw new BusException("未获取到机构信息！");
		handler.executeTask();
		return null;
	}

	@SuppressWarnings("rawtypes")
	public Map fileMoveTask(QrtzJobCfg cfg) throws IOException{
		//1.获取根目录下的所有pdf,xml文件
		String pathRoot = "/";
		
		Set<String> fileNames = new HashSet<String>();
		Map<String,String> paths = new HashMap<String,String>();
		FTPUtil.connect();
		FTPFile[] files =FTPUtil.ftpClient.listFiles("/");
		if(files!=null && files.length>0){
			for(FTPFile file : files){
				if(!file.isDirectory()){
					fileNames.add(file.getName());
				}
			}
		}
		
		//2.根据文件名，查询对应的pv_arch_log表，获取到正确目File_path字段
		if(fileNames!=null && fileNames.size()>0){
			Map<String, Object> filePath = null;
			for(String key : fileNames){
				filePath = null;
				//2.1 pacs，lis的描述文件和病历文件
				String realName = null;
			    if(key.contains("_CA_")){
			    	realName = key.replace("_CA_", "_RP_");
			    }else{
			     //2.2 lis的CA文件	
			    	realName = key;
			    }
			    if(realName.contains(".pdf") || realName.contains(".xml")){
			    	realName = realName.substring(0, realName.lastIndexOf("."))+".pdf";
			    	filePath = DataBaseHelper.queryForMap("select file_path from pv_arch_log where file_name = ? and old_file is null ", new Object[]{realName});
			    	if(null != filePath && null != filePath.get("filePath") && !CommonUtils.isEmptyString(filePath.get("filePath").toString()))
			    		paths.put(key, filePath.get("filePath").toString());
			    }
			}
		}
		
		//3.将文件转移至file_path下,移动完成之后删除文件
		for(Entry<String,String> e : paths.entrySet()){
			String name =  e.getKey();
			String path =  e.getValue();
			
			FTPUtil.connect();
			FTPUtil.moveFile(name, "/", path);
			FTPUtil.removeFile(name);
		}
		return null;
	} 
	
}
