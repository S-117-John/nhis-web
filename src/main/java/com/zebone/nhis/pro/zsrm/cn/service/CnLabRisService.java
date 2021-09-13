package com.zebone.nhis.pro.zsrm.cn.service;

import com.zebone.nhis.pro.zsrm.cn.dao.CnLabRisMapper;
import com.zebone.platform.Application;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.utils.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class CnLabRisService {
    @Autowired
    public CnLabRisMapper reqDao;

    //查询全院项目树
    public List<Map<String,Object>> queryRisLabTreeList(String param , IUser user){
        Map<String,Object> paramMap= JsonUtil.readValue(param, Map.class);
        paramMap.put("dataBaseType", Application.isSqlServer()?"sqlserver":"oracle");
        List<Map<String,Object>> list = reqDao.queryRisLabTreeList(paramMap);
        return list ;
    }

    //查询医生常用检查检验治疗项目
    public List<Map<String,Object>> qryPreferOrders(String param, IUser user){
        Map<String,Object> paramMap = JsonUtil.readValue(param, Map.class);
        List<Map<String,Object>> commonOrdlist = reqDao.qryPreferOrders(paramMap);
        return commonOrdlist;
    }
}
