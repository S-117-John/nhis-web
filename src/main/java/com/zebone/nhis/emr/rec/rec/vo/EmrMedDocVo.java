package com.zebone.nhis.emr.rec.rec.vo;


/**
 * 患者病历文档记录VO
 * @author chengjia
 *
 */
public class EmrMedDocVo{
    /**
     * 
     */
    private String pkDoc;

    private String typeCode;
    
    private String typeName;
    
    private String name;

	public String getPkDoc() {
		return pkDoc;
	}

	public void setPkDoc(String pkDoc) {
		this.pkDoc = pkDoc;
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
    

    
}