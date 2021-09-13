package com.zebone.nhis.common.module.base.bd.code;


import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.FieldType;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: BD_SYSPARAM_TEMP  - bd_sysparam_temp 
 *
 * @since 2016-10-09 08:37:12
 */
@Table(value="BD_SYSPARAM_TEMP")
public class BdSysparamTemp extends BaseModule  {

	@PK
	@Field(value="PK_PARAMTEMP",id=KeyId.UUID)
    private String pkParamtemp;

    /** DT_PACATE - 0公共，1排班，2患者，3就诊，4临床，5收费，6医技，7其他 */
	@Field(value="DT_PACATE")
    private String dtPacate;

	@Field(value="CODE")
    private String code;

	@Field(value="NAME")
    private String name;

	@Field(value="VAL_DEF")
    private String valDef;

	@Field(value="DESC_PARAM")
    private String descParam;

    /** EU_RANGE - 0 全局，1 机构，2 部门，3 工作站 */
	@Field(value="EU_RANGE")
    private String euRange;

	@Field(value="NOTE")
    private String note;
	
	@Field(value="FLAG_PUB")
	private String flagPub;


    public String getPkParamtemp(){
        return this.pkParamtemp;
    }
    public void setPkParamtemp(String pkParamtemp){
        this.pkParamtemp = pkParamtemp;
    }

    public String getDtPacate(){
        return this.dtPacate;
    }
    public void setDtPacate(String dtPacate){
        this.dtPacate = dtPacate;
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

    public String getValDef(){
        return this.valDef;
    }
    public void setValDef(String valDef){
        this.valDef = valDef;
    }

    public String getDescParam(){
        return this.descParam;
    }
    public void setDescParam(String descParam){
        this.descParam = descParam;
    }

    public String getEuRange(){
        return this.euRange;
    }
    public void setEuRange(String euRange){
        this.euRange = euRange;
    }

    public String getNote(){
        return this.note;
    }
    public void setNote(String note){
        this.note = note;
    }
	public String getFlagPub() {
		return flagPub;
	}
	public void setFlagPub(String flagPub) {
		this.flagPub = flagPub;
	}

}