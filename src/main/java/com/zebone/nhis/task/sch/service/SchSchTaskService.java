package com.zebone.nhis.task.sch.service;

import com.alibaba.druid.support.jconsole.model.ColumnGroup;
import com.zebone.nhis.common.module.sch.plan.SchPlan;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.common.support.EnumerateParameter;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.service.ZsphPlatFormSendOpHandler;
import com.zebone.nhis.sch.plan.service.SchAuditService;
import com.zebone.nhis.sch.plan.service.SchService;
import com.zebone.platform.common.support.User;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.modules.core.spring.ServiceLocator;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;
import com.zebone.platform.quartz.modle.QrtzJobCfg;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.text.ParseException;
import java.util.*;

@Service
public class SchSchTaskService {

    @Autowired
    private SchService schService;
    @Autowired
    private SchAuditService schAuditService;
    @Resource
    private ZsphPlatFormSendOpHandler sendOpHandler;

    /**
     * 按计划生成排班
     * {"days":1,"dayExecute":1}
     *
     * @param cfg
     */
    public Map genSchTask(QrtzJobCfg cfg) {
        int genDays = 0, dayExecute = 1;
        Set<String> planCodeSet = null;
        Map<String, Object> paramMap = null;
        try {
            paramMap = JsonUtil.readValue(cfg.getJobparam(), Map.class);
            //从哪一天开始生成（用当日起加上days的日期生成）
            genDays = MapUtils.getIntValue(paramMap, "days", 1);
            //生成多少天的
            dayExecute = MapUtils.getIntValue(paramMap, "dayExecute", 1);
            Object planCodes = MapUtils.getObject(paramMap, "planCodes");
            if (planCodes != null && planCodes instanceof List) {
                planCodeSet = new HashSet<>((List<String>) planCodes);
            }
        } catch (Exception e) {
            throw new BusException("任务参数转换异常" + e.getMessage());
        }
        if (genDays < 1) {
            throw new BusException("设置的days必须满足days>=1，否则不执行");
        }
        if (dayExecute < 1 || dayExecute > 15) {
            throw new BusException("设置的dayExecute必须满足1<=d<=15，否则不执行");
        }
        Map<String, String> result = new HashMap<String, String>();
        List<String> pkOrgList=validForArray(cfg);
        if(pkOrgList==null || pkOrgList.size()==0){
            throw new BusException("未获取到机构编码！");
        }

        for (String pkOrg: pkOrgList ) {
            User user = setUser(pkOrg);
            //获取计划
            List<SchPlan> planList = getSchplan(planCodeSet, pkOrg);
            //组装参数，按计划生成排班,不并行，单个计划，单个事务处理
            StringBuilder sbl = new StringBuilder();
            Date date = new Date();
            paramMap.clear();
            paramMap.put("beginDate", DateUtils.addDate(date, genDays, 3, "yyyy-MM-dd"));
            paramMap.put("endDate", DateUtils.addDate(date, genDays + (dayExecute - 1), 3, "yyyy-MM-dd"));
            paramMap.put("isCover", "0");
            paramMap.put("euSchclass", "0");
            for (SchPlan plan : planList) {
                paramMap.put("pkSchplan", plan.getPkSchplan());
                try {
                    ServiceLocator.getInstance().getBean(SchSchTaskService.class).createSchByPlan(paramMap, user);
                } catch (Exception e) {
                    sbl.append(plan.getCode()).append(":").append(e.getMessage()).append(";");
                }
            }
            if (sbl.length() > 0) {
                result.put("customStatus", sbl.toString());
            }
        }
        return result;
    }

    /**
     * 发布排班
     *
     * @param cfg
     */
    public void auditSch(QrtzJobCfg cfg) {
        int days = 0;
        try {
            Map<String, Object> paramMap = JsonUtil.readValue(cfg.getJobparam(), Map.class);
            days = MapUtils.getIntValue(paramMap, "days", 0);
        } catch (Exception e) {
            throw new BusException("任务参数转换异常" + e.getMessage());
        }
        if (days < 0) {
            throw new BusException("设置的天数d必须满足d>=0，否则不执行");
        }
        Map<String, String> result = new HashMap<String, String>();
        List<String> pkOrgList = validForArray(cfg);
        if (pkOrgList == null || pkOrgList.size() == 0) {
            throw new BusException("未获取到机构编码！");
        }

        for (String pkOrg: pkOrgList ) {
            List<String> pkSchList = DataBaseHelper.getJdbcTemplate().queryForList("select pk_sch from sch_sch where nvl(FLAG_STOP,'0')='0' and DEL_FLAG='0' and eu_status=0 and to_char(DATE_WORK,'yyyyMMdd')>=? and PK_ORG=?"
                    , String.class, DateUtils.addDate(new Date(), days, 3, "yyyyMMdd"), pkOrg);
            Map<String, Object> auditInfo = new HashMap<>();
            auditInfo.put("pkEmpChk", "sysTask");
            auditInfo.put("dateChk", new Date());
            User user = setUser(pkOrg);
            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("euStatus", EnumerateParameter.EIGHT);
            paramMap.put("auditInfo", auditInfo);
            paramMap.put("isReferStatus", "true");
            paramMap.put("pkSchs", pkSchList);
            schAuditService.saveAudit(JsonUtil.writeValueAsString(paramMap), user);
        }
    }

