package com.zebone.nhis.common.module.emr.rec.dict;

import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;



/**
 * view: VIEW_EMR_PAT_LIST 
 *
 * @since 2016-10-17 04:23:26
 */
public class ViewEmrPatList {


    private String pkPi;

    private String codePi;

    private String codeOp;

    private String codeIp;

    private String patNo;

    private String barcode;

    private String pkPicate;

    private String namePi;

    private String name;

    private byte[] photoPi;

    private String dtIdtype;

    private String idNo;

    private String hicNo;

    private String insurNo;

    private String mpi;

    private String flagEhr;

    private String dtSex;

    private String sexName;

    private Date birthDate;

    private String ageTxt;

    private String placeBirth;

    private String dtMarry;

    private String marryName;

    private String dtOccu;

    private String occuName;

    private String dtEdu;

    private String dtCountry;

    private String dtNation;

    private String nationName;

    private String telNo;

    private String mobile;

    private String wechatNo;

    private String email;

    private String unitWork;

    private String telWork;

    private String address;

    private String nameRel;

    private String telRel;

    private String dtBloodAbo;

    private String bloodAboName;

    private String dtBloodRh;

    private String bloodRhName;

    private String pkPv;

    private String pkOrg;

    private String codePv;

    private String euPvtype;

    private Date dateBegin;

    private Date dateEnd;

    private String euStatusPv;

    private String flagIn;

    private String flagSettle;

    private String namePiPv;

    private String dtSexPv;

    private String agePv;

    private String addressPv;

    private String dtMarryPv;

    private String pkDept;

    private String deptName;

    private String pkDeptNs;

    private String deptNsName;

    private Date dateClinic;

    private Date dateAdmit;

    private String pkWg;

    private String bedNo;

    private String pkEmpTre;

    private String nameEmpTre;

    private String pkEmpPhy;

    private String nameEmpPhy;

    private String pkEmpNs;

    private String nameEmpNs;

    private String pkInsu;

    private String insuName;

    private String pkPaticate;

    private String pkEmpReg;

    private String nameEmpReg;

    private Date dateReg;

    private String flagCancel;

    private String pkEmpCancel;

    private String nameEmpCancel;

    private Date dateCancel;

    private String euStatusFp;

    private String creator;

    private Date createTime;

    private String modifier;

    private String delFlag;

    private Date ts;

    private String pkPvip;

    private String pkIpNotice;

    private Long ipTimes;

    private Date dateNotice;

    private String flagOpt;

    private String dtLevelNs;

    private String levelNsName;

    private String dtLevelDise;

    private String levelDiseName;

    private String dtLevelNutr;

    private String dtOutcomes;

    private String flagInfant;

    private Long quanInfant;

    private String euStatusDoc;

    private Date dateCommitDoc;

    private String flagGa;

    private String flagGaNs;

    private String dtIntype;

    private String dtOuttype;

    private String pkDeptAdmit;

    private String pkDeptNsAdmit;

    private String pkDeptDis;

    private String pkDeptNsDis;

    private String flagPrest;

    private Date datePrest;

    private String pkEmpPrest;

    private String nameEmpPrest;

    private String pkPatrec;

    private String diagAdmitTxt;

    private String diagDisTxt;

    private String euStatus;

    private String pkEmpRefer;

    private String referName;

    private String pkEmpConsult;

    private String consultName;

    private String pkEmpDirector;

    private String directorName;

    private Long inHosDays;

    private String tempe;

    private String pluse;

    private String breath;

    private String systolic;

    private String diastolic;


    public String getPkPi(){
        return this.pkPi;
    }
    public void setPkPi(String pkPi){
        this.pkPi = pkPi;
    }

    public String getCodePi(){
        return this.codePi;
    }
    public void setCodePi(String codePi){
        this.codePi = codePi;
    }

    public String getCodeOp(){
        return this.codeOp;
    }
    public void setCodeOp(String codeOp){
        this.codeOp = codeOp;
    }

    public String getCodeIp(){
        return this.codeIp;
    }
    public void setCodeIp(String codeIp){
        this.codeIp = codeIp;
    }

