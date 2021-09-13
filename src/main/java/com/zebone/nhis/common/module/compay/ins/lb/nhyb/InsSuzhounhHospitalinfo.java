package com.zebone.nhis.common.module.compay.ins.lb.nhyb;

import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: INS_SUZHOUNH_HOSPITALINFO 
 *
 * @since 2018-04-19 10:22:00
 */
@Table(value="INS_SUZHOUNH_HOSPITALINFO")
public class InsSuzhounhHospitalinfo extends BaseModule  {

    /** ID - 主键 */
	@PK
	@Field(value="ID",id=KeyId.UUID)
    private String id;

    /** ZZJGDM - ZZJGDM-组织机构代码 */
	@Field(value="ZZJGDM")
    private String zzjgdm;

    /** YLJGMC - YLJGMC-医疗机构名称 */
	@Field(value="YLJGMC")
    private String yljgmc;

    /** YYJSDJBM - YYJSDJBM-医院技术等级编码 */
	@Field(value="YYJSDJBM")
    private String yyjsdjbm;

    /** YYXZJBBM - YYXZJBBM-医院行政级别编码 */
	@Field(value="YYXZJBBM")
    private String yyxzjbbm;

    /** YLZDY - YLZDY-预留字段一 */
	@Field(value="YLZDY")
    private String ylzdy;

    /** YLZDE - YLZDE-预留字段二 */
	@Field(value="YLZDE")
    private String ylzde;

    /** YLZDS - YLZDS-预留字段三 */
	@Field(value="YLZDS")
    private String ylzds;

    /** YLZDSI - YLZDSI-预留字段四 */
	@Field(value="YLZDSI")
    private String ylzdsi;

    /** YLZDW - YLZDW-预留字段五 */
	@Field(value="YLZDW")
    private String ylzdw;

    /** MODIFIER - 最后操作人 */
	@Field(value="MODIFIER")
    private String modifier;

    /** MODIFY_TIME - 最后操作时间 */
	@Field(value="MODIFY_TIME")
    private Date modifyTime;


    public String getId(){
        return this.id;
    }
    public void setId(String id){
        this.id = id;
    }

    public String getZzjgdm(){
        return this.zzjgdm;
    }
    public void setZzjgdm(String zzjgdm){
        this.zzjgdm = zzjgdm;
    }

    public String getYljgmc(){
        return this.yljgmc;
    }
    public void setYljgmc(String yljgmc){
        this.yljgmc = yljgmc;
    }

    public String getYyjsdjbm(){
        return this.yyjsdjbm;
    }
    public void setYyjsdjbm(String yyjsdjbm){
        this.yyjsdjbm = yyjsdjbm;
    }

    public String getYyxzjbbm(){
        return this.yyxzjbbm;
    }
    public void setYyxzjbbm(String yyxzjbbm){
        this.yyxzjbbm = yyxzjbbm;
    }

    public String getYlzdy(){
        return this.ylzdy;
    }
    public void setYlzdy(String ylzdy){
        this.ylzdy = ylzdy;
    }

    public String getYlzde(){
        return this.ylzde;
    }
    public void setYlzde(String ylzde){
        this.ylzde = ylzde;
    }

    public String getYlzds(){
        return this.ylzds;
    }
    public void setYlzds(String ylzds){
        this.ylzds = ylzds;
    }

    public String getYlzdsi(){
        return this.ylzdsi;
    }
    public void setYlzdsi(String ylzdsi){
        this.ylzdsi = ylzdsi;
    }

    public String getYlzdw(){
        return this.ylzdw;
    }
    public void setYlzdw(String ylzdw){
        this.ylzdw = ylzdw;
    }

    public String getModifier(){
        return this.modifier;
    }
    public void setModifier(String modifier){
        this.modifier = modifier;
    }

    public Date getModifyTime(){
        return this.modifyTime;
    }
    public void setModifyTime(Date modifyTime){
        this.modifyTime = modifyTime;
    }
}