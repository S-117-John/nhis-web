package com.zebone.nhis.common.module.nd.record;

import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: ND_RECORD_DT 
 *
 * @since 2017-06-08 11:29:17
 */
@Table(value="ND_RECORD_DT")
public class NdRecordDt extends BaseModule  {
	@PK
	@Field(value="PK_RECORDDT",id=KeyId.UUID)
    private String pkRecorddt;

	@Field(value="PK_RECORD")
    private String pkRecord;

	@Field(value="PK_RECORDROW")
    private String pkRecordrow;

	@Field(value="COLNO")
    private Integer colno;

	@Field(value="COLNAME")
    private String colname;

	@Field(value="VAL")
    private String val;

	@Field(value="VAL2")
    private String val2;
	
	@Field(value="DT_NDCOLTYPE")
	private String dtNdcoltype;

	@Field(value="MODITY_TIME")
    private Date modityTime;
	
	@Field(value="val_orig")
	private String valOrig;
	
	@Field(value="ref_no")
	private String refNo;
	
	@Field(value="def1")
	private String def1;
	
	@Field(value="def2")
	private String def2;
	
	@Field(value="def3")
	private String def3;
	
	@Field(value="def4")
	private String def4;
	
	@Field(value="def3")
	private String def5;
	
    public String getValOrig() {
		return valOrig;
	}
	public void setValOrig(String valOrig) {
		this.valOrig = valOrig;
	}
	public String getRefNo() {
		return refNo;
	}
	public void setRefNo(String refNo) {
		this.refNo = refNo;
	}
	public String getDef1() {
		return def1;
	}
	public void setDef1(String def1) {
		this.def1 = def1;
	}
	public String getDef2() {
		return def2;
	}
	public void setDef2(String def2) {
		this.def2 = def2;
	}
	public String getDef3() {
		return def3;
	}
	public void setDef3(String def3) {
		this.def3 = def3;
	}
	public String getDef4() {
		return def4;
	}
	public void setDef4(String def4) {
		this.def4 = def4;
	}
	public String getDef5() {
		return def5;
	}
	public void setDef5(String def5) {
		this.def5 = def5;
	}
	public String getDtNdcoltype() {
		return dtNdcoltype;
	}
	public void setDtNdcoltype(String dtNdcoltype) {
		this.dtNdcoltype = dtNdcoltype;
	}
	public String getPkRecorddt(){
        return this.pkRecorddt;
    }
    public void setPkRecorddt(String pkRecorddt){
        this.pkRecorddt = pkRecorddt;
    }

    public String getPkRecord(){
        return this.pkRecord;
    }
    public void setPkRecord(String pkRecord){
        this.pkRecord = pkRecord;
    }

    public String getPkRecordrow(){
        return this.pkRecordrow;
    }
    public void setPkRecordrow(String pkRecordrow){
        this.pkRecordrow = pkRecordrow;
    }

    public Integer getColno(){
        return this.colno;
    }
    public void setColno(Integer colno){
        this.colno = colno;
    }

    public String getColname(){
        return this.colname;
    }
    public void setColname(String colname){
        this.colname = colname;
    }

    public String getVal(){
        return this.val;
    }
    public void setVal(String val){
        this.val = val;
    }

    public String getVal2(){
        return this.val2;
    }
    public void setVal2(String val2){
        this.val2 = val2;
    }

    public Date getModityTime(){
        return this.modityTime;
    }
    public void setModityTime(Date modityTime){
        this.modityTime = modityTime;
    }
}