package com.zebone.nhis.ma.pub.arch.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.annotation.Resource;
import javax.xml.bind.JAXBException;

import org.dom4j.DocumentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.zebone.nhis.common.module.arch.PvArchDoc;
import com.zebone.nhis.common.module.arch.PvArchive;
import com.zebone.nhis.common.module.base.ou.BdOuUser;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.ArchUtils;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.common.support.FTPUtil;
import com.zebone.nhis.common.support.XmlUtil;
import com.zebone.nhis.ma.pub.arch.utils.FileArchUtils;
import com.zebone.nhis.ma.pub.arch.vo.ArchCancelParamVo;
import com.zebone.nhis.ma.pub.arch.vo.ArchDownParam;
import com.zebone.nhis.ma.pub.arch.vo.ArchDownResultVo;
import com.zebone.nhis.ma.pub.arch.vo.ArchFilelParamVo;
import com.zebone.nhis.ma.pub.arch.vo.ArchInputParam;
import com.zebone.nhis.ma.pub.arch.vo.ArchPvEncounterVo;
import com.zebone.nhis.ma.pub.arch.vo.ArchQryParamVo;
import com.zebone.nhis.ma.pub.arch.vo.ArchQueryResultVo;
import com.zebone.nhis.ma.pub.arch.vo.ArchResponse;
import com.zebone.nhis.ma.pub.arch.vo.ArchResultVo;
import com.zebone.nhis.ma.pub.arch.vo.ArchStatusRespVo;
import com.zebone.nhis.ma.pub.arch.vo.ArchUploadBody;
import com.zebone.nhis.ma.pub.arch.vo.ArchUploadContent;
import com.zebone.nhis.ma.pub.arch.vo.ArchUploadHead;
import com.zebone.nhis.ma.pub.arch.vo.ArchUploadResultVo;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.utils.JsonUtil;
@Service
public class FileArchHandler {
	@Resource
	private FileArchOuterService archService;
	
	@Resource
	private FileArchCollectService zsrmArchiveCollectService;
	
	private static Logger log = LoggerFactory.getLogger(FileArchHandler.class);
	
	private String receiveFilePath="";
	
