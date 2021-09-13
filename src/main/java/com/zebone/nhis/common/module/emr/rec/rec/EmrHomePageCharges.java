package com.zebone.nhis.common.module.emr.rec.rec;

import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 *
 * @author 
 */
@Table(value = "EMR_HOME_PAGE_CHARGES")
public class EmrHomePageCharges{
    /**
     * 
     */
    @PK
    @Field(value = "PK_CHARGE", id = Field.KeyId.UUID)
    private String pkCharge;
    /**
     * 
     */
    @Field(value = "PK_ORG")
    private String pkOrg;
    /**
     * 
     */
    @Field(value = "PK_PAGE")
    private String pkPage;
    /**
     * 
     */
    @Field(value = "SEQ_NO")
    private Integer seqNo;
    /**
     * 
     */
    @Field(value = "ITEM_CODE")
    private String itemCode;
    /**
     * 
     */
    @Field(value = "ITEM_NAME")
    private String itemName;
    /**
     * 
     */
    @Field(value = "ITEM_AMOUNT")
    private BigDecimal itemAmount;
    /**
     * 
     */
    @Field(value = "DEL_FLAG")
    private String delFlag;
    /**
     * 
     */
    @Field(value = "REMARK")
    private String remark;
    /**
     * 
     */
    @Field(userfield = "pkEmp", userfieldscop = Field.FieldType.INSERT)
    private String creator;
    /**
     * 
     */
    @Field(value = "create_time", date = Field.FieldType.INSERT)
    private Date createTime;
    /**
     * 
     */
    @Field(value = "TS")
    private Date ts;

    private String status;


    private String ctrlName;
    
    
    public String getCtrlName() {
		return ctrlName;
	}

	public void setCtrlName(String ctrlName) {
		this.ctrlName = ctrlName;
	}

	private List<String> itemCodes;
    
    /**
     * 
     */
    public String getPkCharge(){
        return this.pkCharge;
    }

    /**
     * 
     */
    public void setPkCharge(String pkCharge){
        this.pkCharge = pkCharge;
    }    
    /**
     * 
     */
    public String getPkOrg(){
        return this.pkOrg;
    }

    /**
     * 
     */
    public void setPkOrg(String pkOrg){
        this.pkOrg = pkOrg;
    }    
    /**
     * 
     */
    public String getPkPage(){
        return this.pkPage;
    }

    /**
     * 
     */
    public void setPkPage(String pkPage){
        this.pkPage = pkPage;
    }    
    /**
     * 
     */
    public Integer getSeqNo(){
        return this.seqNo;
    }

    /**
     * 
     */
    public void setSeqNo(Integer seqNo){
        this.seqNo = seqNo;
    }    
    /**
     * 
     */
    public String getItemCode(){
        return this.itemCode;
    }

    /**
     * 
     */
    public void setItemCode(String itemCode){
        this.itemCode = itemCode;
    }    
    /**
     * 
     */
    public String getItemName(){
        return this.itemName;
    }

    /**
     * 
     */
    public void setItemName(String itemName){
        this.itemName = itemName;
    }    
    /**
     * 
     */
    public BigDecimal getItemAmount(){
        return this.itemAmount;
    }

    /**
     * 
     */
    public void setItemAmount(BigDecimal itemAmount){
        this.itemAmount = itemAmount;
    }    
    /**
     * 
     */
    public String getDelFlag(){
        return this.delFlag;
    }

    /**
     * 
     */
    public void setDelFlag(String delFlag){
        this.delFlag = delFlag;
    }    
    /**
     * 
     */
    public String getRemark(){
        return this.remark;
    }

    /**
     * 
     */
    public void setRemark(String remark){
        this.remark = remark;
    }    
    /**
     * 
     */
    public String getCreator(){
        return this.creator;
    }

    /**
     * 
     */
    public void setCreator(String creator){
        this.creator = creator;
    }    
    /**
     * 
     */
    public Date getCreateTime(){
        return this.createTime;
    }

    /**
     * 
     */
    public void setCreateTime(Date createTime){
        this.createTime = createTime;
    }    
    /**
     * 
     */
    public Date getTs(){
        return this.ts;
    }

    /**
     * 
     */
    public void setTs(Date ts){
        this.ts = ts;
    }

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public List<String> getItemCodes() {
		return itemCodes;
	}

	public void setItemCodes(List<String> itemCodes) {
		this.itemCodes = itemCodes;
	}    
    
}