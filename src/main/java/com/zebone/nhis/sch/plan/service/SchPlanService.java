package com.zebone.nhis.sch.plan.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.FastDateFormat;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.apache.commons.collections.CollectionUtils;
import com.alibaba.fastjson.JSON;
import com.google.common.base.Function;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import com.google.common.collect.Ordering;
import com.zebone.nhis.common.dao.BaseCodeMapper;
import com.zebone.nhis.common.module.base.bd.code.BdCodeDateslot;
import com.zebone.nhis.common.module.base.ou.BdOuDept;
import com.zebone.nhis.common.module.sch.plan.SchPlan;
import com.zebone.nhis.common.module.sch.plan.SchPlanEmp;
import com.zebone.nhis.common.module.sch.plan.SchPlanWeek;
import com.zebone.nhis.common.module.sch.plan.SchPlanWeekDt;
import com.zebone.nhis.common.module.sch.plan.SchPlanweekPvtype;
import com.zebone.nhis.common.module.sch.plan.SchSchstop;
import com.zebone.nhis.common.module.sch.pub.SchTicketrules;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.common.support.EnumerateParameter;
import com.zebone.nhis.sch.plan.dao.SchMapper;
import com.zebone.nhis.sch.plan.dao.SchPlanMapper;
import com.zebone.nhis.sch.plan.vo.SchPlanWeekDtVo;
import com.zebone.nhis.sch.pub.dao.SchBaseMapper;
import com.zebone.nhis.sch.pub.service.SchHandlerService;
import com.zebone.nhis.sch.pub.support.TypeUtils;
import com.zebone.nhis.sch.pub.vo.SchPlanAndEmpsWeeksParam;
import com.zebone.nhis.sch.pub.vo.SchPlanVo;
import com.zebone.nhis.sch.pub.vo.SchPlanWeekExt;
import com.zebone.platform.common.support.NHISUUID;
import com.zebone.platform.common.support.User;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.dao.build.BuildSql;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;

/**
 * ????????????
 */
@Service
public class SchPlanService {

    @Autowired
    private SchBaseMapper schBaseMapper;

    @Autowired
    private SchPlanMapper schPlanMapper;

    @Autowired
    private BaseCodeMapper codeMapper;
    
    @Autowired
    private SchMapper schMapper;

    @Autowired
    private SchHandlerService handlerService;
    
    @Autowired
	private SchService schService;

    /**
     * ????????????????????????
     *
     * @param param
     * @param user
     * @return
     */
    public SchPlanAndEmpsWeeksParam getSchPlanInfos(String param, IUser user) {
        Map<String, Object> mapParam = JsonUtil.readValue(param, new TypeReference<Map<String, Object>>() {
        });
        String pkSchplan = MapUtils.getString(mapParam, "pkSchplan");
        SchPlanVo schPlan = this.schBaseMapper.getSchPlanByPk(pkSchplan);
        List<SchPlanEmp> emplist = this.schBaseMapper
                .getSchPlanEmpsByPkSchplan(pkSchplan);
        List<SchPlanWeekExt> weeklist = this.schBaseMapper
                .getSchPlanWeeksByPkSchplan(pkSchplan);
        List<SchPlanweekPvtype> weekPvtypeList = this.schBaseMapper
                .getSchPlanWeekPvtypelistByPkPlanWeek(mapParam);
        if (CollectionUtils.isNotEmpty(weekPvtypeList)) {
            Map<String, Collection<SchPlanweekPvtype>> map =
                    Multimaps.index(weekPvtypeList, new Function<SchPlanweekPvtype, String>() {
                        @Override
                        public String apply(SchPlanweekPvtype pvtype) {
                            return pvtype.getPkPlanweek();
                        }
                    }).asMap();
            for (SchPlanWeekExt weekExt : weeklist) {
                weekExt.setPvtypeList(Lists.newArrayList(map.get(weekExt.getPkPlanweek())));
            }
        }

        SchPlanAndEmpsWeeksParam ret = new SchPlanAndEmpsWeeksParam();
//		schPlan.setCntStd(String.valueOf(schResource.getResourceDtList().get(0).getCntStd()));
        ret.setSchPlan(schPlan);
        ret.setPkDeptBelong(schPlan.getPkDeptBelong());
        ret.setEmpList(emplist);
        ret.setWeekList(weeklist);

        return ret;
    }

    /**
     * ??????-????????????????????????
     *
     * @param param
     * @param user
     * @return
     */
    public SchPlanAndEmpsWeeksParam getSchPlanInfosZs(String param, IUser user) {
        SchPlanAndEmpsWeeksParam ret = this.getSchPlanInfos(param, user);
        List<SchPlanWeekDt> list = DataBaseHelper.queryForList("select a.* from sch_planweek_dt a inner join SCH_PLAN_WEEK b on a.PK_PLANWEEK = b.PK_PLANWEEK where b.PK_SCHPLAN=? order by time_begin",
                SchPlanWeekDt.class, ret.getSchPlan().getPkSchplan());
        if (CollectionUtils.isNotEmpty(list)) {
            Function<SchPlanWeekDt, String> fun = new Function<SchPlanWeekDt, String>() {
                @Override
                public String apply(SchPlanWeekDt weekDt) {
                    return weekDt.getPkPlanweek();
                }
            };
            Multimap<String, SchPlanWeekDt> index = Multimaps.index(list, fun);
            Map<String, Collection<SchPlanWeekDt>> map = index.asMap();
            List<SchPlanWeekExt> listPlanweek = ret.getWeekList();
            for (SchPlanWeekExt week : listPlanweek) {
                week.setListPlanWeekDt(Lists.newArrayList(map.get(week.getPkPlanweek())));
            }
        }
        return ret;
    }

