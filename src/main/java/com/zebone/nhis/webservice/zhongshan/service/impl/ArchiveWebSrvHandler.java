package com.zebone.nhis.webservice.zhongshan.service.impl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.Style;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.itextpdf.text.pdf.PdfReader;
import com.lowagie.text.Image;
import com.lowagie.text.pdf.PdfWriter;
import com.zebone.nhis.common.arch.service.ArchiveCollectService;
import com.zebone.nhis.common.module.arch.PvArchDoc;
import com.zebone.nhis.common.module.arch.PvArchive;
import com.zebone.nhis.common.module.base.ou.BdOuUser;
import com.zebone.nhis.common.module.pv.PvEncounter;
import com.zebone.nhis.common.support.FTPUtil;
import com.zebone.nhis.webservice.zhongshan.service.IArchiveWebSrv;
import com.zebone.platform.modules.dao.DataBaseHelper;

@WebService
@SOAPBinding(style = Style.RPC)
public class ArchiveWebSrvHandler implements IArchiveWebSrv{


	
	
	private  Logger logger = LogManager.getLogger(IArchiveWebSrv.class.getName());
	

	@Autowired
	private ArchiveCollectService service;
	
	//归档文件目录
	public static final String FTP_BASE_DIR = "upload/archive/";

	//文件加密密码
	public static final String FTP_PWD = null;
	/**
	 * 病历文件上传
	 * @param dataFile   文档内容（PDF文件序列后的二进制流）
	 * @param file_name  归档号（各个系统内保证其唯一）
	 * @param emp_sn	  归档操作者工号
	 * @param patient_id 病人ID
	 * @param times		 就诊次数
	 * @param doc_type	文档类型（如长期医嘱单、入院记录、检查报告、检验报告等）
	 * @param visit_flag 病人类型（ZY：住院）
	 * @return 
	 */
	@Override
    public String ArcUPFile(byte[] dataFile, String file_name, String emp_sn,
	    String patient_id, String times, String doc_type, String visit_flag){
    	
		logger.info("##Upload File Parameters->[file_name:" + file_name
				+ ", emp_sn:" + emp_sn + ", patient_id:" + patient_id
				+ ", times:" + times + ",doc_type:" + doc_type + ",visit_flag:"
				+ visit_flag + ", 字节:" + dataFile.length + "+]");

		boolean scanFlag = false;
		String fromFileName = new String(dataFile);
		
		
		//1.参数校验
		if (fromFileName.startsWith(FTP_BASE_DIR)) {
			scanFlag = true;
		} else if (dataFile == null || dataFile.length < 1024) {
			return "文档[名称:" + file_name + ", 患者ID:" + patient_id + ", 住院次数:"
					+ times + "]内容为空,请重传";
		}

		if (StringUtils.isBlank(file_name)) {
			return "文档名称为空,请检查重传";
		}

		if (StringUtils.isBlank(emp_sn)) {
			return "文档[名称:" + file_name + ", 患者ID:" + patient_id + ", 住院次数:"
					+ visit_flag + "]归档操作者工号为空,请检查重传";
		}

		if (StringUtils.isBlank(patient_id)) {
			return "文档[名称:" + file_name + ", 住院次数:" + visit_flag
					+ "]患者ID为空,请检查重传";
		}

		if (StringUtils.isBlank(times) || !StringUtils.isNumeric(times)
				|| Integer.valueOf(times) < 0) {
			return "文档[名称:" + file_name + ", 患者ID:" + patient_id + ", 住院次数:"
					+ times + "]住院次数不是数字或小于0,请检查重传";
		}
		if (StringUtils.isBlank(doc_type)) {
			return "文档[名称:" + file_name + ", 患者ID:" + patient_id + ", 住院次数:"
					+ visit_flag + "]文档类型为空,请检查重传";
		}

		if (StringUtils.isBlank(visit_flag)
				|| !("1".equals(visit_flag) || "0".equals(visit_flag)||"MZ".equals(visit_flag)||"ZY".equals(visit_flag))) {
			return "文档[名称:" + file_name + ", 患者ID:" + patient_id + ", 门诊住院:"
					+ visit_flag + "]门诊住院不合法,ZY:住院,MZ:门诊,请检查重传";
		}
		if("ZY".equals(visit_flag)){
			visit_flag = "1";
		}else if("MZ".endsWith(visit_flag)){
			visit_flag = "0";
		}
		// 如果是图片格式那么转成PDF
		dataFile = imag2PDF(dataFile,file_name);

		PvEncounter pv = service.getPvEncounter(patient_id,times,visit_flag);
		
		//emp_sn获取pkEmp,nameEmp
		 BdOuUser usr = DataBaseHelper.queryForBean("select * from bd_ou_user  where code_user = ?", BdOuUser.class, emp_sn);
		if(pv==null){
			return "未查到患者就诊记录，请检查所传患者ID以及住院次数";
		}
		//2.归档记录生成
		PvArchive PA = null;
		List<PvArchive> voList = DataBaseHelper.queryForList("select * from pv_archive where pk_pv = ?", PvArchive.class, pv.getPkPv());
		if(voList!=null && voList.size()>0){
			PA = voList.get(0);
		}else{
			PA = service.genPvArchive(pv, usr);			
		}
		PvArchive pavo = DataBaseHelper.queryForBean("select * from pv_archive where pk_Archive =?", PvArchive.class, PA.getPkArchive());


		//3.归档文件上传
			//3.1 生成文档记录表记录		 
		 	PvArchDoc doc = service.genArchDoc(file_name, doc_type, pavo.getPkOrg(), usr, pavo,null,null);
			//3.2 Ftp文件上传
			String position  = FTP_BASE_DIR+file_name;
			FTPUtil.uploadFileNew(dataFile, position, FTP_PWD);
			
			int pages = 0;
			try{
				 PdfReader reader= new PdfReader(position);
				 pages= reader.getNumberOfPages();
			}catch(java.lang.OutOfMemoryError e){
				
			}catch(IOException e){
				
			}
			//3.3 建立数据库索引，更新表pv_arch_doc		 
			 service.updateArchDoc(dataFile.length, usr, doc, position, pages,null);

			 // 成功
			 return "0";
	}




	

	private byte[] imag2PDF(byte[] input,String fileName) {
		com.lowagie.text.Document document = new com.lowagie.text.Document();
		ByteArrayOutputStream target = new ByteArrayOutputStream();
		try {
			PdfWriter.getInstance(document, target);
			document.open();
			Image img = Image.getInstance(input);
			document.addAuthor("EmrArchiveApplication");
			document.addCreationDate();
			document.addCreator("iText library");
			document.addTitle("ScannerImg");
			if(fileName.startsWith("ARC_")){
				img.setWidthPercentage(100);
			}else{
				img.scalePercent(75);
			}
			document.add(img);
		} catch (Exception e) {
			logger.info("##Not Picture Formate," + e.getMessage());
			return input;
		}
		document.close();
		return target.toByteArray();
	}

}
