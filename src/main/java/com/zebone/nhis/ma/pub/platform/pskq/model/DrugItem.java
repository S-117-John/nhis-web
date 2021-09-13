package com.zebone.nhis.ma.pub.platform.pskq.model;

import com.zebone.nhis.ma.pub.platform.pskq.annotation.MetadataDescribe;

/**
 * @author lijin
 * 药品项目消息模型
 */
public class DrugItem {

    @MetadataDescribe(id= "LHDE0011001",name = "药品编码",eName = "DRUG_CODE")
    private String drugCode;

    @MetadataDescribe(id= "LHDE0011002",name = "药品商品名称",eName = "TRADE_NAME")
    private String tradeName;

    @MetadataDescribe(id= "LHDE0011003",name = "药品商品名拼音码",eName = "TRADE_SPELL_CODE")
    private String spellCode;

    @MetadataDescribe(id= "LHDE0011004",name = "药品商品名五笔码",eName = "TRADE_WB_CODE")
    private String wbCode;

    @MetadataDescribe(id= "LHDE0011005",name = "药品商品名自定义码",eName = "TRADE_CUSTOM_CODE")
    private String tradeCustomCode;

    @MetadataDescribe(id= "LHDE0011006",name = "药品通用名",eName = "REGULAR_NAME")
    private String regularName;

    @MetadataDescribe(id= "LHDE0011007",name = "药品通用名拼音码",eName = "REGULAR_SPELL_CODE")
    private String regularSpell;

    @MetadataDescribe(id= "LHDE0011008",name = "药品通用名五笔码",eName = "REGULAR_WB_CODE")
    private String regularWb;

    @MetadataDescribe(id= "LHDE0011009",name = "药品通用名自定义码",eName = "REGULAR_CUSTOM_CODE")
    private String regularCustom;

    @MetadataDescribe(id= "LHDE0011010",name = "药品学名",eName = "FORMAL_NAME")
    private String formalName;

    @MetadataDescribe(id= "LHDE0011011",name = "药品学名拼音码",eName = "FORMAL_SPELL_CODE")
    private String formalSpell;

    @MetadataDescribe(id= "LHDE0011012",name = "药品学名五笔码",eName = "FORMAL_WB_CODE")
    private String formalWb;

    @MetadataDescribe(id= "LHDE0011013",name = "药品学名自定义码",eName = "FORMAL_CUSTOM_CODE")
    private String formalCustom;

    @MetadataDescribe(id= "LHDE0011014",name = "药品别名",eName = "OTHER_NAME;")
    private String otherName;

    @MetadataDescribe(id= "LHDE0011015",name = "药品别名拼音码",eName = "OTHER_SPELL_CODE")
    private String otherSpell;

    @MetadataDescribe(id= "LHDE0011016",name = "药品别名五笔码",eName = "OTHER_WB_CODE")
    private String otherWb;

    @MetadataDescribe(id= "LHDE0011017",name = "药品别名自定义码",eName = "OTHER_CUSTOM_CODE")
    private String otherCustom;

    @MetadataDescribe(id= "LHDE0011018",name = "药品英文通用名",eName = "ENGLISH_REGULAR")
    private String englishRegular;

    @MetadataDescribe(id= "LHDE0011019",name = "药品英文别名",eName = "ENGLISH_OTHER")
    private String englishOther;

    @MetadataDescribe(id= "LHDE0011020",name = "药品英文名",eName = "ENGLISH_NAME")
    private String englishName;

    @MetadataDescribe(id= "LHDE0011021",name = "国际编码",eName = "INTERNATIONAL_CODE")
    private String internationalCode;

    @MetadataDescribe(id= "LHDE0011022",name = "国家编码",eName = "GB_CODE")
    private String gbCode;

    @MetadataDescribe(id= "LHDE0011023",name = "系统类别",eName = "CLASS_CODE")
    private String classCode;

    @MetadataDescribe(id= "LHDE0011024",name = "最小费用代码",eName = "FEE_CODE")
    private String feeCode;

    @MetadataDescribe(id= "LHDE0011025",name = "药品类型代码",eName = "DRUG_TYPE")
    private String drugType;

