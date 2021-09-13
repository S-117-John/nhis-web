package com.zebone.nhis.common.support;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import com.zebone.nhis.common.module.base.message.SysMessage;
import com.zebone.nhis.common.module.base.message.SysMessageSend;
import com.zebone.nhis.common.module.base.support.MessageConstant;
import com.zebone.platform.Application;
import com.zebone.platform.common.support.NHISUUID;
import com.zebone.platform.common.support.User;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.modules.dao.DataBaseHelper;

public class MessageUtils {
	

	
	private final static Pattern pattern1 = Pattern.compile("\\$\\{.*?\\}");
	
	public static final String CODE_OPER_NS_ORDER_CHECK="050201";//医嘱核对
	

	/**
	 * 推送消息给多个用户用户
	 * @param message 消息    SysMessage类型中   noteMessage:消息内容           messageGroup : 消息分类 【消息分类可填，主要为  界面展示时可根据不同的业务，展示不同的消息显示格式】                      
	 * @param sends   用户  SysMessageSend类型中   sendTarget ：发送目标  用户名或者机器名    sendType ： 类型  0 代表人   1 代表机器 
	 */
    public static void sendMessageToUsers(SysMessage message  , List<SysMessageSend> sends){
    	if(message==null) return;
    	ApplicationUtils.getApplicationService().saveMessage(message, sends);
	}
    
	
	public static String getMessageByFormat(String format,Map data){
		

		StringBuffer sb = new StringBuffer();
		
		Matcher matcher = pattern1.matcher(format);
		
		int end = 0;
		sb = new StringBuffer();
		while (matcher.find())
		{
			 int start = matcher.start();
			 sb.append(format.substring(end, start));
		     end = matcher.end();
		     String match = format.substring(start, end);
		     String filed = match.substring(match.indexOf("{") + 1, match.indexOf("}")).toLowerCase();
		 
		     if(data.containsKey(filed)){
		    	 sb.append(data.get(filed));
		     }else{
		    	 sb.append("");
		     }
		}
		sb.append(format.substring(end));
		
		return sb.toString();
	}
	
	/***
	 * 保存消息和发送信息
	 */
	public static SysMessage saveMessageAndSend(SysMessage message  , List<SysMessageSend> sends){
		if(message==null) return null;
		User u = UserContext.getUser();
		
		//sys_message_send表全删全插
		String pkMessage = message.getPkMessage();
		if(StringUtils.isNotEmpty(pkMessage)){
			DataBaseHelper.execute("delete from sys_message_send where PK_MESSAGE = ?", new Object[]{pkMessage});
		}
		
		message.setPkSender(u.getPkEmp());
		message.setPkSenderDept(u.getPkDept());
		//message.setPkMessage(null);
//		if(message.getDateSend() == null){
//			message.setDateSend(new Date());
//		}
		if(StringUtils.isNotEmpty(pkMessage)){
			DataBaseHelper.updateBeanByPk(message,false);
		}else{
			DataBaseHelper.insertBean(message);
		}
		
		String euScope = message.getEuScope();
		if(!MessageConstant.EU_SCOPE_ALL.equals(euScope)){ //发送全员不写发送表
			for(SysMessageSend send : sends){
				send.setPkMessage(message.getPkMessage());
				send.setCreator(u.getPkEmp());
				send.setCreateTime(new Date());
				send.setTs(new Date());
				send.setModifier(u.getPkEmp());
				send.setDelFlag("0");
				send.setPkOrg(u.getPkOrg());
				send.setPkMessageSend(NHISUUID.getKeyId());
				//DataBaseHelper.insertBean(send);
			}
			DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(SysMessageSend.class), sends);
		}
		
