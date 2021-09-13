package com.zebone.nhis.common.module.base.ou;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;

@Table("BD_EMP_ARGU")
public class BdEmpArgu extends BaseModule {
	@PK
	@Field(value="pk_empargu",id=KeyId.UUID)
	private String pkEmpargu;
	
	@Field(value="pk_org")
	private String pkOrg;
	
	@Field(value="pk_user")
	private String pkUser;
	
	@Field(value="name_user")
	private String nameUser;
	
	@Field(value="pk_emp")
	private String pkEmp;
	
	@Field(value="name_emp")
	private String nameEmp;
	
	@Field(value="code_argu")
	private String codeArgu;
	
	@Field(value="name_argu")
	private String nameArgu;
	
	@Field(value="note_argu")
	private String noteArgu;
	
	@Field(value="arguval")
	private String arguval;

	public String getPkEmp() {
		return pkEmp;
	}

	public void setPkEmp(String pkEmp) {
		this.pkEmp = pkEmp;
	}

	public String getPkEmpargu() {
		return pkEmpargu;
	}

	public void setPkEmpargu(String pkEmpargu) {
		this.pkEmpargu = pkEmpargu;
	}

	public String getPkOrg() {
		return pkOrg;
	}

	public void setPkOrg(String pkOrg) {
		this.pkOrg = pkOrg;
	}

	public String getPkUser() {
		return pkUser;
	}

	public void setPkUser(String pkUser) {
		this.pkUser = pkUser;
	}

	public String getNameUser() {
		return nameUser;
	}

	public void setNameUser(String nameUser) {
		this.nameUser = nameUser;
	}

	public String getNameEmp() {
		return nameEmp;
	}

	public void setNameEmp(String nameEmp) {
		this.nameEmp = nameEmp;
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
}
