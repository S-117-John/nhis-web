package com.zebone.nhis.sch.plan.service;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Function;
import com.google.common.collect.*;
import com.zebone.nhis.common.dao.BaseCodeMapper;
import com.zebone.nhis.common.module.base.bd.code.BdCodeDateslot;
import com.zebone.nhis.common.module.base.bd.code.BdCodeDateslotTime;
import com.zebone.nhis.common.module.base.bd.code.BdWorkcalendardate;
import com.zebone.nhis.common.module.mybatis.MyBatisPage;
import com.zebone.nhis.common.module.sch.plan.*;
import com.zebone.nhis.common.module.sch.pub.SchModlog;
import com.zebone.nhis.common.module.sch.pub.SchResource;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.common.support.EnumerateParameter;
import com.zebone.nhis.ma.pub.platform.PlatFormSendUtils;
import com.zebone.nhis.sch.plan.dao.SchMapper;
import com.zebone.nhis.sch.plan.dao.SchPlanMapper;
import com.zebone.nhis.sch.plan.vo.*;
import com.zebone.nhis.sch.pub.service.SchHandlerService;
import com.zebone.nhis.sch.pub.support.SchEuStatus;
import com.zebone.platform.Application;
import com.zebone.platform.common.support.NHISUUID;
import com.zebone.platform.common.support.User;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.dao.build.BuildSql;
import com.zebone.platform.modules.dao.jdbc.MultiDataSource;
import com.zebone.platform.modules.dao.support.Page;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.FastDateFormat;
import org.codehaus.jackson.type.TypeReference;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * ??????
 *
 * @author
 */
@Service
public class SchService {

    @Autowired
    private SchMapper schMapper;

    @Autowired
    private BaseCodeMapper codeMapper;
    
    @Autowired
    private SchPlanMapper schPlanMapper;

    @Autowired
    private SchHandlerService handlerService;

    @Autowired
    private SchAuditService schAuditService;

