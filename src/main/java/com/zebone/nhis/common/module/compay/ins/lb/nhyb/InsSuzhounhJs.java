package com.zebone.nhis.common.module.compay.ins.lb.nhyb;

import java.util.Date;
import java.util.List;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: INS_SUZHOUNH_JS 
 *
 * @since 2018-07-20 07:42:18
 */
@Table(value="INS_SUZHOUNH_JS")
public class InsSuzhounhJs extends BaseModule  {

    /** ID - 主键 */
	@PK
	@Field(value="ID",id=KeyId.UUID)
    private String id;

    /** YLFZE - YLFZE-医疗费总额 */
	@Field(value="YLFZE")
    private String ylfze;

    /** JJZF - JJZF-基金支付 */
	@Field(value="JJZF")
    private String jjzf;

    /** XJZF - XJZF-现金支付 */
	@Field(value="XJZF")
    private String xjzf;

    /** GRZF - GRZF-个人支付 */
	@Field(value="GRZF")
    private String grzf;

    /** QFJE - QFJE-起付金额 */
	@Field(value="QFJE")
    private String qfje;

    /** ZHZF - ZHZF-帐户支付 */
	@Field(value="ZHZF")
    private String zhzf;

    /** BNDJJLJZF - BNDJJLJZF-本年度基金累计支付 */
	@Field(value="BNDJJLJZF")
    private String bndjjljzf;

    /** BCZFQZHYE - BCZFQZHYE-本次支付前帐户余额 */
	@Field(value="BCZFQZHYE")
    private String bczfqzhye;

    /** KBXZJE - KBXZJE-可报销总金额 */
	@Field(value="KBXZJE")
    private String kbxzje;

    /** BCZFHZHYE - BCZFHZHYE-本次支付后帐户余额 */
	@Field(value="BCZFHZHYE")
    private String bczfhzhye;

    /** ZFJE - ZFJE-自费金额 */
	@Field(value="ZFJE")
    private String zfje;

    /** ZFBLJE - ZFBLJE-自付比例金额 */
	@Field(value="ZFBLJE")
    private String zfblje;

    /** ZXDJH - ZXDJH-中心单据号 */
	@Field(value="ZXDJH")
    private String zxdjh;

    /** HZMC - HZMC-户主名称 */
	@Field(value="HZMC")
    private String hzmc;

    /** WYJCZFY - WYJCZFY-外院检查总费用 */
	@Field(value="WYJCZFY")
    private String wyjczfy;

    /** WYJCBCFY - WYJCBCFY-外院检查补偿费用 */
	@Field(value="WYJCBCFY")
    private String wyjcbcfy;

    /** WYJCKBXFY - WYJCKBXFY-外院检查可报销费用 */
	@Field(value="WYJCKBXFY")
    private String wyjckbxfy;

    /** BFJTCXMZJE - BFJTCXMZJE-部分进统筹项目总金额 */
	@Field(value="BFJTCXMZJE")
    private String bfjtcxmzje;

    /** ZYXMKBXJE - ZYXMKBXJE-中医项目可报销金额 */
	@Field(value="ZYXMKBXJE")
    private String zyxmkbxje;

    /** ZYXMBXJE - ZYXMBXJE-中医项目报销金额 */
	@Field(value="ZYXMBXJE")
    private String zyxmbxje;

    /** JBYWJE - JBYWJE-基本药物金额 */
	@Field(value="JBYWJE")
    private String jbywje;

    /** JBYWBXJE - JBYWBXJE-基本药物报销金额 */
	@Field(value="JBYWBXJE")
    private String jbywbxje;

    /** DBZFYDE - DBZFYDE-单病种费用定额 */
	@Field(value="DBZFYDE")
    private String dbzfyde;

    /** YLJGCDFY - YLJGCDFY-医疗机构承担费用 */
	@Field(value="YLJGCDFY")
    private String yljgcdfy;

    /** MZJZBCJE - MZJZBCJE-民政救助补偿金额 */
	@Field(value="MZJZBCJE")
    private String mzjzbcje;

    /** SFBDBC - SFBDBC-是否保底补偿 */
	@Field(value="SFBDBC")
    private String sfbdbc;

    /** YPFY - YPFY-药品费用 */
	@Field(value="YPFY")
    private String ypfy;

