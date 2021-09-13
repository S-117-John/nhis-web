package com.zebone.nhis.common.module.base.bd.mk;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;


/**
 * Table: BD_SUPPLY  - bd_supply 
 *
 * @since 2016-09-13 04:26:40
 */
@Table(value="BD_SUPPLY")
public class BdSupply extends BaseModule  {

	private static final long serialVersionUID = 1L;

	/** PK_SUPPLY - 用法主键 */
	@PK
	@Field(value="PK_SUPPLY",id=KeyId.UUID)
    private String pkSupply;

    /** CODE - 编码 */
	@Field(value="CODE")
    private String code;

    /** NAME - 名称 */
	@Field(value="NAME")
    private String name;

    /** NAME_PRINT - 打印名称 */
	@Field(value="NAME_PRINT")
    private String namePrint;

    /** SPCODE - 拼音码 */
	@Field(value="SPCODE")
    private String spcode;

    /** D_CODE - 自定义码 */
	@Field(value="D_CODE")
    private String dCode;

    /** FLAG_PRINT - 医嘱打印标志 */
	@Field(value="FLAG_PRINT")
    private String flagPrint;

    /** PK_SUPPLYCATE - 用法分类 */
	@Field(value="PK_SUPPLYCATE")
    private String pkSupplycate;

    /** DT_EXCARDTYPE - 对应执行卡类型：例如 1 护理卡 2 口服卡 3 注射卡 4 输液卡 5 饮食卡 99  其他卡 */
	@Field(value="DT_EXCARDTYPE")
    private String dtExcardtype;

    /** FLAG_OP - 门诊使用标志 */
	@Field(value="FLAG_OP")
    private String flagOp;

    /** FLAG_ER - 急诊使用标志 */
	@Field(value="FLAG_ER")
    private String flagEr;

    /** FLAG_PE - 体检使用标志 */
	@Field(value="FLAG_PE")
    private String flagPe;

    /** FLAG_HM - 家床使用标志 */
	@Field(value="FLAG_HM")
    private String flagHm;

    /** FLAG_IP - 住院使用标志 */
	@Field(value="FLAG_IP")
    private String flagIp;

    /** FLAG_PIVAS - 配液标志 */
	@Field(value="FLAG_PIVAS")
    private String flagPivas;

    /** FLAG_ST - 试敏标志 */
	@Field(value="FLAG_ST")
    private String flagSt;

    /** NOTE - 备注 */
	@Field(value="NOTE")
    private String note;
	
	/** SORTNO - 顺序号*/
	@Field(value="SORTNO")
	private Long sortNo;
	
	//附加用法
	@Field(value="FLAG_ADD")
    private String flagAdd;
	
	/** EU_RANGE - 使用范围：例如 0 不限 1 门急诊 2 住院 8 其他 */
	@Field(value="EU_RANGE")
	private String euRange;

    public String getFlagAdd() {
		return flagAdd;
	}
	public void setFlagAdd(String flagAdd) {
		this.flagAdd = flagAdd;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public Long getSortNo() {
		return sortNo;
	}
	public void setSortNo(Long sortNo) {
		this.sortNo = sortNo;
	}
	public String getPkSupply(){
        return this.pkSupply;
    }
    public void setPkSupply(String pkSupply){
        this.pkSupply = pkSupply;
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

    public String getNamePrint(){
        return this.namePrint;
    }
    public void setNamePrint(String namePrint){
        this.namePrint = namePrint;
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

    public String getFlagPrint(){
        return this.flagPrint;
    }
    public void setFlagPrint(String flagPrint){
        this.flagPrint = flagPrint;
    }

    public String getPkSupplycate(){
        return this.pkSupplycate;
    }
    public void setPkSupplycate(String pkSupplycate){
        this.pkSupplycate = pkSupplycate;
    }

    public String getDtExcardtype(){
        return this.dtExcardtype;
    }
    public void setDtExcardtype(String dtExcardtype){
        this.dtExcardtype = dtExcardtype;
    }

    public String getFlagOp(){
        return this.flagOp;
    }
    public void setFlagOp(String flagOp){
        this.flagOp = flagOp;
    }

    public String getFlagEr(){
        return this.flagEr;
    }
    public void setFlagEr(String flagEr){
        this.flagEr = flagEr;
    }

    public String getFlagPe(){
        return this.flagPe;
    }
    public void setFlagPe(String flagPe){
        this.flagPe = flagPe;
    }

    public String getFlagHm(){
        return this.flagHm;
    }
    public void setFlagHm(String flagHm){
        this.flagHm = flagHm;
    }

    public String getFlagIp(){
        return this.flagIp;
    }
    public void setFlagIp(String flagIp){
        this.flagIp = flagIp;
    }

    public String getFlagPivas(){
        return this.flagPivas;
    }
    public void setFlagPivas(String flagPivas){
        this.flagPivas = flagPivas;
    }

    public String getFlagSt(){
        return this.flagSt;
    }
    public void setFlagSt(String flagSt){
        this.flagSt = flagSt;
    }

    public String getNote(){
        return this.note;
    }
    public void setNote(String note){
        this.note = note;
    }
	public String getEuRange() {
		return euRange;
	}
	public void setEuRange(String euRange) {
		this.euRange = euRange;
	}

}