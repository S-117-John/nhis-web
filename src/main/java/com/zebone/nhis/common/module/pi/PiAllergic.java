package com.zebone.nhis.common.module.pi;

import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.Field.FieldType;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;

import java.util.Date;


/**
 * Table: PI_ALLERGIC  - 患者信息-过敏史 
 *
 * @since 2016-09-12 09:48:04
 */
@Table(value="PI_ALLERGIC")
public class PiAllergic   {

    /** PK_PIAL - 过敏史主键 */
	@PK
	@Field(value="PK_PIAL",id=KeyId.UUID)
    private String pkPial;

    /** PK_PI - 患者主键 */
	@Field(value="PK_PI")
    private String pkPi;
	
	@Field(value="PK_BU")
	private String pkBu;

    /** EU_MCSRC - 0 患者就诊; 1 健康档案; 2 信息平台 */
	@Field(value="EU_MCSRC")
    private String euMcsrc;

    /** EU_ALTYPE - 1速发性过敏 2细胞病毒性过敏 3 免疫复合性过敏 4 迟发性过敏 */
	@Field(value="EU_ALTYPE")
    private String euAltype;
	/**
	 * 药理分类
	 */
	@Field(value="DT_PHARM")
	private String dtPharm;

    /** NAME_AL - 过敏源 */
	@Field(value="NAME_AL")
    private String nameAl;

    /** NAME_AL - 过敏源编码 */
    @Field(value="DT_AL")
    private String dtAl;

    /** DATE_FIND - 发现日期 */
	@Field(value="DATE_FIND")
    private Date dateFind;

    /** DATE_REC - 登记日期 */
	@Field(value="DATE_REC")
    private Date dateRec;

    /** PK_EMP_REC - 登记人编码 */
	@Field(value="PK_EMP_REC")
    private String pkEmpRec;

    /** NAME_EMP_REC - 登记人姓名 */
	@Field(value="NAME_EMP_REC")
    private String nameEmpRec;

    /** NOTE - 备注 */
	@Field(value="NOTE")
    private String note;

    /** CREATOR - 创建人 */
	@Field(value="CREATOR",userfield="pkEmp",userfieldscop=FieldType.INSERT)
    private String creator;

    /** CREATE_TIME - 创建时间 */
	@Field(value="CREATE_TIME",date=FieldType.INSERT)
    private Date createTime;

    /** MODIFIER - 修改人 */
	@Field(userfield="pkEmp",userfieldscop=FieldType.ALL)
    private String modifier;

    /** DEL_FLAG - 删除标志 */
	@Field(value="DEL_FLAG")
    private String delFlag;

    /** TS - 时间戳 */
	@Field(value="TS",date=FieldType.ALL)
    private Date ts;
	
    public String getDtPharm() {
		return dtPharm;
	}
	public void setDtPharm(String dtPharm) {
		this.dtPharm = dtPharm;
	}
	public String getPkPial(){
        return this.pkPial;
    }
    public void setPkPial(String pkPial){
        this.pkPial = pkPial;
    }
    public String getPkBu() {
		return pkBu;
	}
	public void setPkBu(String pkBu) {
		this.pkBu = pkBu;
	}
	public String getPkPi(){
        return this.pkPi;
    }
    public void setPkPi(String pkPi){
        this.pkPi = pkPi;
    }

    public String getEuMcsrc(){
        return this.euMcsrc;
    }
    public void setEuMcsrc(String euMcsrc){
        this.euMcsrc = euMcsrc;
    }

    public String getEuAltype(){
        return this.euAltype;
    }
    public void setEuAltype(String euAltype){
        this.euAltype = euAltype;
    }

    public String getNameAl(){
        return this.nameAl;
    }
    public void setNameAl(String nameAl){
        this.nameAl = nameAl;
    }

    public Date getDateFind(){
        return this.dateFind;
    }
    public void setDateFind(Date dateFind){
        this.dateFind = dateFind;
    }

    public Date getDateRec(){
        return this.dateRec;
    }
    public void setDateRec(Date dateRec){
        this.dateRec = dateRec;
    }

    public String getPkEmpRec(){
        return this.pkEmpRec;
    }
    public void setPkEmpRec(String pkEmpRec){
        this.pkEmpRec = pkEmpRec;
    }

    public String getNameEmpRec(){
        return this.nameEmpRec;
    }
    public void setNameEmpRec(String nameEmpRec){
        this.nameEmpRec = nameEmpRec;
    }

    public String getNote(){
        return this.note;
    }
    public void setNote(String note){
        this.note = note;
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

    public String getDtAl() {
        return dtAl;
    }

    public void setDtAl(String dtAl) {
        this.dtAl = dtAl;
    }
}