package com.zebone.nhis.ma.pub.platform.sd.mq;

import javax.annotation.Resource;
import javax.jms.JMSException;  
import javax.jms.Message;  
import javax.jms.MessageListener;  
import javax.jms.TextMessage;  

import org.slf4j.Logger;

import com.zebone.nhis.ma.pub.platform.sd.common.SDMsgSenderHander;

import ca.uhn.hl7v2.HL7Exception;
  
/**
 * 消息监听器
 * @author chengjia
 *
 */
public class SDQueueMsgListener implements MessageListener {  
  
	private static Logger loger = org.slf4j.LoggerFactory.getLogger(SDQueueMsgListener.class.getName());
	
	@Resource
	private	SDMsgSenderHander sDMsgSenderHander;
	
    //当收到消息后，自动调用该方法  
    @Override  
    public void onMessage(Message message) {  
          
        TextMessage tm = (TextMessage) message;  
        try {
        	String receiveMsg=tm.getText();
        	if(receiveMsg==null||receiveMsg.equals("")) return;
        	String msg=receiveMsg;
        	String[] strs=receiveMsg.split("@@###@@");
        	String saveMsgStr="0";
        	if(strs!=null&&strs.length>1){
        		msg=strs[1];
        		saveMsgStr=strs[0];
        	}
        	boolean saveMsg=false;
        	if(saveMsgStr!=null&&(saveMsgStr.equals("1")||saveMsgStr.toLowerCase().equals("true"))) saveMsg=true;
        	loger.info("QueueMsgListener监听到了文本消息：\t" + receiveMsg);            
        	
        	//发送消息
        	sDMsgSenderHander.sendMsgToSvr(msg,saveMsg);

        } catch (JMSException e){ 
        	e.printStackTrace();   
        } catch (HL7Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}   
    }  
  
} 