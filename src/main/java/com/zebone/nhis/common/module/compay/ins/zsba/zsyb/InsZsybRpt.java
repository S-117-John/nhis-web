package com.zebone.nhis.common.module.compay.ins.zsba.zsyb;

import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.FieldType;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: ins_zsyb_rpt - 外部医保-医保月结报表：(交接业务号查询[4007]、月结算费用汇总统计[2201]、取消月结算费用汇总统计[2204] 
 *
 * @since 2017-09-06 10:42:10
 */
@Table(value="INS_ZSYB_RPT")
public class InsZsybRpt extends BaseModule  {

	@PK
	@Field(value="PK_ACCTRPT",id=KeyId.UUID)
    private String pkAcctrpt;

    /** TJLB - 报表类别=1 返回门诊费用月报表 =2返回医疗住院明细表＝4 返回离休报表  6 返回特定病种报表,，9 返回工伤门诊报表 ，10 返回工伤住院报表， 11 工伤康复门诊报表,12 返回工伤康复住院报 */
	@Field(value="TJLB")
    private String tjlb;

	@Field(value="TJJZRQ")
    private String tjjzrq;

	@Field(value="FHZ")
    private String fhz;

	@Field(value="MSG")
    private String msg;

	@Field(value="JJYWH")
    private String jjywh;

	@Field(value="MODITY_TIME",date=FieldType.UPDATE)
    private Date modityTime;


    public String getPkAcctrpt(){
        return this.pkAcctrpt;
    }
    public void setPkAcctrpt(String pkAcctrpt){
        this.pkAcctrpt = pkAcctrpt;
    }

    public String getTjlb(){
        return this.tjlb;
    }
    public void setTjlb(String tjlb){
        this.tjlb = tjlb;
    }

    public String getTjjzrq(){
        return this.tjjzrq;
    }
    public void setTjjzrq(String tjjzrq){
        this.tjjzrq = tjjzrq;
    }

    public String getFhz(){
        return this.fhz;
    }
    public void setFhz(String fhz){
        this.fhz = fhz;
    }

    public String getMsg(){
        return this.msg;
    }
    public void setMsg(String msg){
        this.msg = msg;
    }

    public String getJjywh(){
        return this.jjywh;
    }
    public void setJjywh(String jjywh){
        this.jjywh = jjywh;
    }

    public Date getModityTime(){
        return this.modityTime;
    }
    public void setModityTime(Date modityTime){
        this.modityTime = modityTime;
    }
}