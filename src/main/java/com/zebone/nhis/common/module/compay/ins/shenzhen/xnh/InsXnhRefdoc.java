package com.zebone.nhis.common.module.compay.ins.shenzhen.xnh;

import java.util.Date;

import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.Field.FieldType;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: INS_XNH_REFDOC - tabledesc 
 * 新农合转诊单
 * @since 2019-12-12 09:07:57
 */
@Table(value="INS_XNH_REFDOC")
public class InsXnhRefdoc   {

    /** PK_REFDOC - 主键 */
	@PK
	@Field(value="PK_REFDOC",id=KeyId.UUID)
    private String pkRefdoc;

    /** PK_ORG - 所属机构 */
	@Field(value="pk_org",userfield="pkOrg",userfieldscop=FieldType.INSERT)
    private String pkOrg;

    /** PK_HP - 医保类别 */
	@Field(value="PK_HP")
    private String pkHp;

    /** PK_PV - 就诊主键 */
	@Field(value="PK_PV")
    private String pkPv;

    /** PK_PI - 患者主键 */
	@Field(value="PK_PI")
    private String pkPi;

    /** CODE_ORG - 医院编码 */
	@Field(value="CODE_ORG")
    private String codeOrg;

    /** CODE_AREA_PV - 就医地地区编码 */
	@Field(value="CODE_AREA_PV")
    private String codeAreaPv;

    /** CODE_AREA_JOIN - 患者参合地区编码 */
	@Field(value="CODE_AREA_JOIN")
    private String codeAreaJoin;

    /** CODE_DOC - 转诊单号 */
	@Field(value="CODE_DOC")
    private String codeDoc;

    /** PERSONNO - 个人代码 */
	@Field(value="PERSONNO")
    private String personno;

    /** NAME_PI - 患者姓名 */
	@Field(value="NAME_PI")
    private String namePi;

    /** CODE_DISE - 疾病代码 */
	@Field(value="CODE_DISE")
    private String codeDise;

    /** NAME_DISE - 疾病名称 */
	@Field(value="NAME_DISE")
    private String nameDise;

    /** DATE_APPLY - 申请日期 */
	@Field(value="DATE_APPLY")
    private Date dateApply;

    /** NOTE_APPLY - 申请说明 */
	@Field(value="NOTE_APPLY")
    private String noteApply;

    /** CODE_OPERORG - 经办机构代码 */
	@Field(value="CODE_OPERORG")
    private String codeOperorg;

    /** CODE_OUTORG - 转出医院代码 */
	@Field(value="CODE_OUTORG")
    private String codeOutorg;

    /** CODE_INORG - 转入医院代码 */
	@Field(value="CODE_INORG")
    private String codeInorg;

    /** DT_TRAN_STATUS - 转诊状态代码 */
	@Field(value="DT_TRAN_STATUS")
    private String dtTranStatus;

    /** DATE_AUDIT - 审核日期 */
	@Field(value="DATE_AUDIT")
    private Date dateAudit;

    /** CODE_CHQX - 参合区（县）地区代码 */
	@Field(value="CODE_CHQX")
    private String codeChqx;

    /** NAME_XNHORG - 经办机构名称 */
	@Field(value="NAME_XNHORG")
    private String nameXnhorg;

    /** NAME_OUT_HOS - 转出医院名称 */
	@Field(value="NAME_OUT_HOS")
    private String nameOutHos;

    /** NAME_IN_HOS - 转入医院名称 */
	@Field(value="NAME_IN_HOS")
    private String nameInHos;

    /** NAME_AUDIT - 审核人姓名 */
	@Field(value="NAME_AUDIT")
    private String nameAudit;

    /** NOTE_AUDIT - 审核说明 */
	@Field(value="NOTE_AUDIT")
    private String noteAudit;

    /** NAME_CHQX - 参合区（县）地区名称 */
	@Field(value="NAME_CHQX")
    private String nameChqx;

    /** NAME_AUDIT_STATUS - 审核状态名称 */
	@Field(value="NAME_AUDIT_STATUS")
    private String nameAuditStatus;

    /** IDNO - 身份证号 */
	@Field(value="IDNO")
    private String idno;

    /** DINO_ADDR - 身份证号存储地址 */
	@Field(value="DINO_ADDR")
    private String dinoAddr;

    /** CODE_MEDI - 医疗证号 */
	@Field(value="CODE_MEDI")
    private String codeMedi;

    /** MEDI_CODE_ADDR - 医疗证号存储地址 */
	@Field(value="MEDI_CODE_ADDR")
    private String mediCodeAddr;

    /** DATE_PV - 就诊日期 */
	@Field(value="DATE_PV")
    private Date datePv;

    /** CODE_SEX - 性别代码 */
	@Field(value="CODE_SEX")
    private String codeSex;

