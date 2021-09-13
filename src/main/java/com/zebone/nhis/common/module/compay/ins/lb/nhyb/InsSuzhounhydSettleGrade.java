package com.zebone.nhis.common.module.compay.ins.lb.nhyb;

import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: INS_SUZHOUNHYD_SETTLE_GRADE 
 *
 * @since 2018-10-19 05:33:13
 */
@Table(value="INS_SUZHOUNHYD_SETTLE_GRADE")
public class InsSuzhounhydSettleGrade extends BaseModule  {

    /** ID - 主键 */
	@PK
	@Field(value="ID",id=KeyId.UUID)
    private String id;

    /** DECINPATIENTSN - DECINPATIENTSN-住院登记流水号 */
	@Field(value="DECINPATIENTSN")
    private String decinpatientsn;

    /** DECSTARTMONEY - DECSTARTMONEY-分段起始金额 */
	@Field(value="DECSTARTMONEY")
    private String decstartmoney;

    /** DECENDMONEY - DECENDMONEY-分段结束金额 */
	@Field(value="DECENDMONEY")
    private String decendmoney;

    /** DECRATIO - DECRATIO-本段补偿比例 */
	@Field(value="DECRATIO")
    private String decratio;

    /** DECREDEEMMONEY - DECREDEEMMONEY-本段补偿金额 */
	@Field(value="DECREDEEMMONEY")
    private String decredeemmoney;

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

    public String getDecinpatientsn(){
        return this.decinpatientsn;
    }
    public void setDecinpatientsn(String decinpatientsn){
        this.decinpatientsn = decinpatientsn;
    }

    public String getDecstartmoney(){
        return this.decstartmoney;
    }
    public void setDecstartmoney(String decstartmoney){
        this.decstartmoney = decstartmoney;
    }

    public String getDecendmoney(){
        return this.decendmoney;
    }
    public void setDecendmoney(String decendmoney){
        this.decendmoney = decendmoney;
    }

    public String getDecratio(){
        return this.decratio;
    }
    public void setDecratio(String decratio){
        this.decratio = decratio;
    }

    public String getDecredeemmoney(){
        return this.decredeemmoney;
    }
    public void setDecredeemmoney(String decredeemmoney){
        this.decredeemmoney = decredeemmoney;
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