package com.zebone.nhis.common.module.base.bd.price;


import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.FieldType;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: BD_INVCATE  - bd_invcate 
 *
 * @since 2016-09-19 06:51:28
 */
@Table(value="BD_INVCATE")
public class BdInvcate extends BaseModule  {

	@PK
	@Field(value="PK_INVCATE",id=KeyId.UUID)
    private String pkInvcate;

    /** EU_TYPE - 0门诊发票 1住院发票 3 挂号凭条 4患者预交金收据 5住院预交金收据 6单位发票 9其他 */
	@Field(value="EU_TYPE")
    private String euType;

	@Field(value="CODE")
    private String code;

	@Field(value="NAME")
    private String name;

    /** UNIT_INV - 票据的包装单位名称：例如本，箱等。 */
	@Field(value="UNIT_INV")
    private String unitInv;

    /** CNT - 对应包装单位下的票数张数。 */
	@Field(value="CNT")
    private Long cnt;

    /** EU_INVNUM - 0 固定位数，1 任意位数 */
	@Field(value="EU_INVNUM")
    private String euInvnum;

	@Field(value="LENGTH")
    private Long length;

	@Field(value="PREFIX")
    private String prefix;

	@Field(value="FLAG_ACTIVE")
    private String flagActive;


    public String getPkInvcate(){
        return this.pkInvcate;
    }
    public void setPkInvcate(String pkInvcate){
        this.pkInvcate = pkInvcate;
    }

    public String getEuType(){
        return this.euType;
    }
    public void setEuType(String euType){
        this.euType = euType;
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

    public String getUnitInv(){
        return this.unitInv;
    }
    public void setUnitInv(String unitInv){
        this.unitInv = unitInv;
    }

    public Long getCnt(){
        return this.cnt;
    }
    public void setCnt(Long cnt){
        this.cnt = cnt;
    }

    public String getEuInvnum(){
        return this.euInvnum;
    }
    public void setEuInvnum(String euInvnum){
        this.euInvnum = euInvnum;
    }

    public Long getLength(){
        return this.length;
    }
    public void setLength(Long length){
        this.length = length;
    }

    public String getPrefix(){
        return this.prefix;
    }
    public void setPrefix(String prefix){
        this.prefix = prefix;
    }

    public String getFlagActive(){
        return this.flagActive;
    }
    public void setFlagActive(String flagActive){
        this.flagActive = flagActive;
    }

}