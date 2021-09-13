package com.zebone.nhis.common.module.ma.msg;

import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: SYS_MSG_REC 
 *
 * @since 2018-04-17 03:22:57
 */
@Table(value="SYS_MSG_REC")
public class SysMsgRec extends BaseModule{

	@PK
	@Field(value="PK_MSG",id=KeyId.UUID)
    private String pkMsg;

	@Field(value="MSG_ID")
    private String msgId;

	@Field(value="MSG_TYPE")
    private String msgType;
	
	@Field(value="ERR_TXT")
    private String errTxt;

	@Field(value="TRANS_TYPE")
    private String transType;

	@Field(value="TRANS_DATE")
    private Date transDate;

	@Field(value="SYS_CODE")
    private String sysCode;

	@Field(value="MSG_STATUS")
    private String msgStatus;

	@Field(value="REMARK")
    private String remark;
	
	@Field(value="EXEC_TIME")
	private Double execTime;
	
	@Field(value="MSG_CONTENT")
    private String msgContent;

    @Field(value="CODE_PI")
    private String codePi;

    @Field(value="CODE_OP")
    private String codeOp;

    @Field(value="CODE_IP")
    private String codeIp;

    @Field(value="CODE_PV")
    private String codePv;

    @Field(value="CODE_OTHER")
    private String codeOther;

    @Field(value="EU_EME")
    private String euEme;

    public String getEuEme() {
        return euEme;
    }

    public void setEuEme(String euEme) {
        this.euEme = euEme;
    }

    public String getPkMsg(){
        return this.pkMsg;
    }
    public void setPkMsg(String pkMsg){
        this.pkMsg = pkMsg;
    }

    public String getPkOrg(){
        return this.pkOrg;
    }
    public void setPkOrg(String pkOrg){
        this.pkOrg = pkOrg;
    }

    public String getMsgId(){
        return this.msgId;
    }
    public void setMsgId(String msgId){
        this.msgId = msgId;
    }

    public String getMsgType(){
        return this.msgType;
    }
    public void setMsgType(String msgType){
        this.msgType = msgType;
    }

    public String getTransType(){
        return this.transType;
    }
    public void setTransType(String transType){
        this.transType = transType;
    }

    public Date getTransDate(){
        return this.transDate;
    }
    public void setTransDate(Date transDate){
        this.transDate = transDate;
    }

    public String getMsgContent(){
        return this.msgContent;
    }
    public void setMsgContent(String msgContent){
        this.msgContent = msgContent;
    }

    public String getSysCode(){
        return this.sysCode;
    }
    public void setSysCode(String sysCode){
        this.sysCode = sysCode;
    }

    public String getMsgStatus(){
        return this.msgStatus;
    }
    public void setMsgStatus(String msgStatus){
        this.msgStatus = msgStatus;
    }

    public String getErrTxt(){
        return this.errTxt;
    }
    public void setErrTxt(String errTxt){
        this.errTxt = errTxt;
    }

    public String getRemark(){
        return this.remark;
    }
    public void setRemark(String remark){
        this.remark = remark;
    }
	public Double getExecTime() {
		return execTime;
	}
	public void setExecTime(Double execTime) {
		this.execTime = execTime;
	}

    public String getCodePi() {
        return codePi;
    }

    public void setCodePi(String codePi) {
        this.codePi = codePi;
    }

    public String getCodeOp() {
        return codeOp;
    }

    public void setCodeOp(String codeOp) {
        this.codeOp = codeOp;
    }

    public String getCodeIp() {
        return codeIp;
    }

    public void setCodeIp(String codeIp) {
        this.codeIp = codeIp;
    }

    public String getCodePv() {
        return codePv;
    }

    public void setCodePv(String codePv) {
        this.codePv = codePv;
    }

    public String getCodeOther() {
        return codeOther;
    }

    public void setCodeOther(String codeOther) {
        this.codeOther = codeOther;
    }
}