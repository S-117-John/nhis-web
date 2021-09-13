package com.zebone.nhis.common.module.base.bd.ques;

import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: BD_TPCATE - BD_TPCATE 
 *
 * @since 2016-11-09 01:55:49
 */
@Table(value="BD_TPCATE")
public class BdTpcate extends BaseModule  {

	@Field(value="PK_TPCATE")
    private String pkTpcate;

	@Field(value="CODE_TPCATE")
    private String codeTpcate;

	@Field(value="NAME_TPCATE")
    private String nameTpcate;

	@Field(value="FONT_FAMILY")
    private String fontFamily;

	@Field(value="FONT_SIZE")
    private Integer fontSize;

	@Field(value="FONT_COLOR")
    private Integer fontColor;

	@Field(value="FONT_STYLE")
    private Integer fontStyle;

	@Field(value="MODITY_TIME")
    private Date modityTime;


    public String getPkTpcate(){
        return this.pkTpcate;
    }
    public void setPkTpcate(String pkTpcate){
        this.pkTpcate = pkTpcate;
    }

    public String getCodeTpcate(){
        return this.codeTpcate;
    }
    public void setCodeTpcate(String codeTpcate){
        this.codeTpcate = codeTpcate;
    }

    public String getNameTpcate(){
        return this.nameTpcate;
    }
    public void setNameTpcate(String nameTpcate){
        this.nameTpcate = nameTpcate;
    }

    public String getFontFamily(){
        return this.fontFamily;
    }
    public void setFontFamily(String fontFamily){
        this.fontFamily = fontFamily;
    }

    public Integer getFontSize(){
        return this.fontSize;
    }
    public void setFontSize(Integer fontSize){
        this.fontSize = fontSize;
    }

    public Integer getFontColor(){
        return this.fontColor;
    }
    public void setFontColor(Integer fontColor){
        this.fontColor = fontColor;
    }

    public Integer getFontStyle(){
        return this.fontStyle;
    }
    public void setFontStyle(Integer fontStyle){
        this.fontStyle = fontStyle;
    }

    public Date getModityTime(){
        return this.modityTime;
    }
    public void setModityTime(Date modityTime){
        this.modityTime = modityTime;
    }
}