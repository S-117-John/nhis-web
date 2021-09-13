package com.zebone.nhis.compay.ins.qgyb.vo;

import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;

import java.util.Date;

@Table(value="INS_QGYB_ITEM")
public class InsQgybItem {

    @PK
    @Field(value="PK_INSITEM",id= Field.KeyId.UUID)
    private String pkInsitem;

    @Field(value="PK_ORG")
    private String pkOrg;


    /**
     * 医保单位
     * 01=深圳医保，02=异地医保
     */
    @Field(value="EU_HPDICTTYPE")
    private String euHpdicttype;

    /**
     * 医保类型
     * 参照：040006
     */
    @Field(value="INS_TYPE")
    private String insType;

    /**
     * 项目类型
     * 1:药品，2:诊疗项目，3:耗材及其他
     */
    @Field(value="ITEM_TYPE")
    private String itemType;

    /**
     * 	目录类别
     * 	101:西成药，102:中药饮片，103:自制剂，104:民族药，201:医疗服务项目，301：耗材
     */
    @Field(value="LIST_TYPE")
    private String listType;

    /**
     * 医保项目编码
     */
    @Field(value="INS_ITEM_CODE")
    private String insItemCode;

    /**
     * 医保项目名称
     */
    @Field(value="INS_ITEM_NAME")
    private String insItemName;

    /**
     * 规格
     */
    @Field(value="SPEC")
    private String spec;

    /**
     * 批准文号
     */
    @Field(value="APPR_NO")
    private String apprNo;

    /**
     * 生产厂家
     */
    @Field(value="FACTORY_NAME")
    private String factoryName;

    /**
     * 剂型
     */
    @Field(value="DOSE_TYPE")
    private String doseType;

    @Field(value="DOSAGE_TYPE")
    private String dosageType;

    /**
     * 旧医保编码/省编码
     */
    @Field(value="OLD_CODE")
    private String oldCode;

    /**
     * 单价
     */
    @Field(value="PRICE")
    private Double price;

    /**
     * 自费比例
     */
    @Field(value="RATIO_SELF")
    private String ratioSelf;

    /**
     * 基本医疗自费比例
     */
    @Field(value="RATIO_BASE")
    private String ratioBase;

    /**
     * 社区门诊比例
     */
    @Field(value="RATIO_COMM")
    private String ratioComm;

    /**
     * 离休自费比例
     */
    @Field(value="RATIO_RET")
    private String ratioRet;

    /**
     * 生育自费比例
     */
    @Field(value="RATIO_BEAR")
    private String ratioBear;

    /**
     * 限价金额
     */
    @Field(value="PRICE_LIMIT")
    private String priceLimit;

    /**
     * 离休限价金额
     */
    @Field(value="PRICE_LIMIT_RET")
    private String priceLimitRet;

    /**
     * 备注
     */
    @Field(value="MEMO")
    private String memo;

    /**
     * 药品本位码(国家编码)
     */
    @Field(value="CODE_STD")
    private String codeStd;

    /**
     * 甲乙类
     */
    @Field(value="CLASS_TYPE")
    private String classType;

    /**
     * 数据来源
     */
    @Field(value="SOURCE")
    private String source;

    @Field(userfield="pkEmp",userfieldscop= Field.FieldType.INSERT)
    private String creator;

    /**
     * 创建时间
     */
    @Field(value="CREATE_TIME",date= Field.FieldType.INSERT)
    private Date createTime;

    @Field(value="DEL_FLAG")
    private String delFlag;



    /**
     * 开始时间
     */
    @Field(value="start_time",date= Field.FieldType.INSERT)
    private Date startTime;


    /**
     * 结束日期
     */
    @Field(value="end_time",date= Field.FieldType.INSERT)
    private Date endtime;


    /**
     * 五笔助记码
     */
    @Field(value="d_code")
    private String dcode;

    /**
     * 拼音助记码
     */
    @Field(value="spcode")
    private String spcode;

    /**
     * 分包装厂家
     */
    @Field(value="pack_factory")
    private String packfactory;



