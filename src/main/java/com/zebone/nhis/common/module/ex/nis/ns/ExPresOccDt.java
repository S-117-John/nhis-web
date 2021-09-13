package com.zebone.nhis.common.module.ex.nis.ns;

import java.util.Date;
import java.math.BigDecimal;

import ca.uhn.hl7v2.model.v24.segment.VAR;
import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: EX_PRES_OCC_DT  - ex_pres_occ_dt 
 *
 * @since 2016-11-11 02:53:47
 */
@Table(value="EX_PRES_OCC_DT")
public class ExPresOccDt extends BaseModule  {

	@PK
	@Field(value="PK_PRESOCCDT",id=KeyId.UUID)
    private String pkPresoccdt;

	@Field(value="PK_PRESOCC")
    private String pkPresocc;

    /** BOX_NO - 指配药后放入的药框编码，一般电子药框接口需要 */
	@Field(value="BOX_NO")
    private String boxNo;

	@Field(value="PK_CNORD")
    private String pkCnord;

	@Field(value="PK_PD")
    private String pkPd;

	@Field(value="PK_UNIT")
    private String pkUnit;

	@Field(value="PACK_SIZE")
    private Integer packSize;

	@Field(value="PRICE")
    private BigDecimal price;

    /** QUAN_CG - 收费数量基本 = 收费数量当前 * 包装量 */
	@Field(value="QUAN_CG")
    private Double quanCg;

    /** ORDS_CG - 仅在中药开立时有效，表示中药的实发付数。默认为1 */
	@Field(value="ORDS_CG")
    private Double ordsCg;

	@Field(value="AMOUNT_CG")
    private BigDecimal amountCg;

	@Field(value="QUAN_DE")
    private Double quanDe;

	@Field(value="ORDS_DE")
    private Double ordsDe;

	@Field(value="AMOUNT_DE")
    private BigDecimal amountDe;

	@Field(value="QUAN_BACK")
    private Double quanBack;

	@Field(value="ORDS_BACK")
    private Double ordsBack;

	@Field(value="AMOUNT_BACK")
    private BigDecimal amountBack;

    /** QUAN_RET - 退费时，对应的当前包装单位下的退费数量：
本次退费数量=收费数量-退费数量-发出数量+退回数量；
约束关系：退费数量<=收费数量；退回数量<=发出数量；发出数量<=收费数量。 */
	@Field(value="QUAN_RET")
    private Double quanRet;

	@Field(value="ORDS_RET")
    private Double ordsRet;

	@Field(value="AMOUNT_RET")
    private BigDecimal amountRet;

	@Field(value="MODIFIER")
    private String modifier;

	@Field(value="MODITY_TIME")
    private Date modityTime;

	@Field(value = "QUAN_MIN_DE")
	private Double quanMinDe;

	@Field(value = "QUAN_MIN_BACK")
	private Double quanMinBack;

	@Field(value = "QUAN_MIN_RET")
	private Double quanMinRet;

	public String getPkPresoccdt(){
        return this.pkPresoccdt;
    }
    public void setPkPresoccdt(String pkPresoccdt){
        this.pkPresoccdt = pkPresoccdt;
    }

    public String getPkPresocc(){
        return this.pkPresocc;
    }
    public void setPkPresocc(String pkPresocc){
        this.pkPresocc = pkPresocc;
    }

    public String getBoxNo(){
        return this.boxNo;
    }
    public void setBoxNo(String boxNo){
        this.boxNo = boxNo;
    }

    public String getPkCnord(){
        return this.pkCnord;
    }
    public void setPkCnord(String pkCnord){
        this.pkCnord = pkCnord;
    }

    public String getPkPd(){
        return this.pkPd;
    }
    public void setPkPd(String pkPd){
        this.pkPd = pkPd;
    }

    public String getPkUnit(){
        return this.pkUnit;
    }
    public void setPkUnit(String pkUnit){
        this.pkUnit = pkUnit;
    }

  

    public BigDecimal getPrice(){
        return this.price;
    }
    public void setPrice(BigDecimal price){
        this.price = price;
    }

   

   

    public BigDecimal getAmountCg(){
        return this.amountCg;
    }
    public void setAmountCg(BigDecimal amountCg){
        this.amountCg = amountCg;
    }

  

    public BigDecimal getAmountDe(){
        return this.amountDe;
    }
    public void setAmountDe(BigDecimal amountDe){
        this.amountDe = amountDe;
    }



    public BigDecimal getAmountBack(){
        return this.amountBack;
    }
    public void setAmountBack(BigDecimal amountBack){
        this.amountBack = amountBack;
    }



    public Integer getPackSize() {
		return packSize;
	}
	public void setPackSize(Integer packSize) {
		this.packSize = packSize;
	}
	public Double getQuanCg() {
		return quanCg;
	}
	public void setQuanCg(Double quanCg) {
		this.quanCg = quanCg;
	}
	public Double getOrdsCg() {
		return ordsCg;
	}
	public void setOrdsCg(Double ordsCg) {
		this.ordsCg = ordsCg;
	}
	public Double getQuanDe() {
		return quanDe;
	}
	public void setQuanDe(Double quanDe) {
		this.quanDe = quanDe;
	}
	public Double getOrdsDe() {
		return ordsDe;
	}
	public void setOrdsDe(Double ordsDe) {
		this.ordsDe = ordsDe;
	}
	public Double getQuanBack() {
		return quanBack;
	}
	public void setQuanBack(Double quanBack) {
		this.quanBack = quanBack;
	}
	public Double getOrdsBack() {
		return ordsBack;
	}
	public void setOrdsBack(Double ordsBack) {
		this.ordsBack = ordsBack;
	}
	public Double getQuanRet() {
		return quanRet;
	}
	public void setQuanRet(Double quanRet) {
		this.quanRet = quanRet;
	}
	public Double getOrdsRet() {
		return ordsRet;
	}
	public void setOrdsRet(Double ordsRet) {
		this.ordsRet = ordsRet;
	}
	public BigDecimal getAmountRet(){
        return this.amountRet;
    }
    public void setAmountRet(BigDecimal amountRet){
        this.amountRet = amountRet;
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

    public Double getQuanMinDe() {
        return quanMinDe;
    }

    public void setQuanMinDe(Double quanMinDe) {
        this.quanMinDe = quanMinDe;
    }

    public Double getQuanMinBack() {
        return quanMinBack;
    }

    public void setQuanMinBack(Double quanMinBack) {
        this.quanMinBack = quanMinBack;
    }

    public Double getQuanMinRet() {
        return quanMinRet;
    }

    public void setQuanMinRet(Double quanMinRet) {
        this.quanMinRet = quanMinRet;
    }
}