package com.zebone.nhis.webservice.syx.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zebone.nhis.common.support.XmlUtil;
import com.zebone.nhis.webservice.syx.dao.OpvisitInfoMapper;
import com.zebone.nhis.webservice.syx.vo.platForm.OpdiagInfoResponse;
import com.zebone.nhis.webservice.syx.vo.platForm.OpdiagInfoResult;
import com.zebone.nhis.webservice.syx.vo.platForm.OpdiagInfoVo;
import com.zebone.nhis.webservice.syx.vo.platForm.OpvisitInfoResponse;
import com.zebone.nhis.webservice.syx.vo.platForm.OpvisitInfoResult;
import com.zebone.nhis.webservice.syx.vo.platForm.OpvisitInfoVo;
import com.zebone.nhis.webservice.syx.vo.platForm.OpvisitinfoRequest;


@Service
public class PlatFormOpvisitService {
	
	@Autowired
	private OpvisitInfoMapper opvisitInfoMapper;
	
	SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
	
	/**
	 * 门诊就诊记录信息的查询
	 * @param message
	 * @return
	 */
	public String opvisitinfoQuery(String message) throws Exception{
		OpvisitinfoRequest reqData = (OpvisitinfoRequest) XmlUtil.XmlToBean(message, OpvisitinfoRequest.class);
		
		Map<String,Object> paramMap=new HashMap<String,Object>();
    	   		    		
	    String hospXml = null;
		
			OpvisitInfoResponse opvisitInfoResponse = null;
			
			String startDate = reqData.getVisitDateTime().getStartTime();
			if(startDate != null && startDate.length()!=0){
				Date startTime = formatter.parse(reqData.getVisitDateTime().getStartTime());
				paramMap.put("startTime", startTime);	    				
			}
			String endDate = reqData.getVisitDateTime().getEndtime();		
			if(endDate != null && endDate.length()!=0){							
				Date endTime = formatter.parse(reqData.getVisitDateTime().getEndtime());
				paramMap.put("endTime", endTime);
				
			}				
			paramMap.put("visitType", reqData.getVisitType());
			OpvisitInfoResult reInfoResult = new OpvisitInfoResult();
			try {
									
				List<Map<String, Object>>  opvisitInfoList = opvisitInfoMapper.opvisitinfoQuery(paramMap);					    	
				opvisitInfoResponse = new OpvisitInfoResponse();
				opvisitInfoResponse.setId(UUID.randomUUID().toString());
				Date currentTime = new Date();   	
				String date = formatter.format(currentTime);
				
				opvisitInfoResponse.setCreationTime(date);
				opvisitInfoResponse.setActionId(reqData.getActionId());
				opvisitInfoResponse.setActionName(reqData.getActionName());
				
				
				
				List<OpvisitInfoVo> lists = new ArrayList<>();
				for (Map<String, Object> map : opvisitInfoList) {
					OpvisitInfoVo opvisitInfoVo = new OpvisitInfoVo();
					opvisitInfoVo.setVisitId(map.get("visitId")==null?"":map.get("visitId").toString());
					opvisitInfoVo.setPatientId(map.get("patientId")==null?"":map.get("patientId").toString());
					opvisitInfoVo.setDeptKey(map.get("deptKey")==null?"":map.get("deptKey").toString());
					opvisitInfoVo.setDeptName(map.get("deptName")==null?"":map.get("deptName").toString());
					opvisitInfoVo.setDoctor(map.get("doctor")==null?"":map.get("doctor").toString());
					opvisitInfoVo.setVisitDate(map.get("visitDate")==null?"":map.get("visitDate").toString());
					opvisitInfoVo.setPatientName(map.get("patientName")==null?"":map.get("patientName").toString());
					opvisitInfoVo.setSex(map.get("sex")==null?"":map.get("sex").toString());
					opvisitInfoVo.setBirthday(map.get("birthday")==null?"":map.get("birthday").toString());
					opvisitInfoVo.setAge(map.get("age")==null?"":map.get("age").toString());
					opvisitInfoVo.setClinicNo(map.get("clinicNo")==null?"":map.get("clinicNo").toString());
					opvisitInfoVo.setClinicCardNo(map.get("clinicCardNo")==null?"":map.get("clinicCardNo").toString());
					opvisitInfoVo.setIdNumber(map.get("idNumber")==null?"":map.get("idNumber").toString());
					opvisitInfoVo.setTelphone(map.get("Telphone")==null?"":map.get("Telphone").toString());
					opvisitInfoVo.setMaritalStatus(map.get("maritalStatus")==null?"":map.get("maritalStatus").toString());
					opvisitInfoVo.setVocation(map.get("vocation")==null?"":map.get("vocation").toString());
					opvisitInfoVo.setNationality(map.get("nationality")==null?"":map.get("nationality").toString());
					opvisitInfoVo.setNation(map.get("nation")==null?"":map.get("nation").toString());
					opvisitInfoVo.setWorkUnit(map.get("workUnit")==null?"":map.get("workUnit").toString());
					opvisitInfoVo.setEducation(map.get("education")==null?"":map.get("education").toString());
					opvisitInfoVo.setPresentAddrProvince(map.get("presentAddrProvince")==null?"":map.get("presentAddrProvince").toString());
					opvisitInfoVo.setPresentAddrCity(map.get("presentAddrCity")==null?"":map.get("presentAddrCity").toString());
					opvisitInfoVo.setPresentAddrArea(map.get("presentAddrArea")==null?"":map.get("presentAddrArea").toString());
					opvisitInfoVo.setPresentAddrDetail(map.get("presentAddrDetail")==null?"":map.get("presentAddrDetail").toString());
					opvisitInfoVo.setResideAddrProv(map.get("resideAddrProv")==null?"":map.get("resideAddrProv").toString());
					opvisitInfoVo.setResideAddrCity(map.get("resideAddrCity")==null?"":map.get("resideAddrCity").toString());
					opvisitInfoVo.setResideAddrArea(map.get("resideAddrArea")==null?"":map.get("resideAddrArea").toString());
					opvisitInfoVo.setResideAddrDetail(map.get("resideAddrDetail")==null?"":map.get("resideAddrDetail").toString());
					opvisitInfoVo.setRstatus(map.get("rstatus")==null?"":map.get("rstatus").toString());
					opvisitInfoVo.setDateSource(map.get("dateSource")==null?"":map.get("dateSource").toString());	
					lists.add(opvisitInfoVo);					
				}
				opvisitInfoResponse.setSubject(lists); 
				if(null == opvisitInfoList || opvisitInfoList.size() ==0){
					reInfoResult.setId("AD");
					reInfoResult.setText("未查询到数据");
				}else{
					reInfoResult.setId("AA");
					reInfoResult.setText("处理成功");
				}			
				reInfoResult.setRequestId(reqData.getId());
				reInfoResult.setRequestTime(reqData.getCreationTime());
			
			    
			} catch (Exception e) {						
				reInfoResult.setId("AE");
				reInfoResult.setText("处理失败");	
			}
			   opvisitInfoResponse.setResult(reInfoResult);
		       hospXml = XmlUtil.beanToXml(opvisitInfoResponse, OpvisitInfoResponse.class);

               return hospXml;	
	}
	
