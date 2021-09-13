package com.zebone.nhis.common.module.pro.lb;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import java.util.Date;

@Table(value="PH_DISEASE_FOODBORNE_DT")
public class PhDiseaseFoodborneDt extends BaseModule {
    @PK
    @Field(value="PK_FOODBORNEDT")
    private String pkFoodbornedt;

    @Field(value="SORTNO")
    private Integer sortno;

    @Field(value="PK_FOODBORNE")
    private String pkFoodborne;

    @Field(value="NAME_FOOD")
    private String nameFood;

    @Field(value="DT_FOODTYPE")
    private String dtFoodtype;

    @Field(value="DT_FOODPROCESSTYPE")
    private String dtFoodprocesstype;

    @Field(value="BRAND")
    private String brand;

    @Field(value="FACTORY")
    private String factory;

    @Field(value="PLACE_EAT")
    private String placeEat;

    @Field(value="DT_EATPLACE")
    private String dtEatplace;

    @Field(value="PLACE_BUY")
    private String placeBuy;

    @Field(value="DATE_EAT")
    private Date dateEat;

    @Field(value="NOP_EAT")
    private Integer nopEat;

    @Field(value="FLAG_MPONSET")
    private String flagMponset;

    public String getPkFoodbornedt() {
        return pkFoodbornedt;
    }

    public void setPkFoodbornedt(String pkFoodbornedt) {
        this.pkFoodbornedt = pkFoodbornedt == null ? null : pkFoodbornedt.trim();
    }

    public Integer getSortno() {
        return sortno;
    }

    public void setSortno(Integer sortno) {
        this.sortno = sortno;
    }

    public String getPkFoodborne() {
        return pkFoodborne;
    }

    public void setPkFoodborne(String pkFoodborne) {
        this.pkFoodborne = pkFoodborne == null ? null : pkFoodborne.trim();
    }

    public String getNameFood() {
        return nameFood;
    }

    public void setNameFood(String nameFood) {
        this.nameFood = nameFood == null ? null : nameFood.trim();
    }

    public String getDtFoodtype() {
        return dtFoodtype;
    }

    public void setDtFoodtype(String dtFoodtype) {
        this.dtFoodtype = dtFoodtype == null ? null : dtFoodtype.trim();
    }

    public String getDtFoodprocesstype() {
        return dtFoodprocesstype;
    }

    public void setDtFoodprocesstype(String dtFoodprocesstype) {
        this.dtFoodprocesstype = dtFoodprocesstype == null ? null : dtFoodprocesstype.trim();
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand == null ? null : brand.trim();
    }

    public String getFactory() {
        return factory;
    }

    public void setFactory(String factory) {
        this.factory = factory == null ? null : factory.trim();
    }

    public String getPlaceEat() {
        return placeEat;
    }

    public void setPlaceEat(String placeEat) {
        this.placeEat = placeEat == null ? null : placeEat.trim();
    }

    public String getDtEatplace() {
        return dtEatplace;
    }

    public void setDtEatplace(String dtEatplace) {
        this.dtEatplace = dtEatplace == null ? null : dtEatplace.trim();
    }

    public String getPlaceBuy() {
        return placeBuy;
    }

    public void setPlaceBuy(String placeBuy) {
        this.placeBuy = placeBuy == null ? null : placeBuy.trim();
    }

    public Date getDateEat() {
        return dateEat;
    }

    public void setDateEat(Date dateEat) {
        this.dateEat = dateEat;
    }

    public Integer getNopEat() {
        return nopEat;
    }

    public void setNopEat(Integer nopEat) {
        this.nopEat = nopEat;
    }

    public String getFlagMponset() {
        return flagMponset;
    }

    public void setFlagMponset(String flagMponset) {
        this.flagMponset = flagMponset == null ? null : flagMponset.trim();
    }
}