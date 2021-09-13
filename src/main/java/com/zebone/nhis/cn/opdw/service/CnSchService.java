package com.zebone.nhis.cn.opdw.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.zebone.nhis.cn.opdw.dao.CnSchMapper;
import com.zebone.nhis.common.module.sch.appt.SchAppt;
import com.zebone.nhis.common.module.sch.appt.SchApptPv;
import com.zebone.nhis.common.module.sch.plan.SchSch;
import com.zebone.nhis.common.module.sch.plan.SchTicket;
import com.zebone.nhis.common.module.sch.pub.SchResource;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.cn.opdw.vo.PiSchVo;
import com.zebone.nhis.common.support.EnumerateParameter;
import com.zebone.nhis.ma.pub.platform.PlatFormSendUtils;
import com.zebone.platform.common.support.NHISUUID;
import com.zebone.platform.common.support.User;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class CnSchService {
    @Autowired
    private CnSchMapper cnSchDao;

    public static SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
    public static SimpleDateFormat sdl = new SimpleDateFormat("yyyy-MM-dd");

    /**
     * 根据出诊日期所选的日期查询出诊时段和可约号数
     * @param param
     * @param user
     * @return
     */
    public List<Map<String, Object>> qryClinicSchList(String param, IUser user){
        User u = (User)user;
        String dateWork = JsonUtil.getFieldValue(param, "dateWork");
        String pkSchres = JsonUtil.getFieldValue(param,"pkSchres");
        String pkDept= JsonUtil.getFieldValue(param,"pkDept");
        if(StringUtils.isEmpty(pkDept)) pkDept=  u.getPkDept();
        String pkEmp= JsonUtil.getFieldValue(param,"pkEmp");
        if(StringUtils.isEmpty(pkEmp)) pkEmp=  u.getPkEmp();
        if(StringUtils.isEmpty(pkSchres)){
            SchResource res = DataBaseHelper.queryForBean("SELECT * FROM sch_resource WHERE pk_dept_belong=? and  pk_emp = ?", SchResource.class,pkDept,pkEmp);
            if(res == null || StringUtils.isBlank(res.getPkSchres())){
                throw new BusException("当前医生排班资源为空！");
            }
            pkSchres = res.getPkSchres();
        }
        if(StringUtils.isEmpty(pkSchres)){
            throw new BusException("当前医生排班资源为空！");
        }

        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("dateWork", dateWork);
        paramMap.put("pkSchres", pkSchres);

        String ifFilter=ApplicationUtils.getSysparam("SCH0016", false);
        List<Map<String, Object>> list=null;
        if("1".equals(ifFilter)){
            list = cnSchDao.qryClinicSchListTic(paramMap);
        }else{
            list = cnSchDao.qryClinicSchList(paramMap);
        }
        return list;
    }

    /**
     * 诊间预约处理
     * @param param
     * @param user
     * @throws ParseException
     */
    public void clinicMakeApp(String param, IUser user) throws ParseException {

        //参数
        List<PiSchVo> paramMap = (List<PiSchVo>)JsonUtil.readValue(param, new TypeReference<List<PiSchVo>>(){});
        User u = (User)user;

        //数据校验
        if(paramMap==null || paramMap!=null && paramMap.size()==0){
            throw new BusException("请传入预约信息！");
        }
        //排班主键
        if(StringUtils.isBlank(paramMap.get(0).getPkSch())){
            throw new BusException("诊间预约传排班主键为空！");
        }
        //排班记录
        SchSch schSch = DataBaseHelper.queryForBean("select * from sch_sch where del_flag = '0' and pk_sch = ?",
                SchSch.class, paramMap.get(0).getPkSch());
        if(schSch==null) throw new BusException("当前医生排班为空！");

        String mainPkSchTicket = null;
        List<String> pkTicktes = Lists.newArrayList();
        PiSchVo mainSch = null;
        for (PiSchVo vo: paramMap) {
            //就诊时间
            Date dateBegin = sdf.parse(vo.getBeginTime());
            Date dateEnd = sdf.parse(vo.getEndTime());

            String sql = "select count(1) from sch_appt where to_char(date_appt,'yyyy-MM-dd')=? and pk_schres=? and pk_dept_ex=? and flag_cancel='0' and pk_pi=? ";
            Integer count = DataBaseHelper.queryForScalar(sql, Integer.class, vo.getDateWork(),schSch.getPkSchres(),schSch.getPkDept(),vo.getPkPi());
            if(count > 0){
                throw new BusException("预约记录已存在!");
            }
            //排班-号表
            List<SchTicket> tickets = DataBaseHelper.queryForList(
                    "select * from sch_ticket where pk_sch = ?  and del_flag = '0' and flag_appt = '1' and flag_stop='0' and flag_used = '0' and  begin_time = ? and end_time =? and TICKETNO = ?  order by  ticketno  ",
                    SchTicket.class, new Object[]{vo.getPkSch(),dateBegin,dateEnd,vo.getTicketno()});
            if(tickets==null||tickets.size()<=0){
                throw new BusException("您所选的排班资源已无可预约号！");
            }
            String pkTicket = tickets.get(0).getPkSchticket();
            pkTicktes.add(pkTicket);
            if(EnumerateParameter.ONE.equals(vo.getFlagAppt()) && mainPkSchTicket ==null){
                mainPkSchTicket = pkTicket;
                mainSch = vo;
            }
            //占用号表数据
            int cnt = DataBaseHelper.update("update sch_ticket set flag_used ='1',PK_SCHTICKET_MAIN=? where pk_schticket =? and flag_used='0'", new Object[]{mainPkSchTicket,pkTicket});
            if(cnt<=0)
                throw new BusException("您所选的挂号号码已被占用，请重试！");
            DataBaseHelper.update("update sch_sch set cnt_used = cnt_used + 1 where pk_sch = ?", new Object[] {vo.getPkSch()});
        }
        Map<String,Object> modMap = Maps.newHashMap();
        modMap.put("mainPk",mainPkSchTicket);
        modMap.put("pks",pkTicktes);
        DataBaseHelper.update("update sch_ticket set PK_SCHTICKET_MAIN=:mainPk where pk_schticket in(:pks)", modMap);
        //依据主号码，生成一条预约记录
        SchAppt appt = getSchAppt(mainSch,schSch,u);;
        DataBaseHelper.insertBean(appt);
        DataBaseHelper.insertBean(getSchApptPv(mainSch,schSch, u,appt));

        Map<String,Object> map = new HashMap<>(16);
        map.put("pkPi",appt.getPkPi());
        map.put("pkSch",appt.getPkSch());
        PlatFormSendUtils.sendReserveOutpatient(map);
    }

    private SchAppt getSchAppt(PiSchVo vo,SchSch schSch,User u) throws ParseException{
        //排班预约
        SchAppt schAppt = new SchAppt();
        schAppt.setPkSchappt(NHISUUID.getKeyId());
        schAppt.setEuSchclass("0");
        schAppt.setPkSch(vo.getPkSch());
        schAppt.setDateAppt(sdl.parse(vo.getDateWork()));
        schAppt.setPkDateslot(vo.getPkDateslot());
        schAppt.setPkSchres(schSch.getPkSchres());
        schAppt.setPkSchsrv(schSch.getPkSchsrv());
        schAppt.setTicketNo(vo.getTicketno());
        schAppt.setBeginTime(sdf.parse(vo.getBeginTime()));
        schAppt.setEndTime(sdf.parse(vo.getEndTime()));
        schAppt.setPkPi(vo.getPkPi());
        schAppt.setDtApptype("0");
        schAppt.setPkDeptEx(schSch.getPkDept());
        schAppt.setPkOrgEx(schSch.getPkOrg());
        schAppt.setDateReg(new Date());
        schAppt.setPkDeptReg(u.getPkDept());
        schAppt.setPkEmpReg(u.getPkEmp());
        schAppt.setNameEmpReg(u.getNameEmp());
        schAppt.setEuStatus("0");//0:登记,1:到达
        schAppt.setFlagPay("0");
        schAppt.setFlagNotice("0");
        schAppt.setFlagCancel("0");
        schAppt.setFlagNoticeCanc("0");
        schAppt.setCode(ApplicationUtils.getCode("0101"));
        schAppt.setNote(vo.getApptNote());
        schAppt.setTs(new Date());
        schAppt.setPkOrg(u.getPkOrg());
        schAppt.setCreator(u.getPkEmp());
        schAppt.setCreateTime(new Date());
        return schAppt;
    }
    private SchApptPv getSchApptPv(PiSchVo vo,SchSch schSch,User u,SchAppt schAppt){
        //预约就诊
        SchApptPv schApptPv = new SchApptPv();
        schApptPv.setPkApptpv(NHISUUID.getKeyId());
        schApptPv.setPkSchappt(schAppt.getPkSchappt());
        schApptPv.setEuApptmode("0");
        schApptPv.setPkEmpPhy(schAppt.getPkEmpReg());
        schApptPv.setNameEmpPhy(schAppt.getNameEmpReg());
        schApptPv.setFlagPv("0");
        schApptPv.setTs(new Date());
        schApptPv.setPkOrg(u.getPkOrg());
        schApptPv.setCreator(u.getPkEmp());
        schApptPv.setCreateTime(new Date());
        return schApptPv;
    }

}
