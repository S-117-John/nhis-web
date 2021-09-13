package com.zebone.nhis.common.module.emr.rec.rec;

import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * 病历患者信息
 * @author chengjia
 */
public class EmrPatList{
    /**
     * 
     */
    private String pkPi;
    /**
     * 
     */
    private String codePi;
    /**
     * 
     */
    private String codeOp;
    /**
     * 
     */
    private String codeIp;
    
    private String patNo;
    /**
     * 
     */
    private String barcode;
    /**
     * 
     */
    private String pkPicate;
    /**
     * 
     */
    private String namePi;
    
    private String name;
    /**
     * 
     */
    private byte[] photoPi;
    /**
     * 
     */
    private String dtIdtype;
    /**
     * 
     */
    private String idNo;
    /**
     * 
     */
    private String hicNo;
    /**
     * 
     */
    private String insurNo;
    /**
     * 
     */
    private String mpi;
    /**
     * 
     */
    private String flagEhr;
    /**
     * 
     */
    private String dtSex;
    
    private String sexName;
    /**
     * 
     */
    private Date birthDate;
    
    private String ageTxt;
    /**
     * 
     */
    private String placeBirth;
    /**
     * 
     */
    private String dtMarry;
    
    private String marryName;
    /**
     * 
     */
    private String dtOccu;
    /**
     * 
     */
    private String occuName;
    /**
     * 
     */
    private String dtEdu;
    /**
     * 
     */
    private String dtCountry;
    /**
     * 
     */
    private String dtNation;
    /**
     * 
     */
    private String nationName;
    /**
     * 
     */
    private String telNo;
    /**
     * 
     */
    private String mobile;
    /**
     * 
     */
    private String wechatNo;
    /**
     * 
     */
    private String email;
    /**
     * 
     */
    private String unitWork;
    /**
     * 
     */
    private String telWork;
    /**
     * 
     */
    private String address;
    /**
     * 
     */
    private String nameRel;
    /**
     * 
     */
    private String telRel;
    /**
     * 
     */
    private String dtBloodAbo;
    
    private String bloodAboName;
    
    /**
     * 
     */
    private String dtBloodRh;
    
    private String bloodRhName;
    /**
     * 
     */
    private String pkPv;
    /**
     * 
     */
    private String pkOrg;
    /**
     * 
     */
    private String codePv;
    /**
     * 
     */
    private String euPvtype;
    /**
     * 
     */
    private Date dateBegin;
    /**
     * 
     */
    private Date dateEnd;
    /**
     * 
     */
    private String euStatusPv;
    /**
     * 
     */
    private String flagIn;
    /**
     * 
     */
    private String flagSettle;
    /**
     * 
     */
    private String namePiPv;
    /**
     * 
     */
    private String dtSexPv;
    /**
     * 
     */
    private String agePv;
    /**
     * 
     */
    private String addressPv;
    /**
     * 
     */
    private String dtMarryPv;
    /**
     * 
     */
    private String pkDept;
    /**
     * 
     */
    private String deptName;
    /**
     * 
     */
    private String pkDeptNs;
    /**
     * 
     */
    private String deptNsName;
    /**
     * 
     */
    private Date dateClinic;
    /**
     * 
     */
    private Date dateAdmit;
    /**
     * 
     */
    private String pkWg;
    /**
     * 
     */
    private String bedNo;
    /**
     * 
     */
    private String pkEmpTre;
    /**
     * 
     */
    private String nameEmpTre;
    /**
     * 
     */
    private String pkEmpPhy;
    /**
     * 
     */
    private String nameEmpPhy;
    /**
     * 
     */
    private String pkEmpNs;
    /**
     * 
     */
    private String nameEmpNs;
    /**
     * 
     */
    private String pkInsu;
    
    private String insuName;
    /**
     * 
     */
    private String pkPaticate;
    /**
     * 
     */
    private String pkEmpReg;
    /**
     * 
     */
    private String nameEmpReg;
    /**
     * 
     */
    private Date dateReg;
    /**
     * 
     */
    private String flagCancel;
    /**
     * 
     */
    private String pkEmpCancel;
    /**
     * 
     */
    private String nameEmpCancel;
    /**
     * 
     */
    private Date dateCancel;
    /**
     * 
     */
    private String euStatusFp;
    /**
     * 
     */
    private String creator;
    /**
     * 
     */
    private Date createTime;
    /**
     * 
     */
    private String modifier;
    /**
     * 
     */
    private String delFlag;
    /**
     * 
     */
    private Date ts;
    /**
     * 
     */
    private String pkPvip;
    /**
     * 
     */
    private String pkIpNotice;
    /**
     * 
     */
    private BigDecimal ipTimes;
    /**
     * 
     */
    private Date dateNotice;
    /**
     * 
     */
    private String flagOpt;
    /**
     * 
     */
    private String dtLevelNs;
    private String levelNsName;
    /**
     * 
     */
    private String dtLevelDise;
    private String levelDiseName;
    /**
     * 
     */
    private String dtLevelNutr;
    /**
     * 
     */
    private String dtOutcomes;
    /**
     * 
     */
    private String flagInfant;
    /**
     * 
     */
    private BigDecimal quanInfant;
    /**
     * 
     */
    private String euStatusDoc;
    /**
     * 
     */
    private Date dateCommitDoc;
    /**
     * 
     */
    private String flagGa;
    /**
     * 
     */
    private String flagGaNs;
    /**
     * 
     */
    private String dtIntype;
    /**
     * 
     */
    private String dtOuttype;
    /**
     * 
     */
    private String pkDeptAdmit;
    /**
     * 
     */
    private String pkDeptNsAdmit;
    /**
     * 
     */
    private String pkDeptDis;
    /**
     * 
     */
    private String pkDeptNsDis;
    /**
     * 
     */
    private String flagPrest;
    /**
     * 
     */
    private Date datePrest;
    /**
     * 
     */
    private String pkEmpPrest;
    /**
     * 
     */
    private String nameEmpPrest;
    /**
     * 
     */
    private String diagAdmitTxt;
    /**
     * 
     */
    private String diagDisTxt;
    /**
     * 
     */
    private String euStatus;
    /**
     * 
     */
    private String pkEmpRefer;
    /**
     * 
     */
    private String referName;
    /**
     * 
     */
    private String pkEmpConsult;
    /**
     * 
     */
    private String consultName;
    /**
     * 
     */
    private String pkEmpDirector;
    /**
     * 
     */
    private String directorName;

