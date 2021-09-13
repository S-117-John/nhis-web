package com.zebone.nhis.ma.kangMei.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zebone.nhis.common.module.ex.nis.ns.ExPdApplyDetail;
import com.zebone.nhis.common.module.ex.nis.ns.ExPresOcc;
import com.zebone.nhis.ma.kangMei.handler.SendKmMsgHandler;
import com.zebone.nhis.scm.pub.service.IScmService;
@Service("kmDrugDeService")
public class IpDeDrugExtService implements IScmService {
	@Autowired
	private SendKmMsgHandler sendKmMsgHandler;
	@Override
	public void processExtIpDe(List<ExPdApplyDetail> exPdAppDetails,String param) {
		//发送消息至康美
		try {
			sendKmMsgHandler.sendMedInfo(exPdAppDetails,param);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	@Override
	public void processExOpDe(ExPresOcc exPres, Map<String, Object> paramMap) {
		try {
			sendKmMsgHandler.sendOpMedInfo(exPres, paramMap);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
