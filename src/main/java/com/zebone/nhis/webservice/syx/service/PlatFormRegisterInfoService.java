package com.zebone.nhis.webservice.syx.service;

import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.common.support.XmlUtil;
import com.zebone.nhis.webservice.syx.dao.PlatFormRegisterInfoMapper;
import com.zebone.nhis.webservice.syx.utils.PfWsUtils;
import com.zebone.nhis.webservice.syx.vo.platForm.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;


/**
 * 预约对帐信息查询接口
 * @author cuijiansheng 2019.7.18
 */
@Service
public class PlatFormRegisterInfoService {

	@Autowired
	private PlatFormRegisterInfoMapper mapper;
	
	public String getRegisterInfo(String content) throws Exception {
		
		RegisterInfoReqSubject req= (RegisterInfoReqSubject)XmlUtil.XmlToBean(content, RegisterInfoReqSubject.class);

		if(!StringUtils.isEmpty(req.getSubject().get(0).getStartDate()))
			req.getSubject().get(0).setStartDate(req.getSubject().get(0).getStartDate()+" 00:00:00");
		if(!StringUtils.isEmpty(req.getSubject().get(0).getEndDate()))
			req.getSubject().get(0).setEndDate(req.getSubject().get(0).getEndDate()+" 23:59:59");

		List<RegisterInfoRes> list = mapper.getRegisterInfo(req.getSubject().get(0));
		
		//subject
		RegisterInfoResSubject subject = new RegisterInfoResSubject();
		subject.setRegInfo(list);
		
		//result
		RegisterInfoResResult result = new RegisterInfoResResult();
		result.setId("AA");
		result.setRequestTime(DateUtils.getDateTimeStr(new Date()));
		result.setRequestId(req.getId());
		result.setText("处理成功!");
		result.setSubject(subject);
		
		//response
		RegisterInfoResExd exd = new RegisterInfoResExd();
		exd.setResult(result);
		
		String xml = XmlUtil.beanToXml(PfWsUtils.constructResBean(req, exd), RegisterInfoResExd.class);
        return xml;
	}
}
