package com.zebone.nhis.pro.zsba.base.service;

import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ZsbaBdItemService {

    public List<Map<String,Object>> qryChargeItem(String param, IUser user){
        Map<String,Object> paramMap = JsonUtil.readValue(param,Map.class);
        StringBuilder sbl = new StringBuilder("select * from [192.168.0.9].his_lxdb_bayy.dbo.view_preparechargeitemselect where 1=1");
        if(StringUtils.isNotBlank(MapUtils.getString(paramMap,"code"))){
            sbl.append(" and (code like concat(concat('%',:code),'%')")
                .append(" or py_code like concat(concat('%',:code),'%')")
                .append(" or d_code like concat(concat('%',:code),'%')")
                .append(")");
        }
        if(StringUtils.isNotBlank(MapUtils.getString(paramMap,"name"))){
            sbl.append(" and name concat(concat('%',:name),'%')");
        }
        return DataBaseHelper.queryForList(sbl.toString(), paramMap);
    }

    public void modChargeItem(String param, IUser user){
        Map<String,Object> paramMap = JsonUtil.readValue(param,Map.class);
        String prepareChargeCode = MapUtils.getString(paramMap,"prepareChargeCode");
        String materialCode = MapUtils.getString(paramMap,"materialCode");
        String chargeCode = MapUtils.getString(paramMap,"chargeCode");
        if(StringUtils.isBlank(prepareChargeCode)){
            throw new BusException("缺少入参prepareChargeCode");
        }
        if(StringUtils.isBlank(materialCode)){
            throw new BusException("缺少入参materialCode");
        }
        if(StringUtils.isBlank(chargeCode)){
            throw new BusException("缺少入参chargeCode");
        }

        DataBaseHelper.executeProcedure("[192.168.0.9].his_lxdb_bayy.dbo.wz_matconvertcharge(?,?,?)"
                , prepareChargeCode,materialCode,chargeCode);
    }
}
