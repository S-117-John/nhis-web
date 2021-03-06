package com.zebone.nhis.webservice.zsrm.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.*;
import java.util.concurrent.Callable;

import javax.annotation.Resource;
import javax.xml.bind.JAXBException;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
import com.zebone.nhis.common.module.arch.PvArchive;
import com.zebone.nhis.common.module.base.ou.BdOuUser;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.ArchUtils;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.common.support.FTPUtil;
import com.zebone.nhis.common.support.XmlUtil;
import com.zebone.nhis.webservice.zhongshan.vo.PvEncounterVo;
import com.zebone.platform.modules.dao.DataBaseHelper;

@Service
public class ZsrmArchOuterHandler {
	@Resource
	private ArchOuterService archOuterService;
	
	@Resource
	private ArchiveCollectService archiveCollectService;
	
	public Logger log = LogManager.getLogger(ZsrmArchOuterHandler.class.getName());
	
	private String receiveFilePath="";
	/**
	 * ????????????
	 * @param content
	 * @return
	 * @throws JAXBException
	 * @throws IOException 
	 */
	public String uploadFile(String content)  {
		ArchUploadParam fileInfo = null;
		ArchResultUp rtnError = null;
		try{
			//??????????????????
			fileInfo = (ArchUploadParam) XmlUtil.XmlToBean(format(content), ArchUploadParam.class);
			if(fileInfo==null){
				rtnError = new ArchResultUp();
				rtnError.setResponse(Response.getErrorInstance("??????????????????,?????????!"));	
				archOuterService.insertErrorLog("??????????????????,?????????!", ArchUtils.getErrorXml(fileInfo));
				return  XmlUtil.beanToXml(rtnError, rtnError.getClass());				
			}
			if(fileInfo.getUploadBody()==null){
				rtnError = new ArchResultUp();
				rtnError.setResponse(Response.getErrorInstance("??????????????????,?????????!"));	
				archOuterService.insertErrorLog("??????????????????,?????????!", ArchUtils.getErrorXml(fileInfo));
				return  XmlUtil.beanToXml(rtnError, rtnError.getClass());	
			}
			if(fileInfo.getUploadHead()==null){
				rtnError = new ArchResultUp();
				rtnError.setResponse(Response.getErrorInstance("??????????????????,?????????!"));	
				archOuterService.insertErrorLog("??????????????????,?????????!", ArchUtils.getErrorXml(fileInfo));
				return  XmlUtil.beanToXml(rtnError, rtnError.getClass());	
			}
			if(!checkBodyFieldIsNull(fileInfo.getUploadBody())){
				rtnError = new ArchResultUp();
				rtnError.setResponse(Response.getErrorInstance("????????????????????????,?????????!"));	
				archOuterService.insertErrorLog("????????????????????????,?????????!", ArchUtils.getErrorXml(fileInfo));
				return  XmlUtil.beanToXml(rtnError, rtnError.getClass());
			}
			if(!checkObjFieldIsNull(fileInfo.getUploadHead())){			
				rtnError = new ArchResultUp();
				rtnError.setResponse(Response.getErrorInstance("????????????????????????,?????????!"));	
				archOuterService.insertErrorLog("????????????????????????,?????????!", ArchUtils.getErrorXml(fileInfo));
				return  XmlUtil.beanToXml(rtnError, rtnError.getClass());	
			}
			//??????????????????
			String rslt = "";
			ArchResultUp result = new ArchResultUp();
			Map<String,Object> resMap = new HashMap<String,Object>();
			//??????????????????
			/*String fileOrgName = fileInfo.getUploadHead().getDocname();
			String codePi = fileInfo.getUploadHead().getPid();
			String pvtype = fileInfo.getUploadHead().getVisittype();
			String codeIp = fileInfo.getUploadHead().getPaticode();
			String ipTimes = fileInfo.getUploadHead().getIptimes();
			String sysName = fileInfo.getUploadHead().getSysname();
			String rid= fileInfo.getUploadHead().getRid();
			String rname=fileInfo.getUploadHead().getMemo();
			String rOperName=fileInfo.getUploadHead().getUser();
			String isArch=fileInfo.getUploadHead().getIsArch();*/
			//PvEncounter pkPvByIpTimes=null;
			//PvEncounterVo pvInfo = null ;???
			
			PvArchive record = null;
			//??????paticode ??? iptimes????????????pv_archive??????
			record = archiveCollectService.getPvArchiveByPvEn(fileInfo.getUploadHead());
			//???????????????????????????????????????
			String chkIpPvStatus = ChkIpPvArch(fileInfo.getUploadHead());
			if(!CommonUtils.isEmptyString(chkIpPvStatus)){
				rtnError = new ArchResultUp();
				rtnError.setResponse(Response.getErrorInstance(chkIpPvStatus));
				return  XmlUtil.beanToXml(rtnError, rtnError.getClass());
			}
			//????????????
			long start = System.currentTimeMillis();
			uploadFileByPvtype(fileInfo,start);
			//????????????????????????
			if(record==null){
				rtnError = new ArchResultUp();
				rtnError.setResponse(Response.getErrorInstance("??????????????????!"));	
				return  XmlUtil.beanToXml(rtnError, rtnError.getClass());	
			}
			//??????pv_arch_doc??????
			PvArchDoc doc = archiveCollectService.getArchDoc(fileInfo.getUploadHead(), record);
			//?????????????????????????????????pv_arch_doc
			BdOuUser user=new BdOuUser();
			user.setCreateTime(new Date());
			user.setNameUser(fileInfo.getUploadHead().getUser());
			archiveCollectService.updateArchDoc(0, user, doc, receiveFilePath,0,null);	
			//??????????????????????????????????????????????????????????????????????????????
			/*if("3".equals(pvtype)) {
				pvInfo = queryPkPvByCodePv(codeIp,ipTimes);
				//2018-05-03 ?????????????????????????????????????????????8-?????????????????????9-??????????????????
				String chkIpPvStatus = ChkIpPvArch(fileInfo.getUploadHead().getVisitcode(), fileInfo.getUploadHead().getIptimes());
				if(!CommonUtils.isEmptyString(chkIpPvStatus)){
					rtnError = new ArchResultUp();
					rtnError.setResponse(Response.getErrorInstance(chkIpPvStatus));
					return  XmlUtil.beanToXml(rtnError, rtnError.getClass());
				}
				//????????????????????????????????????
				if(null == pvInfo){
					rtnError = new ArchResultUp();
					rtnError.setResponse(Response.getErrorInstance("?????????????????????????????????????????????????????????????????????!"));	
					return  XmlUtil.beanToXml(rtnError, rtnError.getClass());	
				}
				//????????????CodePi??????????????????
				if(!codePi.equals(pvInfo.getCodePi()))
					fileInfo.getUploadHead().setPaticode(pvInfo.getCodePi());
				
				//2020-04-09 ????????????????????????????????????????????????pkpv??????????????????
				pkPvByIpTimes =(PvEncounter) pvInfo;
				record = archiveCollectService.geneArchRecrdByPvEn(pkPvByIpTimes);
			}*/
			
			/*boolean flag = TimeTaskUtils.execute(new  MyTimeoutTask(fileInfo,pvtype,start), 10); 
			if(!flag){
				rtnError = new ArchResultUp();
				archOuterService.insertErrorLog("?????????????????????", ArchUtils.getErrorXml(fileInfo),fileInfo);
				rtnError.setResponse(Response.getErrorInstance("?????????????????????"));	
				return  XmlUtil.beanToXml(rtnError, rtnError.getClass());
			}*/
			//if("3".equals(pvtype)){ 
				
			//}
			//6.??????????????????
			Response  res = Response.getInstance();
			result.setResponse(res);
			resMap.put("result", result);
			resMap.put("filePath", receiveFilePath);
			
			ArchResultUp rtn = (ArchResultUp) resMap.get("result");
			rslt = XmlUtil.beanToXml(rtn, rtn.getClass());

			return  rslt;	
			
			
		}catch(com.zebone.platform.modules.exception.BusException e){
			rtnError = new ArchResultUp();
			rtnError.setResponse(Response.getErrorInstance(e.getMessage()));
			archOuterService.insertErrorLog("?????????????????????xml????????????"+e.getMessage(), null);
			return  XmlUtil.beanToXml(rtnError, rtnError.getClass());
		}catch(Exception e){
			rtnError = new ArchResultUp();
			rtnError.setResponse(Response.getErrorInstance(e.getMessage()));
			archOuterService.insertErrorLog("???????????????"+e.getMessage(), content);
			return  XmlUtil.beanToXml(rtnError, rtnError.getClass());
		}
	}
	
