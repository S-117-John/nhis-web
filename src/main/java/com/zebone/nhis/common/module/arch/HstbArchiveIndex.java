package com.zebone.nhis.common.module.arch;

import java.util.Date;

import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: HSTB_ARCHIVE_INDEX 
 *
 * @since 2018-01-23 02:47:09
 */
@Table(value="HSTB_ARCHIVE_INDEX")
public class HstbArchiveIndex   {

//	@PK
//	@Field(value="HAIUUID",id=KeyId.UUID)
//    private Long haiuuid;

	@Field(value="RTYPE")
    private String rtype;

	@Field(value="RID")
    private String rid;

	@Field(value="RSERIAL")
    private String rserial;

	@Field(value="FILEPATH_INDEX")
    private String filepathIndex;

	@Field(value="FILEPATH_REPORT")
    private String filepathReport;

	@Field(value="FILEPATH_CA")
    private String filepathCa;

	@Field(value="PATID")
    private String patid;

	@Field(value="TJH")
    private String tjh;

	@Field(value="BJH")
    private String bjh;

	@Field(value="PNAME")
    private String pname;

	@Field(value="PSEX")
    private String psex;

	@Field(value="PAGE")
    private String page;

	@Field(value="PATDEPT")
    private String patdept;

	@Field(value="PATFLAG")
    private String patflag;

	@Field(value="TIME_ADDTION")
    private Date timeAddtion;

	@Field(value="TIME_VALIDATE")
    private Date timeValidate;

	@Field(value="ITEM_NAME")
    private String itemName;

	@Field(value="TIME_APPLY")
    private Date timeApply;

	@Field(value="TIME_DRAW_BLOOD")
    private Date timeDrawBlood;

	@Field(value="TIME_SENDED")
    private Date timeSended;

	@Field(value="TIME_CONFIRM")
    private Date timeConfirm;

	@Field(value="TIME_CAPTURE")
    private Date timeCapture;

	@Field(value="TIME_REPORT")
    private Date timeReport;

	@Field(value="UPDATE_TIMES")
    private Integer updateTimes;

	@Field(value="UPDATE_TIME")
    private Date updateTime;

	@Field(value="REMARK")
    private String remark;

	@Field(value="STATUS")
    private Integer status;

	@Field(value="HANDLE_HPUUID")
    private Long handleHpuuid;

	@Field(value="HANDLE_TIME")
    private Date handleTime;

	@Field(value="ZYH")
    private String zyh;

	@Field(value="FILEPATH_REPORT_IMAGE")
    private String filepathReportImage;

	@Field(value="FILEPATH_REPORT_IMAGE_NUM")
    private Integer filepathReportImageNum;

	@Field(value="RDEPT_TYPE")
    private String rdeptType;

	@Field(value="RDEPT_SUBTYPE")
    private String rdeptSubtype;


//    public Long getHaiuuid(){
//        return this.haiuuid;
//    }
//    public void setHaiuuid(Long haiuuid){
//        this.haiuuid = haiuuid;
//    }

    public String getRtype(){
        return this.rtype;
    }
    public void setRtype(String rtype){
        this.rtype = rtype;
    }

    public String getRid(){
        return this.rid;
    }
    public void setRid(String rid){
        this.rid = rid;
    }

    public String getRserial(){
        return this.rserial;
    }
    public void setRserial(String rserial){
        this.rserial = rserial;
    }

    public String getFilepathIndex(){
        return this.filepathIndex;
    }
    public void setFilepathIndex(String filepathIndex){
        this.filepathIndex = filepathIndex;
    }

    public String getFilepathReport(){
        return this.filepathReport;
    }
    public void setFilepathReport(String filepathReport){
        this.filepathReport = filepathReport;
    }

    public String getFilepathCa(){
        return this.filepathCa;
    }
    public void setFilepathCa(String filepathCa){
        this.filepathCa = filepathCa;
    }

    public String getPatid(){
        return this.patid;
    }
    public void setPatid(String patid){
        this.patid = patid;
    }

    public String getTjh(){
        return this.tjh;
    }
    public void setTjh(String tjh){
        this.tjh = tjh;
    }

