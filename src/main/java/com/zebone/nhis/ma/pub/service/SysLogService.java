package com.zebone.nhis.ma.pub.service;

import java.util.Date;
import java.util.List;

import com.zebone.nhis.common.module.ma.msg.SysMsgRec;
import com.zebone.nhis.ma.pub.platform.send.impl.syx.vo.SysEsbmsg;
import com.zebone.platform.modules.dao.DataBaseHelper;
/**
 * 平台消息日志服务
 * @author yx
 *
 */
public class SysLogService {
   /**
    * 保存消息日志(只保存发送失败内容)
    * @param sendType  send:发送；receive:接收
    * @param msgContent 可支持定时任务自动重新发送的完整消息体内容
    * @param errText 发送失败错误信息，含发送业务描述，例如：发送手术申请单错误，错误内容：....
    * @param msgType 消息类型，不需要的可以不传
    * @param msgId 消息ID，不需要的可以不传
    */
   public static void saveSysMsgRec(String sendType,String msgContent,String errText,String msgType,String msgId){
	   SysMsgRec rec = new SysMsgRec();
	   rec.setErrTxt(errText);
	   rec.setMsgContent(msgContent);
	   rec.setMsgId(msgId);
	   rec.setMsgStatus("0");//0:未发送，1:已发送
	   rec.setMsgType(msgType);
	   rec.setSysCode("HIS");
	   rec.setTransType(sendType);
	   rec.setTransDate(new Date());
	   DataBaseHelper.insertBean(rec);
   }

/**
 * 医嘱信息批量插入到数据库（包含医嘱，检验，检查，手术）
 * @param vos4save
 */
public static void saveSysEsbMsg(List<SysEsbmsg> vos4save) {
	if(vos4save!=null && !vos4save.isEmpty()){
		DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(SysEsbmsg.class),vos4save);
	}
}
   
}
