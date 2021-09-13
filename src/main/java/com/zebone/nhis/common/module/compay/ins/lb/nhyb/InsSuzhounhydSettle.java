package com.zebone.nhis.common.module.compay.ins.lb.nhyb;

import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: INS_SUZHOUNHYD_SETTLE 
 *
 * @since 2018-10-19 05:32:48
 */
@Table(value="INS_SUZHOUNHYD_SETTLE")
public class InsSuzhounhydSettle extends BaseModule  {

    /** ID - 主键 */
	@PK
	@Field(value="ID",id=KeyId.UUID)
    private String id;

    /** DECSIGN - DECSIGN-成功与否标识符 */
	@Field(value="DECSIGN")
    private String decsign;

    /** DECREDEEMNO - DECREDEEMNO-补偿单号 */
	@Field(value="DECREDEEMNO")
    private String decredeemno;

    /** DECMEMBERNO - DECMEMBERNO-成员编码 */
	@Field(value="DECMEMBERNO")
    private String decmemberno;

    /** DECNAME - DECNAME-成员姓名 */
	@Field(value="DECNAME")
    private String decname;

    /** DECBOOKNO - DECBOOKNO-医疗证、卡号 */
	@Field(value="DECBOOKNO")
    private String decbookno;

    /** DECSEXNAME - DECSEXNAME-性别名称 */
	@Field(value="DECSEXNAME")
    private String decsexname;

    /** DECBIRTHDAY - DECBIRTHDAY-出生年月日 */
	@Field(value="DECBIRTHDAY")
    private String decbirthday;

    /** DECMASTERNAME - DECMASTERNAME-户主姓名 */
	@Field(value="DECMASTERNAME")
    private String decmastername;

    /** DECRELATIONNAME - DECRELATIONNAME-与户主关系名称 */
	@Field(value="DECRELATIONNAME")
    private String decrelationname;

    /** DECIDENTITYNAME - DECIDENTITYNAME-个人身份属性名称 */
	@Field(value="DECIDENTITYNAME")
    private String decidentityname;

    /** DECIDCARD - DECIDCARD-身份证号码 */
	@Field(value="DECIDCARD")
    private String decidcard;

    /** DECCURRYEARREDEEMCOUNT - DECCURRYEARREDEEMCOUNT-当前年度成员住院已补偿次数 */
	@Field(value="DECCURRYEARREDEEMCOUNT")
    private String deccurryearredeemcount;

    /** DECCURRYEARTOTAL - DECCURRYEARTOTAL-当前年度成员住院已补偿总医疗费用 */
	@Field(value="DECCURRYEARTOTAL")
    private String deccurryeartotal;

    /** DECCURRYEARENABLEMONEY - DECCURRYEARENABLEMONEY-当前年度成员住院已补偿总保内费用 */
	@Field(value="DECCURRYEARENABLEMONEY")
    private String deccurryearenablemoney;

    /** DECCURRYEARREDDEMMONEY - DECCURRYEARREDDEMMONEY-当前年度成员住院已补偿金额 */
	@Field(value="DECCURRYEARREDDEMMONEY")
    private String deccurryearreddemmoney;

    /** DECFAMILYNO - DECFAMILYNO-成员家庭编码 */
	@Field(value="DECFAMILYNO")
    private String decfamilyno;

    /** DECADDRESS - DECADDRESS-成员家庭住址 */
	@Field(value="DECADDRESS")
    private String decaddress;

    /** DECDOORPROPNAME - DECDOORPROPNAME-户属性名称 */
	@Field(value="DECDOORPROPNAME")
    private String decdoorpropname;

    /** DECJOINPROPNAME - DECJOINPROPNAME-参合属性名称 */
	@Field(value="DECJOINPROPNAME")
    private String decjoinpropname;

    /** DECCURRFAMILYREDEEMCOUNT - DECCURRFAMILYREDEEMCOUNT-当前年度家庭住院已补偿次数 */
	@Field(value="DECCURRFAMILYREDEEMCOUNT")
    private String deccurrfamilyredeemcount;

    /** DECCURRFAMILYTOTAL - DECCURRFAMILYTOTAL-当前年度家庭住院已补偿总医疗费用 */
	@Field(value="DECCURRFAMILYTOTAL")
    private String deccurrfamilytotal;

    /** DECCURRFAMILYENABLEMONEY - DECCURRFAMILYENABLEMONEY-当前年度家庭住院已补偿保内费用 */
	@Field(value="DECCURRFAMILYENABLEMONEY")
    private String deccurrfamilyenablemoney;

