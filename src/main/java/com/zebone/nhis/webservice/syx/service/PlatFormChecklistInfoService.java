package com.zebone.nhis.webservice.syx.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.common.support.XmlUtil;
import com.zebone.nhis.webservice.syx.dao.PlatFormChecklistInfoMapper;
import com.zebone.nhis.webservice.syx.utils.PfWsUtils;
import com.zebone.nhis.webservice.syx.vo.platForm.ChecklistInfoReq;
import com.zebone.nhis.webservice.syx.vo.platForm.ChecklistInfoReqSubject;
import com.zebone.nhis.webservice.syx.vo.platForm.ChecklistInfoRes;
import com.zebone.nhis.webservice.syx.vo.platForm.ChecklistInfoResExd;
import com.zebone.nhis.webservice.syx.vo.platForm.ChecklistInfoResResult;
import com.zebone.nhis.webservice.syx.vo.platForm.ChecklistInfoResSubject;


/**
 * 预约对帐信息查询接口
 * @author cuijiansheng 2019.7.18
 */
@Service
public class PlatFormChecklistInfoService {

	@Autowired
	private PlatFormChecklistInfoMapper mapper;
	
	public String getchecklistInfo(String content) throws Exception {
		
		ChecklistInfoReqSubject req= (ChecklistInfoReqSubject)XmlUtil.XmlToBean(content, ChecklistInfoReqSubject.class);
		
		ChecklistInfoReq checklistInfoReq = req.getSubject().get(0);
		checklistInfoReq.setStartDate(checklistInfoReq.getStartDate() + " 00:00:00");
		checklistInfoReq.setEndDate(checklistInfoReq.getEndDate() + " 23:59:59");
		List<ChecklistInfoRes> list = mapper.getchecklistInfo(checklistInfoReq);
		
		//subject
		ChecklistInfoResSubject subject = new ChecklistInfoResSubject();
		subject.setItem(list);
		
		//result
		ChecklistInfoResResult result = new ChecklistInfoResResult();
		result.setId("AA");
		result.setRequestTime(DateUtils.getDateTimeStr(new Date()));
		result.setRequestId(req.getId());
		result.setText("处理成功!");
		result.setSubject(subject);
		
		//response
		ChecklistInfoResExd exd = new ChecklistInfoResExd();
		exd.setResult(result);
		
		String hospXml = XmlUtil.beanToXml(PfWsUtils.constructResBean(req, exd), ChecklistInfoResExd.class);
		if(list.size() !=0 ){
        	return PfWsUtils.setResStatus("0","处理成功！",hospXml);
        }else{
        	return PfWsUtils.setResStatus("0","未查询到数据！",hospXml);
        }	
		//return xml;
	}
}
