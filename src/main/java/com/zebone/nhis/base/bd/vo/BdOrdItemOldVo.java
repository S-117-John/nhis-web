package com.zebone.nhis.base.bd.vo;

import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;

@Table(value="BD_ORD_ITEM")
public class BdOrdItemOldVo {
    @PK
    @Field(value="PK_ORDITEM",id= Field.KeyId.UUID)
    private String pkOrditem;

    @Field(value="PK_ORD")
    private String pkOrd;

    @Field(value="PK_ITEM")
    private String pkItem;

    private String code;

    private String codeOld;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCodeOld() {
        return codeOld;
    }

    public void setCodeOld(String codeOld) {
        this.codeOld = codeOld;
    }

    private String name;

    private String price;

    private String pkItemOld;

    private String nameOld;

    private String priceOld;

    private String counts;

    public String getPkOrditem() {
        return pkOrditem;
    }

    public void setPkOrditem(String pkOrditem) {
        this.pkOrditem = pkOrditem;
    }

    public String getPkOrd() {
        return pkOrd;
    }

    public void setPkOrd(String pkOrd) {
        this.pkOrd = pkOrd;
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

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getPkItemOld() {
        return pkItemOld;
    }

    public void setPkItemOld(String pkItemOld) {
        this.pkItemOld = pkItemOld;
    }

    public String getNameOld() {
        return nameOld;
    }

    public void setNameOld(String nameOld) {
        this.nameOld = nameOld;
    }

    public String getPriceOld() {
        return priceOld;
    }

    public void setPriceOld(String priceOld) {
        this.priceOld = priceOld;
    }

    public String getCounts() {
        return counts;
    }

    public void setCounts(String counts) {
        this.counts = counts;
    }
}
