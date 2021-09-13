package com.zebone.nhis.common.module.base.bd.res;


import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * 医技资源表
 * Table: BD_RES_MSP  - bd_res_msp 
 *
 * @since 2016-08-23 10:38:59
 */
@Table(value="BD_RES_MSP")
public class BdResMsp extends BaseModule  {

	@PK
	@Field(value="PK_MSP",id=KeyId.UUID)
    private String pkMsp;

    /** DT_RESTYPE - 来自码表 */
	@Field(value="DT_RESTYPE")
    private String dtRestype;

	@Field(value="CODE")
    private String code;

	@Field(value="NAME")
    private String name;

	@Field(value="PK_DEPT")
    private String pkDept;

	@Field(value="PY_CODE")
    private String pyCode;

	@Field(value="D_CODE")
    private String dCode;

	@Field(value="CODE_FA")
    private String codeFa;

    /** NAME_PLACE - 可由地点带入，也可直接录入。 */
	@Field(value="NAME_PLACE")
    private String namePlace;

	@Field(value="HOLD_FLAG")
	private String holdFlag;
	
    public String getPkMsp(){
        return this.pkMsp;
    }
    public void setPkMsp(String pkMsp){
        this.pkMsp = pkMsp;
    }

    public String getDtRestype(){
        return this.dtRestype;
    }
    public void setDtRestype(String dtRestype){
        this.dtRestype = dtRestype;
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

    public String getCodeFa(){
        return this.codeFa;
    }
    public void setCodeFa(String codeFa){
        this.codeFa = codeFa;
    }

    public String getNamePlace(){
        return this.namePlace;
    }
    public void setNamePlace(String namePlace){
        this.namePlace = namePlace;
    }
	public String getHoldFlag() {
		return holdFlag;
	}
	public void setHoldFlag(String holdFlag) {
		this.holdFlag = holdFlag;
	}
}