    private int inHosDays;
    private String tempe;
    private String pluse;
    private String breath;
    private String systolic;
    private String diastolic;
    private String pkPatrec;
    private String flagAudit;
    private String pkEmpNsCharge;
    private String nsChargeName;
    private String pkEmpNsHead;
    private String nsHeadName;
    private String pkEmpQcDis;
    private String qcDisName;
    private String addrBirth;
    private String addrOrigin;
    private String addrCur;
    private String addrRegi;
    private String addrRel;
    private String dtRalation;
    private String ralationName;
    private String nemsPatDiagnosis;
    private String codeApply;
    private String quanBt;
    private Date transTime;
    
    private String dtArvtype;
    private String dtTglevel;
    private String pkKnowitemTgcate;
    private String pkKnowitemTgtype;
    private String pkKnowitemPb;
    private String pkEmpPt;
    private String nameEmpPt;
    private Date datePt;
    private String temperature;
    private String lowPressure;
    private String highPressure;
    private String nameAl;
    private String nameDurg;
    private String descDiag;
    private String nameOrdOper;
    private String descOrdRis;
    private String descOrdLab;
    private String pulse;
    private String spo2;
    
    
    private String respiration;
    private String descOpt;
    private String descOptPsy;
    private String descOptSoc;
    private String descOptEco;
    private String descOptSpe;
    private String descPain;
    private String descOptSpirit;
    private String descOptUrgent;
    
    private String euLinkQcGrade;
    private BigDecimal linkQcScore;
    private String pkEmpLinkQc;
    private Date linkQcDate;
    
    private Date qcDate;
    private Date submitDate;
    
    private String euPvmode;
    private int outHosDays;

    private String overtimeDays;
    
    private Date dateWeekT;
    
    //封存标志
    private String flagSeal;
    //开放修改标志
    private String flagOpenEdit;
	public Date getDateWeekT() {
		return dateWeekT;
	}

	public void setDateWeekT(Date dateWeekT) {
		this.dateWeekT = dateWeekT;
	}

	/**
     * 
     */
    public String getPkPi(){
        return this.pkPi;
    }

    /**
     * 
     */
    public void setPkPi(String pkPi){
        this.pkPi = pkPi;
    }    
    /**
     * 
     */
    public String getCodePi(){
        return this.codePi;
    }

    /**
     * 
     */
    public void setCodePi(String codePi){
        this.codePi = codePi;
    }    
    /**
     * 
     */
    public String getCodeOp(){
        return this.codeOp;
    }

    /**
     * 
     */
    public void setCodeOp(String codeOp){
        this.codeOp = codeOp;
    }    
    /**
     * 
     */
    public String getCodeIp(){
        return this.codeIp;
    }

    /**
     * 
     */
    public void setCodeIp(String codeIp){
        this.codeIp = codeIp;
    }    
    /**
     * 
     */
    public String getBarcode(){
        return this.barcode;
    }

    /**
     * 
     */
    public void setBarcode(String barcode){
        this.barcode = barcode;
    }    
    /**
     * 
     */
    public String getPkPicate(){
        return this.pkPicate;
    }

    /**
     * 
     */
    public void setPkPicate(String pkPicate){
        this.pkPicate = pkPicate;
    }    
    /**
     * 
     */
    public String getNamePi(){
        return this.namePi;
    }

    /**
     * 
     */
    public void setNamePi(String namePi){
        this.namePi = namePi;
    }    
    /**
     * 
     */
    public byte[] getPhotoPi(){
        return this.photoPi;
    }

    /**
     * 
     */
    public void setPhotoPi(byte[] photoPi){
        this.photoPi = photoPi;
    }    
    /**
     * 
     */
    public String getDtIdtype(){
        return this.dtIdtype;
    }

    /**
     * 
     */
    public void setDtIdtype(String dtIdtype){
        this.dtIdtype = dtIdtype;
    }    
    /**
     * 
     */
    public String getIdNo(){
        return this.idNo;
    }

    /**
     * 
     */
    public void setIdNo(String idNo){
        this.idNo = idNo;
    }    
    /**
     * 
     */
    public String getHicNo(){
        return this.hicNo;
    }

    /**
     * 
     */
    public void setHicNo(String hicNo){
        this.hicNo = hicNo;
    }    
    /**
     * 
     */
    public String getInsurNo(){
        return this.insurNo;
    }

    /**
     * 
     */
    public void setInsurNo(String insurNo){
        this.insurNo = insurNo;
    }    
    /**
     * 
     */
    public String getMpi(){
        return this.mpi;
    }

    /**
     * 
     */
    public void setMpi(String mpi){
        this.mpi = mpi;
    }    
    /**
     * 
     */
    public String getFlagEhr(){
        return this.flagEhr;
    }

