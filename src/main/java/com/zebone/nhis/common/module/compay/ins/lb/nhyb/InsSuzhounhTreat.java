package com.zebone.nhis.common.module.compay.ins.lb.nhyb;

import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: INS_SUZHOUNH_TREAT 
 *
 * @since 2018-04-18 04:36:54
 */
@Table(value="INS_SUZHOUNH_TREAT")
public class InsSuzhounhTreat extends BaseModule  {

    /** ID - 主键 */
	@PK
	@Field(value="ID",id=KeyId.UUID)
    private String id;

    /** ZLFSBM - ZLFSBM-治疗方式编码 */
	@Field(value="ZLFSBM")
    private String zlfsbm;

    /** ZLFSMC - ZLFSMC-治疗方式名称 */
	@Field(value="ZLFSMC")
    private String zlfsmc;

    /** DBZICDBM - DBZICDBM-单病种ICD编码 */
	@Field(value="DBZICDBM")
    private String dbzicdbm;

    /** DBZICDMC - DBZICDMC-单病种ICD名称 */
	@Field(value="DBZICDMC")
    private String dbzicdmc;

    /** YL1 - YL1-预留1 */
	@Field(value="YL1")
    private String yl1;

    /** YL2 - YL2-预留2 */
	@Field(value="YL2")
    private String yl2;

    /** YL3 - YL3-预留3 */
	@Field(value="YL3")
    private String yl3;

    /** YL4 - YL4-预留4 */
	@Field(value="YL4")
    private String yl4;

    /** YL5 - YL5-预留5 */
	@Field(value="YL5")
    private String yl5;

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

    public String getZlfsbm(){
        return this.zlfsbm;
    }
    public void setZlfsbm(String zlfsbm){
        this.zlfsbm = zlfsbm;
    }

    public String getZlfsmc(){
        return this.zlfsmc;
    }
    public void setZlfsmc(String zlfsmc){
        this.zlfsmc = zlfsmc;
    }

    public String getDbzicdbm(){
        return this.dbzicdbm;
    }
    public void setDbzicdbm(String dbzicdbm){
        this.dbzicdbm = dbzicdbm;
    }

    public String getDbzicdmc(){
        return this.dbzicdmc;
    }
    public void setDbzicdmc(String dbzicdmc){
        this.dbzicdmc = dbzicdmc;
    }

    public String getYl1(){
        return this.yl1;
    }
    public void setYl1(String yl1){
        this.yl1 = yl1;
    }

    public String getYl2(){
        return this.yl2;
    }
    public void setYl2(String yl2){
        this.yl2 = yl2;
    }

    public String getYl3(){
        return this.yl3;
    }
    public void setYl3(String yl3){
        this.yl3 = yl3;
    }

    public String getYl4(){
        return this.yl4;
    }
    public void setYl4(String yl4){
        this.yl4 = yl4;
    }

    public String getYl5(){
        return this.yl5;
    }
    public void setYl5(String yl5){
        this.yl5 = yl5;
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