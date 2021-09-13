package com.zebone.nhis.webservice.syx.service;

import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.common.support.XmlUtil;
import com.zebone.nhis.webservice.syx.dao.PlatFormRoomListMapper;
import com.zebone.nhis.webservice.syx.vo.platForm.ChecklistInfoResExd;
import com.zebone.nhis.webservice.syx.vo.platForm.RoomListReqSubject;
import com.zebone.nhis.webservice.syx.vo.platForm.RoomListRes;
import com.zebone.nhis.webservice.syx.vo.platForm.RoomListResExd;
import com.zebone.nhis.webservice.syx.vo.platForm.RoomListResResult;
import com.zebone.nhis.webservice.syx.vo.platForm.RoomListResSubject;
import com.zebone.platform.common.support.NHISUUID;


/**
 * 诊室列表
 * @author cuijiansheng 2019.7.31
 */
@Service
public class PlatFormRoomListService {

	@Autowired
	private PlatFormRoomListMapper mapper;
	
	public String getRoomList(String content) throws Exception {
		
		RoomListReqSubject req= (RoomListReqSubject)XmlUtil.XmlToBean(content, RoomListReqSubject.class);
				
		List<RoomListRes> list = mapper.getRoomList(req.getSubject().get(0));
		
		//subject
		RoomListResSubject subject = new RoomListResSubject();
		if(list == null || list.size() == 0){
			subject.setResultCode("1");
			subject.setErrorMsg("交易失败");
		}else{
			subject.setResultCode("0");
			subject.setErrorMsg("交易成功");		
		}
		subject.setItem(list);
		
		//result
		RoomListResResult result = new RoomListResResult();
		result.setId("AA");
		result.setRequestTime(DateUtils.getDateTimeStr(new Date()));
		result.setRequestId(req.getId());
		result.setText("处理成功!");
		
		//response
		RoomListResExd exd = new RoomListResExd();
		exd.setResult(result);
		exd.setSubject(subject);
		exd.setActionId(req.getActionId());
		exd.setActionName(req.getActionName());
		exd.setCreateTime(DateUtils.getDateTimeStr(new Date()));
		exd.setId(NHISUUID.getKeyId());
		
		String xml = XmlUtil.beanToXml(exd, RoomListResExd.class);
        return xml;
	}
}
