package com.zebone.nhis.webservice.syx.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.common.support.XmlUtil;
import com.zebone.nhis.webservice.syx.dao.PlatFormRegRecordsTodayMapper;
import com.zebone.nhis.webservice.syx.utils.PfWsUtils;
import com.zebone.nhis.webservice.syx.vo.platForm.RegRecordsTodayReqSubject;
import com.zebone.nhis.webservice.syx.vo.platForm.RegRecordsTodayRes;
import com.zebone.nhis.webservice.syx.vo.platForm.RegRecordsTodayResExd;
import com.zebone.nhis.webservice.syx.vo.platForm.RegRecordsTodayResResult;
import com.zebone.nhis.webservice.syx.vo.platForm.RegRecordsTodayResSubject;


/**
 * 当天挂号记录查询接口
 * @author cuijiansheng 2019.7.24
 */
@Service
public class PlatFormRegRecordsTodayService {

	@Autowired
	private PlatFormRegRecordsTodayMapper mapper;
	
	public String getRegRecordsToday(String content) throws Exception {
		
		RegRecordsTodayReqSubject req= (RegRecordsTodayReqSubject)XmlUtil.XmlToBean(content, RegRecordsTodayReqSubject.class);
		
		List<RegRecordsTodayRes> list = mapper.getRegRecordsToday(req.getSubject().get(0));
		
		//subject
		RegRecordsTodayResSubject subject = new RegRecordsTodayResSubject();
		subject.setItem(list);
		
		//result
		RegRecordsTodayResResult result = new RegRecordsTodayResResult();
		result.setId("AA");
		result.setRequestTime(DateUtils.getDateTimeStr(new Date()));
		result.setRequestId(req.getId());
		result.setText("处理成功!");
		result.setSubject(subject);
		
		//response
		RegRecordsTodayResExd exd = new RegRecordsTodayResExd();
		exd.setResult(result);
		
		String xml = XmlUtil.beanToXml(PfWsUtils.constructResBean(req, exd), RegRecordsTodayResExd.class);
        return xml;
	}
}
