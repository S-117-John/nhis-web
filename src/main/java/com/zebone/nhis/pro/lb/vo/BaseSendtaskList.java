package com.zebone.nhis.pro.lb.vo;

import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;

import java.util.Date;

/**
 * @Classname BaseSendtaskList
 * @Description TODO
 * @Date 2019-12-20 14:54
 * @Created by wuqiang
 */
@Table(value="base_sendtask_list")
public class BaseSendtaskList   {

    @PK
    @Field(value="id")
    private Integer id;

    @Field(value="checkStatus")
    private Long checkstatus;

    @Field(value="companyId")
    private String companyid;

    @Field(value="modName")
    private String modname;

    @Field(value="note")
    private String note;

    @Field(value="operDate")
    private Date operdate;

    @Field(value="reqId")
    private Long reqid;

    @Field(value="sendAddress")
    private String sendaddress;

    @Field(value="sendContent")
    private String sendcontent;

    @Field(value="sysName")
    private String sysname;

    @Field(value="taskType")
    private String tasktype;

    @Field(value="timeToSend")
    private Date timetosend;

    @Field(value="reqUserId")
    private Long requserid;

    @Field(value="failureCount")
    private Long failurecount;

    @Field(value="rowCount")
    private Long rowcount;

    @Field(value="sendTime")
    private Date sendtime;


    public Integer getId(){
        return this.id;
    }
    public void setId(Integer id){
        this.id = id;
    }

    public Long getCheckstatus(){
        return this.checkstatus;
    }
    public void setCheckstatus(Long checkstatus){
        this.checkstatus = checkstatus;
    }

    public String getCompanyid(){
        return this.companyid;
    }
    public void setCompanyid(String companyid){
        this.companyid = companyid;
    }

    public String getModname(){
        return this.modname;
    }
    public void setModname(String modname){
        this.modname = modname;
    }

    public String getNote(){
        return this.note;
    }
    public void setNote(String note){
        this.note = note;
    }

    public Date getOperdate(){
        return this.operdate;
    }
    public void setOperdate(Date operdate){
        this.operdate = operdate;
    }

    public Long getReqid(){
        return this.reqid;
    }
    public void setReqid(Long reqid){
        this.reqid = reqid;
    }

    public String getSendaddress(){
        return this.sendaddress;
    }
    public void setSendaddress(String sendaddress){
        this.sendaddress = sendaddress;
    }

    public String getSendcontent(){
        return this.sendcontent;
    }
    public void setSendcontent(String sendcontent){
        this.sendcontent = sendcontent;
    }

    public String getSysname(){
        return this.sysname;
    }
    public void setSysname(String sysname){
        this.sysname = sysname;
    }

    public String getTasktype(){
        return this.tasktype;
    }
    public void setTasktype(String tasktype){
        this.tasktype = tasktype;
    }

    public Date getTimetosend(){
        return this.timetosend;
    }
    public void setTimetosend(Date timetosend){
        this.timetosend = timetosend;
    }

    public Long getRequserid(){
        return this.requserid;
    }
    public void setRequserid(Long requserid){
        this.requserid = requserid;
    }

    public Long getFailurecount(){
        return this.failurecount;
    }
    public void setFailurecount(Long failurecount){
        this.failurecount = failurecount;
    }

    public Long getRowcount(){
        return this.rowcount;
    }
    public void setRowcount(Long rowcount){
        this.rowcount = rowcount;
    }

    public Date getSendtime(){
        return this.sendtime;
    }
    public void setSendtime(Date sendtime){
        this.sendtime = sendtime;
    }
}
