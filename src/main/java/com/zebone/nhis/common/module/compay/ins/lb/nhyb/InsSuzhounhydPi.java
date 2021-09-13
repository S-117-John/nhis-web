package com.zebone.nhis.common.module.compay.ins.lb.nhyb;

import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: INS_SUZHOUNHYD_PI 
 *
 * @since 2018-10-19 04:27:31
 */
@Table(value="INS_SUZHOUNHYD_PI")
public class InsSuzhounhydPi extends BaseModule  {

    /** ID - 主键 */
	@PK
	@Field(value="ID",id=KeyId.UUID)
    private String id;

    /** DECMEMBERNO - DECMEMBERNO-成员编码 */
	@Field(value="DECMEMBERNO")
    private String decmemberno;

    /** DECNAME - DECNAME-姓名 */
	@Field(value="DECNAME")
    private String decname;

    /** DECCOUNTRYTEAMCODE - DECCOUNTRYTEAMCODE-辖区编码 */
	@Field(value="DECCOUNTRYTEAMCODE")
    private String deccountryteamcode;

    /** DECFAMILYSYSNO - DECFAMILYSYSNO-家庭编码 */
	@Field(value="DECFAMILYSYSNO")
    private String decfamilysysno;

    /** DECSEXID - DECSEXID-性别代码 */
	@Field(value="DECSEXID")
    private String decsexid;

    /** DECIDCARDNO - DECIDCARDNO-身份证 */
	@Field(value="DECIDCARDNO")
    private String decidcardno;

    /** DECAGE - DECAGE-年龄 */
	@Field(value="DECAGE")
    private String decage;

    /** DECBIRTHDAY - DECBIRTHDAY-出生日期 */
	@Field(value="DECBIRTHDAY")
    private String decbirthday;

    /** DECBOOKNO - DECBOOKNO-医疗证号 */
	@Field(value="DECBOOKNO")
    private String decbookno;

    /** DECCARDNO - DECCARDNO-医疗卡号 */
	@Field(value="DECCARDNO")
    private String deccardno;

    /** DECFAMILYADDRESS - DECFAMILYADDRESS-家庭住址 */
	@Field(value="DECFAMILYADDRESS")
    private String decfamilyaddress;

    /** DECTEL - DECTEL-电话号码 */
	@Field(value="DECTEL")
    private String dectel;

    /** DECIDENAME - DECIDENAME-个人身份属性名称 */
	@Field(value="DECIDENAME")
    private String decidename;

    /** DECTRANSCODE - DECTRANSCODE-转诊单号 */
	@Field(value="DECTRANSCODE")
    private String dectranscode;

    /** DECOBLIGATEONE - DECOBLIGATEONE-预留字段一 */
	@Field(value="DECOBLIGATEONE")
    private String decobligateone;

    /** DECOBLIGATETWO - DECOBLIGATETWO-预留字段二 */
	@Field(value="DECOBLIGATETWO")
    private String decobligatetwo;

    /** DECOBLIGATETHREE - DECOBLIGATETHREE-预留字段三 */
	@Field(value="DECOBLIGATETHREE")
    private String decobligatethree;

    /** DECOBLIGATEFOUR - DECOBLIGATEFOUR-预留字段四 */
	@Field(value="DECOBLIGATEFOUR")
    private String decobligatefour;

    /** DECOBLIGATEFIVE - DECOBLIGATEFIVE-预留字段五 */
	@Field(value="DECOBLIGATEFIVE")
    private String decobligatefive;

	/**CREATOR - 创建人*/
	@Field(value="CREATOR")
	private String creator;
	
	/**CREATE_TIME - 创建时间*/
	@Field(value="CREATE_TIME")
	private Date createTime;
	
	/**MODIFIER - 最后操作人*/
	@Field(value="MODIFIER")
	private String modifier;
	
    /** MODIFY_TIME - 最后操作时间 */
	@Field(value="MODIFY_TIME")
    private Date modifyTime;
	
	/** DEL_FLAG - 删除标志 */
	@Field(value="DEL_FLAG")
    private String delFlag;
	
	/** PK_ORG - 机构 */
	@Field(value="PK_ORG")
    private String pkOrg;
	
	/** PK_PI - HIS患者主键 */
	@Field(value="PK_PI")
    private String pkPi;

