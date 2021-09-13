package com.zebone.platform.common.util;

import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.platform.modules.exception.BusException;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.regex.Pattern;


public class UploadUtils {
    
    
    public static boolean isUrl(String str) {
        Pattern exp = Pattern.compile("^http(s)?://([\\w-]+\\.)+[\\w-]+(/[\\w- ./?%&=]*)?", Pattern.CASE_INSENSITIVE);
        return exp.matcher(str).matches();
    }
    
    public static String getResUrl(String path) {
        if(StringUtils.isBlank(path)){
            return null;
        }
        if(isUrl(path)){
            return path;
        }
        if(path.startsWith("@")){
            return path.substring(path.indexOf("@") + 1);
        }else{
            String domain = ApplicationUtils.getPropertyValue("upload.domain", "");
            String resUrl = domain + path;
            return resUrl;
        }
    }

    public static String getResRelativePath(String resUrl) {
        String domain = ApplicationUtils.getPropertyValue("upload.domain", "");
        return resUrl.replace(domain, "");
    }

    public static String getUploadPath() {
        return ApplicationUtils.getPropertyValue("upload.path", "");//.replace('\\', File.pathSeparatorChar);
    }
    
    public static String getUploadResName(String oldName){
        String fileExt = oldName.substring(oldName.lastIndexOf(".") + 1).toLowerCase();
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
        String newFileName = df.format(new Date()) + "_" + new Random().nextInt(1000) + "." + fileExt;
        return newFileName;
    }
    
    public static String getUploadResName(String md5, String fileName) {
        String fileExt = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
        return md5 + "." + fileExt;
    }
    
    public static void deleteRes(String relativePath){
        String filePath = getUploadPath() + relativePath;
        File file = new File(filePath);
        if (file.exists())
            file.delete();
    }

    //返回图片保存的相对路径
    public static String save(MultipartFile file) {
        String fileName = file.getOriginalFilename();
        // 上传保存路径
        String uploadPath = getUploadPath();
        String ymd = DateTime.now().toString("yyyyMM");
        uploadPath += ymd + File.separator;
        // 创建文件夹
        File dir = new File(uploadPath);
        if (!dir.exists()) 
            dir.mkdirs();
        // 创建上传后文件名称
        String newFileName = getUploadResName(fileName);
        // 保存图片
        File uploadFile = new File(uploadPath + newFileName);
        try {
            FileCopyUtils.copy(file.getBytes(), uploadFile);
        } catch (IOException e1) {
            throw new BusException("上传失败");
        }
        return ymd + "/" + newFileName;
    }

}
