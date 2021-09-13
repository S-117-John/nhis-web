package com.zebone.nhis.common.module.bl;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;

import java.util.Date;


/**
 * Table: cn_order_bar
 * 临床-医嘱-条码
 * @since
 */
@Table(value="CN_ORDER_BAR")
public class CnOrderBar extends BaseModule  {

	@PK
	@Field(value="PK_CNORD_BAR",id=KeyId.UUID)
    private String pkCnordBar;

	@Field(value="PK_CNORD")
    private String pkCnord;

	@Field(value="BARCODE")
    private String barcode;

	@Field(value = "PK_EMP_ADD")
	private String pkEmpAdd;

    @Field(value="EU_ADDITEM")
	private String euAdditem;

    @Field(value="PK_ITEM")
	private String pkItem;

    @Field(value="NAME")
	private String name;

    @Field(value="QUAN")
	private Double quan;

    public String getPkCnordBar() {
        return pkCnordBar;
    }

    public void setPkCnordBar(String pkCnordBar) {
        this.pkCnordBar = pkCnordBar;
    }

    public String getPkCnord() {
        return pkCnord;
    }

    public void setPkCnord(String pkCnord) {
        this.pkCnord = pkCnord;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getEuAdditem() {
        return euAdditem;
    }

    public void setEuAdditem(String euAdditem) {
        this.euAdditem = euAdditem;
    }

    public String getPkItem() {
        return pkItem;
    }

    public void setPkItem(String pkItem) {
        this.pkItem = pkItem;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getQuan() {
        return quan;
    }

    public void setQuan(Double quan) {
        this.quan = quan;
    }

    public String getPkEmpAdd() {
        return pkEmpAdd;
    }

    public void setPkEmpAdd(String pkEmpAdd) {
        this.pkEmpAdd = pkEmpAdd;
    }
}