    /**
     * 
     */
    public void setFlagEhr(String flagEhr){
        this.flagEhr = flagEhr;
    }    
    /**
     * 
     */
    public String getDtSex(){
        return this.dtSex;
    }

    /**
     * 
     */
    public void setDtSex(String dtSex){
        this.dtSex = dtSex;
    }    
    /**
     * 
     */
    public Date getBirthDate(){
        return this.birthDate;
    }

    /**
     * 
     */
    public void setBirthDate(Date birthDate){
        this.birthDate = birthDate;
    }    
    /**
     * 
     */
    public String getPlaceBirth(){
        return this.placeBirth;
    }

    /**
     * 
     */
    public void setPlaceBirth(String placeBirth){
        this.placeBirth = placeBirth;
    }    
    /**
     * 
     */
    public String getDtMarry(){
        return this.dtMarry;
    }

    /**
     * 
     */
    public void setDtMarry(String dtMarry){
        this.dtMarry = dtMarry;
    }    
    /**
     * 
     */
    public String getDtOccu(){
        return this.dtOccu;
    }

    /**
     * 
     */
    public void setDtOccu(String dtOccu){
        this.dtOccu = dtOccu;
    }    
    /**
     * 
     */
    public String getOccuName(){
        return this.occuName;
    }

    /**
     * 
     */
    public void setOccuName(String occuName){
        this.occuName = occuName;
    }    
    /**
     * 
     */
    public String getDtEdu(){
        return this.dtEdu;
    }

    /**
     * 
     */
    public void setDtEdu(String dtEdu){
        this.dtEdu = dtEdu;
    }    
    /**
     * 
     */
    public String getDtCountry(){
        return this.dtCountry;
    }

    /**
     * 
     */
    public void setDtCountry(String dtCountry){
        this.dtCountry = dtCountry;
    }    
    /**
     * 
     */
    public String getDtNation(){
        return this.dtNation;
    }

    /**
     * 
     */
    public void setDtNation(String dtNation){
        this.dtNation = dtNation;
    }    
    /**
     * 
     */
    public String getNationName(){
        return this.nationName;
    }

    /**
     * 
     */
    public void setNationName(String nationName){
        this.nationName = nationName;
    }    
    /**
     * 
     */
    public String getTelNo(){
        return this.telNo;
    }

    /**
     * 
     */
    public void setTelNo(String telNo){
        this.telNo = telNo;
    }    
    /**
     * 
     */
    public String getMobile(){
        return this.mobile;
    }

    /**
     * 
     */
    public void setMobile(String mobile){
        this.mobile = mobile;
    }    
    /**
     * 
     */
    public String getWechatNo(){
        return this.wechatNo;
    }

    /**
     * 
     */
    public void setWechatNo(String wechatNo){
        this.wechatNo = wechatNo;
    }    
    /**
     * 
     */
    public String getEmail(){
        return this.email;
    }

    /**
     * 
     */
    public void setEmail(String email){
        this.email = email;
    }    
    /**
     * 
     */
    public String getUnitWork(){
        return this.unitWork;
    }

    /**
     * 
     */
    public void setUnitWork(String unitWork){
        this.unitWork = unitWork;
    }    
    /**
     * 
     */
    public String getTelWork(){
        return this.telWork;
    }

    /**
     * 
     */
    public void setTelWork(String telWork){
        this.telWork = telWork;
    }    
    /**
     * 
     */
    public String getAddress(){
        return this.address;
    }

    /**
     * 
     */
    public void setAddress(String address){
        this.address = address;
    }    
    /**
     * 
     */
    public String getNameRel(){
        return this.nameRel;
    }

    /**
     * 
     */
    public void setNameRel(String nameRel){
        this.nameRel = nameRel;
    }    
    /**
     * 
     */
    public String getTelRel(){
        return this.telRel;
    }

    /**
     * 
     */
    public void setTelRel(String telRel){
        this.telRel = telRel;
    }    
    /**
     * 
     */
    public String getDtBloodAbo(){
        return this.dtBloodAbo;
    }

    /**
     * 
     */
    public void setDtBloodAbo(String dtBloodAbo){
        this.dtBloodAbo = dtBloodAbo;
    }    
    /**
     * 
     */
    public String getDtBloodRh(){
        return this.dtBloodRh;
    }

    /**
     * 
     */
    public void setDtBloodRh(String dtBloodRh){
        this.dtBloodRh = dtBloodRh;
    }    
    /**
     * 
     */
    public String getPkPv(){
        return this.pkPv;
    }

    /**
     * 
     */
    public void setPkPv(String pkPv){
        this.pkPv = pkPv;
    }    
    /**
     * 
     */
    public String getPkOrg(){
        return this.pkOrg;
    }

    /**
     * 
     */
    public void setPkOrg(String pkOrg){
        this.pkOrg = pkOrg;
    }    
    /**
     * 
     */
    public String getCodePv(){
        return this.codePv;
    }

    /**
     * 
     */
    public void setCodePv(String codePv){
        this.codePv = codePv;
    }    
    /**
     * 
     */
    public String getEuPvtype(){
        return this.euPvtype;
    }

    /**
     * 
     */
    public void setEuPvtype(String euPvtype){
        this.euPvtype = euPvtype;
    }    
    /**
     * 
     */
    public Date getDateBegin(){
        return this.dateBegin;
    }

    /**
     * 
     */
    public void setDateBegin(Date dateBegin){
        this.dateBegin = dateBegin;
    }    
    /**
     * 
     */
    public Date getDateEnd(){
        return this.dateEnd;
    }