    /**
     * ????????????????????????
     *
     * @param param
     * @param user
     */
    public void saveSchPlan(String param, IUser user) {

        SchPlanAndEmpsWeeksParam planAndEmpsWeeks = JsonUtil.readValue(param,
                SchPlanAndEmpsWeeksParam.class);
        saveSchPlanPrivate(user, planAndEmpsWeeks, false);

    }

    /**
     * ????????????????????????-??????????????????
     *
     * @param param
     * @param user
     */
    public String saveSchPlanZs(String param, IUser user) {
        SchPlanAndEmpsWeeksParam planAndEmpsWeeks = JsonUtil.readValue(param,
                SchPlanAndEmpsWeeksParam.class);
        return saveSchPlanPrivate(user, planAndEmpsWeeks, true);
    }

    /***
     * ??????????????????<br>
     * isWeekDtSch=true????????????????????????????????????????????????????????????????????????????????????????????????????????????
     * @param user
     * @param planAndEmpsWeeks
     * @param isWeekDtSch ?????????????????????????????????
     */
    private String saveSchPlanPrivate(IUser user, SchPlanAndEmpsWeeksParam planAndEmpsWeeks, boolean isWeekDtSch) {
        SchPlanVo schPlanVo = planAndEmpsWeeks.getSchPlan();
        SchPlan schPlan = new SchPlan();
        ApplicationUtils.copyProperties(schPlan, schPlanVo);
        List<SchPlanEmp> emplist = planAndEmpsWeeks.getEmpList();
        List<SchPlanWeekExt> weeklist = planAndEmpsWeeks.getWeekList();

        User userVo = ((User) user);
        String pkOrg = userVo.getPkOrg();
        schPlan.setPkDeptPlan(userVo.getPkDept()); //????????????

        if (!StringUtils.isEmpty(schPlanVo.getPkDept())) {
            BdOuDept bdOuDept =
                    DataBaseHelper.queryForBean("select pk_orgarea from bd_ou_dept where pk_dept = ? ",
                            BdOuDept.class, new Object[]{schPlanVo.getPkDept()});
            schPlan.setPkOrgArea(bdOuDept.getPkOrgarea()); //????????????
        }
        //?????????????????????????????????????????????
        if (!StringUtils.isEmpty(schPlanVo.getEuAppttype())) {
            schPlanVo.setEuAppttype("0");
        }

        String oldDateslottype = null;
        /** ???????????????????????? **/
        if (schPlan.getPkSchplan() == null) {
            schPlan.setPkSchplan(NHISUUID.getKeyId());
            int count_same = DataBaseHelper
                    .queryForScalar(
                            "select count(1) from sch_plan "
                                    + "where del_flag = '0' and pk_schres = ? and pk_schsrv = ? and pk_org = ?",
                            Integer.class, schPlan.getPkSchres(),
                            schPlan.getPkSchsrv(), pkOrg);
            int count_code = DataBaseHelper
                    .queryForScalar(
                            "select count(1) from sch_plan "
                                    + "where del_flag = '0' and code = ? and eu_schclass = ? and pk_org = ?",
                            Integer.class, schPlan.getCode(),
                            schPlan.getEuSchclass(), pkOrg);
            int count_name = DataBaseHelper
                    .queryForScalar(
                            "select count(1) from sch_plan "
                                    + "where del_flag = '0' and name = ? and eu_schclass = ? and pk_org = ?",
                            Integer.class, schPlan.getName(),
                            schPlan.getEuSchclass(), pkOrg);
            Long minute_per = schPlan.getMinutePer();
            if (count_same == 0 && count_code == 0 && count_name == 0
                    && minute_per > 0 && minute_per < 60) {
                DataBaseHelper.insertBean(schPlan);
            } else {
                if (count_same != 0) {
                    throw new BusException("???????????????????????????????????????????????????");
                }
                if (count_code != 0) {
                    throw new BusException("?????????????????????????????????????????????????????????");
                }
                if (count_name != 0) {
                    throw new BusException("?????????????????????????????????????????????????????????");
                }
                if (minute_per > 0 && minute_per < 60) {
                    throw new BusException("??????????????????????????????0??????60???");
                }
            }
        } else {
            oldDateslottype = MapUtils.getString(DataBaseHelper.queryForMap("select dt_dateslottype from sch_plan where pk_schplan=?", new Object[]{schPlan.getPkSchplan()}), "dtDateslottype");
            int count_same = DataBaseHelper
                    .queryForScalar(
                            "select count(1) from sch_plan "
                                    + "where del_flag = '0' and pk_schres = ? and pk_schsrv = ? and pk_org = ? and pk_schplan != ?",
                            Integer.class, schPlan.getPkSchres(),
                            schPlan.getPkSchsrv(), pkOrg,
                            schPlan.getPkSchplan());
            int count_code = DataBaseHelper
                    .queryForScalar(
                            "select count(1) from sch_plan "
                                    + "where del_flag = '0' and code = ? and eu_schclass = ? and pk_org = ? and pk_schplan != ?",
                            Integer.class, schPlan.getCode(),
                            schPlan.getEuSchclass(), pkOrg,
                            schPlan.getPkSchplan());
            int count_name = DataBaseHelper
                    .queryForScalar(
                            "select count(1) from sch_plan "
                                    + "where del_flag = '0' and name = ? and eu_schclass = ? and pk_org = ? and pk_schplan != ?",
                            Integer.class, schPlan.getName(),
                            schPlan.getEuSchclass(), pkOrg,
                            schPlan.getPkSchplan());
            Long minute_per = schPlan.getMinutePer();
            if (count_same == 0 && count_code == 0 && count_name == 0
                    && minute_per > 0 && minute_per < 60) {
                DataBaseHelper.updateBeanByPk(schPlan, false);
                //??????date_end???null??????????????????????????????
                DataBaseHelper.update("update SCH_PLAN set date_end=? where PK_SCHPLAN=? ", new Object[]{schPlan.getDateEnd(), schPlan.getPkSchplan()});
            } else {
                if (count_same != 0) {
                    throw new BusException("???????????????????????????????????????????????????");
                }
                if (count_code != 0) {
                    throw new BusException("?????????????????????????????????????????????????????????");
                }
                if (count_name != 0) {
                    throw new BusException("?????????????????????????????????????????????????????????");
                }
                if (minute_per > 0 && minute_per < 60) {
                    throw new BusException("??????????????????????????????0??????60???");
                }
            }
        }

        /** ?????????????????? **/
        if (emplist != null) {
            if (emplist.size() != 0) {
                // ?????????
                String pkSchplan = schPlan.getPkSchplan();
                DataBaseHelper.execute(
                        "delete from sch_plan_emp where pk_schplan = ?",
                        new Object[]{pkSchplan});
                for (SchPlanEmp emp : emplist) {
                    emp.setPkSchplan(pkSchplan);
                    emp.setPkPlanemp(NHISUUID.getKeyId());
                    DataBaseHelper.insertBean(emp);
                }
            } else {
                String pkSchplan = schPlan.getPkSchplan();
                DataBaseHelper.execute(
                        "delete sch_plan_emp where pk_schplan = ?",
                        new Object[]{pkSchplan});
            }
        }

        /** ????????????????????? **/
        if (weeklist != null) {
            if (weeklist.size() != 0) {
                /**
                 * ????????????????????????????????????---??????????????????????????????????????????????????????
                 */
                String pkSchplan = schPlan.getPkSchplan();
                //????????????????????????????????????????????????
                boolean soltChange = StringUtils.isNotEmpty(schPlan.getDtDateslottype()) && schPlan.getDtDateslottype().equals(oldDateslottype);
                if (!isWeekDtSch || !soltChange) {
                    DataBaseHelper.execute("delete from sch_planweek_dt where pk_planweek in(select pk_planweek from sch_plan_week where pk_schplan = ?)",
                            new Object[]{pkSchplan});
                    DataBaseHelper.execute(
                            "delete from sch_plan_week where pk_schplan = ?",
                            new Object[]{pkSchplan});
                }
                for (SchPlanWeekExt week : weeklist) {
                    if (isWeekDtSch) {
                        //???????????????????????????????????????????????????????????????????????????pk???????????????
                        DataBaseHelper.update("delete from sch_plan_week where pk_planweek in(?,?)", week.getPkPlanweek(), week.getPkPlanweekDel());
                        if (StringUtils.isNotBlank(week.getPkPlanweekDel())) {
                            DataBaseHelper.update("delete from sch_planweek_dt where pk_planweek =?", new Object[]{week.getPkPlanweekDel()});
                        }
                        //???????????????
                        if (StringUtils.isBlank(week.getPkPlanweek()))
                            continue;
                    }
                    week.setPkSchplan(pkSchplan);
                    week.setPkOrg(pkOrg);
                    SchPlanWeek planWeek = new SchPlanWeek();
                    BeanUtils.copyProperties(week, planWeek);
                    DataBaseHelper.insertBean(planWeek);
                    if (isWeekDtSch) {
                        savePlanweekDt(week);
                    }
                    List<SchPlanweekPvtype> pvtypeList = week.getPvtypeList();
                    /**
                     * ????????????????????????_??????????????????
                     */
                    saveWeekPvtypeList(schPlan, pvtypeList, pkOrg);
                }
            } else {
                String pkSchplan = schPlan.getPkSchplan();
                DataBaseHelper.execute("delete from SCH_PLANWEEK_DT where PK_PLANWEEK in (select PK_PLANWEEK from SCH_PLAN_WEEK where PK_SCHPLAN=?)",new Object[]{pkSchplan});
                DataBaseHelper.execute(
                        "delete from sch_plan_week where pk_schplan = ?",
                        new Object[]{pkSchplan});
            }
        }
        return schPlan.getPkSchplan();
    }

