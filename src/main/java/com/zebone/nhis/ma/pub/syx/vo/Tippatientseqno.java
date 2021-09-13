package com.zebone.nhis.ma.pub.syx.vo;

import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.Table;


/**
 * Table: tIPPatientSeqNO 
 *
 * @since 2018-12-18 11:59:28
 */
@Table(value="tIPPatientSeqNO")
public class Tippatientseqno   {

	@Field(value="IPSeqPrefix")
    private String ipseqprefix;

	@Field(value="IPSeqNo")
    private String ipseqno;

	@Field(value="IPSeqNoText")
    private String ipseqnotext;

	@Field(value="StatusFlag")
    private String statusflag;

	@Field(value="IPTimes")
    private Integer iptimes;

	@Field(value="OldIPNo")
    private String oldipno;

	@Field(value="PatientID")
    private String patientid;

	@Field(value="LastInPatientID")
    private String lastinpatientid;


    public String getIpseqprefix(){
        return this.ipseqprefix;
    }
    public void setIpseqprefix(String ipseqprefix){
        this.ipseqprefix = ipseqprefix;
    }

    public String getIpseqno(){
        return this.ipseqno;
    }
    public void setIpseqno(String ipseqno){
        this.ipseqno = ipseqno;
    }

    public String getIpseqnotext(){
        return this.ipseqnotext;
    }
    public void setIpseqnotext(String ipseqnotext){
        this.ipseqnotext = ipseqnotext;
    }

    public String getStatusflag(){
        return this.statusflag;
    }
    public void setStatusflag(String statusflag){
        this.statusflag = statusflag;
    }

    public Integer getIptimes(){
        return this.iptimes;
    }
    public void setIptimes(Integer iptimes){
        this.iptimes = iptimes;
    }

    public String getOldipno(){
        return this.oldipno;
    }
    public void setOldipno(String oldipno){
        this.oldipno = oldipno;
    }

    public String getPatientid(){
        return this.patientid;
    }
    public void setPatientid(String patientid){
        this.patientid = patientid;
    }

    public String getLastinpatientid(){
        return this.lastinpatientid;
    }
    public void setLastinpatientid(String lastinpatientid){
        this.lastinpatientid = lastinpatientid;
    }
}