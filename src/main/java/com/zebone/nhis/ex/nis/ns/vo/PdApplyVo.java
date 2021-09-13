package com.zebone.nhis.ex.nis.ns.vo;

import com.zebone.nhis.common.module.ex.nis.ns.ExPdApply;
import com.zebone.nhis.common.module.ex.nis.ns.ExPdApplyDetail;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 生成请领单模型
 *
 * @author yangxue
 */
public class PdApplyVo {
    private String pkHp;
    private String ordsnParent;
    private String ordsn;
    private String codeOrdtype;
    private String euAlways;//类型
    private String pkUnitBase;//基本包装单位主键
    private String codeFreq;
    private String codeSupply;
    private String medName;//药品名称

    /**
     * 药品编号
     */
    private String pdcode;
    private String spec;        //规格
    private String nameUnitBase;    //基本包装单位名称
    private double price;            //单价
    private double amt;                //金额
    private String pkPdapdt;

    private List<String> pkExLists;
    private String noteSupply;
    private int isCrea = 1;            //1生成，0不生成
    private String flagArr;            //是否有余额
    private int isEnough = 1;            //1充足，0不充足

    private double quan;//数量
    private String pkCnord;
    private String sign;
    private String flagBase;        //基数药标志
    private String flagEmer;        //加急标志
    private String flagMedout;        //出院带药标志
    private String dtDispmode;            //药品发放模式
    private String flagPivas;//静配标志
    private String flagSelf;//自备药标志


    private String bedNo;//床号
    private String namePi;//姓名
    private String namefreq;//频次
    private String namesupply;//用法

    private ExPdApplyDetail apdt;//请领明细
    private ExPdApply ap;//请领单
    private String pkDeptOcc; //执行科室
    private String nameDeptOcc;//执行科室名称
    private String pkDeptExOrd;//医嘱开立时对应的执行科室--切换静配非静配使用
    private String nameDeptExOrd;//医嘱开立时对应的执行科室--切换静配非静配使用
    private String pkDeptOrd;//开立科室--根据临床科室获取静配药房时使用

    private String pkPd;//药品主键
    private Integer packSize;//基本单位对仓库单位的包装量
    private String flagStop;//物品停用
    private String flagStoreStop;//仓库停用
    private String flagOrdStop;//医嘱停止
    private String flagStopChk;//医嘱停止核对
    private Date dateStop;//医嘱停止时间
    private Date datePlan;//计划执行时间
    private String euSt; //皮试标志
    
    private String nameUnitOrd;//order表的用量单位
    private Integer packSizeOrd;//order表的里包装量
   
    //为按天合单使用
    private Map<String,List<String>> pkExMap;

    public Map<String, List<String>> getPkExMap() {
        return pkExMap;
    }

    public void setPkExMap(Map<String, List<String>> pkExMap) {
        this.pkExMap = pkExMap;
    }
    public List<ExPdApplyDetail> getApdtList() {
        return apdtList;
    }

    public void setApdtList(List<ExPdApplyDetail> apdtList) {
        this.apdtList = apdtList;
    }

    private List<ExPdApplyDetail> apdtList;//请领明细

    public String getEuSt() {
        return euSt;
    }

    public void setEuSt(String euSt) {
        this.euSt = euSt;
    }

    public String getOrdsn() {
        return ordsn;
    }

    public void setOrdsn(String ordsn) {
        this.ordsn = ordsn;
    }

    public String getFlagStop() {
        return flagStop;
    }

    public void setFlagStop(String flagStop) {
        this.flagStop = flagStop;
    }

    public String getFlagStoreStop() {
        return flagStoreStop;
    }

    public void setFlagStoreStop(String flagStoreStop) {
        this.flagStoreStop = flagStoreStop;
    }

    public String getPkDeptOrd() {
        return pkDeptOrd;
    }

    public void setPkDeptOrd(String pkDeptOrd) {
        this.pkDeptOrd = pkDeptOrd;
    }

    public String getPkDeptExOrd() {
        return pkDeptExOrd;
    }

    public void setPkDeptExOrd(String pkDeptExOrd) {
        this.pkDeptExOrd = pkDeptExOrd;
    }

    public String getNameDeptExOrd() {
        return nameDeptExOrd;
    }

    public void setNameDeptExOrd(String nameDeptExOrd) {
        this.nameDeptExOrd = nameDeptExOrd;
    }

    public String getFlagPivas() {
        return flagPivas;
    }

    public void setFlagPivas(String flagPivas) {
        this.flagPivas = flagPivas;
    }

    public String getFlagSelf() {
        return flagSelf;
    }

    public void setFlagSelf(String flagSelf) {
        this.flagSelf = flagSelf;
    }

    public String getFlagMedout() {
        return flagMedout;
    }

    public void setFlagMedout(String flagMedout) {
        this.flagMedout = flagMedout;
    }

    public String getFlagEmer() {
        return flagEmer;
    }

    public void setFlagEmer(String flagEmer) {
        this.flagEmer = flagEmer;
    }

    public String getNameDeptOcc() {
        return nameDeptOcc;
    }

    public void setNameDeptOcc(String nameDeptOcc) {
        this.nameDeptOcc = nameDeptOcc;
    }

    public Integer getPackSize() {
        return packSize;
    }

    public void setPackSize(Integer packSize) {
        this.packSize = packSize;
    }

    public String getPkPd() {
        return pkPd;
    }

    public void setPkPd(String pkPd) {
        this.pkPd = pkPd;
    }

    public String getPkDeptOcc() {
        return pkDeptOcc;
    }

