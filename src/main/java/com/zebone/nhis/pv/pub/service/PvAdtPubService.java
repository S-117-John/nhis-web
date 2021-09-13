package com.zebone.nhis.pv.pub.service;

import com.alibaba.fastjson.JSON;
import com.zebone.nhis.common.module.base.bd.price.BdHp;
import com.zebone.nhis.common.module.cn.ipdw.CnDiag;
import com.zebone.nhis.common.module.cn.ipdw.CnDiagDt;
import com.zebone.nhis.common.module.mybatis.MyBatisPage;
import com.zebone.nhis.common.module.pi.PiInsurance;
import com.zebone.nhis.common.module.pi.PiMaster;
import com.zebone.nhis.common.module.pv.*;
import com.zebone.nhis.common.module.pv.support.PvConstant;
import com.zebone.nhis.common.support.*;
import com.zebone.nhis.ex.pub.service.AdtPubService;
import com.zebone.nhis.ex.pub.service.FixedCostService;
import com.zebone.nhis.ex.pub.vo.DeptCgItemVo;
import com.zebone.nhis.ex.pub.vo.ExlistPubVo;
import com.zebone.nhis.ma.pub.platform.PlatFormSendUtils;
import com.zebone.nhis.ma.pub.support.ExtSystemProcessUtils;
import com.zebone.nhis.pi.pub.service.PiPubService;
import com.zebone.nhis.pi.pub.support.ClientUtils;
import com.zebone.nhis.pi.pub.vo.PiMasterParam;
import com.zebone.nhis.pv.pub.dao.PvInfoPubMapper;
import com.zebone.nhis.pv.pub.dao.RegPubMapper;
import com.zebone.nhis.pv.pub.vo.*;
import com.zebone.platform.common.support.NHISUUID;
import com.zebone.platform.common.support.User;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.core.spring.ServiceLocator;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.dao.support.Page;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * 住院相关服务
 *
 * @author wangpeng
 * @date 2017年5月18日
 */
@Service
public class PvAdtPubService {

    @Autowired
    private RegPubMapper regPubMapper;

    @Resource
    private AdtPubService adtPubService;

    @Resource
    private FixedCostService fixedCostService;

    @Resource
    private PiPubService piPubService;
//	// 用来控制手动事物
//	@Resource(name = "transactionManager")
//	private PlatformTransactionManager platformTransactionManager;

    @Autowired
    private PvInfoPubMapper pvInfoPubMapper;


    private Logger logger = LoggerFactory.getLogger("com.zebone");

    /**
     * 交易号：003004001002<br>
     * 保存入院登记信息<br>
     * <pre>
     * 1、保存 pv_encounter （患者就诊-就诊记录）：
     * 1.1  就诊号：ApplicationUtils.getCode("")
     * 1.2  就诊类型：3住院； 就诊状态：0 登记；  在院标志：？；  在院标志：0表示未出院结算；  退诊标志：？；  随访状态：0和null  未产生随访
     * 2、保存  pv_ip（住院属性表）
     * 2.1 住院次数: 取就诊记录中的最大的住院次数
     * 3、保存 pv_insurance（保险计划表）
     * 3.1 pv_encounter.pk_insu 作为 pv_insurance。pk_hp 保存
     * 4、保存 pv_diag（临床诊断表）
     * 5、若存在入院通知单，根据pk_pi更新入院通知单表pv_in_notice相应数据
     * 5.1 pv_ip_notice.eu_status = 2 ; pv_ip_notice.pk_pv_ip = pv_encounter.pk_pv
     * 6、更新pi_master（患者基本信息表）
     * 6.1 地址相关信息（户口，籍贯，现住址，出生地，工作单位，联系人等相关信息）
     * </pre>
     *
     * @param param
     * @return user
     * @throws
     * @author wangpeng
     * @date 2016年9月21日
     */
    public PvEncounter saveAdtReg(String param, IUser user) {
        AdtRegParam regParam = JsonUtil.readValue(param, AdtRegParam.class);
        PiMaster pi = regParam.getPiMaster();
        String isPiAdd = "1";
        if (CommonUtils.isEmptyString(pi.getPkPi())) {
            isPiAdd = "0";
        }
        PvEncounter pv = saveADTReg(regParam);
        //发送入院登记信息至平台
        Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
        paramMap.put("pkEmp", UserContext.getUser().getPkEmp());
        paramMap.put("nameEmp", UserContext.getUser().getNameEmp());
        paramMap.put("codeEmp", UserContext.getUser().getCodeEmp());
        paramMap.put("pkPv", pv.getPkPv());
        paramMap.put("isAdd", "0");
        paramMap.put("isPiAdd", isPiAdd);//发送患者消息开关；0-新增 1-修改
        paramMap.put("pkPi", pv.getPkPi());
        List<Map<String, Object>> piList = (List<Map<String, Object>>) ClientUtils.addEmpi(regParam.getPiMaster());
        PlatFormSendUtils.sendPvInMsg(paramMap);//改为入科时发送消息(入院登记消息)
        pv.setPiList(piList);
        paramMap = null;
        return pv;
    }

    /**
     * 患者入院登记后查询之前的入院记录里是否存在未结算记录
     * 交易号：003004001045
     *
     * @param param
     * @param user
     * @return
     */
    public String qryNotPayedHistoryPv(String param, IUser user) {
        String retInfo = "0";//不存在未结算记录
        @SuppressWarnings("unchecked")
        Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
        if (paramMap != null) {
            String pkPi = (String) paramMap.get("pkPi");
            String pkPv = (String) paramMap.get("pkPv");
            StringBuilder sb = new StringBuilder();
            Object[] objArray = null;
            sb.append("SELECT pe.pk_pv as pkpv ");
            sb.append("FROM   pv_encounter pe ");
            sb.append("WHERE  pe.eu_pvtype = '3' ");
            sb.append("AND    pe.eu_status IN ('0', '1', '2') ");
            sb.append("AND    pe.pk_pv != ? ");
            sb.append("AND    pe.pk_pi = ? ");
            objArray = new Object[]{pkPv, pkPi};
            String sql = sb.toString();
            List<Map<String, Object>> objList = DataBaseHelper.queryForList(sql, objArray);
            for (Map<String, Object> mapItem : objList) {
                retInfo = "1";//存在未结算记录
                break;
            }
        }
        return retInfo;
    }

    /**
     * 为ApplicationUtil.execService()调用方式提供方法
     *
     * @param param
     * @param user
     * @return
     */
    public PvEncounter saveADTReg(String param, IUser user) {
        AdtRegParam regParam = JsonUtil.readValue(param, AdtRegParam.class);
//		PvEncounter pvent=null;
//		try {
        PvEncounter pvent = saveADTReg(regParam);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
        return pvent;
    }

    /**
     * 入院登记校验数据接口
     *
     * @param pi
     * @param pv
     */
    private void checkAdtData(PiMaster pi, PvEncounter pv) {
        //获取检验并管处理service，对应配置文件lab.properties配置文件
        String processClass = ApplicationUtils.getPropertyValue("adt.processClass", "");
        if (!CommonUtils.isEmptyString(processClass)) {
            Object bean = ServiceLocator.getInstance().getBean(processClass);
            if (bean != null) {
                IAdtPsadService handler = (IAdtPsadService) bean;
                handler.dealAdtPsadMethod(pi, pv);
            }
        }
    }

