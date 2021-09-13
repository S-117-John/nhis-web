package com.zebone.nhis.ma.pub.platform.sd.common;

import ca.uhn.hl7v2.DefaultHapiContext;
import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.HapiContext;
import ca.uhn.hl7v2.parser.Parser;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.ma.pub.platform.sd.mq.SDProducerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * HL7消息处理(灵璧复制版本)
 * @author chengjia
 *
 */
@Service
public class SDHl7MsgHander {
	
	private static Logger log = LoggerFactory.getLogger("nhis.lbHl7Log");
	
	@Resource
	private	SDMsgSenderHander sDMsgSenderHander;
	
	@Resource
	private SDProducerService sDproducerService;
	
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
	 * 消息发送同步发送 目前不经过mq的可实现同步调用（两种模式：1、通过MQ:MQ再通过MINA与集成平台通讯；2、不用MQ:直接通过MINA与集成平台通信
	 * 
	 * @param msgId,msg
	 * @return
	 * @throws HL7Exception
	 */
	public Object sendMsgFor(String msgId,String msg) {
		//默认保存消息记录
		return sendMsgFor(msgId,msg,true);
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
				sDMsgSenderHander.sendMsgToSvr(msgId, msg, parser,saveMsg);
			} catch (Exception e) {
				// TODO: handle exception
				log.info(e+"HL7发送失败：--"+msg);
				e.printStackTrace();
			}
		} else {
			//经过MQ
			try {
				String sendMsg=saveMsg+"@@###@@"+msg;
				sDproducerService.sendJmsMessage(sendMsg);
			} catch (Exception e) {
				log.info("JMS消息发送失败!");
				e.printStackTrace();
				//连接MQ失败，直接发送消息
				try {
					sDMsgSenderHander.sendMsgToSvr(msgId, msg, parser,saveMsg);
				} catch (HL7Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

			}
		}

	}
	
	/**
	 * 消息发送（两种模式：1、通过MQ:MQ再通过MINA与集成平台通讯；2、不用MQ:直接通过MINA与集成平台通信
	 * 
	 * @param msgId,msg,saveMsg
	 * @return
	 * @throws HL7Exception
	 */
	public Object sendMsgFor(String msgId,String msg,boolean saveMsg){
		
		if(context==null) context = new DefaultHapiContext();
		if(parser==null) parser=context.getGenericParser();
		
		String sendToMq = ApplicationUtils.getPropertyValue("msg.send.mq","0");
		if (sendToMq.equals("0")) {
			try {
			// 不经过MQ
				return sDMsgSenderHander.sendMsgToSvrFor(msgId, msg, parser,saveMsg);
			} catch (Exception e) {
				// TODO: handle exception
				log.info(e+"HL7发送失败：--"+msg);
				e.printStackTrace();
			}
		} else {
			//经过MQ
			try {
				String sendMsg=saveMsg+"@@###@@"+msg;
				sDproducerService.sendJmsMessage(sendMsg);
				
			} catch (Exception e) {
				log.info("JMS消息发送失败!");
				e.printStackTrace();
				//连接MQ失败，直接发送消息
				try {
					return	sDMsgSenderHander.sendMsgToSvrFor(msgId, msg, parser,saveMsg);
				} catch (HL7Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

			}
			return "";
		}
		return "";
	}
	
    /**
     * 发送HL7消息（深大没有用此方法；使用灵璧包下的方法）
     * @param map
     * @return
     */
//	public void sendHl7Msg(String param , IUser user){
//    	Map<?, ?> map = JsonUtil.readValue(param,Map.class);
//    	boolean bHaveKey=map.containsKey("msgId");
//		String msgId=bHaveKey?(map.get("msgId")==null?SDMsgUtils.getMsgId():map.get("msgId").toString()):SDMsgUtils.getMsgId();
//		String msg=map.get("msg").toString();
//
//		sendMsg(msgId, msg,false);
//    }
    
    /**
     * 创建HL7消息树（深大使用灵璧包下的方法）
     * @param map
     * @return
     */
//	public List<MsgTreeNode> createHl7MsgTree(String param , IUser user){
//    	Map<?, ?> map = JsonUtil.readValue(param,Map.class);
//		String msg=map.get("msg").toString();
//
//    	return SDMsgUtils.createMsgTree(msg);
//    }
}
