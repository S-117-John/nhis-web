package com.zebone.nhis.arch.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.net.ftp.FTPFile;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.springframework.stereotype.Service;

import com.zebone.nhis.arch.vo.ArchiveDto;
import com.zebone.nhis.common.module.arch.PvArchApply;
import com.zebone.nhis.common.module.arch.PvArchive;
import com.zebone.nhis.common.module.pv.PvEncounter;
import com.zebone.nhis.common.support.ArchUtils;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.common.support.FTPUtil;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.utils.JsonUtil;


/**
 * 归档管理申请业务处理
 * @author Roger
 *
 */
@Service
public class ArchApplyService {
	


	/**
	 * 查询申请业务数据
	 * @param param
	 * @param user
	 */
	public List<Map<String,Object>> qryApplyData(String param, IUser user) {
		
		
		ArchiveDto para = JsonUtil.readValue(param, ArchiveDto.class);
		
		StringBuilder sbSql = new StringBuilder("select ap.pk_apply,  ap.pk_archive, pv.code_pv, pi.name_pi,  pi.code_pi, ap.eu_type, ");
		sbSql.append(" ap.pk_dept,  ap.pk_emp_ap, ap.name_emp_ap, ap.date_ap,  ap.note_ap,");
		sbSql.append(" ap.eu_status, ap.pk_emp_resp, ap.name_emp_resp,ap.date_resp");
		sbSql.append(" from pv_arch_apply ap inner join pv_archive arch on ap.pk_archive=arch.pk_archive");
		sbSql.append(" inner join pv_encounter pv on arch.pk_pv=pv.pk_pv");
		sbSql.append(" inner join pi_master pi on pv.pk_pi=pi.pk_pi where 1=1 ");

		List args = new LinkedList();
		if(!CommonUtils.isEmptyString(para.getCodePv())){
			sbSql.append(" and pv.code_pv = ?");
			args.add(para.getCodePv());
		}else if(!CommonUtils.isEmptyString(para.getNamePi())){
			sbSql.append(" and pi.name_pi like ?");
			para.setNamePi("%"+para.getNamePi()+"%");
			args.add(para.getNamePi());
		}else if(!CommonUtils.isEmptyString(para.getPkDept())){
			sbSql.append(" and ap.pk_dept = ?");
			args.add(para.getPkDept());
		}else if(!CommonUtils.isEmptyString(para.getNameEmpAp())){
			sbSql.append(" and  ap.name_emp_ap  like ?");
			para.setNamePi("%"+para.getNameEmpAp()+"%");
			args.add(para.getNameEmpAp());
		}else if(!CommonUtils.isEmptyString(para.getEuStatus())){
			sbSql.append(" and ap.eu_status = ?");
			args.add(para.getEuStatus());
		}else if(para.getDateBegin()!=null){
			sbSql.append(" and ap.date_ap > ?");
			args.add(para.getDateBegin());
		}else if(para.getDateEnd()!=null){
			sbSql.append(" and ap.date_ap < ?");
			args.add(para.getDateEnd());
		}
		
		return DataBaseHelper.queryForList(sbSql.toString(), args.toArray());
	}
	
	/**
	 * 批准解除归档申请
	 * @param param
	 * @param user
	 * @throws IOException 
	 * @throws DocumentException 
	 */
	public void approveApply(String param, IUser user) throws IOException, DocumentException {
		 Map<String,Object> para = JsonUtil.readValue(param, java.util.HashMap.class);
		 String sql = " update pv_arch_apply set eu_status = '9', pk_emp_resp = ? ,name_emp_resp = ? date_resp = ? where pk_apply = ? and eu_status = '0'";
		 
		 DataBaseHelper.execute(sql, UserContext.getUser().getPkEmp(),UserContext.getUser().getNameEmp(),new Date(),para.get("pkApply"));
		 
		 PvArchApply app = DataBaseHelper.queryForBean("select * from pv_arch_apply where pk_apply=?", PvArchApply.class, para.get("pkApply"));

            // archiveXml((String)para.get("pkArchive"),"0");

	}