	public String getCreator() {
		return creator;
	}
	public void setCreator(String creator) {
		this.creator = creator;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public String getModifier() {
		return modifier;
	}
	public void setModifier(String modifier) {
		this.modifier = modifier;
	}
	public String getDelFlag() {
		return delFlag;
	}
	public void setDelFlag(String delFlag) {
		this.delFlag = delFlag;
	}
	public String getPkOrg() {
		return pkOrg;
	}
	public void setPkOrg(String pkOrg) {
		this.pkOrg = pkOrg;
	}
	public String getPkPi() {
		return pkPi;
	}
	public void setPkPi(String pkPi) {
		this.pkPi = pkPi;
	}
	@Field(value="YEAR")
    private String year;

	@Field(value="HOSPCODE")
    private String hospcode;


    public String getId(){
        return this.id;
    }
    public void setId(String id){
        this.id = id;
    }

    public String getDecmemberno(){
        return this.decmemberno;
    }
    public void setDecmemberno(String decmemberno){
        this.decmemberno = decmemberno;
    }

    public String getDecname(){
        return this.decname;
    }
    public void setDecname(String decname){
        this.decname = decname;
    }

    public String getDeccountryteamcode(){
        return this.deccountryteamcode;
    }
    public void setDeccountryteamcode(String deccountryteamcode){
        this.deccountryteamcode = deccountryteamcode;
    }

    public String getDecfamilysysno(){
        return this.decfamilysysno;
    }
    public void setDecfamilysysno(String decfamilysysno){
        this.decfamilysysno = decfamilysysno;
    }

    public String getDecsexid(){
        return this.decsexid;
    }
    public void setDecsexid(String decsexid){
        this.decsexid = decsexid;
    }

    public String getDecidcardno(){
        return this.decidcardno;
    }
    public void setDecidcardno(String decidcardno){
        this.decidcardno = decidcardno;
    }

    public String getDecage(){
        return this.decage;
    }
    public void setDecage(String decage){
        this.decage = decage;
    }

    public String getDecbirthday(){
        return this.decbirthday;
    }
    public void setDecbirthday(String decbirthday){
        this.decbirthday = decbirthday;
    }

    public String getDecbookno(){
        return this.decbookno;
    }
    public void setDecbookno(String decbookno){
        this.decbookno = decbookno;
    }

    public String getDeccardno(){
        return this.deccardno;
    }
    public void setDeccardno(String deccardno){
        this.deccardno = deccardno;
    }

    public String getDecfamilyaddress(){
        return this.decfamilyaddress;
    }
    public void setDecfamilyaddress(String decfamilyaddress){
        this.decfamilyaddress = decfamilyaddress;
    }

    public String getDectel(){
        return this.dectel;
    }
    public void setDectel(String dectel){
        this.dectel = dectel;
    }

    public String getDecidename(){
        return this.decidename;
    }
    public void setDecidename(String decidename){
        this.decidename = decidename;
    }

    public String getDectranscode(){
        return this.dectranscode;
    }
    public void setDectranscode(String dectranscode){
        this.dectranscode = dectranscode;
    }

    public String getDecobligateone(){
        return this.decobligateone;
    }
    public void setDecobligateone(String decobligateone){
        this.decobligateone = decobligateone;
    }

    public String getDecobligatetwo(){
        return this.decobligatetwo;
    }
    public void setDecobligatetwo(String decobligatetwo){
        this.decobligatetwo = decobligatetwo;
    }

    public String getDecobligatethree(){
        return this.decobligatethree;
    }
    public void setDecobligatethree(String decobligatethree){
        this.decobligatethree = decobligatethree;
    }

    public String getDecobligatefour(){
        return this.decobligatefour;
    }
    public void setDecobligatefour(String decobligatefour){
        this.decobligatefour = decobligatefour;
    }

    public String getDecobligatefive(){
        return this.decobligatefive;
    }
    public void setDecobligatefive(String decobligatefive){
        this.decobligatefive = decobligatefive;
    }

    public Date getModifyTime(){
        return this.modifyTime;
    }
    public void setModifyTime(Date modifyTime){
        this.modifyTime = modifyTime;
    }

    public String getYear(){
        return this.year;
    }
    public void setYear(String year){
        this.year = year;
    }

    public String getHospcode(){
        return this.hospcode;
    }
    public void setHospcode(String hospcode){
        this.hospcode = hospcode;
    }
}