    @MetadataDescribe(id= "LHDE0011026",name = "药品性质",eName = "DRUG_QUALITY")
    private String drugQuality;

    @MetadataDescribe(id= "LHDE0011027",name = "药品级别",eName = "ITEM_GRADE")
    private String itemGrade;

    @MetadataDescribe(id= "LHDE0011028",name = "药品规格",eName = "SPECS")
    private String specs;

    @MetadataDescribe(id= "LHDE0011029",name = "参考零售价",eName = "RETAIL_PRICE")
    private String retailPrice;

    @MetadataDescribe(id= "LHDE0011030",name = "参考零售价（VIP）",eName = "RETAIL_PRICE1")
    private String retailPrice1;

    @MetadataDescribe(id= "LHDE0011031",name = "参考零售价2",eName = "RETAIL_PRICE2")
    private String retailPrice2;

    @MetadataDescribe(id= "LHDE0011032",name = "参考批发价",eName = "WHOLESALE_PRICE")
    private String wholesalePrice;

    @MetadataDescribe(id= "LHDE0011033",name = "最新购入价",eName = "PURCHASE_PRICE")
    private String purchasePrice;

    @MetadataDescribe(id= "LHDE0011034",name = "最高零售价",eName = "TOP_RETAILPRICE")
    private String topRetailprice;

    @MetadataDescribe(id= "LHDE0011035",name = "包装单位",eName = "PACK_UNIT")
    private String packUnitl;

    @MetadataDescribe(id= "LHDE0011036",name = "包装数",eName = "PACK_QTY")
    private String packQty;

    @MetadataDescribe(id= "LHDE0011037",name = "最小单位",eName = "MIN_UNIT")
    private String minUnit;

    @MetadataDescribe(id= "LHSD0011035",name = "剂型编码",eName = "DOSE_MODEL_CODE")
    private String doseModelCode;

    @MetadataDescribe(id= "LHSD0011036",name = "基本剂量",eName = "BASE_DOSE")
    private String baseDose;

    @MetadataDescribe(id= "LHSD0011036",name = "第二基本剂量",eName = "SECOND_BASE_DOSE")
    private String secondBaseDose;

    @MetadataDescribe(id= "LHSD0011037",name = "剂量单位",eName = "DOSE_UNIT")
    private String doseUnit;

    @MetadataDescribe(id= "LHSD0011037",name = "第二剂量单位",eName = "SECOND_DOSE_UNIT")
    private String secondDoseUnit;

    @MetadataDescribe(id= "LHSD0011038",name = "用药途径代码",eName = "USAGE_CODE")
    private String usageCode;

    @MetadataDescribe(id= "LHSD0011039",name = "频次编码",eName = "FREQUENCY_CODE")
    private String frequencyCode;

    @MetadataDescribe(id= "LHSD0011040",name = "一次用量",eName = "ONCE_DOSE")
    private String onceDose;

    @MetadataDescribe(id= "LHSD0011040",name = "一次用量单位",eName = "ONCE_DOSE_UNIT")
    private String onceDoseUnit;

    @MetadataDescribe(id= "LHSD0011041",name = "注意事项",eName = "CAUTION")
    private String caution;

    @MetadataDescribe(id= "LHSD0011042",name = "一级药理作用",eName = "PHY_FUNCTION1")
    private String phyFunction1;

    @MetadataDescribe(id= "LHSD0011043",name = "二级药理作用",eName = "PHY_FUNCTION2")
    private String phyFunction2;

    @MetadataDescribe(id= "LHSD0011044",name = "三级药理作用",eName = "PHY_FUNCTION3")
    private String phyFunction3;

    @MetadataDescribe(id= "LHSD0011045",name = "有效性标志",eName = "VALID_STATE")
    private String validState;

    @MetadataDescribe(id= "LHSD0011046",name = "自制标志",eName = "SELF_FLAG")
    private String selfFlag;

    @MetadataDescribe(id= "LHSD0011047",name = "OCT标志",eName = "OCT_FLAG")
    private String octFlag;

