package com.zebone.nhis.pro.sd.scm.vo;


import java.util.Date;
import java.util.List;

import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.Field.FieldType;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: REG_SZYJ_PDDELIVERY 
 *
 * @since 2020-01-04 10:37:07
 */
@Table(value="REG_SZYJ_PDDELIVERY")
public class RegSzyjPddelivery   {

	@PK
	@Field(value="PK_PDDIV",id=KeyId.UUID)
    private String pkPddiv;

	@Field(value="pk_org",userfield="pkOrg",userfieldscop=FieldType.INSERT)
    private String pkOrg;

	@Field(value="PSDTXM")
    private String txm;

	/**
	 * 医院编码
	 */
	@Field(value="YYBM")
    private String yybm;

	/**
	 * 配送点编码
	 */
	@Field(value="PSDBM")
    private String psdbm;

	/**
	 * 供应商编码
	 */
	@Field(value="GYSBM")
    private String gysbm;

	/**
	 * 采购计划编码
	 */
	@Field(value="CGJHBH")
    private String cgjhbh;

	/**
	 * 订单编号
	 */
	@Field(value="DDBH")
    private String ddbh;

	/**
	 * 配送单编号
	 */
	@Field(value="PSDBH")
    private String psdbh;

	/**
	 * 药企配送单编号
	 */
	@Field(value="YQPSDBH")
    private String yqpsdbh;

	/**
	 * 配送商编码
	 */
	@Field(value="PSSBM")
    private String pssbm;

	/**
	 * 配送商名称
	 */
	@Field(value="PSSMC")
    private String pssmc;

	/**
	 * 配送时间
	 */
	@Field(value="PSSJ")
    private Date pssj;

	/**
	 * 记录数
	 */
	@Field(value="JLS")
    private Integer jls;

	@Field(userfield="pkEmp",userfieldscop=FieldType.INSERT)
    private String creator;

	@Field(value="CREATE_TIME",date=FieldType.INSERT)
    private Date createTime;

	@Field(date=FieldType.ALL)
    private Date ts;
	
	/**
	 * 配送状态
	 */
	@Field(value="EU_STATUS")
	private String euStatus;
	
	private List<RegSzyjPddeliveryDt> mx;


    public String getPkPddiv(){
        return this.pkPddiv;
    }
    public void setPkPddiv(String pkPddiv){
        this.pkPddiv = pkPddiv;
    }

    public String getPkOrg(){
        return this.pkOrg;
    }
    public void setPkOrg(String pkOrg){
        this.pkOrg = pkOrg;
    }

   
    public String getTxm() {
		return txm;
	}
	public void setTxm(String txm) {
		this.txm = txm;
	}
	public String getYybm(){
        return this.yybm;
    }
    public void setYybm(String yybm){
        this.yybm = yybm;
    }

    public String getPsdbm(){
        return this.psdbm;
    }
    public void setPsdbm(String psdbm){
        this.psdbm = psdbm;
    }

    public String getGysbm(){
        return this.gysbm;
    }
    public void setGysbm(String gysbm){
        this.gysbm = gysbm;
    }

    public String getCgjhbh(){
        return this.cgjhbh;
    }
    public void setCgjhbh(String cgjhbh){
        this.cgjhbh = cgjhbh;
    }

    public String getDdbh(){
        return this.ddbh;
    }
    public void setDdbh(String ddbh){
        this.ddbh = ddbh;
    }

    public String getPsdbh(){
        return this.psdbh;
    }
    public void setPsdbh(String psdbh){
        this.psdbh = psdbh;
    }

    public String getYqpsdbh(){
        return this.yqpsdbh;
    }
    public void setYqpsdbh(String yqpsdbh){
        this.yqpsdbh = yqpsdbh;
    }

    public String getPssbm(){
        return this.pssbm;
    }
    public void setPssbm(String pssbm){
        this.pssbm = pssbm;
    }

    public String getPssmc(){
        return this.pssmc;
    }
    public void setPssmc(String pssmc){
        this.pssmc = pssmc;
    }

    public Date getPssj(){
        return this.pssj;
    }
    public void setPssj(Date pssj){
        this.pssj = pssj;
    }

    public Integer getJls(){
        return this.jls;
    }
    public void setJls(Integer jls){
        this.jls = jls;
    }

    public String getCreator(){
        return this.creator;
    }
    public void setCreator(String creator){
        this.creator = creator;
    }

    public Date getCreateTime(){
        return this.createTime;
    }
    public void setCreateTime(Date createTime){
        this.createTime = createTime;
    }

    public Date getTs(){
        return this.ts;
    }
    public void setTs(Date ts){
        this.ts = ts;
    }
	public List<RegSzyjPddeliveryDt> getMx() {
		return mx;
	}
	public void setMx(List<RegSzyjPddeliveryDt> mx) {
		this.mx = mx;
	}
	public String getEuStatus() {
		return euStatus;
	}
	public void setEuStatus(String euStatus) {
		this.euStatus = euStatus;
	}
    
    
}