    /**
     * ?????????????????????
     *
     * @param week
     */
    private void savePlanweekDt(SchPlanWeekExt week) {
        List<SchPlanWeekDt> listPlanWeekDt = week.getListPlanWeekDt();
        if (CollectionUtils.isNotEmpty(listPlanWeekDt)) {
            DataBaseHelper.update("delete from SCH_PLANWEEK_DT where PK_PLANWEEK=? and pk_dateslot=?", week.getPkPlanweek(), week.getPkDateslot());
            for (SchPlanWeekDt dt : listPlanWeekDt) {
                if (StringUtils.isBlank(dt.getPkPlanweek())) {
                    dt.setPkPlanweek(week.getPkPlanweek());
                }
                if (StringUtils.isBlank(dt.getPkDateslot())) {
                    dt.setPkDateslot(week.getPkDateslot());
                }
                ApplicationUtils.setDefaultValue(dt, true);
            }
            DataBaseHelper.batchUpdate(BuildSql.buildInsertSqlWithClass(SchPlanWeekDt.class), listPlanWeekDt);
        } else {
            //???????????????????????????
            int count = DataBaseHelper.queryForScalar("select count(*) from SCH_PLAN_WEEK where PK_PLANWEEK=? and PK_DATESLOT=?",
                    Integer.class, new Object[]{week.getPkPlanweek(), week.getPkDateslot()});
            if (count > 0 && DataBaseHelper.queryForScalar("select count(*) from SCH_PLANWEEK_DT where PK_PLANWEEK=? and PK_DATESLOT=?",
                    Integer.class, new Object[]{week.getPkPlanweek(), week.getPkDateslot()}) == 0) {
                throw new BusException("??????????????????????????????????????????");
            }
        }
    }

