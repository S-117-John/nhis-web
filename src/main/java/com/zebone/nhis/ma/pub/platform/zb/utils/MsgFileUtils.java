package com.zebone.nhis.ma.pub.platform.zb.utils;

import java.io.FileWriter;

import org.slf4j.Logger;

/**
 * 消息文件处理工具类
 * @author chengjia
 *
 */
public class MsgFileUtils {
	private static Logger log = org.slf4j.LoggerFactory.getLogger(MsgFileUtils.class.getName());
	
    /**
     * 文件创建
     * @param fileName 文件名
     * @param str 内容
     * @param path 路径
     * @param fileExtension 文件扩展名
     */
    public static void fileCreate(String fileName,String content,String path,String fileExtension){
		try {
			log.info(path+"---"+fileName);
			if(fileExtension==null||fileExtension.equals("")) fileExtension="hl7";
			FileWriter fw = new FileWriter(path+fileName+"."+fileExtension);
			fw.write(content);
			fw.flush();
			fw.close();
		} catch (Exception e) {
		}
	}
}
