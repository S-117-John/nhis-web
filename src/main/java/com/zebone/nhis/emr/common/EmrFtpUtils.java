package com.zebone.nhis.emr.common;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.io.IOUtils;
import org.apache.commons.net.ftp.FTPClient;
import org.slf4j.Logger;

import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.FTPUtil.UploadStatus;

/**
 * 电子病历ftp工具
 * @author chengjia
 *
 */
public class EmrFtpUtils {
	
	private static Logger logger = org.slf4j.LoggerFactory.getLogger(EmrFtpUtils.class);
    
    /**   
     * FTP上传单个文件   
     */   
    public static String fileUpload(String pathName,String fileName,byte[] fileData) {   
        FTPClient ftpClient = new FTPClient(); 
        String pathStr="";
        try {   
            ftpClient.connect(ApplicationUtils.getPropertyValue("emr.ftp.host", "127.0.0.1"),
            				Integer.parseInt(ApplicationUtils.getPropertyValue("emr.ftp.port", "21")));   
            ftpClient.login(ApplicationUtils.getPropertyValue("emr.ftp.user", "emr"), 
            				ApplicationUtils.getPropertyValue("emr.ftp.pwd", "emr"));
            String path=ApplicationUtils.getPropertyValue("emr.ftp.path", "emr");
            //ByteArrayInputStream in = new ByteArrayInputStream(fileData);
            //File srcFile = new File(localpath);   
            //fis = new FileInputStream(srcFile);   
            //设置上传目录   
            pathStr=path+pathName+"/";
            String directory = pathStr.substring(0, pathStr.lastIndexOf("/") + 1);
            if (!directory.equals("/")
    				&& !ftpClient.changeWorkingDirectory(new String(directory
    						.getBytes("GBK"), "iso-8859-1"))) {
    			// 如果远程目录不存在，则递归创建远程服务器目录
    			int start = 0;
    			int end = 0;
    			if (directory.startsWith("/")) {
    				start = 1;
    			} else {
    				start = 0;
    			}
    			end = directory.indexOf("/", start);
    			while (true) {
    				String subDirectory = new String(pathStr.substring(start, end)
    						.getBytes("GBK"), "iso-8859-1");
    				if (!ftpClient.changeWorkingDirectory(subDirectory)) {
    					if (ftpClient.makeDirectory(subDirectory)) {
    						ftpClient.changeWorkingDirectory(subDirectory);
    					} else {
    						logger.info("创建目录失败");
    						throw new RuntimeException("创建目录失败！");  
    					}
    				}

    				start = end + 1;
    				end = directory.indexOf("/", start);

    				// 检查所有目录是否创建完毕
    				if (end <= start) {
    					break;
    				}
    			}
    		}
            
            //System.out.println("pathStr:"+pathStr);
            ftpClient.changeWorkingDirectory(pathStr);   
            //ftpClient.setBufferSize(1024 * 1024 * 8);   
            ftpClient.setControlEncoding("utf-8");   
            ftpClient.enterLocalPassiveMode();
            //设置文件类型（二进制）   
            ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
            ftpClient.setFileTransferMode(FTPClient.BINARY_FILE_TYPE);
            //ftpClient.storeFile(filename, fis);
            
            OutputStream out = ftpClient.storeFileStream(new String(fileName.getBytes("GBK"), "iso-8859-1"));
            out.write(fileData, 0, fileData.length);
//            byte[] bytes = new byte[1024];
//    		int c;
//    		while ((c = fis.read(bytes)) != -1) {
//    			out.write(bytes, 0, c);
//    			
//    		}
    		out.flush();
    		
            // 关闭输入流  
            out.close();
            // 退出ftp  
            ftpClient.logout();  
            //System.out.println("====上传成功====");  
        } catch (IOException e) {   
            e.printStackTrace();   
            throw new RuntimeException("FTP客户端出错！", e);   
        } finally {   
            try {   
                ftpClient.disconnect();   
            } catch (IOException e) {   
                e.printStackTrace();   
                throw new RuntimeException("关闭FTP连接发生异常！", e);   
            }   
        }
        return pathStr;
    }

}
