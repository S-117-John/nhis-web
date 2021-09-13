package com.zebone.nhis.webservice.zhongshan.service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.Callable;

import javax.annotation.Resource;
import javax.xml.bind.JAXBException;

import org.dom4j.DocumentException;
import org.springframework.stereotype.Service;

import com.zebone.nhis.common.arch.service.ArchOuterService;
import com.zebone.nhis.common.arch.service.ArchiveCollectService;
import com.zebone.nhis.common.arch.vo.ArchCancelParam;
import com.zebone.nhis.common.arch.vo.ArchDownloadParam;
import com.zebone.nhis.common.arch.vo.ArchQryParam;
import com.zebone.nhis.common.arch.vo.ArchQueryResult;
import com.zebone.nhis.common.arch.vo.ArchResult;
import com.zebone.nhis.common.arch.vo.ArchResultUp;
import com.zebone.nhis.common.arch.vo.ArchStatusParam;
import com.zebone.nhis.common.arch.vo.ArchUploadParam;
import com.zebone.nhis.common.arch.vo.Response;
import com.zebone.nhis.common.arch.vo.UploadBody;
import com.zebone.nhis.common.arch.vo.UploadHead;
import com.zebone.nhis.common.module.arch.PvArchDoc;
import com.zebone.nhis.common.module.arch.PvArchErrorlog;
import com.zebone.nhis.common.module.arch.PvArchLog;
import com.zebone.nhis.common.module.arch.PvArchive;
import com.zebone.nhis.common.module.base.ou.BdOuUser;
import com.zebone.nhis.common.module.pv.PvEncounter;
import com.zebone.nhis.common.support.ArchFileUtils;
import com.zebone.nhis.common.support.ArchUtils;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.common.support.FTPUtil;
import com.zebone.nhis.common.support.TimeTaskUtils;
import com.zebone.nhis.common.support.XmlUtil;
import com.zebone.nhis.webservice.zhongshan.vo.PvEncounterVo;
import com.zebone.platform.modules.dao.DataBaseHelper;

@Service
public class ArchOuterHandler {
	
	@Resource
	private ArchOuterService archOuterService;
	
	@Resource
	private ArchiveCollectService archiveCollectService;
	
