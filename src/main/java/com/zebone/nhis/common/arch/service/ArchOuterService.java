package com.zebone.nhis.common.arch.service;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.Callable;

import org.apache.commons.net.ftp.FTPFile;
import org.apache.shiro.util.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.stereotype.Service;

import com.zebone.nhis.common.arch.vo.ArchCancelParam;
import com.zebone.nhis.common.arch.vo.ArchDownloadParam;
import com.zebone.nhis.common.arch.vo.ArchQryParam;
import com.zebone.nhis.common.arch.vo.ArchQryParamBody;
import com.zebone.nhis.common.arch.vo.ArchQueryResult;
import com.zebone.nhis.common.arch.vo.ArchResult;
import com.zebone.nhis.common.arch.vo.ArchResultBody;
import com.zebone.nhis.common.arch.vo.ArchResultUp;
import com.zebone.nhis.common.arch.vo.ArchStatusParam;
import com.zebone.nhis.common.arch.vo.ArchUploadParam;
import com.zebone.nhis.common.arch.vo.Fileinfo;
import com.zebone.nhis.common.arch.vo.Response;
import com.zebone.nhis.common.arch.vo.UploadBody;
import com.zebone.nhis.common.arch.vo.UploadHead;
import com.zebone.nhis.common.module.arch.BdArchDoctype;
import com.zebone.nhis.common.module.arch.HstbArchiveIndex;
import com.zebone.nhis.common.module.arch.PvArchApply;
import com.zebone.nhis.common.module.arch.PvArchDoc;
import com.zebone.nhis.common.module.arch.PvArchErrorlog;
import com.zebone.nhis.common.module.arch.PvArchLog;
import com.zebone.nhis.common.module.arch.PvArchive;
import com.zebone.nhis.common.module.emr.mgr.EmrOperateLogs;
import com.zebone.nhis.common.module.pv.PvEncounter;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.ArchUtils;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.common.support.FTPUtil;
import com.zebone.nhis.common.support.TimeTaskUtils;
import com.zebone.nhis.common.support.XmlUtil;
import com.zebone.platform.common.support.NHISUUID;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.dao.jdbc.DataSourceRoute;

@Service
public class ArchOuterService {
	
	private String receiveFilePath="";
	/**
	 * ??????????????????????????????
	 * @param pacs
	 * @return
	 * @throws IOException 
	 */
	public Map<String,Object> uploadFile(ArchUploadParam fileInfo)  {

		Map<String,Object> resMap = new HashMap<String,Object>();

		long start = System.currentTimeMillis();
		
		try{
				
				if(fileInfo==null){
					ArchResultUp rtnError = new ArchResultUp();
					rtnError.setResponse(Response.getErrorInstance("??????????????????,?????????!"));	
					insertErrorLog("??????????????????,?????????!", ArchUtils.getErrorXml(fileInfo));
					
				    
				    resMap.put("result", rtnError);
					return resMap;			
				}
				if(fileInfo.getUploadBody()==null){
					ArchResultUp rtnError = new ArchResultUp();
					rtnError.setResponse(Response.getErrorInstance("??????????????????,?????????!"));	
					insertErrorLog("??????????????????,?????????!", ArchUtils.getErrorXml(fileInfo));
					 resMap.put("result", rtnError);
						return resMap;		
				}
				if(fileInfo.getUploadHead()==null){
					ArchResultUp rtnError = new ArchResultUp();
					rtnError.setResponse(Response.getErrorInstance("??????????????????,?????????!"));	
					insertErrorLog("??????????????????,?????????!", ArchUtils.getErrorXml(fileInfo));
					 resMap.put("result", rtnError);
						return resMap;	
				}
				
				
		
					ArchResultUp result = new ArchResultUp();
			        StringBuilder sbDir = new StringBuilder();
					
						
						//1.???????????????????????????visitcode???paticode???docname???;
						List<String> bodyFields = new ArrayList<String>();
						bodyFields.add("filecontent");
						List<String>  headFields = new ArrayList<String>();
						headFields.add("visitcode");
						headFields.add("paticode");
						headFields.add("docname");
						headFields.add("iptimes");
						
						if(!checkBodyFieldIsNull(fileInfo.getUploadBody())){
							ArchResultUp rtnError = new ArchResultUp();
							rtnError.setResponse(Response.getErrorInstance("????????????????????????,?????????!"));	
							
							insertErrorLog("????????????????????????,?????????!", ArchUtils.getErrorXml(fileInfo));
							 resMap.put("result", rtnError);
								return resMap;	
						}
						if(!checkObjFieldIsNull(fileInfo.getUploadHead())){			
							ArchResultUp rtnError = new ArchResultUp();
							rtnError.setResponse(Response.getErrorInstance("????????????????????????,?????????!"));	
							insertErrorLog("????????????????????????,?????????!", ArchUtils.getErrorXml(fileInfo));
							 resMap.put("result", rtnError);
								return resMap;		
							
						}
						
						//?????? ???????????????????????????????????? 2018-02-04 22???33???
						String pvtype = fileInfo.getUploadHead().getVisittype();
					    boolean flag = TimeTaskUtils.execute(new  MyTimeoutTask(fileInfo,pvtype,start), 20); 
					   // uploadFileByPvtype(fileInfo, pvtype,start);
					    insertLog(fileInfo,100l);
					    if(!flag){
					    	ArchResultUp rtnError = new ArchResultUp();
							rtnError.setResponse(Response.getErrorInstance("?????????????????????"));	
							insertErrorLog("?????????????????????", ArchUtils.getErrorXml(fileInfo));
							 resMap.put("result", rtnError);
								return resMap;	 
					    }
					//6.??????????????????
			         Response  res = Response.getInstance();
				     result.setResponse(res);
				     resMap.put("result", result);
				     resMap.put("filePath", receiveFilePath);
						
				 ArchResultUp rtn = (ArchResultUp) resMap.get("result");
				 resMap.put("result", rtn);
					return resMap;
		}catch(com.zebone.platform.modules.exception.BusException e){
			ArchResultUp rtnError = new ArchResultUp();
			rtnError.setResponse(Response.getErrorInstance(e.getMessage()));
			insertErrorLog("?????????????????????xml????????????"+e.getMessage(), null);
			 resMap.put("result", rtnError);
				return resMap;	
		}catch(Exception e){
			ArchResultUp rtnError = new ArchResultUp();
			rtnError.setResponse(Response.getErrorInstance(e.getMessage()));
			insertErrorLog("???????????????"+e.getMessage(), null);
			 resMap.put("result", rtnError);
		     return resMap;	 
		}
	
	}
	
