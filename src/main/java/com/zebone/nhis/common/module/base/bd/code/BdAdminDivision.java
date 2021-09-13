package com.zebone.nhis.common.module.base.bd.code;

import java.util.Date;
import java.math.BigDecimal;

import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.FieldType;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: BD_ADMIN_DIVISION 
 *
 * @since 2016-08-30 01:06:52
 */
@Table(value="BD_ADMIN_DIVISION")
public class BdAdminDivision   {

	@PK
	@Field(value="PK_DIVISION",id=KeyId.UUID)
    private String pkDivision;

	@Field(value="CODE_DIV")
    private String codeDiv;

	@Field(value="NAME_DIV")
    private String nameDiv;

	@Field(value="PK_FATHER")
    private String pkFather;

	@Field(value="PY_CODE")
    private String pyCode;

	@Field(value="D_CODE")
    private String dCode;

	@Field(value="NOTE")
    private String note;

	@Field(value="SORTNO")
    private BigDecimal sortno;

	@Field(userfield="pkEmp",userfieldscop=FieldType.INSERT)
    private String creator;

	@Field(value="CREATE_TIME",date=FieldType.INSERT)
    private Date createTime;

    /** DEL_FLAG - 0:未删除 1:已删除 */
	@Field(value="DEL_FLAG")
    private String delFlag;

	@Field(date=FieldType.ALL)
    private Date ts;

	@Field(value="prov")
    private String prov;
	@Field(value="city")
    private String city;
	@Field(value="dist")
    private String dist;

    public String getPkDivision(){
        return this.pkDivision;
    }
    public void setPkDivision(String pkDivision){
        this.pkDivision = pkDivision;
    }

    public String getCodeDiv(){
        return this.codeDiv;
    }
    public void setCodeDiv(String codeDiv){
        this.codeDiv = codeDiv;
    }

    public String getNameDiv(){
        return this.nameDiv;
    }
    public void setNameDiv(String nameDiv){
        this.nameDiv = nameDiv;
    }

    public String getPkFather(){
        return this.pkFather;
    }
    public void setPkFather(String pkFather){
        this.pkFather = pkFather;
    }

    public String getPyCode(){
        return this.pyCode;
    }
    public void setPyCode(String pyCode){
        this.pyCode = pyCode;
    }

    public String getdCode(){
        return this.dCode;
    }
    public void setdCode(String dCode){
        this.dCode = dCode;
    }

    public String getNote(){
        return this.note;
    }
    public void setNote(String note){
        this.note = note;
    }

    public BigDecimal getSortno(){
        return this.sortno;
    }
    public void setSortno(BigDecimal sortno){
        this.sortno = sortno;
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
	public String getProv() {
		return prov;
	}
	public void setProv(String prov) {
		this.prov = prov;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getDist() {
		return dist;
	}
	public void setDist(String dist) {
		this.dist = dist;
	}
}