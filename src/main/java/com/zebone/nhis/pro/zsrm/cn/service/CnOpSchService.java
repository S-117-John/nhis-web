package com.zebone.nhis.pro.zsrm.cn.service;

import com.zebone.nhis.bl.pub.service.CgQryMaintainService;
import com.zebone.nhis.bl.pub.service.OpCgPubService;
import com.zebone.nhis.bl.pub.support.Constant;
import com.zebone.nhis.bl.pub.vo.BillItemVo;
import com.zebone.nhis.bl.pub.vo.BlPubParamVo;
import com.zebone.nhis.bl.pub.vo.BlPubReturnVo;
import com.zebone.nhis.bl.pub.vo.ItemPriceVo;
import com.zebone.nhis.common.module.base.bd.price.BdHp;
import com.zebone.nhis.common.module.base.ou.BdOuEmpjob;
import com.zebone.nhis.common.module.bl.opcg.BlOpDt;
import com.zebone.nhis.common.module.pi.InsQgybPV;
import com.zebone.nhis.common.module.pi.PiInsurance;
import com.zebone.nhis.common.module.pi.PiMaster;
import com.zebone.nhis.common.module.pv.PvEncounter;
import com.zebone.nhis.common.module.pv.PvEr;
import com.zebone.nhis.common.module.pv.PvInsurance;
import com.zebone.nhis.common.module.pv.PvOp;
import com.zebone.nhis.common.module.pv.support.PvConstant;
import com.zebone.nhis.common.module.sch.appt.SchAppt;
import com.zebone.nhis.common.module.sch.appt.SchApptPv;
import com.zebone.nhis.common.module.sch.plan.SchSch;
import com.zebone.nhis.common.module.sch.plan.SchTicket;
import com.zebone.nhis.common.module.sch.pub.SchResource;
import com.zebone.nhis.common.module.sch.pub.SchSrv;
import com.zebone.nhis.common.support.*;
import com.zebone.nhis.ma.pub.platform.PlatFormSendUtils;
import com.zebone.nhis.pro.zsrm.cn.dao.CnOpSchMapper;
import com.zebone.nhis.pro.zsrm.cn.vo.PiOpVo;
import com.zebone.nhis.cn.opdw.vo.PiSchVo;
import com.zebone.nhis.pro.zsrm.cn.vo.SchOrPv;
import com.zebone.nhis.pv.pub.service.PvInfoPubService;
import com.zebone.nhis.sch.pub.support.SchEuStatus;
import com.zebone.platform.Application;
import com.zebone.platform.common.support.NHISUUID;
import com.zebone.platform.common.support.User;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.type.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CnOpSchService {

    private Logger logger = LoggerFactory.getLogger("nhis.schBl");

    @Autowired
    private CnOpSchMapper cnSchDao;

    @Resource
    private CgQryMaintainService cgQryMaintainService;

    @Resource
    private OpCgPubService opCgPubService;
    
    @Resource
    private PvInfoPubService pvInfoPubService;

    public static final String reqCodeCg = "cn.order.codeCg";

    /**
     * ??????????????????????????????????????????????????????????????????
     *
     * @param param
     * @param user
     * @return
     */
    public List<Map<String, Object>> qryClinicSchList(String param, IUser user) {
        User u = (User) user;
        String dateWork = JsonUtil.getFieldValue(param, "dateWork");
        SchResource res = DataBaseHelper.queryForBean("SELECT * FROM sch_resource WHERE pk_dept_belong=? and  pk_emp = ?", SchResource.class, u.getPkDept(), u.getPkEmp());
        if (res == null || StringUtils.isBlank(res.getPkSchres())) {
            throw new BusException("?????????????????????????????????");
        }
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("dateWork", dateWork);
        paramMap.put("pkSchres", res.getPkSchres());
        List<Map<String, Object>> list = cnSchDao.qryClinicSchList(paramMap);
        return list;
    }

    /**
     * ??????????????????
     *
     * @param param
     * @param user
     * @throws ParseException
     */
    public void clinicMakeApp(String param, IUser user) throws ParseException {
        //??????
        List<PiSchVo> schVoList = (List<PiSchVo>) JsonUtil.readValue(param, new TypeReference<List<PiSchVo>>() {
        });
        User u = (User) user;
        //????????????
        if (schVoList == null || schVoList != null && schVoList.size() == 0) {
            throw new BusException("????????????????????????");
        }
        //????????????
        if (StringUtils.isBlank(schVoList.get(0).getPkSch())) {
            throw new BusException("????????????????????????????????????");
        }
        //????????????
        SchSch schSch = DataBaseHelper.queryForBean("select * from sch_sch where del_flag = '0' and pk_sch = ?",
                SchSch.class, schVoList.get(0).getPkSch());
        if (schSch == null) {
            throw new BusException("???????????????????????????");
        }
        PiSchVo apptVo = null;
        //?????????????????????????????????????????????????????????
        if (schVoList.size() > 1) {
            List<PiSchVo> collect = schVoList.stream().filter(vo -> vo != null && "1".equals(vo.getFlagAppt())).collect(Collectors.toList());
            if (CollectionUtils.isEmpty(collect)) {
                throw new BusException("???????????????????????????FlagAppt???1????????????");
            } else if (collect.size() > 1) {
                throw new BusException("?????????????????????FlagAppt???1???????????????????????????");
            }
            apptVo = collect.get(0);
        } else {
            apptVo = schVoList.get(0);
        }
        String sql = "select count(1) from sch_appt where to_char(date_appt,'yyyy-MM-dd')=? and pk_schres=? and pk_dept_ex=? and flag_cancel='0' and pk_pi=? ";
        Integer count = DataBaseHelper.queryForScalar(sql, Integer.class, apptVo.getDateWork(), schSch.getPkSchres(), schSch.getPkDept(), apptVo.getPkPi());
        if (count > 0) {
            throw new BusException("?????????????????????!");
        }
        String mainPkSchTicket = getSchTicket(apptVo).getPkSchticket();
        for (PiSchVo vo : schVoList) {
            SchTicket schTicket = getSchTicket(vo);
            //??????????????????
            int cnt = DataBaseHelper.update("update sch_ticket set flag_used ='1',PK_SCHTICKET_MAIN=? where pk_schticket =? and flag_used='0'"
                    , new Object[]{StringUtils.equals(vo.getTicketno(), mainPkSchTicket) ? null : mainPkSchTicket, schTicket.getPkSchticket()});
            if (cnt <= 0) {
                throw new BusException("???????????????????????????????????????????????????");
            }
            DataBaseHelper.update("update sch_sch set cnt_used = cnt_used + 1 where pk_sch = ?", new Object[]{vo.getPkSch()});
        }
        //????????????
        SchAppt appt = getSchAppt(apptVo, schSch, u);
        DataBaseHelper.insertBean(appt);
        DataBaseHelper.insertBean(getSchApptPv(apptVo, schSch, u, appt));
        //???????????????
        Map<String, Object> msgPiParam = new HashMap<String, Object>();
        msgPiParam.put("pkSchappt", appt.getPkSchappt());
        PlatFormSendUtils.sendSchApptReg(msgPiParam);
    }

    private SchTicket getSchTicket(PiSchVo vo) throws ParseException {
        Date dateBegin = DateUtils.parseDate(vo.getBeginTime(), "yyyyMMddHHmmss");
        Date dateEnd = DateUtils.parseDate(vo.getEndTime(), "yyyyMMddHHmmss");
        //??????-??????
        Object[] objects = {vo.getPkSch(), dateBegin, dateEnd, vo.getTicketno()};
        String otherSql = "";
        if (StringUtils.isNotBlank(vo.getDtApptype())) {
            objects = new Object[]{vo.getPkSch(), dateBegin, dateEnd, vo.getTicketno(), vo.getDtApptype()};
            otherSql = " and DT_APPTYPE=? ";
        }
        List<SchTicket> tickets = DataBaseHelper.queryForList(
                "select * from sch_ticket where pk_sch = ?  and del_flag = '0' and flag_appt = '1' and flag_stop='0' and flag_used = '0' and  begin_time = ? and end_time =? and TICKETNO = ?" +
                        otherSql +
                        "  order by  ticketno  ",
                SchTicket.class, objects);
        if (tickets == null || tickets.size() == 0) {
            throw new BusException("?????????????????????????????????????????????");
        }
        return tickets.get(0);
    }

    private SchAppt getSchAppt(PiSchVo vo, SchSch schSch, User u) throws ParseException {
        //????????????
        SchAppt schAppt = new SchAppt();
        schAppt.setPkSchappt(NHISUUID.getKeyId());
        schAppt.setEuSchclass("0");
        schAppt.setPkSch(vo.getPkSch());
        schAppt.setDateAppt(DateUtils.parseDate(vo.getDateWork(), "yyyy-MM-dd"));
        schAppt.setPkDateslot(vo.getPkDateslot());
        schAppt.setPkSchres(schSch.getPkSchres());
        schAppt.setPkSchsrv(schSch.getPkSchsrv());
        schAppt.setTicketNo(vo.getTicketno());
        schAppt.setBeginTime(DateUtils.parseDate(vo.getBeginTime(), "yyyyMMddHHmmss"));
        schAppt.setEndTime(DateUtils.parseDate(vo.getEndTime(), "yyyyMMddHHmmss"));
        schAppt.setPkPi(vo.getPkPi());
        schAppt.setDtApptype("0");
        schAppt.setPkDeptEx(schSch.getPkDept());
        schAppt.setPkOrgEx(schSch.getPkOrg());
        schAppt.setDateReg(new Date());
        schAppt.setPkDeptReg(u.getPkDept());
        schAppt.setPkEmpReg(u.getPkEmp());
        schAppt.setNameEmpReg(u.getNameEmp());
        schAppt.setEuStatus("0");//0:??????,1:??????
        schAppt.setFlagPay("0");
        schAppt.setFlagNotice("0");
        schAppt.setFlagCancel("0");
        schAppt.setFlagNoticeCanc("0");
        schAppt.setCode(ApplicationUtils.getCode("0101"));
        //schAppt.setNote(vo.getApptNote());
        schAppt.setTs(new Date());
        schAppt.setPkOrg(u.getPkOrg());
        schAppt.setCreator(u.getPkEmp());
        schAppt.setCreateTime(new Date());
        schAppt.setNoteAppt(vo.getApptNote());
        return schAppt;
    }

    private SchApptPv getSchApptPv(PiSchVo vo, SchSch schSch, User u, SchAppt schAppt) {
        //????????????
        String pkEmp = StringUtils.isEmpty(vo.getPkEmp()) ? u.getPkEmp() : vo.getPkEmp();
        String nameEmp = StringUtils.isEmpty(vo.getNameEmp()) ? u.getNameEmp() : vo.getNameEmp();
        SchApptPv schApptPv = new SchApptPv();
        schApptPv.setPkApptpv(NHISUUID.getKeyId());
        schApptPv.setPkSchappt(schAppt.getPkSchappt());
        schApptPv.setEuApptmode("0");
        schApptPv.setPkEmpPhy(pkEmp);
        schApptPv.setNameEmpPhy(nameEmp);
        schApptPv.setFlagPv("0");
        schApptPv.setTs(new Date());
        schApptPv.setPkOrg(u.getPkOrg());
        schApptPv.setCreator(u.getPkEmp());
        schApptPv.setCreateTime(new Date());
        return schApptPv;
    }

    /**
     * ????????????????????????????????????????????????????????????
     *
     * @param param
     * @param user
     * @return
     */
    public SchOrPv qrySchByPkPi(String param, IUser user) {
        SchOrPv schPv = JsonUtil.readValue(param, SchOrPv.class);
        if (schPv == null) {
            throw new BusException("????????????????????????");
        }
        String endTime = ApplicationUtils.getSysparam("PV0055", false);//???????????????????????????????????????;
        if (StringUtils.isEmpty(endTime)) {
            endTime = "48";
        }
        int endTimeInt = 48;
        try {
            endTimeInt = Integer.parseInt(endTime);
        } catch (Exception e) {
            throw new BusException("???????????????PV0055???????????????");
        }
        //??????48???????????????????????????
        String time = DateUtils.addDate(new Date(), -endTimeInt, 4, "yyyy-MM-dd HH:mm:ss");
        schPv.setTime(time);
        List<Map<String, Object>> pvSch = cnSchDao.qrySchpv(schPv);
        schPv.setPvSch(pvSch);
        List<Map<String, Object>> pvInf = new ArrayList<>(16);
        if (Application.isSqlServer()) {
            pvInf = cnSchDao.qryPvSql(schPv);
        } else {
            pvInf = cnSchDao.qryPv(schPv);
        }
        schPv.setPvInf(pvInf);
        return schPv;
    }

    /**
     * ??????????????????
     *
     * @param param
     * @param user
     * @return
     */
    public List<Map<String, Object>> getPibaseVoList(String param, IUser user) {
        Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
        if (paramMap == null) {
            throw new BusException("????????????????????????");
        }
        if (CommonUtils.isNull(paramMap.get("codePi")) && CommonUtils.isNull(paramMap.get("codeOp"))
                && CommonUtils.isNull(paramMap.get("codeIp"))
                && CommonUtils.isNull(paramMap.get("namePi"))
                && CommonUtils.isNull(paramMap.get("dtSex"))
                && CommonUtils.isNull(paramMap.get("idNo"))
                && CommonUtils.isNull(paramMap.get("pkPi"))
                && CommonUtils.isNull(paramMap.get("insurNo"))
                && CommonUtils.isNull(paramMap.get("mobile"))
                && CommonUtils.isNull(paramMap.get("cardNo"))
                && CommonUtils.isNull(paramMap.get("hicNo"))) {
            throw new BusException("???????????????022006004005??????????????????????????????????????????????????????");
        }
        List<Map<String, Object>> ret = cnSchDao.qryPimaster(paramMap);
        return ret;
    }

    /**
     * ??????????????????
     *
     * @param param
     * @param user
     * @throws ParseException
     */
    public PvEncounter savePvinfo(String param, IUser user) throws ParseException {
        Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
        User doctor = new User();
        BeanUtils.copyProperties((User) user, doctor);
        PvEncounter pvinfo = null;
        String triageCall = MapUtils.getString(paramMap, "triageCall");//???????????????????????????
        String pkSchappt = MapUtils.getString(paramMap, "pkSchappt");
        boolean haveApp = StringUtils.isNotBlank(pkSchappt);
        boolean ifAdd = paramMap.get("pkPv") == null || StringUtils.isBlank(paramMap.get("pkPv").toString());
        if (ifAdd) {
            //?????????????????????????????????????????????paramMap
            if (haveApp && StringUtils.equals("1", triageCall)) {
                Map<String, Object> appMap = DataBaseHelper.queryForMap(
                        "select appt.PK_PI,appt.PK_SCHSRV,appt.PK_SCHRES,appt.PK_DATESLOT,appt.PK_SCH " +
                                " ,(case srv.EU_SRVTYPE when '9' then '2' else '1' end) eu_pvtype" +
                                " from SCH_APPT appt left join sch_srv srv on appt.PK_SCHSRV=srv.PK_SCHSRV " +
                                " where appt.pk_schappt=?", new Object[]{pkSchappt});
                if (MapUtils.isEmpty(appMap)) {
                    throw new BusException("????????????????????????????????????");
                }
                paramMap.putAll(appMap);
            }
            if (paramMap.get("pkPi") == null || StringUtils.isBlank(paramMap.get("pkPi").toString())) {
                throw new BusException("????????????????????????");
            }
            PiMaster piInfo = DataBaseHelper.queryForBean("SELECT * FROM PI_MASTER WHERE pk_pi=? and  del_flag ='0' ", PiMaster.class, paramMap.get("pkPi").toString());
            if (piInfo == null) {
                throw new BusException("???????????????????????????");
            }
            pvinfo = savePv(piInfo, paramMap, doctor);
            //3.?????????????????????????????????pv_op???
            if (EnumerateParameter.ONE.equals(pvinfo.getEuPvtype()) || EnumerateParameter.FOUR.equals(pvinfo.getEuPvtype())) {
                this.savePvOp(pvinfo, paramMap, doctor);//??????????????????
            } else {//??????????????????
                this.savePvEr(pvinfo, paramMap, doctor);
            }
        }
        if (haveApp) {
            String pkPv = paramMap.get("pkPv") == null ? pvinfo.getPkPv() : paramMap.get("pkPv").toString();
            /**?????????????????????**/
            DataBaseHelper.update(" update sch_appt set eu_status='1' where pk_schappt=? ", new Object[]{pkSchappt});
            /**???????????????????????????**/
            DataBaseHelper.update(" update sch_appt_pv set pk_pv=? where pk_schappt=? ", pkPv, pkSchappt);
        }
        //?????????????????????
        Map<String,Object> msgParam =  new HashMap<String,Object>();
        msgParam.put("pkPv", paramMap.get("pkPv") == null ? pvinfo.getPkPv() : paramMap.get("pkPv").toString());
        msgParam.put("isAdd", ifAdd);
        PlatFormSendUtils.sendPvOpRegMsg(msgParam);
        return pvinfo;
    }

    private PvEncounter savePv(PiMaster master, Map<String, Object> paramMap, User u) {
        PvEncounter pvEncounter = new PvEncounter();
        pvEncounter.setPkPv(NHISUUID.getKeyId());
        //?????????????????????bd_ou_dept.dt_medicaltype='30'??????eu_pvtype=???4???
        if (StringUtils.isNotBlank(u.getPkDept()) &&
                "30".equals(CommonUtils.getPropValueStr(DataBaseHelper.queryForMap("select dt_medicaltype from bd_ou_dept where pk_dept=?",
                        new Object[]{u.getPkDept()}), "dtMedicaltype"))) {
            paramMap.put("euPvtype", EnumerateParameter.FOUR);
        }
        // ??????????????????
        boolean jz = "2".equals(MapUtils.getString(paramMap, "euPvtype"));
        pvEncounter.setPkPi(master.getPkPi());
        pvEncounter.setPkDept(u.getPkDept());//????????????
        pvEncounter.setCodePv(ApplicationUtils.getCode(SysConstant.ENCODERULE_CODE_MZ));
        pvEncounter.setEuPvtype(jz ? PvConstant.ENCOUNTER_EU_PVTYPE_2 : (StringUtils.isBlank(paramMap.get("euPvtype").toString()) ? PvConstant.ENCOUNTER_EU_PVTYPE_1 : paramMap.get("euPvtype").toString())); // ??????|??????
        pvEncounter.setEuStatus(PvConstant.ENCOUNTER_EU_STATUS_0); // ??????
        pvEncounter.setNamePi(master.getNamePi());
        pvEncounter.setDtSex(master.getDtSex());
        pvEncounter.setAgePv(ApplicationUtils.getAgeFormat(master.getBirthDate(), null));
        pvEncounter.setAddress(master.getAddress());
        pvEncounter.setFlagIn("0");
        pvEncounter.setFlagSettle("1");
        pvEncounter.setDtMarry(master.getDtMarry());
        pvEncounter.setPkInsu(getPkHp(master.getPkPi()));//????????????
        pvEncounter.setPkPicate(master.getPkPicate());
        pvEncounter.setPkEmpReg(UserContext.getUser().getPkEmp());
        pvEncounter.setNameEmpReg(UserContext.getUser().getNameEmp());
        pvEncounter.setDateReg(new Date());
        pvEncounter.setDateBegin(new Date());
        pvEncounter.setDateClinic(new Date());
        pvEncounter.setFlagCancel("0");
        pvEncounter.setDtIdtypeRel("01");
        pvEncounter.setDtPvsource(master.getDtSource());
        pvEncounter.setNameRel(master.getNameRel());
        pvEncounter.setIdnoRel(master.getIdnoRel());
        pvEncounter.setTelRel(master.getTelRel());
        pvEncounter.setEuPvmode("0");
        pvEncounter.setFlagSpec(isSpec(CommonUtils.getString(paramMap.get("pkSchsrv"))) ? "1" : "0");
        pvEncounter.setEuStatusFp("0");
        pvEncounter.setEuLocked("0");
        pvEncounter.setEuDisetype("0");
        pvEncounter.setPkEmpPhy(u.getPkEmp());
        pvEncounter.setNameEmpPhy(u.getNameEmp());
        
        String pkDeptArea = pvInfoPubService.getPkDeptArea(CommonUtils.getString(paramMap.get("pkSchres")));
   		pvEncounter.setPkDeptArea(pkDeptArea);
        DataBaseHelper.insertBean(pvEncounter);
        return pvEncounter;
    }

    /**
     * ?????????????????????????????????????????????????????? ????????? ??????
     *
     * @param pkSrv
     * @return
     */
    private boolean isSpec(String pkSrv) {
        if (StringUtils.isNotBlank(pkSrv)) {
            SchSrv srv = DataBaseHelper.queryForBean("select eu_srvtype from sch_srv where pk_schsrv=?", SchSrv.class, pkSrv);
            return srv != null && "2".equals(srv.getEuSrvtype());
        }
        return false;
    }

    //??????????????????
    private String getPkHp(String pkPi) {
        if (StringUtils.isNotBlank(pkPi)) {
            PiInsurance piIns = DataBaseHelper.queryForBean("select * from pi_insurance ins where ins.pk_pi=? and ins.flag_def='1' and ins.del_flag='0'", PiInsurance.class, new Object[]{pkPi});
            if (piIns != null && StringUtils.isNotBlank(piIns.getPkHp())) {
                return piIns.getPkHp();
            }
        }
        BdHp bdHp = DataBaseHelper.queryForBean("select * from bd_hp where eu_hptype = '0' and flag_ip = '1'  and del_flag = '0'", BdHp.class);
        if (bdHp == null) {
            return null;
        } else {
            return bdHp.getPkHp();
        }
    }

    /**
     * ??????????????????????????????
     *
     * @return
     */
    public PvOp savePvOp(PvEncounter pv, Map<String, Object> paramMap, User u) {
        // ??????????????????
        PvOp pvOp = new PvOp();
        pvOp.setPkPv(pv.getPkPv());
        Integer opTimes = cnSchDao.getMaxOpTimes(pv.getPkPi());
        pvOp.setOpTimes(new Long(opTimes + 1));
        pvOp.setPkSchsrv(CommonUtils.getString(paramMap.get("pkSchsrv")));
        pvOp.setPkRes(CommonUtils.getString(paramMap.get("pkSchres")));
        pvOp.setPkDateslot(paramMap.get("pkDateslot").toString());
        pvOp.setPkSch(CommonUtils.getString(paramMap.get("pkSch")));
        pvOp.setPkSchappt(MapUtils.getString(paramMap, "pkSchappt"));
        String flagAdd = CommonUtils.getString(paramMap.get("flagAdd"));
        if ("1".equals(flagAdd)) {
            pvOp.setFlagAdd(flagAdd);
            pvOp.setOpTimesRel(new Long(opTimes));
        } else if ("0".equals(flagAdd)) {
            pvOp.setFlagAdd("0");
            pvOp.setOpTimesRel(pvOp.getOpTimes());
        }
        pvOp.setPkDeptPv(u.getPkDept());
        pvOp.setPkEmpPv(u.getPkEmp());
        pvOp.setNameEmpPv(u.getNameEmp());
        pvOp.setFlagFirst("1"); // ??????
        pvOp.setEuRegtype("0");//????????????
        // ?????????????????????????????????????????????????????????????????????:date_end=to_date(date_begin,'yyyymmdd') +
        // ( ??????-1) || '23:59:59'
        pvOp.setDateBegin(pv.getDateBegin());
        if (!"9".equals(pv.getEuPvtype())) {
            pvOp.setDateEnd(ApplicationUtils.getPvDateEnd(pv.getDateBegin()));
        } else {//?????????24????????????
            pvOp.setDateEnd(ApplicationUtils.getPvDateEnd(pv.getDateBegin()));
        }
        pvOp.setFlagNorelease("0");
        pvOp.setDateSign(new Date());
        DataBaseHelper.insertBean(pvOp);
        return pvOp;
    }

    /**
     * ??????????????????????????????
     *
     * @return
     */
    private PvEr savePvEr(PvEncounter pv, Map<String, Object> paramMap, User u) {
        // ??????????????????
        PvEr pvEr = new PvEr();
        pvEr.setPkPv(pv.getPkPv());
        pvEr.setPkSchsrv(CommonUtils.getString(paramMap.get("pkSchsrv")));
        pvEr.setPkRes(CommonUtils.getString(paramMap.get("pkSchres")));
        pvEr.setPkDateslot(CommonUtils.getString(paramMap.get("pkSchres")));
        pvEr.setPkDeptPv(u.getPkDept());
        pvEr.setPkEmpPv(u.getPkEmp());
        pvEr.setNameEmpPv(u.getNameEmp());
        pvEr.setPkSch(CommonUtils.getString(paramMap.get("pkSch")));
        // ?????????????????????????????????????????????????????????????????????:date_end=to_date(date_begin,'yyyymmdd') +
        // ( ??????-1) || '23:59:59'
        pvEr.setDateBegin(pv.getDateBegin());
        pvEr.setDateEnd(ApplicationUtils.getPvDateEnd(pv.getDateBegin()));
        pvEr.setDateArv(new Date());
        DataBaseHelper.insertBean(pvEr);
        return pvEr;
    }

    /**
     * ?????????????????????????????????
     *
     * @param param
     * @param user
     */
    public void saveBlReg(String param, IUser user) throws ParseException {
        PiOpVo piOpVo = JsonUtil.readValue(param, PiOpVo.class);
        logger.info("????????????????????????{}",piOpVo);
        if (piOpVo == null || StringUtils.isEmpty(piOpVo.getPkPv()) || StringUtils.isEmpty(piOpVo.getPkSchsrv())) {
            throw new BusException("???????????????????????????????????????");
        }
        //????????????????????????
        PvEncounter pvInfo = DataBaseHelper.queryForBean("SELECT * FROM PV_ENCOUNTER WHERE pk_pv=? and  del_flag ='0' ", PvEncounter.class, new Object[]{piOpVo.getPkPv()});
        if (pvInfo == null) {
            throw new BusException("??????????????????????????????????????????");
        }
        logger.info("????????????????????????????????????????????????{}",pvInfo);
        if (pvInfo.getPkDept() == null) {
            throw new BusException("???????????????????????????????????????");
        }
        if (pvInfo.getPkEmpPhy() == null) {
            throw new BusException("???????????????????????????????????????");
        }


        blReg(piOpVo,pvInfo);

    }

    private void blReg(PiOpVo piOpVo,PvEncounter pvInfo){
        logger.info("???????????????????????????????????????????????????{}",pvInfo);
        logger.info("?????????????????????????????????????????????????????????{}",piOpVo);
        if(StringUtils.isEmpty(pvInfo.getPkDeptArea())){
            pvInfo.setPkDeptArea(piOpVo.getPkDeptArea());
        }
        String endTime = ApplicationUtils.getSysparam("PV0055", false);//???????????????????????????????????????;
        if (StringUtils.isEmpty(endTime)) {
            endTime = "48";
        }
        int endTimeInt = 48;
        try {
            endTimeInt = Integer.parseInt(endTime);
        } catch (Exception e) {
            throw new BusException("???????????????PV0055???????????????");
        }
        
        //bug 35226 ??????ID 6681331??? 3???30???????????????????????????,???????????????????????????
        //??????????????????????????????????????????????????????????????????????????????????????????      BL0061: 1,??????????????????2.???????????????
        //??????48???????????????????????????  
        Date  compareDate = pvInfo.getDateBegin();
        if(null == compareDate ) {
        	compareDate = new Date();
        }
        String time = DateUtils.addDate(compareDate, -endTimeInt, 4, "yyyy-MM-dd HH:mm:ss");
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT BL.date_hap,PV.PK_DEPT,pv.PK_EMP_PHY FROM BL_OP_DT bl INNER JOIN PV_ENCOUNTER pv on PV.pk_pv=BL.pk_pv");
        sql.append(" WHERE bl.flag_pv='1' and bl.del_flag ='0'  and PV.PK_DEPT=? and PV.PK_EMP_PHY=? AND BL.PK_PI=? ");
        sql.append(" and BL.DATE_CG>= to_date(?,'YYYY-MM-DD HH24:MI:SS')");
        List<SchOrPv> listBl = DataBaseHelper.queryForList(sql.toString(), SchOrPv.class, new Object[]{pvInfo.getPkDept(), pvInfo.getPkEmpPhy(), pvInfo.getPkPi(), time});
        if (listBl != null && listBl.size() > 0) {
            return;
        }
        //?????????????????????????????????????????????
        List<String> list = getDictCode(reqCodeCg);
        if (list != null && list.size() > 0) {
            boolean onlyHS = true; //????????????????????????
            sql.delete(0, sql.length());
            sql.append(" select DISTINCT b.CODE from BL_OP_DT a INNER JOIN BD_ITEM b on a.PK_ITEM=b.PK_ITEM where a.pk_pv=? ");
            List<Map<String, Object>> opDts = DataBaseHelper.queryForList(sql.toString(), new Object[]{pvInfo.getPkPv()});
            if (opDts != null && opDts.size() > 0) {
                for (Map<String, Object> opdt : opDts) {
                    if (!list.contains(MapUtils.getString(opdt, "code"))) {
                        onlyHS = false;
                        break;
                    }
                }
            } else { //??????????????????????????????
                onlyHS = false;
            }
            //??????????????????
            if (onlyHS) {
                return;
            }
        }
        sql.delete(0, sql.length());
        sql.append("select  ord.pk_ord,bdord.name,bdord.code_ordtype,srv.PK_ITEM_SPEC , bdord.FLAG_CG    from SCH_SRV srv ");
        sql.append("INNER JOIN sch_srv_ord ord on ORD.PK_SCHSRV=SRV.PK_SCHSRV INNER JOIN bd_ord bdord on bdord.pk_ord = ord.pk_ord ");
        sql.append("where srv.PK_SCHSRV=?");
        SchOrPv srvMap = DataBaseHelper.queryForBean(sql.toString(), SchOrPv.class, new Object[]{piOpVo.getPkSchsrv()});
        if (srvMap == null) {
            throw new BusException("?????????????????????????????????????????????");
        }
        if ("0".equals(srvMap.getFlagCg())){
            return;
        }
        //??????????????????????????????
        BdOuEmpjob emp = DataBaseHelper.queryForBean(
                "SELECT * FROM bd_ou_empjob WHERE pk_emp =? AND is_main = '1' ", BdOuEmpjob.class, new Object[]{UserContext.getUser().getPkEmp()});
        String pkDeptJob = emp != null && StringUtils.isNotBlank(emp.getPkDept()) ? emp.getPkDept() : null;
        //??????????????????
        List<BlPubParamVo> blOpCgList = new ArrayList<BlPubParamVo>();
        BlPubParamVo vo = opOrdToOpCg(pvInfo, srvMap, pkDeptJob);
        blOpCgList.add(vo);
        //?????????????????????
        if (StringUtils.isNotEmpty(srvMap.getPkItemSpec())) {
            BlPubParamVo vo1 = opItemToOpCg(pvInfo, srvMap, pkDeptJob);
            blOpCgList.add(vo1);
        }
        BlPubReturnVo retVo = opCgPubService.blOpCgBatch(blOpCgList);
        //????????????????????????
        if (retVo != null && retVo.getBods() != null && retVo.getBods().size() > 0) {
            DataBaseHelper.batchUpdate("update BL_OP_DT set flag_pv='1' where PK_CGOP=:pkCgop", retVo.getBods());
        }
    }
    /**
     * ???????????????
     *
     * @param item
     * @param pvInfo
     * @return
     */
    private BlOpDt getItemBl(ItemPriceVo item, PvEncounter pvInfo, User user) {
        BlOpDt vo = new BlOpDt();
        ApplicationUtils.copyProperties(vo, item);
        vo.setPkPv(pvInfo.getPkPv());
        vo.setPkPi(pvInfo.getPkPi());
        vo.setPkOrg(pvInfo.getPkOrg());
        //????????????????????????
        Set<String> itemSet = new HashSet<String>();
        Set<String> pdSet = new HashSet<String>();
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("pkOrg", UserContext.getUser().getPkOrg());
        param.put("euType", Constant.OPINV);
        itemSet.add(item.getPkItem());
        param.put("itemList", CommonUtils.convertSetToSqlInPart(itemSet, "pk_item"));
        param.put("pdList", CommonUtils.convertSetToSqlInPart(pdSet, "pk_item"));
        List<BillItemVo> bills = cgQryMaintainService.qryBillAndAccountCodeByPkItems(param);
        if (bills == null || bills.size() <= 0) {
            throw new BusException("???????????????????????????????????????????????????????????????????????????");
        }
        for (BillItemVo bill : bills) {
            if ("bill".equals(bill.getBillType()) && bill.getFlagPd().equals(vo.getFlagPd()) && bill.getPkItem().equals(vo.getPkItem())) {
                vo.setCodeBill(bill.getCodeBill());
            }
            if ("acc".equals(bill.getBillType()) && bill.getFlagPd().equals(vo.getFlagPd()) && bill.getPkItem().equals(vo.getPkItem())) {
                vo.setCodeAudit(bill.getCodeBill());
            }
        }
        vo.setPkOrgApp(user.getPkOrg());
        vo.setPkDeptApp(user.getPkDept());
        vo.setPkEmpApp(user.getPkEmp());
        vo.setNameEmpApp(user.getNameEmp());
        vo.setPkOrgEx(user.getPkOrg());
        vo.setPkDeptEx(user.getPkDept());
        vo.setDateHap(new Date());
        vo.setFlagAcc(EnumerateParameter.ZERO);
        vo.setFlagSettle(EnumerateParameter.ZERO);
        String codeCg = ApplicationUtils.getCode("0601");
        vo.setCodeCg(codeCg);
        vo.setDateCg(new Date());
        vo.setPkDeptCg(user.getPkDept());
        vo.setPkEmpCg(user.getPkEmp());
        vo.setNameEmpCg(user.getNameEmp());
        vo.setCreator(user.getPkEmp());
        vo.setCreateTime(new Date());
        vo.setTs(new Date());
        vo.setRatioAdd(0D);
        Double priceOrg = item.getRatioSelf() == null ? item.getPrice() : MathUtils.mul(item.getPrice(), item.getRatioSelf());
        vo.setPriceOrg(priceOrg); //????????????
        vo.setPrice(item.getPriceOrg() == null ? 0 : item.getPriceOrg());//????????????
        vo.setAmountAdd(MathUtils.sub(vo.getPrice(), vo.getPriceOrg()));
        vo.setFlagRecharge(EnumerateParameter.ZERO);
        vo.setAmount(item.getPriceOrg());
        vo.setAmountPi(item.getPriceOrg());
        vo.setAmountHppi(vo.getAmount().doubleValue());
        //??????
        vo.setPkDeptApp(pvInfo.getPkDeptArea());
        return vo;
    }

    /**
     * ???????????????????????????????????????
     *
     * @param param
     * @param user
     */
    public void cancleBlReg(String param, IUser user) {
        Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
        if (paramMap == null || CommonUtils.isNull(paramMap.get("pkPv"))) {
            throw new BusException("??????????????????????????????");
        }
        List<BlOpDt> blOpDts = DataBaseHelper.queryForList(" select * from bl_op_dt where pk_pv=? and flag_pv='1' and del_flag='0' ", BlOpDt.class, paramMap.get("pkPv").toString());
        if (blOpDts == null || blOpDts.size() == 0) {
            return;
        }
        for (BlOpDt vo : blOpDts) {
            if ("1".equals(vo.getFlagSettle())) {
                throw new BusException("????????????????????????????????????!");
            }
            vo.setDelFlag("1");//????????????
        }
        DataBaseHelper.batchUpdate("delete from BL_OP_DT where PK_CGOP=:pkCgop ", blOpDts);
    }

    /***
     *?????????????????????
     * @param param
     * @param user
     * @return
     */
    public int qryBlPv(String param, IUser user) {
        Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
        if (paramMap == null || CommonUtils.isNull(paramMap.get("pkPv"))) {
            throw new BusException("??????????????????????????????");
        }
        List<BlOpDt> blOpDts = DataBaseHelper.queryForList(" select * from bl_op_dt where pk_pv=? and flag_pv='1' and del_flag='0' ", BlOpDt.class, paramMap.get("pkPv").toString());
        if (blOpDts == null || blOpDts.size() == 0) {
            return 0;
        }
        return blOpDts.size();
    }

    /***
     * ????????????
     * @param param
     * @param user
     * @return
     */
    public Map<String, Object> addSchPatient(String param, IUser user) {
        PvEncounter pv = getSchAndCreateReg(param, user);
        //pkPv
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("pkPv", pv.getPkPv());
        Map<String, Object> pvMode = getHpAttr(paramMap);
        //?????????????????????
        Map<String,Object> msgParam =  new HashMap<String,Object>();
        msgParam.put("pkPv", pv.getPkPv());
        msgParam.put("isAdd", "0");
        PlatFormSendUtils.sendPvOpRegMsg(msgParam);
        return pvMode;
    }

    public PvEncounter getSchAndCreateReg(String param, IUser user) {
        Map<String, Object> paramMap = JsonUtil.readValue(param, new TypeReference<Map<String, Object>>() {
        });
        if (MapUtils.isEmpty(paramMap)) {
            throw new BusException("???????????????");
        }
        String pkSchres = MapUtils.getString(paramMap, "pkSchres");
        String pkSchsrv = MapUtils.getString(paramMap, "pkSchsrv");
        String pkPi = MapUtils.getString(paramMap, "pkPi");
        String pkInsu = MapUtils.getString(paramMap, "pkHp");
        String pkOrgarea = MapUtils.getString(paramMap, "pkOrgarea");
        String pkSch = MapUtils.getString(paramMap, "pkSch");
        String flagAdd = MapUtils.getString(paramMap, "flagAdd");
        if (StringUtils.isBlank(pkSchres)) {
            throw new BusException("???????????????????????????");
        }
        if (StringUtils.isBlank(pkSchsrv)) {
            throw new BusException("???????????????????????????");
        }
        //??????????????????
        if (StringUtils.isBlank(pkInsu)) {
            pkInsu = getPkHp(pkPi);
        }
        //??????????????????
        SchSrv schSrv = DataBaseHelper.queryForBean("select * from sch_srv where pk_schsrv=?", SchSrv.class, new Object[]{pkSchsrv});
        if (schSrv == null) {
            throw new BusException("??????????????????????????????????????????????????????");
        }
        //??????????????????????????????????????????????????????????????????sch_sch
        SchSch schSch = null;
        if (StringUtils.isBlank(pkSch)) { //?????????????????????
            //schSch = saveSch(pkSchres, pkSchsrv, pkOrgarea);
            schSch = new SchSch();
            schSch.setPkSchsrv(pkSchsrv);
            schSch.setPkSchres(pkSchres);
        } else {
            schSch = DataBaseHelper.queryForBean("select * from sch_sch where pk_sch=?", SchSch.class, new Object[]{pkSch});
        }
        //????????????????????????pv0052????????????????????????????????????
        Map<String, Object> myParam = JsonUtil.readValue(param, Map.class);
        String myPkPi = MapUtils.getString(myParam, "pkPi");
        String sql = "SELECT * FROM PI_MASTER WHERE PK_PI=?";
        PiMaster piMaster = DataBaseHelper.queryForBean(sql, PiMaster.class, new Object[]{myPkPi});
        if (piMaster != null) {
            String flag = piMaster.getFlagRealmobile();
            if (!"1".equalsIgnoreCase(flag)) {
                //?????????
                sql = "SELECT COUNT(1) CNT FROM PV_ENCOUNTER WHERE PK_PI=? AND EU_STATUS <>'9'";
                Map<String, Object> countMap = DataBaseHelper.queryForMap(sql, new Object[]{myPkPi});
                int numberOfVisits = 0;
                try {
                    numberOfVisits = Integer.valueOf(MapUtils.getString(countMap, "cnt"));
                } catch (Exception e) {
                }
                String pv0052 = ApplicationUtils.getSysparam("PV0052", false, "????????????????????????PV0052???");
                Integer max = Integer.valueOf(pv0052);
                if (numberOfVisits == max) {
                    throw new BusException("??????????????????????????????????????????????????????");
                }
            }
        }
        //??????????????????????????????????????????????????????pv_encounter
        PvEncounter pvEncount = savePvEncount(pkPi, pkInsu, schSrv.getEuSrvtype());
        if ("9".equals(schSrv.getEuSrvtype())) { //??????
            //???????????????????????????pv_op ,?????????????????????
            savePvEr(pvEncount, schSch);//??????????????????
        } else {
            //???????????????????????????pv_op ,?????????????????????
            savePvOp(pvEncount, schSch, flagAdd);//??????????????????
        }
        //????????????????????????
        savePvInsurance(pvEncount);
        return pvEncount;
    }

    private SchSch saveSch(String pkSchres, String pkSchsrv, String pkOrgarea) {
        SchResource schResource = DataBaseHelper.queryForBean("select * from sch_resource where pk_schres=?", SchResource.class, new Object[]{pkSchres});
        if (schResource == null) {
            throw new BusException("??????????????????????????????????????????????????????");
        }
//        Map<String, Object> mapDateslot = DataBaseHelper.queryForMap("select ds.pk_dateslot from sch_resource res inner join bd_code_dateslot ds on res.dt_dateslottype=ds.dt_dateslottype"
//                        + " where res.pk_schres=? and to_date(?,'HH24:MI:SS') between to_date(ds.TIME_BEGIN,'HH24:MI:SS') and to_date(ds.TIME_END,'HH24:MI:SS')"
//                , new Object[]{pkSchres, DateUtils.getDate("HH:mm:ss")});
//        if (MapUtils.isEmpty(mapDateslot)) {
//            throw new BusException("?????????????????????????????????????????????");
//        }
        User u = UserContext.getUser();
        Date d = new Date();
        //?????????????????????????????????
//        String sql = "select sch.* from  sch_sch sch inner join bd_code_dateslot lot on lot.pk_dateslot=sch.pk_dateslot  where sch.pk_dept=?  and sch.pk_schres=? and sch.pk_schsrv=? "
//                + "and sch.date_work=to_date(?,'yyyyMMdd') and sch.eu_status = '8' and sch.cnt_total = 0 and to_date(?,'HH24:MI:SS') between to_date(lot.TIME_BEGIN,'HH24:MI:SS') and to_date(lot.TIME_END,'HH24:MI:SS') ";
//        List<SchSch> schList = DataBaseHelper.queryForList(sql, SchSch.class, new Object[]{schResource.getPkDeptBelong(), pkSchres, pkSchsrv, DateUtils.getDateStr(d), DateUtils.getDate("HH:mm:ss")});
//        if (schList != null && schList.size() > 0) {
//            return schList.get(0);
//        }
        //String pkDateslot = MapUtils.getString(mapDateslot, "pkDateslot");
        SchSch schSch = new SchSch();
        schSch.setPkSch(NHISUUID.getKeyId());
        //schSch.setPkDateslot(pkDateslot);
        schSch.setPkSchres(pkSchres);
        schSch.setPkSchsrv(pkSchsrv);
        schSch.setMinutePer(schResource.getMinutePer().intValue());
        schSch.setPkOrg(u.getPkOrg());
        schSch.setPkDept(schResource.getPkDeptBelong());
        schSch.setEuSchclass(schResource.getEuSchclass());
        schSch.setDateWork(DateUtils.getDateMorning(d, 0));
        schSch.setPkPlanweek(null);
        schSch.setPkSchplan(null);
        schSch.setCntTotal(0);
        schSch.setCntAdd(0);
        schSch.setCntAppt(0);
        schSch.setCntOver(0);
        schSch.setCntUsed(0);
        schSch.setFlagStop("0");
        schSch.setFlagModi("1");
        schSch.setEuStatus(SchEuStatus.AUDIT.getStatus());
        schSch.setEuAppttype("0");
        schSch.setPkOrgarea(pkOrgarea);
        schSch.setPkEmpSch(u.getPkEmp());
        schSch.setNameEmpSch(u.getNameEmp());
        schSch.setDateSch(d);
        schSch.setPkEmpChk(u.getPkEmp());
        schSch.setNameEmpChk(u.getNameEmp());
        schSch.setDateChk(d);
        schSch.setNote("????????????");//(u.getUserName() + DateUtils.getDate("yyyy-MM-dd HH:mm:ss") + "???????????????");
        DataBaseHelper.insertBean(schSch);
        return schSch;
    }

    private PvEncounter savePvEncount(String pkPi, String pkInsu, String euSrvtype) {
        PiMaster master = DataBaseHelper.queryForBean("select * from pi_master where pk_pi=?", PiMaster.class, new Object[]{pkPi});
        if (master == null) {
            throw new BusException("????????????????????????????????????????????????");
        }
        User u = UserContext.getUser();
        Date d = new Date();
        // ??????????????????
        PvEncounter pvEncounter = new PvEncounter();
        pvEncounter.setPkPv(NHISUUID.getKeyId());
        pvEncounter.setPkPi(master.getPkPi());
        pvEncounter.setPkInsu(pkInsu);
        pvEncounter.setCodePv(ApplicationUtils.getCode(SysConstant.ENCODERULE_CODE_MZ));
        if ("9".equals(euSrvtype)) { //??????
            pvEncounter.setEuPvtype(PvConstant.ENCOUNTER_EU_PVTYPE_2); //??????????????????
        } else {
            pvEncounter.setEuPvtype(PvConstant.ENCOUNTER_EU_PVTYPE_1); //??????????????????
            pvEncounter.setFlagSpec("0");
            if ("2".equals(euSrvtype)) //??????
            {
                pvEncounter.setFlagSpec("1");
            }
        }
        pvEncounter.setEuStatus(PvConstant.ENCOUNTER_EU_STATUS_0); // ??????
        pvEncounter.setEuLocked("0");
        pvEncounter.setDateClinic(d);
        pvEncounter.setPkDept(u.getPkDept());//????????????
        pvEncounter.setPkEmpPhy(u.getPkEmp());
        pvEncounter.setNameEmpPhy(u.getNameEmp());
        pvEncounter.setDateBegin(d);
        pvEncounter.setDateReg(d);
        pvEncounter.setPkEmpReg(u.getPkEmp());
        pvEncounter.setNameEmpReg(u.getNameEmp());
        pvEncounter.setNamePi(master.getNamePi());
        pvEncounter.setDtSex(master.getDtSex());
        pvEncounter.setAgePv(DateUtils.getAgeByBirthday(master.getBirthDate(), pvEncounter.getDateBegin()));
        pvEncounter.setAddress(master.getAddress());
        pvEncounter.setFlagIn("0");
        pvEncounter.setFlagSettle("0");
        pvEncounter.setDtMarry(master.getDtMarry());
        pvEncounter.setPkPicate(master.getPkPicate());
        pvEncounter.setFlagCancel("0");
        pvEncounter.setDtIdtypeRel("01");
        pvEncounter.setDtPvsource(master.getDtSource());
        pvEncounter.setNameRel(master.getNameRel());
        pvEncounter.setIdnoRel(master.getIdnoRel());
        pvEncounter.setTelRel(master.getTelRel());
        pvEncounter.setEuPvmode("0");
        pvEncounter.setEuStatusFp("0");
        pvEncounter.setEuDisetype("0");
        DataBaseHelper.insertBean(pvEncounter);
        return pvEncounter;
    }

    private PvEr savePvEr(PvEncounter pvEncount, SchSch schSch) {
        PvEr er = new PvEr();
        er.setPkPv(pvEncount.getPkPv());
        er.setPkSchsrv(schSch.getPkSchsrv());
        er.setPkRes(schSch.getPkSchres());
        er.setPkDateslot(schSch.getPkDateslot());
        er.setPkDeptPv(pvEncount.getPkDept());
        er.setPkEmpPv(pvEncount.getPkEmpPhy());
        er.setNameEmpPv(pvEncount.getNameEmpPhy());
        er.setPkResCn(schSch.getPkSchres());
        er.setPkSchsrvCn(schSch.getPkSchsrv());
        er.setTicketno(CommonUtils.isEmptyString(schSch.getTicketNo()) ? 0 : Long.parseLong(schSch.getTicketNo()));
        er.setPkSch(schSch.getPkSch());
        er.setDateBegin(pvEncount.getDateBegin());
        er.setDateEnd(ApplicationUtils.getPvDateEndEr(pvEncount.getDateBegin()));
        DataBaseHelper.insertBean(er);
        return er;
    }

    private PvOp savePvOp(PvEncounter pv, SchSch schSch, String flagAdd) {
        // ??????????????????
        PvOp pvOp = new PvOp();
        pvOp.setPkPv(pv.getPkPv());
        String val = ApplicationUtils.getSysparam("PV0056", false);
        Integer opTimes;
        if ("2".equals(val)) {
            opTimes = cnSchDao.getMaxOpTimesFromPiMaster(pv.getPkPi());
        } else {
            opTimes = cnSchDao.getMaxOpTimes(pv.getPkPi());
        }
        pvOp.setOpTimes(new Long(opTimes + 1));
        if ("1".equals(flagAdd)) {
            pvOp.setFlagAdd(flagAdd);
            pvOp.setOpTimesRel(new Long(opTimes));
        } else if ("0".equals(flagAdd)) {
            pvOp.setFlagAdd("0");
            pvOp.setOpTimesRel(pvOp.getOpTimes());
        }
        pvOp.setPkSchsrv(schSch.getPkSchsrv());
        pvOp.setPkRes(schSch.getPkSchres());
        pvOp.setPkDateslot(schSch.getPkDateslot());
        pvOp.setPkDeptPv(pv.getPkDept());
        pvOp.setPkEmpPv(pv.getPkEmpPhy());
        pvOp.setNameEmpPv(pv.getNameEmpPhy());
        pvOp.setPkResCn(schSch.getPkSchres());
        pvOp.setPkSchsrvCn(schSch.getPkSchsrv());
        pvOp.setTicketno(CommonUtils.isEmptyString(schSch.getTicketNo()) ? 0 : Long.parseLong(schSch.getTicketNo()));
        pvOp.setPkSch(schSch.getPkSch());
        pvOp.setFlagFirst("1"); // ??????
        pvOp.setEuRegtype("0"); //????????????
        // ?????????????????????????????????????????????????????????????????????:date_end=to_date(date_begin,'yyyymmdd') +
        // ( ??????-1) || '23:59:59'
        pvOp.setDateBegin(pv.getDateBegin());
        pvOp.setDateEnd(ApplicationUtils.getPvDateEnd(pv.getDateBegin()));
        DataBaseHelper.insertBean(pvOp);
        DataBaseHelper.update("update PI_MASTER set CNT_OP=? where PK_PI=?",new Object[]{opTimes+1,pv.getPkPi()});
        return pvOp;
    }

    /**
     * ????????????????????????
     *
     * @param pv
     */
    private PvInsurance savePvInsurance(PvEncounter pv) {
        if (CommonUtils.isNotNull(pv.getPkInsu())) {
            PvInsurance insu = new PvInsurance();
            insu.setPkPvhp(NHISUUID.getKeyId());
            insu.setPkOrg(UserContext.getUser().getPkOrg());
            insu.setCreator(UserContext.getUser().getPkEmp());
            insu.setCreateTime(new Date());
            insu.setModifier(UserContext.getUser().getPkEmp());
            insu.setTs(new Date());
            insu.setPkPv(pv.getPkPv());
            insu.setPkHp(pv.getPkInsu());
            DataBaseHelper.insertBean(insu);
            return insu;
        }
        return null;
    }

    private Map<String, Object> getHpAttr(Map<String, Object> paramMap) {
        Map<String, Object> pvMode = new HashMap<String, Object>();
        List<Map<String, Object>> pvModelist = cnSchDao.qryPvMode(paramMap);
        if (pvModelist != null && pvModelist.size() == 1) {
            pvMode = pvModelist.get(0);
        } else {
            throw new BusException("??????????????????????????????????????????????????????");
        }
        String pkHp = pvMode.get("pkInsu") != null ? pvMode.get("pkInsu").toString() : "";
        if (StringUtils.isNotBlank(pkHp)) {
            List<Map<String, Object>> attresList = DataBaseHelper.queryForList("select   attrtmp.code_attr, attr.val_attr from bd_hp hp  inner join bd_dictattr attr on hp.pk_hp = attr.pk_dict inner join bd_dictattr_temp attrtmp on attrtmp.pk_dictattrtemp = attr.pk_dictattrtemp  where hp.pk_hp=? ", new Object[]{pkHp});
            if (attresList != null && attresList.size() > 0) {
                Map<String, Object> hpAttr = new HashMap<String, Object>();
                for (Map<String, Object> attrM : attresList) {
                    String codeAttr = CommonUtils.getString(attrM.get("codeAttr"));
                    String valAttr = CommonUtils.getString(attrM.get("valAttr"));
                    if (StringUtils.isNotEmpty(valAttr)) {
                        hpAttr.put(codeAttr, valAttr);
                    }
                }
                String nameHp = CommonUtils.getString(pvMode.get("insuName"));
                if (!hpAttr.containsKey("0313")) {
                    throw new BusException("???????????????" + nameHp + "??????????????????0313???????????????????????????????????????");
                }
                if (!hpAttr.containsKey("0314")) {
                    throw new BusException("???????????????" + nameHp + "??????????????????0314????????????????????????????????????");
                }
                if (!hpAttr.containsKey("0315")) {
                    throw new BusException("???????????????" + nameHp + "??????????????????0315???????????????????????????????????????");
                }
                if (!hpAttr.containsKey("0316")) {
                    throw new BusException("???????????????" + nameHp + "??????????????????0316???????????????????????????????????????");
                }
                if (!hpAttr.containsKey("0317")) {
                    throw new BusException("???????????????" + nameHp + "??????????????????0317?????????????????????????????????");
                }
                pvMode.put("Attres", attresList);
            } else {
                throw new BusException("??????????????????????????????????????????????????????");
            }
        } else {
            throw new BusException("?????????????????????????????????????????????????????????????????????");
        }
        return pvMode;
    }

    /**
     * ?????????????????????????????????null
     *
     * @param paramMap
     * @param key
     * @return
     */
    private boolean mapKeyIsNotEmpty(Map<String, Object> paramMap, String key) {
        boolean isNotEmpty = false;
        if (paramMap.containsKey(key)) {
            if (paramMap.get(key) == null || StringUtils.isNotEmpty(paramMap.get(key).toString())) { //???????????????key??????null ??? ??????????????????
                isNotEmpty = true;
            }
        } else { //?????????????????????
            isNotEmpty = true;
        }
        return isNotEmpty;
    }

    //vo??????--cnord???cg
    public BlPubParamVo opOrdToOpCg(PvEncounter pvInfo, SchOrPv srvMap, String pkDeptJob) {
        User u = UserContext.getUser();
        String pkOrg = u.getPkOrg();
        String pkDept = u.getPkDept();
        BlPubParamVo bpb = new BlPubParamVo();
        bpb.setPkOrg(pkOrg);
        bpb.setEuPvType(pvInfo.getEuPvtype());
        bpb.setPkPv(pvInfo.getPkPv());
        bpb.setPkPi(pvInfo.getPkPi());
        bpb.setDateStart(new Date());
        bpb.setCodeOrdtype(srvMap.getCodeOrdtype()); //????????????
        bpb.setPkOrd(srvMap.getPkOrd()); //????????????
        bpb.setPkItem(null);
        bpb.setQuanCg(1d);
        bpb.setPkOrgEx(u.getPkOrg());
        bpb.setPkOrgApp(u.getPkOrg());
        bpb.setPkDeptEx(u.getPkDept());
        bpb.setPkDeptApp(u.getPkDept());
        bpb.setPkEmpApp(u.getPkEmp());
        bpb.setNameEmpApp(u.getNameEmp());
        bpb.setFlagPd("0");
        bpb.setNamePd(srvMap.getName()); //??????????????????
        bpb.setFlagPv("1");
        //bpb.setPkUnitPd(opOrd.getPkUnitCg()); //??????????????????
        //bpb.setPackSize(opOrd.getPackSize().intValue()); //????????????
        // bpb.setPrice(opOrd.getPriceCg()); //??????
        bpb.setDateHap(new Date());
        bpb.setPkDeptCg(pkDept);
        bpb.setPkEmpCg(u.getPkEmp());
        bpb.setNameEmpCg(u.getNameEmp());
        bpb.setEuAdditem("0"); //????????????
        bpb.setPkDeptJob(pkDeptJob);
        bpb.setPkDeptAreaapp(pvInfo.getPkDeptArea());
        return bpb;
    }

    public BlPubParamVo opItemToOpCg(PvEncounter pvInfo, SchOrPv srvMap, String pkDeptJob) {
        User u = UserContext.getUser();
        String pkOrg = u.getPkOrg();
        String pkDept = u.getPkDept();
        BlPubParamVo bpb = new BlPubParamVo();
        bpb.setPkOrg(pkOrg);
        bpb.setEuPvType(pvInfo.getEuPvtype());
        bpb.setPkPv(pvInfo.getPkPv());
        bpb.setPkPi(pvInfo.getPkPi());
        bpb.setDateStart(new Date());
        bpb.setCodeOrdtype(srvMap.getCodeOrdtype()); //????????????
        bpb.setPkOrd(null); //????????????
        bpb.setPkItem(srvMap.getPkItemSpec());//??????????????????
        bpb.setQuanCg(1d);
        bpb.setPkOrgEx(u.getPkOrg());
        bpb.setPkOrgApp(u.getPkOrg());
        bpb.setPkDeptEx(u.getPkDept());
        bpb.setPkDeptApp(u.getPkDept());
        bpb.setPkEmpApp(u.getPkEmp());
        bpb.setNameEmpApp(u.getNameEmp());
        bpb.setFlagPd("0");
        bpb.setNamePd(srvMap.getName()); //??????????????????
        bpb.setFlagPv("1");
        bpb.setDateHap(new Date());
        bpb.setPkDeptCg(pkDept);
        bpb.setPkEmpCg(u.getPkEmp());
        bpb.setNameEmpCg(u.getNameEmp());
        bpb.setEuAdditem("0"); //????????????
        bpb.setPkDeptJob(pkDeptJob);
        bpb.setPkDeptAreaapp(pvInfo.getPkDeptArea());
        return bpb;
    }

    public static List<String> getDictCode(String field) {
        String str = ApplicationUtils.getPropertyValue(field, "");
        if (org.springframework.util.StringUtils.isEmpty(str)) {
            return null;
        }
        List<String> list = Arrays.asList(str.split(","));
        return list;
    }
    /***???????????? start***/
    /**
     * ????????????????????????????????????
     *
     * @param param
     * @param user
     * @return
     */
    public List<Map<String, Object>> qrySchRes(String param, IUser user) {
        SchOrPv schPv = JsonUtil.readValue(param, SchOrPv.class);
        User user1=(User)user;
        if (schPv == null) {
            throw new BusException("???????????????(??????/??????)???????????????");
        }
        if (StringUtils.isEmpty(schPv.getPkDept()) || StringUtils.isEmpty(schPv.getPkEmp())) {
            throw new BusException("???????????????(??????/??????)???????????????");
        }
        if(StringUtils.isEmpty(schPv.getPkOrg())){
            schPv.setPkOrg(user1.getPkOrg());
        }
        List<Map<String, Object>> mapList = cnSchDao.qryResSch(schPv);
        //????????????
        if (mapList != null && mapList.size() > 0) {
            for (Map<String, Object> map : mapList) {
                qryBlOrd(map);
            }
        }
        return mapList;
    }

    private void qryBlOrd(Map<String, Object> map) {
        Double price = 0d;
        Double zero = 0d;
        String pkSchsrv = MapUtils.getString(map, "pkSchsrv");
        String schsrv = MapUtils.getString(map, "schsrv");
        if (StringUtils.isEmpty(pkSchsrv)) {
            return;
        }
        //???????????????????????????
        List<Map<String, Object>> mapOrd = cnSchDao.qryPvSeeOrd(map);
        //????????????
        List<String> pkOrds = new ArrayList<String>();
        List<String> pkOrdA = new ArrayList<String>();
        Map<String, Integer> pkOrds1 = new HashMap<String, Integer>();
        if (mapOrd != null && mapOrd.size() > 0) {
            for (Map<String, Object> vo : mapOrd) {
                pkOrdA.add(MapUtils.getString(vo, "pkOrd"));
                if (!pkOrds.contains(MapUtils.getString(vo, "pkOrd"))) {
                    pkOrds.add(MapUtils.getString(vo, "pkOrd"));
                }
                //?????????????????????
                if (StringUtils.isNotEmpty(MapUtils.getString(vo, "pkItemSpec"))) {
                    pkOrdA.add(MapUtils.getString(vo, "pkItemSpec"));
                    if (!pkOrds.contains(MapUtils.getString(vo, "pkItemSpec"))) {
                        pkOrds.add(MapUtils.getString(vo, "pkItemSpec"));
                    }
                }
            }
        }
        map.put("pk_ords", pkOrds);
        List<Map<String, Object>> mapBl = cnSchDao.qryPvSeeBl(map);
        if (mapBl != null && mapBl.size() > 0) {
            for (String ord : pkOrdA) {
                for (Map<String, Object> bl : mapBl) {
                    if (ord.equals(MapUtils.getString(bl, "pkOrd"))) {
                        if (StringUtils.isNotEmpty(MapUtils.getString(bl, "price")) && StringUtils.isNotEmpty(MapUtils.getString(bl, "quan"))) {
                            price = price + MathUtils.mul(Double.parseDouble(MapUtils.getString(bl, "price")), Double.parseDouble(MapUtils.getString(bl, "quan")));
                            break;
                        }
                    }
                }
            }
        }
        if (price > zero) {
            DecimalFormat df = new DecimalFormat(".##");
            schsrv = schsrv + "(" + df.format(price) + "???)";
        }
        map.put("schsrv", schsrv);
    }
    /***???????????? end ***/

    /***
     * ??????
     * @param param
     * @param user
     */
    public void finshCheckClinic(String param , IUser user){
        PiOpVo piOpVo = JsonUtil.readValue(param, PiOpVo.class);
        if(piOpVo == null ) return;
        User u=(User) user;
        boolean Zcf="1".equals(piOpVo.getZcf())?true:false;
        boolean Yb="1".equals(piOpVo.getYb())?true:false;

        //????????????????????????
        PvEncounter pvInfo = DataBaseHelper.queryForBean("SELECT * FROM PV_ENCOUNTER WHERE pk_pv=? and  del_flag ='0' ", PvEncounter.class, new Object[]{piOpVo.getPkPv()});
        if (pvInfo == null) {
            throw new BusException("??????????????????????????????????????????");
        }
        //?????????
        if(Zcf){
            if ( StringUtils.isEmpty(piOpVo.getPkPv()) || StringUtils.isEmpty(piOpVo.getPkSchsrv())) {
                throw new BusException("???????????????????????????????????????");
            }

            if (pvInfo.getPkDept() == null) {
                throw new BusException("???????????????????????????????????????");
            }
            if (pvInfo.getPkEmpPhy() == null) {
                throw new BusException("???????????????????????????????????????");
            }
            blReg(piOpVo,pvInfo);
        }

        //??????
        if(Yb){
            InsQgybPV ins=piOpVo.getPati();

            if(pvInfo.getPkPv()==null) {
                throw new BusException("??????????????????????????????");
            }
            if(pvInfo.getPkPi()==null) {
                throw new BusException("????????????????????????");
            }
            saveYB(pvInfo,u,ins);
        }

        /************????????????????????????*************/
//        Map<String,Object> paramMap1 = JsonUtil.readValue(param, Map.class);
//        PlatFormSendUtils.sendUpPiInfoMsg(paramMap1);
    }

    private void saveYB(PvEncounter pvInfo,User u,InsQgybPV ins){
        String ybpvSql="select count(1) from ins_qgyb_pv where pk_pv=?";
        int countpv=DataBaseHelper.queryForScalar(ybpvSql,Integer.class,new Object[]{pvInfo.getPkPv()});

        //????????????
        if(StringUtils.isBlank(ins.getPkInspv()) && countpv==0){ //??????
            ins.setPkOrg(u.getPkOrg());
            ins.setPkPi(pvInfo.getPkPi());
            ins.setPkPv(pvInfo.getPkPv());
            ins.setPkHp(pvInfo.getPkInsu());
            ins.setDelFlag("0");
            ins.setCreator(u.getPkEmp());
            ins.setCreateTime(new Date());
            ins.setTs(new Date());
            DataBaseHelper.insertBean(ins);
        }else{
            DataBaseHelper.update(" update ins_qgyb_pv set med_type=:medType,dise_codg=:diseCodg,dise_name=:diseName,birctrl_type=:birctrlType,birctrl_matn_date=:birctrlMatnDate,matn_type=:matnType,geso_val=:gesoVal where pk_pv=:pkPv",ins);
        }

        /*** ???????????????????????? start ***/
        String sql="";

        //???????????????
        if(Application.isSqlServer()){
            sql = "select top 1 * from CN_EMR_OP where PK_PV = ? order by ts desc ";
        }else{
            sql = "select * from CN_EMR_OP where PK_PV = ? and rownum=1 order by ts desc ";
        }
        Map<String,Object> rtn=DataBaseHelper.queryForMap(sql,new Object[]{pvInfo.getPkPv()});

        if(StringUtils.isNotEmpty(MapUtils.getString(rtn,"present")) ||  StringUtils.isNotEmpty(MapUtils.getString(rtn,"problem"))){
            sql="update CN_RIS_APPLY set note_dise=? where pk_ordris in (select b.pk_ordris from CN_ORDER a inner join CN_RIS_APPLY b on a.pk_cnord=b.pk_cnord where a.PK_PV=? and ( b.note_dise is null or   ltrim(rtrim(b.note_dise)) ='') )";
            String present=MapUtils.getString(rtn,"present");
            String problem = MapUtils.getString(rtn,"problem");
            if(StringUtils.isNotEmpty(present) && StringUtils.isNotEmpty(problem)){
                if(problem.contains(present)){
                    DataBaseHelper.update(sql,new Object[]{problem,pvInfo.getPkPv()});
                }else {
                    DataBaseHelper.update(sql,new Object[]{problem+ "\r\n"+present,pvInfo.getPkPv()});
                }
            }else if(StringUtils.isNotEmpty(present)){
                DataBaseHelper.update(sql,new Object[]{present,pvInfo.getPkPv()});
            }else if(StringUtils.isNotEmpty(problem)){
                DataBaseHelper.update(sql,new Object[]{problem,pvInfo.getPkPv()});
            }
        }
        /*** ???????????????????????? end ***/

    }

}
