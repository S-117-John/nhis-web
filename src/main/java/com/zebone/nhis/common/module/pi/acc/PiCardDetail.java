package com.zebone.nhis.common.module.pi.acc;

import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.Field.FieldType;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;


/**
 * Table: PI_CARD_DETAIL 
 *
 * @since 2016-09-20 02:11:36
 */
@Table(value="PI_CARD_DETAIL")
public class PiCardDetail extends BaseModule  {
	
	/**
	 * 操作类型:0建卡，1挂失，2启用，8 退卡，9 销卡
	 */
	public static String CARD_EU_OPTYPE_0 = "0";
	
	/**
	 * 操作类型:0建卡，1挂失，2启用，8 退卡，9 销卡
	 */
	public static String CARD_EU_OPTYPE_1 = "1";
	
	/**
	 * 操作类型:0建卡，1挂失，2启用，8 退卡，9 销卡
	 */
	public static String CARD_EU_OPTYPE_2 = "2";
	
	/**
	 * 操作类型:0建卡，1挂失，2启用，8 退卡，9 销卡
	 */
	public static String CARD_EU_OPTYPE_8 = "8";
	
	/**
	 * 操作类型:0建卡，1挂失，2启用，8 退卡，9 销卡
	 */
	public static String CARD_EU_OPTYPE_9 = "9";

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** PK_PICARDDT - 患者卡操作明细主键 */
	@PK
	@Field(value="PK_PICARDDT",id=KeyId.UUID)
    private String pkPicarddt;

    /** PK_PICARD - 患者卡主键 */
	@Field(value="PK_PICARD")
    private String pkPicard;

    /** PK_PI - 患者主键 */
	@Field(value="PK_PI")
    private String pkPi;

    /** CARD_NO - 卡号 */
	@Field(value="CARD_NO")
    private String cardNo;

    /** EU_OPTYPE - 操作类型:0建卡，1挂失，2启用，8 退卡，9 销卡 */
	@Field(value="EU_OPTYPE")
    private String euOptype;

    /** PK_EMP_OPERA - 操作人员 */
	@Field(value="PK_EMP_OPERA")
    private String pkEmpOpera;

    /** NAME_EMP_OPERA - 操作人员名称 */
	@Field(value="NAME_EMP_OPERA")
    private String nameEmpOpera;

    /** DATE_HAP - 操作日期 */
	@Field(value="DATE_HAP")
    private Date dateHap;

    /** NOTE - 备注 */
	@Field(value="NOTE")
    private String note;

    /** MODIFIER - 修改人 */
	@Field(userfield="pkEmp",userfieldscop=FieldType.ALL)
    private String modifier;


    public String getPkPicarddt(){
        return this.pkPicarddt;
    }
    public void setPkPicarddt(String pkPicarddt){
        this.pkPicarddt = pkPicarddt;
    }

    public String getPkPicard(){
        return this.pkPicard;
    }
    public void setPkPicard(String pkPicard){
        this.pkPicard = pkPicard;
    }

    public String getPkPi(){
        return this.pkPi;
    }
    public void setPkPi(String pkPi){
        this.pkPi = pkPi;
    }

    public String getCardNo(){
        return this.cardNo;
    }
    public void setCardNo(String cardNo){
        this.cardNo = cardNo;
    }

    public String getEuOptype(){
        return this.euOptype;
    }
    public void setEuOptype(String euOptype){
        this.euOptype = euOptype;
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

    public Date getDateHap(){
        return this.dateHap;
    }
    public void setDateHap(Date dateHap){
        this.dateHap = dateHap;
    }

    public String getNote(){
        return this.note;
    }
    public void setNote(String note){
        this.note = note;
    }

    public String getModifier(){
        return this.modifier;
    }
    public void setModifier(String modifier){
        this.modifier = modifier;
    }
}