    /**
     * ????????????????????????_?????????????????? ????????????????????????????????????????????????
     * <p>
     * ???????????????eu_schclass ??? 0 ???????????????1 ???????????????2 ???????????????3 ????????????
     **/
    private void saveWeekPvtypeList(SchPlan schPlan,
                                    List<SchPlanweekPvtype> pvtypeList, String pkOrg) {
        if ("1".equals(schPlan.getEuSchclass()) && pvtypeList != null) {
            if (pvtypeList.size() != 0) {
                for (SchPlanweekPvtype weekPvtype : pvtypeList) {
                    if (weekPvtype.getPkPlanpvtype() != null) {
                        weekPvtype.setPkOrg(pkOrg);
                        DataBaseHelper.updateBeanByPk(weekPvtype, false);
                    } else {
                        weekPvtype.setPkOrg(pkOrg);
                        DataBaseHelper.insertBean(weekPvtype);
                    }
                }
            }
        }
    }

    /**
     * ??????????????????
     *
     * @param param
     * @param user
     */
    public void delSchPlan(String param, IUser user) {
        String pkSchplan = JSON.parseObject(param).getString("pkSchplan");

//        Integer count = DataBaseHelper.queryForScalar("select count(1) from SCH_SCH where PK_SCHPLAN = ? and DATE_WORK>=? and del_flag='0' and eu_status='8' and flag_stop='0'", Integer.class,
//                new Object[]{pkSchplan, DateUtils.getDateMorning(new Date(), 0)});
//        if(count>0){
//            throw new BusException("??????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????");
//        }

        DataBaseHelper.execute("delete from sch_plan_emp where pk_schplan = ?",
                new Object[]{pkSchplan});
        DataBaseHelper.execute(
                "delete from SCH_PLANWEEK_DT where pk_planweek in(select pk_planweek from sch_plan_week where pk_schplan = ?)",
                new Object[]{pkSchplan});
        DataBaseHelper.execute(
                "delete from sch_plan_week where pk_schplan = ?",
                new Object[]{pkSchplan});
        DataBaseHelper.execute("delete from sch_plan where pk_schplan = ?",
                new Object[]{pkSchplan});
    }

    /**
     * ????????????????????????
     *
     * @param param
     * @param user
     * @return
     */
    public Map<String, Object> getPlanEditData(String param, IUser user) {
        Map<String, String> params = JsonUtil.readValue(param, Map.class);
        String pkTicketrules = params.get("pkTicketrules");
        // String pkDateslot = params.get("pkDateslot");

        // ????????????
        String dtDateslottype = params.get("dtDateslottype");
        int minutePer = Integer.valueOf(params.get("minutePer"));
        int cntTotal = Integer.valueOf(params.get("cntTotal"));
        // ????????????
        int cntAppt = cntTotal;
        SchTicketrules schTicketrules = schBaseMapper
                .getSchTicketrulesByPk(pkTicketrules);
        int euRuletype;
        try {
            euRuletype = Integer.valueOf(schTicketrules.getEuRuletype());
        } catch (Exception e) {
            // TODO: handle exception
            euRuletype = TypeUtils.euRuleType_all;
        }
        switch (euRuletype) {
            case TypeUtils.euRuleType_all:
                break;
            case TypeUtils.euRuleType_allnot:
                cntAppt = 0;
                break;
            case TypeUtils.euRuleType_even:
                cntAppt = cntTotal / 2;
                break;
            case TypeUtils.euRuleType_odd:
                cntAppt = cntTotal % 2 == 0 ? cntTotal / 2 : cntTotal / 2 + 1;
                break;
            case TypeUtils.euRuleType_segment:
                int begin = schTicketrules.getBeginNo();
                int end = schTicketrules.getEndNo();
                if (cntTotal < begin) {
                    cntAppt = 0;
                } else if (cntTotal >= begin && cntTotal <= end) {
                    cntAppt = cntTotal - begin + 1;
                } else {
                    cntAppt = end - begin + 1;
                }
                break;
            case TypeUtils.euRuleType_enumerate:
                List<String> strTickets = Splitter.on(",").trimResults()
                        .splitToList(schTicketrules.getTickets());
                Function<String, Integer> strToIntFun = new Function<String, Integer>() {
                    @Override
                    public Integer apply(String arg0) {
                        // TODO Auto-generated method stub
                        return Integer.parseInt(arg0);
                    }
                };
                List<Integer> intTickets = Lists.transform(strTickets, strToIntFun);
                Integer min = Ordering.natural().min(intTickets);
                Integer max = Ordering.natural().max(intTickets);
                int cntEnum = intTickets.size();
                if (cntTotal < min) {
                    cntAppt = 0;
                } else if (cntTotal >= min && cntTotal <= max) {
                    cntAppt = 0;
                    for (Integer ticketNo : intTickets) {
                        if (ticketNo <= cntTotal) {
                            cntAppt++;
                        }
                    }
                } else {
                    cntAppt = cntEnum;
                }
                break;
            case TypeUtils.euRuleType_func:
                break;
            default:
                break;
        }
        // ????????????
        // BdCodeDateslot bdCodeDateslot =
        // codeMapper.selectBdCodeDateslotByPk(pkDateslot);
        int jhsc = handlerService.getPlanTime(dtDateslottype, params.get("pkDateslot"));
        // ????????????
        int sjsc = cntTotal * minutePer;
        // ?????????
        int cntAdd = jhsc - sjsc <= 0 ? 0 : (jhsc - sjsc) / minutePer;
        Integer cntStd = codeMapper.findCntStd(params);
        Map<String, Object> resultMap = Maps.newHashMap();
        resultMap.put("cntAppt", cntAppt);
        resultMap.put("jhsc", jhsc);
        resultMap.put("sjsc", sjsc);
        resultMap.put("cntAdd", cntAdd);
        resultMap.put("cntStd", cntStd);

        return resultMap;
    }


