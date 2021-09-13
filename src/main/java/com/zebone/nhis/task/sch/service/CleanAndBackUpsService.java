package com.zebone.nhis.task.sch.service;


import com.zebone.nhis.task.sch.dao.CleanAndBackUpsMapper;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;
import com.zebone.platform.quartz.modle.QrtzJobCfg;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

@Service
public class CleanAndBackUpsService {
    @Resource
    CleanAndBackUpsMapper cleanAndBackUpsMapper;

    public void master(QrtzJobCfg cfg){
        String deleteDay = getDeleteDay(cfg);
        String insertDay = getInsertDay(cfg);
        String deleteWeek = getDeleteWeek(cfg);
        delSchTicketBeforeDay(deleteDay);
        insertSchSchBBeforeDay(insertDay);
        delSchTicketBeforeWeekSSB(deleteWeek);
        delSchTicketBeforeWeekSAP(deleteWeek);
        insertSchSchBBeforeDaySA(insertDay);
        delSchTicketBeforeDayPQ(deleteDay);
    }

    /**
     *排班明细 删除前一天所有内容
     */
    public Map<String,Object> delSchTicketBeforeDay(String day){
        int count = cleanAndBackUpsMapper.delSchTicketBeforeDay(Integer.valueOf(day)-1);
        Map<String,Object> result = new HashMap<String,Object>();
        String msg = "删除定时任务排班明细"+count+"条！";
        System.out.println(msg);
        result.put("msg",msg);
        return result;
    }
    /**
     *排班记录 转储前一天所有内容
     */
    public Map<String,Object> insertSchSchBBeforeDay(String day){
        int execute = DataBaseHelper.execute("insert into sch_sch_b select * from sch_sch where to_char(create_time,'yyyy-mm-dd')=to_char(sysdate-" + Integer.valueOf(day) + ",'yyyy-mm-dd')");
        Map<String,Object> result = new HashMap<String,Object>();
        String msg = "转储定时任务排班记录"+execute+"条！";
        System.out.println(msg);
        result.put("msg",msg);
        return result;
    }
    /**
     *排班记录 删除前一周(七天)
     */
    public Map<String,Object> delSchTicketBeforeWeekSSB(String day){
        int count = cleanAndBackUpsMapper.delSchTicketBeforeWeekSSB(Integer.valueOf(day));
        Map<String,Object> result = new HashMap<String,Object>();
        String msg = "删除定时任务排班记录 删除前一周"+count+"条！";
        System.out.println(msg);
        result.put("msg",msg);
        return result;
    }
    /**
     *门诊预约就诊记录 删除前一周(七天)
     */
    public Map<String,Object> delSchTicketBeforeWeekSAP(String day){
        int count = cleanAndBackUpsMapper.delSchTicketBeforeWeekSAP(Integer.valueOf(day));
        Map<String,Object> result = new HashMap<String,Object>();
        String msg = "删除定时任务门诊预约就诊记录 删除前一周"+count+"条！";
        System.out.println(msg);
        result.put("msg",msg);
        return result;
    }
    /**
     *门诊预约记录 转储前一天所有内容
     */
    public Map<String,Object> insertSchSchBBeforeDaySA(String day){
        int execute = DataBaseHelper.execute("insert into sch_appt_b select * from sch_appt where to_char(create_time,'yyyy-mm-dd')=to_char(sysdate-" + Integer.valueOf(day) + ",'yyyy-mm-dd')");
        Map<String,Object> result = new HashMap<String,Object>();
        String msg = "转储定时任务门诊预约记录"+execute+"条！";
        System.out.println(msg);
        result.put("msg",msg);
        return result;
    }
    /**
     *门诊排队记录 删除前一天所有内容
     */
    public Map<String,Object> delSchTicketBeforeDayPQ(String day){
        int count = cleanAndBackUpsMapper.delSchTicketBeforeDayPQ(Integer.valueOf(day)-1);
        Map<String,Object> result = new HashMap<String,Object>();
        String msg = "删除定时任务门诊排队记录"+count+"条！";
        System.out.println(msg);
        result.put("msg",msg);
        return result;
    }
    public String getDeleteDay(QrtzJobCfg cfg){
        String day=JsonUtil.readValue(cfg.getJobparam(), new TypeReference<Map<String,String>>(){}).get("deleteDay");
        if (StringUtils.isBlank(day)){
            throw new BusException("参数不能为空");
        }
        return day;
    }
    public String getDeleteWeek(QrtzJobCfg cfg){
        String day=JsonUtil.readValue(cfg.getJobparam(), new TypeReference<Map<String,String>>(){}).get("deleteWeek");
        if (StringUtils.isBlank(day)){
            throw new BusException("参数不能为空");
        }
        return day;
    }
    public String getInsertDay(QrtzJobCfg cfg){
        String day=JsonUtil.readValue(cfg.getJobparam(), new TypeReference<Map<String,String>>(){}).get("insertDay");
        if (StringUtils.isBlank(day)){
            throw new BusException("参数不能为空");
        }
        return day;
    }
}
