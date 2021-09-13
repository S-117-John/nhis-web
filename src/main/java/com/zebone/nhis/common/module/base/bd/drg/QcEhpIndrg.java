package com.zebone.nhis.common.module.base.bd.drg;


import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: QC_EHP_INDRG - DRG入组结果 
 *
 * @since 2020-10-28 02:05:38
 */
@Table(value="QC_EHP_INDRG")
public class QcEhpIndrg extends BaseModule  {

    /** PK_INDRG - 主键 */
	@PK
	@Field(value="PK_INDRG",id=KeyId.UUID)
    private String pkIndrg;

    /** ORG_CODE - 机构代码 */
	@Field(value="ORG_CODE")
    private String orgCode;

    /** YBLSH - 医保流水号 */
	@Field(value="YBLSH")
    private String yblsh;

    /** ZYCS - 住院次数 */
	@Field(value="ZYCS")
    private Integer zycs;

    /** BAH - 病案号 */
	@Field(value="BAH")
    private String bah;

    /** GENERATE_DATE - 上传时间 */
	@Field(value="GENERATE_DATE")
    private Date generateDate;

    /** IN_DRG - 入组标识 */
	@Field(value="IN_DRG")
    private String inDrg;

    /** REMARKS1 - 预留信息1 */
	@Field(value="REMARKS1")
    private String remarks1;

    /** REMARKS2 - 预留信息2 */
	@Field(value="REMARKS2")
    private String remarks2;

    /** REMARKS3 - 预留信息3 */
	@Field(value="REMARKS3")
    private String remarks3;


    public String getPkIndrg(){
        return this.pkIndrg;
    }
    public void setPkIndrg(String pkIndrg){
        this.pkIndrg = pkIndrg;
    }

    public String getOrgCode(){
        return this.orgCode;
    }
    public void setOrgCode(String orgCode){
        this.orgCode = orgCode;
    }

    public String getYblsh(){
        return this.yblsh;
    }
    public void setYblsh(String yblsh){
        this.yblsh = yblsh;
    }

    public Integer getZycs(){
        return this.zycs;
    }
    public void setZycs(Integer zycs){
        this.zycs = zycs;
    }

    public String getBah(){
        return this.bah;
    }
    public void setBah(String bah){
        this.bah = bah;
    }

    public Date getGenerateDate(){
        return this.generateDate;
    }
    public void setGenerateDate(Date generateDate){
        this.generateDate = generateDate;
    }

    public String getInDrg(){
        return this.inDrg;
    }
    public void setInDrg(String inDrg){
        this.inDrg = inDrg;
    }

    public String getRemarks1(){
        return this.remarks1;
    }
    public void setRemarks1(String remarks1){
        this.remarks1 = remarks1;
    }

    public String getRemarks2(){
        return this.remarks2;
    }
    public void setRemarks2(String remarks2){
        this.remarks2 = remarks2;
    }

    public String getRemarks3(){
        return this.remarks3;
    }
    public void setRemarks3(String remarks3){
        this.remarks3 = remarks3;
    }
}