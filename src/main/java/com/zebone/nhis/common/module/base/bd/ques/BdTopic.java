package com.zebone.nhis.common.module.base.bd.ques;

import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: BD_TOPIC - bd_topic 
 *
 * @since 2016-11-09 01:46:40
 */
@Table(value="BD_TOPIC")
public class BdTopic extends BaseModule  {

	@Field(value="PK_TPBANK")
    private String pkTpbank;

	@Field(value="PK_SCHM")
    private String pkSchm;

	@Field(value="CODE_TP")
    private String codeTp;

	@Field(value="DESC_TP")
    private String descTp;

    /** EU_TPTYPE - 0 单选，1 多选，2 数字，3 文本 */
	@Field(value="EU_TPTYPE")
    private String euTptype;

    /** EU_DIRECT - 0 横排，1竖排 */
	@Field(value="EU_DIRECT")
    private String euDirect;

	@Field(value="VAL_MAX")
    private Integer valMax;

	@Field(value="VAL_MIN")
    private Integer valMin;

	@Field(value="PRECISION")
    private Integer precision;

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


    public String getPkTpbank(){
        return this.pkTpbank;
    }
    public void setPkTpbank(String pkTpbank){
        this.pkTpbank = pkTpbank;
    }

    public String getPkSchm(){
        return this.pkSchm;
    }
    public void setPkSchm(String pkSchm){
        this.pkSchm = pkSchm;
    }

    public String getCodeTp(){
        return this.codeTp;
    }
    public void setCodeTp(String codeTp){
        this.codeTp = codeTp;
    }

    public String getDescTp(){
        return this.descTp;
    }
    public void setDescTp(String descTp){
        this.descTp = descTp;
    }

    public String getEuTptype(){
        return this.euTptype;
    }
    public void setEuTptype(String euTptype){
        this.euTptype = euTptype;
    }

    public String getEuDirect(){
        return this.euDirect;
    }
    public void setEuDirect(String euDirect){
        this.euDirect = euDirect;
    }

    public Integer getValMax(){
        return this.valMax;
    }
    public void setValMax(Integer valMax){
        this.valMax = valMax;
    }

    public Integer getValMin(){
        return this.valMin;
    }
    public void setValMin(Integer valMin){
        this.valMin = valMin;
    }

    public Integer getPrecision(){
        return this.precision;
    }
    public void setPrecision(Integer precision){
        this.precision = precision;
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