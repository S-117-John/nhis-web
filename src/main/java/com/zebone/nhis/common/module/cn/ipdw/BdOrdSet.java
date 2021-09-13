package com.zebone.nhis.common.module.cn.ipdw;


import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;


/**
 * Table: BD_ORD_SET - bd_ord_set 
 *
 * @since 2016-10-25 11:33:07
 */
@Table(value="BD_ORD_SET")
public class BdOrdSet extends BaseModule  {

	@PK
	@Field(value="PK_ORDSET",id=KeyId.UUID)
    private String pkOrdset;

	@Field(value="CODE")
    private String code;

	@Field(value="NAME")
    private String name;

    /** EU_RIGHT - 0 全院，1 科室，2 医生 */
	@Field(value="EU_RIGHT")
    private String euRight;

	@Field(value="PK_DEPT")
    private String pkDept;

	@Field(value="PK_EMP")
    private String pkEmp;

	@Field(value="PK_DIAG")
    private String pkDiag;

	@Field(value="PK_PARENT")
    private String pkParent;

	@Field(value="SPCODE")
    private String spcode;

	@Field(value="D_CODE")
    private String dCode;
	
    private String rowStatus;
    
    @Field(value="EU_ORDTYPE" )
    private String euOrdtype;
    
    @Field(value="MODIFIER")
    private String modifier;
    
    @Field(value="FLAG_OP")
    private String flagOp;
    
    @Field(value="FLAG_IP")
    private String flagIp;
    @Field(value="NOTE")
    private  String note;
    
    public String getModifier() {
		return modifier;
	}
	public void setModifier(String modifier) {
		this.modifier = modifier;
	}
	
	
	public String getEuOrdtype() {
		return euOrdtype;
	}
	public void setEuOrdtype(String euOrdtype) {
		this.euOrdtype = euOrdtype;
	}
	public String getRowStatus() {
		return rowStatus;
	}
	public void setRowStatus(String rowStatus) {
		this.rowStatus = rowStatus;
	}
	public String getPkOrdset(){
        return this.pkOrdset;
    }
    public void setPkOrdset(String pkOrdset){
        this.pkOrdset = pkOrdset;
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

    public String getEuRight(){
        return this.euRight;
    }
    public void setEuRight(String euRight){
        this.euRight = euRight;
    }

    public String getPkDept(){
        return this.pkDept;
    }
    public void setPkDept(String pkDept){
        this.pkDept = pkDept;
    }

    public String getPkEmp(){
        return this.pkEmp;
    }
    public void setPkEmp(String pkEmp){
        this.pkEmp = pkEmp;
    }

    public String getPkDiag(){
        return this.pkDiag;
    }
    public void setPkDiag(String pkDiag){
        this.pkDiag = pkDiag;
    }

    public String getPkParent(){
        return this.pkParent;
    }
    public void setPkParent(String pkParent){
        this.pkParent = pkParent;
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
	public String getFlagOp() {
		return flagOp;
	}
	public void setFlagOp(String flagOp) {
		this.flagOp = flagOp;
	}
	public String getFlagIp() {
		return flagIp;
	}
	public void setFlagIp(String flagIp) {
		this.flagIp = flagIp;
	}

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}