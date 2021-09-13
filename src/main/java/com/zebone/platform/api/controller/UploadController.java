package com.zebone.platform.api.controller;

import com.zebone.platform.common.util.UploadUtils;
import org.apache.commons.io.FileUtils;
import org.joda.time.DateTime;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.RandomAccessFile;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/static")
public class UploadController {
    
    @RequestMapping(value = "/uploadPage")
    public String chunkUpload() {
        return "upload";
    }

    /**
     * 大文件分片上传
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/chunkupload", method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> chunkUpload(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String,Object> resultMap = new HashMap<>();
        resultMap.put("status", 0);
        resultMap.put("desc", "成功");
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        // 总分片数量
        int chunks = Integer.parseInt(multipartRequest.getParameter("chunks"));
        // 当前为第几块分片
        int chunk = Integer.parseInt(multipartRequest.getParameter("chunk"));
        // 上传文件的md5值
        String md5 = multipartRequest.getParameter("id");
        // 当前分片大小
        //long chunkSize = Long.parseLong(multipartRequest.getParameter("chunkSize"));
        long chunkSize = 15 * 1024;
        MultipartFile file = multipartRequest.getFile("file");
        // 上传文件的原始文件名称
        String fileName = file.getOriginalFilename();

        // 上传保存路径
        String uploadPath = UploadUtils.getUploadPath();
        String ymd = DateTime.now().toString("yyyyMM");
        uploadPath += ymd + File.separator;//yyyyMM+分隔符
        // 创建文件夹
        File dir = new File(uploadPath);
        if (!dir.exists())
            dir.mkdirs();

        String newFileName = UploadUtils.getUploadResName(md5, fileName);
        
        File uploadFile = new File(uploadPath, newFileName);
        if (uploadFile.exists()) { // 文件已上传过直接返回资源url
            String resUrl = UploadUtils.getResUrl(ymd+"/"+newFileName);
            resultMap.put("data", resUrl);
            return resultMap;
        }
        
        File confFile = new File(uploadPath, newFileName + ".conf");
        File tmpFile = new File(uploadPath, newFileName + ".tmp");

        RandomAccessFile accessTmpFile = new RandomAccessFile(tmpFile, "rw");
        RandomAccessFile accessConfFile = new RandomAccessFile(confFile, "rw");

        long offset = chunkSize * chunk;
        // 定位到该分片的偏移量
        accessTmpFile.seek(offset);
        // 写入该分片数据
        accessTmpFile.write(file.getBytes());

        // 把该分段标记为 true 表示完成
        accessConfFile.setLength(chunks);
        accessConfFile.seek(chunk);
        accessConfFile.write(Byte.MAX_VALUE);

        // completeList 检查是否全部完成,如果数组里是否全部都是(全部分片都成功上传)
        byte[] completeList = FileUtils.readFileToByteArray(confFile);
        byte isComplete = Byte.MAX_VALUE;
        for (int i = 0; i < completeList.length && isComplete == Byte.MAX_VALUE; i++) {
            // 与运算, 如果有部分没有完成则 isComplete 不是 Byte.MAX_VALUE
            isComplete = (byte) (isComplete & completeList[i]);
        }

        accessTmpFile.close();
        accessConfFile.close();
        if (isComplete == Byte.MAX_VALUE) {
            confFile.delete();
            tmpFile.renameTo(uploadFile);
            String resUrl = UploadUtils.getResUrl(ymd+"/"+newFileName);
            resultMap.put("data", resUrl);
            return resultMap;
        } else {
            resultMap.put("status", -1);
            resultMap.put("desc", "上传未结束");
            return resultMap;
        }
    }
    
    /**
     * 单文件上传
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> upload(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String,Object> resultMap = new HashMap<>();
        resultMap.put("status", 0);
        resultMap.put("desc", "成功");
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
       
        // 上传文件的md5值
        String md5 = multipartRequest.getParameter("id");
       
        MultipartFile file = multipartRequest.getFile("file");
        // 上传文件的原始文件名称
        String fileName = file.getOriginalFilename();

        // 上传保存路径
        String uploadPath = UploadUtils.getUploadPath();
        String ymd = DateTime.now().toString("yyyyMM");
        uploadPath += ymd + File.separator;//yyyyMM+分隔符
        // 创建文件夹
        File dir = new File(uploadPath);
        if (!dir.exists())
            dir.mkdirs();

        String newFileName = UploadUtils.getUploadResName(md5, fileName);
        
        File uploadFile = new File(uploadPath, newFileName);
        if (uploadFile.exists()) { // 文件已上传过直接返回资源url
            String resUrl = UploadUtils.getResUrl(ymd+"/"+newFileName);
            resultMap.put("data", resUrl);
            return resultMap;
        }
        
        File confFile = new File(uploadPath, newFileName + ".conf");
        File tmpFile = new File(uploadPath, newFileName + ".tmp");

        RandomAccessFile accessTmpFile = new RandomAccessFile(tmpFile, "rw");
        RandomAccessFile accessConfFile = new RandomAccessFile(confFile, "rw");

       
        // 写入该分片数据
        accessConfFile.seek(0);
        accessTmpFile.write(file.getBytes());

        // 把该分段标记为 true 表示完成
        accessConfFile.setLength(1);
        accessConfFile.seek(0);
        accessConfFile.write(Byte.MAX_VALUE);

        // completeList 检查是否全部完成,如果数组里是否全部都是(全部分片都成功上传)
        byte[] completeList = FileUtils.readFileToByteArray(confFile);
        byte isComplete = Byte.MAX_VALUE;
        for (int i = 0; i < completeList.length && isComplete == Byte.MAX_VALUE; i++) {
            // 与运算, 如果有部分没有完成则 isComplete 不是 Byte.MAX_VALUE
            isComplete = (byte) (isComplete & completeList[i]);
        }

        accessTmpFile.close();
        accessConfFile.close();
        if (isComplete == Byte.MAX_VALUE) {
            confFile.delete();
            tmpFile.renameTo(uploadFile);
            String resUrl = UploadUtils.getResUrl(ymd+"/"+newFileName);
            resultMap.put("data", resUrl);
            return resultMap;
        } else {
            resultMap.put("status", -1);
            resultMap.put("desc", "上传未结束");
            return resultMap;
        }
    }
}