    /**
     * 生产企业编号
     */
    @Field(value="factory_no")
    private String factoryno;

    /**
     * 特殊限价药品标志
     */
    @Field(value="fixedPrice_flag")
    private String fixedPriceflag;

    /**
     * 特殊药品标志
     */
    @Field(value="specialDrugs_flag")
    private String specialDrugsflag;

    /**
     * 限制使用范围
     */
    @Field(value="desc_fit")
    private String descfit;


    /**
     * 限制使用标志
     */
    @Field(value="flag_fit")
    private String flagfit;

    /**
     * 药品注册证号
     */
    @Field(value="drugReg_no")
    private String drugRegNo;

    /**
     * 药品注册证号开始日期
     */
    @Field(value="drugReg_StartTime")
    private Date drugRegStartTime;


    /**
     * 药品注册证号结束日期
     */
    @Field(value="drugReg_EndTime")
    private Date drugRegEndTime;


    /**
     * 批准文号开始日期
     */
    @Field(value="appr_StartTime")
    private Date apprStartTime;


    /**
     * 批准文号结束日期
     */
    @Field(value="appr_EndTime")
    private Date apprEndTime;

    /**
     * 医保谈判药品标志
     */
    @Field(value="flag_negot")
    private String flagNegot;


    /**
     * 医保谈判药品名称
     */
    @Field(value="name_negot")
    private String nameNegot;

    /**
     * 版本号
     */
    @Field(value="version")
    private String version;


