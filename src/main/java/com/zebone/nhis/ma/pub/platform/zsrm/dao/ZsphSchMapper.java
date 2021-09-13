package com.zebone.nhis.ma.pub.platform.zsrm.dao;

import com.zebone.nhis.common.module.bl.opcg.BlOpDt;
import com.zebone.nhis.common.module.sch.pub.SchResource;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.model.SchDeptVo;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.model.SchDoctorVo;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.model.SchVo;
import com.zebone.platform.modules.mybatis.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface ZsphSchMapper {

    List<SchVo> getSchbyParam(Map<String,Object> paramMap);

    /**
     * 查询有可预约号（且未使用）的部门信息
     * @param paramMap
     * @return
     */
    List<SchDeptVo> getSchApptDept(Map<String,Object> paramMap);

    //必须给医生排班，这些方法也查询的是具体医生排班。
    // 如果联立资源查医生列表，那么一个排班的一批号源对应成了一个科室，不正确。

    /**
     * 查询有可预约号区分午别
     * @param paramMap
     * @return
     */
    List<SchDoctorVo> getSchApptDoctor(Map<String,Object> paramMap);

    /**
     * 查询有可预约号区分午别:共享标志
     * @param paramMap
     * @return
     */
    List<SchDoctorVo> getSchApptDoctorShare(Map<String,Object> paramMap);
    /**
     * 查询有可预约号不区分午别
     * @param paramMap
     * @return
     */
    List<SchDoctorVo> getSchApptDoctorClass(Map<String,Object> paramMap);

    /**
     * 查询有可预约号不区分午别:共享标志
     * @param paramMap
     * @return
     */
    List<SchDoctorVo> getSchApptDoctorClassShare(Map<String,Object> paramMap);
    /**
     * 查询有可预约号（且未使用）的时段信息
     * @param paramMap
     * @return
     */
    List<SchVo> getSchApptTime(Map<String,Object> paramMap);

    /**
     * 查询诊疗资源不相等的排班资源
     * @param paramMap
     * @return
     */
    List<SchResource> getResource(Map<String,Object> paramMap);
    /**
     * 批量更新排班资源的诊疗服务
     * @param resioList
     * @return
     */
    int updateSchResourceList(List<SchResource> resioList);
}
