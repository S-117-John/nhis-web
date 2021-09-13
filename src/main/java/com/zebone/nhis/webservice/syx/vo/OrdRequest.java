package com.zebone.nhis.webservice.syx.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Request")
@XmlAccessorType(XmlAccessType.FIELD)
public class OrdRequest {
		
		@XmlElement(name="euPvType")
		private String euPvType;
		@XmlElement(name="func_id")
	    private String funcId;
		@XmlElement(name="code_pi")
	    private String codePi;
		@XmlElement(name="code_pv")
	    private String codePv;
		@XmlElement(name="code_ord")
	    private String codeOrd;
		@XmlElement(name="date_start")
	    private String dateStart;
		@XmlElement(name="name_ord")
	    private String nameOrd;
		@XmlElement(name="desc_ord")
	    private String descOrd;
		@XmlElement(name="quan")
	    private String quan;
		@XmlElement(name="code_freq")
	    private String codeFreq;
		@XmlElement(name="code_dept")
	    private String codeDept;
		@XmlElement(name="code_dr")
	    private String codeDr;
		@XmlElement(name="code_dept_ns")
	    private String codeDeptNs;
		@XmlElement(name="code_dept_ex")
	    private String codeDeptEx;
		@XmlElement(name="flag_note")
	    private String flagNote;
		@XmlElement(name="flag_bl")
	    private String flagBl;
		@XmlElement(name="code_ordtype")
	    private String codeOrdtype;
		//医嘱备注
		@XmlElement(name = "unit")
		private String unit;
		@XmlElement(name = "dt_bt_abo")
		private String dtBtAbo;
		@XmlElement(name = "dt_bttype")
		private String dtBttype;
		@XmlElement(name = "dt_bt_rh")
		private String dtBtRh;
		@XmlElement(name = "code_apply")
		private String codeApply;
		@XmlElement(name = "note_ord")
		private String noteOrd;
		@XmlElement(name = "bt_content")
		private String btContent;
		
		public String getFuncId() {
			return funcId;
		}
		public void setFuncId(String funcId) {
			this.funcId = funcId;
		}
		public String getCodePi() {
			return codePi;
		}
		public void setCodePi(String codePi) {
			this.codePi = codePi;
		}
		public String getCodePv() {
			return codePv;
		}
		public void setCodePv(String codePv) {
			this.codePv = codePv;
		}
		public String getCodeOrd() {
			return codeOrd;
		}
		public void setCodeOrd(String codeOrd) {
			this.codeOrd = codeOrd;
		}
		public String getDateStart() {
			return dateStart;
		}
		public void setDateStart(String dateStart) {
			this.dateStart = dateStart;
		}
		public String getNameOrd() {
			return nameOrd;
		}
		public void setNameOrd(String nameOrd) {
			this.nameOrd = nameOrd;
		}
		public String getDescOrd() {
			return descOrd;
		}
		public void setDescOrd(String descOrd) {
			this.descOrd = descOrd;
		}
		public String getQuan() {
			return quan;
		}
		public void setQuan(String quan) {
			this.quan = quan;
		}
		public String getCodeFreq() {
			return codeFreq;
		}
		public void setCodeFreq(String codeFreq) {
			this.codeFreq = codeFreq;
		}
		public String getCodeDept() {
			return codeDept;
		}
		public void setCodeDept(String codeDept) {
			this.codeDept = codeDept;
		}
		public String getCodeDr() {
			return codeDr;
		}
		public void setCodeDr(String codeDr) {
			this.codeDr = codeDr;
		}
		public String getCodeDeptNs() {
			return codeDeptNs;
		}
		public void setCodeDeptNs(String codeDeptNs) {
			this.codeDeptNs = codeDeptNs;
		}
		public String getCodeDeptEx() {
			return codeDeptEx;
		}
		public void setCodeDeptEx(String codeDeptEx) {
			this.codeDeptEx = codeDeptEx;
		}
		public String getFlagNote() {
			return flagNote;
		}
		public void setFlagNote(String flagNote) {
			this.flagNote = flagNote;
		}
		public String getFlagBl() {
			return flagBl;
		}
		public void setFlagBl(String flagBl) {
			this.flagBl = flagBl;
		}
		public String getCodeOrdtype() {
			return codeOrdtype;
		}
		public void setCodeOrdtype(String codeOrdtype) {
			this.codeOrdtype = codeOrdtype;
		}
		public String getEuPvType() {
			return euPvType;
		}
		public void setEuPvType(String euPvType) {
			this.euPvType = euPvType;
		}
		public String getNoteOrd() {
			return noteOrd;
		}
		public void setNoteOrd(String noteOrd) {
			this.noteOrd = noteOrd;
		}
		public String getUnit() {
			return unit;
		}
		public void setUnit(String unit) {
			this.unit = unit;
		}
		public String getDtBtAbo() {
			return dtBtAbo;
		}
		public void setDtBtAbo(String dtBtAbo) {
			this.dtBtAbo = dtBtAbo;
		}
		public String getDtBttype() {
			return dtBttype;
		}
		public void setDtBttype(String dtBttype) {
			this.dtBttype = dtBttype;
		}
		public String getDtBtRh() {
			return dtBtRh;
		}
		public void setDtBtRh(String dtBtRh) {
			this.dtBtRh = dtBtRh;
		}
		public String getCodeApply() {
			return codeApply;
		}
		public void setCodeApply(String codeApply) {
			this.codeApply = codeApply;
		}
		public String getBtContent() {
			return btContent;
		}
		public void setBtContent(String btContent) {
			this.btContent = btContent;
		}
	    

}