    /** DECCURRFAMILYREDDEMMONEY - DECCURRFAMILYREDDEMMONEY-当前年度家庭住院已补偿金额 */
	@Field(value="DECCURRFAMILYREDDEMMONEY")
    private String deccurrfamilyreddemmoney;

    /** DECTOTALCOSTS - DECTOTALCOSTS-本次住院总医疗费用 */
	@Field(value="DECTOTALCOSTS")
    private String dectotalcosts;

    /** DECENABLEMONEY - DECENABLEMONEY-本次住院保内费用 */
	@Field(value="DECENABLEMONEY")
    private String decenablemoney;

    /** DECESSENTIALMEDICINEMONEY - DECESSENTIALMEDICINEMONEY-本次住院费用中国定基本药品费用(单位元,小数点后保留两位) */
	@Field(value="DECESSENTIALMEDICINEMONEY")
    private String decessentialmedicinemoney;

    /** DECPROVINCEMEDICINEMONEY - DECPROVINCEMEDICINEMONEY-本次住院费用中省补基本药品(单位元,小数点后保留两位) */
	@Field(value="DECPROVINCEMEDICINEMONEY")
    private String decprovincemedicinemoney;

    /** DECSTARTMONEY - DECSTARTMONEY-本次住院补偿扣除起伏线金额（单位元，小数点后保留两位） */
	@Field(value="DECSTARTMONEY")
    private String decstartmoney;

    /** DECCHECKMONEY - DECCHECKMONEY-本次住院补偿核算金额（单位元，小数点后保留两位） */
	@Field(value="DECCHECKMONEY")
    private String deccheckmoney;

    /** DECREDEEMTYPENAME - DECREDEEMTYPENAME-补偿类型名称 */
	@Field(value="DECREDEEMTYPENAME")
    private String decredeemtypename;

    /** DECCHECKDATE - DECCHECKDATE-核算日期 */
	@Field(value="DECCHECKDATE")
    private String deccheckdate;

    /** DECISSPECIAL - DECISSPECIAL-是否为单病种补偿 */
	@Field(value="DECISSPECIAL")
    private String decisspecial;

    /** DECISPAUL - DECISPAUL-是否实行保底补偿 */
	@Field(value="DECISPAUL")
    private String decispaul;

    /** DECISADUIT - DECISADUIT-是否需要审核 */
	@Field(value="DECISADUIT")
    private String decisaduit;

    /** DECANLAGERNMONEY - DECANLAGERNMONEY-追补金额，中药和国定基本药品提高补偿额部分（单位元，小数点后保留两位） */
	@Field(value="DECANLAGERNMONEY")
    private String decanlagernmoney;

    /** DECFUNDPAYMONEY - DECFUNDPAYMONEY-单病种费用定额（单位元，小数点后保留两位） */
	@Field(value="DECFUNDPAYMONEY")
    private String decfundpaymoney;

    /** DECHOSPASSUMEMONEY - DECHOSPASSUMEMONEY-医疗机构承担费用(单位元，小数点后保留两位) */
	@Field(value="DECHOSPASSUMEMONEY")
    private String dechospassumemoney;

    /** DECPERSONALPAYMONEY - DECPERSONALPAYMONEY-单病种个人自付费用（单位元，小数点后保留两位） */
	@Field(value="DECPERSONALPAYMONEY")
    private String decpersonalpaymoney;

    /** DECCIVILREDEEMMONEY - DECCIVILREDEEMMONEY-民政救助补偿金额（单位元，小数点后保留两位） */
	@Field(value="DECCIVILREDEEMMONEY")
    private String deccivilredeemmoney;

    /** DECMATERIALMONEY - DECMATERIALMONEY-高额材料限价超额费用（单位元，小数点后保留两位） */
	@Field(value="DECMATERIALMONEY")
    private String decmaterialmoney;

    /** DECOBLIGATEONE - DECOBLIGATEONE-大病保险补偿金额 */
	@Field(value="DECOBLIGATEONE")
    private String decobligateone;

    /** DECOBLIGATETWO - DECOBLIGATETWO-财政兜底资金金额 */
	@Field(value="DECOBLIGATETWO")
    private String decobligatetwo;

    /** DECOBLIGATETHREE - DECOBLIGATETHREE-其他保障金额 */
	@Field(value="DECOBLIGATETHREE")
    private String decobligatethree;

