package com.zebone.nhis.pro.zsba.tpserv.vo;

import java.util.Date;

import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.Field.FieldType;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;


/**
 * Table: tp_serv_item_unit - 第三方服务项目单位 
 *
 * @since 2017-09-07 12:29:02
 */
@Table(value="TP_SERV_ITEM_UNIT")
public class TpServItemUnit{

    /** PK_ITEM_UNIT - 项目单位主键 */
	@PK
	@Field(value="PK_ITEM_UNIT",id=KeyId.UUID)
    private String pkItemUnit;

    /** NAME - 名称 */
	@Field(value="NAME")
    private String name;

    /** CODE - 编码 */
	@Field(value="CODE")
    private String code;

    /** SPCODE - 拼音码 */
	@Field(value="SPCODE")
    private String spcode;

    /** NOTE - 备注 */
	@Field(value="NOTE")
    private String note;

    /** USE_FLAG - 使用标志 0：使用，1：禁用 */
	@Field(value="USE_FLAG")
    private String useFlag;

	/**
     * 创建人
     */
	@Field(userfield="pkEmp",userfieldscop=FieldType.INSERT)
    public String creator;
	


	/**
     * 修改人
     */
	@Field(userfield="pkEmp",userfieldscop=FieldType.ALL)
    private String modifier;

    /** MODITY_TIME - 修改时间 */
	@Field(value="MODITY_TIME",date=FieldType.UPDATE)
    private Date modityTime;

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
	@Field(value="DEL_FLAG")
	public String delFlag = "0";  // 0未删除  1：删除




    public String getPkItemUnit(){
        return this.pkItemUnit;
    }
    public void setPkItemUnit(String pkItemUnit){
        this.pkItemUnit = pkItemUnit;
    }

    public String getName(){
        return this.name;
    }
    public void setName(String name){
        this.name = name;
    }

    public String getCode(){
        return this.code;
    }
    public void setCode(String code){
        this.code = code;
    }

    public String getSpcode(){
        return this.spcode;
    }
    public void setSpcode(String spcode){
        this.spcode = spcode;
    }

    public String getNote(){
        return this.note;
    }
    public void setNote(String note){
        this.note = note;
    }

    public String getUseFlag(){
        return this.useFlag;
    }
    public void setUseFlag(String useFlag){
        this.useFlag = useFlag;
    }
	public String getCreator() {
		return creator;
	}
	public void setCreator(String creator) {
		this.creator = creator;
	}
	public String getModifier() {
		return modifier;
	}
	public void setModifier(String modifier) {
		this.modifier = modifier;
	}
	public Date getModityTime() {
		return modityTime;
	}
	public void setModityTime(Date modityTime) {
		this.modityTime = modityTime;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Date getTs() {
		return ts;
	}
	public void setTs(Date ts) {
		this.ts = ts;
	}
	public String getDelFlag() {
		return delFlag;
	}
	public void setDelFlag(String delFlag) {
		this.delFlag = delFlag;
	}



}