    /**
     * ?????????????????????????????????????????????
     *
     * @param param
     * @param user
     * @return
     */
    public Map<String, Object> getPlanEditDataZs(String param, IUser user) {
        Map<String, String> params = JsonUtil.readValue(param, Map.class);
        String pkPlanweek = null;
        String pkSchres = null;
        if (StringUtils.isBlank(params.get("pkDateslot"))) {
            throw new BusException("????????????????????????????????????");
        }
        //??????????????????????????????
        if (StringUtils.isBlank(pkPlanweek = params.get("pkPlanweek"))
                && StringUtils.isBlank(pkSchres = params.get("pkSchres"))) {
            throw new BusException("???????????????????????????????????????");
        }

        Map<String, Object> resultMap = Maps.newHashMap();
        Integer cntAdd = null;
        String pkDeptunit = null, pkDept = null;
        //?????????????????????????????????????????????
        String pkSchplan = params.get("pkSchplan");
        if (StringUtils.isNotBlank(pkPlanweek)) {
            Map<String, Object> mapWeek = DataBaseHelper.queryForMap("select a.cnt_total,a.cnt_add,a.pk_schplan,b.pk_dept,b.pk_schres,a.pk_deptunit "
                    + " from SCH_PLAN_WEEK a join SCH_PLAN b on a.PK_SCHPLAN=b.PK_SCHPLAN where a.pk_planweek=?", pkPlanweek);
            if (mapWeek == null) {
                throw new BusException("??????????????????????????????????????????");
            }
            cntAdd = MapUtils.getInteger(mapWeek, "cntAdd");
            pkDeptunit = MapUtils.getString(mapWeek, "pkDeptunit");
            pkDept = MapUtils.getString(mapWeek, "pkDept");
            pkSchres = MapUtils.getString(mapWeek, "pkSchres");
        } else if (StringUtils.isNotBlank(pkSchplan)) {
            //???????????????????????????????????????????????????????????????
            if (StringUtils.isNotBlank(params.get("pkDateslot")) && StringUtils.isNotBlank(params.get("dateWork"))) {
                Date d;
                try {
                    d = FastDateFormat.getInstance("yyyyMMddHHmmss").parse(params.get("dateWork"));
                } catch (ParseException e) {
                    throw new BusException("??????????????????");
                }
                params.put("weekNo", DateUtils.getDayNumOfWeek(d) + "");
                Map<String, Object> planMap = schPlanMapper.queryPlanAndWeek(params);
                if (planMap == null)
                    throw new BusException("??????????????????????????????");
                cntAdd = cntAdd == null ? MapUtils.getInteger(planMap, "cntAdd") : cntAdd;
                pkSchres = MapUtils.getString(planMap, "pkSchres");
                pkDeptunit = StringUtils.isBlank(pkDeptunit) ? MapUtils.getString(planMap, "pkDeptunit") : pkDeptunit;
                pkDept = StringUtils.isBlank(pkDept) ? MapUtils.getString(planMap, "pkDeptunit") : pkDept;
            }
        }

        params.put("pkSchres", pkSchres);
        resultMap.put("cntStd", codeMapper.findCntStd(params));
        resultMap.put("pkDeptunit", pkDeptunit);
        resultMap.put("pkDept", pkDept);
        resultMap.put("cntAdd", cntAdd);

        return resultMap;
    }

    /**
     * ????????????????????????
     *
     * @param param
     * @param user
     * @return
     */
    public Map<String, Object> getCheckPlanEditData(String param, IUser user) {
        Map<String, String> params = JsonUtil.readValue(param, Map.class);
        // String pkDateslot = params.get("pkDateslot");
        String dtDateslottype = params.get("dtDateslottype");
        int minutePer = Integer.valueOf(params.get("minutePer"));
        int cntTotal = Integer.valueOf(params.get("cntTotal"));

        // ????????????
        int jhsc = 0;
        List<BdCodeDateslot> bdCodeDateslots = codeMapper
                .listByDtDateslottype(dtDateslottype);
        Date now = new Date();
        for (BdCodeDateslot bdCodeDateslot : bdCodeDateslots) {
            jhsc += handlerService.getEffectivePlanDuration(bdCodeDateslot.getPkDateslot(),
                    now);
        }
        // ????????????
        int sjsc = cntTotal * minutePer;

        Map<String, Object> resultMap = Maps.newHashMap();
        resultMap.put("jhsc", jhsc);
        resultMap.put("sjsc", sjsc);
        return resultMap;
    }

