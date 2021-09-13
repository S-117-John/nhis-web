package com.zebone.nhis.ma.pub.platform.sd.common;

import ca.uhn.hl7v2.DefaultHapiContext;
import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.HapiContext;
import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.model.Segment;
import ca.uhn.hl7v2.model.v24.segment.MSA;
import ca.uhn.hl7v2.model.v24.segment.MSH;
import ca.uhn.hl7v2.parser.Parser;
import com.zebone.nhis.common.module.ma.msg.SysMsgRec;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.ma.pub.platform.sd.receive.SDMsgReceive;
import com.zebone.nhis.ma.pub.platform.sd.service.SDSysMsgService;
import com.zebone.nhis.ma.pub.platform.sd.util.SDMsgFileUtils;
import com.zebone.nhis.ma.pub.platform.sd.util.SDMsgUtils;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

/**
 *
 * 服务端Handler
 * @author chengjia
 *
 */
@Service
public class SDServerHandler extends IoHandlerAdapter {

	//private final int IDLE = 300;
	private static Logger log = LoggerFactory.getLogger("nhis.lbHl7Log");

	@Resource
	private SDSysMsgService sDMsgService;

	@Resource
	private SDMsgReceive sDMsgReceive;

	@Resource
	private SDMsgUtils sDMsgUtils;

	static HapiContext context = null;
	static Parser parser=null;

	private static void initParser(){
		if(context==null) {
			context = new DefaultHapiContext();
		}
		if(parser==null) {
			parser = context.getGenericParser();
		}
	}