    /**
     * 保存入院登记信息
     *
     * @param regParam
     * @return
     */
    public PvEncounter saveADTReg(AdtRegParam regParam) {
        User u = UserContext.getUser();
        if (null == regParam.getPiMaster()) {
            throw new BusException("请录入患者基本信息，再办理入院登记！");
        }
        if (null == regParam.getPvEncounter()) {
            throw new BusException("请录入患者就诊信息，再办理入院登记！");
        }
        PiMaster piMaster = regParam.getPiMaster();//获取前台传回的患者基本信息
        PvEncounter pvEncounter = regParam.getPvEncounter();//获取前台传回的患者就诊信息
        if (CommonUtils.isEmptyString(piMaster.getCodeIp())) {
            piMaster.setCodeIp(getCodeIp());//获取住院号
            logger.info("==============【" + piMaster.getNamePi() + "】获取第一次住院号:" + piMaster.getCodePi() + "============");
        }
//		// 关闭事务自动提交
//		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
//		def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
//		TransactionStatus status = platformTransactionManager.getTransaction(def);
//		try{
        //1、更新PiMaster
        //中山二院需求
        Integer piCon = (Integer) ExtSystemProcessUtils.processExtMethod("PiInfo", "isExistPi", piMaster.getPkPi(), piMaster.getCodeIp());
        if (piCon != null && piCon == 0) {//查询新系统是否存在该患者，因为查询旧系统患者时会根据旧系统的住院号删除掉新系统没有就诊记录的患者，故患者入院前先判断pi_master表的数据是否被删除
            piMaster.setPkPi(null);
        }
        if (CommonUtils.isEmptyString(piMaster.getPkPi())) {
            PiMasterParam mParam = new PiMasterParam();
            piMaster.setCodeEr(ApplicationUtils.getCode("0303"));
            mParam.setMaster(piMaster);
            //添加一条默认医保
            List<PiInsurance> insulist = new ArrayList<PiInsurance>();
            PiInsurance piInsu = new PiInsurance();
            Date dateIn = new Date();
            piInsu.setDateBegin(dateIn);
            piInsu.setDateEnd(DateUtils.strToDate(DateUtils.addDate(dateIn, 10, 1, "yyyyMMddHHmmss")));
            piInsu.setDelFlag("0");
            piInsu.setFlagDef("1");
            piInsu.setPkHp(pvEncounter.getPkInsu());
            piInsu.setSortNo((long) 1);
            insulist.add(piInsu);
            mParam.setInsuranceList(insulist);
            PiMaster master = piPubService.savePiMasterParamAutoCommint(mParam);//保存新增患者
        } else {
            //入院登记个性化业务接口
            checkAdtData(piMaster, pvEncounter);
            String sysParam = ApplicationUtils.getSysparam("BL0048", false);//通过参数控制判断
            if (!CommonUtils.isEmptyString(sysParam) && "1".equals(sysParam)) {
                //2、判断已存在患者是否入院状态,在住院中判断
                int count = DataBaseHelper.queryForScalar("select count(1) from pv_encounter where del_flag = '0' "
                        + "and eu_pvtype = '3' and (eu_status = '0' or eu_status = '1' or eu_status = '2') and pk_pi = ? and date_end is null", Integer.class, piMaster.getPkPi());
                if (count > 0) throw new BusException("该患者现为入院状态，无法再次办理入院登记！");
                //控制未结算或欠费患者再入院
                int arrearsPiCout = regPubMapper.getArrearsPi(piMaster.getPkPi());
                if (arrearsPiCout > 0) throw new BusException("该患者存在未结算或欠费的住院记录，不能再次入院！");
            }
            //健康卡是否需要读取外部接口 (老系统导入的患者，没有健康码，更新的时候需要注册健康码)
            String extHealth = ApplicationUtils.getSysparam("PI0019", false);
            if ("1".equals(extHealth)) {
                //如果不存在健康码进行健康码注册
                if (CommonUtils.isEmptyString(piMaster.getHicNo())) {
                    Map<String, Object> ehealthMap = new HashMap<>(16);
                    ehealthMap.put("piMaster", piMaster);
                    //电子健康码注册
                    Map<String, String> hicNo = (Map<String, String>) ExtSystemProcessUtils.processExtMethod("EHealthCode", "eHealthCodeEHC01", new Object[]{ehealthMap});
                    if (hicNo != null) {
                        piMaster.setHicNo(hicNo.get("hicNo"));
                        piMaster.setNote(piMaster.getNote() + hicNo.get("note"));
                    }
                }
            }
            DataBaseHelper.updateBeanByPk(piMaster, false);
        }
        //3、保存PvEncounter（插入）
        if (pvEncounter == null)
            pvEncounter = new PvEncounter();
        pvEncounter.setAddrcodeRegi(piMaster.getAddrcodeRegi());
        pvEncounter.setAddrRegi(piMaster.getAddrRegi());
        pvEncounter.setAddrRegiDt(piMaster.getAddrRegiDt());
        pvEncounter.setPostcodeRegi(piMaster.getPostcodeRegi());
        pvEncounter.setAddrcodeCur(piMaster.getAddrcodeCur());
        pvEncounter.setAddrCur(piMaster.getAddrCur());
        pvEncounter.setAddrCurDt(piMaster.getAddrCurDt());
        pvEncounter.setPostcodeCur(piMaster.getPostcodeCur());
        pvEncounter.setPkPi(piMaster.getPkPi());
        pvEncounter.setPkPicate(piMaster.getPkPicate().trim());//患者分类
        pvEncounter.setEuPvtype(PvConstant.ENCOUNTER_EU_PVTYPE_3);//就诊类型: 3 住院
        pvEncounter.setEuStatus(PvConstant.ENCOUNTER_EU_STATUS_0);//就诊状态: 0 登记
        //pvEncounter.setAgePv(DateUtils.getAgeByBirthday(piMaster.getBirthDate()));//年龄
        pvEncounter.setAgePv(ApplicationUtils.getAgeFormat(piMaster.getBirthDate(), pvEncounter.getDateBegin()));
        pvEncounter.setPkEmpReg(UserContext.getUser().getPkEmp());//登记人
        pvEncounter.setNameEmpReg(UserContext.getUser().getUserName());//登记人姓名
        pvEncounter.setDateReg(pvEncounter.getDateBegin());//登记时间
        pvEncounter.setEuStatusFp("0"); //随访状态
        pvEncounter.setEuPvmode(pvEncounter.getEuPvmode());
        pvEncounter.setCodeIp(piMaster.getCodeIp());
        DataBaseHelper.insertBean(pvEncounter);
        //4、保存 pv_ip
        PvIp pvIp = new PvIp();
        pvIp.setPkIpNotice(regParam.getPkInNotice());
        pvIp.setPkPv(pvEncounter.getPkPv());
        pvIp.setIpTimes(regParam.getIpTimes());
        pvIp.setPkDeptAdmit(regParam.getPkDeptAdmit());
        pvIp.setPkDeptNsAdmit(regParam.getPkDeptNsAdmit());
        //入院登记时是否同步修改dt_level_dise字段，0修改，1不修改
        String dtLevelDise = ApplicationUtils.getSysparam("PV0041", false);
        if ("1".equals(dtLevelDise)) {
            pvIp.setDtLevelDise(IDictCodeConst.DT_BQDJ_WD);
        } else {
            pvIp.setDtLevelDise(regParam.getDtLevelDise());
        }
        pvIp.setDtLevelDiseInit(regParam.getDtLevelDise());
        pvIp.setDtIntype(regParam.getDtIntype());
        pvIp.setFlagInfant("0");//婴儿标识必填
        pvIp.setQuanInfant(new Long(0));//婴儿数量必填
        pvIp.setFlagPrest("0"); //预结算标志
        pvIp.setCodeDiag(regParam.getCodeDiag());//诊断编码
        if (!CommonUtils.isEmptyString(regParam.getNameDiag())) {
            pvIp.setNameDiag(regParam.getNameDiag());
        } else {
            pvIp.setNameDiag(regParam.getDescDiag());
        }
        pvIp.setFlagInfected(regParam.getFlagInfected());//感染标志
        DataBaseHelper.insertBean(pvIp);
        //5、保存 pv_insurance
        PvInsurance pvInsurance = new PvInsurance();
        pvInsurance.setPkPv(pvEncounter.getPkPv());
        pvInsurance.setSortNo(1);//新建就诊记录时关联的医保计划也是首次插入
        pvInsurance.setPkHp(regParam.getPkInsu());
        pvInsurance.setFlagMaj("1"); //主医保计划标识
        DataBaseHelper.insertBean(pvInsurance);
        //6、辅医保
        String[] pkHps = regParam.getPkHps();
        if (pkHps != null) {
            DataBaseHelper.execute("delete from pv_insurance where pk_pv = ? and flag_maj = '0'", new Object[]{pvEncounter.getPkPv(),});
            int length = regParam.getPkHps().length;
            if (length > 0) {
                List<PvInsurance> insuList = new ArrayList<PvInsurance>();
                for (int i = 0; i < length; i++) {
                    PvInsurance insu = new PvInsurance();
                    insu.setPkOrg(u.getPkOrg());
                    insu.setPkPv(pvEncounter.getPkPv());
                    insu.setFlagMaj("0");
                    insu.setCreator(u.getPkEmp());
                    insu.setCreateTime(new Date());
                    insu.setModifier(u.getPkEmp());
                    insu.setDelFlag("0");
                    insu.setTs(new Date());
                    insu.setPkPvhp(NHISUUID.getKeyId());
                    insu.setSortNo(i + 1);
                    insu.setPkHp(pkHps[i]);
                    insuList.add(insu);
                }
                DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(PvInsurance.class), insuList);
            }
        }
        //获取系统参数PV0039。 入院登记时，入院诊断是否记录pv_diag表，0记录，1不记录
        String flagDiag = ApplicationUtils.getSysparam("PV0039", false);
        if (CommonUtils.isEmptyString(flagDiag) || !"1".equals(flagDiag)) {
            //7、保存诊断表 pv_diag
            PvDiag pvDiag = new PvDiag();
            pvDiag.setPkPv(pvEncounter.getPkPv());
            pvDiag.setSortNo(1);
            pvDiag.setFlagMaj("1");//默认主诊断
            pvDiag.setFlagCure("0");
            pvDiag.setFlagFinally("0");
            pvDiag.setFlagSusp("0");
            pvDiag.setFlagContagion("0");
            pvDiag.setPkDiag(regParam.getPkDiag());
            pvDiag.setDtDiagtype(regParam.getDtDiagtype());
            pvDiag.setDateDiag(regParam.getDateReg());
            if (!CommonUtils.isEmptyString(regParam.getNameDiag())) {
                pvDiag.setNameDiag(regParam.getNameDiag());
            } else {
                pvDiag.setNameDiag(regParam.getDescDiag());
            }
            if ("-1".equals(regParam.getPkDiag())) {
                pvDiag.setCodeIcd("-1");
            } else {
                pvDiag.setCodeIcd(regParam.getCodeDiag());
            }
            pvDiag.setDescDiag(regParam.getDescDiag());
            pvDiag.setPkEmpDiag(regParam.getPkEmpTre());
            pvDiag.setNameEmpDiag(regParam.getNameEmpTre());
            DataBaseHelper.insertBean(pvDiag);
        }
        //8、保存诊断历史相关：cn_diag、cn_diag_dt
        CnDiag cnDiag = new CnDiag();
        cnDiag.setEuPvtype(PvConstant.ENCOUNTER_EU_PVTYPE_3);
        cnDiag.setDescDiags(regParam.getDescDiag());
        if (!CommonUtils.isEmptyString(regParam.getNameDiag())) {
            cnDiag.setDescDiags(regParam.getNameDiag());
        } else {
            cnDiag.setDescDiags(regParam.getDescDiag());
        }
        cnDiag.setDateDiag(regParam.getDateReg());
        cnDiag.setPkPv(pvEncounter.getPkPv());
        DataBaseHelper.insertBean(cnDiag);
        CnDiagDt cnDiagDt = new CnDiagDt();
        cnDiagDt.setPkCndiag(cnDiag.getPkCndiag());
        cnDiagDt.setSortNo(1);
        cnDiagDt.setDtDiagtype(regParam.getDtDiagtype());
        cnDiagDt.setPkDiag(regParam.getPkDiag());
        if (!CommonUtils.isEmptyString(regParam.getNameDiag())) {
            cnDiagDt.setDescDiag(regParam.getNameDiag());
        } else {
            cnDiagDt.setDescDiag(regParam.getDescDiag());
        }
        cnDiagDt.setFlagMaj("1"); //主诊断标志
        cnDiagDt.setFlagSusp("0"); //疑似标志
        cnDiagDt.setFlagInfect("0"); //传染病标志
        cnDiagDt.setFlagFinally("0"); //确诊标志
        cnDiagDt.setFlagCure("0"); //治愈标志
        DataBaseHelper.insertBean(cnDiagDt);
        //9、更新pv_in_notice
        PvIpNotice notice = DataBaseHelper.queryForBean("select * from pv_ip_notice where pk_in_notice = ?", PvIpNotice.class, regParam.getPkInNotice());
        if (notice != null) {
            notice.setPkHp(regParam.getPkInsu()); //医保计划
            notice.setPkPvIp(pvEncounter.getPkPv()); //入院后对应的入院就诊主键
            notice.setEuStatus(PvConstant.NOTICE_EU_STATUS_2); //通知单状态 2 患者入院
            DataBaseHelper.updateBeanByPk(notice, false);
        }
        //10、计算距离上次入院天数
        String sql = "select * from pv_encounter where pk_pi = ? and del_flag = '0' and date_end is not null order by ts desc ";
        List<PvEncounter> list = DataBaseHelper.queryForList(sql, PvEncounter.class, pvEncounter.getPkPi());
        if (list != null && list.size() > 0) {
            Calendar aCalendar = Calendar.getInstance();
            aCalendar.setTime(list.get(0).getDateBegin());
            Long tempDay1 = aCalendar.getTimeInMillis();
            //int day1 = aCalendar.get(Calendar.DAY_OF_YEAR);
            aCalendar.setTime(pvEncounter.getDateBegin());
            Long tempDay2 = aCalendar.getTimeInMillis();
            Long result = tempDay2 - tempDay1;
            aCalendar.setTimeInMillis(result);
            int temp = aCalendar.get(Calendar.DAY_OF_YEAR);
            //int day2 = aCalendar.get(Calendar.DAY_OF_YEAR);
            //int day = day2 - day1;
            pvEncounter.setDayFromLast(temp);
        } else {
            pvEncounter.setDayFromLast(-1);//表示该患者从未入院
        }
        //2018-09-21 中二需求 ： 广州公医或单位医保患者，同时更新ins_gzgy_pv表
        ExtSystemProcessUtils.processExtMethod("medicalInsurance", "AddInsGzgyPv", regParam, piMaster, pvEncounter);
//		String paramGZGY = ApplicationUtils.getSysparam("BL0023", false);// 获取是否启用广州公用医保
//		if("1".equals(paramGZGY))
//			AddInsGzgyPv(regParam, piMaster, pvEncounter);
        //2018-10-15中二需求：入院登记时，根据患者类型关联的特诊标志更新就诊记录。
        //1.读取患者类型关联的特诊标志,pi_cate表    2.更新就诊记录表pv_encounter特诊标志flag_spec
        updateRecordByFlagsp(pvEncounter);
//			platformTransactionManager.commit(status); // 提交事务
//		} catch (Exception e) {
//			platformTransactionManager.rollback(status); // 添加失败 回滚事务；
//			e.printStackTrace();
//			throw new RuntimeException();
////			throw new BusException("保存患者信息失败：" + e.getMessage());
//		}
        pvEncounter.setCodePi(piMaster.getCodePi());
        return pvEncounter;
    }

    //获取住院号   获取规则：1）首先从被退院的患者信息中获取； 2）如果获取的返回值为空，从系统编码规则接口中获取。
    private String getCodeIp() {
        String codeIp = pvInfoPubMapper.getCodeiP();
        if (!CommonUtils.isEmptyString(codeIp)) {
            DataBaseHelper.execute("update pi_master set code_ip=null  where code_ip=?", new Object[]{codeIp});
        } else {
            codeIp = ApplicationUtils.getCode(SysConstant.ENCODERULE_CODE_ZYBL);
            logger.info("==============PvAdtPubService.getCodeIp获取住院号:" + codeIp + "============");
        }
        return codeIp;
    }

    /**
     * 2018-11-26 中二需求  入院登记时判断该患者是否为欠费患者
     *
     * @param param
     * @param user
     * @return
     */
    public int isArrearsPi(String param, IUser user) {
        String pkPi = JsonUtil.getFieldValue(param, "pkPi");
        //控制未结算或欠费患者再入院
        int arrearsPiCout = regPubMapper.getArrearsPi(pkPi);
        return arrearsPiCout;
    }

    /**
     * 2018-10-15中二需求：入院登记时，根据患者类型关联的特诊标志更新就诊记录。
     * 1.读取患者类型关联的特诊标志pi_cate表
     * 2.更新就诊记录表pv_encounter特诊标志flag_spec
     *
     * @param pvEncounter
     */
    private void updateRecordByFlagsp(PvEncounter pvEncounter) {
        String flagSpec = "";
        Map<String, Object> flagSpecMap = DataBaseHelper.queryForMap("select pc.flag_spec from pi_cate pc where pc.pk_picate=?", pvEncounter.getPkPicate());
        if (flagSpecMap != null && flagSpecMap.get("flagSpec") != null) {
            flagSpec = flagSpecMap.get("flagSpec").toString();// 急诊使用
            DataBaseHelper.execute("update pv_encounter set flag_spec=? where pk_pv=?", new Object[]{flagSpec, pvEncounter.getPkPv()});
        }
    }


    /**
     * 已弃用
     *
     * @param regParam
     * @return
     */
    public PvEncounter saveAdtReg(AdtRegParam regParam) {
        User u = UserContext.getUser();
        String pkPi = regParam.getPkPi();
        Map<String, Object> flagInfant = DataBaseHelper.queryForMap("select flag_infant from pv_ip ip inner join pv_encounter pv on pv.pk_pv = ip.pk_pv where pv.pk_pi=? ", pkPi);
        if (flagInfant != null && !flagInfant.get("flagInfant").equals("1")) {
            //判断患者是否入院状态,在住院中判断
            int count = DataBaseHelper.queryForScalar("select count(1) from pv_encounter where eu_pvtype = '3' and del_flag = '0' and (eu_status = '0' or eu_status = '1' or eu_status = '2') and pk_pi = ?", Integer.class, pkPi);
            if (count > 0) {
                throw new BusException("该患者现为入院状态，无法再次办理入院登记！");
            }
        }
        //保存 pv_encounter
        PvEncounter pvEncounter = new PvEncounter();
        //String codePv = ApplicationUtils.getCode("003"); //住院流水号
        PiMaster master = DataBaseHelper.queryForBean("select * from pi_master where del_flag = '0' and pk_pi = ?", PiMaster.class, pkPi);
        if (master == null) {
            throw new BusException("参数错误，无法获取患者基本信息！");
        }
        master.setNameRel(regParam.getNameRel()); //联系人
        master.setTelRel(regParam.getTelRel()); //联系电话
        //DataBaseHelper.updateBeanByPk(master, false);
        pvEncounter.setPkPi(pkPi);
        pvEncounter.setCodePv(regParam.getCodePv());
        pvEncounter.setEuPvtype(PvConstant.ENCOUNTER_EU_PVTYPE_3);//就诊类型: 3 住院
        pvEncounter.setEuStatus(PvConstant.ENCOUNTER_EU_STATUS_0);//就诊状态: 0 登记
        pvEncounter.setDateBegin(regParam.getDateReg());
        pvEncounter.setFlagIn("0");
        pvEncounter.setFlagSettle("0"); //结算标志
        pvEncounter.setNamePi(master.getNamePi());
        pvEncounter.setDtSex(master.getDtSex());
        pvEncounter.setAgePv(DateUtils.getAgeByBirthday(master.getBirthDate(), pvEncounter.getDateBegin()));//年龄
        if (StringUtils.isNotEmpty(regParam.getAddress())) {
            pvEncounter.setAddress(regParam.getAddress());
            master.setAddress(regParam.getAddress());
        }
        if (StringUtils.isNotEmpty(regParam.getDtMarry())) {
            pvEncounter.setDtMarry(regParam.getDtMarry());
            master.setDtMarry(regParam.getDtMarry());
        }
        if (regParam.getHeight() != null) {
            pvEncounter.setHeight(regParam.getHeight()); //身高
        }
        pvEncounter.setPkEmpTre(regParam.getPkEmpTre());
        pvEncounter.setNameEmpTre(regParam.getNameEmpTre());
        pvEncounter.setDateReg(regParam.getDateReg());
        pvEncounter.setPkInsu(regParam.getPkInsu());
        pvEncounter.setPkPicate(regParam.getPkPicate());
        pvEncounter.setPkEmpReg(UserContext.getUser().getPkEmp());
        pvEncounter.setNameEmpReg(UserContext.getUser().getUserName());
        pvEncounter.setFlagCancel("0"); //退诊标志
        pvEncounter.setEuStatusFp("0"); //随访状态
        pvEncounter.setPkDept(regParam.getPkDeptAdmit());
        pvEncounter.setPkDeptNs(regParam.getPkDeptNsAdmit());
        DataBaseHelper.insertBean(pvEncounter);
        //更新患者信息表
        DataBaseHelper.updateBeanByPk(master, false);
        //保存 pv_ip
        PvIp pvIp = new PvIp();
        pvIp.setPkIpNotice(regParam.getPkInNotice());
        pvIp.setPkPv(pvEncounter.getPkPv());
        pvIp.setIpTimes(regParam.getIpTimes());
        pvIp.setPkDeptAdmit(regParam.getPkDeptAdmit());
        pvIp.setPkDeptNsAdmit(regParam.getPkDeptNsAdmit());
        pvIp.setFlagInfant("0");//婴儿标识必填
        pvIp.setQuanInfant(new Long(0));//婴儿数量必填
        //pvIp.setFlagOpt(flagOpt); //是否手术标志
        pvIp.setDtLevelDise(regParam.getDtLevelDise());
        pvIp.setDtIntype(regParam.getDtIntype());
        pvIp.setFlagPrest("0"); //预结算标志
        DataBaseHelper.insertBean(pvIp);
        //保存 pv_insurance
        PvInsurance pvInsurance = new PvInsurance();
        pvInsurance.setPkPv(pvEncounter.getPkPv());
        //新建就诊记录时关联的医保计划也是首次插入
        pvInsurance.setSortNo(1);
        pvInsurance.setPkHp(regParam.getPkInsu());
        pvInsurance.setFlagMaj("1"); //主医保计划标识
        DataBaseHelper.insertBean(pvInsurance);
        //辅医保
        String[] pkHps = regParam.getPkHps();
        if (pkHps != null) {
            DataBaseHelper.execute("delete from pv_insurance where pk_pv = ? and flag_maj = '0'", new Object[]{pvEncounter.getPkPv(),});
            int length = regParam.getPkHps().length;
            if (length > 0) {
                List<PvInsurance> insuList = new ArrayList<PvInsurance>();
                for (int i = 0; i < length; i++) {
                    PvInsurance insu = new PvInsurance();
                    insu.setPkOrg(u.getPkOrg());
                    insu.setPkPv(pvEncounter.getPkPv());
                    insu.setFlagMaj("0");
                    insu.setCreator(u.getPkEmp());
                    insu.setCreateTime(new Date());
                    insu.setModifier(u.getPkEmp());
                    insu.setDelFlag("0");
                    insu.setTs(new Date());
                    insu.setPkPvhp(NHISUUID.getKeyId());
                    insu.setSortNo(i + 1);
                    insu.setPkHp(pkHps[i]);
                    insuList.add(insu);
                }
                DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(PvInsurance.class), insuList);
            }
        }
        //保存诊断表 pv_diag
        PvDiag pvDiag = new PvDiag();
        pvDiag.setPkPv(pvEncounter.getPkPv());
        pvDiag.setPkDiag(regParam.getPkDiag());
        pvDiag.setSortNo(1);
        pvDiag.setDtDiagtype(regParam.getDtDiagtype());
        pvDiag.setDateDiag(regParam.getDateReg());
        pvDiag.setDescDiag(regParam.getDescDiag());
        pvDiag.setFlagMaj("1");//默认主诊断
        DataBaseHelper.insertBean(pvDiag);
        //保存诊断历史相关：cn_diag、cn_diag_dt
        CnDiag cnDiag = new CnDiag();
        cnDiag.setEuPvtype(PvConstant.ENCOUNTER_EU_PVTYPE_3);
        cnDiag.setPkPv(pvEncounter.getPkPv());
        cnDiag.setDescDiags(regParam.getDescDiag());
        cnDiag.setDateDiag(regParam.getDateReg());
        DataBaseHelper.insertBean(cnDiag);
        CnDiagDt cnDiagDt = new CnDiagDt();
        cnDiagDt.setPkCndiag(cnDiag.getPkCndiag());
        cnDiagDt.setSortNo(1);
        cnDiagDt.setDtDiagtype(regParam.getDtDiagtype());
        cnDiagDt.setPkDiag(regParam.getPkDiag());
        cnDiagDt.setDescDiag(regParam.getDescDiag());
        cnDiagDt.setFlagMaj("1"); //主诊断标志
        cnDiagDt.setFlagSusp("0"); //疑似标志
        cnDiagDt.setFlagInfect("0"); //传染病标志
        cnDiagDt.setFlagFinally("0"); //确诊标志
        cnDiagDt.setFlagCure("0"); //治愈标志
        DataBaseHelper.insertBean(cnDiagDt);
        //更新pv_in_notice
        PvIpNotice notice = DataBaseHelper.queryForBean("select * from pv_ip_notice where pk_in_notice = ?", PvIpNotice.class, regParam.getPkInNotice());
        if (notice != null) {
            notice.setPkHp(regParam.getPkInsu()); //医保计划
            notice.setPkPvIp(pvEncounter.getPkPv()); //入院后对应的入院就诊主键
            notice.setEuStatus(PvConstant.NOTICE_EU_STATUS_2); //通知单状态 2 患者入院
            DataBaseHelper.updateBeanByPk(notice, false);
        }
        return pvEncounter;
    }

    /**
     * 交易号：003004001005<br>
     * 出院<br>
     * <pre>
     * 1、判断是否可以办理出院
     * 1.1 存在未停医嘱或未做废(新开医嘱)不允许办理出院；
     * 1.2 医嘱有效，检验申请单处在申请、预约、采集时不允许出院；
     * 1.3 医嘱有效，检查申请单处在申请、预约时不允许出院；
     * 1.4 医嘱有效，手术申请单未完成时不允许出院；
     * 1.5 医嘱有效，输血申请单未完成不允许出院；
     * 1.6 医嘱有效，医技辅申请单未完成不允许出院；
     * 1.7 物品请领单已经发放完成或者停发，否则不允许出院；
     * 1.8 存在包床信息，未退包床不允许出院；
     * 2、办理出院后更新的表结构
     * 2.1 更新就诊表pv_encounter：eu_status=2  flag_in=0 date_end=param:dateEnd；
     * 2.2 更新住院属性表pv_ip：dt_outcomes=param:dtOutcomes  pk_dept_dis=pv_encounter.pk_dept   pk_dept_ns_dis=pv_encounter.pk_dept_ns  dt_outtype=param:dtOuttype；
     * 2.3 更新医护人员表pv_staff：date_end=param:dateEnd；
     * 2.4 更新医疗组表date_end：date_end=param:dateEnd；
     * 2.5 更新转科表pv_adt:date_end=param:dateEnd   pk_emp_end=user:id  name_emp_end=user:name  flag_dis=1
     * 2.6 更新床位记录表pv_bed：date_end=param:dateEnd  pk_emp_end=user:id  name_emp_end=user:name
     * 2.7 更新床位表bd_res_bed：dt_sex=？ flag_active=？ eu_status=？ flag_ocupy=？ pk_dept_used=？ pk_pi=？
     * 2.8 更新入院通知单为出院PV_IP_NOTICE
     * </pre>
     *
     * @param param
     * @param user
     * @return
     * @throws
     * @author wangpeng
     * @date 2016年9月29日
     */
    @SuppressWarnings("unchecked")
    public void outHospital(String param, IUser user) {
        AdtOutParam outParam = JsonUtil.readValue(param, AdtOutParam.class);
        this.execOutHospital(outParam, user);
        //中山二院--调用平安APP接口推送消息
        //ExtSystemProcessUtils.processExtMethod("PAAPP", "noticeOutHosp", param);
        //发送出院信息至平台
        Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
        paramMap.put("pkPv", paramMap.get("pkPv"));
        paramMap.put("pkEmp", UserContext.getUser().getPkEmp());
        paramMap.put("nameEmp", UserContext.getUser().getNameEmp());
        paramMap.put("codeEmp", UserContext.getUser().getCodeEmp());
        paramMap.put("status", "add");
        PlatFormSendUtils.sendPvOutMsg(paramMap);
        paramMap = null;
    }

    /**
     * 办理出院功能服务（yangxue添加）
     *
     * @param outParam
     * @param user
     */
    public void execOutHospital(AdtOutParam outParam, IUser user) {
        User u = (User) user;
        String pkPv = outParam.getPkPv();
        //校验是否存在记费发生日期大于出院时间的费用明细(超出出院时间收退为0的不算)
        Map<String, String> mapA = adtPubService.countBlIpDtAfterDateEnd(outParam.getPkPv(), outParam.getDateEnd());
        boolean isHave = MapUtils.isNotEmpty(mapA) &&
                MapUtils.getString(mapA, "amout").compareTo("0.00") > 0 &&
                MapUtils.getString(mapA, "cou").compareTo("0.00")>0;
        if (isHave) {
            throw new BusException("该患者存在" +  MapUtils.getString(mapA, "cou") + "条费用日期大于出院日期的费用，请先核查处理！");
        }
        //查看该患者是不是在产房就诊，在产房不允许出科
        Map<String, Object> mapLab = DataBaseHelper.queryForMap("select eu_status from pv_labor where pk_pv = ? and (eu_status = '1' or eu_status = '0') "
                , new Object[]{pkPv});
        if (mapLab != null && "1".equals(mapLab.get("euStatus"))) {
            throw new BusException("该患者已在产房就诊，不允许出院！");
        }
        PvEncounter encounter = regPubMapper.getPvEncounterByPkPv(pkPv);
        //调用固定计费服务，yangxue添加
        List<String> pk_pvs = new ArrayList<String>();
        pk_pvs.add(pkPv);
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("pkPvs", pk_pvs);
        paramMap.put("pkDeptNs", encounter.getPkDeptNs());
        paramMap.put("pkOrg", encounter.getPkOrg());
        List<DeptCgItemVo> cgList = fixedCostService.execFixedCharge(paramMap, true, u, outParam.getDateEnd());
        if (cgList != null && cgList.size() > 0) {
            fixedCostService.saveFixedCost(encounter.getPkDeptNs(), 0, cgList, true, false, outParam.getDateEnd());
        }
        //2019-07-13 婴儿则不校验
        if ("1".equals(outParam.getFlagInfant())) {
            //2019-04-06 出院时，添加是否录入出院诊断校验
            int cnt = DataBaseHelper.queryForScalar("select count(1) from pv_diag where pk_pv=? and dt_diagtype='0109' and del_flag='0'"
                    , Integer.class, new Object[]{pkPv});
            if (cnt < 1)
                throw new BusException("该患者没有出院诊断，请联系主管医生！！！");
        }
        //出院校验
        String flagCheck = outParam.getFlagCheck();
        if ("1".equals(flagCheck)) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("pkPv", pkPv);
            String error = this.OutChkData(outParam, map, user);//出院校验
            if (!CommonUtils.isEmptyString(error)) {
                throw new BusException(error);
            }
        }
        Map<String, Object> mapParam = new HashMap<String, Object>();
        mapParam.put("modifier", u.getPkEmp());
        mapParam.put("ts", new Date());
        mapParam.put("pkPv", pkPv);
        mapParam.put("dateEnd", outParam.getDateEnd());
        mapParam.put("pkPi", encounter.getPkPi());
        mapParam.put("dateNotice", new Date());//由原来的出院时间 => 办理出院时间【2019-05-30】
        mapParam.put("dtOutcomes", outParam.getDtOutcomes());
        mapParam.put("dtSttypeIns", outParam.getDtSttypeIns());
        mapParam.put("pkDeptDis", encounter.getPkDept());
        mapParam.put("pkDeptNsDis", encounter.getPkDeptNs());
        mapParam.put("dtOuttype", outParam.getDtOuttype());
        mapParam.put("nameEmp", u.getNameEmp());
        mapParam.put("pkBedAn", null);//如果为婴儿清空婴儿陪护占床主键
        mapParam.put("euStatus","3");
        //更新表结构
        //更新就诊记录表pv_encounter:就诊状态改为‘2’护士站出院
        DataBaseHelper.update("update pv_encounter set eu_status = '2', flag_in = '0', date_end =:dateEnd, modifier =:modifier, ts =:ts where pk_pv =:pkPv", mapParam);
        //更新住院属性表pv_ip【2019-05-30 更新：dateNotice 当前时间，pk_emp_notice、name_emp_notice 当前操作人】
        DataBaseHelper.update("update pv_ip set dt_outcomes =:dtOutcomes, pk_bed_an =:pkBedAn, "
                + (null != mapParam.get("dtSttypeIns") && !CommonUtils.isEmptyString(mapParam.get("dtSttypeIns").toString()) ? "dt_sttype_ins =:dtSttypeIns, " : "")
                + " pk_dept_dis =:pkDeptDis, pk_dept_ns_dis =:pkDeptNsDis,"
                + " date_notice =:dateNotice, pk_emp_notice =:modifier, name_emp_notice =:nameEmp, "
                + " dt_outtype =:dtOuttype, modifier =:modifier, ts =:ts where pk_pv =:pkPv", mapParam);
        //更新医护人员表pv_staff
        DataBaseHelper.update("update pv_staff set date_end =:dateEnd, modifier =:modifier, ts =:ts where pk_pv =:pkPv and date_end is null", mapParam);
        //更新医疗组pv_clinic_group
        DataBaseHelper.update("update pv_clinic_group set date_end =:dateEnd, modifier =:modifier, ts =:ts where pk_pv =:pkPv and date_end is null", mapParam);
        //更新转科表pv_adt
        DataBaseHelper.update("update pv_adt set date_end =:dateEnd, pk_emp_end =:modifier, name_emp_end =:nameEmp, flag_dis = '1', modifier =:modifier, ts =:ts where pk_pv =:pkPv and date_end is null", mapParam);
        //更新就诊床位记录表pv_bed
        DataBaseHelper.update("update pv_bed set date_end =:dateEnd, pk_emp_end =:modifier, name_emp_end =:nameEmp, modifier =:modifier, ts =:ts where pk_pv =:pkPv and date_end is null", mapParam);
        //更新婴儿信息
        int infCnt = DataBaseHelper.update("update pv_infant set eu_status_adt = '2' where pk_pv_infant =:pkPv and del_flag ='0' ", mapParam);
        //更新床位表bd_res_bed   2019-08-26 中山二院需求：患者出院后禁用床位类型为临时床（06）的床位
        StringBuffer upBedSql = new StringBuffer("update bd_res_bed set flag_ocupy = '0', eu_status = '01',pk_pi = null,pk_dept_used = null,modifier =:modifier, ts =:ts");
        //2020-02-24 根据BD0013参数判断，婴儿出院时婴儿床只更新床位bd_res_bed的状态和清空患者信息，不删除和关闭床位，在婴儿出院后床位一览卡还可以显示该床位。
       //更新入院通知单
        DataBaseHelper.update("update PV_IP_NOTICE set EU_STATUS=:euStatus,DATE_END=:dateEnd,modifier=:modifier,ts=:ts  where PK_PV_IP=:pkPv", mapParam);
        if (infCnt > 0 && !"0".equals(ApplicationUtils.getSysparam("BD0013", false)))
            upBedSql.append(" ,del_flag = '1',flag_active = '1' ");
        else
            upBedSql.append(",flag_active=(case when dt_bedtype='06' then '0' else flag_active end)");
        upBedSql.append(" where pk_pi =:pkPi ");
        DataBaseHelper.update(upBedSql.toString(), mapParam);
    }

    /**
     * 出院校验
     *
     * @param outParam
     * @return
     */
    @SuppressWarnings("rawtypes")
    private String OutChkData(AdtOutParam outParam, Map<String, Object> map, IUser user) {
        Map<String, Object> result = new HashMap<String, Object>();
        if (null != outParam.getItemIndex() && "1".endsWith(outParam.getItemIndex())) {
            map.put("dateEnd", DateUtils.getDateStr(outParam.getDateEnd()) + "000000");
            map.put("isChkNs", outParam.getFlagInfant());
            result = adtPubService.getDeptOutVerfyDataBySyx(map, user);
        } else {
            result = adtPubService.getDeptOutVerfyData(map, user);
        }
        if (result != null) {
            //护士站出院时提醒而不限制的未完成业务页签
            String outHospitalVerify = ApplicationUtils.getSysparam("EX0071", false);
            List<String> splitVerify = CommonUtils.isEmptyString(outHospitalVerify) ? null : Arrays.asList(outHospitalVerify.split(","));
            //如果为空或者包含0，全部验证
            boolean verifyFlag = CommonUtils.isNotNull(splitVerify) && !splitVerify.contains("0");
            boolean flag = verifyFlag && splitVerify.contains("1");
            if (!flag && CommonUtils.isNotNull(result.get("ordData")) && ((List) result.get("ordData")).size() > 0) {
                return "存在未停未作废医嘱，不允许出院！";
            }
            flag = verifyFlag && splitVerify.contains("2");
            if (!flag && CommonUtils.isNotNull(result.get("exListData")) && ((List) result.get("exListData")).size() > 0) {
                for (ExlistPubVo ex : (List<ExlistPubVo>) result.get("exListData")) {
                    //未执行的不作限制
                    if ("0".equals(ex.getEuStatus())) {
                        return "存在未执行执行单，不允许出院！";
                    }
                }
            }
            flag = verifyFlag && splitVerify.contains("3");
            if (!flag && CommonUtils.isNotNull(result.get("risData")) && ((List) result.get("risData")).size() > 0) {
                return "存在未执行医技项目，不允许出院！";
            }
            flag = verifyFlag && splitVerify.contains("4");
            if (!flag && CommonUtils.isNotNull(result.get("apData")) && ((List) result.get("apData")).size() > 0) {
                return "存在未完成请领单，不允许出院！";
            }
            flag = verifyFlag && splitVerify.contains("5");
            if (!flag && CommonUtils.isNotNull(result.get("opData")) && ((List) result.get("opData")).size() > 0) {
                return "存在未完成的手术申请单，不允许出院！";
            }
            flag = verifyFlag && splitVerify.contains("6");
            if (!flag && CommonUtils.isNotNull(result.get("packBedData")) && ((List) result.get("packBedData")).size() > 0) {
                return "存在包床记录，不允许出院！";
            }
            flag = verifyFlag && splitVerify.contains("7");
            if (!flag && (CommonUtils.isNotNull(result.get("hpCgChkData")) && ((List) result.get("hpCgChkData")).size() > 0)
                    || (CommonUtils.isNotNull(result.get("groupCgChkData")) && ((List) result.get("groupCgChkData")).size() > 0)) {
                return "存在不符合医保政策的收费项目，不允许出院！";
            }
            if (CommonUtils.isNotNull(result.get("cpData")) && ((List) result.get("cpData")).size() > 0) {
                return "存在未完成的在径医嘱，不允许出院！";
            }
            if (CommonUtils.isNotNull(result.get("infData")) && ((List) result.get("infData")).size() > 0) {
                return "存在同病区在诊婴儿，不允许出院！";
            }
        }
        return null;
    }

    /**
     * 保存留观转住院 - 入院登记
     *
     * @param user
     * @param ts
     * @param pkPvOld
     * @return
     */
    public PvEncounter saveErReg(User user, Date ts, String pkPvOld, Map<String, Object> paramMap) {
        PvEncounter oldPv = regPubMapper.getPvEncounterByPkPv(pkPvOld);
        if (null == oldPv)
            throw new BusException("未获取到患者的 就诊信息，无法完成【留观转住院】操作！");
        PiMaster oldPi = regPubMapper.getPiMasterNoPhoto(oldPv.getPkPi());
        if (null == oldPi)
            throw new BusException("未获取到患者的 基本信息，无法完成【留观转住院】操作！");
        AdtRegParam regParam = new AdtRegParam();
        ApplicationUtils.copyProperties(regParam, oldPi);
        ApplicationUtils.copyProperties(regParam, oldPv);
        regParam.setPiMaster(oldPi);//患者基本信息
        regParam.setPvEncounter(oldPv);
        regParam.getPvEncounter().setPkPv(null);//清空就诊主键
        regParam.getPvEncounter().setBedNo(null);//清空床号
        regParam.getPvEncounter().setDateAdmit(null);//清空入科时间
        regParam.getPvEncounter().setDateEnd(null);//清空出院是时间
//		regParam.getPvEncounter().setDateBegin(ts);//设置入院时间为当前时间//2019-06-26 入院日期为留观的入院时间
        regParam.getPvEncounter().setDateReg(ts);//设置登记时间为当前时间
        regParam.getPvEncounter().setEuPvmode("0");
        regParam.getPvEncounter().setFlagIn("0");
        regParam.getPvEncounter().setEuStatus("0");
        regParam.getPvEncounter().setFlagSettle("0");
        regParam.getPvEncounter().setFlagSpec("0");
        regParam.getPvEncounter().setCodePv(paramMap.get("codePvNew").toString());
        regParam.getPvEncounter().setPkDept(paramMap.get("pkDeptNew").toString());
        regParam.getPvEncounter().setPkDeptNs(paramMap.get("pkDeptNsNew").toString());
        regParam.getPvEncounter().setPkWg(null);
        regParam.getPvEncounter().setPkEmpNs(null);
        regParam.getPvEncounter().setNameEmpNs(null);
        regParam.getPvEncounter().setPkEmpPhy(null);
        regParam.getPvEncounter().setNameEmpPhy(null);
        regParam.getPvEncounter().setPkEmpReg(user.getPkEmp());
        regParam.getPvEncounter().setNameEmpReg(user.getNameEmp());
        regParam.getPvEncounter().setPkEmpTre(oldPv.getPkEmpPhy());
        regParam.getPvEncounter().setNameEmpTre(oldPv.getNameEmpPhy());
        regParam.getPvEncounter().setCreator(user.getPkEmp());
        regParam.getPvEncounter().setCreateTime(ts);
        regParam.setNameEmpTre(oldPv.getNameEmpPhy());
        regParam.setPkEmpTre(oldPv.getPkEmpPhy());
        regParam.setDateReg(ts);
        //设置住院就诊信息
        int cnt = regPubMapper.getMaxIpTimes(oldPv.getPkPi());
        regParam.setIpTimes(cnt + 1);//设置入院次数
        regParam.setDtIntype("0001");//00-急诊，0001 急诊留观
        regParam.setDtLevelDise("00");
        regParam.setPkDeptAdmit(paramMap.get("pkDeptNew").toString());
        regParam.setPkDeptNsAdmit(paramMap.get("pkDeptNsNew").toString());
        //设置医保[原逻辑:设置默认住院使用医保]
//		List<BdHp> defHps = DataBaseHelper.queryForList("select * from BD_HP where DEL_FLAG = '0' and EU_HPTYPE = '0' and FLAG_IP = '1'", BdHp.class, new Object[]{});
//		if(null != defHps && defHps.size() > 0)
//		{
//			regParam.getPvEncounter().setPkInsu(defHps.get(0).getPkHp());
//			regParam.setPkInsu(defHps.get(0).getPkHp());
//			regParam.setEuHptype(defHps.get(0).getEuHptype());
//		}
        //设置医保[现逻辑：设置为原急诊医保][2019-07-25]
        List<BdHp> defHps = DataBaseHelper.queryForList("select * from BD_HP where DEL_FLAG = '0' and pk_hp = ? ", BdHp.class, new Object[]{regParam.getPvEncounter().getPkInsu()});
        if (null != defHps && defHps.size() > 0) {
            regParam.setPkInsu(defHps.get(0).getPkHp());
            regParam.setEuHptype(defHps.get(0).getEuHptype());
        }
        //设置备注
        String note = "注：" + paramMap.get("nameDept").toString() + " => " + paramMap.get("nameDeptNew").toString() + "，原pkPv = " + pkPvOld;
        regParam.getPvEncounter().setNote((!CommonUtils.isEmptyString(oldPv.getNote()) ? oldPv.getNote() + "，" : "") + note);
        //设置诊断
        List<PvDiag> diagList = DataBaseHelper.queryForList("select * from pv_diag where DEL_FLAG='0' "
                + " and dt_diagtype='0109' and pk_pv = ? order by date_diag desc ", PvDiag.class, new Object[]{pkPvOld});
        if (null == diagList || diagList.size() < 1)
            throw new BusException("未获取到留观的出院诊断，无法完成【留观转住院】操作！");
        regParam.setDtDiagtype("0100");
        regParam.setPkDiag(diagList.get(0).getPkDiag());
        regParam.setDescDiag(diagList.get(0).getDescDiag());//诊断名称
        regParam.setCodeDiag(diagList.get(0).getCodeIcd());//诊断编码
        PvEncounter newPv = saveAdtReg(ApplicationUtils.objectToJson(regParam), user);
        if (null == newPv)
            throw new BusException("办理入院失败，无法完成入科操作！");
        return newPv;
    }

    public String getflager(String param, IUser user) {
        String pkdept = JSON.parseObject(param).getString("pkdept");
        String flager = "";
        String stSql = "select flag_er from BD_OU_DEPT where PK_DEPT=?";
        Map<String, Object> flagerMap = DataBaseHelper.queryForMap(stSql, pkdept);
        if (flagerMap != null && flagerMap.get("flagEr") != null) {
            flager = flagerMap.get("flagEr").toString();// 急诊使用
        }
        return flager;
    }

    /**
     * 校验当前婴儿是否存在在院母亲
     *
     * @param param
     * @return
     */
    public List<PvEncounter> chkHaveMaInHis(String param, IUser user) {
        Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
        List<PvEncounter> list = DataBaseHelper.queryForList("select pvM.* from pv_encounter pv "
                        + " inner join pv_infant inf on pv.pk_pv = inf.pk_pv_infant and inf.del_flag = '0' "
                        + " inner join pv_encounter pvM on pvM.pk_pv = inf.pk_pv and pvM.pk_dept_ns = pv.pk_dept_ns"
                        + " where pv.pk_dept_ns = ? and pv.pk_pv = ?"
                , PvEncounter.class, new Object[]{paramMap.get("pkDeptNs"), paramMap.get("pkPv")});
        return list;
    }

    /**
     * 住院患者分页
     *
     * @param param
     * @param user
     * @return
     */
    public PagePvListVo getPvEncounterListPaging(String param, IUser user) {
        PagePvListVo piListAndTotal = new PagePvListVo();
        PageQryPvParam qryparam = JsonUtil.readValue(param,
                PageQryPvParam.class);
        if (qryparam == null)
            throw new BusException("查询参数有问题！");
        int pageIndex = CommonUtils.getInteger(qryparam.getPageIndex());
        int pageSize = CommonUtils.getInteger(qryparam.getPageSize());
        // 分页操作
        String pkOrg = UserContext.getUser().getPkOrg();
        qryparam.setPkOrg(pkOrg);
        MyBatisPage.startPage(pageIndex, pageSize);
        List<PvEncounterListVo> list = pvInfoPubMapper.getPvEncounterVoList(qryparam);
        Page<List<PvEncounterListVo>> page = MyBatisPage.getPage();
        piListAndTotal.setPatiList(list);
        piListAndTotal.setTotalCount(page.getTotalCount());
        return piListAndTotal;
    }

    /**
     * 获取线上入院登记记录
     *
     * @param param
     * @param user
     * @return
     */
    public PageAuditVo getSearchPatiAuditInfoPaging(String param, IUser user) {
        PageAuditVo piList = new PageAuditVo();
        PageQryAuditParam qryparam = JsonUtil.readValue(param,
                PageQryAuditParam.class);
        if (qryparam == null)
            throw new BusException("查询参数有问题！");
        int pageIndex = CommonUtils.getInteger(qryparam.getPageIndex());
        int pageSize = CommonUtils.getInteger(qryparam.getPageSize());
        // 分页操作
        String pkOrg = UserContext.getUser().getPkOrg();
        qryparam.setPkOrg(pkOrg);
        MyBatisPage.startPage(pageIndex, pageSize);
        List<PvEnCounterAuditVo> list = pvInfoPubMapper.getSearchPatiAuditInfoPaging(qryparam);
        Page<List<PvEnCounterAuditVo>> page = MyBatisPage.getPage();
        piList.setPatiList(list);
        piList.setTotalCount(page.getTotalCount());
        return piList;
    }

    /**
     * 更新线上入院登记记录的审核状态
     *
     * @param param
     * @param user
     */
    public void updateFlagCheckin(String param, IUser user) {
        String pkpv = JsonUtil.getFieldValue(param, "pkpv");
        Object[] params = new Object[3];
        params[0] = new Date();
        params[1] = UserContext.getUser().getPkEmp();
        params[2] = pkpv;
        if (pkpv != null) {
            String sql = " update pv_ip set flag_checkin='1', date_chk=?,pk_emp_chk=? where pk_pv=?";
            DataBaseHelper.execute(sql, params);
        } else {
            throw new BusException("传入参数有空值");
        }
        //中山二院--调用平安APP接口推送消息
        ExtSystemProcessUtils.processExtMethod("PAAPP", "noticeInHospExamine", param);
    }

    /**
     * 获取科室拓展属性
     * 022006006038
     * @param param
     * @param user
     * @return
     */
    public List<Map<String,Object>> queryDepartmentDictAttr(String param,IUser user){
        Map<String,Object> paramMap = JsonUtil.readValue(param, Map.class);
        List<Map<String,Object>> list = pvInfoPubMapper.queryDepartmentDictAttr(paramMap);
        return list;
    }

}