    /**
     * 
     */
    public void setDateEnd(Date dateEnd){
        this.dateEnd = dateEnd;
    }    
    /**
     * 
     */
    public String getEuStatusPv(){
        return this.euStatusPv;
    }

    /**
     * 
     */
    public void setEuStatusPv(String euStatusPv){
        this.euStatusPv = euStatusPv;
    }    
    /**
     * 
     */
    public String getFlagIn(){
        return this.flagIn;
    }

    /**
     * 
     */
    public void setFlagIn(String flagIn){
        this.flagIn = flagIn;
    }    
    /**
     * 
     */
    public String getFlagSettle(){
        return this.flagSettle;
    }

    /**
     * 
     */
    public void setFlagSettle(String flagSettle){
        this.flagSettle = flagSettle;
    }    
    /**
     * 
     */
    public String getNamePiPv(){
        return this.namePiPv;
    }

    /**
     * 
     */
    public void setNamePiPv(String namePiPv){
        this.namePiPv = namePiPv;
    }    
    /**
     * 
     */
    public String getDtSexPv(){
        return this.dtSexPv;
    }

    /**
     * 
     */
    public void setDtSexPv(String dtSexPv){
        this.dtSexPv = dtSexPv;
    }    
    /**
     * 
     */
    public String getAgePv(){
        return this.agePv;
    }

    /**
     * 
     */
    public void setAgePv(String agePv){
        this.agePv = agePv;
    }    
    /**
     * 
     */
    public String getAddressPv(){
        return this.addressPv;
    }

    /**
     * 
     */
    public void setAddressPv(String addressPv){
        this.addressPv = addressPv;
    }    
    /**
     * 
     */
    public String getDtMarryPv(){
        return this.dtMarryPv;
    }

    /**
     * 
     */
    public void setDtMarryPv(String dtMarryPv){
        this.dtMarryPv = dtMarryPv;
    }    
    /**
     * 
     */
    public String getPkDept(){
        return this.pkDept;
    }

    /**
     * 
     */
    public void setPkDept(String pkDept){
        this.pkDept = pkDept;
    }    
    /**
     * 
     */
    public String getDeptName(){
        return this.deptName;
    }

    /**
     * 
     */
    public void setDeptName(String deptName){
        this.deptName = deptName;
    }    
    /**
     * 
     */
    public String getPkDeptNs(){
        return this.pkDeptNs;
    }

    /**
     * 
     */
    public void setPkDeptNs(String pkDeptNs){
        this.pkDeptNs = pkDeptNs;
    }    
    /**
     * 
     */
    public String getDeptNsName(){
        return this.deptNsName;
    }

    /**
     * 
     */
    public void setDeptNsName(String deptNsName){
        this.deptNsName = deptNsName;
    }    
    /**
     * 
     */
    public Date getDateClinic(){
        return this.dateClinic;
    }

    /**
     * 
     */
    public void setDateClinic(Date dateClinic){
        this.dateClinic = dateClinic;
    }    
    /**
     * 
     */
    public Date getDateAdmit(){
        return this.dateAdmit;
    }

    /**
     * 
     */
    public void setDateAdmit(Date dateAdmit){
        this.dateAdmit = dateAdmit;
    }    
    /**
     * 
     */
    public String getPkWg(){
        return this.pkWg;
    }

    /**
     * 
     */
    public void setPkWg(String pkWg){
        this.pkWg = pkWg;
    }    
    /**
     * 
     */
    public String getBedNo(){
        return this.bedNo;
    }

    /**
     * 
     */
    public void setBedNo(String bedNo){
        this.bedNo = bedNo;
    }    
    /**
     * 
     */
    public String getPkEmpTre(){
        return this.pkEmpTre;
    }

    /**
     * 
     */
    public void setPkEmpTre(String pkEmpTre){
        this.pkEmpTre = pkEmpTre;
    }    
    /**
     * 
     */
    public String getNameEmpTre(){
        return this.nameEmpTre;
    }

    /**
     * 
     */
    public void setNameEmpTre(String nameEmpTre){
        this.nameEmpTre = nameEmpTre;
    }    
    /**
     * 
     */
    public String getPkEmpPhy(){
        return this.pkEmpPhy;
    }

    /**
     * 
     */
    public void setPkEmpPhy(String pkEmpPhy){
        this.pkEmpPhy = pkEmpPhy;
    }    
    /**
     * 
     */
    public String getNameEmpPhy(){
        return this.nameEmpPhy;
    }

    /**
     * 
     */
    public void setNameEmpPhy(String nameEmpPhy){
        this.nameEmpPhy = nameEmpPhy;
    }    
    /**
     * 
     */
    public String getPkEmpNs(){
        return this.pkEmpNs;
    }

    /**
     * 
     */
    public void setPkEmpNs(String pkEmpNs){
        this.pkEmpNs = pkEmpNs;
    }    
    /**
     * 
     */
    public String getNameEmpNs(){
        return this.nameEmpNs;
    }

    /**
     * 
     */
    public void setNameEmpNs(String nameEmpNs){
        this.nameEmpNs = nameEmpNs;
    }    
    /**
     * 
     */
    public String getPkInsu(){
        return this.pkInsu;
    }

    /**
     * 
     */
    public void setPkInsu(String pkInsu){
        this.pkInsu = pkInsu;
    }    
    /**
     * 
     */
    public String getPkPaticate(){
        return this.pkPaticate;
    }

    /**
     * 
     */
    public void setPkPaticate(String pkPaticate){
        this.pkPaticate = pkPaticate;
    }    
    /**
     * 
     */
    public String getPkEmpReg(){
        return this.pkEmpReg;
    }

