package com.zebone.nhis.common.module.compay.ins.syx.xnh;
import java.util.Date;

import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.Field.FieldType;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: INS_GZYB_ST_XNH_DT - tabledesc 
 *
 * @since 2018-08-21 10:40:03
 */
@Table(value="INS_GZYB_ST_XNH_DT")
public class InsGzybStXnhDt   {

    /** PK_INSSTXNHDT - 主键 */
	@PK
	@Field(value="PK_INSSTXNHDT",id=KeyId.UUID)
    private String pkInsstxnhdt;

    /** PK_ORG - 所属机构 */
	@Field(value="pk_org",userfield="pkOrg",userfieldscop=FieldType.INSERT)
    private String pkOrg;

    /** PK_INSSTXNH - 新农合结算主键 */
	@Field(value="PK_INSSTXNH")
    private String pkInsstxnh;

    /** CODE_IPPRES - 住院处方流水号 */
	@Field(value="CODE_IPPRES")
    private String codeIppres;

    /** CODE_HIS - HIS 系统项目代码 */
	@Field(value="CODE_HIS")
    private String codeHis;

    /** NAME_HIS - HIS 系统项目名称 */
	@Field(value="NAME_HIS")
    private String nameHis;

    /** AMOUNT - 费用金额 */
	@Field(value="AMOUNT")
    private Double amount;

    /** AMT_BX - 报销金额 */
	@Field(value="AMT_BX")
    private Double amtBx;

    /** AMT_KJ - 扣减金额 */
	@Field(value="AMT_KJ")
    private Double amtKj;

    /** REASON_KJ - 扣减原因 */
	@Field(value="REASON_KJ")
    private String reasonKj;

    /** CREATOR - 创建人 */
	@Field(userfield="pkEmp",userfieldscop=FieldType.INSERT)
    private String creator;

    /** CREATE_TIME - 创建时间 */
	@Field(value="CREATE_TIME",date=FieldType.INSERT)
    private Date createTime;

    /** DEL_FLAG - 删除标志 */
	@Field(value="DEL_FLAG")
    private String delFlag;

    /** TS - 时间戳 */
	@Field(date=FieldType.ALL)
    private Date ts;


    public String getPkInsstxnhdt(){
        return this.pkInsstxnhdt;
    }
    public void setPkInsstxnhdt(String pkInsstxnhdt){
        this.pkInsstxnhdt = pkInsstxnhdt;
    }

    public String getPkOrg(){
        return this.pkOrg;
    }
    public void setPkOrg(String pkOrg){
        this.pkOrg = pkOrg;
    }

    public String getPkInsstxnh(){
        return this.pkInsstxnh;
    }
    public void setPkInsstxnh(String pkInsstxnh){
        this.pkInsstxnh = pkInsstxnh;
    }

    public String getCodeIppres(){
        return this.codeIppres;
    }
    public void setCodeIppres(String codeIppres){
        this.codeIppres = codeIppres;
    }

    public String getCodeHis(){
        return this.codeHis;
    }
    public void setCodeHis(String codeHis){
        this.codeHis = codeHis;
    }

    public String getNameHis(){
        return this.nameHis;
    }
    public void setNameHis(String nameHis){
        this.nameHis = nameHis;
    }

    public Double getAmount(){
        return this.amount;
    }
    public void setAmount(Double amount){
        this.amount = amount;
    }

    public Double getAmtBx(){
        return this.amtBx;
    }
    public void setAmtBx(Double amtBx){
        this.amtBx = amtBx;
    }

    public Double getAmtKj(){
        return this.amtKj;
    }
    public void setAmtKj(Double amtKj){
        this.amtKj = amtKj;
    }

    public String getReasonKj(){
        return this.reasonKj;
    }
    public void setReasonKj(String reasonKj){
        this.reasonKj = reasonKj;
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
}