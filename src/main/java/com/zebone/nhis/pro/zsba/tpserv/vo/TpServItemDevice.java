package com.zebone.nhis.pro.zsba.tpserv.vo;

import java.math.BigDecimal;
import java.util.Date;

import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.Field.FieldType;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;


/**
 * Table: tp_serv_item_device - 第三方服务项目设备 
 *
 * @since 2017-09-07 12:29:02
 */
@Table(value="TP_SERV_ITEM_DEVICE")
public class TpServItemDevice{

    /** PK_ITEM_DEVICE - 项目设备主键 */
	@PK
	@Field(value="PK_ITEM_DEVICE",id=KeyId.UUID)
    private String pkItemDevice;

    /** FK_DEPT - 所属科室 */
	@Field(value="FK_DEPT")
    private String fkDept;

    /** FK_ITEM_TYPE - 项目类型主键 */
	@Field(value="FK_ITEM_TYPE")
    private String fkItemType;

    /** NAME - 名称 */
	@Field(value="NAME")
    private String name;

    /** CODE - 编码 */
	@Field(value="CODE")
    private String code;

    /** SPCODE - 拼音码 */
	@Field(value="SPCODE")
    private String spcode;

    /** CASH_PLEDGE - 押金，为空则不需要缴纳押金 */
	@Field(value="CASH_PLEDGE")
    private BigDecimal cashPledge;

    /** NOTE - 备注 */
	@Field(value="NOTE")
    private String note;

	/** NUM - 设备数量 */
	@Field(value="num")
    private int num;
	
	/** RENTED_ALREADY_NUM - 已出租数量 */
	@Field(value="rented_already_num")
    private int rentedAlreadyNum;

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

    /** MODITY_TIME - 修改时间 */
	@Field(value="MODITY_TIME",date=FieldType.UPDATE)
    private Date modityTime;



    public String getPkItemDevice(){
        return this.pkItemDevice;
    }
    public void setPkItemDevice(String pkItemDevice){
        this.pkItemDevice = pkItemDevice;
    }

    public String getFkDept(){
        return this.fkDept;
    }
    public void setFkDept(String fkDept){
        this.fkDept = fkDept;
    }

    public String getFkItemType(){
        return this.fkItemType;
    }
    public void setFkItemType(String fkItemType){
        this.fkItemType = fkItemType;
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

    public BigDecimal getCashPledge(){
        return this.cashPledge;
    }
    public void setCashPledge(BigDecimal cashPledge){
        this.cashPledge = cashPledge;
    }

    public String getNote(){
        return this.note;
    }
    public void setNote(String note){
        this.note = note;
    }
    public int getNum() {
		return num;
	}
	public void setNum(int num) {
		this.num = num;
	}
	public int getRentedAlreadyNum() {
		return rentedAlreadyNum;
	}
	public void setRentedAlreadyNum(int rentedAlreadyNum) {
		this.rentedAlreadyNum = rentedAlreadyNum;
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
	public Date getModityTime() {
		return modityTime;
	}
	public void setModityTime(Date modityTime) {
		this.modityTime = modityTime;
	}


}