    /**
     * 
     */
    public void setPkEmpReg(String pkEmpReg){
        this.pkEmpReg = pkEmpReg;
    }    
    /**
     * 
     */
    public String getNameEmpReg(){
        return this.nameEmpReg;
    }

    /**
     * 
     */
    public void setNameEmpReg(String nameEmpReg){
        this.nameEmpReg = nameEmpReg;
    }    
    /**
     * 
     */
    public Date getDateReg(){
        return this.dateReg;
    }

    /**
     * 
     */
    public void setDateReg(Date dateReg){
        this.dateReg = dateReg;
    }    
    /**
     * 
     */
    public String getFlagCancel(){
        return this.flagCancel;
    }

    /**
     * 
     */
    public void setFlagCancel(String flagCancel){
        this.flagCancel = flagCancel;
    }    
    /**
     * 
     */
    public String getPkEmpCancel(){
        return this.pkEmpCancel;
    }

    /**
     * 
     */
    public void setPkEmpCancel(String pkEmpCancel){
        this.pkEmpCancel = pkEmpCancel;
    }    
    /**
     * 
     */
    public String getNameEmpCancel(){
        return this.nameEmpCancel;
    }

    /**
     * 
     */
    public void setNameEmpCancel(String nameEmpCancel){
        this.nameEmpCancel = nameEmpCancel;
    }    
    /**
     * 
     */
    public Date getDateCancel(){
        return this.dateCancel;
    }

    /**
     * 
     */
    public void setDateCancel(Date dateCancel){
        this.dateCancel = dateCancel;
    }    
    /**
     * 
     */
    public String getEuStatusFp(){
        return this.euStatusFp;
    }

    /**
     * 
     */
    public void setEuStatusFp(String euStatusFp){
        this.euStatusFp = euStatusFp;
    }    
    /**
     * 
     */
    public String getCreator(){
        return this.creator;
    }

    /**
     * 
     */
    public void setCreator(String creator){
        this.creator = creator;
    }    
    /**
     * 
     */
    public Date getCreateTime(){
        return this.createTime;
    }

    /**
     * 
     */
    public void setCreateTime(Date createTime){
        this.createTime = createTime;
    }    
    /**
     * 
     */
    public String getModifier(){
        return this.modifier;
    }

    /**
     * 
     */
    public void setModifier(String modifier){
        this.modifier = modifier;
    }    
    /**
     * 
     */
    public String getDelFlag(){
        return this.delFlag;
    }

    /**
     * 
     */
    public void setDelFlag(String delFlag){
        this.delFlag = delFlag;
    }    
    /**
     * 
     */
    public Date getTs(){
        return this.ts;
    }

    /**
     * 
     */
    public void setTs(Date ts){
        this.ts = ts;
    }    
    /**
     * 
     */
    public String getPkPvip(){
        return this.pkPvip;
    }

    /**
     * 
     */
    public void setPkPvip(String pkPvip){
        this.pkPvip = pkPvip;
    }    
    /**
     * 
     */
    public String getPkIpNotice(){
        return this.pkIpNotice;
    }

    /**
     * 
     */
    public void setPkIpNotice(String pkIpNotice){
        this.pkIpNotice = pkIpNotice;
    }    
    /**
     * 
     */
    public BigDecimal getIpTimes(){
        return this.ipTimes;
    }

    /**
     * 
     */
    public void setIpTimes(BigDecimal ipTimes){
        this.ipTimes = ipTimes;
    }    
    /**
     * 
     */
    public Date getDateNotice(){
        return this.dateNotice;
    }

    /**
     * 
     */
    public void setDateNotice(Date dateNotice){
        this.dateNotice = dateNotice;
    }    
    /**
     * 
     */
    public String getFlagOpt(){
        return this.flagOpt;
    }

    /**
     * 
     */
    public void setFlagOpt(String flagOpt){
        this.flagOpt = flagOpt;
    }    
    /**
     * 
     */
    public String getDtLevelNs(){
        return this.dtLevelNs;
    }

    /**
     * 
     */
    public void setDtLevelNs(String dtLevelNs){
        this.dtLevelNs = dtLevelNs;
    }    
    /**
     * 
     */
    public String getDtLevelDise(){
        return this.dtLevelDise;
    }

    /**
     * 
     */
    public void setDtLevelDise(String dtLevelDise){
        this.dtLevelDise = dtLevelDise;
    }    
    /**
     * 
     */
    public String getDtLevelNutr(){
        return this.dtLevelNutr;
    }

    /**
     * 
     */
    public void setDtLevelNutr(String dtLevelNutr){
        this.dtLevelNutr = dtLevelNutr;
    }    
    /**
     * 
     */
    public String getDtOutcomes(){
        return this.dtOutcomes;
    }

    /**
     * 
     */
    public void setDtOutcomes(String dtOutcomes){
        this.dtOutcomes = dtOutcomes;
    }    
    /**
     * 
     */
    public String getFlagInfant(){
        return this.flagInfant;
    }

    /**
     * 
     */
    public void setFlagInfant(String flagInfant){
        this.flagInfant = flagInfant;
    }    
    /**
     * 
     */
    public BigDecimal getQuanInfant(){
        return this.quanInfant;
    }

    /**
     * 
     */
    public void setQuanInfant(BigDecimal quanInfant){
        this.quanInfant = quanInfant;
    }    
    /**
     * 
     */
    public String getEuStatusDoc(){
        return this.euStatusDoc;
    }

    /**
     * 
     */
    public void setEuStatusDoc(String euStatusDoc){
        this.euStatusDoc = euStatusDoc;
    }    
    /**
     * 
     */
    public Date getDateCommitDoc(){
        return this.dateCommitDoc;
    }