    @MetadataDescribe(id= "LHSD0011048",name = "GMP标志",eName = "GMP_FLAG")
    private String gmpFlag;

    @MetadataDescribe(id= "LHSD0011049",name = "皮试标志",eName = "TEST_FLAG")
    private String testFlag;

    @MetadataDescribe(id= "LHSD0011050",name = "新药标记",eName = "NEW_FLAG")
    private String newFlag;

    @MetadataDescribe(id= "LHSD0011051",name = "附材标志",eName = "APPEND_FLAG")
    private String appendFlag;

    @MetadataDescribe(id= "LHSD0011052",name = "缺药标志",eName = "LACK_FLAG")
    private String lackFlag;

    @MetadataDescribe(id= "LHSD0011053",name = "大屏幕显示标记",eName = "SHOW_FLAG")
    private String showFlag;

    @MetadataDescribe(id= "LHSD0011054",name = "招标标志",eName = "TENDER_FLAG")
    private String tenderFlag;

    @MetadataDescribe(id= "LHSD0011055",name = "招标价",eName = "TENDER_PRICE")
    private String tenderPrice;

    @MetadataDescribe(id= "LHSD0011056",name = "中标公司",eName = "TENDER_COMPANY")
    private String tenderCompany;

    @MetadataDescribe(id= "LHSD0011057",name = "中标开始日期",eName = "TENDER_BEGINDATE")
    private String tenderBegindate;

    @MetadataDescribe(id= "LHSD0011058",name = "中标结束日期",eName = "TENDER_ENDDATE")
    private String tenderEnddate;

    @MetadataDescribe(id= "LHSD0011059",name = "最新供药公司",eName = "COMPANY_CODE")
    private String companyCode;

    @MetadataDescribe(id= "LHSD0011060",name = "价格形式",eName = "PRICE_FORM")
    private String priceForm;

    @MetadataDescribe(id= "LHSD0011061",name = "招标采购合同编号",eName = "CONTRACT_CODE")
    private String contractCode;

    @MetadataDescribe(id= "LHSD0011062",name = "产地",eName = "PRODUCING_AREA")
    private String producingArea;

    @MetadataDescribe(id= "LHSD0011063",name = "生产厂家",eName = "PRODUCER_CODE")
    private String producerCode;

    @MetadataDescribe(id= "LHSD0011064",name = "批文信息",eName = "APPROVE_INFO")
    private String approveInfo;

    @MetadataDescribe(id= "LHSD0011065",name = "商标",eName = "LABEL")
    private String label;

    @MetadataDescribe(id= "LHSD0011066",name = "有效成分",eName = "INGREDIENT")
    private String ingredient;

    @MetadataDescribe(id= "LHSD0011067",name = "执行标准",eName = "EXECUTE_STANDARD")
    private String executeStandard;

    @MetadataDescribe(id= "LHSD0011068",name = "储藏条件",eName = "STORE_CONDITION")
    private String storeCondition;

    @MetadataDescribe(id= "LHSD0011069",name = "药品简介",eName = "BRIEF_INTRODUCTION")
    private String briefIntroduction;

    @MetadataDescribe(id= "LHSD0011070",name = "说明书内容",eName = "MANUAL")
    private String manual;

    @MetadataDescribe(id= "LHSD0011071",name = "条形码",eName = "BAR_CODE")
    private String barCode;

    @MetadataDescribe(id= "LHSD0011072",name = "旧系统药品编码",eName = "OLD_DRUG_CODE")
    private String oldDrugCode;

    @MetadataDescribe(id= "LHSD0011073",name = "备注",eName = "MARK")
    private String mark;

    @MetadataDescribe(id= "LHSD0011074",name = "操作员编码",eName = "OPER_CODE")
    private String operCode;

    @MetadataDescribe(id= "LHSD0011075",name = "操作时间",eName = "OPER_DATE")
    private String operDate;

    @MetadataDescribe(id= "LHSD0011076",name = "省限制",eName = "SPECIAL_FLAG")
    private String specialFlag;

    @MetadataDescribe(id= "LHSD0011077",name = "市限制",eName = "SPECIAL_FLAG1")
    private String specialFlag1;

