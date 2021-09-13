package com.zebone.nhis.base.bd.vo;

import java.util.Date;

public class BdParamListVo {
	private String pkPcargu;		//主键
	private String pkOrg;			//机构
	private String pkPc;			//pc工作站
	private String pkArgu;			//参数
	private String codeArgu;		//参数编码
	private String nameArgu;		//参数名称
	private String noteArgu;		//参数说明
	private String arguval;			//参数值
	private String flagStop;		//停用标志
	private String creator;			//创建人
	private Date createTime;		//创建时间
	private String modifier;		//修改人
	private Date modityTime;		//修改时间
	private String delFlag;			//删除标志
	private Date ts;				//时间戳、
	public String getPkPcargu() {
		return pkPcargu;
	}
	public void setPkPcargu(String pkPcargu) {
		this.pkPcargu = pkPcargu;
	}
	public String getPkOrg() {
		return pkOrg;
	}
	public void setPkOrg(String pkOrg) {
		this.pkOrg = pkOrg;
	}
	public String getPkPc() {
		return pkPc;
	}
	public void setPkPc(String pkPc) {
		this.pkPc = pkPc;
	}
	public String getPkArgu() {
		return pkArgu;
	}
	public void setPkArgu(String pkArgu) {
		this.pkArgu = pkArgu;
	}
	public String getCodeArgu() {
		return codeArgu;
	}
	public void setCodeArgu(String codeArgu) {
		this.codeArgu = codeArgu;
	}
	public String getNameArgu() {
		return nameArgu;
	}
	public void setNameArgu(String nameArgu) {
		this.nameArgu = nameArgu;
	}
	public String getNoteArgu() {
		return noteArgu;
	}
	public void setNoteArgu(String noteArgu) {
		this.noteArgu = noteArgu;
	}
	public String getArguval() {
		return arguval;
	}
	public void setArguval(String arguval) {
		this.arguval = arguval;
	}
	public String getFlagStop() {
		return flagStop;
	}
	public void setFlagStop(String flagStop) {
		this.flagStop = flagStop;
	}
	public String getCreator() {
		return creator;
	}
	public void setCreator(String creator) {
		this.creator = creator;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public String getModifier() {
		return modifier;
	}
	public void setModifier(String modifier) {
		this.modifier = modifier;
	}
	public Date getModityTime() {
		return modityTime;
	}
	public void setModityTime(Date modityTime) {
		this.modityTime = modityTime;
	}
	public String getDelFlag() {
		return delFlag;
	}
	public void setDelFlag(String delFlag) {
		this.delFlag = delFlag;
	}
	public Date getTs() {
		return ts;
	}
	public void setTs(Date ts) {
		this.ts = ts;
	}
	
	
	
}
