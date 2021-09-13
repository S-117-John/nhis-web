package com.zebone.nhis.ma.pub.zsrm.vo;

import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 *
 **/
@Component
@Table(value="atf_ypxx")
public class AtfYpxx {

    @Field(value = "inpatient_no")
    private String inpatientNo;//住院号
    @Field(value = "p_id")
    private String pId;//病人ID
    @Field(value = "name")
    private String name;//患者姓名
    @Field(value = "dept_sn")
    private String deptSn;//科室编码
    @Field(value = "dept_name")
    private String deptName;//科室名称
    @Field(value = "ward_sn")
    private String wardSn;//病区编码
    @Field(value = "ward_name")
    private String wardName;//病区名称
    @Field(value = "bed_no")
    private String bedNo;//床号
    @Field(value = "doctor")
    private String doctor;//医生
    @Field(value = "comment")
    private String comment;//服药方法
    @Field(value = "comm2")
    private String comm2;//医嘱嘱托
    @Field(value = "drug_code")
    private String drugCode;//药品编码
    @Field(value = "drugname")
    private String drugname;//药品名称
    @Field(value = "specification")
    private String specification;//药品规格
    @Field(value = "drug_spec")
    private String drugSpec;//药品规格
    @Field(value = "dos_per")
    private String dosPer;//剂量
    @Field(value = "dos_per_units")
    private String dosPerUnits;//剂量单位
    @Field(value = "dosage")
    private double dosage;//单次剂量
    @Field(value = "dos_unit")
    private String dosUnit;//单位
    @Field(value = "amount")
    private double amount;//服用数量
    @Field(value = "total")
    private double total;//每日总量
    @Field(value = "occ_time")
    private Date occTime;//服药时间
    @Field(value = "flag")
    private String flag;//处理标志:0原始数据1已经生成数据文件3重新生成
    @Field(value = "atf_no")
    private String atfNo;//送往ATF机器的编号，比如1号机、2号机
    @Field(value = "pri_flag")
    private String priFlag;//优先级0普通1优先
    @Field(value = "page_no")
    private String pageNo;//单号,本次发药的唯一标识
    @Field(value = "detail_sn")
    private String detailSn;//明细编号
    @Field(value = "gen_time")
    private Date genTime;//生成包药机数据文件的时间
    @Field(value = "mz_flag")
    private String mzFlag;//住院０门诊为１
    public List<AtfYpxx> getAtfYpxxByDetail(List<AtfYpxxDetailVo> atfYpxxDetailVos){
        List<AtfYpxx> atfYpxxList=new LinkedList<>();
        for (AtfYpxxDetailVo atfYpxxDetailVo : atfYpxxDetailVos){
            AtfYpxx atfYpxx=new AtfYpxx();
            atfYpxx.inpatientNo=atfYpxxDetailVo.getInpatientNo();
            atfYpxx.pId=atfYpxxDetailVo.getPatientId();
            atfYpxx.name=atfYpxxDetailVo.getPatientName();
            atfYpxx.deptSn=atfYpxxDetailVo.getCodeDept();
            atfYpxx.deptName=atfYpxxDetailVo.getNameDept();
            atfYpxx.wardSn=atfYpxxDetailVo.getWardSn();
            atfYpxx.wardName=atfYpxxDetailVo.getWardName();
            atfYpxx.bedNo=atfYpxxDetailVo.getBedNo();
            atfYpxx.doctor=atfYpxxDetailVo.getDoctor();
            atfYpxx.comment=atfYpxxDetailVo.getComment1();
            atfYpxx.comm2=atfYpxxDetailVo.getComment2();
            atfYpxx.drugCode=atfYpxxDetailVo.getDrugCode();
            atfYpxx.drugname=atfYpxxDetailVo.getDrugName();
            atfYpxx.specification=atfYpxxDetailVo.getSpecification();
            atfYpxx.drugSpec="待定";
            atfYpxx.dosPer= String.valueOf(atfYpxxDetailVo.getDosPer());
            atfYpxx.dosPerUnits=atfYpxxDetailVo.getDosPerUnits();
            atfYpxx.dosage=atfYpxxDetailVo.getDosage();
            atfYpxx.dosUnit=atfYpxxDetailVo.getDosUnit();
            atfYpxx.amount=atfYpxxDetailVo.getAmount();
            atfYpxx.total=atfYpxxDetailVo.getTotal();
            atfYpxx.occTime=atfYpxxDetailVo.getOccTime();
            atfYpxx.flag="0";
            atfYpxx.atfNo="1";
            atfYpxx.priFlag="0";
            atfYpxx.pageNo=atfYpxxDetailVo.getPageNo();
            atfYpxx.genTime=new Date();
            atfYpxx.detailSn=atfYpxxDetailVo.getDetailSn();
            if ("3".equals(atfYpxxDetailVo.getPatientType())){
                atfYpxx.mzFlag=atfYpxxDetailVo.getPatientType();
            }
            atfYpxxList.add(atfYpxx);
        }

        return atfYpxxList;
    }

