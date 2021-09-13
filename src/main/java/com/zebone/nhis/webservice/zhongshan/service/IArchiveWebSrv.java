package com.zebone.nhis.webservice.zhongshan.service;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.Style;

@WebService
@SOAPBinding(style = Style.RPC)
public interface IArchiveWebSrv {
	
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
	@WebMethod
    public String ArcUPFile(@WebParam(name="dataFile")byte[] dataFile,
    		@WebParam(name="file_name")String file_name, 
    		@WebParam(name="emp_sn")String emp_sn,
    		@WebParam(name="patient_id") String patient_id, 
    		@WebParam(name="times")String times, 
    		@WebParam(name="doc_type")String doc_type, 
    		@WebParam(name="visit_flag") String visit_flag);
}