    public String getPatNo(){
        return this.patNo;
    }
    public void setPatNo(String patNo){
        this.patNo = patNo;
    }

    public String getBarcode(){
        return this.barcode;
    }
    public void setBarcode(String barcode){
        this.barcode = barcode;
    }

    public String getPkPicate(){
        return this.pkPicate;
    }
    public void setPkPicate(String pkPicate){
        this.pkPicate = pkPicate;
    }

    public String getNamePi(){
        return this.namePi;
    }
    public void setNamePi(String namePi){
        this.namePi = namePi;
    }

    public String getName(){
        return this.name;
    }
    public void setName(String name){
        this.name = name;
    }

    public byte[] getPhotoPi(){
        return this.photoPi;
    }
    public void setPhotoPi(byte[] photoPi){
        this.photoPi = photoPi;
    }

    public String getDtIdtype(){
        return this.dtIdtype;
    }
    public void setDtIdtype(String dtIdtype){
        this.dtIdtype = dtIdtype;
    }

    public String getIdNo(){
        return this.idNo;
    }
    public void setIdNo(String idNo){
        this.idNo = idNo;
    }

    public String getHicNo(){
        return this.hicNo;
    }
    public void setHicNo(String hicNo){
        this.hicNo = hicNo;
    }

    public String getInsurNo(){
        return this.insurNo;
    }
    public void setInsurNo(String insurNo){
        this.insurNo = insurNo;
    }

    public String getMpi(){
        return this.mpi;
    }
    public void setMpi(String mpi){
        this.mpi = mpi;
    }

    public String getFlagEhr(){
        return this.flagEhr;
    }
    public void setFlagEhr(String flagEhr){
        this.flagEhr = flagEhr;
    }

    public String getDtSex(){
        return this.dtSex;
    }
    public void setDtSex(String dtSex){
        this.dtSex = dtSex;
    }

    public String getSexName(){
        return this.sexName;
    }
    public void setSexName(String sexName){
        this.sexName = sexName;
    }

    public Date getBirthDate(){
        return this.birthDate;
    }
    public void setBirthDate(Date birthDate){
        this.birthDate = birthDate;
    }

    public String getAgeTxt(){
        return this.ageTxt;
    }
    public void setAgeTxt(String ageTxt){
        this.ageTxt = ageTxt;
    }

    public String getPlaceBirth(){
        return this.placeBirth;
    }
    public void setPlaceBirth(String placeBirth){
        this.placeBirth = placeBirth;
    }

    public String getDtMarry(){
        return this.dtMarry;
    }
    public void setDtMarry(String dtMarry){
        this.dtMarry = dtMarry;
    }

    public String getMarryName(){
        return this.marryName;
    }
    public void setMarryName(String marryName){
        this.marryName = marryName;
    }

    public String getDtOccu(){
        return this.dtOccu;
    }
    public void setDtOccu(String dtOccu){
        this.dtOccu = dtOccu;
    }

    public String getOccuName(){
        return this.occuName;
    }
    public void setOccuName(String occuName){
        this.occuName = occuName;
    }

    public String getDtEdu(){
        return this.dtEdu;
    }
    public void setDtEdu(String dtEdu){
        this.dtEdu = dtEdu;
    }

    public String getDtCountry(){
        return this.dtCountry;
    }
    public void setDtCountry(String dtCountry){
        this.dtCountry = dtCountry;
    }

    public String getDtNation(){
        return this.dtNation;
    }
    public void setDtNation(String dtNation){
        this.dtNation = dtNation;
    }

    public String getNationName(){
        return this.nationName;
    }
    public void setNationName(String nationName){
        this.nationName = nationName;
    }

    public String getTelNo(){
        return this.telNo;
    }
    public void setTelNo(String telNo){
        this.telNo = telNo;
    }

    public String getMobile(){
        return this.mobile;
    }
    public void setMobile(String mobile){
        this.mobile = mobile;
    }

    public String getWechatNo(){
        return this.wechatNo;
    }
    public void setWechatNo(String wechatNo){
        this.wechatNo = wechatNo;
    }