    /** KBXYPFY - KBXYPFY-可报销药品费用 */
	@Field(value="KBXYPFY")
    private String kbxypfy;

    /** YBZLFBC - YBZLFBC-一般诊疗费补偿 */
	@Field(value="YBZLFBC")
    private String ybzlfbc;

    /** DBBXBCJE - DBBXBCJE-大病保险补偿金额 */
	@Field(value="DBBXBCJE")
    private String dbbxbcje;

    /** YJYPJE - YJYPJE-预警药品金额 */
	@Field(value="YJYPJE")
    private String yjypje;

    /** PKRKBZ - PKRKBZ-贫困人口标志 */
	@Field(value="PKRKBZ")
    private String pkrkbz;

    /** JHSYTSJTSXBM - JHSYTSJTSXBM-计划生育特殊家庭属性编码 */
	@Field(value="JHSYTSJTSXBM")
    private String jhsytsjtsxbm;

    /** CZDDZJBCJE - CZDDZJBCJE-财政兜底资金补偿金额 */
	@Field(value="CZDDZJBCJE")
    private String czddzjbcje;

    /** QTBCJE - QTBCJE-其他补偿金额 */
	@Field(value="QTBCJE")
    private String qtbcje;

    /** MBBCE - MBBCE-慢病补偿额 */
	@Field(value="MBBCE")
    private String mbbce;

    /** YL1 - YL1-预留1 */
	@Field(value="YL1")
    private String yl1;

    /** YL2 - YL2-预留2 */
	@Field(value="YL2")
    private String yl2;

    /** YL3 - YL3-预留3 */
	@Field(value="YL3")
    private String yl3;

    /** YL4 - YL4-预留4 */
	@Field(value="YL4")
    private String yl4;

    /** MODIFY_TIME - 最后操作时间 */
	@Field(value="MODIFY_TIME")
    private Date modifyTime;

    /** PK_PV - 就诊主键 */
	@Field(value="PK_PV")
    private String pkPv;

    /** PK_SETTLE - 结算主键 */
	@Field(value="PK_SETTLE")
    private String pkSettle;

    /** RECCODE - 就诊号 */
	@Field(value="RECCODE")
    private String reccode;

    /** INPATTYPE - 就诊类型1：门诊2：住院3：转院 */
	@Field(value="INPATTYPE")
    private String inpattype;

    /** EXPENSETYPE - 结算类型0：门诊结算1：正常出院结算3：住院平产4：住院剖腹产9：慢病结算10:特殊慢病门诊结算138：专科慢性病门诊 */
	@Field(value="EXPENSETYPE")
    private String expensetype;

    /** OPERATE - 登记人 */
	@Field(value="OPERATE")
    private String operate;

    /** EXPENSEDATE - 结算时间 */
	@Field(value="EXPENSEDATE")
    private Date expensedate;

    /** REGDATE - 入院时间 */
	@Field(value="REGDATE")
    private Date regdate;

    /** LEAVEDATE - 出院时间 */
	@Field(value="LEAVEDATE")
    private Date leavedate;

    /** DISEASENO1 - 入院主诊断名称 */
	@Field(value="DISEASENO1")
    private String diseaseno1;

    /** LDISEASENO1 - 出院主诊断名称 */
	@Field(value="LDISEASENO1")
    private String ldiseaseno1;

    /** BILLNO - 医院单据号(发票号) */
	@Field(value="BILLNO")
    private String billno;

    /** HOMEPAY - 本次门诊账户递减金额（门诊结算是传） */
	@Field(value="HOMEPAY")
    private String homepay;

    /** CALCTYPE - 结算类别1：预结算2：正式结算 */
	@Field(value="CALCTYPE")
    private String calctype;

    /** LDISEASE1 - 出院主诊断编码 */
	@Field(value="LDISEASE1")
    private String ldisease1;

    /** LREASON - 出院原因 */
	@Field(value="LREASON")
    private String lreason;

    public List<InsSuzhounhJsGrade> getGradeList() {
        return gradeList;
    }

    public void setGradeList(List<InsSuzhounhJsGrade> gradeList) {
        this.gradeList = gradeList;
    }

    /**
     * 农合医保分段计费信息
     */
    private List<InsSuzhounhJsGrade> gradeList;

    public String getId(){
        return this.id;
    }
    public void setId(String id){
        this.id = id;
    }