    @MetadataDescribe(id= "LHSD0011078",name = "自费项目",eName = "SPECIAL_FLAG2")
    private String specialFlag2;

    @MetadataDescribe(id= "LHSD0011079",name = "特限药品标记",eName = "SPECIAL_FLAG3")
    private String specialFlag3;

    @MetadataDescribe(id= "LHSD0011080",name = "特殊标记",eName = "SPECIAL_FLAG4")
    private String specialFlag4;

    @MetadataDescribe(id= "LHSD0011081",name = "变动类型",eName = "SHIFT_TYPE")
    private String shiftType;

    @MetadataDescribe(id= "LHSD0011082",name = "变动时间",eName = "SHIFT_DATE")
    private String shiftDate;

    @MetadataDescribe(id= "LHSD0011083",name = "变动原因",eName = "SHIFT_MARK")
    private String shiftMark;

    @MetadataDescribe(id= "LHSD0011084",name = "药品外观图片",eName = "TRADE_PICTURE")
    private String tradePicture;

    @MetadataDescribe(id= "LHSD0011085",name = "拆分类型",eName = "SPLIT_TYPE")
    private String splitType;

    @MetadataDescribe(id= "LHSD0011086",name = "协定处方标志",eName = "NOSTRUM_FLAG")
    private String nostrumFlag;


    @MetadataDescribe(id= "LHSD0011091",name = "建立时间",eName = "CREATE_TIME")
    private String createTime;

    @MetadataDescribe(id= "LHSD0011096",name = "住院长嘱拆分",eName = "CDSPLIT_TYPE")
    private String cdsplitType;

    @MetadataDescribe(id= "LHSD0011097",name = "住院临瞩拆分",eName = "LZSPLIT_TYPE")
    private String lzsplitType;

    @MetadataDescribe(id= "LHSD0011101",name = "抗菌药物对应编码",eName = "ATC_NO")
    private String atcNo;

    @MetadataDescribe(id= "LHSD0011102",name = "使用范围",eName = "USESCOPE")
    private String usescope;

    @MetadataDescribe(id= "LHSD0011103",name = "大包装单位",eName = "BIGPACKUNIT")
    private String bigpackunit;

    @MetadataDescribe(id= "LHSD0011104",name = "大包装数量",eName = "BIGPACKQTY")
    private String bigpackqty;

    @MetadataDescribe(id= "LHSD0011106",name = "医生级别",eName = "DOCTOR_GRADE")
    private String doctorGrade;

    @MetadataDescribe(id= "LHSD0011107",name = "静配标记",eName = "PIVAS_FORCE_FLAG")
    private String pivasForceFlag;

    @MetadataDescribe(id= "LHSD0011108",name = "静配类型",eName = "PIVAS_DRUG_TYPE")
    private String pivasDrugType;

    @MetadataDescribe(id= "LHSD0011109",name = "机构/院部代码",eName = "ORG_CODE")
    private String orgCode;

    @MetadataDescribe(id= "LHSD0011109",name = "计生药品标识",eName = "PLAN_FLAG")
    private String planFlag;

    @MetadataDescribe(id= "LHSD0011109",name = "孕妇禁用药品标识",eName = "PREGANT_FLAG")
    private String pregantFlag;

    @MetadataDescribe(id= "LHSD0011109",name = "儿童禁用药品标识",eName = "CHILDREN_FLAG")
    private String childrenFlag;

    @MetadataDescribe(id= "LHSD0011109",name = "高危药品标识",eName = "DANGER_FLAG")
    private String dangerFlag;

    @MetadataDescribe(id= "LHSD0011109",name = "抗肿瘤药物标识",eName = "ANTITUMOR_FLAG")
    private String antitumorFlag;

    public String getDrugCode() {
        return drugCode;
    }

    public void setDrugCode(String drugCode) {
        this.drugCode = drugCode;
    }

    public String getTradeName() {
        return tradeName;
    }

    public void setTradeName(String tradeName) {
        this.tradeName = tradeName;
    }

    public String getSpellCode() {
        return spellCode;
    }

