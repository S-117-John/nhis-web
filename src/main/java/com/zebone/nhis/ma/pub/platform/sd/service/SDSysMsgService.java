package com.zebone.nhis.ma.pub.platform.sd.service;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.model.Segment;
import ca.uhn.hl7v2.model.v24.segment.MSH;

import com.zebone.nhis.common.module.ma.msg.SysMsgRec;
import com.zebone.nhis.ma.pub.platform.PlatFormSendUtils;
import com.zebone.nhis.ma.pub.platform.sd.dao.SDMsgMapper;
import com.zebone.nhis.ma.pub.platform.sd.send.SDMsgSendAdt;
import com.zebone.nhis.ma.pub.platform.sd.util.SDMsgUtils;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.utils.JsonUtil;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@SuppressWarnings("unchecked")
public class SDSysMsgService {
	@Resource
	private	SDMsgMapper sDMsgMapper;
	
	@Resource
	private	SDMsgSendAdt sDMsgSendAdt;
	
	/**
	 * 保存Hl7消息
	 * @param message
	 * @param expandStr
	 * @throws HL7Exception
	 */
	public SysMsgRec saveHl7MsgReceive(Message message,String msg) throws HL7Exception{
		Segment segment = (Segment) message.get("MSH");
		MSH msh=(MSH)segment;
		SysMsgRec rec = new SysMsgRec();
		rec.setTransType("receive");
		rec.setMsgType(msh.getMessageType().getMessageType().getValue()+"^"+msh.getMessageType().getTriggerEvent().getValue());
		rec.setMsgId(msh.getMessageControlID().getValue());
		rec.setTransDate(new Date());
		rec.setMsgContent(msg);
		String sysCode=msh.getSendingApplication().getNamespaceID().getValue();
		if(sysCode==null||sysCode.equals("")) sysCode="EAI";
		rec.setSysCode(sysCode);
		rec.setMsgStatus("SAVE");
		DataBaseHelper.insertBean(rec);
		return rec;
	}
	
	/**
	 * 保存系统消息日志
	 * @param message
	 * @param expandStr
	 * @throws HL7Exception
	 */
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public void saveHl7Msg(Message message,String msg) throws HL7Exception{
		Segment segment = (Segment) message.get("MSH");
		MSH msh=(MSH)segment;
		SysMsgRec rec = new SysMsgRec();
		rec.setTransType("send");
		rec.setMsgType(msh.getMessageType().getMessageType().getValue()+"^"+msh.getMessageType().getTriggerEvent().getValue());
		rec.setMsgId(msh.getMessageControlID().getValue());
		rec.setTransDate(new Date());
		rec.setMsgContent(msg);
		rec.setSysCode("NHIS");
		rec.setMsgStatus("SAVE");
		DataBaseHelper.insertBean(rec);
	}
		
	/**
	 * 保存系统消息日志
	 * @param list
	 * @param pkPi
	 */
	public void saveSysMsgRec(SysMsgRec rec) {
		DataBaseHelper.insertBean(rec);
	}
	
	/**更新系统消息日志
	 * @param msgId
	 * @param msgStatus
	 * @param msgTxt
	 */
	public void updateSysMsgRec(String msgId,String msgStatus,String errTxt) {
		DataBaseHelper.update("update sys_msg_rec set msg_status = '"+msgStatus+"',err_txt=? where msg_id = ?",
				new Object[] { errTxt,msgId});
	}
	
	/**更新系统消息日志
	 * @param msgId
	 * @param msgStatus
	 * @param msgTxt
	 */
	public void updateSysMsgRec(String msgId,String msgStatus) {
		DataBaseHelper.update("update sys_msg_rec set msg_status = '"+msgStatus+"' where msg_id = ?",
				new Object[] {msgId});
	}

	/**
	 * 接收消息保存
	 * @param msgId
	 * @param msgType
	 * @param msgStatus
	 */
	public void updateReceiveSysMsgRec(String msgId,String msgType,String msgStatus){
		DataBaseHelper.update(" update sys_msg_rec  set msg_type= '"+msgType+"',msg_status= '"+msgStatus+"' where msg_id=?",new Object[] {msgId});
	}	
	
    /**
     * 查询HL7消息记录（前台调用）
     * @param map
     * @return
     */
	public List<SysMsgRec> querMsgList(String param , IUser user){
    	Map<String,Object> map = JsonUtil.readValue(param,Map.class);
    	return sDMsgMapper.queryMsgList(map);
    }
  
    /*
	 *  发送Hl7消息重推（前台调用）
	 * @param triggerEvent
	 * @param paramMap 
	 * @throws HL7Exception
	 */
	public void againNewsPush(String param,IUser u){
		Map<String,Object> paramMap = JsonUtil.readValue(param, Map.class);
		try {
			sDMsgSendAdt.sendAdtMsg(SDMsgUtils.getPropValueStr(paramMap,"triggerEvent"),paramMap);
		} catch (HL7Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/*
	 *  发送Hl7消息重推（前台调用）
	 * @param triggerEvent
	 * @param paramMap 
	 * @throws HL7Exception
	 */
	public void sendExOrderCheckMsg(String param,IUser u){
		Map<String,Object> paramMap = new HashMap<String,Object>();
		paramMap.put("ordlist", JsonUtil.readValue(param, Map.class));
	    PlatFormSendUtils.sendExOrderCheckMsg(paramMap);
	}
}
