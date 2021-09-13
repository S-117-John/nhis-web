package com.zebone.nhis.pro.zsrm.cn.service;

import com.zebone.nhis.pro.zsrm.cn.dao.CnOpDiagMapper;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class CnOpDiagService {
    @Autowired
    private CnOpDiagMapper cndiag;

    /**
     * 查询诊断是否需要弹出入院指征提示
     * 022006004028
     * @param param
     * @param user
     * @return
     */
    public Map<String,Object> qryDiagAtt(String param, IUser user){
        Map<String,Object> paramMap = JsonUtil.readValue(param, Map.class);
        String codeIcd=MapUtils.getString(paramMap,"pkDiag");
        if(StringUtils.isEmpty(codeIcd)){
            throw new BusException("请传入患者诊断主键！");
        }
        List<Map<String,Object>> rtn1=cndiag.qryDaigByPk(paramMap);
        if(rtn1==null || rtn1.size()==0) return null;
        paramMap.put("codeIcd",MapUtils.getString(rtn1.get(0),"code"));
        List<Map<String,Object>> rtn=cndiag.qryDaigAtt(paramMap);
        return rtn==null || rtn.size()==0 ? null:rtn.get(0);
    }

    /**
     * 查询患者诊断
     * @param param
     * @param user
     * @return
     */
    public Map<String,Object> qryDiag(String param, IUser user){
        Map<String,Object> paramMap = JsonUtil.readValue(param, Map.class);
        String codeIcd=MapUtils.getString(paramMap,"pkDiag");
        if(StringUtils.isEmpty(codeIcd)){
            throw new BusException("请传入患者诊断主键！");
        }
        List<Map<String,Object>> rtn=cndiag.qryDaigByPk(paramMap);
        return rtn==null || rtn.size()==0 ? null:rtn.get(0);
    }
}
