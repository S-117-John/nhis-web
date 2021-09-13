package com.zebone.nhis.webservice.zhongshan.service;

import java.io.IOException;
import java.util.Date;

import javax.xml.bind.JAXBException;

import org.springframework.stereotype.Service;

import com.zebone.nhis.common.module.base.support.CvMsg;
import com.zebone.nhis.common.module.base.support.CvMsgSend;
import com.zebone.nhis.common.support.XmlUtil;
import com.zebone.nhis.webservice.zhongshan.vo.CriticalXmlResquest;
import com.zebone.platform.common.support.NHISUUID;
import com.zebone.platform.modules.dao.DataBaseHelper;

@Service
public class CriticalService {

	
	public String addMsg(String content) throws JAXBException, IOException {
		CriticalXmlResquest cxr=(CriticalXmlResquest) XmlUtil.XmlToBean(content, CriticalXmlResquest.class);
		CvMsg cvmsg=new CvMsg();
		CvMsgSend cms=new CvMsgSend();
		cvmsg.setPkMsg(NHISUUID.getKeyId());
		cms.setPkMsg(cvmsg.getPkMsg());
		cms.setDateSend(new Date());
		cms.setCodeDept(cxr.getBody().getCodeDept());
		cvmsg.setSubject(cxr.getBody().getSubject());
		cvmsg.setCodePi(cxr.getBody().getCodePi());
		cvmsg.setCodePv(cxr.getBody().getCodePv());
		cvmsg.setNamePi(cxr.getBody().getNamePi());
		cvmsg.setDtSystype(cxr.getHead().getSysId().equals("1")?"lis":"pacs");
		cvmsg.setEuLevel(cxr.getBody().getLevel());
		cvmsg.setCodeDept(cxr.getBody().getCodeDept());
		cvmsg.setNameDept(cxr.getBody().getNameDept());
		cvmsg.setEuStatus("1");
		cvmsg.setNameSender(cxr.getHead().getOperator());
		DataBaseHelper.insertBean(cvmsg);
		DataBaseHelper.insertBean(cms);
		return null;
	}
}