	/**
	 * 文件上传
	 * @param param json
	 * @param type 1:Body 2:MultipartFile
	 * @return
	 * @throws JAXBException
	 * @throws IOException 
	 */
	public String uploadFile(String param,String type)  {
		ArchResultVo rtnError = null;
		ArchInputParam input = null;
		ArchUploadContent content = null;
		if(type==null) type="1";
		try{
			if(type.equals("2")) {
				content = JsonUtil.readValue(param, ArchUploadContent.class);
			}else{
				input=JsonUtil.readValue(param, ArchInputParam.class);
				if(input==null){
					rtnError = new ArchResultVo();
					rtnError.setResponse(ArchResponse.getErrorInstance("参数格式不正确,请检查!"));	
					return  JsonUtil.writeValueAsString(rtnError);				
				}
				content = input.getContent();
			}
			
			//校验数据格式
			if(content==null){
				rtnError = new ArchResultVo();
				rtnError.setResponse(ArchResponse.getErrorInstance("参数格式不正确,请检查!"));	
				return  JsonUtil.writeValueAsString(rtnError);				
			}
			if(content.getBody()==null){
				rtnError = new ArchResultVo();
				rtnError.setResponse(ArchResponse.getErrorInstance("报告文件缺失,请检查!"));	
				//archOuterService.insertErrorLog("报告文件缺失,请检查!", ArchUtils.getErrorXml(fileInfo));
				return  JsonUtil.writeValueAsString(rtnError);
			}
			if(content.getHead()==null){
				rtnError = new ArchResultVo();
				rtnError.setResponse(ArchResponse.getErrorInstance("描述文件缺失,请检查!"));	
				return  JsonUtil.writeValueAsString(rtnError);
			}
			if(content.getBody().getFilecontent()==null){
				rtnError = new ArchResultVo();
				rtnError.setResponse(ArchResponse.getErrorInstance("文件参数格式缺失,请检查!"));	
				//archOuterService.insertErrorLog("报告文件参数缺失,请检查!", ArchUtils.getErrorXml(fileInfo));
				return  JsonUtil.writeValueAsString(rtnError);
			}
			if(!checkObjFieldIsNull(content.getHead())){			
				rtnError = new ArchResultVo();
				rtnError.setResponse(ArchResponse.getErrorInstance("描述文件参数缺失,请检查!"));	
				//archOuterService.insertErrorLog("描述文件参数缺失,请检查!", ArchUtils.getErrorXml(fileInfo));
				return  JsonUtil.writeValueAsString(rtnError);
			}
			//返回结果说明
			String rslt = "";
			ArchUploadResultVo result = new ArchUploadResultVo();
			
			PvArchive record = null;
			record = zsrmArchiveCollectService.getPvArchiveByPvEn(content.getHead());
			if(record==null){
				rtnError = new ArchResultVo();
				rtnError.setResponse(ArchResponse.getErrorInstance("未找到归档记录！"));
				return  JsonUtil.writeValueAsString(rtnError);
			}
			
			//上传校验，归档不可重新上传
			String chkIpPvStatus = ChkIpPvArch(content.getHead(),record.getPkArchive());
			if(!CommonUtils.isEmptyString(chkIpPvStatus)){
				rtnError = new ArchResultVo();
				rtnError.setResponse(ArchResponse.getErrorInstance(chkIpPvStatus));
				return  JsonUtil.writeValueAsString(rtnError);
			}
			
			//上传文件
			long start = System.currentTimeMillis();
			uploadFileByPvtype(content,start);

			//增加pv_arch_doc记录
			PvArchDoc doc = zsrmArchiveCollectService.getArchDoc(content.getHead(), record);
			
			//建立数据库索引，更新表pv_arch_doc
			BdOuUser user=new BdOuUser();
			user.setCreateTime(new Date());
			user.setNameUser(content.getHead().getUser());
			zsrmArchiveCollectService.updateArchDoc(0, user, doc, receiveFilePath,0,null);	
			
			//6.返回处理结果
			ArchResponse  res = ArchResponse.getInstance();
			result.setResponse(res);
			rslt = JsonUtil.writeValueAsString(result);

			return  rslt;	
			
		}catch(com.zebone.platform.modules.exception.BusException e){
			rtnError = new ArchResultVo();
			rtnError.setResponse(ArchResponse.getErrorInstance(e.getMessage()));
			archService.insertErrorLog("错误原因：传入json格式有误"+e.getMessage(), null);
			return  JsonUtil.writeValueAsString(rtnError);
		}catch(Exception e){
			rtnError = new ArchResultVo();
			rtnError.setResponse(ArchResponse.getErrorInstance(e.getMessage()));
			archService.insertErrorLog("错误原因："+e.getMessage(), param);
			return  JsonUtil.writeValueAsString(rtnError);
		}
	}
	
