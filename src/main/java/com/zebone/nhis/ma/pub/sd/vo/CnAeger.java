package com.zebone.nhis.ma.pub.sd.vo;

import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.FieldType;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: CN_AEGER 
 *
 * @since 2020-01-15 10:11:38
 */
@Table(value="CN_AEGER")
public class CnAeger{

	@PK
	@Field(value="PK_CNAEGER",id=KeyId.UUID)
    private String pkCnaeger;
	
	@Field(value="pk_org",userfield="pkOrg",userfieldscop=FieldType.INSERT)
	private String pkOrg;

	@Field(value="EU_PVTYPE")
    private String euPvtype;

	@Field(value="PK_PV")
    private String pkPv;

	@Field(value="NAME_DIAG")
    private String nameDiag;

	@Field(value="DESC_AEGERS")
    private String descAegers;

	@Field(value="DATE_BEGIN")
    private Date dateBegin;

	@Field(value="DATE_END")
    private Date dateEnd;

	@Field(value="PK_EMP",userfield="pkEmp",userfieldscop=FieldType.INSERT)
    private String pkEmp;

	@Field(value="NAME_EMP",userfield="nameEmp",userfieldscop=FieldType.INSERT)
    private String nameEmp;

	@Field(value="NOTE")
    private String note;
	
	@Field(value="CREATOR",userfield="pkEmp",userfieldscop=FieldType.INSERT)
	private String creator;
	
	@Field(value="create_time",date=FieldType.INSERT)
	private Date createTime;
	
	@Field(value="MODIFIER",userfield="pkEmp",userfieldscop=FieldType.UPDATE)
    private String modifier;

	@Field(value="MODITY_TIME",date=FieldType.UPDATE)
    private Date modityTime;
	
	@Field(value="TS",date=FieldType.ALL)
	private Date ts;
	
	@Field(value="DATE_OUT")
	private Date dateOut;
	
	@Field(value="ADDRESS")
    private String address;
	
	/**
     *删除标志  
     */
	@Field(value="DEL_FLAG")
	private String delFlag="0";


    public String getPkCnaeger(){
        return this.pkCnaeger;
    }
    public void setPkCnaeger(String pkCnaeger){
        this.pkCnaeger = pkCnaeger;
    }

    public String getEuPvtype(){
        return this.euPvtype;
    }
    public void setEuPvtype(String euPvtype){
        this.euPvtype = euPvtype;
    }

    public String getPkPv(){
        return this.pkPv;
    }
    public void setPkPv(String pkPv){
        this.pkPv = pkPv;
    }

    public String getNameDiag(){
        return this.nameDiag;
    }
    public void setNameDiag(String nameDiag){
        this.nameDiag = nameDiag;
    }

    public String getDescAegers(){
        return this.descAegers;
    }
    public void setDescAegers(String descAegers){
        this.descAegers = descAegers;
    }

    public Date getDateBegin(){
        return this.dateBegin;
    }
    public void setDateBegin(Date dateBegin){
        this.dateBegin = dateBegin;
    }

    public Date getDateEnd(){
        return this.dateEnd;
    }
    public void setDateEnd(Date dateEnd){
        this.dateEnd = dateEnd;
    }

    public String getPkEmp(){
        return this.pkEmp;
    }
    public void setPkEmp(String pkEmp){
        this.pkEmp = pkEmp;
    }

    public String getNameEmp(){
        return this.nameEmp;
    }
    public void setNameEmp(String nameEmp){
        this.nameEmp = nameEmp;
    }

    public String getNote(){
        return this.note;
    }
    public void setNote(String note){
        this.note = note;
    }

    public Date getModityTime(){
        return this.modityTime;
    }
    public void setModityTime(Date modityTime){
        this.modityTime = modityTime;
    }
	public String getPkOrg() {
		return pkOrg;
	}
	public void setPkOrg(String pkOrg) {
		this.pkOrg = pkOrg;
	}
	public String getCreator() {
		return creator;
	}
	public void setCreator(String creator) {
		this.creator = creator;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public String getModifier() {
		return modifier;
	}
	public void setModifier(String modifier) {
		this.modifier = modifier;
	}
	public Date getTs() {
		return ts;
	}
	public void setTs(Date ts) {
		this.ts = ts;
	}
	public String getDelFlag() {
		return delFlag;
	}
	public void setDelFlag(String delFlag) {
		this.delFlag = delFlag;
	}
	public Date getDateOut() {
		return dateOut;
	}
	public void setDateOut(Date dateOut) {
		this.dateOut = dateOut;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
}