    /**
     * 
     */
    public void setDateCommitDoc(Date dateCommitDoc){
        this.dateCommitDoc = dateCommitDoc;
    }    
    /**
     * 
     */
    public String getFlagGa(){
        return this.flagGa;
    }

    /**
     * 
     */
    public void setFlagGa(String flagGa){
        this.flagGa = flagGa;
    }    
    /**
     * 
     */
    public String getFlagGaNs(){
        return this.flagGaNs;
    }

    /**
     * 
     */
    public void setFlagGaNs(String flagGaNs){
        this.flagGaNs = flagGaNs;
    }    
    /**
     * 
     */
    public String getDtIntype(){
        return this.dtIntype;
    }

    /**
     * 
     */
    public void setDtIntype(String dtIntype){
        this.dtIntype = dtIntype;
    }    
    /**
     * 
     */
    public String getDtOuttype(){
        return this.dtOuttype;
    }

    /**
     * 
     */
    public void setDtOuttype(String dtOuttype){
        this.dtOuttype = dtOuttype;
    }    
    /**
     * 
     */
    public String getPkDeptAdmit(){
        return this.pkDeptAdmit;
    }

    /**
     * 
     */
    public void setPkDeptAdmit(String pkDeptAdmit){
        this.pkDeptAdmit = pkDeptAdmit;
    }    
    /**
     * 
     */
    public String getPkDeptNsAdmit(){
        return this.pkDeptNsAdmit;
    }

    /**
     * 
     */
    public void setPkDeptNsAdmit(String pkDeptNsAdmit){
        this.pkDeptNsAdmit = pkDeptNsAdmit;
    }    
    /**
     * 
     */
    public String getPkDeptDis(){
        return this.pkDeptDis;
    }

    /**
     * 
     */
    public void setPkDeptDis(String pkDeptDis){
        this.pkDeptDis = pkDeptDis;
    }    
    /**
     * 
     */
    public String getPkDeptNsDis(){
        return this.pkDeptNsDis;
    }

    /**
     * 
     */
    public void setPkDeptNsDis(String pkDeptNsDis){
        this.pkDeptNsDis = pkDeptNsDis;
    }    
    /**
     * 
     */
    public String getFlagPrest(){
        return this.flagPrest;
    }

    /**
     * 
     */
    public void setFlagPrest(String flagPrest){
        this.flagPrest = flagPrest;
    }    
    /**
     * 
     */
    public Date getDatePrest(){
        return this.datePrest;
    }

    /**
     * 
     */
    public void setDatePrest(Date datePrest){
        this.datePrest = datePrest;
    }    
    /**
     * 
     */
    public String getPkEmpPrest(){
        return this.pkEmpPrest;
    }

    /**
     * 
     */
    public void setPkEmpPrest(String pkEmpPrest){
        this.pkEmpPrest = pkEmpPrest;
    }    
    /**
     * 
     */
    public String getNameEmpPrest(){
        return this.nameEmpPrest;
    }

    /**
     * 
     */
    public void setNameEmpPrest(String nameEmpPrest){
        this.nameEmpPrest = nameEmpPrest;
    }    
    /**
     * 
     */
    public String getDiagAdmitTxt(){
        return this.diagAdmitTxt;
    }

    /**
     * 
     */
    public void setDiagAdmitTxt(String diagAdmitTxt){
        this.diagAdmitTxt = diagAdmitTxt;
    }    
    /**
     * 
     */
    public String getDiagDisTxt(){
        return this.diagDisTxt;
    }

    /**
     * 
     */
    public void setDiagDisTxt(String diagDisTxt){
        this.diagDisTxt = diagDisTxt;
    }    
    /**
     * 
     */
    public String getEuStatus(){
        return this.euStatus;
    }

    /**
     * 
     */
    public void setEuStatus(String euStatus){
        this.euStatus = euStatus;
    }    
    /**
     * 
     */


	public String getPatNo() {
		return patNo;
	}

	public String getPkEmpRefer() {
		return pkEmpRefer;
	}

	public void setPkEmpRefer(String pkEmpRefer) {
		this.pkEmpRefer = pkEmpRefer;
	}

	public String getReferName() {
		return referName;
	}

	public void setReferName(String referName) {
		this.referName = referName;
	}

	public String getPkEmpConsult() {
		return pkEmpConsult;
	}

	public void setPkEmpConsult(String pkEmpConsult) {
		this.pkEmpConsult = pkEmpConsult;
	}

	public String getConsultName() {
		return consultName;
	}

	public void setConsultName(String consultName) {
		this.consultName = consultName;
	}

	public String getPkEmpDirector() {
		return pkEmpDirector;
	}

	public void setPkEmpDirector(String pkEmpDirector) {
		this.pkEmpDirector = pkEmpDirector;
	}

	public String getDirectorName() {
		return directorName;
	}

	public void setDirectorName(String directorName) {
		this.directorName = directorName;
	}

