package com.zebone.nhis.pro.zsba.hisser.service;

import com.zebone.nhis.pro.zsba.hisser.dao.BaPubMapper;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.jdbc.DataSourceRoute;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Classname BaHandler
 * @Description 博爱项目nhis使用
 * @Date 2020-05-08 10:23
 * @Created by wuqiang
 */

@Service
public class BaHandler {

    @Autowired
    private BaPubMapper baPubMapper;

    /**
     * @return java.util.List<java.util.Map < java.lang.String, java.lang.Object>>
     * @Description 查询患者输血检查结果
     * @auther wuqiang
     * @Date 2020-05-13
     * @Param [param , user]
     * "codeIp": "住院号",
     * "ipTimes": “住院次数”
     */
    public List<Map<String, Object>> qryBloodLis(String param, IUser user) {
        Map<String, Object> params = JsonUtil.readValue(param, HashMap.class);
        DataSourceRoute.putAppId("LIS_bayy");
        try {
            List<Map<String, Object>> list = baPubMapper.qryBloodLis(params);
            if (list.size() > 0) {
                for (Map<String, Object> map : list) {
                    if (map.containsKey("patDate")) {
                        map.put("dateRpt", map.get("patDate"));
                    }
                    if (map.containsKey("resEcd")) {
                        map.put("codeIndex", map.get("resEcd"));
                    }
                    if (map.containsKey("resName")) {
                        map.put("nameIndex", map.get("resName"));
                    }
                    if (map.containsKey("resChr")) {
                        map.put("val", map.get("resChr"));
                    }
                    if (map.containsKey("itmUnit")) {
                        map.put("unit", map.get("itmUnit"));
                    }
                }
            }
            return list;
        } catch (Exception e) {
            throw new BusException("查询输血结果出错！！！");
        } finally {
            DataSourceRoute.putAppId("default");
        }


    }


}