	private void uploadFileByPvtype(ArchUploadContent fileInfo,long start) throws IOException,
	UnsupportedEncodingException {
//		try {
//			Thread.sleep(10);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		fileInfo = ArchUtils.handlePaticodeNew(fileInfo);
		
		//2.创建一系列文件夹
		StringBuffer sbDir = new StringBuffer();
		String archPath = ApplicationUtils.getPropertyValue("arch.file.path", "");
		String archDir = ApplicationUtils.getPropertyValue("arch.file.dir", "");
		String separator = ApplicationUtils.getPropertyValue("arch.separator", "");
		
		sbDir.append(archPath);//"/data/archive"
		sbDir.append(archDir);//"/NHISArchive/ArchUtils.BASE_DIR
		sbDir.append(separator);//ArchUtils.SEPARATOR
		String sPath = sbDir.toString();
		sbDir.append(ArchUtils.get1stDir(fileInfo.getHead().getPaticode()));
		sbDir.append(separator).append(fileInfo.getHead().getPaticode()).
		append(separator).append(fileInfo.getHead().getVisitcode());
		//创建文件夹
		String dirStr=sbDir.toString().replaceAll("\\\\", "/");
		File dir = new File(dirStr);
		//File dir = new File(sbDir.toString());
        if (!dir.exists()) {// 判断目录是否存在     
            dir.mkdirs();   
        }
		//3.上传病历文件（可能是pdf，也有可能是图像）
	    String fileStr = fileInfo.getBody().getFilecontent();
	    byte[] fileBytes = Base64.getDecoder().decode(fileStr);
	      /*for(int i=0;i<fileBytes.length;++i)
          {
              if(fileBytes[i]<0)
              {//调整异常数据
            	  fileBytes[i]+=256;
              }
          }*/
	      //3.1 文件名称处理
	      receiveFilePath=sbDir.toString().replace(sPath, "");

	      String fileName = fileInfo.getHead().getDocname();
	      String finalName = null;
	      if(fileName.contains(".")){
			/*String fileStr = fileInfo.getUploadBody().getFilecontent();
			byte[] fileBytes = ArchUtils.decoder.decodeBuffer(fileStr);
			//3.1 文件名称处理
			String fileName = fileInfo.getUploadHead().getDocname();
		    String finalName = null;*/
		    if(fileName.contains(".")){
			   finalName =fileName.substring(0,fileName.lastIndexOf("."))+ArchUtils.FILESUFFIX;
		    }
		    if(fileName.indexOf("pdf")<0 && fileName.indexOf("PDF")<0){
		    	fileBytes = FTPUtil.imag2PDF(fileBytes, finalName);
		    }
		     
		    StringBuffer sbFile = new StringBuffer(sbDir);
		    String filePath = sbFile.append(separator).append(finalName).toString();
			  
		    //4.判断是否存在CA文件	
		    String caFileStr = fileInfo.getBody().getCafilecontent();
		    String caFileName = fileInfo.getBody().getCafilename();
		    if(!CommonUtils.isEmptyString(caFileStr) && !CommonUtils.isEmptyString(caFileName)){
			   StringBuffer sbCa = new StringBuffer(sbDir);
			   byte[] caBytes = Base64.getDecoder().decode(caFileStr);
			   String caPath =  sbCa.append(separator).append(caFileName).toString();
			   String caFilePath = sbFile.append(separator).append(caFileName).toString();
			   
			   writeFile(caBytes, caFilePath);
				
			   //FTPUtil.uploadFileNew(caBytes, new String(caPath.getBytes("GBK"),"iso-8859-1"), null);
		    } 
	      
		    //5.改写并上传描述文件
		    fileInfo.getHead().setDocname(finalName);
		    fileInfo.getHead().setPath(receiveFilePath);//sbDir.toString()
		    fileInfo.getHead().setStatus("0");
		    fileInfo.getBody().setFilecontent(null);
		    fileInfo.getBody().setCafilecontent(null);
		    String xmlDesc = JsonUtil.writeValueAsString(fileInfo);
		    StringBuffer sbDesc = new  StringBuffer(sbDir);
		    //sbFile.append(separator).append(finalName).toString();
		    String descFileName=fileName.substring(0,fileName.lastIndexOf("."))+".json";
		    //String descPath = sbDesc.append(ArchUtils.SEPARATOR).append(finalName.contains(".")?finalName.substring(0,finalName.lastIndexOf(".")):finalName).append(ArchUtils.XMLSUFFIX).toString();
		    //FTPUtil.uploadFileNew(xmlDesc.toString().getBytes(), new String(descPath.getBytes("GBK"),"iso-8859-1"), null);
		    String descFilePath = sbDesc.append(separator).append(descFileName).toString();
		    //写入描述文件
		    writeFile(xmlDesc.toString().getBytes(), descFilePath);
		    
		    //写入报告文件
		    writeFile(fileBytes, filePath);

		    //6.写日志表TODO
		    fileInfo.getHead().setDocname(finalName);
		    long end = System.currentTimeMillis();
		    String version = archService.insertLog(fileInfo,end-start);
	      }
	}