	/**
	 * 医生提交/取消提交
	 * @param pkArchive
	 * @param status
	 * @param pkEmp
	 * @param nameEmp
	 * @throws IOException
	 * @throws DocumentException
	 */
	     public void archiveXml(String pkArchive,String status,String pkEmp,String nameEmp)
			throws IOException, DocumentException {
	    	 
	    	
	    	      
	    	 
		//		2）	解除/归档；
	    	 String sql = "";
	    	 
	    	 if("1".equals(status)){
				  sql = " update pv_archive arch  set arch.eu_archtype=?,arch.flag_submit='1', arch.date_submit=?, arch.pk_emp_submit=?,arch.name_emp_submit=?,arch.eu_status='1'   where arch.pk_archive=?";
				  DataBaseHelper.execute(sql, "0", new Date(),pkEmp,nameEmp,pkArchive);
	    	 }else{

				  sql = "update pv_archive arch set arch.flag_submit='0',arch.date_submit=null,arch.pk_emp_submit=null, arch.name_emp_submit=null,arch.eu_status='0'  where arch.pk_archive=? and  arch.eu_status='1'";
				  DataBaseHelper.execute(sql,  pkArchive);
	    	 }
		         PvArchive archive = DataBaseHelper.queryForBean("select * from pv_archive where pk_archive = ?", PvArchive.class, pkArchive);
				
		         
					PvEncounter pv = DataBaseHelper.queryForBean(" select pv.* ,pi.code_pi from pv_encounter pv inner join pi_master pi on pi.pk_pi = pv.pk_pi where pv.pk_pv = ?", PvEncounter.class, archive.getPkPv());
				
					String paticode = pv.getCodePi();
					String fileDir = ArchUtils.BASE_DIR+"/"+ArchUtils.get1stDir(paticode)+"/"+paticode+"/"+pv.getCodePv();
					FTPUtil.connect();
					FTPUtil.ftpClient.enterLocalPassiveMode();
					FTPFile[] files = FTPUtil.ftpClient.listFiles(fileDir);
					 Document document = null;
				        InputStream ins = null;
					for(FTPFile file : files){
						String temp = file.getName();
						if(file.getName().contains(".xml")){
							FTPUtil.connect();
							FTPUtil.ftpClient.enterLocalPassiveMode();
						    SAXReader saxreader = new SAXReader();
						    saxreader.setEncoding("GB2312");
				            ins = FTPUtil.ftpClient.retrieveFileStream(fileDir+"/"+file.getName());
				            document = saxreader.read(ins);
				            Element stuElem=document.getRootElement().element("head").element("status");
				           if("1".equals(status)){
				        	   stuElem.setText("0");
				           }else{
				        	   stuElem.setText("1");
				           }
				            
				            OutputFormat format = OutputFormat.createPrettyPrint();
				    		
				           try {
				        	   
				        	   //先删除源文件
		
					            FTPUtil.ftpClient.deleteFile(fileDir+"/"+file.getName());
					            //创建新文件
					            String temp11 = document.asXML().toString();
					            FTPUtil.uploadFileNew(document.asXML().toString().getBytes(), fileDir+"/"+file.getName(), null);
				               // 创建XMLWriter对象
				               XMLWriter writer = new XMLWriter(FTPUtil.downloadFile(fileDir, file.getName()), format);
		
				               //设置不自动进行转义
				               writer.setEscapeText(false);
		
				               // 生成XML文件
				               writer.write(document);
		
				               //关闭XMLWriter对象
				               writer.close();
				           }catch (Exception e){
				        	  // res = Response.getErrorInstance("解除归档失败,发生传输错误!");
				        	   //throw new  BusException("解除归档失败,发生传输错误!");
				           }
				      
						}
					}
	}
	
	/**
	 * 拒绝归档申请
	 * @param param
	 * @param user
	 */
	public void refuseApply(String param, IUser user) {
		 Map<String,Object> para = JsonUtil.readValue(param, java.util.HashMap.class);
		 String sql = "update pv_arch_apply  set eu_status='8', pk_emp_resp=?, name_emp_resp=?, date_resp=? where pk_apply=? and eu_status='0'";
		 
		 DataBaseHelper.execute(sql, UserContext.getUser().getPkEmp(),UserContext.getUser().getNameEmp(),new Date(),para.get("pkApply"));

	}
	
	

	
	
}
