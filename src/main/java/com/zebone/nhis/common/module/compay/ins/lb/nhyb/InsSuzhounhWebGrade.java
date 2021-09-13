package com.zebone.nhis.common.module.compay.ins.lb.nhyb;

import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: INS_SUZHOUNH_WEB_GRADE 
 *
 * @since 2019-04-26 08:36:32
 */
@Table(value="INS_SUZHOUNH_WEB_GRADE")
public class InsSuzhounhWebGrade extends BaseModule  {

    /** ID - 主键 */
	@PK
	@Field(value="ID",id=KeyId.UUID)
    private String id;
	
    /** STARTMONEY - STARTMONEY-分段起始值 */
	@Field(value="PK_JS")
    private String pkJs;

    /** STARTMONEY - STARTMONEY-分段起始值 */
	@Field(value="STARTMONEY")
    private String startmoney;

    /** ENDMONEY - ENDMONEY-分段截止值 */
	@Field(value="ENDMONEY")
    private String endmoney;

    /** RATIO - RATIO-本段补偿比例 */
	@Field(value="RATIO")
    private String ratio;

    /** REDEEMMONEY - REDEEMMONEY-本段核算实际补偿金额 */
	@Field(value="REDEEMMONEY")
    private String redeemmoney;

    /** MODIFY_TIME - 最后操作时间 */
	@Field(value="MODIFY_TIME")
    private Date modifyTime;


    public String getId(){
        return this.id;
    }
    public void setId(String id){
        this.id = id;
    }

    public String getStartmoney(){
        return this.startmoney;
    }
    public void setStartmoney(String startmoney){
        this.startmoney = startmoney;
    }

    public String getEndmoney(){
        return this.endmoney;
    }
    public void setEndmoney(String endmoney){
        this.endmoney = endmoney;
    }

    public String getRatio(){
        return this.ratio;
    }
    public void setRatio(String ratio){
        this.ratio = ratio;
    }

    public String getRedeemmoney(){
        return this.redeemmoney;
    }
    public void setRedeemmoney(String redeemmoney){
        this.redeemmoney = redeemmoney;
    }

    public Date getModifyTime(){
        return this.modifyTime;
    }
    public void setModifyTime(Date modifyTime){
        this.modifyTime = modifyTime;
    }
	public String getPkJs() {
		return pkJs;
	}
	public void setPkJs(String pkJs) {
		this.pkJs = pkJs;
	}
}