package com.zebone.nhis.webservice.syx.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.common.support.XmlUtil;
import com.zebone.nhis.webservice.syx.dao.PlatFormQueueListMapper;
import com.zebone.nhis.webservice.syx.vo.platForm.QueueListReq;
import com.zebone.nhis.webservice.syx.vo.platForm.QueueListReqSubject;
import com.zebone.nhis.webservice.syx.vo.platForm.QueueListRes;
import com.zebone.nhis.webservice.syx.vo.platForm.QueueListResExd;
import com.zebone.nhis.webservice.syx.vo.platForm.QueueListResResult;
import com.zebone.nhis.webservice.syx.vo.platForm.QueueListResSubject;
import com.zebone.platform.common.support.NHISUUID;


/**
 * 当前号源列表
 * @author cuijiansheng 2019.7.31
 */
@Service
public class PlatFormQueueListService {

	@Autowired
	private PlatFormQueueListMapper mapper;
	
	public String getQueueList(String content) throws Exception {
		
		QueueListReqSubject req= (QueueListReqSubject)XmlUtil.XmlToBean(content, QueueListReqSubject.class);
				
		QueueListReq queueListReq = req.getSubject().get(0);
		List<QueueListRes> list = mapper.getQueueList(queueListReq);
		
		//subject
		QueueListResSubject subject = new QueueListResSubject();
		if(list == null || list.size() == 0){
			subject.setResultCode("1");
			subject.setErrorMsg("交易失败");
		}else{
			subject.setResultCode("0");
			subject.setErrorMsg("交易成功");		
		}
		subject.setItem(list);
		
		//result
		QueueListResResult result = new QueueListResResult();
		result.setId("AA");
		result.setRequestTime(DateUtils.getDateTimeStr(new Date()));
		result.setRequestId(req.getId());
		result.setText("处理成功!");
		
		//response
		QueueListResExd exd = new QueueListResExd();
		exd.setResult(result);
		exd.setSubject(subject);
		exd.setActionId(req.getActionId());
		exd.setActionName(req.getActionName());
		exd.setCreateTime(DateUtils.getDateTimeStr(new Date()));
		exd.setId(NHISUUID.getKeyId());
		
		String xml = XmlUtil.beanToXml(exd, QueueListResExd.class);
        return xml;
	}
}
