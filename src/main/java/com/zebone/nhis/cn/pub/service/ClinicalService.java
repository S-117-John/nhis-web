package com.zebone.nhis.cn.pub.service;

import com.zebone.nhis.cn.pub.dao.ClinicalMapper;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ClinicalService {
    @Autowired
    private ClinicalMapper cliMapper;

    /**
     * 查询检验列表  004001005030
     * @param param
     * @param user
     * @return
     */
    public List<Map<String,Object>> qryLab(String param, IUser user){
        Map<String,Object> paraMap= JsonUtil.readValue(param, Map.class);
        if(paraMap.get("pkPv")==null){
            throw new BusException("未获取到患者信息！");
        }
        return cliMapper.qryLab(paraMap);
    }

    /**
     * 查询患者检验报告信息  004001005031
     * @param param
     * @param user
     * @return
     */
    public List<Map<String,Object>> qryLabReport(String param, IUser user){
        Map<String,Object> paraMap= JsonUtil.readValue(param, Map.class);
        if(paraMap.get("codeApply")==null){
            throw new BusException("未获取到申请单号信息！");
        }
        return cliMapper.qryLabReport(paraMap);
    }

    /**
     * 查询检查列表  004001005032
     * @param param
     * @param user
     * @return
     */
    public List<Map<String,Object>> qryRis(String param, IUser user){
        Map<String,Object> paraMap= JsonUtil.readValue(param, Map.class);
        if(paraMap.get("pkPv")==null){
            throw new BusException("未获取到患者信息！");
        }
        return cliMapper.qryRis(paraMap);
    }

}

