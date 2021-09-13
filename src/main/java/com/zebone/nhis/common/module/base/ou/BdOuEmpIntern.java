package com.zebone.nhis.common.module.base.ou;

import java.util.Date;
import java.math.BigDecimal;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: BD_OU_EMP_INTERN 
 *
 * @since 2017-11-09 11:41:44
 */
@Table(value="BD_OU_EMP_INTERN")
public class BdOuEmpIntern extends BaseModule  {

	/**
	 * 主键
	 */
	@PK
	@Field(value="PK_EMPINTERN",id=KeyId.UUID)
    private String pkEmpintern;

	/**
	 * 机构
	 */
	@Field(value="PK_ORG")
	private String pkOrg;
	
	/**
	 * 人员
	 */
	@Field(value="PK_EMP")
    private String pkEmp;
	/**
	 * 来源
	 */
	@Field(value="DT_INTERNSRC")
    private String dtInternsrc;
	/**
	 * 开始时间
	 */
	@Field(value="DATE_BEGIN")
    private Date dateBegin;
	/**
	 * 结束时间
	 */
	@Field(value="DATE_END")
    private Date dateEnd;
	/**
	 * 实习工作方式
	 */
	@Field(value="EU_WORKTYPE")
    private String euWorktype;
	/**
	 * 轮转周期
	 */
	@Field(value="CYCLE")
    private BigDecimal cycle;
	/**
	 * 带教方式
	 */
	@Field(value="EU_TEACHTYPE")
    private String euTeachtype;
	/**
	 * 带教导师
	 */
	@Field(value="PK_EMP_TEACH")
    private String pkEmpTeach;
	/**
	 * 导师姓名
	 */
	@Field(value="NAME_EMP_TEACH")
    private String nameEmpTeach;
	/**
	 * 备注
	 */
	@Field(value="NOTE")
    private String note;
	/**
	 * 修改人
	 */
	@Field(value="MODIFIER")
    private String modifier;
	/**
	 * 修改时间
	 */
	@Field(value="MODITY_TIME")
    private Date modityTime;
	
	//新增，修改标志
	private String flag;		

	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}

	public String getPkOrg() {
		return pkOrg;
	}
	public void setPkOrg(String pkOrg) {
		this.pkOrg = pkOrg;
	}
	public String getPkEmpintern(){
        return this.pkEmpintern;
    }
    public void setPkEmpintern(String pkEmpintern){
        this.pkEmpintern = pkEmpintern;
    }

    public String getPkEmp(){
        return this.pkEmp;
    }
    public void setPkEmp(String pkEmp){
        this.pkEmp = pkEmp;
    }

    public String getDtInternsrc(){
        return this.dtInternsrc;
    }
    public void setDtInternsrc(String dtInternsrc){
        this.dtInternsrc = dtInternsrc;
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

    public String getEuWorktype(){
        return this.euWorktype;
    }
    public void setEuWorktype(String euWorktype){
        this.euWorktype = euWorktype;
    }

    public BigDecimal getCycle(){
        return this.cycle;
    }
    public void setCycle(BigDecimal cycle){
        this.cycle = cycle;
    }

    public String getEuTeachtype(){
        return this.euTeachtype;
    }
    public void setEuTeachtype(String euTeachtype){
        this.euTeachtype = euTeachtype;
    }

    public String getPkEmpTeach(){
        return this.pkEmpTeach;
    }
    public void setPkEmpTeach(String pkEmpTeach){
        this.pkEmpTeach = pkEmpTeach;
    }

    public String getNameEmpTeach(){
        return this.nameEmpTeach;
    }
    public void setNameEmpTeach(String nameEmpTeach){
        this.nameEmpTeach = nameEmpTeach;
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