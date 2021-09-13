package com.zebone.nhis.pro.zsba.rent.api;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import com.zebone.platform.modules.exception.BusException;

/**
 * 文件-文件夹处理
 * @author Administrator
 *
 */
public class FileDriUtils {
	

	/**
	 * 保存文件，返回相对路径
	 * @param file		要保存的文件
	 * @param subDri	在容器根目录下追加子级目录
	 * @return			返回存放文件的相对路径,完整路径
	 */
    public static String[] save(MultipartFile file, String subDri) {
        String fileName = file.getOriginalFilename();
        
        // 上传保存路径
        String uploadPath = getUploadPath(subDri);
        
        // 创建文件夹
        File dir = new File(uploadPath);
        if (!dir.exists()){
        	dir.mkdirs();
        }
            
        // 创建上传后文件名称
        String newFileName = getUploadResName(fileName);
        
        // 保存文件
        String fullPath = uploadPath + File.separator + newFileName;
        File uploadFile = new File(fullPath);
        try {
            FileCopyUtils.copy(file.getBytes(), uploadFile);
        } catch (IOException e1) {
            throw new BusException("上传失败");
        }
        
        return new String[]{newFileName, fullPath};
    }
    
    /**
     * 获取保存路径
     * @param subDir	追加子级目录
     * @return
     */
    public static String getUploadPath(String joinPath){
		String filePath = "";
		try {
			String path = FileDriUtils.class.getResource("").toURI().getPath();
			int n = path.lastIndexOf("WEB-INF");
			if (n != -1) {
				if ("\\".equals(File.separator)) {
					filePath = path.substring(1, n) + joinPath;//windows系统下
				} else if ("/".equals(File.separator)) {
					filePath = path.substring(0, n) + joinPath;//linux系统下
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return filePath;
	}
    
    /**
     * 文件重命名
     * @param oldName	原来的文件名
     * @return			新的文件名
     */
    public static String getUploadResName(String oldName){
        String fileExt = oldName.substring(oldName.lastIndexOf(".") + 1).toLowerCase();
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
        String newFileName = df.format(new Date()) + "_" + new Random().nextInt(1000) + "." + fileExt;
        return newFileName;
    }
    
    

}
