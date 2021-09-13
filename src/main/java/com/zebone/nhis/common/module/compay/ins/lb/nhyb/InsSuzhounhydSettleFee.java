package com.zebone.nhis.common.module.compay.ins.lb.nhyb;

import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: INS_SUZHOUNHYD_SETTLE_FEE 
 *
 * @since 2018-10-19 05:33:05
 */
@Table(value="INS_SUZHOUNHYD_SETTLE_FEE")
public class InsSuzhounhydSettleFee extends BaseModule  {

    /** ID - 主键 */
	@PK
	@Field(value="ID",id=KeyId.UUID)
    private String id;

    /** DECFEECODE - DECFEECODE-分类编码 */
	@Field(value="DECFEECODE")
    private String decfeecode;

    /** DECFEENAME - DECFEENAME-分类名称 */
	@Field(value="DECFEENAME")
    private String decfeename;

    /** DECTOTALCOSTS - DECTOTALCOSTS-总医疗费用 */
	@Field(value="DECTOTALCOSTS")
    private String dectotalcosts;

    /** DECENABLEMONEY - DECENABLEMONEY-总保内费用 */
	@Field(value="DECENABLEMONEY")
    private String decenablemoney;

    /** DECBASICTOTALMONEY - DECBASICTOTALMONEY-国家基本药品费用 */
	@Field(value="DECBASICTOTALMONEY")
    private String decbasictotalmoney;

    /** DECAUDITMONEY - DECAUDITMONEY-审核可报金额 */
	@Field(value="DECAUDITMONEY")
    private String decauditmoney;

    /** DECCHECKMONEY - DECCHECKMONEY-核算可报金额 */
	@Field(value="DECCHECKMONEY")
    private String deccheckmoney;

    /** MODIFY_TIME - 最后操作时间 */
	@Field(value="MODIFY_TIME")
    private Date modifyTime;

	@Field(value="PK_INS_SETTLE")
    private String pkInsSettle;

	@Field(value="PK_SETTLE")
    private String pkSettle;

	@Field(value="PK_PV")
    private String pkPv;


    public String getId(){
        return this.id;
    }
    public void setId(String id){
        this.id = id;
    }

    public String getDecfeecode(){
        return this.decfeecode;
    }
    public void setDecfeecode(String decfeecode){
        this.decfeecode = decfeecode;
    }

    public String getDecfeename(){
        return this.decfeename;
    }
    public void setDecfeename(String decfeename){
        this.decfeename = decfeename;
    }

    public String getDectotalcosts(){
        return this.dectotalcosts;
    }
    public void setDectotalcosts(String dectotalcosts){
        this.dectotalcosts = dectotalcosts;
    }

    public String getDecenablemoney(){
        return this.decenablemoney;
    }
    public void setDecenablemoney(String decenablemoney){
        this.decenablemoney = decenablemoney;
    }

    public String getDecbasictotalmoney(){
        return this.decbasictotalmoney;
    }
    public void setDecbasictotalmoney(String decbasictotalmoney){
        this.decbasictotalmoney = decbasictotalmoney;
    }

    public String getDecauditmoney(){
        return this.decauditmoney;
    }
    public void setDecauditmoney(String decauditmoney){
        this.decauditmoney = decauditmoney;
    }

    public String getDeccheckmoney(){
        return this.deccheckmoney;
    }
    public void setDeccheckmoney(String deccheckmoney){
        this.deccheckmoney = deccheckmoney;
    }

    public Date getModifyTime(){
        return this.modifyTime;
    }
    public void setModifyTime(Date modifyTime){
        this.modifyTime = modifyTime;
    }

    public String getPkInsSettle(){
        return this.pkInsSettle;
    }
    public void setPkInsSettle(String pkInsSettle){
        this.pkInsSettle = pkInsSettle;
    }

    public String getPkSettle(){
        return this.pkSettle;
    }
    public void setPkSettle(String pkSettle){
        this.pkSettle = pkSettle;
    }

    public String getPkPv(){
        return this.pkPv;
    }
    public void setPkPv(String pkPv){
        this.pkPv = pkPv;
    }
}