    public String getEmail(){
        return this.email;
    }
    public void setEmail(String email){
        this.email = email;
    }

    public String getUnitWork(){
        return this.unitWork;
    }
    public void setUnitWork(String unitWork){
        this.unitWork = unitWork;
    }

    public String getTelWork(){
        return this.telWork;
    }
    public void setTelWork(String telWork){
        this.telWork = telWork;
    }

    public String getAddress(){
        return this.address;
    }
    public void setAddress(String address){
        this.address = address;
    }

    public String getNameRel(){
        return this.nameRel;
    }
    public void setNameRel(String nameRel){
        this.nameRel = nameRel;
    }

    public String getTelRel(){
        return this.telRel;
    }
    public void setTelRel(String telRel){
        this.telRel = telRel;
    }

    public String getDtBloodAbo(){
        return this.dtBloodAbo;
    }
    public void setDtBloodAbo(String dtBloodAbo){
        this.dtBloodAbo = dtBloodAbo;
    }

    public String getBloodAboName(){
        return this.bloodAboName;
    }
    public void setBloodAboName(String bloodAboName){
        this.bloodAboName = bloodAboName;
    }

    public String getDtBloodRh(){
        return this.dtBloodRh;
    }
    public void setDtBloodRh(String dtBloodRh){
        this.dtBloodRh = dtBloodRh;
    }

    public String getBloodRhName(){
        return this.bloodRhName;
    }
    public void setBloodRhName(String bloodRhName){
        this.bloodRhName = bloodRhName;
    }

    public String getPkPv(){
        return this.pkPv;
    }
    public void setPkPv(String pkPv){
        this.pkPv = pkPv;
    }

    public String getPkOrg(){
        return this.pkOrg;
    }
    public void setPkOrg(String pkOrg){
        this.pkOrg = pkOrg;
    }

    public String getCodePv(){
        return this.codePv;
    }
    public void setCodePv(String codePv){
        this.codePv = codePv;
    }

    public String getEuPvtype(){
        return this.euPvtype;
    }
    public void setEuPvtype(String euPvtype){
        this.euPvtype = euPvtype;
    }

    public Date getDateBegin(){
        return this.dateBegin;
    }
    public void setDateBegin(Date dateBegin){
        this.dateBegin = dateBegin;
    }

    public Date getDateEnd(){
        return this.dateEnd;
    }
    public void setDateEnd(Date dateEnd){
        this.dateEnd = dateEnd;
    }

    public String getEuStatusPv(){
        return this.euStatusPv;
    }
    public void setEuStatusPv(String euStatusPv){
        this.euStatusPv = euStatusPv;
    }

    public String getFlagIn(){
        return this.flagIn;
    }
    public void setFlagIn(String flagIn){
        this.flagIn = flagIn;
    }

    public String getFlagSettle(){
        return this.flagSettle;
    }
    public void setFlagSettle(String flagSettle){
        this.flagSettle = flagSettle;
    }

    public String getNamePiPv(){
        return this.namePiPv;
    }
    public void setNamePiPv(String namePiPv){
        this.namePiPv = namePiPv;
    }

    public String getDtSexPv(){
        return this.dtSexPv;
    }
    public void setDtSexPv(String dtSexPv){
        this.dtSexPv = dtSexPv;
    }

    public String getAgePv(){
        return this.agePv;
    }
    public void setAgePv(String agePv){
        this.agePv = agePv;
    }

    public String getAddressPv(){
        return this.addressPv;
    }
    public void setAddressPv(String addressPv){
        this.addressPv = addressPv;
    }

    public String getDtMarryPv(){
        return this.dtMarryPv;
    }
    public void setDtMarryPv(String dtMarryPv){
        this.dtMarryPv = dtMarryPv;
    }

    public String getPkDept(){
        return this.pkDept;
    }
    public void setPkDept(String pkDept){
        this.pkDept = pkDept;
    }

    public String getDeptName(){
        return this.deptName;
    }
    public void setDeptName(String deptName){
        this.deptName = deptName;
    }