	private void writeFile(byte[] fileBytes, String filePath)
			throws FileNotFoundException, UnsupportedEncodingException, IOException {
		byte[] data = filePath.getBytes("UTF-8");
		OutputStream out = new FileOutputStream(new File(new String(data,"UTF-8"))); //iso-8859-1
		out.write(fileBytes);
		out.flush();
		out.close();
	}

	private void uploadFileByPvtypeBak(ArchUploadContent fileInfo,long start) throws IOException,
	UnsupportedEncodingException {
		try {
			Thread.sleep(10);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//2018-02-04 增加处理博爱医院门诊，急诊，住院患者ID
		fileInfo = FileArchUtils.handlePaticode(fileInfo);
		//2.创建一系列文件夹
		StringBuffer sbDir = new StringBuffer();
		sbDir.append("/data/archive");
		//sbDir.append("D://");
		sbDir.append(ArchUtils.BASE_DIR);//"/NHISArchive
		sbDir.append(ArchUtils.SEPARATOR);
		sbDir.append(ArchUtils.get1stDir(fileInfo.getHead().getPaticode()));
		sbDir.append(ArchUtils.SEPARATOR).append(fileInfo.getHead().getPaticode()).
		append(ArchUtils.SEPARATOR).append(fileInfo.getHead().getVisitcode());
		//创建文件夹
		File dir = new File(sbDir.toString().replaceAll("\\\\", "/"));
		//File dir = new File(sbDir.toString());
        if (!dir.exists()) {// 判断目录是否存在     
            dir.mkdirs();   
        }
		//3.上传病历文件（可能是pdf，也有可能是图像）
	    String fileStr = fileInfo.getBody().getFilecontent();
	    byte[] fileBytes = Base64.getDecoder().decode(fileStr);
	      /*for(int i=0;i<fileBytes.length;++i)
          {
              if(fileBytes[i]<0)
              {//调整异常数据
            	  fileBytes[i]+=256;
              }
          }*/
	      //3.1 文件名称处理
	      receiveFilePath=sbDir.toString();

	      String fileName = fileInfo.getHead().getDocname();
	      String finalName = null;
	      if(fileName.contains(".")){
			/*String fileStr = fileInfo.getUploadBody().getFilecontent();
			byte[] fileBytes = ArchUtils.decoder.decodeBuffer(fileStr);
			//3.1 文件名称处理
			String fileName = fileInfo.getUploadHead().getDocname();
		    String finalName = null;*/
		    if(fileName.contains(".")){
			   finalName =fileName.substring(0,fileName.lastIndexOf("."))+ArchUtils.FILESUFFIX;
		    }
		    if(fileName.indexOf("pdf")<0 && fileName.indexOf("PDF")<0){
		    	fileBytes = FTPUtil.imag2PDF(fileBytes, finalName);
		    }
		     
		    StringBuffer sbFile = new StringBuffer(sbDir);
		    String filePath = sbFile.append(ArchUtils.SEPARATOR).append(finalName).toString();
			  
		    //4.判断是否存在CA文件	
		    String caFileStr = fileInfo.getBody().getCafilecontent();
		    String caFileName = fileInfo.getBody().getCafilename();
		    if(!CommonUtils.isEmptyString(caFileStr) && !CommonUtils.isEmptyString(caFileName)){
			   StringBuffer sbCa = new StringBuffer(sbDir);
			   byte[] caBytes = Base64.getDecoder().decode(caFileStr);
			   String caPath =  sbCa.append(ArchUtils.SEPARATOR).append(caFileName).toString();
			   FTPUtil.uploadFileNew(caBytes, new String(caPath.getBytes("GBK"),"iso-8859-1"), null);
		    } 
	      
		    //5.改写并上传描述文件
		    fileInfo.getHead().setDocname(finalName);
		    fileInfo.getHead().setPath(sbDir.toString());
		    fileInfo.getHead().setStatus("0");
		    fileInfo.getBody().setFilecontent(null);
		    fileInfo.getBody().setCafilecontent(null);
	      
		    String xmlDesc;
		    xmlDesc = XmlUtil.beanToXml(fileInfo, ArchUploadContent.class);
		    StringBuffer sbDesc = new  StringBuffer(sbDir);
		    String descPath = sbDesc.append(ArchUtils.SEPARATOR).append(finalName.contains(".")?finalName.substring(0,finalName.lastIndexOf(".")):finalName).append(ArchUtils.XMLSUFFIX).toString();
		    writeFile(fileBytes, filePath);

		    //6.写日志表TODO
		    fileInfo.getHead().setDocname(finalName);
		    long end = System.currentTimeMillis();
		    String version = archService.insertLog(fileInfo,end-start);
	      }
	}
	
	private  boolean checkBodyFieldIsNull(ArchUploadBody obj)  {
		if(obj==null){  
			return false;
		}else{
			return !CommonUtils.isEmptyString(obj.getFilecontent());
		}
	}
	
	private  boolean checkObjFieldIsNull(ArchUploadHead head)  {
		if(head!=null){
			if("3".equals(head.getVisittype())){
				boolean flag = !CommonUtils.isEmptyString(head.getPaticode())&&!CommonUtils.isEmptyString(head.getTimes())
						&&!CommonUtils.isEmptyString(head.getVisitcode())&&
						!CommonUtils.isEmptyString(head.getVisittype())&&
						!CommonUtils.isEmptyString(head.getDocname());  
				return flag;
			}else{
				boolean flag = !CommonUtils.isEmptyString(head.getPaticode())
						&&!CommonUtils.isEmptyString(head.getVisitcode())&&
						!CommonUtils.isEmptyString(head.getVisittype())&&
						!CommonUtils.isEmptyString(head.getDocname());  
				return flag;
			}
		}else{
			return false;
		}
	}

//	class MyTimeoutTask implements Callable<Boolean> {  
//		private ArchUploadContent fileInfo;
//		private String pvtype;
//		private long start;
//		
//		public MyTimeoutTask(ArchUploadContent fileInfo,String pvtype,long start){
//			this.fileInfo = fileInfo;
//			this.pvtype = pvtype;
//			this.start = start;
//		}
//	    @Override  
//	    public Boolean call() throws Exception {  
//	         uploadFileByPvtype(this.fileInfo, this.start); 
//	        return true;  
//	    }  
//	}
	
	/**
	 * 添加上传校验
	 * @param pk_archive
	 * @param name_doc
	 * @return
	 */
	public String ChkIpPvArch(ArchUploadHead headInfo,String pkArchive){
		String rs = "";
		try {
			if(CommonUtils.isEmptyString(headInfo.getDocname()))
				return "上传时，文档名称为空！";
			List<PvArchDoc> pas = DataBaseHelper.queryForList("select doc.isarch,doc.pk_doc "
					+ " from pv_archive arch "
					+ "inner join pv_arch_doc doc on arch.pk_archive=doc.pk_archive "
					+ "where arch.pk_archive=? and doc.name_doc=?"
					,PvArchDoc.class, pkArchive,headInfo.getDocname().split("\\.")[0]+".pdf");
			if(pas!=null && pas.size()>0){
				PvArchDoc pa = pas.get(0);
				if("1".equals(pa.getIsArch())){
					return pa.getPkDoc()+"-"+headInfo.getDocname()+":该已归档，请先提交解除归档申请，再重新上传！";
				}
			}
		} catch (Exception e) {
			return e.getMessage();
		}
		return rs;
	}

	/**
	 * 根据住院次数、住院号，定位患者本次就诊次数
	 * @param codeIp
	 * @param ipTimes
	 * @return
	 */
	public ArchPvEncounterVo queryPkPvByCodePv(String codeIp, String ipTimes) {
		ArchPvEncounterVo pv = null;
		String sql = "select pi.code_pi,pi.code_ip, pv.* from PV_ENCOUNTER pv inner join pv_ip ip on pv.pk_pv = ip.pk_pv inner join pi_master pi on pv.pk_pi = pi.pk_pi and pi.DEL_FLAG='0' where pi.CODE_IP=? and ip.IP_TIMES=? and ip.DEL_FLAG='0' and pv.EU_STATUS!='9' ";
	    pv = DataBaseHelper.queryForBean(sql, ArchPvEncounterVo.class, codeIp,ipTimes);
		return pv;
	};
	
	/**
	 * 文件下载
	 * @param content
	 * @return
	 * @throws JAXBException
	 * @throws IOException 
	 */
	public String downloadFile(String content) throws IOException {
		log.info("downloadFile:"+content);
		ArchDownParam param = JsonUtil.readValue(content, ArchDownParam.class);
		String rslt = "";
		ArchDownResultVo rtn = archService.downloadFileZSRM(param);
	
	    rslt = JsonUtil.writeValueAsString(rtn);

	    //writeFile("c:\\logs\\aaa.txt",rslt);
	    
		return  rslt;				
	}
	
	
	/**

	*

	* @param path

	* path:保存日志文件路径

	* @param content

	* content：日志内容

	*/

	public static void writeFile(String path, String content) {
	File writefile;

	try {
		// 通过这个对象来判断是否向文本文件中追加内容
		// boolean addStr = append;
		writefile = new File(path);
		// 如果文本文件不存在则创建它
		if (!writefile.exists()) {
		writefile.createNewFile();
	
		writefile = new File(path); // 重新实例化
	
		}
	
		FileOutputStream fw = new FileOutputStream(writefile,true);
	
		Writer out = new OutputStreamWriter(fw, "utf-8");
	
		out.write(content);
	
		String newline = System.getProperty("line.separator");
	
		//写入换行
	
		out.write(newline);
	
		out.close();
	
		fw.flush();
	
		fw.close();
	
		} catch (Exception ex) {
		System.out.println(ex.getMessage());
	
		}

	}

	// 获取当前时间

	public static String getCurrentYYYYMMDDHHMMSS() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
	
		formatter.setTimeZone(TimeZone.getTimeZone("GMT+8"));
	
		Date currTime = new Date();
	
		String thisTime = new String(formatter.format(currTime));
	
		return thisTime;

	}

		
	/**
	 * 文件查询
	 * @param param
	 * @return
	 * @throws JAXBException
	 * @throws IOException 
	 * @throws DocumentException 
	 */
	public String qryFile(String param) throws IOException, DocumentException {
		ArchQryParamVo qryParam = JsonUtil.readValue(param, ArchQryParamVo.class);	
		String rslt = "";
		ArchQueryResultVo rtn = archService.queryFile(qryParam);
	
		rslt = JsonUtil.writeValueAsString(rtn);
		return  rslt;				
	}
	
	
	/**
	 * 文件查询
	 * @param content
	 * @return
	 * @throws JAXBException
	 * @throws IOException 
	 * @throws DocumentException 
	 */
	public String qryFileBak(String content) throws IOException, DocumentException {
		//content = "<request><body>"+content+"</body></request>";
		content = "{'employees': {"+content+"}";
		ArchQryParamVo qryParam = JsonUtil.readValue(content, ArchQryParamVo.class);	
		String rslt = "";
		ArchQueryResultVo rtn = archService.queryFile(qryParam);
	
		rslt = XmlUtil.beanToXml(rtn, rtn.getClass());		
		return  rslt;				
	}
		
	/**
	 * 请求解除归档
	 * @param content
	 * @return
	 * @throws JAXBException
	 * @throws IOException 
	 */
	public String cancelArch(String content) throws IOException {
		String rslt = "";
		ArchCancelParamVo param = JsonUtil.readValue(content, ArchCancelParamVo.class);
		ArchUploadResultVo rtn = archService.requestCancelArchZSRM(param);
	
		rslt = JsonUtil.writeValueAsString(rtn);		
		return  rslt;				
	}
		
	/**
	 * 查询文档归档状态
	 * @param content
	 * @return
	 */
	public String queryFileStatus(String content) {
		ArchFilelParamVo param = JsonUtil.readValue(content, ArchFilelParamVo.class);
		String rslt = "";
		ArchStatusRespVo rtn = archService.queryFileStatus(param);
	
	    rslt = JsonUtil.writeValueAsString(rtn);		
		return  rslt;
	}
	
	

    
}