    /**
     * ????????????????????????
     *
     * @param param
     * @param user
     * @return
     */
    public String queryDtDateslottype(String param, IUser user) {
        String pkSchres = JsonUtil.getFieldValue(param, "pkSchres");
        String dateslottype = schBaseMapper.queryDtDateslottype(pkSchres);
        return dateslottype;
    }

    /**
     * ??????????????????
     *
     * @param param
     * @param user
     * @return
     */
    public List<Map<String, Object>> queryPlanList(String param, IUser user) {
        Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
        User u = (User) user;
        if (paramMap == null) {
            paramMap = new HashMap<String, Object>();
        }
        if (paramMap.get("pkOrg") == null || "".equals(paramMap.get("pkOrg").toString())) {
            paramMap.put("pkOrg", u.getPkOrg());
        }
        //???????????????=1????????????SCH0018????????????
        String flagRole = CommonUtils.getPropValueStr(paramMap, "flagRole");
        if (EnumerateParameter.ONE.equals(flagRole)) {
            //???????????????SCH0018????????????????????????????????????????????????????????????
            String schAuthority = ApplicationUtils.getSysparam("SCH0018", false);
            paramMap.put("ftByUser", schAuthority);
            paramMap.put("pkUser", u.getPkUser());
            paramMap.put("pkEmp", u.getPkEmp());
            paramMap.put("pkDeptArea", u.getPkDept());
        }
        return schPlanMapper.getPlanList(paramMap);
    }

    /**
     * ??????????????????????????????
     *
     * @param param
     * @param user
     * @return
     */
    public List<SchPlanWeekDtVo> getWeekplanDt(String param, IUser user) {
        Map<String, String> params = JsonUtil.readValue(param, new TypeReference<Map<String, String>>() {
        });
        String pkPlanweek = MapUtils.getString(params, "pkPlanweek");
        if (StringUtils.isBlank(pkPlanweek)) {
            Date d;
            try {
                d = FastDateFormat.getInstance("yyyyMMddHHmmss").parse(params.get("dateWork"));
            } catch (ParseException e) {
                throw new BusException("??????????????????");
            }
            Map<String, Object> map = DataBaseHelper.queryForMap("select pk_planweek,pk_deptunit from sch_plan_week where pk_schplan=? and pk_dateslot=? and week_no=?",
                    new Object[]{params.get("pkSchplan"), params.get("pkDateslot"), DateUtils.getDayNumOfWeek(d)});
            if (MapUtils.isEmpty(map)) {
                throw new BusException("??????????????????????????????????????????");
            }
            pkPlanweek = MapUtils.getString(map, "pkPlanweek");
        }
        return DataBaseHelper.queryForList("select dt.*,plana.pk_deptunit from sch_planweek_dt dt left join sch_plan_week plana on dt.pk_planweek = plana.pk_planweek where dt.pk_planweek=?  order by time_begin", SchPlanWeekDtVo.class, pkPlanweek);
    }

    /**
     * ?????????????????????????????????
     * 022006006027
     * @param param
     * @param user
     * @return
     */
    public void checkSch(String param, IUser user) {
        String pkSchplan = JsonUtil.getFieldValue(param, "pkSchplan");
        Integer count = DataBaseHelper.queryForScalar("select count(1) from sch_sch where eu_status='8' and flag_stop='0' and eu_schclass='0' and pk_schplan=? and date_work>?", Integer.class,
                new Object[]{pkSchplan, DateUtils.getDateMorning(new Date(), 0)});
        if(count > 0){
            throw new BusException("??????????????????????????????????????????????????????????????????????????????????????????????????????????????????");
        }
    }
    
    
    /**  
    * <p>Desc:???????????????????????????: ??????????????????????????????,?????????????????????????????????????????????</p>
    * @param    flagStop???0?????????/1?????????  
    * @author : wangpengyong  
    * @date   : 2021???7???21???  
    */  
    public void  updateExistSch(SchSchstop schSchstop,String flagStop,IUser user) {
       SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
       String sStop = flagStop.equals("0")?"1":"0";//???????????????????????????????????????????????????
       String schSql = "select sch.pk_sch,sch.pk_schres,sch.pk_dateslot from sch_sch sch where sch.del_flag = '0' and sch.flag_stop = ? and sch.pk_emp_sch =? and sch.date_work >= to_date (?, 'yyyy-mm-dd') and sch.date_work <= to_date (?, 'yyyy-mm-dd')";
       List<Map<String,Object>> pkSchList = DataBaseHelper.queryForList(schSql,sStop,schSchstop.getPkEmpStop(),sdf.format(schSchstop.getDateStop()),sdf.format(schSchstop.getDateEnd()));
       if(CollectionUtils.isNotEmpty(pkSchList)) {
    	   for(Map<String,Object> sch:pkSchList) {
    		    //????????????????????????
	       	    //1.????????????????????????
	       	    String pkDateslot = sch.get("pkDateslot")==null?null:sch.get("pkDateslot").toString();
	       	    String resoucePK = sch.get("pkSchres")==null?null:sch.get("pkSchres").toString();
	       	    
	       	    String euNoon = null;//?????????????????????
	       	    String sEuNoon = schSchstop.getEuNoon();//???????????????
	   	       	Map<String,String> params = new HashMap<String,String>();
	   	       	params.put("pkDateslot",pkDateslot);
	   	       	params.put("pkSchres", resoucePK);
	   	       	if(params.size() >=2 ) {
	   	        	 euNoon = schMapper.getEuNoonByResource(params);
	   	       	}
	       	   
	       	    //2.??????????????????????????????????????????????????????????????????,?????????????????????
	   			boolean sameEuNoon = false;
	   			if (StringUtils.isBlank(sEuNoon)) {
	   				sameEuNoon = true;//????????????
	   			} else if(StringUtils.isNotBlank(euNoon) && StringUtils.isNotBlank(sEuNoon) && euNoon.equals(sEuNoon)) {
	   				sameEuNoon = true;
	   			}  
	   			
	   			if(sameEuNoon) {//????????????????????????????????????????????????
	   			   String pkSch = sch.get("pkSch").toString();
	    		   schService.stopRecoverSch(pkSch,flagStop,user);
	   			}
    	   }
       }
    }
    