    /** NAME_SEX - 性别名称 */
	@Field(value="NAME_SEX")
    private String nameSex;

    /** ORGCONT - 机构联系人 */
	@Field(value="ORGCONT")
    private String orgcont;

    /** ORGCONT_WAY - 机构联系方式 */
	@Field(value="ORGCONT_WAY")
    private String orgcontWay;

    /** ORGCONT_MAIL - 机构联系邮箱 */
	@Field(value="ORGCONT_MAIL")
    private String orgcontMail;

    /** BIRTHDAY - 出生日期 */
	@Field(value="BIRTHDAY")
    private Date birthday;

    /** DATE_CREATE - 创建时间 */
	@Field(value="DATE_CREATE")
    private Date dateCreate;

    /** DATE_UPDATE - 更新时间 */
	@Field(value="DATE_UPDATE")
    private Date dateUpdate;

    /** CODE_SINGLE_DISE - 单病种代码 */
	@Field(value="CODE_SINGLE_DISE")
    private String codeSingleDise;

    /** NAME_SINGLE_DISE - 单病种名称 */
	@Field(value="NAME_SINGLE_DISE")
    private String nameSingleDise;

    /** PATI_CONT - 患者联系人 */
	@Field(value="PATI_CONT")
    private String patiCont;

    /** PATI_CONT_NUMBER - 患者联系电话 */
	@Field(value="PATI_CONT_NUMBER")
    private String patiContNumber;

    /** FLAG_MZQZ - 民政救助标识 */
	@Field(value="FLAG_MZQZ")
    private String flagMzqz;

    /** FLAG_DBQZ - 大病救助标识 */
	@Field(value="FLAG_DBQZ")
    private String flagDbqz;

    /** CODE_FAMI - 家庭编码 */
	@Field(value="CODE_FAMI")
    private String codeFami;

    /** CODE_INORG2 - 转入医院 2 代码 */
	@Field(value="CODE_INORG2")
    private String codeInorg2;

    /** NAME_INORG2 - 转入医院 2 名称 */
	@Field(value="NAME_INORG2")
    private String nameInorg2;

    /** CODE_INORG3 - 转入医院 3 代码 */
	@Field(value="CODE_INORG3")
    private String codeInorg3;

    /** NAME_INORG3 - 转入医院 3 名称 */
	@Field(value="NAME_INORG3")
    private String nameInorg3;

    /** AMT_ACCU - 累计金额（元） */
	@Field(value="AMT_ACCU")
    private Double amtAccu;

    /** NAME_ACCOUNT - 账户名称 */
	@Field(value="NAME_ACCOUNT")
    private String nameAccount;

    /** CODE_ACCOUNT - 开户银行账号 */
	@Field(value="CODE_ACCOUNT")
    private String codeAccount;

    /** NAME_BANK - 开户银行名称 */
	@Field(value="NAME_BANK")
    private String nameBank;

    /** CODE_PAYMODE - 医疗付费方式代码 */
	@Field(value="CODE_PAYMODE")
    private String codePaymode;

    /** NAME_PAYMODE - 医疗付费方式名称 */
	@Field(value="NAME_PAYMODE")
    private String namePaymode;

    /** CODE_CHS - 参合省代码 */
	@Field(value="CODE_CHS")
    private String codeChs;

    /** NAME_CHS - 参合省名称 */
	@Field(value="NAME_CHS")
    private String nameChs;

    /** CODE_CHSH - 参合市代码 */
	@Field(value="CODE_CHSH")
    private String codeChsh;

    /** NAME_CHSH - 参合市名称 */
	@Field(value="NAME_CHSH")
    private String nameChsh;

    /** CREATOR - 创建人 */
	@Field(userfield="pkEmp",userfieldscop=FieldType.INSERT)
    private String creator;

    /** CREATE_TIME - 创建时间1 */
	@Field(value="CREATE_TIME",date=FieldType.INSERT)
    private Date createTime;

    /** DEL_FLAG - 删除标志 */
	@Field(value="DEL_FLAG")
    private String delFlag;

    /** TS - 时间戳 */
	@Field(date=FieldType.ALL)
    private Date ts;


    public String getPkRefdoc(){
        return this.pkRefdoc;
    }
    public void setPkRefdoc(String pkRefdoc){
        this.pkRefdoc = pkRefdoc;
    }

    public String getPkOrg(){
        return this.pkOrg;
    }
    public void setPkOrg(String pkOrg){
        this.pkOrg = pkOrg;
    }

    public String getPkHp(){
        return this.pkHp;
    }
    public void setPkHp(String pkHp){
        this.pkHp = pkHp;
    }

    public String getPkPv(){
        return this.pkPv;
    }
    public void setPkPv(String pkPv){
        this.pkPv = pkPv;
    }

