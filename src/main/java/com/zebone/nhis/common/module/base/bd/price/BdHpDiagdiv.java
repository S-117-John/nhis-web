package com.zebone.nhis.common.module.base.bd.price;


import java.util.List;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.FieldType;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: BD_HP_DIAGDIV  - bd_hp_diagdiv 
 *
 * @since 2016-09-27 02:49:25
 */
@Table(value="BD_HP_DIAGDIV")
public class BdHpDiagdiv extends BaseModule  {

	@PK
	@Field(value="PK_TOTALDIV",id=KeyId.UUID)
    private String pkTotaldiv;

	@Field(value="PK_HP")
    private String pkHp;

    /** EU_PVTYPE - 1门诊，2急诊，3住院，4体检，5家床 */
	@Field(value="EU_PVTYPE")
    private String euPvtype;

	@Field(value="PK_DIAG")
    private String pkDiag;

	@Field(value="AMOUNT")
    private Double amount;

	@Field(value="NOTE")
    private String note;
	
	private List<BdHpDiagdivItemcate> diagitemcates;

	public List<BdHpDiagdivItemcate> getDiagitemcates() {
		return diagitemcates;
	}
	public void setDiagitemcates(List<BdHpDiagdivItemcate> diagitemcates) {
		this.diagitemcates = diagitemcates;
	}
	public String getPkTotaldiv(){
        return this.pkTotaldiv;
    }
    public void setPkTotaldiv(String pkTotaldiv){
        this.pkTotaldiv = pkTotaldiv;
    }

    public String getPkHp(){
        return this.pkHp;
    }
    public void setPkHp(String pkHp){
        this.pkHp = pkHp;
    }

    public String getEuPvtype(){
        return this.euPvtype;
    }
    public void setEuPvtype(String euPvtype){
        this.euPvtype = euPvtype;
    }

    public String getPkDiag(){
        return this.pkDiag;
    }
    public void setPkDiag(String pkDiag){
        this.pkDiag = pkDiag;
    }

    public Double getAmount(){
        return this.amount;
    }
    public void setAmount(Double amount){
        this.amount = amount;
    }

    public String getNote(){
        return this.note;
    }
    public void setNote(String note){
        this.note = note;
    }

}