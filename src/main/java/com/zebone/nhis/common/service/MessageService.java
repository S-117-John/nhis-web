package com.zebone.nhis.common.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import com.zebone.nhis.common.module.base.message.SysMessage;
import com.zebone.nhis.common.module.base.message.SysMessageSend;
import com.zebone.nhis.common.module.base.support.MessageConstant;
import com.zebone.nhis.common.support.MessageUtils;
import com.zebone.platform.common.support.NHISUUID;
import com.zebone.platform.modules.exception.BusException;

/***
 * 消息公共service
 * 
 * @author wanpeng
 *
 */
@Service
public class MessageService {
	
	Logger logger = LogManager.getLogger(MessageService.class.getName());
	
	/***
	 * 业务处理过程中，自动生成系统消息<br>
	 * <pre>
	 * 
	 * </pre>
	 */
	public void saveMessage(SysMessage message){
		if(message==null) return;
		//全员不写send表，科室一人看就算看了
		List<SysMessageSend> sendList = new ArrayList<SysMessageSend>();
		String euScope = message.getEuScope();
		String euTargetType = ""; //发送类型
		
		String[] scopes = message.getScopes(); //科室或人员主键
		
		int length;
		if(scopes != null){
			length = scopes.length;
		}else{
			length = 0;
		}
		if(MessageConstant.EU_SCOPE_ALL.equals(euScope)){ //全员
			//不写发送表			
		}else if(MessageConstant.EU_SCOPE_DEPT.equals(euScope)){//科室
			euTargetType = MessageConstant.EU_TARGET_TYPE_DEPT;
			if(length == 0){
				throw new BusException("接收范围为科室时，科室主键不能为空！");				
			}
		}else if(MessageConstant.EU_SCOPE_STAFF.equals(euScope)){ //人员
			euTargetType = MessageConstant.EU_TARGET_TYPE_STAFF;
			if(length == 0){
				throw new BusException("接收范围为人员时，人员主键不能为空！");
			}
		}
		
		for(int i=0;i<length;i++){
			SysMessageSend send = new SysMessageSend();
			send.setPkMessageSend(NHISUUID.getKeyId());
			send.setSendTarget(scopes[i]);
			send.setEuTargetType(euTargetType);
			send.setEuSendStatus(MessageConstant.EU_SEND_STATUS_NO);
			send.setFlagRead("0");
			
			sendList.add(send);
		}
		message.setEuHandleStatus("0"); //新消息
		MessageUtils.saveMessageAndSend(message, sendList);
	}

}