    public String getYlfze(){
        return this.ylfze;
    }
    public void setYlfze(String ylfze){
        this.ylfze = ylfze;
    }

    public String getJjzf(){
        return this.jjzf;
    }
    public void setJjzf(String jjzf){
        this.jjzf = jjzf;
    }

    public String getXjzf(){
        return this.xjzf;
    }
    public void setXjzf(String xjzf){
        this.xjzf = xjzf;
    }

    public String getGrzf(){
        return this.grzf;
    }
    public void setGrzf(String grzf){
        this.grzf = grzf;
    }

    public String getQfje(){
        return this.qfje;
    }
    public void setQfje(String qfje){
        this.qfje = qfje;
    }

    public String getZhzf(){
        return this.zhzf;
    }
    public void setZhzf(String zhzf){
        this.zhzf = zhzf;
    }

    public String getBndjjljzf(){
        return this.bndjjljzf;
    }
    public void setBndjjljzf(String bndjjljzf){
        this.bndjjljzf = bndjjljzf;
    }

    public String getBczfqzhye(){
        return this.bczfqzhye;
    }
    public void setBczfqzhye(String bczfqzhye){
        this.bczfqzhye = bczfqzhye;
    }

    public String getKbxzje(){
        return this.kbxzje;
    }
    public void setKbxzje(String kbxzje){
        this.kbxzje = kbxzje;
    }

    public String getBczfhzhye(){
        return this.bczfhzhye;
    }
    public void setBczfhzhye(String bczfhzhye){
        this.bczfhzhye = bczfhzhye;
    }

    public String getZfje(){
        return this.zfje;
    }
    public void setZfje(String zfje){
        this.zfje = zfje;
    }

    public String getZfblje(){
        return this.zfblje;
    }
    public void setZfblje(String zfblje){
        this.zfblje = zfblje;
    }

    public String getZxdjh(){
        return this.zxdjh;
    }
    public void setZxdjh(String zxdjh){
        this.zxdjh = zxdjh;
    }

    public String getHzmc(){
        return this.hzmc;
    }
    public void setHzmc(String hzmc){
        this.hzmc = hzmc;
    }

    public String getWyjczfy(){
        return this.wyjczfy;
    }
    public void setWyjczfy(String wyjczfy){
        this.wyjczfy = wyjczfy;
    }

    public String getWyjcbcfy(){
        return this.wyjcbcfy;
    }
    public void setWyjcbcfy(String wyjcbcfy){
        this.wyjcbcfy = wyjcbcfy;
    }

    public String getWyjckbxfy(){
        return this.wyjckbxfy;
    }
    public void setWyjckbxfy(String wyjckbxfy){
        this.wyjckbxfy = wyjckbxfy;
    }

    public String getBfjtcxmzje(){
        return this.bfjtcxmzje;
    }
    public void setBfjtcxmzje(String bfjtcxmzje){
        this.bfjtcxmzje = bfjtcxmzje;
    }

    public String getZyxmkbxje(){
        return this.zyxmkbxje;
    }
    public void setZyxmkbxje(String zyxmkbxje){
        this.zyxmkbxje = zyxmkbxje;
    }

    public String getZyxmbxje(){
        return this.zyxmbxje;
    }
    public void setZyxmbxje(String zyxmbxje){
        this.zyxmbxje = zyxmbxje;
    }

    public String getJbywje(){
        return this.jbywje;
    }
    public void setJbywje(String jbywje){
        this.jbywje = jbywje;
    }

    public String getJbywbxje(){
        return this.jbywbxje;
    }
    public void setJbywbxje(String jbywbxje){
        this.jbywbxje = jbywbxje;
    }

    public String getDbzfyde(){
        return this.dbzfyde;
    }
    public void setDbzfyde(String dbzfyde){
        this.dbzfyde = dbzfyde;
    }

    public String getYljgcdfy(){
        return this.yljgcdfy;
    }
    public void setYljgcdfy(String yljgcdfy){
        this.yljgcdfy = yljgcdfy;
    }

    public String getMzjzbcje(){
        return this.mzjzbcje;
    }
    public void setMzjzbcje(String mzjzbcje){
        this.mzjzbcje = mzjzbcje;
    }

    public String getSfbdbc(){
        return this.sfbdbc;
    }
    public void setSfbdbc(String sfbdbc){
        this.sfbdbc = sfbdbc;
    }

