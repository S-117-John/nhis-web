package com.zebone.nhis.common.module.base.support;

import java.util.Date;

import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.Field.FieldType;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: CV_MSG_SEND - CV_MSG_SEND 
 *
 * @since 2017-8-17 10:01:03
 */
@Table(value="CV_MSG_SEND")
public class CvMsgSend   {

	@PK
	@Field(value="PK_MSGSEND",id=KeyId.UUID)
    private String pkMsgsend;

	@Field(value="PK_MSG")
    private String pkMsg;

	@Field(value="DATE_SEND")
    private Date dateSend;

	@Field(value="CODE_RECVER")
    private String codeRecver;

	@Field(value="NAME_RECVER")
    private String nameRecver;

    /** EU_DEVICETYPE - 0 PC，1 PHONE，2 PAD，9 OTH */
	@Field(value="EU_DEVICETYPE")
    private String euDevicetype;

	@Field(value="IP_RECV")
    private String ipRecv;

	@Field(value="DATE_READ")
    private Date dateRead;

	@Field(value="FLAG_READ")
    private String flagRead;

	@Field(value="FLAG_REPLY")
    private String flagReply;

	@Field(value="DATE_REPLY")
    private Date dateReply;

	@Field(value="CONT_REPLY")
    private String contReply;

	@Field(value="IP_REPLY")
    private String ipReply;

	@Field(userfield="pkEmp",userfieldscop=FieldType.INSERT)
    private String creator;

	@Field(value="CREATE_TIME",date=FieldType.INSERT)
    private Date createTime;

	@Field(userfield="pkEmp",userfieldscop=FieldType.ALL)
    private String modifier;

	@Field(value="MODITY_TIME")
    private Date modityTime;

	@Field(value="DEL_FLAG")
    private String delFlag;

	@Field(date=FieldType.ALL)
    private Date ts;
	
	private String State;
	
	@Field(value="CODE_DEPT")
	private String codeDept;
	@Field(value="NAME_DEPT")
	private String nameDept;

    public String getCodeDept() {
		return codeDept;
	}
	public void setCodeDept(String codeDept) {
		this.codeDept = codeDept;
	}
	public String getNameDept() {
		return nameDept;
	}
	public void setNameDept(String nameDept) {
		this.nameDept = nameDept;
	}
	public String getState() {
		return State;
	}
	public void setState(String state) {
		State = state;
	}
	public String getPkMsgsend(){
        return this.pkMsgsend;
    }
    public void setPkMsgsend(String pkMsgsend){
        this.pkMsgsend = pkMsgsend;
    }

    public String getPkMsg(){
        return this.pkMsg;
    }
    public void setPkMsg(String pkMsg){
        this.pkMsg = pkMsg;
    }

    public Date getDateSend(){
        return this.dateSend;
    }
    public void setDateSend(Date dateSend){
        this.dateSend = dateSend;
    }

    public String getCodeRecver(){
        return this.codeRecver;
    }
    public void setCodeRecver(String codeRecver){
        this.codeRecver = codeRecver;
    }

    public String getNameRecver(){
        return this.nameRecver;
    }
    public void setNameRecver(String nameRecver){
        this.nameRecver = nameRecver;
    }

    public String getEuDevicetype(){
        return this.euDevicetype;
    }
    public void setEuDevicetype(String euDevicetype){
        this.euDevicetype = euDevicetype;
    }

    public String getIpRecv(){
        return this.ipRecv;
    }
    public void setIpRecv(String ipRecv){
        this.ipRecv = ipRecv;
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

    public String getFlagReply(){
        return this.flagReply;
    }
    public void setFlagReply(String flagReply){
        this.flagReply = flagReply;
    }

    public Date getDateReply(){
        return this.dateReply;
    }
    public void setDateReply(Date dateReply){
        this.dateReply = dateReply;
    }

    public String getContReply(){
        return this.contReply;
    }
    public void setContReply(String contReply){
        this.contReply = contReply;
    }

    public String getIpReply(){
        return this.ipReply;
    }
    public void setIpReply(String ipReply){
        this.ipReply = ipReply;
    }

    public String getCreator(){
        return this.creator;
    }
    public void setCreator(String creator){
        this.creator = creator;
    }

    public Date getCreateTime(){
        return this.createTime;
    }
    public void setCreateTime(Date createTime){
        this.createTime = createTime;
    }

    public String getModifier(){
        return this.modifier;
    }
    public void setModifier(String modifier){
        this.modifier = modifier;
    }

    public Date getModityTime(){
        return this.modityTime;
    }
    public void setModityTime(Date modityTime){
        this.modityTime = modityTime;
    }

    public String getDelFlag(){
        return this.delFlag;
    }
    public void setDelFlag(String delFlag){
        this.delFlag = delFlag;
    }

    public Date getTs(){
        return this.ts;
    }
    public void setTs(Date ts){
        this.ts = ts;
    }
}