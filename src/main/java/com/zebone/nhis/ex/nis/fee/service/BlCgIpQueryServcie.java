package com.zebone.nhis.ex.nis.fee.service;

import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.ex.nis.fee.dao.BlCgIpQueryMapper;
import com.zebone.nhis.ex.pub.support.PatiFeeHandler;
import com.zebone.nhis.ex.pub.vo.PatiCardVo;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 患者费用查询
 *
 * @author yangxue
 */
@Service
public class BlCgIpQueryServcie {
    @Resource
    private BlCgIpQueryMapper blCgQueryMapper;
    @Resource
    private PatiFeeHandler patiFeeHandler;

    /**
     * 查询患者费用汇总  患者就诊主键，开始日期，结束日期 不能为空
     *
     * @param map{pkPvs,dateBegin,dateEnd,type：0非物品，1物品,pkItemcate}
     * @return
     */
    public List<Map<String, Object>> queryBlCgIpSummer(String param, IUser user) {
        Map<String, Object> map = JsonUtil.readValue(param, Map.class);
        String endDate = CommonUtils.getString(map.get("dateEnd"));
        if (endDate != null && !endDate.equals("")) {
            endDate = endDate.substring(0, 8) + "235959";
            map.put("dateEnd", endDate);
        }
        //所有费用查询，以pkPv为标准（即，查询本次就诊的所有费用，不区分科室）
        List<String> pvlist = (List) map.get("pkPvs");
        if (pvlist == null || pvlist.size() <= 0)
            throw new BusException("未获取到患者就诊信息！");
        // String pk_dept_cur = ((User)user).getPkDept();
        //map.put("pkDeptNs", pk_dept_cur);
        List<Map<String, Object>> result = blCgQueryMapper.queryBlCgIpSummer(map);
        //如果是产房，查询的时候增加婴儿的费用信息
        //if(PatiPvInfoUtil.isDeptCF(pk_dept_cur)){
        //	 List<Map<String,Object>> inflist =  blCgQueryMapper.queryInfantBlCgIpSummer(map);
        //	 if(inflist!=null&&inflist.size()>0) result.addAll(inflist);
        //}
        return result;
    }

    /**
     * 查询患者费用明细  患者就诊主键，开始日期，结束日期 不能为空
     *
     * @param map{pkPvs,dateBegin,dateEnd,type：0非物品，1物品}
     * @return
     */
    public List<Map<String, Object>> queryBlCgIpDetails(String param, IUser user) {
        Map<String, Object> map = JsonUtil.readValue(param, Map.class);
        String endDate = CommonUtils.getString(map.get("dateEnd"));
        if (endDate != null && !endDate.equals("")) {
            endDate = endDate.substring(0, 8) + "235959";
            map.put("dateEnd", endDate);
        }
        List<String> pvlist = (List) map.get("pkPvs");
        if (pvlist == null || pvlist.size() <= 0)
            throw new BusException("未获取到患者就诊信息！");
        // String pk_dept_cur = ((User)user).getPkDept();
        //map.put("pkDeptNs", pk_dept_cur);
        //所有费用查询，以pkPv为标准（即，查询本次就诊的所有费用，不区分科室）
        List<Map<String, Object>> result = blCgQueryMapper.queryBlCgIpDetails(map);
        //如果是产房，查询的时候增加婴儿的费用信息
        //if(PatiPvInfoUtil.isDeptCF(pk_dept_cur)){
        //	 List<Map<String,Object>> inflist =  blCgQueryMapper.queryInfantBlCgIpDetails(map);
        //	 if(inflist!=null&&inflist.size()>0) result.addAll(inflist);
        // }
        return result;
    }

    /**
     * 获取患者费用信息
     *
     * @param param
     * @param user
     * @return
     */
    public PatiCardVo getPatiFee(String param, IUser user) {
        //String pk_pv = JsonUtil.readValue(param, String.class);
        Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
        if (CommonUtils.isEmptyString(CommonUtils.getString(paramMap.get("pkPve"))))
            throw new BusException("未获取到患者就诊信息！");
        PatiCardVo pativo = new PatiCardVo();
        pativo.setPkPv(CommonUtils.getString(paramMap.get("pkPve")));
        if (paramMap.get("dateBegin") != null && paramMap.get("dateSt") != null) {
            pativo.setDateBegin(DateUtils.strToDate(CommonUtils.getString(paramMap.get("dateBegin"))));
            String dateSt = CommonUtils.getString(paramMap.get("dateSt")).substring(0, 8) + "235959";
            pativo.setDateSt(DateUtils.strToDate(dateSt));
        }

        return patiFeeHandler.setPatiFee(pativo);
    }

