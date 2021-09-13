package com.zebone.nhis.pro.zsba.compay.ins.qgyb.vo.Business;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;

/**
 * Table: INS_CLEARING_QG
 *
 * @since 2020-10-26 10:42:10
 */
@Table(value="INS_CLEARBRANCH_QG")
public class InsZsbaClearbranchQg extends BaseModule{

	@PK
	@Field(value="PK_INSCLEARBRANCHQG",id=KeyId.UUID)
    private String pkInsclearbranchqg;
	
	@Field(value="TRT_YEAR")
    private String trtYear;
	
	@Field(value="TRT_MONTH")
    private String trtMonth;

	public String getPkInsclearbranchqg() {
		return pkInsclearbranchqg;
	}

	public void setPkInsclearbranchqg(String pkInsclearbranchqg) {
		this.pkInsclearbranchqg = pkInsclearbranchqg;
	}

	public String getTrtYear() {
		return trtYear;
	}

	public void setTrtYear(String trtYear) {
		this.trtYear = trtYear;
	}

	public String getTrtMonth() {
		return trtMonth;
	}

	public void setTrtMonth(String trtMonth) {
		this.trtMonth = trtMonth;
	}
	
	

	
}
