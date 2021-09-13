package com.zebone.nhis.pro.zsba.compay.up.service;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.pro.zsba.compay.up.dao.ZsBlCgIpQueryMapper;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;

/**
 * 患者费用查询
 * @author lipz
 *
 */
@Service
public class ZsBlCgIpQueryServcie {
	
	@Resource
    private ZsBlCgIpQueryMapper blCgQueryMapper;
	
	/**
     * 查询患者费用汇总【实际收费】 - 按账单码显示
     * 患者就诊主键，开始日期，结束日期 不能为空
     *
     * @param map{pkPvs,dateBegin,dateEnd}
     * @return
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public List<Map<String, Object>> queryCgIpSummer(String param, IUser user) {
		Map<String, Object> map = JsonUtil.readValue(param, Map.class);
        String endDate = CommonUtils.getString(map.get("dateEnd"));
        if (endDate != null && !"".equals(endDate)) {
            endDate = endDate.substring(0, 8) + "235959";
            map.put("dateEnd", endDate);
        }
        //所有费用查询，以pkPv为标准（即，查询本次就诊的所有费用，不区分科室）
        List<String> pvlist = (List) map.get("pkPvs");
        if (pvlist == null || pvlist.size() <= 0){
        	throw new BusException("未获取到患者就诊信息！");
        }
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
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public List<Map<String, Object>> queryCgIpDetails(String param, IUser user) {
        Map<String, Object> map = JsonUtil.readValue(param, Map.class);
        String endDate = CommonUtils.getString(map.get("dateEnd"));
        if (endDate != null && !"".equals(endDate)) {
            endDate = endDate.substring(0, 8) + "235959";
            map.put("dateEnd", endDate);
        }
        List<String> pvlist = (List) map.get("pkPvs");
        if (pvlist == null || pvlist.size() <= 0){
        	throw new BusException("未获取到患者就诊信息！");
        }
        List<Map<String, Object>> result = blCgQueryMapper.queryCgIpDetailsByDateHap(map);
        return result;
    }

}
