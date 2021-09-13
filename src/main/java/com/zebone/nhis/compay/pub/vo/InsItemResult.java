package com.zebone.nhis.compay.pub.vo;

/**
 * 保存匹配审批结果
 */
public class InsItemResult {

	private String pkHp; //"医保计划主键",
	private String pkItem; //"项目主键",
	private String codeInsitem; //"医保项目编码",
	private String euExamine; //"审核标志",
	private String examineNote; //"审核说明"
	public String getPkHp() {
		return pkHp;
	}
	public void setPkHp(String pkHp) {
		this.pkHp = pkHp;
	}
	public String getPkItem() {
		return pkItem;
	}
	public void setPkItem(String pkItem) {
		this.pkItem = pkItem;
	}
	public String getCodeInsitem() {
		return codeInsitem;
	}
	public void setCodeInsitem(String codeInsitem) {
		this.codeInsitem = codeInsitem;
	}
	public String getEuExamine() {
		return euExamine;
	}
	public void setEuExamine(String euExamine) {
		this.euExamine = euExamine;
	}
	public String getExamineNote() {
		return examineNote;
	}
	public void setExamineNote(String examineNote) {
		this.examineNote = examineNote;
	}
	
}