    public void setSpellCode(String spellCode) {
        this.spellCode = spellCode;
    }

    public String getWbCode() {
        return wbCode;
    }

    public void setWbCode(String wbCode) {
        this.wbCode = wbCode;
    }

    public String getRegularName() {
        return regularName;
    }

    public void setRegularName(String regularName) {
        this.regularName = regularName;
    }

    public String getRegularSpell() {
        return regularSpell;
    }

    public void setRegularSpell(String regularSpell) {
        this.regularSpell = regularSpell;
    }

    public String getRegularWb() {
        return regularWb;
    }

    public void setRegularWb(String regularWb) {
        this.regularWb = regularWb;
    }

    public String getRegularCustom() {
        return regularCustom;
    }

    public void setRegularCustom(String regularCustom) {
        this.regularCustom = regularCustom;
    }

    public String getFormalName() {
        return formalName;
    }

    public void setFormalName(String formalName) {
        this.formalName = formalName;
    }

    public String getFormalSpell() {
        return formalSpell;
    }

    public void setFormalSpell(String formalSpell) {
        this.formalSpell = formalSpell;
    }

    public String getFormalWb() {
        return formalWb;
    }

    public void setFormalWb(String formalWb) {
        this.formalWb = formalWb;
    }

    public String getFormalCustom() {
        return formalCustom;
    }

    public void setFormalCustom(String formalCustom) {
        this.formalCustom = formalCustom;
    }

    public String getOtherName() {
        return otherName;
    }

    public void setOtherName(String otherName) {
        this.otherName = otherName;
    }

    public String getOtherSpell() {
        return otherSpell;
    }

    public void setOtherSpell(String otherSpell) {
        this.otherSpell = otherSpell;
    }

    public String getOtherWb() {
        return otherWb;
    }

    public void setOtherWb(String otherWb) {
        this.otherWb = otherWb;
    }

    public String getOtherCustom() {
        return otherCustom;
    }

    public void setOtherCustom(String otherCustom) {
        this.otherCustom = otherCustom;
    }

    public String getEnglishRegular() {
        return englishRegular;
    }

    public void setEnglishRegular(String englishRegular) {
        this.englishRegular = englishRegular;
    }

    public String getEnglishOther() {
        return englishOther;
    }

    public void setEnglishOther(String englishOther) {
        this.englishOther = englishOther;
    }

    public String getEnglishName() {
        return englishName;
    }

    public void setEnglishName(String englishName) {
        this.englishName = englishName;
    }

    public String getInternationalCode() {
        return internationalCode;
    }

    public void setInternationalCode(String internationalCode) {
        this.internationalCode = internationalCode;
    }

    public String getGbCode() {
        return gbCode;
    }

    public void setGbCode(String gbCode) {
        this.gbCode = gbCode;
    }

    public String getClassCode() {
        return classCode;
    }

    public void setClassCode(String classCode) {
        this.classCode = classCode;
    }

    public String getFeeCode() {
        return feeCode;
    }

    public void setFeeCode(String feeCode) {
        this.feeCode = feeCode;
    }

    public String getDrugType() {
        return drugType;
    }

    public void setDrugType(String drugType) {
        this.drugType = drugType;
    }

    public String getDrugQuality() {
        return drugQuality;
    }

    public void setDrugQuality(String drugQuality) {
        this.drugQuality = drugQuality;
    }

    public String getItemGrade() {
        return itemGrade;
    }

    public void setItemGrade(String itemGrade) {
        this.itemGrade = itemGrade;
    }

    public String getSpecs() {
        return specs;
    }

    public void setSpecs(String specs) {
        this.specs = specs;
    }

    public String getRetailPrice() {
        return retailPrice;
    }

    public void setRetailPrice(String retailPrice) {
        this.retailPrice = retailPrice;
    }

    public String getWholesalePrice() {
        return wholesalePrice;
    }

    public void setWholesalePrice(String wholesalePrice) {
        this.wholesalePrice = wholesalePrice;
    }

    public String getPurchasePrice() {
        return purchasePrice;
    }

    public void setPurchasePrice(String purchasePrice) {
        this.purchasePrice = purchasePrice;
    }