    /**
     * ????????????????????????
     * 009002001013
     * @param param
     * @param user
     */
    public void saveSchSchstop(String param, IUser user) {
    	List<SchSchstop> schSchstopList = JsonUtil.readValue(param, new TypeReference<List<SchSchstop>>() {});
    	if(schSchstopList != null && schSchstopList.size() > 0) {
    		List<SchSchstop> addSchSchstopList = new ArrayList<SchSchstop>();
        	List<SchSchstop> updateSchSchstopList = new ArrayList<SchSchstop>();
        	
        	User u = (User)user;
        	String pkOrg = u.getPkOrg();
        	StringBuffer pkEmpSbf = new StringBuffer();
    		for (SchSchstop schSchstop : schSchstopList) {
    			//???????????????????????????
    			if(CommonUtils.isNull(schSchstop.getPkSchstop())) {
    				ApplicationUtils.setDefaultValue(schSchstop, true);
    				addSchSchstopList.add(schSchstop);
    			}else {
    				ApplicationUtils.setDefaultValue(schSchstop, false);
    				updateSchSchstopList.add(schSchstop);
    			}
    			
    			pkEmpSbf.append(schSchstop.getPkEmpStop()+",");
			}
    		
    		//?????????????????????????????????
    		Map<String,String>  reMap = loadCheckSchStop(pkOrg,pkEmpSbf,schSchstopList);
    		if(reMap.get("result").equals("1")) {//????????????,????????????
    	        	throw new BusException(reMap.get("msg"));
    	    }
    		
    		//??????
        	if(addSchSchstopList.size() > 0 ) {
        		DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(SchSchstop.class),addSchSchstopList);
        	}
        	//??????
        	if(updateSchSchstopList.size() > 0 ) {
        		DataBaseHelper.batchUpdate(DataBaseHelper.getUpdateSql(SchSchstop.class),updateSchSchstopList);
        	}
        	
        	//????????????????????????
        	if(CollectionUtils.isNotEmpty(schSchstopList)) {
        		for (SchSchstop schSchstop:schSchstopList) {
        			updateExistSch(schSchstop,"1",user);
        		}
        	}
    	}
    }
    
    /**  
     * <p>Desc: ??????????????????????????????????????????????????????(????????????)</p>  
     * @author : wangpengyong  
     * @date   : 2021???6???18???  
     */  
     public Map<String,String>  loadCheckSchStop(String pkOrg,StringBuffer pkEmpSbf,List<SchSchstop> schStopList) {
     	Map<String,String> returnMap = new HashMap<String,String>();
     	String result = "0";//????????????,????????????
     	String msg = "";//??????????????????
     	String pkEmpStr = pkEmpSbf.toString().substring(0,pkEmpSbf.length()-1);
     	
     	//??????????????????????????????(???????????????????????????)
     	SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        List<SchSchstop> existStopList = DataBaseHelper.queryForList("SELECT * from SCH_SCHSTOP where del_flag = '0' and  pk_org = ? and pk_emp_stop in (?)", 
        		 SchSchstop.class, new Object[]{pkOrg,pkEmpStr});
        if(null != existStopList && existStopList.size() > 0) {
        	forOuter:
        	for(SchSchstop eStop:existStopList) { 
        		String pkSchstop = eStop.getPkSchstop();
        		Date dateStop = eStop.getDateStop();//??????????????????
              	Date dateEnd = eStop.getDateEnd();//??????????????????
              	String euNoon = eStop.getEuNoon();//????????????
              	String pkEmp = eStop.getPkEmpStop();//??????????????????
              	String pkEmpName = eStop.getNameEmpStop();//??????????????????
              	
              	for(SchSchstop sch:schStopList) {
              		String sPkSchstop = sch.getPkSchstop();
              		Date sDateStop = sch.getDateStop();//??????????????????
              		Date sDateEnd = sch.getDateEnd();//??????????????????
                  	String sEuNoon = sch.getEuNoon();//????????????
                  	String sPkEmp = sch.getPkEmpStop();//??????????????????
                  	
                  	if(StringUtils.isNotBlank(sPkSchstop) && pkSchstop.equals(sPkSchstop)) {
                  		continue;//???????????????????????????
                  	}
                  	
                     //???????????????????????????:?????????????????????????????????????????????
                  	 boolean euNoonLimit = false;//???????????????
                  	 if(StringUtils.isBlank(euNoon) || StringUtils.isBlank(sEuNoon)) {
                  		euNoonLimit = true;//????????????
                  	 } else if (StringUtils.isNotBlank(euNoon) && StringUtils.isNotBlank(sEuNoon) && euNoon.equals(sEuNoon)) {
                  		euNoonLimit = true;//?????????????????????
                  	 }
                  	 
                  	if(StringUtils.isNotBlank(sPkEmp) && StringUtils.isNotBlank(pkEmp) && sPkEmp.equals(pkEmp) ) {//???????????????
                  		if( null != dateStop && null != dateEnd) {//????????????,???????????????????????????,?????????????????????
                  			//?????????: ?????????????????????
                  			if( DateUtils.compareToDay(sDateStop, dateStop) >= 0 && DateUtils.compareToDay(sDateStop, dateEnd) <= 0  && euNoonLimit ){
             					result = "1";
             					msg = "???"+pkEmpName+"?????????,???"+sdf.format(dateStop)+"-"+sdf.format(dateEnd)+"???????????????????????????????????????";
                     			break forOuter;
             				}
                  			
                  		    //?????????: ?????????????????????
                  			if( DateUtils.compareToDay(sDateEnd, dateStop) >= 0 && DateUtils.compareToDay(sDateEnd, dateEnd) <= 0  && euNoonLimit ){
             					result = "1";
             					msg = "???"+pkEmpName+"?????????,???"+sdf.format(dateStop)+"-"+sdf.format(dateEnd)+"???????????????????????????????????????";
                     			break forOuter;
             				}
                  			
                  		    //?????????: ?????????????????????????????????
                  			if(DateUtils.compareToDay(sDateStop, dateStop) < 0  && DateUtils.compareToDay(sDateEnd, dateEnd) > 0 && euNoonLimit ){
             					result = "1";
             					msg = "???"+pkEmpName+"?????????,???"+sdf.format(dateStop)+"-"+sdf.format(dateEnd)+"???????????????????????????????????????";
                     			break forOuter;
             				}
                     	} else if( null != dateEnd ) {//???????????????(????????????)
             				if(DateUtils.compareToDay(sDateStop, dateEnd) <= 0 && euNoonLimit ){
             					result = "1";
             					msg = "???"+pkEmpName+"?????????,???"+sdf.format(dateEnd)+"???????????????????????????????????????";
                     			break forOuter;
             				}
                     	} else if(null != dateStop){//???????????????(????????????)
                     		if(DateUtils.compareToDay(sDateEnd, dateStop) >= 0 && euNoonLimit ){
                     			result = "1";
             					msg = "???"+pkEmpName+"?????????,???"+sdf.format(dateStop)+"???????????????????????????????????????";
                     			break forOuter;
             				}
                     	} 
                  	}
              	}
        	}
        }
     	returnMap.put("result", result);
     	returnMap.put("msg",msg);
     	return returnMap;
     }
    
    /**
     * ????????????????????????
     * 009002001014
     * @param param
     * @param user
     */
    public List<SchSchstop> getSchSchstop(String param, IUser user) {
    	Map<String,Object> paramMap = JsonUtil.readValue(param, Map.class);
    	User u = (User)user;
    	paramMap.put("pkOrg", u.getPkOrg());
    	
    	//??????????????????
    	String dateEnd = paramMap.get("dateEnd") == null?null:paramMap.get("dateEnd").toString();
    	if(StringUtils.isNotBlank(dateEnd) && dateEnd.length() >= 8) {
    		dateEnd = dateEnd.substring(0, 8);
    		paramMap.put("dateEnd", dateEnd);
    	}
    	return schPlanMapper.getSchSchstop(paramMap);
    }
    
    
    /**
     * ?????????????????????????????????
     * 009002001016
     * @param param
     * @param user
     */
    public String getExistSch(String param, IUser user) {
    	String schFlag = "0";//?????????
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    	SchSchstop schSchstop = JsonUtil.readValue(param, SchSchstop.class);
    	if(null != schSchstop) {
    		 String schSql = "select sch.pk_sch from sch_sch sch where sch.del_flag = '0' and sch.flag_stop = '1' and sch.pk_emp_sch =? and sch.date_work >= to_date (?, 'yyyy-mm-dd') and sch.date_work <= to_date (?, 'yyyy-mm-dd')";
             List<Map<String,Object>> pkSchList = DataBaseHelper.queryForList(schSql,schSchstop.getPkEmpStop(),sdf.format(schSchstop.getDateStop()),sdf.format(schSchstop.getDateEnd()));
             if(CollectionUtils.isNotEmpty(pkSchList)) {
            	 schFlag =  "1";
             }
    	}
    	return schFlag;
    }
    
    
    /**
     * ????????????????????????
     * 009002001015
     * @param param
     * @param user
     */
    public void delSchSchstop(String param, IUser user) {
    	SchSchstop schSchstop = JsonUtil.readValue(param, SchSchstop.class);
    	User u = (User)user;
    	if(schSchstop != null) {
    		DataBaseHelper.update("update sch_schstop set del_flag = '1',modifier=?,modity_time=to_date(?,'yyyyMMddhh24miss')  where pk_schstop = ?", new Object[]{u.getPkEmp(),DateUtils.dateToStr("yyyyMMddHHmmss", new Date()),schSchstop.getPkSchstop()});
    		
    		//???????????????????????????,?????????
    		if(StringUtils.isNotBlank(schSchstop.getNote()) && schSchstop.getNote().equals("1")) {
    			updateExistSch(schSchstop,"0",user);
    		}
    	}
    }
}