    public String getPkDeptNs(){
        return this.pkDeptNs;
    }
    public void setPkDeptNs(String pkDeptNs){
        this.pkDeptNs = pkDeptNs;
    }

    public String getDeptNsName(){
        return this.deptNsName;
    }
    public void setDeptNsName(String deptNsName){
        this.deptNsName = deptNsName;
    }

    public Date getDateClinic(){
        return this.dateClinic;
    }
    public void setDateClinic(Date dateClinic){
        this.dateClinic = dateClinic;
    }

    public Date getDateAdmit(){
        return this.dateAdmit;
    }
    public void setDateAdmit(Date dateAdmit){
        this.dateAdmit = dateAdmit;
    }

    public String getPkWg(){
        return this.pkWg;
    }
    public void setPkWg(String pkWg){
        this.pkWg = pkWg;
    }

    public String getBedNo(){
        return this.bedNo;
    }
    public void setBedNo(String bedNo){
        this.bedNo = bedNo;
    }

    public String getPkEmpTre(){
        return this.pkEmpTre;
    }
    public void setPkEmpTre(String pkEmpTre){
        this.pkEmpTre = pkEmpTre;
    }

    public String getNameEmpTre(){
        return this.nameEmpTre;
    }
    public void setNameEmpTre(String nameEmpTre){
        this.nameEmpTre = nameEmpTre;
    }

    public String getPkEmpPhy(){
        return this.pkEmpPhy;
    }
    public void setPkEmpPhy(String pkEmpPhy){
        this.pkEmpPhy = pkEmpPhy;
    }

    public String getNameEmpPhy(){
        return this.nameEmpPhy;
    }
    public void setNameEmpPhy(String nameEmpPhy){
        this.nameEmpPhy = nameEmpPhy;
    }

    public String getPkEmpNs(){
        return this.pkEmpNs;
    }
    public void setPkEmpNs(String pkEmpNs){
        this.pkEmpNs = pkEmpNs;
    }

    public String getNameEmpNs(){
        return this.nameEmpNs;
    }
    public void setNameEmpNs(String nameEmpNs){
        this.nameEmpNs = nameEmpNs;
    }

    public String getPkInsu(){
        return this.pkInsu;
    }
    public void setPkInsu(String pkInsu){
        this.pkInsu = pkInsu;
    }

    public String getInsuName(){
        return this.insuName;
    }
    public void setInsuName(String insuName){
        this.insuName = insuName;
    }

    public String getPkPaticate(){
        return this.pkPaticate;
    }
    public void setPkPaticate(String pkPaticate){
        this.pkPaticate = pkPaticate;
    }

    public String getPkEmpReg(){
        return this.pkEmpReg;
    }
    public void setPkEmpReg(String pkEmpReg){
        this.pkEmpReg = pkEmpReg;
    }

    public String getNameEmpReg(){
        return this.nameEmpReg;
    }
    public void setNameEmpReg(String nameEmpReg){
        this.nameEmpReg = nameEmpReg;
    }

    public Date getDateReg(){
        return this.dateReg;
    }
    public void setDateReg(Date dateReg){
        this.dateReg = dateReg;
    }

    public String getFlagCancel(){
        return this.flagCancel;
    }
    public void setFlagCancel(String flagCancel){
        this.flagCancel = flagCancel;
    }

    public String getPkEmpCancel(){
        return this.pkEmpCancel;
    }
    public void setPkEmpCancel(String pkEmpCancel){
        this.pkEmpCancel = pkEmpCancel;
    }

    public String getNameEmpCancel(){
        return this.nameEmpCancel;
    }
    public void setNameEmpCancel(String nameEmpCancel){
        this.nameEmpCancel = nameEmpCancel;
    }

    public Date getDateCancel(){
        return this.dateCancel;
    }
    public void setDateCancel(Date dateCancel){
        this.dateCancel = dateCancel;
    }

    public String getEuStatusFp(){
        return this.euStatusFp;
    }
    public void setEuStatusFp(String euStatusFp){
        this.euStatusFp = euStatusFp;
    }

