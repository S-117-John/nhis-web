package com.zebone.nhis.ma.lb.handler;

import ca.uhn.hl7v2.DefaultHapiContext;
import ca.uhn.hl7v2.HapiContext;
import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.model.Segment;
import ca.uhn.hl7v2.model.v24.segment.MSH;
import ca.uhn.hl7v2.parser.Parser;
import com.zebone.nhis.common.module.ma.msg.SysMsgRec;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.ma.lb.service.CpMsgService;
import com.zebone.nhis.ma.lb.service.SelfMsgService;
import com.zebone.nhis.ma.pub.platform.zb.receive.MsgReceive;
import com.zebone.nhis.ma.pub.platform.zb.utils.MsgFileUtils;
import com.zebone.nhis.ma.pub.platform.zb.utils.MsgUtils;
import org.slf4j.Logger;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

/**
 * 
 * 服务端Handler
 * @author chengjia
 *
 */
public class LbServerHandler extends IoHandlerAdapter {
	
	private final int IDLE = 300;
	private final Logger log = org.slf4j.LoggerFactory.getLogger(LbServerHandler.class);
	
	/*@Resource
	private SysMsgService msgService;*/
	
//	
//	@Resource
//	private MsgUtils msgCreate;
	
	@Resource
	private SelfMsgService msgReceiveLbZzj;
	
	@Resource
	private CpMsgService cpMsgService;
	
	@Resource
	private MsgReceive msgReceive;
	
	private Parser parser=null;

	@PostConstruct
	public void initParser(){
		HapiContext context = new DefaultHapiContext();
		parser = context.getGenericParser();
	}

    @Override
    public void messageReceived(IoSession session, Object message)
            throws Exception {
    	String saveFile = ApplicationUtils.getPropertyValue("msg.save.file","0");
    	String savePath = ApplicationUtils.getPropertyValue("msg.save.path","c:\\hl7\\");
    	// TODO Auto-generated method stub
        //super.messageReceived(session, message);
        //System.out.println("message :"+message);
    	try {
	    	String msg=message.toString();
	    	
	    	String[] strs=msg.split("\\|");
			if(strs!=null&&strs.length>6){
				String msgDateTime=strs[6];
				if(msgDateTime!=null&&msgDateTime.length()>=14){
					strs[6]=msgDateTime.substring(0,14);
					msg=MsgUtils.arrayStrHl7Msg(strs);

				}
			}
	    	//去掉时区
			//msg=msg.replaceAll("\\+0800000", "");
			//msg=msg.replaceAll("\\+0800", "");
			byte[] bs = msg.getBytes();
			int len=bs.length;
			if(bs!=null&&len>0){
				byte[] bsnew = new byte[len-1];
				if(bs[0]!=0x0b){
					return;
				}
				for(int i=1;i<len;i++){
					bsnew[i-1]=bs[i];
				}
				
				String msgNew=new String(bsnew);
				
				
				Message hapiMsg=null;
				try {
					hapiMsg = parser.parse(msgNew);
					
				} catch (Exception e) {
					log.info("parse error");
					e.printStackTrace();
				}
				 
				if(hapiMsg==null){
					log.info("hapiMsg is null");
					return;
				}
	
				//SysMsgRec rec = msgService.saveHl7MsgReceive(hapiMsg,msgNew);
				SysMsgRec rec=null;
				//应平台要求，ACK返回msgId改为新产生的ID
				String msgIdAck=rec.getMsgId().toString();
				msgIdAck=MsgUtils.getMsgId();
				String[] ackMsgs = MsgUtils.getAckMsg(msgIdAck,hapiMsg);
				
				if(ackMsgs==null||ackMsgs.length!=2) return;				
				
				log.info("NHIS收到客户端发来的消息为" + "  " + msg);
				
				//msgService.updateReceiveSysMsgRec(rec.getMsgId().toString(),ackMsgs[0],"ACK");
				
				String sendMsg="";
				
				String ZZJmsg=msgReceiveLbZzj.handleSysMsgRecZZJ(rec);//灵璧自主机交互
				cpMsgService.handleSysMsgRecExtCp(rec);
				if(!ZZJmsg.isEmpty()){
					sendMsg=(char)0x0b+ZZJmsg;
				}else{
					sendMsg=(char)0x0b+ackMsgs[1];
				}
				session.write(sendMsg);
				
				//保存msg文件
				if(saveFile!=null&&saveFile.equals("1")){
					Segment segment = (Segment) hapiMsg.get("MSH");
					MSH msh=(MSH)segment;
					String msgId=msh.getMessageControlID().getValue();
					String fileName=msh.getMessageType().getMessageType().getValue()+"^"+msh.getMessageType().getTriggerEvent().getValue()+"-"+msgId;
					String fileContent=msg;
					MsgFileUtils.fileCreate(fileName, fileContent, savePath,"hl7");
				}
				
				//处理数据
				msgReceive.handleSysMsgRec(rec);
			}
			
		} catch (Exception e) {
			log.info("error");
			e.printStackTrace();
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