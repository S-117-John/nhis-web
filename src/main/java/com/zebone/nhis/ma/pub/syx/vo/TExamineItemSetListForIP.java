package com.zebone.nhis.ma.pub.syx.vo;


import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: TEXAMINEITEMSETLISTFORIP 
 *
 * @since 2018-12-14 03:33:41
 */
@Table(value="tExamineItemSetListForIP")
public class TExamineItemSetListForIP   {

	@PK
//	@Field(value="ExamineItemSetListForIPID",id=KeyId.UUID)
    private Long examineItemSetListForIPID;

	/** HIS的检验申请单流水号，tExamineRequestForIP. ExamineRequestID*/
	@Field(value="ExamineRequestID")
    private Long examineRequestID;

	/** 检验套单ID*/
	@Field(value="ItemSetID")
    private Long itemSetID;

	/** 检验套单编码*/
	@Field(value="ItemSetNo")
    private String itemSetNo;

	@Field(value="ItemSetDesc")
    private String itemSetDesc;

	@Field(value="ItemSetPrice")
    private Double itemSetPrice;

	@Field(value="ExamineExemplarID")
    private Long examineExemplarID;

	@Field(value="ExamineExemplarNo")
    private String examineExemplarNo;

	@Field(value="ExamineExemplarDesc")
    private String examineExemplarDesc;
	
	private String dtSamptype;
	
	private String dtTubetype;
	
	private String tField1;
	
	private String tField2;
	
	private String tField3;
	
	private Integer ordsn;
	
	public Integer getOrdsn() {
		return ordsn;
	}

	public void setOrdsn(Integer ordsn) {
		this.ordsn = ordsn;
	}

	public String getDtSamptype() {
		return dtSamptype;
	}

	public void setDtSamptype(String dtSamptype) {
		this.dtSamptype = dtSamptype;
	}

	public String getDtTubetype() {
		return dtTubetype;
	}

	public void setDtTubetype(String dtTubetype) {
		this.dtTubetype = dtTubetype;
	}

	public String gettField1() {
		return tField1;
	}

	public void settField1(String tField1) {
		this.tField1 = tField1;
	}

	public String gettField2() {
		return tField2;
	}

	public void settField2(String tField2) {
		this.tField2 = tField2;
	}

	public String gettField3() {
		return tField3;
	}

	public void settField3(String tField3) {
		this.tField3 = tField3;
	}

	public Long getExamineItemSetListForIPID() {
		return examineItemSetListForIPID;
	}

	public void setExamineItemSetListForIPID(Long examineItemSetListForIPID) {
		this.examineItemSetListForIPID = examineItemSetListForIPID;
	}

	public Long getExamineRequestID() {
		return examineRequestID;
	}

	public void setExamineRequestID(Long examineRequestID) {
		this.examineRequestID = examineRequestID;
	}

	public Long getItemSetID() {
		return itemSetID;
	}

	public void setItemSetID(Long itemSetID) {
		this.itemSetID = itemSetID;
	}

	public String getItemSetNo() {
		return itemSetNo;
	}

	public void setItemSetNo(String itemSetNo) {
		this.itemSetNo = itemSetNo;
	}

	public String getItemSetDesc() {
		return itemSetDesc;
	}

	public void setItemSetDesc(String itemSetDesc) {
		this.itemSetDesc = itemSetDesc;
	}

	public Double getItemSetPrice() {
		return itemSetPrice;
	}

	public void setItemSetPrice(Double itemSetPrice) {
		this.itemSetPrice = itemSetPrice;
	}

	public Long getExamineExemplarID() {
		return examineExemplarID;
	}

	public void setExamineExemplarID(Long examineExemplarID) {
		this.examineExemplarID = examineExemplarID;
	}

	public String getExamineExemplarNo() {
		return examineExemplarNo;
	}

	public void setExamineExemplarNo(String examineExemplarNo) {
		this.examineExemplarNo = examineExemplarNo;
	}

	public String getExamineExemplarDesc() {
		return examineExemplarDesc;
	}

	public void setExamineExemplarDesc(String examineExemplarDesc) {
		this.examineExemplarDesc = examineExemplarDesc;
	}
}