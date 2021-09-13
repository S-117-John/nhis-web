package com.zebone.nhis.common.module.base.bd.code;

import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: BD_DICTATTR 
 *
 * @since 2018-08-01 05:46:22
 */
@Table(value="BD_DICTATTR")
public class BdDictattr extends BaseModule  {

	@PK
	@Field(value="PK_DICTATTR",id=KeyId.UUID)
    private String pkDictattr;

	@Field(value="PK_DICTATTRTEMP")
    private String pkDictattrtemp;

	@Field(value="PK_DICT")
    private String pkDict;

	@Field(value="VAL_ATTR")
    private String valAttr;

	@Field(value="MODIFIER")
    private String modifier;
	
	@Field(value="CODE_ATTR")
	private String codeAttr;
	
	@Field(value="NAME_ATTR")
	private String nameAttr;

	@Field(value="MODITY_TIME")
    private Date modityTime;


    public String getCodeAttr() {
		return codeAttr;
	}
	public void setCodeAttr(String codeAttr) {
		this.codeAttr = codeAttr;
	}
	public String getNameAttr() {
		return nameAttr;
	}
	public void setNameAttr(String nameAttr) {
		this.nameAttr = nameAttr;
	}
	public String getPkDictattr(){
        return this.pkDictattr;
    }
    public void setPkDictattr(String pkDictattr){
        this.pkDictattr = pkDictattr;
    }

    public String getPkDictattrtemp(){
        return this.pkDictattrtemp;
    }
    public void setPkDictattrtemp(String pkDictattrtemp){
        this.pkDictattrtemp = pkDictattrtemp;
    }

    public String getPkDict(){
        return this.pkDict;
    }
    public void setPkDict(String pkDict){
        this.pkDict = pkDict;
    }

    public String getValAttr(){
        return this.valAttr;
    }
    public void setValAttr(String valAttr){
        this.valAttr = valAttr;
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