	/**
	 * 门诊患者诊断信息的查询
	 * @param message
	 * @return
	 */
	public String opdiaginfoQuery(String message) throws Exception{
		OpvisitinfoRequest reqData = (OpvisitinfoRequest) XmlUtil.XmlToBean(message, OpvisitinfoRequest.class);
				
		Map<String,Object>  paramMap=new HashMap<String,Object>();
		String startDate = reqData.getVisitDateTime().getStartTime();
		if(startDate != null && startDate.length()!=0){
			Date startTime = formatter.parse(reqData.getVisitDateTime().getStartTime());
			paramMap.put("startTime", startTime);	    				
		}
		String endDate = reqData.getVisitDateTime().getEndtime();		
		if(endDate != null && endDate.length()!=0){							
			Date endTime = formatter.parse(reqData.getVisitDateTime().getEndtime());
			paramMap.put("endTime", endTime);
			
		}
		paramMap.put("visitType", reqData.getVisitType());	    			    		
    	
    	OpdiagInfoResponse opdiagInfoResponse = null;
    	OpdiagInfoResult opdiagInfoResult = new OpdiagInfoResult();
		try {
			List<Map<String, Object>>  opdiagInfoVoList = opvisitInfoMapper.opdiaginfoQueryDao(paramMap);    	    	
			opdiagInfoResponse = new OpdiagInfoResponse();   
			
			opdiagInfoResponse.setId(UUID.randomUUID().toString());
			Date currentTime = new Date();   	
			String date = formatter.format(currentTime);
			
			opdiagInfoResponse.setCreationTime(date);
			opdiagInfoResponse.setActionId(reqData.getActionId());
			opdiagInfoResponse.setActionName(reqData.getActionName());
			
			
			List<OpdiagInfoVo> lists = new ArrayList<>();
			for (Map<String, Object> map : opdiagInfoVoList) {
				OpdiagInfoVo opdiagInfoVo = new OpdiagInfoVo();
				opdiagInfoVo.setDiagnosisId(map.get("diagnosisId")==null?"":map.get("diagnosisId").toString());
				opdiagInfoVo.setVisitId(map.get("visitId")==null?"":map.get("visitId").toString());
				opdiagInfoVo.setPatientId(map.get("patientId")==null?"":map.get("patientId").toString());
				opdiagInfoVo.setDiagCode(map.get("diagCode")==null?"":map.get("diagCode").toString());
				opdiagInfoVo.setDiagDesc(map.get("diagDesc")==null?"":map.get("diagDesc").toString());
				opdiagInfoVo.setDiagDate(map.get("diagDate")==null?"":map.get("diagDate").toString());
				opdiagInfoVo.setVisitType(map.get("visitType")==null?"":map.get("visitType").toString());
				opdiagInfoVo.setRstatus(map.get("rstatus")==null?"":map.get("rstatus").toString());
				opdiagInfoVo.setDateSource(map.get("dateSource")==null?"":map.get("dateSource").toString());
				lists.add(opdiagInfoVo);
			}
			opdiagInfoResponse.setSubject(lists);
									
			if(null == opdiagInfoVoList || opdiagInfoVoList.size() ==0){
				opdiagInfoResult.setId("AD");
				opdiagInfoResult.setText("未查询到数据");
			}else{
				opdiagInfoResult.setId("AA");
				opdiagInfoResult.setText("处理成功");
			}
			
			opdiagInfoResult.setRequestId(reqData.getId());
			opdiagInfoResult.setRequestTime(reqData.getCreationTime());
		} catch (Exception e) {
			opdiagInfoResult.setId("AE");
			opdiagInfoResult.setText("处理失败");
		}		
		opdiagInfoResponse.setResult(opdiagInfoResult);
    	String hospXml = XmlUtil.beanToXml(opdiagInfoResponse, OpdiagInfoResponse.class);
    	
		return hospXml;		
	}

}