    /**
     * 仿制药一致性评价药品
     */
    @Field(value="evaluate_drug")
    private String evaluateDrug;


//***************************************************************************************************************

//    @Field(value="TABLE1301_ITEM1")
//    private String table1301Item1;
//
//    @Field(value="TABLE1301_ITEM2")
//    private String table1301Item2;
//
//    @Field(value="TABLE1301_ITEM3")
//    private String table1301Item3;
//
//    @Field(value="TABLE1301_ITEM4")
//    private String table1301Item4;
//
//    @Field(value="TABLE1301_ITEM5")
//    private String table1301Item5;
//
//    @Field(value="TABLE1301_ITEM6")
//    private String table1301Item6;
//
//    @Field(value="TABLE1301_ITEM7")
//    private String table1301Item7;
//
//    @Field(value="TABLE1301_ITEM9")
//    private String table1301Item9;
//
//    @Field(value="TABLE1301_ITEM10")
//    private String table1301Item10;
//
//    @Field(value="TABLE1301_ITEM11")
//    private String table1301Item11;
//
//    @Field(value="TABLE1301_ITEM12")
//    private String table1301Item12;
//
//    @Field(value="TABLE1301_ITEM13")
//    private String table1301Item13;
//
//    @Field(value="TABLE1301_ITEM14")
//    private String table1301Item14;
//
//    @Field(value="TABLE1301_ITEM15")
//    private String table1301Item15;
//
//    @Field(value="TABLE1301_ITEM16")
//    private String table1301Item16;
//
//    @Field(value="TABLE1301_ITEM17")
//    private String table1301Item17;
//
//    @Field(value="TABLE1301_ITEM18")
//    private String table1301Item18;
//
//    @Field(value="TABLE1301_ITEM19")
//    private String table1301Item19;
//
//    @Field(value="TABLE1301_ITEM20")
//    private String table1301Item20;
//
//    @Field(value="TABLE1301_ITEM21")
//    private String table1301Item21;
//
//    @Field(value="TABLE1301_ITEM22")
//    private String table1301Item22;
//
//    @Field(value="TABLE1301_ITEM23")
//    private String table1301Item23;
//
//    @Field(value="TABLE1301_ITEM24")
//    private String table1301Item24;
//
//    @Field(value="TABLE1301_ITEM25")
//    private String table1301Item25;
//
//    @Field(value="TABLE1301_ITEM26")
//    private String table1301Item26;
//
//    @Field(value="TABLE1301_ITEM27")
//    private String table1301Item27;
//
//    @Field(value="TABLE1301_ITEM28")
//    private String table1301Item28;
//
//    @Field(value="TABLE1301_ITEM29")
//    private String table1301Item29;
//
//    @Field(value="TABLE1301_ITEM30")
//    private String table1301Item30;
//
//    @Field(value="TABLE1301_ITEM31")
//    private String table1301Item31;
//
//    @Field(value="TABLE1301_ITEM32")
//    private String table1301Item32;
//
//    @Field(value="TABLE1301_ITEM33")
//    private String table1301Item33;
//
//    @Field(value="TABLE1301_ITEM34")
//    private String table1301Item34;
//
//    @Field(value="TABLE1301_ITEM35")
//    private String table1301Item35;
//
//    @Field(value="TABLE1301_ITEM36")
//    private String table1301Item36;
//
//    @Field(value="TABLE1301_ITEM37")
//    private String table1301Item37;
//
//    @Field(value="TABLE1301_ITEM38")
//    private String table1301Item38;
//
//    @Field(value="TABLE1301_ITEM39")
//    private String table1301Item39;
//
//    @Field(value="TABLE1301_ITEM40")
//    private String table1301Item40;
//
//    @Field(value="TABLE1301_ITEM41")
//    private String table1301Item41;
//
//    @Field(value="TABLE1301_ITEM42")
//    private String table1301Item42;
//
//    @Field(value="TABLE1301_ITEM43")
//    private String table1301Item43;
//
//    @Field(value="TABLE1301_ITEM44")
//    private String table1301Item44;
//
//    @Field(value="TABLE1301_ITEM45")
//    private String table1301Item45;
//
//    @Field(value="TABLE1301_ITEM46")
//    private String table1301Item46;
//
//    @Field(value="TABLE1301_ITEM47")
//    private String table1301Item47;
//
//    @Field(value="TABLE1301_ITEM48")
//    private String table1301Item48;
//
//    @Field(value="TABLE1301_ITEM49")
//    private String table1301Item49;
//
//    @Field(value="TABLE1301_ITEM50")
//    private String table1301Item50;
//
//    @Field(value="TABLE1301_ITEM51")
//    private String table1301Item51;
//
//    @Field(value="TABLE1301_ITEM52")
//    private String table1301Item52;
//
//    @Field(value="TABLE1301_ITEM53")
//    private String table1301Item53;
//
//    @Field(value="TABLE1301_ITEM54")
//    private String table1301Item54;
//
//    @Field(value="TABLE1301_ITEM55")
//    private String table1301Item55;
//
//    @Field(value="TABLE1301_ITEM56")
//    private String table1301Item56;
//
//    @Field(value="TABLE1301_ITEM57")
//    private String table1301Item57;
//
//    @Field(value="TABLE1301_ITEM58")
//    private String table1301Item58;
//
//    @Field(value="TABLE1301_ITEM59")
//    private String table1301Item59;
//
//    @Field(value="TABLE1301_ITEM60")
//    private String table1301Item60;
//
//    @Field(value="TABLE1301_ITEM61")
//    private String table1301Item61;
//
//    @Field(value="TABLE1301_ITEM62")
//    private String table1301Item62;
//
//    @Field(value="TABLE1301_ITEM63")
//    private String table1301Item63;
//
//    @Field(value="TABLE1301_ITEM64")
//    private String table1301Item64;
//
//    @Field(value="TABLE1301_ITEM65")
//    private String table1301Item65;
//
//    @Field(value="TABLE1301_ITEM66")
//    private String table1301Item66;
//
//    @Field(value="TABLE1301_ITEM67")
//    private String table1301Item67;
//
//    @Field(value="TABLE1301_ITEM68")
//    private String table1301Item68;
//
//    @Field(value="TABLE1301_ITEM69")
//    private String table1301Item69;
//
//    @Field(value="TABLE1301_ITEM70")
//    private String table1301Item70;
//
//
//    @Field(value="TABLE1301_ITEM71")
//    private String table1301Item71;
//
//    @Field(value="TABLE1301_ITEM72")
//    private String table1301Item72;
//
//    @Field(value="TABLE1301_ITEM73")
//    private String table1301Item73;
//
//    @Field(value="TABLE1301_ITEM74")
//    private String table1301Item74;
//
//    @Field(value="TABLE1301_ITEM75")
//    private String table1301Item75;
//
//    @Field(value="TABLE1301_ITEM76")
//    private String table1301Item76;
//
//    @Field(value="TABLE1301_ITEM77")
//    private String table1301Item77;
//
//    @Field(value="TABLE1301_ITEM78")
//    private String table1301Item78;
//
//    @Field(value="TABLE1301_ITEM79")
//    private String table1301Item79;
//
//    @Field(value="TABLE1301_ITEM80")
//    private String table1301Item80;
//
//    @Field(value="TABLE1301_ITEM81")
//    private String table1301Item81;
//
//    @Field(value="TABLE1301_ITEM82")
//    private String table1301Item82;
//
//    @Field(value="TABLE1301_ITEM83")
//    private String table1301Item83;
//
//    @Field(value="TABLE1301_ITEM84")
//    private String table1301Item84;
//
//    @Field(value="TABLE1301_ITEM85")
//    private String table1301Item85;
//
//    @Field(value="TABLE1301_ITEM86")
//    private String table1301Item86;
//
//    @Field(value="TABLE1301_ITEM87")
//    private String table1301Item87;
//
//    @Field(value="TABLE1301_ITEM88")
//    private String table1301Item88;
//
//    @Field(value="TABLE1301_ITEM89")
//    private String table1301Item89;
//
//    @Field(value="TABLE1301_ITEM90")
//    private String table1301Item90;
//
//    @Field(value="TABLE1301_ITEM91")
//    private String table1301Item91;
//
//    @Field(value="TABLE1301_ITEM92")
//    private String table1301Item92;
//
//
//



