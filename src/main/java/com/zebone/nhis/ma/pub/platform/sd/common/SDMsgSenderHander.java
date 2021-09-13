package com.zebone.nhis.ma.pub.platform.sd.common;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;

import ca.uhn.hl7v2.DefaultHapiContext;
import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.HapiContext;
import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.model.Segment;
import ca.uhn.hl7v2.model.v24.segment.MSH;
import ca.uhn.hl7v2.parser.Parser;

import com.zebone.nhis.common.module.ma.msg.SysMsgRec;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.ma.pub.platform.sd.service.SDSysMsgService;
import com.zebone.nhis.ma.pub.platform.sd.util.SDMsgFileUtils;
import com.zebone.nhis.ma.pub.platform.socket.client.Hl7MsgSender;
import com.zebone.nhis.ma.pub.platform.socket.client.MsgBounder;

import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioSocketConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;


/**
 * HL7消息发送处理(灵璧复制版本)
 * 
 * @author chengjia
 * 
 */
@Service
public class SDMsgSenderHander {

	@Resource
	private SDSysMsgService sDSysMsgService;
	
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
		if(msgId==null||msgId.equals("")) msgId=msh.getMessageControlID().getValue();;
		String msgType = msh.getMessageType().encode();
		loger.info("sendMsg:"+msgType);
		//保存消息记录
		if(saveMsg) sDSysMsgService.saveHl7Msg(hapiMsg, msg);
		
		msg=(char)0x0b+msg+(char)0x1c+(char)0x0d;
        loger.info("sendMsg:{},转化消息：{}",msgType,msg);
		SysMsgRec msgRec = new SysMsgRec();
		msgRec.setMsgId(msgId);
		msgRec.setMsgStatus("ACK");
		Hl7MsgSender.getInstance().sendMsg(new MsgBounder(msg,msgRec));
		//保存msg文件
		if(saveFile!=null&&saveFile.equals("1")){
			String fileName=msh.getMessageType().getMessageType().getValue()+"^"+msh.getMessageType().getTriggerEvent().getValue()+"-"+msgId;
			String fileContent=msg;
			SDMsgFileUtils.fileCreate(fileName, fileContent, savePath,"hl7");
		}
	}
	
	/**
	 * 向服务端同步发消息
	 * 
	 * @throws HL7Exception
	 */
	public Object sendMsgToSvrFor(String msgId,String msg,Parser pParser,boolean saveMsg) throws HL7Exception {
		Object restMsg=new Object();
		NioSocketConnector connector =null;
		try {
			String host = ApplicationUtils.getPropertyValue("msg.send.host","127.0.0.1");
			int port = Integer.parseInt(ApplicationUtils.getPropertyValue("msg.send.port", "5010"));
			int timeout = Integer.parseInt(ApplicationUtils.getPropertyValue("msg.send.timeout", "10000"));
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
			if(msgId==null||msgId.equals("")) msgId=msh.getMessageControlID().getValue();;
			String msgType = msh.getMessageType().encode();
			loger.info("sendMsg:"+msgType);
			//消息发送保存
			if(saveMsg) sDSysMsgService.saveHl7Msg(hapiMsg, msg);
			
			
			msg=(char)0x0b+msg+(char)0x1c+(char)0x0d;
			// 初始化通信
			connector = new NioSocketConnector();
			connector.getFilterChain().addLast("codec",new ProtocolCodecFilter(new SDHl7CodeFactory(Charset.forName("GBK")))); // 璁剧疆缂栫爜杩囨护鍣�
			connector.getFilterChain().addLast("logging", new LoggingFilter());
			connector.setHandler(new SDClientHandler());

			//同步接受响应消息
			connector.getSessionConfig().setUseReadOperation(true);
			
			ConnectFuture cf = connector.connect(new InetSocketAddress(host, port));// 寤虹珛杩炴帴
			if(cf==null){
				loger.info("ConnectFuture is null...");
				return restMsg;
			}
			IoSession session;
			cf.awaitUninterruptibly(timeout);// 等待时间
			session=cf.getSession();
			if(session==null){
				loger.info("session is null...");
				return restMsg;
			}
			session.write(msg);// 消息发送
			
			Object result = session.read().awaitUninterruptibly().getMessage();
			if(result==null){
				return restMsg;
			}
			sDSysMsgService.updateSysMsgRec(msgId,"ACK");
			//文件处理
			if(saveFile!=null&&saveFile.equals("1")){
				String fileName=msh.getMessageType().getMessageType().getValue()+"^"+msh.getMessageType().getTriggerEvent().getValue()+"-"+msgId;
			    String fileContent=msg;
				SDMsgFileUtils.fileCreate(fileName, fileContent, savePath,"hl7");
			}
		   return result;
		} catch (Exception e) {
			loger.info("消息同步发送异常："+e.getMessage());
			e.printStackTrace();
		}finally{
			//连接关闭
			connector.dispose();
		}
		return restMsg;
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
	
//	public String getAckMsgTxt(String msgId,Message message) throws HL7Exception{
//		Segment segment = (Segment) message.get("MSH");
//		 		 
//		if(segment==null)  return null;
//		MSH msh;
//		msh=(MSH)segment;
//		String msgType = msh.getMessageType().encode();
//		if(msh==null)  return null;
//		segment = (Segment) message.get("MSA");
//		if(segment==null)  return null;
//		MSA msa;
//		msa=(MSA)segment;
//		if(msa==null)  return null;
//		
//		String errTxt=null;
//		String ackCode=msa.getAcknowledgementCode().getValue();
//		if(ackCode==null||!ackCode.equals("AA")){
//			errTxt=msa.getTextMessage().getValue();
//		}
//		if("RSP^K21".equals(msgType)){
//			segment = (Segment) message.get("QAK");
//			if(segment==null)  return null;
//			QAK qak;
//			qak=(QAK)segment;
//			if(qak==null)  return null;
//			errTxt = qak.getQueryTag().getValue();
//		}
//		
//		return errTxt;
//	}

}
