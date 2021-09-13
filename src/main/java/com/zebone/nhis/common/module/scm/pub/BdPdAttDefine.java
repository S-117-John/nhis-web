package com.zebone.nhis.common.module.scm.pub;


import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: BD_PD_ATT_DEFINE - bd_pd_att_define 
 *
 * @since 2016-10-24 09:20:04
 */
@Table(value="BD_PD_ATT_DEFINE")
public class BdPdAttDefine extends BaseModule  {

	@PK
	@Field(value="PK_PDATTDEF",id=KeyId.UUID)
    private String pkPdattdef;

    /** EU_PDTYPE - 0药品，1卫生材料，2后勤物资，3医疗器械，4医疗设备，5通用设备，9 其他 */
	@Field(value="EU_PDTYPE")
    private String euPdtype;

	@Field(value="CODE_ATT")
    private String codeAtt;

	@Field(value="NAME_ATT")
    private String nameAtt;

	@Field(value="SPCODE")
    private String spcode;

	@Field(value="DESC_VAL")
    private String descVal;

	@Field(value="VAL_DEF")
    private String valDef;
	
	@Field(value="DT_PDTYPE")
	private String dtPdtype;		//物品分类

    public String getDtPdtype() {
		return dtPdtype;
	}
	public void setDtPdtype(String dtPdtype) {
		this.dtPdtype = dtPdtype;
	}
	public String getPkPdattdef(){
        return this.pkPdattdef;
    }
    public void setPkPdattdef(String pkPdattdef){
        this.pkPdattdef = pkPdattdef;
    }

    public String getEuPdtype(){
        return this.euPdtype;
    }
    public void setEuPdtype(String euPdtype){
        this.euPdtype = euPdtype;
    }

    public String getCodeAtt(){
        return this.codeAtt;
    }
    public void setCodeAtt(String codeAtt){
        this.codeAtt = codeAtt;
    }

    public String getNameAtt(){
        return this.nameAtt;
    }
    public void setNameAtt(String nameAtt){
        this.nameAtt = nameAtt;
    }

    public String getSpcode(){
        return this.spcode;
    }
    public void setSpcode(String spcode){
        this.spcode = spcode;
    }

    public String getDescVal(){
        return this.descVal;
    }
    public void setDescVal(String descVal){
        this.descVal = descVal;
    }

    public String getValDef(){
        return this.valDef;
    }
    public void setValDef(String valDef){
        this.valDef = valDef;
    }
}