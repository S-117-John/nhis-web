package com.zebone.nhis.compay.ins.lb.vo.nhyb;

import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;

public class InsNhybItemMap extends BaseModule  {

    private String pkInsitemmap;

    private String pkHp;

    private String pkItem;

    private String code;

    private String name;

    private Date modityTime;

    private String fylb;
    //费用类别
    private String xmlb;
    //报销比例
    private String enableratio;
    //报销金额
    private String enablemoney;


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
	public String getEnableratio() {
		return enableratio;
	}
	public void setEnableratio(String enableratio) {
		this.enableratio = enableratio;
	}
	public String getEnablemoney() {
		return enablemoney;
	}
	public void setEnablemoney(String enablemoney) {
		this.enablemoney = enablemoney;
	}
}
