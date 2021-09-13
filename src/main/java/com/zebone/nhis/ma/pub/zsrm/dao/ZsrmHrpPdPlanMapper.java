package com.zebone.nhis.ma.pub.zsrm.dao;

import com.zebone.nhis.common.module.scm.purchase.PdPlan;
import com.zebone.nhis.common.module.scm.purchase.PdPlanDetail;
import com.zebone.nhis.ma.pub.zsrm.vo.*;
import com.zebone.platform.modules.mybatis.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface ZsrmHrpPdPlanMapper {

    /**
     * 发起调拨
     * @param pkPdplan
     * @return
     */
    List<ApplyVo> getPdplanByPkPdplan(String pkPdplan);

    /**
     * 查询pd_plan_detail 获取写入pd_st_detail的数据
     * @param allocaReqVo
     * @return
     */
    PdPlanDetail getPdplanDetailByPkPdplandt(String pkPdplandt);


    /**
     * 药房退药
     */
    List<ApplyVo>  pharmacyDrugWithdrawal(String pkPdst);

    List<ApplyVo> querylistToHrp(@Param(value="stock") ZsrmHrpStock stock);

    /**
     * 获取药品信息(出库参数)
     */
    PdPlanDetail getPdInfoByKeyCode(String medicineCode);

}
