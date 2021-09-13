package com.zebone.nhis.base.msg.service;

import com.alibaba.fastjson.JSON;
import com.zebone.nhis.base.msg.vo.MessageSendParam;
import com.zebone.nhis.common.module.base.message.SysMessage;
import com.zebone.nhis.common.module.base.message.SysMessageSend;
import com.zebone.nhis.common.module.base.support.MessageConstant;
import com.zebone.nhis.common.support.MessageUtils;
import com.zebone.platform.common.support.NHISUUID;
import com.zebone.platform.common.support.User;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class BaseMsgService {

	public void sendMessage(String param , IUser user){
		
		MessageSendParam mParam = JsonUtil.readValue(param, MessageSendParam.class);
		
		MessageUtils.sendMessageToUsers(mParam.getMessage(), mParam.getSends());
	
		
	}
	
	/***
	 * 交易号：001004001002<br>
	 * 保存消息和发送信息<br>
	 * <pre>
	 * 参数scopes：根据接收范围区分：1（全员）-null；2（科室）-科室主键列表；3（指定人员）-人员主键列表
	 * 1、保存或者修改，即保存草稿，不发送,处理状态设为2；修改时sys_message_send全删全插
	 * 2、保存需要验证状态，如果已发送（非草稿状态），不能再保存；
	 * 3、如果是及时消息，保存时发送时间为空，发送时发送时间为当前时间；
	 * </pre>
	 * 
	 * @param param
	 * @param user
	 */
	public SysMessage saveMessageAndSend(String param , IUser user){
		//全员不写send表，科室一人看就算看了
		
		SysMessage message = JsonUtil.readValue(param, SysMessage.class);
		List<SysMessageSend> sendList = new ArrayList<SysMessageSend>();
		String euScope = message.getEuScope();
		String euTargetType = ""; //发送类型
		//StringBuffer sql = new StringBuffer("select pk_emp from bd_ou_employee where flag_active = '1' and del_flag = '0' ");
		
		String[] scopes = message.getScopes(); //科室或人员主键
		//List<Map<String,Object>> pkEmpList = new ArrayList<Map<String,Object>>();
		//保存验证状态，如果已发送，不能再保存或者发送；
		String pkMessage = message.getPkMessage();
		if(StringUtils.isNotEmpty(pkMessage)){
			Map<String,Object> statusMap = DataBaseHelper.queryForMap("select EU_HANDLE_STATUS from sys_message where PK_MESSAGE = ?",  new Object[]{pkMessage});
			if(statusMap!=null&&statusMap.get("euHandleStatus") != null && !"2".equals(statusMap.get("euHandleStatus").toString())){
				throw new BusException("该消息已发送，无法继续操作！");
			}
		}
		
		int length;
		if(scopes != null){
			length = scopes.length;
		}else{
			length = 0;
		}
		if(MessageConstant.EU_SCOPE_ALL.equals(euScope)){ //全员
			//不写发送表			
		}else if(MessageConstant.EU_SCOPE_DEPT.equals(euScope)){//科室
			euTargetType = MessageConstant.EU_TARGET_TYPE_DEPT;
			if(length == 0){
				throw new BusException("接收范围为科室时，科室主键不能为空！");				
			}
		}else if(MessageConstant.EU_SCOPE_STAFF.equals(euScope)){ //人员
			euTargetType = MessageConstant.EU_TARGET_TYPE_STAFF;
			if(length == 0){
				throw new BusException("接收范围为人员时，人员主键不能为空！");
			}
		}
		
		for(int i=0;i<length;i++){
			SysMessageSend send = new SysMessageSend();
			send.setPkMessageSend(NHISUUID.getKeyId());
			send.setSendTarget(scopes[i]);
			send.setEuTargetType(euTargetType);
			send.setEuSendStatus(MessageConstant.EU_SEND_STATUS_NO);
			send.setFlagRead("0");
			
			sendList.add(send);
		}
		message.setEuHandleStatus("2"); //草稿
		message = MessageUtils.saveMessageAndSend(message, sendList);
		return message;
	}
	
	/***
	 * 交易号：001004001003<br>
	 * 获取消息列表<br>
	 * <pre>
	 * 参数type：1-发送列表  2-接收列表
	 * 1、发送列表：sys_message.del_flag='0', sys_message.pk_sender=user.pkEmp;
	 * 2、接收列表：sys_message_send.del_flag='0',sys_message.eu_scope='1'(全员) or sys_message_send.send_target=user.pkOrg(科室) or sys_message_send.send_target=user.pkEmp(人员)
	 * </pre>
	 * 
	 * @param param
	 * @param user
	 * @return List<Map<String, Object>>
	 */
	public List<Map<String, Object>> getMessageAndSendList(String param , IUser user){
		MessageSendParam msgParam = JsonUtil.readValue(param, MessageSendParam.class);
		String type = msgParam.getType();
		User u = UserContext.getUser();
		
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Map<String, Object> mapParam = new HashMap<String, Object>();
		mapParam.put("pkEmp", u.getPkEmp());
		mapParam.put("pkOrg", u.getPkOrg());
		mapParam.put("pkDept", u.getPkDept());
		if("1".equals(type)){ //发送
			list = DataBaseHelper.queryForList("select m.* from sys_message m where m.del_flag='0' and m.pk_sender=:pkEmp order by m.ts desc", mapParam);
		}else if("2".equals(type)){//接收(只查询已读消息)
			list = DataBaseHelper.queryForList("select t.* from (select m.*, '' as pk_message_send, case m.EU_HANDLE_STATUS when '3' then '1' else '0' end as FLAG_READ from sys_message m where m.eu_scope = '1' and m.eu_handle_status = '1' UNION all "
					+ "select m.*, s.pk_message_send, s.FLAG_READ from sys_message_send s left join sys_message m on s.pk_message = m.pk_message "
					+ "where s.del_flag='0' and m.eu_handle_status = '1' and m.eu_scope!='1' and (s.send_target=:pkDept or s.send_target=:pkEmp)) t "
					+ "order by t.ts desc", mapParam);
		}
		return list;
	}
	
	/***
	 * 交易号：001004001004<br>
	 * 接收消息
	 * 
	 * @param param
	 * @param user
	 */
	public void updateMessageSend(String param , IUser user){
		List<SysMessageSend> messageList = JSON.parseArray(param, SysMessageSend.class);
		User u = UserContext.getUser();
		Map<String, Object> mapParam = new HashMap<String, Object>();
		mapParam.put("modifier", u.getPkEmp());
		mapParam.put("ts", new Date());
		mapParam.put("pkDept", u.getPkDept());
		for(SysMessageSend messageSend : messageList){
			String pkMessage = messageSend.getPkMessage();
			String pkMessageSend = messageSend.getPkMessageSend();
			if(StringUtils.isNotEmpty(pkMessageSend)){
				mapParam.put("pkMessageSend", pkMessageSend);
				DataBaseHelper.update("update sys_message_send set EU_SEND_STATUS = '1', PK_READER =:modifier, PK_READER_DEPT =:pkDept, DATE_READ =:ts, FLAG_READ = '1', modifier=:modifier, ts=:ts  where PK_MESSAGE_SEND=:pkMessageSend", mapParam);
			}
			//如果明细表都已读过，把主表设置为已读
			Integer count = DataBaseHelper.queryForScalar("select count(1) from sys_message_send where FLAG_READ = '0' and DEL_FLAG = '0' and PK_MESSAGE = ?", Integer.class, new Object[]{pkMessage});
			if(count == null || count == 0 || StringUtils.isEmpty(pkMessageSend)){
				mapParam.put("pkMessage", pkMessage);
				DataBaseHelper.update("update sys_message set EU_HANDLE_STATUS = '3', ts =:ts, MODIFIER =:modifier where PK_MESSAGE =:pkMessage", mapParam);
			}
		}
	}
	
	/***
	 * 交易号：001004001005<br>
	 * 删除消息
	 * 
	 * @param param
	 * @param user
	 */
	public void deleteMessageOrSend(String param , IUser user){
		List<SysMessageSend> messageList = JSON.parseArray(param, SysMessageSend.class);
		for(SysMessageSend message : messageList){
			String pkMessage = message.getPkMessage();
			String pkMessageSend = message.getPkMessageSend();
			User u = UserContext.getUser();
			Map<String, Object> mapParam = new HashMap<String, Object>();
			mapParam.put("modifier", u.getPkEmp());
			mapParam.put("ts", new Date());
			
			if(StringUtils.isNotBlank(pkMessage)){
				Map<String, Object> messageMap = DataBaseHelper.queryForMap("select pk_message,eu_handle_status from sys_message where pk_message = ?", new Object[]{pkMessage});
				if(messageMap != null){
					//新消息跟草稿可删除
					if(MessageConstant.EU_HANDLE_STATUS_NEW.equals(messageMap.get("euHandleStatus").toString()) || MessageConstant.EU_HANDLE_STATUS_MANUSCRIPT.equals(messageMap.get("euHandleStatus").toString())){ //新消息
						mapParam.put("pkMessage", pkMessage);								
						DataBaseHelper.update("update sys_message set modifier=:modifier, ts=:ts, del_flag='1' where pk_message=:pkMessage", mapParam);
					}else{
						throw new BusException("存在已缓存或已接收消息，无法删除！");
					}
				}else{
					throw new BusException("参数错误，无法获取消息信息！");
				}
			}else if(StringUtils.isNotBlank(pkMessageSend)){
				mapParam.put("pkMessageSend", pkMessageSend);	
				DataBaseHelper.update("update sys_message_send set modifier=:modifier, ts=:ts, del_flag='1' where pk_message_send=:pkMessageSend", mapParam);
			}
		}				
	}
	
	/***
	 * 交易号：001004001006<br>
	 * 根据消息主键获取消息详情
	 * 
	 * @param param
	 * @param user
	 * @return SysMessage
	 */
	public SysMessage getMessageAndSend(String param , IUser user){
		SysMessage message = JsonUtil.readValue(param, SysMessage.class);
		String pkMessage = message.getPkMessage();
		message = DataBaseHelper.queryForBean("select * from sys_message where DEL_FLAG = '0' and PK_MESSAGE = ?", SysMessage.class, new Object[]{pkMessage});
		List<SysMessageSend> sendList = DataBaseHelper.queryForList("select * from sys_message_send where DEL_FLAG = '0' and PK_MESSAGE = ? order by ts", SysMessageSend.class, new Object[]{pkMessage});
		if(CollectionUtils.isEmpty(sendList)){
			//messageMap.put("scopes", null);
		}else{
			String[] scopes = new String[sendList.size()];
			int i = 0;
			for(SysMessageSend send : sendList){
				scopes[i] = send.getSendTarget();
				i++;
			}
			message.setScopes(scopes);			
		}
		return message;
	}
	
	/***
	 * 交易号：001004001007<br>
	 * 更新消息处理状态（支持批量更新）<br>
	 * <pre>
	 * 1、发送时间为当前时间；
	 * 2、发送时把消息状态置为0（新消息）；
	 * 3、发送时需要验证状态，已发送（非草稿状态）的不能再次发送;
	 * </pre>
	 * 
	 * @param param
	 * @param user
	 * @return Map<String, Object>
	 */
	public void updateHandleStatus(String param , IUser user){
		List<SysMessage> list = JSON.parseArray(param,SysMessage.class);
		User u = UserContext.getUser();
		for(SysMessage message : list){
			String pkMessage = message.getPkMessage();
			if(StringUtils.isNotEmpty(pkMessage)){
				Map<String,Object> statusMap = DataBaseHelper.queryForMap("select EU_HANDLE_STATUS from sys_message where PK_MESSAGE = ?", pkMessage);
				if(statusMap!=null&&statusMap.get("euHandleStatus") != null && !"2".equals(statusMap.get("euHandleStatus").toString())){
					throw new BusException("该消息已发送，无法继续操作！");
				}
			}
			message.setEuHandleStatus("0");//新消息
			if("1".equals(message.getEuSendType())){ //发送方式  1-即时；2-定时
				message.setDateSend(new Date()); //定时时发送时间前台传过来
			}
			if(StringUtils.isEmpty(message.getPkSender())){
				message.setPkSender(u.getPkEmp());
			}
			if(StringUtils.isEmpty(message.getPkSenderDept())){
				message.setPkSenderDept(u.getPkDept());
			}
			message.setTs(new Date());
		}
		DataBaseHelper.batchUpdate(DataBaseHelper.getUpdateSql(SysMessage.class), list);
	}
	
}