    @Override
    public void messageReceived(IoSession session, Object  message) throws Exception {
    	long startTime=System.currentTimeMillis();
		String logPrefix = "TEMPINFO[]";
    	log.info(Thread.currentThread().getName()+"》》》NHIS收到客户端发来的消息实例："+message.toString());
		log.info(logPrefix + " enter messageReceived:"+message.toString());
    	String saveFile = ApplicationUtils.getPropertyValue("msg.save.file","0");
    	String savePath = ApplicationUtils.getPropertyValue("msg.save.path","c:\\hl7\\");
    	initParser();
		Message hapiMsg=null;
		String sendMsg = "";
		String msgNew = null;
		//反馈消息id
		String msgIdAck = SDMsgUtils.getMsgId();
    	try {
	    	String msg = message.toString();
	    	String[] strs=msg.split("\\|");
			if(strs.length > 6){
				String msgDateTime=strs[6];
				if(msgDateTime!=null&&msgDateTime.length()>=14){
					strs[6]=msgDateTime.substring(0,14);
					msg=SDMsgUtils.arrayStrHl7Msg(strs);
				}
			}

			byte[] bs = msg.getBytes();
			int len=bs.length;
			if(len > 0){
				byte[] bsnew = new byte[len-1];
				if(bs[0]!=0x0b)
				{
					session.write((char)0x0b + sDMsgUtils.getUnDefinAE(message.toString(), "Message Not Start With [0X0B]"));
					return;
				}
				System.arraycopy(bs, 1, bsnew, 0, len - 1);
				msgNew = new String(bsnew);

				hapiMsg = parser.parse(msgNew);
				if(hapiMsg == null){
					log.info("hapiMsg is null");
					throw new HL7Exception("hapiMsg is null");
				}
				//保存接收的hl7消息记录到数据库
				log.info(logPrefix + " begin saveHl7MsgReceive:"+message.toString());
				SysMsgRec rec = sDMsgService.saveHl7MsgReceive(hapiMsg,msgNew);				
				log.info(logPrefix + " end saveHl7MsgReceive:"+message.toString());
				logPrefix = "TEMPINFO[" + rec.getMsgId() + "]MsgType:" + rec.getMsgType() + " ackMsgId:" + msgIdAck;
				
				//log.info("NHIS解析后的消息实例：  "+ msg);

				//处理数据		
				log.info(logPrefix + " begin handleSysMsgRec");
				String ackMsg = sDMsgReceive.handleSysMsgRec(rec);
				log.info(logPrefix + " end handleSysMsgRec");
				
				//处理结果,默认返回
				if("".equals(ackMsg)){
					log.info(logPrefix + " begin getAckMsg");
					String[] ackMsgs = sDMsgUtils.getAckMsg(msgIdAck,hapiMsg, logPrefix);
					log.info(logPrefix + " end getAckMsg");
					if(ackMsgs == null||ackMsgs.length!=2) {
						return;
					}
					log.info(logPrefix + " begin updateReceiveSysMsgRec");
					sDMsgService.updateReceiveSysMsgRec(msgIdAck,ackMsgs[0],"ACK");
					log.info(logPrefix + " end updateReceiveSysMsgRec");
					sendMsg=(char)0x0b+ackMsgs[1];
				}else{
					//处理失败，返回错误信息消息
					sendMsg = (char)0x0b+ackMsg;
				}
				//发送反馈消息
				log.info(logPrefix + " begin write:" + sendMsg);
				session.write(sendMsg);
				log.info(logPrefix + " end write");
				log.info(logPrefix + Thread.currentThread().getName() + "》》》处理时间："+ (double) (System.currentTimeMillis() - startTime) +"》》》NHIS服务端响应："+sendMsg);
				//保存msg文件
				if("1".equals(saveFile)){
					log.info(logPrefix + " begin saveFile");
					Segment segment = (Segment) hapiMsg.get("MSH");
					MSH msh=(MSH)segment;
					String msgId=msh.getMessageControlID().getValue();
					String fileName=msh.getMessageType().getMessageType().getValue()+"^"+msh.getMessageType().getTriggerEvent().getValue()+"-"+msgId;
					SDMsgFileUtils.fileCreate(fileName, msg, savePath,"hl7");
					log.info(logPrefix + " end saveFile");
				}
			}
		} catch (Exception e) {
			//e.printStackTrace();
			log.error(logPrefix, e);
			log.info(logPrefix + "NHIS内部逻辑处理失败："+ e.toString()+e.getMessage());
			if(hapiMsg == null)
			{
				//解析消息失败时,
				//case1,MSH段无误时
				String splitChar = "" + (char)0x0d;
				String[] strArray = msgNew.split(splitChar);
				String strMSH = "";
				for(int k = 0; k < strArray.length; k++)
				{
					String item = strArray[k];
					if(!"".equals(item))
					{
						if(item.indexOf("MSH") == 0)
						{
							strMSH = item;
							break;
						}
					}
				}
				msgNew = strMSH + splitChar;
				hapiMsg = parser.parse(msgNew);
			}
			String[] ackMsgs = sDMsgUtils.getAckMsgError(msgIdAck, hapiMsg, e.getMessage());
			sendMsg=(char)0x0b+ackMsgs[1];
			//发送反馈消息
			log.info(logPrefix + Thread.currentThread().getName() + " begin write error:" + sendMsg);
			session.write(sendMsg);
			log.info(logPrefix + Thread.currentThread().getName() + " end write error");
		}finally{
			//finally 块用于保存响应消息返回数据 sendMsg
			if(hapiMsg!=null){
				log.info(logPrefix + " begin finally");
				Segment segment = (Segment) hapiMsg.get("MSH");
				MSH msh=(MSH)segment;
				SysMsgRec msgrec = new SysMsgRec();
				msgrec.setTransType("receive_reponse");
				msgrec.setMsgType("ACK^"+msh.getMessageType().getTriggerEvent().getValue());
				msgrec.setMsgId(msgIdAck);
				msgrec.setTransDate(new Date());
				msgrec.setMsgContent(sendMsg.substring(1));
				Message ackMsg=parser.parse(sendMsg.substring(1));
				Segment msaSeg=(Segment) ackMsg.get("MSA");
				MSA msa=(MSA)msaSeg;
				if("AE".equals(msa.getAcknowledgementCode().getValue())){
					msgrec.setMsgStatus("ERROR");
					msgrec.setErrTxt(msa.getTextMessage().getValue());
				}else{
					msgrec.setMsgStatus("SAVE");
				}
				double execTime= (double) (System.currentTimeMillis() - startTime);
				msgrec.setExecTime(execTime);
				msgrec.setPkMsg(SDMsgUtils.getPk());
				sDMsgService.saveSysMsgRec(msgrec);
				log.info(logPrefix + " end finally");
				log.info(logPrefix + Thread.currentThread().getName() + " 线程状态："+Thread.currentThread().getState()+ "》》》处理时间："+ execTime);
			}
			else
			{
				//case2,MSH段也有问题时
				String errorAck = sDMsgUtils.getUnDefinAE(message.toString(), "消息格式有误");
				session.write((char)0x0b + errorAck);
				double execTime= (double) (System.currentTimeMillis() - startTime);
				log.info(logPrefix + Thread.currentThread().getName() + " 线程状态："+Thread.currentThread().getState()+ "》》》处理时间："+ execTime + " 错误消息返回值:" + errorAck);
			}
		}
	}

    @Override
    public void sessionClosed(IoSession session) throws Exception {
        // TODO Auto-generated method stub
        super.sessionClosed(session);
    }

    @Override
    public void sessionIdle(IoSession session, IdleStatus status)
            throws Exception {
        // TODO Auto-generated method stub
        super.sessionIdle(session, status);
    }

    @Override
    public void messageSent(IoSession session, Object message) throws Exception {

        System.out.println("发送的消息是："+message.toString());
        //super.messageSent(session, message);
    }

    @Override
    public void sessionCreated(IoSession session) throws Exception {

        super.sessionCreated(session);
    }

    @Override
    public void sessionOpened(IoSession session) throws Exception {
        super.sessionOpened(session);
    }

}