    public void setPkDeptOcc(String pkDeptOcc) {
        this.pkDeptOcc = pkDeptOcc;
    }

    public ExPdApply getAp() {
        return ap;
    }

    public void setAp(ExPdApply ap) {
        this.ap = ap;
    }

    public ExPdApplyDetail getApdt() {
        return apdt;
    }

    public void setApdt(ExPdApplyDetail apdt) {
        this.apdt = apdt;
    }

    public List<String> getPkExLists() {
        return pkExLists;
    }

    public void setPkExLists(List<String> pkExLists) {
        this.pkExLists = pkExLists;
    }

    public String getOrdsnParent() {
        return ordsnParent;
    }

    public void setOrdsnParent(String ordsnParent) {
        this.ordsnParent = ordsnParent;
    }

    //	public int getIsArr() {
//		return isArr;
//	}
//	public void setIsArr(int isArr) {
//		this.isArr = isArr;
//	}
    public int getIsEnough() {
        return isEnough;
    }

    public void setIsEnough(int isEnough) {
        this.isEnough = isEnough;
    }

    public String getPkHp() {
        return pkHp;
    }

    public void setPkHp(String pkHp) {
        this.pkHp = pkHp;
    }

    public String getCodeOrdtype() {
        return codeOrdtype;
    }

    public void setCodeOrdtype(String codeOrdtype) {
        this.codeOrdtype = codeOrdtype;
    }

    public String getEuAlways() {
        return euAlways;
    }

    public void setEuAlways(String euAlways) {
        this.euAlways = euAlways;
    }

    public String getPkUnitBase() {
        return pkUnitBase;
    }

    public void setPkUnitBase(String pkUnitBase) {
        this.pkUnitBase = pkUnitBase;
    }

    public String getCodeFreq() {
        return codeFreq;
    }

    public void setCodeFreq(String codeFreq) {
        this.codeFreq = codeFreq;
    }

    public String getCodeSupply() {
        return codeSupply;
    }

    public void setCodeSupply(String codeSupply) {
        this.codeSupply = codeSupply;
    }

    public String getMedName() {
        return medName;
    }

    public void setMedName(String medName) {
        this.medName = medName;
    }

    public String getPdcode() {
        return pdcode;
    }

    public void setPdcode(String pdcode) {
        this.pdcode = pdcode;
    }

    public String getSpec() {
        return spec;
    }

    public void setSpec(String spec) {
        this.spec = spec;
    }

    public String getNameUnitBase() {
        return nameUnitBase;
    }

    public void setNameUnitBase(String nameUnitBase) {
        this.nameUnitBase = nameUnitBase;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getAmt() {
        return amt;
    }

    public void setAmt(double amt) {
        this.amt = amt;
    }

    public String getPkPdapdt() {
        return pkPdapdt;
    }

    public void setPkPdapdt(String pkPdapdt) {
        this.pkPdapdt = pkPdapdt;
    }

    public String getNoteSupply() {
        return noteSupply;
    }

    public void setNoteSupply(String noteSupply) {
        this.noteSupply = noteSupply;
    }

    public int getIsCrea() {
        return isCrea;
    }

    public void setIsCrea(int isCrea) {
        this.isCrea = isCrea;
    }

    public String isFlagArr() {
        return flagArr;
    }

    public void setFlagArr(String flagArr) {
        this.flagArr = flagArr;
    }

    public double getQuan() {
        return quan;
    }

    public void setQuan(double quan) {
        this.quan = quan;
    }

    public String getPkCnord() {
        return pkCnord;
    }

    public void setPkCnord(String pkCnord) {
        this.pkCnord = pkCnord;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getFlagBase() {
        return flagBase;
    }

    public void setFlagBase(String flagBase) {
        this.flagBase = flagBase;
    }

    public String getDtDispmode() {
        return dtDispmode;
    }

    public void setDtDispmode(String dtDispmode) {
        this.dtDispmode = dtDispmode;
    }

    public String getBedNo() {
        return bedNo;
    }

    public void setBedNo(String bedNo) {
        this.bedNo = bedNo;
    }

    public String getNamePi() {
        return namePi;
    }

    public void setNamePi(String namePi) {
        this.namePi = namePi;
    }

    public String getNamefreq() {
        return namefreq;
    }

    public void setNamefreq(String namefreq) {
        this.namefreq = namefreq;
    }

    public String getNamesupply() {
        return namesupply;
    }

    public void setNamesupply(String namesupply) {
        this.namesupply = namesupply;
    }

    public String getFlagOrdStop() {
        return flagOrdStop;
    }

    public void setFlagOrdStop(String flagOrdStop) {
        this.flagOrdStop = flagOrdStop;
    }

    public String getFlagStopChk() {
        return flagStopChk;
    }

    public void setFlagStopChk(String flagStopChk) {
        this.flagStopChk = flagStopChk;
    }

    public String getFlagArr() {
        return flagArr;
    }

    public Date getDateStop() {
        return dateStop;
    }

    public void setDateStop(Date dateStop) {
        this.dateStop = dateStop;
    }

    public Date getDatePlan() {
        return datePlan;
    }

    public void setDatePlan(Date datePlan) {
        this.datePlan = datePlan;
    }

	public String getNameUnitOrd() {
		return nameUnitOrd;
	}

	public void setNameUnitOrd(String nameUnitOrd) {
		this.nameUnitOrd = nameUnitOrd;
	}

	public Integer getPackSizeOrd() {
		return packSizeOrd;
	}

	public void setPackSizeOrd(Integer packSizeOrd) {
		this.packSizeOrd = packSizeOrd;
	}

}
