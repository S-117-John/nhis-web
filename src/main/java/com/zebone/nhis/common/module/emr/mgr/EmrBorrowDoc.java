package com.zebone.nhis.common.module.emr.mgr;

import java.util.Date;

/**
 *
 * @author 
 */
public class EmrBorrowDoc{
    /**
     * 
     */
    private String pkBorrowDoc;
    /**
     * 
     */
    private String pkOrg;
    /**
     * 
     */
    private String pkBorrow;
    /**
     * 
     */
    private String pkRec;
    /**
     * 
     */
    private String delFlag;
    /**
     * 
     */
    private String remark;
    /**
     * 
     */
    private String creator;
    /**
     * 
     */
    private Date createTime;
    /**
     * 
     */
    private Date ts;

    private String status;
    
    private String name;
    
    private String typeCode;
    
    private String typeName;
    
    /**
     * 
     */
    public String getPkBorrowDoc(){
        return this.pkBorrowDoc;
    }

    /**
     * 
     */
    public void setPkBorrowDoc(String pkBorrowDoc){
        this.pkBorrowDoc = pkBorrowDoc;
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
    public String getPkBorrow(){
        return this.pkBorrow;
    }

    /**
     * 
     */
    public void setPkBorrow(String pkBorrow){
        this.pkBorrow = pkBorrow;
    }    
    /**
     * 
     */
    public String getPkRec(){
        return this.pkRec;
    }

    /**
     * 
     */
    public void setPkRec(String pkRec){
        this.pkRec = pkRec;
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTypeCode() {
		return typeCode;
	}

	public void setTypeCode(String typeCode) {
		this.typeCode = typeCode;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}    
    
    
}