    public String getCreator(){
        return this.creator;
    }
    public void setCreator(String creator){
        this.creator = creator;
    }

    public Date getCreateTime(){
        return this.createTime;
    }
    public void setCreateTime(Date createTime){
        this.createTime = createTime;
    }

    public String getModifier(){
        return this.modifier;
    }
    public void setModifier(String modifier){
        this.modifier = modifier;
    }

    public String getDelFlag(){
        return this.delFlag;
    }
    public void setDelFlag(String delFlag){
        this.delFlag = delFlag;
    }

    public Date getTs(){
        return this.ts;
    }
    public void setTs(Date ts){
        this.ts = ts;
    }

    public String getPkPvip(){
        return this.pkPvip;
    }
    public void setPkPvip(String pkPvip){
        this.pkPvip = pkPvip;
    }

    public String getPkIpNotice(){
        return this.pkIpNotice;
    }
    public void setPkIpNotice(String pkIpNotice){
        this.pkIpNotice = pkIpNotice;
    }

    public Long getIpTimes(){
        return this.ipTimes;
    }
    public void setIpTimes(Long ipTimes){
        this.ipTimes = ipTimes;
    }

    public Date getDateNotice(){
        return this.dateNotice;
    }
    public void setDateNotice(Date dateNotice){
        this.dateNotice = dateNotice;
    }

    public String getFlagOpt(){
        return this.flagOpt;
    }
    public void setFlagOpt(String flagOpt){
        this.flagOpt = flagOpt;
    }

    public String getDtLevelNs(){
        return this.dtLevelNs;
    }
    public void setDtLevelNs(String dtLevelNs){
        this.dtLevelNs = dtLevelNs;
    }

    public String getLevelNsName(){
        return this.levelNsName;
    }
    public void setLevelNsName(String levelNsName){
        this.levelNsName = levelNsName;
    }

    public String getDtLevelDise(){
        return this.dtLevelDise;
    }
    public void setDtLevelDise(String dtLevelDise){
        this.dtLevelDise = dtLevelDise;
    }

    public String getLevelDiseName(){
        return this.levelDiseName;
    }
    public void setLevelDiseName(String levelDiseName){
        this.levelDiseName = levelDiseName;
    }

    public String getDtLevelNutr(){
        return this.dtLevelNutr;
    }
    public void setDtLevelNutr(String dtLevelNutr){
        this.dtLevelNutr = dtLevelNutr;
    }

    public String getDtOutcomes(){
        return this.dtOutcomes;
    }
    public void setDtOutcomes(String dtOutcomes){
        this.dtOutcomes = dtOutcomes;
    }

    public String getFlagInfant(){
        return this.flagInfant;
    }
    public void setFlagInfant(String flagInfant){
        this.flagInfant = flagInfant;
    }

    public Long getQuanInfant(){
        return this.quanInfant;
    }
    public void setQuanInfant(Long quanInfant){
        this.quanInfant = quanInfant;
    }

    public String getEuStatusDoc(){
        return this.euStatusDoc;
    }
    public void setEuStatusDoc(String euStatusDoc){
        this.euStatusDoc = euStatusDoc;
    }

    public Date getDateCommitDoc(){
        return this.dateCommitDoc;
    }
    public void setDateCommitDoc(Date dateCommitDoc){
        this.dateCommitDoc = dateCommitDoc;
    }

    public String getFlagGa(){
        return this.flagGa;
    }
    public void setFlagGa(String flagGa){
        this.flagGa = flagGa;
    }

    public String getFlagGaNs(){
        return this.flagGaNs;
    }
    public void setFlagGaNs(String flagGaNs){
        this.flagGaNs = flagGaNs;
    }

    public String getDtIntype(){
        return this.dtIntype;
    }
    public void setDtIntype(String dtIntype){
        this.dtIntype = dtIntype;
    }

    public String getDtOuttype(){
        return this.dtOuttype;
    }
    public void setDtOuttype(String dtOuttype){
        this.dtOuttype = dtOuttype;
    }

