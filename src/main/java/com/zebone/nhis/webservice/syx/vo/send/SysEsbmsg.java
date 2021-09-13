package com.zebone.nhis.webservice.syx.vo.send;


import java.util.Date;
import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: SYS_ESBMSG 
 *
 * @since 2019-07-02 02:32:41
 */
@Table(value="SYS_ESBMSG")
public class SysEsbmsg extends BaseModule  {

	@PK
	@Field(value="PK_ESBMSG",id=KeyId.UUID)
    private String pkEsbmsg;

	@Field(value="ID_MSG")
    private String idMsg;

	@Field(value="PK_PV")
    private String pkPv;

	@Field(value="DT_ESBMSGTYPE")
    private String dtEsbmsgtype;

	@Field(value="CONTENT_MSG")
    private String contentMsg;

	@Field(value="EU_STATUS")
    private String euStatus;

	@Field(value="DATE_SEND")
    private Date dateSend;

	@Field(value="DATE_HANDLE")
    private Date dateHandle;

	@Field(value="DESC_ERROR")
    private String descError;

	@Field(value="CNT_HANDLE")
    private Integer cntHandle;

	@Field(value="IP_SEND")
    private String ipSend;

	@Field(value="ADDR_SEND")
    private String addrSend;

	@Field(value="NOTE")
    private String note;

	@Field(value="MODIFIER")
    private String modifier;

	@Field(value="MODITY_TIME")
    private Date modityTime;


    public String getPkEsbmsg(){
        return this.pkEsbmsg;
    }
    public void setPkEsbmsg(String pkEsbmsg){
        this.pkEsbmsg = pkEsbmsg;
    }

    public String getIdMsg(){
        return this.idMsg;
    }
    public void setIdMsg(String idMsg){
        this.idMsg = idMsg;
    }

    public String getPkPv(){
        return this.pkPv;
    }
    public void setPkPv(String pkPv){
        this.pkPv = pkPv;
    }

    public String getDtEsbmsgtype(){
        return this.dtEsbmsgtype;
    }
    public void setDtEsbmsgtype(String dtEsbmsgtype){
        this.dtEsbmsgtype = dtEsbmsgtype;
    }

    public String getContentMsg(){
        return this.contentMsg;
    }
    public void setContentMsg(String contentMsg){
        this.contentMsg = contentMsg;
    }

    public String getEuStatus(){
        return this.euStatus;
    }
    public void setEuStatus(String euStatus){
        this.euStatus = euStatus;
    }

    public Date getDateSend(){
        return this.dateSend;
    }
    public void setDateSend(Date dateSend){
        this.dateSend = dateSend;
    }

    public Date getDateHandle(){
        return this.dateHandle;
    }
    public void setDateHandle(Date dateHandle){
        this.dateHandle = dateHandle;
    }

    public String getDescError(){
        return this.descError;
    }
    public void setDescError(String descError){
        this.descError = descError;
    }

    public Integer getCntHandle(){
        return this.cntHandle;
    }
    public void setCntHandle(Integer cntHandle){
        this.cntHandle = cntHandle;
    }

    public String getIpSend(){
        return this.ipSend;
    }
    public void setIpSend(String ipSend){
        this.ipSend = ipSend;
    }

    public String getAddrSend(){
        return this.addrSend;
    }
    public void setAddrSend(String addrSend){
        this.addrSend = addrSend;
    }

    public String getNote(){
        return this.note;
    }
    public void setNote(String note){
        this.note = note;
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
}