    /**
     * 根据排班计划设置的截至时间生成排班
     *
     * @param cfg
     * @return
     */
    public Map genSchTaskByPlan(QrtzJobCfg cfg) {
        //是否发布排班，默认为发布
        String isPublish = "1";
        int defAddDays=0;
        Map<String, Object> paramMap = null;
        try {
            paramMap = JsonUtil.readValue(cfg.getJobparam(), Map.class);
            isPublish = MapUtils.getString(paramMap, "isPublish", "1");
            defAddDays=MapUtils.getInteger(paramMap,"defAddDays",0);
        } catch (Exception e) {
            throw new BusException("任务参数转换异常" + e.getMessage());
        }

        List<String> pkOrgList=validForArray(cfg);
        if(pkOrgList==null || pkOrgList.size()==0){
            throw new BusException("未获取到机构编码！");
        }

        Map<String, String> result = new HashMap<String, String>();
        for (String pkOrg: pkOrgList ) {
            User user = setUser(pkOrg);
            //获取计划
            List<SchPlan> planList = getSchplan(null, pkOrg);

            //组装参数，按计划生成排班,不并行，单个计划，单个事务处理
            StringBuilder sbl = new StringBuilder();
            if (planList == null || planList.size() <= 0) {
                result.put(pkOrg,"当前机构排班生成失败，未查询到符合条件的排班计划！");
                continue;
            } else {
                Date nowDate = new Date();
                for (SchPlan plan : planList) {
                    paramMap.put("pkSchplan", plan.getPkSchplan());
                    paramMap.put("isPublish", isPublish);
                    paramMap.put("beginDate", DateUtils.formatDate(nowDate, "yyyy-MM-dd"));
                    paramMap.put("isCover", "0");
                    paramMap.put("euSchclass", "0");
                    paramMap.put("pkOrg", pkOrg);

                    String endDate = null;
                    //排班周期,为空默认为7
                    int cyclePlan = plan.getCyclePlan() == null || plan.getCyclePlan() <= 0 ? 7 : plan.getCyclePlan().intValue();
                    cyclePlan=cyclePlan+defAddDays;
                    //截至日期不为空时生成的排班截至到该日期停止
                    if (plan.getDateEnd() != null) {
                        if (DateUtils.getDateSpace(nowDate, plan.getDateEnd()) > 0) {
                            endDate = DateUtils.getDateSpace(nowDate, plan.getDateEnd()) > cyclePlan ?
                                    DateUtils.addDate(nowDate, cyclePlan - 1, 3, "yyyy-MM-dd") : DateUtils.formatDate(plan.getDateEnd(), "yyyy-MM-dd");
                            paramMap.put("endDate", endDate);
                            try {
                                ServiceLocator.getInstance().getBean(SchSchTaskService.class).createSchPublishByPlan(paramMap, user);
                            } catch (Exception e) {
                                sbl.append(plan.getCode()).append(":").append(e.getMessage()).append(";");
                            }
                        }
                    } else {
                        endDate = DateUtils.addDate(nowDate, cyclePlan - 1, 3, "yyyy-MM-dd");
                        paramMap.put("endDate", endDate);
                        try {
                            ServiceLocator.getInstance().getBean(SchSchTaskService.class).createSchPublishByPlan(paramMap, user);
                        } catch (Exception e) {
                            sbl.append(plan.getCode()).append(":").append(e.getMessage()).append(";");
                        }
                    }
                }
            }

            if (sbl.length() > 0) {
                result.put("customStatus", sbl.toString());
            }
        }
        return result;
    }

    private String valid(QrtzJobCfg cfg) {
        String pkOrg = cfg.getJgs();
        if (StringUtils.isBlank(pkOrg)) {
            throw new BusException("请先对任务授权");
        }
        if (pkOrg != null && pkOrg.contains(",")) {
            pkOrg = pkOrg.replace(CommonUtils.getGlobalOrg(), "").replace(",", "");
        } else if (CommonUtils.getGlobalOrg().equals(pkOrg)) {
            throw new BusException("请将任务授权给具体机构");
        }
        if (StringUtils.isBlank(cfg.getJobparam())) {
            throw new BusException("请设置任务执行参数");
        }
        return pkOrg;
    }

