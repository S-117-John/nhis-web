package com.zebone.nhis.common.module.compay.ins.syx;

import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;

/**
 * Table: INS_DEPT_MAP  - 医保科室对照 
 *
 * @since 2018-10-12 10:27:24
 */
@Table(value="INS_DEPT_MAP")
public class InsDeptMap extends BaseModule{
	 /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** PK_INSDEPTMAP - 主键 */
		@PK
		@Field(value="PK_INSDEPTMAP",id=KeyId.UUID)
	    private String pkInsdeptmap;

	    /** DT_OUT_INS - 外部医保 */
		@Field(value="DT_OUT_INS")
	    private String dtOutIns;

	    /** PK_DEPT - HIS科室主键 */
		@Field(value="PK_DEPT")
	    private String pkDept;

	    /** CODE_INSDEPT - 医保科室编码 */
		@Field(value="CODE_INSDEPT")
	    private String codeInsdept;

	    /** NAME_INSDEPT - 医保科室名称 */
		@Field(value="NAME_INSDEPT")
	    private String nameInsdept;

	    /** NOTE - 备注 */
		@Field(value="NOTE")
	    private String note;

	    /** MODIFIER - 修改人 */
		@Field(value="MODIFIER")
	    private String modifier;

	    /** MODITY_TIME - 修改时间 */
		@Field(value="MODITY_TIME")
	    private Date modityTime;
		
		/** CREATOR-创建人 */
		@Field(value = "CREATOR")
		private String creator;

		/** CREATE_TIME-创建时间 */
		@Field(value = "CREATE_TIME")
		private Date createTime;

		/** DEL_FLAG-删除标志 */
		@Field(value = "DEL_FLAG")
		private String delFlag;

		/** TS-时间戳 */
		@Field(value = "TS")
		private Date ts;

		public String getPkInsdeptmap() {
			return pkInsdeptmap;
		}

		public void setPkInsdeptmap(String pkInsdeptmap) {
			this.pkInsdeptmap = pkInsdeptmap;
		}

		public String getDtOutIns() {
			return dtOutIns;
		}

		public void setDtOutIns(String dtOutIns) {
			this.dtOutIns = dtOutIns;
		}

		public String getPkDept() {
			return pkDept;
		}

		public void setPkDept(String pkDept) {
			this.pkDept = pkDept;
		}

		public String getCodeInsdept() {
			return codeInsdept;
		}

		public void setCodeInsdept(String codeInsdept) {
			this.codeInsdept = codeInsdept;
		}

		public String getNameInsdept() {
			return nameInsdept;
		}

		public void setNameInsdept(String nameInsdept) {
			this.nameInsdept = nameInsdept;
		}

		public String getNote() {
			return note;
		}

		public void setNote(String note) {
			this.note = note;
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