    public String getTopRetailprice() {
        return topRetailprice;
    }

    public void setTopRetailprice(String topRetailprice) {
        this.topRetailprice = topRetailprice;
    }

    public String getPackUnitl() {
        return packUnitl;
    }

    public void setPackUnitl(String packUnitl) {
        this.packUnitl = packUnitl;
    }

    public String getPackQty() {
        return packQty;
    }

    public void setPackQty(String packQty) {
        this.packQty = packQty;
    }

    public String getMinUnit() {
        return minUnit;
    }

    public void setMinUnit(String minUnit) {
        this.minUnit = minUnit;
    }

    public String getDoseModelCode() {
        return doseModelCode;
    }

    public void setDoseModelCode(String doseModelCode) {
        this.doseModelCode = doseModelCode;
    }

    public String getBaseDose() {
        return baseDose;
    }

    public void setBaseDose(String baseDose) {
        this.baseDose = baseDose;
    }

    public String getDoseUnit() {
        return doseUnit;
    }

    public void setDoseUnit(String doseUnit) {
        this.doseUnit = doseUnit;
    }

    public String getUsageCode() {
        return usageCode;
    }

    public void setUsageCode(String usageCode) {
        this.usageCode = usageCode;
    }

    public String getFrequencyCode() {
        return frequencyCode;
    }

    public void setFrequencyCode(String frequencyCode) {
        this.frequencyCode = frequencyCode;
    }

    public String getOnceDose() {
        return onceDose;
    }

    public void setOnceDose(String onceDose) {
        this.onceDose = onceDose;
    }

    public String getCaution() {
        return caution;
    }

    public void setCaution(String caution) {
        this.caution = caution;
    }

    public String getPhyFunction1() {
        return phyFunction1;
    }

    public void setPhyFunction1(String phyFunction1) {
        this.phyFunction1 = phyFunction1;
    }

    public String getPhyFunction2() {
        return phyFunction2;
    }

    public void setPhyFunction2(String phyFunction2) {
        this.phyFunction2 = phyFunction2;
    }

    public String getPhyFunction3() {
        return phyFunction3;
    }

    public void setPhyFunction3(String phyFunction3) {
        this.phyFunction3 = phyFunction3;
    }

    public String getValidState() {
        return validState;
    }

    public void setValidState(String validState) {
        this.validState = validState;
    }

    public String getSelfFlag() {
        return selfFlag;
    }

    public void setSelfFlag(String selfFlag) {
        this.selfFlag = selfFlag;
    }

    public String getOctFlag() {
        return octFlag;
    }

    public void setOctFlag(String octFlag) {
        this.octFlag = octFlag;
    }

    public String getGmpFlag() {
        return gmpFlag;
    }

    public void setGmpFlag(String gmpFlag) {
        this.gmpFlag = gmpFlag;
    }

    public String getTestFlag() {
        return testFlag;
    }

    public void setTestFlag(String testFlag) {
        this.testFlag = testFlag;
    }

    public String getNewFlag() {
        return newFlag;
    }

    public void setNewFlag(String newFlag) {
        this.newFlag = newFlag;
    }

    public String getAppendFlag() {
        return appendFlag;
    }

    public void setAppendFlag(String appendFlag) {
        this.appendFlag = appendFlag;
    }

    public String getLackFlag() {
        return lackFlag;
    }

    public void setLackFlag(String lackFlag) {
        this.lackFlag = lackFlag;
    }

    public String getShowFlag() {
        return showFlag;
    }

    public void setShowFlag(String showFlag) {
        this.showFlag = showFlag;
    }

    public String getTenderFlag() {
        return tenderFlag;
    }

    public void setTenderFlag(String tenderFlag) {
        this.tenderFlag = tenderFlag;
    }

    public String getTenderPrice() {
        return tenderPrice;
    }

    public void setTenderPrice(String tenderPrice) {
        this.tenderPrice = tenderPrice;
    }

    public String getTenderCompany() {
        return tenderCompany;
    }

    public void setTenderCompany(String tenderCompany) {
        this.tenderCompany = tenderCompany;
    }

