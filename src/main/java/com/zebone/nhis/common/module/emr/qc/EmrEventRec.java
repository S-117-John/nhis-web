package com.zebone.nhis.common.module.emr.qc;

import java.util.Date;

/**
 *
 * @author 
 */
public class EmrEventRec{
    /**
     * 
     */
    private String pkEvtrec;
    /**
     * 
     */
    private String pkOrg;
    /**
     * 
     */
    private String pkPi;
    /**
     * 
     */
    private Integer times;
    /**
     * 
     */
    private String pkPv;
    /**
     * 
     */
    private String pkRec;
    /**
     * emr_dict_code:class_code='Emr_Event_Type'
admit	入院
discharge	出院
operation	手术
trans	转科
normal	正常
critical	病危
severity	病重
rescue	抢救
death	死亡


     */
    private String eventCode;
    /**
     * 
     */
    private Date eventDate;
    /**
     * 
     */
    private Date monitDate;
    /**
     * 
     */
    private String euPatState;
    /**
     * 0：在用
1：结束
     */
    private String euEventStatus;
    /**
     * 关联业务系统流水号：医嘱号等
     */
    private String relateNo;
    /**
     * 
     */
    private String pkDept;
    /**
     * 
     */
    private String pkDeptNs;
    /**
     * 
     */
    private String pkEmp;
    /**
     * 
     */
    private String pkEmpNs;
    /**
     * 
     */
    private String delFlag;
    /**
     * 
     */
    private String remark;
    /**
     * 
     */
    private String creator;
    /**
     * 
     */
    private Date createTime;
    /**
     * 
     */
    private Date ts;

    /**
     * 
     */
    public String getPkEvtrec(){
        return this.pkEvtrec;
    }

    /**
     * 
     */
    public void setPkEvtrec(String pkEvtrec){
        this.pkEvtrec = pkEvtrec;
    }    
    /**
     * 
     */
    public String getPkOrg(){
        return this.pkOrg;
    }

    /**
     * 
     */
    public void setPkOrg(String pkOrg){
        this.pkOrg = pkOrg;
    }    
    /**
     * 
     */
    public String getPkPi(){
        return this.pkPi;
    }

    /**
     * 
     */
    public void setPkPi(String pkPi){
        this.pkPi = pkPi;
    }    
    /**
     * 
     */
    public Integer getTimes(){
        return this.times;
    }

    /**
     * 
     */
    public void setTimes(Integer times){
        this.times = times;
    }    
    /**
     * 
     */
    public String getPkPv(){
        return this.pkPv;
    }

    /**
     * 
     */
    public void setPkPv(String pkPv){
        this.pkPv = pkPv;
    }    
    /**
     * 
     */
    public String getPkRec(){
        return this.pkRec;
    }

    /**
     * 
     */
    public void setPkRec(String pkRec){
        this.pkRec = pkRec;
    }    
    /**
     * emr_dict_code:class_code='Emr_Event_Type'
admit	入院
discharge	出院
operation	手术
trans	转科
normal	正常
critical	病危
severity	病重
rescue	抢救
death	死亡


     */
    public String getEventCode(){
        return this.eventCode;
    }

    /**
     * emr_dict_code:class_code='Emr_Event_Type'
admit	入院
discharge	出院
operation	手术
trans	转科
normal	正常
critical	病危
severity	病重
rescue	抢救
death	死亡


     */
    public void setEventCode(String eventCode){
        this.eventCode = eventCode;
    }    
    /**
     * 
     */
    public Date getEventDate(){
        return this.eventDate;
    }

    /**
     * 
     */
    public void setEventDate(Date eventDate){
        this.eventDate = eventDate;
    }    
    /**
     * 
     */
    public Date getMonitDate(){
        return this.monitDate;
    }

    /**
     * 
     */
    public void setMonitDate(Date monitDate){
        this.monitDate = monitDate;
    }    
    /**
     * 
     */
    public String getEuPatState(){
        return this.euPatState;
    }

    /**
     * 
     */
    public void setEuPatState(String euPatState){
        this.euPatState = euPatState;
    }    
    /**
     * 0：在用
1：结束
     */
    public String getEuEventStatus(){
        return this.euEventStatus;
    }

    /**
     * 0：在用
1：结束
     */
    public void setEuEventStatus(String euEventStatus){
        this.euEventStatus = euEventStatus;
    }    
    /**
     * 关联业务系统流水号：医嘱号等
     */
    public String getRelateNo(){
        return this.relateNo;
    }

    /**
     * 关联业务系统流水号：医嘱号等
     */
    public void setRelateNo(String relateNo){
        this.relateNo = relateNo;
    }    
    /**
     * 
     */
    public String getPkDept(){
        return this.pkDept;
    }

    /**
     * 
     */
    public void setPkDept(String pkDept){
        this.pkDept = pkDept;
    }    
    /**
     * 
     */
    public String getPkDeptNs(){
        return this.pkDeptNs;
    }

    /**
     * 
     */
    public void setPkDeptNs(String pkDeptNs){
        this.pkDeptNs = pkDeptNs;
    }    
    /**
     * 
     */
    public String getPkEmp(){
        return this.pkEmp;
    }

    /**
     * 
     */
    public void setPkEmp(String pkEmp){
        this.pkEmp = pkEmp;
    }    
    /**
     * 
     */
    public String getPkEmpNs(){
        return this.pkEmpNs;
    }

    /**
     * 
     */
    public void setPkEmpNs(String pkEmpNs){
        this.pkEmpNs = pkEmpNs;
    }    
    /**
     * 
     */
    public String getDelFlag(){
        return this.delFlag;
    }

    /**
     * 
     */
    public void setDelFlag(String delFlag){
        this.delFlag = delFlag;
    }    
    /**
     * 
     */
    public String getRemark(){
        return this.remark;
    }

    /**
     * 
     */
    public void setRemark(String remark){
        this.remark = remark;
    }    
    /**
     * 
     */
    public String getCreator(){
        return this.creator;
    }

    /**
     * 
     */
    public void setCreator(String creator){
        this.creator = creator;
    }    
    /**
     * 
     */
    public Date getCreateTime(){
        return this.createTime;
    }

    /**
     * 
     */
    public void setCreateTime(Date createTime){
        this.createTime = createTime;
    }    
    /**
     * 
     */
    public Date getTs(){
        return this.ts;
    }

    /**
     * 
     */
    public void setTs(Date ts){
        this.ts = ts;
    }    
}