    /**
     * ?????????????????????????????????????????????????????????????????????
     *
     * @param param
     * @param user
     */
    public List<Map<String, Object>> getSchInfoByResource(String param, IUser user) {
        Map paramMap = JsonUtil.readValue(param, Map.class);
        String rescode = null;
        if (paramMap.get("rescode") != null) {
            rescode = paramMap.get("rescode").toString();
        }
        String pkSchres = null;
        if (paramMap.get("pkSchres") != null) {
            pkSchres = paramMap.get("pkSchres").toString();
        }
        String pkOrg = ((User) user).getPkOrg();
        DateTime dt = DateTime.now();
        String nowdate = dt.toString("yyyy-MM-dd");//???????????????
        List<Map<String, Object>> todaylist = null;
        /**1.?????????????????????????????????*/
		/*if(Application.isSqlServer()){
			todaylist = this.schMapper.getTodaySchInfos(rescode, pkOrg, nowdate);
		}else{
			todaylist = this.schMapper.getTodaySchInfosOracle(rescode, pkOrg, nowdate);
		}*/
        todaylist = this.schMapper.getTodaySchInfos(rescode, pkOrg, nowdate, pkSchres);
        /**2.?????????templist????????????????????????????????????????????????????????????????????????????????????*/
        List<Map<String, Object>> templist = new ArrayList<Map<String, Object>>();
        for (Map<String, Object> element : todaylist) {
            String pkDateslot = element.get("pkDateslot").toString();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date dateWork = null;
            try {
                dateWork = sdf.parse(nowdate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            //????????????????????????????????????
            if (dt.isBefore(this.getSchEndTime(pkDateslot, dateWork))) {
                templist.add(element);
            }
        }
        /**3.????????????mainlist??????????????????????????????????????????????????????????????????????????????????????????????????????*/
        List<Map<String, Object>> mainlist = new ArrayList<Map<String, Object>>();
        Map<String, String> pkresmap = new HashMap<String, String>();
        for (Map<String, Object> temp : templist) {
            String pkRes = temp.get("pkRes").toString();
            String pkDateslot = temp.get("pkDateslot").toString();
            if (pkresmap.containsKey(pkRes) && !pkDateslot.equals(pkresmap.get(pkRes))) {
            } else {
                pkresmap.put(pkRes, pkDateslot);
                mainlist.add(temp);
            }
        }
        /**??????????????????????????????--??????*/
        Map<String, Map<String, Object>> srvMap = DataBaseHelper
                .queryListToMap("select so.pk_schsrv as key_, sum(t.price) as price from sch_srv_ord so "
                        + "inner join bd_ord_item ot on so.pk_ord = ot.pk_ord "
                        + "inner join bd_item t on ot.pk_item=t.pk_item where so.del_flag = '0' and ot.del_flag = '0' " + "group by so.pk_schsrv");
        /**??????????????????????????????--??????*/
        Map<String, Map<String, Object>> deptMap = DataBaseHelper
                .queryListToMap("select d.pk_dept as key_,d.name_dept as dept_belong_name from bd_ou_dept d where d.del_flag='0'");
        for (Map<String, Object> main : mainlist) {
            if (srvMap.get(main.get("pkSchsrv")) != null) {
                main.putAll(srvMap.get(main.get("pkSchsrv")));
            }
            if (deptMap.get(main.get("pkDeptBelong")) != null) {
                main.putAll(deptMap.get(main.get("pkDeptBelong")));
            }
        }
        return mainlist;
    }

    /**
     * ????????????????????????????????????????????????????????????????????? - ????????????
     *
     * @param param
     * @param user
     */
    public List<Map<String, Object>> getSchInfoByResAndTime(String param, IUser user) {
        Map paramMap = JsonUtil.readValue(param, Map.class);
        String rescode = paramMap.get("rescode") == null ? null : paramMap.get("rescode").toString();
        String pkSchres = paramMap.get("pkSchres") == null ? null : paramMap.get("pkSchres").toString();
        String pkOrg = ((User) user).getPkOrg();
        DateTime dt = DateTime.now();
        String nowDate = dt.toString("yyyyMMdd") + "000000";//???????????????
        String nowDay = dt.toString("MM-dd");//????????????
        String nowTime = dt.toString("HH:mm:ss");//????????????
        List<Map<String, Object>> todaylist = null;
        /**1.??????????????????????????????*/
        todaylist = this.schMapper.getTodaySchInfosByDate(rescode, pkOrg, nowDate, nowDay, nowTime, pkSchres);
        if (todaylist != null && todaylist.size() > 0) {
            /**??????????????????????????????--??????*/
            Map<String, Map<String, Object>> srvMap = DataBaseHelper
                    .queryListToMap("select so.pk_schsrv as key_, sum(t.price) as price from sch_srv_ord so "
                            + "inner join bd_ord_item ot on so.pk_ord = ot.pk_ord "
                            + "inner join bd_item t on ot.pk_item=t.pk_item where so.del_flag = '0' and ot.del_flag = '0' " + "group by so.pk_schsrv");
            for (Map<String, Object> main : todaylist) {
                if (srvMap.get(main.get("pkSchsrv")) != null) {
                    main.putAll(srvMap.get(main.get("pkSchsrv")));
                }
            }
        }
        return todaylist;
    }

    /**
     * ??????????????????
     *
     * @return
     */
    public List<SchInfoVo> getSchInfo(String param, IUser user) {
        User u = (User) user;
        Map<String, String> params = JsonUtil.readValue(param, Map.class);
        params.put("pkOrg", u.getPkOrg());
        List<SchSchVo> list = schMapper.getSchInfo(params);
        /**list??????*/
        //??????pkSchres:schresName:pkSchsrv:schsrvName ???list????????????
        ImmutableList<SchSchVo> digits = ImmutableList.copyOf(list);
        //???????????????pkSchres:schresName:pkSchsrv:schsrvName??????????????????
        Function<SchSchVo, String> group = new Function<SchSchVo, String>() {
            @Override
            public String apply(SchSchVo schSchVo) {
                return schSchVo.getPkSchres() + ":" + schSchVo.getSchresName() + ":" + schSchVo.getPkSchsrv() + ":" + schSchVo.getSchsrvName();
            }
        };
        //??????????????????
        ImmutableListMultimap<String, SchSchVo> groupMap = Multimaps.index(digits, group);
        List<SchInfoVo> infoList = Lists.newArrayList();
        for (String key : groupMap.keySet()) {
            String[] arr = key.split(":");
            String schsrvName = "";
            if (arr.length == 4) {
                schsrvName = arr[3];
            }
            SchInfoVo infoVo = new SchInfoVo(arr[0], arr[1], arr[2], schsrvName);
            List<SchSchVo> schVos = groupMap.get(key);
            Set<String> pkDateslots = Sets.newHashSet();
            for (SchSchVo schSchVo : schVos) {
                pkDateslots.add(schSchVo.getPkDateslot());
            }
            infoVo.setBdCodeDateslots(codeMapper.getBdCodeDateslotByPkForType(pkDateslots));
            infoVo.setSchschs(schVos);
            infoVo.setPkDateslots(pkDateslots);
            infoList.add(infoVo);
        }
        return infoList;
    }

    /**
     * ???????????? ????????????????????????
     *
     * @param param
     * @param user
     * @return
     */
    public Page<SchSchVo> getSchInfoZs(String param, IUser user) {
        User u = (User) user;
        Map<String, String> params = JsonUtil.readValue(param, Map.class);
        params.put("pkOrg", u.getPkOrg());
        //???????????????=1????????????SCH0012????????????
        String flagRole = params.get("flagRole");
        if (EnumerateParameter.ONE.equals(flagRole)) {
            //??????????????????????????????????????????????????????
            params.put("ftByUser", ApplicationUtils.getSysparam("SCH0012", false));
            params.put("pkEmpJob", u.getPkEmp());
            params.put("pkUser", u.getPkUser());
            params.put("pkDeptArea", u.getPkDept());
        }
        params.put("dbType",MultiDataSource.getCurDbType());
        MyBatisPage.startPage(MapUtils.getIntValue(params, "pageIndex"), MapUtils.getIntValue(params, "pageSize"));
        List<SchSchVo> list = schMapper.getSchInfoZs(params);
        if(Application.isSqlServer() && CollectionUtils.isNotEmpty(list)){
            List<SchSchVo> countList = DataBaseHelper.queryForList("select pk_sch, count(1) cnt_appt_sur from sch_TICKET  t where t.flag_appt='1' and t.flag_used='0' and t.FLAG_STOP='0' and t.pk_sch in("
                    + CommonUtils.convertSetToSqlInPart(list.stream().map(v -> v.getPkSch()).collect(Collectors.toSet()), "pk_sch") + ") group by pk_sch", SchSchVo.class);
            Map<String, String> mapCount = countList.parallelStream().collect(Collectors.toMap(SchSchVo::getPkSch, SchSchVo::getCntApptSur));
            list.parallelStream().forEach(v -> v.setCntApptSur(mapCount.get(v.getPkSch())));
        }
        Page<SchSchVo> page = MyBatisPage.getPage();
        page.setRows(list);
        return page;
    }

    /**
     * ????????????009002002025
     * ?????????????????????????????????????????????
     *
     * @param param
     * @param user
     * @return
     */
    public List<SchSchVo> qrySchByPkSchSrv(String param, IUser user) {
        List<SchSchVo> rtnList = new ArrayList<>();
        Map<String, String> params = JsonUtil.readValue(param, Map.class);
        if (params != null && params.size() > 0) {
            rtnList = schMapper.qrySchByPkSchSrv(params);
        }
        return rtnList;
    }

    /**
     * ?????????????????????????????????????????????????????????????????????
     * ????????????009002002026
     *
     * @param param
     * @param user
     * @return
     */
    public List<SchSchVo> qrySchByParam(String param, IUser user) {
        List<SchSchVo> rtnList = new ArrayList<SchSchVo>();
        @SuppressWarnings("unchecked")
        Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
        if (paramMap != null) {
            String pkDateslot = (String) paramMap.get("pkDateslot");
            String pkSchres = (String) paramMap.get("pkSchres");
            String dateWork = (String) paramMap.get("dateWork");
            StringBuilder sb = new StringBuilder();
            Object[] objArray = null;
            sb.append("SELECT sch.* ");
            sb.append("FROM   sch_sch sch ");
            sb.append("WHERE  sch.date_work = to_date(?,'yyyy-mm-dd') ");
            sb.append("AND    sch.Pk_Dateslot = ? ");
            sb.append("AND    sch.pk_schres = ? ");
            sb.append("AND    sch.eu_status = '8' ");
            sb.append("AND    sch.del_flag = '0' ");
            objArray = new Object[]{dateWork, pkDateslot, pkSchres};

            String sql = sb.toString();
            rtnList = DataBaseHelper.queryForList(sql, SchSchVo.class, objArray);
        }
        return rtnList;
    }


    /**
     * ??????????????????
     *
     * @param param
     * @param user
     * @return
     */
    public Map<String, Object> getSchDetail(String param, IUser user) {
        String pkSch = JSON.parseObject(param).getString("pkSch");
        SchSch schSch = DataBaseHelper.queryForBean("select * from sch_sch where del_flag = '0' and pk_sch = ? ",
                SchSch.class, new Object[]{pkSch});
        String euRestype = DataBaseHelper.queryForScalar("select eu_restype from sch_resource where pk_schres=?", String.class, schSch.getPkSchres());
        List<SchEmp> schEmps = DataBaseHelper.queryForList("select * from SCH_EMP where del_flag = '0' and pk_sch = ? ",
                SchEmp.class, new Object[]{pkSch});
        List<SchPvtype> schPvtypes = DataBaseHelper.queryForList(
                "select * from SCH_PVTYPE where del_flag = '0' and pk_sch = ? order by EU_PVTYPE", SchPvtype.class,
                new Object[]{pkSch});
        List<SchTicket> schTickets = DataBaseHelper.queryForList(
                "select * from SCH_TICKET where del_flag = '0' and pk_sch = ? order by cast(case when ticketno is null then '0' else ticketno end as int)",
                SchTicket.class, new Object[]{pkSch});
        int pkSchplanCheck = DataBaseHelper.queryForScalar("select count(1) from sch_plan where del_flag = '0' and pk_schplan = ?", Integer.class, new Object[]{schSch.getPkSchplan()});
        Map<String, Object> resultMap = Maps.newHashMap();
        resultMap.put("euRestype", euRestype);
        resultMap.put("schSch", schSch);
        resultMap.put("schEmps", schEmps);
        resultMap.put("schPvtypes", schPvtypes);
        resultMap.put("schTickets", schTickets);
        resultMap.put("pkSchplanCheck", pkSchplanCheck > 0 ? schSch.getPkSchplan() : null);
        return resultMap;
    }

    /**
     * ??????????????????
     *
     * @param param
     * @param user
     * @return
     */
    public void deleteSch(String param, IUser user) {
        List<String> pkSchs = JsonUtil.readValue(param, List.class);
        User u = (User)user;
        Date date = new Date();
        schMapper.deleteSchSch(pkSchs,date,u.getPkEmp(),date);
        schMapper.deleteSchEmp(pkSchs,date,u.getPkEmp(),date);
        schMapper.deleteSchPvtype(pkSchs,date,u.getPkEmp(),date);
        schMapper.deleteSchTicket(pkSchs,date,u.getPkEmp(),date);
        //????????????????????????
        List<SchModlog> schModlogs = new ArrayList<>();
        for(String str:pkSchs){
            SchModlog schModlog = new SchModlog();
            schModlog.setPkSchlog(NHISUUID.getKeyId());
            schModlog.setPkSch(str);
            schModlog.setEuType("99");//??????
            schModlog.setReasons("??????");
            schModlogs.add(schModlog);
        }
        handlerService.saveSchModLogBatch(schModlogs);
    }

    /**
     * ??????????????????
     *
     * @param param
     * @param user
     */
    public List<SchSaveParam> saveSch(String param, IUser user) {
        List<SchSaveParam> schSaveParams = JsonUtil.readValue(param, new TypeReference<List<SchSaveParam>>() {
        });
        Map<String, Object> paramMapUpdate = new HashMap<String, Object>();
        Map<String, Object> paramMapAdd = new HashMap<String, Object>();
        //????????????????????????
        if (schSaveParams.size() == 1) {
            //??????????????????????????????
            List<SchEmp> schEmps = schSaveParams.get(0).getSchEmps();
            if (schEmps != null && schEmps.size() > 0) {
                Set<String> pkEmps = Sets.newHashSet();
                for (SchEmp temp : schEmps) {
                    pkEmps.add(temp.getPkEmp());
                }
                if (pkEmps.size() != schEmps.size()) {
                    throw new BusException("?????????????????????");
                }
            }
            //?????????????????????????????????????????????????????????
            String pkSch = schSaveParams.get(0).getSchSch().getPkSch();
            if (!StringUtils.isEmpty(pkSch)) {
                //count??????0?????????????????????
                Integer count = DataBaseHelper.queryForScalar("SELECT count(*) FROM SCH_SCH WHERE CNT_USED>0 AND PK_SCH=?", Integer.class, pkSch);
                Integer cntTotal = DataBaseHelper.queryForScalar("SELECT CNT_TOTAL FROM SCH_SCH WHERE PK_SCH=? ", Integer.class, pkSch);
                // ?????????????????????????????????
                if (count > 0 && cntTotal != schSaveParams.get(0).getSchSch().getCntTotal()) {
                    throw new BusException("?????????????????????????????????????????????");
                }
            }
            //???????????????,
            SchSaveParam schSpFirst = schSaveParams.get(0);
            SchSch sch = schSpFirst.getSchSch();
            /**
             * ????????????????????????
             */
            SchSch schSch = DataBaseHelper.queryForBean("select pk_sch from sch_sch where  pk_schres =? and pk_schsrv = ? and pk_dateslot = ? and to_char(date_work,'YYYY-MM-DD') = ?", SchSch.class, new Object[]{sch.getPkSchres(), sch.getPkSchsrv(), sch.getPkDateslot(), FastDateFormat.getInstance("yyyy-MM-dd").format(sch.getDateWork())});
            if (schSch != null) {
                List<String> pkSchList = new ArrayList<>();
                pkSchList.add(schSch.getPkSch());
                String status = "update";
                paramMapUpdate.put("status", status);
                paramMapUpdate.put("pkSchList", pkSchList);
                PlatFormSendUtils.sendPvOpNoMsg(paramMapUpdate);
            }
            //BUG25250 ???????????????????????????????????????????????????????????????????????????????????????
            //DataBaseHelper.update("delete from sch_sch where  pk_schres =? and pk_schsrv = ? and pk_dateslot = ? and to_char(date_work,'YYYY-MM-DD') = ?", new Object[]{sch.getPkSchres(), sch.getPkSchsrv(), sch.getPkDateslot(), FastDateFormat.getInstance("yyyy-MM-dd").format(sch.getDateWork())});
            //sch.setPkSch(null);
        }
        User u = (User) user;
//		String pkOrgarea = handlerService.getCurrentOrgAreaPk(u);
        List<String> pkSchList = new ArrayList<>();
        for (SchSaveParam schSaveParam : schSaveParams) {
            SchSch schSch = schSaveParam.getSchSch();
            String pkSch = schSch.getPkSch();
            List<SchEmp> schEmps = schSaveParam.getSchEmps();
            List<SchPvtype> schPvtypes = schSaveParam.getSchPvtypes();
            List<SchPlanWeekDtVo> listAddToTicket = schSaveParam.getListPlanWeekDt();
            //pkSchList.add(pkSch);
            Map<String, Object> map = DataBaseHelper.queryForMap("select pk_orgarea from bd_ou_dept dept INNER JOIN SCH_RESOURCE res on DEPT.PK_DEPT = RES.PK_DEPT_BELONG WHERE RES.PK_SCHRES=? ", new Object[]{schSch.getPkSchres()});
            String pkOrgarea = MapUtils.getString(map, "pkOrgarea");
            if (schSch.getPkSch() == null) {
                schSch.setEuStatus(StringUtils.isBlank(schSch.getEuStatus()) ? SchEuStatus.INIT.getStatus() : schSch.getEuStatus());
                schSch.setPkOrgarea(pkOrgarea);
                schSch.setPkEmpSch(u.getPkEmp());
                schSch.setNameEmpSch(u.getNameEmp());
                schSch.setDateSch(schSch.getDateSch() == null ? (new Date()) : schSch.getDateSch());
                String pkschtemp = NHISUUID.getKeyId();
                schSch.setPkSch(pkschtemp);
                DataBaseHelper.insertBean(schSch);
                pkSch = schSch.getPkSch();
                pkSchList.add(pkschtemp);
            } else {
                DataBaseHelper.updateBeanByPk(schSch, false);
                pkSch = schSch.getPkSch();
            }
            if (schEmps != null) {
                //?????????
                DataBaseHelper.execute("update sch_emp set del_flag=1 where pk_sch=?", new Object[]{schSch.getPkSch()});
                // ??????schEmp
                if (schEmps.size() == 0) {
                    schMapper.deleteSchEmpByPkSch(pkSch);
                }
                for (SchEmp schEmp : schEmps) {
                    if (schEmp.getPkSchemp() == null) {
                        schEmp.setPkSch(pkSch);
                        //					DataBaseHelper.insertBean(schEmp);
                    } else {
                        DataBaseHelper.updateBeanByPk(schEmp, false);
                    }
                }
            }
            if (schPvtypes != null) {
                // ??????schPvtypes
                if (schPvtypes.size() == 0) {
                    schMapper.deleteSchPvtypeByPkSch(pkSch);
                }
                for (SchPvtype schPvtype : schPvtypes) {
                    if (schPvtype.getPkSchpvtype() == null) {
                        schPvtype.setPkSch(pkSch);
                        DataBaseHelper.insertBean(schPvtype);
                    } else {
                        DataBaseHelper.updateBeanByPk(schPvtype, false);
                    }
                }
            }
            boolean flag = DataBaseHelper.queryForScalar("select count(*) from SCH_TICKET where pk_sch = ?",
                    Integer.class, new Object[]{pkSch}) > 0;
            // ????????????
            if (flag) {
                Map<String, Object> mapRes = DataBaseHelper.queryForMap("select pk_ticketrules from sch_resource where pk_schres=?", new Object[]{schSch.getPkSchres()});
                generateSchTicket1(schSch, MapUtils.getString(mapRes, "pkTicketrules"), schPvtypes);
            } else {
                generateSchTicket1(schSch, null, schPvtypes);
            }
        }
        /**
         * ????????????????????????
         */
        String status = "add";
        paramMapAdd.put("status", status);
        paramMapAdd.put("pkSchList", pkSchList);
        PlatFormSendUtils.sendPvOpNoMsg(paramMapAdd);
        return schSaveParams;
    }

    /**
     * ??????????????????????????????
     *
     * @param pkDateslot ??????????????????
     * @param dateWork   ?????????????????????
     * @return
     */
    private DateTime getSchBeginTime(String pkDateslot, Date dateWork) {
        DateTime dt = new DateTime(dateWork);
        String dateWorkStr = dt.toString("MM-dd");
        String yeardate = dt.toString("yyyy-MM-dd");
        BdCodeDateslotTime bdCodeDateslotTime = DataBaseHelper.queryForBean(
                "select *  from bd_code_dateslot_time where del_flag = '0' "
                        + "and pk_dateslot = ? "
                        + "and lpad(valid_month_begin,2,'0') || '-' || lpad(valid_day_begin,2,'0') <= ? "
                        + "and lpad(valid_month_end,2,'0') || '-' || lpad(valid_day_end,2,'0') >= ? ",
                BdCodeDateslotTime.class, pkDateslot, dateWorkStr, dateWorkStr);
        DateTimeFormatter dtf = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
        DateTime begin;
        if (bdCodeDateslotTime == null) {
            BdCodeDateslot bdCodeDateslot = DataBaseHelper.queryForBean(
                    "select * from bd_code_dateslot where pk_dateslot = ?", BdCodeDateslot.class,
                    pkDateslot);
            if (bdCodeDateslot == null)
                throw new BusException("?????????????????????");
            begin = DateTime.parse(yeardate + " " + bdCodeDateslot.getTimeBegin(), dtf);
        } else {
            begin = DateTime.parse(yeardate + " " + bdCodeDateslotTime.getTimeBegin(), dtf);
        }
        return begin;
    }

    /**
     * ??????????????????????????????
     *
     * @param pkDateslot ??????????????????
     * @param dateWork   ?????????????????????
     * @return
     */
    private DateTime getSchEndTime(String pkDateslot, Date dateWork) {
        DateTime dt = new DateTime(dateWork);
        String dateWorkStr = dt.toString("MM-dd");
        String yeardate = dt.toString("yyyy-MM-dd");
        BdCodeDateslotTime bdCodeDateslotTime = DataBaseHelper.queryForBean(
                "select *  from bd_code_dateslot_time where del_flag = '0' "
                        + "and pk_dateslot = ? "
                        + "and lpad(valid_month_begin,2,'0') || '-' || lpad(valid_day_begin,2,'0') <= ? "
                        + "and lpad(valid_month_end,2,'0') || '-' || lpad(valid_day_end,2,'0') >= ? ",
                BdCodeDateslotTime.class, pkDateslot, dateWorkStr, dateWorkStr);
        DateTimeFormatter dtf = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
        DateTime end;
        if (bdCodeDateslotTime == null) {
            BdCodeDateslot bdCodeDateslot = DataBaseHelper.queryForBean(
                    "select * from bd_code_dateslot where pk_dateslot = ?", BdCodeDateslot.class,
                    pkDateslot);
            if (bdCodeDateslot == null)
                throw new BusException("?????????????????????");
            end = DateTime.parse(yeardate + " " + bdCodeDateslot.getTimeEnd(), dtf);
        } else {
            end = DateTime.parse(yeardate + " " + bdCodeDateslotTime.getTimeEnd(), dtf);
        }
        return end;
    }

    /**
     * ????????????
     *
     * @param schSch     ???????????????
     * @param cntTotal   ????????????????????????
     * @param ticketNos  ???????????????
     * @param schTickets
     */
    private void insertTickets(SchSch schSch, int cntTotal, List<Integer> ticketNos, List<SchTicket> schTickets) {
//		DateTime begin = new DateTime(DataBaseHelper.queryForScalar("select min(BEGIN_TIME) from SCH_TICKET where del_flag = '0' and pk_sch = ?",
//					Date.class, schSch.getPkSch()));
        DateTime begin = getSchBeginTime(schSch.getPkDateslot(), schSch.getDateWork());
        int minutePer = schSch.getMinutePer();
        int schTicketsSize = CollectionUtils.isNotEmpty(schTickets) ? schTickets.size() : 0;
        for (int i = 1; i <= cntTotal; i++) {
            int ticketno = i;
            SchTicket schTicket = new SchTicket();
            schTicket.setPkSch(schSch.getPkSch());
            schTicket.setFlagAppt(ticketNos.contains(i) ? "1" : "0");
            if (i <= schTicketsSize && Integer.parseInt(schTickets.get(i - 1).getTicketno()) == i) {
                schTicket.setFlagUsed(schTickets.get(i - 1).getFlagUsed());
            } else {
                schTicket.setFlagUsed("0");
            }
            schTicket.setFlagStop("0");
            schTicket.setBeginTime(begin.plusMinutes((ticketno - 1) * minutePer).toDate());
            schTicket.setEndTime(begin.plusMinutes(ticketno * minutePer).toDate());
            schTicket.setTicketno(ticketno + "");
            DataBaseHelper.insertBean(schTicket);
        }
    }

    /**
     * ??????????????????????????????
     */
    private void generateSchTicket1(SchSch schSch, String pkTicketrules, List<SchPvtype> schPvtypes) {
//		if (schPvtypes == null || schPvtypes.isEmpty())
//			return;
        genTicket(schSch, pkTicketrules, schPvtypes, null);
    }

    private void genTicket(SchSch schSch, String pkTicketrules, List<SchPvtype> schPvtypes, List<SchPlanWeekDt> listPlanweekDt) {
        List<SchTicket> schTickets = getAndDelTicket(schSch);
        if (CollectionUtils.isNotEmpty(listPlanweekDt)) {
            insertTicketsByPlanweekDt(schSch, schTickets, listPlanweekDt);
        } else {
            List<Integer> ticketNos = handlerService.getAppointmentNos(schSch.getCntTotal(), pkTicketrules);
            insertTickets(schSch, schSch.getCntTotal(), ticketNos, schTickets);
        }
        generateSchPvtype(schSch.getPkSch(), schPvtypes);
    }

    private List<SchTicket> getAndDelTicket(SchSch schSch) {
        List<SchTicket> schTickets = DataBaseHelper.queryForList("select * from sch_ticket where pk_sch=? order by ticketno", SchTicket.class, schSch.getPkSch());
        DataBaseHelper.execute("delete from sch_ticket where pk_sch = ?",
                new Object[]{schSch.getPkSch()});
        return schTickets;
    }

    private void genTicketByTimePeriod(SchSch schSch, String pkTicketrules, List<SchPlanWeekDtVo> listPlanweekDt) {
        List<SchTicket> schTickets = getAndDelTicket(schSch);
        insertTicketsByPlanweekDtVo(schSch, schTickets, listPlanweekDt);
    }


    private void generateSchPvtype(String pkSch, List<SchPvtype> schPvtypes) {
        if (schPvtypes != null) {
            DataBaseHelper.update("update SCH_TICKET set eu_pvtype = null where pk_sch = ?", new Object[]{pkSch});
            for (SchPvtype schPvtype : schPvtypes) {
                if (StringUtils.isNotEmpty(schPvtype.getTicketrules())) {
                    List<String> ticketnos = Arrays.asList(schPvtype.getTicketrules().split(","));
                    schMapper.updateSchTicketnoType(pkSch, schPvtype.getEuPvtype(), ticketnos);
                }
            }
        }
    }

    /**
     * ??????????????????
     */
    private void generateSchplanEmp(SchSch schSch, String pkSchplan) {
        List<SchPlanEmp> emps = schMapper.getSchPlanEmpByPkSchplan(pkSchplan);
        for (SchPlanEmp planEmp : emps) {
            SchEmp schEmp = new SchEmp();
            schEmp.setPkSch(schSch.getPkSch());
            schEmp.setPkOrg(planEmp.getPkOrg());
            schEmp.setNameEmp(planEmp.getNameEmp());
            schEmp.setPkEmp(planEmp.getPkEmp());
            schEmp.setCntTotal(schSch.getCntTotal());
            schEmp.setCntUsed(0);
            DataBaseHelper.insertBean(schEmp);
        }
    }

    /**
     * ????????????????????????
     */
    private List<SchPvtype> generateSchPvType(String pkSch, String pkPlanweek) {
        List<SchPlanweekPvtype> schPlanweekPvtypes = schMapper.getSchPlanweekPvtypeByPkSchplanweek(pkPlanweek);
        List<SchPvtype> schPvtypes = Lists.newArrayList();
        for (SchPlanweekPvtype schPlanweekPvtype : schPlanweekPvtypes) {
            SchPvtype schPvtype = new SchPvtype();
            schPvtype.setPkSch(pkSch);
            schPvtype.setEuPvtype(schPlanweekPvtype.getEuPvtype());
            schPvtype.setTicketrules(schPlanweekPvtype.getTicketrules());
            schPvtype.setFlagStop("0");
            schPvtype.setCntTotal(schPlanweekPvtype.getCntAppt());
            schPvtype.setCntUsed(0);
            DataBaseHelper.insertBean(schPvtype);
            schPvtypes.add(schPvtype);
        }
        return schPvtypes;
    }

    /**
     * ??????????????????
     *
     * @param param
     * @param user
     * @throws ParseException
     */
    @SuppressWarnings("unchecked")
    public void generateSch(String param, IUser user) throws ParseException {
        Map<String, String> params = JsonUtil.readValue(param, Map.class);
        genSch(user, params);
    }

    /**
     * ????????????????????????????????????????????????????????????
     *
     * @param param
     * @param user
     */
    public void genSchByPlan(String param, IUser user) {
        Set<String> pkSchplans = JsonUtil.readValue(param, new TypeReference<Set<String>>() {
        });

        List<SchPlan> schPlans = DataBaseHelper.queryForList("select * from sch_plan where nvl(FLAG_STOP,'0')='0' and pk_schplan in (" + CommonUtils.convertSetToSqlInPart(pkSchplans, "pk_schplan") + ")", SchPlan.class);
        if (schPlans != null && schPlans.size() > 0) {
            for (SchPlan schPlan : schPlans) {
                Map<String, Object> paramSch = Maps.newHashMap();
                paramSch.put("pkSchplan", schPlan.getPkSchplan());

                paramSch.put("isCover", "1");
                paramSch.put("euSchclass", "0");
                paramSch.put("pkOrg", UserContext.getUser().getPkOrg());

                Date nowDate = DateUtils.getSpecifiedDay(new Date(), 1);
                paramSch.put("beginDate", DateUtils.formatDate(nowDate, "yyyy-MM-dd"));

                //????????????
                int cyclePlan = schPlan.getCyclePlan() == null || schPlan.getCyclePlan().intValue() <= 0 ? 7 : schPlan.getCyclePlan().intValue();
                if (schPlan.getDateEnd() != null && DateUtils.getDateSpace(nowDate, schPlan.getDateEnd()) <= cyclePlan) {
                    if (DateUtils.getDateSpace(nowDate, schPlan.getDateEnd()) > 0)
                        paramSch.put("endDate", DateUtils.formatDate(schPlan.getDateEnd(), "yyyy-MM-dd"));
                    else
                        return;
                } else {
                    paramSch.put("endDate", DateUtils.addDate(nowDate, cyclePlan - 1, 3, "yyyy-MM-dd"));
                }

                //??????????????????????????????????????????
                try {
                    createSchPublishByPlan(paramSch, UserContext.getUser());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public void createSchPublishByPlan(Map<String, Object> paramSch, User user) {
        try {
            //????????????
            List<String> pkSchs = generateSchByTimeInterval(JsonUtil.writeValueAsString(paramSch), user);
            Set<String> pkSchSet = new HashSet<String>(pkSchs);

            //????????????
            List<String> pkSchList = DataBaseHelper.getJdbcTemplate().queryForList("select pk_sch from sch_sch where PK_SCH in ("+CommonUtils.convertSetToSqlInPart(pkSchSet,"pk_sch")+")", String.class);
            if (pkSchList != null && pkSchList.size() > 0) {
                Map<String, Object> auditInfo = new HashMap<>();
                auditInfo.put("pkEmpChk", UserContext.getUser().getPkEmp());
                auditInfo.put("dateChk", new Date());
                Map<String, Object> paramMapPublish = new HashMap<>();
                paramMapPublish.put("euStatus", EnumerateParameter.EIGHT);
                paramMapPublish.put("auditInfo", auditInfo);
                paramMapPublish.put("isReferStatus", "true");
                paramMapPublish.put("pkSchs", pkSchList);
                schAuditService.saveAudit(JsonUtil.writeValueAsString(paramMapPublish), user);
            }
        } catch (ParseException e) {
            throw new BusException(e.getMessage());
        }
    }

    /**
     * ??????????????????-?????????????????????<br>
     * ????????????????????????????????????????????????????????????????????????
     *
     * @param param
     * @param user
     * @throws ParseException
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<String> generateSchByTimeInterval(String param, IUser user) throws ParseException {
        Map<String, String> params = JsonUtil.readValue(param, Map.class);
        params.put("hasWeekDt", Boolean.toString(true));
        return genSch(user, params);
    }

    /**
     * ????????????<br>
     * <b>1.??????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
     * 2.??????????????????????????????????????????????????????????????????????????????????????????????????????
     * 3.??????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
     * 4.?????????????????????????????????????????????????????????????????? ?????????/???????????? ????????????</b>
     * <br><i>?????????????????????????????????????????????????????????????????????????????????????????????</i>
     *
     * @param user
     * @param params
     */
    private List<String> genSch(IUser user, Map<String, String> params) {
        User u = ((User) user);
        String beginDate = params.get("beginDate");
        String endDate = params.get("endDate");
        String flagModi = params.get("flagModi");
        //String isAudit = params.get("isGenAudit");????????????????????????????????????????????????
        boolean hasWeekDt = MapUtils.getBooleanValue(params, "hasWeekDt");
        boolean isCover = StringUtils.equals("1", params.get("isCover"));
        if (StringUtils.isBlank(beginDate) || StringUtils.isBlank(endDate)) {
            throw new BusException("????????????????????????????????????");
        }
        params.put("pkOrg", u.getPkOrg());
        List<SchPlanWithWeek> plans = schMapper.getSchplanWithWeek(params);
        if (plans == null || plans.size() == 0) {
            throw new BusException("?????????????????????????????????");
        }
        
        //String pkOrgarea = handlerService.getCurrentOrgAreaPk(u);
        /**
         * 1.??????????????????????????????	2.???????????????????????????????????????	3.??????????????????????????????????????????	4.????????????
         * ???????????????????????????????????? ?????????????????????????????????????????????????????? ?????????????????????????????????????????????????????????????????????????????????
         */
        Map<String, Object> prms = Maps.newHashMap();
        prms.put("beginDate", beginDate);
        prms.put("endDate", endDate);
        List<String> pkSchList = new ArrayList<>();
        if (isCover) {
            // ?????? ??????????????????????????????????????????????????????????????????
            prms.put("pkSchplan", params.get("pkSchplan"));
            prms.put("euStatusN", SchEuStatus.AUDIT.getStatus());
            prms.put("pkDept", MapUtils.getString(params, "pkDept"));
            schMapper.deleteSchSchByPlanWeeks(prms);
        }
        DateTimeFormatter dtf = DateTimeFormat.forPattern("yyyy-MM-dd");
        DateTime dtBegin = DateTime.parse(beginDate, dtf);
        DateTime dtEnd = DateTime.parse(endDate, dtf);
        int daysBetween = Days.daysBetween(dtBegin, dtEnd).getDays() + 1;
        List<BdWorkcalendardate> workDates = null;
        for (SchPlanWithWeek schPlanWithWeek : plans) {// ????????????
            String pkSchplan = schPlanWithWeek.getPkSchplan();
            String pkEmp = schPlanWithWeek.getPkEmp();
            // ??????????????????
            params.put("pkWorkcalendar", schPlanWithWeek.getPkWorkcalendar());
            //TODO~~???????????????????????????????????????????????????????????????????????????????????????
            workDates = codeMapper.getWordcalendardate(params);
            if (workDates == null || workDates.isEmpty() || daysBetween != workDates.size()) {
                throw new BusException("??????????????????");
            }
            if (hasWeekDt && DataBaseHelper.queryForScalar("select count(*) from SCH_PLANWEEK_DT where PK_PLANWEEK=? and PK_DATESLOT=?",
                    Integer.class, new Object[]{schPlanWithWeek.getPkPlanweek(), schPlanWithWeek.getPkDateslot()}) == 0) {
                throw new BusException("????????????????????????" + schPlanWithWeek.getPlName() + "???-???" + schPlanWithWeek.getWeekNo() + "???-???" + schPlanWithWeek.getNameDateslot() + "???????????????????????????");
            }
            //???????????????+??????+?????????+?????????????????????????????????????????????????????????????????????
            Map<String, Object> schParam = Maps.newHashMap();
            schParam.put("beginDate", beginDate);
            schParam.put("endDate", endDate);
//            schParam.put("pkSchplan", pkSchplan);//??????BUG???????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
            schParam.put("exclude0cnt", true);
            schParam.put("pkSchres",schPlanWithWeek.getPkSchres());
            schParam.put("pkDateslot", schPlanWithWeek.getPkDateslot());
            List<Date> schSchDateworks = schMapper.getSchSchDateworks(schParam);
            //??????????????????
            List<Map<String,Object>> schSchstopworks = new ArrayList<Map<String,Object>>();
            if(CommonUtils.isNotNull(pkEmp)) {
            	schSchstopworks = schMapper.getSchstopworks(pkEmp);
            }
            Map<String, Object> map = DataBaseHelper.queryForMap("select pk_orgarea from bd_ou_dept where pk_dept = ? ", new Object[]{schPlanWithWeek.getPkDept()});
            String pkOrgarea = MapUtils.getString(map, "pkOrgarea");
            // ?????????????????? ??????????????????
            for (BdWorkcalendardate bdWorkcalendardate : workDates) {
                if ("1".equals(bdWorkcalendardate.getDatetype())
                        && schPlanWithWeek.getWeekNo().equals(bdWorkcalendardate.getWeekno())) {// ?????????
                    if (schSchDateworks.contains(bdWorkcalendardate.getCalendardate())) {
                        continue;
                    }
                    //?????????????????????????????????????????????
                    if(schSchstopworks != null && hasCurDate(schSchstopworks,schPlanWithWeek.getEuNoon(),bdWorkcalendardate.getCalendardate())) {
                    	continue;
                    }
                    // ???????????????
                    SchSch schSch = new SchSch();
                    schSch.setDateWork(bdWorkcalendardate.getCalendardate());
                    schSch.setPkDateslot(schPlanWithWeek.getPkDateslot());
                    schSch.setPkPlanweek(schPlanWithWeek.getPkPlanweek());
                    schSch.setPkSchplan(schPlanWithWeek.getPkSchplan());
                    schSch.setPkSchres(schPlanWithWeek.getPkSchres());
                    schSch.setPkSchsrv(schPlanWithWeek.getPkSchsrv());
                    schSch.setCntTotal(schPlanWithWeek.getCntTotal());
                    schSch.setCntAdd(schPlanWithWeek.getCntAdd());
                    schSch.setCntAppt(schPlanWithWeek.getCntAppt());
                    schSch.setCntOver(0);
                    schSch.setCntUsed(0);
                    schSch.setFlagStop("0");
                    schSch.setMinutePer(schPlanWithWeek.getMinutePer());
                    schSch.setPkDept(schPlanWithWeek.getPkDept());
                    schSch.setEuSchclass(schPlanWithWeek.getEuSchclass());
                    schSch.setPkOrg(u.getPkOrg());
                    schSch.setFlagModi(flagModi);
                    schSch.setEuStatus(SchEuStatus.INIT.getStatus());
                    schSch.setPkOrgarea(pkOrgarea);
                    schSch.setPkEmpSch(u.getPkEmp());
                    schSch.setNameEmpSch(u.getNameEmp());
                    schSch.setDateSch(new Date());
                    schSch.setEuAppttype(schPlanWithWeek.getEuAppttype());
                    schSch.setPkDeptunit(schPlanWithWeek.getPkDeptunit());
                    String pkSch = NHISUUID.getKeyId();
                    schSch.setPkSch(pkSch);
                    DataBaseHelper.insertBean(schSch);
                    pkSchList.add(pkSch);
                    // ????????????????????????
                    List<SchPvtype> schPvtypes = generateSchPvType(schSch.getPkSch(), schPlanWithWeek.getPkPlanweek());
                    // ????????????
                    generateSchplanEmp(schSch, pkSchplan);
                    // ??????
                    if ("1".equals(schPlanWithWeek.getFlagTicket())) {
                        if (hasWeekDt) {
                            List<SchPlanWeekDt> listPlanweekDt = DataBaseHelper.queryForList("select * from SCH_PLANWEEK_DT where PK_PLANWEEK=?  order by TIME_BEGIN",
                                    SchPlanWeekDt.class, schPlanWithWeek.getPkPlanweek());
                            genTicket(schSch, null, schPvtypes, listPlanweekDt);
                        } else {
                            generateSchTicket1(schSch, schPlanWithWeek.getPkTicketrules(), schPvtypes);
                        }
                    }
                }
            }
        }
        String status = "add";
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("status", status);
        paramMap.put("pkSchList", pkSchList);
        PlatFormSendUtils.sendPvOpNoMsg(paramMap);

        return pkSchList;
    }
    
    
    /**  
    * <p>Desc:???????????????????????????????????????????????????????????????</p>  
    * @param  : 
    * @author : wangpengyong  
    * @date   : 2021???6???7???  
    */  
    public boolean  hasCurDate(List<Map<String,Object>> schSchstopworks,String euNoon,Date cDate) {
    	boolean  dFlag = false;
    	if(null != cDate && (null !=schSchstopworks && schSchstopworks.size() > 0) ) {
    		for(Map<String,Object> map:schSchstopworks) {
    			Date dateStop = map.get("DATESTOP")==null?null:(Date)map.get("DATESTOP");//????????????
    			Date dateEnd =  map.get("DATEEND")==null?null:(Date)map.get("DATEEND");//????????????
    			String stopEuNoon = map.get("EUNOON")==null?null:map.get("EUNOON").toString();//????????????????????????
    			
    			// ??????????????????,????????????????????????????????????????????????,???????????????
    			// (euNoon:???????????????????????????????????????????????????????????????????????????????????????????????????????????????)
    			boolean sameEuNoon = false;
    			if (StringUtils.isBlank(stopEuNoon)) {
    				sameEuNoon = true;//????????????
    			} else if(StringUtils.isNotBlank(euNoon) && StringUtils.isNotBlank(stopEuNoon) && euNoon.equals(stopEuNoon)) {
    				sameEuNoon = true;
    			}
    			
    			if(null != dateStop && null != dateEnd ) {//??????/???????????????
    				if((DateUtils.compareToDay(cDate,dateStop)>=0 && DateUtils.compareToDay(cDate,dateEnd)<=0) && sameEuNoon) {
    					dFlag = true;
            			break;
    				}
            	} else if(null != dateStop){
            		if((DateUtils.compareToDay(cDate,dateStop)>=0) && sameEuNoon) {//???????????????
    					dFlag = true;
            			break;
    				}
            	} else if(null != dateEnd){
            		if(DateUtils.compareToDay(cDate,dateEnd)<=0 && sameEuNoon) {//???????????????
    					dFlag = true;
            			break;
    				}
            	} 
    		}
    	}
    	return dFlag;
    }
    

    /**
     * ????????????
     *
     * @param param
     * @param user
     */
    public void adjustSch(String param, IUser user) {
        User u = (User) user;
        SchAdjustParam adjustParam = JsonUtil.readValue(param, SchAdjustParam.class);
        List<SchExclude> schExcludes = adjustParam.getSchExcludes();
        List<SchAdjustDate> schAdjustDates = adjustParam.getSchAdjustDates();
        String euSchclass = adjustParam.getEuSchclass();
        // 0????????????1?????????
        String adjustType = adjustParam.getAdjustType();
        ImmutableListMultimap<String, SchExclude> groupMap = null;
        if (schExcludes != null) {
            // ???????????????????????? ?????????????????????
            ImmutableList<SchExclude> digits = ImmutableList.copyOf(schExcludes);
            //???????????????datework??????????????????
            Function<SchExclude, String> group = new Function<SchExclude, String>() {
                @Override
                public String apply(SchExclude schExclude) {
                    return schExclude.getDateWork();
                }
            };
            //??????????????????
            groupMap = Multimaps.index(digits, group);
        }
        List<String> pkSchList = new ArrayList<>();
        // ????????????
        if ("0".equals(adjustType)) {// ??????
            for (SchAdjustDate schAdjustDate : schAdjustDates) {
                String adjustDate = schAdjustDate.getAdjustDate();
                //??????????????????????????????
                StringBuffer excludeSql = new StringBuffer();
                if (groupMap != null && groupMap.containsKey(adjustDate)) {
                    excludeSql.append("and pk_schres || '@' ||  pk_schsrv not in (");
                    List<SchExclude> list = groupMap.get(adjustDate);
                    for (int i = 0; i < list.size(); i++) {
                        SchExclude se = list.get(i);
                        if (i == 0) {
                            excludeSql.append("'").append(se.getPkSchres()).append("@").append(se.getPkSchsrv()).append("'");
                        } else {
                            excludeSql.append(" , ");
                            excludeSql.append("'").append(se.getPkSchres()).append("@").append(se.getPkSchsrv()).append("'");
                        }
                    }
                    excludeSql.append(")");
                }
                StringBuffer deleteSql = new StringBuffer("delete from sch_sch where pk_org = '" + u.getPkOrg() + "' and date_work = to_date('" + adjustDate + "','yyyy-mm-dd')");
                DataBaseHelper.execute(deleteSql.append(excludeSql).toString());
                //?????????????????????????????????
                StringBuffer sql = new StringBuffer("select * from sch_sch where pk_org = '" + u.getPkOrg() + "' and eu_schclass = '" + euSchclass + "' and del_flag = '0' and to_char(date_work,'yyyy-MM-dd') = '" + schAdjustDate.getTargetDate() + "'");
                List<SchSch> targets = DataBaseHelper.queryForList(sql.toString(), SchSch.class);
                //?????????????????????
                List<SchEmp> emps = Lists.newArrayList();
                List<SchPvtype> pvtypes = Lists.newArrayList();
                List<SchTicket> tickets = Lists.newArrayList();
                if (targets != null && targets.size() > 0) {
                    for (int i = targets.size() - 1; i >= 0; i--) {
                        SchSch schSch = targets.get(i);
                        if (groupMap != null && isExclude(schSch.getPkSchres() + "@" + schSch.getPkSchsrv(), groupMap.get(adjustDate))) {
                            targets.remove(i);
                        } else {
                            List<SchEmp> tmpEmps = DataBaseHelper.queryForList("select * from sch_emp where del_flag = '0' and pk_sch = ?", SchEmp.class, schSch.getPkSch());
                            List<SchPvtype> tmpPvtypes = DataBaseHelper.queryForList("select * from sch_pvtype where del_flag = '0' and pk_sch = ?", SchPvtype.class, schSch.getPkSch());
                            List<SchTicket> tmpTickets = DataBaseHelper.queryForList("select * from sch_ticket where del_flag = '0' and pk_sch = ?", SchTicket.class, schSch.getPkSch());
                            schSch.setPkSch(NHISUUID.getKeyId());
                            schSch.setDateWork(new DateTime(adjustDate).toDate());
                            schSch.setCreator(u.getPkEmp());
                            schSch.setModifier(u.getPkEmp());
                            schSch.setTs(new Date());
                            schSch.setCreateTime(new Date());
                            for (SchEmp emp : tmpEmps) {
                                emp.setPkSchemp(NHISUUID.getKeyId());
                                emp.setPkSch(schSch.getPkSch());
                                emp.setModifier(u.getPkEmp());
                            }
                            for (SchPvtype pvtype : tmpPvtypes) {
                                pvtype.setPkSchpvtype(NHISUUID.getKeyId());
                                pvtype.setPkSch(schSch.getPkSch());
                                pvtype.setModifier(u.getPkEmp());
                            }
                            for (SchTicket ticket : tmpTickets) {
                                ticket.setPkSchticket(NHISUUID.getKeyId());
                                ticket.setPkSch(schSch.getPkSch());
                                ticket.setModifier(u.getPkEmp());
                            }
                            emps.addAll(tmpEmps);
                            pvtypes.addAll(tmpPvtypes);
                            tickets.addAll(tmpTickets);
                        }
                    }
                    DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(SchSch.class), targets);
                    DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(SchEmp.class), emps);
                    DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(SchPvtype.class), pvtypes);
                    DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(SchTicket.class), tickets);
                    for (SchSch schSch : targets) {
                        pkSchList.add(schSch.getPkSch());
                    }
                }
            }
        } else {// ??????
            for (SchAdjustDate schAdjustDate : schAdjustDates) {
                StringBuffer sql = new StringBuffer("update sch_sch set flag_stop = '1' "
                        + "where pk_org = '" + u.getPkOrg() + "' and eu_schclass = '" + euSchclass + "' and to_char(date_work,'yyyy-MM-dd') = '" + schAdjustDate.getAdjustDate() + "'");
                StringBuffer sql1 = new StringBuffer("select pk_sch from sch_sch  "
                        + "where pk_org = '" + u.getPkOrg() + "' and eu_schclass = '" + euSchclass + "' and to_char(date_work,'yyyy-MM-dd') = '" + schAdjustDate.getAdjustDate() + "'");
                if (groupMap != null && groupMap.containsKey(schAdjustDate.getAdjustDate())) {
                    sql.append("and pk_schres || '@' ||  pk_schsrv not in (");
                    sql1.append("and pk_schres || '@' ||  pk_schsrv not in (");
                    List<SchExclude> list = groupMap.get(schAdjustDate.getAdjustDate());
                    for (int i = 0; i < list.size(); i++) {
                        SchExclude se = list.get(i);
                        if (i == 0) {
                            sql.append("'").append(se.getPkSchres()).append("@").append(se.getPkSchsrv()).append("'");
                            sql1.append("'").append(se.getPkSchres()).append("@").append(se.getPkSchsrv()).append("'");
                        } else {
                            sql.append(" , ");
                            sql1.append(" , ");
                            sql.append("'").append(se.getPkSchres()).append("@").append(se.getPkSchsrv()).append("'");
                            sql1.append("'").append(se.getPkSchres()).append("@").append(se.getPkSchsrv()).append("'");
                        }
                    }
                    sql.append(")");
                    sql1.append(")");
                }
                DataBaseHelper.execute(sql.toString());
                SchSch schSch = DataBaseHelper.queryForBean(sql1.toString(), SchSch.class);
                pkSchList.add(schSch.getPkSch());
            }
        }
        String status = "update";
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("status", status);
        paramMap.put("pkSchList", pkSchList);
        PlatFormSendUtils.sendPvOpNoMsg(paramMap);
    }

    public boolean isExclude(String key, List<SchExclude> list) {
        boolean result = false;
        if (list != null) {
            for (SchExclude schExclude : list) {
                if (key.equals(schExclude.getPkSchres() + "@" + schExclude.getPkSchsrv())) {
                    result = true;
                    break;
                }
            }
        }
        return result;
    }
    
    /**  
    * <p>Desc:??????????????????????????????(???????????????)</p>  
    * @author : wangpengyong  
    * @date   : 2021???6???18???  
    */  
    public Map<String,String>  loadCheckEuNoon(SchSch schSch,IUser user) {
    	Map<String,String> returnMap = new HashMap<String,String>();
    	returnMap.put("result", "0");//????????????,????????????
    	returnMap.put("msg", "");//??????????????????
    	Date cDate = schSch.getDateWork();//????????????
    	String resoucePK = schSch.getPkSchres();
    	String schPlanPK = schSch.getPkSchplan();
    	String pkDateslot = schSch.getPkDateslot();//??????????????????
    	String euNoon = null;//???????????????????????????????????????
    	String msg = "";
    	String pkEmpName =null;//????????????
    	String pkEmp = null;//????????????????????????
    	
    	//0. ?????????????????????????????????????????????(?????????????????????)
        Map<String, Object> stopEmpMap = DataBaseHelper.queryForMap(" SELECT pk_emp from SCH_RESOURCE where pk_schres = ? ", new Object[]{resoucePK});
        if(null != stopEmpMap && stopEmpMap.size() > 0) {
        	pkEmp = stopEmpMap.get("pkEmp").toString();
        }
    	
    	//1.??????????????????
    	Map<String,String> params = new HashMap<String,String>();
    	if(StringUtils.isNotBlank(pkDateslot)) {
    		params.put("pkDateslot",pkDateslot);
    		if(StringUtils.isNotBlank(schPlanPK)) {
        		params.put("pkSchplan", schPlanPK);
        		euNoon = schMapper.getEuNoonByPlan(params);
        	} else if (StringUtils.isNotBlank(resoucePK)) {
        		params.put("pkSchres", resoucePK);
        		euNoon = schMapper.getEuNoonByResource(params);
        	}
    	}
    	//2.?????????????????????????????????????????????????????????(?????????????????????????????????,??????????????????)
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
    	Map<String,Object> paramMap = new HashMap<String,Object>();
    	User u = (User)user;
    	paramMap.put("pkOrg", u.getPkOrg());
    	paramMap.put("pkEmpStop", pkEmp);
    	List<SchSchstop> schStopList = schPlanMapper.getSchSchstop(paramMap);
    	if(null != schStopList && schStopList.size() > 0 ) {
    		for(SchSchstop sch:schStopList) {
    			Date dateStop = sch.getDateStop();//??????????????????
    			Date dateEnd = sch.getDateEnd();//??????????????????
    			pkEmpName = sch.getNameEmpStop();//??????????????????
    			String sEuNoon = sch.getEuNoon();
    			
    			//?????????????????????????????????????????????????????????,?????????????????????
    			boolean sameEuNoon = false;
    			if (StringUtils.isBlank(sEuNoon)) {
    				sameEuNoon = true;//????????????
    			} else if(StringUtils.isNotBlank(euNoon) && StringUtils.isNotBlank(sEuNoon) && euNoon.equals(sEuNoon)) {
    				sameEuNoon = true;
    			}  
    			
    			if(null != dateStop && null != dateEnd ) {//??????/???????????????
    				if((DateUtils.compareToDay(cDate,dateStop)>=0 && DateUtils.compareToDay(cDate,dateEnd)<=0) && sameEuNoon) {
    					returnMap.put("result", "1");
    					msg = "???"+pkEmpName+"?????????,???"+sdf.format(cDate)+"???????????????????????????????????????";
            			break;
    				}
            	} else if(null != dateStop){
            		if((DateUtils.compareToDay(cDate,dateStop)>=0 ) && sameEuNoon) {//???????????????
            			returnMap.put("result", "1");
    					msg = "???"+pkEmpName+"?????????,???"+sdf.format(dateStop)+"???????????????????????????????????????";
            			break;
    				}
            	} else if(null != dateEnd){
            		if(DateUtils.compareToDay(cDate,dateEnd)<=0 && sameEuNoon) {//???????????????
            			returnMap.put("result", "1");
    					msg = "???"+pkEmpName+"?????????,???"+sdf.format(dateEnd)+"???????????????????????????????????????";
            			break;
    				}
            	} 
    		}
    	}
    	returnMap.put("msg",msg);
    	return returnMap;
    }

    /**
     * ?????????????????????<br>
     * ?????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
     *
     * @param param
     * @param user
     * @return
     */
    public String saveSchByTimeInterval(String param, IUser user) {
        SchSaveParam schVo = JsonUtil.readValue(param, new TypeReference<SchSaveParam>() {
        });
        
        SchSch schSch = schVo.getSchSch();
        Map<String,String> reMap = loadCheckEuNoon(schSch,user);
        if(reMap.get("result").equals("1")) {//????????????,????????????
        	throw new BusException(reMap.get("msg"));
        }
        
        if (schSch == null) {
            throw new BusException("?????????????????????");
        }
        if (CollectionUtils.isEmpty(schVo.getListPlanWeekDt())) {
            throw new BusException("???????????????????????????");
        }
        String type = "add";
        String tickedRules = null;
        User u = (User) user;
        List<SchPlanWeekDt> listAddToTicket = null;//?????????????????????????????????|??????????????????????????????????????????????????????????????????
        boolean genTicket = true;
        Map<String, Object> map = DataBaseHelper.queryForMap("select pk_orgarea from bd_ou_dept dept INNER JOIN SCH_RESOURCE res on DEPT.PK_DEPT = RES.PK_DEPT_BELONG WHERE RES.PK_SCHRES=? ", new Object[]{schSch.getPkSchres()});
        String pkOrgarea = MapUtils.getString(map, "pkOrgarea");
        if (StringUtils.isNotBlank(schSch.getPkSch())) {
            Map<String, Object> mapSch = DataBaseHelper.queryForMap("SELECT cnt_used,cnt_total FROM SCH_SCH WHERE CNT_USED>0 AND PK_SCH=?",
                    schSch.getPkSch());
            //??????????????????????????????
            genTicket = MapUtils.getIntValue(mapSch, "cntUsed") == 0;
            if (MapUtils.isNotEmpty(mapSch)
                    && MapUtils.getIntValue(mapSch, "cntUsed") > 0) {
                listAddToTicket = compareSchPlanWeekDt(schSch.getPkSch(), schVo.getListPlanWeekDt(), schVo.getNotAutoModifyOfTotalCAndBookCFlag());
            }  
            ApplicationUtils.setDefaultValue(schSch, false);
            DataBaseHelper.updateBeanByPk(schSch, false);
            type = "update";
        } else {
            //???????????????
            if (StringUtils.isNotBlank(schSch.getPkSchplan())) {
                SchPlan plan = DataBaseHelper.queryForBean("select * from sch_plan where pk_schplan=?",
                        SchPlan.class, schSch.getPkSchplan());
                if (plan == null) {
                    throw new BusException("?????????????????????????????????????????????");
                }
                tickedRules = plan != null ? plan.getPkTicketrules() : null;
                SchPlanWeek planWeek = DataBaseHelper.queryForBean("select * from sch_plan_week where pk_schplan=? and pk_dateslot=? and week_no=?",
                        SchPlanWeek.class, new Object[]{schSch.getPkSchplan(), schSch.getPkDateslot(), DateUtils.getDayNumOfWeek(schSch.getDateWork())});
                if (planWeek == null) {
                    throw new BusException("??????????????????????????????????????????");
                }
                schSch.setPkPlanweek(planWeek.getPkPlanweek());
                schSch.setPkSchsrv(plan.getPkSchsrv());
                schSch.setMinutePer(plan.getMinutePer().intValue());
                schSch.setPkDept(plan.getPkDept());
                schSch.setPkSchres(plan.getPkSchres());
            } else if (StringUtils.isNotBlank(schSch.getPkSchres())) {
                //???????????????
                SchResource schResource = DataBaseHelper.queryForBean("select * from sch_resource where del_flag = '0' and pk_schres = ? ", SchResource.class, schSch.getPkSchres());
                if (schResource == null) {
                    throw new BusException("?????????????????????????????????????????????");
                }
                if (StringUtils.isBlank(tickedRules)) {
                    tickedRules = schResource != null ? schResource.getPkTicketrules() : null;
                }
                schSch.setPkSchsrv(StringUtils.isBlank(schSch.getPkSchsrv()) ? schResource.getPkSchsrv() : schSch.getPkSchsrv());
                schSch.setMinutePer(schResource.getMinutePer().intValue());
                schSch.setPkDept(schResource.getPkDeptBelong());
            }
            //?????????+???????????????+?????????????????????????????????????????? ?????????BD_CODE_DATESLOT??????EU_NOON???????????????
            if (DataBaseHelper.queryForScalar("select count(*) from SCH_SCH where del_flag = '0' and PK_SCHRES=? and to_char(DATE_WORK,'yyyyMMdd')=? and cnt_total>=0 and (PK_DATESLOT=?  or "
            		+ "exists(select 1 from BD_CODE_DATESLOT lot where lot.EU_NOON in (select EU_NOON from BD_CODE_DATESLOT where PK_DATESLOT = ? and EU_NOON is not null) and lot.PK_DATESLOT =SCH_SCH.PK_DATESLOT)) ",
                    Integer.class, new Object[]{schSch.getPkSchres(), DateUtils.formatDate(schSch.getDateWork(), "yyyyMMdd"), schSch.getPkDateslot(), schSch.getPkDateslot()}) > 0) {
                throw new BusException("?????????????????????????????????????????????????????????????????????????????????????????????");
            }
            
//			String pkOrgarea = handlerService.getCurrentOrgAreaPk(u);
            schSch.setEuStatus(StringUtils.isBlank(schSch.getEuStatus()) ? SchEuStatus.INIT.getStatus() : schSch.getEuStatus());
            schSch.setPkOrgarea(pkOrgarea);
            schSch.setPkEmpSch(u.getPkEmp());
            schSch.setNameEmpSch(u.getNameEmp());
            schSch.setDateSch(schSch.getDateSch() == null ? (new Date()) : schSch.getDateSch());
            schSch.setDateWork(schSch.getDateWork());
            schSch.setFlagStop("0");
            ApplicationUtils.setDefaultValue(schSch, true);
            DataBaseHelper.insertBean(schSch);
        }
        //????????????????????????????????????????????????????????????????????????????????????????????????"1".equals(schPlan.getFlagTicket())??????????????????????????????????????????????????????
        if (genTicket) {
            genTicketByTimePeriod(schSch, tickedRules, schVo.getListPlanWeekDt());
        } else {
            //?????????????????????1.??????????????? 2????????????????????????N?????????????????????
            //????????????????????????????????????????????????????????????KFC????????????????????????????????????????????????????????????????????????????????????????????????????????????;
            if (CollectionUtils.isNotEmpty(listAddToTicket)) {
                int maxTicketNo = DataBaseHelper.queryForScalar("select max(cast((case when TICKETNO is null then '0' else TICKETNO end )as int)) from sch_ticket where pk_sch=?",
                        Integer.class, new Object[]{schSch.getPkSch()});
                insertTicketsByPlanweekDt(schSch, null, listAddToTicket, maxTicketNo + 1);//????????????????????????????????????????????????????????????
                for (SchPlanWeekDt dt : listAddToTicket) {
                    if (Math.abs(dt.getCntAppt()) < 1)
                        continue;
                    StringBuilder sql = new StringBuilder();
                    sql.append("update SCH_TICKET set FLAG_APPT=1 where PK_SCHTICKET in(")
                            .append("select pk_schticket from(select pk_schticket from sch_ticket tk where tk.PK_SCH=? ");
                    if (!StringUtils.isEmpty(dt.getDtApptype())) {
                        sql.append(" and nvl(tk.dt_Apptype,'')=? ");
                    }
                    sql.append("and to_char(tk.begin_time,'HH24:mi:ss')=? order by case  when (tk.ticketno is null or tk.ticketno='') then  0 else cast(tk.ticketno as int) end) t where rownum<=?")
                            .append(")");
                    if (!StringUtils.isEmpty(dt.getDtApptype())) {
                        DataBaseHelper.update(sql.toString(), new Object[]{schSch.getPkSch(), null == dt.getDtApptype() ? "" : dt.getDtApptype()
                                , dt.getTimeBegin(), Math.abs(dt.getCntAppt())});
                    } else {
                        DataBaseHelper.update(sql.toString(), new Object[]{schSch.getPkSch(), dt.getTimeBegin(), Math.abs(dt.getCntAppt())});
                    }
                }
            }
        }
        sendSchMsg(type, schSch.getPkSch());
        Map<String, Object> platParam = new HashMap<String, Object>();
        List<String> pkSchs = new ArrayList<String>();
        pkSchs.add(schSch.getPkSch());
        platParam.put("pkSchs", pkSchs);
        platParam.put("operation", type);
        PlatFormSendUtils.sendSchInfo(platParam);
        return schSch.getPkSch();
    }

    /**
     * ?????????????????????????????????????????????????????????<br>
     *
     * @param pkSch
     * @param list  ?????????????????????
     * @param notAutoModifyOfTotalCAndBookCFlag  
     * @return
     */
    private List<SchPlanWeekDt> compareSchPlanWeekDt(String pkSch, List<SchPlanWeekDtVo> list, String notAutoModifyOfTotalCAndBookCFlag) {
        List<SchPlanWeekDt> result = null;
        if (StringUtils.isNotBlank(pkSch) && CollectionUtils.isNotEmpty(list)) {
            Map<String, Map<String, Object>> mapDb = getTicketCntOfPeriod(pkSch);
            if (MapUtils.isNotEmpty(mapDb)) {
                result = new ArrayList<>();
                for (SchPlanWeekDtVo dt : list) {
                    if (null == dt.getOldDtapptype()) {
                        dt.setOldDtapptype("");
                    }
                    Map<String, Object> old = MapUtils.getMap(mapDb, dt.getTimeBegin() + dt.getTimeEnd() + dt.getOldDtapptype());
                    //AddOrUpd?????????????????????add????????????????????????????????????
                    if (StringUtils.isEmpty(dt.getAddOrUpd()) || !"add".equals(dt.getAddOrUpd())) {
                        if (MapUtils.isNotEmpty(old)) {
                            //???????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
                            if (!StringUtils.isEmpty(dt.getDtApptype())) {
                                if (!dt.getDtApptype().equals(dt.getOldDtapptype())) {
                                    updateTicketDtType(pkSch, dt);
                                }
                            }
                            //????????????????????????
                            int oldCnt = MapUtils.getIntValue(old, "cnt", 0);
                            int oldCntAppt = MapUtils.getIntValue(old, "cntAppt", 0);
                            if ((dt.getCnt() != null && dt.getCnt().intValue() < oldCnt)
                                    || (dt.getCntAppt() != null && dt.getCntAppt().intValue() < oldCntAppt)) {
                                throw new BusException(String.format("???????????????????????????????????????????????????????????????%s??????????????????%s,?????????%s",
                                        dt.getTimeBegin() + "-" + dt.getTimeEnd(), oldCnt, oldCntAppt));
                            }
                            SchPlanWeekDt dtAdd = new SchPlanWeekDt();
                            dtAdd.setCnt(dt.getCnt().intValue() - oldCnt);
                            if("1".equals(notAutoModifyOfTotalCAndBookCFlag))
                            {
                            	dtAdd.setCntAppt(dt.getCntAppt().intValue() - oldCntAppt);
                            }
                            else
                            {
                                dtAdd.setCntAppt(dt.getCntAppt().intValue());//dtAdd.setCntAppt(-dt.getCntAppt().intValue());//??????????????????????????????????????????bug-33938???                            	
                            }
                            dtAdd.setTimeBegin(dt.getTimeBegin());
                            dtAdd.setTimeEnd(dt.getTimeEnd());
                            dtAdd.setDtApptype(dt.getDtApptype());
                            if (dtAdd.getCnt().intValue() == 0
                                    && Integer.compare(dt.getCntAppt().intValue(), oldCntAppt) == 0)
                                continue;
                            result.add(dtAdd);
                        } else {
                            //dt.setCntAppt(-dt.getCntAppt().intValue());//??????????????????????????????????????????bug-33938???
                            result.add(dt);
                        }
                    } else {
                        //dt.setCntAppt(-dt.getCntAppt().intValue());//??????????????????????????????????????????bug-33938???
                        result.add(dt);
                    }
                }
            }
        }
        return result;
    }

    /**
     * ??????????????????
     */
    public void updateTicketDtType(String pkSch, SchPlanWeekDtVo dt) {
        if (!StringUtils.isEmpty(dt.getDtApptype())) {
            if (!dt.getDtApptype().equals(dt.getOldDtapptype())) {
                StringBuilder sql = new StringBuilder();
                sql.append("update SCH_TICKET set DT_APPTYPE=? where PK_SCHTICKET in(")
                        .append("select pk_schticket from(select pk_schticket from sch_ticket tk where tk.PK_SCH=? ");
                if (!StringUtils.isEmpty(dt.getOldDtapptype())) {
                    sql.append(" and nvl(tk.dt_Apptype,'')=? ");
                }
                sql.append("and to_char(tk.begin_time,'HH24:mi:ss')=? order by case  when (tk.ticketno is null or tk.ticketno='') then  0 else cast(tk.ticketno as int) end) t where rownum<=?")
                        .append(")");
                if (!StringUtils.isEmpty(dt.getOldDtapptype())) {
                    DataBaseHelper.update(sql.toString(), new Object[]{dt.getDtApptype(), pkSch, null == dt.getOldDtapptype() ? "" : dt.getOldDtapptype()
                            , dt.getTimeBegin(), Math.abs(dt.getCntAppt())});
                } else {
                    DataBaseHelper.update(sql.toString(), new Object[]{dt.getDtApptype(), pkSch, dt.getTimeBegin(), Math.abs(dt.getCntAppt())});
                }
            }
        }
    }

    public Map<String, Map<String, Object>> getTicketCntOfPeriod(String pkSch) {
        Map<String, Map<String, Object>> mapDb = DataBaseHelper.queryListToMap(
                "select to_char(begin_time,'HH24:mi:ss')||to_char(END_TIME,'HH24:mi:ss')||DT_APPTYPE as key_,count(*) as cnt,"
                        + "sum((case when t.FLAG_APPT=1 then 1 else 0 end)) as cnt_appt,DT_APPTYPE from  SCH_TICKET t where t.PK_SCH=? group by begin_time,end_time,DT_APPTYPE",
                new Object[]{pkSch});
        return mapDb;
    }

    /**
     * ?????????????????????????????????????????????
     *
     * @param param
     * @param user
     * @return
     */
    public Map<String, Object> getSchAdnTimePeriod(String param, IUser user) {
        String pkSch = JSON.parseObject(param).getString("pkSch");
        SchSchVo schVo = DataBaseHelper.queryForBean("select sch.*,(case when sch.PK_SCHPLAN is null then res.PK_TICKETRULES else pl.PK_TICKETRULES end )PK_TICKETRULES" +
                " ,(case when sch.PK_DATESLOT is null then (case when sch.PK_SCHPLAN is null then res.DT_DATESLOTTYPE else pl.DT_DATESLOTTYPE end) else st.DT_DATESLOTTYPE end)DT_DATESLOTTYPE "
                + " from sch_sch sch left join SCH_PLAN pl on sch.pk_schplan=pl.pk_schplan left join SCH_RESOURCE res on sch.PK_SCHRES=res.PK_SCHRES" +
                " left join BD_CODE_DATESLOT st on sch.PK_DATESLOT=st.PK_DATESLOT where sch.pk_sch=?", SchSchVo.class, pkSch);
        Map<String, Object> result = new HashMap<String, Object>();
        Map<String, String> params = new HashMap<String, String>();
        params.put("pkDateslot", schVo.getPkDateslot());
        params.put("pkSchres", schVo.getPkSchres());
        /***
         * ?????????????????????????????????????????????,??????????????????,????????????????????????????????????????????????????????? ?????????
         * ??????????????????????????????????????????????????????????????????????????????
         */
        List<SchPlanWeekDtVo> listPlanWeekDt = null;
        //??????????????????????????????????????????????????????????????????????????????????????????
        StringBuilder sql = new StringBuilder("SELECT time_begin,time_end,max(cnt) cnt,max(cnt_appt) cnt_appt,DT_APPTYPE,old_DTAPPTYPE,(case when sum(tkused)>0 then '1' else '0' end ) flag_used from (");
        sql.append("select (case when tk.time_begin is null THEN sec.time_begin else tk.time_begin end) time_begin,");
        sql.append("(case when tk.time_end is null THEN sec.time_end else tk.time_end end) time_end,");
        sql.append("(case when tk.cnt is null THEN 0 ELSE tk.cnt END) as cnt,(case when tk.cnt_appt is null THEN 0 ELSE tk.cnt_appt END) as cnt_appt,tk.DT_APPTYPE,tk.DT_APPTYPE old_DTAPPTYPE,");
        if (Application.isSqlServer()) {
            sql.append("isnull(tk.tkused,0) tkused ");
        } else {
            sql.append("nvl(tk.tkused,0) tkused ");
        }
        sql.append(" from bd_code_dateslot_sec sec ");
        sql.append("LEFT JOIN sch_sch sch on sec.pk_dateslot = SCH.pk_dateslot and sch.pk_sch =? LEFT JOIN ");
        sql.append("(select to_char(begin_time, 'hh24:mi:ss') time_begin,to_char(end_time, 'hh24:mi:ss') time_end,count(*) cnt,sum(case when flag_appt=1 then 1 else 0 end) cnt_appt,pk_sch,DT_APPTYPE,sum(case when FLAG_USED=1 then 1 else 0 end) tkused from sch_ticket ");
        sql.append(" where pk_sch=? group by begin_time,end_time,pk_sch,DT_APPTYPE) tk ON sch.pk_sch = tk.pk_sch where SEC.pk_dateslot=?) tb ");
        sql.append(" group by time_begin,time_end,DT_APPTYPE,tb.old_DTAPPTYPE ORDER BY time_begin");
        listPlanWeekDt = DataBaseHelper.queryForList(sql.toString(), SchPlanWeekDtVo.class, new Object[]{pkSch, pkSch, schVo.getPkDateslot()});
        Map<String, Object> mapEuresType = DataBaseHelper.queryForMap("select b.EU_RESTYPE from sch_sch a inner join SCH_RESOURCE b on a.PK_SCHRES = b.PK_SCHRES where a.PK_SCH=?", pkSch);
        result.put("cntStd", codeMapper.findCntStd(params));
        result.put("schSch", schVo);
        result.put("listPlanWeekDt", listPlanWeekDt);
        result.put("euRestype", MapUtils.getString(mapEuresType, "euRestype"));
        result.put("cntStd", codeMapper.findCntStd(params));
        return result;
    }

    /**
     * ??????,???????????????
     *
     * @param param
     * @param user
     */
    public void saveStopOrRecoverSch(String param, IUser user) {
        Map<String, Object> mapParam = JsonUtil.readValue(param, new TypeReference<Map<String, Object>>() {
        });
        String pkSch = MapUtils.getString(mapParam, "pkSch");
        String flagStop = MapUtils.getString(mapParam, "flagStop");
        if (StringUtils.isBlank(pkSch) && StringUtils.isBlank(flagStop)) {
            throw new BusException("??????????????????????????????");
        }
        if (!"0".equals(flagStop) && !"1".equals(flagStop)) {
            throw new BusException("?????????????????????0?????????/1?????????");
        }
        
        stopRecoverSch(pkSch,flagStop,user);
    }
    
    
    /**  
    * <p>Desc:  ????????????????????????(????????????????????????????????????)</p> 
    * @param  : pkSch:???????????????flagStop???0?????????/1?????????
    * @author : wangpengyong  
    * @date   : 2021???7???21???  
    */  
    public void stopRecoverSch(String pkSch, String flagStop,IUser user) {
    	User u = (User) user;
        List<String> ticketNos = null;
        int upCount = 0;
        String schSqlPart = "";
        if (flagStop.equals("1")) {
            ticketNos = DataBaseHelper.getJdbcTemplate().queryForList("select ticket_no from SCH_APPT where EU_STATUS < '1' and PK_SCH=?", String.class, pkSch);
            if (ticketNos != null && ticketNos.size() > 0) {
                upCount = DataBaseHelper.update("update SCH_APPT set FLAG_CANCEL='1',EU_STATUS='9',date_cancel=?,pk_emp_cancel=?,name_emp_cancel=? where EU_STATUS < '1' and PK_SCH=?",
                        new Object[]{new Date(), UserContext.getUser().getPkEmp(), UserContext.getUser().getNameEmp(), pkSch});

                DataBaseHelper.update("update sch_ticket set flag_used='0',ts=?,modifier=?,modity_time=? where PK_SCH=? and ticketno in (" + CommonUtils.convertListToSqlInPart(ticketNos) + ")",
                        new Object[]{new Date(), u.getPkEmp(), new Date(),pkSch});

                schSqlPart = ",cnt_used=cnt_used-" + upCount + " ";
            }
        }
        DataBaseHelper.update("update sch_sch set FLAG_STOP = ?,ts=?,modifier=?,modity_time=?" + schSqlPart + " where PK_SCH=?",
                new Object[]{flagStop,new Date(), u.getPkEmp(), new Date(), pkSch});
        DataBaseHelper.update("update sch_pvtype set ts=?,modifier=?,modity_time=?,FLAG_STOP = ?" + schSqlPart + " where PK_SCH=?", new Object[]{new Date(), u.getPkEmp(), new Date(),flagStop, pkSch});
        DataBaseHelper.update("update sch_ticket set ts=?,modifier=?,modity_time=?,FLAG_STOP = ? where PK_SCH=?", new Object[]{new Date(), u.getPkEmp(), new Date(),flagStop, pkSch});
        //????????????????????????
        SchModlog schModlog = new SchModlog();
        schModlog.setPkSchlog(NHISUUID.getKeyId());
        schModlog.setPkSch(pkSch);
        schModlog.setEuType("99");//??????
        schModlog.setReasons("0".equals(flagStop)?"??????":"??????");
        handlerService.saveSchModLogBatch(Arrays.asList(schModlog));
        Map<String, Object> platParam = new HashMap<String, Object>();
        List<String> pkSchs = new ArrayList<String>();
        pkSchs.add(pkSch);
        platParam.put("pkSchs", pkSchs);
        platParam.put("isSendShortMsg", "1");//??????????????????????????????-????????????
        platParam.put("isWeChat", "1");//??????????????????????????????-????????????
        PlatFormSendUtils.sendSchInfo(platParam);
    }
    
    

    /**
     * ????????????????????????<br>
     * ??????????????????????????????????????????N??????????????????
     *
     * @param schSch
     * @param schTickets
     * @param listPlanweekDt
     */
    private void insertTicketsByPlanweekDt(SchSch schSch, List<SchTicket> schTickets, List<SchPlanWeekDt> listPlanweekDt) {
        insertTicketsByPlanweekDt(schSch, schTickets, listPlanweekDt, 1);
    }

    /**
     * ????????????????????????<br>
     * ??????????????????????????????????????????N??????????????????
     *
     * @param schSch
     * @param schTickets
     * @param listPlanweekDt
     */
    private void insertTicketsByPlanweekDtVo(SchSch schSch, List<SchTicket> schTickets, List<SchPlanWeekDtVo> listPlanweekDt) {
        List<SchPlanWeekDt> listPlanweekDts = new ArrayList<SchPlanWeekDt>();
        for (SchPlanWeekDtVo schPlanWeekDtVo : listPlanweekDt) {
            SchPlanWeekDt schPlanWeekDt = new SchPlanWeekDt();
            ApplicationUtils.copyProperties(schPlanWeekDt, schPlanWeekDtVo);
            listPlanweekDts.add(schPlanWeekDt);
        }
        insertTicketsByPlanweekDt(schSch, schTickets, listPlanweekDts, 1);
    }

    private void insertTicketsByPlanweekDt(SchSch schSch, List<SchTicket> schTickets, List<SchPlanWeekDt> listPlanweekDt, int start) {
        if (CollectionUtils.isNotEmpty(listPlanweekDt)) {
            start = start < 1 ? 1 : start;
            DateTimeFormatter dtf = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
            DateTime dt = new DateTime(schSch.getDateWork());
            String yeardate = dt.toString("yyyy-MM-dd");
            Map<String, String> mapTicket = new HashMap<>();
            if (CollectionUtils.isNotEmpty(schTickets)) {
                for (SchTicket ticket : schTickets) {
                    mapTicket.put(ticket.getTicketno(), ticket.getFlagUsed());
                }
            }
            int ticketno = start;
            for (int i = 0; i < listPlanweekDt.size(); i++) {
                SchPlanWeekDt weekDt = listPlanweekDt.get(i);
                if (StringUtils.isBlank(weekDt.getTimeBegin())
                        || StringUtils.isBlank(weekDt.getTimeEnd())) {
                    throw new BusException("???????????????????????????????????????????????????");
                }
                DateTime begin = DateTime.parse(yeardate + " " + weekDt.getTimeBegin(), dtf);
                DateTime end = DateTime.parse(yeardate + " " + weekDt.getTimeEnd(), dtf);
                List<SchTicket> listTicket = new ArrayList<>();
                int cntAppt = weekDt.getCntAppt();
                for (int j = 1; j <= weekDt.getCnt(); j++) {
                    SchTicket schTicket = new SchTicket();
                    schTicket.setPkSch(schSch.getPkSch());
                    schTicket.setFlagAppt((cntAppt--) > 0 ? "1" : "0");
                    if (mapTicket.containsKey(ticketno)) {
                        schTicket.setFlagUsed(mapTicket.get(ticketno));
                    } else {
                        schTicket.setFlagUsed("0");
                    }
                    schTicket.setFlagStop("0");
                    schTicket.setBeginTime(begin.toDate());
                    schTicket.setEndTime(end.toDate());
                    schTicket.setTicketno(String.valueOf(ticketno++));
                    schTicket.setDtApptype(weekDt.getDtApptype());
                    ApplicationUtils.setDefaultValue(schTicket, true);
                    listTicket.add(schTicket);
                    mapTicket.remove(String.valueOf(ticketno));
                }
                if (CollectionUtils.isNotEmpty(listTicket))
                    DataBaseHelper.batchUpdate(BuildSql.buildInsertSqlWithClass(SchTicket.class), listTicket);
            }

            upSugTimeInSchTicket(schSch);
        }
    }

    public void upSugTimeInSchTicket(SchSch sch) {

        List<SchTicket> listTicket = DataBaseHelper.queryForList("select * from sch_ticket where pk_sch=? order by begin_time,FLAG_USED desc,ticketno", SchTicket.class, new Object[]{sch.getPkSch()});

        /** list??????,sug_time?????? */
        // ??????begin_time ???list????????????
        ImmutableList<SchTicket> digits = ImmutableList.copyOf(listTicket);
        // ???????????????begin_time??????????????????  key:beginTime-endTime-(endTime???beginTime)
        Function<SchTicket, String> group = ticket ->
                DateUtils.dateToStr("yyyyMMddHHmmss", ticket.getBeginTime()) + "-"
                        + DateUtils.dateToStr("yyyyMMddHHmmss", ticket.getEndTime()) + "-"
                        + DateUtils.getMinsBetween(ticket.getBeginTime(), ticket.getEndTime());
        // ??????????????????
        ImmutableListMultimap<String, SchTicket> groupMap = Multimaps.index(digits, group);
        List<SchTicket> upSchList = Lists.newArrayList();
        for (String key : groupMap.keySet()) {
            List<SchTicket> schTickets = groupMap.get(key);
            String[] arr = key.split("-");
            DateTime beginTime = new DateTime(DateUtils.strToDate(arr[0], "yyyyMMddHHmmss").getTime());
            DateTime endTime = new DateTime(DateUtils.strToDate(arr[1], "yyyyMMddHHmmss").getTime());
            Integer specTime = Integer.parseInt(arr[2]);
            int count = 0;
            if (specTime / schTickets.size() <= 1) {
                for (SchTicket ticket : schTickets) {
                    if (count >= specTime) {
                        ticket.setSugTime(endTime.plusMinutes(-1).toDate());
                    } else {
                        ticket.setSugTime(beginTime.plusMinutes(count).toDate());
                        count++;
                    }
                    upSchList.add(ticket);
                }
            }else {
                for (SchTicket ticket : schTickets) {
                    ticket.setSugTime(beginTime.plusMinutes(count).toDate());
                    count += specTime / schTickets.size();
                    upSchList.add(ticket);
                }
            }
        }
        DataBaseHelper.batchUpdate(DataBaseHelper.getUpdateSql(SchTicket.class), upSchList);
    }

    private void sendSchMsg(String status, String pkSch) {
        Map<String, Object> map = new HashMap<String, Object>();
        List<String> pkSchList = new ArrayList<>();
        pkSchList.add(pkSch);
        map.put("status", status);
        map.put("pkSchList", pkSchList);
        PlatFormSendUtils.sendPvOpNoMsg(map);
    }

    /**
     * ??????????????????[{"pkSch":"","pkSchticket":"","flagStop":""}]
     *
     * @param param
     * @param user
     */
    public void modTicketFlagStop(String param, IUser user) {
        List<SchTicket> list = JsonUtil.readValue(param, new TypeReference<List<SchTicket>>() {
        });
        if (CollectionUtils.isEmpty(list)) {
            throw new BusException("???????????????");
        }
        Set<String> setPkSch = new HashSet<>();
        for (SchTicket ticket : list) {
            if (ticket == null) {
                throw new BusException("?????????????????????");
            }
            if (StringUtils.isAnyBlank(ticket.getPkSchticket(), ticket.getFlagStop(), ticket.getPkSch())) {
                throw new BusException("???????????????pkSchticket??????flagStop??????pkSch");
            }
            if (StringUtils.indexOfAny(ticket.getFlagStop(), EnumerateParameter.ONE, EnumerateParameter.ZERO) < 0) {
                throw new BusException("?????????flagStop????????????");
            }
            setPkSch.add(ticket.getPkSch());
        }
        String now = "sysdate";
        if (Application.isSqlServer()) {
            now = "getdate()";
        }
        List<Map<String, Object>> mod = setPkSch.parallelStream().map(s -> new HashMap<String, Object>() {{
            put("pkSch", s);
            put("flagStop","");
        }}).collect(Collectors.toList());
        //?????????????????????????????????????????????
        DataBaseHelper.batchUpdate("update sch_ticket set flag_stop=:flagStop,FLAG_APPT=:flagAppt where PK_SCHTICKET=:pkSchticket and FLAG_USED=0 and to_char(BEGIN_TIME,'yyyMMdd')>=to_char(" + now + ",'yyyMMdd')", list);
        //  ?????????+??????????????????1=?????????
        DataBaseHelper.batchUpdate("update SCH_SCH set CNT_USED = (select count(1) from SCH_TICKET tk where tk.PK_SCH=:pkSch and (tk.FLAG_USED='1' or tk.FLAG_STOP='1')) " +
                "" + ",cnt_appt=(select count(1) from SCH_TICKET tk where tk.PK_SCH=:pkSch and ( tk.FLAG_STOP='0' and flag_appt='1'))"+
                "where PK_SCH=:pkSch", mod);


        //??????????????????~~?????????????????????????????????????????????????????????????????????
        //??????????????????
        Map<String, Object> paramMap = new HashMap<>(16);
        paramMap.put("pkSchs", new ArrayList<>(setPkSch));
        PlatFormSendUtils.sendSchInfo(paramMap);
    }

    /**
     * ????????????????????????
     *
     * @param param
     * @param user
     * @return
     */
    public void saveSchModLog(String param, IUser user) {
        SchModlog schModlog = JsonUtil.readValue(param, new TypeReference<SchModlog>() {
        });
        Map<String, Object> pam = JsonUtil.readValue(param, Map.class);
        if (schModlog == null) {
            throw new BusException("??????????????????");
        }
        if (StringUtils.isEmpty(schModlog.getPkSch())) {
            throw new BusException("????????????????????????");
        }

        //???????????????????????????????????????
        if (MapUtils.getString(pam, "flagStop", "").equals("1")) {
            DataBaseHelper.update("update sch_appt set note=? where pk_sch=? and note is null ", new Object[]{schModlog.getReasons(), schModlog.getPkSch()});
        }
        ApplicationUtils.setDefaultValue(schModlog, true);
        DataBaseHelper.insertBean(schModlog);
    }

    /**
     * ????????????????????????????????????????????????????????????
     *
     * 009002002028
     * @param param
     * @param user
     */
    public void delSchDetail(String param, IUser user) {
        SchPlanWeekDtVo schPlanWeekDtVo = JsonUtil.readValue(param, SchPlanWeekDtVo.class);

        SchSch schSch = DataBaseHelper.queryForBean("select * from sch_sch where pk_sch = ?", SchSch.class, schPlanWeekDtVo.getPkSch());

        if (schSch != null) {
            //??????????????????????????????????????????
            if (schSch.getPkPlanweek() != null) {

                SchPlanWeekDt schPlanWeekDt = DataBaseHelper.queryForBean("select * from SCH_PLANWEEK_DT where TIME_BEGIN = ? and TIME_END=? and DT_APPTYPE=? and PK_PLANWEEK=?", SchPlanWeekDt.class,
                        new Object[]{schPlanWeekDtVo.getTimeBegin(), schPlanWeekDtVo.getTimeEnd(), schPlanWeekDtVo.getDtApptype(), schSch.getPkPlanweek()});

                if (schPlanWeekDt != null) {
                    SchPlanWeek schPlanWeek = DataBaseHelper.queryForBean("select * from SCH_PLAN_WEEK where PK_PLANWEEK = ?", SchPlanWeek.class, schPlanWeekDt.getPkPlanweek());
                    schPlanWeekDtVo.setPkPlanweek(schPlanWeek.getPkPlanweek());

                    int execute = DataBaseHelper.execute("delete from SCH_TICKET where PK_SCH=? and DT_APPTYPE=? and BEGIN_TIME=to_date(?,'yyyymmddhh24:mi:ss') and END_TIME=to_date(?,'yyyymmddhh24:mi:ss') and flag_used='0' ",
                            new Object[]{schSch.getPkSch(), schPlanWeekDt.getDtApptype(),
                                    DateUtils.dateToStr("yyyyMMdd", schSch.getDateWork()) + schPlanWeekDt.getTimeBegin(),
                                    DateUtils.dateToStr("yyyyMMdd", schSch.getDateWork()) + schPlanWeekDt.getTimeEnd()});

                    if (execute != schPlanWeekDt.getCnt()) {
                        throw new BusException("?????????????????????????????????????????????");
                    }
                    DataBaseHelper.execute("delete from SCH_PLANWEEK_DT where PK_PLANWEEKDT=?", schPlanWeekDt.getPkPlanweekdt());
                    DataBaseHelper.update("update sch_plan_week set cnt_total=cnt_total-?,cnt_appt=cnt_appt-? where pk_planweek=?", new Object[]{execute, schPlanWeekDt.getCntAppt(), schPlanWeek.getPkPlanweek()});
                    DataBaseHelper.update("update SCH_SCH set cnt_total=cnt_total-?,cnt_appt=cnt_appt-? where PK_SCH=?", new Object[]{execute, schPlanWeekDt.getCntAppt(), schSch.getPkSch()});
                } else {//????????????????????????????????????????????????????????????????????????????????????????????????????????????
                    delDaySch(schSch,schPlanWeekDtVo);
                }
            }else {//?????????????????????????????????
                delDaySch(schSch,schPlanWeekDtVo);
            }
        }
    }

    /**
     * ???????????????????????????????????????
     *
     * @param schSch
     * @param schPlanWeekDtVo
     */
    private void delDaySch(SchSch schSch, SchPlanWeekDtVo schPlanWeekDtVo) {

        List<String> pkSchTickets = DataBaseHelper.getJdbcTemplate().queryForList("select PK_SCHTICKET from SCH_TICKET where DT_APPTYPE=? and PK_SCH=? and flag_used='0' and BEGIN_TIME=to_date(?,'yyyymmddhh24:mi:ss') and END_TIME=to_date(?,'yyyymmddhh24:mi:ss')", String.class,
                new Object[]{schPlanWeekDtVo.getDtApptype(), schSch.getPkSch(),
                        DateUtils.dateToStr("yyyyMMdd", schSch.getDateWork()) + schPlanWeekDtVo.getTimeBegin(),
                        DateUtils.dateToStr("yyyyMMdd", schSch.getDateWork()) + schPlanWeekDtVo.getTimeEnd()});

        int execute = DataBaseHelper.execute("delete from sch_ticket where PK_SCHTICKET in (" + CommonUtils.convertListToSqlInPart(pkSchTickets) + ") and flag_used='0' ");
        if (execute != schPlanWeekDtVo.getCnt()) {
            throw new BusException("?????????????????????????????????????????????");
        }

        DataBaseHelper.update("update sch_plan_week set cnt_total=cnt_total-?,cnt_appt=cnt_appt-? where pk_planweek=?", new Object[]{execute, schPlanWeekDtVo.getCntAppt(), schPlanWeekDtVo.getPkPlanweek()});
        DataBaseHelper.update("update SCH_SCH set cnt_total=cnt_total-?,cnt_appt=cnt_appt-? where PK_SCH=?", new Object[]{execute, schPlanWeekDtVo.getCntAppt(), schSch.getPkSch()});
    }

    
}
