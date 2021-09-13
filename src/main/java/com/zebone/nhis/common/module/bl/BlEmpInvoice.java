package com.zebone.nhis.common.module.bl;

import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.Field.FieldType;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;


/**
 * Table: BL_EMP_INVOICE  - bl_emp_invoice 
 *
 * @since 2016-09-28 01:32:45
 */
@Table(value="BL_EMP_INVOICE")
public class BlEmpInvoice extends BaseModule  {

	@PK
	@Field(value="PK_EMPINV",id=KeyId.UUID)
    private String pkEmpinv;

    /** EU_OPTYPE - 0领用 1转移 2退  (当前使用的发票号段在收费员结账后,自动转移,也就是再
插入一条记录,其中上一条记录的当前号作为新记录的起始号,且操作类型
为转移) */
	@Field(value="EU_OPTYPE")
    private String euOptype;

	@Field(value="DATE_OPERA")
    private Date dateOpera;

    /** PK_INVCATE - 对应院内票据分类 */
	@Field(value="PK_INVCATE")
    private String pkInvcate;

	@Field(value="INV_PREFIX")
    private String invPrefix;

    /** INV_COUNT - 在领用时记录的本票据包张数。 */
	@Field(value="INV_COUNT")
    private Long invCount;

    /** FLAG_USE - 表示此票据包在使用中 */
	@Field(value="FLAG_USE")
    private String flagUse;

	@Field(value="PK_EMP_OPERA")
    private String pkEmpOpera;

	@Field(value="NAME_EMP_OPERA")
    private String nameEmpOpera;

    /** BEGIN_NO - 本票据包的开始号码，对于单张管理时的单张票据号。 */
	@Field(value="BEGIN_NO")
    private Long beginNo;

    /** END_NO - 本票据包的结束号码，对于单张管理时此字段无效。 */
	@Field(value="END_NO")
    private Long endNo;

    /** CNT_USE - 当前票据的可使用张数，使用时系统需要实时更新 */
	@Field(value="CNT_USE")
    private Long cntUse;

    /** CUR_NO - 操作员下次使用的号码 */
	@Field(value="CUR_NO")
    private Long curNo;

    /** FLAG_ACTIVE - 表示此票据包可使用，在使用完成，退回，作废，退库等操作时，需要将此标志置为false */
	@Field(value="FLAG_ACTIVE")
    private String flagActive;

	@Field(value="NAME_MACHINE")
    private String nameMachine;
	
	@Field(value="MODIFIER")
	private String modifier;
	
	@Field(value="NOTE")
	private String note;
	
	
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
	/**票据分类名称*/
	private String InvcateName;
	
	public String getInvcateName() {
		return InvcateName;
	}
	public void setInvcateName(String invcateName) {
		InvcateName = invcateName;
	}
	public String getPkEmpinv(){
        return this.pkEmpinv;
    }
    public void setPkEmpinv(String pkEmpinv){
        this.pkEmpinv = pkEmpinv;
    }

    public String getEuOptype(){
        return this.euOptype;
    }
    public void setEuOptype(String euOptype){
        this.euOptype = euOptype;
    }

    public Date getDateOpera(){
        return this.dateOpera;
    }
    public void setDateOpera(Date dateOpera){
        this.dateOpera = dateOpera;
    }

    public String getPkInvcate(){
        return this.pkInvcate;
    }
    public void setPkInvcate(String pkInvcate){
        this.pkInvcate = pkInvcate;
    }

    public String getInvPrefix(){
        return this.invPrefix;
    }
    public void setInvPrefix(String invPrefix){
        this.invPrefix = invPrefix;
    }

    public Long getInvCount(){
        return this.invCount;
    }
    public void setInvCount(Long invCount){
        this.invCount = invCount;
    }

    public String getFlagUse(){
        return this.flagUse;
    }
    public void setFlagUse(String flagUse){
        this.flagUse = flagUse;
    }

    public String getPkEmpOpera(){
        return this.pkEmpOpera;
    }
    public void setPkEmpOpera(String pkEmpOpera){
        this.pkEmpOpera = pkEmpOpera;
    }

    public String getNameEmpOpera(){
        return this.nameEmpOpera;
    }
    public void setNameEmpOpera(String nameEmpOpera){
        this.nameEmpOpera = nameEmpOpera;
    }

    public Long getBeginNo(){
        return this.beginNo;
    }
    public void setBeginNo(Long beginNo){
        this.beginNo = beginNo;
    }

    public Long getEndNo(){
        return this.endNo;
    }
    public void setEndNo(Long endNo){
        this.endNo = endNo;
    }

    public Long getCntUse(){
        return this.cntUse;
    }
    public void setCntUse(Long cntUse){
        this.cntUse = cntUse;
    }

    public Long getCurNo(){
        return this.curNo;
    }
    public void setCurNo(Long curNo){
        this.curNo = curNo;
    }

    public String getFlagActive(){
        return this.flagActive;
    }
    public void setFlagActive(String flagActive){
        this.flagActive = flagActive;
    }


    public String getNameMachine(){
        return this.nameMachine;
    }
    public void setNameMachine(String nameMachine){
        this.nameMachine = nameMachine;
    }
}