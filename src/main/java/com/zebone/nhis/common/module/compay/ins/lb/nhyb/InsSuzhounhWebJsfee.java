package com.zebone.nhis.common.module.compay.ins.lb.nhyb;

import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: INS_SUZHOUNH_WEB_JSFEE 
 *
 * @since 2019-04-26 10:47:52
 */
@Table(value="INS_SUZHOUNH_WEB_JSFEE")
public class InsSuzhounhWebJsfee extends BaseModule  {

    /** ID - 主键 */
	@PK
	@Field(value="ID",id=KeyId.UUID)
    private String id;

    /** FEECODE - FEECODE-分类编码 */
	@Field(value="FEECODE")
    private String feecode;

    /** FEENAME - FEENAME-分类名称 */
	@Field(value="FEENAME")
    private String feename;

    /** TOTALCOSTS - TOTALCOSTS-总医疗费用 */
	@Field(value="TOTALCOSTS")
    private String totalcosts;

    /** ENABLEMONEY - ENABLEMONEY-总保内费用 */
	@Field(value="ENABLEMONEY")
    private String enablemoney;

    /** BASICTOTALMONEY - BASICTOTALMONEY-国家基本药品费用 */
	@Field(value="BASICTOTALMONEY")
    private String basictotalmoney;

    /** AUDITMONEY - AUDITMONEY-审核可报金额 */
	@Field(value="AUDITMONEY")
    private String auditmoney;

    /** CHECKMONEY - CHECKMONEY-核算可报金额 */
	@Field(value="CHECKMONEY")
    private String checkmoney;
	
	@Field(value="PK_JS")
	private String pkJs;
	
	@Field(value="PK_SETTLE")
	private String pkSettle;

    /** MODIFY_TIME - 最后操作时间 */
	@Field(value="MODIFY_TIME")
    private Date modifyTime;


    public String getId(){
        return this.id;
    }
    public void setId(String id){
        this.id = id;
    }

    public String getFeecode(){
        return this.feecode;
    }
    public void setFeecode(String feecode){
        this.feecode = feecode;
    }

    public String getFeename(){
        return this.feename;
    }
    public void setFeename(String feename){
        this.feename = feename;
    }

    public String getTotalcosts(){
        return this.totalcosts;
    }
    public void setTotalcosts(String totalcosts){
        this.totalcosts = totalcosts;
    }

    public String getEnablemoney(){
        return this.enablemoney;
    }
    public void setEnablemoney(String enablemoney){
        this.enablemoney = enablemoney;
    }

    public String getBasictotalmoney(){
        return this.basictotalmoney;
    }
    public void setBasictotalmoney(String basictotalmoney){
        this.basictotalmoney = basictotalmoney;
    }

    public String getAuditmoney(){
        return this.auditmoney;
    }
    public void setAuditmoney(String auditmoney){
        this.auditmoney = auditmoney;
    }

    public String getCheckmoney(){
        return this.checkmoney;
    }
    public void setCheckmoney(String checkmoney){
        this.checkmoney = checkmoney;
    }

    public Date getModifyTime(){
        return this.modifyTime;
    }
    public void setModifyTime(Date modifyTime){
        this.modifyTime = modifyTime;
    }
	public String getPkSettle() {
		return pkSettle;
	}
	public void setPkSettle(String pkSettle) {
		this.pkSettle = pkSettle;
	}
	public String getPkJs() {
		return pkJs;
	}
	public void setPkJs(String pkJs) {
		this.pkJs = pkJs;
	}
}