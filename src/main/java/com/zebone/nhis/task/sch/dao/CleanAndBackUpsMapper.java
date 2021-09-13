package com.zebone.nhis.task.sch.dao;

import com.zebone.platform.modules.mybatis.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface CleanAndBackUpsMapper {
    /**
     *排班明细 删除前一天所有内容
     */
    int delSchTicketBeforeDay(@Param("day")int day);
    /**
     *排班记录 删除前一周
     */
    int delSchTicketBeforeWeekSSB(@Param("day") int day);
    /**
     *门诊预约就诊记录 删除前一周
     */
    int delSchTicketBeforeWeekSAP(@Param("day") int day);
    /**
     *门诊排队记录 删除前一天所有内容
     */
    int delSchTicketBeforeDayPQ(@Param("day") int day);
}
