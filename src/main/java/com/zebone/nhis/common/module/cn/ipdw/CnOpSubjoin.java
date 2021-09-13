package com.zebone.nhis.common.module.cn.ipdw;

import java.util.Date;

import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.Field.FieldType;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;


/**
 * Table: CN_OP_SUBJOIN 
 *
 * @since 2016-09-12 10:30:28
 */
@Table(value="CN_OP_SUBJOIN")
public class CnOpSubjoin   {

	@PK
	@Field(value="PK_CNOPJOIN",id=KeyId.UUID)
    private String pkCnopjoin;

	@Field(value="PK_ORDOP")
    private String pkOrdop;

	@Field(value="SORT_NO")
    private Integer sortNo;

	@Field(value="PK_DIAG_SUB")
    private String pkDiagSub;

	@Field(value="NOTE")
    private String note;

	@Field(userfield="pkEmp",userfieldscop=FieldType.INSERT)
    private String creator;

	@Field(value="CREATE_TIME",date=FieldType.INSERT)
    private Date createTime;

	@Field(userfield="pkEmp",userfieldscop=FieldType.ALL)
    private String modifier;

	@Field(value="MODITY_TIME")
    private Date modityTime;

	@Field(value="DEL_FLAG")
    private String delFlag;

	@Field(date=FieldType.ALL)
    private Date ts;
	
	@Field(value="CNT_OP")
	private Integer cntOp;
	
	@Field(value="DT_INCITYPE")
	private String dtIncitype;

	private String opName;
	private String opCode;
	/**
	 * 数据更新状态
	 */
	private String RowStatus;

	
    public Integer getCntOp() {
		return cntOp;
	}
	public void setCntOp(Integer cntOp) {
		this.cntOp = cntOp;
	}
	public String getDtIncitype() {
		return dtIncitype;
	}
	public void setDtIncitype(String dtIncitype) {
		this.dtIncitype = dtIncitype;
	}
	public String getPkCnopjoin(){
        return this.pkCnopjoin;
    }
    public void setPkCnopjoin(String pkCnopjoin){
        this.pkCnopjoin = pkCnopjoin;
    }

    public String getPkOrdop(){
        return this.pkOrdop;
    }
    public void setPkOrdop(String pkOrdop){
        this.pkOrdop = pkOrdop;
    }

    public Integer getSortNo(){
        return this.sortNo;
    }
    public void setSortNo(Integer sortNo){
        this.sortNo = sortNo;
    }

    public String getPkDiagSub(){
        return this.pkDiagSub;
    }
    public void setPkDiagSub(String pkDiagSub){
        this.pkDiagSub = pkDiagSub;
    }

    public String getNote(){
        return this.note;
    }
    public void setNote(String note){
        this.note = note;
    }

    public String getCreator(){
        return this.creator;
    }
    public void setCreator(String creator){
        this.creator = creator;
    }

    public Date getCreateTime(){
        return this.createTime;
    }
    public void setCreateTime(Date createTime){
        this.createTime = createTime;
    }

    public String getModifier(){
        return this.modifier;
    }
    public void setModifier(String modifier){
        this.modifier = modifier;
    }

    public Date getModityTime(){
        return this.modityTime;
    }
    public void setModityTime(Date modityTime){
        this.modityTime = modityTime;
    }

    public String getDelFlag(){
        return this.delFlag;
    }
    public void setDelFlag(String delFlag){
        this.delFlag = delFlag;
    }

    public Date getTs(){
        return this.ts;
    }
    public void setTs(Date ts){
        this.ts = ts;
    }
	public String getRowStatus() {
		return RowStatus;
	}
	public void setRowStatus(String rowStatus) {
		RowStatus = rowStatus;
	}
	public String getOpName() {
		return opName;
	}
	public void setOpName(String opName) {
		this.opName = opName;
	}
	public String getOpCode() {
		return opCode;
	}
	public void setOpCode(String opCode) {
		this.opCode = opCode;
	}
    
}