    public String getTenderBegindate() {
        return tenderBegindate;
    }

    public void setTenderBegindate(String tenderBegindate) {
        this.tenderBegindate = tenderBegindate;
    }

    public String getTenderEnddate() {
        return tenderEnddate;
    }

    public void setTenderEnddate(String tenderEnddate) {
        this.tenderEnddate = tenderEnddate;
    }

    public String getCompanyCode() {
        return companyCode;
    }

    public void setCompanyCode(String companyCode) {
        this.companyCode = companyCode;
    }

    public String getPriceForm() {
        return priceForm;
    }

    public void setPriceForm(String priceForm) {
        this.priceForm = priceForm;
    }

    public String getContractCode() {
        return contractCode;
    }

    public void setContractCode(String contractCode) {
        this.contractCode = contractCode;
    }

    public String getProducingArea() {
        return producingArea;
    }

    public void setProducingArea(String producingArea) {
        this.producingArea = producingArea;
    }

    public String getProducerCode() {
        return producerCode;
    }

    public void setProducerCode(String producerCode) {
        this.producerCode = producerCode;
    }

    public String getApproveInfo() {
        return approveInfo;
    }

    public void setApproveInfo(String approveInfo) {
        this.approveInfo = approveInfo;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getIngredient() {
        return ingredient;
    }

    public void setIngredient(String ingredient) {
        this.ingredient = ingredient;
    }

    public String getExecuteStandard() {
        return executeStandard;
    }

    public void setExecuteStandard(String executeStandard) {
        this.executeStandard = executeStandard;
    }

    public String getStoreCondition() {
        return storeCondition;
    }

    public void setStoreCondition(String storeCondition) {
        this.storeCondition = storeCondition;
    }

    public String getBriefIntroduction() {
        return briefIntroduction;
    }

    public void setBriefIntroduction(String briefIntroduction) {
        this.briefIntroduction = briefIntroduction;
    }

    public String getManual() {
        return manual;
    }

    public void setManual(String manual) {
        this.manual = manual;
    }

    public String getBarCode() {
        return barCode;
    }

    public void setBarCode(String barCode) {
        this.barCode = barCode;
    }

    public String getOldDrugCode() {
        return oldDrugCode;
    }

    public void setOldDrugCode(String oldDrugCode) {
        this.oldDrugCode = oldDrugCode;
    }

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }

    public String getOperCode() {
        return operCode;
    }

    public void setOperCode(String operCode) {
        this.operCode = operCode;
    }

    public String getOperDate() {
        return operDate;
    }

    public void setOperDate(String operDate) {
        this.operDate = operDate;
    }

    public String getSpecialFlag() {
        return specialFlag;
    }

    public void setSpecialFlag(String specialFlag) {
        this.specialFlag = specialFlag;
    }

    public String getSpecialFlag1() {
        return specialFlag1;
    }

    public void setSpecialFlag1(String specialFlag1) {
        this.specialFlag1 = specialFlag1;
    }

    public String getSpecialFlag2() {
        return specialFlag2;
    }

    public void setSpecialFlag2(String specialFlag2) {
        this.specialFlag2 = specialFlag2;
    }

    public String getSpecialFlag3() {
        return specialFlag3;
    }

    public void setSpecialFlag3(String specialFlag3) {
        this.specialFlag3 = specialFlag3;
    }

    public String getSpecialFlag4() {
        return specialFlag4;
    }

    public void setSpecialFlag4(String specialFlag4) {
        this.specialFlag4 = specialFlag4;
    }

    public String getShiftType() {
        return shiftType;
    }

    public void setShiftType(String shiftType) {
        this.shiftType = shiftType;
    }

    public String getShiftDate() {
        return shiftDate;
    }

    public void setShiftDate(String shiftDate) {
        this.shiftDate = shiftDate;
    }

    public String getShiftMark() {
        return shiftMark;
    }

    public void setShiftMark(String shiftMark) {
        this.shiftMark = shiftMark;
    }

    public String getTradePicture() {
        return tradePicture;
    }

