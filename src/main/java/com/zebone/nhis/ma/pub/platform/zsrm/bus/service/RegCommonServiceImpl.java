package com.zebone.nhis.ma.pub.platform.zsrm.bus.service;

import com.zebone.nhis.common.module.pv.PvEncounter;
import com.zebone.nhis.common.module.pv.PvEr;
import com.zebone.nhis.common.module.pv.PvOp;
import com.zebone.nhis.common.module.pv.support.PvConstant;
import com.zebone.nhis.common.module.sch.appt.SchAppt;
import com.zebone.nhis.common.module.sch.appt.SchApptPv;
import com.zebone.nhis.common.module.sch.plan.SchSch;
import com.zebone.nhis.common.module.sch.pub.SchResource;
import com.zebone.nhis.common.module.sch.pub.SchSrv;
import com.zebone.nhis.common.support.*;
import com.zebone.nhis.ma.pub.platform.zsrm.bus.RegCommonService;
import com.zebone.nhis.ma.pub.platform.zsrm.vo.PiMasterRegVo;
import com.zebone.nhis.pv.pub.dao.RegPubMapper;
import com.zebone.nhis.pv.pub.service.TicketPubService;
import com.zebone.platform.common.support.User;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class RegCommonServiceImpl implements RegCommonService {

    @Autowired
    private RegPubMapper regPubMapper;
    @Autowired
    private TicketPubService ticketPubService;
    @Override
    public PvEncounter savePvEncounter(PiMasterRegVo regVo) {
        // 保存就诊记录
        //科室的专业类型bd_ou_dept.dt_medicaltype='30'时，eu_pvtype=’4’
        if(StringUtils.isNotBlank(regVo.getPkDept()) &&
                "30".equals(CommonUtils.getPropValueStr(DataBaseHelper.queryForMap("select dt_medicaltype from bd_ou_dept where pk_dept=?",
                        new Object[]{regVo.getPkDept()}),"dtMedicaltype"))){
            regVo.setEuPvtype(EnumerateParameter.FOUR);
        }
        boolean jz = "2".equals(regVo.getEuPvtype());
        PvEncounter pvEncounter = new PvEncounter();
        pvEncounter.setPkPv(regVo.getPkPv());
        pvEncounter.setPkPi(regVo.getPkPi());
        pvEncounter.setPkDept(regVo.getPkDept());//就诊科室
        pvEncounter.setCodePv(ApplicationUtils.getCode(SysConstant.ENCODERULE_CODE_MZ));
        pvEncounter.setEuPvtype(jz ? PvConstant.ENCOUNTER_EU_PVTYPE_2 : (StringUtils.isBlank(regVo.getEuPvtype())?PvConstant.ENCOUNTER_EU_PVTYPE_1:regVo.getEuPvtype())); // 急诊|门诊
        pvEncounter.setEuStatus(PvConstant.ENCOUNTER_EU_STATUS_0); // 登记
        pvEncounter.setNamePi(regVo.getNamePi());
        pvEncounter.setDtSex(regVo.getDtSex());
        pvEncounter.setAgePv(ApplicationUtils.getAgeFormat(regVo.getBirthDate(),null));
        pvEncounter.setAddress(regVo.getAddress());
        pvEncounter.setFlagIn("0");
        pvEncounter.setFlagSettle("1");
        pvEncounter.setDtMarry(regVo.getDtMarry());
        pvEncounter.setPkInsu(regVo.getPkHp());
        pvEncounter.setPkPicate(regVo.getPkPicate());
        pvEncounter.setPkEmpReg(UserContext.getUser().getPkEmp());
        pvEncounter.setNameEmpReg(UserContext.getUser().getNameEmp());
        pvEncounter.setDateReg(new Date());
        if(regVo.getDateAppt()!=null){
            //预约开始时间应该为日期+预约号所在的开始时间
            pvEncounter.setDateBegin(getDateAppt(regVo.getPkDateslot(), regVo.getDateAppt()));
        }else{
            pvEncounter.setDateBegin(regVo.getDateReg());//挂号的排班日期
        }
        pvEncounter.setFlagCancel("0");
        pvEncounter.setDtIdtypeRel("01");
        pvEncounter.setDtPvsource(regVo.getDtSource());
        pvEncounter.setNameRel(regVo.getNameRel());
        pvEncounter.setIdnoRel(regVo.getIdnoRel());
        pvEncounter.setTelRel(regVo.getTelRel());
        pvEncounter.setEuPvmode("0");
        pvEncounter.setFlagSpec(isSpec(regVo.getPkSrv())?"1":"0");
        pvEncounter.setEuStatusFp("0");
        pvEncounter.setEuLocked("0");
        pvEncounter.setEuDisetype("0");
        DataBaseHelper.insertBean(pvEncounter);
        return pvEncounter;
    }

    @Override
    public void savePvPt(PiMasterRegVo regVo){
        if(EnumerateParameter.ONE.equals(regVo.getEuPvtype()) || EnumerateParameter.FOUR.equals(regVo.getEuPvtype())) {
            this.savePvOp(regVo);
        }else if(EnumerateParameter.TWO.equals(regVo.getEuPvtype())){
            this.savePvEr(regVo);
        }
    }

    @Override
    public void saveAppt(PiMasterRegVo piMasterRegVo) {
        SchSch schSch = DataBaseHelper.queryForBean("select * from sch_sch where del_flag = '0' and pk_sch = ?",
                SchSch.class, piMasterRegVo.getPkSch());
        if(schSch == null){
            throw new BusException("没有对应排班信息");
        }
        Map<String, Object> dateslotMap = DataBaseHelper.queryForMap("select bd.TIME_END from bd_code_dateslot bd where bd.pk_dateslot=?", new Object[]{schSch.getPkDateslot()});
        String schDate = DateUtils.formatDate(schSch.getDateWork(),"yyyy-MM-dd")+" "+ MapUtils.getString(dateslotMap,"timeEnd");
        if(schDate.compareTo(DateFormatUtils.format(new Date(),"yyyy-MM-dd HH:mm:ss"))<0){
            throw new BusException("不能预约已过期排班");
        }
        if(StringUtils.length(piMasterRegVo.getPkSchappt())!=32){
            throw new BusException("预约主键只能是32位唯一码");
        }
        int count = DataBaseHelper.queryForScalar("select count(1) from sch_appt where pk_pi = ? and date_appt >= ? and date_appt <= ? and  " +
                "pk_sch = ? and  flag_cancel = '0'", Integer.class, new Object[]{piMasterRegVo.getPkPi(),schSch.getDateWork(),schSch.getDateWork(),piMasterRegVo.getPkSch()});
        if(count > 0){
            throw new BusException("已经预约过了，不能再次预约；只能预约次日及以后日期的挂号");
        }
        SchResource schRes = DataBaseHelper.queryForBean(
                "select * from SCH_RESOURCE where del_flag = '0' and pk_schres = ?", SchResource.class,
                schSch.getPkSchres());
        SchAppt schAppt = new SchAppt();
        schAppt.setPkSchappt(piMasterRegVo.getPkSchappt());
        schAppt.setEuSchclass("0");
        schAppt.setPkSch(piMasterRegVo.getPkSch());
        schAppt.setCode(ApplicationUtils.getCode("0101"));
        schAppt.setDateAppt(schSch.getDateWork());
        schAppt.setPkDateslot(schSch.getPkDateslot());
        schAppt.setPkSchres(schSch.getPkSchres());
        schAppt.setPkSchsrv(schSch.getPkSchsrv());
        schAppt.setBeginTime(piMasterRegVo.getStartTime());
        schAppt.setEndTime(piMasterRegVo.getEndTime());
        schAppt.setPkPi(piMasterRegVo.getPkPi());
        schAppt.setDtApptype(piMasterRegVo.getDtApptype());
        schAppt.setPkDeptEx(schSch.getPkDept());
        schAppt.setDateReg(new Date());
        schAppt.setPkDeptReg(schSch.getPkDept());
        schAppt.setPkEmpReg(piMasterRegVo.getPkEmp());
        schAppt.setNameEmpReg(piMasterRegVo.getNamePi());
        schAppt.setEuStatus("0");
        schAppt.setFlagPay("0");
        schAppt.setFlagNotice("0");
        schAppt.setFlagCancel("0");
        schAppt.setFlagNoticeCanc("0");
        schAppt.setPkOrgEx(piMasterRegVo.getPkOrg());
        schAppt.setTicketNo(piMasterRegVo.getTicketNo());
        schAppt.setOrderidExt(piMasterRegVo.getNote());
        DataBaseHelper.insertBean(schAppt);

        //写预约就诊表
        SchApptPv schApptPv = new SchApptPv();
        schApptPv.setPkSchappt(schAppt.getPkSchappt());
        schApptPv.setEuApptmode("0");
        if(schRes != null) {
            schApptPv.setPkEmpPhy(schRes.getPkEmp());
            Map<String,Object> nameEmp = DataBaseHelper.queryForMap("select name_emp from BD_OU_EMPLOYEE where pk_emp = ?", schRes.getPkEmp());
            schApptPv.setNameEmpPhy(nameEmp.get("nameEmp")==null?"":nameEmp.get("nameEmp").toString());
        }
        schApptPv.setFlagPv("0");
        schApptPv.setPkPv(piMasterRegVo.getPkPv());
        DataBaseHelper.insertBean(schApptPv);
        DataBaseHelper.update("update sch_sch set cnt_used = cnt_used + 1 where pk_sch = ?",
                new Object[] { piMasterRegVo.getPkSch() });
    }

    @Override
    public void cancelAppt(SchAppt appt) {
        if(appt ==null){
            throw new BusException("预约对象为空");
        }
        if(StringUtils.isBlank(appt.getPkSchappt())){
            throw new BusException("预约主键为空");
        }
        SchAppt schAppt = DataBaseHelper.queryForBean("select * from SCH_APPT where PK_SCHAPPT = ?",
                SchAppt.class, appt.getPkSchappt());
        if(schAppt == null){
            throw new BusException("没有对应预约信息！");
        }
        if("9".equals(schAppt.getEuStatus())){
            throw new BusException("该患者已经取消预约");
        }
        if ("1".equals(schAppt.getEuStatus())) {
            throw new BusException("该患者已经就诊过了");
        }
        Date dateappt = schAppt.getEndTime();
        Date now = new Date();
        if (dateappt.before(now)) {
            throw new BusException("预约已过期，不能取消");
        }

        //只允许修改部分属性
//        User u  = UserContext.getUser();
//        if(u!=null){
//            schAppt.setPkEmpCancel(u.getPkEmp());
//            schAppt.setNameEmpCancel(u.getNameEmp());
//        }
        schAppt.setNote(appt.getNote());
        schAppt.setFlagCancel("1");
        schAppt.setEuStatus("9");
        schAppt.setDateCancel(new Date());
        schAppt.setNameEmpCancel(appt.getNameEmpCancel());
        DataBaseHelper.updateBeanByPk(schAppt, false);
        DataBaseHelper.update("update sch_sch set cnt_used = cnt_used - 1 where pk_sch = ?", new Object[]{appt.getPkSch()});

        DataBaseHelper.update("update sch_sch set cnt_used = cnt_used - 1 where pk_sch = ?",
                new Object[]{schAppt.getPkSch()});
        DataBaseHelper.update("update sch_ticket set flag_used = '0' where pk_sch = ? and ticketno = ?",
                new Object[] { schAppt.getPkSch(), schAppt.getTicketNo() });
        List<String> pkCnords = DataBaseHelper.queryForList("select pk_cnord from sch_appt_ord where pk_schappt = ?",String.class, new Object[] { schAppt.getPkSchappt() });
        if (pkCnords.size() > 0) {
            String pkCnord = pkCnords.get(0);
            // 更新检查申请单
            DataBaseHelper.update("update cn_ris_apply set eu_status = '1' where pk_cnord = ?", new Object[] { pkCnord });
            // 更新病理申请单
            DataBaseHelper.update("update cn_pa_apply set eu_status = '1' where pk_cnord = ?", new Object[] { pkCnord });
        }
    }

    /**
     * 保存就诊记录门诊属性
     * @return
     */
    public PvOp savePvOp(PiMasterRegVo regVo){
        // 保存门诊属性
        PvOp pvOp = new PvOp();
        pvOp.setPkPv(regVo.getPkPv());
        Integer opTimes = regPubMapper.getMaxOpTimes(regVo.getPkPi());
        pvOp.setOpTimes(new Long(opTimes+1));
        pvOp.setPkSchsrv(regVo.getPkSchsrv());
        pvOp.setPkRes(regVo.getPkSchres());
        pvOp.setPkDateslot(regVo.getPkDateslot());
        pvOp.setPkDeptPv(regVo.getPkDept());
        pvOp.setPkEmpPv(regVo.getPkEmpReg());//挂号医生
        pvOp.setNameEmpPv(regVo.getNameEmpReg());
        pvOp.setTicketno(CommonUtils.isEmptyString(regVo.getTicketNo())?0:Long.parseLong(regVo.getTicketNo()));
        pvOp.setPkSch(regVo.getPkSch());
        pvOp.setFlagFirst("1"); // 初诊
        pvOp.setPkAppo(regVo.getPkAppt()); // 字段重复
        pvOp.setPkSchappt(regVo.getPkAppt());// 对应预约

        if(CommonUtils.isNull(regVo.getPkAppt())){//挂号方式
            pvOp.setEuRegtype("0");
        }else{
            pvOp.setEuRegtype("1");
        }
        pvOp.setDtApptype(regVo.getDtApptype() == null?getDefAppType(): regVo.getDtApptype());
        // 有效开始时间为登记时间，结束时间根据设置来计算:date_end=to_date(date_begin,'yyyymmdd') +
        // ( 参数-1) || '23:59:59'
        pvOp.setDateBegin(regVo.getDateBegin());
        if(!"9".equals(regVo.getEuSrvtype())){
            pvOp.setDateEnd(ApplicationUtils.getPvDateEnd(regVo.getDateBegin()));
        }else{//急诊号24小时有效
            pvOp.setDateEnd(DateUtils.strToDate(DateUtils.addDate(regVo.getDateBegin(), 24, 4, "yyyyMMddHHmmss")));
        }
        pvOp.setFlagNorelease("0");
        DataBaseHelper.insertBean(pvOp);
        return pvOp;
    }

    public String getDefAppType(){
        return MapUtils.getString(DataBaseHelper.queryForMap("select code from BD_DEFDOC where  DEL_FLAG = '0' and CODE_DEFDOCLIST = '020100' and DEL_FLAG='0' and FLAG_DEF='1' "),"code");
    }
    /**
     * 保存就诊记录急诊属性
     * @return
     */
    private PvEr savePvEr(PiMasterRegVo regVo){
        // 保存急诊属性
        PvEr pvEr = new PvEr();
        pvEr.setPkPv(regVo.getPkPv());
        pvEr.setPkSchsrv(regVo.getPkSchsrv());
        pvEr.setPkRes(regVo.getPkSchres());
        pvEr.setPkDateslot(regVo.getPkDateslot());
        pvEr.setPkDeptPv(regVo.getPkDept());
        pvEr.setPkEmpPv(regVo.getPkEmpReg());//挂号医生
        pvEr.setNameEmpPv(regVo.getNameEmpReg());
        pvEr.setTicketno(CommonUtils.isEmptyString(regVo.getTicketNo())?0:Long.parseLong(regVo.getTicketNo()));
        pvEr.setPkSch(regVo.getPkSch());
        pvEr.setDateBegin(regVo.getDateBegin());
        pvEr.setDateEnd(ApplicationUtils.getPvDateEndEr(regVo.getDateBegin()));
        pvEr.setDateArv(new Date());
        DataBaseHelper.insertBean(pvEr);
        return pvEr;
    }

    public Date getDateAppt(String pkDateslot, Date date){
        String sql = "select time_begin from bd_code_dateslot where pk_dateslot = ?";
        Map<String, Object> queryForMap = DataBaseHelper.queryForMap(sql, pkDateslot);
        String dateStr = DateUtils.dateToStr("yyyyMMdd", date);
        String dateAppt = dateStr + " " + queryForMap.get("timeBegin").toString();
        return DateUtils.strToDate(dateAppt,"yyyyMMdd HH:mm:ss");
    }

    public boolean isSpec(String pkSrv){
        if(StringUtils.isNotBlank(pkSrv)){
            SchSrv srv = DataBaseHelper.queryForBean("select eu_srvtype from sch_srv where pk_schsrv=?", SchSrv.class, pkSrv);
            return srv!=null && "2".equals(srv.getEuSrvtype());
        }
        return false;
    }
}