	private void uploadFileByPvtype(ArchUploadParam fileInfo,long start) throws IOException,
	UnsupportedEncodingException {
		//log.info("uploadFileByPvtype");
		try {
			Thread.sleep(10);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//??????visitcode
		fileInfo = ArchUtils.handlePaticodeZsrm(fileInfo);
		//2.????????????????????????
		String archPath = ApplicationUtils.getPropertyValue("arch.file.path", "");
		if(StringUtils.isBlank(archPath)) archPath="/data/archive";
		String archDir = ApplicationUtils.getPropertyValue("arch.file.dir", "");
		if(StringUtils.isBlank(archDir)) archDir="/NHISArchive";
		String separator = ApplicationUtils.getPropertyValue("arch.separator", "");
		if(StringUtils.isBlank(separator)) archDir="/";
		
		StringBuffer sbDir = new StringBuffer();
		sbDir.append(archPath);//"/data/archive"
		sbDir.append(archDir);//"/NHISArchive:ArchUtils.BASE_DIR
		sbDir.append(separator);//ArchUtils.SEPARATOR
		
		//sbDir.append("/data/archive");
		//sbDir.append(ArchUtils.BASE_DIR);//"/NHISArchive
		//sbDir.append(ArchUtils.SEPARATOR);
		String sPath = sbDir.toString();
		
		sbDir.append(ArchUtils.get1stDir(fileInfo.getUploadHead().getP_id()));
		sbDir.append(separator).append(fileInfo.getUploadHead().getP_id()).
		append(separator).append(fileInfo.getUploadHead().getVisitcode());
		
		//sbDir.append(ArchUtils.get1stDir(fileInfo.getUploadHead().getPaticode()));
		//sbDir.append(ArchUtils.SEPARATOR).append(fileInfo.getUploadHead().getPaticode()).
		//append(ArchUtils.SEPARATOR).append(fileInfo.getUploadHead().getVisitcode());
		
		//???????????????
		File dir = new File(sbDir.toString().replaceAll("\\\\", "/"));
		//File dir = new File(sbDir.toString());
        if (!dir.exists()) {// ????????????????????????     
            dir.mkdirs();   
        }
		//3.??????????????????????????????pdf???????????????????????????
	    String fileStr = fileInfo.getUploadBody().getFilecontent();
	    byte[] fileBytes = Base64.getDecoder().decode(fileStr);
	      /*for(int i=0;i<fileBytes.length;++i)
          {
              if(fileBytes[i]<0)
              {//??????????????????
            	  fileBytes[i]+=256;
              }
          }*/
	      //3.1 ??????????????????
	      //receiveFilePath=sbDir.toString();
	      receiveFilePath=sbDir.toString().replace(sPath, "");
	      
	      String fileName = fileInfo.getUploadHead().getDocname();
	      String finalName = null;
	      if(fileName.contains(".")){
			/*String fileStr = fileInfo.getUploadBody().getFilecontent();
			byte[] fileBytes = ArchUtils.decoder.decodeBuffer(fileStr);
			//3.1 ??????????????????
			String fileName = fileInfo.getUploadHead().getDocname();
		    String finalName = null;*/
		    if(fileName.contains(".")){
			   finalName =fileName.substring(0,fileName.lastIndexOf("."))+ArchUtils.FILESUFFIX;
		    }
		    if(fileName.indexOf("pdf")<0 && fileName.indexOf("PDF")<0){
		    	fileBytes = FTPUtil.imag2PDF(fileBytes, finalName);
		    }
		     
		    StringBuffer sbFile = new StringBuffer(sbDir);
		    //String filePath = sbFile.append(ArchUtils.SEPARATOR).append(finalName).toString();
		    String filePath = sbFile.append(separator).append(finalName).toString();
		    
		    //4.??????????????????CA??????	
		    String caFileStr = fileInfo.getUploadBody().getCafilecontent();
		    String caFileName = fileInfo.getUploadBody().getCafilename();
		    if(!CommonUtils.isEmptyString(caFileStr) && !CommonUtils.isEmptyString(caFileName)){
			   StringBuffer sbCa = new StringBuffer(sbDir);
			   byte[] caBytes = Base64.getDecoder().decode(caFileStr);
			   //String caPath =  sbCa.append(ArchUtils.SEPARATOR).append(caFileName).toString();
			   String caPath = sbFile.append(separator).append(caFileName).toString();
			   
			   FTPUtil.uploadFileNew(caBytes, new String(caPath.getBytes("GBK"),"iso-8859-1"), null);
		    } 
	      
		    //5.???????????????????????????
		    fileInfo.getUploadHead().setDocname(finalName);
		    fileInfo.getUploadHead().setPath(sbDir.toString());
		    fileInfo.getUploadHead().setStatus("0");
		    fileInfo.getUploadBody().setFilecontent(null);
		    fileInfo.getUploadBody().setCafilecontent(null);
	      
		    String xmlDesc;
		    xmlDesc = XmlUtil.beanToXml(fileInfo, ArchUploadParam.class);
		    StringBuffer sbDesc = new  StringBuffer(sbDir);
		    //String descPath = sbDesc.append(ArchUtils.SEPARATOR).append(finalName.contains(".")?finalName.substring(0,finalName.lastIndexOf(".")):finalName).append(ArchUtils.XMLSUFFIX).toString();
		    String descFileName=fileName.substring(0,fileName.lastIndexOf("."))+".xml";
		    String descFilePath = sbDesc.append(separator).append(descFileName).toString();		    
		    writeFile(xmlDesc.toString().getBytes(), descFilePath);
		    
		    //TODO
		    //3.3????????????
		    OutputStream out = new FileOutputStream(new File(new String(filePath.getBytes("GBK"),"iso-8859-1"))); 
			out.write(fileBytes);
			out.flush();
			out.close();

		    //6.????????????TODO
		    fileInfo.getUploadHead().setDocname(finalName);
		    long end = System.currentTimeMillis();
		    String version = archOuterService.insertLog(fileInfo,end-start);
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
	
	private String format(String content){
		return "<request>"+content+"</request>";
	}
	
	private  boolean checkBodyFieldIsNull(UploadBody obj)  {
		if(obj==null){  
			return false;
		}else{
			return !CommonUtils.isEmptyString(obj.getFilecontent());
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
	         uploadFileByPvtype(this.fileInfo, this.start); 
	        return true;  
	    }  
	}
	
	/**
	 * ??????????????????
	 * @param codeIp
	 * @param iptimes
	 * @return
	 */
	public String ChkIpPvArch(UploadHead headInfo){
		String rs = "";
		try {
			if(CommonUtils.isEmptyString(headInfo.getPaticode()))
				return "??????????????????PatiCode????????????";
			if(CommonUtils.isEmptyString(headInfo.getTimes()))
				return "??????????????????Times?????????";
			if(CommonUtils.isEmptyString(headInfo.getDocname()))
				return "?????????????????????????????????";
			List<PvArchDoc> pas = DataBaseHelper.queryForList("select doc.ISARCH from pv_archive arch "
					+ "inner join pv_arch_doc doc on arch.pk_archive=doc.pk_archive "
					+ "where arch.paticode=? and arch.TIMES=? and doc.NAME_DOC=?"
					,PvArchDoc.class, headInfo.getPaticode() , headInfo.getTimes(),headInfo.getDocname().split("\\.")[0]+".pdf");
			if(pas!=null && pas.size()>0){
				PvArchDoc pa = pas.get(0);
				if("1".equals(pa.getIsArch())){
					return "????????????????????????????????????????????????????????????????????????";
				}
			}
		} catch (Exception e) {
			return e.getMessage();
		}
		return rs;
	}

	/**
	 * ???????????????????????????????????????????????????????????????
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
	
	/**
	 * ????????????
	 * @param content
	 * @return
	 * @throws JAXBException
	 * @throws IOException 
	 */
	public String downloadFile(String content) throws JAXBException, IOException {
		ArchDownloadParam param = (ArchDownloadParam) XmlUtil.XmlToBean(format(content), ArchDownloadParam.class);	
		String rslt = "";
		ArchResult rtn = archOuterService.downloadFileZSRM(param);
	
	    rslt = XmlUtil.beanToXml(rtn, rtn.getClass());		
		return  rslt;				
	}
	
	/**
	 * ????????????
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
	 * ??????????????????
	 * @param content
	 * @return
	 * @throws JAXBException
	 * @throws IOException 
	 */
	public String cancelArch(String content) throws JAXBException, IOException {
		String rslt = "";
		content = format(content);
		ArchCancelParam param = (ArchCancelParam) XmlUtil.XmlToBean(content, ArchCancelParam.class);
	    ArchResult rtn = archOuterService.requestCancelArchZSRM(param);
	
		rslt = XmlUtil.beanToXml(rtn, rtn.getClass());		
		return  rslt;				
	}
	
	
	/**
	 * ????????????????????????
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
	
}
