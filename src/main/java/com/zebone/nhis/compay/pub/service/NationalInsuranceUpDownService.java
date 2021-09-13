package com.zebone.nhis.compay.pub.service;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.common.support.HttpClientUtil;
import com.zebone.nhis.compay.pub.support.NationalConfig;
import com.zebone.nhis.compay.pub.vo.HisInsuResVo;
import com.zebone.nhis.compay.pub.vo.InsDownFileParamVo;
import com.zebone.nhis.compay.pub.vo.InsFileResponse;
import com.zebone.nhis.compay.pub.vo.NationalHeadReqVo;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

@Service
public class NationalInsuranceUpDownService {
    private static final Logger logger = LoggerFactory.getLogger("nhis.ZsrmQGLog");

    @Resource
    private NationalConfig nationalConfig;

    @Resource
    private NationalInsuranceService insuranceService;

    public InsFileResponse uploadFile(String filePrefix, List<Object[]> paramList){
        if(CollectionUtils.isEmpty(paramList)){
            throw new BusException("未传入数据");
        }
        File zip = doZip(filePrefix, paramList);
        try {
            Map<String, Object> insurMap = new HashMap<>();
            insurMap.put("in", Files.readAllBytes(Paths.get(zip.getPath())));
            insurMap.put("filename",zip.getName());
            insurMap.put("fixmedins_code",insuranceService.getFixmedinsCode());
            Map<String, Object> inputParam = Maps.newHashMap();
            inputParam.put("fsUploadIn",JSON.toJSON(insurMap));
            HisInsuResVo insuResVo = insuranceService.sendHttpPost("9101",inputParam);
            return JsonUtil.readValue((String) insuResVo.getResVo().getOutput(), InsFileResponse.class);
        } catch (Exception e) {
            logger.error("上传异常：",e);
            throw new BusException(e.getMessage());
        } finally {
            FileUtils.deleteQuietly(zip);
        }
    }

    /**
     * 压缩数据
     * @param filePrefix
     * @param paramList
     */
    private File doZip(String filePrefix,List<Object[]> paramList){
        ZipOutputStream zos = null;
        File zipFile = null;
        try {
            //TODO 固定目录，没有考虑DFS？ 每次下载和读取必须一起先
        	String fileName = filePrefix+ DateUtils.formatDate(new Date(),"yyyyMMddHHmmss")+"_"+ RandomStringUtils.randomNumeric(5);
            
            zipFile = new File(insuranceService.getUploadFilePath()+File.separator+fileName+".zip");
            if(!zipFile.getParentFile().exists()){
                zipFile.getParentFile().mkdir();
            }
            zipFile.deleteOnExit();
            zipFile.createNewFile();

            zos = new ZipOutputStream(new FileOutputStream(zipFile));
            zos.putNextEntry(new ZipEntry(fileName+".txt"));
            for (int i = 0; i < paramList.size(); i++) {
                Object[] objects = paramList.get(i);
                for (Object object : objects) {
                    //里面只可能存储普通对象，所以直接转为String，如果是引用对象入数组，自定义类等这样就是不对的
                    zos.write(String.valueOf(object).getBytes("utf-8"));
                    zos.write("\t".getBytes());
                }
                if((i+1)!=paramList.size())
                    zos.write("\r\n".getBytes());
            }
            zos.closeEntry();
            zos.flush();
        } catch (Exception e){
            logger.error("压缩异常：",e);
            throw new BusException(e.getMessage());
        } finally {
            IOUtils.closeQuietly(zos);
        }
        return zipFile;
    }


    /**
     * 下载并读取内容
     * @param prefix
     * @param downFileParamVo
     * @return
     */
    public List<Object[]> downAndGet(String prefix, InsDownFileParamVo downFileParamVo){
        File file = null;
        try {
            file = downFile(prefix, downFileParamVo);
            return getZipContent(file);
        } finally {
            if(file !=null)
                file.delete();
        }
    }

    public File downFile(String prefix, InsDownFileParamVo downFileParamVo) {
        NationalHeadReqVo reqVo = nationalConfig.buildReq("9102");
        reqVo.setFixmedinsCode(insuranceService.getFixmedinsCode());
        Map<String,Object> mapFile = Maps.newHashMap();
        mapFile.put("file_qury_no", downFileParamVo.getFileNo());
        mapFile.put("filename", downFileParamVo.getFileName());
        mapFile.put("fixmedins_code", "plc");
        Map<String,Object> mapDown = Maps.newHashMap();
        mapDown.put("fsDownloadIn",mapFile);
        reqVo.setInput(mapDown);
        String jsonStr = JsonUtil.writeValueAsString(reqVo);
        jsonStr = insuranceService.humpToLine(jsonStr);
        Map<String, String> map = nationalConfig.getHeaderElement();
        File file;
        try {
            file = File.createTempFile(prefix,".zip");
        } catch (IOException e) {
            throw new BusException("创建文件失败");
        }
        System.out.println("9102下载文件入参" + jsonStr);
        HttpClientUtil.sendHttpDownload(nationalConfig.getHsaUrl()+"9102", jsonStr,map,file);
        return file;
    }

    public List<Object[]> getZipContent(File file){
        List<Object[]> dataList = Lists.newArrayList();
        ZipInputStream zipInputStream = null;
        ZipFile zip = null;
        BufferedReader bufferedReader = null;
        try {
            zipInputStream = new ZipInputStream(new FileInputStream(file));
            ZipEntry ze;
            while ((ze = zipInputStream.getNextEntry()) != null) {
                if(!ze.isDirectory()){
                	bufferedReader = new BufferedReader(new InputStreamReader(zipInputStream, StandardCharsets.UTF_8));
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        StringTokenizer stk = new StringTokenizer(line,"\t");
                        Object[] objects = new Object[stk.countTokens()];
                        int i=0;
                        while(stk.hasMoreTokens()){
                            String str = stk.nextToken();
                            objects[i++] = "null".equals(str)?null:str;
                        }
                        dataList.add(objects);
                    }
                }
            }
        } catch (Exception e) {
            logger.error("读取文件异常:",e);
            throw new BusException("读取文件异常"+e.getMessage());
        } finally {
        	if(bufferedReader != null){
        		try {
					bufferedReader.close();
				} catch (IOException e) {
					e.printStackTrace();
					logger.error("关闭流异常:",e);
		            throw new BusException("关闭流异常:"+e.getMessage());
				}
        	}
            IOUtils.closeQuietly(zipInputStream);
            IOUtils.closeQuietly(zip);
        }
        return dataList;
    }

}
