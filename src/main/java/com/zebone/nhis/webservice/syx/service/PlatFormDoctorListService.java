package com.zebone.nhis.webservice.syx.service;

import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.common.support.XmlUtil;
import com.zebone.nhis.webservice.syx.dao.PlatFormDoctorListMapper;
import com.zebone.nhis.webservice.syx.vo.platForm.DoctorListReq;
import com.zebone.nhis.webservice.syx.vo.platForm.DoctorListReqSubject;
import com.zebone.nhis.webservice.syx.vo.platForm.DoctorListRes;
import com.zebone.nhis.webservice.syx.vo.platForm.DoctorListResExd;
import com.zebone.nhis.webservice.syx.vo.platForm.DoctorListResResult;
import com.zebone.nhis.webservice.syx.vo.platForm.DoctorListResSubject;
import com.zebone.platform.common.support.NHISUUID;


/**
 * 门诊医生列表
 * @author cuijiansheng 2019.7.31
 */
@Service
public class PlatFormDoctorListService {

	@Autowired
	private PlatFormDoctorListMapper mapper;
	
	public String getDoctorList(String content) throws Exception {
		
		DoctorListReqSubject req= (DoctorListReqSubject)XmlUtil.XmlToBean(content, DoctorListReqSubject.class);
		
		DoctorListReq doctorListReq = req.getSubject().get(0);
		doctorListReq.setDateWork(DateUtils.dateToStr("yyyy-MM-dd", new Date()));
		List<DoctorListRes> list = mapper.getDoctorList(doctorListReq);
		
		//subject
		DoctorListResSubject subject = new DoctorListResSubject();
		if(list == null || list.size() == 0){
			subject.setResultCode("1");
			subject.setErrorMsg("交易失败");
		}else{
			subject.setResultCode("0");
			subject.setErrorMsg("交易成功");		
		}
		subject.setItem(list);
		
		//result
		DoctorListResResult result = new DoctorListResResult();
		result.setId("AA");
		result.setRequestTime(DateUtils.getDateTimeStr(new Date()));
		result.setRequestId(req.getId());
		result.setText("处理成功!");
		
		//response
		DoctorListResExd exd = new DoctorListResExd();
		exd.setResult(result);
		exd.setSubject(subject);
		exd.setActionId(req.getActionId());
		exd.setActionName(req.getActionName());
		exd.setCreateTime(DateUtils.getDateTimeStr(new Date()));
		exd.setId(NHISUUID.getKeyId());
		
		String xml = XmlUtil.beanToXml(exd, DoctorListResExd.class);
        return xml;
	}
}
