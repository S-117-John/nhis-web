package com.zebone.nhis.common.module.scm.pub;

import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: BD_PD_STORE - bd_pd_store 
 *
 * @since 2016-10-29 09:39:06
 */
@Table(value="BD_PD_STORE")
public class BdPdStore extends BaseModule  {

	@PK
	@Field(value="PK_PDSTORE",id=KeyId.UUID)
    private String pkPdstore;

	@Field(value="PK_DEPT")
    private String pkDept;

	@Field(value="PK_STORE")
    private String pkStore;

	@Field(value="PK_PD")
    private String pkPd;

	@Field(value="PK_PDCONVERT")
    private String pkPdconvert;

	@Field(value="FLAG_STOP")
    private String flagStop;

	@Field(value="POSI_NO")
    private String posiNo;

	@Field(value="STOCK_MAX")
    private Double stockMax;

	@Field(value="STOCK_MIN")
    private Double stockMin;

    /** COUNT_PER - 天 */
	@Field(value="COUNT_PER")
    private Double countPer;

	@Field(value="LAST_DATE")
    private Date lastDate;
	
	@Field(value="QUAN_THRE")
	private Double quanThre;
	
	@Field(value="PK_UNIT")
	private String pkUnit;			//包装单位
	
	@Field(value="PACK_SIZE")
	private Long packSize;		//包装数量

    @Field(value="NUM_LIMIT")
    private Integer numLimit; // 药品开立控制倍数

	public String getPkUnit() {
		return pkUnit;
	}
	public void setPkUnit(String pkUnit) {
		this.pkUnit = pkUnit;
	}
	public Long getPackSize() {
		return packSize;
	}
	public void setPackSize(Long packSize) {
		this.packSize = packSize;
	}
	public String getPkPdstore(){
        return this.pkPdstore;
    }
    public void setPkPdstore(String pkPdstore){
        this.pkPdstore = pkPdstore;
    }

    public String getPkDept(){
        return this.pkDept;
    }
    public void setPkDept(String pkDept){
        this.pkDept = pkDept;
    }

    public String getPkStore(){
        return this.pkStore;
    }
    public void setPkStore(String pkStore){
        this.pkStore = pkStore;
    }

    public String getPkPd(){
        return this.pkPd;
    }
    public void setPkPd(String pkPd){
        this.pkPd = pkPd;
    }

    public String getPkPdconvert(){
        return this.pkPdconvert;
    }
    public void setPkPdconvert(String pkPdconvert){
        this.pkPdconvert = pkPdconvert;
    }

    public String getFlagStop(){
        return this.flagStop;
    }
    public void setFlagStop(String flagStop){
        this.flagStop = flagStop;
    }

    public String getPosiNo(){
        return this.posiNo;
    }
    public void setPosiNo(String posiNo){
        this.posiNo = posiNo;
    }

    public Double getStockMax(){
        return this.stockMax;
    }
    public void setStockMax(Double stockMax){
        this.stockMax = stockMax;
    }

    public Double getStockMin(){
        return this.stockMin;
    }
    public void setStockMin(Double stockMin){
        this.stockMin = stockMin;
    }

    public Double getCountPer(){
        return this.countPer;
    }
    public void setCountPer(Double countPer){
        this.countPer = countPer;
    }

    public Date getLastDate(){
        return this.lastDate;
    }
    public void setLastDate(Date lastDate){
        this.lastDate = lastDate;
    }
	public Double getQuanThre() {
		return quanThre;
	}
	public void setQuanThre(Double quanThre) {
		this.quanThre = quanThre;
	}

	public Integer getNumLimit(){return this.numLimit;}
	public void setNumLimit(Integer numLimit){this.numLimit=numLimit;}
}