    /** DECOBLIGATEFOUR - DECOBLIGATEFOUR-预留字段4 */
	@Field(value="DECOBLIGATEFOUR")
    private String decobligatefour;

    /** DECOBLIGATEFIVE - DECOBLIGATEFIVE-预留字段5 */
	@Field(value="DECOBLIGATEFIVE")
    private String decobligatefive;

    /** MODIFY_TIME - 最后操作时间 */
	@Field(value="MODIFY_TIME")
    private Date modifyTime;

	@Field(value="PK_PV")
    private String pkPv;

	@Field(value="PK_SETTLE")
    private String pkSettle;

	@Field(value="PK_INS_PI")
    private String pkInsPi;


    public String getId(){
        return this.id;
    }
    public void setId(String id){
        this.id = id;
    }

    public String getDecsign(){
        return this.decsign;
    }
    public void setDecsign(String decsign){
        this.decsign = decsign;
    }

    public String getDecredeemno(){
        return this.decredeemno;
    }
    public void setDecredeemno(String decredeemno){
        this.decredeemno = decredeemno;
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

    public String getDecbookno(){
        return this.decbookno;
    }
    public void setDecbookno(String decbookno){
        this.decbookno = decbookno;
    }

    public String getDecsexname(){
        return this.decsexname;
    }
    public void setDecsexname(String decsexname){
        this.decsexname = decsexname;
    }

    public String getDecbirthday(){
        return this.decbirthday;
    }
    public void setDecbirthday(String decbirthday){
        this.decbirthday = decbirthday;
    }

    public String getDecmastername(){
        return this.decmastername;
    }
    public void setDecmastername(String decmastername){
        this.decmastername = decmastername;
    }

    public String getDecrelationname(){
        return this.decrelationname;
    }
    public void setDecrelationname(String decrelationname){
        this.decrelationname = decrelationname;
    }

    public String getDecidentityname(){
        return this.decidentityname;
    }
    public void setDecidentityname(String decidentityname){
        this.decidentityname = decidentityname;
    }

    public String getDecidcard(){
        return this.decidcard;
    }
    public void setDecidcard(String decidcard){
        this.decidcard = decidcard;
    }

    public String getDeccurryearredeemcount(){
        return this.deccurryearredeemcount;
    }
    public void setDeccurryearredeemcount(String deccurryearredeemcount){
        this.deccurryearredeemcount = deccurryearredeemcount;
    }

    public String getDeccurryeartotal(){
        return this.deccurryeartotal;
    }
    public void setDeccurryeartotal(String deccurryeartotal){
        this.deccurryeartotal = deccurryeartotal;
    }

    public String getDeccurryearenablemoney(){
        return this.deccurryearenablemoney;
    }
    public void setDeccurryearenablemoney(String deccurryearenablemoney){
        this.deccurryearenablemoney = deccurryearenablemoney;
    }

    public String getDeccurryearreddemmoney(){
        return this.deccurryearreddemmoney;
    }
    public void setDeccurryearreddemmoney(String deccurryearreddemmoney){
        this.deccurryearreddemmoney = deccurryearreddemmoney;
    }

    public String getDecfamilyno(){
        return this.decfamilyno;
    }
    public void setDecfamilyno(String decfamilyno){
        this.decfamilyno = decfamilyno;
    }

    public String getDecaddress(){
        return this.decaddress;
    }
    public void setDecaddress(String decaddress){
        this.decaddress = decaddress;
    }

    public String getDecdoorpropname(){
        return this.decdoorpropname;
    }
    public void setDecdoorpropname(String decdoorpropname){
        this.decdoorpropname = decdoorpropname;
    }

    public String getDecjoinpropname(){
        return this.decjoinpropname;
    }
    public void setDecjoinpropname(String decjoinpropname){
        this.decjoinpropname = decjoinpropname;
    }

    public String getDeccurrfamilyredeemcount(){
        return this.deccurrfamilyredeemcount;
    }
    public void setDeccurrfamilyredeemcount(String deccurrfamilyredeemcount){
        this.deccurrfamilyredeemcount = deccurrfamilyredeemcount;
    }

    public String getDeccurrfamilytotal(){
        return this.deccurrfamilytotal;
    }
    public void setDeccurrfamilytotal(String deccurrfamilytotal){
        this.deccurrfamilytotal = deccurrfamilytotal;
    }

    public String getDeccurrfamilyenablemoney(){
        return this.deccurrfamilyenablemoney;
    }
    public void setDeccurrfamilyenablemoney(String deccurrfamilyenablemoney){
        this.deccurrfamilyenablemoney = deccurrfamilyenablemoney;
    }

    public String getDeccurrfamilyreddemmoney(){
        return this.deccurrfamilyreddemmoney;
    }
    public void setDeccurrfamilyreddemmoney(String deccurrfamilyreddemmoney){
        this.deccurrfamilyreddemmoney = deccurrfamilyreddemmoney;
    }

    public String getDectotalcosts(){
        return this.dectotalcosts;
    }
    public void setDectotalcosts(String dectotalcosts){
        this.dectotalcosts = dectotalcosts;
    }

    public String getDecenablemoney(){
        return this.decenablemoney;
    }
    public void setDecenablemoney(String decenablemoney){
        this.decenablemoney = decenablemoney;
    }

    public String getDecessentialmedicinemoney(){
        return this.decessentialmedicinemoney;
    }
    public void setDecessentialmedicinemoney(String decessentialmedicinemoney){
        this.decessentialmedicinemoney = decessentialmedicinemoney;
    }

    public String getDecprovincemedicinemoney(){
        return this.decprovincemedicinemoney;
    }
    public void setDecprovincemedicinemoney(String decprovincemedicinemoney){
        this.decprovincemedicinemoney = decprovincemedicinemoney;
    }

    public String getDecstartmoney(){
        return this.decstartmoney;
    }
    public void setDecstartmoney(String decstartmoney){
        this.decstartmoney = decstartmoney;
    }

    public String getDeccheckmoney(){
        return this.deccheckmoney;
    }
    public void setDeccheckmoney(String deccheckmoney){
        this.deccheckmoney = deccheckmoney;
    }

    public String getDecredeemtypename(){
        return this.decredeemtypename;
    }
    public void setDecredeemtypename(String decredeemtypename){
        this.decredeemtypename = decredeemtypename;
    }

    public String getDeccheckdate(){
        return this.deccheckdate;
    }
    public void setDeccheckdate(String deccheckdate){
        this.deccheckdate = deccheckdate;
    }

    public String getDecisspecial(){
        return this.decisspecial;
    }
    public void setDecisspecial(String decisspecial){
        this.decisspecial = decisspecial;
    }

    public String getDecispaul(){
        return this.decispaul;
    }
    public void setDecispaul(String decispaul){
        this.decispaul = decispaul;
    }

    public String getDecisaduit(){
        return this.decisaduit;
    }
    public void setDecisaduit(String decisaduit){
        this.decisaduit = decisaduit;
    }

    public String getDecanlagernmoney(){
        return this.decanlagernmoney;
    }
    public void setDecanlagernmoney(String decanlagernmoney){
        this.decanlagernmoney = decanlagernmoney;
    }

    public String getDecfundpaymoney(){
        return this.decfundpaymoney;
    }
    public void setDecfundpaymoney(String decfundpaymoney){
        this.decfundpaymoney = decfundpaymoney;
    }

    public String getDechospassumemoney(){
        return this.dechospassumemoney;
    }
    public void setDechospassumemoney(String dechospassumemoney){
        this.dechospassumemoney = dechospassumemoney;
    }

    public String getDecpersonalpaymoney(){
        return this.decpersonalpaymoney;
    }
    public void setDecpersonalpaymoney(String decpersonalpaymoney){
        this.decpersonalpaymoney = decpersonalpaymoney;
    }

    public String getDeccivilredeemmoney(){
        return this.deccivilredeemmoney;
    }
    public void setDeccivilredeemmoney(String deccivilredeemmoney){
        this.deccivilredeemmoney = deccivilredeemmoney;
    }

    public String getDecmaterialmoney(){
        return this.decmaterialmoney;
    }
    public void setDecmaterialmoney(String decmaterialmoney){
        this.decmaterialmoney = decmaterialmoney;
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

    public String getPkPv(){
        return this.pkPv;
    }
    public void setPkPv(String pkPv){
        this.pkPv = pkPv;
    }

    public String getPkSettle(){
        return this.pkSettle;
    }
    public void setPkSettle(String pkSettle){
        this.pkSettle = pkSettle;
    }

    public String getPkInsPi(){
        return this.pkInsPi;
    }
    public void setPkInsPi(String pkInsPi){
        this.pkInsPi = pkInsPi;
    }
}