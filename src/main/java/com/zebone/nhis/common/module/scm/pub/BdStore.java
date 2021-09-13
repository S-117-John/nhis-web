package com.zebone.nhis.common.module.scm.pub;


import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: BD_STORE - bd_store 
 *
 * @since 2016-10-21 01:12:45
 */
@Table(value="BD_STORE")
public class BdStore extends BaseModule  {

	@PK
	@Field(value="PK_STORE",id=KeyId.UUID)
    private String pkStore;

	@Field(value="CODE")
    private String code;

	@Field(value="NAME")
    private String name;

	@Field(value="PK_DEPT")
    private String pkDept;

    /** EU_OUTTYPE - 0 先进先出，1 有效期，2 后进先出，3 选择出库 */
	@Field(value="EU_OUTTYPE")
    private String euOuttype;

	@Field(value="SPCODE")
    private String spcode;

	@Field(value="D_CODE")
    private String dCode;

    /** EU_PACKTYPE - 0门诊，1住院 */
	@Field(value="EU_PACKTYPE")
    private String euPacktype;
	
	/** EU_TYPE -0普通，1代销*/
	@Field(value="EU_TYPE")
	private String euType;	//仓库类型

    /** Note -备注*/
    @Field(value="Note")
    private String note;	//备注

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getEuType() {
		return euType;
	}
	public void setEuType(String euType) {
		this.euType = euType;
	}
	public String getPkStore(){
        return this.pkStore;
    }
    public void setPkStore(String pkStore){
        this.pkStore = pkStore;
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

    public String getPkDept(){
        return this.pkDept;
    }
    public void setPkDept(String pkDept){
        this.pkDept = pkDept;
    }

    public String getEuOuttype(){
        return this.euOuttype;
    }
    public void setEuOuttype(String euOuttype){
        this.euOuttype = euOuttype;
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

    public String getEuPacktype(){
        return this.euPacktype;
    }
    public void setEuPacktype(String euPacktype){
        this.euPacktype = euPacktype;
    }
}