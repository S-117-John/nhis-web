package com.zebone.nhis.ma.pub.platform.sd.util;

import java.io.File;
import java.io.FileWriter;

import org.slf4j.Logger;


/**
 * 消息文件处理工具类(灵璧直接复制版)
 * @author maijiaxing
 *
 */
public class SDMsgFileUtils {
	private static Logger log = org.slf4j.LoggerFactory.getLogger(SDMsgFileUtils.class.getName());
	//private static SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");

    /**
     * 文件创建
     * @param fileName 文件名
     * @param content 内容
     * @param path 路径
     * @param fileExtension 文件扩展名
     */
    public static void fileCreate(String fileName,String content,String path,String fileExtension){
		try {
			log.info(path+"---"+fileName);
			//创建文件夹
			File file = new File(path);
			if(!file.exists()){
				file.mkdir();
			}
			
			if(fileExtension==null||fileExtension.equals("")){
				fileExtension="hl7";
			}
			FileWriter fw = new FileWriter(path+fileName+"."+fileExtension);
			fw.write(content);
			fw.flush();
			fw.close();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}finally{
			
		}
	}
}
