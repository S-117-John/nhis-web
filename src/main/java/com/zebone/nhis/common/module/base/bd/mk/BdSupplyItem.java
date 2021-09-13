package com.zebone.nhis.common.module.base.bd.mk;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;


/**
 * 医嘱用法附加费用
 * Table: BD_SUPPLY_ITEM  -bd_supply_item
 *
 * @since 2016-08-31 08:47:39
 */
@Table(value="BD_SUPPLY_ITEM")
public class BdSupplyItem extends BaseModule{

	private static final long serialVersionUID = 1L;

	/** PK_SUPPLYITEM - 用法费用主键 */
	@PK
	@Field(value="PK_SUPPLYITEM",id=KeyId.UUID)
    private String pkSupplyitem;

    /** PK_SUPPLY - 用法主键 */
	@Field(value="PK_SUPPLY")
    private String pkSupply;

    /** PK_ITEM - 绑定项目 */
	@Field(value="PK_ITEM")
    private String pkItem;

    /** EU_PVTYPE - 就诊类别 ：1门诊，2急诊，3住院，4体检，5家床 */
	@Field(value="EU_PVTYPE")
    private String euPvtype;

    /** QUAN - 绑定数量 */
	@Field(value="QUAN")
    private Double quan;

    /** EU_MODE - 加收模式 */
    @Field(value="EU_MODE")
    private String euMode;

    private String price;//价格

	private String unit;//单位

	private String spec;//规格

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getSpec() {
        return spec;
    }

    public void setSpec(String spec) {
        this.spec = spec;
    }

    public String getPkSupplyitem(){
        return this.pkSupplyitem;
    }
    public void setPkSupplyitem(String pkSupplyitem){
        this.pkSupplyitem = pkSupplyitem;
    }

    public String getPkSupply(){
        return this.pkSupply;
    }
    public void setPkSupply(String pkSupply){
        this.pkSupply = pkSupply;
    }

    public String getPkItem(){
        return this.pkItem;
    }
    public void setPkItem(String pkItem){
        this.pkItem = pkItem;
    }

    public String getEuPvtype(){
        return this.euPvtype;
    }
    public void setEuPvtype(String euPvtype){
        this.euPvtype = euPvtype;
    }

    public Double getQuan(){
        return this.quan;
    }
    public void setQuan(Double quan){
        this.quan = quan;
    }

    public String getEuMode() {
        return euMode;
    }

    public void setEuMode(String euMode) {
        this.euMode = euMode;
    }
}