    public void setTradePicture(String tradePicture) {
        this.tradePicture = tradePicture;
    }

    public String getSplitType() {
        return splitType;
    }

    public void setSplitType(String splitType) {
        this.splitType = splitType;
    }

    public String getNostrumFlag() {
        return nostrumFlag;
    }

    public void setNostrumFlag(String nostrumFlag) {
        this.nostrumFlag = nostrumFlag;
    }


    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }



    public String getRetailPrice2() {
        return retailPrice2;
    }

    public void setRetailPrice2(String retailPrice2) {
        this.retailPrice2 = retailPrice2;
    }


    public String getCdsplitType() {
        return cdsplitType;
    }

    public void setCdsplitType(String cdsplitType) {
        this.cdsplitType = cdsplitType;
    }

    public String getLzsplitType() {
        return lzsplitType;
    }

    public void setLzsplitType(String lzsplitType) {
        this.lzsplitType = lzsplitType;
    }

    public String getSecondBaseDose() {
        return secondBaseDose;
    }

    public void setSecondBaseDose(String secondBaseDose) {
        this.secondBaseDose = secondBaseDose;
    }

    public String getSecondDoseUnit() {
        return secondDoseUnit;
    }

    public void setSecondDoseUnit(String secondDoseUnit) {
        this.secondDoseUnit = secondDoseUnit;
    }

    public String getOnceDoseUnit() {
        return onceDoseUnit;
    }

    public void setOnceDoseUnit(String onceDoseUnit) {
        this.onceDoseUnit = onceDoseUnit;
    }

    public String getAtcNo() {
        return atcNo;
    }

    public void setAtcNo(String atcNo) {
        this.atcNo = atcNo;
    }

    public String getUsescope() {
        return usescope;
    }

    public void setUsescope(String usescope) {
        this.usescope = usescope;
    }

    public String getBigpackunit() {
        return bigpackunit;
    }

    public void setBigpackunit(String bigpackunit) {
        this.bigpackunit = bigpackunit;
    }

    public String getBigpackqty() {
        return bigpackqty;
    }

    public void setBigpackqty(String bigpackqty) {
        this.bigpackqty = bigpackqty;
    }

    public String getRetailPrice1() {
        return retailPrice1;
    }

    public void setRetailPrice1(String retailPrice1) {
        this.retailPrice1 = retailPrice1;
    }

    public String getDoctorGrade() {
        return doctorGrade;
    }

    public void setDoctorGrade(String doctorGrade) {
        this.doctorGrade = doctorGrade;
    }

    public String getPivasForceFlag() {
        return pivasForceFlag;
    }

    public void setPivasForceFlag(String pivasForceFlag) {
        this.pivasForceFlag = pivasForceFlag;
    }

    public String getPivasDrugType() {
        return pivasDrugType;
    }

    public void setPivasDrugType(String pivasDrugType) {
        this.pivasDrugType = pivasDrugType;
    }

    public String getOrgCode() {
        return orgCode;
    }

    public void setOrgCode(String orgCode) {
        this.orgCode = orgCode;
    }

    public String getTradeCustomCode() {
        return tradeCustomCode;
    }

    public void setTradeCustomCode(String tradeCustomCode) {
        this.tradeCustomCode = tradeCustomCode;
    }

    public String getPlanFlag() {
        return planFlag;
    }

    public void setPlanFlag(String planFlag) {
        this.planFlag = planFlag;
    }

    public String getPregantFlag() {
        return pregantFlag;
    }

    public void setPregantFlag(String pregantFlag) {
        this.pregantFlag = pregantFlag;
    }

    public String getChildrenFlag() {
        return childrenFlag;
    }

    public void setChildrenFlag(String childrenFlag) {
        this.childrenFlag = childrenFlag;
    }

    public String getDangerFlag() {
        return dangerFlag;
    }

    public void setDangerFlag(String dangerFlag) {
        this.dangerFlag = dangerFlag;
    }

    public String getAntitumorFlag() {
        return antitumorFlag;
    }

    public void setAntitumorFlag(String antitumorFlag) {
        this.antitumorFlag = antitumorFlag;
    }
}