    @Field(date= Field.FieldType.ALL)
    private Date ts;

    public String getPkInsitem() {
        return pkInsitem;
    }

    public void setPkInsitem(String pkInsitem) {
        this.pkInsitem = pkInsitem;
    }

    public String getPkOrg() {
        return pkOrg;
    }

    public void setPkOrg(String pkOrg) {
        this.pkOrg = pkOrg;
    }

    public String getEuHpdicttype() {
        return euHpdicttype;
    }

    public void setEuHpdicttype(String euHpdicttype) {
        this.euHpdicttype = euHpdicttype;
    }

    public String getInsType() {
        return insType;
    }

    public void setInsType(String insType) {
        this.insType = insType;
    }

    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    public String getListType() {
        return listType;
    }

    public void setListType(String listType) {
        this.listType = listType;
    }

    public String getInsItemCode() {
        return insItemCode;
    }

    public void setInsItemCode(String insItemCode) {
        this.insItemCode = insItemCode;
    }

    public String getInsItemName() {
        return insItemName;
    }

    public void setInsItemName(String insItemName) {
        this.insItemName = insItemName;
    }

    public String getSpec() {
        return spec;
    }

    public void setSpec(String spec) {
        this.spec = spec;
    }

    public String getApprNo() {
        return apprNo;
    }

    public void setApprNo(String apprNo) {
        this.apprNo = apprNo;
    }

    public String getFactoryName() {
        return factoryName;
    }

    public void setFactoryName(String factoryName) {
        this.factoryName = factoryName;
    }

    public String getDoseType() {
        return doseType;
    }

    public void setDoseType(String doseType) {
        this.doseType = doseType;
    }

    public String getDosageType() {
        return dosageType;
    }

    public void setDosageType(String dosageType) {
        this.dosageType = dosageType;
    }

    public String getOldCode() {
        return oldCode;
    }