		return message;
	}

	/**
	 * 医嘱消息推送
	 * @param subject 主题（新医嘱、停止医嘱、作废医嘱)
	 * @param content 内容（医嘱名称)
	 * @param pkPv 就诊编码
	 * @param pkWard 病区编码
	 * @param flag 是否传入功能编码
	 * @return msg
	 */
	public static SysMessage createSysMessageOrd(String subject,String content,String pkPv,String pkWard,boolean flag){
		
		String[] scopes={pkWard};
		Date now=new Date();
		Calendar cal = Calendar.getInstance();   
		cal.setTime(now);
		cal.add(Calendar.MINUTE, 100);
		//添加校验，如果该患者存在未读医嘱信息，则不再发送
		String sqlStr="";
		if(Application.isSqlServer()){
			sqlStr = "select count(1)  from sys_message_send  sender "+ 
					" inner join sys_message msg on sender.pk_message = msg.pk_message "+ 
					" where CONVERT(VARCHAR, msg.param_navigation)  = ?  and flag_read='0'";
		}else{
			sqlStr = "select count(1)  from sys_message_send  sender "+ 
					" inner join sys_message msg on sender.pk_message = msg.pk_message "+ 
					" where msg.param_navigation = ?  and flag_read='0'";
		}
		Integer unReadCount = DataBaseHelper.queryForScalar(sqlStr, Integer.class, pkPv);
		
		if(unReadCount>0) return null;
		SysMessage msg= null;
		if(flag){
			msg = createSysMessage(subject,content,"1","2",3,CODE_OPER_NS_ORDER_CHECK,pkPv,null,scopes);

		}else{
			msg = createSysMessage(subject,content,"1","2",3,null,pkPv,null,scopes);
		}

		return msg;
	}
	
	/**
	 * 系统消息推送表
	 * @param subject 主题
	 * @param content 内容
	 * @param sendType 发送方式  1-即时；2-定时
	 * @param scope 接收范围 1-全员；2-科室；3-指定人员
	 * @param interval 提醒间隔（分钟）
	 * @param codeOper 关联功能编码
	 * @param navigation 功能导航参数 表示功能导航参数，一般为记录主键（例如：pk_pv)或为JSON格式表示的一个对象
	 * @param dateExpired 有效截止时间
	 * @param scopes 根据接收范围区分：1（全员）-null；2（科室）-科室主键列表；3（指定人员）-人员主键列表
	 * @return msg
	 */
	public static SysMessage createSysMessage(String subject,String content,String sendType,String scope,int interval,String codeOper,String navigation,Date dateExpired,String[] scopes){
		User u = UserContext.getUser();
		
		SysMessage msg=new SysMessage();

		//后面有人加了有id去更新的判断
		//msg.setPkMessage(NHISUUID.getKeyId());
		//消息分类
		msg.setMessageGroup(null);
		//消息类型 1-系统消息；2-短信；3-微信
		msg.setEuMessageType("1");
		//消息主题
		msg.setSubjectMessage(subject);
		//消息内容
		msg.setContentMessage(content);
		//发送方式  1-即时；2-定时
		msg.setEuSendType(sendType);
		//接收范围 1-全员；2-科室；3-指定人员
		msg.setEuScope(scope);
		//发送人
		msg.setPkSender(u.getPkEmp());
		//发送科室
		msg.setPkSenderDept(u.getPkDept());
		//发送时间
		msg.setDateSend(new Date());
		//提醒间隔（分钟）
		msg.setRemindInterval(interval);
		//关联功能编码
		msg.setCodeOper(codeOper);
		//功能导航参数 表示功能导航参数，一般为记录主键（例如：pk_pv)或为JSON格式表示的一个对象
		msg.setParamNavigation(navigation);
		//有效截止时间
		msg.setDateExpired(dateExpired);
		//处理状态 0-新消息；1-消息已缓存
		msg.setEuHandleStatus("0");
		//根据接收范围区分：1（全员）-null；2（科室）-科室主键列表；3（指定人员）-人员主键列表
		msg.setScopes(scopes);
		
		return msg;
	}
}