    public String getPkPi(){
        return this.pkPi;
    }
    public void setPkPi(String pkPi){
        this.pkPi = pkPi;
    }

    public String getCodeOrg(){
        return this.codeOrg;
    }
    public void setCodeOrg(String codeOrg){
        this.codeOrg = codeOrg;
    }

    public String getCodeAreaPv(){
        return this.codeAreaPv;
    }
    public void setCodeAreaPv(String codeAreaPv){
        this.codeAreaPv = codeAreaPv;
    }

    public String getCodeAreaJoin(){
        return this.codeAreaJoin;
    }
    public void setCodeAreaJoin(String codeAreaJoin){
        this.codeAreaJoin = codeAreaJoin;
    }

    public String getCodeDoc(){
        return this.codeDoc;
    }
    public void setCodeDoc(String codeDoc){
        this.codeDoc = codeDoc;
    }

    public String getPersonno(){
        return this.personno;
    }
    public void setPersonno(String personno){
        this.personno = personno;
    }

    public String getNamePi(){
        return this.namePi;
    }
    public void setNamePi(String namePi){
        this.namePi = namePi;
    }

    public String getCodeDise(){
        return this.codeDise;
    }
    public void setCodeDise(String codeDise){
        this.codeDise = codeDise;
    }

    public String getNameDise(){
        return this.nameDise;
    }
    public void setNameDise(String nameDise){
        this.nameDise = nameDise;
    }

    public Date getDateApply(){
        return this.dateApply;
    }
    public void setDateApply(Date dateApply){
        this.dateApply = dateApply;
    }

    public String getNoteApply(){
        return this.noteApply;
    }
    public void setNoteApply(String noteApply){
        this.noteApply = noteApply;
    }

    public String getCodeOperorg(){
        return this.codeOperorg;
    }
    public void setCodeOperorg(String codeOperorg){
        this.codeOperorg = codeOperorg;
    }

    public String getCodeOutorg(){
        return this.codeOutorg;
    }
    public void setCodeOutorg(String codeOutorg){
        this.codeOutorg = codeOutorg;
    }

    public String getCodeInorg(){
        return this.codeInorg;
    }
    public void setCodeInorg(String codeInorg){
        this.codeInorg = codeInorg;
    }

    public String getDtTranStatus(){
        return this.dtTranStatus;
    }
    public void setDtTranStatus(String dtTranStatus){
        this.dtTranStatus = dtTranStatus;
    }

    public Date getDateAudit(){
        return this.dateAudit;
    }
    public void setDateAudit(Date dateAudit){
        this.dateAudit = dateAudit;
    }

    public String getCodeChqx(){
        return this.codeChqx;
    }
    public void setCodeChqx(String codeChqx){
        this.codeChqx = codeChqx;
    }

    public String getNameXnhorg(){
        return this.nameXnhorg;
    }
    public void setNameXnhorg(String nameXnhorg){
        this.nameXnhorg = nameXnhorg;
    }

    public String getNameOutHos(){
        return this.nameOutHos;
    }
    public void setNameOutHos(String nameOutHos){
        this.nameOutHos = nameOutHos;
    }

    public String getNameInHos(){
        return this.nameInHos;
    }
    public void setNameInHos(String nameInHos){
        this.nameInHos = nameInHos;
    }

    public String getNameAudit(){
        return this.nameAudit;
    }
    public void setNameAudit(String nameAudit){
        this.nameAudit = nameAudit;
    }

    public String getNoteAudit(){
        return this.noteAudit;
    }
    public void setNoteAudit(String noteAudit){
        this.noteAudit = noteAudit;
    }

    public String getNameChqx(){
        return this.nameChqx;
    }
    public void setNameChqx(String nameChqx){
        this.nameChqx = nameChqx;
    }

    public String getNameAuditStatus(){
        return this.nameAuditStatus;
    }
    public void setNameAuditStatus(String nameAuditStatus){
        this.nameAuditStatus = nameAuditStatus;
    }

    public String getIdno(){
        return this.idno;
    }
    public void setIdno(String idno){
        this.idno = idno;
    }

    public String getDinoAddr(){
        return this.dinoAddr;
    }
    public void setDinoAddr(String dinoAddr){
        this.dinoAddr = dinoAddr;
    }

    public String getCodeMedi(){
        return this.codeMedi;
    }
    public void setCodeMedi(String codeMedi){
        this.codeMedi = codeMedi;
    }

    public String getMediCodeAddr(){
        return this.mediCodeAddr;
    }
    public void setMediCodeAddr(String mediCodeAddr){
        this.mediCodeAddr = mediCodeAddr;
    }

    public Date getDatePv(){
        return this.datePv;
    }
    public void setDatePv(Date datePv){
        this.datePv = datePv;
    }

