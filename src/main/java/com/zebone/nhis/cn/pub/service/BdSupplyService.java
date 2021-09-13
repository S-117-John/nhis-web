package com.zebone.nhis.cn.pub.service;


import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.utils.JsonUtil;
import org.apache.commons.collections.MapUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class BdSupplyService {

    /**
     * 用法费用
     * 004001005045
     * @param param
     * @param user
     */
    public List<Object> usageFee(String param, IUser user){
        List<Object> feeResult = new ArrayList<>();
        Map<String,Object> paramObject = JsonUtil.readValue(param,Map.class);
        String termCode = MapUtils.getString(paramObject,"termCode");
        Integer feeCount = MapUtils.getInteger(paramObject,"feeCount");
        String supplyCode = MapUtils.getString(paramObject,"supplyCode");
        Integer day = MapUtils.getInteger(paramObject,"day");
        if(StringUtils.isEmpty(termCode)){
            return feeResult;
        }
        if(feeCount<=0){
            return feeResult;
        }

        if(day<=0){
            return feeResult;
        }


        String sql = "select item.pk_item, item.quan, item.eu_mode from bd_supply_item item inner join bd_supply sup on item.pk_supply=sup.pk_supply where sup.code=?";
        List<Map<String,Object>> result = DataBaseHelper.queryForList(sql,new Object[]{supplyCode});
        if(result.size()<=0){
            return feeResult;
        }
        Map<String,Object> supply = result.get(0);
        String euMode = MapUtils.getString(supply,"euMode");

        sql = "select * from BD_TERM_FREQ where CODE = ?";
        Map<String,Object> termResult = DataBaseHelper.queryForMap(sql,new Object[]{termCode});
        Integer cnt = MapUtils.getInteger(termResult,"cnt");
        if(StringUtils.isEmpty(euMode)){

            return feeResult;
        }
        Integer resultCount= 0;
        switch (euMode){
            case "1":
                feeResult.add("first");
                break;
            case "2":
                feeResult.add("second");
                break;
            case "3":
                feeResult.add("child");
                break;
            default:
                break;
        }
        resultCount = cnt*day*feeCount;
        feeResult.add(resultCount);
        return feeResult;
    }
}
