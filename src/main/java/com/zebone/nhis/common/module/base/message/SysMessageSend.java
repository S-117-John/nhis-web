package com.zebone.nhis.common.module.base.message;


import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;


/**
 * Table: SYS_MESSAGE_SEND  - 系统消息推送明细 
 *
 * @since 2016-10-09 01:38:17
 */
@Table(value="SYS_MESSAGE_SEND")
public class SysMessageSend  extends BaseModule{

	private static final long serialVersionUID = 1L;

	/** PK_MESSAGE_SEND - 消息发送主键 */
	@PK
	@Field(value="PK_MESSAGE_SEND",id=KeyId.UUID)
    private String pkMessageSend;

    /** PK_MESSAGE - 消息主键 */
	@Field(value="PK_MESSAGE")
    private String pkMessage;

    /** SEND_TARGET - 发送目标 pk_emp/pk_dept/pk_pi/ip */
	@Field(value="SEND_TARGET")
    private String sendTarget;

    /** EU_TARGET_TYPE - 发送类型 1-员工；2-部门；3-患者；9-IP */
	@Field(value="EU_TARGET_TYPE")
    private String euTargetType;

    /** EU_SEND_STATUS - 发送状态 0-未发送；1-发送成功；2-发送失败 */
	@Field(value="EU_SEND_STATUS")
    private String euSendStatus;

    /** FAILURE_REASON - 失败原因 */
	@Field(value="FAILURE_REASON")
    private String failureReason;

    /** PK_READER - 读取人员 */
	@Field(value="PK_READER")
    private String pkReader;

    /** PK_READER_DEPT - 读取科室 */
	@Field(value="PK_READER_DEPT")
    private String pkReaderDept;

    /** DATE_READ - 读取时间 */
	@Field(value="DATE_READ")
    private Date dateRead;

    /** FLAG_READ - 读取标志 */
	@Field(value="FLAG_READ")
    private String flagRead;

    public String getPkMessageSend(){
        return this.pkMessageSend;
    }
    public void setPkMessageSend(String pkMessageSend){
        this.pkMessageSend = pkMessageSend;
    }

    public String getPkMessage(){
        return this.pkMessage;
    }
    public void setPkMessage(String pkMessage){
        this.pkMessage = pkMessage;
    }

    public String getSendTarget(){
        return this.sendTarget;
    }
    public void setSendTarget(String sendTarget){
        this.sendTarget = sendTarget;
    }

    public String getEuTargetType(){
        return this.euTargetType;
    }
    public void setEuTargetType(String euTargetType){
        this.euTargetType = euTargetType;
    }

    public String getEuSendStatus(){
        return this.euSendStatus;
    }
    public void setEuSendStatus(String euSendStatus){
        this.euSendStatus = euSendStatus;
    }

    public String getFailureReason(){
        return this.failureReason;
    }
    public void setFailureReason(String failureReason){
        this.failureReason = failureReason;
    }

    public String getPkReader(){
        return this.pkReader;
    }
    public void setPkReader(String pkReader){
        this.pkReader = pkReader;
    }

    public String getPkReaderDept(){
        return this.pkReaderDept;
    }
    public void setPkReaderDept(String pkReaderDept){
        this.pkReaderDept = pkReaderDept;
    }

    public Date getDateRead(){
        return this.dateRead;
    }
    public void setDateRead(Date dateRead){
        this.dateRead = dateRead;
    }

    public String getFlagRead(){
        return this.flagRead;
    }
    public void setFlagRead(String flagRead){
        this.flagRead = flagRead;
    }
}