    public String getYpfy(){
        return this.ypfy;
    }
    public void setYpfy(String ypfy){
        this.ypfy = ypfy;
    }

    public String getKbxypfy(){
        return this.kbxypfy;
    }
    public void setKbxypfy(String kbxypfy){
        this.kbxypfy = kbxypfy;
    }

    public String getYbzlfbc(){
        return this.ybzlfbc;
    }
    public void setYbzlfbc(String ybzlfbc){
        this.ybzlfbc = ybzlfbc;
    }

    public String getDbbxbcje(){
        return this.dbbxbcje;
    }
    public void setDbbxbcje(String dbbxbcje){
        this.dbbxbcje = dbbxbcje;
    }

    public String getYjypje(){
        return this.yjypje;
    }
    public void setYjypje(String yjypje){
        this.yjypje = yjypje;
    }

    public String getPkrkbz(){
        return this.pkrkbz;
    }
    public void setPkrkbz(String pkrkbz){
        this.pkrkbz = pkrkbz;
    }

    public String getJhsytsjtsxbm(){
        return this.jhsytsjtsxbm;
    }
    public void setJhsytsjtsxbm(String jhsytsjtsxbm){
        this.jhsytsjtsxbm = jhsytsjtsxbm;
    }

    public String getCzddzjbcje(){
        return this.czddzjbcje;
    }
    public void setCzddzjbcje(String czddzjbcje){
        this.czddzjbcje = czddzjbcje;
    }

    public String getQtbcje(){
        return this.qtbcje;
    }
    public void setQtbcje(String qtbcje){
        this.qtbcje = qtbcje;
    }

    public String getMbbce(){
        return this.mbbce;
    }
    public void setMbbce(String mbbce){
        this.mbbce = mbbce;
    }

    public String getYl1(){
        return this.yl1;
    }
    public void setYl1(String yl1){
        this.yl1 = yl1;
    }

    public String getYl2(){
        return this.yl2;
    }
    public void setYl2(String yl2){
        this.yl2 = yl2;
    }

    public String getYl3(){
        return this.yl3;
    }
    public void setYl3(String yl3){
        this.yl3 = yl3;
    }

    public String getYl4(){
        return this.yl4;
    }
    public void setYl4(String yl4){
        this.yl4 = yl4;
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

    public String getReccode(){
        return this.reccode;
    }
    public void setReccode(String reccode){
        this.reccode = reccode;
    }

    public String getInpattype(){
        return this.inpattype;
    }
    public void setInpattype(String inpattype){
        this.inpattype = inpattype;
    }

    public String getExpensetype(){
        return this.expensetype;
    }
    public void setExpensetype(String expensetype){
        this.expensetype = expensetype;
    }

    public String getOperate(){
        return this.operate;
    }
    public void setOperate(String operate){
        this.operate = operate;
    }

    public Date getExpensedate(){
        return this.expensedate;
    }
    public void setExpensedate(Date expensedate){
        this.expensedate = expensedate;
    }

    public Date getRegdate(){
        return this.regdate;
    }
    public void setRegdate(Date regdate){
        this.regdate = regdate;
    }

    public Date getLeavedate(){
        return this.leavedate;
    }
    public void setLeavedate(Date leavedate){
        this.leavedate = leavedate;
    }

    public String getDiseaseno1(){
        return this.diseaseno1;
    }
    public void setDiseaseno1(String diseaseno1){
        this.diseaseno1 = diseaseno1;
    }

    public String getLdiseaseno1(){
        return this.ldiseaseno1;
    }
    public void setLdiseaseno1(String ldiseaseno1){
        this.ldiseaseno1 = ldiseaseno1;
    }

    public String getBillno(){
        return this.billno;
    }
    public void setBillno(String billno){
        this.billno = billno;
    }

    public String getHomepay(){
        return this.homepay;
    }
    public void setHomepay(String homepay){
        this.homepay = homepay;
    }

    public String getCalctype(){
        return this.calctype;
    }
    public void setCalctype(String calctype){
        this.calctype = calctype;
    }

    public String getLdisease1(){
        return this.ldisease1;
    }
    public void setLdisease1(String ldisease1){
        this.ldisease1 = ldisease1;
    }

    public String getLreason(){
        return this.lreason;
    }
    public void setLreason(String lreason){
        this.lreason = lreason;
    }
}