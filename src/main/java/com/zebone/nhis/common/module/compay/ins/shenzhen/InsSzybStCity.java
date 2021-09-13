package com.zebone.nhis.common.module.compay.ins.shenzhen;


import java.util.Date;

import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.Field.FieldType;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;


/**
 * Table: INS_SZYB_ST_CITY - ins_szyb_st_city 
 *
 * @since 2019-12-11 04:36:24
 */
@Table(value="INS_SZYB_ST_CITY")
public class InsSzybStCity   {

    /** PK_INSSTCITY - 主键 */
	@PK
	@Field(value="PK_INSSTCITY",id=KeyId.UUID)
    private String pkInsstcity;

	@Field(value="pk_org",userfield="pkOrg",userfieldscop=FieldType.INSERT)
    private String pkOrg;

	@Field(value="PK_INSST")
    private String pkInsst;

    /** EU_TREATTYPE - 交易类型 “1”：门诊挂号 “2”：收费  "3" 住院中途结算 “4“ 出院结算 */
	@Field(value="EU_TREATTYPE")
    private String euTreattype;

	@Field(value="DT_MEDICATE")
    private String dtMedicate;

    /** DIAGCODE_INP - 入院诊断疾病编号 */
	@Field(value="DIAGCODE_INP")
    private String diagcodeInp;

    /** DIAGNAME_INP - 入院诊断疾病名称 */
	@Field(value="DIAGNAME_INP")
    private String diagnameInp;

    /** REASON_OUTP - 出院原因 */
	@Field(value="REASON_OUTP")
    private String reasonOutp;

    /** DIAGCODE_OUTP - 出院诊断疾病编号 */
	@Field(value="DIAGCODE_OUTP")
    private String diagcodeOutp;

    /** DIAGNAME_OUTP - 出院诊断疾病名称 */
	@Field(value="DIAGNAME_OUTP")
    private String diagnameOutp;

    /** DIAGCODE2_OUTP - 出院诊断疾病编号 */
	@Field(value="DIAGCODE2_OUTP")
    private String diagcode2Outp;

    /** DIAGNAME2_OUTP - 出院诊断疾病名称 */
	@Field(value="DIAGNAME2_OUTP")
    private String diagname2Outp;

	@Field(value="STATUS_OUTP")
    private String statusOutp;

	@Field(value="AMT_JJZF")
    private Double amtJjzf;

	@Field(value="AMT_GRZHZF")
    private Double amtGrzhzf;

	@Field(value="AMT_GRZF")
    private Double amtGrzf;

	@Field(value="AMT_GRZH")
    private Double amtGrzh;

	@Field(userfield="pkEmp",userfieldscop=FieldType.INSERT)
    private String creator;

	@Field(value="CREATE_TIME",date=FieldType.INSERT)
    private Date createTime;

	@Field(value="DEL_FLAG")
    private String delFlag;

	@Field(date=FieldType.ALL)
    private Date ts;


    public String getPkInsstcity(){
        return this.pkInsstcity;
    }
    public void setPkInsstcity(String pkInsstcity){
        this.pkInsstcity = pkInsstcity;
    }

    public String getPkOrg(){
        return this.pkOrg;
    }
    public void setPkOrg(String pkOrg){
        this.pkOrg = pkOrg;
    }

    public String getPkInsst(){
        return this.pkInsst;
    }
    public void setPkInsst(String pkInsst){
        this.pkInsst = pkInsst;
    }

    public String getEuTreattype(){
        return this.euTreattype;
    }
    public void setEuTreattype(String euTreattype){
        this.euTreattype = euTreattype;
    }

    public String getDtMedicate(){
        return this.dtMedicate;
    }
    public void setDtMedicate(String dtMedicate){
        this.dtMedicate = dtMedicate;
    }

    public String getDiagcodeInp(){
        return this.diagcodeInp;
    }
    public void setDiagcodeInp(String diagcodeInp){
        this.diagcodeInp = diagcodeInp;
    }

    public String getDiagnameInp(){
        return this.diagnameInp;
    }
    public void setDiagnameInp(String diagnameInp){
        this.diagnameInp = diagnameInp;
    }

    public String getReasonOutp(){
        return this.reasonOutp;
    }
    public void setReasonOutp(String reasonOutp){
        this.reasonOutp = reasonOutp;
    }

    public String getDiagcodeOutp(){
        return this.diagcodeOutp;
    }
    public void setDiagcodeOutp(String diagcodeOutp){
        this.diagcodeOutp = diagcodeOutp;
    }

    public String getDiagnameOutp(){
        return this.diagnameOutp;
    }
    public void setDiagnameOutp(String diagnameOutp){
        this.diagnameOutp = diagnameOutp;
    }

    public String getDiagcode2Outp(){
        return this.diagcode2Outp;
    }
    public void setDiagcode2Outp(String diagcode2Outp){
        this.diagcode2Outp = diagcode2Outp;
    }

    public String getDiagname2Outp(){
        return this.diagname2Outp;
    }
    public void setDiagname2Outp(String diagname2Outp){
        this.diagname2Outp = diagname2Outp;
    }

    public String getStatusOutp(){
        return this.statusOutp;
    }
    public void setStatusOutp(String statusOutp){
        this.statusOutp = statusOutp;
    }

    public Double getAmtJjzf(){
        return this.amtJjzf;
    }
    public void setAmtJjzf(Double amtJjzf){
        this.amtJjzf = amtJjzf;
    }

    public Double getAmtGrzhzf(){
        return this.amtGrzhzf;
    }
    public void setAmtGrzhzf(Double amtGrzhzf){
        this.amtGrzhzf = amtGrzhzf;
    }

    public Double getAmtGrzf(){
        return this.amtGrzf;
    }
    public void setAmtGrzf(Double amtGrzf){
        this.amtGrzf = amtGrzf;
    }

    public Double getAmtGrzh(){
        return this.amtGrzh;
    }
    public void setAmtGrzh(Double amtGrzh){
        this.amtGrzh = amtGrzh;
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
