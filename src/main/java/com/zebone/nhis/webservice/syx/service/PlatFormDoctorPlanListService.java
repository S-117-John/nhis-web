package com.zebone.nhis.webservice.syx.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.common.support.XmlUtil;
import com.zebone.nhis.webservice.syx.dao.PlatFormDoctorPlanListMapper;
import com.zebone.nhis.webservice.syx.dao.PlatFormRoomListMapper;
import com.zebone.nhis.webservice.syx.vo.platForm.ChecklistInfoResExd;
import com.zebone.nhis.webservice.syx.vo.platForm.DoctorPlanListReq;
import com.zebone.nhis.webservice.syx.vo.platForm.DoctorPlanListReqSubject;
import com.zebone.nhis.webservice.syx.vo.platForm.DoctorPlanListRes;
import com.zebone.nhis.webservice.syx.vo.platForm.DoctorPlanListResExd;
import com.zebone.nhis.webservice.syx.vo.platForm.DoctorPlanListResResult;
import com.zebone.nhis.webservice.syx.vo.platForm.DoctorPlanListResSubject;
import com.zebone.nhis.webservice.syx.vo.platForm.RoomListReqSubject;
import com.zebone.nhis.webservice.syx.vo.platForm.RoomListRes;
import com.zebone.nhis.webservice.syx.vo.platForm.RoomListResExd;
import com.zebone.nhis.webservice.syx.vo.platForm.RoomListResResult;
import com.zebone.nhis.webservice.syx.vo.platForm.RoomListResSubject;
import com.zebone.platform.common.support.NHISUUID;


/**
 * 当前排班医生列表
 * @author cuijiansheng 2019.7.31
 */
@Service
public class PlatFormDoctorPlanListService {

	@Autowired
	private PlatFormDoctorPlanListMapper mapper;
	
	public String getDoctorPlanList(String content) throws Exception {
		
		DoctorPlanListReqSubject req= (DoctorPlanListReqSubject)XmlUtil.XmlToBean(content, DoctorPlanListReqSubject.class);
		
		DoctorPlanListReq doctorPlanListReq = req.getSubject().get(0);
		doctorPlanListReq.setDateWork(DateUtils.dateToStr("yyyy-MM-dd", new Date()));
		List<DoctorPlanListRes> list = mapper.getDoctorPlanList(doctorPlanListReq);
		
		//subject
		DoctorPlanListResSubject subject = new DoctorPlanListResSubject();
		if(list == null || list.size() == 0){
			subject.setResultCode("1");
			subject.setErrorMsg("交易失败");
		}else{
			subject.setResultCode("0");
			subject.setErrorMsg("交易成功");		
		}
		subject.setItem(list);
		
		//result
		DoctorPlanListResResult result = new DoctorPlanListResResult();
		result.setId("AA");
		result.setRequestTime(DateUtils.getDateTimeStr(new Date()));
		result.setRequestId(req.getId());
		result.setText("处理成功!");
		
		//response
		DoctorPlanListResExd exd = new DoctorPlanListResExd();
		exd.setResult(result);
		exd.setSubject(subject);
		exd.setActionId(req.getActionId());
		exd.setActionName(req.getActionName());
		exd.setCreateTime(DateUtils.getDateTimeStr(new Date()));
		exd.setId(NHISUUID.getKeyId());
		
		String xml = XmlUtil.beanToXml(exd, DoctorPlanListResExd.class);
        return xml;
	}
}