    /**
     * 查询患者费用汇总【实际收费】 - 按账单码显示
     * 患者就诊主键，开始日期，结束日期 不能为空
     *
     * @param map{pkPvs,dateBegin,dateEnd}
     * @return
     */
    public List<Map<String, Object>> queryCgIpSummer(String param, IUser user) {
        Map<String, Object> map = JsonUtil.readValue(param, Map.class);
        String endDate = CommonUtils.getString(map.get("dateEnd"));
        if (endDate != null && !endDate.equals("")) {
            endDate = endDate.substring(0, 8) + "235959";
            map.put("dateEnd", endDate);
        }
        //所有费用查询，以pkPv为标准（即，查询本次就诊的所有费用，不区分科室）
        List<String> pvlist = (List) map.get("pkPvs");
        if (pvlist == null || pvlist.size() <= 0)
            throw new BusException("未获取到患者就诊信息！");
        List<Map<String, Object>> result = blCgQueryMapper.queryCgIpSummerByDateHap(map);
        return result;
    }

    /**
     * 查询患者费用明细【实际收费】 - 按分类中的项目汇总查询
     * 患者就诊主键，开始日期，结束日期 不能为空
     *
     * @param map{pkPvs,dateBegin,dateEnd}
     * @return
     */
    public List<Map<String, Object>> queryCgIpDetails(String param, IUser user) {
        Map<String, Object> map = JsonUtil.readValue(param, Map.class);
        String endDate = CommonUtils.getString(map.get("dateEnd"));
        if (endDate != null && !endDate.equals("")) {
            endDate = endDate.substring(0, 8) + "235959";
            map.put("dateEnd", endDate);
        }
        List<String> pvlist = (List) map.get("pkPvs");
        if (pvlist == null || pvlist.size() <= 0)
            throw new BusException("未获取到患者就诊信息！");
        List<Map<String, Object>> result = blCgQueryMapper.queryCgIpDetailsByDateHap(map);
        return result;
    }

    /**
     * 查询患者费用汇总【实际执行】 - 按账单码显示
     * 患者就诊主键，开始日期，结束日期 不能为空
     *
     * @param map{pkPvs,dateBegin,dateEnd}
     * @return
     */
    public List<Map<String, Object>> queryExIpSummer(String param, IUser user) {
        Map<String, Object> map = JsonUtil.readValue(param, Map.class);
        String endDate = CommonUtils.getString(map.get("dateEnd"));
        if (endDate != null && !endDate.equals("")) {
            endDate = endDate.substring(0, 8) + "235959";
            map.put("dateEnd", endDate);
        }
        //所有费用查询，以pkPv为标准（即，查询本次就诊的所有费用，不区分科室）
        List<String> pvlist = (List) map.get("pkPvs");
        if (pvlist == null || pvlist.size() <= 0)
            throw new BusException("未获取到患者就诊信息！");
        List<Map<String, Object>> result = blCgQueryMapper.queryExIpSummerByDatePlan(map);
        return result;
    }

    /**
     * 查询患者费用明细【实际执行】 - 按分类中的项目汇总查询
     * 患者就诊主键，开始日期，结束日期 不能为空
     *
     * @param map{pkPvs,dateBegin,dateEnd}
     * @return
     */
    public List<Map<String, Object>> queryExIpDetails(String param, IUser user) {
        Map<String, Object> map = JsonUtil.readValue(param, Map.class);
        String endDate = CommonUtils.getString(map.get("dateEnd"));
        if (endDate != null && !endDate.equals("")) {
            endDate = endDate.substring(0, 8) + "235959";
            map.put("dateEnd", endDate);
        }
        List<String> pvlist = (List) map.get("pkPvs");
        if (pvlist == null || pvlist.size() <= 0)
            throw new BusException("未获取到患者就诊信息！");
        List<Map<String, Object>> result = blCgQueryMapper.queryExIpDetailsByDatePlan(map);
        return result;
    }
   /***
    * @Description 查询患者费用总额，预交金总额，余额
    * 005002004012
    * @auther wuqiang
    * @Date 2019-12-06 
    * @Param [param, user]
    * @return java.util.List<java.util.Map<java.lang.String,java.lang.Object>>
    */
    public List<Map<String, Object>> queryPvIpIncount(String param, IUser user) {
        List<String> pkPvs = JsonUtil.readValue(param, ArrayList.class);
        List<Map<String, Object>> toalBlAmount = blCgQueryMapper.queryToalBlAmount(pkPvs);
        List<Map<String, Object>> toalPreAmount = blCgQueryMapper.queryToalPreAmount(pkPvs);
        for (int i = 0; i < toalBlAmount.size(); i++) {
            String pkPv = String.valueOf(toalBlAmount.get(i).get("pkPv"));
            for (int j = 0; j < toalPreAmount.size(); j++) {
                if (pkPv.equals(String.valueOf(toalPreAmount.get(j).get("pkPv")))) {
                    toalBlAmount.get(i).put("prepayAmount", toalPreAmount.get(j).get("prepayAmount"));
                    BigDecimal totalAmount = BigDecimal.valueOf(Double.valueOf(String.valueOf(toalBlAmount.get(i).get("totalAmount"))));
                    BigDecimal prepayAmount =BigDecimal.valueOf(Double.valueOf(String.valueOf( toalPreAmount.get(j).get("prepayAmount"))));
                    toalBlAmount.get(i).put("amtYe", prepayAmount.subtract(totalAmount));
                }
            }
        }
        return toalBlAmount;
    }


}