    /**
     * 多机构模式获取机构主键
     * @param cfg
     * @return
     */
    private List<String> validForArray(QrtzJobCfg cfg) {
        String pkOrg = cfg.getJgs();
        List<String> pkOrgs=new ArrayList<>();
        if (StringUtils.isBlank(pkOrg)) {
            throw new BusException("请先对任务授权");
        }
        if (pkOrg != null && pkOrg.contains(",")) {
            pkOrg = pkOrg.replace(CommonUtils.getGlobalOrg(), "");
            if(!pkOrg.contains(",")){
                pkOrgs.add(pkOrg);
            }else {
                List<String> org_temp= Arrays.asList(pkOrg.split(","));
                if(org_temp!=null && org_temp.size()>0){
                    pkOrgs.addAll(org_temp);
                }
            }
        } else if (CommonUtils.getGlobalOrg().equals(pkOrg)) {
            throw new BusException("请将任务授权给具体机构");
        }
        if (StringUtils.isBlank(cfg.getJobparam())) {
            throw new BusException("请设置任务执行参数");
        }
        return pkOrgs;
    }

    private User setUser(String pkOrg) {
        User user = new User();
        user.setPkOrg(pkOrg);
        user.setPkEmp("sysTask");
        user.setNameEmp("sysTask");
        UserContext.setUser(user);
        return user;
    }

    private List<SchPlan> getSchplan(Set<String> planCode, String pkOrg) {
        StringBuilder sql = new StringBuilder("select pk_schplan,code,cycle_plan,date_end from SCH_PLAN where nvl(FLAG_STOP,'0')='0' and DEL_FLAG='0' and pk_org=?");
        if (planCode != null && planCode.size() > 0) {
            sql.append(" and CODE in ('").append(StringUtils.join(planCode, "','")).append("')");
        }
        return DataBaseHelper.queryForList(sql.toString(), SchPlan.class, pkOrg);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public void createSchByPlan(Map<String, Object> paramMap, User user) {
        try {
            schService.generateSchByTimeInterval(JsonUtil.writeValueAsString(paramMap), user);
        } catch (ParseException e) {
            throw new BusException("json转换异常");
        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public void createSchPublishByPlan(Map<String, Object> paramMap, User user) {
        try {
            List<String> pkSchs = schService.generateSchByTimeInterval(JsonUtil.writeValueAsString(paramMap), user);
            Set<String> pkSchSet = new HashSet<String>(pkSchs);
            if (paramMap.get("isPublish").equals("1") && DateUtils.getDateSpace(DateUtils.strToDate(paramMap.get("beginDate").toString(), "yyyy-MM-dd"), DateUtils.strToDate(paramMap.get("endDate").toString(), "yyyy-MM-dd")) > 0) {
                //将生成的排班发布
                List<String> pkSchList = DataBaseHelper.getJdbcTemplate().queryForList("select pk_sch from sch_sch where PK_SCH in (" + CommonUtils.convertSetToSqlInPart(pkSchSet, "pk_sch") + ")", String.class);
                if (pkSchList != null && pkSchList.size() > 0) {
                    Map<String, Object> auditInfo = new HashMap<>();
                    auditInfo.put("pkEmpChk", "sysTask");
                    auditInfo.put("dateChk", new Date());
                    Map<String, Object> paramMapPublish = new HashMap<>();
                    paramMapPublish.put("euStatus", EnumerateParameter.EIGHT);
                    paramMapPublish.put("auditInfo", auditInfo);
                    paramMapPublish.put("isReferStatus", "true");
                    paramMapPublish.put("pkSchs", pkSchList);
                    schAuditService.saveAudit(JsonUtil.writeValueAsString(paramMapPublish), user);
                }
            }
        } catch (ParseException e) {
            throw new BusException("json转换异常");
        }
    }


    /**
     * 根据时间发送预约短信
     * @param cfg
     */
    public void sendApptRegMsg(QrtzJobCfg cfg) {
        String pkOrg = cfg.getJgs();
        if(StringUtils.isBlank(pkOrg)){
            throw new BusException("请先对任务授权");
        }
        //这哪是先把机构去掉，多个机构的话在保存消息日志的时候会报错
        /*User user = new User();
        user.setPkOrg(pkOrg);
        UserContext.setUser(user);*/
        sendOpHandler.sendNewTastShortMsgApptRegMsg();
    }
}
