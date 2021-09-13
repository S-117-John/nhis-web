package com.zebone.nhis.webservice.syx.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.common.support.XmlUtil;
import com.zebone.nhis.webservice.syx.dao.PlatFormStopplanRegisterInfoMapper;
import com.zebone.nhis.webservice.syx.utils.PfWsUtils;
import com.zebone.nhis.webservice.syx.vo.platForm.StopplanRegisterInfoReqSubject;
import com.zebone.nhis.webservice.syx.vo.platForm.StopplanRegisterInfoRes;
import com.zebone.nhis.webservice.syx.vo.platForm.StopplanRegisterInfoResExd;
import com.zebone.nhis.webservice.syx.vo.platForm.StopplanRegisterInfoResResult;
import com.zebone.nhis.webservice.syx.vo.platForm.StopplanRegisterInfoResSubject;

/**
 * 因医生停诊且未处理的预约挂号信息
 * @author cuijiansheng 2019.7.17
 */
@Service
public class PlatFormStopplanRegisterInfoService {

	@Autowired
	private PlatFormStopplanRegisterInfoMapper mapper;
	
	public String getstopplanRegisterInfo(String content) throws Exception {
		
		StopplanRegisterInfoReqSubject req= (StopplanRegisterInfoReqSubject)XmlUtil.XmlToBean(content, StopplanRegisterInfoReqSubject.class);
		
		List<StopplanRegisterInfoRes> list = mapper.getstopplanRegisterInfo(req.getSubject().get(0));
		
		//subject
		StopplanRegisterInfoResSubject subject = new StopplanRegisterInfoResSubject();
		subject.setRegInfo(list);
		
		//result
		StopplanRegisterInfoResResult result = new StopplanRegisterInfoResResult();
		result.setId("AA");
		result.setRequestTime(DateUtils.getDateTimeStr(new Date()));
		result.setRequestId(req.getId());
		result.setText("处理成功!");
		result.setSubject(subject);
		
		//response
		StopplanRegisterInfoResExd exd = new StopplanRegisterInfoResExd();
		exd.setResult(result);
		
		String hospXml = XmlUtil.beanToXml(PfWsUtils.constructResBean(req, exd), StopplanRegisterInfoResExd.class);
        if(list.size() !=0 ){
        	return PfWsUtils.setResStatus("0","处理成功！",hospXml);
        }else{
        	return PfWsUtils.setResStatus("0","未查询数据!",hospXml);
        }		
	}
}