    public String getInpatientNo() {
        return inpatientNo;
    }

    public void setInpatientNo(String inpatientNo) {
        this.inpatientNo = inpatientNo;
    }

    public String getpId() {
        return pId;
    }

    public void setpId(String pId) {
        this.pId = pId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDeptSn() {
        return deptSn;
    }

    public void setDeptSn(String deptSn) {
        this.deptSn = deptSn;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getWardSn() {
        return wardSn;
    }

    public void setWardSn(String wardSn) {
        this.wardSn = wardSn;
    }

    public String getWardName() {
        return wardName;
    }

    public void setWardName(String wardName) {
        this.wardName = wardName;
    }

    public String getBedNo() {
        return bedNo;
    }

    public void setBedNo(String bedNo) {
        this.bedNo = bedNo;
    }

    public String getDoctor() {
        return doctor;
    }

    public void setDoctor(String doctor) {
        this.doctor = doctor;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getComm2() {
        return comm2;
    }

    public void setComm2(String comm2) {
        this.comm2 = comm2;
    }

    public String getDrugCode() {
        return drugCode;
    }

    public void setDrugCode(String drugCode) {
        this.drugCode = drugCode;
    }

    public String getDrugname() {
        return drugname;
    }

    public void setDrugname(String drugname) {
        this.drugname = drugname;
    }

    public String getSpecification() {
        return specification;
    }

    public void setSpecification(String specification) {
        this.specification = specification;
    }

    public String getDrugSpec() {
        return drugSpec;
    }

    public void setDrugSpec(String drugSpec) {
        this.drugSpec = drugSpec;
    }

    public String getDosPer() {
        return dosPer;
    }

    public void setDosPer(String dosPer) {
        this.dosPer = dosPer;
    }

    public String getDosPerUnits() {
        return dosPerUnits;
    }

    public void setDosPerUnits(String dosPerUnits) {
        this.dosPerUnits = dosPerUnits;
    }

    public double getDosage() {
        return dosage;
    }

    public void setDosage(double dosage) {
        this.dosage = dosage;
    }

    public String getDosUnit() {
        return dosUnit;
    }

    public void setDosUnit(String dosUnit) {
        this.dosUnit = dosUnit;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public Date getOccTime() {
        return occTime;
    }

    public void setOccTime(Date occTime) {
        this.occTime = occTime;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getAtfNo() {
        return atfNo;
    }

    public void setAtfNo(String atfNo) {
        this.atfNo = atfNo;
    }

    public String getPriFlag() {
        return priFlag;
    }

    public void setPriFlag(String priFlag) {
        this.priFlag = priFlag;
    }

    public String getPageNo() {
        return pageNo;
    }

    public void setPageNo(String pageNo) {
        this.pageNo = pageNo;
    }

    public String getDetailSn() {
        return detailSn;
    }

    public void setDetailSn(String detailSn) {
        this.detailSn = detailSn;
    }

    public Date getGenTime() {
        return genTime;
    }

    public void setGenTime(Date genTime) {
        this.genTime = genTime;
    }

    public String getMzFlag() {
        return mzFlag;
    }

    public void setMzFlag(String mzFlag) {
        this.mzFlag = mzFlag;
    }
}
