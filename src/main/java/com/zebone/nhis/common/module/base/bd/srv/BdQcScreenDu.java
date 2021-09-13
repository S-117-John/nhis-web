package com.zebone.nhis.common.module.base.bd.srv;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: BD_QC_SCREEN_DU 
 *
 * @since 2019-08-28 
 */
@Table(value="BD_QC_SCREEN_DU")
public class BdQcScreenDu extends BaseModule  {

	@PK
	@Field(value="PK_QCSCREENDU",id=KeyId.UUID)
    private String pkQcscreendu;

	@Field(value="PK_QCSCREEN")
    private String pkQcscreen;

	@Field(value="PK_DEPTUNIT")
    private String pkDeptunit;

	@Field(value="NOTE")
	private String note;

	public String getPkQcscreendu() {
		return pkQcscreendu;
	}

	public void setPkQcscreendu(String pkQcscreendu) {
		this.pkQcscreendu = pkQcscreendu;
	}

	public String getPkQcscreen() {
		return pkQcscreen;
	}

	public void setPkQcscreen(String pkQcscreen) {
		this.pkQcscreen = pkQcscreen;
	}

	public String getPkDeptunit() {
		return pkDeptunit;
	}

	public void setPkDeptunit(String pkDeptunit) {
		this.pkDeptunit = pkDeptunit;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}
}