    public void setOldCode(String oldCode) {
        this.oldCode = oldCode;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getRatioSelf() {
        return ratioSelf;
    }

    public void setRatioSelf(String ratioSelf) {
        this.ratioSelf = ratioSelf;
    }

    public String getRatioBase() {
        return ratioBase;
    }

    public void setRatioBase(String ratioBase) {
        this.ratioBase = ratioBase;
    }

    public String getRatioComm() {
        return ratioComm;
    }

    public void setRatioComm(String ratioComm) {
        this.ratioComm = ratioComm;
    }

    public String getRatioRet() {
        return ratioRet;
    }

    public void setRatioRet(String ratioRet) {
        this.ratioRet = ratioRet;
    }

    public String getRatioBear() {
        return ratioBear;
    }

    public void setRatioBear(String ratioBear) {
        this.ratioBear = ratioBear;
    }

    public String getPriceLimit() {
        return priceLimit;
    }

    public void setPriceLimit(String priceLimit) {
        this.priceLimit = priceLimit;
    }

    public String getPriceLimitRet() {
        return priceLimitRet;
    }

    public void setPriceLimitRet(String priceLimitRet) {
        this.priceLimitRet = priceLimitRet;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getCodeStd() {
        return codeStd;
    }

    public void setCodeStd(String codeStd) {
        this.codeStd = codeStd;
    }

    public String getClassType() {
        return classType;
    }

    public void setClassType(String classType) {
        this.classType = classType;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

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

    public String getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag;
    }

    public Date getTs() {
        return ts;
    }

    public void setTs(Date ts) {
        this.ts = ts;
    }



    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }
    public String getDcode() {
        return dcode;
    }

    public void setDcode(String dcode) {
        this.dcode = dcode;
    }

    public Date getEndtime() {
        return endtime;
    }

    public void setEndtime(Date endtime) {
        this.endtime = endtime;
    }

    public String getSpcode() {
        return spcode;
    }

    public void setSpcode(String spcode) {
        this.spcode = spcode;
    }

    public String getPackfactory() {
        return packfactory;
    }

    public void setPackfactory(String packfactory) {
        this.packfactory = packfactory;
    }

    public String getFactoryno() {
        return factoryno;
    }

    public void setFactoryno(String factoryno) {
        this.factoryno = factoryno;
    }

    public String getFixedPriceflag() {
        return fixedPriceflag;
    }

    public void setFixedPriceflag(String fixedPriceflag) {
        this.fixedPriceflag = fixedPriceflag;
    }

    public String getSpecialDrugsflag() {
        return specialDrugsflag;
    }

    public void setSpecialDrugsflag(String specialDrugsflag) {
        this.specialDrugsflag = specialDrugsflag;
    }


    public String getDescfit() {
        return descfit;
    }

    public void setDescfit(String descfit) {
        this.descfit = descfit;
    }

    public String getFlagfit() {
        return flagfit;
    }

    public void setFlagfit(String flagfit) {
        this.flagfit = flagfit;
    }

    public String getDrugRegNo() {
        return drugRegNo;
    }

    public void setDrugRegNo(String drugRegNo) {
        this.drugRegNo = drugRegNo;
    }

    public Date getDrugRegStartTime() {
        return drugRegStartTime;
    }

    public void setDrugRegStartTime(Date drugRegStartTime) {
        this.drugRegStartTime = drugRegStartTime;
    }


    public Date getDrugRegEndTime() {
        return drugRegEndTime;
    }

    public void setDrugRegEndTime(Date drugRegEndTime) {
        this.drugRegEndTime = drugRegEndTime;
    }

    public Date getApprStartTime() {
        return apprStartTime;
    }

    public void setApprStartTime(Date apprStartTime) {
        this.apprStartTime = apprStartTime;
    }

    public Date getApprEndTime() {
        return apprEndTime;
    }

    public void setApprEndTime(Date apprEndTime) {
        this.apprEndTime = apprEndTime;
    }

    public String getFlagNegot() {
        return flagNegot;
    }

    public void setFlagNegot(String flagNegot) {
        this.flagNegot = flagNegot;
    }

    public String getNameNegot() {
        return nameNegot;
    }

    public void setNameNegot(String nameNegot) {
        this.nameNegot = nameNegot;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getEvaluateDrug() {
        return evaluateDrug;
    }

    public void setEvaluateDrug(String evaluateDrug) {
        this.evaluateDrug = evaluateDrug;
    }
}
