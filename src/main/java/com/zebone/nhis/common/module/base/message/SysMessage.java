package com.zebone.nhis.common.module.base.message;

import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;


/**
 * Table: SYS_MESSAGE  - 系统消息推送表 
 *
 * @since 2016-10-09 01:38:17
 */
@Table(value="SYS_MESSAGE")
public class SysMessage extends BaseModule{
	
	private static final long serialVersionUID = 1L;

	@PK
	@Field(value="PK_MESSAGE",id=KeyId.UUID)
    private String pkMessage;

    /** MESSAGE_GROUP - 消息分类 */
	@Field(value="MESSAGE_GROUP")
    private String messageGroup;

    /** EU_MESSAGE_TYPE - 消息类型 1-系统消息；2-短信；3-微信 */
	@Field(value="EU_MESSAGE_TYPE")
    private String euMessageType;

    /** SUBJECT_MESSAGE - 消息主题 */
	@Field(value="SUBJECT_MESSAGE")
    private String subjectMessage;

    /** CONTENT_MESSAGE - 消息内容 */
	@Field(value="CONTENT_MESSAGE")
    private String contentMessage;

    /** EU_SEND_TYPE - 发送方式  1-即时；2-定时 */
	@Field(value="EU_SEND_TYPE")
    private String euSendType;

    /** EU_SCOPE - 接收范围 1-全员；2-科室；3-指定人员 */
	@Field(value="EU_SCOPE")
    private String euScope;

    /** PK_SENDER - 发送人 */
	@Field(value="PK_SENDER")
    private String pkSender;

    /** PK_SENDER_DEPT - 发送科室 */
	@Field(value="PK_SENDER_DEPT")
    private String pkSenderDept;

    /** DATE_SEND - 发送时间 */
	@Field(value="DATE_SEND")
    private Date dateSend;

    /** REMIND_INTERVAL - 提醒间隔 */
	@Field(value="REMIND_INTERVAL")
    private Integer remindInterval;

    /** CODE_OPER - 关联功能编码 */
	@Field(value="CODE_OPER")
    private String codeOper;

    /** PARAM_NAVIGATION - 功能导航参数 表示功能导航参数，一般为记录主键（例如：pk_pv)或为JSON格式表示的一个对象 */
	@Field(value="PARAM_NAVIGATION")
    private String paramNavigation;

    /** DATE_EXPIRED - 有效截止时间 */
	@Field(value="DATE_EXPIRED")
    private Date dateExpired;

    /** EU_HANDLE_STATUS - 处理状态 0-新消息；1-消息已缓存； 2-草稿；3-已读*/
	@Field(value="EU_HANDLE_STATUS")
    private String euHandleStatus;	
	
	/**
	 * 根据接收范围区分：1（全员）-null；2（科室）-科室主键列表；3（指定人员）-人员主键列表
	 */
	private String[] scopes;
	
	/** PK_MESSAGE_SEND - 消息发送主键 */
    private String pkMessageSend;

    public String getPkMessage(){
        return this.pkMessage;
    }
    public void setPkMessage(String pkMessage){
        this.pkMessage = pkMessage;
    }

    public String getMessageGroup(){
        return this.messageGroup;
    }
    public void setMessageGroup(String messageGroup){
        this.messageGroup = messageGroup;
    }

    public String getEuMessageType(){
        return this.euMessageType;
    }
    public void setEuMessageType(String euMessageType){
        this.euMessageType = euMessageType;
    }

    public String getSubjectMessage(){
        return this.subjectMessage;
    }
    public void setSubjectMessage(String subjectMessage){
        this.subjectMessage = subjectMessage;
    }

    public String getContentMessage(){
        return this.contentMessage;
    }
    public void setContentMessage(String contentMessage){
        this.contentMessage = contentMessage;
    }

    public String getEuSendType(){
        return this.euSendType;
    }
    public void setEuSendType(String euSendType){
        this.euSendType = euSendType;
    }

    public String getEuScope(){
        return this.euScope;
    }
    public void setEuScope(String euScope){
        this.euScope = euScope;
    }

    public String getPkSender(){
        return this.pkSender;
    }
    public void setPkSender(String pkSender){
        this.pkSender = pkSender;
    }

    public String getPkSenderDept(){
        return this.pkSenderDept;
    }
    public void setPkSenderDept(String pkSenderDept){
        this.pkSenderDept = pkSenderDept;
    }

    public Date getDateSend(){
        return this.dateSend;
    }
    public void setDateSend(Date dateSend){
        this.dateSend = dateSend;
    }

    public Integer getRemindInterval(){
        return this.remindInterval;
    }
    public void setRemindInterval(Integer remindInterval){
        this.remindInterval = remindInterval;
    }

    public String getCodeOper(){
        return this.codeOper;
    }
    public void setCodeOper(String codeOper){
        this.codeOper = codeOper;
    }

    public String getParamNavigation(){
        return this.paramNavigation;
    }
    public void setParamNavigation(String paramNavigation){
        this.paramNavigation = paramNavigation;
    }

    public Date getDateExpired(){
        return this.dateExpired;
    }
    public void setDateExpired(Date dateExpired){
        this.dateExpired = dateExpired;
    }

    public String getEuHandleStatus(){
        return this.euHandleStatus;
    }
    public void setEuHandleStatus(String euHandleStatus){
        this.euHandleStatus = euHandleStatus;
    }
	public String[] getScopes() {
		return scopes;
	}
	public void setScopes(String[] scopes) {
		this.scopes = scopes;
	}
	public String getPkMessageSend() {
		return pkMessageSend;
	}
	public void setPkMessageSend(String pkMessageSend) {
		this.pkMessageSend = pkMessageSend;
	}

}