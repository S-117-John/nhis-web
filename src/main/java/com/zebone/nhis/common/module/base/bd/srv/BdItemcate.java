package com.zebone.nhis.common.module.base.bd.srv;

import java.util.Date;

import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.Field.FieldType;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;


/**
 * Table: BD_ITEMCATE  - bd_itemcate 
 *
 * @since 2016-09-09 01:29:29
 */
@Table(value="BD_ITEMCATE")
public class BdItemcate   {

	@PK
	@Field(value="PK_ITEMCATE",id=KeyId.UUID)
    private String pkItemcate;

	@Field(value="CODE")
    private String code;

	@Field(value="NAME")
    private String name;

	@Field(value="PK_PARENT")
    private String pkParent;

	@Field(value="FLAG_PD")
    private String flagPd;

    /** EU_PRICEMODE - 0 本服务定价;  1 服务套成员合计价;   2 服务套成员项目数量定价;   3 服务套成员项目数量加收;   4 对应物品价格;  5 体检包总价模式 */
	@Field(value="EU_PRICEMODE")
    private String euPricemode;

	@Field(value="DT_CHCATE")
    private String dtChcate;

	@Field(value="NOTE")
    private String note;

	@Field(value="SPCODE")
    private String spcode;

	@Field(value="D_CODE")
    private String dCode;

	@Field(userfield="pkEmp",userfieldscop=FieldType.ALL)
    private String modifier;

	/**
     * 创建人
     */
	@Field(userfield="pkEmp",userfieldscop=FieldType.INSERT)
    public String creator;

	/**
     * 创建时间
     */
	@Field(value="create_time",date=FieldType.INSERT)
	public Date createTime;
    

	/**
     * 时间戳
     */
	@Field(date=FieldType.ALL)
	public Date ts;
	
	/**
     *删除标志  
     */
	@Field(value="del_flag")
	public String delFlag = "0";  // 0未删除  1：删除

	public String state;
	
    public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getPkItemcate(){
        return this.pkItemcate;
    }
    public void setPkItemcate(String pkItemcate){
        this.pkItemcate = pkItemcate;
    }

    public String getCode(){
        return this.code;
    }
    public void setCode(String code){
        this.code = code;
    }

    public String getName(){
        return this.name;
    }
    public void setName(String name){
        this.name = name;
    }

    public String getPkParent(){
        return this.pkParent;
    }
    public void setPkParent(String pkParent){
        this.pkParent = pkParent;
    }

    public String getFlagPd(){
        return this.flagPd;
    }
    public void setFlagPd(String flagPd){
        this.flagPd = flagPd;
    }

    public String getEuPricemode(){
        return this.euPricemode;
    }
    public void setEuPricemode(String euPricemode){
        this.euPricemode = euPricemode;
    }

    public String getDtChcate(){
        return this.dtChcate;
    }
    public void setDtChcate(String dtChcate){
        this.dtChcate = dtChcate;
    }

    public String getNote(){
        return this.note;
    }
    public void setNote(String note){
        this.note = note;
    }

    public String getSpcode(){
        return this.spcode;
    }
    public void setSpcode(String spcode){
        this.spcode = spcode;
    }

    public String getdCode(){
        return this.dCode;
    }
    public void setdCode(String dCode){
        this.dCode = dCode;
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