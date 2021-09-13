package com.zebone.nhis.common.module.cn.ipdw;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;

@Table(value="CN_OP_DIAG")
public class CnOpDiag extends BaseModule{

	@PK
	@Field(value="PK_OPDIAG",id=KeyId.UUID)
    private String pkOpdiag;

	@Field(value="PK_ORG")
    private String pkOrg;
	
	@Field(value="PK_ORDOP")
	private String pkOrdop;
	
	@Field(value="PK_DIAG")
	private String pkDiag;
	
	@Field(value="CODE_DIAG")
	private String codeDiag;
	
	@Field(value="DESC_DIAG")
	private String descDiag;
	
	@Field(value="SORTNO")
	private Integer sortno;

	public String getPkOpdiag() {
		return pkOpdiag;
	}

	public void setPkOpdiag(String pkOpdiag) {
		this.pkOpdiag = pkOpdiag;
	}

	public String getPkOrg() {
		return pkOrg;
	}

	public void setPkOrg(String pkOrg) {
		this.pkOrg = pkOrg;
	}

	public String getPkOrdop() {
		return pkOrdop;
	}

	public void setPkOrdop(String pkOrdop) {
		this.pkOrdop = pkOrdop;
	}

	public String getPkDiag() {
		return pkDiag;
	}

	public void setPkDiag(String pkDiag) {
		this.pkDiag = pkDiag;
	}

	public String getCodeDiag() {
		return codeDiag;
	}

	public void setCodeDiag(String codeDiag) {
		this.codeDiag = codeDiag;
	}

	public String getDescDiag() {
		return descDiag;
	}

	public void setDescDiag(String descDiag) {
		this.descDiag = descDiag;
	}

	public Integer getSortno() {
		return sortno;
	}

	public void setSortno(Integer sortno) {
		this.sortno = sortno;
	}
	
	public String rowStatus;

	public String getRowStatus() {
		return rowStatus;
	}
	public void setRowStatus(String rowStatus) {
		this.rowStatus = rowStatus;
	}
}