    public String getPkDeptAdmit(){
        return this.pkDeptAdmit;
    }
    public void setPkDeptAdmit(String pkDeptAdmit){
        this.pkDeptAdmit = pkDeptAdmit;
    }

    public String getPkDeptNsAdmit(){
        return this.pkDeptNsAdmit;
    }
    public void setPkDeptNsAdmit(String pkDeptNsAdmit){
        this.pkDeptNsAdmit = pkDeptNsAdmit;
    }

    public String getPkDeptDis(){
        return this.pkDeptDis;
    }
    public void setPkDeptDis(String pkDeptDis){
        this.pkDeptDis = pkDeptDis;
    }

    public String getPkDeptNsDis(){
        return this.pkDeptNsDis;
    }
    public void setPkDeptNsDis(String pkDeptNsDis){
        this.pkDeptNsDis = pkDeptNsDis;
    }

    public String getFlagPrest(){
        return this.flagPrest;
    }
    public void setFlagPrest(String flagPrest){
        this.flagPrest = flagPrest;
    }

    public Date getDatePrest(){
        return this.datePrest;
    }
    public void setDatePrest(Date datePrest){
        this.datePrest = datePrest;
    }

    public String getPkEmpPrest(){
        return this.pkEmpPrest;
    }
    public void setPkEmpPrest(String pkEmpPrest){
        this.pkEmpPrest = pkEmpPrest;
    }

    public String getNameEmpPrest(){
        return this.nameEmpPrest;
    }
    public void setNameEmpPrest(String nameEmpPrest){
        this.nameEmpPrest = nameEmpPrest;
    }

    public String getPkPatrec(){
        return this.pkPatrec;
    }
    public void setPkPatrec(String pkPatrec){
        this.pkPatrec = pkPatrec;
    }

    public String getDiagAdmitTxt(){
        return this.diagAdmitTxt;
    }
    public void setDiagAdmitTxt(String diagAdmitTxt){
        this.diagAdmitTxt = diagAdmitTxt;
    }

    public String getDiagDisTxt(){
        return this.diagDisTxt;
    }
    public void setDiagDisTxt(String diagDisTxt){
        this.diagDisTxt = diagDisTxt;
    }

    public String getEuStatus(){
        return this.euStatus;
    }
    public void setEuStatus(String euStatus){
        this.euStatus = euStatus;
    }

    public String getPkEmpRefer(){
        return this.pkEmpRefer;
    }
    public void setPkEmpRefer(String pkEmpRefer){
        this.pkEmpRefer = pkEmpRefer;
    }

    public String getReferName(){
        return this.referName;
    }
    public void setReferName(String referName){
        this.referName = referName;
    }

    public String getPkEmpConsult(){
        return this.pkEmpConsult;
    }
    public void setPkEmpConsult(String pkEmpConsult){
        this.pkEmpConsult = pkEmpConsult;
    }

    public String getConsultName(){
        return this.consultName;
    }
    public void setConsultName(String consultName){
        this.consultName = consultName;
    }

    public String getPkEmpDirector(){
        return this.pkEmpDirector;
    }
    public void setPkEmpDirector(String pkEmpDirector){
        this.pkEmpDirector = pkEmpDirector;
    }

    public String getDirectorName(){
        return this.directorName;
    }
    public void setDirectorName(String directorName){
        this.directorName = directorName;
    }

    public Long getInHosDays(){
        return this.inHosDays;
    }
    public void setInHosDays(Long inHosDays){
        this.inHosDays = inHosDays;
    }

    public String getTempe(){
        return this.tempe;
    }
    public void setTempe(String tempe){
        this.tempe = tempe;
    }

    public String getPluse(){
        return this.pluse;
    }
    public void setPluse(String pluse){
        this.pluse = pluse;
    }

    public String getBreath(){
        return this.breath;
    }
    public void setBreath(String breath){
        this.breath = breath;
    }

    public String getSystolic(){
        return this.systolic;
    }
    public void setSystolic(String systolic){
        this.systolic = systolic;
    }

    public String getDiastolic(){
        return this.diastolic;
    }
    public void setDiastolic(String diastolic){
        this.diastolic = diastolic;
    } 
}
