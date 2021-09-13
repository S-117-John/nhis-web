package com.zebone.nhis.common.module.base.bd.res;


import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: BD_RES_OPT  - bd_res_opt 
 *
 * @since 2016-08-25 11:00:03
 */
@Table(value="BD_RES_OPT")
public class BdResOpt extends BaseModule  {

	@PK
	@Field(value="PK_OPT",id=KeyId.UUID)
    private String pkOpt;

	@Field(value="CODE")
    private String code;

	@Field(value="NAME")
    private String name;

    /** DT_OPTTYPE - 来自码表 */
	@Field(value="DT_OPTTYPE")
    private String dtOpttype;

	@Field(value="PK_DEPT")
    private String pkDept;

	@Field(value="PY_CODE")
    private String pyCode;

	@Field(value="D_CODE")
    private String dCode;

    /** HOLD_FLAG - 1：占用
0：空闲 */
	@Field(value="HOLD_FLAG")
    private String holdFlag;

	@Field(value="CODE_FA")
    private String codeFa;

    /** NAME_PLACE - 可由地点带入，也可直接录入。 */
	@Field(value="NAME_PLACE")
    private String namePlace;


    public String getPkOpt(){
        return this.pkOpt;
    }
    public void setPkOpt(String pkOpt){
        this.pkOpt = pkOpt;
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

    public String getDtOpttype(){
        return this.dtOpttype;
    }
    public void setDtOpttype(String dtOpttype){
        this.dtOpttype = dtOpttype;
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

    public String getHoldFlag(){
        return this.holdFlag;
    }
    public void setHoldFlag(String holdFlag){
        this.holdFlag = holdFlag;
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
}