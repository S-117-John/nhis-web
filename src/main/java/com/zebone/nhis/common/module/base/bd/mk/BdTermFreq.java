package com.zebone.nhis.common.module.base.bd.mk;

import java.util.Date;

import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.Field.FieldType;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;


/**
 * Table: BD_TERM_FREQ  - 医嘱频次定义 
 *
 * @since 2016-10-09 03:46:40
 */
@Table(value="BD_TERM_FREQ")
public class BdTermFreq   {

    /** PK_FREQ - 频次主键 */
	@PK
	@Field(value="PK_FREQ",id=KeyId.UUID)
    private String pkFreq;

    /** PK_ORG - 所属机构 */
	@Field(value="PK_ORG",userfield="pkOrg",userfieldscop=FieldType.INSERT)
    private String pkOrg;

    /** CODE - 频次编码:例如 qd */
	@Field(value="CODE")
    private String code;

    /** NAME - 频次名称:例如 ，每日一次 */
	@Field(value="NAME")
    private String name;

    /** NAME_PRINT - 打印名称 */
	@Field(value="NAME_PRINT")
    private String namePrint;

    /** EU_CYCLE - 频次周期类型:0按天执行 1按周执行  2按小时执行 3按分钟执行 */
	@Field(value="EU_CYCLE")
    private String euCycle;

    /** CNT_CYCLE - 频次周期数 */
	@Field(value="CNT_CYCLE")
    private Long cntCycle;

    /** FLAG_PLAN - 计划频次标志 */
	@Field(value="FLAG_PLAN")
    private String flagPlan;

    /** CNT - 周期执行次数 */
	@Field(value="CNT")
    private Long cnt;

    /** EU_ALWAYS - 重复类型:0长期，1临时 */
	@Field(value="EU_ALWAYS")
    private String euAlways;

    /** NOTE - 频次描述 */
	@Field(value="NOTE")
    private String note;

    /** SPCODE - 拼音码 */
	@Field(value="SPCODE")
    private String spcode;

    /** D_CODE - 自定义码 */
	@Field(value="D_CODE")
    private String dCode;

    /** CREATOR - 创建人 */
	@Field(value="CREATOR",userfieldscop=FieldType.INSERT)
    private String creator;

    /** CREATE_TIME - 创建时间 */
	@Field(value="CREATE_TIME",date=FieldType.INSERT)
    private Date createTime;

    /** FLAG_DEL - 0:未删除 1:已删除 */
	@Field(value="DEL_FLAG")
    private String delFlag;

    /** TS - 时间戳 */
	@Field(value="TS",date=FieldType.ALL)
    private Date ts;
	
	/**SORTNO - 顺序号*/
	@Field(value="SORTNO")
	private Long sortNo;
	
	@Field(value="DT_FREQTYPE")
	private String dtFreqtype;

    /** FLAG_PLAN - 计划频次标志 */
    @Field(value="FLAG_EMER")
    private String flagEmer;

    @Field(value = "EU_EXTIME")
    private String euExtime;

    @Field(value = "EU_FIRSTTYPE")
    private String euFirsttype;

    @Field(value = "EU_RANGE")
    private String euRange;

    @Field(value = "CNT_INTERVAL")
    private Long cntInterval;

    public String getFlagEmer() {
        return flagEmer;
    }

    public void setFlagEmer(String flagEmer) {
        this.flagEmer = flagEmer;
    }

    public Long getSortNo() {
		return sortNo;
	}
	public void setSortNo(Long sortNo) {
		this.sortNo = sortNo;
	}
	public String getPkFreq(){
        return this.pkFreq;
    }
    public void setPkFreq(String pkFreq){
        this.pkFreq = pkFreq;
    }

    public String getPkOrg(){
        return this.pkOrg;
    }
    public void setPkOrg(String pkOrg){
        this.pkOrg = pkOrg;
    }

    public String getCode(){
        return this.code;
    }
    public void setCode(String code){
        this.code = code;
    }

    public String getName(){
        return this.name;
    }
    public void setName(String name){
        this.name = name;
    }

    public String getNamePrint(){
        return this.namePrint;
    }
    public void setNamePrint(String namePrint){
        this.namePrint = namePrint;
    }

    public String getEuCycle(){
        return this.euCycle;
    }
    public void setEuCycle(String euCycle){
        this.euCycle = euCycle;
    }

    public Long getCntCycle(){
        return this.cntCycle;
    }
    public void setCntCycle(Long cntCycle){
        this.cntCycle = cntCycle;
    }

    public String getFlagPlan(){
        return this.flagPlan;
    }
    public void setFlagPlan(String flagPlan){
        this.flagPlan = flagPlan;
    }

    public Long getCnt(){
        return this.cnt;
    }
    public void setCnt(Long cnt){
        this.cnt = cnt;
    }

    public String getEuAlways(){
        return this.euAlways;
    }
    public void setEuAlways(String euAlways){
        this.euAlways = euAlways;
    }

    public String getNote(){
        return this.note;
    }
    public void setNote(String note){
        this.note = note;
    }

    public String getSpcode(){
        return this.spcode;
    }
    public void setSpcode(String spcode){
        this.spcode = spcode;
    }

    public String getdCode(){
        return this.dCode;
    }
    public void setdCode(String dCode){
        this.dCode = dCode;
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

    public String getDelFlag() {
		return delFlag;
	}
    
	public void setDelFlag(String delFlag) {
		this.delFlag = delFlag;
	}
	
	public Date getTs(){
        return this.ts;
    }
	
    public void setTs(Date ts){
        this.ts = ts;
    }
	public String getDtFreqtype() {
		return dtFreqtype;
	}
	public void setDtFreqtype(String dtFreqtype) {
		this.dtFreqtype = dtFreqtype;
	}

    public String getEuExtime() { return euExtime; }
    public void setEuExtime(String euExtime) { this.euExtime = euExtime; }

    public String getEuFirsttype() { return euFirsttype; }
    public void setEuFirsttype(String euFirsttype) { this.euFirsttype = euFirsttype; }

    public String getEuRange() { return euRange; }
    public void setEuRange(String euRange) { this.euRange = euRange; }

    public Long getCntInterval() {
        return cntInterval;
    }

    public void setCntInterval(Long cntInterval) {
        this.cntInterval = cntInterval;
    }
}