package com.zebone.nhis.common.module.cn.cp;

import java.util.Date;
import java.util.List;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;


/**
 * Table: BD_CP_CATEORD  - bd_cp_cateord 
 *
 * @since 2016-12-12 02:43:26
 */
@Table(value="BD_CP_CATEORD")
public class BdCpCateord extends BaseModule  {

	@PK
	@Field(value="PK_CATEORD",id=KeyId.UUID)
    private String pkCateord;

	@Field(value="CODE_ORD")
    private String codeOrd;

	@Field(value="NAME_ORD")
    private String nameOrd;

    /** EU_CATETYPE - 0 名称，1 字典 */
	@Field(value="EU_CATETYPE")
    private String euCatetype;

	@Field(value="MODIFIER")
    private String modifier;

	@Field(value="MODITY_TIME")
    private Date modityTime;

	@Field(value="SPCODE")
    private String spcode;
	
	@Field(value="D_CODE")
    private String dCode;

	@Field(value="EU_ORDTYPE")
    private String euOrdtype;

	public String getPkCateord(){
        return this.pkCateord;
    }
    public void setPkCateord(String pkCateord){
        this.pkCateord = pkCateord;
    }

    public String getCodeOrd(){
        return this.codeOrd;
    }
    public void setCodeOrd(String codeOrd){
        this.codeOrd = codeOrd;
    }

    public String getNameOrd(){
        return this.nameOrd;
    }
    public void setNameOrd(String nameOrd){
        this.nameOrd = nameOrd;
    }

    public String getEuCatetype(){
        return this.euCatetype;
    }
    public void setEuCatetype(String euCatetype){
        this.euCatetype = euCatetype;
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
    
    private List<BdCpCateordDt> details;


	public List<BdCpCateordDt> getDetails() {
		return details;
	}
	public void setDetails(List<BdCpCateordDt> details) {
		this.details = details;
	}
    
	private String rowStatus;

	public String getRowStatus() {
		return rowStatus;
	}
	public void setRowStatus(String rowStatus) {
		this.rowStatus = rowStatus;
	}
	public String getSpcode() {
		return spcode;
	}
	public void setSpcode(String spcode) {
		this.spcode = spcode;
	}
	public String getdCode() {
		return dCode;
	}
	public void setdCode(String dCode) {
		this.dCode = dCode;
	}
	public String getEuOrdtype() {
		return euOrdtype;
	}
	public void setEuOrdtype(String euOrdtype) {
		this.euOrdtype = euOrdtype;
	}
    private String delMessage;

	public String getDelMessage() {
		return delMessage;
	}
	public void setDelMessage(String delMessage) {
		this.delMessage = delMessage;
	}
    
}