    public String getBjh(){
        return this.bjh;
    }
    public void setBjh(String bjh){
        this.bjh = bjh;
    }

    public String getPname(){
        return this.pname;
    }
    public void setPname(String pname){
        this.pname = pname;
    }

    public String getPsex(){
        return this.psex;
    }
    public void setPsex(String psex){
        this.psex = psex;
    }

    public String getPage(){
        return this.page;
    }
    public void setPage(String page){
        this.page = page;
    }

    public String getPatdept(){
        return this.patdept;
    }
    public void setPatdept(String patdept){
        this.patdept = patdept;
    }

    public String getPatflag(){
        return this.patflag;
    }
    public void setPatflag(String patflag){
        this.patflag = patflag;
    }

    public Date getTimeAddtion(){
        return this.timeAddtion;
    }
    public void setTimeAddtion(Date timeAddtion){
        this.timeAddtion = timeAddtion;
    }

    public Date getTimeValidate(){
        return this.timeValidate;
    }
    public void setTimeValidate(Date timeValidate){
        this.timeValidate = timeValidate;
    }

    public String getItemName(){
        return this.itemName;
    }
    public void setItemName(String itemName){
        this.itemName = itemName;
    }

    public Date getTimeApply(){
        return this.timeApply;
    }
    public void setTimeApply(Date timeApply){
        this.timeApply = timeApply;
    }

    public Date getTimeDrawBlood(){
        return this.timeDrawBlood;
    }
    public void setTimeDrawBlood(Date timeDrawBlood){
        this.timeDrawBlood = timeDrawBlood;
    }

    public Date getTimeSended(){
        return this.timeSended;
    }
    public void setTimeSended(Date timeSended){
        this.timeSended = timeSended;
    }

    public Date getTimeConfirm(){
        return this.timeConfirm;
    }
    public void setTimeConfirm(Date timeConfirm){
        this.timeConfirm = timeConfirm;
    }

    public Date getTimeCapture(){
        return this.timeCapture;
    }
    public void setTimeCapture(Date timeCapture){
        this.timeCapture = timeCapture;
    }

    public Date getTimeReport(){
        return this.timeReport;
    }
    public void setTimeReport(Date timeReport){
        this.timeReport = timeReport;
    }

    public Integer getUpdateTimes(){
        return this.updateTimes;
    }
    public void setUpdateTimes(Integer updateTimes){
        this.updateTimes = updateTimes;
    }

    public Date getUpdateTime(){
        return this.updateTime;
    }
    public void setUpdateTime(Date updateTime){
        this.updateTime = updateTime;
    }

    public String getRemark(){
        return this.remark;
    }
    public void setRemark(String remark){
        this.remark = remark;
    }

    public Integer getStatus(){
        return this.status;
    }
    public void setStatus(Integer status){
        this.status = status;
    }

    public Long getHandleHpuuid(){
        return this.handleHpuuid;
    }
    public void setHandleHpuuid(Long handleHpuuid){
        this.handleHpuuid = handleHpuuid;
    }

    public Date getHandleTime(){
        return this.handleTime;
    }
    public void setHandleTime(Date handleTime){
        this.handleTime = handleTime;
    }

    public String getZyh(){
        return this.zyh;
    }
    public void setZyh(String zyh){
        this.zyh = zyh;
    }

    public String getFilepathReportImage(){
        return this.filepathReportImage;
    }
    public void setFilepathReportImage(String filepathReportImage){
        this.filepathReportImage = filepathReportImage;
    }

    public Integer getFilepathReportImageNum(){
        return this.filepathReportImageNum;
    }
    public void setFilepathReportImageNum(Integer filepathReportImageNum){
        this.filepathReportImageNum = filepathReportImageNum;
    }

    public String getRdeptType(){
        return this.rdeptType;
    }
    public void setRdeptType(String rdeptType){
        this.rdeptType = rdeptType;
    }

    public String getRdeptSubtype(){
        return this.rdeptSubtype;
    }
    public void setRdeptSubtype(String rdeptSubtype){
        this.rdeptSubtype = rdeptSubtype;
    }
}