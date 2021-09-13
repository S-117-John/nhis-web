package com.zebone.nhis.webservice.syx.service;

import java.util.Date;
import java.util.List;
import org.springframework.stereotype.Service;
import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.common.support.XmlUtil;
import com.zebone.nhis.webservice.syx.vo.platForm.SetQueueListReq;
import com.zebone.nhis.webservice.syx.vo.platForm.SetQueueListReqSubject;
import com.zebone.nhis.webservice.syx.vo.platForm.SetQueueListResExd;
import com.zebone.nhis.webservice.syx.vo.platForm.SetQueueListResResult;
import com.zebone.nhis.webservice.syx.vo.platForm.SetQueueListResSubject;
import com.zebone.platform.common.support.NHISUUID;


/**
 * 当前号源列表
 * @author cuijiansheng 2019.7.31
 */
@Service
public class PlatFormSetQueueListService {

	
	public String setQueueList(String content) throws Exception {
		
		SetQueueListReqSubject req= (SetQueueListReqSubject)XmlUtil.XmlToBean(content, SetQueueListReqSubject.class);
				
		List<SetQueueListReq> item = req.getItem();
				
		//subject
		SetQueueListResSubject subject = new SetQueueListResSubject();
		if(item == null || item.size() == 0){
			subject.setResultCode("1");
			subject.setErrorMsg("交易失败");
		}else{
			subject.setResultCode("0");
			subject.setErrorMsg("交易成功");		
		}
		
		//result
		SetQueueListResResult result = new SetQueueListResResult();
		result.setId("AA");
		result.setRequestTime(DateUtils.getDateTimeStr(new Date()));
		result.setRequestId(req.getId());
		result.setText("处理成功!");
		
		//response
		SetQueueListResExd exd = new SetQueueListResExd();
		exd.setResult(result);
		exd.setSubject(subject);
		exd.setActionId(req.getActionId());
		exd.setActionName(req.getActionName());
		exd.setCreateTime(DateUtils.getDateTimeStr(new Date()));
		exd.setId(NHISUUID.getKeyId());
		
		String xml = XmlUtil.beanToXml(exd, SetQueueListResExd.class);
        return xml;
	}
}