	private void uploadFileByPvtype(ArchUploadParam fileInfo,String pvtype,long start) throws IOException,
		UnsupportedEncodingException {
		try{
			//2018-02-04 ??????????????????????????????????????????????????????ID
			fileInfo = ArchUtils.handlePaticode(fileInfo);
			//2.????????????????????????
			StringBuilder sbDir = new StringBuilder();
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
			//3.??????????????????????????????pdf???????????????????????????
			String fileStr = fileInfo.getUploadBody().getFilecontent();
			byte[] fileBytes = Base64.getDecoder().decode(fileStr);
			//3.1 ??????????????????
			   
			receiveFilePath=sbDir.toString();
			String fileName = fileInfo.getUploadHead().getDocname();
			String finalName = null;
			if(fileName.contains(".")){
			   finalName =fileName.substring(0,fileName.lastIndexOf("."))+ArchUtils.FILESUFFIX;
			}
			if(fileName.indexOf("pdf")<0 || fileName.indexOf("PDF")<0){
			   
			   fileBytes = FTPUtil.imag2PDF(fileBytes, finalName);
			}
			
			StringBuilder sbFile = new StringBuilder(sbDir);
			String filePath = sbFile.append(ArchUtils.SEPARATOR).append(finalName).toString();
			
			  
			//4.??????????????????CA??????	
			String caFileStr = fileInfo.getUploadBody().getCafilecontent();
			String caFileName = fileInfo.getUploadBody().getCafilename();
			if(!CommonUtils.isEmptyString(caFileStr) && !CommonUtils.isEmptyString(caFileName)){
			   StringBuilder sbCa = new StringBuilder(sbDir);
			   byte[] caBytes = Base64.getDecoder().decode(caFileStr);
			   String caPath =  sbCa.append(ArchUtils.SEPARATOR).append(caFileName).toString();
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
			StringBuilder sbDesc = new  StringBuilder(sbDir);
			String descPath = sbDesc.append(ArchUtils.SEPARATOR).append(finalName.contains(".")?finalName.substring(0,finalName.lastIndexOf(".")):finalName).append(ArchUtils.XMLSUFFIX).toString();
			FTPUtil.uploadFileNew(xmlDesc.toString().getBytes(), new String(descPath.getBytes("GBK"),"iso-8859-1"), null);
			
			//6.????????????TODO
			fileInfo.getUploadHead().setDocname(finalName);
			long end = System.currentTimeMillis();
			String version =  insertLog(fileInfo,end-start);
			FTPUtil.uploadFileNew(fileBytes, new String(filePath.getBytes("GBK"),"iso-8859-1"), null,version);
		}catch(Exception ex){
			EmrOperateLogs emrOpeLogs = new EmrOperateLogs();
			emrOpeLogs.setCode("archive");
			emrOpeLogs.setName("????????????");
			emrOpeLogs.setDelFlag("0");
			emrOpeLogs.setEuStatus("0");
			emrOpeLogs.setPkOrg(UserContext.getUser().getPkOrg());
			emrOpeLogs.setPkPv(fileInfo.getUploadHead().getPaticode());
			if(ex.getMessage().length()>1000){
				emrOpeLogs.setOperateTxt("????????????"+fileInfo.getUploadHead().getDocname()+"???????????????: "+ex.getMessage().substring(0, 1000));
			}else{
				emrOpeLogs.setOperateTxt("????????????"+fileInfo.getUploadHead().getDocname()+"???????????????: "+ex.getMessage());
			}
			DataBaseHelper.insertBean(emrOpeLogs);
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
	 *  ????????????
	 * @param qryParam
	 * @return
	 */
	public ArchResult downloadFile(ArchDownloadParam param )  {
		ArchResult result = new ArchResult();
		if(CommonUtils.isEmptyString(param.getFilename())&& CommonUtils.isEmptyString(param.getFilepath())){
			
		}
         ByteArrayOutputStream baos = FTPUtil.downloadFile(param.getFilepath(), param.getFilename());
         byte[] bitmapBytes = baos.toByteArray();
         
         result.setFilecontent(Base64.getEncoder().encodeToString(bitmapBytes).trim());
         result.setResponse(Response.getInstance());
         return result;
		
	}
	
	/**
	 *  ????????????--??????
	 * @param qryParam
	 * @return
	 */
	public ArchResult downloadFileZSRM(ArchDownloadParam param )  {
		ArchResult result = new ArchResult();
		if(CommonUtils.isEmptyString(param.getFilename())&& CommonUtils.isEmptyString(param.getFilepath())){
			result.setResponse(Response.getErrorInstance("????????????"));
			return result;
		}
         byte[] bitmapBytes = getFileBase64StrByLocalFile(param.getFilepath()+param.getFilename());
         result.setFilecontent(Base64.getEncoder().encodeToString(bitmapBytes).trim());
         result.setResponse(Response.getInstance());
         return result;
		
	}
	
	public static byte[] getFileBase64StrByLocalFile(String localFilePath){
        InputStream in = null;
        byte[] data = null;
        //????????????????????????
        try 
        {
            in = new FileInputStream(localFilePath);        
            data = new byte[in.available()];
            in.read(data);
            in.close();
        } 
        catch (IOException e) 
        {
            e.printStackTrace();
        }
        return data;
    }
	
	/**
	 * ????????????
	 * @param visitcode
	 * @return
	 */
	public ArchResult requestCancelArch(ArchCancelParam param){
		ArchResult result = new ArchResult();
		Response res = null;
		if(param!=null && !CommonUtils.isEmptyString(param.getVisitcode())&& !CommonUtils.isEmptyString(param.getIptimes())){
			String visitcode = param.getVisitcode();
			String ipTimes = param.getIptimes();
			//1.???????????????????????????????????????
//			List<PvArchive> pas = DataBaseHelper.queryForList(" select * from pv_archive where code_ip = ? and ip_times = ? and flag_arch = '1' ",PvArchive.class,visitcode,ipTimes);
			List<PvArchive> pas = DataBaseHelper.queryForList("select pa.* from  pv_archive pa"
					+ " inner join pv_encounter pv on pv.pk_pv = pa.pk_pv and pv.eu_pvtype = '3'"
					+ " inner join pi_master pi on pi.pk_pi = pv.pk_pi and pi.del_flag = '0'"
					+ " inner join pv_ip ip on ip.pk_pv = pv.pk_pv and ip.del_flag = '0'"
					+ " where pi.code_ip = ? and ip.ip_times = ? ",PvArchive.class, visitcode , ipTimes);
			if(pas!=null && pas.size()>0){
				PvArchive pa = pas.get(0);
				try{
					if("1".equals(pa.getFlagArch())){
						//?????????????????????????????????????????????
						PvArchApply apply = new PvArchApply();
						apply.setPkArchive(pa.getPkArchive());
						apply.setPkOrg("~");
						apply.setEuType("0");
						apply.setEuStatus("0");
						apply.setFlagDel("0");
						apply.setPkApply(NHISUUID.getKeyId());
						DataBaseHelper.insertBean(apply);
						
						res = Response.getInstance();
					}else{
						res = Response.getErrorInstance("??????????????????,??????????????????!");
					}
				}catch(Exception e){
					res = Response.getErrorInstance("??????????????????!");
				}
			}else{
				res = Response.getErrorInstance("?????????????????????????????????");
			}
		}else{
			res = Response.getErrorInstance("????????????");
		}
		result.setResponse(res);
		return result;
	}
	/**
	 * ????????????--??????
	 * @param visitcode
	 * @return
	 */
	public ArchResult requestCancelArchZSRM(ArchCancelParam param){
		ArchResult result = new ArchResult();
		Response res = null;
		if(param!=null && !CommonUtils.isEmptyString(param.getPaticode())&& !CommonUtils.isEmptyString(param.getTimes()) 
				&& !CommonUtils.isEmptyString(param.getType()) && !CommonUtils.isEmptyString(param.getFilename())){
			String paticode = param.getPaticode();
			String times = param.getTimes();
			String type=param.getType();
			String filename=param.getFilename();
			if(type.equals("0")) {
				
			}else if(type.equals("1")) {
				List<PvArchDoc> pas = DataBaseHelper.queryForList("select doc.pk_doc,doc.ISARCH from pv_archive arch "
						+ "inner join pv_arch_doc doc on arch.pk_archive=doc.pk_archive "
						+ "where arch.paticode=? and arch.TIMES=? and doc.NAME_DOC=?"
						,PvArchDoc.class, paticode , times,filename.split("\\.")[0]+".pdf");
				if(pas!=null && pas.size()>0){
					PvArchDoc pa = pas.get(0);
					try{
						if("1".equals(pa.getIsArch())){
							//???????????????????????????
							PvArchApply apply = new PvArchApply();
							apply.setPkArchive(pa.getPkDoc());
							apply.setPkOrg("~");
							apply.setEuType("0");
							apply.setEuStatus("0");
							apply.setFlagDel("0");
							apply.setPkApply(NHISUUID.getKeyId());
							DataBaseHelper.insertBean(apply);
							res = Response.getInstance();
							
							String sql="update pv_arch_doc set isarch='0' where pk_doc='"+pa.getPkDoc()+"'";
							DataBaseHelper.update(sql, new Object[]{});
						}else{
							res = Response.getErrorInstance("??????????????????,??????????????????!");
						}
					}catch(Exception e){
						res = Response.getErrorInstance("??????????????????!");
					}
				}
			}
		}else{
			res = Response.getErrorInstance("????????????");
		}
		result.setResponse(res);
		return result;
	}
	/**
	 *  ????????????
	 * 	    1?????????????????????????????????
			2???	???????????????????????????????????????????????????????????????+?????????????????????
	 * @param qryParam
	 * @return
	 * @throws IOException 
	 * @throws DocumentException 
	 */
	public ArchQueryResult queryFileFromXMl(ArchQryParam qryParam) throws IOException, DocumentException  {
		
		ArchQueryResult  result = new ArchQueryResult();
		try{
				ArchQryParamBody param  = qryParam.getArchQryParamBody();
				if(param==null){
					result.setResponse(Response.getErrorInstance("???????????????"));
					return result;
				}
	
				String patiCode = "";
				String pvCode = "";
				ArchQryParamBody paramBody = qryParam.getArchQryParamBody();
				if(paramBody==null){
					result.setResponse(Response.getErrorInstance("???????????????"));
					return result;
				}
				if(CommonUtils.isEmptyString(param.getVisitcode())){
					result.setResponse(Response.getErrorInstance("??????visitcode?????????"));
					return result;
				}
				pvCode =param.getVisitcode();
				if(CommonUtils.isEmptyString(param.getPaticode())){
					result.setResponse(Response.getErrorInstance("??????paticode?????????"));
					return result;
				}
				patiCode =param.getPaticode();
			    
				List<Fileinfo> fileinfos = new ArrayList<Fileinfo>();
				List<Fileinfo> fileinfosNew = qryFileList(patiCode,pvCode,true);
				List<Fileinfo> fileinfosOld = qryFileList(patiCode,pvCode,false);

				if(fileinfosNew!=null){
					fileinfos.addAll(fileinfosNew);
				}
				if(fileinfosOld!=null){
					fileinfos.addAll(fileinfosOld);
				}
				
				if(fileinfos==null || fileinfos.size()<1){
					result.setResponse(Response.getErrorInstance("?????????????????????"));
					return result;
				}
				
		       ArchResultBody body = new ArchResultBody();
		       body.setFileinfos(fileinfos);
		       result.setArchResultBody(body);
		       result.setResponse(Response.getInstance());	
		       return result;
		}catch(Exception e){
			result.setResponse(Response.getErrorInstance("??????????????????"));
			return result;
		}
	}
	
	
	/**
	 *      ????????????--??????????????????
	 * 	    1?????????????????????????????????
			2???	???????????????????????????????????????????????????????????????+?????????????????????
	 * @param qryParam
	 * @return
	 * @throws IOException 
	 * @throws DocumentException 
	 */
	public ArchQueryResult queryFile(ArchQryParam qryParam) throws IOException, DocumentException  {
		
		ArchQueryResult  result = new ArchQueryResult();
		try{
			ArchQryParamBody param  = qryParam.getArchQryParamBody();
			if(param==null){
				result.setResponse(Response.getErrorInstance("???????????????"));
				return result;
			}
			String patiCode = "";
			String pvCode = "";
			String patiname = "";
			ArchQryParamBody paramBody = qryParam.getArchQryParamBody();
			if(paramBody==null){
				result.setResponse(Response.getErrorInstance("???????????????"));
				return result;
			}
			if(CommonUtils.isEmptyString(param.getVisitcode())){
				result.setResponse(Response.getErrorInstance("??????visitcode?????????"));
				return result;
			}
			pvCode =param.getVisitcode();
			if(CommonUtils.isEmptyString(param.getPaticode())){
				result.setResponse(Response.getErrorInstance("??????paticode?????????"));
				return result;
			}
			patiCode =param.getPaticode();
			
			if(CommonUtils.isEmptyString(param.getPatiname())){
				result.setResponse(Response.getErrorInstance("??????patiname?????????"));
				return result;
			}
			patiname =param.getPatiname();
			String start = param.getDatestart();
			String end = param.getDateend();
			List<Fileinfo> fileinfos = new ArrayList<Fileinfo>();
			List<PvArchLog> filelogs = new ArrayList<PvArchLog>();
			
			if(checkDate(start) && checkDate(end)){
				String sql = "select * from pv_arch_log where paticode= ? and code_pv = ? and patiname = ?";
				sql += " and ts > ? and ts < ?";
				List<PvArchLog> logsStart = DataBaseHelper.queryForList(sql, PvArchLog.class, patiCode,pvCode,patiname,start + " 00:00:00",end + " 23:59:59");
				if(logsStart!=null && logsStart.size()>0){
					filelogs.addAll(logsStart);
				}
			}else{
				String sql = "select * from pv_arch_log where paticode= ? and code_pv = ? and patiname = ?";
				filelogs = DataBaseHelper.queryForList(sql, PvArchLog.class, patiCode,pvCode,patiname);
			}
			
			if(filelogs!=null && filelogs.size()>0){
				List<PvArchLog> sortFile = sortByDate(filelogs);//2018-11-01 ??????????????????
				for(PvArchLog log : sortFile){
					Fileinfo info = new Fileinfo();
					info.setDate(log.getDaterpt());
					info.setDept(log.getDept());
					info.setDeptr(log.getDetprpt());
					info.setDoctype(log.getDoctype());
					info.setFilename(log.getFileName());
					info.setFilepath(log.getFilePath());
					info.setMemo(log.getItemName());
					fileinfos.add(info);
				}
			}
			ArchResultBody body = new ArchResultBody();
			body.setFileinfos(fileinfos);
			result.setArchResultBody(body);
			result.setResponse(Response.getInstance());	
			return result;
		}catch(Exception e){
			result.setResponse(Response.getErrorInstance("??????????????????"));
			return result;
		}
	}

	/**
	 * ???????????????????????????
	 * @param list
	 * @return
	 * @throws ParseException
	 */
	public List<PvArchLog> sortByDate(List<PvArchLog> list){
		//1.??????????????????tempmap????????????Map<key,Map<String,Object>>???key???????????????
		List<PvArchLog> rs = new ArrayList<PvArchLog>();
		Map<String,PvArchLog> tempmap = new HashMap<String,PvArchLog>();
		String fileName = "";
		Date dateRpt = null;
		//2.??????list
		for(PvArchLog arch : list){
			//2.1???????????????????????????
			fileName = arch.getFileName();
			dateRpt = arch.getDateUpload();
			//2.2??????tempmap?????????????????????????????????
			PvArchLog file = tempmap.get(fileName);
			if(null == file){
				tempmap.put(fileName, arch);
			}else{
				//2.3 ???file?????????????????????????????????????????????
				Date dateRpt2 = file.getDateUpload();
				//2.4????????????????????????????????????file??????????????????????????????
				if(dateRpt.after(dateRpt2))
					tempmap.put(fileName, arch);
			}
		}
		Iterator<Entry<String, PvArchLog>> iterator = tempmap.entrySet().iterator();
		while (iterator.hasNext()) {
			Entry<String, PvArchLog> entry = iterator.next();
			rs.add(entry.getValue()) ;
		}
		return rs;
	}
	
	private boolean checkDate(String date) {
	   
		if(CommonUtils.isEmptyString(date)){
			return false;
		}
		if(date.trim().length()>10){
			return false;
		}
		SimpleDateFormat sdf =new SimpleDateFormat("yyyy-MM-dd");

		try {
			sdf.parse(date);
			return  true;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}

	/**
	 *  ????????????
	 * 	    1?????????????????????????????????
			2???	???????????????????????????????????????????????????????????????+?????????????????????
	 * @param qryParam
	 * @return
	 * @throws IOException 
	 * @throws DocumentException 
	 */
	public List<Map<String,Object>> queryFileByPkPv(String pkPv) throws IOException, DocumentException  {
		
		PvEncounter pv = DataBaseHelper.queryForBean("select pv.*,pi.code_pi  from pv_encounter pv inner join pi_master pi on pi.pk_pi = pv.pk_pi where pv.pk_pv = ? ", PvEncounter.class, pkPv);
		List<Map<String,Object>> res = new ArrayList<Map<String,Object>>();
		
		Document document = null;
		InputStream ins = null;
		String paticode = pv.getCodePi();
		StringBuilder sbDir = new StringBuilder(ArchUtils.BASE_DIR);
		sbDir.append(ArchUtils.SEPARATOR).append(ArchUtils.get1stDir(paticode)).append(ArchUtils.SEPARATOR).append(ArchUtils.patidHandle(paticode)).append(ArchUtils.SEPARATOR).append(pv.getCodePv()+"_1");
		
		FTPUtil.connect();
		FTPUtil.ftpClient.enterLocalPassiveMode();
		FTPFile[] files = FTPUtil.ftpClient.listFiles(sbDir.toString());
		for(FTPFile file : files){
			if(file.getName().contains(".xml") && !file.getName().contains("_ca.xml") ){
				FTPUtil.connect();
				FTPUtil.ftpClient.enterLocalPassiveMode();
				SAXReader saxreader = new SAXReader();
				saxreader.setEncoding("GB2312");
				ins = FTPUtil.ftpClient.retrieveFileStream(sbDir.toString()+ArchUtils.SEPARATOR+file.getName());
				document = saxreader.read(ins);
				Element stuElem=document.getRootElement().element("head");
				Map<String,Object> fileMap = new HashMap<String,Object>();
				
				fileMap.put("path", stuElem.elementText("path"));
				fileMap.put("fileName", stuElem.elementText("docname"));
				fileMap.put("sysname", stuElem.elementText("sysname"));
				fileMap.put("status", stuElem.elementText("status"));
				fileMap.put("date", stuElem.elementText("date"));
				fileMap.put("codeDoctype", stuElem.elementText("doctype"));
				fileMap.put("memo", stuElem.elementText("memo"));
				BdArchDoctype type = DataBaseHelper.queryForBean("select * from bd_arch_doctype where code_doctype = ?", BdArchDoctype.class, stuElem.elementText("doctype"));
				fileMap.put("nameDoctype", type==null?null:type.getNameDoctype());
				
				res.add(fileMap);
				ins.close();
			}
		}
		return res;
	}
	
	
	private List<Fileinfo> qryFileList(String paticode,String codePv,boolean isNew){

		List<Fileinfo> fileinfos = new ArrayList<Fileinfo>();
		
		 Document document = null;
		 InputStream ins = null;
		    
		  StringBuilder sbDir = new StringBuilder();
		  if(isNew){
			  sbDir.append(ArchUtils.BASE_DIR);
			  sbDir.append(ArchUtils.SEPARATOR);
			  sbDir.append(ArchUtils.get1stDir(paticode));
		  }else{
			  sbDir.append(ArchUtils.BASE_DIR_OLD);
			  sbDir.append(ArchUtils.SEPARATOR);
			  sbDir.append(ArchUtils.get1stDirOld(paticode));
		  }
		  sbDir.append(ArchUtils.SEPARATOR).append(paticode).append(ArchUtils.SEPARATOR).append(codePv);
		try{
	        FTPUtil.connect();
	        FTPUtil.ftpClient.enterLocalPassiveMode();
			FTPFile[] files = FTPUtil.ftpClient.listFiles(sbDir.toString());
			
			if(files==null || files.length<1){
				return null;
			}
			
			for(FTPFile file : files){
				if(file.getName().contains(".xml") && !file.getName().contains("CA_") ){
					FTPUtil.connect();
			        FTPUtil.ftpClient.enterLocalPassiveMode();
				    SAXReader saxreader = new SAXReader();
				    saxreader.setEncoding("GB2312");
		            ins = FTPUtil.ftpClient.retrieveFileStream(sbDir.toString()+ArchUtils.SEPARATOR+file.getName());
		            document = saxreader.read(ins);
		            Element stuElem=document.getRootElement().element("head");
		            String path = stuElem.elementText("path");
		            String filename = stuElem.elementText("docname");
		            String doctype = stuElem.elementText("doctype");
		            String date = stuElem.elementText("date");
		            String memo = stuElem.elementText("memo");
		            String dept = stuElem.elementText("dept");
		            String deptr = stuElem.elementText("deptr");
		            Fileinfo info =  new Fileinfo();
		            info.setFilepath(path);
		            info.setDept(dept);
		            info.setDate(date);
		            info.setMemo(memo);
		            info.setFilename(filename);
		            info.setDeptr(deptr);
		            info.setDoctype(doctype);
		            fileinfos.add(info);
		            ins.close();
				}
			}
		}catch(Exception e){
			return null;
		}
		return fileinfos;
	}
	
	public Date dateTrans(String date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date temp = null;
		if(!CommonUtils.isEmptyString(date)){
			try {
				temp = sdf.parse(date);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		return temp;
	}

	public ArchResult queryFileStatus(ArchStatusParam param)  {
		String status = null;
		ArchResult result = new ArchResult();
		Response res = null;
		try{
			if(param!=null && !CommonUtils.isEmptyString(param.getVisitcode())){
				String pvCode = param.getVisitcode();
				 PvEncounter pv = DataBaseHelper.queryForBean("select pv.*,pi.code_pi from pv_encounter pv inner join pi_master pi on pi.pk_pi = pv.pk_pi where pv.code_pv = ?", PvEncounter.class, pvCode);
				 Document document = null;
				 InputStream ins = null;
				 String paticode = pv.getCodePi();
				 StringBuilder sbDir = new StringBuilder(ArchUtils.BASE_DIR);
				 sbDir.append(ArchUtils.SEPARATOR).append(ArchUtils.get1stDir(paticode)).append(ArchUtils.SEPARATOR).append(paticode).append(ArchUtils.SEPARATOR).append(pv.getCodePv());
				 FTPUtil.connect();
			     FTPUtil.ftpClient.enterLocalPassiveMode();
			     FTPFile[] files = FTPUtil.ftpClient.listFiles(sbDir.toString());
				 for(FTPFile file : files){
					 String temp = file.getName();
					 if(file.getName().contains(param.getFilename()) && file.getName().contains(".xml") ){
						 FTPUtil.connect();
					        FTPUtil.ftpClient.enterLocalPassiveMode();
						    SAXReader saxreader = new SAXReader();
						    saxreader.setEncoding("GB2312");
				            ins = FTPUtil.ftpClient.retrieveFileStream(sbDir.toString()+ArchUtils.SEPARATOR+file.getName());
				            document = saxreader.read(ins);
				            Element stuElem=document.getRootElement().element("head");
				            status = stuElem.elementText("status");
				            break;
					 }
				 }
				  res = Response.getInstance();
				  result.setResponse(res);
				  ArchResultBody body = new ArchResultBody();
				  body.setStatus(status);
				  result.setArchResultBody(body);
			}else{
				res = Response.getErrorInstance("???????????????????????????????????????");
			}
		}catch (IOException e){
			res = Response.getErrorInstance("ftp???????????????");
		}catch(DocumentException e){
			res = Response.getErrorInstance("xml?????????????????????");
		}
		
		result.setResponse(res);
		return result;
	}
	
	
	/**
	 * ????????????????????????????????????????????????ftp????????????????????????????????????????????????
	 * @param vo
	 */
	public void insert4MZ(UploadHead vo,int version){
		String base = "U:\\ftp_mz";
		
		///archive/6992000~6994000/6993061/6993061_1
		//U:\ftp_mz\Archive\2015-07-15\PACS_RP_20150715822442.jpg
		String fileName = vo.getDocname();
		if(fileName.contains(".")){
			fileName = fileName.substring(0,fileName.lastIndexOf("."));
		}
		String filePath = vo.getPath().replace("/", "\\")+"\\"+fileName;
		String newName = base+"\\"+filePath+".pdf";
		//1.????????????????????????????????????????????????????????????????????????????????????
				int cnt = DataBaseHelper.queryForScalar("select count(1) cnt  from HSTB_ARCHIVE_INDEX where  RID = ? ", Integer.class, vo.getRid());
				if(cnt>0){
					DataBaseHelper.execute("update HSTB_ARCHIVE_INDEX set UPDATE_TIMES = UPDATE_TIMES+1 where  RID = ? ", vo.getRid());
					return;
				}
		HstbArchiveIndex  index = new HstbArchiveIndex();
		
		index.setFilepathCa(" ");
		index.setFilepathReport(newName);
		index.setFilepathIndex(base+"\\"+filePath+".xml");
		//index.setHaiuuid(haiuuid);
		index.setRtype(vo.getSysname());
		index.setItemName(vo.getMemo());
		index.setPage(vo.getAge());
		index.setRid(vo.getRid());
		index.setRserial(vo.getRserial());
		index.setPatid(vo.getPaticode());
		index.setPname(vo.getPatiname());
		index.setPatdept(vo.getDept());
		index.setPsex(vo.getSex());
		index.setPatflag(getPATFLAG(vo.getVisittype()));
		index.setTimeAddtion(new Date());
		index.setUpdateTime(new Date());
		index.setUpdateTimes(version);
		index.setStatus(9);
		index.setHandleHpuuid(0L);
        index.setHandleTime(new Date());		
		
        DataBaseHelper.insertBean(index);
        DataSourceRoute.putAppId(null);
	}

	/**
	 * ?????????????????????
	 * @param fileInfo
	 */
	public String insertLog(ArchUploadParam fileInfo,long time) {
		
		
		String oldname = null;
		int version  = 1;
		UploadHead head = fileInfo.getUploadHead();
		int cnt = DataBaseHelper.queryForScalar("select count(0) cnt from pv_arch_log where file_name = ? and paticode = ?", Integer.class, head.getDocname(),head.getPaticode()) ;
		PvArchLog log  =  new PvArchLog();
		log.setCodePv(head.getVisitcode());
		log.setDateUpload(new Date());
		String docName = head.getDocname();
		if(docName.contains(".")){
			docName = docName.substring(0,docName.lastIndexOf("."))+".pdf";
			
		}
		head.setDocname(docName);
		log.setFileName(head.getDocname());
		log.setFilePath(head.getPath());
		log.setFileType(head.getDoctype());
		log.setNameEmpOper(head.getUser());
		log.setItemName(head.getMemo());
		
		//log.setPaticode(head.getPaticode());
		log.setPatiname(head.getPatiname());
		log.setPkOrg("~");
		log.setPkLog(NHISUUID.getKeyId());
		log.setTs(new Date());
		log.setPvtype(head.getVisittype());
		log.setDetprpt(head.getDeptr());
		log.setPaticode(head.getPaticode());
		
		if(cnt>0){
			//????????????????????????????????????,?????????????????????
			int ver = DataBaseHelper.queryForScalar("select Max(version) ver from pv_arch_log where file_name = ?", Integer.class, head.getDocname());
		     version= ver+1;
			oldname = FTPUtil.getFtpHistFileName(head.getPath()+ArchUtils.SEPARATOR+head.getDocname(), version+"");
			log.setOldFile(oldname);
			log.setOldPath("/history");//????????????????????????????????????/history
			log.setVersion(version+"");
			
		}else{
			//???????????????????????????
			log.setOldPath(null);
			log.setOldFile(null);
			log.setVersion("1");
		}
		log.setAge(head.getAge());
		log.setSysName(head.getSysname());
		log.setSex(head.getSex());
		log.setDept(head.getDept());
		log.setRid(head.getRid());
		log.setRserial(head.getRserial());
		log.setIptimes(head.getIptimes());
		log.setDoctype(head.getDoctype());
		log.setDaterpt(head.getDate());
		log.setVisitdate(head.getVisitdate());
		long timeUsed = time/1000;
		log.setTimeused(timeUsed+"");
		DataBaseHelper.insertBean(log);
		
		//????????????????????????
		//2020-02-26??????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
		//@todo ?????????
		/*
		if(head.getVisittype()!=null&&head.getVisittype().equals("3")){
			if(head.getDoctype()!=null){
				String sql;
				if(head.getDoctype().equals("0016")){
					sql="update ex_ris_occ set code_rpt = ? where pk_pv in (select pk_pv from pv_encounter where code_pv = ?) and code_apply = ?";
					
					//??????
					DataBaseHelper.execute(sql, head.getRserial(),head.getVisitcode(),head.getRid());
				}else if (head.getDoctype().equals("0017")){
					//??????
					sql="update ex_lab_occ set code_rpt = ? where pk_pv in (select pk_pv from pv_encounter where code_pv = ?) and code_apply = ?";
					
					DataBaseHelper.execute(sql, head.getRserial(),head.getVisitcode(),head.getRid());
				}
			}
		}
		*/
		return version+"";
	}
	
	
	/**
	 * ??????????????????
	 * @param fileInfo
	 */
	public void insertErrorLog(String errMsg,String xml) {
		PvArchErrorlog log = new PvArchErrorlog();
		log.setErrReson(errMsg);
		if(xml!=null &&xml.trim().length()>4000){
			xml = xml.trim().substring(0,3998);
		}else{
			xml = xml.trim();
		}
		log.setXmlInfo(xml);
		log.setPkOrg("~");
		log.setTs(new Date());
		log.setFlagDel("0");
		log.setPkErrLog(NHISUUID.getKeyId());
		DataBaseHelper.insertBean(log);
		
	}
	
	/**
	 * ??????????????????
	 * @param fileInfo
	 */
	public void insertErrorLog(String errMsg,String xml,ArchUploadParam param) {
		PvArchErrorlog log = new PvArchErrorlog();
		if(param!=null && param.getUploadHead()!=null){
			UploadHead head = param.getUploadHead();
			log.setDept(head.getDept());
			log.setPaticode(head.getPaticode());
			log.setPatiname(head.getPatiname());
			log.setVisitcode(head.getVisitcode());
			log.setFileName(head.getDocname());
			log.setIptimes(head.getIptimes());
			log.setDeptrpt(head.getDeptr());
			log.setPvtype(head.getVisittype());
			log.setNameEmpOper(head.getUser());
		}
		log.setErrReson(errMsg);
		if(xml!=null &&xml.trim().length()>4000){
			xml = xml.trim().substring(0,3998);
		}else{
			xml = xml.trim();
		}
		log.setXmlInfo(xml);
		log.setPkOrg("~");
		log.setTs(new Date());
		log.setFlagDel("0");
		log.setPkErrLog(NHISUUID.getKeyId());
		DataBaseHelper.insertBean(log);
		
	}
	
	private String getPATFLAG(String visittype){
		String res = "";
		switch (visittype) {
			case "1" : res = "??????"; break;
			case "2" : res = "??????";break;
			case "3" : res = "??????";break;
			case "4" : res = "??????";break;
			default : res = "??????";break;
		}
		return res;
	}

	public void fromTo() throws IOException, DocumentException {
		
		List<String> files = new ArrayList<String>();
		
		String sql =""; //"select * from PV_ARCH_LOG where CHARINDEX(paticode,file_path) =0 and FILE_NAME like '%LIS%'  and LEN(CODE_PV)>4";
		//sql+= " and file_name not in ('LIS_RP_DXI80220180203949439.pdf','LIS_RP_OTT201802035.pdf','LIS_RP_sk2018020349.pdf','LIS_RP_OTT2018020310.pdf','LIS_RP_STA-R2018020374.pdf','LIS_RP_LJL2018020345.pdf','LIS_RP_OTT201802038.pdf','LIS_RP_1235201802026.pdf','LIS_RP_AU580020180204952036.pdf','LIS_RP_LJL2018020350.pdf','LIS_RP_LJL2018020351.pdf','LIS_RP_OTT201802036.pdf','LIS_RP_OTT201802037.pdf','LIS_RP_OTT201802039.pdf')";
		sql = "select * from pv_arch_log where pk_log = 'f5027b1048804778a90c81f6efa2631b'";
		List<PvArchLog> logs = DataBaseHelper.queryForList(sql, PvArchLog.class);
		long start = System.currentTimeMillis();
		for(int i =0;i<logs.size();i++){
			PvArchLog log = logs.get(i);
		
			String fileName = log.getFileName();
			String oldPath = log.getFilePath();
			
			String paticode = log.getPaticode();
			String pvcode = ArchUtils.patidHandle(log.getCodePv());
			
			StringBuilder sbDir = new StringBuilder(ArchUtils.BASE_DIR);
			sbDir.append(ArchUtils.SEPARATOR).append(ArchUtils.get1stDir(paticode)).
			append(ArchUtils.SEPARATOR).append(paticode).
			append(ArchUtils.SEPARATOR).append(pvcode);
			FTPUtil.connect();
			FTPUtil.ftpClient.enterLocalPassiveMode();
			FTPUtil.createDirectory(sbDir.toString());
			
			
			FTPUtil.moveFile(fileName, oldPath, sbDir.toString());
			FTPUtil.removeFile(oldPath+"/"+fileName);
			
			
			String xmlName = fileName.replace(".pdf", ".xml");
			editXMl(xmlName, oldPath, sbDir.toString());
			
			
			String caName = xmlName.replace("_RP_", "_CA_");
			FTPUtil.moveFile(caName, oldPath, sbDir.toString());
			FTPUtil.removeFile(oldPath+"/"+caName);
			
			
			DataBaseHelper.execute(" update pv_arch_log set file_path = ? where pk_log= ? ", sbDir.toString(),log.getPkLog());

			System.out.println("????????????"+log.getPatiname()+"____"+i+"???");	
		}
		long end = System.currentTimeMillis();
		long e = (end -start)/1000;
		System.out.println("??????????????????,??????"+e+"???");
		
	}
	
	public void deleteLog(String fileName){
		DataBaseHelper.execute("update  PV_ARCH_ERRORLOG  set flag_del = '1' where file_name like ? or xml_info like ? ", new Object[]{fileName, fileName});
	}
	public static void editXMl(String file,String oldPath,String newpath) throws IOException, DocumentException{
		 Document document = null;
	        InputStream ins = null;
		FTPUtil.connect();
		FTPUtil.ftpClient.enterLocalPassiveMode();
	    SAXReader saxreader = new SAXReader();
	    saxreader.setEncoding("GB2312");
        ins = FTPUtil.ftpClient.retrieveFileStream(oldPath+"/"+file);
        document = saxreader.read(ins);
        Element stuElem=document.getRootElement().element("head").element("path");
        stuElem.setText(newpath);
       try {
    	   //??????????????????
            FTPUtil.ftpClient.deleteFile(oldPath+"/"+file);
            //???????????????
            String temp11 = document.asXML().toString();
            FTPUtil.uploadFileNew(document.asXML().toString().getBytes(), newpath+"/"+file, null);
          
       }catch (Exception e){
    	 
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
	         uploadFileByPvtype(this.fileInfo, this.pvtype, this.start); 
	        return true;  
	    }  
	}
	
	/**
	 * ????????????????????????
	 * @param param
	 * @return
	 */
	public String pushMesByUrl(String param){
		
		String URL = ApplicationUtils.getPropertyValue("upload.push.url", "http://192.168.200.32:8087/zswebinterface/itf/push/pushWechatMessage");
		PrintWriter out = null;
		BufferedReader in = null;
		String result = "";
		try {
			URL url = new URL(URL);
			// ?????????URL???????????????
			URLConnection conn = url.openConnection();
			// ???????????????????????????
			conn.setRequestProperty("accept", "*/*");
			conn.setRequestProperty("connection", "Keep-Alive");
			conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			conn.setConnectTimeout(2000);  
			conn.setReadTimeout(2000);  
			// ??????POST??????????????????????????????
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setRequestProperty("content-type", "application/x-www-form-urlencoded;charset=UTF-8");
			// ??????URLConnection????????????????????????
			out = new PrintWriter(new OutputStreamWriter(conn.getOutputStream(), "UTF-8"));//??????????????????????????????
			// ??????????????????
			out.print(param);
			// flush??????????????????
			out.flush();
			// ??????BufferedReader??????????????????URL?????????
			in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));//??????????????????????????????
			String line;
			while ((line = in.readLine()) != null) {
				result += line;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "HTTP???????????????????????????" + e.toString();
		}finally{
			//??????finally?????????????????????????????????
			try{
				if(out!=null){
					out.close();
				}
				if(in!=null){
					in.close();
				}
			}catch(IOException ex){
				ex.printStackTrace();
				return "IO??????????????????????????????" + ex.toString();
			}
		}
		return result;
	}
	
	/**
	 * ???????????????????????????????????????????????????
	 * @param uploadParam
	 * @return
	 */
	public String setPushParam(ArchUploadParam uploadParam){
		//1????????????????????????????????????
		if(uploadParam == null || uploadParam.getUploadHead() == null)
			return null;
		String dept = uploadParam.getUploadHead().getDept();//????????????
		String itemName = uploadParam.getUploadHead().getMemo();//????????????
		String doctype = uploadParam.getUploadHead().getDoctype();//????????????
		String patiId = uploadParam.getUploadHead().getPaticode();//??????ID
		String patiName = uploadParam.getUploadHead().getPatiname();//????????????
		if(CommonUtils.isEmptyString(patiId) || CommonUtils.isEmptyString(patiName)	
				|| CommonUtils.isEmptyString(doctype) || CommonUtils.isEmptyString(itemName)		
				|| CommonUtils.isEmptyString(dept))	
			return null;
		
		//2????????????????????? : ???????????????????????????|????????????????????????????????????
		String PV_TYPE = ApplicationUtils.getPropertyValue("upload.push.pvtype", "");//?????????????????????
		if(CommonUtils.isEmptyString(uploadParam.getUploadHead().getVisittype()) ||
			(!CommonUtils.isEmptyString(uploadParam.getUploadHead().getVisittype()) 
				&& !CommonUtils.isEmptyString(PV_TYPE) 
				&& !PV_TYPE.contains(uploadParam.getUploadHead().getVisittype())))
			return null;
		
		//3????????????????????? ??? ???????????????|??????
		String doctypeLis = ApplicationUtils.getPropertyValue("upload.push.lis", "");//????????????????????????
		String doctypeRis = ApplicationUtils.getPropertyValue("upload.push.ris", "");//????????????????????????
		if(doctypeRis.equals(doctype))
			doctype = "??????";
		else if(doctypeLis.equals(doctype))
			doctype = "??????";
		else
			return null;
		
		//4??????????????? ????????????????????????????????????
		String deptConf = ApplicationUtils.getPropertyValue("upload.push.dept", "");//?????????????????????????????????
		if(ChkDataFromConfig(deptConf, dept))
			return null;
		
		//5????????????????????? ??? ?????????????????????????????????????????????
		String deptprtConf = ApplicationUtils.getPropertyValue("upload.push.deptprt", "");//????????????????????? ????????????
		if(CommonUtils.isEmptyString(uploadParam.getUploadHead().getDeptr())  
				||( !CommonUtils.isEmptyString(uploadParam.getUploadHead().getDeptr()) 
						&& ChkDataFromConfig(deptprtConf, uploadParam.getUploadHead().getDeptr()) ))
			return null;
		
		//5????????????????????? ??? ???????????????????????????????????????
		String itemConf = ApplicationUtils.getPropertyValue("upload.push.itemname", "");//?????????????????????
		if(!CommonUtils.isEmptyString(itemConf) && ChkDataFromConfig(itemConf, itemName))
			return null;
		//6?????????????????????????????????????????????
		dept = uploadParam.getUploadHead().getDeptr();//????????????
		String dateUpload = DateUtils.getDate("yyyy-MM-dd HH:mm:ss");
		String rptTime = uploadParam.getUploadHead().getVisitdate();//????????????
		StringBuffer strBuf = new StringBuffer();
		strBuf.append("type=5");
		strBuf.append("&patientId=" + patiId);
		strBuf.append("&patientName=" + patiName);
		strBuf.append("&dateTime=" + dateUpload);
//		strBuf.append("&first=" + patiName +"@@"+ patiId +"@@"+ doctype+"@@"+ itemName);
//		strBuf.append("&keyword=" + itemName+"@@"+ dateUpload);
//		strBuf.append("&uniqueValue="+patiId+"@@"+ itemName +"@@"+ dateUpload +"@@"+ dept +"@@"+ doctype);
		String first = patiName +"@@"+ patiId +"@@"+ doctype+"@@"+ itemName;
		String keyword = itemName+"@@"+ dateUpload;
		String uniqueValue = patiId+"@@"+ itemName +"@@"+ rptTime +"@@"+ dept +"@@"+ doctype;
		try {
			strBuf.append("&first=" + URLEncoder.encode(first, "UTF-8") );
			strBuf.append("&keyword=" + URLEncoder.encode(keyword, "UTF-8") );
			strBuf.append("&uniqueValue=" + URLEncoder.encode(uniqueValue, "UTF-8") );
		} catch (UnsupportedEncodingException e) {
			strBuf.append("&first=" + first);
			strBuf.append("&keyword=" + keyword);
			strBuf.append("&uniqueValue=" + uniqueValue);
			e.printStackTrace();
		}
		return strBuf.toString();
	}

	/**
	 * ?????????????????????????????????
	 * @param strCof
	 * @param testStr
	 * @return
	 */
	private boolean ChkDataFromConfig(String strCof, String testStr) {
		if(CommonUtils.isEmptyString(strCof))
			return false;
		List<String> strs = java.util.Arrays.asList(strCof.split(",")); 
		if(strs == null || strs.size() < 1) 
			return false;
			
		for (String str : strs) {
			if(testStr.contains(str))
				return true;
		}
		return false;
	}
	
}
	



