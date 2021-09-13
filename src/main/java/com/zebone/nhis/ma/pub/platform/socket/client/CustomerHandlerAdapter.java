package com.zebone.nhis.ma.pub.platform.socket.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.uhn.hl7v2.model.v24.segment.MSA;

import com.zebone.nhis.common.module.ma.msg.SysMsgRec;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.ma.pub.platform.zb.service.SysMsgService;
import com.zebone.platform.modules.core.spring.ServiceLocator;


public class CustomerHandlerAdapter implements CustomerHandler{
	private static final Logger logger = LoggerFactory.getLogger("nhis.lbHl7Log");

    private SysMsgService msgService;

    public CustomerHandlerAdapter(){
        this.msgService = ServiceLocator.getInstance().getBean(SysMsgService.class);
    }
    @Override
    public void completeSend(MsgBounder msgBounder) {
    }

    @Override
    public void afterReceived(MsgBounder msgBounder,Object receivedData) {
        if(msgBounder!= null){
            Object serviceData = msgBounder.getServiceData();
            if(serviceData!=null && serviceData instanceof SysMsgRec){
                SysMsgRec msgRec = (SysMsgRec)serviceData;
                msgBounder.setMsgAck(receivedData);
                logger.info("线程名：{},msgId：{},状态：{}",Thread.currentThread().getName(),msgRec.getMsgId(),msgRec.getMsgStatus());
                msgService.updateSysMsgRec(msgRec.getMsgId(),msgRec.getMsgStatus());
                return;
            }
        }
        if(receivedData!=null){
        	Map<String,List<Map<String,Object>>> resMap=resolueMessage(receivedData.toString());
        	if(resMap!=null && resMap.containsKey("MSA")){
        		Map<String,Object> msaMap=resMap.get("MSA").get(0);
        		if(msaMap!=null){
        			String msgStatus="";
        			String msgId=CommonUtils.getString(msaMap.get("2"), "");
        			if("AA".equals(msaMap.get("1"))){
        				msgStatus="ACK";
        			}else{
        				msgStatus="ERROR";
        			}
        			logger.info("线程名：{},msgId：{},状态：{}",Thread.currentThread().getName(),msgId,msgStatus);
        			msgService.updateSysMsgRec(msgId,msgStatus);
        		}
        	}
        }
    }
    
    /**
	 * 接收消息通用解析方法
	 * @param message
	 * @return
	 */
	private  Map<String,List<Map<String,Object>>> resolueMessage(String message){
		Map<String,List<Map<String,Object>>> resMap=new HashMap<String,List<Map<String,Object>>>();
		String [] splitMsg=message.split("\r");
		for (int i = 0; i < splitMsg.length; i++) {
			List<Map<String,Object>> list=new ArrayList<>();
			String [] splitforClass=splitMsg[i].split("\\|");
			if(resMap.containsKey(splitforClass[0])){
				list=resMap.get(splitforClass[0]);
			}

			Map<String,Object> splitval=new HashMap<String,Object>();
			for (int j = 0; j < splitforClass.length; j++) {
				splitval.put(String.valueOf(j), splitforClass[j]);
			}
			list.add(splitval);
			resMap.put(splitforClass[0], list);
		}
		return resMap;
	}
}