	public void setPatNo(String patNo) {
		this.patNo = patNo;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSexName() {
		return sexName;
	}

	public void setSexName(String sexName) {
		this.sexName = sexName;
	}

	public String getAgeTxt() {
		return ageTxt;
	}

	public void setAgeTxt(String ageTxt) {
		this.ageTxt = ageTxt;
	}

	public String getBloodAboName() {
		return bloodAboName;
	}

	public void setBloodAboName(String bloodAboName) {
		this.bloodAboName = bloodAboName;
	}

	public String getBloodRhName() {
		return bloodRhName;
	}

	public void setBloodRhName(String bloodRhName) {
		this.bloodRhName = bloodRhName;
	}

	public void setMarryName(String marryName) {
		this.marryName = marryName;
	}

	public String getLevelNsName() {
		return levelNsName;
	}

	public void setLevelNsName(String levelNsName) {
		this.levelNsName = levelNsName;
	}

	public String getLevelDiseName() {
		return levelDiseName;
	}

	public void setLevelDiseName(String levelDiseName) {
		this.levelDiseName = levelDiseName;
	}

	public int getInHosDays() {
		return inHosDays;
	}

	public void setInHosDays(int inHosDays) {
		this.inHosDays = inHosDays;
	}

	public String getTempe() {
		return tempe;
	}

	public void setTempe(String tempe) {
		this.tempe = tempe;
	}

	public String getPluse() {
		return pluse;
	}

	public void setPluse(String pluse) {
		this.pluse = pluse;
	}

	public String getBreath() {
		return breath;
	}

	public void setBreath(String breath) {
		this.breath = breath;
	}

	public String getSystolic() {
		return systolic;
	}

	public void setSystolic(String systolic) {
		this.systolic = systolic;
	}

	public String getDiastolic() {
		return diastolic;
	}

	public void setDiastolic(String diastolic) {
		this.diastolic = diastolic;
	}

	public String getInsuName() {
		return insuName;
	}

	public void setInsuName(String insuName) {
		this.insuName = insuName;
	}

	public String getPkPatrec() {
		return pkPatrec;
	}

	public void setPkPatrec(String pkPatrec) {
		this.pkPatrec = pkPatrec;
	}

	public String getFlagAudit() {
		return flagAudit;
	}

	public void setFlagAudit(String flagAudit) {
		this.flagAudit = flagAudit;
	}

	public String getMarryName() {
		return marryName;
	}

	public String getPkEmpNsCharge() {
		return pkEmpNsCharge;
	}

	public void setPkEmpNsCharge(String pkEmpNsCharge) {
		this.pkEmpNsCharge = pkEmpNsCharge;
	}

	public String getNsChargeName() {
		return nsChargeName;
	}

	public void setNsChargeName(String nsChargeName) {
		this.nsChargeName = nsChargeName;
	}

	public String getPkEmpNsHead() {
		return pkEmpNsHead;
	}

	public void setPkEmpNsHead(String pkEmpNsHead) {
		this.pkEmpNsHead = pkEmpNsHead;
	}

	public String getNsHeadName() {
		return nsHeadName;
	}

	public void setNsHeadName(String nsHeadName) {
		this.nsHeadName = nsHeadName;
	}

	public String getPkEmpQcDis() {
		return pkEmpQcDis;
	}

	public void setPkEmpQcDis(String pkEmpQcDis) {
		this.pkEmpQcDis = pkEmpQcDis;
	}

	public String getQcDisName() {
		return qcDisName;
	}

	public void setQcDisName(String qcDisName) {
		this.qcDisName = qcDisName;
	}

	public String getAddrBirth() {
		return addrBirth;
	}

	public void setAddrBirth(String addrBirth) {
		this.addrBirth = addrBirth;
	}

	public String getAddrOrigin() {
		return addrOrigin;
	}

	public void setAddrOrigin(String addrOrigin) {
		this.addrOrigin = addrOrigin;
	}

	public String getAddrCur() {
		return addrCur;
	}

	public void setAddrCur(String addrCur) {
		this.addrCur = addrCur;
	}

	public String getNemsPatDiagnosis() {
		return nemsPatDiagnosis;
	}

	public void setNemsPatDiagnosis(String nemsPatDiagnosis) {
		this.nemsPatDiagnosis = nemsPatDiagnosis;
	}

	public String getCodeApply() {
		return codeApply;
	}

	public void setCodeApply(String codeApply) {
		this.codeApply = codeApply;
	}

	public String getQuanBt() {
		return quanBt;
	}

	public void setQuanBt(String quanBt) {
		this.quanBt = quanBt;
	}

	public Date getTransTime() {
		return transTime;
	}

	public void setTransTime(Date transTime) {
		this.transTime = transTime;
	}

	public String getAddrRegi() {
		return addrRegi;
	}

	public void setAddrRegi(String addrRegi) {
		this.addrRegi = addrRegi;
	}

	public String getAddrRel() {
		return addrRel;
	}

	public void setAddrRel(String addrRel) {
		this.addrRel = addrRel;
	}

	public String getDtRalation() {
		return dtRalation;
	}

	public void setDtRalation(String dtRalation) {
		this.dtRalation = dtRalation;
	}

	public String getRalationName() {
		return ralationName;
	}

	public void setRalationName(String ralationName) {
		this.ralationName = ralationName;
	}

	public String getDtArvtype() {
		return dtArvtype;
	}

	public void setDtArvtype(String dtArvtype) {
		this.dtArvtype = dtArvtype;
	}

	public String getDtTglevel() {
		return dtTglevel;
	}

	public void setDtTglevel(String dtTglevel) {
		this.dtTglevel = dtTglevel;
	}

	public String getPkKnowitemTgcate() {
		return pkKnowitemTgcate;
	}

	public void setPkKnowitemTgcate(String pkKnowitemTgcate) {
		this.pkKnowitemTgcate = pkKnowitemTgcate;
	}

	public String getPkKnowitemTgtype() {
		return pkKnowitemTgtype;
	}

	public void setPkKnowitemTgtype(String pkKnowitemTgtype) {
		this.pkKnowitemTgtype = pkKnowitemTgtype;
	}

	public String getPkKnowitemPb() {
		return pkKnowitemPb;
	}

	public void setPkKnowitemPb(String pkKnowitemPb) {
		this.pkKnowitemPb = pkKnowitemPb;
	}

	public String getPkEmpPt() {
		return pkEmpPt;
	}

	public void setPkEmpPt(String pkEmpPt) {
		this.pkEmpPt = pkEmpPt;
	}

	public String getNameEmpPt() {
		return nameEmpPt;
	}

	public void setNameEmpPt(String nameEmpPt) {
		this.nameEmpPt = nameEmpPt;
	}

	public Date getDatePt() {
		return datePt;
	}

	public void setDatePt(Date datePt) {
		this.datePt = datePt;
	}

	public String getTemperature() {
		return temperature;
	}

	public void setTemperature(String temperature) {
		this.temperature = temperature;
	}

	public String getLowPressure() {
		return lowPressure;
	}

	public void setLowPressure(String lowPressure) {
		this.lowPressure = lowPressure;
	}

	public String getHighPressure() {
		return highPressure;
	}

	public void setHighPressure(String highPressure) {
		this.highPressure = highPressure;
	}

	public String getNameAl() {
		return nameAl;
	}

	public void setNameAl(String nameAl) {
		this.nameAl = nameAl;
	}

	public String getNameDurg() {
		return nameDurg;
	}

	public void setNameDurg(String nameDurg) {
		this.nameDurg = nameDurg;
	}

	public String getDescDiag() {
		return descDiag;
	}

	public void setDescDiag(String descDiag) {
		this.descDiag = descDiag;
	}

	public String getNameOrdOper() {
		return nameOrdOper;
	}

	public void setNameOrdOper(String nameOrdOper) {
		this.nameOrdOper = nameOrdOper;
	}

	public String getDescOrdRis() {
		return descOrdRis;
	}

	public void setDescOrdRis(String descOrdRis) {
		this.descOrdRis = descOrdRis;
	}

	public String getDescOrdLab() {
		return descOrdLab;
	}

	public void setDescOrdLab(String descOrdLab) {
		this.descOrdLab = descOrdLab;
	}

	public String getPulse() {
		return pulse;
	}

	public void setPulse(String pulse) {
		this.pulse = pulse;
	}

	public String getSpo2() {
		return spo2;
	}

	public void setSpo2(String spo2) {
		this.spo2 = spo2;
	}

	public String getRespiration() {
		return respiration;
	}

	public void setRespiration(String respiration) {
		this.respiration = respiration;
	}

	public String getDescOpt() {
		return descOpt;
	}

	public void setDescOpt(String descOpt) {
		this.descOpt = descOpt;
	}

	public String getDescOptPsy() {
		return descOptPsy;
	}

	public void setDescOptPsy(String descOptPsy) {
		this.descOptPsy = descOptPsy;
	}

	public String getDescOptSoc() {
		return descOptSoc;
	}

	public void setDescOptSoc(String descOptSoc) {
		this.descOptSoc = descOptSoc;
	}

	public String getDescOptEco() {
		return descOptEco;
	}

	public void setDescOptEco(String descOptEco) {
		this.descOptEco = descOptEco;
	}

	public String getDescOptSpe() {
		return descOptSpe;
	}

	public void setDescOptSpe(String descOptSpe) {
		this.descOptSpe = descOptSpe;
	}

	public String getDescPain() {
		return descPain;
	}

	public void setDescPain(String descPain) {
		this.descPain = descPain;
	}

	public String getDescOptSpirit() {
		return descOptSpirit;
	}

	public void setDescOptSpirit(String descOptSpirit) {
		this.descOptSpirit = descOptSpirit;
	}

	public String getDescOptUrgent() {
		return descOptUrgent;
	}

	public void setDescOptUrgent(String descOptUrgent) {
		this.descOptUrgent = descOptUrgent;
	}

	public Date getQcDate() {
		return qcDate;
	}

	public void setQcDate(Date qcDate) {
		this.qcDate = qcDate;
	}

	public Date getSubmitDate() {
		return submitDate;
	}

	public void setSubmitDate(Date submitDate) {
		this.submitDate = submitDate;
	}

	public String getEuPvmode() {
		return euPvmode;
	}

	public void setEuPvmode(String euPvmode) {
		this.euPvmode = euPvmode;
	}

	public int getOutHosDays() {
		return outHosDays;
	}

	public void setOutHosDays(int outHosDays) {
		this.outHosDays = outHosDays;
	}

	public String getEuLinkQcGrade() {
		return euLinkQcGrade;
	}

	public void setEuLinkQcGrade(String euLinkQcGrade) {
		this.euLinkQcGrade = euLinkQcGrade;
	}

	public BigDecimal getLinkQcScore() {
		return linkQcScore;
	}

	public void setLinkQcScore(BigDecimal linkQcScore) {
		this.linkQcScore = linkQcScore;
	}

	public String getPkEmpLinkQc() {
		return pkEmpLinkQc;
	}

	public void setPkEmpLinkQc(String pkEmpLinkQc) {
		this.pkEmpLinkQc = pkEmpLinkQc;
	}

	public Date getLinkQcDate() {
		return linkQcDate;
	}

	public void setLinkQcDate(Date linkQcDate) {
		this.linkQcDate = linkQcDate;
	}

	public String getOvertimeDays() {
		return overtimeDays;
	}

	public void setOvertimeDays(String overtimeDays) {
		this.overtimeDays = overtimeDays;
	}

	public String getFlagSeal() {
		return flagSeal;
	}

	public void setFlagSeal(String flagSeal) {
		this.flagSeal = flagSeal;
	}

	public String getFlagOpenEdit() {
		return flagOpenEdit;
	}

	public void setFlagOpenEdit(String flagOpenEdit) {
		this.flagOpenEdit = flagOpenEdit;
	}
	
}