    public String getCodeSex(){
        return this.codeSex;
    }
    public void setCodeSex(String codeSex){
        this.codeSex = codeSex;
    }

    public String getNameSex(){
        return this.nameSex;
    }
    public void setNameSex(String nameSex){
        this.nameSex = nameSex;
    }

    public String getOrgcont(){
        return this.orgcont;
    }
    public void setOrgcont(String orgcont){
        this.orgcont = orgcont;
    }

    public String getOrgcontWay(){
        return this.orgcontWay;
    }
    public void setOrgcontWay(String orgcontWay){
        this.orgcontWay = orgcontWay;
    }

    public String getOrgcontMail(){
        return this.orgcontMail;
    }
    public void setOrgcontMail(String orgcontMail){
        this.orgcontMail = orgcontMail;
    }

    public Date getBirthday(){
        return this.birthday;
    }
    public void setBirthday(Date birthday){
        this.birthday = birthday;
    }

    public Date getDateCreate(){
        return this.dateCreate;
    }
    public void setDateCreate(Date dateCreate){
        this.dateCreate = dateCreate;
    }

    public Date getDateUpdate(){
        return this.dateUpdate;
    }
    public void setDateUpdate(Date dateUpdate){
        this.dateUpdate = dateUpdate;
    }

    public String getCodeSingleDise(){
        return this.codeSingleDise;
    }
    public void setCodeSingleDise(String codeSingleDise){
        this.codeSingleDise = codeSingleDise;
    }

    public String getNameSingleDise(){
        return this.nameSingleDise;
    }
    public void setNameSingleDise(String nameSingleDise){
        this.nameSingleDise = nameSingleDise;
    }

    public String getPatiCont(){
        return this.patiCont;
    }
    public void setPatiCont(String patiCont){
        this.patiCont = patiCont;
    }

    public String getPatiContNumber(){
        return this.patiContNumber;
    }
    public void setPatiContNumber(String patiContNumber){
        this.patiContNumber = patiContNumber;
    }

    public String getFlagMzqz(){
        return this.flagMzqz;
    }
    public void setFlagMzqz(String flagMzqz){
        this.flagMzqz = flagMzqz;
    }

    public String getFlagDbqz(){
        return this.flagDbqz;
    }
    public void setFlagDbqz(String flagDbqz){
        this.flagDbqz = flagDbqz;
    }

    public String getCodeFami(){
        return this.codeFami;
    }
    public void setCodeFami(String codeFami){
        this.codeFami = codeFami;
    }

    public String getCodeInorg2(){
        return this.codeInorg2;
    }
    public void setCodeInorg2(String codeInorg2){
        this.codeInorg2 = codeInorg2;
    }

    public String getNameInorg2(){
        return this.nameInorg2;
    }
    public void setNameInorg2(String nameInorg2){
        this.nameInorg2 = nameInorg2;
    }

    public String getCodeInorg3(){
        return this.codeInorg3;
    }
    public void setCodeInorg3(String codeInorg3){
        this.codeInorg3 = codeInorg3;
    }

    public String getNameInorg3(){
        return this.nameInorg3;
    }
    public void setNameInorg3(String nameInorg3){
        this.nameInorg3 = nameInorg3;
    }

    public Double getAmtAccu(){
        return this.amtAccu;
    }
    public void setAmtAccu(Double amtAccu){
        this.amtAccu = amtAccu;
    }

    public String getNameAccount(){
        return this.nameAccount;
    }
    public void setNameAccount(String nameAccount){
        this.nameAccount = nameAccount;
    }

    public String getCodeAccount(){
        return this.codeAccount;
    }
    public void setCodeAccount(String codeAccount){
        this.codeAccount = codeAccount;
    }

    public String getNameBank(){
        return this.nameBank;
    }
    public void setNameBank(String nameBank){
        this.nameBank = nameBank;
    }

    public String getCodePaymode(){
        return this.codePaymode;
    }
    public void setCodePaymode(String codePaymode){
        this.codePaymode = codePaymode;
    }

    public String getNamePaymode(){
        return this.namePaymode;
    }
    public void setNamePaymode(String namePaymode){
        this.namePaymode = namePaymode;
    }

    public String getCodeChs(){
        return this.codeChs;
    }
    public void setCodeChs(String codeChs){
        this.codeChs = codeChs;
    }

    public String getNameChs(){
        return this.nameChs;
    }
    public void setNameChs(String nameChs){
        this.nameChs = nameChs;
    }

    public String getCodeChsh(){
        return this.codeChsh;
    }
    public void setCodeChsh(String codeChsh){
        this.codeChsh = codeChsh;
    }

    public String getNameChsh(){
        return this.nameChsh;
    }
    public void setNameChsh(String nameChsh){
        this.nameChsh = nameChsh;
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
}