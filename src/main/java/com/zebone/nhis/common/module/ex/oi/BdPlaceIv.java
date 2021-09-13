package com.zebone.nhis.common.module.ex.oi;

import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;

/**
 * Table: BD_PLACE_IV  - bd_place_iv 
 *
 * @since 2017-10-19 10:01:58
 */
@Table(value="BD_PLACE_IV")
public class BdPlaceIv extends BaseModule  {

	@PK
	@Field(value="PK_PLACEIV",id=KeyId.UUID)
    private String pkPlaceiv;

	@Field(value="PK_DEPTIV")
    private String pkDeptiv;

    /** EU_SEATTYPE - 0:椅位1:床位 */
	@Field(value="EU_SEATTYPE")
    private String euSeattype;

    /** EU_PLACEATTR - 0：普通1：临时9：虚拟 */
	@Field(value="EU_PLACEATTR")
    private String euPlaceattr;

	@Field(value="CODE")
    private String code;

	@Field(value="NAME")
    private String name;

	@Field(value="PK_ITEM")
    private String pkItem;

	@Field(value="PRICE")
    private Double price;

	@Field(value="ROOM_NO")
    private String roomNo;

	@Field(value="DT_SEX")
    private String dtSex;

	@Field(value="XPOS")
    private Integer xpos;

	@Field(value="YPOS")
    private Integer ypos;

	@Field(value="NOTE")
    private String note;

    /** EU_STATUS - 0：未占用1：已占用 */
	@Field(value="EU_STATUS")
    private String euStatus;

	@Field(value="PK_PI")
    private String pkPi;

	@Field(value="PK_PV")
    private String pkPv;
	
	@Field(value="PK_INFUOCC")
    private String pkInfuocc;

	@Field(value="MODIFIER")
    private String modifier;

	@Field(value="MODITY_TIME")
    private Date modityTime;
	
	@Field(value="FLAG_ACTIVE")
    private String flagActive;
   

	public String getPkInfuocc() {
		return pkInfuocc;
	}

	public void setPkInfuocc(String pkInfuocc) {
		this.pkInfuocc = pkInfuocc;
	}
	
	public String getPkPlaceiv(){
        return this.pkPlaceiv;
    }
    
	public void setPkPlaceiv(String pkPlaceiv){
        this.pkPlaceiv = pkPlaceiv;
    }

    public String getPkDeptiv(){
        return this.pkDeptiv;
    }
    public void setPkDeptiv(String pkDeptiv){
        this.pkDeptiv = pkDeptiv;
    }

    public String getEuSeattype(){
        return this.euSeattype;
    }
    public void setEuSeattype(String euSeattype){
        this.euSeattype = euSeattype;
    }

    public String getEuPlaceattr(){
        return this.euPlaceattr;
    }
    public void setEuPlaceattr(String euPlaceattr){
        this.euPlaceattr = euPlaceattr;
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

    public String getPkItem(){
        return this.pkItem;
    }
    public void setPkItem(String pkItem){
        this.pkItem = pkItem;
    }

    public Double getPrice(){
        return this.price;
    }
    public void setPrice(Double price){
        this.price = price;
    }

    public String getRoomNo(){
        return this.roomNo;
    }
    public void setRoomNo(String roomNo){
        this.roomNo = roomNo;
    }

    public String getDtSex(){
        return this.dtSex;
    }
    public void setDtSex(String dtSex){
        this.dtSex = dtSex;
    }

    public Integer getXpos(){
        return this.xpos;
    }
    public void setXpos(Integer xpos){
        this.xpos = xpos;
    }

    public Integer getYpos(){
        return this.ypos;
    }
    public void setYpos(Integer ypos){
        this.ypos = ypos;
    }

    public String getNote(){
        return this.note;
    }
    public void setNote(String note){
        this.note = note;
    }

    public String getEuStatus(){
        return this.euStatus;
    }
    public void setEuStatus(String euStatus){
        this.euStatus = euStatus;
    }

    public String getPkPi(){
        return this.pkPi;
    }
    public void setPkPi(String pkPi){
        this.pkPi = pkPi;
    }

    public String getPkPv(){
        return this.pkPv;
    }
    public void setPkPv(String pkPv){
        this.pkPv = pkPv;
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
    
    public String getFlagActive() {
		return flagActive;
	}

	public void setFlagActive(String flagActive) {
		this.flagActive = flagActive;
	}   
    
}
