package com.zebone.nhis.ma.pub.platform.zb.comm;

import ca.uhn.hl7v2.DefaultHapiContext;
import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.HapiContext;
import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.parser.Parser;

import com.zebone.nhis.common.module.ma.msg.SysMsgRec;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.ma.pub.platform.zb.mq.ProducerService;
import com.zebone.nhis.ma.pub.platform.zb.service.SysMsgService;
import com.zebone.nhis.ma.pub.platform.zb.utils.MsgUtils;
import com.zebone.nhis.ma.pub.platform.zb.vo.MsgTreeNode;
import com.zebone.nhis.ma.pub.service.IMsgRetryHandler;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.utils.JsonUtil;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * HL7消息处理
 * @author chengjia
 *
 */
@Service
public class Hl7MsgHander implements IMsgRetryHandler {
	
	private static Logger log = LoggerFactory.getLogger("nhis.lbHl7Log");
	
	@Resource
	private	MsgSenderHander msgSender;
	
	@Resource
	private ProducerService producerService;
	@Resource
	private MsgUtils msgUtils;
	
	HapiContext context=null;
	
	Parser parser=null;
	
	/**
	 * 消息发送（两种模式：1、通过MQ:MQ再通过MINA与集成平台通讯；2、不用MQ:直接通过MINA与集成平台通信
	 * 
	 * @param msgId,msg
	 * @return
	 * @throws HL7Exception
	 */
	public void sendMsg(String msgId,String msg) {
		//默认保存消息记录
		sendMsg(msgId,msg,true);
	}
	/**
	 * 消息发送（两种模式：1、通过MQ:MQ再通过MINA与集成平台通讯；2、不用MQ:直接通过MINA与集成平台通信
	 * 
	 * @param msgId,msg,saveMsg
	 * @return
	 * @throws HL7Exception
	 */
	public void sendMsg(String msgId,String msg,boolean saveMsg){
		
		if(context==null) context = new DefaultHapiContext();
		if(parser==null) parser=context.getGenericParser();
		
		String sendToMq = ApplicationUtils.getPropertyValue("msg.send.mq","0");
		if (sendToMq.equals("0")) {
			try {
			// 不经过MQ
			msgSender.sendMsgToSvr(msgId, msg, parser,saveMsg);
			} catch (Exception e) {
				// TODO: handle exception
				log.error("HL7发送失败：--{},{}",msg,e.getMessage());
			}
		} else {
			//经过MQ
			try {
				String sendMsg=saveMsg+"@@###@@"+msg;
				producerService.sendJmsMessage(sendMsg);
			} catch (Exception e) {
				log.info("JMS消息发送失败!");
				//连接MQ失败，直接发送消息
				try {
					msgSender.sendMsgToSvr(msgId, msg, parser,saveMsg);
				} catch (HL7Exception e1) {
					// TODO Auto-generated catch block
				}

			}
		}

	}
	
    /**
     * 发送HL7消息
     * @param map
     * @return
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
	public void sendHl7Msg(String param , IUser user){
    	Map map = JsonUtil.readValue(param,Map.class);
    	boolean bHaveKey=map.containsKey("msgId");
		String msgId=bHaveKey?(map.get("msgId")==null?msgUtils.getMsgId():map.get("msgId").toString()):msgUtils.getMsgId();
		String msg=map.get("msg").toString();
		
		sendMsg(msgId, msg,false);
    }
    
    /**
     * 创建HL7消息树
     * @param map
     * @return
     */
    @SuppressWarnings("unchecked")
	public List<MsgTreeNode> createHl7MsgTree(String param , IUser user){
    	Map map = JsonUtil.readValue(param,Map.class);
		String msg=map.get("msg").toString();
		
    	return msgUtils.createMsgTree(msg);
    }

	@Override
	public void send(SysMsgRec sysMsgRec) {
    	if(sysMsgRec!=null && StringUtils.isNotBlank(sysMsgRec.getMsgId())){
			Map<String,Object> paramMap = new HashMap<>();
			paramMap.put("msgId", sysMsgRec.getMsgId());
			paramMap.put("msg",sysMsgRec.getMsgContent());
			sendHl7Msg(JsonUtil.writeValueAsString(paramMap),UserContext.getUser());
		}

	}
}