	private String receiveFilePath="";
	/**
	 * 文件上传
	 * @param content
	 * @return
	 * @throws JAXBException
	 * @throws IOException 
	 */
	public String uploadFile(String content)  {
		ArchUploadParam fileInfo = null;
		ArchResultUp rtnError = null;
		try{
			fileInfo = (ArchUploadParam) XmlUtil.XmlToBean(format(content), ArchUploadParam.class);
			if(fileInfo==null){
				rtnError = new ArchResultUp();
				rtnError.setResponse(Response.getErrorInstance("参数格式非法,请检查!"));	
				archOuterService.insertErrorLog("参数格式非法,请检查!", ArchUtils.getErrorXml(fileInfo));
				return  XmlUtil.beanToXml(rtnError, rtnError.getClass());				
			}
			if(fileInfo.getUploadBody()==null){
				rtnError = new ArchResultUp();
				rtnError.setResponse(Response.getErrorInstance("报告文件缺失,请检查!"));	
				archOuterService.insertErrorLog("报告文件缺失,请检查!", ArchUtils.getErrorXml(fileInfo));
				return  XmlUtil.beanToXml(rtnError, rtnError.getClass());	
			}
			if(fileInfo.getUploadHead()==null){
				rtnError = new ArchResultUp();
				rtnError.setResponse(Response.getErrorInstance("描述文件缺失,请检查!"));	
				archOuterService.insertErrorLog("描述文件缺失,请检查!", ArchUtils.getErrorXml(fileInfo));
				return  XmlUtil.beanToXml(rtnError, rtnError.getClass());	
			}
			
			//返回结果说明
			String rslt = "";
			
			ArchResultUp result = new ArchResultUp();
			Map<String,Object> resMap = new HashMap<String,Object>();
			
			//1.参数校验（源文件，visitcode，paticode，docname）;
			List<String> bodyFields = new ArrayList<String>();
			bodyFields.add("filecontent");
			List<String>  headFields = new ArrayList<String>(); 
			headFields.add("visitcode");
			headFields.add("paticode");
			headFields.add("docname");
			headFields.add("iptimes");
			
			if(!checkBodyFieldIsNull(fileInfo.getUploadBody())){
				rtnError = new ArchResultUp();
				rtnError.setResponse(Response.getErrorInstance("文件参数格式缺失,请检查!"));	
				archOuterService.insertErrorLog("报告文件参数缺失,请检查!", ArchUtils.getErrorXml(fileInfo));
				return  XmlUtil.beanToXml(rtnError, rtnError.getClass());
			}
			if(!checkObjFieldIsNull(fileInfo.getUploadHead())){			
				rtnError = new ArchResultUp();
				rtnError.setResponse(Response.getErrorInstance("描述文件参数缺失,请检查!"));	
				archOuterService.insertErrorLog("描述文件参数缺失,请检查!", ArchUtils.getErrorXml(fileInfo));
				return  XmlUtil.beanToXml(rtnError, rtnError.getClass());	
			}
			
			String fileOrgName = fileInfo.getUploadHead().getDocname();
			//增加 根据就诊类型确定上传逻辑 2018-02-04 22：33，
			String codePi = fileInfo.getUploadHead().getPaticode();
			String pvtype = fileInfo.getUploadHead().getVisittype();
			String codeIp = fileInfo.getUploadHead().getVisitcode();
			String ipTimes = fileInfo.getUploadHead().getIptimes();
			String sysName = fileInfo.getUploadHead().getSysname();
			String rid= fileInfo.getUploadHead().getRid();
			String rname=fileInfo.getUploadHead().getMemo();
			String rOperName=fileInfo.getUploadHead().getUser();
			PvEncounter pkPvByIpTimes=null;
			PvArchive record = null;
			PvEncounterVo pvInfo = null ;
			//2020-06-18 添加检查登记时调整为急诊的记录,将类型回执成住院流程
			if("2".equals(pvtype) || "3".equals(pvtype))
				pvInfo = queryPkPvByCodePv(codeIp,ipTimes);
			if("2".equals(pvtype) && "PACS".equals(sysName)){
				if(null != pvInfo){
					fileInfo.getUploadHead().setVisittype("3");	
					pvtype = "3";
				}
			}
			if("3".equals(pvtype))
			{ 
				//2018-05-03 增加住院时上传校验，归档状态为8-提交接触申请，9-不可重新上传
				String chkIpPvStatus = ChkIpPvArch(fileInfo.getUploadHead().getVisitcode(), fileInfo.getUploadHead().getIptimes());
				if(!CommonUtils.isEmptyString(chkIpPvStatus)){
					rtnError = new ArchResultUp();
					rtnError.setResponse(Response.getErrorInstance(chkIpPvStatus));
					return  XmlUtil.beanToXml(rtnError, rtnError.getClass());
				}
				
				//2020-04-09 针对pacs传入数据有误的情况，程序手工获取数据后重置入参
				if(null == pvInfo){
					rtnError = new ArchResultUp();
					rtnError.setResponse(Response.getErrorInstance("根据传入的住院号和住院次数查不出相应的就诊记录!"));	
					return  XmlUtil.beanToXml(rtnError, rtnError.getClass());	
				}
				if(!codePi.equals(pvInfo.getCodePi()))
					fileInfo.getUploadHead().setPaticode(pvInfo.getCodePi());
				
				//2020-04-09 根据传入的住院号和就住院次数查询pkpv生成归档记录
				pkPvByIpTimes =(PvEncounter) pvInfo;
				record = archiveCollectService.geneArchRecrdByPvEn(pkPvByIpTimes);
			}
			long start = System.currentTimeMillis();
			boolean flag = TimeTaskUtils.execute(new  MyTimeoutTask(fileInfo,pvtype,start), 10); 
			if(!flag){
				FTPUtil.disconnect();//上传超时，即刻关闭ftp连接
				rtnError = new ArchResultUp();
				archOuterService.insertErrorLog("上传文件超时！", ArchUtils.getErrorXml(fileInfo),fileInfo);
				if(ArchFileUtils.getReader(content, fileOrgName)){
					Response tempRes = Response.getInstance();
					//加此段代码是为了重归的删除验证使用
					tempRes.setErrormsg("99999");
					rtnError.setResponse(tempRes);
					return  XmlUtil.beanToXml(rtnError, rtnError.getClass());
				}
				rtnError.setResponse(Response.getErrorInstance("上传文件超时！"));	
				return  XmlUtil.beanToXml(rtnError, rtnError.getClass());
			}
			if("3".equals(pvtype)){ 
				if(record==null){
					rtnError = new ArchResultUp();
					rtnError.setResponse(Response.getErrorInstance("归档记录为空!"));	
					return  XmlUtil.beanToXml(rtnError, rtnError.getClass());	
				}
				BdOuUser user=new BdOuUser();
				user.setPkOrg(pkPvByIpTimes.getPkOrg());
				user.setPkEmp(null);
				user.setCreateTime(new Date());
				user.setNameUser(rOperName);
				PvArchDoc doc = archiveCollectService.genArchDoc(fileInfo.getUploadHead().getDocname(),fileInfo.getUploadHead().getDoctype(), pkPvByIpTimes.getPkOrg(), user, record,rid,rname);
				//建立数据库索引，更新表pv_arch_doc
				archiveCollectService.updateArchDoc(0, user, doc, receiveFilePath,0,null);	
			}
			//6.返回处理结果
			Response  res = Response.getInstance();
			result.setResponse(res);
			resMap.put("result", result);
			resMap.put("filePath", receiveFilePath);
			
			ArchResultUp rtn = (ArchResultUp) resMap.get("result");
			rslt = XmlUtil.beanToXml(rtn, rtn.getClass());
			
			//添加微信推送超时控制
			start = System.currentTimeMillis();
			boolean flag_push = TimeTaskUtils.execute(new  PushWXTimeoutTask(fileInfo,start),5);
			if(!flag_push)
				System.err.println("微信推送超时");

			return  rslt;		
		}catch(com.zebone.platform.modules.exception.BusException e){
			rtnError = new ArchResultUp();
			rtnError.setResponse(Response.getErrorInstance(e.getMessage()));
			archOuterService.insertErrorLog("错误原因：传入xml格式有误"+e.getMessage(), null);
			return  XmlUtil.beanToXml(rtnError, rtnError.getClass());
		}catch(Exception e){
			rtnError = new ArchResultUp();
			rtnError.setResponse(Response.getErrorInstance(e.getMessage()));
			archOuterService.insertErrorLog("错误原因："+e.getMessage(), content);
			return  XmlUtil.beanToXml(rtnError, rtnError.getClass());
		}
	}

	
	/**
	 * 上传文件方法
	 * @param fileInfo
	 * @param pvtype
	 * @param start
	 * @throws IOException
	 * @throws UnsupportedEncodingException
	 */
	private void uploadFileByPvtype(ArchUploadParam fileInfo,String pvtype,long start) throws IOException,
			UnsupportedEncodingException {
		try {
			Thread.sleep(10);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//2018-02-04 增加处理博爱医院门诊，急诊，住院患者ID
		fileInfo = ArchUtils.handlePaticode(fileInfo);
		//2.创建一系列文件夹
		StringBuffer sbDir = new StringBuffer();
		if("4".equals(pvtype)){
			sbDir.append(ArchUtils.BASE_DIR_OLD);
			sbDir.append(ArchUtils.SEPARATOR);
			sbDir.append(ArchUtils.get1stDirOld(fileInfo.getUploadHead().getPaticode()));
			
		}else{
			sbDir.append(ArchUtils.BASE_DIR);
			sbDir.append(ArchUtils.SEPARATOR);
			sbDir.append(ArchUtils.get1stDir(fileInfo.getUploadHead().getPaticode()));
		}
		sbDir.append(ArchUtils.SEPARATOR).append(fileInfo.getUploadHead().getPaticode()).
		append(ArchUtils.SEPARATOR).append(fileInfo.getUploadHead().getVisitcode());
		FTPUtil.connect();
		FTPUtil.ftpClient.enterLocalPassiveMode();
		FTPUtil.createDirectory(sbDir.toString());
		FTPUtil.disconnect();
		//3.上传病历文件（可能是pdf，也有可能是图像）
     String fileStr = fileInfo.getUploadBody().getFilecontent();
      byte[] fileBytes = Base64.getDecoder().decode(fileStr);
      //3.1 文件名称处理
      receiveFilePath=sbDir.toString();

      String fileName = fileInfo.getUploadHead().getDocname();
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
	    if(fileName.indexOf("pdf")<0 || fileName.indexOf("PDF")<0){
	    	fileBytes = FTPUtil.imag2PDF(fileBytes, finalName);
	    }
	     
	    StringBuffer sbFile = new StringBuffer(sbDir);
	    String filePath = sbFile.append(ArchUtils.SEPARATOR).append(finalName).toString();
		  
	    //4.判断是否存在CA文件	
	    String caFileStr = fileInfo.getUploadBody().getCafilecontent();
	    String caFileName = fileInfo.getUploadBody().getCafilename();
	    if(!CommonUtils.isEmptyString(caFileStr) && !CommonUtils.isEmptyString(caFileName)){
		   StringBuffer sbCa = new StringBuffer(sbDir);
		   byte[] caBytes = Base64.getDecoder().decode(caFileStr);
		   String caPath =  sbCa.append(ArchUtils.SEPARATOR).append(caFileName).toString();
		   FTPUtil.uploadFileNew(caBytes, new String(caPath.getBytes("GBK"),"iso-8859-1"), null);
	    } 
      
	    //5.改写并上传描述文件
	    fileInfo.getUploadHead().setDocname(finalName);
	    fileInfo.getUploadHead().setPath(sbDir.toString());
	    fileInfo.getUploadHead().setStatus("0");
	    fileInfo.getUploadBody().setFilecontent(null);
	    fileInfo.getUploadBody().setCafilecontent(null);
      
	    String xmlDesc;
	    xmlDesc = XmlUtil.beanToXml(fileInfo, ArchUploadParam.class);
	    StringBuffer sbDesc = new  StringBuffer(sbDir);
	    String descPath = sbDesc.append(ArchUtils.SEPARATOR).append(finalName.contains(".")?finalName.substring(0,finalName.lastIndexOf(".")):finalName).append(ArchUtils.XMLSUFFIX).toString();
	    FTPUtil.uploadFileNew(xmlDesc.toString().getBytes(), new String(descPath.getBytes("GBK"),"iso-8859-1"), null);

	    //6.写日志表TODO
	    fileInfo.getUploadHead().setDocname(finalName);
	    FTPUtil.uploadFileNew(fileBytes, new String(filePath.getBytes("GBK"),"iso-8859-1"), null,"1");
	    long end = System.currentTimeMillis();
	    String version = archOuterService.insertLog(fileInfo,end-start);
//      FTPUtil.uploadFileNew(fileBytes, new String(filePath.getBytes("GBK"),"iso-8859-1"), null,version);
      
      //7.如果是门诊，写入索引数据
//		       if(fileInfo.getUploadHead().getVisittype().equals("1")){
//		    	   DataSourceRoute.putAppId("ARCH");//切换数据源
//		    	   
//			       archOuterService.insert4MZ(fileInfo.getUploadHead(),CommonUtils.isEmptyString(version)?0:Integer.parseInt(version));
//		       }
	}
	}
	private  boolean checkObjFieldIsNull(UploadHead head)  {
		if(head!=null){
			if("3".equals(head.getVisittype())){
				boolean flag = !CommonUtils.isEmptyString(head.getPaticode())&&!CommonUtils.isEmptyString(head.getIptimes())
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
	
	private  boolean checkBodyFieldIsNull(UploadBody obj)  {
		if(obj==null){  
			return false;
		}else{
			return !CommonUtils.isEmptyString(obj.getFilecontent());
		}
	}
	/**
	 * 文件查询
	 * @param content
	 * @return
	 * @throws JAXBException
	 * @throws IOException 
	 * @throws DocumentException 
	 */
	public String qryFile(String content) throws JAXBException, IOException, DocumentException {
		content = "<request><body>"+content+"</body></request>";
		ArchQryParam qryParam = (ArchQryParam) XmlUtil.XmlToBean(content, ArchQryParam.class);	
		String rslt = "";
		ArchQueryResult rtn = archOuterService.queryFile(qryParam);
	
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
	public String cancelArch(String content) throws JAXBException, IOException {
		String rslt = "";
		content = format(content);
		ArchCancelParam param = (ArchCancelParam) XmlUtil.XmlToBean(content, ArchCancelParam.class);
	    ArchResult rtn = archOuterService.requestCancelArch(param);
	
		rslt = XmlUtil.beanToXml(rtn, rtn.getClass());		
		return  rslt;				
	}
	/**
	 * 文件下载
	 * @param content
	 * @return
	 * @throws JAXBException
	 * @throws IOException 
	 */
	public String downloadFile(String content) throws JAXBException, IOException {
		ArchDownloadParam param = (ArchDownloadParam) XmlUtil.XmlToBean(format(content), ArchDownloadParam.class);	
		String rslt = "";
		ArchResult rtn = archOuterService.downloadFile(param);
	
	    rslt = XmlUtil.beanToXml(rtn, rtn.getClass());		
		return  rslt;				
	}

	/**
	 * 查询文档归档状态
	 * @param content
	 * @return
	 */
	public String queryFileStatus(String content) {
		ArchStatusParam param = (ArchStatusParam) XmlUtil.XmlToBean(format(content), ArchStatusParam.class);
		String rslt = "";
		ArchResult rtn = archOuterService.queryFileStatus(param);
	
	    rslt = XmlUtil.beanToXml(rtn, rtn.getClass());		
		return  rslt;
	}
	
	private String format(String content){
		return "<request>"+content+"</request>";
	}


	public String  fromTo() throws IOException, DocumentException {
		
		archOuterService.fromTo();
		 return  "";
	}
	
	class MyTimeoutTask implements Callable<Boolean> {  
	    
		private ArchUploadParam fileInfo;
		private String pvtype;
		private long start;
		
		public MyTimeoutTask(ArchUploadParam fileInfo,String pvtype,long start){
			this.fileInfo = fileInfo;
			this.pvtype = pvtype;
			this.start = start;
		}
	    @Override  
	    public Boolean call() throws Exception {  
	         uploadFileByPvtype(this.fileInfo, this.pvtype, this.start); 
	        return true;  
	    }  
	}
	
	class PushWXTimeoutTask implements Callable<Boolean> {  
		private ArchUploadParam fileInfo;
		private long start;
		public PushWXTimeoutTask(ArchUploadParam fileInfo,long start){
			this.fileInfo = fileInfo;
			this.start = start;
		}
		@Override  
		public Boolean call() throws Exception {  
			//添加微信推送业务
			String pushParam = archOuterService.setPushParam(fileInfo);
			System.out.println(pushParam);
			if(!CommonUtils.isEmptyString(pushParam))
				archOuterService.pushMesByUrl(pushParam);
			long end = System.currentTimeMillis();
			System.out.println("微信推送耗时：" + (end - start) + " ms");
			return true;  
		}  
	}
	
	
	public void executeTask(){
		List<String> failFiles = new ArrayList<String>();
		
		//1.查询今天所有的失败任务
		String sql = "select distinct(FILE_NAME) file_name   from PV_ARCH_ERRORLOG  where flag_del <> '1' and TS > ?  ";		
		List<Map<String,Object>> res =DataBaseHelper.queryForList(sql,"2018-03-14 16:00:00");
		//2.循环处理
		if(res !=null && res.size()>0){
			for(Map<String,Object> map : res){
				
				//1.
				 String resMsg = null;
				
				String fileName = (String)map.get("fileName");
				if(CommonUtils.isEmptyString(fileName)) continue;
				if(fileName.lastIndexOf(".")>0){
					fileName = fileName.substring(0,fileName.lastIndexOf("."));
				}
				
		        Integer cnt = DataBaseHelper.queryForScalar("select count(1) cnt from pv_arch_log where file_name = ?", Integer.class, fileName+".pdf");
		       
		        if(cnt<=0){
		        	try {
			        	   
		        		   String content = ArchFileUtils.writeInFile(fileName+ArchFileUtils.SUFFIX);
		        		   resMsg =uploadFile(content);
				        	
			           } catch (IOException e) {
						    // TODO Auto-generated catch block
						    e.printStackTrace();
						    continue;
					     }
		        }else{
		        	 List<PvArchErrorlog> errlogs = DataBaseHelper.queryForList("select * from PV_ARCH_ERRORLOG where file_name like ? order by ts desc",PvArchErrorlog.class, '%'+fileName+"%");
		        	 List<PvArchLog> logs = DataBaseHelper.queryForList("select * from pv_arch_log where file_name = ?",PvArchLog.class, fileName+".pdf");
				       
		        	PvArchLog log = logs.get(0);
		        	PvArchErrorlog errlog = errlogs.get(0);
		        	Date errTs = errlog.getTs();
		        	Date ts = log.getDateUpload();
		        	if(errTs.compareTo(ts)>0){
		        		try {
			        		   String content = ArchFileUtils.writeInFile(fileName+ArchFileUtils.SUFFIX);
			        		   resMsg = uploadFile(content);
				           } catch (IOException e) {
							    // TODO Auto-generated catch block
							    e.printStackTrace();
							    continue;
						    }
		        	}else{
		        		 archOuterService.deleteLog("%"+fileName+"%");
					     ArchFileUtils.deleteFile(ArchFileUtils.TEMPPATH+fileName+ArchFileUtils.SUFFIX);
		        	}
		        }
		        
		        if(!CommonUtils.isEmptyString(resMsg) && !resMsg.contains("99999")){
		        	 archOuterService.deleteLog("%"+fileName+"%");
				     ArchFileUtils.deleteFile(ArchFileUtils.TEMPPATH+fileName+ArchFileUtils.SUFFIX);
		        }
			}
		}
		
	}
	
	public String dateTrans(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String temp = null;
		if(date!=null){
			temp = sdf.format(date);
		}
		
		return temp;
	}

	/**
	 * 添加住院-是否可上传校验
	 * @param codeIp
	 * @param iptimes
	 * @return
	 */
	public String ChkIpPvArch(String codeIp,String iptimes){
		String rs = "";
		try {
			if(CommonUtils.isEmptyString(codeIp))
				return "住院上传时，患者住院号为空！";
			if(CommonUtils.isEmptyString(iptimes))
				return "住院上传时，患者就诊次数为空！";
			List<PvArchive> pas = DataBaseHelper.queryForList("select pa.* from  pv_archive pa"
					+ " inner join pv_encounter pv on pv.pk_pv = pa.pk_pv and pv.eu_pvtype = '3'"
					+ " inner join pi_master pi on pi.pk_pi = pv.pk_pi and pi.del_flag = '0'"
					+ " inner join pv_ip ip on ip.pk_pv = pv.pk_pv and ip.del_flag = '0'"
					+ " where pi.code_ip = ? and ip.ip_times = ? ",PvArchive.class, codeIp , iptimes);
			if(pas!=null && pas.size()>0){
				PvArchive pa = pas.get(0);
				if("8".equals(pa.getEuStatus())){
					return "该患者已归档，请先提交解除归档申请，再重新上传！";
				}else if("9".equals(pa.getEuStatus())){
					return "该患者已封存，不能重新上传！";
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
	public PvEncounterVo queryPkPvByCodePv(String codeIp, String ipTimes) {
		PvEncounterVo pv = null;
		String sql = "select pi.code_pi,pi.code_ip, pv.* from PV_ENCOUNTER pv inner join pv_ip ip on pv.pk_pv = ip.pk_pv inner join pi_master pi on pv.pk_pi = pi.pk_pi and pi.DEL_FLAG='0' where pi.CODE_IP=? and ip.IP_TIMES=? and ip.DEL_FLAG='0' and pv.EU_STATUS!='9' ";
	    pv = DataBaseHelper.queryForBean(sql, PvEncounterVo.class, codeIp,ipTimes);
		return pv;
	};
	
}
