package com.zebone.nhis.compay.ins.lb.vo.szyb;

import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.nhis.common.module.bl.BlIpDt;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: INS_SZYB_ITEM_MAP 
 *
 * @since 2018-06-02 10:00:09
 */
@Table(value="INS_SZYB_ITEM_MAP")
public class InsSzybItemMap extends BaseModule implements Cloneable  {

    public Object clone() {
        InsSzybItemMap o = null;
        try {
            o = (InsSzybItemMap) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return o;
    }

    @Field(value="PK_INSITEMMAP")
    private String pkInsitemmap;

	@Field(value="PK_HP")
    private String pkHp;

	@Field(value="PK_ITEM")
    private String pkItem;

	@Field(value="CODE")
    private String code;

	@Field(value="NAME")
    private String name;

	@Field(value="MODITY_TIME")
    private Date modityTime;

	@Field(value="FYLB")
    private String fylb;

	@Field(value="XMLB")
    private String xmlb;


    public String getPkInsitemmap(){
        return this.pkInsitemmap;
    }
    public void setPkInsitemmap(String pkInsitemmap){
        this.pkInsitemmap = pkInsitemmap;
    }

    public String getPkHp(){
        return this.pkHp;
    }
    public void setPkHp(String pkHp){
        this.pkHp = pkHp;
    }

    public String getPkItem(){
        return this.pkItem;
    }
    public void setPkItem(String pkItem){
        this.pkItem = pkItem;
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

    public Date getModityTime(){
        return this.modityTime;
    }
    public void setModityTime(Date modityTime){
        this.modityTime = modityTime;
    }

    public String getFylb(){
        return this.fylb;
    }
    public void setFylb(String fylb){
        this.fylb = fylb;
    }

    public String getXmlb(){
        return this.xmlb;
    }
    public void setXmlb(String xmlb){
        this.xmlb = xmlb;
    }
}
