package com.zebone.nhis.ma.pub.platform.send.impl.sd.service;

import ca.uhn.hl7v2.HL7Exception;
import com.zebone.nhis.ma.pub.platform.sd.send.*;
import com.zebone.nhis.ma.pub.platform.sd.util.SDMsgUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;

/**
 * 门诊发消息
 * @author maijiaxing
 *
 */
@Service
public class SDPlatFormSendOpHandler {

	@Resource
	private SDMsgSendOpAdt sDMsgSendOpAdt;
	@Resource
	private SDMsgSendOpSch msgSendOpSch;
	@Resource
	private SDMsgSendOpCn sDMsgSendOpCn;
	@Resource
	private SDMsgSendOp sDMsgSendOp;
	
	private static Logger loger = LoggerFactory.getLogger("nhis.lbHl7Log");

	/**
    * 门诊诊毕（深圳项目）
    * @param paramMap
    * @throws HL7Exception
    */
	public void sendOpCompleteMsg(Map<String, Object> paramMap) throws Exception {
		sDMsgSendOpAdt.sendOpCompleteMsg(paramMap);
	}

	/**
	 * 门诊排班接口实现
	 * @param paramMap
	 * @throws HL7Exception
	 */
	public void sendOpSchInfo(Map<String, Object> paramMap )throws HL7Exception{
		//新增不发消息
		if("add".equals(SDMsgUtils.getPropValueStr(paramMap, "operation"))) {
			return;
		}
		msgSendOpSch.sendSchDict(paramMap);
	}


	/**
	 * 发送门诊检查检验消息
	 * @param paramMap
	 */
	public void sendCnOpAppMsg(Map<String, Object> paramMap )throws Exception{
		sDMsgSendOpCn.sendCnOpAppMsg(paramMap);
	}
	
	/**
	 * 发送门诊手术消息（门诊）
	 * @param paramMap
	 */
	public void sendOpApplyMsg(Map<String, Object> paramMap )throws Exception{
		sDMsgSendOp.sendOpApplyMsg(paramMap);
	}
}
