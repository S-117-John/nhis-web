package com.zebone.nhis.webservice.syx.service;

import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.common.support.XmlUtil;
import com.zebone.nhis.webservice.syx.dao.PlatFormStopRegMapper;
import com.zebone.nhis.webservice.syx.utils.PfWsUtils;
import com.zebone.nhis.webservice.syx.vo.platForm.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 停诊信息查询接口
 * @author cuijiansheng 2019.7.17
 */
@Service
public class PlatFormStopRegService {

	@Autowired
	private PlatFormStopRegMapper platFormStopRegMapper;
	
	public String stopReg(String content) throws Exception {

		StopRegReqSubject stopRegReqSubject= (StopRegReqSubject)XmlUtil.XmlToBean(content, StopRegReqSubject.class);

		stopRegReqSubject.getSubject().get(0).setBeginDate(stopRegReqSubject.getSubject().get(0).getBeginDate()+" 00:00:00");
		stopRegReqSubject.getSubject().get(0).setEndDate(stopRegReqSubject.getSubject().get(0).getEndDate()+" 23:59:59");

		List<StopRegRes> list = platFormStopRegMapper.stopReg(stopRegReqSubject.getSubject().get(0));
		
		//subject
		StopRegResSubject subject = new StopRegResSubject();
		subject.setItem(list);
		
		//result
		StopRegResResult stopRegResResult = new StopRegResResult();
		stopRegResResult.setId("AA");
		stopRegResResult.setRequestTime(DateUtils.getDateTimeStr(new Date()));
		stopRegResResult.setRequestId(stopRegReqSubject.getId());
		stopRegResResult.setText("处理成功!");
		stopRegResResult.setSubject(subject);
		
		//response
		StopRegResExd stopRegResExd = new StopRegResExd();
		stopRegResExd.setResult(stopRegResResult);
		
		String hospXml = XmlUtil.beanToXml(PfWsUtils.constructResBean(stopRegReqSubject, stopRegResExd), StopRegResExd.class);
        if(list.size() != 0){
        	return PfWsUtils.setResStatus("0","处理成功！",hospXml);
        }else{
        	return PfWsUtils.setResStatus("0","未查询到数据!",hospXml);
        }		
	}
}
