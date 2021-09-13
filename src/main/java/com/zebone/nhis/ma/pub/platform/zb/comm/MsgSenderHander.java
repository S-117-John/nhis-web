package com.zebone.nhis.ma.pub.platform.zb.comm;

import javax.annotation.Resource;
import com.zebone.nhis.common.module.ma.msg.SysMsgRec;
import com.zebone.nhis.ma.pub.platform.socket.client.Hl7MsgSender;
import com.zebone.nhis.ma.pub.platform.socket.client.MsgBounder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ca.uhn.hl7v2.DefaultHapiContext;
import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.HapiContext;
import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.model.Segment;
import ca.uhn.hl7v2.model.v24.segment.MSA;
import ca.uhn.hl7v2.model.v24.segment.MSH;
import ca.uhn.hl7v2.model.v24.segment.QAK;
import ca.uhn.hl7v2.parser.Parser;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.ma.pub.platform.zb.service.SysMsgService;
import com.zebone.nhis.ma.pub.platform.zb.utils.MsgFileUtils;

/**
 * HL7消息发送处理
 * 
 * @author chengjia
 * 
 */
@Service
public class MsgSenderHander {

	@Resource
	private SysMsgService msgService;
	
	private static Logger loger = LoggerFactory.getLogger("nhis.lbHl7Log");
	
	Parser parser=null;
	
	public MSH RSPmsh = null;

	/**
	 * 向服务端发消息
	 * 
	 * @throws HL7Exception
	 */
	public void sendMsgToSvr(String msgId,String msg,Parser pParser,boolean saveMsg) throws HL7Exception {
		String saveFile = ApplicationUtils.getPropertyValue("msg.save.file","0");
		String savePath = ApplicationUtils.getPropertyValue("msg.save.path","c:\\hl7\\");

		if(pParser==null){
			HapiContext context = new DefaultHapiContext();
			parser = context.getGenericParser();
		}else{
			parser=pParser;
		}
		Message hapiMsg = parser.parse(msg);
		Segment segment = (Segment) hapiMsg.get("MSH");
		MSH msh=(MSH)segment;
		if(msgId==null||msgId.equals("")) msgId=msh.getMessageControlID().getValue();
		String msgType = msh.getMessageType().encode();
		loger.info("sendMsg:{}",msgType);
		    
	  //保存消息记录
		if(saveMsg){
			msgService.saveHl7Msg(hapiMsg, msg);
		}
		
		String fileName=msh.getMessageType().getMessageType().getValue()+"^"+msh.getMessageType().getTriggerEvent().getValue()+"-"+msgId;
		String fileContent=msg;

		msg=(char)0x0b+msg+(char)0x1c+(char)0x0d;

		SysMsgRec msgRec = new SysMsgRec();
		msgRec.setMsgId(msgId);
		msgRec.setMsgStatus("ACK");
		Hl7MsgSender.getInstance().sendMsg(new MsgBounder(msg,msgRec));

		//保存msg文件
		if(saveFile!=null&&saveFile.equals("1")){
			MsgFileUtils.fileCreate(fileName, fileContent, savePath,"hl7");
		}
		

	}
	
	/**
	 * 向服务端发消息
	 * 
	 * @param msg
	 * @throws HL7Exception
	 */
	public void sendMsgToSvr(String msg,boolean saveMsg) throws HL7Exception {
		sendMsgToSvr(null,msg,null,saveMsg);
	}
	
	public String getAckMsgTxt(String msgId,Message message) throws HL7Exception{
		Segment segment = (Segment) message.get("MSH");
		 		 
		if(segment==null)  return null;
		MSH msh;
		msh=(MSH)segment;
		String msgType = msh.getMessageType().encode();
		if(msh==null)  return null;
		segment = (Segment) message.get("MSA");
		if(segment==null)  return null;
		MSA msa;
		msa=(MSA)segment;
		if(msa==null)  return null;
		
		String errTxt=null;
		String ackCode=msa.getAcknowledgementCode().getValue();
		if(ackCode==null||!ackCode.equals("AA")){
			errTxt=msa.getTextMessage().getValue();
		}
		if("RSP^K21".equals(msgType)){
			segment = (Segment) message.get("QAK");
			if(segment==null)  return null;
			QAK qak;
			qak=(QAK)segment;
			if(qak==null)  return null;
			errTxt = qak.getQueryTag().getValue();
		}
		
		return errTxt;
	}

}
