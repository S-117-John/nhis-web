package com.zebone.nhis.common.module.emr.scires;

import java.util.Date;

/**
 *
 * @author 
 */
public class EmrRptExp{
    /**
     * 
     */
    private String pkExp;
    /**
     * 
     */
    private String pkOrg;
    /**
     * 
     */
    private String pkRpt;
    /**
     * 
     */
    private String docType;
    
    private String docTypeName;
    /**
     * 
     */
    private String paraCode;
    
    private String paraName;
    /**
     * 
     */
    private String nodeType;
    /**
     * 
     */
    private String nodeName;
    /**
     * 
     */
    private String nodeCode;
    /**
     * 
     */
    private String expCode;
    /**
     * 
     */
    private Integer expNum;
    /**
     * 
     */
    private String remark;
    /**
     * 
     */
    private String delFlag;
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
    
    /**
     * 
     */
    public String getPkExp(){
        return this.pkExp;
    }

    /**
     * 
     */
    public void setPkExp(String pkExp){
        this.pkExp = pkExp;
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
    public String getPkRpt(){
        return this.pkRpt;
    }

    /**
     * 
     */
    public void setPkRpt(String pkRpt){
        this.pkRpt = pkRpt;
    }    
    /**
     * 
     */
    public String getDocType(){
        return this.docType;
    }

    /**
     * 
     */
    public void setDocType(String docType){
        this.docType = docType;
    }    
    /**
     * 
     */
    public String getParaCode(){
        return this.paraCode;
    }

    /**
     * 
     */
    public void setParaCode(String paraCode){
        this.paraCode = paraCode;
    }    
    /**
     * 
     */
    public String getNodeType(){
        return this.nodeType;
    }

    /**
     * 
     */
    public void setNodeType(String nodeType){
        this.nodeType = nodeType;
    }    
    /**
     * 
     */
    public String getNodeName(){
        return this.nodeName;
    }

    /**
     * 
     */
    public void setNodeName(String nodeName){
        this.nodeName = nodeName;
    }    
    /**
     * 
     */
    public String getNodeCode(){
        return this.nodeCode;
    }

    /**
     * 
     */
    public void setNodeCode(String nodeCode){
        this.nodeCode = nodeCode;
    }    
    /**
     * 
     */
    public String getExpCode(){
        return this.expCode;
    }

    /**
     * 
     */
    public void setExpCode(String expCode){
        this.expCode = expCode;
    }    
    /**
     * 
     */
    public Integer getExpNum(){
        return this.expNum;
    }

    /**
     * 
     */
    public void setExpNum(Integer expNum){
        this.expNum = expNum;
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

	public String getDocTypeName() {
		return docTypeName;
	}

	public void setDocTypeName(String docTypeName) {
		this.docTypeName = docTypeName;
	}

	public String getParaName() {
		return paraName;
	}

	public void setParaName(String paraName) {
		this.paraName = paraName;
	}   
    
}